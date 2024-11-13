package com.fyzermc.factionscore.misc.customitem.data;

import com.fyzermc.factionscore.misc.customitem.CustomItem;
import com.fyzermc.factionscore.user.FactionUser;
import com.fyzermc.factionscore.user.FactionUserUtils;
import com.fyzermc.factionscore.util.ItemBuilder;
import com.fyzermc.factionscore.util.messages.Message;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.util.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.greenrobot.eventbus.Subscribe;

public class ExtraMemberItem extends CustomItem {

    public static final String KEY = "extra_member";

    private final ItemBuilder itemBuilder;

    public ExtraMemberItem() {
        super(KEY);
        this.itemBuilder = new ItemBuilder(Material.TRIPWIRE_HOOK)
                .name("&2Membro Extra")
                .glowing(true)
                .lore(
                        "&7Adicione &e+1 &7limite de membros",
                        "&7extras em sua facção.",
                        "",
                        "&e* &7Limite de uso: &f3 vezes"
                );
    }

    @Override
    protected ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public String getDisplayName() {
        return "Membro Extra";
    }

    @Subscribe
    public void on(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();

        FactionUser factionUser = FactionUserUtils.wrap(player);

        MPlayer mPlayer = factionUser.getMPlayer();
        if (mPlayer == null) {
            return;
        }

        Faction faction = mPlayer.getFaction();
        if (faction.isNone()) {
            Message.ERROR.send(player, "Você precisa estar em uma facção para utilizar este item.");
            return;
        }

        if (faction.getMemberBoost() >= 3) {
            player.sendMessage("§cVocê já atingiu o limite de membros máximo.");
            return;
        }

        faction.setMemberBoost(faction.getMemberBoost() + 1);

        InventoryUtils.subtractOneOnHand(player);

        Message.SUCCESS.send(player, "+1 limite de membros adicionado com sucesso!");
    }
}