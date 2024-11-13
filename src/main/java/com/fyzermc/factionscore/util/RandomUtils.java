package com.fyzermc.factionscore.util;

import java.util.Random;

public class RandomUtils {

    public static final Random RANDOM = new Random();

    public static int randomInt(int start, int end) {
        return RANDOM.nextInt(Math.abs(end - start) + 1) + Math.min(start, end);
    }
}