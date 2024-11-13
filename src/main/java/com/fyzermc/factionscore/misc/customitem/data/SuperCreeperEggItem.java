package com.fyzermc.factionscore.misc.customitem.data;

import com.fyzermc.factionscore.misc.customitem.CustomItem;
import com.fyzermc.factionscore.util.ItemBuilder;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.util.InventoryUtils;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.greenrobot.eventbus.Subscribe;

public class SuperCreeperEggItem extends CustomItem {

    public static final String KEY = "super_creeper";

    private final ItemBuilder itemBuilder;

    public SuperCreeperEggItem() {
        super(KEY);
        this.itemBuilder = ItemBuilder.of(Material.MONSTER_EGG, (short) 50)
                .glowing(true)
                .name("&6Ovo de Super Creeper")
                .lore("Invoca um creeper eletrizado", "que causa mais dano e possui", "um raio de explosão maior.")
                .lore("")
                .lore("&7Alcance da explosão: &f5 blocos", "&7Dano em blocos: &f3 pontos");
    }

    @Override
    public ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public String getDisplayName() {
        return "Ovo de Super Creeper";
    }

    @Subscribe
    public void on(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }

        event.setCancelled(true);

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Location location = event.getClickedBlock()
                .getRelative(event.getBlockFace())
                .getLocation()
                .clone()
                .add(.5, 0, .5);


        Faction factionAt = BoardColl.get().getFactionAt(PS.valueOf(location));
        if (!factionAt.isNone() && !factionAt.isNormal()) {
            return;
        }

        Creeper creeper = (Creeper) location.getWorld().spawnEntity(location, EntityType.CREEPER);
        creeper.setMaxHealth(60d);
        creeper.setMaxHealth(60d);
        creeper.setPowered(true);

        InventoryUtils.subtractOneOnHand(event);
    }
}