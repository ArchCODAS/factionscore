package com.fyzermc.factionscore.misc.altar.entity;

import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.misc.altar.controller.AltarController;
import com.fyzermc.factionscore.misc.customitem.CustomItem;
import com.fyzermc.factionscore.misc.customitem.CustomItemRegistry;
import com.fyzermc.factionscore.misc.customitem.data.DragonEggCustomItem;
import com.fyzermc.factionscore.util.NumberUtils;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class AltarEntity extends EntityEnderCrystal {

    public AltarEntity(World world) {
        super(world);

        this.b = 75000;

        this.setCustomNameVisible(true);

        this.updateDisplayName();
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.dead) {
            return false;
        }

        this.b -= (int) f;

        if (this.b <= 0) {
            this.die();

            AltarController.scheduleSpawn();

            Entity entity = damagesource.getEntity();
            if (entity instanceof EntityPlayer) {
                CraftEntity craftEntity = entity.getBukkitEntity();

                Player player = (Player) craftEntity;

                CustomItem item = CustomItemRegistry.getItem(DragonEggCustomItem.KEY);

                ItemStack itemStack = item.asItemStack();

                player.getInventory().addItem(itemStack);
            }

            return false;
        }

        this.updateDisplayName();

        return true;
    }

    @Override
    public void die() {
        super.die();

        AltarController.SPAWNED = false;

        this.getBukkitEntity().getLocation().getWorld().getNearbyEntities(this.getBukkitEntity().getLocation(), 3.0, 3.0, 3.0).forEach(entity -> {
            if (entity.getType() == EntityType.ARMOR_STAND) {
                entity.remove();
            }
        });

        announce();

        playAnimations();
    }

    private void updateDisplayName() {
        this.setCustomName("§f" + NumberUtils.format(this.b) + " §c§l❤");
    }

    private void playAnimations() {
        CraftEntity bukkitEntity = this.getBukkitEntity();

        Location location = bukkitEntity.getLocation();

        Location spawnLocation = location.clone().add(0.0D, 3.5D, 0.0D);

        for (int i = 0; i < 4; ++i) {
            Firework firework = (Firework) location.getWorld().spawnEntity(spawnLocation.add(0.0D, 1.0D, 0.0D), EntityType.FIREWORK);
            FireworkMeta fireworkMeta = firework.getFireworkMeta();
            fireworkMeta.setPower(3);
            fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.AQUA).withColor(Color.BLACK).flicker(true).build());
            firework.setFireworkMeta(fireworkMeta);
            Bukkit.getScheduler().runTaskLater(FactionsCorePlugin.INSTANCE, firework::detonate, 8L);
        }
    }

    private void announce() {
        CraftServer server = this.world.getServer();
        server.broadcastMessage("");
        server.broadcastMessage("§6§l[ARENA]§f O altar foi derrotado!");
        server.broadcastMessage("");

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENDERMAN_SCREAM, 6.0F, -6.0F);
        }
    }
}