package uk.ac.ebi.fgpt.urigen.impl;

import uk.ac.ebi.fgpt.urigen.model.UrigenRequest;

import java.net.URI;

/**
 * @author Simon Jupp
 * @date 16/12/2011
 * Functional Genomics Group EMBL-EBI
 */
public class SimpleUrigenRequestImpl implements UrigenRequest{

    private int userId;
    private String localUri;
    private int preferencesId;
    private String label;
    private String comment;

    public SimpleUrigenRequestImpl(int userId, String localUri, int preferencesId, String label, String comment) {
        this.userId = userId;
        this.localUri = localUri;
        this.preferencesId = preferencesId;
        this.label = label;
        this.comment = comment;
    }

    public int getUserId() {
        return userId;
    }

    public String getLocalUri() {
        return localUri;
    }

    public int getPreferencesId() {
        return preferencesId;
    }

    public String getLabel() {
        return label;
    }

    public String getComment() {
        return comment;
    }

}
