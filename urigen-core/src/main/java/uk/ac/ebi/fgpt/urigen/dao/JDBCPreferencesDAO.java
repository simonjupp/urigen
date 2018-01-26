package uk.ac.ebi.fgpt.urigen.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import uk.ac.ebi.fgpt.urigen.exception.PreferenceCreationException;
import uk.ac.ebi.fgpt.urigen.exception.UrigenException;
import uk.ac.ebi.fgpt.urigen.impl.SimpleUrigenPreferencesImpl;
import uk.ac.ebi.fgpt.urigen.impl.UrigenUserRangeImpl;
import uk.ac.ebi.fgpt.urigen.model.UrigenPreference;
import uk.ac.ebi.fgpt.urigen.model.UrigenUserRange;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Simon Jupp
 * @date 01/03/2012
 * Functional Genomics Group EMBL-EBI
 */
public class JDBCPreferencesDAO implements PreferencesDAO{

    private JdbcTemplate jdbcTemplate;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public Logger getLog () {
        return log;
    }

    public static final String PREF_SELECT =
            "select id, " +
                    "ontology_uri, " +
                    "ontology_physical_uri, " +
                    "ontology_name, " +
                    "base_uri, " +
                    "separator, " +
                    "prefix, " +
                    "suffix, " +
                    "digit_count, " +
                    "digit_start, " +
                    "digit_end, " +
                    "last_digit, " +
                    "generator_type_id, " +
                    "ID_GENERATOR_TYPE.class_name, " +
                    "check_source, " +
                    "last_update, " +
                    "ontology_md5, " +
                    "status_ok, " +
                    "status_message " +
                    "from PREFERENCE, ID_GENERATOR_TYPE where PREFERENCE.generator_type_id = ID_GENERATOR_TYPE.id ";

    public static final String PREF_INSERT =
            "insert into PREFERENCE ( " +
                    "ontology_uri, " +
                    "ontology_physical_uri, " +
                    "ontology_name, " +
                    "base_uri, " +
                    "separator, " +
                    "prefix, " +
                    "suffix, " +
                    "digit_count, " +
                    "digit_start, " +
                    "digit_end, " +
                    "last_digit, " +
                    "generator_type_id, " +
                    "check_source, " +
                    "status_ok, " +
                    "status_message " +
                    ") " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public static final String PREF_UPDATE =
            "update PREFERENCE set " +
                    "ontology_uri = ?, " +
                    "ontology_physical_uri = ?, " +
                    "ontology_name = ?, " +
                    "base_uri = ?, " +
                    "separator = ?, " +
                    "prefix = ?, " +
                    "suffix = ?, " +
                    "digit_count = ?, " +
                    "digit_start = ?, " +
                    "digit_end = ?, " +
                    "last_digit = ?, " +
                    "generator_type_id = ?, " +
                    "check_source = ?, " +
                    "status_ok = ?, " +
                    "status_message = ? " +
                    "where id = ?";

    public static final String PREF_DELETE =
            "delete from PREFERENCE where id = ?";

    public static final String PREF_SELECT_BY_NAME = PREF_SELECT + " " +
            "AND ontology_name = ?";

    public static final String PREF_SELECT_BY_ID = PREF_SELECT + " " +
            "AND id = ?";

    public static final String GENERATOR_TYPE_SELECT =
            "select id, class_name " +
                    "from ID_GENERATOR_TYPE";

    public static final String GENERATOR_TYPE_BY_ID = GENERATOR_TYPE_SELECT + " where id = ?";

    public static final String GENERATOR_TYPE_BY_NAME = GENERATOR_TYPE_SELECT + " where class_name = ?";

    public static final String SELECT_RANGE = "select id, user_id, preferences_id, digit_start, digit_end, last_digit " +
            " from USER_RANGE";

    public static final String SELECT_RANGE_BY_PREF = SELECT_RANGE +
            " where preferences_id = ?";

    public static final String SELECT_RANGE_BY_UI_AND_PREF = SELECT_RANGE +
            " where user_id = ? and preferences_id = ?";


    public static final String INSERT_USER_RANGE = "insert into USER_RANGE (user_id, preferences_id, digit_start, digit_end, last_digit) " +
            " values (?,?,?,?,?)";

