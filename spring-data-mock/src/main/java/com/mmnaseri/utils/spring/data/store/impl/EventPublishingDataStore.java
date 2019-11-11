package com.mmnaseri.utils.spring.data.store.impl;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.error.CorruptDataException;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreEvent;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListenerContext;
import com.mmnaseri.utils.spring.data.store.DataStoreEventPublisher;

/**
 * This implementation relies on a delegate data store to handling the actual storage/retrieval. It decorates the
 * delegate with event triggering capabilities and some additional data integrity checks (null checking).
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/6/15)
 */
public class EventPublishingDataStore<K, E> implements DataStore<K, E>, DataStoreEventPublisher {

    private static final Log log = LogFactory.getLog(EventPublishingDataStore.class);
    private final DataStore<K, E> delegate;
    private final RepositoryMetadata repositoryMetadata;
    private final DataStoreEventListenerContext listenerContext;

    public EventPublishingDataStore(DataStore<K, E> delegate, RepositoryMetadata repositoryMetadata, DataStoreEventListenerContext listenerContext) {
        this.delegate = delegate;
        this.repositoryMetadata = repositoryMetadata;
        this.listenerContext = listenerContext;
    }

    @Override
    public boolean hasKey(K key) {
        return delegate.hasKey(key);
    }

    @Override
    public boolean save(K key, E entity) {
        if (key == null) {
            log.error("Cannot save an entity under a null key");
            throw new CorruptDataException(getEntityType(), null, "Cannot save an entity with a null key");
        }
        if (entity == null) {
            log.error("Cannot save a null value into the data store");
            throw new CorruptDataException(getEntityType(), null, "Cannot save null into the data store");
        }
        final boolean entityIsNew = !delegate.hasKey(key);
        if (entityIsNew) {
            log.info("About to insert a new entity in the data store under key " + key);
            publishEvent(new BeforeInsertDataStoreEvent(repositoryMetadata, this, entity));
        } else {
            log.info("About to update the entity in the data store under key " + key);
            publishEvent(new BeforeUpdateDataStoreEvent(repositoryMetadata, this, entity));
        }
        delegate.save(key, entity);
        if (entityIsNew) {
            log.info("Finished inserting the entity in the data store under key " + key);
            publishEvent(new AfterInsertDataStoreEvent(repositoryMetadata, this, entity));
            return true;
        } else {
            log.info("Finished updating the entity under key " + key);
            publishEvent(new AfterUpdateDataStoreEvent(repositoryMetadata, this, entity));
            return false;
        }
    }

    @Override
    public boolean delete(K key) {
        if (!delegate.hasKey(key)) {
            log.info("Attempted to delete entity with key " + key + " but found nothing");
            return false;
        }
        final E entity = delegate.retrieve(key);
        log.info("About to delete an entity with key " + key);
        publishEvent(new BeforeDeleteDataStoreEvent(repositoryMetadata, this, entity));
        delegate.delete(key);
        log.info("Finished deleting the entity with key " + key);
        publishEvent(new AfterDeleteDataStoreEvent(repositoryMetadata, this, entity));
        return true;
    }

    @Override
    public E retrieve(K key) {
        return delegate.retrieve(key);
    }

    @Override
    public Collection<K> keys() {
        return delegate.keys();
    }

    @Override
    public Collection<E> retrieveAll() {
        return delegate.retrieveAll();
    }

    @Override
    public Class<E> getEntityType() {
        return delegate.getEntityType();
    }

    @Override
    public void truncate() {
        delegate.truncate();
    }

    @Override
    public void publishEvent(DataStoreEvent event) {
         listenerContext.trigger(event);
    }

}
