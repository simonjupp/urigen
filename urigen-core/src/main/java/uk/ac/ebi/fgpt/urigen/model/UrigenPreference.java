package uk.ac.ebi.fgpt.urigen.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Collection;
import java.util.Date;

/**
 * @author Simon Jupp
 * @date 13/12/2011
 * Functional Genomics Group EMBL-EBI
 */
@JsonSerialize(typing = JsonSerialize.Typing.STATIC)
public interface UrigenPreference {

    int getPreferenceId();

    String getOntologyName();

    String getOntologyPhysicalUri();

    String getOntologyUri();

    String getBaseUri();

    String getSeparator();

    AutoIDGenerator getAutoIDGenerator ();

    String getPrefix();

    String getSuffix();

    int getAutoIdDigitCount();

    int getAutoIdStart();

    int getAutoIdEnd();

    long getLastIdInSequence();

    boolean getCheckSource();

    boolean statusOK();

    String conflictMessage();

    void setPreferenceId(int id);

    void setLastIdInSequence(long lastId);

    Collection<UrigenUserRange> getAllUserRange();

    Date getLastOntologyCheckDate();

    String getOntologyMD5checksum();

    void setLastOntologyCheckDate(Date date);

    void setOntologyMD5checksum(String md5);


}
