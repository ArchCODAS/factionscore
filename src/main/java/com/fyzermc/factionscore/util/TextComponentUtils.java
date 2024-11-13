package com.fyzermc.factionscore.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class TextComponentUtils {

    public static TextComponent getAcceptOrDenyMessage(ChatColor baseColor, String acceptCommand, String denyCommand) {
        TextComponent one = new TextComponent("Clique ");
        one.setColor(baseColor);

        TextComponent two = new TextComponent("AQUI");
        two.setColor(ChatColor.GREEN);
        two.setBold(true);
        two.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, acceptCommand));
        two.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.GREEN + "Clique aqui para aceitar.\n" + ChatColor.GRAY + acceptCommand)));

        TextComponent three = new TextComponent(" para aceitar e ");
        three.setColor(baseColor);

        TextComponent four = new TextComponent("AQUI");
        four.setColor(ChatColor.RED);
        four.setBold(true);
        four.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, denyCommand));
        four.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.RED + "Clique aqui para negar.\n" + ChatColor.GRAY + denyCommand)));

        TextComponent five = new TextComponent(" para negar.");
        five.setColor(baseColor);

        return new TextComponent(one, two, three, four, five);
    }
}   