#!/usr/bin/env python3
"""Recover unavailable legacy artifacts from repository jars; record SHA-256 provenance.

The full original jar is registered only for legacy server and plugin coordinates.
For third-party libraries embedded in a fat jar, only their package prefixes are extracted.
This does not change tracked dependencies or substitute source plugin implementations.
"""
from pathlib import Path
import zipfile,hashlib,json,shutil,xml.etree.ElementTree as E
import os
r=Path(__file__).resolve().parents[1]
base=Path(os.environ.get('ASTRO_BUILD_CACHE','/workspace/.onboarding'))
cache=base/'m2'; out=base/'recovered-libraries';out.mkdir(parents=True,exist_ok=True)
manifest=[]
def seed(g,a,v,source,prefixes=None):
 source=r/source
 artifact=source
 if prefixes:
  artifact=out/(a+'-'+v+'.jar')
  with zipfile.ZipFile(source) as z,zipfile.ZipFile(artifact,'w',zipfile.ZIP_DEFLATED) as target:
   for n in z.namelist():
    if any(n.startswith(p) for p in prefixes) and not n.endswith('/'):target.writestr(n,z.read(n))
 dest=cache/g.replace('.','/')/a/v;dest.mkdir(parents=True,exist_ok=True)
 shutil.copyfile(artifact,dest/(a+'-'+v+'.jar'))
 (dest/(a+'-'+v+'.pom')).write_text('<project><modelVersion>4.0.0</modelVersion><groupId>'+g+'</groupId><artifactId>'+a+'</artifactId><version>'+v+'</version></project>')
 manifest.append(dict(coordinate=f'{g}:{a}:{v}',source=str(source.relative_to(r)),source_sha256=hashlib.sha256(source.read_bytes()).hexdigest(),prefixes=prefixes))
for g,a,v in [('org.spigotmc','spigot','1.8.8-R0.1-SNAPSHOT'),('org.spigotmc','spigot-api','1.8.8-R0.1-SNAPSHOT'),('com.conaxgames','server','1.8.8-R0.2-SNAPSHOT')]:seed(g,a,v,'AstroCollect/lib/banana.jar')
seed('com.sk89q','worldguard','6.1','Utils/lib/worldguard-6.1.jar')
for g,a,v,p in [('com.github.MilkBowl','VaultAPI','1.7',['net/milkbowl/']),('xyz.xenondevs','particle','1.7',['xyz/xenondevs/']),('me.mattstudios.utils','matt-framework','1.4.6',['me/mattstudios/mf/']),('de.tr7zw','item-nbt-api-plugin','2.9.2',['de/tr7zw/','javassist/']),('net.ess3','EssentialsX','2.17.2',['com/earth2me/','net/ess3/','org/essentials/','io/papermc/lib/'])]:seed(g,a,v,'.lib/CosmicPvPUtils.jar',p)
seed('com.massivecraft','Factions','1.6.9.5-U0.5.18','.lib/Factions(1).jar')
for a,f in [('Arkkit','.lib/Arkkit.jar'),('Sets','Ark/lib/CosmicArmorSets.jar'),('CompilexPermissions','Ark/lib/CompilexPermissions_2_1_1_1.jar'),('Enchantments','.lib/Enchantments.jar'),('Lootboxes','Core/lib/Lootboxes-0.1.jar'),('SpaceChests','Core/lib/CosmicSpaceChests.jar'),('Duels','Core/lib/CosmicDuel.jar'),('Conquest','Core/lib/Conquest-0.1.jar'),('AuctionHouse','Core/lib/Auction.jar')]:
 if (r/f).exists():seed('com.conaxgames',a,'0.1',f)
