package uk.ac.ebi.fgpt.urigen.panel;

import org.semanticweb.owlapi.model.IRI;

public class UrigenPreferenceOption {
    private IRI ontologyIri;
    private String serverURL;
    private String preferenceName;
    private String restApiKey;

    public UrigenPreferenceOption(IRI ontologyIri, String serverURL, String preferenceName, String restApiKey) {
        this.ontologyIri = ontologyIri;
        this.serverURL = serverURL;
        this.preferenceName = preferenceName;
        this.restApiKey = restApiKey;
    }

    public UrigenPreferenceOption() {
    }

    public IRI getOntologyIri() {
        return this.ontologyIri;
    }

    public void setOntologyIri(IRI ontologyIri) {
        this.ontologyIri = ontologyIri;
    }

    public String getServerURL() {
        return this.serverURL;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    public String getPreferenceName() {
        return this.preferenceName;
    }

    public void setPreferenceName(String preferenceName) {
        this.preferenceName = preferenceName;
    }

    public String getRestApiKey() {
        return this.restApiKey;
    }

    public void setRestApiKey(String restApiKey) {
        this.restApiKey = restApiKey;
    }
}

/* Location:           /Users/jupp/Downloads/org.protege.urigen.jar
 * Qualified Name:     uk.ac.ebi.fgpt.urigen.panel.UrigenPreferenceOption
 * JD-Core Version:    0.6.1
 */