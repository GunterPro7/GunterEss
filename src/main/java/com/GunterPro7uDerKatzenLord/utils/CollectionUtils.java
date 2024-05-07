package com.GunterPro7uDerKatzenLord.utils;

import java.util.*;
import java.util.function.Supplier;

public class CollectionUtils {

    public static <T> List<T> listOf(T... params) {
        return new ArrayList<>(Arrays.asList(params));
    }

    public static <T> List<T> listOf(Supplier<T> supplier, int times) {
        List<T> list = new ArrayList<>();

        for (int i = 0; i < times; i++) {
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

    public static <K, V> HashMap<K, V> createMap(Class<K> keyClass, Class<V> valueClass, Object... elements) {
        if (elements.length % 2 != 0) {
            throw new IllegalArgumentException("An even number of elements is required to create a key-value map.");
        }

        final HashMap<K, V> map = new HashMap<>();
        for (int i = 0; i < elements.length; i += 2) {
            if (keyClass.isInstance(elements[i]) && valueClass.isInstance(elements[i + 1])) {
                K key = keyClass.cast(elements[i]);
                V value = valueClass.cast(elements[i + 1]);
                map.put(key, value);
            } else {
                throw new IllegalArgumentException("Invalid key-value pair at index " + i);
            }
        }

        return map;
    }
}
