package uk.ac.ebi.fgpt.urigen.impl;

import uk.ac.ebi.fgpt.urigen.model.UrigenRequest;

/**
 * @author Simon Jupp
 * @date 16/12/2011
 * Functional Genomics Group EMBL-EBI
 */
public class SimpleUrigenRequestImpl implements UrigenRequest{

    private final int userId;
    private final String localUri;
    private final int preferencesId;
    private final String label;
    private final String comment;

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
