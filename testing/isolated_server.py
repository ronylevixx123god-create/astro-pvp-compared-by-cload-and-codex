#!/usr/bin/env python3
"""Stage rebuilt plugins and run bounded, loopback-only integration checks.

Never points at an existing/live server. Each invocation creates a new run directory.
An accepted EULA is required explicitly. Database credentials stay outside the checkout.
"""
import argparse,hashlib,json,os,re,shutil,subprocess,time,uuid,zipfile,urllib.parse
from pathlib import Path
import yaml
from probe_results import CONTRACTS, probe_passed as check_probe, run_succeeded
ROOT=Path(__file__).resolve().parents[1]
parser=argparse.ArgumentParser()
parser.add_argument('--accept-eula',action='store_true')
parser.add_argument('--state',type=Path,default=Path('/workspace/.integration-server'))
parser.add_argument('--server',default='AstroCollect/lib/banana.jar')
parser.add_argument('--probe',default='')
parser.add_argument('--seconds',type=int,default=30)
parser.add_argument('--omit-plugin',action='append',default=[],help='Explicitly omit a plugin for an isolated absence test; recorded in results')
parser.add_argument('--db-config',type=Path)
parser.add_argument('--lp-profile',default='')
parser.add_argument('--phase',choices=['grant','verify'],default='grant')
args=parser.parse_args()
if args.probe and args.probe not in CONTRACTS:parser.error('Probe has no registered assertion contract; inspect imported diagnostics manually')
if not args.accept_eula:parser.error('Accept the Minecraft EULA before using --accept-eula')
state=args.state.resolve();run=state/'runs'/(time.strftime('%Y%m%dT%H%M%SZ',time.gmtime())+'-'+uuid.uuid4().hex[:8]);run.mkdir(parents=True)
plugins=run/'plugins';plugins.mkdir()
java_home=Path(os.environ['JAVA_HOME']);java=str(java_home/'bin/java');javac=str(java_home/'bin/javac')
staged={};manifest=[]
def stage(p,label):
 p=p.resolve()
 with zipfile.ZipFile(p) as z:
  if z.testzip():raise RuntimeError('Invalid jar: '+str(p))
  if 'plugin.yml' not in z.namelist():return
  desc=yaml.safe_load(z.read('plugin.yml'));name=desc['name']
 if name in args.omit_plugin:return
 if name in staged:raise RuntimeError('Duplicate plugin '+name)
 target=plugins/(name+'.jar');shutil.copyfile(p,target);staged[name]=target
 manifest.append(dict(name=name,source=label,sha256=hashlib.sha256(p.read_bytes()).hexdigest(),depend=desc.get('depend',[])))
for module in sorted(ROOT.iterdir()):
 if not module.is_dir() or module.name in ['WildPvP','FactionLogs','Trials','PlayerVaultsX','AstroFactions']:continue
 jars=[p for p in (module/'target').glob('*.jar') if not p.name.startswith('original-') and not p.name.endswith(('-sources.jar','-javadoc.jar'))]
 if module.name=='StorageUtil':jars=[p for p in jars if p.name=='StorageUtil-0.1-shaded.jar']
 if module.name=='AstroWildPvP':jars=[p for p in jars if p.name=='AstroWildPvP.jar']
 for p in jars:stage(p,str(p.relative_to(ROOT)))
external=['.lib/Factions(1).jar','Enchantments/lib/worldedit-bukkit-6.1.7.3.jar','Utils/lib/worldguard-6.1.jar','HotFix/lib/ProtocolLib.jar','EnchantmentsAPI/lib/EssentialsX.jar','Core/lib/LuckPerms-Bukkit-5.4.30.jar','.lib/FactionsKore-1.3.62.1.jar','Ruins/lib/Commons.jar','CCVerify/libs/BuycraftX.jar','Ruins/lib/AstroKillCounters-1.0 (6).jar','AstroCollect/lib/HolographicDisplays (2).jar','AstroCustomBlocks/lib/item-nbt-api-plugin-2.7.1.jar','AstroScoreboard/lib/TAB.v3.1.2.jar','Dungeon/lib/Kotlin-1.3.72.jar','Ark/lib/LibsDisguises.jar','Ark/lib/CoreProtect.jar','Ark/lib/mcMMO.jar','Ark/lib/ParticleAPI_v2.1.1.jar']
for p in external:
 if (ROOT/p).exists():stage(ROOT/p,p+' (bundled runtime dependency)')
if (state/'Vault.jar').exists():stage(state/'Vault.jar','official Vault 1.7.3 release')
shutil.copyfile(ROOT/args.server,run/'server.jar')
(run/'server.properties').write_text('server-ip=127.0.0.1\nserver-port=25575\nonline-mode=false\nlevel-name=world\nlevel-type=FLAT\ngenerate-structures=false\nspawn-monsters=false\nspawn-animals=false\nspawn-npcs=false\nallow-nether=false\nview-distance=3\nmax-players=10\nmax-tick-time=60000\nspawn-protection=0\nenable-query=false\nenable-rcon=false\n')
(run/'eula.txt').write_text('eula=true\n')
def write_config(name,filename,data):
 folder=plugins/name;folder.mkdir(exist_ok=True)
 p=folder/filename;p.write_text(yaml.safe_dump(data,sort_keys=False));p.chmod(0o600)
def default_config(name):
 with zipfile.ZipFile(staged[name]) as z:return yaml.safe_load(z.read('config.yml')) or {}
