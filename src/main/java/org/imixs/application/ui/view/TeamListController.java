package org.imixs.application.ui.view;

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.imixs.application.config.PropertyController;
import org.imixs.marty.team.TeamController;
import org.imixs.workflow.ItemCollection;
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

	@Inject
	TeamController teamController;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		this.setQuery("(type:space*)");
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

	/**
	 * Returns the teams to part of the selection.
	 * 
	 * Depending on the options the list can be either the complete list of spaces
	 * or a subset.
	 * 
	 * Possible Options are:
	 * 
	 * 
	 * 
	 * @param workitem
	 * @param options
	 * @return
	 */
	public List<ItemCollection> getTeams(ItemCollection workitem, String options) {
		if (options == null) {
			options = "";
		}
		List<ItemCollection> result = null;
		if (options.toLowerCase().contains("byprocess=true")) {
			result = teamController.getSpacesByProcessId(workitem.getItemValueString("process.ref"));
		} else {
			result = teamController.getSpaces();
		}

		// apply regex filter?
		if (options.contains("regex=")) {
			String _regex = extractRegexValue(options);
			Pattern pattern = Pattern.compile(_regex);
			return result.stream()
					.filter(space -> pattern.matcher(space.getItemValueString("name")).matches())
					.collect(Collectors.toList());
		}
		// default result
		return result;

	}

	/**
	 * This method updates the space.ref item with the first space ID the current
	 * user is member of.
	 * The method is used as a default selector by form part 'spaceref.xhtml'
	 * 
	 * The method updates the items space.ref and space.name and returns the
	 * space.ref
	 * 
	 * Possible Options are:
	 * 
	 * <pre>
	 * default-selection=member  | The first section, the current user is member of will be pre-selected.     
	 * default-selection=team    | The first section, the current user is team member of will be pre-selected. 
	 * default-selection=manager | The first section, the current user is manager of will be pre-selected.  
	 * default-selection=assist  | The first section, the current user is assist of will be pre-selected.
	 * </pre>
	 */
	public String setDefaultTeam(ItemCollection workitem, String options) {
		if (options == null) {
			options = "";
		}

		// return if space.ref is already set!
		if (!workitem.getItemValueString("space.ref").isEmpty()) {
			return ""; // no op!
		}

		ItemCollection defaultSpace = null;
		List<ItemCollection> _spaceList = getTeams(workitem, options);

		// find a matching space in the spaces list.
		if (_spaceList != null) {
			for (ItemCollection space : _spaceList) {
				if (options.toLowerCase().contains("default-selection=assist")
						&& space.getItemValueBoolean("isAssist")) {
					defaultSpace = space;
					break;
				}
				if (options.toLowerCase().contains("default-selection=team") && space.getItemValueBoolean("isTeam")) {
					defaultSpace = space;
					break;
				}
				if (options.toLowerCase().contains("default-selection=manager")
						&& space.getItemValueBoolean("isManager")) {
					defaultSpace = space;
					break;
				}

				if (options.toLowerCase().contains("default-selection=member")
						&& space.getItemValueBoolean("isMember")) {
					defaultSpace = space;
					break;
				}
			}
		}

		// do we have a match?
		if (defaultSpace != null) {
			workitem.setItemValue("space.ref", defaultSpace.getUniqueID());
			workitem.setItemValue("space.parent.name", defaultSpace.getItemValueString("space.parent.name"));
			workitem.setItemValue("space.name", defaultSpace.getItemValueString("space.name"));
			return defaultSpace.getUniqueID();
		}

		// no match !
		return "";
	}

	private static String extractRegexValue(String input) {
		Pattern pattern = Pattern.compile("regex=([^;]+)");
		Matcher matcher = pattern.matcher(input);
		return matcher.find() ? matcher.group(1) : null;
	}
}
