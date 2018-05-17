package uk.ac.ebi.fgpt.urigen.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.fgpt.urigen.dao.OWLOntologyDAO;
import uk.ac.ebi.fgpt.urigen.dao.PreferencesDAO;
import uk.ac.ebi.fgpt.urigen.dao.UrigenEntityDAO;
import uk.ac.ebi.fgpt.urigen.exception.AutoIDException;
import uk.ac.ebi.fgpt.urigen.exception.UrigenException;
import uk.ac.ebi.fgpt.urigen.impl.SimpleUrigenEntityImpl;

import java.util.concurrent.Semaphore;

/**
 * @author Simon Jupp
 * @date 17/12/2011
 * Functional Genomics Group EMBL-EBI
 */
public abstract class AbstractAutoIDGenerator implements AutoIDGenerator{

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Semaphore semaphore = new Semaphore(1);

    private UrigenEntityDAO urigenEntityDao;

    private PreferencesDAO preferencesDao;

//    private UserRangeDAO userRangeDao;

    private OWLOntologyDAO ontologyDao;

    public void setUrigenEntityDao(UrigenEntityDAO urigenEntityDao) {
        this.urigenEntityDao = urigenEntityDao;
    }

//    public void setUrigenRangeDao(UserRangeDAO userRangeDao) {
//        this.userRangeDao = userRangeDao;
//    }


    public void setPreferencesDao(PreferencesDAO preferencesDao) {
        this.preferencesDao = preferencesDao;
    }

    public void setOWLOntologyDao (OWLOntologyDAO ontologyDao) {
        this.ontologyDao = ontologyDao;
    }


    public PreferencesDAO getUrigenPreferencesDao() {
        return preferencesDao;
    }

    public UrigenEntityDAO getUrigenEntityDao() {
        return urigenEntityDao;
    }

//    public UserRangeDAO getUserRangeDao() {
//        return userRangeDao;
//    }

    public OWLOntologyDAO getOntologyDao() {
        return ontologyDao;
    }

    public UrigenEntity getNextID(UrigenRequest request, OWLOntologyDAO ontologyDao) throws AutoIDException {

        try {
            semaphore.acquire();

            UrigenPreference p = getUrigenPreferencesDao().getPreferencesById(request.getPreferencesId());

            String base_url = p.getBaseUri();
            String separator = p.getSeparator();
            String prefix = p.getPrefix();
            String fragment = getUniqueUriFragment(request);
            String suffix = p.getSuffix();

            StringBuilder generatedUri = new StringBuilder();
            generatedUri.append(base_url);
            generatedUri.append(separator);
            generatedUri.append(prefix);
            generatedUri.append(fragment);
            generatedUri.append(suffix);

            // todo logging
            log.info("generated URI " + generatedUri.toString() );

            UrigenEntity newEntity = getUrigenEntityDao().saveUrigenEntity(new SimpleUrigenEntityImpl(
                    generatedUri.toString(), request.getLocalUri(),
                    request.getLabel(), request.getUserId(), request.getPreferencesId(),
                    null, request.getComment()));


            semaphore.release();

            return newEntity;


        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new AutoIDException("could not lock database: "+e.getMessage());
        }
    }

    public boolean isValid(UrigenPreference p) throws UrigenException {




        return true;
    }
}
