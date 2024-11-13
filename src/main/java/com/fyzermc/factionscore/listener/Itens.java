package com.fyzermc.factionscore.listener;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum Itens {
    BONE(Material.BONE, "§aOsso Compactado", true),
    ROTTEN_FLESH(Material.ROTTEN_FLESH, "§aZombie Compactado", true),
    PRISMARINE_SHARD(Material.PRISMARINE_SHARD, "§aPrismarinho Compactado", true),
    GHAST_TEAR(Material.GHAST_TEAR, "§aLágrima Compactado", true),
    MAGMA_CREAM(Material.MAGMA_CREAM, "§aMagma Compactado", true),
    STRING(Material.WOOL, "§aTeia Compactada", true),
    QUARTZ(Material.QUARTZ, "§aQuartzo Compactado", true),
    BLAZE_ROD(Material.BLAZE_ROD, "§aBlaze Compactado", true),
    LEATHER(Material.LEATHER, "§aCouro Compactado", true),
    RABBIT_FOOT(Material.RABBIT_FOOT, "§aPé de Coelho Compactado", true),
    COAL(Material.COAL_BLOCK, "§7Bloco de Carvão", false),
    IRON_INGOT(Material.IRON_BLOCK, "§7Bloco de Ferro", false),
    GOLD_INGOT(Material.GOLD_BLOCK, "§7Bloco de Ouro", false),
    DIAMOND(Material.DIAMOND_BLOCK, "§7Bloco de Diamante", false),
    EMERALD(Material.EMERALD_BLOCK, "§7Bloco de Esmeralda", false),
    INK_SACK(Material.LAPIS_BLOCK, "§7Bloco de Lápis-Lazúli", false),
    GOLD_NUGGET(Material.GOLD_INGOT, "§7Barra de Ouro", false),
    MELON(Material.MELON_BLOCK, "§7Bloco de Melancia", false),
    REDSTONE(Material.REDSTONE_BLOCK, "§7Bloco de Redstone", false),
    SLIME_BALL(Material.SLIME_BLOCK, "§7Bloco de Slime", false);


    private final Material material;
    private final String displayName;
    private final boolean hasMeta;
    private Itens(Material material, String displayName, boolean hasMeta) {
        this.material = material;
        this.displayName = displayName;
        this.hasMeta = hasMeta;
    }

    public Material getBlock() {
        return this.material;
    }

    public boolean hasMetadata() {
        return this.hasMeta;
    }

    public ItemStack toItemStack(int quantidade) {
        ItemStack itemStack = new ItemStack(this.material, quantidade);
        if (this.hasMeta) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(this.displayName);
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    public Material getMaterial() {
        return this.material;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}