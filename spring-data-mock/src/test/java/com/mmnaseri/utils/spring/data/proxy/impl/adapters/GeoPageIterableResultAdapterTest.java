package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ReturnTypeSampleRepository;

import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoPage;
import org.springframework.data.geo.GeoResult;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/5/15)
 */
public class GeoPageIterableResultAdapterTest {

    @Test
    public void testAdapting() throws Exception {
        final GeoPageIterableResultAdapter adapter = new GeoPageIterableResultAdapter();
        final GeoResult[] geoResults = {new GeoResult<>(1, new Distance(0)), new GeoResult<>(2, new Distance(1)), new GeoResult<>(3, new Distance(1)), new GeoResult<>(4, new Distance(2))};
        final GeoPage<?> value = adapter.adapt(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findGeoPage"), null), Arrays.asList(geoResults));
        assertThat(value, is(notNullValue()));
        assertThat(value.getAverageDistance(), is(new Distance(1)));
        assertThat(value.getNumber(), is(0));
        assertThat(value.getNumberOfElements(), is(4));
        assertThat(value.getSize(), is(0));
        assertThat(value.getTotalElements(), is(4L));
        assertThat(value.getTotalPages(), is(1));
        assertThat(value.getSort(), is(Sort.unsorted()));
        assertThat(value.getContent(), hasSize(4));
        assertThat(value.getContent(), containsInAnyOrder(geoResults));
    }

    @Test
    public void testAccepting() throws Exception {
        final GeoPageIterableResultAdapter adapter = new GeoPageIterableResultAdapter();
        assertThat(adapter.accepts(null, null), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findOther"), new Object[]{}), new Object()), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findGeoPage"), new Object[]{}), new Object()), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findGeoPage"), new Object[]{}), new ArrayList<>()), is(true));
    }

}
