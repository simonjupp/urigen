/*     */ package uk.ac.ebi.fgpt.urigen.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.client.methods.HttpPost;
/*     */ import org.apache.http.entity.StringEntity;
/*     */ import org.apache.http.impl.client.DefaultHttpClient;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.codehaus.jackson.map.ObjectMapper;
/*     */ import org.codehaus.jackson.type.TypeReference;
/*     */ import org.semanticweb.owlapi.model.IRI;
/*     */ import uk.ac.ebi.fgpt.urigen.exception.UrigenException;
/*     */ import uk.ac.ebi.fgpt.urigen.web.view.PreferenceBean;
/*     */ import uk.ac.ebi.fgpt.urigen.web.view.UrigenEntityBean;
/*     */ import uk.ac.ebi.fgpt.urigen.web.view.UrigenRequestBean;
/*     */ import uk.ac.ebi.fgpt.urigen.web.view.UserBean;
/*     */ 
/*     */ public class ServerURLConnection
/*     */   implements Connection
/*     */ {
/*  39 */   private Logger logger = Logger.getLogger(ServerURLConnection.class);
/*     */   private HttpClient client;
/*     */   private String serverURL;
/*     */ 
/*     */   public ServerURLConnection(String serverURL)
/*     */     throws IOException
/*     */   {
/*  47 */     this.serverURL = serverURL;
/*  48 */     this.client = new DefaultHttpClient();
/*     */ 
/*  50 */     HttpGet httpget = new HttpGet(serverURL);
/*     */ 
/*  52 */     HttpResponse response = this.client.execute(httpget);
/*     */ 
/*  54 */     if (response.getStatusLine().getStatusCode() != 200)
/*  55 */       throw new IOException("Can't reach server at: " + serverURL);
/*     */   }
/*     */ 
/*     */   public PreferenceBean getUrigenPreference(IRI ontologyIri)
/*     */     throws UrigenException
/*     */   {
/*     */     try
/*     */     {
/*  63 */       for (PreferenceBean ps : getUrigenPreferences()) {
/*  64 */         if (ps.getOntologyUri().equals(ontologyIri.toString()))
/*  65 */           return ps;
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  70 */       throw new UrigenException(e);
/*     */     }
/*     */ 
/*  88 */     throw new UrigenException("No preferences found on server");
/*     */   }
/*     */ 
/*     */   public Collection<PreferenceBean> getUrigenPreferences() throws UrigenException
/*     */   {
/*  93 */     HttpGet httpget = new HttpGet(this.serverURL + "/api/preferences");
/*  94 */     this.client = new DefaultHttpClient();
/*     */     try
/*     */     {
/*  97 */       HttpResponse respone = this.client.execute(httpget);
/*  98 */       ObjectMapper mapper = new ObjectMapper();
/*     */ 
/* 100 */       Collection<PreferenceBean> bean = (Collection<PreferenceBean>) mapper.readValue(respone.getEntity().getContent(), new TypeReference<Collection<PreferenceBean>>() { });
/* 102 */       this.logger.debug("Got preference back from urigen server: " + bean.toString());
/* 103 */       return bean;
/*     */     }
/*     */     catch (Exception e) {
/* 106 */       e.printStackTrace();
/* 107 */     }throw new UrigenException("Can't retrieve preference");
/*     */   }
/*     */ 
/*     */   public UserBean getUrigenUserByApiKey(String apiKey) throws UrigenException
/*     */   {
/* 112 */     HttpGet httpget = new HttpGet(this.serverURL + "/api/users/query?restApiKey=" + apiKey);
/* 113 */     this.client = new DefaultHttpClient();
/*     */     try
/*     */     {
/* 116 */       HttpResponse respone = this.client.execute(httpget);
/* 117 */       ObjectMapper mapper = new ObjectMapper();
/* 118 */       UserBean bean = (UserBean)mapper.readValue(respone.getEntity().getContent(), UserBean.class);
/* 119 */       this.logger.debug("Got user back from urigen server: " + bean.toString());
/* 120 */       return bean;
/*     */     }
/*     */     catch (Exception e) {
/* 123 */       e.printStackTrace();
/* 124 */     }throw new UrigenException("Can't retrieve user");
/*     */   }
/*     */ 
/*     */   public UrigenEntityBean getNewUri(UrigenRequestBean request, String apiKey)
/*     */     throws UrigenException
/*     */   {
/* 130 */     HttpPost httppost = new HttpPost(this.serverURL + "/api/uris?restApiKey=" + apiKey);
/* 131 */     ObjectMapper mapper = new ObjectMapper();
/*     */     try
/*     */     {
/* 135 */       String input = mapper.writeValueAsString(request);
/* 136 */       StringEntity entity = new StringEntity(input);
/* 137 */       entity.setContentType("application/json");
/* 138 */       httppost.setEntity(entity);
/*     */     } catch (IOException e) {
/* 140 */       this.logger.error("Can't serialize request object");
/* 141 */       e.printStackTrace();
/*     */     }
/* 143 */     this.client = new DefaultHttpClient();
/*     */     try
/*     */     {
/* 146 */       HttpResponse response = this.client.execute(httppost);
/*     */ 
/* 148 */       if (response.getStatusLine().getStatusCode() != 200) {
/* 149 */         throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
/*     */       }
/*     */ 
/* 153 */       this.logger.debug(response.toString());
/* 154 */       UrigenEntityBean bean = (UrigenEntityBean)mapper.readValue(response.getEntity().getContent(), UrigenEntityBean.class);
/* 155 */       this.logger.debug("Got new URI back from urigen server: " + bean.toString());
/* 156 */       return bean;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 160 */       e.printStackTrace();
/*     */     }
/* 162 */     return new UrigenEntityBean(false, "Nothing returned from server");
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/* 169 */       ServerURLConnection con = new ServerURLConnection("http://localhost:8083/urigen/");
/*     */ 
/* 171 */       System.out.println("Getting preferences");
/* 172 */       for (PreferenceBean p : con.getUrigenPreferences()) {
/* 173 */         System.out.println("pref:" + p.getPreferenceId() + "(" + p.getOntologyUri() + ")");
/*     */       }
/*     */ 
/* 176 */       PreferenceBean bean = con.getUrigenPreference(IRI.create("http://purl.obolibrary.org/obo/cl.owl"));
/* 177 */       System.out.println(bean.statusOK());
/* 178 */       UserBean ub = con.getUrigenUserByApiKey("8CC30333F7E29D729467E9E14CFADE83E943B524");
/* 179 */       System.out.println(ub.toString());
/*     */ 
/* 181 */       UrigenRequestBean request = new UrigenRequestBean(ub.getId(), "_://local_uri/098989876", 3, "", "");
/* 182 */       con.getNewUri(request, "8CC30333F7E29D729467E9E14CFADE83E943B524");
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 187 */       System.err.println("Problem connecting to service");
/*     */     } catch (UrigenException e) {
/* 189 */       System.err.println("nope sorry!");
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/jupp/Downloads/org.protege.urigen.jar
 * Qualified Name:     uk.ac.ebi.fgpt.urigen.entity.ServerURLConnection
 * JD-Core Version:    0.6.1
 */