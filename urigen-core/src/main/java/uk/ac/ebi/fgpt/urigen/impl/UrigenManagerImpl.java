package uk.ac.ebi.fgpt.urigen.impl;


import uk.ac.ebi.fgpt.urigen.dao.*;
import uk.ac.ebi.fgpt.urigen.model.*;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Simon Jupp
 * @date 15/12/2011
 * Functional Genomics Group EMBL-EBI
 */
public class UrigenManagerImpl extends AbstractUrigenManager {

    public UrigenManagerImpl() {
        super();
    }

    public UrigenManagerImpl(PreferencesDAO preferenceDAO, UrigenEntityDAO urigenEntityDAO, UserDAO userDAO, OWLOntologyDAO ontologyDAO) {
        super(preferenceDAO, urigenEntityDAO, userDAO, ontologyDAO);
    }


}
