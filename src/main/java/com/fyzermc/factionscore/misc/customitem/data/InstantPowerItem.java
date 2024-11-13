package com.fyzermc.factionscore.misc.customitem.data;

import com.fyzermc.factionscore.misc.customitem.CustomItem;
import com.fyzermc.factionscore.user.FactionUserUtils;
import com.fyzermc.factionscore.util.ItemBuilder;
import com.fyzermc.factionscore.util.messages.Message;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.util.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.greenrobot.eventbus.Subscribe;

public class InstantPowerItem extends CustomItem {

    public static final String KEY = "poder_instantaneo";

    private final ItemBuilder itemBuilder;

    public InstantPowerItem() {
        super(KEY);
        this.itemBuilder = ItemBuilder.of(Material.PAPER)
                .name("&aPoder Instantâneo")
                .lore(
                        "&7Utilize para regenerar instantaneamente 1 de poder."
                )
                .glowing(true);
    }

    @Override
    protected ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public String getDisplayName() {
        return "Poder Instantâneo";
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

        if (mPlayer.getPower() >= mPlayer.getPowerMax()) {
            Message.ERROR.send(player, "Você já está com seu poder totalmente regenerado.");
            return;
        }

        InventoryUtils.subtractOneOnHand(player);

        mPlayer.setPower(mPlayer.getPower() + 1.0);

        Message.INFO.send(player, "Seu poder atual foi aumentado em +1 com sucesso.");
    }
}