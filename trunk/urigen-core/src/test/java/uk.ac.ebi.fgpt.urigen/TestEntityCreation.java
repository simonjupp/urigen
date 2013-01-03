package uk.ac.ebi.fgpt.urigen;/*
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

import junit.framework.TestCase;
import uk.ac.ebi.fgpt.urigen.dao.*;
import uk.ac.ebi.fgpt.urigen.exception.PreferenceCreationException;
import uk.ac.ebi.fgpt.urigen.exception.UrigenException;
import uk.ac.ebi.fgpt.urigen.exception.UserCreateException;
import uk.ac.ebi.fgpt.urigen.impl.*;
import uk.ac.ebi.fgpt.urigen.model.*;

/**
 * Author: Simon Jupp<br>
 * Date: Dec 12, 2011<br>
 * Functional Genomics Group, EMBL-EBI<br>
 */

public class TestEntityCreation extends TestCase {

    public void testEntityCreation () {

        SimpleUrigenPreferencesImpl prefs = new SimpleUrigenPreferencesImpl();

        UrigenUser user = new SimpleUrigenUserImpl("simon", "jupp@ebi.ac.uk", Long.toString(System.currentTimeMillis()), true);

        PreferencesDAO pDAO = new InMemPreferencesDAO();
        UrigenEntityDAO eDAO = new InMemUrigenEntityDAO();
        UserDAO uDAO = new InMemUserDAO();


        try {
            pDAO.savePreferences(prefs);
            uDAO.saveUser(user);

        } catch (PreferenceCreationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UserCreateException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        UrigenManager manager = new UrigenManagerImpl(pDAO, eDAO, uDAO, new OWLAPIOntologyDAO());

        try {
            UrigenRequest request1 = new SimpleUrigenRequestImpl(user.getId(), "http://local_id/000101", prefs.getPreferenceId(), "label 1", " no comment");
            UrigenEntity entity1 = manager.getNewEntity(request1);

            UrigenRequest request2 = new SimpleUrigenRequestImpl(user.getId(), "http://local_id/000102", prefs.getPreferenceId(), "label 2", " no comment");
            UrigenEntity entity2 = manager.getNewEntity(request2);

            UrigenRequest request3 = new SimpleUrigenRequestImpl(user.getId(), "http://local_id/000103", prefs.getPreferenceId(), "label 3", " no comment");
            UrigenEntity entity3 = manager.getNewEntity(request3);

        } catch (UrigenException e) {
            fail();
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        for (UrigenEntity e : manager.getAllGeneratedUris()) {
            System.out.println(e.toString());
        }
    }
}
