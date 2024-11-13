package com.fyzermc.factionscore.command;

import java.util.ArrayList;

import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.listener.ActionBar;
import com.fyzermc.factionscore.listener.Itens;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CompactCommand implements CommandExecutor, Listener {

    public CompactCommand(FactionsCorePlugin plugin) {
        plugin.getCommand("compactar").setExecutor(this);

    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String lbl, String[] args) {
        if (!(s instanceof Player)) {
            s.sendMessage("§cVocê não pode compactar itens pelo console!");
        } else {
            Player p = (Player)s;
            int compactados = this.compactarItens(p.getInventory().getContents(), p.getInventory());
            if (compactados == 0) {
                ActionBar.sendToPlayer("§cVocê não possui itens para compactar.");
            } else {
                ActionBar.sendToPlayer("§a" + compactados + " item" + (compactados > 1 ? "s" : "") + " compactado com sucesso!");
            }
        }

        return true;
    }

    public int compactarItens(ItemStack[] itens, PlayerInventory inv) {
        int compactados = 0;
        ArrayList<ItemStack> devolver = new ArrayList();
        ItemStack[] var5 = itens;
        int var6 = itens.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            ItemStack item = var5[var7];
            if (item != null && item.getType() != Material.AIR && item.getAmount() >= 9 && !item.hasItemMeta() && (item.getType() != Material.INK_SACK || item.getDurability() == 4)) {
                Itens ores = null;

                try {
                    ores = Itens.valueOf(item.getType().name());
                } catch (IllegalArgumentException var14) {
                }

                if (ores != null) {
                    int quantidade = item.getAmount();
                    int give = quantidade / 9;
                    int resto = quantidade % 9;
                    ItemStack block;
                    if (ores.hasMetadata()) {
                        block = ores.toItemStack(give);
                    } else {
                        block = new ItemStack(ores.getMaterial(), give);
                    }

                    inv.removeItem(new ItemStack[]{item});
                    inv.addItem(new ItemStack[]{block});
                    if (resto > 0) {
                        devolver.add(new ItemStack(item.getType(), resto, item.getDurability()));
                    }

                    compactados += give * 9;
                }
            }
        }

        return compactados;
    }
}