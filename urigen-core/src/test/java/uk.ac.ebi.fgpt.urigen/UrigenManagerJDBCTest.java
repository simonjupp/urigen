package uk.ac.ebi.fgpt.urigen;

import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import uk.ac.ebi.fgpt.urigen.dao.*;
import uk.ac.ebi.fgpt.urigen.exception.PreferenceCreationException;
import uk.ac.ebi.fgpt.urigen.exception.UrigenException;
import uk.ac.ebi.fgpt.urigen.exception.UserCreateException;
import uk.ac.ebi.fgpt.urigen.impl.*;
import uk.ac.ebi.fgpt.urigen.model.UrigenManager;
import uk.ac.ebi.fgpt.urigen.model.UrigenPreference;
import uk.ac.ebi.fgpt.urigen.model.UrigenRequest;
import uk.ac.ebi.fgpt.urigen.model.UrigenUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Simon Jupp
 * @date 02/03/2012
 * Functional Genomics Group EMBL-EBI
 */
public class UrigenManagerJDBCTest extends TestCase {

    private JDBCUserDAO uDao = new JDBCUserDAO();
    private JDBCPreferencesDAO pDao = new JDBCPreferencesDAO();
    private JDBCUrigenEntityDAO eDao = new JDBCUrigenEntityDAO();

    private UrigenManager uriGenManager;

    private Connection connection;
    private Logger log = LoggerFactory.getLogger(getClass());


    @Override
    protected void setUp() throws Exception {

        try {
            // load driver
            Class.forName("org.hsqldb.jdbc.JDBCDriver");

            // get connection and create table
            connection = DriverManager.getConnection("jdbc:hsqldb:file:target/hsql/urigen_test;hsqldb.write_delay=false");

            rebuildDatabases();

            for (String id : GeneratorTypes.ALL_TYPES_ID) {
                log.debug("populating generator types table with id : " +id);
                PreparedStatement p  = connection.prepareStatement("insert into ID_GENERATOR_TYPE (class_name) values(?)");
                p.setString(1, id);
                p.execute();
            }

            SingleConnectionDataSource ds = new SingleConnectionDataSource(connection, true);
            uDao.setJdbcTemplate(new JdbcTemplate(ds));
            pDao.setJdbcTemplate(new JdbcTemplate(ds));
            eDao.setJdbcTemplate(new JdbcTemplate(ds));

            uriGenManager = new UrigenManagerImpl(pDao, eDao, uDao, new OWLAPIOntologyDAO());
        }
        catch (Exception e) {
            e.printStackTrace();
            tearDown();
            fail();
        }


    }

    public void testLoadUser() {

        UrigenUser adminUser =new SimpleUrigenUserImpl("admin", "admin@test", "12345", true);
        UrigenUser regularUser =new SimpleUrigenUserImpl("test", "test@test", "54321", false);

        assertEquals(uriGenManager.getUsers().size(), 0);


        try {
            assertEquals(uriGenManager.saveUser(adminUser).getId(), 0);

        } catch (UserCreateException e) {
            rebuildDatabases();
            fail();
        }

        try {
            assertEquals(uriGenManager.saveUser(regularUser).getId(), 1);

        } catch (UserCreateException e) {
            rebuildDatabases();
            fail();
        }

        assertEquals(uriGenManager.getUsers().size(), 2);





    }

