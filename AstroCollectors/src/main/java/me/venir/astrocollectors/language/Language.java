package me.venir.astrocollectors.language;

import me.venir.astrocollectors.utils.Utils;
import org.bukkit.command.CommandSender;

import java.util.function.UnaryOperator;

public enum Language {
    NEED_FACTION("&c&l(!)&c You need a faction to do this."),
    COLLECTOR_HELP("&7/collector &a<player> <amount>"),
    COLLECTOR_SELL("&a&l+ $%price%"),
    COLLECTOR_TNT("&c&l+ %tnt% TNT"),
    PLACED_COLLECTOR("&a&l(!)&a Chunk collector placed, mob deaths will cause compatible mob drops to be automatically collected."),
    CAN_NOT_PLACE_HERE("&c&l(!)&c You cannot place another collector in this chunk"),
    CAN_NOT_PLACE_IN_WILD("&c&l(!)&c You cannot place a collector in wilderness")
    ;

    private final String message;

    Language(String message) {
        this.message = message;
    }

    public String getMessage() {
        return Utils.format(message);
    }

    public void send(CommandSender sender, UnaryOperator<String> function) {
        sender.sendMessage(Utils.format(function.apply(message)));
    }

    public void send(CommandSender sender) {
        sender.sendMessage(Utils.format(message));
    }
}
