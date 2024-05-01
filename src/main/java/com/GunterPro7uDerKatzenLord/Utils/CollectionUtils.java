package com.GunterPro7uDerKatzenLord.Utils;

import java.util.*;

public class CollectionUtils {

    public static <T> List<T> listOf(T... params) {
        return new ArrayList<>(Arrays.asList(params));
    }

    public static <T, U> Map<T, U> mapOf(Object... params) {
        Map<T, U> map = new HashMap<>();

        for (int i = 0; i < params.length; i += 2) {
            map.put((T) params[i], (U) params[i + 1]);
        }

        return map;
    }
}
