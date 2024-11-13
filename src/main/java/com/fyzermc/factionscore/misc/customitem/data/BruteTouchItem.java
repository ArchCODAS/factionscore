package com.fyzermc.factionscore.misc.customitem.data;

import com.fyzermc.factionscore.misc.customitem.CustomItem;
import com.fyzermc.factionscore.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockDamageEvent;
import org.greenrobot.eventbus.Subscribe;

public class BruteTouchItem extends CustomItem {

    public static final String KEY = "brute_touch";

    private final ItemBuilder itemBuilder;

    public BruteTouchItem() {
        super(KEY);
        this.itemBuilder = new ItemBuilder(Material.DIAMOND_PICKAXE)
                .name("&bPicareta de Diamante")
                .lore(
                        "&7Toque Bruto I",
                        "",
                        "&e* &7Esta picareta tem a capacidade de quebrar bedrocks."
                )
                .glowing(true);
    }

    @Override
    protected ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public String getDisplayName() {
        return "Toque Bruto";
    }

    @Subscribe
    public void on(BlockDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();

        if (block.getY() <= 1) {
            return;
        }

        if (block.getType() == Material.BEDROCK) {
            event.setInstaBreak(true);
        }
    }
}