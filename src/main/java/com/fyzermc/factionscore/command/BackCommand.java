package com.fyzermc.factionscore.command;

import com.fyzermc.factionscore.FactionsCoreConstants;
import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.misc.customitem.data.GodsEyeItem;
import com.fyzermc.factionscore.user.FactionUser;
import com.fyzermc.factionscore.user.FactionUserUtils;
import com.fyzermc.factionscore.util.location.unserializer.BukkitLocationParser;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class BackCommand implements CommandExecutor, Listener {

    public BackCommand(FactionsCorePlugin plugin) {
        plugin.getCommand("back").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando só pode ser usado por jogadores.");
            return true;
        }

        Player player = (Player) sender;

        FactionUser wrap = FactionUserUtils.wrap(player);
        if (wrap.getBackLocation() == null) {
            player.sendMessage(ChatColor.RED + "Você não possui um local para retornar.");
            return true;
        }

        Location location = wrap.getBackLocation().parser(FactionsCoreConstants.LOCATION_PARSER);
        if (!isValidTeleport(wrap, location)) {
            player.sendMessage(ChatColor.RED + "Você não pode voltar para esta localização!");
            return true;
        }

        player.teleport(location);

        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getPlayer().hasMetadata(GodsEyeItem.KEY)) {
            return;
        }

        if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        if (!isValidCause(event.getCause()) || isSameBlock(event.getFrom(), event.getTo())) {
            return;
        }

        FactionUser wrap = FactionUserUtils.wrap(event.getPlayer());

        if (isValidTeleport(wrap, event.getFrom())) {
            wrap.setBackLocation(BukkitLocationParser.serialize(event.getFrom()));
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().hasMetadata(GodsEyeItem.KEY)) {
            return;
        }

        FactionUser wrap = FactionUserUtils.wrap(event.getEntity());

        if (isValidTeleport(wrap, event.getEntity().getLocation())) {
            wrap.setBackLocation(BukkitLocationParser.serialize(event.getEntity().getLocation()));
        }
    }

    private boolean isValidTeleport(FactionUser wrap, Location location) {
        MPlayer mPlayer = wrap.getMPlayer();
        if (mPlayer == null) {
            return false;
        }

        Faction factionAt = BoardColl.get().getFactionAt(PS.valueOf(location.getChunk()));
        if (!factionAt.getMPlayers().contains(mPlayer) && factionAt.getRelationTo(mPlayer.getFaction()) != Rel.ALLY) {
            return factionAt.getId().equals("none") || factionAt.getId().equals("warzone") || factionAt.getId().equals("safezone");
        }

        return true;
    }

    private boolean isSameBlock(Location one, Location two) {
        return one.getBlockX() == two.getBlockX() && one.getBlockZ() == two.getBlockZ()
                && one.getBlockY() == two.getBlockY();
    }

    private boolean isValidCause(PlayerTeleportEvent.TeleportCause cause) {
        return cause == PlayerTeleportEvent.TeleportCause.COMMAND ||
                cause == PlayerTeleportEvent.TeleportCause.PLUGIN;
    }
}