package uk.ac.ebi.fgpt.urigen.dao;

import uk.ac.ebi.fgpt.urigen.exception.AutoIDException;
import uk.ac.ebi.fgpt.urigen.impl.GeneratorTypes;
import uk.ac.ebi.fgpt.urigen.model.AbstractAutoIDGenerator;
import uk.ac.ebi.fgpt.urigen.model.UrigenRequest;

/**
 * @author Simon Jupp
 * @date 15/12/2011
 * Functional Genomics Group EMBL-EBI
 */
public class PseudoRandomIdGenerator extends AbstractAutoIDGenerator {


    public String getUniqueUriFragment(UrigenRequest request) throws AutoIDException {
        long nextId = System.nanoTime();
        return String.valueOf(nextId);
    }

    public String getId() {
        return GeneratorTypes.PSEUDO_RANDOM.getClassId();
    }

    public String getDescription() {
        return GeneratorTypes.PSEUDO_RANDOM.getDescription();
    }

}
