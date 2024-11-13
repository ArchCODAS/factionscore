package com.fyzermc.factionscore.user.cache;

import com.fyzermc.factionscore.user.FactionUser;
import com.fyzermc.factionscore.util.cache.LocalCache;

import java.util.HashMap;
import java.util.Map;

public class FactionUserLocalCache implements LocalCache {

    private final Map<String, FactionUser> userMap = new HashMap<>();

    public FactionUser get(String userName) {

        return userMap.computeIfAbsent(userName, FactionUser::new);
    }

    public FactionUser remove(String userName) {
        return userMap.remove(userName);
    }
}