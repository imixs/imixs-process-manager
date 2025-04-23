package org.imixs.application.ui.view;

import java.util.logging.Logger;

import org.imixs.application.config.PropertyController;
import org.imixs.workflow.engine.WorkflowService;
import org.imixs.workflow.exceptions.ModelException;
import org.imixs.workflow.faces.data.ViewController;
import org.imixs.workflow.faces.data.WorkflowController;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Select the list of teams sorted by name
 * 
 * @author rsoika
 */
@Named
@ViewScoped
// @RequestScoped
public class TeamListController extends ViewController {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(TeamListController.class.getName());

	@Inject
	protected WorkflowController workflowController;

	@Inject
	WorkflowService workflowService;

	@Inject
	protected PropertyController propertyController;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		this.setQuery("(type:team*)");
		this.setSortBy("name");
		this.setSortReverse(false);
		this.setLoadStubs(false);
	}

	/**
	 * This method creates a new empty team assigned to the marty system model
	 * 
	 * @throws ModelException
	 */
	public void create() throws ModelException {
		workflowController.create(propertyController.getProperty("setup.system.model"), 100, null);
	}

}
