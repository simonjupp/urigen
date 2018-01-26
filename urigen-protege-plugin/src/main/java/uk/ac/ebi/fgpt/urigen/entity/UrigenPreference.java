package uk.ac.ebi.fgpt.urigen.entity;

import org.protege.editor.core.prefs.Preferences;
import org.protege.editor.core.prefs.PreferencesManager;
import org.semanticweb.owlapi.model.IRI;

import java.util.ArrayList;
import java.util.List;

public class UrigenPreference {
    private static final String PREFERENCES_SET_KEY = "uk.ac.ebi.fgpt.entity.creation";
    private static final String USE_URIGEN_KEY = ":::USE_URIGEN:::";
    private static final String PREFERENCE_IDS = "PREFERENCE_IDS";
    private static final String SERVER_KEY = ":::SERVER_URL:::";
    private static final String PREF_ID_NAME = ":::PREF_NAME:::";
    private static final String API_KEY = ":::API_KEY:::";
    private static final String DYNAMIC = ":::DYNAMIC:::";

    private static Preferences getPrefs() {
        return PreferencesManager.getInstance().getApplicationPreferences("uk.ac.ebi.fgpt.entity.creation");
    }

    public static void save(IRI ontologyIRI, String serverURL, String name, String apiKey) {
        List allPreferences = getPrefs().getStringList("PREFERENCE_IDS", new ArrayList());

        if (!allPreferences.contains(ontologyIRI.toString())) {
            allPreferences.add(ontologyIRI.toString());
        }

        getPrefs().putStringList("PREFERENCE_IDS", allPreferences);

        getPrefs().putString(":::SERVER_URL:::" + ontologyIRI.toString(), serverURL);

        getPrefs().putString(":::PREF_NAME:::" + ontologyIRI.toString(), name);
        getPrefs().putString(":::API_KEY:::" + ontologyIRI.toString(), apiKey);
    }

    public static boolean getUseUrigen() {
        return getPrefs().getBoolean(":::USE_URIGEN:::", false);
    }

    public static void setUseUrigen(boolean use) {
        getPrefs().putBoolean(":::USE_URIGEN:::", use);
    }

    public static List<String> getAllOntologyIris() {
        return getPrefs().getStringList("PREFERENCE_IDS", new ArrayList());
    }

    public static String getServerUrlByOntologyIri(IRI ontologyIRI) {
        String iri = ontologyIRI.toString();
        return getPrefs().getString(":::SERVER_URL:::" + iri, "");
    }

    public static String getNameByOntologyIri(IRI ontologyIRI) {
        String iri = ontologyIRI.toString();
        return getPrefs().getString(":::PREF_NAME:::" + iri, "");
    }

    public static String getApiKeyByOntologyIRI(IRI ontologyIRI) {
        String iri = ontologyIRI.toString();
        return getPrefs().getString(":::API_KEY:::" + iri, "");
    }

    public static boolean getIsDynamicCreation() {
        return getPrefs().getBoolean(":::DYNAMIC:::", true);
    }

    public static void remove(IRI ontologyIRI) {
        String iri = ontologyIRI.toString();

        List allPreferences = getPrefs().getStringList("PREFERENCE_IDS", new ArrayList());
        allPreferences.remove(iri);

        getPrefs().putStringList("PREFERENCE_IDS", allPreferences);
        getPrefs().putString(":::SERVER_URL:::" + ontologyIRI.toString(), "");

        getPrefs().putString(":::PREF_NAME:::" + ontologyIRI.toString(), "");
        getPrefs().putString(":::API_KEY:::" + ontologyIRI.toString(), "");
    }

    public void setDynamicEntityCreation(boolean createDynamically) {
        getPrefs().putBoolean(":::DYNAMIC:::", createDynamically);
    }
}

/* Location:           /Users/jupp/Downloads/org.protege.urigen.jar
 * Qualified Name:     uk.ac.ebi.fgpt.urigen.entity.UrigenPreference
 * JD-Core Version:    0.6.1
 */