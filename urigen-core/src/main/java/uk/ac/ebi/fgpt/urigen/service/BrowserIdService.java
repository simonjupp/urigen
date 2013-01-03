package uk.ac.ebi.fgpt.urigen.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UrlPathHelper;
import uk.ac.ebi.fgpt.urigen.web.view.BrowserIdResponseBean;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.Properties;

/**
 * @author Simon Jupp
 * @date 21/02/2012
 * Functional Genomics Group EMBL-EBI
 */
public class BrowserIdService {


    public static final String BROWSER_ID_URL = "https://browserid.org/verify";

    public static final String PROXY_HOST_KEY  = "urigen.http.proxyHost";

    public static final String PROXY_PORT_KEY  = "urigen.http.proxyPort";

    public static String proxyHost;

    public static int proxyPort = 80;

    public static String getApiKeyByBrowserId(String assertion, String host) {

        Logger log = LoggerFactory.getLogger(BrowserIdService.class);
        log.debug("fetching browser id for : " + assertion);

        String email = "";
        BrowserIdResponseBean response = new BrowserIdResponseBean();
        log.debug("hostname:" + host);

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
                con = (HttpURLConnection) new URL(BROWSER_ID_URL).openConnection(proxy);
            }
            else {
                con = (HttpURLConnection) new URL(BROWSER_ID_URL).openConnection();
            }


            con.setDoOutput(true);
            con.setRequestMethod("POST");

            String params = "assertion=" + assertion + "&audience=" + host;
            OutputStream os = con.getOutputStream();
            os.write(params.getBytes());
            os.flush();

            ObjectMapper mapper = new ObjectMapper(); // from org.codeahaus.jackson.map
            response = mapper.readValue(con.getInputStream(), BrowserIdResponseBean.class);

            log.debug(response.toString());
            email = response.getEmail();
//
            if (!response.getStatus().equals("okay")) {
                log.error("something went wrong getting browser id auth: " + response.getReason());
                throw new IOException();
            }

            if (email != null) {
                log.debug("got an email: " + email);
            }
            else {
                log.error("something went wrong getting browser id auth: " + response.getReason());
                throw new IOException();
            }

