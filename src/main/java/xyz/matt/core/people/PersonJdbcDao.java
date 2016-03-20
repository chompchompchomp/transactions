package xyz.matt.core.people;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

@Repository
public class PersonJdbcDao {

    static final PersonMapper PERSON_MAPPER = new PersonMapper();
    static final String SELECT_PERSON = "select * from person where id = ?";
    static final String INSERT_PERSON = "insert into person (id, cas_value, name, favorite_color) values (?,?,?,?)";
    static final String UPDATE_PERSON = "update person set cas_value = ?, name = ?, favorite_color = ? where id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = checkNotNull(jdbcTemplate);
    }

    public Optional<Person> getPerson(UUID id) {
        checkNotNull(id);
        final List<Person> personList = jdbcTemplate.query(SELECT_PERSON, PERSON_MAPPER, id.toString());
        if (personList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(personList.get(0));
        }
    }

    public void insertPerson(Person person) {
        checkNotNull(person);
        jdbcTemplate.update(INSERT_PERSON,
                person.getId(),
                person.getCasValue(),
                person.getName(),
                person.getFavoriteColor());
    }

    public void updatePerson(Person person) {
        checkNotNull(person);
        jdbcTemplate.update(UPDATE_PERSON,
                person.getCasValue(),
                person.getName(),
                person.getFavoriteColor(),
                person.getId());
    }

    static class PersonMapper implements RowMapper<Person> {
        @Override
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            final UUID id = UUID.fromString(rs.getString("id"));
            final UUID casValue = UUID.fromString(rs.getString("cas_value"));
            final String name = rs.getString("name");
            final String favoriteColor = rs.getString("favorite_color");
            return new Person(id, casValue, name, favoriteColor);
        }
    }
}
