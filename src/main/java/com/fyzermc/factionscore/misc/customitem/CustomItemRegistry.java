package com.fyzermc.factionscore.misc.customitem;

import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;

import java.util.HashMap;
import java.util.Map;

public class CustomItemRegistry {

    public static final String NBT_KEY = "custom_item_key";
    private final static Map<String, CustomItem> ITEMS = new HashMap<>();
    private final static Map<String, EventBus> BUS = new HashMap<>();

    public static void registerCustomItem(CustomItem... items) {
        for (CustomItem item : items) {
            if (isRegistered(item)) {
                throw new IllegalRegistryException("O item " + item.getKey() + " já está registrado.");
            }

            EventBus bus = EventBus.builder()
                    .logNoSubscriberMessages(false)
                    .throwSubscriberException(true)
                    .build();

            try {
                bus.register(item);
            } catch (EventBusException ignored) {

            }

            ITEMS.put(item.getKey(), item);
            BUS.put(item.getKey(), bus);

            if (item instanceof Listener) {
                FactionsCorePlugin.INSTANCE.getLogger().info(item.getKey() + " é listener e foi registrado.");

                FactionsCorePlugin.INSTANCE.getServer().getPluginManager()
                        .registerEvents((Listener) item, FactionsCorePlugin.INSTANCE);
            }
        }
    }

    public static EventBus getEventBus(CustomItem item) {
        return BUS.get(item.getKey());
    }

    public static boolean isRegistered(CustomItem item) {
        return item != null && getItem(item.getKey()) != null;
    }

    public static CustomItem getItem(String id) {
        return CustomItemRegistry.ITEMS.get(id);
    }

    public static CustomItem getByItemStack(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return null;
        }

        ItemBuilder itemBuilder = ItemBuilder.of(itemStack, true);

        return CustomItemRegistry.getItem(itemBuilder.nbtString(CustomItemRegistry.NBT_KEY));
    }
}