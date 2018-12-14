package com.neo4j.sdn.examples.repositories;

import com.neo4j.sdn.examples.domain.Location;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface LocationRepository extends Neo4jRepository<Location, Long> {

}