package com.mmnaseri.utils.spring.data.repository;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableRepositoryMetadata;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import com.mmnaseri.utils.spring.data.sample.repositories.SimplePersonRepository;
import com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore;
import org.springframework.data.gemfire.repository.Wrapper;
import org.testng.annotations.Test;

import java.io.Serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/11/16, 1:13 PM)
 */
public class DefaultGemfireRepositoryTest {

    @Test
    public void testSave() throws Exception {
        final MemoryDataStore<Object, Person> dataStore = new MemoryDataStore<>(Person.class);
        final ImmutableRepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, Person.class, SimplePersonRepository.class, "id");
        final DefaultGemfireRepository repository = new DefaultGemfireRepository();
        repository.setDataStore(dataStore);
        repository.setRepositoryMetadata(repositoryMetadata);
        final String id = "12345";
        final Wrapper<?, ?> wrapper = new Wrapper<>(new Person(), id);
        //noinspection unchecked
        final Object saved = repository.save((Wrapper<Object, Object>) wrapper);
        assertThat(saved, is(notNullValue()));
        assertThat(saved, is(instanceOf(Person.class)));
        assertThat(((Person) saved).getId(), is(notNullValue()));
        assertThat(((Person) saved).getId(), is(id));
    }

}
