package uk.ac.ebi.fgpt.urigen.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.semanticweb.owlapi.model.IRI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.fgpt.urigen.exception.UrigenException;
import uk.ac.ebi.fgpt.urigen.web.view.PreferenceBean;
import uk.ac.ebi.fgpt.urigen.web.view.UrigenEntityBean;
import uk.ac.ebi.fgpt.urigen.web.view.UrigenRequestBean;
import uk.ac.ebi.fgpt.urigen.web.view.UserBean;

import java.io.IOException;
import java.util.Collection;

public class ServerURLConnection
        implements Connection {
    private final Logger logger = LoggerFactory.getLogger(ServerURLConnection.class);
    private HttpClient client;
    private final String serverURL;

    public ServerURLConnection(String serverURL)
            throws IOException {
        this.serverURL = serverURL;
        this.client = new DefaultHttpClient();

        HttpGet httpget = new HttpGet(serverURL);

        HttpResponse response = this.client.execute(httpget);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new IOException("Can't reach server at: " + serverURL);
        }
    }

    public static void main(String[] args) {
        try {
            ServerURLConnection con = new ServerURLConnection("http://localhost:8083/urigen/");

            System.out.println("Getting preferences");
            for (PreferenceBean p : con.getUrigenPreferences()) {
                System.out.println("pref:" + p.getPreferenceId() + "(" + p.getOntologyUri() + ")");
            }

            PreferenceBean bean = con.getUrigenPreference(IRI.create("http://purl.obolibrary.org/obo/cl.owl"));
            System.out.println(bean.statusOK());
            UserBean ub = con.getUrigenUserByApiKey("8CC30333F7E29D729467E9E14CFADE83E943B524");
            System.out.println(ub.toString());

            UrigenRequestBean request = new UrigenRequestBean(ub.getId(), "_://local_uri/098989876", 3, "", "");
            con.getNewUri(request, "8CC30333F7E29D729467E9E14CFADE83E943B524");
        } catch (IOException e) {
            System.err.println("Problem connecting to service");
        } catch (UrigenException e) {
            System.err.println("nope sorry!");
        }
    }

    public PreferenceBean getUrigenPreference(IRI ontologyIri)
            throws UrigenException {
        try {
            for (PreferenceBean ps : getUrigenPreferences()) {
                if (ps.getOntologyUri().equals(ontologyIri.toString())) {
                    return ps;
                }
            }
        } catch (Exception e) {
            throw new UrigenException(e);
        }

        throw new UrigenException("No preferences found on server");
    }

    public Collection<PreferenceBean> getUrigenPreferences() throws UrigenException {
        HttpGet httpget = new HttpGet(this.serverURL + "/api/preferences");
        this.client = new DefaultHttpClient();
        try {
            HttpResponse respone = this.client.execute(httpget);
            ObjectMapper mapper = new ObjectMapper();

            Collection<PreferenceBean> bean = mapper.readValue(respone.getEntity().getContent(), new TypeReference<Collection<PreferenceBean>>() {
            });
            this.logger.debug("Got preference back from urigen server: " + bean.toString());
            return bean;
        } catch (Exception e) {
            this.logger.error("Error getting preferences: {}", e.getMessage(), e);
            e.printStackTrace();
        }
        throw new UrigenException("Can't retrieve preference");
    }

    public UserBean getUrigenUserByApiKey(String apiKey) throws UrigenException {
        HttpGet httpget = new HttpGet(this.serverURL + "/api/users/query?restApiKey=" + apiKey);
        this.client = new DefaultHttpClient();
        try {
            HttpResponse respone = this.client.execute(httpget);
            ObjectMapper mapper = new ObjectMapper();
            UserBean bean = mapper.readValue(respone.getEntity().getContent(), UserBean.class);
            this.logger.debug("Got user back from urigen server: " + bean.toString());
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new UrigenException("Can't retrieve user");
    }

    public UrigenEntityBean getNewUri(UrigenRequestBean request, String apiKey)
            throws UrigenException {
        HttpPost httppost = new HttpPost(this.serverURL + "/api/uris?restApiKey=" + apiKey);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String input = mapper.writeValueAsString(request);
            StringEntity entity = new StringEntity(input);
            entity.setContentType("application/json");
            httppost.setEntity(entity);
        } catch (IOException e) {
            this.logger.error("Can't serialize request object");
            e.printStackTrace();
        }
        this.client = new DefaultHttpClient();
        try {
            HttpResponse response = this.client.execute(httppost);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            this.logger.debug(response.toString());
            UrigenEntityBean bean = mapper.readValue(response.getEntity().getContent(), UrigenEntityBean.class);
            this.logger.debug("Got new URI back from urigen server: " + bean.toString());
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new UrigenEntityBean(false, "Nothing returned from server");
    }
}

/* Location:           /Users/jupp/Downloads/org.protege.urigen.jar
 * Qualified Name:     uk.ac.ebi.fgpt.urigen.entity.ServerURLConnection
 * JD-Core Version:    0.6.1
 */