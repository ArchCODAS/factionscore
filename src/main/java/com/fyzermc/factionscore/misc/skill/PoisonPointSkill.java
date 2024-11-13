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

public class PoisonPointSkill implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getDamager().getType() != EntityType.PLAYER || event.getEntity().getType() != EntityType.PLAYER) {
            return;
        }

        Player entity = (Player) event.getEntity();

        if (!entity.hasPermission("skills.poisonpoint")) {
            return;
        }

        if (Math.random() <= 0.2) {
            if (!PlayerCooldowns.hasEnded(entity.getName(), "poison-cooldown")) {
                return;
            }

            Player damager = (Player) event.getDamager();

            damager.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 4, 1));

            PlayerCooldowns.start(entity.getName(), "poison-cooldown", 10, TimeUnit.SECONDS);
        }
    }
}