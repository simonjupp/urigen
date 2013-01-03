package uk.ac.ebi.fgpt.urigen.service;

import uk.ac.ebi.fgpt.urigen.model.UrigenUser;

import java.util.Collection;

/**
 * @author Simon Jupp
 * @date 03/01/2012
 * Functional Genomics Group EMBL-EBI
 */
public interface UrigenUserService {

    UrigenUser createNewUser(String userName,
                             String email,
                             boolean admin);

    UrigenUser getUser(int id);

    UrigenUser getUserByUserName(String userName);

    UrigenUser getUserByEmail(String email);

    UrigenUser getUserByApiKey(String apiKey);

    Collection<UrigenUser> getUsers();

    UrigenUser updateUser(UrigenUser user);

    boolean isAdmin (UrigenUser user);

    void removeUser(UrigenUser user);

}
