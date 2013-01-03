package uk.ac.ebi.fgpt.urigen.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.net.URI;
import java.util.Date;

/**
 * @author Simon Jupp
 * @date 13/12/2011
 * Functional Genomics Group EMBL-EBI
 */
@JsonSerialize(typing = JsonSerialize.Typing.STATIC)
public interface UrigenEntity {


    int getId ();

    String getGeneratedUri();

    String getOriginalUri();

    String getShortForm();

    String getLabel();

    String getComment();

    int getUserId();

    int getPreferencesId();

    Date getDate();

    void setId(int id);




}
