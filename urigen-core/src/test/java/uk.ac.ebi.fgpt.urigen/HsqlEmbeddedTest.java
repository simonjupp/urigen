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
import uk.ac.ebi.fgpt.urigen.model.UrigenPreference;
import uk.ac.ebi.fgpt.urigen.model.UrigenUser;
import uk.ac.ebi.fgpt.urigen.model.UrigenUserRange;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Simon Jupp
 * @date 01/03/2012
 * Functional Genomics Group EMBL-EBI
 */
public class HsqlEmbeddedTest extends TestCase{

    private Logger log = LoggerFactory.getLogger(getClass());

    private Connection connection;

    private JDBCUserDAO uDao = new JDBCUserDAO();
    private JDBCPreferencesDAO pDao = new JDBCPreferencesDAO();


    @Override
    protected void setUp() throws Exception {
        try {
            // load driver
            Class.forName("org.hsqldb.jdbc.JDBCDriver");

            // get connection and create table
            connection = DriverManager.getConnection("jdbc:hsqldb:file:target/hsql/urigen_test;hsqldb.write_delay=false");
            connection.prepareStatement("create table testtable (" +
                    "id INTEGER, " +
                    "name VARCHAR(100));").execute();
            connection.prepareStatement("insert into testtable(id, name) values (1, 'testvalue');").execute();

            rebuildDatabases();

            for (String id : GeneratorTypes.ALL_TYPES_ID) {
                log.debug("populating generator types table with id : " + id);
                PreparedStatement p  = connection.prepareStatement("insert into ID_GENERATOR_TYPE (class_name) values(?)");
                p.setString(1, id);
                p.execute();
            }
            SingleConnectionDataSource ds = new SingleConnectionDataSource(connection, true);
            uDao.setJdbcTemplate(new JdbcTemplate(ds));
            pDao.setJdbcTemplate(new JdbcTemplate(ds));


        }
        catch (Exception e) {
            e.printStackTrace();
            tearDown();
            fail();
        }

    }


