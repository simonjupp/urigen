package uk.ac.ebi.fgpt.urigen.web.view;

import uk.ac.ebi.fgpt.urigen.model.UrigenRequest;

import java.io.Serializable;

/**
 * @author Simon Jupp
 * @date 28/02/2012
 * Functional Genomics Group EMBL-EBI
 */
public class UrigenRequestBean implements UrigenRequest, Serializable {
    private int userId;
    private String localUri;
    private int preferencesId;
    private String label;
    private String comment;

    public UrigenRequestBean(int userId, String localUri, int preferencesId, String label, String comment) {
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

    public void setLocalUri(String localUri) {
        this.localUri = localUri;
    }

    public void setPreferencesId(int preferencesId) {
        this.preferencesId = preferencesId;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UrigenRequestBean() {

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

    @Override
    public String toString() {
        return "UrigenRequestBean{" +
                "userId=" + userId +
                ", localUri='" + localUri + '\'' +
                ", preferencesId=" + preferencesId +
                ", label='" + label + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    public void setUserId(int id) {
        this.userId = id;
    }
}
