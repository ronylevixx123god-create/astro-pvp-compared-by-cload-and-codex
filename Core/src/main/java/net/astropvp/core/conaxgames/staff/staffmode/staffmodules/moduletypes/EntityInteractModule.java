package net.astropvp.core.conaxgames.staff.staffmode.staffmodules.moduletypes;

/*
public abstract class EntityInteractModule extends StaffModule implements Listener {


    @Getter
    private final StaffModeManager staffModeManager;

    public EntityInteractModule(String name, Material material, int slot, String... description) {
        super(name, material, slot, description);
        this.staffModeManager = AstroCore.getInstance().getStaffModeManager();
    }

    protected abstract void onPlayerInteract(PlayerInteractAtEntityEvent event, Player clickedPlayer);

    @EventHandler
    public void onStaffItemClick(PlayerInteractAtEntityEvent event) {
        if (!this.staffModeManager.isStaffModeToggled(event.getPlayer())) return;
        if (event.getRightClicked() == null) return;
        if (event.getPlayer().getItemInHand() == null) return;
        if (event.getPlayer().getItemInHand().getType() != super.getMaterial()) return;
        if (!(event.getRightClicked() instanceof Player)) return;

        this.onPlayerInteract(event, (Player) event.getRightClicked());
        event.setCancelled(true);
    }

}


 */