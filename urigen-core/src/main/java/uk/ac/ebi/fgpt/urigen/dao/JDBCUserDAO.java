package uk.ac.ebi.fgpt.urigen.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import uk.ac.ebi.fgpt.urigen.exception.UserCreateException;
import uk.ac.ebi.fgpt.urigen.impl.SimpleUrigenUserImpl;
import uk.ac.ebi.fgpt.urigen.model.UrigenUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author Simon Jupp
 * @date 01/03/2012
 * Functional Genomics Group EMBL-EBI
 */
public class JDBCUserDAO implements UserDAO {

    private JdbcTemplate jdbcTemplate;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String USER_SELECT =
            "select id, user_name, email, restapikey, admin " +
                    "from USER";
    public static final String USER_SELECT_BY_USERNAME = USER_SELECT + " " +
            "where user_name = ?";
    public static final String USER_SELECT_BY_USER_ID = USER_SELECT + " " +
            "where id = ?";
    public static final String USER_SELECT_BY_EMAIL = USER_SELECT + " " +
            "where email = ?";
    public static final String USER_SELECT_BY_REST_API_KEY = USER_SELECT + " " +
            "where restapikey = ?";

    public static final String USER_INSERT =
            "insert into USER (user_name, email, restapikey, admin) " +
                    "values (?, ?, ?, ?)";
    public static final String USER_UPDATE =
            "update USER " +
                    "set user_name = ?, email = ?, restapikey = ?, admin = ? " +
                    "where id = ?";
    public static final String USER_DELETE =
            "delete from USER where id = ?";



    protected Logger getLog() {
        return log;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public UrigenUser getUser(int userId) {
        try {
            return getJdbcTemplate().queryForObject(USER_SELECT_BY_USER_ID,
                    new Object[]{userId},
                    new UrigenUserMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public UrigenUser getUserByApiKey(String apiKey) {
        try {
            return getJdbcTemplate().queryForObject(USER_SELECT_BY_REST_API_KEY,
                    new Object[]{apiKey},
                    new UrigenUserMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public UrigenUser getUserByUserName(String userName) {
        try {
            return getJdbcTemplate().queryForObject(USER_SELECT_BY_USERNAME,
                    new Object[]{userName},
                    new UrigenUserMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public UrigenUser getUserByEmail(String email) {
        try {
            return getJdbcTemplate().queryForObject(USER_SELECT_BY_EMAIL,
                    new Object[]{email},
                    new UrigenUserMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    public Collection<UrigenUser> getUsers() {
        try {
            return getJdbcTemplate().query(USER_SELECT,
                    new UrigenUserMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return new HashSet<>();
        }
    }

    public UrigenUser saveUser(UrigenUser user) throws UserCreateException {

        if (getUserByEmail(user.getEmail()) == null) {

            getLog().info("saving user to DB: " + user.toString());
            getJdbcTemplate().update(USER_INSERT,
                    user.getUserName(),
                    user.getEmail(),
                    user.getApiKey(),
                    user.isAdmin()
            );
        }
        else {
            getLog().error("Error saving user to DB: User with this e-mail already exists");
            throw new UserCreateException("User with this e-mail already exists");
        }
        return getUserByEmail(user.getEmail());
    }

    public UrigenUser updateUser(UrigenUser user) throws UserCreateException {
        if (getUserByEmail(user.getEmail()) == null) {

            getLog().info("saving user to DB: " + user.toString());
            try {
                getJdbcTemplate().update(USER_UPDATE,
                        user.getUserName(),
                        user.getEmail(),
                        user.getApiKey(),
                        user.isAdmin(),
                        user.getId()
                );
            }
            catch (Exception e) {
                throw new UserCreateException("Error updating user: " + user.toString() + ", message: " + e.getLocalizedMessage());
            }
        }
        return getUserByEmail(user.getEmail());
    }

    public void removeUser(UrigenUser user) {
        getJdbcTemplate().update(USER_DELETE,
                user.getId() );
    }

    public boolean contains(int userId) {
        try {
            getJdbcTemplate().queryForObject(USER_SELECT_BY_USER_ID,
                    new Object[]{userId},
                    new UrigenUserMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    public boolean isAdmin(UrigenUser user) {
        UrigenUser u = getJdbcTemplate().queryForObject(USER_SELECT_BY_USER_ID,
                new Object[]{user.getId()},
                new UrigenUserMapper());
        return u.isAdmin();
    }

    private class UrigenUserMapper implements RowMapper<UrigenUser> {


        public UrigenUser mapRow(ResultSet resultSet, int i) throws SQLException {
            SimpleUrigenUserImpl user = new SimpleUrigenUserImpl(resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getBoolean(5)
            );
            user.setId(resultSet.getInt(1));
            return user;
        }
    }
}
