"use strict";

// Imixs-Wopi Adapter Script
//
// Provides methods for integration

IMIXS.namespace("org.imixs.workflow.wopi");


var documentPreview;			// active document preview element
var documentPreviewIframe;  	// active iFrame
var documentPreviewURL;			// URL for current displayed document
var isWorkitemLoading=true; 	// indicates if the workitem is still loading
var chornicleSize=1;			// default cronicle size (33%)
var chroniclePreview=true; 		// indicates if documetns should be shown in the cornicle column


/**
 * Init Method for the workitem page
 * 
 * 
 * 
 */
$(document).ready(function() {
	
	
});




// define core module
IMIXS.org.imixs.workflow.wopi = (function() {
	if (!IMIXS.org.imixs.core) {
		console.error("ERROR - missing dependency: imixs-core.js");
	}

	var imixs = IMIXS.org.imixs.core,
	
	formID ="", 
	viewerID ="", 
	
	// switch to iframe mode and load editor
	loadLibreOffice =function(ref) {
		
		var wopiuri=ref;
		var imixsform=$('#'+imixsWopi.formID);
		var libreoffice=$('#'+imixsWopi.viewerID);
		// show iframe...
		imixsform.hide();
		libreoffice.show();
		
		// hack.....
		wopiuri=wopiuri.replace("libreoffice-app","localhost");
		
		// hack 2
		//	wopiuri=wopiuri+"&NotWOPIButIframe=true"
		
		var form=$('#libreoffice_online-viewer').contents().find('#libreoffice-form');
		$(form).get(0).setAttribute('action', wopiuri); 
		
		//alert("Submit URI="+wopiuri);
		form.submit();
	      
	},
		
	
	dummy = function(cname, cvalue, exdays=999) {
	 
	};
	
	


	// public API
	return {
		loadLibreOffice : loadLibreOffice,
		dummy : dummy,
		formID : formID,
		viewerID : viewerID
	};

}());	
	
// Define public namespace
var imixsWopi = IMIXS.org.imixs.workflow.wopi;	

