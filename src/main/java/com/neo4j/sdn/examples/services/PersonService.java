package com.neo4j.sdn.examples.services;

import com.neo4j.sdn.examples.domain.Person;
import com.neo4j.sdn.examples.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class PersonService {

    private final static Logger LOG = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional
    public Iterable<Person> createPersons(Collection<Person> persons) {
        return personRepository.saveAll(persons);
    }

}
