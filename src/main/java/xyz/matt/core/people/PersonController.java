package xyz.matt.core.people;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

@RestController()
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = checkNotNull(personService);
    }

    @RequestMapping(value = "/people/{personId}", method = RequestMethod.PUT)
    public void putPerson(@RequestBody Person person, @PathVariable("personId") String urlPersonId) {
        final String personId = person.getId().toString();
        if (! personId.equals(urlPersonId)) {
            throw new IllegalArgumentException("Body and url id do not match.");
        }

        if (! personService.safeUpsert(person)) {
            throw new IllegalArgumentException("bad cas value");
        }
    }

    @RequestMapping(value = "/people/{personId}", method = RequestMethod.GET)
    public Person getPerson(@PathVariable("personId") String personId) {
        return personService.getPerson(UUID.fromString(personId)).get();
    }
}
