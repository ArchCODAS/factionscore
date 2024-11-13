package com.fyzermc.factionscore.misc.customitem.data;

import com.fyzermc.factionscore.misc.customitem.CustomItem;
import com.fyzermc.factionscore.user.FactionUserUtils;
import com.fyzermc.factionscore.util.ItemBuilder;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.util.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.greenrobot.eventbus.Subscribe;

public class ResetKdrItem extends CustomItem {

    public static final String KEY = "reset_kdr";

    private final ItemBuilder itemBuilder;

    public ResetKdrItem() {
        super(KEY);
        itemBuilder = ItemBuilder.of(Material.FIREBALL)
                .flags(ItemFlag.values())
                .name("&eReset KDR")
                .lore("&7Utilize para resetar seus abates/mortes.");
    }

    @Override
    public ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public String getDisplayName() {
        return "Reset KDR";
    }

    @Subscribe
    public void on(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();

        MPlayer mPlayer = FactionUserUtils.wrap(player).getMPlayer();

        if (mPlayer.getKdr() == 0.0) {
            player.sendMessage("§cSeu KDR já está zerado.");
            return;
        }

        InventoryUtils.subtractOneOnHand(player);

        mPlayer.setKills(0);
        mPlayer.setDeaths(0);

        player.sendMessage("§eBoa! KDR resetado com sucesso!");
    }
}