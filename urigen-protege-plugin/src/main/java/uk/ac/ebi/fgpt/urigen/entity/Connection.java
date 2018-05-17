package uk.ac.ebi.fgpt.urigen.entity;

import org.semanticweb.owlapi.model.IRI;
import uk.ac.ebi.fgpt.urigen.exception.UrigenException;
import uk.ac.ebi.fgpt.urigen.web.view.PreferenceBean;
import uk.ac.ebi.fgpt.urigen.web.view.UrigenEntityBean;
import uk.ac.ebi.fgpt.urigen.web.view.UrigenRequestBean;
import uk.ac.ebi.fgpt.urigen.web.view.UserBean;

public interface Connection {
    UserBean getUrigenUserByApiKey(String paramString)
            throws UrigenException;

    UrigenEntityBean getNewUri(UrigenRequestBean paramUrigenRequestBean, String paramString)
            throws UrigenException;

    PreferenceBean getUrigenPreference(IRI paramIRI)
            throws UrigenException;
}

/* Location:           /Users/jupp/Downloads/org.protege.urigen.jar
 * Qualified Name:     uk.ac.ebi.fgpt.urigen.entity.Connection
 * JD-Core Version:    0.6.1
 */