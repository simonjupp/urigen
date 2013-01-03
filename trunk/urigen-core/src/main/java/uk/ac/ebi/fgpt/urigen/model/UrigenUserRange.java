package uk.ac.ebi.fgpt.urigen.model;

/**
 * @author Simon Jupp
 * @date 21/02/2012
 * Functional Genomics Group EMBL-EBI
 */
public interface UrigenUserRange {

    int getId();
    void setId(int id);
    int getUserId();
    int getPrefsId();
    int getAutoIdDigitStart();
    int getAutoIdDigitEnd();
    long getLastIdInSequence();

    void setPreferenceId(int id);
    void setLastIdInSequence(long id);
    void setAutoIdDigitStart(int start);
    void setAutoIdDigitEnd(int end);
}
