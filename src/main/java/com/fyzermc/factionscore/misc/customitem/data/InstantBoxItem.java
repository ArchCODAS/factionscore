package com.fyzermc.factionscore.misc.customitem.data;

import com.fyzermc.factionscore.misc.customitem.CustomItem;
import com.fyzermc.factionscore.user.FactionUserUtils;
import com.fyzermc.factionscore.util.InventoryUtils;
import com.fyzermc.factionscore.util.ItemBuilder;
import com.fyzermc.factionscore.util.PlayerCooldowns;
import com.fyzermc.factionscore.util.WorldCuboid;
import com.fyzermc.factionscore.util.messages.Message;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

public class InstantBoxItem extends CustomItem {

    public static final String KEY = "instant_box";

    private final ItemBuilder itemBuilder;

    public InstantBoxItem() {
        super(KEY);
        itemBuilder = ItemBuilder.of(Material.ENDER_STONE)
                .name("&6Box Instantânea")
                .lore(
                        "&7Clique com este item no chão",
                        "&7para gerar uma box de 16x16"
                )
                .glowing(true);
    }

    @Override
    protected ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public String getDisplayName() {
        return "Box Instantânea";
    }

    @Subscribe
    public void on(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();

        event.setCancelled(true);

        Chunk chunk = event.getClickedBlock().getChunk();
        MPlayer mPlayer = FactionUserUtils.wrap(player).getMPlayer();
        Faction factionAt = BoardColl.get().getFactionAt(PS.valueOf(chunk));

        if (factionAt != mPlayer.getFaction()) {
            Message.ERROR.send(player, "Você só pode utilizar este item em terrenos da sua facção.");
            return;
        }

        if (!PlayerCooldowns.hasEnded(player.getName(), "instant-box-item")) {
            Message.ERROR.send(player, "Aguarde para utilizar este item novamente.");
            return;
        }

        Block block = event.getClickedBlock();
        if (block.getLocation().getY() <= 5) {
            Message.ERROR.send(player, "Não é possível utilizar este item nessa camada.");
            return;
        }

        int xmin = chunk.getX() * 16,
                xmax = xmin + 15,
                y = block.getY() + 20,
                zmin = chunk.getZ() * 16,
                zmax = zmin + 15;

        WorldCuboid worldCuboid = new WorldCuboid(player.getWorld().getName(), xmin, y - 20, zmin, xmax, y, zmax);

        worldCuboid.getBlocks(areaBlock -> {
            if (areaBlock.getType() != Material.MOB_SPAWNER) {
                areaBlock.setType(Material.AIR);
            }
        });

        worldCuboid.getWalls(boxBlock -> {
            if (boxBlock.getType() == Material.AIR) boxBlock.setType(Material.BEDROCK);
        });

        worldCuboid.getRoof(boxBlock -> {
            if (boxBlock.getType() == Material.AIR) boxBlock.setType(Material.BEDROCK);
        });

        worldCuboid.getFloor(boxBlock -> {
            if (boxBlock.getType() == Material.AIR) boxBlock.setType(Material.ENDER_STONE);
        });

        PlayerCooldowns.start(player.getName(), "instant-box-item", 30, TimeUnit.SECONDS);

        InventoryUtils.subtractItem(player);
    }
}