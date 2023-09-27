package org.imixs.application.ui.view;

import java.io.Serializable;
import java.util.logging.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import org.imixs.workflow.faces.data.ViewController;

/**
 * Select the current list of tasks sorted by modification timestamp
 * 
 * @author rsoika
 */
@Named
@ViewScoped
public class OpenTaskListController extends ViewController implements Serializable {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(OpenTaskListController.class.getName());

	@Override
	@PostConstruct
	public void init() {
		super.init();
		this.setQuery("(type:\"workitem\")");
        this.setSortBy("$modified");
        this.setSortReverse(true);
        this.setLoadStubs(true);
	}

}
