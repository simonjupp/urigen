/*     */ package uk.ac.ebi.fgpt.urigen.entity;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.protege.editor.core.prefs.Preferences;
/*     */ import org.protege.editor.core.prefs.PreferencesManager;
/*     */ import org.semanticweb.owlapi.model.IRI;
/*     */ 
/*     */ public class UrigenPreference
/*     */ {
/*     */   private static final String PREFERENCES_SET_KEY = "uk.ac.ebi.fgpt.entity.creation";
/*     */   private static final String USE_URIGEN_KEY = ":::USE_URIGEN:::";
/*     */   private static final String PREFERENCE_IDS = "PREFERENCE_IDS";
/*     */   private static final String SERVER_KEY = ":::SERVER_URL:::";
/*     */   private static final String PREF_ID_NAME = ":::PREF_NAME:::";
/*     */   private static final String API_KEY = ":::API_KEY:::";
/*     */   private static final String DYNAMIC = ":::DYNAMIC:::";
/*     */ 
/*     */   private static Preferences getPrefs()
/*     */   {
/*  34 */     return PreferencesManager.getInstance().getApplicationPreferences("uk.ac.ebi.fgpt.entity.creation");
/*     */   }
/*     */ 
/*     */   public static void save(IRI ontologyIRI, String serverURL, String name, String apiKey)
/*     */   {
/*  39 */     List allPreferences = getPrefs().getStringList("PREFERENCE_IDS", new ArrayList());
/*     */ 
/*  41 */     if (!allPreferences.contains(ontologyIRI.toString())) {
/*  42 */       allPreferences.add(ontologyIRI.toString());
/*     */     }
/*     */ 
/*  45 */     getPrefs().putStringList("PREFERENCE_IDS", allPreferences);
/*     */ 
/*  47 */     getPrefs().putString(":::SERVER_URL:::" + ontologyIRI.toString(), serverURL);
/*     */ 
/*  49 */     getPrefs().putString(":::PREF_NAME:::" + ontologyIRI.toString(), name);
/*  50 */     getPrefs().putString(":::API_KEY:::" + ontologyIRI.toString(), apiKey);
/*     */   }
/*     */ 
/*     */   public static void setUseUrigen(boolean use) {
/*  54 */     getPrefs().putBoolean(":::USE_URIGEN:::", use);
/*     */   }
/*     */ 
/*     */   public static boolean getUseUrigen() {
/*  58 */     return getPrefs().getBoolean(":::USE_URIGEN:::", false);
/*     */   }
/*     */ 
/*     */   public static List<String> getAllOntologyIris()
/*     */   {
/*  63 */     return getPrefs().getStringList("PREFERENCE_IDS", new ArrayList());
/*     */   }
/*     */ 
/*     */   public static String getServerUrlByOntologyIri(IRI ontologyIRI) {
/*  67 */     String iri = ontologyIRI.toString();
/*  68 */     return getPrefs().getString(":::SERVER_URL:::" + iri, "");
/*     */   }
/*     */ 
/*     */   public static String getNameByOntologyIri(IRI ontologyIRI)
/*     */   {
/*  77 */     String iri = ontologyIRI.toString();
/*  78 */     return getPrefs().getString(":::PREF_NAME:::" + iri, "");
/*     */   }
/*     */   public static String getApiKeyByOntologyIRI(IRI ontologyIRI) {
/*  81 */     String iri = ontologyIRI.toString();
/*  82 */     return getPrefs().getString(":::API_KEY:::" + iri, "");
/*     */   }
/*     */ 
/*     */   public void setDynamicEntityCreation(boolean createDynamically) {
/*  86 */     getPrefs().putBoolean(":::DYNAMIC:::", createDynamically);
/*     */   }
/*     */ 
/*     */   public static boolean getIsDynamicCreation() {
/*  90 */     return getPrefs().getBoolean(":::DYNAMIC:::", true);
/*     */   }
/*     */ 
/*     */   public static void remove(IRI ontologyIRI) {
/*  94 */     String iri = ontologyIRI.toString();
/*     */ 
/*  96 */     List allPreferences = getPrefs().getStringList("PREFERENCE_IDS", new ArrayList());
/*  97 */     allPreferences.remove(iri);
/*     */ 
/*  99 */     getPrefs().putStringList("PREFERENCE_IDS", allPreferences);
/* 100 */     getPrefs().putString(":::SERVER_URL:::" + ontologyIRI.toString(), "");
/*     */ 
/* 102 */     getPrefs().putString(":::PREF_NAME:::" + ontologyIRI.toString(), "");
/* 103 */     getPrefs().putString(":::API_KEY:::" + ontologyIRI.toString(), "");
/*     */   }
/*     */ }

/* Location:           /Users/jupp/Downloads/org.protege.urigen.jar
 * Qualified Name:     uk.ac.ebi.fgpt.urigen.entity.UrigenPreference
 * JD-Core Version:    0.6.1
 */