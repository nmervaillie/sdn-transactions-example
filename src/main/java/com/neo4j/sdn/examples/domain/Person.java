package com.neo4j.sdn.examples.domain;


import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Collection;

@NodeEntity
public class Person {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @Relationship(type = "LOCATED_AT")
    private Collection<Location> locations;

    public Person(String name, Collection<Location> locations) {
        this.name = name;
        this.locations = locations;
    }

    public String getName() {
        return name;
    }
}