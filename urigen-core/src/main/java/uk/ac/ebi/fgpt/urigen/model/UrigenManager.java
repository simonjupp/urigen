package uk.ac.ebi.fgpt.urigen.model;

import uk.ac.ebi.fgpt.urigen.exception.*;
import uk.ac.ebi.fgpt.urigen.impl.GeneratorTypes;

import java.util.Collection;

/**
 * @author Simon Jupp
 * @date 15/12/2011
 * Functional Genomics Group EMBL-EBI
 */
public interface UrigenManager {


    /**
     * Returns a collection of preferences stored in the manager
     *
     * @return a Collection<UrigenPreference>.
     */

    Collection<UrigenPreference> getPreferences();

    UrigenPreference getPreferencesById(int id);

    /**
     * Returns a collection of users stored in the manager
     *
     * @return a Collection<UrigenPreference>.
     */

    Collection<UrigenUser> getUsers();

    UrigenUser getUserById(int id);

    /*
     * Methods for adding new Users and preferecnes
     */

    UrigenPreference savePreference(UrigenPreference prefs) throws PreferenceCreationException;

    UrigenUser saveUser(UrigenUser user) throws UserCreateException;
//
//    /**
//     * Returns a collection of entities stored in the manager
//     *
//     * @return a Collection<UrigenPreference>.
//     */
//
    Collection<UrigenEntity> getAllGeneratedUris();
//
//    UrigenEntity getEntityById(int id);
//
//    UrigenEntity addNewEntity(UrigenEntity entity) throws UrigenException;

//    UserDAO getUserDAO();
//
//    UrigenEntityDAO getUrigenEntityDAO();
//
//    PreferencesDAO getPreferencesDAO();
//
//    OWLOntologyDAO getOntologyDAO();

    UrigenEntity getNewEntity(UrigenRequest request)  throws AutoIDException, OntologyClashException;

    boolean isValidPreference(int preferencesId);

    boolean isValidUser(int userId);

//    /**
//     * Returns a factory for creating Urigen objects
//     *
//     * @return a UrigenFactory.
//     */
//    UrigenFactory getUrigenFactory();

//    UserRangeDAO getUserRangeDAO();

    GeneratorTypes[] getGeneratorTypes();

    UrigenPreference updatePreference(UrigenPreference preference) throws PreferenceCreationException;

//    Collection<UrigenUserRange> getAllUserPrefRangeByPrefId(int preferenceId);
//
//    UrigenUserRange saveUserRange(UrigenUserRange userRange) throws PreferenceCreationException;
//
//    UrigenUserRange updateUserRange(UrigenUserRange userRange) throws PreferenceCreationException;

    UrigenUser getUserByUserName(String userName);

    UrigenUser getUserByEmail(String email);

    UrigenUser getUserByApiKey(String apiKey);

    UrigenUser updateUser(UrigenUser user) throws UserCreateException;

    void removeUser(UrigenUser user);

    void removePreference(UrigenPreference preference);
}
