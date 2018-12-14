package com.neo4j.sdn.examples.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Location {

    @Id
    @GeneratedValue
    private Long id;

    private long latitude;
    private long longitude;
    @Relationship(type = "LOCATED_AT", direction = Relationship.INCOMING)
    private Person person;

    public Location(long latitude, long longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
