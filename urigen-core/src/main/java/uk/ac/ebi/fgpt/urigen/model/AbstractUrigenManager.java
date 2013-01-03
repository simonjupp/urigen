package uk.ac.ebi.fgpt.urigen.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.fgpt.urigen.dao.*;
import uk.ac.ebi.fgpt.urigen.exception.*;
import uk.ac.ebi.fgpt.urigen.impl.GeneratorTypes;

import java.util.Collection;

/**
 * @author Simon Jupp
 * @date 16/12/2011
 * Functional Genomics Group EMBL-EBI
 */
public abstract class AbstractUrigenManager implements UrigenManager {

    private Logger log = LoggerFactory.getLogger(getClass());

    private PreferencesDAO preferenceDAO;
    private UrigenEntityDAO urigenEntityDAO;
    private UserDAO userDAO;
    private OWLOntologyDAO ontoloyDAO;

    public AbstractUrigenManager () {
    }

    public AbstractUrigenManager(PreferencesDAO preferenceDAO,
                                 UrigenEntityDAO urigenEntityDAO,
                                 UserDAO userDAO,
                                 OWLOntologyDAO ontologyDAO) {
        this.preferenceDAO = preferenceDAO;
        this.urigenEntityDAO = urigenEntityDAO;
        this.userDAO = userDAO;
        this.ontoloyDAO = ontologyDAO;
    }


    public GeneratorTypes[] getGeneratorTypes() {
        return GeneratorTypes.values();
    }

    public PreferencesDAO getPreferencesDAO() {
        return preferenceDAO;
    }

    public UrigenEntityDAO getUrigenEntityDAO() {
        return urigenEntityDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setOntoloyDAO(OWLOntologyDAO ontoloyDAO) {
        this.ontoloyDAO = ontoloyDAO;
    }

    public void setPreferenceDAO(PreferencesDAO preferenceDAO) {
        this.preferenceDAO = preferenceDAO;
    }

    public void setUrigenEntityDAO(UrigenEntityDAO urigenEntityDAO) {
        this.urigenEntityDAO = urigenEntityDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Collection<UrigenPreference> getPreferences() {
        return preferenceDAO.getAllPreferences();
    }

    public Collection<UrigenEntity> getAllGeneratedUris() {
        return urigenEntityDAO.getAllEntities();
    }

    public UrigenPreference getPreferencesById(int id) {
        return preferenceDAO.getPreferencesById(id);
    }

    public Collection<UrigenUser> getUsers() {
        return userDAO.getUsers();
    }

    public UrigenUser getUserById(int id) {
        return userDAO.getUser(id);
    }

    public UrigenPreference savePreference(UrigenPreference prefs) throws PreferenceCreationException {
        return preferenceDAO.savePreferences(prefs);
    }

    public UrigenUser saveUser(UrigenUser user) throws UserCreateException {
        return userDAO.saveUser(user);
    }

    public UrigenUser getUserByUserName(String userName) {
        return userDAO.getUserByUserName(userName);
    }

    public UrigenUser getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    public UrigenUser updateUser(UrigenUser user) throws UserCreateException {
        return userDAO.updateUser(user);
    }

    public UrigenUser getUserByApiKey(String apiKey) {
        return userDAO.getUserByApiKey(apiKey);
    }

    public void removeUser(UrigenUser user) {
        userDAO.removeUser(user);
    }

    public void removePreference(UrigenPreference preference) {
        preferenceDAO.removePref(preference);

    }

    public UrigenPreference updatePreference(UrigenPreference preference) throws PreferenceCreationException {
        return preferenceDAO.update(preference);
    }

    public UrigenEntity getNewEntity(UrigenRequest request) throws AutoIDException, OntologyClashException {

        UrigenPreference p = preferenceDAO.getPreferencesById(request.getPreferencesId());
        UrigenUser u = userDAO.getUser(request.getUserId());
        if (isValidUser(u.getId()) && isValidPreference(p.getPreferenceId())) {

            log.info("request received, valid user and preference id: " + u.getId() + ", " + p.getPreferenceId());
            AutoIDGenerator generator = p.getAutoIDGenerator();
            log.debug("generator type:" + generator.getDescription());
            generator.setPreferencesDao(getPreferencesDAO());
            generator.setUrigenEntityDao (getUrigenEntityDAO());
            generator.setOWLOntologyDao(getOntologyDAO());

            UrigenEntity uri_fragment = p.getAutoIDGenerator().getNextID(request, getOntologyDAO());

            if (p.getCheckSource()) {
                if (ontoloyDAO.ontologyContainsUri(p.getOntologyUri(), uri_fragment.getGeneratedUri())) {
                    throw new OntologyClashException("The new URI already exists in the source ontology: " + uri_fragment.getGeneratedUri().toString());
                }
            }
            return uri_fragment;

        }

        throw new AutoIDException("Unable to generate new ID");
    }

    public OWLOntologyDAO getOntologyDAO() {
        return ontoloyDAO;
    }

    public boolean isValidPreference(int preferencesId) {
        return preferenceDAO.contains(preferencesId);
    }

    public boolean isValidUser(int userId) {
        return userDAO.contains(userId);
    }
}
