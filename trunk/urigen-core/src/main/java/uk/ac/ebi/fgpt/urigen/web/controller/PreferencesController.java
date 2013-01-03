package uk.ac.ebi.fgpt.urigen.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.fgpt.urigen.exception.PreferenceCreationException;
import uk.ac.ebi.fgpt.urigen.exception.UrigenException;
import uk.ac.ebi.fgpt.urigen.impl.GeneratorTypes;
import uk.ac.ebi.fgpt.urigen.model.UrigenPreference;
import uk.ac.ebi.fgpt.urigen.model.UrigenUser;
import uk.ac.ebi.fgpt.urigen.service.PreferencesService;
import uk.ac.ebi.fgpt.urigen.service.UrigenUserService;
import uk.ac.ebi.fgpt.urigen.web.view.PreferenceBean;
import uk.ac.ebi.fgpt.urigen.web.view.PreferenceTypeBean;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Simon Jupp
 * @date 10/01/2012
 * Functional Genomics Group EMBL-EBI
 */
@Controller
@RequestMapping("/preferences")
public class PreferencesController {

    private Logger log = LoggerFactory.getLogger(getClass());

    protected Logger getLog() {
        return log;
    }

    private UrigenUserService userService;

    private PreferencesService prefsService;

    public PreferencesService getPreferenceService() {
        return prefsService;
    }

    @Autowired
    public void setPreferencesService(PreferencesService prefsService) {
        this.prefsService = prefsService;
    }


