package org.imixs.application.ui.view;

import java.io.Serializable;
import java.util.logging.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.util.logging.Level;

import org.imixs.workflow.faces.data.ViewController;

/**
 * Simple example for a search controller searching the Imixs Fulltext Index
 * 
 * @author rsoika
 */
@Named
@SessionScoped
public class SearchController extends ViewController implements Serializable {
	private static final long serialVersionUID = 1L;

	private String input;
	
	
	private static final Logger logger = Logger.getLogger(SearchController.class.getName());

	@Override
	@PostConstruct
	public void init() {
		super.init();
		this.setQuery("(type:\"workitem\")");
        this.setSortBy("$modified");
        this.setSortReverse(true);
        this.setLoadStubs(true);

	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	
	public void search() {
		this.setQuery("(type:\"workitem\" OR type:\"workitemarchive\") AND ("+input+"*)");
		
		logger.log(Level.INFO, "serach query={0}", this.getQuery());
	}
	
}
