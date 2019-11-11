package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import java.util.Collection;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.error.ResultAdapterFailureException;
import com.mmnaseri.utils.spring.data.tools.CollectionInstanceUtils;

/**
 * <p>This adapter will adapt results from an iterable to a stream. It will accept
 * adaptations if the original value is an iterable object.</p>
 *
 * <p>This adapter will execute at priority {@literal -300}.</p>
 */
public class StreamIterableResultAdapter extends AbstractIterableResultAdapter<Stream> {

    public StreamIterableResultAdapter() {
        super(-300);
    }

    @Override
    protected Stream doAdapt(Invocation invocation, Iterable iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        return originalValue != null && originalValue instanceof Iterable && Stream.class.isAssignableFrom(invocation.getMethod().getReturnType());
    }
}
