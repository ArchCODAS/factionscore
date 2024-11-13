package com.fyzermc.factionscore.misc.customitem.data;

import com.fyzermc.factionscore.misc.customitem.CustomItem;
import com.fyzermc.factionscore.misc.customitem.CustomItemRegistry;
import com.fyzermc.factionscore.util.ItemBuilder;
import com.fyzermc.factionscore.util.RandomList;
import com.massivecraft.factions.util.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.greenrobot.eventbus.Subscribe;

public class DragonEggCustomItem extends CustomItem {

    public static final String KEY = "dragon_egg";

    private final ItemBuilder itemBuilder;

    public DragonEggCustomItem() {
        super(KEY);
        this.itemBuilder = new ItemBuilder(Material.DRAGON_EGG)
                .name("&5Ovo de Dragão")
                .lore(
                        "&7Coloque este Ovo de Dragão no chão",
                        "&7para clamar a sua recompensa."
                );
    }

    @Override
    protected ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public String getDisplayName() {
        return "Ovo de Dragão";
    }

    @Subscribe
    public void on(PlayerInteractEvent event) {

        InventoryUtils.subtractOneOnHand(event.getPlayer());

        RandomList<ItemStack> random = new RandomList<>();

        random.add(CustomItemRegistry.getItem(LauncherItem.KEY).asItemStack(32), 20);
        random.add(CustomItemRegistry.getItem(DivineAppleCustomItem.KEY).asItemStack(16), 25);
        random.add(CustomItemRegistry.getItem(ExtraMemberItem.KEY).asItemStack(1), 10);
        random.add(CustomItemRegistry.getItem(ExtraPowerItem.KEY).asItemStack(1), 15);

        event.getPlayer().getInventory().addItem(random.raffle());

        event.setCancelled(true);
    }
}