package uk.ac.ebi.fgpt.urigen.dao;

import uk.ac.ebi.fgpt.urigen.exception.AutoIDException;
import uk.ac.ebi.fgpt.urigen.exception.UrigenException;
import uk.ac.ebi.fgpt.urigen.impl.GeneratorTypes;
import uk.ac.ebi.fgpt.urigen.model.*;

import java.text.FieldPosition;
import java.text.NumberFormat;

/**
 * @author Simon Jupp
 * @date 21/02/2012
 * Functional Genomics Group EMBL-EBI
 */
public class UserRangeGenerator  extends AbstractAutoIDGenerator {

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

        System.out.println("processing request" +  request.toString());

        UrigenPreference p = getUrigenPreferencesDao().getPreferencesById(request.getPreferencesId());

        UrigenUserRange r = getUrigenPreferencesDao().getUserRange(request.getUserId(), request.getPreferencesId());
        if (r == null) {
            throw new AutoIDException("No user range associated with this preference");
        }
        long end = r.getAutoIdDigitEnd();
        long id = r.getAutoIdDigitStart();
        if(r.getLastIdInSequence() == -1) {
            id = r.getAutoIdDigitStart();
        }
        else if (r.getLastIdInSequence() == r.getAutoIdDigitStart()) {
            id = r.getLastIdInSequence() + 1;
        }
        else if (r.getLastIdInSequence() > r.getAutoIdDigitStart()) {
            id = r.getLastIdInSequence() + 1;
        }

        if (end != -1 && id > end){
            throw new AutoIDException("You have run out of IDs for creating new entities - max = " + end);
        }
        r.setLastIdInSequence(id);
        try {
            getUrigenPreferencesDao().updateUserRange(r);
        } catch (UrigenException e) {
            throw new AutoIDException("Problem updating auto id for user with id: " + r.getUserId() );
        }
        return pad(id , p.getAutoIdDigitCount());
    }

    public String getId() {
        return GeneratorTypes.ITERATIVE_RANGE.getClassId();
    }

    public String getDescription() {
        return GeneratorTypes.ITERATIVE_RANGE.getDescription();
    }
}
