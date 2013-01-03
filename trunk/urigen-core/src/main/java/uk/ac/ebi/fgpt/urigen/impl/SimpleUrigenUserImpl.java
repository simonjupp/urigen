package uk.ac.ebi.fgpt.urigen.impl;

import uk.ac.ebi.fgpt.urigen.model.UrigenUser;

/**
 * @author Simon Jupp
 * @date 15/12/2011
 * Functional Genomics Group EMBL-EBI
 */
public class SimpleUrigenUserImpl implements UrigenUser {

    private int id;
    private String userName;
    private String email;
    private String apiKey;
    private boolean isAdmin = false;

    public SimpleUrigenUserImpl(String userName, String email, String apiKey, boolean admin) {
        this.userName = userName;
        this.email = email;
        this.apiKey = apiKey;
        isAdmin = admin;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "SimpleUrigenUserImpl{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
