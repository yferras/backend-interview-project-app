package com.ninjaone.backendinterviewproject.service.device;

import com.ninjaone.backendinterviewproject.common.exception.NoDataException;

import java.io.Serializable;

public interface IDeviceCacheService<K extends Serializable, V> {

    V get(K key) throws NoDataException;

    void put(K key, V value);

    void delete(K key);

    default void preload() {}

}
