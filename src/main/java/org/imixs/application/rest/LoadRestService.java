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

package org.imixs.application.rest;

import java.io.InputStream;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.imixs.workflow.ItemCollection;
import org.imixs.workflow.WorkflowKernel;
import org.imixs.workflow.engine.WorkflowService;
import org.imixs.workflow.exceptions.AccessDeniedException;
import org.imixs.workflow.exceptions.ModelException;
import org.imixs.workflow.exceptions.PluginException;
import org.imixs.workflow.exceptions.ProcessingErrorException;

/**
 * The WorkflowService Handler supports methods to process different kind of
 * request URIs
 * 
 * Example URL:
 * 
 * http://localhost:8080/workflow/test?modelversion=1.0.1&processid=1000&activityid=10&maxcount=2
 * 
 * 
 * 
 * @author rsoika
 * 
 */
@Path("/")
@Produces({ "text/html", "application/xml", "application/json" })
@Stateless
public class LoadRestService {

	@javax.ws.rs.core.Context
	private HttpServletRequest servletRequest;

	private static Logger logger = Logger.getLogger(LoadRestService.class.getName());

	@EJB
	private WorkflowService workflowService;

	/**
	 * GET Method to start an internal load test
	 * 
	 * @param requestBodyStream
	 * @return
	 */
	@GET
	@Path("/load")
	public String getLoadTest(InputStream requestBodyStream, @QueryParam("modelversion") String modelversion,
			@QueryParam("processid") int processid, @QueryParam("activityid") int activityid,
			@QueryParam("maxcount") int maxcount) {
		int count = 0;
		long time = System.currentTimeMillis();
		while (count < maxcount) {

			// create example workitem
			ItemCollection workitem = new ItemCollection();
			
			workitem.model( modelversion).task( processid).event( activityid);
			workitem.replaceItemValue(WorkflowKernel.TYPE, "workitem");
			workitem.replaceItemValue("subject", "Loadtest-" + System.currentTimeMillis());
			try {
				workflowService.processWorkItem(workitem);
			} catch (AccessDeniedException | ProcessingErrorException | PluginException | ModelException e) {
				e.printStackTrace();

				return "load test failed: " + e.getMessage();
			}

			count++;

		}
		String message = "load test finished, " + count + " workitems processed in "
				+ (System.currentTimeMillis() - time) + "ms";
		logger.info(message);
		return message;
	}

}
