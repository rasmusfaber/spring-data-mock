package com.mmnaseri.utils.spring.data.sample.repositories;

import com.mmnaseri.utils.spring.data.sample.models.Person;

import java.util.List;
import java.util.Optional;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/12/16, 6:47 PM)
 */
public interface SimpleCrudPersonRepository extends SimplePersonRepository {

    Person save(Person person);

    void delete(Person person);

    List<Person> findAll();

    List<Person> deleteAll();

    List<Person> findByLastName(String lastName);

}
