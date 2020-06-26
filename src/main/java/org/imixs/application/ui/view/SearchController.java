package org.imixs.application.ui;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

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
	
	
	private static Logger logger = Logger.getLogger(SearchController.class.getName());

	@Override
	@PostConstruct
	public void init() {
		super.init();
		this.setQuery("(type:\"workitem\")");
		this.setSortBy("$modified");
		this.setSortReverse(true);
		this.setLoadStubs(false);
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	
	public void search() {
		this.setQuery("(type:\"workitem\" OR type:\"workitemarchive\") AND ("+input+"*)");
		
		logger.info("serach query=" + this.getQuery());
	}
	
}
