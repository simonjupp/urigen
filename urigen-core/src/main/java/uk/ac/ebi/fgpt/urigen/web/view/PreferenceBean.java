package uk.ac.ebi.fgpt.urigen.web.view;

import org.codehaus.jackson.annotate.JsonIgnore;
import uk.ac.ebi.fgpt.urigen.model.AutoIDGenerator;
import uk.ac.ebi.fgpt.urigen.model.UrigenPreference;
import uk.ac.ebi.fgpt.urigen.model.UrigenUserRange;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

/**
 * @author Simon Jupp
 * @date 04/01/2012
 * Functional Genomics Group EMBL-EBI
 */
public class PreferenceBean implements UrigenPreference {

    private int preferenceId =-1;
    private String ontologyName;
    private AutoIDGenerator autoIDGenerator;
    private String ontologyUri;
    private String ontologyPhysicalUri;
    private String baseUri;
    private String separator;
    private String prefix;
    private String suffix;
    private int autoIdStart;
    private int autoIdEnd;
    private int autoIdDigitCount;
    private long lastIdInSequence;
    private boolean checkSource;
    private Date lastChecked;
    private String md5;

    private Collection<UrigenUserRange> allUserRange = new HashSet<UrigenUserRange>();

    private String autoIdGenerator;

    private boolean success = true;
    private String message;

    public PreferenceBean() {

    }
    public PreferenceBean(boolean success, String message) {
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

    public void setAutoIdGenerator(String autoIdGenerator) {
        this.autoIdGenerator = autoIdGenerator;
    }

    public void setOntologyUri(String ontologyUri) {
        this.ontologyUri = ontologyUri;
    }

    public String getAutoIdGenerator() {
        return autoIdGenerator;
    }


    public boolean getCheckSource() {
        return checkSource;
    }

    public boolean statusOK() {
        return success;
    }

    public String conflictMessage() {
        return message;
    }

    public void setCheckSource(boolean checkSource) {
        this.checkSource = checkSource;
    }

    public int getPreferenceId() {
        return preferenceId;
    }

    public String getOntologyName() {
        return ontologyName;
    }

    public void setOntologyName(String ontologyname) {
        this.ontologyName = ontologyname;
    }

    public String getOntologyPhysicalUri() {
        return ontologyPhysicalUri;
    }

    public String getOntologyUri() {
        return ontologyUri;
    }

    public void setOntologyPhysicalUri(String ontologyPhysicalUri) {
        this.ontologyPhysicalUri = ontologyPhysicalUri;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public int getAutoIdStart() {
        return autoIdStart;
    }

    public void setAutoIdStart(int autoIdStart) {
        this.autoIdStart = autoIdStart;
    }

    public int getAutoIdEnd() {
        return autoIdEnd;
    }

    public void setAutoIdEnd(int autoIdDigitEnd) {
        this.autoIdEnd = autoIdDigitEnd;
    }

    public int getAutoIdDigitCount() {
        return autoIdDigitCount;
    }

    public void setAutoIdDigitCount(int autoIdDigitCount) {
        this.autoIdDigitCount = autoIdDigitCount;
    }

    public long getLastIdInSequence() {
        return lastIdInSequence;
    }

    public void setLastIdInSequence(long lastIdInSequence) {
        this.lastIdInSequence = lastIdInSequence;
    }

    @Override
    public String toString() {
        return "PreferenceBean{" +
                "preferenceId=" + preferenceId +
                ", ontologyName='" + ontologyName + '\'' +
                ", ontologyPhysicalUri='" + ontologyPhysicalUri + '\'' +
                ", baseUri='" + baseUri + '\'' +
                ", separator='" + separator + '\'' +
                ", prefix='" + prefix + '\'' +
                ", suffix='" + suffix + '\'' +
                ", autoIdStart=" + autoIdStart +
                ", autoIdEnd=" + autoIdEnd +
                ", autoIdDigitCount=" + autoIdDigitCount +
                ", lastIdInSequence=" + lastIdInSequence +
                ", checkSource=" + checkSource +
                ", allUserRange=" + allUserRange +
                ", autoIdGenerator='" + autoIdGenerator + '\'' +
                '}';
    }


    public void setPreferenceId(int id) {
        this.preferenceId = id;
    }

    @JsonIgnore
    public AutoIDGenerator getAutoIDGenerator() {

        Class<? extends AutoIDGenerator> prefAutoIDClass;
        try {

            prefAutoIDClass = (Class<? extends AutoIDGenerator>) Class.forName(getAutoIdGenerator());
            if (autoIDGenerator == null){
                autoIDGenerator = prefAutoIDClass.newInstance();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return autoIDGenerator;
    }

    public Collection<UrigenUserRange> getAllUserRange() {
        return allUserRange;
    }

    @JsonIgnore
    public Date getLastOntologyCheckDate() {
        return lastChecked;
    }

    @JsonIgnore
    public String getOntologyMD5checksum() {
        return md5;
    }

    public void setLastOntologyCheckDate(Date date) {
        this.lastChecked = date;
    }

    public void setOntologyMD5checksum(String md5) {
        this.md5 = md5;
    }

    //    public Collection<UrigenUserRangeBean> getAllUserRangeBean() {
//        return allUserRange;
//    }
//
    public void setAllUserRange(Collection<UrigenUserRangeBean> allUserRange) {
        for (UrigenUserRangeBean b : allUserRange) {
            this.allUserRange.add(b);
        }
    }
//

}

