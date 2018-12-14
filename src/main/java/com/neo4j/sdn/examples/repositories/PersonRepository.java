package com.neo4j.sdn.examples.repositories;

import com.neo4j.sdn.examples.domain.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

}