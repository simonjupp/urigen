package uk.ac.ebi.fgpt.urigen.web.view;

/**
 * @author Simon Jupp
 * @date 04/01/2012
 * Functional Genomics Group EMBL-EBI
 */
public class ActionResponseBean {

    private final String statusMessage;

    public ActionResponseBean (String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

}