    public void testUserTable() {


        try {

            // load a user

            UrigenUser adminUser =new SimpleUrigenUserImpl("admin", "admin@test", "12345", true);
            UrigenUser regularUser =new SimpleUrigenUserImpl("test", "test@test", "54321", false);

            assertEquals(uDao.getUsers().size(), 0);

            UrigenUser newAdmin = uDao.saveUser(adminUser);
            assertEquals(newAdmin.getId(), 0);

            log.debug("new user created: " + newAdmin.toString());
            userEqual(newAdmin, adminUser);


            UrigenUser newRegular = uDao.saveUser(regularUser);
            assertEquals(newRegular.getId(), 1);

            log.debug("new user created: " + newRegular.toString());
            userEqual(newRegular, regularUser);

            // query for user tests

            userEqual(uDao.getUser(0), newAdmin);
            userEqual(uDao.getUserByUserName("admin"), newAdmin);
            userEqual(uDao.getUserByEmail("admin@test"), newAdmin);
            userEqual(uDao.getUserByApiKey("12345"), newAdmin);

            assertNotSame(uDao.getUserByUserName("admin").getId(), uDao.getUserByUserName("test").getId());

            // fail to add
            UrigenUser duplicateadmin =new SimpleUrigenUserImpl("admin", "admin@test", "12345", true);

            try {
                uDao.saveUser(duplicateadmin);
            }
            catch (UserCreateException e) {
                log.debug("Failed to create user");
                assertEquals(e.getMessage(), "User with this e-mail already exists");
            }

            assertEquals(uDao.getUsers().size(), 2);

            assertTrue(uDao.isAdmin(newAdmin));
            // update user

            newAdmin.setEmail("newemail@test");
            uDao.updateUser(newAdmin);
            assertEquals(uDao.getUserByEmail("newemail@test").getId(), newAdmin.getId() );

            // try update email with existing email
            try {
                newAdmin.setEmail("test@test");
                uDao.updateUser(newAdmin);
            }
            catch (UserCreateException e) {
                assertEquals("Error saving user to DB: User with this e-mail already exists", e.getMessage());
            }

            // delete user

            uDao.removeUser(newAdmin);
            assertEquals(uDao.getUsers().size(), 1);

            assertFalse(uDao.contains(newAdmin.getId()));
            assertTrue(uDao.contains(newRegular.getId()));



        } catch (UserCreateException e) {
            fail(e.getMessage());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public void testPreferencesTable () {

        Set<UrigenPreference> validPrefs = new HashSet<UrigenPreference>();

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
            pref1 = pDao.savePreferences(pref1);
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
            pref2 = pDao.savePreferences(pref2);
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
            pref3 = pDao.savePreferences(pref3);
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
            pref4 = pDao.savePreferences(pref4);
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
            pref5 = pDao.savePreferences(pref5);
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
            pref6 = pDao.savePreferences(pref6);
        } catch (PreferenceCreationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        validPrefs.add(pref1);
        validPrefs.add(pref2);
        validPrefs.add(pref3);
        validPrefs.add(pref4);
        validPrefs.add(pref5);
        validPrefs.add(pref6);

        log.debug("Testing loaded preferences");
        for (UrigenPreference prefs : validPrefs) {
            UrigenPreference newP = pDao.getPreferencesById(prefs.getPreferenceId());
            assertEquals(newP.getOntologyName(), prefs.getOntologyName());
            assertEquals(newP.getOntologyUri(), prefs.getOntologyUri());
            assertEquals(newP.getOntologyPhysicalUri(), prefs.getOntologyPhysicalUri());
            assertEquals(newP.getBaseUri(), prefs.getBaseUri());
            assertEquals(newP.getPrefix(), prefs.getPrefix());
            assertEquals(newP.getSuffix(), prefs.getSuffix());
            assertEquals(newP.getAutoIdDigitCount(), prefs.getAutoIdDigitCount());
            assertEquals(newP.getAutoIdStart(), prefs.getAutoIdStart());
            assertEquals(newP.getAutoIdEnd(), prefs.getAutoIdEnd());
            assertEquals(newP.getLastIdInSequence(), prefs.getLastIdInSequence());
            assertEquals(newP.getAutoIDGenerator().getId(), prefs.getAutoIDGenerator().getId());
            assertEquals(newP.getCheckSource(), prefs.getCheckSource());
        }

        try {

            UrigenUser user1 = new SimpleUrigenUserImpl("test1", "test1@ebi.ac.uk", Long.toString(System.currentTimeMillis()), true);
            UrigenUser newU = uDao.saveUser(user1);

            UrigenUser user2 = new SimpleUrigenUserImpl("test2", "test1@ebi.ac.uk", Long.toString(System.currentTimeMillis()), true);
            try {
                UrigenUser newU2 = uDao.saveUser(user2);
                fail();
            }
            catch (UserCreateException e) {
                assertEquals(e.getLocalizedMessage(), "User with this e-mail already exists");
            }
            user2 =  new SimpleUrigenUserImpl("test2", "test2@ebi.ac.uk", Long.toString(System.currentTimeMillis()), true);
            UrigenUser newU2 = uDao.saveUser(user2);

            try {
                UrigenUserRange userRange1 = new UrigenUserRangeImpl(uDao.getUserByUserName("test1").getId(),pref6.getPreferenceId(), 0, 999);
                pref6.getAllUserRange().add(userRange1);
                UrigenPreference p = pDao.update(pref6);
                for (UrigenUserRange r : p.getAllUserRange()) {
                    assertEquals(r.getUserId(), user1.getId());
                    assertEquals(r.getPrefsId(), p.getPreferenceId());
                }

                try {
                    pDao.saveUserRange(new UrigenUserRangeImpl(uDao.getUserByUserName("test2").getId(),pref6.getPreferenceId(), 1000, 1999));
                    assertEquals(pDao.getPreferencesById(pref6.getPreferenceId()).getAllUserRange().size(), 2);

                } catch (UrigenException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }


            } catch (PreferenceCreationException e) {
                e.printStackTrace();
                fail();
            }

//        rDAO.save(userRange1);

//            UrigenManager uriGenManager = new UrigenManagerImpl(pDao, , uDAO, new OWLAPIOntologyDAO());


            if (newU == null) {
                fail("Failed to create user: " + user1.toString());
            }
        } catch (UserCreateException e) {
            fail("Failed to create user");
        }

        rebuildDatabases();


    }

    public void rebuildDatabases ()  {
        try {
            connection.prepareStatement("drop table if exists testtable;").execute();
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

    public void userEqual (UrigenUser user1, UrigenUser user2) {

        assertEquals(user1.getUserName(), user2.getUserName());
        assertEquals(user1.getEmail(), user2.getEmail());
        assertEquals(user1.getApiKey(), user2.getApiKey());
        assertEquals(user1.isAdmin(), user2.isAdmin());

    }

    @Override
    protected void tearDown() throws Exception {
        connection.prepareStatement("SHUTDOWN;").execute();

    }
}
