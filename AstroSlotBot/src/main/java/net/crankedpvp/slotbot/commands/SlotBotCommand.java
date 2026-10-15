package net.crankedpvp.slotbot.commands;

import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import net.crankedpvp.slotbot.SlotBotPlugin;
import net.crankedpvp.slotbot.inventory.SlotBotInventory;
import net.crankedpvp.slotbot.struct.SlotBotType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("slot")
@Alias({"slotbot", "bot", "roll", "rollbot"})
public class SlotBotCommand extends CommandBase {

    private final SlotBotPlugin plugin;
    public SlotBotCommand(SlotBotPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
    public void defaultCommand(final CommandSender commandSender) {

        final SlotBotInventory slotBotInventory = new SlotBotInventory((Player) commandSender);
        slotBotInventory.getSlotBotInventory().open((Player)commandSender);

    }

    @SubCommand("give")
    @Permission("cranked.admin")
    @WrongUsage("&c/slotbot give <player> [amount]")
    public void giveCommand(final CommandSender commandSender, @Completion("#players") final Player receiver, @Optional Integer amount) {
        if(amount == null) amount = 1;
        receiver.getInventory().addItem(SlotBotType.SLOT_BOT.build(amount));
    }

    @SubCommand("heroic")
    @Permission("cranked.admin")
    @WrongUsage("&c/slotbot give <player> [amount]")
    public void giveHeroicCommand(final CommandSender commandSender, @Completion("#players") final Player receiver, @Optional Integer amount) {
        if(amount == null) amount = 1;
        receiver.getInventory().addItem(SlotBotType.HEROIC_SLOT_BOT.build(amount));
    }

    @SubCommand("mastery")
    @Permission("cranked.admin")
    @WrongUsage("&c/slotbot give <player> [amount]")
    public void giveMasteryCommand(final CommandSender commandSender, @Completion("#players") final Player receiver, @Optional Integer amount) {
        if(amount == null) amount = 1;
        receiver.getInventory().addItem(SlotBotType.MASTERY_SLOT_BOT.build(amount));
    }

}
