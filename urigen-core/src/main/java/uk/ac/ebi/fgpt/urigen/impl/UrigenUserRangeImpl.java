package uk.ac.ebi.fgpt.urigen.impl;

import uk.ac.ebi.fgpt.urigen.model.UrigenUserRange;

/**
 * @author Simon Jupp
 * @date 23/02/2012
 * Functional Genomics Group EMBL-EBI
 */
public class UrigenUserRangeImpl implements UrigenUserRange {

    private int id;
    private int userId;
    private int prefsId;
    private int autoIdDigitStart;
    private int autoIdDigitEnd;
    private long lastIdInSequence = -1;



    public UrigenUserRangeImpl(int userId, int prefsId, int autoIdDigitStart, int autoIdDigitEnd) {
        this.userId = userId;
        this.prefsId = prefsId;
        this.autoIdDigitStart = autoIdDigitStart;
        this.autoIdDigitEnd = autoIdDigitEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPrefsId() {
        return prefsId;
    }

    public void setPrefsId(int prefsId) {
        this.prefsId = prefsId;
    }

    public int getAutoIdDigitStart() {
        return autoIdDigitStart;
    }

    public void setAutoIdDigitStart(int autoIdDigitStart) {
        this.autoIdDigitStart = autoIdDigitStart;
    }

    public int getAutoIdDigitEnd() {
        return autoIdDigitEnd;
    }

    public void setAutoIdDigitEnd(int autoIdDigitEnd) {
        this.autoIdDigitEnd = autoIdDigitEnd;
    }

    public long getLastIdInSequence() {
        return lastIdInSequence;
    }

    public void setPreferenceId(int id) {
        this.prefsId = id;
    }

    public void setLastIdInSequence(long id) {
        lastIdInSequence = id;
    }

    public void setLastIdInSequence(int lastIdInSequence) {
        this.lastIdInSequence = lastIdInSequence;
    }


    @Override
    public String toString() {
        return "UrigenUserRangeImpl{" +
//                "id=" + id +
                ", userId=" + userId +
                ", prefsId=" + prefsId +
                ", autoIdDigitStart=" + autoIdDigitStart +
                ", autoIdDigitEnd=" + autoIdDigitEnd +
                ", lastIdInSequence=" + lastIdInSequence +
                '}';
    }
}
