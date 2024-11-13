package com.fyzermc.factionscore.misc.blocks.task;

import com.fyzermc.factionscore.FactionsCoreProvider;

public class CleanInMemoryBlocksTask implements Runnable {

    @Override
    public void run() {
        FactionsCoreProvider.Cache.Local.BLOCKS.provide().clean();
    }
}