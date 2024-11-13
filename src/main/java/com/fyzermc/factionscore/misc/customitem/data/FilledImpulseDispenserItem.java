package com.fyzermc.factionscore.misc.customitem.data;

import com.fyzermc.factionscore.misc.customitem.CustomItem;
import com.fyzermc.factionscore.misc.customitem.CustomItemRegistry;
import com.fyzermc.factionscore.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Dispenser;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.greenrobot.eventbus.Subscribe;

public class FilledImpulseDispenserItem extends CustomItem {

        public static final String KEY = "impulse_filled_dispenser";

    private final ItemBuilder itemBuilder;

    public FilledImpulseDispenserItem() {
        super(KEY);
        this.itemBuilder = new ItemBuilder(Material.DISPENSER)
                .glowing(true)
                .name("&cDispenser de TNT de Impulsão")
                .lore("&7Ao colocar este dispenser no chão", "&7ele virá com 576 TNTs de impulsão.");
    }

    @Override
    public ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public String getDisplayName() {
        return "&cDispenser de TNT de Impulsão";
    }

    @Subscribe
    public void on(BlockPlaceEvent event) {
        if (event.isCancelled() || !event.canBuild()) {
            return;
        }

        ItemStack itemStack = CustomItemRegistry.getItem(ImpulseTntItem.KEY)
                .asItemStack(576);

        Dispenser dispenser = (Dispenser) event.getBlock().getState();
        dispenser.getInventory().addItem(itemStack);
        dispenser.update(true, true);

        event.getBlock().getState().update();
    }
}