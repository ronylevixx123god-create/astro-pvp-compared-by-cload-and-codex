package net.astropvp.vouchers.commands;

import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.base.CommandBase;
import net.astropvp.vouchers.VouchersPlugin;

@Command("voucher")
public class VoucherCommand extends CommandBase {

    private final VouchersPlugin plugin;
    public VoucherCommand(VouchersPlugin plugin) {
        this.plugin = plugin;
    }
}
