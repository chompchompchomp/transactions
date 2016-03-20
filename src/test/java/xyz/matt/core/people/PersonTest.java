package xyz.matt.core.people;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by matt on 3/19/16.
 */
public class PersonTest {

    static final String JSON = "{\"id\":\"a2969535-a9f9-49e1-ab2a-eb7c592e5b45\",\"casValue\":\"cc3c202d-ed0c-460e-9139-4890f13ec481\",\"name\":\"matt\",\"favoriteColor\":\"green\"}";

    @Test
    public void testJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Person person = new Person(UUID.randomUUID(), UUID.randomUUID(), "matt", "green");
        System.out.println(objectMapper.writeValueAsString(person));
        objectMapper.readValue(JSON, Person.class);
    }
}