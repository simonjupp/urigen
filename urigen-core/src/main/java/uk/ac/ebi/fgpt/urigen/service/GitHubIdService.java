package uk.ac.ebi.fgpt.urigen.service;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.naming.AuthenticationException;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * @author Simon Jupp
 * @date 02/12/2016
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class GitHubIdService {

    public static final String PROXY_HOST_KEY  = "urigen.http.proxyHost";

    public static final String PROXY_PORT_KEY  = "urigen.http.proxyPort";

    public static String proxyHost;

    public static int proxyPort = 80;

    public static final String GITHUB_ACCESS_URL = "https://github.com/login/oauth/access_token";
    public static final String GITHUB_USER_URL = "https://api.github.com/user/emails";

    public String clientId = "";
    public String secretId = "";
    public String redirectUrl = "";
    public String state = "";


    public GitHubIdService() {
        this.state = UUID.randomUUID().toString();

    }

    public GitHubIdService(String clientId, String secretId, String redirectUrl) {
        this.clientId = clientId;
        this.secretId = secretId;
        this.redirectUrl = redirectUrl;
        this.state = UUID.randomUUID().toString();
    }

    public String getClientId() {
        return clientId;
    }

    public String getSecretId() {
        return secretId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getState() {
        return state;
    }

    public Collection<String> getEmailFromGithub(String code, String state) throws AuthenticationException {

        Logger log = LoggerFactory.getLogger(GitHubIdService.class);
        log.debug("fetching github id for : " + code);
        log.debug("state:" + state);

        Collection<String> email = new HashSet<String>();

        HttpURLConnection con;

        proxyHost = System.getProperty(PROXY_HOST_KEY);
        proxyPort = 80;
        if (System.getProperty(PROXY_PORT_KEY) != null ) {
            proxyPort = Integer.parseInt(System.getProperty(PROXY_PORT_KEY, "80"));
        }

        log.debug(proxyHost +":"+proxyPort);

        try {
            if (proxyHost != null) {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
                con = (HttpsURLConnection) new URL(GITHUB_ACCESS_URL).openConnection(proxy);
            }
            else {
                con = (HttpsURLConnection) new URL(GITHUB_ACCESS_URL).openConnection();
            }


            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept", "application/json");

            String params = "client_id=" + clientId + "&client_secret=" + secretId + "&code=" + code  + "&redirect_uri=" + redirectUrl+ "&state=" + state;
            OutputStream os = con.getOutputStream();
            os.write(params.getBytes());
            os.flush();

            ObjectMapper mapper = new ObjectMapper(); // from org.codeahaus.jackson.map
            GitHubAccessResponseBean response = mapper.readValue(con.getInputStream(), GitHubAccessResponseBean.class);
            log.debug(response.toString());

            String access_token = response.getAccess_token();

            con.disconnect();
            String error;

            if (access_token != null) {

                // not get e-mail with access token


                if (proxyHost != null) {
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
                    con = (HttpsURLConnection) new URL(GITHUB_USER_URL).openConnection(proxy);
                }
                else {
                    con = (HttpsURLConnection) new URL(GITHUB_USER_URL).openConnection();
                }

                con.setDoOutput(true);
                con.setRequestMethod("GET");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Authorization", "token " + access_token);

                mapper = new ObjectMapper(); // from org.codeahaus.jackson.map
                List<GitHubUserResponseBean> userResponse = mapper.readValue(con.getInputStream(), new TypeReference<List<GitHubUserResponseBean>>(){});
                for (GitHubUserResponseBean user : userResponse) {

                    email.add(user.getEmail());
                }

                con.disconnect();

            }
//
            if (!email.isEmpty()) {
                log.debug("got an email: " + email);
            }
            else {
                log.error("something went wrong getting github id auth");
                throw new IOException();
            }


        } catch (Exception e) {
            log.error("Github login exception");
            throw new AuthenticationException("Can't login to github");
        }

        return email;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GitHubUserResponseBean {
        private String email;
        private boolean primary;
        private boolean verified;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPrimary(boolean primary) {
            this.primary = primary;
        }

        public void setVerified(boolean verified) {
            this.verified = verified;
        }

        public boolean isPrimary() {
            return primary;
        }

        public boolean isVerified() {
            return verified;
        }

        public GitHubUserResponseBean() {

        }
    }

    public static class GitHubAccessResponseBean {
        public String access_token;
        public String scope;
        public String token_type;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public GitHubAccessResponseBean() {

        }
    }

}
