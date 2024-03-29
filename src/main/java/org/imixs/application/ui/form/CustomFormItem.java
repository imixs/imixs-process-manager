/*******************************************************************************
 *  Imixs Workflow 
 *  Copyright (C) 2001, 2011 Imixs Software Solutions GmbH,  
 *  http://www.imixs.com
 *  
 *  This program is free software; you can redistribute it and/or 
 *  modify it under the terms of the GNU General Public License 
 *  as published by the Free Software Foundation; either version 2 
 *  of the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 *  General Public License for more details.
 *  
 *  You can receive a copy of the GNU General Public
 *  License at http://www.gnu.org/licenses/gpl.html
 *  
 *  Project: 
 *  	http://www.imixs.org
 *  	http://java.net/projects/imixs-workflow
 *  
 *  Contributors:  
 *  	Imixs Software Solutions GmbH - initial API and implementation
 *  	Ralph Soika - Software Developer
 *******************************************************************************/

package org.imixs.application.ui.form;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces.model.SelectItem;

/**
 * This CustomFormItem provides the informations from a single item inside a
 * custom form section
 * <p>
 * The optional 'options' contains a list of select options. Example:
 * <code>SEPA|sepa_transfer;Bankeinzug/ Kreditkarte|direct_debit"</code>
 * 
 * 
 * @author rsoika
 * @version 1.0
 */
public class CustomFormItem {

    String name;
    String type;
    String label;
    boolean required;
    boolean readonly;
    String options;

    public CustomFormItem(String name, String type, String label, boolean required, boolean readonly, String options) {
        super();
        this.label = label;
        this.name = name;
        this.type = type;
        this.required = required;
        this.readonly = readonly;
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    /**
     * SelectItem getter Method provides a getter method to an ArrayList of
     * <SelectItem> objects for a given options String. The options String contains
     * multiple options spearated by ; One option can be devided by a | into a label
     * and a value component. Example:
     * <p>
     * <code>
     *   SEPA|sepa_transfer;Bankeinzug/ Kreditkarte|direct_debit"
     * </code>
     * 
     * <code>
     * <f:selectItems value="#{item.selectItems}" />
     * </code>
     * 
     * @return
     * @throws Exception
     */
    public List<SelectItem> getSelectItems() throws Exception {
        ArrayList<SelectItem> selection;
        selection = new ArrayList<>();

        // check if a value for this param is available...
        // if not return an empty list
        if (this.options == null || this.options.isEmpty()) {
            return selection;
        }

        // get value list first value from vector if size >0
        String[] valueList = options.split(";");
        for (String aValue : valueList) {
            // test if aValue has a | as an delimiter
            String sValue = aValue;
            String sName = sValue;
            if (sValue.contains("|")) {
                sValue = sValue.substring(0, sValue.indexOf("|"));
                sName = sName.substring(sName.indexOf("|") + 1);
            }
            selection.add(new SelectItem(sName.trim(), sValue.trim()));
        }
        return selection;
    }
}
