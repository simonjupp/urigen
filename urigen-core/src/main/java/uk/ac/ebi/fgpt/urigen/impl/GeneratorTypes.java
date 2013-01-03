package uk.ac.ebi.fgpt.urigen.impl;

import java.lang.reflect.GenericDeclaration;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Simon Jupp
 * @date 15/12/2011
 * Functional Genomics Group EMBL-EBI
 */
public enum GeneratorTypes {

    PSEUDO_RANDOM ("uk.ac.ebi.fgpt.urigen.dao.PseudoRandomIdGenerator", "Pseudo Random"),

    ITERATIVE ("uk.ac.ebi.fgpt.urigen.dao.IterativeIdGenerator", "Iterative"),

    ITERATIVE_RANGE ("uk.ac.ebi.fgpt.urigen.dao.UserRangeGenerator", "User Range"),

    RANDOM ("uk.ac.ebi.fgpt.urigen.dao.RandomIdGenerator", "Random");

    private String classId;
    private String description;
    public static final Set<String> ALL_TYPES_ID;
    public static final Set<String> ALL_TYPES_DESC;

    static {
        ALL_TYPES_ID = new HashSet<String>();
        ALL_TYPES_DESC = new HashSet<String>();

        for(GeneratorTypes v : GeneratorTypes.values()) {
            ALL_TYPES_ID.add(v.getClassId());
            ALL_TYPES_DESC.add(v.getDescription());
        }
    }

    public String getClassId() {
        return classId;
    }

    public String getDescription() {
        return description;
    }

    GeneratorTypes(String classId, String description) {
        this.classId = classId;
        this.description = description;
    }

//    public String getDescriptions(String classid) {
//        for(GeneratorTypes v : GeneratorTypes.values()) {
//            if (v.getClassId().equals(classid)) {
//                return v.getDescription();
//            }
//        }
//        return null;
//    }

    @Override
    public String toString() {
        return classId;
    }
}
