package uk.ac.ebi.fgpt.urigen.dao;

import uk.ac.ebi.fgpt.urigen.exception.UserCreateException;
import uk.ac.ebi.fgpt.urigen.model.UrigenUser;

import java.util.Collection;

/**
 * @author Simon Jupp
 * @date 15/12/2011
 * Functional Genomics Group EMBL-EBI
 */
public interface UserDAO {

    UrigenUser getUser(int userId);

    UrigenUser getUserByApiKey(String apiKey);

    Collection<UrigenUser> getUsers();

    UrigenUser saveUser(UrigenUser user) throws UserCreateException;

    UrigenUser updateUser(UrigenUser user) throws UserCreateException;

    void removeUser(UrigenUser user);

    boolean contains(int userId);

    boolean isAdmin(UrigenUser user);

    UrigenUser getUserByUserName(String userName);

    UrigenUser getUserByEmail(String email);


}
