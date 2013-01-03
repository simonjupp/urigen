package uk.ac.ebi.fgpt.urigen.dao;

import uk.ac.ebi.fgpt.urigen.model.UrigenUser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Jupp
 * @date 03/01/2012
 * Functional Genomics Group EMBL-EBI
 */
public class InMemUserDAO implements UserDAO {

    private Map<Integer, UrigenUser> users;
    private int id = 0;

    public InMemUserDAO () {
        users = new HashMap<Integer, UrigenUser>();
    }

    public UrigenUser getUser(int userId) {
        return users.get(userId);
    }

    public UrigenUser getUserByApiKey(String apiKey) {
        for (UrigenUser u : users.values()) {
            if (u.getApiKey().equals(apiKey)) {
                return u;
            }
        }
        return null;
    }

    public Collection<UrigenUser> getUsers() {
        return users.values();
    }

    public UrigenUser saveUser(UrigenUser user) {
        user.setId(id);
        users.put(id, user);
        id++;
        return user;
    }

    public UrigenUser updateUser(UrigenUser user) {
        users.put(user.getId(), user);
        return user;
    }

    public void removeUser(UrigenUser user) {
        users.remove(user.getId());
    }

    public boolean contains(int userId) {
        return users.containsKey(userId);
    }

    public boolean isAdmin(UrigenUser user) {
        return user.isAdmin();
    }

    public UrigenUser getUserByUserName(String userName) {
        for (UrigenUser u : users.values()) {
            if (u.getUserName().equals(userName)) {
                return u;
            }
        }
        return null;
    }

    public UrigenUser getUserByEmail(String email) {
        for (UrigenUser u : users.values()) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }
}
