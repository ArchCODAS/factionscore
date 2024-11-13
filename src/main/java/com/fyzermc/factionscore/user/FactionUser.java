package com.fyzermc.factionscore.user;

import com.fyzermc.factionscore.util.location.SerializedLocation;
import com.massivecraft.factions.entity.MPlayer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;

@Getter
@EqualsAndHashCode(of = "userName")
public class FactionUser {

    private final String userName;

    private WeakReference<MPlayer> mPlayerWeakReference;

    private SerializedLocation backLocation;

    public FactionUser(String userName) {
        this.userName = userName;
    }

    public MPlayer getMPlayer() {
        if (mPlayerWeakReference == null) {
            Player player = Bukkit.getPlayer(this.userName);

            mPlayerWeakReference = new WeakReference<>(MPlayer.get(player));
        }

        return mPlayerWeakReference.get();
    }

    public SerializedLocation getBackLocation() {
        return backLocation;
    }

    public void setBackLocation(SerializedLocation backLocation) {
        this.backLocation = backLocation;
    }
}