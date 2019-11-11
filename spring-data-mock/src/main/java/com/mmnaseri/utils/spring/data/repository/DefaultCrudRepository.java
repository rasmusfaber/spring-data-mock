package com.mmnaseri.utils.spring.data.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mmnaseri.utils.spring.data.error.EntityMissingKeyException;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;

/**
 * <p>
 * This class will provide implementations for the methods introduced by the
 * Spring framework through
 * {@link org.springframework.data.repository.CrudRepository}.</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/6/15)
 */
@SuppressWarnings({"unchecked", "WeakerAccess"})
public class DefaultCrudRepository extends CrudRepositorySupport {

    private static final Log log = LogFactory.getLog(DefaultCrudRepository.class);

    /**
     * Saves all the given entities
     *
     * @param entities entities to save (insert or update)
     * @return saved entities
     */
    public Iterable<Object> save(Iterable entities) {
        final List<Object> list = new LinkedList<>();
        log.info("Going to save a number of entities in the underlying data store");
        log.debug(entities);
        for (Object entity : entities) {
            list.add(save(entity));
        }
        return list;
    }

    /**
     * Finds the entity that was saved with this key, or returns {@literal null}
     *
     * @param key the key
     * @return the entity
     */
    public Object findOne(Object key) {
        log.info("Attempting to load the entity with key " + key);
        return getDataStore().retrieve(key);
    }

    /**
     * Checks whether the given key represents an entity in the data store
     *
     * @param key the key
     * @return {@literal true} if the key is valid
     */
    public boolean exists(Object key) {
        return getDataStore().hasKey(key);
    }

    /**
     * Finds all the entities that match the given set of ids
     *
     * @param ids ids to look for
     * @return entities that matched the ids.
     */
    public Iterable findAll(Iterable ids) {
        final List entities = new LinkedList();
        log.info("Looking for multiple entities for a number of ids");
        log.debug(ids);
        for (Object id : ids) {
            final Object found = findOne(id);
            if (found != null) {
                log.trace("Entity found for key " + id + ", adding the found entity to the list of returned entity");
                entities.add(found);
            }
        }
        return entities;
    }

    public Object delete(Object entity) {
        Object id = PropertyUtils.getPropertyValue(entity, getRepositoryMetadata().getIdentifierProperty());
        if (id == null) {
            log.error("The entity that was supposed to be deleted, does not have a key");
            throw new EntityMissingKeyException(getRepositoryMetadata().getEntityType(), getRepositoryMetadata().getIdentifierProperty());
        }
        return deleteById(id);
    }

    /**
     * Deletes the entity with the given id and returns the actual entity that
     * was just deleted.
     *
     * @param id the id
     * @return the entity that was deleted or {@literal null} if it wasn't found
     */
    public Object deleteById(Object id) {
        Object retrieved = getDataStore().retrieve(id);
        log.info("Attempting to delete the entity with key " + id);
        getDataStore().delete(id);
        return retrieved;
    }

    /**
     * Deletes all specified <em>entities</em> from the data store.
     *
     * @param entities the entities to delete
     * @return the entities that were actually deleted
     */
    public Iterable delete(Iterable entities) {
        log.info("Attempting to delete multiple entities via entity objects themselves");
        log.debug(entities);
        List deletedEntities = new ArrayList();
        for (Object entity : entities) {
            Object deleted = delete(entity);
            if (deleted != null) {
                deletedEntities.add(deleted);
            }
        }
        return deletedEntities;
    }

    /**
     * Deletes everything from the data store
     *
     * @return all the entities that were removed
     */
    public Iterable deleteAll() {
        log.info("Attempting to delete all entities at once");
        final Collection keys = getDataStore().keys();
        List deletedEntities = new ArrayList();
        log.debug("There are " + keys.size() + " entities altogether in the data store that are going to be deleted");
        for (Object key : keys) {
            Object deleted = deleteById(key);
            if (deleted != null) {
                deletedEntities.add(deleted);
            }
        }
        return deletedEntities;
    }

}
