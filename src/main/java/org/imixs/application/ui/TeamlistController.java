package org.imixs.application.ui;

import java.io.Serializable;
import java.util.logging.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import org.imixs.workflow.faces.data.ViewController;

/**
 * Select the list of teams sorted by name
 * 
 * @author rsoika
 */
@Named
@ViewScoped
public class TeamlistController extends ViewController implements Serializable {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(TeamlistController.class.getName());

	@Override
	@PostConstruct
	public void init() {
		super.init();
		this.setQuery("(type:\"team\")");
		this.setSortBy("txtname");
		this.setSortReverse(false);
		this.setLoadStubs(false);
	}

}
