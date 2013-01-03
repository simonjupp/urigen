package uk.ac.ebi.fgpt.urigen.web.view;

/**
 * @author Simon Jupp
 * @date 11/01/2012
 * Functional Genomics Group EMBL-EBI
 */
public class PreferenceTypeBean {

    String classid;
    String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassid() {

        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public PreferenceTypeBean(String classid, String description) {

        this.classid = classid;
        this.description = description;
    }
}