if args.lp_profile:
 if not re.fullmatch(r'[A-Za-z0-9_-]+',args.lp_profile):parser.error('Invalid test profile name')
 profile=state/'lp-profiles'/args.lp_profile;profile.mkdir(parents=True,exist_ok=True)
 (plugins/'LuckPerms').symlink_to(profile,target_is_directory=True)
if args.probe=='KitsLPProbe':
 folder=plugins/'KitsLPProbe';folder.mkdir();(folder/'phase.txt').write_text(args.phase)
write_config('PluginMetrics','config.yml',{'opt-out':True});write_config('bStats','config.yml',{'enabled':False})
write_config('LuckPerms','config.yml',{'server':'astro-integration','storage-method':'h2','watch-files':False})
cache=state/'luckperms-libs';cache.mkdir(exist_ok=True)
if not (plugins/'LuckPerms/libs').exists():(plugins/'LuckPerms/libs').symlink_to(cache,target_is_directory=True)
if args.db_config:
 db=json.loads(args.db_config.read_text())
 write_config('AstroCore','sqlconfig.yml',{'hostname':db['host'],'port':str(db['port']),'user':db['user'],'password':db['password'],'database':db['database']})
 write_config('CosmicGlobalKits','sqlconfig.yml',{'hostname':db['host'],'port':db['port'],'user':db['user'],'password':db['password'],'database':db['database']})
 config=default_config('Auction');config['sql']={'host':db['host'],'port':str(db['port']),'database':db['database'],'user':db['user'],'pass':db['password']};write_config('Auction','config.yml',config)
 if 'CosmicContests' in staged:
  write_config('CosmicContests','sqlcredentials.yml',{'sql':{'hostname':db['host'],'port':db['port'],'username':db['user'],'password':db['password'],'database':db['database']}})
for probe_name in ['IntegrationWorlds'] + ([args.probe] if args.probe else []):
 probe=ROOT/'testing/probes'/probe_name;out=run/('probe-classes-'+probe_name);out.mkdir()
 classpath=os.pathsep.join([str(run/'server.jar')]+[str(p) for p in plugins.glob('*.jar')])
 subprocess.run([javac,'-encoding','UTF-8','-source','8','-target','8','-proc:none','-sourcepath',str(out),'-cp',classpath,'-d',str(out)]+[str(p) for p in (probe/'src').rglob('*.java')],check=True)
 subprocess.run([str(java_home/'bin/jar'),'cf',str(plugins/(probe_name+'.jar')),'-C',str(probe),'plugin.yml','-C',str(out),'.'],check=True)
(run/'staged.json').write_text(json.dumps(manifest,indent=2))
log=run/'server.log';started=time.monotonic();ready=False;forced=False
with log.open('w') as output:
 proxy=urllib.parse.urlparse(os.environ.get('HTTPS_PROXY') or os.environ.get('https_proxy') or '')
 options=['-Djavax.net.ssl.trustStore=/etc/ssl/certs/java/cacerts']
 if proxy.hostname:
  if proxy.username or proxy.password:raise RuntimeError('Authenticated proxy requires supported runtime configuration')
  options += ['-Dhttps.proxyHost='+proxy.hostname,'-Dhttps.proxyPort='+str(proxy.port or 80),'-Dhttp.proxyHost='+proxy.hostname,'-Dhttp.proxyPort='+str(proxy.port or 80),'-Dhttp.nonProxyHosts=localhost|127.*']
 process=subprocess.Popen([java,'-Xmx2G','-Dfile.encoding=UTF-8']+options+['-jar','server.jar','nogui'],cwd=run,stdin=subprocess.PIPE,stdout=output,stderr=subprocess.STDOUT,text=True)
 try:
  while process.poll() is None and time.monotonic()-started<120:
   text=log.read_text(errors='replace')
   if 'Done (' in text:
    ready=True;break
   time.sleep(.5)
  if ready:
   if args.probe:
    try:process.wait(timeout=90)
    except subprocess.TimeoutExpired:forced=True
   else:
    process.stdin.write('version\nplugins\n');process.stdin.flush();time.sleep(args.seconds)
  if process.poll() is None:
   process.stdin.write('stop\n');process.stdin.flush()
   try:process.wait(timeout=30)
   except subprocess.TimeoutExpired:forced=True;process.terminate();process.wait(timeout=15)
 finally:
  if process.poll() is None:process.kill();process.wait()
text=log.read_text(errors='replace')
probe_lines=[line.split('PROBE ',1)[1] for line in text.splitlines() if 'PROBE ' in line]
errors=[line for line in text.splitlines() if any(x in line for x in ['ERROR]','SEVERE]','Error occurred while','Could not load','UnknownDependency','NoClassDefFoundError','NoSuchMethodError','NoSuchFieldError'])]
probe_passed=check_probe(args.probe,args.phase,probe_lines) if args.probe else None
summary={'check_scope':'probe' if args.probe else 'startup','probe_passed':probe_passed if args.probe else None,'startup_has_errors':bool(errors),'run':run.name,'server':args.server,'ready':ready,'exit_code':process.returncode,'forced_stop':forced,'staged_plugins':len(staged),'omitted_plugins':args.omit_plugin,'probe':args.probe,'probe_lines':probe_lines,'errors':errors,'elapsed_seconds':round(time.monotonic()-started,1)}
(run/'summary.json').write_text(json.dumps(summary,indent=2));print(json.dumps({'run':str(run),'ready':ready,'exit_code':process.returncode,'staged_plugins':len(staged),'probe':probe_lines,'error_count':len(errors)},indent=2))
if not run_succeeded(ready,forced,process.returncode,args.probe,probe_passed,errors):raise SystemExit(1)