            con.disconnect();

        } catch (MalformedURLException e) {
            log.error("Couldn't access browser id service at " + BROWSER_ID_URL);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        } catch (IOException e) {
            log.error("IO exception accessing: " + BROWSER_ID_URL);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }

        return email;
    }

    public static void main(String[] args) {

        String i = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJicm93c2VyaWQub3JnIiwiZXhwIjoxMzI5ODQwMjg1Njg2LCJpYXQiOjEzMjk3NTM4ODU2ODYsInB1YmxpYy1rZXkiOnsiYWxnb3JpdGhtIjoiRFMiLCJ5IjoiMzg2ZWNkYmFmOTFjMzFlZDUwY2M2N2JiMTUxYmNjYjM5NDIwODYzMmEwNzE2OGQzODc4MTk3OTRjYjZhYzIzYmExZDBkNjI4NDMzOTVhNzgzZDY3MDY4YTQ0MDY4OGFkN2Y0ZjAzOGI0Mjg3NDY3ZmM0OTFkYzVmYTJhYTFhYjE5OTNiZGQwMDE0YzdiNDY1MTUyYWJlY2I0ODI0ZDMwYzFmYmNmNGNkNjNhYTdlNTY0NGNkNDc3ZjAxZWFlZjUwZDkzMWE4ODhhNTM2NzY3ZDYzOTQ5OTEwMjlhMjRlMGM5YmRiZDNjNjhiMDFlYjExMzg1YjkyNDM1NDVmMmM0NTg5YzkyNzVhNGJlNzUzMTQ3MjMwY2U3NDIzZGExYjRmOGI5ZjYzYmEwZTE1YmNhMDI2ZWNkNzAwZmNhNWEzNDI2M2VmMjNhN2M4ZTc4NDI4OWU1ZTc4Y2JkNDNhMDcwNGVjNGJjOGZiMTIxNWQ3ZmIzZTRmZDZmODU0Mjc5OGU4Zjk5NjIyODljYTljMzM1OGRkY2VjMTFiNjYzMTBjMTBiYmU2NDg3OGQ5MWEwOGU4ZmNiOGM5NmNmMWZhN2ZlYzM2NTI4YjFkMTI3MDMzYzA3M2RhMWI3Zjg5ZDg2NTZhOTRiMWYyYmI0OGRiZWQyZWE0ZTIwOGY4M2VhY2U0N2IiLCJwIjoiZDZjNGU1MDQ1Njk3NzU2YzdhMzEyZDAyYzIyODljMjVkNDBmOTk1NDI2MWY3YjU4NzYyMTRiNmRmMTA5YzczOGI3NjIyNmIxOTliYjdlMzNmOGZjN2FjMWRjYzMxNmUxZTdjNzg5NzM5NTFiZmM2ZmYyZTAwY2M5ODdjZDc2ZmNmYjBiOGMwMDk2YjBiNDYwZmZmYWM5NjBjYTQxMzZjMjhmNGJmYjU4MGRlNDdjZjdlNzkzNGMzOTg1ZTNiM2Q5NDNiNzdmMDZlZjJhZjNhYzM0OTRmYzNjNmZjNDk4MTBhNjM4NTM4NjJhMDJiYjFjODI0YTAxYjdmYzY4OGU0MDI4NTI3YTU4YWQ1OGM5ZDUxMjkyMjY2MGRiNWQ1MDViYzI2M2FmMjkzYmM5M2JjZDZkODg1YTE1NzU3OWQ3ZjUyOTUyMjM2ZGQ5ZDA2YTRmYzNiYzIyNDdkMjFmMWE3MGY1ODQ4ZWIwMTc2NTEzNTM3Yzk4M2Y1YTM2NzM3ZjAxZjgyYjQ0NTQ2ZThlN2YwZmFiYzQ1N2UzZGUxZDljNWRiYTk2OTY1YjEwYTJhMDU4MGIwYWQwZjg4MTc5ZTEwMDY2MTA3ZmI3NDMxNGEwN2U2NzQ1ODYzYmM3OTdiNzAwMmViZWMwYjAwMGE5OGViNjk3NDE0NzA5YWMxN2I0MDEiLCJxIjoiYjFlMzcwZjY0NzJjODc1NGNjZDc1ZTk5NjY2ZWM4ZWYxZmQ3NDhiNzQ4YmJiYzA4NTAzZDgyY2U4MDU1YWIzYiIsImciOiI5YTgyNjlhYjJlM2I3MzNhNTI0MjE3OWQ4ZjhkZGIxN2ZmOTMyOTdkOWVhYjAwMzc2ZGIyMTFhMjJiMTljODU0ZGZhODAxNjZkZjIxMzJjYmM1MWZiMjI0YjA5MDRhYmIyMmRhMmM3Yjc4NTBmNzgyMTI0Y2I1NzViMTE2ZjQxZWE3YzRmYzc1YjFkNzc1MjUyMDRjZDdjMjNhMTU5OTkwMDRjMjNjZGViNzIzNTllZTc0ZTg4NmExZGRlNzg1NWFlMDVmZTg0NzQ0N2QwYTY4MDU5MDAyYzM4MTlhNzVkYzdkY2JiMzBlMzllZmFjMzZlMDdlMmM0MDRiN2NhOThiMjYzYjI1ZmEzMTRiYTkzYzA2MjU3MThiZDQ4OWNlYTZkMDRiYTRiMGI3ZjE1NmVlYjRjNTZjNDRiNTBlNGZiNWJjZTlkN2FlMGQ1NWIzNzkyMjVmZWIwMjE0YTA0YmVkNzJmMzNlMDY2NGQyOTBlN2M4NDBkZjNlMmFiYjVlNDgxODlmYTRlOTA2NDZmMTg2N2RiMjg5YzY1NjA0NzY3OTlmN2JlODQyMGE2ZGMwMWQwNzhkZTQzN2YyODBmZmYyZDdkZGYxMjQ4ZDU2ZTFhNTRiOTMzYTQxNjI5ZDZjMjUyOTgzYzU4Nzk1MTA1ODAyZDMwZDdiY2Q4MTljZjZlZiJ9LCJwcmluY2lwYWwiOnsiZW1haWwiOiJzaW1vbi5qdXBwQGdtYWlsLmNvbSJ9fQ.PlP_S8GVfx5ls9lA9ewcfQlgHFKHHT0eC9aH7LTiOHsAdFxHIA75E8lBHTagu7lyMyhzt9WNKFpN7xKFNWH3MUQUS1liLev4OOhZi8ha6GdkeoGjqEpeMAwt1rMqmwTO0UNkuDAFbl2vOfyR-vZRO0sU0Ns0bC7HM_X7Sh9RiuCpJ_T6eKrBeo84eLEI29HdCvHt2dS6MAZB7TMi4OWLMDy6LS4UwW__7atD8BaYL61cckrXaDUMg_y8G0dz7cWrxfmGq8qH7K9bw1zV_BoqRjG41e_Sfuv0NUTi0O1vRpa0VlqSGsDXL5xqFuyDauFxTnQxyKdI8DiKU7QlS0Cy9g~eyJhbGciOiJEUzI1NiJ9.eyJleHAiOjEzMjk4MTk2MzQzODQsImF1ZCI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MyJ9.oE8jvcIpgf8wm9ymacApVdhunSASqqoHruXPj_3WrEADYdWWNsSS0HB5j6jiYv6IhE5CYDu45SX7n3hwPtU77Q";
        BrowserIdService.getApiKeyByBrowserId(i, "localhost");

    }

}