    public static final String UPDATE_USER_RANGE = "update USER_RANGE set " +
            "user_id = ?, " +
            "preferences_id = ?, " +
            "digit_start = ?, " +
            "digit_end= ?, " +
            "last_digit= ? " +
            "where id = ?";



    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UrigenPreference getPreferencesById(int id) {
        try {
            UrigenPreference p = getJdbcTemplate().queryForObject(PREF_SELECT_BY_ID,
                    new Object[]{id},
                    new UrigenPreferenceMapper());
            p.getAllUserRange().addAll(getUserRangeByPreference(p.getPreferenceId()));

            return p;
        }  catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void removePref(UrigenPreference preference) {

        getJdbcTemplate().update(PREF_DELETE, preference.getPreferenceId());

    }

    public Collection<UrigenPreference> getAllPreferences() {
        Set<UrigenPreference> ps = new HashSet<>();
        for (UrigenPreference p : getJdbcTemplate().query(PREF_SELECT,
                new UrigenPreferenceMapper())) {

            ps.add(getPreferencesById(p.getPreferenceId()));
        }
        return ps;
    }

    public GeneratorType getGeneratorTypeById(int id)  {
        return getJdbcTemplate().queryForObject(GENERATOR_TYPE_BY_ID, new Object[]{id}, new GeneratorTypeMapper());
    }

    public GeneratorType getGeneratorTypeByName(String name)  {
        getLog().debug("looking up generator type: " + name);
        try {
            return getJdbcTemplate().queryForObject(GENERATOR_TYPE_BY_NAME, new Object[]{name}, new GeneratorTypeMapper());
        } catch (EmptyResultDataAccessException e)  {
            getLog().error("No generator type found for: " + name);
            return null;
        }
    }

    public Collection<GeneratorType> getGeneratorTypes()  {
        return getJdbcTemplate().query(GENERATOR_TYPE_SELECT, new GeneratorTypeMapper());
    }

    public UrigenPreference update(UrigenPreference preference) throws PreferenceCreationException {

        GeneratorType gt = getGeneratorTypeByName(preference.getAutoIDGenerator().getClass().getName());
        if (gt == null) {
            getLog().error("No generator type found for: " + preference.getAutoIDGenerator().getClass().getName());
            throw new PreferenceCreationException("Can't update preference, no generator type found for: " + preference.getAutoIDGenerator().getClass().getName());
        }
        try {

            getJdbcTemplate().update(PREF_UPDATE,
                    preference.getOntologyUri(),
                    preference.getOntologyPhysicalUri(),
                    preference.getOntologyName(),
                    preference.getBaseUri(),
                    preference.getSeparator(),
                    preference.getPrefix(),
                    preference.getSuffix(),
                    preference.getAutoIdDigitCount(),
                    preference.getAutoIdStart(),
                    preference.getAutoIdEnd(),
                    preference.getLastIdInSequence(),
                    gt.getId(),
                    preference.getCheckSource(),
                    preference.statusOK(),
                    preference.conflictMessage(),
                    preference.getPreferenceId()
            );

            for (UrigenUserRange r : preference.getAllUserRange()) {
                UrigenUserRange range = getUserRange(r.getUserId(), preference.getPreferenceId());
                if (range == null) {
                    r.setPreferenceId(preference.getPreferenceId());
                    getLog().debug("creating new user range: " + r.toString());
                    saveUserRange(r);
                }
                else {
                    getLog().debug("updating user range: " + r.toString());
                    range.setAutoIdDigitStart(r.getAutoIdDigitStart());
                    range.setAutoIdDigitEnd(r.getAutoIdDigitEnd());
                    updateUserRange(range);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            getLog().error("Can't update: " + e.getMessage());
            throw new PreferenceCreationException("Can't update preference: " + e.getLocalizedMessage());
        }
        return getPreferencesById(preference.getPreferenceId());
    }

    public UrigenPreference savePreferences(UrigenPreference preference) throws PreferenceCreationException {

        if (getPreferenceByName(preference.getOntologyName()) != null) {
            throw new PreferenceCreationException("Preference with this name already exists");
        }
        GeneratorType gt = getGeneratorTypeByName(preference.getAutoIDGenerator().getClass().getName());
        if (gt == null) {
            getLog().error("No generator type found for: " + preference.getAutoIDGenerator().getClass().getName());
            throw new PreferenceCreationException("Can't save preference, no generator type found for: " + preference.getAutoIDGenerator().getClass().getName());
        }
        try {
            getJdbcTemplate().update(PREF_INSERT,
                    preference.getOntologyUri(),
                    preference.getOntologyPhysicalUri(),
                    preference.getOntologyName(),
                    preference.getBaseUri(),
                    preference.getSeparator(),
                    preference.getPrefix(),
                    preference.getSuffix(),
                    preference.getAutoIdDigitCount(),
                    preference.getAutoIdStart(),
                    preference.getAutoIdEnd(),
                    preference.getLastIdInSequence(),
                    gt.getId(),
                    preference.getCheckSource(),
                    preference.statusOK(),
                    preference.conflictMessage()
            );

            UrigenPreference newPref = getPreferenceByName(preference.getOntologyName());
            if (newPref != null) {
                getLog().debug("New urigen pref created: " + newPref.toString());
                for (UrigenUserRange r : preference.getAllUserRange()) {
                    getLog().debug("adding user range to new pref: " + r.toString());
                    r.setPreferenceId(newPref.getPreferenceId());
                    saveUserRange(r);
                }
                return getPreferencesById(newPref.getPreferenceId());
            }
        }
        catch (Exception e) {
            getLog().error("Can't save: " + e.getLocalizedMessage());
            throw new PreferenceCreationException("Can't save preference: " + preference.toString());
        }
        return null;
    }

    public UrigenPreference getPreferenceByName (String name) {
        try {
            return getJdbcTemplate().queryForObject(PREF_SELECT_BY_NAME,
                    new Object[]{name},
                    new UrigenPreferenceMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean contains(int preferencesId) {
        try {
            getJdbcTemplate().queryForObject(PREF_SELECT_BY_ID,
                    new Object[]{preferencesId},
                    new UrigenPreferenceMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }


    public Collection<UrigenUserRange> getUserRangeByPreference(int preferenceId) {
        try {
            return getJdbcTemplate().query(SELECT_RANGE_BY_PREF,
                    new Object[]{preferenceId},
                    new UserRangeMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return new HashSet<>();
        }
    }

    public UrigenUserRange getUserRange(int userId, int preferencesId) {
        UrigenUserRange range = null;
        try {
            range = getJdbcTemplate().queryForObject(SELECT_RANGE_BY_UI_AND_PREF,
                    new Object[]{userId, preferencesId},
                    new UserRangeMapper());
        }
        catch (EmptyResultDataAccessException e) {
            getLog().debug("cant find user range: "+ userId + ":" +preferencesId);
            return range;
        }
        return range;
    }

    public UrigenUserRange saveUserRange (UrigenUserRange range) throws UrigenException {

        getLog().debug("inserting user range:  " + range.toString());
        try {
            getJdbcTemplate().update(INSERT_USER_RANGE,
                    range.getUserId(),
                    range.getPrefsId(),
                    range.getAutoIdDigitStart(),
                    range.getAutoIdDigitEnd(),
                    range.getLastIdInSequence());
        } catch (Exception e) {
            getLog().error("can't add user range: " + range.toString() + " reason: " + e.getLocalizedMessage());
            throw new UrigenException("can't add user range: " + range.toString() + " reason: " + e.getLocalizedMessage());
        }
        return getUserRange(range.getUserId(), range.getPrefsId());
    }

    public void updateUserRange(UrigenUserRange range) throws UrigenException {

        getLog().debug("updating user range:  " + range.toString());
        try {
            getJdbcTemplate().update(UPDATE_USER_RANGE,
                    range.getUserId(),
                    range.getPrefsId(),
                    range.getAutoIdDigitStart(),
                    range.getAutoIdDigitEnd(),
                    range.getLastIdInSequence(),
                    range.getId());
        } catch (Exception e) {
            getLog().error("can't update user range: " + range.toString() + " reason: " + e.getLocalizedMessage());
            throw new UrigenException("can't update user range: " + range.toString() + " reason: " + e.getLocalizedMessage());
        }

    }

    public UrigenPreference getPreferenceByUserRange(UrigenUserRange range) {
        return getPreferencesById(range.getPrefsId());
    }

    public void setLastOntologyCheckDate(int prefId, Date date) {

    }

    public void updateMD5checksum(int prefId, String md5) {

    }

    private class GeneratorType {

        private int id;
        private String class_name;

        private GeneratorType() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getClassName() {
            return class_name;
        }

        public void setClassName(String class_name) {
            this.class_name = class_name;
        }

        private GeneratorType(int id, String class_name) {

            this.id = id;
            this.class_name = class_name;
        }
    }

    private class GeneratorTypeMapper implements RowMapper<GeneratorType> {

        public GeneratorType mapRow(ResultSet resultSet, int i) throws SQLException {
            return new GeneratorType(resultSet.getInt(1), resultSet.getString(2));
        }
    }

    private class UserRangeMapper implements RowMapper<UrigenUserRange> {

        public UrigenUserRange mapRow(ResultSet resultSet, int i) throws SQLException {
            UrigenUserRange range = new UrigenUserRangeImpl(
                    resultSet.getInt(2),
                    resultSet.getInt(3),
                    resultSet.getInt(4),
                    resultSet.getInt(5)
            );
            range.setLastIdInSequence(resultSet.getLong(6));
            range.setId(resultSet.getInt(1));
            return range;
        }
    }

    private class UrigenPreferenceMapper implements RowMapper<UrigenPreference> {

        public UrigenPreference mapRow(ResultSet resultSet, int i) throws SQLException {

            SimpleUrigenPreferencesImpl pref = new SimpleUrigenPreferencesImpl(
                    resultSet.getString(4),
                    resultSet.getString(2),
                    resultSet.getString(5),
                    resultSet.getString(3),
                    resultSet.getString(6),
                    resultSet.getString(14),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    resultSet.getInt(9),
                    resultSet.getInt(10),
                    resultSet.getInt(11),
                    resultSet.getBoolean(15));

            pref.setPreferenceId(resultSet.getInt(1));
            pref.setLastIdInSequence(resultSet.getLong(12));
            pref.setStatusOK(resultSet.getBoolean(18));
            pref.setConflictMessage(resultSet.getString(19));
            return pref;

        }
    }
}
