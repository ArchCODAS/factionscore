package com.fyzermc.factionscore.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityTypes;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMS {
    public static WorldServer getWorld(org.bukkit.World world) {
        return ((CraftWorld) world).getHandle();
    }

    public static EntityPlayer getPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    public static Class<?> getNMSClassByName(String inName) {
        try {
            return Class.forName("net.minecraft.server." + getMinecraftRevision() + "." + inName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getMinecraftRevision() {
        Class<? extends Server> serverClass = Bukkit.getServer().getClass();
        String remaining = serverClass.getPackage().getName().replace("org.bukkit.craftbukkit.", "");
        return remaining.split("\\.")[0];
    }

    public static Field getOrRegisterField(Class<?> inSource, String inField) {
        Field field;
        try {
            String id = inSource.getName() + "_" + inField;
            if (s_cachedFields.containsKey(id)) {
                field = s_cachedFields.get(id);
            } else {
                field = inSource.getDeclaredField(inField);
                field.setAccessible(true);
                s_cachedFields.put(id, field);
            }

            return field;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Field getOrRegisterNMSField(String inNMSClass, String inField) {
        Field field;
        try {
            String id = inNMSClass + "_" + inField;
            if (s_cachedFields.containsKey(id)) {
                field = s_cachedFields.get(id);
            } else {
                field = Objects.requireNonNull(getNMSClassByName(inNMSClass)).getDeclaredField(inField);
                field.setAccessible(true);
                s_cachedFields.put(id, field);
            }

            return field;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void registerCustomEntity(Class<? extends Entity> customEntityClass, Class<? extends Entity> entityClass) {
        try {
            Class<?> entityTypes = getNMSClassByName("EntityTypes");
            Field nameMap = getOrRegisterField(entityTypes, "d");
            Field idMap = getOrRegisterField(entityTypes, "f");

            assert idMap != null;
            Integer id = (Integer) ((Map<?, ?>) idMap.get(null)).get(entityClass);
            assert nameMap != null;
            String name = (String) ((Map<?, ?>) nameMap.get(null)).get(entityClass);

            registerEntityType(customEntityClass, name, id);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void registerEntityType(Class<?> inClass, String inName, int inID) {
        try {
            clearEntityType(inName, inID);
            @SuppressWarnings("rawtypes")
            Class[] args = new Class[3];
            args[0] = Class.class;
            args[1] = String.class;
            args[2] = int.class;

            Method a = Objects.requireNonNull(getNMSClassByName("EntityTypes")).getDeclaredMethod("a", args);
            a.setAccessible(true);

            a.invoke(a, inClass, inName, inID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearEntityType(String inName, int inID) {
        try {
            Field cMap = getOrRegisterNMSField("EntityTypes", "c");
            Field eMap = getOrRegisterNMSField("EntityTypes", "e");
            assert cMap != null;
            ((Map<?, ?>) cMap.get(null)).remove(inName);
            assert eMap != null;
            ((Map<?, ?>) eMap.get(null)).remove(inID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final Map<String, Field> s_cachedFields = new HashMap<>();

    public static Field PATHFINDER_GOAL_1, PATHFINDER_GOAL_2;

    static {
        try {
            PATHFINDER_GOAL_1 = PathfinderGoalSelector.class.getDeclaredField("b");
            PATHFINDER_GOAL_1.setAccessible(true);

            PATHFINDER_GOAL_2 = PathfinderGoalSelector.class.getDeclaredField("c");
            PATHFINDER_GOAL_2.setAccessible(true);

            Field ENTITY_NAME_TO_CLASS = EntityTypes.class.getDeclaredField("c");
            ENTITY_NAME_TO_CLASS.setAccessible(true);

            Field ENTITY_CLASS_TO_NAME = EntityTypes.class.getDeclaredField("d");
            ENTITY_CLASS_TO_NAME.setAccessible(true);

            Field ENTITY_CLASS_TO_ID = EntityTypes.class.getDeclaredField("f");
            ENTITY_CLASS_TO_ID.setAccessible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}