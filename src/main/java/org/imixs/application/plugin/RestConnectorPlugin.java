/*******************************************************************************
 *  Imixs Workflow 
 *  Copyright (C) 2001, 2024 Imixs Software Solutions GmbH,  
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

import java.io.StringReader;
import java.util.List;
import java.util.logging.Logger;

import org.imixs.workflow.ItemCollection;
import org.imixs.workflow.engine.WorkflowService;
import org.imixs.workflow.engine.plugins.AbstractPlugin;
import org.imixs.workflow.exceptions.PluginException;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

/**
 * This Plugin demonstrates how to implement a JAX-RS client to call a URL
 * and extract an element from the JSON response.
 * <p>
 * The Plugin can be configured in the BPMN Event
 * 
 * <p>
 * Example api-connector Prompt:
 * 
 * <pre>
 * {@code
        <api-connector name="GET">
            <endpoint>https://catfact.ninja/fact/</endpoint>
            <item>length</item>
        </api-connector>
 * }
 * </pre>
 * 
 * @author Ralph Soika
 * @version 1.0
 *
 */

public class RestConnectorPlugin extends AbstractPlugin {

	private static final Logger logger = Logger.getLogger(RestConnectorPlugin.class.getName());

	@Inject
	private WorkflowService workflowService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemCollection run(ItemCollection workitem, ItemCollection event)
			throws PluginException {

		logger.info("...connecting rest api...");

		// read configuration from event
		List<ItemCollection> apiDefinitions = workflowService.evalWorkflowResultXML(event, "api-connector",
				"GET", workitem, false);

		if (apiDefinitions != null) {
			for (ItemCollection apiDefinition : apiDefinitions) {
				String endpoint = apiDefinition.getItemValueString("endpoint");
				String item = apiDefinition.getItemValueString("item");

				// Create a JAX-RS client
				Client client = ClientBuilder.newClient();
				WebTarget target = client.target(endpoint);
				Response response = target.request().get();
				if (response.getStatus() == 200) {
					// Parse the JSON response
					String jsonResponse = response.readEntity(String.class);
					JsonReader jsonReader = Json.createReader(new StringReader(jsonResponse));
					JsonObject jsonObject = jsonReader.readObject();
					String itemValue = jsonObject.getString(item);
					// Update workitem
					workitem.setItemValue(item, itemValue);
					jsonReader.close();
				} else {
					throw new PluginException(RestConnectorPlugin.class.getSimpleName(), "API-ERROR",
							"Failed to fetch data: " + response.getStatus());
				}
				response.close();
				client.close();
			}
		}

		return workitem;
	}

}
