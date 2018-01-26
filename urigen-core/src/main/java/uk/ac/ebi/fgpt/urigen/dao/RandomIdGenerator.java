package uk.ac.ebi.fgpt.urigen.dao;

import uk.ac.ebi.fgpt.urigen.exception.AutoIDException;
import uk.ac.ebi.fgpt.urigen.impl.GeneratorTypes;
import uk.ac.ebi.fgpt.urigen.model.AbstractAutoIDGenerator;
import uk.ac.ebi.fgpt.urigen.model.UrigenRequest;

import java.util.UUID;

/**
 * @author Simon Jupp
 * @date 15/12/2011
 * Functional Genomics Group EMBL-EBI
 */
public class RandomIdGenerator extends AbstractAutoIDGenerator {

    public String getUniqueUriFragment(UrigenRequest request) throws AutoIDException {
        UUID nextId = UUID.randomUUID();
        return nextId.toString();
    }

    public String getId() {
        return GeneratorTypes.RANDOM.getClassId();
    }

    public String getDescription() {
        return GeneratorTypes.RANDOM.getDescription();
    }

}
