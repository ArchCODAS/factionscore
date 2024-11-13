package com.fyzermc.factionscore.util;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.material.MaterialData;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;

import static net.md_5.bungee.api.ChatColor.COLOR_CHAR;

public class ItemBuilder implements Cloneable {

    private final ItemStack stack;
    private ItemMeta meta;

    public ItemBuilder() {
        this(Material.AIR);
    }

    public ItemBuilder(Material type) {
        this(type, (short) 0);
    }

    public ItemBuilder(Material type, int amount) {
        this(type, amount, (short) 0);
    }

    public ItemBuilder(Material type, short damage) {
        this(type, 1, damage);
    }

    public ItemBuilder(Material type, int amount, short damage) {
        this(new ItemStack(type, amount, damage));
    }

    public ItemBuilder(MaterialData materialData) {
        this(new ItemStack(materialData.getItemType(), 1, (short) 0));
    }

    public ItemBuilder(MaterialData materialData, int amount) {
        this(new ItemStack(materialData.getItemType(), amount, (short) 0));
    }

    public ItemBuilder(MaterialData materialData, short damage) {
        this(new ItemStack(materialData.getItemType(), 1, damage));
    }

    public ItemBuilder(MaterialData materialData, int amount, short damage) {
        this(new ItemStack(materialData.getItemType(), amount, damage));
    }

    public ItemBuilder(ItemStack itemStack) {
        this(itemStack, false);
    }

    public ItemBuilder(ItemStack stack, boolean keepOriginal) {
        this.stack = keepOriginal ? stack : stack.clone();
        this.meta = this.stack.getItemMeta();
    }

    public static ItemBuilder of(Material type) {
        return new ItemBuilder(type);
    }

    public static ItemBuilder of(Material type, int amount) {
        return new ItemBuilder(type, amount);
    }

    public static ItemBuilder of(Material type, short damage) {
        return new ItemBuilder(type, damage);
    }

    public static ItemBuilder of(Material type, int amount, short damage) {
        return new ItemBuilder(type, amount, damage);
    }

    public static ItemBuilder of(ItemStack itemStack, boolean keepOriginal) {
        return new ItemBuilder(itemStack, keepOriginal);
    }

    public static ItemBuilder of(ItemStack itemStack) {
        return of(itemStack, false);
    }

    public ItemBuilder amount(final Integer itemAmt) {
        make().setAmount(itemAmt);
        return this;
    }

    public ItemBuilder name(final String name) {
        meta().setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        make().setItemMeta(meta());
        return this;
    }

    public String name() {
        return meta().getDisplayName();
    }

    public ItemBuilder lore(String... lore) {
        return lore(false, lore);
    }

    public ItemBuilder lore(boolean override, String... lore) {
        LinkedList<String> lines = new LinkedList<>();
        for (String targetLore : lore) {
            String s = ChatColor.translateAlternateColorCodes('&', targetLore);
            lines.add(s);
        }

        if (!override) {
            List<String> oldLines = meta().getLore();

            if (oldLines != null && !oldLines.isEmpty()) {
                lines.addAll(0, oldLines);
            }
        }

        java.util.regex.Pattern COLOR_PATTERN = java.util.regex.Pattern.compile("(?i)" + COLOR_CHAR + "[0-9A-F]");
        java.util.regex.Pattern START_COLOR_PATTERN = java.util.regex.Pattern.compile("^(?i)" + COLOR_CHAR + "[0-9A-F].*$");

        for (int i = 0; i < lines.size() - 1; i++) {
            String line = lines.get(i);

            if (line == null || line.isEmpty()) {
                continue;
            }

            Matcher nextMatcher = START_COLOR_PATTERN.matcher(lines.get(i + 1));

            if (nextMatcher.find()) {
                continue;
            }

            Matcher currentMatcher = COLOR_PATTERN.matcher(line);

            if (currentMatcher.find()) {
                String lastColor = currentMatcher.group(currentMatcher.groupCount());

                lines.set(i + 1, lastColor + lines.get(i + 1));
            }
        }

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line != null && !line.isEmpty() && !START_COLOR_PATTERN.matcher(line).find()) {
                lines.set(i, ChatColor.GRAY + line);
            }

            if (Objects.requireNonNull(ChatColor.stripColor(line)).isEmpty()) {
                lines.set(i, "");
            }
        }

        meta().setLore(lines);
        make().setItemMeta(meta());
        return this;
    }

    public List<String> lore() {
        return meta().getLore() == null ? Collections.emptyList() : Lists.newArrayList(meta().getLore());
    }

    public void durability(final int durability) {
        make().setDurability((short) durability);
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder data(final int data) {
        make().setData(new MaterialData(make().getType(), (byte) data));
        return this;
    }

    public ItemBuilder glowing(boolean glowing) {
        if (make().getType().equals(Material.GOLDEN_APPLE)) {
            durability((short) (glowing ? 1 : 0));
            make().setItemMeta(meta());
            return this;
        }

        if (enchantments().isEmpty()) {
            if (glowing) {
                nbt("ench", new NBTTagList());
            } else {
                removeNbt("ench");
            }
        }

        make().setItemMeta(meta());
        return this;
    }

    public ItemBuilder flags(ItemFlag... flags) {
        meta().addItemFlags(flags);
        make().setItemMeta(meta());
        return this;
    }

    public Map<Enchantment, Integer> enchantments() {
        return make().getEnchantments();
    }

    private void nbt(Consumer<NBTTagCompound> consumer) {
        net.minecraft.server.v1_8_R3.ItemStack nmsCopy = CraftItemStack.asNMSCopy(make());

        NBTTagCompound compound = (nmsCopy.hasTag()) ? nmsCopy.getTag() : new NBTTagCompound();

        consumer.accept(compound);

        nmsCopy.setTag(compound);

        meta = CraftItemStack.asBukkitCopy(nmsCopy).getItemMeta();
        make().setItemMeta(meta());
    }

    private <T> T nbt(Function<NBTTagCompound, T> function) {
        net.minecraft.server.v1_8_R3.ItemStack nmsCopy = CraftItemStack.asNMSCopy(make());

        NBTTagCompound compound = (nmsCopy.hasTag()) ? nmsCopy.getTag() : new NBTTagCompound();

        return function.apply(compound);
    }

    public void removeNbt(String tag) {
        nbt(compound -> {
            compound.remove(tag);
        });
    }

    public void nbt(String tag, NBTBase value) {
        nbt(compound -> {
            compound.set(tag, value);
        });

    }

    public void nbt(String tag, String value) {
        nbt(compound -> {
            compound.setString(tag, value);
        });

    }

    public String nbtString(String tag) {
        return nbt(compound -> compound.hasKey(tag) ? compound.getString(tag) : null);
    }

    public ItemMeta meta() {
        return meta;
    }

    public ItemStack make() {
        return stack;
    }

    @Override
    public ItemBuilder clone() {
        return new ItemBuilder(this.make().clone());
    }
}