package com.fyzermc.factionscore.misc.customitem.data;

import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.misc.customitem.CustomItem;
import com.fyzermc.factionscore.misc.customitem.CustomItemRegistry;
import com.fyzermc.factionscore.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.greenrobot.eventbus.Subscribe;

public class ImpulseTntItem extends CustomItem implements Listener {

    public static final String KEY = "impulse_tnt";

    public static final String METADATA_KEY = "ImpulseTnT";

    private final ItemBuilder itemBuilder;

    public ImpulseTntItem() {
        super(KEY);
        itemBuilder = new ItemBuilder(Material.TNT)
                .glowing(true)
                .name("&cTnt de Impulsão")
                .lore(
                        "&7Esta TNT não destrói outros blocos e",
                        "&7apenas serve para impulsionar outras tnt's."
                );
    }

    @Override
    public ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public String getDisplayName() {
        return "Tnt de Impulsão";
    }

    @Subscribe
    public void on(BlockPlaceEvent event) {
        event.getBlock().removeMetadata(METADATA_KEY, FactionsCorePlugin.INSTANCE);

        event.getBlock().setMetadata(
                METADATA_KEY,
                new FixedMetadataValue(
                        FactionsCorePlugin.INSTANCE,
                        true
                )
        );
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (block.hasMetadata(ImpulseTntItem.METADATA_KEY)) {
            event.setCancelled(true);

            block.removeMetadata(ImpulseTntItem.METADATA_KEY, FactionsCorePlugin.INSTANCE);
            block.setType(Material.AIR);
            block.getState().update();

            ItemStack itemStack = CustomItemRegistry.getItem(ImpulseTntItem.KEY).asItemStack();

            PlayerInventory inventory = event.getPlayer().getInventory();
            if (inventory.firstEmpty() == -1) {
                block.getWorld().dropItem(
                        block.getLocation(),
                        itemStack
                );
            } else {
                inventory.addItem(itemStack);
            }
        }
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || !event.hasBlock()) {
            return;
        }

        ItemStack itemInHand = event.getPlayer().getItemInHand();
        if (itemInHand.getType() != Material.FLINT_AND_STEEL) {
            return;
        }

        if (!event.getClickedBlock().hasMetadata(ImpulseTntItem.METADATA_KEY)) {
            return;
        }

        event.setCancelled(true);

        igniteBlock(event.getClickedBlock().getLocation());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(BlockDispenseEvent event) {
        ItemStack item = event.getItem();

        if (CustomItemRegistry.getByItemStack(item) instanceof ImpulseTntItem) {
            event.setCancelled(true);

            Block block = event.getBlock();

            Bukkit.getScheduler().callSyncMethod(FactionsCorePlugin.INSTANCE, () -> {
                if (block.getState() instanceof Dispenser) {
                    ((Dispenser) block.getState()).getInventory().removeItem(item);
                }

                return true;
            });

            igniteBlock(event.getVelocity().toLocation(block.getWorld()).getBlock().getLocation(), false);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void placeRedstone(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getBlockPower() >= 1) {
            checkNearby(block);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void entityExplode(EntityExplodeEvent event) {
        if (event.getEntity().hasMetadata(ImpulseTntItem.METADATA_KEY)) {
            event.blockList().clear();
        }
    }

    private void checkNearby(Block block) {
        for (BlockFace blockFace : BlockFace.values()) {
            Block relative = block.getRelative(blockFace);
            if (relative.getType() == Material.TNT && relative.hasMetadata(ImpulseTntItem.METADATA_KEY)) {
                this.igniteBlock(relative.getLocation());
            }
        }
    }

    private void igniteBlock(Location location) {
        igniteBlock(location, true);
    }

    private void igniteBlock(Location location, boolean isTnt) {
        if (isTnt) {
            location.getBlock().removeMetadata(ImpulseTntItem.METADATA_KEY, FactionsCorePlugin.INSTANCE);
            location.getBlock().setType(Material.AIR);
        }

        TNTPrimed tnt = (TNTPrimed) location.getWorld().spawnEntity(location.add(0.5, 0.0, 0.5), EntityType.PRIMED_TNT);
        //tnt.setVelocity(new Vector(0.02, 0.15, 0.02));

        tnt.setMetadata(
                METADATA_KEY,
                new FixedMetadataValue(
                        FactionsCorePlugin.INSTANCE,
                        true
                )
        );

        location.getWorld().playSound(location, Sound.FIRE_IGNITE, -4.0f, 12.0f);
    }
}