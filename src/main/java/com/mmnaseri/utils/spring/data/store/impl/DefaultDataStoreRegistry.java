package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.error.DataStoreNotFoundException;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreRegistry;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class DefaultDataStoreRegistry implements DataStoreRegistry {

    private final Map<Class<?>, DataStore<?, ?>> dataStores = new ConcurrentHashMap<Class<?>, DataStore<?, ?>>();

    @Override
    public <E, K extends Serializable> void register(DataStore<K, E> dataStore) {
        dataStores.put(dataStore.getEntityType(), dataStore);
    }

    @Override
    public <E, K extends Serializable> DataStore<K, E> getDataStore(Class<E> entityType) {
        if (!dataStores.containsKey(entityType)) {
            throw new DataStoreNotFoundException(entityType);
        }
        //noinspection unchecked
        return (DataStore<K, E>) dataStores.get(entityType);
    }

    @Override
    public boolean has(Class<?> entityType) {
        return dataStores.containsKey(entityType);
    }

}