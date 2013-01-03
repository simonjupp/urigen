package uk.ac.ebi.fgpt.urigen.impl;
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

import uk.ac.ebi.fgpt.urigen.model.UrigenEntity;
import uk.ac.ebi.fgpt.urigen.model.UrigenPreference;
import uk.ac.ebi.fgpt.urigen.model.UrigenUser;

import java.net.URI;
import java.util.Date;

/**
 * Author: Simon Jupp<br>
 * Date: Dec 12, 2011<br>
 * Functional Genomics Group, EMBL-EBI<br>
 */

public class SimpleUrigenEntityImpl implements UrigenEntity {

    private int Id;

    private String generatedUri;

    private String originalUri;

    private String shortForm = "";

    private String label = "";

    private int userId;

    private int preferencesId;

    private Date date;

    private String comment = "";

    public SimpleUrigenEntityImpl(String generatedUri, String originalUri, String label, int user, int preferences, Date date, String comment) {
        this.generatedUri = generatedUri;
        this.originalUri = originalUri;
        this.label = label;
        this.userId = user;
        this.preferencesId = preferences;
        this.date = date;
        this.comment = comment;

    }

    public SimpleUrigenEntityImpl() {

    }

    public Date getDate() {
        return date;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public int getId() {
        return Id;
    }

    public String getGeneratedUri() {
//        if (generatedUri != null) {
//            return URI.create(generatedUri);
//        }
        return generatedUri;
    }

    public String getOriginalUri() {
//        if (originalUri != null) {
//            return URI.create(originalUri);
//        }
        return originalUri;
    }

    public String getShortForm() {
        return shortForm;
    }

    public String getLabel() {
        return label;
    }

    public String getComment() {
        return null;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "SimpleUrigenEntityImpl{" +
                "Id=" + Id +
                ", generatedUri='" + generatedUri + '\'' +
                ", originalUri='" + originalUri + '\'' +
                ", shortForm='" + shortForm + '\'' +
                ", label='" + label + '\'' +
                ", date=" + date +
                ", comment='" + comment + '\'' +
                '}';
    }

    public int getPreferencesId() {
        return preferencesId;
    }

}
