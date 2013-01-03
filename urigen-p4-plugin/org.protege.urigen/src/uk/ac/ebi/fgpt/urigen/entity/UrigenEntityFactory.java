/*     */ package uk.ac.ebi.fgpt.urigen.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.protege.editor.owl.model.OWLModelManager;
/*     */ import org.protege.editor.owl.model.entity.AutoIDException;
/*     */ import org.protege.editor.owl.model.entity.CustomOWLEntityFactory;
/*     */ import org.protege.editor.owl.model.entity.CustomOWLEntityFactory.EntityNameInfo;
/*     */ import org.protege.editor.owl.model.entity.OWLEntityCreationException;
/*     */ import org.protege.editor.owl.model.entity.OWLEntityCreationSet;
/*     */ import org.semanticweb.owlapi.model.IRI;
/*     */ import org.semanticweb.owlapi.model.OWLEntity;
/*     */ import org.semanticweb.owlapi.model.OWLOntology;
/*     */ import org.semanticweb.owlapi.model.OWLOntologyID;
/*     */ import uk.ac.ebi.fgpt.urigen.exception.UrigenException;
/*     */ import uk.ac.ebi.fgpt.urigen.web.view.PreferenceBean;
/*     */ import uk.ac.ebi.fgpt.urigen.web.view.UrigenEntityBean;
/*     */ import uk.ac.ebi.fgpt.urigen.web.view.UrigenRequestBean;
/*     */ import uk.ac.ebi.fgpt.urigen.web.view.UserBean;
/*     */ 
/*     */ public class UrigenEntityFactory extends CustomOWLEntityFactory
/*     */ {
/*  29 */   private Logger logger = Logger.getLogger(UrigenEntityFactory.class);
/*     */ 
/*  31 */   private static String defaultBaseURI = "http://urigen_local_uri/";
/*     */   private OWLModelManager mngr;
/*     */ 
/*     */   public UrigenEntityFactory(OWLModelManager mngr)
/*     */   {
/*  37 */     super(mngr);
/*  38 */     this.mngr = mngr;
/*     */   }
/*     */ 
/*     */   private boolean isIRIAlreadyUsed(IRI iri)
/*     */   {
/*  43 */     for (OWLOntology ont : this.mngr.getOntologies()) {
/*  44 */       if (ont.containsEntityInSignature(iri)) {
/*  45 */         return true;
/*     */       }
/*     */     }
/*  48 */     return false;
/*     */   }
/*     */ 
/*     */   protected <T extends OWLEntity> CustomOWLEntityFactory.EntityNameInfo generateName(Class<T> type, String shortName, IRI baseURI) throws AutoIDException, URISyntaxException, OWLEntityCreationException
/*     */   {
/*  53 */     UUID random = UUID.randomUUID();
/*  54 */     baseURI = IRI.create(defaultBaseURI + random.toString());
/*     */ 
/*  56 */     if (isIRIAlreadyUsed(baseURI)) {
/*  57 */       throw new OWLEntityCreationException("Entity already exists: " + baseURI.toString());
/*     */     }
/*     */ 
/*  60 */     this.logger.debug("generating uri: " + shortName + " baseURI: " + baseURI);
/*     */ 
/*  67 */     IRI activeOntologyIri = this.mngr.getActiveOntology().getOntologyID().getOntologyIRI();
/*     */ 
/*  71 */     String serverUrl = UrigenPreference.getServerUrlByOntologyIri(activeOntologyIri);
/*  72 */     if ((serverUrl == null) || ("".equals(serverUrl))) {
/*  73 */       this.logger.warn("no urigen server associated with " + activeOntologyIri.toString());
/*  74 */       return super.generateName(type, shortName, baseURI);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  80 */       Connection connection = new ServerURLConnection(serverUrl);
/*     */ 
/*  83 */       String apiKey = UrigenPreference.getApiKeyByOntologyIRI(activeOntologyIri);
/*     */ 
/*  87 */       PreferenceBean p = connection.getUrigenPreference(activeOntologyIri);
/*     */ 
/*  90 */       UserBean u = connection.getUrigenUserByApiKey(apiKey);
/*     */ 
/*  93 */       UrigenRequestBean request = new UrigenRequestBean(u.getId(), baseURI.toString(), p.getPreferenceId(), shortName, "");
/*  94 */       UrigenEntityBean newEntity = connection.getNewUri(request, apiKey);
/*  95 */       if (newEntity.getStatusOK()) {
/*  96 */         return new CustomOWLEntityFactory.EntityNameInfo(IRI.create(newEntity.getGeneratedUri()), "", shortName);
/*     */       }
/*     */ 
/*  99 */       this.logger.error("Error creating new entity: " + newEntity.getErrorMessage());
/* 100 */       return super.generateName(type, shortName, baseURI);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 105 */       this.logger.debug("Can't connect to Urigen server at : " + serverUrl);
/* 106 */       return super.generateName(type, shortName, baseURI);
/*     */     } catch (UrigenException e) {
/* 108 */       this.logger.error("Can't access urigen server: " + e.getMessage());
/* 109 */       e.printStackTrace();
/* 110 */     }return super.generateName(type, shortName, baseURI);
/*     */   }
/*     */ 
/*     */   public <T extends OWLEntity> OWLEntityCreationSet<T> preview(Class<T> type, String shortName, IRI baseIRI)
/*     */     throws OWLEntityCreationException
/*     */   {
/* 129 */     this.logger.debug("generating uri: " + shortName + " baseURI: " + baseIRI);
/*     */ 
/* 131 */     UUID random = UUID.randomUUID();
/* 132 */     baseIRI = IRI.create(defaultBaseURI + random.toString());
/*     */ 
/* 134 */     OWLEntity entity = getOWLEntity(this.mngr.getOWLDataFactory(), type, baseIRI);
/*     */ 
/* 136 */     List changes = getChanges(entity, new CustomOWLEntityFactory.EntityNameInfo(baseIRI, "", shortName));
/*     */ 
/* 138 */     return new OWLEntityCreationSet(entity, changes);
/*     */   }
/*     */ }

/* Location:           /Users/jupp/Downloads/org.protege.urigen.jar
 * Qualified Name:     uk.ac.ebi.fgpt.urigen.entity.UrigenEntityFactory
 * JD-Core Version:    0.6.1
 */