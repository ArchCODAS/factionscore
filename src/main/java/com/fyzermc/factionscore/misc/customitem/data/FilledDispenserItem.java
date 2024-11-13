package com.fyzermc.factionscore.misc.customitem.data;

import com.fyzermc.factionscore.misc.customitem.CustomItem;
import com.fyzermc.factionscore.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Dispenser;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.greenrobot.eventbus.Subscribe;

public class FilledDispenserItem extends CustomItem {

    public static final String KEY = "filled_dispenser";

    private final ItemBuilder itemBuilder;

    public FilledDispenserItem() {
        super(KEY);
        this.itemBuilder = new ItemBuilder(Material.DISPENSER)
                .glowing(true)
                .name("&cDispenser de TNT")
                .lore("&7Ao colocar este dispenser", "&7no chão, ele virá com 576 TNTs.");
    }

    @Override
    public ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public String getDisplayName() {
        return "Dispenser de TNT";
    }

    @Subscribe
    public void on(BlockPlaceEvent event) {
        if (event.isCancelled() || !event.canBuild()) {
            return;
        }

        ItemStack itemStack = new ItemStack(Material.TNT, 576);

        Dispenser dispenser = (Dispenser) event.getBlock().getState();
        dispenser.getInventory().addItem(itemStack);
        dispenser.update(true, true);

        event.getBlock().getState().update();
    }
}