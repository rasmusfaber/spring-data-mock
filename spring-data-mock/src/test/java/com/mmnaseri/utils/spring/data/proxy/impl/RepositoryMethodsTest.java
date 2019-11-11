package com.mmnaseri.utils.spring.data.proxy.impl;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.springframework.data.repository.Repository;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.mmnaseri.utils.spring.data.domain.impl.DefaultOperatorContext;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultRepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.MethodQueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.domain.impl.key.NoOpKeyGenerator;
import com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactory;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.query.impl.DefaultDataFunctionRegistry;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import com.mmnaseri.utils.spring.data.sample.repositories.ClearableSimpleCrudPersonRepository;
import com.mmnaseri.utils.spring.data.sample.repositories.RepositoryClearerMapping;
import com.mmnaseri.utils.spring.data.sample.repositories.SimpleCrudPersonRepository;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreEventListenerContext;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreRegistry;

public class RepositoryMethodsTest {
    private TestRepository repository;

    @BeforeMethod
    public void setUp() {
        repository = RepositoryFactoryBuilder.builder().mock(TestRepository.class);
    }

    @Test
    public void findWithOptional() {
        Person person = new Person();
        person.setId("1");
        person.setFirstName("foo");
        repository.save(person);
        Optional<Person> res = repository.findByFirstName("foo");
        assertThat(res.isPresent(), is(true));
    }

    @Test
    public void findWithStream() {
        Person person = new Person();
        person.setId("1");
        person.setFirstName("foo");
        repository.save(person);
        Stream<Person> res = repository.findAllByFirstName("foo");
        assertThat(res.collect(Collectors.toList()), hasSize(1));
    }

    public interface TestRepository extends Repository<Person, String> {
        void save(Person person);
        Optional<Person> findByFirstName(String firstName);
        Stream<Person> findAllByFirstName(String firstName);
    }
}
