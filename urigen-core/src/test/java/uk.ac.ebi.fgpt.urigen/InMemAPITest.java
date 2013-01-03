package uk.ac.ebi.fgpt.urigen;

import uk.ac.ebi.fgpt.urigen.dao.*;
import junit.framework.TestCase;
import uk.ac.ebi.fgpt.urigen.exception.PreferenceCreationException;
import uk.ac.ebi.fgpt.urigen.exception.UrigenException;
import uk.ac.ebi.fgpt.urigen.exception.UserCreateException;
import uk.ac.ebi.fgpt.urigen.impl.*;
import uk.ac.ebi.fgpt.urigen.model.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Simon Jupp
 * @date 23/02/2012
 * Functional Genomics Group EMBL-EBI
 */
public class InMemAPITest extends TestCase {

    PreferencesDAO pDAO = new InMemPreferencesDAO();
    UrigenEntityDAO eDAO = new InMemUrigenEntityDAO();
    UserDAO uDAO = new InMemUserDAO();
    UrigenManager uriGenManager;

    Set<UrigenPreference> validPrefs = new HashSet<UrigenPreference>();
    Set<UrigenUser> validUser = new HashSet<UrigenUser>();

    UrigenUserRange userRange1;


    public void setUp() {

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


        validPrefs.add(pref1);
        validPrefs.add(pref2);
        validPrefs.add(pref3);
        validPrefs.add(pref4);
        validPrefs.add(pref5);


        UrigenUser user1 = new SimpleUrigenUserImpl("simon", "jupp@ebi.ac.uk", Long.toString(System.currentTimeMillis()), true);


        for (UrigenPreference prefs : validPrefs) {
            try {
                UrigenPreference newP = pDAO.savePreferences(prefs);
                if (newP == null) {
                    fail("Failed to create prefs:" +  prefs.toString());
                }
            } catch (PreferenceCreationException e) {
                fail("Failed to create prefs");
            }
        }

        validUser.add(user1);
        for (UrigenUser u : validUser) {
            try {
                UrigenUser newU = uDAO.saveUser(user1);
                if (newU == null) {
                    fail("Failed to create user: " + user1.toString());
                }
            } catch (UserCreateException e) {
                fail("Failed to create user");
            }
        }

        try {
            userRange1 = new UrigenUserRangeImpl(uDAO.getUserByUserName("simon").getId(),pDAO.savePreferences(pref6).getPreferenceId(), 400, 700);
            pref6.getAllUserRange().add(userRange1);
            pDAO.update(pref6);

        } catch (PreferenceCreationException e) {
            e.printStackTrace();
        }

//        rDAO.save(userRange1);

        uriGenManager = new UrigenManagerImpl(pDAO, eDAO, uDAO, new OWLAPIOntologyDAO());






    }

    public void testLoadedPreferences () {

        System.out.println("Testing loaded preferences");
        for (UrigenPreference prefs : validPrefs) {
            UrigenPreference newP = pDAO.getPreferencesById(prefs.getPreferenceId());
            assertEquals(newP.getOntologyName(), prefs.getOntologyName());
            // todo repeat for all values
        }
    }

    public void testLoadedUsers() {
        System.out.println("Testing loaded users");
        for (UrigenUser u : validUser) {
            UrigenUser newUser = uDAO.getUser(u.getId());
            assertEquals(newUser.getUserName(), u.getUserName());
            assertEquals(newUser.getEmail(), u.getEmail());
            assertEquals(newUser.isAdmin(), u.isAdmin());
            assertEquals(newUser.getApiKey(), u.getApiKey());
        }
    }

    public void testGetEntity() {

        for (UrigenUser u : uDAO.getUsers()) {
            for (UrigenPreference p : pDAO.getAllPreferences()) {

                UrigenRequest request1 = new SimpleUrigenRequestImpl(u.getId(), "", p.getPreferenceId(), "", "");
                try {
                    System.out.println(p.toString());
                    for (int x = 0; x <100; x++) {
                        uriGenManager.getNewEntity(request1);
                    }
                } catch (UrigenException e) {
                    fail(e.getMessage());
                }

            }

        }

    }



}
