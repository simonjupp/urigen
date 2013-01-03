package uk.ac.ebi.fgpt.urigen.dao;

import uk.ac.ebi.fgpt.urigen.exception.PreferenceCreationException;
import uk.ac.ebi.fgpt.urigen.model.UrigenPreference;
import uk.ac.ebi.fgpt.urigen.model.UrigenUserRange;

import java.util.*;

/**
 * @author Simon Jupp
 * @date 03/01/2012
 * Functional Genomics Group EMBL-EBI
 */
public class InMemPreferencesDAO implements PreferencesDAO{

    private Map<Integer, UrigenPreference> preferenceMap;
    private int prefId = 0;

    public InMemPreferencesDAO () {
        preferenceMap = new HashMap<Integer, UrigenPreference>();
    }

    public UrigenPreference getPreferencesById(int id) {
        return preferenceMap.get(id);
    }

    public Collection<UrigenPreference> getAllPreferences() {
        return preferenceMap.values();
    }



    public UrigenPreference savePreferences(UrigenPreference preference) throws PreferenceCreationException {
        preference.setPreferenceId(prefId);
        if (preference.getAllUserRange() != null) {
            for (UrigenUserRange range : preference.getAllUserRange()) {
                range.setPreferenceId(prefId);
            }
        }
        this.preferenceMap.put(prefId, preference);
        prefId++;
        return preference;
    }

    public UrigenPreference update(UrigenPreference preference) {

        for (UrigenUserRange range : preference.getAllUserRange()) {
            range.setPreferenceId(prefId);
        }
        this.preferenceMap.put(preference.getPreferenceId(), preference);
        return preference;
    }

    public boolean contains(int preferencesId) {
        return this.preferenceMap.containsKey(preferencesId);
    }

    public UrigenUserRange getUserRange(int userId, int preferencesId) {
        UrigenPreference p = preferenceMap.get(preferencesId);
        for (UrigenUserRange r : p.getAllUserRange()) {
            if (r.getUserId() == userId) {
                return r;
            }
        }
        return null;
    }

    public UrigenPreference getPreferenceByUserRange(UrigenUserRange range) {
        return preferenceMap.get(range.getPrefsId());
    }

    public void setLastOntologyCheckDate(int prefId, Date date) {

    }

    public void updateMD5checksum(int prefId, String md5) {

    }

    public void removePref(UrigenPreference preference) {


    }


    public void updateUserRange(UrigenUserRange r) {
        UrigenPreference p = getPreferenceByUserRange(r);

        if (p != null) {
            for (UrigenUserRange range : p.getAllUserRange()) {
                if (range.getUserId() == r.getUserId()) {
                    range.setPreferenceId(p.getPreferenceId());
                    range.setAutoIdDigitStart(r.getAutoIdDigitStart());
                    range.setAutoIdDigitEnd(r.getAutoIdDigitEnd());
                    range.setLastIdInSequence(r.getLastIdInSequence());
                }
            }
            preferenceMap.put(p.getPreferenceId(), p);
        }
    }
}
