package org.imixs.application.ui.view;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.imixs.workflow.faces.data.ViewController;
import org.imixs.workflow.faces.util.LoginController;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ProfileViewController extends ViewController {

	private static final long serialVersionUID = 1L;

	@Inject
	LoginController loginController;

	@Inject
	@ConfigProperty(name = "admin.view.entries.max", defaultValue = "999")
	transient int adminViewPageSize;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		this.setQuery("(type:profile*) ");
		this.setSortBy("txtname");
		this.setSortReverse(false);
		this.setPageSize(adminViewPageSize);
		this.setLoadStubs(false);
	}

}
