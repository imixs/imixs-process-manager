"use strict";

// Imixs-Wopi Adapter Script
//
// Provides methods for integration

IMIXS.namespace("org.imixs.workflow.wopi");



/**
 * Init Method for the wopi integration
 * 
 * Register a message listener
 */
$(document).ready(function() {
	//  Install the wopi message listener.
	// receive messages form libreoffice online
	window.addEventListener("message", receiveMessage, false);
});


// This function is invoked when the iframe posts a message back.
function receiveMessage(event) {
	console.log('==== framed.doc.html receiveMessage: ' + event.data);
	var msg = JSON.parse(event.data);
	if (!msg) {
		return;
	}
	if (msg.MessageId == 'App_LoadingStatus') {
		if (msg.Values) {
			if (msg.Values.Status == 'Document_Loaded') {
				console.log('==== Document loaded');
				window.frames[0].postMessage(JSON.stringify({ 'MessageId': 'Host_PostmessageReady' }), '*');
			}
		}
	} else if (msg.MessageId == 'UI_Save') {
		if (msg.Values) {
			console.log('==== UI_Save');
			imixsWopi.closeViewer();
			if (wopiControllerUpdateFile) {
				wopiControllerUpdateFile();
			}
		}
	} else if (msg.MessageId == 'Doc_ModifiedStatus') {
		if (msg.Values) {
			if (msg.Values.Modified == true) {
				console.log('==== Modified');
			}
			else {
				console.log('==== unknown');
				
			}
		}
	} else if (msg.MessageId == 'Action_Save_Resp') {
		if (msg.Values) {
			if (msg.Values.success == true) {
				console.log('==== Saved');
			} else {
				console.log('==== Error during save');
			}
		}
	}
}


// define core module
IMIXS.org.imixs.workflow.wopi = (function() {
	if (!IMIXS.org.imixs.core) {
		console.error("ERROR - missing dependency: imixs-core.js");
	}

	var imixs = IMIXS.org.imixs.core,

		formID = "",
		viewerID = "",

		// switch to iframe mode and load editor
		openViewer = function(ref) {

			var wopiuri = ref;
			var imixsform = $('#' + imixsWopi.formID);
			var libreoffice = $('#' + imixsWopi.viewerID);
			// show iframe...
			imixsform.hide();
			libreoffice.show();

			// reset iframe
			buildViewer(imixsWopi.viewerID);


			// hack.....
			wopiuri = wopiuri.replace("libreoffice-app", "localhost");

			var form = $('#libreoffice_online-viewer').contents().find('#libreoffice-form');
			$(form).get(0).setAttribute('action', wopiuri);

			//alert("Submit URI="+wopiuri);
			form.submit();

		},


		/*
		 * This helper method builds a ifram with a form at a given 
		 * element. This is used to show the LibreOffice editor later
		 */
		buildViewer = function(elementid) {

			var iframeElement = $("#" + elementid);

			$(iframeElement).empty();

			// build iframe....
			var content = '<iframe id="libreoffice_online-viewer" src="" width="100%" height="1000"></iframe>';

			$(iframeElement).append(content);


			var iframe = document.getElementById('libreoffice_online-viewer');
			iframe = iframe.contentWindow || (iframe.contentDocument.document || iframe.contentDocument);

			iframe.document.open();
			iframe.document.write('<html><body><form action="" enctype="multipart/form-data" method="post" id="libreoffice-form"><input name="dummy" value="" type="hidden" id="dummy"/> <input type="submit" value="Load..." /></form></body></html>');
			iframe.document.close();

		},

		// close the libreoffice viewer and show the form part again
		closeViewer = function() {
			var imixsform = $('#' + imixsWopi.formID);
			var libreoffice = $('#' + imixsWopi.viewerID);
			$(libreoffice).empty();
			// show iframe...
			libreoffice.hide();
			imixsform.show();


		};


	// public API
	return {
		openViewer: openViewer,
		closeViewer: closeViewer,
		formID: formID,
		viewerID: viewerID
	};

}());

// Define public namespace
var imixsWopi = IMIXS.org.imixs.workflow.wopi;

