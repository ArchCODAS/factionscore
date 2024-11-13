package com.fyzermc.factionscore;

import com.fyzermc.factionscore.util.location.SerializedLocation;
import com.fyzermc.factionscore.util.location.unserializer.BukkitLocationParser;

public class FactionsCoreConstants {

        public static final BukkitLocationParser LOCATION_PARSER = new BukkitLocationParser();

        public static final String METADATA_FALL_BYPASS_KEY = "fall-bypass";

        public static final SerializedLocation SPAWN = new SerializedLocation(
                0.500,
                150.0,
                0.500,
                90.0F,
                0.0F
        );

        public static final SerializedLocation SHOP = new SerializedLocation(
                -33.500,
                150.0,
                0.500,
                90.0F,
                0.0F
        );

        public static final SerializedLocation ARENA = new SerializedLocation(
                0.500,
                148.0,
                -33.500,
                90.0F,
                0.0F
        );
}