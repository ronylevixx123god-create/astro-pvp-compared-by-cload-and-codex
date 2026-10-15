package net.astropvp.core.conaxgames.staff.staffmode.staffmodules.moduletypes;

/*
public abstract class InteractModule extends StaffModule implements Listener {


    @Getter
    private final StaffModeManager staffModeManager;

    public InteractModule(String name, Material material, int slot, String... description) {
        super(name, material, slot, description);
        this.staffModeManager = AstroCore.getInstance().getStaffModeManager();
    }

    protected abstract void onPlayerInteract(PlayerInteractEvent event);

    @EventHandler
    public void onStaffItemClick(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!this.staffModeManager.isStaffModeToggled(event.getPlayer())) return;
        if (event.getItem() == null) return;
        if (this.getMaterial() != event.getItem().getType()) return;
        this.onPlayerInteract(event);
        event.setCancelled(true);
    }

}


 */