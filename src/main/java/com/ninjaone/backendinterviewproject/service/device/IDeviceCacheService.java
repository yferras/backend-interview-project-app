package com.ninjaone.backendinterviewproject.service.device;

import java.io.Serializable;

public interface IDeviceCacheService<K extends Serializable, V> {

    V get(K key);

    void put(K key, V value);

    void delete(K key);

    default void preload() {}

}
