/*    */ package uk.ac.ebi.fgpt.urigen.panel;
/*    */ 
/*    */ import org.semanticweb.owlapi.model.IRI;
/*    */ 
/*    */ public class UrigenPreferenceOption
/*    */ {
/*    */   private IRI ontologyIri;
/*    */   private String serverURL;
/*    */   private String preferenceName;
/*    */   private String restApiKey;
/*    */ 
/*    */   public IRI getOntologyIri()
/*    */   {
/* 21 */     return this.ontologyIri;
/*    */   }
/*    */ 
/*    */   public void setOntologyIri(IRI ontologyIri) {
/* 25 */     this.ontologyIri = ontologyIri;
/*    */   }
/*    */ 
/*    */   public String getServerURL() {
/* 29 */     return this.serverURL;
/*    */   }
/*    */ 
/*    */   public void setServerURL(String serverURL) {
/* 33 */     this.serverURL = serverURL;
/*    */   }
/*    */ 
/*    */   public String getPreferenceName() {
/* 37 */     return this.preferenceName;
/*    */   }
/*    */ 
/*    */   public void setPreferenceName(String preferenceName) {
/* 41 */     this.preferenceName = preferenceName;
/*    */   }
/*    */ 
/*    */   public String getRestApiKey() {
/* 45 */     return this.restApiKey;
/*    */   }
/*    */ 
/*    */   public void setRestApiKey(String restApiKey) {
/* 49 */     this.restApiKey = restApiKey;
/*    */   }
/*    */ 
/*    */   public UrigenPreferenceOption(IRI ontologyIri, String serverURL, String preferenceName, String restApiKey)
/*    */   {
/* 54 */     this.ontologyIri = ontologyIri;
/* 55 */     this.serverURL = serverURL;
/* 56 */     this.preferenceName = preferenceName;
/* 57 */     this.restApiKey = restApiKey;
/*    */   }
/*    */ 
/*    */   public UrigenPreferenceOption()
/*    */   {
/*    */   }
/*    */ }

/* Location:           /Users/jupp/Downloads/org.protege.urigen.jar
 * Qualified Name:     uk.ac.ebi.fgpt.urigen.panel.UrigenPreferenceOption
 * JD-Core Version:    0.6.1
 */