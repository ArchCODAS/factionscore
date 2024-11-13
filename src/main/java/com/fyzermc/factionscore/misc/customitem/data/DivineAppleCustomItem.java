package com.fyzermc.factionscore.misc.customitem.data;

import com.fyzermc.factionscore.misc.customitem.CustomItem;
import com.fyzermc.factionscore.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.greenrobot.eventbus.Subscribe;

public class DivineAppleCustomItem extends CustomItem {

    public static final String KEY = "divine_apple";

    private final ItemBuilder itemBuilder;

    public DivineAppleCustomItem() {
        super(KEY);
        this.itemBuilder = new ItemBuilder(Material.GOLDEN_APPLE)
                .name("&6Maçã Divina")
                .glowing(true);
    }

    @Override
    protected ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public String getDisplayName() {
        return "Maçã Divina";
    }

    @Subscribe
    public void on(PlayerItemConsumeEvent event) {

        Player player = event.getPlayer();

        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 45 * 20, 1), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 90 * 20, 4), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 90 * 20, 4), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (60 * 5) * 20, 1), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, (60 * 5) * 20, 0), true);
    }
}