package me.cosmicpvp.CosmicGlobalKits.permissions;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.context.DefaultContextKeys;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Grants global-kit permission nodes through the LuckPerms API.
 *
 * Grants are scoped with a {@code server} context. The scope comes from the
 * {@code luckperms-server-context} config value, or, when that is blank, from LuckPerms' own
 * configured server name so the node matches the context LuckPerms actually applies. If that
 * would be LuckPerms' "global"/empty name, the grant is refused rather than silently made global;
 * a global grant only happens when the config explicitly says {@code global}.
 *
 * LuckPerms loads, modifies and saves the user, so offline players are supported by UUID. The
 * callback always runs on the main thread, after the save has completed or failed.
 */
public final class KitPermissionGranter {
    public static final String CONFIG_KEY = "luckperms-server-context";
    static final String GLOBAL = "global";

    private final Supplier<LuckPerms> luckPerms;
    private final Executor mainThread;
    private final Logger logger;

    public KitPermissionGranter(Supplier<LuckPerms> luckPerms, Executor mainThread, Logger logger) {
        this.luckPerms = luckPerms;
        this.mainThread = mainThread;
        this.logger = logger;
    }

    public static KitPermissionGranter bukkit(Plugin plugin) {
        return new KitPermissionGranter(() -> LuckPermsProvider.get(), task -> Bukkit.getScheduler().runTask(plugin, task), plugin.getLogger());
    }

    public void grant(UUID uuid, String permission, String configuredServerContext, Consumer<GrantResult> onMainThread) {
        final LuckPerms api;
        final ServerScope scope;
        try {
            api = this.luckPerms.get();
            scope = ServerScope.resolve(configuredServerContext, api.getServerName());
        } catch (IllegalStateException | LinkageError e) {
            this.logger.severe("[GlobalKits] Cannot grant " + permission + " to " + uuid + ": " + e.getMessage());
            this.mainThread.execute(() -> onMainThread.accept(GrantResult.FAILED));
            return;
        }

        PermissionNode.Builder builder = api.getNodeBuilderRegistry().forPermission().permission(permission).value(true);
        if (!scope.isGlobal()) {
            builder.withContext(DefaultContextKeys.SERVER_KEY, scope.getServer());
        }
        final Node node = builder.build();

        final AtomicReference<DataMutateResult> mutateResult = new AtomicReference<>();
        CompletableFuture<Void> saved;
        try {
            saved = api.getUserManager().modifyUser(uuid, user -> mutateResult.set(user.data().add(node)));
        } catch (RuntimeException e) {
            saved = new CompletableFuture<>();
            saved.completeExceptionally(e);
        }

        saved.whenComplete((ignored, error) -> {
            GrantResult result = toResult(error, mutateResult.get());
            if (result == GrantResult.FAILED) {
                this.logger.severe("[GlobalKits] Failed to grant " + permission + " (" + scope + ") to " + uuid
                        + ": " + (error != null ? error : mutateResult.get()));
            }
            this.mainThread.execute(() -> onMainThread.accept(result));
        });
    }

    static GrantResult toResult(Throwable error, DataMutateResult mutateResult) {
        if (error != null) {
            return GrantResult.FAILED;
        }
        if (mutateResult == DataMutateResult.SUCCESS) {
            return GrantResult.GRANTED;
        }
        if (mutateResult == DataMutateResult.FAIL_ALREADY_HAS) {
            return GrantResult.ALREADY_GRANTED;
        }
        return GrantResult.FAILED;
    }

    /** The server scope a grant is made in: a named server, or (only when explicitly configured) global. */
    static final class ServerScope {
        private final String server;

        private ServerScope(String server) {
            this.server = server;
        }

        static ServerScope resolve(String configured, String luckPermsServerName) {
            String explicit = configured == null ? "" : configured.trim();
            if (!explicit.isEmpty()) {
                return new ServerScope(explicit.equalsIgnoreCase(GLOBAL) ? null : explicit);
            }
            String fromLuckPerms = luckPermsServerName == null ? "" : luckPermsServerName.trim();
            if (fromLuckPerms.isEmpty() || fromLuckPerms.equalsIgnoreCase(GLOBAL)) {
                throw new IllegalStateException("no kit permission scope configured: LuckPerms' server name is '"
                        + fromLuckPerms + "'. Set 'server:' in LuckPerms' config.yml to this server's name, or set '"
                        + CONFIG_KEY + "' in CosmicGlobalKits' config.yml (use 'global' only to deliberately grant on every server).");
            }
            return new ServerScope(fromLuckPerms);
        }

        boolean isGlobal() {
            return this.server == null;
        }

        String getServer() {
            return this.server;
        }

        @Override
        public String toString() {
            return this.server == null ? "global" : "server=" + this.server;
        }
    }
}
