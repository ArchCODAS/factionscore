package com.fyzermc.factionscore;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.fantasy.combatlog.manager.CombatManager;
import com.fyzermc.factionscore.command.*;
import com.fyzermc.factionscore.command.BackCommand;
import com.fyzermc.factionscore.command.tell.ReplyCommand;
import com.fyzermc.factionscore.command.tell.TellCommand;
import com.fyzermc.factionscore.command.tpa.commands.TPACancelCommand;
import com.fyzermc.factionscore.command.tpa.commands.TPACommand;
import com.fyzermc.factionscore.command.tpa.commands.TPADenyCommand;
import com.fyzermc.factionscore.command.tpa.commands.TPAcceptCommand;
import com.fyzermc.factionscore.command.vanish.VanishCommand;
import com.fyzermc.factionscore.command.vanish.listener.VanishListeners;
import com.fyzermc.factionscore.command.visibility.HideCommand;
import com.fyzermc.factionscore.command.visibility.ShowCommand;
import com.fyzermc.factionscore.listener.*;
import com.fyzermc.factionscore.listener.packet.TileEntityPacketListener;
import com.fyzermc.factionscore.misc.altar.commands.RemoveAltarsCommand;
import com.fyzermc.factionscore.misc.altar.controller.AltarController;
import com.fyzermc.factionscore.misc.altar.entity.AltarEntity;
import com.fyzermc.factionscore.misc.blocks.listener.FactionsBlocksListeners;
import com.fyzermc.factionscore.misc.blocks.task.CleanInMemoryBlocksTask;
import com.fyzermc.factionscore.misc.chat.command.ChatCommand;
import com.fyzermc.factionscore.misc.chat.command.GlobalChatCommand;
import com.fyzermc.factionscore.misc.chat.listener.ChatListener;
import com.fyzermc.factionscore.misc.clearlag.ClearLagRunnable;
import com.fyzermc.factionscore.misc.customitem.CustomItemRegistry;
import com.fyzermc.factionscore.misc.customitem.command.GiveCustomItemCommand;
import com.fyzermc.factionscore.misc.customitem.data.*;
import com.fyzermc.factionscore.misc.customitem.listeners.CustomItemListener;
import com.fyzermc.factionscore.misc.customitem.listeners.EntityListeners;
import com.fyzermc.factionscore.misc.genbucket.customitem.GenBucketItem;
import com.fyzermc.factionscore.misc.genbucket.listeners.GenBucketListeners;
import com.fyzermc.factionscore.misc.genbucket.task.GenerationTask;
import com.fyzermc.factionscore.misc.skill.*;
import com.fyzermc.factionscore.misc.tax.command.TaxCommand;
import com.fyzermc.factionscore.util.NMS;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_8_R3.EntityEnderCrystal;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class FactionsCorePlugin extends JavaPlugin {

    public static FactionsCorePlugin INSTANCE;

    public static GenerationTask GENERATION_TASK;

    private Economy economy;

    @Override
    public void onEnable() {
        super.onEnable();
        INSTANCE = this;

        if (!setupEconomy()) {
            this.getLogger().severe("");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        NMS.registerCustomEntity(AltarEntity.class, EntityEnderCrystal.class);

        for (World world : getServer().getWorlds()) {
            for (ArmorStand entitiesByClass : world.getEntitiesByClass(ArmorStand.class)) {
                entitiesByClass.remove();
            }
        }

        AltarController.scheduleSpawn();

        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskTimer(this, new CleanInMemoryBlocksTask(), 1200L, 1200L);
        scheduler.runTaskTimer(this, new ClearLagRunnable(), 4800L, 4800L);

        GENERATION_TASK = new GenerationTask();

        registerCommands();
        registerListeners();
        registerSkills();

        CustomItemRegistry.registerCustomItem(
                new BruteTouchItem(),
                new ChunkCleanerItem(),
                new DivineAppleCustomItem(),
                new DragonEggCustomItem(),
                new ExtraMemberItem(),
                new FilledDispenserItem(),
                new FilledImpulseDispenserItem(),
                new GenBucketItem(),
                new GodsEyeItem(),
                new ImpulseTntItem(),
                new InstantBoxItem(),
                new InstantPowerItem(),
                new LauncherItem(),
                new MasterLightningItem(),
                new ExtraPowerItem(),
                new ResetKdrItem(),
                new SuperCreeperEggItem(),
                new TrapItem()
        );
    }

    @Override
    public void onDisable() {
        super.onDisable();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.closeInventory();
        }

        CombatManager.COMBAT.clear();
    }

    private void registerCommands() {
        new ArenaCommand(this);
        BackCommand backCommand = new BackCommand(this);
        new ChatCommand(this);
        new GiveCustomItemCommand(this);
        new GlobalChatCommand(this);
        new GodCommand(this);
        new KickAllCommand(this);
        new FpsCommand(this);
        new HideCommand(this);
        new ReplyCommand(this);
        new ShowCommand(this);
        new SpawnCommand(this);
        new ShopCommand(this);
        new TaxCommand(this);
        new RemoveAltarsCommand(this);
        new TellCommand(this);
        new TPACancelCommand(this);
        new TPAcceptCommand(this);
        new TPACommand(this);
        new TPADenyCommand(this);
        new VanishCommand(this);
        new CompactCommand(this);

        getServer().getPluginManager().registerEvents(backCommand, this);
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new ChatListener(), this);
        pluginManager.registerEvents(new CustomItemListener(), this);
        pluginManager.registerEvents(new FactionsBlocksListeners(), this);
        pluginManager.registerEvents(new PlayerListeners(), this);
        pluginManager.registerEvents(new GenBucketListeners(), this);
        pluginManager.registerEvents(new ServerListeners(), this);
        pluginManager.registerEvents(new RedstoneListener(), this);
        pluginManager.registerEvents(new EntityListeners(), this);
        pluginManager.registerEvents(new VanishListeners(), this);

        ProtocolLibrary.getProtocolManager().addPacketListener(
                new TileEntityPacketListener(this, PacketType.Play.Server.TILE_ENTITY_DATA)
        );
    }

    private void registerSkills() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new AssassinSkill(), this);
        pluginManager.registerEvents(new SunnyDaySkill(), this);
        pluginManager.registerEvents(new PoisonPointSkill(), this);
        pluginManager.registerEvents(new StaticSkill(), this);
    }

    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> registeredServiceProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (registeredServiceProvider == null) {
            return false;
        }

        economy = registeredServiceProvider.getProvider();
        return economy != null;
    }

    public Economy getEconomy() {
        return economy;
    }
}