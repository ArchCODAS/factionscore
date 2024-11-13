package com.fyzermc.factionscore.misc.customitem.data;

import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.misc.customitem.CustomItem;
import com.fyzermc.factionscore.util.ItemBuilder;
import com.fyzermc.factionscore.util.WorldCuboid;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.util.InventoryUtils;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.greenrobot.eventbus.Subscribe;

import java.util.Map;
import java.util.WeakHashMap;

@SuppressWarnings("all")
public class TrapItem extends CustomItem {

    public static final String KEY = "armadilha";

    private final ItemBuilder itemBuilder;

    private final Map<String, BlockFace> blockFaceMap = new WeakHashMap<>();

    public TrapItem() {
        super(KEY);
        itemBuilder = ItemBuilder.of(Material.SNOW_BALL)
                .glowing(true)
                .name("&aArmadilha")
                .lore(
                        "&7Durante 20 segundos, um quadrado",
                        "&7de teia será formado no local",
                        "&7onde a bola de neve atingir."
                );
    }

    @Override
    protected ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public String getDisplayName() {
        return "Armadilha";
    }

    @Subscribe
    public void on(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.getLocation().getWorld().getName().equals("world")) {
            event.setCancelled(true);
            return;
        }

        Faction factionAt = BoardColl.get().getFactionAt(PS.valueOf(player.getLocation()));
        if (factionAt.getId().equals("warzone") || factionAt.getId().equals("safezone")) {
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);

        InventoryUtils.subtractOneOnHand(player);

        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setVelocity(player.getEyeLocation().getDirection().add(player.getVelocity()).multiply(1.0));

        snowball.setMetadata(
                "custom_item_key",
                new FixedMetadataValue(FactionsCorePlugin.INSTANCE, "armadilha")
        );

        blockFaceMap.put(player.getName(), event.getBlockFace());
    }

    @Subscribe
    public void on(ProjectileHitEvent event) {
        Projectile entity = event.getEntity();
        entity.removeMetadata("custom_item_key", FactionsCorePlugin.INSTANCE);

        Player player = (Player) entity.getShooter();

        BlockFace face = this.blockFaceMap.remove(player.getName());
        if (face == null) {
            face = BlockFace.SOUTH;
        }

        WorldCuboid cuboid = getCuboid(entity.getLocation().getBlock(), face, 1);
        for (Block block : cuboid) {
            if (block.getType() == Material.AIR) {
                block.setType(Material.WEB);
            }
        }

        Bukkit.getScheduler().runTaskLater(FactionsCorePlugin.INSTANCE, () -> {
            for (Block blocks : cuboid) {
                if (blocks.getType() == Material.WEB) {
                    blocks.setType(Material.AIR);
                }
            }
        }, 180L);
    }

    private WorldCuboid getCuboid(Block block, BlockFace face, int level) {
        Location blockLocation = block.getLocation();
        Location minLocation = blockLocation.clone();
        Location maxLocation = blockLocation.clone();
        level = Math.max(level, 1);
        level = Math.min(5, level);
        --level;
        switch (face) {
            case NORTH:
                minLocation.add(1.0D, -1.0D, level);
                maxLocation.add(-1.0D, 1.0D, 0.0D);
                break;
            case SOUTH:
                minLocation.add(-1.0D, -1.0D, 0.0D);
                maxLocation.add(1.0D, 1.0D, -level);
                break;
            case EAST:
                minLocation.add(0.0D, -1.0D, 1.0D);
                maxLocation.add(-level, 1.0D, -1.0D);
                break;
            case WEST:
                minLocation.add(level, -1.0D, -1.0D);
                maxLocation.add(0.0D, 1.0D, 1.0D);
                break;
            case UP:
                minLocation.add(1.0D, -level, 1.0D);
                maxLocation.add(-1.0D, 0.0D, -1.0D);
                break;
            case DOWN:
                minLocation.add(-1.0D, level, -1.0D);
                maxLocation.add(1.0D, 0.0D, 1.0D);
        }

        return new WorldCuboid(minLocation, maxLocation, blockLocation.getWorld());
    }
}