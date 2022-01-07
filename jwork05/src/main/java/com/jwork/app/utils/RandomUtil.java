package com.jwork.app.utils;

import java.util.HashSet;
import java.util.Set;

public class RandomUtil {

    private static final Set<Integer> set = new HashSet<>();

    public static Integer getRandom(int min, int max) {
        Integer num = new java.util.Random().nextInt(max - min) + min;
        boolean b = true;

        while (b) {
            if (set.contains(num)) {
                num = new java.util.Random().nextInt(max - min) + min;
            } else {
                set.add(num);
                return num;
            }
            if (set.size() == max - min) {
                b = false;
                num = null;
                return num;
            }
        }
        return num;
    }

    public static void clearSet() {
        set.clear();
    }
}