package com.fyzermc.factionscore.listener.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.command.FpsCommand;
import org.bukkit.entity.Player;

public class TileEntityPacketListener extends PacketAdapter {

    public TileEntityPacketListener(FactionsCorePlugin plugin, PacketType... types) {
        super(plugin, types);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (event.isPlayerTemporary()) {
            return;
        }

        Player player = event.getPlayer();
        if (player == null) {
            return;
        }

        PacketContainer packetContainer = event.getPacket();
        if (packetContainer.getIntegers().read(0) != 1) {
            return;
        }

        if (!FpsCommand.CACHED.contains(player.getName())) {
            return;
        }

        NbtCompound nbt = (NbtCompound) packetContainer.getNbtModifier().read(0);
        if (nbt.getKeys().contains("EntityId")) {
            nbt.put("EntityId", "null");
        }

        if (nbt.getKeys().contains("SpawnData")) {
            nbt.put("SpawnData", nbt.getCompound("SpawnData").put("id", "null"));
        }

        nbt.put("RequiredPlayerRange", (short) 0);
    }
}