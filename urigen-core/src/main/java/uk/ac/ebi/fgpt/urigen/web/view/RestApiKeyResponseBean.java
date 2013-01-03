package uk.ac.ebi.fgpt.urigen.web.view;

import uk.ac.ebi.fgpt.urigen.model.UrigenUser;

/**
 * @author Simon Jupp
 * @date 05/01/2012
 * Functional Genomics Group EMBL-EBI
 */
public class RestApiKeyResponseBean {
    private UrigenUser user;

    public RestApiKeyResponseBean(UrigenUser user) {
        this.user = user;
    }

    /**
     * Gets the actual value of the REST API key requested
     *
     * @return the rest api key
     */
    public String getRestApiKey() {
        return user.getApiKey();
    }

    /**
     * Gets the user this REST API key belongs to
     *
     * @return the user who holds the key contained in this bean
     */
    public UrigenUser getUser() {
        return user;
    }
}
