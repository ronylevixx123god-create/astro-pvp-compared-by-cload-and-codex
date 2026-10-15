package com.conaxgames.customskulls.util.inventoryui;

import org.bukkit.ChatColor;

public final class StringUtil {

	public static String toNiceString(String string) {
		string = ChatColor.stripColor(string).replace('_', ' ').toLowerCase();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < string.toCharArray().length; i++) {
			char c = string.toCharArray()[i];
			if (i > 0) {
				char prev = string.toCharArray()[i - 1];
				if (prev == ' ' || prev == '[' || prev == '(') {
					if (i == string.toCharArray().length - 1 || c != 'x' ||
							!Character.isDigit(string.toCharArray()[i + 1])) {
						c = Character.toUpperCase(c);
					}
				}
			} else {
				if (c != 'x' || !Character.isDigit(string.toCharArray()[i + 1])) {
					c = Character.toUpperCase(c);
				}
			}
			sb.append(c);
		}

		return sb.toString();
	}
}
