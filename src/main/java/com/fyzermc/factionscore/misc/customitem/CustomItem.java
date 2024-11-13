package com.fyzermc.factionscore.misc.customitem;

import com.fyzermc.factionscore.util.ItemBuilder;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public abstract class CustomItem {

    private final String key;

    protected CustomItem(String key) {
        this.key = key;
    }

    protected abstract ItemBuilder getItemBuilder();

    public String getKey() {
        return key;
    }

    public abstract String getDisplayName();

    public ItemStack asItemStack() {
        return this.asItemStack(1);
    }

    public ItemStack asItemStack(int amount) {
        ItemBuilder builder = getItemBuilder().clone();

        return asItemStack(builder.clone().amount(amount));
    }

    protected final ItemStack asItemStack(ItemBuilder itemBuilder) {
        if (!CustomItemRegistry.isRegistered(this)) {
            throw new IllegalRegistryException("O item " + key + " não está registrado.");
        }

        if (itemBuilder == null) {
            throw new IllegalRegistryException("O item " + key + " não possui um item builder.");
        }

        if (this instanceof INonStackable) {
            itemBuilder.nbt("INonStackable", UUID.randomUUID().toString());
        }

        ItemStack out = itemBuilder.clone().make();

        CustomItemUtil.tagItem(this, out);

        return out;
    }

    public List<Class<? extends Event>> getNonUseEvents() {
        return Collections.emptyList();
    }
}