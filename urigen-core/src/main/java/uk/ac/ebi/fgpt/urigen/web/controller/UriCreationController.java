package uk.ac.ebi.fgpt.urigen.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.fgpt.urigen.exception.AutoIDException;
import uk.ac.ebi.fgpt.urigen.exception.OntologyClashException;
import uk.ac.ebi.fgpt.urigen.exception.UrigenException;
import uk.ac.ebi.fgpt.urigen.model.UrigenEntity;
import uk.ac.ebi.fgpt.urigen.model.UrigenUser;
import uk.ac.ebi.fgpt.urigen.service.PreferencesService;
import uk.ac.ebi.fgpt.urigen.service.UriCreationService;
import uk.ac.ebi.fgpt.urigen.service.UrigenUserService;
import uk.ac.ebi.fgpt.urigen.web.view.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Simon Jupp
 * @date 28/02/2012
 * Functional Genomics Group EMBL-EBI
 */
@Controller
@RequestMapping("/uris")
public class UriCreationController {

    private Logger log = LoggerFactory.getLogger(getClass());

    protected Logger getLog() {
        return log;
    }

    private UrigenUserService userService;

    private PreferencesService prefService;

    private UriCreationService uriService;

    public UriCreationService getUriCreationService() {
        return uriService;
    }

    public UrigenUserService getUserService() {
        return userService;
    }

    public PreferencesService getPreferencesService() {
        return prefService;
    }


    @Autowired
    public void setUserService(UrigenUserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUriCreationService(UriCreationService service) {
        this.uriService = service;
    }

    @Autowired
    public void setPreferencesService(PreferencesService pref ) {
        this.prefService = pref;
    }


    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Collection<UrigenEntity> getUris() {
        return getUriCreationService().getGeneratedUris();
    }


    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    UrigenEntityBean requestNewUrigenEntity(@RequestBody UrigenRequestBean urigenEntityBean,
                                            @RequestParam(value = "restApiKey", required = false) String restApiKey,
                                            @RequestParam(value = "email", required = false) String email) throws UrigenException {

        getLog().debug("requesting new URI: " + urigenEntityBean.toString());
        if (email == null && restApiKey == null) {
            getLog().error("No email or rest api key provided");
            return new UrigenEntityBean(false, "Only admins can create new preferences");
        }

        UrigenUser user = getUser(restApiKey, email);

        if (user == null) {
            getLog().error("Can't find user with id: " + urigenEntityBean.getUserId());
            return new UrigenEntityBean(false, "Only admins can create new preferences");
        }
        else {
            urigenEntityBean.setUserId(user.getId());
            UrigenEntity newEntity = null;
            try {
                newEntity = getUriCreationService().generateNewUri(urigenEntityBean);
                getLog().debug("New entity created: " + newEntity.toString());
                UrigenEntityBean bean = new UrigenEntityBean(newEntity.getGeneratedUri(),
                        newEntity.getOriginalUri(),
                        newEntity.getShortForm(),
                        newEntity.getLabel(),
                        newEntity.getUserId(),
                        newEntity.getPreferencesId(),
                        newEntity.getDate(),
                        newEntity.getComment());
                bean.setStatusOK(true);
                return bean;

            } catch (AutoIDException e) {
                getLog().error("Autoid creation exception: " + e.getLocalizedMessage() + " for bean: " + urigenEntityBean.toString());
                e.printStackTrace();
                return new UrigenEntityBean(false, e.getLocalizedMessage());
            } catch (OntologyClashException e) {
                getLog().error("Ontology id clash: " + urigenEntityBean.toString());
                e.printStackTrace();
                return new UrigenEntityBean(false, "Ontology id clash: try again or fix on server");
            }
        }
    }

    @RequestMapping(value = "/jdatatable",method = RequestMethod.GET)
    public @ResponseBody
    DataTableResponseBean requestJqueryEntityTable(HttpServletRequest request) throws UrigenException {

        if(request.getParameter("sEcho")!=null && request.getParameter("sEcho")!= "")
        {
            DataTableParamPojo param = new DataTableParamPojo();
            param.sEcho = request.getParameter("sEcho");
            param.sSearch = request.getParameter("sSearch");
            param.sColumns = request.getParameter("sColumns");
            param.iDisplayStart = Integer.parseInt( request.getParameter("iDisplayStart") );
            param.iDisplayLength = Integer.parseInt( request.getParameter("iDisplayLength") );
            param.iColumns = Integer.parseInt( request.getParameter("iColumns") );
//            param.iSortingCols = Integer.parseInt( request.getParameter("iSortingCols") );
//            param.iSortColumnIndex = Integer.parseInt(request.getParameter("iSortCol_0"));
//            param.sSortDirection = request.getParameter("sSortDir_0");


            DataTableResponseBean response = new DataTableResponseBean();
            List<UrigenEntity> entities = new ArrayList<UrigenEntity>(getUris());
            response.setiTotalRecords(String.valueOf(entities.size()));

            response.setiDisplayLength(String.valueOf(param.iDisplayLength));
            response.setiTotalDisplayRecords(String.valueOf(entities.size()));
            response.setsEcho(param.sEcho);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            int start =  param.getiDisplayStart();
            int end = param.getiDisplayLength() + start;

            String [][] aaData;

            int array_size = entities.size() - start;

            if (array_size > param.getiDisplayLength()) {
                array_size = param.getiDisplayLength();
            }

            aaData = new String [array_size][5];

            for (int x = 0; x < array_size; x++) {
                int j = start + x;

                if (entities.get(j).getGeneratedUri()!= null) {

                    aaData[x][0] = entities.get(j).getGeneratedUri();
                    aaData[x][1] = entities.get(j).getLabel();
                    aaData[x][2] = getPreferencesService().getPreference(entities.get(j).getPreferencesId()).getOntologyName();
                    aaData[x][3] = getUserService().getUser(entities.get(j).getUserId()).getUserName();
                    Date d = entities.get(j).getDate();
                    aaData[x][4] = formatter.format(d);
                }
            }

            response.setAaData(aaData);
            return response;
        }
        return new DataTableResponseBean();
    }

    private UrigenUser getUser(String restApiKey, String email) {
        UrigenUser user = getUserService().getUserByEmail(email);
        if (user == null ) {
            return  getUserService().getUserByApiKey(restApiKey);
        }
        return user;
    }
}
