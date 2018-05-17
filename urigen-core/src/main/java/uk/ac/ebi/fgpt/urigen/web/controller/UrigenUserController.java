package uk.ac.ebi.fgpt.urigen.web.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.fgpt.urigen.exception.UserCreateException;
import uk.ac.ebi.fgpt.urigen.model.UrigenUser;
import uk.ac.ebi.fgpt.urigen.service.BrowserIdService;
import uk.ac.ebi.fgpt.urigen.service.GitHubIdService;
import uk.ac.ebi.fgpt.urigen.service.UrigenUserService;
import uk.ac.ebi.fgpt.urigen.web.view.ActionResponseBean;
import uk.ac.ebi.fgpt.urigen.web.view.UserBean;
import uk.ac.ebi.fgpt.urigen.web.view.UserNotFoundException;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Simon Jupp
 * @date 04/01/2012
 * Functional Genomics Group EMBL-EBI
 */
@Controller
@RequestMapping("/users")
public class UrigenUserController {

    private UrigenUserService userService;
    private GitHubIdService gitHubIdService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    protected Logger getLog() {
        return log;
    }

    public UrigenUserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(UrigenUserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setGitHubService(GitHubIdService gitHubIdService) {
        this.gitHubIdService = gitHubIdService;
    }

    public GitHubIdService getGitHubIdService() {
        return gitHubIdService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody UrigenUser getUser(@PathVariable int id) {
        return getUserService().getUser(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Collection<UserBean> getUsers( @RequestParam(value = "restApiKey", required = false) String restApiKey) {

        Set<UserBean> users = new HashSet<>();
        for (UrigenUser u : getUserService().getUsers()) {

            UserBean ub = getUserByRestApiKey(restApiKey);
            if (restApiKey != null &&
                    !restApiKey.equals("") &&
                    ub != null &&
                    ub.getAdmin()) {
                users.add(
                        new UserBean(
                                u.getId(),
                                u.getUserName(),
                                u.getEmail(),
                                u.getApiKey(),
                                u.isAdmin()
                        ));
            }
            else {
                users.add(
                        new UserBean(
                                u.getId(),
                                u.getUserName(),
                                u.getEmail(),
                                null,
                                u.isAdmin()
                        ));
            }
        }
        return users;
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody UserBean createUser(@RequestBody UserBean userBean,
                                             @RequestParam(value = "email", required = false) String email,
                                             @RequestParam(value = "restApiKey", required = false) String restApiKey) throws UserCreateException {


        if (getUserService().getUsers().size() == 0) {
            getLog().debug("No users exist, so creating admin account for e-mail: " + userBean.getEmail() );
            UrigenUser user = getUserService().createNewUser(userBean.getUserName(),
                    userBean.getEmail(),
                    userBean.getAdmin());
            return getUserBeanFromUser(user);
        }
        else if (email != null && getUserByEmail(email).getAdmin()) {
            getLog().debug("Admin creating new user for " + userBean.getEmail() );
            if (getUserService().getUserByUserName(userBean.getUserName()) == null) {

                return getUserBeanFromUser(getUserService().createNewUser(userBean.getUserName(),
                        userBean.getEmail(),
                        userBean.getAdmin()));
            }
            else {
                getLog().error("Failed to create new user; found existing user with username: " + userBean.getUserName());

                throw new UserCreateException("Unable to create new user with username: " + userBean.getUserName());
            }
        }
        else if (restApiKey != null && getUserByRestApiKey(restApiKey).getAdmin()) {
            if (getUserService().getUserByUserName(userBean.getUserName()) == null) {

                return getUserBeanFromUser(getUserService().createNewUser(userBean.getUserName(),
                        userBean.getEmail(),
                        userBean.getAdmin()));
            }
            else {
                getLog().error("Failed to create new user; found existing user with username: " + userBean.getUserName());

                throw new UserCreateException("Unable to create new user with username: " + userBean.getUserName());
            }

        }
        else {
            getLog().error("Only admins can request new users, failed to create username: " + userBean.getUserName() );
            throw new UserCreateException("Only admins can request new users, failed to create user: " + userBean.getUserName());
        }
    }

    private UserBean getUserBeanFromUser(UrigenUser user) {
        return new UserBean(user.getId(), user.getUserName(), user.getEmail(), user.getApiKey(), user.isAdmin());
    }

    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody UrigenUser updateUser(@RequestBody UserBean userBean,
                                               @RequestParam(value = "restApiKey", required = false) String restApiKey) throws UserCreateException {



        // only admins or the actual user can update a particular user
        UserBean requestingUser = getUserByRestApiKey(restApiKey);

        if (requestingUser == null) {
            getLog().error("Failed to get user with api key: " + userBean.getApiKey());
            throw new UserCreateException("Failed to update user username: " + userBean.getUserName());
        }
        else if (requestingUser.getAdmin() || requestingUser.getApiKey().equals(restApiKey)) {

            UrigenUser updatedUser = getUserService().getUser(userBean.getId());
            updatedUser.setUserName(userBean.getUserName());
            updatedUser.setEmail(userBean.getEmail());
            updatedUser.setAsAdmin(userBean.getAdmin());
            return getUserService().updateUser(updatedUser);

        }
        else {
            getLog().error("Failed to update user: " + userBean.getUserName());
            throw new UserCreateException("Failed to update user username: " + userBean.getUserName());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody
    ActionResponseBean removeUser(@RequestBody UserBean userBean,
                                  @RequestParam(value = "restApiKey", required = false) String restApiKey) throws UserCreateException {

        // only admins can remove a particular user
        UserBean user = getUserByRestApiKey(restApiKey);

        if (user == null) {
            getLog().error("Failed to get user with api key: " + userBean.getApiKey());
            throw new UserCreateException("Failed to update user username: " + userBean.getUserName());
        }
        else if (user.getAdmin()) {

            getUserService().removeUser(getUser(userBean.getId()));
            return new ActionResponseBean("Removed user: " + userBean.getId());

        }
        else {
            getLog().error("Failed to update user: " + userBean.getUserName());
            throw new UserCreateException("Failed to update user username: " + userBean.getUserName());
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public @ResponseBody UserBean loginUser(
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "code", required = false) String code) throws UserNotFoundException, AuthenticationException {

        UserBean userBean = null;
        if (code != null && state != null) {
            for (String email : getGitHubIdService().getEmailFromGithub(code, state)) {
                if (email != null) {
                    userBean = getUserByEmail(email);
                    if (userBean!= null) {
                        return userBean;
                    }
                }
            }
        }
        throw new UserNotFoundException();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(value= HttpStatus.UNAUTHORIZED, reason="User Not Found") //401
    public @ResponseBody String githubLoginException(HttpServletRequest request, Exception ex) throws IOException {

        ExceptionJSONInfo response = new ExceptionJSONInfo();
        response.setUrl(request.getRequestURL().toString());
        response.setMessage(ex.getMessage());

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="User Not Found") //404
    public @ResponseBody String handleEmployeeNotFoundException(HttpServletRequest request, Exception ex) throws IOException {

        ExceptionJSONInfo response = new ExceptionJSONInfo();
        response.setUrl(request.getRequestURL().toString());
        response.setMessage(ex.getMessage());

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(response);
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public @ResponseBody UserBean searchForUser(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "restApiKey", required = false) String restApiKey) throws AuthenticationException {

        if (restApiKey != null) {
            return getUserByRestApiKey(restApiKey);
        }
        else if (email != null) {
            UserBean b = getUserByEmail(email);
            if (b != null) {
                b.setApiKey("");
            }
            return b;
        }
        else {
            throw new IllegalArgumentException("Must supply at least the email address of the user to query for");
        }
    }

    private String getApiKeyByBrowserId(String browserId, String host) {
        return BrowserIdService.getApiKeyByBrowserId(browserId, host);  //To change body of created methods use File | Settings | File Templates.
    }


    public UserBean getUserByEmail(String email) {
        getLog().debug("Attempting to acquire user with email " + email);

        try {
            String decodedEmail = URLDecoder.decode(email, "UTF-8");
            UrigenUser u = getUserService().getUserByEmail(decodedEmail);
            if (u != null) {
                return getUserBeanFromUser(u);
            }
        }
        catch (UnsupportedEncodingException e) {
            return null;
        }
        return null;
    }

//    @RequestMapping(value = "/{userID}/restApiKey", method = RequestMethod.GET)
//    public @ResponseBody
//    RestApiKeyResponseBean getRestApiKeyForUser(@PathVariable String userID) {
//        getLog().debug("Requesting REST API key for user " + userID);
//        UrigenUser user = getUserService().getUser(userID);
//        getLog().debug("Got user " + user.getEmail());
//        return new RestApiKeyResponseBean(user);
//    }


    public UserBean getUserByRestApiKey (String restApiKey) {
        getLog().debug("Getting user by api key: " + restApiKey);
        try {
            UrigenUser b = getUserService().getUserByApiKey(restApiKey);
            if (b != null) {
                return getUserBeanFromUser(getUserService().getUserByApiKey(restApiKey));
            }
        }
        catch (IllegalArgumentException e) {
            getLog().debug("No such user for api key: " + restApiKey);
            return null;
        }
        getLog().debug("No such user for api key: " + restApiKey);
        return null;
    }


    private class ExceptionJSONInfo {
        String url;
        String message;

        public ExceptionJSONInfo(String url, String message) {
            this.url = url;
            this.message = message;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ExceptionJSONInfo() {

        }
    }
}
