package uk.ac.ebi.fgpt.urigen.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import uk.ac.ebi.fgpt.urigen.exception.AutoIDException;
import uk.ac.ebi.fgpt.urigen.impl.SimpleUrigenEntityImpl;
import uk.ac.ebi.fgpt.urigen.model.UrigenEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * @author Simon Jupp
 * @date 02/03/2012
 * Functional Genomics Group EMBL-EBI
 */
public class JDBCUrigenEntityDAO implements UrigenEntityDAO{

    private JdbcTemplate jdbcTemplate;

    private final Logger log = LoggerFactory.getLogger(getClass());

    protected Logger getLog() {
        return log;
    }

    public final static String SELECT_URIS =
            "select id, preference_id, user_id, original_uri, generated_uri, uri_label, uri_comment, creation_date " +
            "from URI";

    public final static String ORDER = "order by creation_date desc";

    public final static String SELECT_URIS_BY_ID = SELECT_URIS + " "  +
            "where id = ?" + " " + ORDER;

    public final static String SELECT_URIS_BY_USER = SELECT_URIS + " "  +
            "where user_id = ?"+ " " + ORDER;

    public final static String SELECT_URIS_BY_PREF = SELECT_URIS + " "  +
            "where preference_id = ?"+ " " + ORDER;

    public final static String SELECT_URIS_BY_URI = SELECT_URIS + " "  +
            "where generated_uri = ?"+ " " + ORDER;


    public final static String INSERT_URI = "" +
            "insert into URI (" +
            "preference_id, user_id, original_uri, generated_uri, uri_label, uri_comment) " +
            "values (?,?,?,?,?,?)";

    public final static String DELETE_URI =
            "delete from URI where id = ?";

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public UrigenEntity getEntityById(int id) {
        try {
            return getJdbcTemplate().queryForObject(SELECT_URIS_BY_ID,
                    new Object[]{id},
                    new UrigenEntityMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Collection<UrigenEntity> getAllEntities() {
        return getJdbcTemplate().query(SELECT_URIS + " " + ORDER,
                new UrigenEntityMapper());
    }

    public Collection<UrigenEntity> getAllEntitiesByUser(int id) {
        return getJdbcTemplate().query(SELECT_URIS_BY_USER,
                new Object[]{id},
                new UrigenEntityMapper());
    }

    public Collection<UrigenEntity> getAllEntitiesByPreferenceId(int id) {
        return getJdbcTemplate().query(SELECT_URIS_BY_PREF,
                new Object[]{id},
                new UrigenEntityMapper());
    }

    public void RemoveEntity(UrigenEntity entity) {
        getJdbcTemplate().update(DELETE_URI, entity.getId());
    }

    public UrigenEntity saveUrigenEntity(UrigenEntity entity) throws AutoIDException {

        if (getEntityByUri(entity.getGeneratedUri()) != null) {
           throw new AutoIDException("URI already exists: " + entity.getGeneratedUri());
        }
        try {
            getJdbcTemplate().update(INSERT_URI,
                    entity.getPreferencesId(),
                    entity.getUserId(),
                    entity.getOriginalUri(),
                    entity.getGeneratedUri(),
                    entity.getLabel(),
                    entity.getComment()
                    );
        } catch (Exception e) {
            getLog().error("error creating new user: " + entity.toString() + " e=" + e.getMessage());
            throw new AutoIDException(e.getMessage());
        }
        return getEntityByUri(entity.getGeneratedUri());
    }

    public UrigenEntity getEntityByUri(String uri) {
        try {
            return getJdbcTemplate().queryForObject(SELECT_URIS_BY_URI,
                    new Object[]{uri},
                    new UrigenEntityMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;

        }

    }

    private class UrigenEntityMapper implements RowMapper<UrigenEntity> {

        public UrigenEntity mapRow(ResultSet resultSet, int i) throws SQLException {

            SimpleUrigenEntityImpl ent = new SimpleUrigenEntityImpl(
                    resultSet.getString(5),
                    resultSet.getString(4),
                    resultSet.getString(6),
                    resultSet.getInt(3),
                    resultSet.getInt(2),
                    resultSet.getTimestamp(8),
                    resultSet.getString(7)
            );

            ent.setId(resultSet.getInt(1));
            return ent;
        }
    }

}
