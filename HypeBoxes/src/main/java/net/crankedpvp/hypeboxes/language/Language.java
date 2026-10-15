package net.crankedpvp.hypeboxes.language;

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

    PREFIX,
    FULL_INVENTORY,


    ALREADY_HYPEBOX,
    CREATED_HYPEBOX,
    DELETED_HYPEBOX,
    HYPEBOX_DISABLED,
    HYPEBOX_ENABLED,
    HYPEBOX_INVALID,
    RELOADED_ALL,
    RELOADED_SINGULAR,

    HYPEBOX_RECEIVED,
    HYPEBOX_GIVEN,

    HYPEBOX_ADDITEM,
    HYPEBOX_ITEM_ALREADY_IN,

    HYPEBOX_ITEM_NOT_FOUND,
    HYPEBOX_REMOVE_ITEM,

    PREVIEW_LOOT,
    HYPEBOX_OPENED,


    ;

    private String value;
    private ImmutableList<String> valueList;

    public String getValue() {
        return this.value != null ? this.value : this.name();
    }

    public String getValue(UnaryOperator<String> function) {
        return this.value != null ? function.apply(this.value) : this.name();
    }

    public String getPath() {
        return this.name();
    }

    public List<String> getValuesAsList() {
        return this.valueList;
    }

    public List<String> getValuesAsList(String oldString, String newString) {
        List<String> list = new ArrayList<>(this.valueList);
        list.replaceAll(s -> s.replace(oldString, newString));
        return list;
    }

    void setValue(String value) {
        this.value = CC.translate(value);

        List<String> tempList = Arrays.stream(StringUtils.split(value, System.lineSeparator()))
                .map(CC::translate)
                .collect(Collectors.toList());
        this.valueList = ImmutableList.copyOf(tempList);
    }

    void setValue(List<String> valueList) {
        this.value = CC.translate(valueList.parallelStream().collect(Collectors.joining(System.lineSeparator())));

        List<String> tempList = valueList.stream()
                .map(CC::translate)
                .collect(Collectors.toList());
        this.valueList = ImmutableList.copyOf(tempList);
    }

    public void send(CommandSender sender, UnaryOperator<String> function) {
        sender.sendMessage(CC.translate(this.getValue(function)));
    }

    public void send(CommandSender sender) {
        sender.sendMessage(CC.translate(this.getValue()));
    }


    public void send(List<CommandSender> senders, UnaryOperator<String> function) {
        String msg = CC.translate(this.getValue(function));
        senders.forEach(sender -> sender.sendMessage(msg));
    }

    public void sendPlayers(List<Player> senders, UnaryOperator<String> function) {
        String msg = CC.translate(this.getValue(function));
        senders.forEach(sender -> sender.sendMessage(msg));
    }

    public void send(CommandSender ...senders) {
        String msg = CC.translate(this.getValue());
        Arrays.asList(senders).forEach(sender -> sender.sendMessage(msg));
    }

    public String getMessage() {
        return CC.translate(this.getValue());
    }

    public String getMessage(UnaryOperator<String> function) {
        return CC.translate(this.getValue(function));
    }

}
