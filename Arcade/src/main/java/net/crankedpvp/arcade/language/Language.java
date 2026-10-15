package net.crankedpvp.arcade.language;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public enum Language {
    BORDER_TIMER,
    BORDER_STARTED,
    BORDER_EXPIRED,
    CANT_PVP,
    MATCH_STARTED_KILLER,
    MATCH_STARTED_VICTIM,
    MATCH_ENDED,
    MATCH_KILLED,
    MATCH_STARTED_BROADCAST,
    MATCH_KILLED_BROADCAST,
    ADMIN_RELOADING,
    ADMIN_RELOADED,
    GRACE_PERIOD_OTHER,
    GRACE_PERIOD,
    TELEPORT_NO_SET,
    WHITELISTED_COMMANDS,
    BLACKLISTED_COMMAND,
    ENABLED_MESSAGE,
    CLOSED_MESSAGE
    ;

    private String value;
    private ImmutableList<String> valueList;

    public String getValue() {
        return value != null ? value : this.name();
    }

    public String getValue(UnaryOperator<String> function) {
        return value != null ? function.apply(value) : this.name();
    }

    public String getPath() {
        return this.name();
    }

    public List<String> getValuesAsList() {
        return valueList;
    }

    public List<String> getValuesAsList(String oldString, String newString) {
        List<String> list = new ArrayList<>(valueList);
        list.replaceAll(s -> s.replace(oldString, newString));
        return list;
    }

    void setValue(String value) {
        this.value = CC.translate(value);

        List<String> tempList = Arrays.stream(StringUtils.split(value, System.lineSeparator()))
                .map(CC::translate)
                .collect(Collectors.toList());
        valueList = ImmutableList.copyOf(tempList);
    }

    void setValue(List<String> valueList) {
        value = CC.translate(valueList.parallelStream().collect(Collectors.joining(System.lineSeparator())));

        List<String> tempList = valueList.stream()
                .map(CC::translate)
                .collect(Collectors.toList());
        this.valueList = ImmutableList.copyOf(tempList);
    }

    public void send(CommandSender sender, UnaryOperator<String> function) {
        sender.sendMessage(CC.translate(getValue(function)));
    }

    public void send(CommandSender sender) {
        sender.sendMessage(CC.translate(getValue()));
    }


    public void send(List<CommandSender> senders, UnaryOperator<String> function) {
        String msg = CC.translate(getValue(function));
        senders.forEach(sender -> sender.sendMessage(msg));
    }

    public void sendPlayers(List<Player> senders, UnaryOperator<String> function) {
        String msg = CC.translate(getValue(function));
        senders.forEach(sender -> sender.sendMessage(msg));
    }

    public void send(CommandSender ...senders) {
        String msg = CC.translate(getValue());
        Arrays.asList(senders).forEach(sender -> sender.sendMessage(msg));
    }

    public String getMessage() {
        return CC.translate(getValue());
    }

    public String getMessage(UnaryOperator<String> function) {
        return CC.translate(getValue(function));
    }

}