    public UrigenUserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(UrigenUserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    UrigenPreference getPreference(@PathVariable int id) {
        return getPreferenceService().getPreference(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Collection<UrigenPreference> getPreferences() {
        return getPreferenceService().getAllPreferences();
    }

    @RequestMapping(value = "/types", method = RequestMethod.GET)
    public @ResponseBody
    Collection<PreferenceTypeBean> getPreferencesTypes() {
        Collection<PreferenceTypeBean> types = new HashSet<PreferenceTypeBean>();
        for (GeneratorTypes type : getPreferenceService().getGeneratorTypes()) {
            types.add(new PreferenceTypeBean(type.getClassId(), type.getDescription()));
        }
        return types;
    }


    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    UrigenPreference createPreferences(@RequestBody PreferenceBean prefsBean,
                                       @RequestParam(value = "restApiKey", required = false) String restApiKey,
                                       @RequestParam(value = "email", required = false) String email) throws UrigenException {

        getLog().debug("attempting to create new prefs from : " + prefsBean.toString());

        if (email == null && restApiKey == null) {
            getLog().error("No email or rest api key provided");
            return new PreferenceBean(false, "Only admins can create new preferences");
        }

        UrigenUser user = getUser(restApiKey, email);

        if (user == null || !user.isAdmin()) {
            getLog().error("Only admin users can create new preferences");
            return new PreferenceBean(false, "Only admins can create new preferences");
        }
        else if (user.isAdmin())  {
            try {
                UrigenPreference bean = getPreferenceService().createNewPreference(prefsBean);
                getLog().debug("New preference created: " + bean.toString());
                return bean;
            } catch (PreferenceCreationException e) {
                return new PreferenceBean(false, e.getMessage());
            }
        }
        else {
            getLog().error("No rest API key or users when updating preferences bean");
            throw  new UrigenException();
        }
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody
    UrigenPreference removePreferences(@PathVariable int id,
                                       @RequestParam(value = "restApiKey", required = false) String restApiKey,
                                       @RequestParam(value = "email", required = false) String email) throws UrigenException {

        getLog().debug("attempting to delete prefs with id : " + id);

        if (email == null && restApiKey == null) {
            getLog().error("No email or rest api key provided");
            return new PreferenceBean(false, "Only admins can update preferences");
        }

        UrigenUser user = getUser(restApiKey, email);

        if (user == null || !user.isAdmin()) {
            getLog().error("Only admin users can create new preferences");
            return new PreferenceBean(false, "Only admins can create new preferences");
        }
        else if (user.isAdmin())  {

            getPreferenceService().removePreference(getPreference(id));
            getLog().debug("preference deleted:" + id);
            return new PreferenceBean(true, "");
        }
        else {
            getLog().error("No rest API key or users when deleting preferences bean");
            throw  new UrigenException();
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody
    UrigenPreference updatePreferences(@RequestBody PreferenceBean prefsBean,
                                       @RequestParam(value = "restApiKey", required = false) String restApiKey,
                                       @RequestParam(value = "email", required = false) String email) throws UrigenException {

        getLog().debug("attempting to update prefs from : " + prefsBean.toString());

        if (email == null && restApiKey == null) {
            getLog().error("No email or rest api key provided");
            return new PreferenceBean(false, "Only admins can update preferences");
        }

        UrigenUser user = getUser(restApiKey, email);

        if (user == null || !user.isAdmin()) {
            getLog().error("Only admin users can update preferences");
            return new PreferenceBean(false, "Only admins can update preferences");
        }
        else if (user.isAdmin())  {
            try {
                UrigenPreference bean = getPreferenceService().updatePreference(prefsBean);
                getLog().debug("preference updated: " + bean.toString());

                return bean;
            } catch (PreferenceCreationException e) {
                return new PreferenceBean(false, e.getMessage());
            }
        }
        else {
            getLog().error("No rest API key or users when creating preferences bean");
            throw  new UrigenException();
        }
    }


    private UrigenUser getUser(String restApiKey, String email) {
        UrigenUser user = getUserService().getUserByEmail(email);
        if (user == null ) {
            return  getUserService().getUserByApiKey(restApiKey);
        }
        return user;
    }


//    @RequestMapping(value = "/{id}/range", method = RequestMethod.POST)
//    public @ResponseBody
//    UrigenUserRange createUserRange(@RequestBody UrigenUserRange rangeBean,
//                                    @RequestParam(value = "restApiKey", required = false) String restApiKey,
//                                    @RequestParam(value = "email", required = false) String email) throws UrigenException {
//
//        getLog().debug("attempting to create user range for : " + rangeBean.toString());
//
//        if (email == null && restApiKey == null) {
//            getLog().error("No email or rest api key provided");
//            return new UrigenUserRangeBean(false, "Only admins can create new user ranges");
//        }
//
//        UrigenUser user = getUser(restApiKey, email);
//
//        if (user == null || !user.isAdmin()) {
//            getLog().error("Only admin users can create new ranges");
//            return new UrigenUserRangeBean(false, "Only admins can create new ranges");
//        }
//        else if (user.isAdmin())  {
//            getLog().debug("Admin user adding new user range: " + user.getUserName() );
//            getLog().debug("Creating new user range for ontology: " + rangeBean.toString());
//            try {
//                UrigenUserRange bean = getPreferenceService().createNewUserRange(rangeBean);
//                getLog().debug("Creating new user range: "+ bean.toString());
//                return bean;
//            } catch (PreferenceCreationException e) {
//                return new UrigenUserRangeBean(false, e.getMessage());
//            }
//        }
//        else {
//            getLog().error("No rest API key or users when creating use range bean");
//            throw  new UrigenException();
//        }
//    }
//
//    @RequestMapping(value = "/{id}/range", method = RequestMethod.PUT)
//    public @ResponseBody
//    UrigenUserRange updateUserRange(@RequestBody UrigenUserRange rangeBean,
//                                    @RequestParam(value = "restApiKey", required = false) String restApiKey,
//                                    @RequestParam(value = "email", required = false) String email) throws UrigenException {
//
//        getLog().debug("attempting to update user range for : " + rangeBean.toString());
//
//        if (email == null && restApiKey == null) {
//            getLog().error("No email or rest api key provided");
//            return new UrigenUserRangeBean(false, "Only admins can update user ranges");
//        }
//
//        UrigenUser user = getUser(restApiKey, email);
//
//        if (user == null || !user.isAdmin()) {
//            getLog().error("Only admin users can update ranges");
//            return new UrigenUserRangeBean(false, "Only admins can update ranges");
//        }
//        else if (user.isAdmin())  {
//            getLog().debug("Admin updating user range: " + user.getUserName() );
//            getLog().debug("Updating user range for ontology: " + rangeBean.toString());
//            try {
//                UrigenUserRange bean = getPreferenceService().updateUserRange(rangeBean);
//                getLog().debug("Updating user range: "+ bean.toString());
//                return bean;
//            } catch (PreferenceCreationException e) {
//                return new UrigenUserRangeBean(false, e.getMessage());
//            }
//        }
//        else {
//            getLog().error("No rest API key or users when updating user range bean");
//            throw  new UrigenException();
//        }
//    }


}