seed('co.aikar','acf-paper','0.5.0-SNAPSHOT','Core/lib/AstroDiscordSync-0.1.jar',['co/aikar/','net/jodah/expiringmap/'])
(out/'manifest.json').write_text(json.dumps(manifest,indent=2))
seed('de.tr7zw','item-nbt-api-plugin','2.7.1','CustomBlocks/lib/item-nbt-api-plugin-2.7.1.jar')
seed('de.tr7zw','item-nbt-api-plugin','2.6.0','AstroScoreboard/lib/AstroEvents-1.0.jar',['de/tr7zw/'])
seed('net.dv8tion','JDA','4.2.0_204','Core/lib/AstroDiscordSync-0.1.jar',['net/dv8tion/','okhttp3/','okio/','gnu/trove/','org/apache/commons/collections4/','com/neovisionaries/','org/json/','com/fasterxml/'])
(out/'manifest.json').write_text(json.dumps(manifest,indent=2))
import urllib.parse
tree=E.ElementTree(E.Element('settings'))
proxyurl=os.environ.get('HTTPS_PROXY') or os.environ.get('https_proxy')
if proxyurl:
 u=urllib.parse.urlparse(proxyurl)
 if u.username or u.password:raise RuntimeError('Authenticated proxy needs supported Maven authentication configuration')
 proxy=E.SubElement(E.SubElement(tree.getroot(),'proxies'),'proxy')
 for k,v in [('id','cloud'),('active','true'),('protocol','http'),('host',u.hostname),('port',str(u.port or 80))]:E.SubElement(proxy,k).text=v
mirrors=E.SubElement(tree.getroot(),'mirrors');mirror=E.SubElement(mirrors,'mirror')
for k,v in [('id','central-only'),('mirrorOf','*'),('url','https://repo.maven.apache.org/maven2')]:E.SubElement(mirror,k).text=v
tree.write(base/'central-settings.xml',encoding='unicode')
print('Seeded',len(manifest),'bundled legacy artifacts; provenance recorded')

# The archived ACF bytecode references shaded.com.google, but no supplied jar
# contains that namespace. Restore its references to the server's canonical Guava;
# do not modify the archived source jar or substitute any application classes.
import subprocess
work=base/'acf-compat';work.mkdir(exist_ok=True)
(work/'pom.xml').write_text('''<project xmlns="http://maven.apache.org/POM/4.0.0">
<modelVersion>4.0.0</modelVersion><groupId>recovered.astro</groupId><artifactId>acf-compat</artifactId><version>1</version>
<dependencies><dependency><groupId>co.aikar</groupId><artifactId>acf-paper</artifactId><version>0.5.0-SNAPSHOT</version></dependency></dependencies>
<build><plugins><plugin><artifactId>maven-shade-plugin</artifactId><version>3.2.4</version>
<executions><execution><phase>package</phase><goals><goal>shade</goal></goals></execution></executions>
<configuration><createDependencyReducedPom>false</createDependencyReducedPom><relocations><relocation><pattern>shaded.com.google</pattern><shadedPattern>com.google</shadedPattern></relocation></relocations></configuration>
</plugin></plugins></build></project>''')
mvn=os.environ.get('MAVEN') or shutil.which('mvn') or str(base/'tools/apache-maven-3.9.11/bin/mvn')
with (work/'build.log').open('w') as log:
 subprocess.run([mvn,'-B','-ntp','-s',str(base/'central-settings.xml'),'-Dmaven.repo.local='+str(cache),'-f',str(work/'pom.xml'),'package'],stdout=log,stderr=subprocess.STDOUT,check=True)
patched=work/'target/acf-compat-1.jar'
shutil.copyfile(patched,cache/'co/aikar/acf-paper/0.5.0-SNAPSHOT/acf-paper-0.5.0-SNAPSHOT.jar')
manifest.append(dict(coordinate='co.aikar:acf-paper:0.5.0-SNAPSHOT',compatibility_change='Restore missing shaded.com.google references to canonical com.google',patched_sha256=hashlib.sha256(patched.read_bytes()).hexdigest()))
(out/'manifest.json').write_text(json.dumps(manifest,indent=2))
print('Recovered ACF namespace linkage; original repository jars remain unchanged')
