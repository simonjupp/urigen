package uk.ac.ebi.fgpt.urigen.web.view;

/**
 * @author Simon Jupp
 * @date 21/02/2012
 * Functional Genomics Group EMBL-EBI
 */
public class BrowserIdResponseBean {

    public String status;
    public String email;
    public String audience;
    public String expires;
    public String issuer;

    public String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "BrowserIdPojo{" +
                "status='" + status + '\'' +
                ", email='" + email + '\'' +
                ", audience='" + audience + '\'' +
                ", expires='" + expires + '\'' +
                ", issuer='" + issuer + '\'' +
                '}';
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }



}
