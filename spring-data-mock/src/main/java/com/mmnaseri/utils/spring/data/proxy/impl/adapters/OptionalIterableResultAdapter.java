package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.mmnaseri.utils.spring.data.domain.Invocation;

/**
 * <p>This class will adapt results from an iterable object to an optional.</p>
 *
 * <p>It will accept adaptations wherein the original value is some sort of iterable and the required return type
 * is an instance of {@link Optional}. Remember that it does <em>not</em> check for individual object type
 * compatibility.</p>
 *
 * <p>This adapter will execute at priority {@literal -100}.</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/28/15)
 */
public class OptionalIterableResultAdapter extends AbstractIterableResultAdapter<Optional> {

    public OptionalIterableResultAdapter() {
        super(-100);
    }

    @Override
    protected Optional doAdapt(Invocation invocation, final Iterable iterable) {
        final Iterator iterator = iterable.iterator();
        final Object value = iterator.next();
        return Optional.ofNullable(value);
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        return originalValue != null && originalValue instanceof Iterable && invocation.getMethod().getReturnType().equals(Optional.class);
    }

}
