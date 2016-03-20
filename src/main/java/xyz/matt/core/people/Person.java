package xyz.matt.core.people;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class Person {

    private final UUID id;
    private final UUID casValue;
    private final String name;
    private final String favoriteColor;

    @JsonCreator
    public Person(
            @JsonProperty("id") UUID id,
            @JsonProperty("casValue") UUID casValue,
            @JsonProperty("name") String name,
            @JsonProperty("favoriteColor") String favoriteColor) {
        this.id = checkNotNull(id);
        this.casValue = checkNotNull(casValue);
        this.name = checkNotNull(name);
        this.favoriteColor = checkNotNull(favoriteColor);
    }

    @JsonIgnore
    public Person createWithNewCasValue() {
        return new Person(id, UUID.randomUUID(), name, favoriteColor);
    }

    @JsonProperty
    public UUID getId() {
        return id;
    }

    @JsonProperty
    public UUID getCasValue() {
        return casValue;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public String getFavoriteColor() {
        return favoriteColor;
    }
}
