package com.mmnaseri.utils.spring.data.sample.mocks;

import java.io.Serializable;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/10/16)
 */
public class OperationRequest<K, E> {

    private final Long timestamp;
    private final Operation operation;
    private final K key;
    private final E entity;

    public OperationRequest(Long timestamp, Operation operation, K key, E entity) {
        this.timestamp = timestamp;
        this.operation = operation;
        this.key = key;
        this.entity = entity;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Operation getOperation() {
        return operation;
    }

    public K getKey() {
        return key;
    }

    public E getEntity() {
        return entity;
    }
}
