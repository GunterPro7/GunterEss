package com.GunterPro7uDerKatzenLord.utils;

import java.util.*;
import java.util.function.Supplier;

public class CollectionUtils {

    public static <T> List<T> listOf(T... params) {
        return new ArrayList<>(Arrays.asList(params));
    }

    public static <T> List<T> listOf(Supplier<T> supplier, int times) {
        List<T> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            list.add(supplier.get());
        }

        return list;
    }

    public static <T, U> Map<T, U> mapOf(List<T> keys, List<U> values) {
        Map<T, U> map = new HashMap<>();

        for (int i = 0; i < keys.size(); i++) {
            map.put(keys.get(i), values.get(i));
        }

        return map;
    }

    public static <T, U> Map<T, U> mapOf(Object... params) {
        Map<T, U> map = new HashMap<>();

        for (int i = 0; i < params.length; i += 2) {
            map.put((T) params[i], (U) params[i + 1]);
        }

        return map;
    }
}
