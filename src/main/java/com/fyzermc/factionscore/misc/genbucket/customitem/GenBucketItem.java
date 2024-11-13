package com.fyzermc.factionscore.misc.genbucket.customitem;

import com.fantasy.combatlog.manager.CombatManager;
import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.misc.customitem.CustomItem;
import com.fyzermc.factionscore.misc.genbucket.generation.Generation;
import com.fyzermc.factionscore.util.ItemBuilder;
import com.fyzermc.factionscore.util.messages.Message;
import com.massivecraft.factions.engine.EngineCustomClaim;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.util.InventoryUtils;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.greenrobot.eventbus.Subscribe;

public class GenBucketItem extends CustomItem {

    public static final String KEY = "gerador";

    private final ItemBuilder itemBuilder;

    public GenBucketItem() {
        super(KEY);
        this.itemBuilder = new ItemBuilder(Material.LAVA_BUCKET)
                .name("&eGerador de Camadas")
                .lore("&7Clique com este item no chão", "&7e gere uma torre de Bedrock.");
    }

    protected ItemBuilder getItemBuilder() {
        return this.itemBuilder;
    }

    public String getDisplayName() {
        return "Gerador de Camadas";
    }

    @Subscribe
    public void on(PlayerBucketEmptyEvent event) {
        if (!event.isCancelled()) {
            event.setCancelled(true);
            if (event.getBlockFace() != BlockFace.UP && event.getBlockFace() != BlockFace.DOWN) {

                Block blockClicked = event.getBlockClicked();
                if (blockClicked.getY() > 90) {
                    Message.ERROR.send(event.getPlayer(), "O gerador de camadas só pode ser usado da camada 60 pra baixo.");
                    return;
                }

                if (BoardColl.get().getFactionAt(PS.valueOf(blockClicked)).isInAttack()) {
                    Message.ERROR.send(event.getPlayer(), "O gerador de camadas não pode ser utilizado com a facção sob ataque.");
                    return;
                }

                Chunk chunk = blockClicked.getChunk();
                if (EngineCustomClaim.get().isChunkTemporary(chunk.getX(), chunk.getZ())) {
                    Message.ERROR.send(event.getPlayer(), "Isto não pode ser usado aqui.");
                    return;
                }

                CombatManager combatManager = new CombatManager(event.getPlayer());

                if (combatManager.hasCombat()) {
                    return;
                }

                InventoryUtils.subtractOneOnHand(event.getPlayer());

                Block block = blockClicked.getRelative(event.getBlockFace());

                block.setType(Material.BEDROCK);

                FactionsCorePlugin.GENERATION_TASK.addGeneration(new Generation(block));
            } else {
                Message.ERROR.send(event.getPlayer(), "Algo de errado aconteceu, tente novamente.");
            }
        }
    }
}