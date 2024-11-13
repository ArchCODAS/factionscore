package com.fyzermc.factionscore.misc.skill;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SunnyDaySkill implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getDamager().getType() != EntityType.PLAYER || event.getEntity().getType() != EntityType.PLAYER) {
            return;
        }

        Player damager = (Player) event.getDamager();

        if (!damager.hasPermission("skills.sunnyday")) {
            return;
        }

        Player entity = (Player) event.getEntity();

        if (entity.getFireTicks() > 0) {
            event.setDamage(event.getDamage() * 1.1);
        }
    }
}