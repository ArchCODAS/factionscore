package com.fyzermc.factionscore.misc.tax;

import com.fyzermc.factionscore.FactionsCorePlugin;
import com.google.common.util.concurrent.AtomicDouble;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.DoubleConsumer;

@SuppressWarnings("deprecation")
public class TaxUtil {

    public static void tax(Faction faction, DoubleConsumer doubleConsumer) {
        AtomicDouble total = new AtomicDouble(0);

        List<CompletableFuture<Void>> futures = new LinkedList<>();

        for (MPlayer factionMPlayer : faction.getMPlayers()) {
            String name = factionMPlayer.getName();

            futures.add(CompletableFuture.supplyAsync(() -> FactionsCorePlugin.INSTANCE.getEconomy().getBalance(name))
                    .thenAccept(account -> {

                        double percent = (account * 25.0) / 100.0;

                        FactionsCorePlugin.INSTANCE.getEconomy().withdrawPlayer(name, percent);

                        total.getAndAdd(percent);
                    })
            );
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenAccept($ -> doubleConsumer.accept(total.get()));
    }
}