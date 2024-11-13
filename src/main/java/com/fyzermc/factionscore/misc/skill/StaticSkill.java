package com.fyzermc.factionscore.misc.skill;

import com.fyzermc.factionscore.util.PlayerCooldowns;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.TimeUnit;

public class StaticSkill implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getDamager().getType() != EntityType.PLAYER || event.getEntity().getType() != EntityType.PLAYER) {
            return;
        }

        Player entity = (Player) event.getEntity();

        if (!entity.hasPermission("skills.static")) {
            return;
        }

        if (Math.random() <= 0.015) {
            if (!PlayerCooldowns.hasEnded(entity.getName(), "static-cooldown")) {
                return;
            }

            Player damager = (Player) event.getDamager();

            damager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 2, 100));
            damager.sendMessage("§eVocê foi paralisado por STATIC!");

            entity.sendMessage("§eVocê paralisou o inimigo §f" + damager.getName() + " §eutilizando STATIC!");

            PlayerCooldowns.start(entity.getName(), "static-cooldown", 30, TimeUnit.SECONDS);
        }
    }
}