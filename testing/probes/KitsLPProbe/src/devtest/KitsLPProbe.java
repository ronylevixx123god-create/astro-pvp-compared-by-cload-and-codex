package devtest;

import me.cosmicpvp.CosmicGlobalKits.permissions.GrantResult;
import me.cosmicpvp.CosmicGlobalKits.permissions.KitPermissionGranter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

/**
 * Isolated-test-server probe: drives the real Kits KitPermissionGranter against the real
 * LuckPerms plugin. Phase is read from plugins/KitsLPProbe/phase.txt; results are logged
 * as "PROBE ..." lines, then the server is stopped.
 */
public class KitsLPProbe extends JavaPlugin {
    static final UUID OFFLINE = UUID.fromString("11111111-2222-3333-4444-555555555555");
    static final UUID BROKEN = UUID.fromString("66666666-7777-8888-9999-000000000000");
    static final String PERM = "cosmickits.kit.hyper";

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this, this::run, 20L);
    }

    private void log(String s) {
        getLogger().info("PROBE " + s);
    }

    private void run() {
        String phase;
        try {
            phase = new String(Files.readAllBytes(new File(getDataFolder(), "phase.txt").toPath()), StandardCharsets.UTF_8).trim();
        } catch (Exception e) {
            log("no phase.txt: " + e);
            Bukkit.shutdown();
            return;
        }
        LuckPerms lp = LuckPermsProvider.get();
        log("phase=" + phase + " luckperms server=" + lp.getServerName());
        KitPermissionGranter granter = KitPermissionGranter.bukkit(this);
        switch (phase) {
            case "grant":
                granter.grant(OFFLINE, PERM, "", r1 -> {
                    log("grant1=" + r1 + " mainThread=" + Bukkit.isPrimaryThread());
                    granter.grant(OFFLINE, PERM, "", r2 -> {
                        log("grant2=" + r2 + " mainThread=" + Bukkit.isPrimaryThread());
                        Bukkit.shutdown();
                    });
                });
                break;
            case "verify":
                lp.getUserManager().loadUser(OFFLINE).thenAccept(user -> Bukkit.getScheduler().runTask(this, () -> {
                    for (Node n : user.getNodes()) {
                        if (n.getKey().equals(PERM)) {
                            log("stored node=" + n.getKey() + " value=" + n.getValue() + " contexts=" + n.getContexts());
                        }
                    }
                    log("access server=" + lp.getServerName() + " -> " + check(user, lp.getServerName()));
                    log("access server=othersrv -> " + check(user, "othersrv"));
                    Bukkit.shutdown();
                }));
                break;
            case "fail":
                granter.grant(BROKEN, PERM, "", r -> {
                    log("failGrant=" + r + " mainThread=" + Bukkit.isPrimaryThread());
                    Bukkit.shutdown();
                });
                break;
            case "global":
                granter.grant(OFFLINE, "cosmickits.kit.destroyer", "", r -> {
                    log("globalGrant=" + r + " mainThread=" + Bukkit.isPrimaryThread());
                    Bukkit.shutdown();
                });
                break;
            default:
                log("unknown phase");
                Bukkit.shutdown();
        }
    }

    private static boolean check(User user, String server) {
        QueryOptions q = QueryOptions.contextual(ImmutableContextSet.of("server", server));
        return user.getCachedData().getPermissionData(q).checkPermission(PERM).asBoolean();
    }
}
