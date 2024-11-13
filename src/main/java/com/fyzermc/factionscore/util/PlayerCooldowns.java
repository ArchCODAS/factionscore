package com.fyzermc.factionscore.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.concurrent.TimeUnit;

public class PlayerCooldowns {

    private static final Table<String, String, Long> COOLDOWNS = HashBasedTable.create();

    public static void start(String userName, String cooldownKey, int amount, TimeUnit unit) {
        COOLDOWNS.put(
                userName,
                cooldownKey,
                System.currentTimeMillis() + unit.toMillis(amount)
        );
    }

    public static boolean hasEnded(String userName, String cooldownKey) {
        Long cooldown = COOLDOWNS.get(userName, cooldownKey);

        if (cooldown == null) {
            return true;
        }

        if (cooldown <= System.currentTimeMillis()) {
            COOLDOWNS.remove(userName, cooldownKey);
            return true;
        }

        return false;
    }

    public static long getMillisLeft(String user, String key) {
        return hasEnded(user, key) ? 0L : COOLDOWNS.get(user, key) - System.currentTimeMillis();
    }

    public static int getSecondsLeft(String user, String key) {
        return hasEnded(user, key) ? 0 : ((int) TimeUnit.MILLISECONDS.toSeconds(getMillisLeft(user, key))) + 1;
    }
}