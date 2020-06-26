package org.imixs.application.ui.view;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.imixs.workflow.faces.data.ViewController;
import org.imixs.workflow.faces.util.LoginController;


@Named
@ViewScoped
public class MyTaskListController extends ViewController {

	private static final long serialVersionUID = 1L;

	@Inject
	LoginController loginController;
	

	/**
	 * Initialize default behavior configured by the BASIC configuration entity.
	 */
	@Override
	@PostConstruct
	public void init() {
		super.init();
		this.setQuery("(type:\"workitem\" AND $owner:\"" + loginController.getRemoteUser() + "\")");
	      this.setSortBy("$modified");
	        this.setSortReverse(true);
	        this.setLoadStubs(true);

	}


}
