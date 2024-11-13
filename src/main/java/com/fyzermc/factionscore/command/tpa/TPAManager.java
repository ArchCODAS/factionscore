package com.fyzermc.factionscore.command.tpa;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.List;

public class TPAManager {

    private static final ListMultimap<String, String> CACHE = ArrayListMultimap.create();

    public static boolean hasRequest(String to, String from) {
        return CACHE.containsEntry(to, from);
    }

    public static void request(String to, String from) {
        CACHE.put(to, from);
    }

    public static String getLast(String userName) {
        List<String> requests = CACHE.get(userName);
        if (requests.isEmpty()) {
            return null;
        }

        return requests.get(0);
    }

    public static void remove(String to, String from) {
        CACHE.remove(to, from);
    }

    public static void remove(String name) {
        CACHE.removeAll(name);
    }
}