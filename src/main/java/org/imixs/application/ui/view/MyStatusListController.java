package org.imixs.application.ui.view;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.imixs.workflow.faces.data.ViewController;
import org.imixs.workflow.faces.util.LoginController;

@Named
@ViewScoped
public class MyStatusListController extends ViewController {

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
        this.setQuery("(type:\"workitem\" AND $creator:\"" + loginController.getRemoteUser() + "\")");
        this.setSortBy("$modified");
        this.setSortReverse(true);
        this.setLoadStubs(true);

    }

}
