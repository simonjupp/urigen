package uk.ac.ebi.fgpt.urigen.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.fgpt.urigen.dao.UserDAO;
import uk.ac.ebi.fgpt.urigen.exception.UserCreateException;
import uk.ac.ebi.fgpt.urigen.impl.SimpleUrigenUserImpl;
import uk.ac.ebi.fgpt.urigen.model.UrigenManager;
import uk.ac.ebi.fgpt.urigen.model.UrigenUser;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

/**
 * @author Simon Jupp
 * @date 04/01/2012
 * Functional Genomics Group EMBL-EBI
 */
public class DefaultUrigenUserService implements UrigenUserService {

    private UrigenManager manager;

    private Logger log = LoggerFactory.getLogger(getClass());

    protected Logger getLog() {
        return log;
    }

    public void setUrigenManager (UrigenManager manager) {
        this.manager = manager;
    }

    public UrigenUser createNewUser(String userName, String email, boolean admin) {
        getLog().debug("Creating new user for " + userName);

        String newApiKey = generateRestApiKey(email);
        SimpleUrigenUserImpl user = new SimpleUrigenUserImpl(userName, email, newApiKey, admin);
        getLog().debug("Generated new user: " + user.toString());
        try {
            return manager.saveUser(user);
        } catch (UserCreateException e) {
            getLog().error("Error creating user: " + e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }

    public UrigenUser getUser(int id) {
        return manager.getUserById(id);
    }

    public UrigenUser getUserByUserName(String userName) {
        getLog().debug("Getting user by username " + userName);
        UrigenUser u = manager.getUserByUserName(userName);
        if (u == null) {
            getLog().debug("No user : " + userName);
            return null;
        }
        else {
            getLog().debug("Found user: " + u.toString());
        }
        return u;
    }

    public UrigenUser getUserByEmail(String email) {
        getLog().debug("Getting user by email " + email);
        UrigenUser u = manager.getUserByEmail(email);
        if (u == null) {
            getLog().debug("No user : " + email);
            return null;
        }
        else {
            getLog().debug("Found user: " + u.toString());
            return u;
        }

    }

    public UrigenUser getUserByApiKey(String apiKey) {
        return manager.getUserByApiKey(apiKey);
    }

    public Collection<UrigenUser> getUsers() {
        return manager.getUsers();
    }

    public UrigenUser updateUser(UrigenUser user) {
        try {
            return manager.updateUser(user);
        } catch (UserCreateException e) {
            getLog().debug("Error updating user: " + e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean isAdmin(UrigenUser user) {
        return manager.getUserById(user.getId()).isAdmin();
    }

    public void removeUser(UrigenUser user) {
        manager.removeUser(user);
    }

    protected String generateRestApiKey(String email) {
        String timestamp = Long.toString(System.currentTimeMillis());
        getLog().debug("Generating new REST API key for " + email);
        String keyContent = email + timestamp;
        try {
            // encode the email using SHA-1
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] digest = messageDigest.digest(keyContent.getBytes("UTF-8"));

            // now translate the resulting byte array to hex
            String restKey = getHexRepresentation(digest);
            getLog().debug("REST API key was generated: " + restKey);
            return restKey;
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 not supported!");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not available, required to generate REST api key");
        }
    }

    protected String getHexRepresentation(byte[] raw) {
        if (raw == null) {
            return null;
        }
        final String hexes = "0123456789ABCDEF";
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(hexes.charAt((b & 0xF0) >> 4)).append(hexes.charAt((b & 0x0F)));
        }
        return hex.toString();
    }


}
