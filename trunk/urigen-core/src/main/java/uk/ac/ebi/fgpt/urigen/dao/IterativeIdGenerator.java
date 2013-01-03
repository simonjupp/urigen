package uk.ac.ebi.fgpt.urigen.dao;

import uk.ac.ebi.fgpt.urigen.exception.AutoIDException;
import uk.ac.ebi.fgpt.urigen.exception.PreferenceCreationException;
import uk.ac.ebi.fgpt.urigen.impl.GeneratorTypes;
import uk.ac.ebi.fgpt.urigen.model.*;

import java.text.FieldPosition;
import java.text.NumberFormat;

/**
 * @author Simon Jupp
 * @date 15/12/2011
 * Functional Genomics Group EMBL-EBI
 */

public class IterativeIdGenerator  extends AbstractAutoIDGenerator  {


    private String pad(long l, int padding) {
        StringBuffer sb = new StringBuffer();
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(0);
        format.setMinimumIntegerDigits(padding);
        format.setMaximumIntegerDigits(padding);
        format.setGroupingUsed(false);
        format.format(l, sb, new FieldPosition(0));
        return sb.toString();
    }


    public String getUniqueUriFragment(UrigenRequest request) throws AutoIDException {
        UrigenPreference p = getUrigenPreferencesDao().getPreferencesById(request.getPreferencesId());

        request.getUserId();

        long end = p.getAutoIdEnd();
        long id = p.getAutoIdStart();

        if(p.getLastIdInSequence() == -1) {
            id = p.getAutoIdStart();
        }
        else if (p.getLastIdInSequence() == p.getAutoIdStart()) {
            id = p.getLastIdInSequence() + 1;
        }
        else if (p.getLastIdInSequence() > p.getAutoIdStart()) {
            id = p.getLastIdInSequence() + 1;
        }

        if (end != -1 && id > end){
            throw new AutoIDException("You have run out of IDs for creating new entities - max = " + end);
        }

        p.setLastIdInSequence(id);
        try {
            getUrigenPreferencesDao().update(p);
        } catch (PreferenceCreationException e) {
            throw new AutoIDException("Error updating id in the database");
        }


        return pad(id , p.getAutoIdDigitCount());
    }

    public String getId() {
        return GeneratorTypes.ITERATIVE.getClassId();
    }

    public String getDescription() {
        return GeneratorTypes.ITERATIVE.getDescription();
    }


}
