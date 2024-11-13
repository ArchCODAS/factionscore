package com.fyzermc.factionscore.misc.customitem.data;

import com.fyzermc.factionscore.misc.customitem.CustomItem;
import com.fyzermc.factionscore.util.ItemBuilder;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.util.InventoryUtils;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.greenrobot.eventbus.Subscribe;

import java.util.Set;

public class MasterLightningItem extends CustomItem {

    public static final String KEY = "raio_mestre";

    private final ItemBuilder itemBuilder;

    public MasterLightningItem() {
        super(KEY);
        this.itemBuilder = ItemBuilder.of(Material.BLAZE_ROD)
                .name("&eRaio Mestre")
                .lore("&7Um raio irá cair no local em que você clicar.")
                .glowing(true);
    }

    @Override
    protected ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public String getDisplayName() {
        return "Raio Mestre";
    }

    @Subscribe
    public void on(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();

        Location location;

        if (event.hasBlock()) {
            location = event.getClickedBlock().getLocation();
        } else {
            location = player.getTargetBlock((Set<Material>) null, 10).getLocation();
        }

        Faction factionAt = BoardColl.get().getFactionAt(PS.valueOf(location));
        if (factionAt.getId().equals("warzone") || factionAt.getId().equals("safezone")) {
            return;
        }

        InventoryUtils.subtractOneOnHand(player);

        location.getWorld().strikeLightning(location);
    }
}