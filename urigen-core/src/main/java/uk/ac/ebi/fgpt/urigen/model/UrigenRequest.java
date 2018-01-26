package uk.ac.ebi.fgpt.urigen.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author Simon Jupp
 * @date 15/12/2011
 * Functional Genomics Group EMBL-EBI
 */
@JsonSerialize(typing = JsonSerialize.Typing.STATIC)
public interface UrigenRequest {

    int getUserId();

    String getLocalUri();

    int getPreferencesId();

    String getLabel();

    String getComment();

}
