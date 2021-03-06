package uk.ac.ebi.fgpt.urigen.model;
/*
 * Copyright (C) 2011, European Bioinformatics Institute
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

import org.codehaus.jackson.annotate.JsonIgnore;
import uk.ac.ebi.fgpt.urigen.dao.*;
import uk.ac.ebi.fgpt.urigen.exception.AutoIDException;
import uk.ac.ebi.fgpt.urigen.exception.UrigenException;

/**
 * Author: Simon Jupp<br>
 * Date: Dec 12, 2011<br>
 * Functional Genomics Group, EMBL-EBI<br>
 */

public interface AutoIDGenerator {


    UrigenEntity getNextID(UrigenRequest request, OWLOntologyDAO ontologyDao) throws AutoIDException;

    @JsonIgnore
    UrigenEntityDAO getUrigenEntityDao();

    @JsonIgnore
    PreferencesDAO getUrigenPreferencesDao();

    String getUniqueUriFragment(UrigenRequest request) throws AutoIDException;

    void setPreferencesDao(PreferencesDAO preferenceDAO);

    void setUrigenEntityDao(UrigenEntityDAO urigenEntityDAO);

//    void setUrigenRangeDao(UserRangeDAO urigenRangeDAO);

    void setOWLOntologyDao(OWLOntologyDAO ontoloyDAO);

    String getId();

    String getDescription();

    boolean isValid(UrigenPreference p) throws UrigenException;

}

