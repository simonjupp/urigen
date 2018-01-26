package uk.ac.ebi.fgpt.urigen.service;

import uk.ac.ebi.fgpt.urigen.exception.PreferenceCreationException;
import uk.ac.ebi.fgpt.urigen.impl.GeneratorTypes;
import uk.ac.ebi.fgpt.urigen.model.UrigenPreference;

import java.util.Collection;

/**
 * @author Simon Jupp
 * @date 03/01/2012
 * Functional Genomics Group EMBL-EBI
 */
public interface PreferencesService {

    UrigenPreference getPreference (int preferenceId);

    Collection<UrigenPreference> getAllPreferences();

    UrigenPreference createNewPreference(UrigenPreference preference) throws PreferenceCreationException;

    UrigenPreference updatePreference (UrigenPreference preference) throws PreferenceCreationException;

    void removePreference (UrigenPreference preference);

    GeneratorTypes[] getGeneratorTypes();


}
