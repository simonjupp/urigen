package uk.ac.ebi.fgpt.urigen.service;

import uk.ac.ebi.fgpt.urigen.exception.PreferenceCreationException;
import uk.ac.ebi.fgpt.urigen.impl.GeneratorTypes;
import uk.ac.ebi.fgpt.urigen.model.UrigenManager;
import uk.ac.ebi.fgpt.urigen.model.UrigenPreference;

import java.util.Collection;

/**
 * @author Simon Jupp
 * @date 10/01/2012
 * Functional Genomics Group EMBL-EBI
 */
public class DefaultPreferencesService implements PreferencesService {

    private UrigenManager manager;

    public void setUrigenManager (UrigenManager manager) {
        this.manager = manager;
    }

    public GeneratorTypes[] getGeneratorTypes() {
        return manager.getGeneratorTypes();
    }

    public UrigenPreference getPreference(int preferenceId) {
        return manager.getPreferencesById(preferenceId);
    }

    public Collection<UrigenPreference> getAllPreferences() {
        return manager.getPreferences();
    }

    public UrigenPreference createNewPreference(UrigenPreference preference) throws PreferenceCreationException {
        return manager.savePreference(preference);
    }

    public UrigenPreference updatePreference(UrigenPreference preference) throws PreferenceCreationException {
        return manager.updatePreference(preference);
    }

    public void removePreference(UrigenPreference preference) {
        manager.removePreference(preference);
    }
}
