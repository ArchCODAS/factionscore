package com.fyzermc.factionscore;

import com.fyzermc.factionscore.misc.blocks.cache.FactionsBlockLocalCache;
import com.fyzermc.factionscore.user.cache.FactionUserLocalCache;
import com.fyzermc.factionscore.util.providers.LocalCacheProvider;

public class FactionsCoreProvider {

    public static class Cache {

        public static class Local {

            public static final LocalCacheProvider<FactionUserLocalCache> USERS = new LocalCacheProvider<>(
                    new FactionUserLocalCache()
            );

            public static final LocalCacheProvider<FactionsBlockLocalCache> BLOCKS = new LocalCacheProvider<>(
                    new FactionsBlockLocalCache()
            );
        }
    }
}