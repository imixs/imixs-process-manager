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

package org.imixs.application.plugin;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;

import org.imixs.workflow.ItemCollection;
import org.imixs.workflow.engine.DocumentService;
import org.imixs.workflow.engine.plugins.AbstractPlugin;
import org.imixs.workflow.exceptions.PluginException;

/**
 * The TeamPlugin copies the team members from the selected team into the item
 * 'teamMembers'. This item is mapped into the workflow model 'ticket.bpmn'.
 * When a ticket is submitted, the teamMembers become owner of the process
 * instance.
 * 
 * @See team.xhtml, sub_main.xhtm.
 * @author Ralph Soika
 * @version 1.0
 *
 */

public class TeamPlugin extends AbstractPlugin {

	// inject services...
	@EJB
	DocumentService documentService;

	private static Logger logger = Logger.getLogger(TeamPlugin.class.getName());

	/**
	 * The run method looks up the team entity and copies the member list into the
	 * item 'teamMembers'
	 */
	@SuppressWarnings("unchecked")
	public ItemCollection run(ItemCollection documentContext, ItemCollection event) throws PluginException {

		String teamRef = documentContext.getItemValueString("team");
		logger.info("...lookup team reference: " + teamRef);

		// now we load the team configuration
		ItemCollection team = documentService.load(teamRef);
		if (team != null) {
			// copy team members into the item 'namTeam'
			List<String> teamMembers = team.getItemValue("members");
			documentContext.replaceItemValue("teamMembers", teamMembers);
			// write new team list into the server log....
			for (String member : teamMembers) {
				logger.info("...... add team member " + member);
			}
		}
		return documentContext;
	}

}
