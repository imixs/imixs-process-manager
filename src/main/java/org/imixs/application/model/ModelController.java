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

package org.imixs.application.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.imixs.workflow.FileData;
import org.imixs.workflow.ItemCollection;
import org.imixs.workflow.bpmn.BPMNUtil;
import org.imixs.workflow.engine.ModelService;
import org.imixs.workflow.engine.WorkflowService;
import org.imixs.workflow.exceptions.AccessDeniedException;
import org.imixs.workflow.exceptions.ModelException;
import org.imixs.workflow.faces.data.WorkflowController;
import org.openbpmn.bpmn.BPMNModel;
import org.openbpmn.bpmn.exceptions.BPMNModelException;
import org.openbpmn.bpmn.util.BPMNModelFactory;
import org.xml.sax.SAXException;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * The ModelController provides informations about workflow models.
 * 
 * A ModelVersion is always expected in the format
 * 
 * 'DOMAIN-LANGUAGE-VERSIONNUMBER'
 * 
 * e.g. office-de-0.1, support-en-2.0, system-de-0.0.1
 * 
 * 
 * @author rsoika
 * 
 */
@Named
@RequestScoped
public class ModelController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	protected ModelUploadHandler modelUploadHandler;

	@Inject
	WorkflowController workflowController;

	@EJB
	protected ModelService modelService;

	@EJB
	protected WorkflowService workflowService;

	private static final Logger logger = Logger.getLogger(ModelController.class.getName());

	/**
	 * returns all groups for a version
	 * 
	 * @param version
	 * @return
	 */
	public Set<String> getGroups(String version) {
		try {
			BPMNModel model;
			model = modelService.getModelManager().getModel(version);
			return modelService.getModelManager().findAllGroups(model);
		} catch (ModelException e) {
			logger.log(Level.WARNING, "Unable to load groups:{0}", e.getMessage());
		}
		// return empty result
		return new LinkedHashSet<>();
	}

	/**
	 * Returns a sorted list of all WorkflowGroups from all models.
	 * 
	 * Workflow groups of the system model will be skipped.
	 * 
	 * A workflowGroup with a '~' in its name will be skipped. This indicates a
	 * child process.
	 * 
	 * The worflowGroup list is used to assign a workflow Group to a core process.
	 * 
	 * @return list of workflow groups
	 */
	public List<String> getWorkflowGroups() {

		// Set<String> set = new HashSet<>();
		// List<String> versions = modelService.getModelManager().getVersions();
		List<String> result = new ArrayList<>();

		for (BPMNModel model : modelService.getModelManager().getAllModels()) {
			String version = BPMNUtil.getVersion(model);
			// Skip system model..
			if (version.startsWith("system-")) {
				continue;
			}
			Set<String> groups = modelService.getModelManager().findAllGroups(model);
			for (String groupName : groups) {
				if (result.contains(groupName))
					continue;
				if (groupName.contains("~"))
					continue;
				result.add(groupName);
			}
		}

		Collections.sort(result);
		return result;

	}

	/**
	 * Finds the first matching model version for a group name
	 * 
	 * @param group
	 * @return
	 * @throws ModelException
	 */
	public String findVersionByGroup(String group) throws ModelException {
		return modelService.getModelManager().findVersionByGroup(group);
	}

	/**
	 * Loads the first start task in the corresponding workflow group
	 * 
	 * @param group
	 * @return
	 * @throws ModelException
	 */
	public ItemCollection findStartTaskByGroup(String version, String group) throws ModelException {
		ItemCollection result = null;
		BPMNModel model = modelService.getModelManager().getModel(version);
		List<ItemCollection> startTasks;
		try {
			startTasks = modelService.getModelManager().findStartTasks(model, group);
			if (startTasks.size() > 0) {
				result = startTasks.get(0);
			}
		} catch (BPMNModelException e) {
			throw new ModelException(ModelException.INVALID_MODEL,
					"Unable to create new workitem by group '" + group + "'", e);
		}
		if (result == null) {
			logger.warning("No Model found for Group '" + group + "'");
		}

		return result;

	}

	/**
	 * Creates a new process instance based on a given start task in the
	 * corresponding workflow model
	 * group
	 * 
	 * @param version   - model version
	 * @param startTask - initial task
	 * @return new process instance
	 * @throws ModelException
	 */
	public ItemCollection startWorkflowByTask(String version, ItemCollection startTask) throws ModelException {
		ItemCollection result = null;
		workflowController.create(version, startTask.getItemValueInteger("numprocessid"), null);
		if (result == null) {
			logger.warning("No Model found for model version '" + version + "'");
		}
		return result;

	}

	/**
	 * returns all model versions. Used by the Model View
	 * 
	 * @return
	 */
	public List<String> getVersions() {
		return modelService.getModelManager().getVersions();
	}

	/**
	 * Returns a Model Entity. Used by the Model View
	 * 
	 * @param version
	 * @return
	 */
	public ItemCollection getModelEntity(String version) {
		return modelService.loadModel(version);
	}

	/**
	 * This method adds all uploaded model files. The method tests the model type
	 * (.bmpm, .ixm). BPMN Model will be handled by the ImixsBPMNParser. A .ixm file
	 * will be imported using the default import mechanism.
	 * 
	 * @param event
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws ParseException
	 * @throws ModelException
	 * 
	 */
	public void doUploadModel(ActionEvent event)
			throws ModelException {
		List<FileData> fileList = modelUploadHandler.getModelUploads().getFileData();

		if (fileList == null) {
			return;
		}
		for (FileData file : fileList) {

			// test if bpmn model?
			if (file.getName().endsWith(".bpmn")) {
				InputStream inputStream = new ByteArrayInputStream(file.getContent());
				BPMNModel model;
				try {
					model = BPMNModelFactory.read(inputStream);

					modelService.saveModel(model);
					continue;
				} catch (BPMNModelException e) {
					throw new ModelException(ModelException.INVALID_MODEL,
							"Unable to read model file: " + file.getName(), e);
				}
			} else {
				// model type not supported!
				logger.log(Level.WARNING, "Invalid Model Type. Model {0} can't be imported!", file.getName());
			}
		}
		modelUploadHandler.reset();
	}

	/**
	 * This Method deletes the given model from the database and the internal model
	 * cache.
	 * 
	 * @param modelversion
	 * @throws AccessDeniedException
	 * @throws ModelException
	 */
	public void deleteModel(String modelversion) throws AccessDeniedException, ModelException {
		modelService.deleteModel(modelversion);
	}

}
