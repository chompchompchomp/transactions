package xyz.matt.core.people;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

@Transactional(isolation = Isolation.DEFAULT)
@Service
public class PersonService {
    private static final Logger log = LoggerFactory.getLogger(PersonService.class);

    private final PersonJdbcDao personJdbcDao;

    @Autowired
    public PersonService(PersonJdbcDao personJdbcDao) {
        this.personJdbcDao = checkNotNull(personJdbcDao);
    }

    public Optional<Person> getPerson(UUID id) {
        return personJdbcDao.getPerson(id);
    }

    public boolean safeUpsert(Person personToUpdate) {
        final Optional<Person> optionalPersonFromDb = getPerson(personToUpdate.getId());
        if (optionalPersonFromDb.isPresent()) {
            final Person personFromDb = optionalPersonFromDb.get();
            return casBasedUpdate(personToUpdate, personFromDb);
        } else {
            personJdbcDao.insertPerson(personToUpdate);
            return true;
        }
    }

    private boolean casBasedUpdate(Person personToUpdate, Person personFromDb) {
        final UUID oldCas = personFromDb.getCasValue();
        final UUID newCas = personToUpdate.getCasValue();
        if (oldCas.equals(newCas)) {
            log.info("Updating with new cas-value.");
            personJdbcDao.updatePerson(personToUpdate.createWithNewCasValue());
            return true;
        } else {
            log.info("not updating because of cas-value clash.");
            return false;
        }
    }
}
