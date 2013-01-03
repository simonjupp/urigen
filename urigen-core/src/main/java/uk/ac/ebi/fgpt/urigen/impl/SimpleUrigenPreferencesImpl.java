package uk.ac.ebi.fgpt.urigen.impl;

import uk.ac.ebi.fgpt.urigen.model.AutoIDGenerator;
import uk.ac.ebi.fgpt.urigen.model.UrigenPreference;
import uk.ac.ebi.fgpt.urigen.model.UrigenUserRange;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

/**
 * @author Simon Jupp
 * @date 13/12/2011
 * Functional Genomics Group EMBL-EBI
 */
public class SimpleUrigenPreferencesImpl implements UrigenPreference {

    private long nano_time = System.nanoTime();

    // this should get the info it needs from the database... all it needs is a
    private String ontologyName;

    private String ontologyUri;

    private String baseUri = "http://www.semanticweb.org/uriserver/Ontology_" + nano_time;

    private String ontologyPhysicalUri;

    private int id = 1;

    private long lastId = -1;

    private String separator = "/";

    private String autoIdGeneratorClassName = GeneratorTypes.ITERATIVE.toString();

    private String prefix = "TEST_";

    private String suffix = "";

    private int autoIdDigitCount = 20;

    private int autoIdStart = 1;

    private int autoIdEnd = -1;

    private boolean checkSourceOntology = false;

    private boolean statusOK = true;

    private String conflictMessage = "";

    private AutoIDGenerator autoIDGenerator;

    private Date lastChecked;

    private String md5;

    public void setConflictMessage(String conflictMessage) {
        this.conflictMessage = conflictMessage;
    }

    public void setStatusOK(boolean statusOK) {
        this.statusOK = statusOK;
    }

    public SimpleUrigenPreferencesImpl(String ontologyName,
                                       String ontologyUri,
                                       String baseUri,
                                       String ontologyPhysicalUri,
                                       String separator,
                                       String autoIdGeneratorClassName,
                                       String prefix,
                                       String suffix,
                                       int autoIdDigitCount,
                                       int start,
                                       int autoIdEnd,
                                       boolean checkSourceOntology) {
        this.ontologyName = ontologyName;
        this.ontologyUri = ontologyUri;
        this.baseUri = baseUri;
        this.ontologyPhysicalUri = ontologyPhysicalUri;
        this.separator = separator;
        this.autoIdGeneratorClassName = autoIdGeneratorClassName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.autoIdDigitCount = autoIdDigitCount;
        this.autoIdStart = start;
        this.autoIdEnd = autoIdEnd;
        this.checkSourceOntology = checkSourceOntology;
    }

    public SimpleUrigenPreferencesImpl() {

    }

    private Collection<UrigenUserRange> userRange = new HashSet<UrigenUserRange>();


    public int getPreferenceId() {
        return id;
    }

    public String getOntologyName() {
        return ontologyName;
    }

    public String getOntologyUri() {
        return ontologyUri;
    }

    public String getOntologyPhysicalUri() {
        return ontologyPhysicalUri;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public String getSeparator() {
        return separator;
    }

    public AutoIDGenerator getAutoIDGenerator() {

        Class<? extends AutoIDGenerator> prefAutoIDClass;
        try {

            prefAutoIDClass = (Class<? extends AutoIDGenerator>) Class.forName(autoIdGeneratorClassName);
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

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public int getAutoIdDigitCount() {
        return autoIdDigitCount;
    }

    public int getAutoIdStart() {
        return autoIdStart;
    }

    public int getAutoIdEnd() {
        return autoIdEnd;
    }

    public long getLastIdInSequence() {
        return lastId;
    }

    public boolean getCheckSource() {
        return checkSourceOntology;
    }

    public boolean statusOK() {
        return statusOK;
    }

    public String conflictMessage() {
        return conflictMessage;
    }

    public void setPreferenceId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SimpleUrigenPreferencesImpl{" +
                ", ontologyName='" + ontologyName + '\'' +
                ", ontologyUri='" + ontologyUri + '\'' +
                ", baseUri='" + baseUri + '\'' +
                ", ontologyPhysicalUri='" + ontologyPhysicalUri + '\'' +
                ", id=" + id +
                ", lastId=" + lastId +
                ", separator='" + separator + '\'' +
                ", autoIdGeneratorClassName='" + autoIdGeneratorClassName + '\'' +
                ", prefix='" + prefix + '\'' +
                ", suffix='" + suffix + '\'' +
                ", autoIdDigitCount=" + autoIdDigitCount +
                ", autoIdStart=" + autoIdStart +
                ", autoIdEnd=" + autoIdEnd +
                ", checkSourceOntology=" + checkSourceOntology +
                ", statusOK=" + statusOK +
                ", conflictMessage='" + conflictMessage + '\'' +
                ", userRange=" + userRange +
                '}';
    }

    public void setLastIdInSequence(long lastId) {
        this.lastId = lastId;
    }

    public Collection<UrigenUserRange> getAllUserRange() {
        return userRange;
    }

    public Date getLastOntologyCheckDate() {
        return lastChecked;
    }

    public String getOntologyMD5checksum() {
        return md5;
    }

    public void setLastOntologyCheckDate(Date date) {
        lastChecked = date;

    }

    public void setOntologyMD5checksum(String md5) {
        this.md5 = md5;

    }
}
