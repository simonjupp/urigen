package uk.ac.ebi.fgpt.urigen.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author Simon Jupp
 * @date 13/12/2011
 * Functional Genomics Group EMBL-EBI
 */
@JsonSerialize(typing = JsonSerialize.Typing.STATIC)
public interface UrigenUser{

    int getId();

    String getUserName ();

    String getEmail();

    void setUserName(String userName);

    void setEmail(String email);

    void setAsAdmin(boolean isAdmin);

    @JsonIgnore
    String getApiKey();

    void setId(int id);

    boolean isAdmin();

}
