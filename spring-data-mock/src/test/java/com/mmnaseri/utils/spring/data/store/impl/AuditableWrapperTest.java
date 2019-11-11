package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableRepositoryMetadata;
import com.mmnaseri.utils.spring.data.sample.models.*;
import com.mmnaseri.utils.spring.data.sample.usecases.store.SampleAuditorAware;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.springframework.data.domain.AuditorAware;
import org.springframework.util.ReflectionUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/9/16)
 */
@SuppressWarnings("WeakerAccess, unused")
public class AuditableWrapperTest {

    private AuditorAware<?> auditorAware;

    @BeforeMethod
    public void setUp() throws Exception {
        auditorAware = new SampleAuditorAware();
    }

    @Test
    public void testCreatedBy() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithCreatedBy.class, null, "id");
        final EntityWithCreatedBy entity = new EntityWithCreatedBy();
        final AuditableWrapper<Object, Object, Instant> wrapper = new AuditableWrapper<>(entity, repositoryMetadata);
        assertThat(entity.getCreatedBy(), is(nullValue()));
        assertThat(wrapper.getCreatedBy(), is(Optional.empty()));
        wrapper.setCreatedBy(auditorAware.getCurrentAuditor());
        assertThat(wrapper.getCreatedBy().get(), Matchers.is(SampleAuditorAware.AUDITOR));
        assertThat(entity.getCreatedBy(), is(SampleAuditorAware.AUDITOR));
    }

    @Test
    public void testLastModifiedBy() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithLastModifiedBy.class, null, "id");
        final EntityWithLastModifiedBy entity = new EntityWithLastModifiedBy();
        final AuditableWrapper<Object, Object, Instant> wrapper = new AuditableWrapper<>(entity, repositoryMetadata);
        assertThat(entity.getLastModifiedBy(), is(nullValue()));
        assertThat(wrapper.getLastModifiedBy(), is(Optional.empty()));
        wrapper.setLastModifiedBy(auditorAware.getCurrentAuditor());
        assertThat(wrapper.getLastModifiedBy().get(), Matchers.is(SampleAuditorAware.AUDITOR));
        assertThat(entity.getLastModifiedBy(), is(SampleAuditorAware.AUDITOR));
    }

    @Test
    public void testCreatedDate() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithCreatedDate.class, null, "id");
        final EntityWithCreatedDate entity = new EntityWithCreatedDate();
        final AuditableWrapper<Object, Object, Instant> wrapper = new AuditableWrapper<>(entity, repositoryMetadata);
        assertThat(entity.getCreatedDate(), is(nullValue()));
        assertThat(wrapper.getCreatedDate(), is(Optional.empty()));
        final Instant time = Instant.now();
        wrapper.setCreatedDate(time);
        assertThat(wrapper.getCreatedDate().get().toEpochMilli(), is(time.toEpochMilli()));
        assertThat(entity.getCreatedDate(), is(new java.util.Date(time.toEpochMilli())));
    }

    @Test
    public void testUtilDateLastModifiedDate() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithUtilDateLastModifiedDate.class, null, "id");
        final EntityWithUtilDateLastModifiedDate entity = new EntityWithUtilDateLastModifiedDate();
        final AuditableWrapper<Object, Object, Instant> wrapper = new AuditableWrapper<>(entity, repositoryMetadata);
        final Instant time = Instant.now();
        assertThat(entity.getLastModifiedDate(), is(nullValue()));
        assertThat(wrapper.getLastModifiedDate(), is(Optional.empty()));
        wrapper.setLastModifiedDate(time);
        assertThat(wrapper.getLastModifiedDate().get().toEpochMilli(), is(time.toEpochMilli()));
        assertThat(entity.getLastModifiedDate(), is(new java.util.Date(time.toEpochMilli())));
    }

    @Test
    public void testSqlDateLastModifiedDate() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithSqlDateLastModifiedDate.class, null, "id");
        final EntityWithSqlDateLastModifiedDate entity = new EntityWithSqlDateLastModifiedDate();
        final AuditableWrapper<Object, Object, Instant> wrapper = new AuditableWrapper<>(entity, repositoryMetadata);
        final Instant time = Instant.now();
        assertThat(entity.getLastModifiedDate(), is(nullValue()));
        assertThat(wrapper.getLastModifiedDate(), is(Optional.empty()));
        wrapper.setLastModifiedDate(time);
        assertThat(wrapper.getLastModifiedDate().get().toEpochMilli(), is(time.toEpochMilli()));
        assertThat(entity.getLastModifiedDate(), is(new java.sql.Date(time.toEpochMilli())));
    }

    @Test
    public void testTimestampLastModifiedDate() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithTimestampDateLastModifiedDate.class, null, "id");
        final EntityWithTimestampDateLastModifiedDate entity = new EntityWithTimestampDateLastModifiedDate();
        final AuditableWrapper<Object, Object, Instant> wrapper = new AuditableWrapper<>(entity, repositoryMetadata);
        final Instant time = Instant.now();
        assertThat(entity.getLastModifiedDate(), is(nullValue()));
        assertThat(wrapper.getLastModifiedDate(), is(Optional.empty()));
        wrapper.setLastModifiedDate(time);
        assertThat(wrapper.getLastModifiedDate().get().toEpochMilli(), is(time.toEpochMilli()));
        assertThat(entity.getLastModifiedDate(), is(new Timestamp(time.toEpochMilli())));
    }

    @Test
    public void testTimeLastModifiedDate() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithTimeLastModifiedDate.class, null, "id");
        final EntityWithTimeLastModifiedDate entity = new EntityWithTimeLastModifiedDate();
        final AuditableWrapper<Object, Object, Instant> wrapper = new AuditableWrapper<>(entity, repositoryMetadata);
        final Instant time = Instant.now();
        assertThat(entity.getLastModifiedDate(), is(nullValue()));
        assertThat(wrapper.getLastModifiedDate(), is(Optional.empty()));
        wrapper.setLastModifiedDate(time);
        assertThat(wrapper.getLastModifiedDate().get().toEpochMilli(), is(time.toEpochMilli()));
        assertThat(entity.getLastModifiedDate(), is(new Time(time.toEpochMilli())));
    }

    @Test
    public void testDateTimeLastModifiedDate() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithDateTimeLastModifiedDate.class, null, "id");
        final EntityWithDateTimeLastModifiedDate entity = new EntityWithDateTimeLastModifiedDate();
        final AuditableWrapper<Object, Object, Instant> wrapper = new AuditableWrapper<>(entity, repositoryMetadata);
        final Instant time = Instant.now();
        assertThat(entity.getLastModifiedDate(), is(nullValue()));
        assertThat(wrapper.getLastModifiedDate(), is(Optional.empty()));
        wrapper.setLastModifiedDate(time);
        assertThat(wrapper.getLastModifiedDate().get().toEpochMilli(), is(time.toEpochMilli()));
        assertThat(entity.getLastModifiedDate().getMillis(), is(time.toEpochMilli()));
    }

    @Test
    public void testSettingNullValue() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithCreatedBy.class, null, "id");
        final EntityWithCreatedBy entity = new EntityWithCreatedBy();
        final AuditableWrapper<Object, Object, Instant> wrapper = new AuditableWrapper<>(entity, repositoryMetadata);
        assertThat(entity.getCreatedBy(), is(nullValue()));
        wrapper.setCreatedBy(null);
        assertThat(entity.getCreatedBy(), is(nullValue()));
    }

    @Test
    public void testGettingIdentifier() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithCreatedBy.class, null, "id");
        final EntityWithCreatedBy entity = new EntityWithCreatedBy();
        final AuditableWrapper<Object, Object, Instant> wrapper = new AuditableWrapper<>(entity, repositoryMetadata);
        assertThat(wrapper.getId(), is(nullValue()));
        entity.setId("some identifier");
        assertThat(wrapper.getId(), is(notNullValue()));
        assertThat(wrapper.getId(), Matchers.is(entity.getId()));
    }

    @Test
    public void testDirtyChecking() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithCreatedBy.class, null, "id");
        final EntityWithCreatedBy entity = new EntityWithCreatedBy();
        final AuditableWrapper<Object, Object, Instant> wrapper = new AuditableWrapper<>(entity, repositoryMetadata);
        assertThat(wrapper.isNew(), is(true));
        entity.setId("some identifier");
        assertThat(wrapper.isNew(), is(false));
    }

    @Test
    public void testSettingReadOnlyProperty() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithFinalCreatedBy.class, null, "id");
        final EntityWithFinalCreatedBy entity = new EntityWithFinalCreatedBy();
        final AuditableWrapper<Object, Object, Instant> wrapper = new AuditableWrapper<>(entity, repositoryMetadata);
        assertThat(entity.getCreatedBy(), is(nullValue()));
        assertThat(wrapper.getCreatedBy(), is(Optional.empty()));
        wrapper.setCreatedBy(auditorAware.getCurrentAuditor());
        assertThat(wrapper.getCreatedBy(), is(Optional.empty()));
        assertThat(entity.getCreatedBy(), is(nullValue()));
    }

    @Test
    public void testWriteOnlyCreatedBy() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithWriteOnlyCreatedBy.class, null, "id");
        final EntityWithWriteOnlyCreatedBy entity = new EntityWithWriteOnlyCreatedBy();
        final AuditableWrapper<Object, Object, Instant> wrapper = new AuditableWrapper<>(entity, repositoryMetadata);
        final Field createdBy = ReflectionUtils.findField(EntityWithWriteOnlyCreatedBy.class, "createdBy");
        createdBy.setAccessible(true);
        assertThat(createdBy.get(entity), is(nullValue()));
        assertThat(wrapper.getCreatedBy(), is(Optional.empty()));
        wrapper.setCreatedBy(auditorAware.getCurrentAuditor());
        //the wrapper will not be able to read the value ...
        assertThat(wrapper.getCreatedBy(), is(Optional.empty()));
        //... but the value is set anyway
        assertThat(createdBy.get(entity), Matchers.is(SampleAuditorAware.AUDITOR));
    }

}
