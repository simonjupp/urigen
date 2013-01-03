/*     */ package uk.ac.ebi.fgpt.urigen.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.protege.editor.owl.model.OWLModelManager;
/*     */ import org.protege.editor.owl.model.entity.AutoIDException;
/*     */ import org.protege.editor.owl.model.entity.CustomOWLEntityFactory;
/*     */ import org.protege.editor.owl.model.entity.OWLEntityCreationException;
/*     */ import org.protege.editor.owl.model.entity.OWLEntityCreationSet;
/*     */ import org.protege.editor.owl.model.entity.OWLEntityFactory;
/*     */ import org.semanticweb.owlapi.model.AddAxiom;
/*     */ import org.semanticweb.owlapi.model.IRI;
/*     */ import org.semanticweb.owlapi.model.OWLAnnotationProperty;
/*     */ import org.semanticweb.owlapi.model.OWLAxiom;
/*     */ import org.semanticweb.owlapi.model.OWLClass;
/*     */ import org.semanticweb.owlapi.model.OWLDataFactory;
/*     */ import org.semanticweb.owlapi.model.OWLDataProperty;
/*     */ import org.semanticweb.owlapi.model.OWLDatatype;
/*     */ import org.semanticweb.owlapi.model.OWLEntity;
/*     */ import org.semanticweb.owlapi.model.OWLNamedIndividual;
/*     */ import org.semanticweb.owlapi.model.OWLObjectProperty;
/*     */ import org.semanticweb.owlapi.model.OWLOntology;
/*     */ import org.semanticweb.owlapi.model.OWLOntologyChange;
/*     */ import org.semanticweb.owlapi.model.OWLOntologyID;
/*     */ import uk.ac.ebi.fgpt.urigen.exception.UrigenException;
/*     */ import uk.ac.ebi.fgpt.urigen.web.view.PreferenceBean;
/*     */ import uk.ac.ebi.fgpt.urigen.web.view.UrigenEntityBean;
/*     */ import uk.ac.ebi.fgpt.urigen.web.view.UrigenRequestBean;
/*     */ import uk.ac.ebi.fgpt.urigen.web.view.UserBean;
/*     */ 
/*     */ public class UrigenEntityFactory_copy
/*     */   implements OWLEntityFactory
/*     */ {
/*  26 */   private Logger logger = Logger.getLogger(UrigenEntityFactory_copy.class);
/*     */ 
/*  28 */   private static String defaultBaseURI = "http://urigen_local_uri/";
/*     */   private OWLModelManager mngr;
/*     */   private CustomOWLEntityFactory customOWLEntityFactory;
/*     */ 
/*     */   public UrigenEntityFactory_copy(OWLModelManager mngr)
/*     */   {
/*  35 */     this.mngr = mngr;
/*  36 */     this.customOWLEntityFactory = new CustomOWLEntityFactory(mngr);
/*     */   }
/*     */ 
/*     */   public static <T extends OWLEntity> T getOWLEntity(OWLDataFactory factory, Class<T> type, IRI iri) {
///*  40 */     if (OWLClass.class.isAssignableFrom(type)) {
///*  41 */       return (OWLEntity)type.cast(factory.getOWLClass(iri));
///*     */     }
///*  43 */     if (OWLObjectProperty.class.isAssignableFrom(type)) {
///*  44 */       return (OWLEntity)type.cast(factory.getOWLObjectProperty(iri));
///*     */     }
///*  46 */     if (OWLDataProperty.class.isAssignableFrom(type)) {
///*  47 */       return (OWLEntity)type.cast(factory.getOWLDataProperty(iri));
///*     */     }
///*  49 */     if (OWLNamedIndividual.class.isAssignableFrom(type)) {
///*  50 */       return (OWLEntity)type.cast(factory.getOWLNamedIndividual(iri));
///*     */     }
///*  52 */     if (OWLAnnotationProperty.class.isAssignableFrom(type)) {
///*  53 */       return (OWLEntity)type.cast(factory.getOWLAnnotationProperty(iri));
///*     */     }
///*  55 */     if (OWLDatatype.class.isAssignableFrom(type)) {
///*  56 */       return (OWLEntity)type.cast(factory.getOWLDatatype(iri));
///*     */     }
/*  58 */     return null;
/*     */   }
/*     */ 
/*     */   public OWLEntityCreationSet<OWLClass> createOWLClass(String shortName, IRI baseIRI) throws OWLEntityCreationException {
/*  62 */     return createOWLEntity(OWLClass.class, shortName, baseIRI);
/*     */   }
/*     */ 
/*     */   public OWLEntityCreationSet<OWLObjectProperty> createOWLObjectProperty(String shortName, IRI baseURI) throws OWLEntityCreationException
/*     */   {
/*  67 */     return createOWLEntity(OWLObjectProperty.class, shortName, baseURI);
/*     */   }
/*     */ 
/*     */   public OWLEntityCreationSet<OWLDataProperty> createOWLDataProperty(String shortName, IRI baseURI) throws OWLEntityCreationException
/*     */   {
/*  72 */     return createOWLEntity(OWLDataProperty.class, shortName, baseURI);
/*     */   }
/*     */ 
/*     */   public OWLEntityCreationSet<OWLAnnotationProperty> createOWLAnnotationProperty(String shortName, IRI baseURI) throws OWLEntityCreationException
/*     */   {
/*  77 */     return createOWLEntity(OWLAnnotationProperty.class, shortName, baseURI);
/*     */   }
/*     */ 
/*     */   public OWLEntityCreationSet<OWLNamedIndividual> createOWLIndividual(String shortName, IRI baseURI) throws OWLEntityCreationException
/*     */   {
/*  82 */     return createOWLEntity(OWLNamedIndividual.class, shortName, baseURI);
/*     */   }
/*     */ 
/*     */   public OWLEntityCreationSet<OWLDatatype> createOWLDatatype(String shortName, IRI baseIRI) throws OWLEntityCreationException
/*     */   {
/*  87 */     return createOWLEntity(OWLDatatype.class, shortName, baseIRI);
/*     */   }
/*     */ 
/*     */   public <T extends OWLEntity> OWLEntityCreationSet<T> createOWLEntity(Class<T> type, String shortName, IRI baseURI) throws OWLEntityCreationException
/*     */   {
/*     */     try {
/*  93 */       EntityNameInfo name = generateName(type, shortName, baseURI);
/*     */ 
/*  95 */       OWLEntity entity = getOWLEntity(this.mngr.getOWLDataFactory(), type, name.getIri());
/*     */ 
/*  97 */       List changes = getChanges(entity, name);
/*     */ 
/*  99 */       return new OWLEntityCreationSet(entity, changes);
/*     */     }
/*     */     catch (URISyntaxException e) {
/* 102 */       throw new OWLEntityCreationException(e);
/*     */     }
/*     */     catch (AutoIDException e) {
/* 105 */       throw new OWLEntityCreationException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected <T extends OWLEntity> EntityNameInfo generateName(Class<T> type, String shortName, IRI baseURI)
/*     */     throws AutoIDException, URISyntaxException, OWLEntityCreationException
/*     */   {
/* 112 */     UUID random = UUID.randomUUID();
/* 113 */     baseURI = IRI.create(defaultBaseURI + random.toString());
/*     */ 
/* 119 */     this.logger.debug("generating uri: " + shortName + " baseURI: " + baseURI);
/*     */ 
/* 121 */     if (!UrigenPreference.getIsDynamicCreation())
/*     */     {
/* 124 */       return new EntityNameInfo(baseURI, "", shortName);
/*     */     }
/*     */ 
/* 127 */     IRI activeOntologyIri = this.mngr.getActiveOntology().getOntologyID().getOntologyIRI();
/*     */ 
/* 131 */     String serverUrl = UrigenPreference.getServerUrlByOntologyIri(activeOntologyIri);
/* 132 */     if ((serverUrl == null) || ("".equals(serverUrl))) {
/* 133 */       this.logger.warn("no urigen server associated with " + activeOntologyIri.toString());
/* 134 */       return new EntityNameInfo(baseURI, "", shortName);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 140 */       Connection connection = new ServerURLConnection(serverUrl);
/*     */ 
/* 143 */       String apiKey = UrigenPreference.getApiKeyByOntologyIRI(activeOntologyIri);
/*     */ 
/* 147 */       PreferenceBean p = connection.getUrigenPreference(activeOntologyIri);
/*     */ 
/* 150 */       UserBean u = connection.getUrigenUserByApiKey(apiKey);
/*     */ 
/* 153 */       UrigenRequestBean request = new UrigenRequestBean(u.getId(), baseURI.toString(), p.getPreferenceId(), shortName, "");
/* 154 */       UrigenEntityBean newEntity = connection.getNewUri(request, apiKey);
/* 155 */       if (newEntity.getStatusOK()) {
/* 156 */         return new EntityNameInfo(IRI.create(newEntity.getGeneratedUri()), "", shortName);
/*     */       }
/*     */ 
/* 159 */       this.logger.error("Error creating new entity: " + newEntity.getErrorMessage());
/* 160 */       return new EntityNameInfo(baseURI, baseURI.toString(), shortName);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 164 */       this.logger.debug("Can't connect to Urigen server at : " + serverUrl);
/* 165 */       return new EntityNameInfo(baseURI, "", shortName);
/*     */     } catch (UrigenException e) {
/* 167 */       this.logger.error("Can't access urigen server: " + e.getMessage());
/* 168 */       e.printStackTrace();
/* 169 */     }return new EntityNameInfo(baseURI, "", shortName);
/*     */   }
/*     */ 
/*     */   protected <T extends OWLEntity> List<OWLOntologyChange> getChanges(T entity, EntityNameInfo name)
/*     */   {
/* 177 */     List changes = new ArrayList();
/*     */ 
/* 180 */     OWLDataFactory df = this.mngr.getOWLDataFactory();
/* 181 */     OWLAxiom ax = df.getOWLDeclarationAxiom(entity);
/* 182 */     changes.add(new AddAxiom(this.mngr.getActiveOntology(), ax));
/* 183 */     return changes;
/*     */   }
/*     */ 
/*     */   public <T extends OWLEntity> OWLEntityCreationSet<T> preview(Class<T> type, String shortName, IRI baseIRI) throws OWLEntityCreationException
/*     */   {
/* 188 */     this.logger.debug("generating uri: " + shortName + " baseURI: " + baseIRI);
/*     */ 
/* 190 */     UUID random = UUID.randomUUID();
/* 191 */     baseIRI = IRI.create(defaultBaseURI + random.toString());
/*     */ 
/* 193 */     OWLEntity entity = getOWLEntity(this.mngr.getOWLDataFactory(), type, baseIRI);
/*     */ 
/* 195 */     List changes = getChanges(entity, new EntityNameInfo(baseIRI, "", shortName));
/*     */ 
/* 197 */     return new OWLEntityCreationSet(entity, changes);
/*     */   }
/*     */ 
/*     */   private boolean isIRIAlreadyUsed(IRI iri)
/*     */   {
/* 223 */     for (OWLOntology ont : this.mngr.getOntologies()) {
/* 224 */       if (ont.containsEntityInSignature(iri)) {
/* 225 */         return true;
/*     */       }
/*     */     }
/* 228 */     return false;
/*     */   }
/*     */ 
/*     */   public static class EntityNameInfo
/*     */   {
/*     */     private IRI iri;
/*     */     private String id;
/*     */     private String shortName;
/*     */ 
/*     */     public EntityNameInfo(IRI iri, String id, String shortName)
/*     */     {
/* 206 */       this.iri = iri;
/* 207 */       this.id = id;
/* 208 */       this.shortName = shortName;
/*     */     }
/*     */     public IRI getIri() {
/* 211 */       return this.iri;
/*     */     }
/*     */     public String getId() {
/* 214 */       return this.id;
/*     */     }
/*     */     public String getShortName() {
/* 217 */       return this.shortName;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/jupp/Downloads/org.protege.urigen.jar
 * Qualified Name:     uk.ac.ebi.fgpt.urigen.entity.UrigenEntityFactory_copy
 * JD-Core Version:    0.6.1
 */