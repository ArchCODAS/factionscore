package com.fyzermc.factionscore.util.messages;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Pattern;

import static net.md_5.bungee.api.ChatColor.COLOR_CHAR;

public class MessageUtils {

    public static class Patterns {
        public static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + COLOR_CHAR + "[0-9A-FK-OR]");
    }

    public static String translateColorCodes(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String stripColor(final String input) {
        if (input == null) {
            return null;
        }
        return Patterns.STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public static String stripColor(String message, ChatColor... colors) {

        if (colors.length < 1) {
            colors = ChatColor.values();
        }

        StringBuilder codes = new StringBuilder();

        for (ChatColor color : colors) {
            codes.append(color.toString().toCharArray()[1]);
        }

        return Pattern.compile("(?i)" + COLOR_CHAR + "[" + codes.toString().toUpperCase() + "]")
                .matcher(message)
                .replaceAll("");
    }
}