package com.fyzermc.factionscore.misc.customitem.data;

import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.misc.customitem.CustomItem;
import com.fyzermc.factionscore.user.FactionUserUtils;
import com.fyzermc.factionscore.util.ItemBuilder;
import com.fyzermc.factionscore.util.PlayerCooldowns;
import com.fyzermc.factionscore.util.messages.Message;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.engine.EngineCustomClaim;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class    ChunkCleanerItem extends CustomItem {

    public static final String KEY = "chunk_cleaner";

    private final ItemBuilder itemBuilder;

    private final Set<Chunk> chunks = new HashSet<>();

    public ChunkCleanerItem() {
        super(KEY);
        this.itemBuilder = new ItemBuilder(Material.ENDER_PORTAL_FRAME)
                .name("&aLimpador de Chunk")
                .lore(
                        "&7Coloque este item em uma chunk para",
                        "&7limpá-la por completo."
                )
                .glowing(true);
    }

    @Override
    public ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public String getDisplayName() {
        return "Limpador de Chunk";
    }

    @Subscribe
    public void on(BlockPlaceEvent event) {

        Player player = event.getPlayer();

        if (!PlayerCooldowns.hasEnded(player.getName(), "chunk-bust")) {
            event.setCancelled(true);

            Message.ERROR.send(player, "Aguarde para utilizar outro limpador.");
            return;
        }

        final Chunk chunk = event.getBlock().getChunk();
        if (this.chunks.contains(chunk)) {
            event.setCancelled(true);

            Message.ERROR.send(player, "Já existe um limpador neste chunk.");
            return;
        }

        MPlayer mPlayer = FactionUserUtils.wrap(player).getMPlayer();

        Faction factionAt = BoardColl.get().getFactionAt(PS.valueOf(chunk));
        if (factionAt.isInAttack() || EngineCustomClaim.get().isChunkTemporary(chunk.getX(), chunk.getZ())) {
            event.setCancelled(true);

            Message.ERROR.send(player, "Você não pode fazer isto aqui.");
            return;
        }

        if (factionAt.isNone() || !factionAt.getTag().equals(mPlayer.getFactionTag()) ||
                mPlayer.getRole().isLessThan(Rel.MEMBER)) {
            event.setCancelled(true);

            Message.ERROR.send(player, "Você não pode fazer isto aqui.");
            return;
        }

        PlayerCooldowns.start(player.getName(), "chunk-bust", 1, TimeUnit.MINUTES);

        this.chunks.add(chunk);

        int startingHeight = event.getBlockPlaced().getY() - 1;

        final Queue<Block> blocks = new LinkedList<>();

        this.processBlocks(chunk, startingHeight, blocks::add);

        BukkitRunnable killBlock = new BukkitRunnable() {
            @Override
            public void run() {
                event.getBlock().setType(Material.AIR);
            }
        };
        killBlock.runTaskLater(FactionsCorePlugin.INSTANCE, 1L);

        final BukkitRunnable cleanup = new BukkitRunnable() {
            @Override
            public void run() {
                processBlocks(chunk, startingHeight, block -> block.setType(Material.AIR));
                chunks.remove(chunk);

                if (player.isOnline()) {
                    Message.INFO.send(player, "O limpador de chunk foi finalizado.");
                }
            }
        };

        BukkitRunnable countdown = new BukkitRunnable() {
            private int seconds = 10;

            @Override
            public void run() {
                if (--this.seconds < 0) {
                    this.cancel();
                    cleanup.runTask(FactionsCorePlugin.INSTANCE);
                }
            }
        };

        final BukkitRunnable buster = new BukkitRunnable() {
            @Override
            public void run() {
                for (int x = 0; x < 300; x++) {
                    Block block = blocks.poll();
                    if (block == null) {
                        this.cancel();
                        countdown.runTaskTimer(FactionsCorePlugin.INSTANCE, 1L, 20L);
                        break;
                    }
                    block.setType(Material.AIR);
                }
            }
        };

        buster.runTaskTimer(FactionsCorePlugin.INSTANCE, 1, 10L);

        Message.SUCCESS.send(player, "Limpando...");
    }

    private void processBlocks(Chunk chunk, int startingHeight, Consumer<Block> consumer) {
        Block block;
        for (int y = startingHeight; y >= 1; y--) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    block = chunk.getBlock(x, y, z);
                    if (!(block.getType() == Material.AIR) && !(block.getType() == Material.BEDROCK)) {
                        consumer.accept(block);
                    }
                }
            }
        }
    }
}