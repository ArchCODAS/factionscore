package com.fyzermc.factionscore.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InventoryUtils {

    public static void subtractItem(Player player) {
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        ItemStack item = player.getItemInHand();
        int amount = item.getAmount();

        if (amount > 1) {
            item.setAmount(amount - 1);
        } else {
            item.setAmount(0);
            item.setType(Material.AIR);
            item.setData(new MaterialData(Material.AIR));
            item.setItemMeta(null);
            player.setItemInHand(new ItemStack(Material.AIR));
        }
    }

    public static Inventory copy(Inventory inventory) {
        Inventory inv = Bukkit.createInventory(null, inventory.getSize(), inventory.getTitle());

        ItemStack[] orginal = inventory.getContents();
        ItemStack[] clone = new ItemStack[orginal.length];

        for (int i = 0; i < orginal.length; i++) {
            if (orginal[i] != null) {
                clone[i] = orginal[i].clone();
            }
        }

        inv.setContents(clone);

        return inv;
    }

    public static boolean fits(Inventory inventory, ItemStack... stacks) {
        Inventory clonedInventory = InventoryUtils.copy(inventory);

        ItemStack[] clone = new ItemStack[stacks.length];

        for (int i = 0; i < stacks.length; i++) {
            if (stacks[i] != null) {
                clone[i] = stacks[i].clone();
            }
        }

        return clonedInventory.addItem(clone).isEmpty();
    }
}