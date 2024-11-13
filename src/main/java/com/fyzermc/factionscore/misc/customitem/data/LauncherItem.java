package com.fyzermc.factionscore.misc.customitem.data;

import com.fantasy.combatlog.manager.CombatManager;
import com.fyzermc.factionscore.FactionsCoreConstants;
import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.misc.customitem.CustomItem;
import com.fyzermc.factionscore.util.ItemBuilder;
import com.fyzermc.factionscore.util.PlayerCooldowns;
import com.fyzermc.factionscore.util.messages.Message;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.util.InventoryUtils;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

public class LauncherItem extends CustomItem {

    public static final String KEY = "lancador";

    private final ItemBuilder itemBuilder;

    public LauncherItem() {
        super(KEY);
        itemBuilder = ItemBuilder.of(Material.FIREWORK)
                .name("&aLançador")
                .lore("&7Utilize para ser lançado 60 blocos para cima.")
                .glowing(true);
    }

    @Override
    protected ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public String getDisplayName() {
        return "Lançador";
    }

    @Subscribe
    public void on(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();
        if (player.getLocation().getBlockY() >= 220) {
            return;
        }

        if (!PlayerCooldowns.hasEnded(player.getName(), "user-launcher-item")) {
            return;
        }

        Faction factionAt = BoardColl.get().getFactionAt(PS.valueOf(player.getLocation()));
        if (factionAt.getId().equals("warzone") || factionAt.getId().equals("safezone")) {
            Message.ERROR.send(player, "Não é possível utilizar este item na zona atual.");
            return;
        }

        CombatManager combatManager = new CombatManager(player);

        if (combatManager.hasCombat()) {
            PlayerCooldowns.start(player.getName(), "user-launcher-item", 3, TimeUnit.SECONDS);
        }

        double up = 4.0;
        player.setVelocity(new Vector(0.0D, up, 0.0D));

        InventoryUtils.subtractOneOnHand(event);

        player.getWorld().playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 5.0F, 0.0F);
        player.setMetadata(FactionsCoreConstants.METADATA_FALL_BYPASS_KEY, new FixedMetadataValue(FactionsCorePlugin.INSTANCE, true));
    }
}