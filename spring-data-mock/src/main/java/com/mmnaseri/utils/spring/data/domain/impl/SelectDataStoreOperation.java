package com.mmnaseri.utils.spring.data.domain.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.query.Page;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.query.Sort;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;

/**
 * This is a data store operation that will read values from the underlying data store and match them up against the
 * query description's different decision branches. Once all the values are loaded and filtered, it will then sort them
 * according to the sort instruction, and then paginate them if necessary.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/17/15)
 */
public class SelectDataStoreOperation<K, E> implements DataStoreOperation<List<E>, K, E> {

    private static final Log log = LogFactory.getLog(SelectDataStoreOperation.class);
    private final QueryDescriptor descriptor;

    public SelectDataStoreOperation(QueryDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public QueryDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public List<E> execute(DataStore<K, E> store, RepositoryConfiguration configuration, Invocation invocation) {
        log.info("Selecting the data according to the provided selection descriptor: " + descriptor);
        final List<E> selection = new LinkedList<>();
        final Collection<E> all = new LinkedList<>(store.retrieveAll());
        for (E entity : all) {
            if (descriptor.matches(entity, invocation)) {
                selection.add(entity);
            }
        }
        log.info("Matched " + selection.size() + " items from the data store");
        if (descriptor.isDistinct()) {
            final Set<E> distinctValues = new HashSet<>(selection);
            selection.clear();
            selection.addAll(distinctValues);
            log.info("After clearing up duplicates, " + selection.size() + " items remained");
        }
        final Sort sort = descriptor.getSort(invocation);
        final Page page = descriptor.getPage(invocation);
        if (sort != null) {
            log.info("Sorting the selected items according to the provided ordering");
            PropertyComparator.sort(selection, sort);
        }
        if (page != null) {
            log.info("We need to paginate the selection to fit the selection criteria");
            int start = page.getPageSize() * page.getPageNumber();
            int finish = Math.min(start + page.getPageSize(), selection.size());
            if (start > selection.size()) {
                selection.clear();
            } else {
                final List<E> view = new ArrayList<>();
                for (E item : selection.subList(start, finish)) {
                    view.add(item);
                }
                selection.clear();
                selection.addAll(view);
            }
        }
        if (descriptor.getLimit() > 0) {
            log.info("Going to limit the result to " + descriptor.getLimit() + " items");
            final List<E> view = new ArrayList<>();
            for (E item : selection.subList(0, Math.min(selection.size(), descriptor.getLimit()))) {
                view.add(item);
            }
            selection.clear();
            selection.addAll(view);
        }
        return selection;
    }

    @Override
    public String toString() {
        return descriptor.toString();
    }

}
