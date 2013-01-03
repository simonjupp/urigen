package uk.ac.ebi.fgpt.urigen.web.view;

import uk.ac.ebi.fgpt.urigen.model.UrigenUserRange;

/**
 * @author Simon Jupp
 * @date 22/02/2012
 * Functional Genomics Group EMBL-EBI
 */
public class UrigenUserRangeBean implements UrigenUserRange {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private int userId;
    private int prefsId;
    private int autoIdDigitStart;
    private int autoIdDigitEnd;
    private long lastIdInSequence;
    private boolean success = true;
    private String message;

    public UrigenUserRangeBean() {
    }

    public UrigenUserRangeBean(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public void setLastIdInSequence(long lastIdInSequence) {
        this.lastIdInSequence = lastIdInSequence;
    }

    @Override
    public String toString() {
        return "UrigenUserRangeBean{" +
//                "id=" + id +
                ", userId=" + userId +
                ", prefsId=" + prefsId +
                ", autoIdDigitStart=" + autoIdDigitStart +
                ", autoIdDigitEnd=" + autoIdDigitEnd +
                ", lastIdInSequence=" + lastIdInSequence +
                ", success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
