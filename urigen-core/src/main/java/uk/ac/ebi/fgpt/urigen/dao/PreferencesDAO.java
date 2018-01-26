package uk.ac.ebi.fgpt.urigen.dao;

import uk.ac.ebi.fgpt.urigen.exception.PreferenceCreationException;
import uk.ac.ebi.fgpt.urigen.exception.UrigenException;
import uk.ac.ebi.fgpt.urigen.model.UrigenPreference;
import uk.ac.ebi.fgpt.urigen.model.UrigenUserRange;

import java.util.Collection;
import java.util.Date;

/**
 * @author Simon Jupp
 * @date 15/12/2011
 * Functional Genomics Group EMBL-EBI
 */
public interface PreferencesDAO {

    UrigenPreference getPreferencesById (int id);

    Collection<UrigenPreference> getAllPreferences();

    UrigenPreference update (UrigenPreference preference) throws PreferenceCreationException;

    UrigenPreference savePreferences(UrigenPreference preferences) throws PreferenceCreationException;

    boolean contains(int preferencesId);

    UrigenUserRange getUserRange(int userId, int preferencesId);

    void updateUserRange(UrigenUserRange r) throws UrigenException;

    UrigenPreference getPreferenceByUserRange(UrigenUserRange range);

    void setLastOntologyCheckDate(int prefId, Date date);

    void updateMD5checksum(int prefId, String md5);

    void removePref(UrigenPreference preference);
}
