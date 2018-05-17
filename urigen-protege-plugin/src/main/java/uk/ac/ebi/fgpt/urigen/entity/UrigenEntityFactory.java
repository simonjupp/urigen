package uk.ac.ebi.fgpt.urigen.entity;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.entity.AutoIDException;
import org.protege.editor.owl.model.entity.CustomOWLEntityFactory;
import org.protege.editor.owl.model.entity.OWLEntityCreationException;
import org.protege.editor.owl.model.entity.OWLEntityCreationSet;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.fgpt.urigen.exception.UrigenException;
import uk.ac.ebi.fgpt.urigen.web.view.PreferenceBean;
import uk.ac.ebi.fgpt.urigen.web.view.UrigenEntityBean;
import uk.ac.ebi.fgpt.urigen.web.view.UrigenRequestBean;
import uk.ac.ebi.fgpt.urigen.web.view.UserBean;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

public class UrigenEntityFactory extends CustomOWLEntityFactory {
    private static final String defaultBaseURI = "http://urigen_local_uri/";
    private final Logger logger = LoggerFactory.getLogger(UrigenEntityFactory.class);
    private final OWLModelManager mngr;

    public UrigenEntityFactory(OWLModelManager mngr) {
        super(mngr);
        this.mngr = mngr;
    }

    private boolean isIRIAlreadyUsed(IRI iri) {
        for (OWLOntology ont : this.mngr.getOntologies()) {
            if (ont.containsEntityInSignature(iri)) {
                return true;
            }
        }
        return false;
    }

    protected <T extends OWLEntity> CustomOWLEntityFactory.EntityNameInfo generateName(Class<T> type, String shortName, IRI baseURI) throws AutoIDException, URISyntaxException, OWLEntityCreationException {
        UUID random = UUID.randomUUID();
        baseURI = IRI.create(defaultBaseURI + random.toString());

        if (isIRIAlreadyUsed(baseURI)) {
            throw new OWLEntityCreationException("Entity already exists: " + baseURI.toString());
        }

        this.logger.debug("generating uri: " + shortName + " baseURI: " + baseURI);

        IRI activeOntologyIri = this.mngr.getActiveOntology().getOntologyID().getOntologyIRI().orNull();

        String serverUrl = UrigenPreference.getServerUrlByOntologyIri(activeOntologyIri);
        if ((serverUrl == null) || ("".equals(serverUrl))) {
            this.logger.warn("no urigen server associated with " + activeOntologyIri.toString());
            return super.generateName(type, shortName, baseURI);
        }

        try {
            Connection connection = new ServerURLConnection(serverUrl);

            String apiKey = UrigenPreference.getApiKeyByOntologyIRI(activeOntologyIri);

            PreferenceBean p = connection.getUrigenPreference(activeOntologyIri);

            UserBean u = connection.getUrigenUserByApiKey(apiKey);

            UrigenRequestBean request = new UrigenRequestBean(u.getId(), baseURI.toString(), p.getPreferenceId(), shortName, "");
            UrigenEntityBean newEntity = connection.getNewUri(request, apiKey);
            if (newEntity.getStatusOK()) {
                return new CustomOWLEntityFactory.EntityNameInfo(IRI.create(newEntity.getGeneratedUri()), "", shortName);
            }

            this.logger.error("Error creating new entity: " + newEntity.getErrorMessage());
            return super.generateName(type, shortName, baseURI);
        } catch (IOException e) {
            this.logger.debug("Can't connect to Urigen server at : " + serverUrl);
            return super.generateName(type, shortName, baseURI);
        } catch (UrigenException e) {
            this.logger.error("Can't access urigen server: " + e.getMessage());
            e.printStackTrace();
        }
        return super.generateName(type, shortName, baseURI);
    }

    public <T extends OWLEntity> OWLEntityCreationSet<T> preview(Class<T> type, String shortName, IRI baseIRI)
            throws OWLEntityCreationException {
        this.logger.debug("generating uri: " + shortName + " baseURI: " + baseIRI);

        UUID random = UUID.randomUUID();
        baseIRI = IRI.create(defaultBaseURI + random.toString());

        OWLEntity entity = getOWLEntity(this.mngr.getOWLDataFactory(), type, baseIRI);

        List changes = getChanges(entity, new CustomOWLEntityFactory.EntityNameInfo(baseIRI, "", shortName));

        return new OWLEntityCreationSet(entity, changes);
    }
}

/* Location:           /Users/jupp/Downloads/org.protege.urigen.jar
 * Qualified Name:     uk.ac.ebi.fgpt.urigen.entity.UrigenEntityFactory
 * JD-Core Version:    0.6.1
 */