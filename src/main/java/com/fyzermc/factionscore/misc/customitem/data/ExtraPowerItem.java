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
import org.greenrobot.eventbus.Subscribe;

public class ExtraPowerItem extends CustomItem {

    public static final String KEY = "poder_extra";

    private final ItemBuilder itemBuilder;

    public ExtraPowerItem() {
        super(KEY);
        itemBuilder = ItemBuilder.of(Material.NETHER_STAR)
                .name("&dPoder Extra")
                .lore(
                        "&7Utilizando este item você irá aumentar",
                        "&7um ponto de seu poder máximo."
                )
                .glowing(true);
    }

    @Override
    public ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public String getDisplayName() {
        return "Poder Extra";
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

        if (mPlayer.getPowerBoost() >= 2) {
            player.sendMessage("§cVocê já atingiu o limite de poder máximo.");
            return;
        }

        InventoryUtils.subtractOneOnHand(player);

        mPlayer.setPowerBoost(mPlayer.getPowerBoost() + 1.0);

        player.sendMessage("§eSeu limite de poder máximo foi aumentado em 1 com sucesso.");
    }
}