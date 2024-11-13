package com.fyzermc.factionscore.misc.customitem.data;

import com.fantasy.combatlog.manager.CombatManager;
import com.fyzermc.factionscore.FactionsCoreConstants;
import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.misc.customitem.CustomItem;
import com.fyzermc.factionscore.user.FactionUser;
import com.fyzermc.factionscore.user.FactionUserUtils;
import com.fyzermc.factionscore.util.ItemBuilder;
import com.fyzermc.factionscore.util.messages.Message;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.util.InventoryUtils;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.github.paperspigot.Title;
import org.greenrobot.eventbus.Subscribe;

public class GodsEyeItem extends CustomItem implements Listener {

    public static final String KEY = "gods_eye";

    private final ItemBuilder itemBuilder;

    public GodsEyeItem() {
        super(KEY);
        itemBuilder = new ItemBuilder(Material.EYE_OF_ENDER)
                .name("&6Olho de Deus")
                .lore(
                        "&7Utilize este item para ver tudo",
                        "&7sobre as paredes que o cercam.",
                        "",
                        "&7Duração: &f20 segundos",
                        "",
                        "&e* &7Utilizado em invasões"
                )
                .glowing(true);
    }

    @Override
    protected ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public String getDisplayName() {
        return "Olho de Deus";
    }

    @Subscribe
    public void on(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();

        CombatManager combatManager = new CombatManager(player);

        if (combatManager.hasCombat()) {
            Message.ERROR.send(player, "Você não pode usar o Olho de Deus em combate.");
            return;
        }

        Faction factionAt = BoardColl.get().getFactionAt(PS.valueOf(player.getLocation()));
        if (factionAt.getId().equals("warzone") || factionAt.getId().equals("safezone")) {
            return;
        }

        InventoryUtils.subtractOneOnHand(player);

        player.setGameMode(GameMode.SPECTATOR);
        player.setMetadata(KEY, new FixedMetadataValue(FactionsCorePlugin.INSTANCE, true));

        Message.SUCCESS.send(player, "Você usou o Olho de Deus.");

        FactionUser wrap = FactionUserUtils.wrap(event.getPlayer());

        wrap.setBackLocation(FactionsCoreConstants.SPAWN);

        Location originLocation = player.getLocation();

        new BukkitRunnable() {
            int seconds = 20;

            @Override
            public void run() {
                if (seconds < 0) {
                    cancel();
                    return;
                }

                if (player.isOnline() && seconds >= 0) {
                    if (player.getSpectatorTarget() != null) {
                        player.setSpectatorTarget(null);
                    }

                    Title title;
                    title = new Title("§cFim do Olho de Deus em " + seconds + "s.");

                    player.sendTitle(title);
                    if (seconds == 0) {
                        if (player.getSpectatorTarget() != null) {
                            player.setSpectatorTarget(null);
                        }

                        Title titleEnd;
                        titleEnd = new Title("§cFim do Olho de Deus!");

                        player.sendTitle(titleEnd);

                        player.removeMetadata(KEY, FactionsCorePlugin.INSTANCE);

                        player.setGameMode(GameMode.SURVIVAL);

                        player.eject();
                        player.leaveVehicle();

                        wrap.setBackLocation(FactionsCoreConstants.SPAWN);

                        if (!player.teleport(originLocation)) {
                            player.kickPlayer("Você ficou soterrado ao usar o Olho de Deus.");
                        }
                    }
                }

                seconds--;
            }
        }.runTaskTimer(FactionsCorePlugin.INSTANCE, 20, 20);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if (player.hasMetadata(KEY) && player.getGameMode() == GameMode.SPECTATOR) {
            event.setCancelled(true);
            if (player.getSpectatorTarget() != null) {
                player.setSpectatorTarget(null);
            }
        }
    }

    @EventHandler
    void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (player.hasMetadata("god-eye-item") && entity.getType().equals(EntityType.PLAYER) || player.getGameMode() == GameMode.SPECTATOR && !player.hasPermission("group.moderador")) {
            event.setCancelled(true);
            player.setSpectatorTarget((Entity)null);
            player.sendMessage("§cVocê não pode fazer isto usando o olho de deus.");
        }

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.hasMetadata(KEY) && player.getGameMode() == GameMode.SPECTATOR) {
            event.setCancelled(true);
            if (player.getSpectatorTarget() != null) {
                player.setSpectatorTarget(null);
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if (player.hasMetadata(KEY) && player.getGameMode() == GameMode.SPECTATOR) {
            event.setCancelled(true);
            if (player.getSpectatorTarget() != null) {
                player.setSpectatorTarget(null);
            }

            player.removeMetadata(KEY, FactionsCorePlugin.INSTANCE);

            player.setGameMode(GameMode.SURVIVAL);

            player.kickPlayer("Você não pode fazer isso enquanto usa o Olho de Deus!");
        }
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (player.hasMetadata("god-eye-item") && player.getGameMode() == GameMode.SPECTATOR) {
            event.setCancelled(true);
            player.sendMessage("§cVocê não pode fazer isto usando o olho de deus.");
        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        if (player.hasMetadata(KEY) && player.getGameMode() == GameMode.SPECTATOR) {
            event.setCancelled(true);
        }
    }
}