    public void testLoadPrefs() {

        UrigenUser adminUser =new SimpleUrigenUserImpl("admin", "admin@test", "12345", true);
        UrigenUser regularUser =new SimpleUrigenUserImpl("test", "test@test", "54321", false);

        assertEquals(uriGenManager.getUsers().size(), 0);


        try {
            assertEquals(uriGenManager.saveUser(adminUser).getId(), 0);

        } catch (UserCreateException e) {
            rebuildDatabases();
            fail();
        }

        try {
            assertEquals(uriGenManager.saveUser(regularUser).getId(), 1);

        } catch (UserCreateException e) {
            rebuildDatabases();
            fail();
        }


        assertEquals(uriGenManager.getUsers().size(), 2);

        UrigenPreference pref1 = new SimpleUrigenPreferencesImpl(
                "test-onto-1",
                "http://www.uri-manager/test-1.owl",
                "http://www.uri-manager/test-1.owl",
                "../rescources/test-1.owl",
                "/",
                GeneratorTypes.ITERATIVE.getClassId(),
                "TEST1_",
                "",
                6,
                0,
                -1,
                true
        );

        try {
            log.debug("saving pref 1");
            pref1 = uriGenManager.savePreference(pref1);
            assertEquals((pref1.getPreferenceId()), 0);
        } catch (PreferenceCreationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        UrigenPreference pref2 = new SimpleUrigenPreferencesImpl(
                "test-onto-2",
                "http://www.uri-manager/test-2.owl",
                "http://www.uri-manager/test-2.owl",
                "../rescources/test-2.owl",
                "#",
                GeneratorTypes.ITERATIVE.getClassId(),
                "TEST2_",
                "",
                6,
                0,
                -1,
                true
        );

        try {
            log.debug("saving pref 2");
            pref2 = uriGenManager.savePreference(pref2);
            assertEquals((pref2.getPreferenceId()), 1);

        } catch (PreferenceCreationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        UrigenPreference pref3 = new SimpleUrigenPreferencesImpl(
                "test-onto-3",
                "http://www.uri-manager/test-3.owl",
                "http://www.uri-manager/test-3.owl",
                "../rescources/test-3.owl",
                ":",
                GeneratorTypes.ITERATIVE.getClassId(),
                "TEST3_",
                "",
                6,
                0,
                -1,
                true
        );

        try {
            log.debug("saving pref 3");
            pref3 = uriGenManager.savePreference(pref3);
            assertEquals((pref3.getPreferenceId()), 2);

        } catch (PreferenceCreationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        UrigenPreference pref4 = new SimpleUrigenPreferencesImpl(
                "test-onto-4",
                "http://www.uri-manager/test-4.owl",
                "http://www.uri-manager/test-4.owl",
                "../rescources/test-4.owl",
                ":",
                GeneratorTypes.PSEUDO_RANDOM.getClassId(),
                "TEST4_",
                "_PSEUDO_RANDOM",
                6,
                -1,
                -1,
                true);

        try {
            log.debug("saving pref 4");
            pref4 = uriGenManager.savePreference(pref4);
            assertEquals((pref4.getPreferenceId()), 3);

        } catch (PreferenceCreationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        UrigenPreference pref5 = new SimpleUrigenPreferencesImpl(
                "test-onto-5",
                "http://www.uri-manager/test-5.owl",
                "http://www.uri-manager/test-5.owl",
                "../rescources/test-5.owl",
                ":",
                GeneratorTypes.RANDOM.getClassId(),
                "TEST5_",
                "_RANDOM",
                10,
                -1,
                -1,
                true
        );

        try {
            log.debug("saving pref 5");
            pref5 = uriGenManager.savePreference(pref5);
            assertEquals((pref5.getPreferenceId()), 4);

        } catch (PreferenceCreationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        UrigenPreference pref6 = new SimpleUrigenPreferencesImpl(
                "test-onto-6",
                "http://www.uri-manager/test-6.owl",
                "http://www.uri-manager/test-6.owl",
                "../rescources/test-6.owl",
                ":",
                GeneratorTypes.ITERATIVE_RANGE.getClassId(),
                "TEST_6_",
                "_RANGE",
                8,
                -1,
                -1,
                true
        );

        try {
            log.debug("saving pref 6");
            pref6 = uriGenManager.savePreference(pref6);
            assertEquals((pref6.getPreferenceId()), 5);

        } catch (PreferenceCreationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        pref6.getAllUserRange().add(new UrigenUserRangeImpl(uriGenManager.getUserByUserName("admin").getId(),pref6.getPreferenceId(), 400, 700));

        log.debug("added new user range to pref 6: " + pref6.getPreferenceId());
        try {
            uriGenManager.updatePreference(pref6);
        } catch (PreferenceCreationException e) {
            rebuildDatabases();
            fail();
        }

        assertEquals(uriGenManager.getUsers().size(), 2);
        assertEquals(uriGenManager.getPreferences().size(), 6);

        log.info("generating URIS...");
        for (UrigenUser u : uriGenManager.getUsers()) {
            for (UrigenPreference p : uriGenManager.getPreferences()) {


                UrigenRequest request1 = new SimpleUrigenRequestImpl(u.getId(), "", p.getPreferenceId(), "", "");
                try {
                    System.out.println(p.toString());
                    for (int x = 0; x <100; x++) {
                        uriGenManager.getNewEntity(request1);
                    }
                } catch (UrigenException e) {
                    if (!e.getLocalizedMessage().equals("No user range associated with this preference")) {
                        fail(e.getMessage());
                    }

                }

            }

        }


    }

    public void testGenerateUris() {




    }


    public void rebuildDatabases ()  {
        try {
            connection.prepareStatement("drop table if exists  URI;").execute();
            connection.prepareStatement("drop table if exists  USER_RANGE;").execute();
            connection.prepareStatement("drop table if exists  PREFERENCE;").execute();
            connection.prepareStatement("drop table if exists  ID_GENERATOR_TYPE;").execute();
            connection.prepareStatement("drop table if exists  USER;").execute();

            connection.prepareStatement(HSQLDBInitializer.USER).execute();
            connection.prepareStatement(HSQLDBInitializer.ID_GENERATOR_TYPE).execute();
            connection.prepareStatement(HSQLDBInitializer.ONTOLOGY_PREFS).execute();
            connection.prepareStatement(HSQLDBInitializer.ONTOLOGY_RANGES).execute();
            connection.prepareStatement(HSQLDBInitializer.GENERATED_URI).execute();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    @Override
    protected void tearDown() throws Exception {
        rebuildDatabases();
    }
}
