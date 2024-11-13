package com.fyzermc.factionscore.misc.skill;

import com.fyzermc.factionscore.util.PlayerCooldowns;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AssassinSkill implements Listener {

    private final Map<Player, Integer> attacksCount = new HashMap<>();
    private final Map<Player, Long> vulnerablePlayers = new HashMap<>();

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getDamager().getType() != EntityType.PLAYER || event.getEntity().getType() != EntityType.PLAYER) {
            return;
        }

        Player damager = (Player) event.getDamager();

        if (!damager.hasPermission("skills.assassin")) {
            return;
        }

        Player entity = (Player) event.getEntity();

        if (damager == entity) {
            return;
        }

        int count = attacksCount.getOrDefault(damager, 0) + 1;
        attacksCount.put(damager, count);

        if (count >= 3) {
            attacksCount.put(damager, 0);
            if (!PlayerCooldowns.hasEnded(entity.getName(), "assassin-cooldown")) {
                return;
            }

            vulnerablePlayers.put(entity, System.currentTimeMillis() + (4 * 1000L));

            damager.sendMessage("§eVocê deixou o inimigo vulnerável!");

            PlayerCooldowns.start(entity.getName(), "assassin-cooldown", 30, TimeUnit.SECONDS);
        }
    }

    @EventHandler
    public void onEntityDamageWithVulnerability(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getDamager().getType() != EntityType.PLAYER || event.getEntity().getType() != EntityType.PLAYER) {
            return;
        }

        Player entity = (Player) event.getEntity();

        if (isPlayerVulnerable(entity)) {
            event.setDamage(event.getDamage() * 1.2);
        }
    }

    private boolean isPlayerVulnerable(Player player) {
        return vulnerablePlayers.containsKey(player) && vulnerablePlayers.get(player) > System.currentTimeMillis();
    }
}