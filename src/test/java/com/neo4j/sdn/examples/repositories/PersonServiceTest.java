package com.neo4j.sdn.examples.repositories;


import com.neo4j.sdn.examples.domain.Location;
import com.neo4j.sdn.examples.domain.Person;
import com.neo4j.sdn.examples.services.PersonService;
import org.apache.commons.collections4.ListUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonServiceTest {

    private static final int NB_PERSONS = 2000;
    private static final int NB_LOCATIONS_PER_PERSON = 200;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Before
    public void setUp() {
        personRepository.deleteAll();
        locationRepository.deleteAll();
    }

    @Test
    public void import_person() {

        Collection<Location> locations = Collections.singleton(new Location(123, 456));
        personService.createPersons(Collections.singleton(new Person("nicolas", locations)));

        assertThat(personRepository.count()).isEqualTo(1);
        assertThat(locationRepository.count()).isEqualTo(1);
    }

    // This test takes a lot of time to execute, because there is significant overhead in SDN, managing many
    // relationships. Anyway, in general, holding that many objects and sending them all at one is something
    // one should avoid, because it ends up with a big and long transaction on the database side,
    // and a lot of memory usage on the app side.
    @Test
    public void import_many_persons_and_locations_at_once() {

        List<Person> persons = testPersons();
        personService.createPersons(persons);

        assertThat(personRepository.count()).isEqualTo(NB_PERSONS);
        assertThat(locationRepository.count()).isEqualTo(NB_LOCATIONS_PER_PERSON * NB_PERSONS);
    }

    // This one is faster than the previous one, because saves are executed in separate tx.
    // The session is cleared after every tx and does not get too big
    // it is important to make sure that the loop stays outside the transaction here
    @Test
    public void import_many_persons_and_locations_split() {

        List<Person> persons = testPersons();
        ListUtils.partition(persons, NB_PERSONS / 100).forEach(part -> personService.createPersons(part));

        assertThat(personRepository.count()).isEqualTo(NB_PERSONS);
        assertThat(locationRepository.count()).isEqualTo(NB_LOCATIONS_PER_PERSON * NB_PERSONS);
    }

    // even faster with parallel version
    @Test
    public void parallel_import_many_persons_and_locations_split() {

        List<Person> persons = testPersons();

        // this part would best live in a (non transactional) service
        // and use an executor service for more control.
        ListUtils.partition(persons, NB_PERSONS / 100).parallelStream().forEach(part -> personService.createPersons(part));

        assertThat(personRepository.count()).isEqualTo(NB_PERSONS);
        assertThat(locationRepository.count()).isEqualTo(NB_LOCATIONS_PER_PERSON * NB_PERSONS);
    }

    private List<Person> testPersons() {
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < NB_PERSONS; i++) {
            Collection<Location> locations = new ArrayList<>();
            for (int j = 0; j < NB_LOCATIONS_PER_PERSON; j++) {
                locations.add(new Location(123, 456));
            }
            persons.add(new Person("person " + i, locations));
        }
        return persons;
    }
}