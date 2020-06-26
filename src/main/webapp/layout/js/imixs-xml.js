/*******************************************************************************
 * imixs-xml.js Copyright (C) 2015, http://www.imixs.org
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You can receive a copy of the GNU General Public License at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Project:  http://www.imixs.org
 * 
 * Contributors: Ralph Soika - Software Developer
 ******************************************************************************/

/**
 * This library provides methods to convert a XML Entity Stream provided by the
 * Imixs-Workflow Rest API into a JavaScript model object. It implements the
 * representation of an ItemCollection similar to the
 * org.imixs.workflow.ItemCollection class
 * 
 * Version 3.0.1
 */

IMIXS.namespace("org.imixs.xml");
IMIXS.org.imixs.xml = (function() {

	if (!IMIXS.org.imixs.core) {
		console.error("ERROR - missing dependency: imixs-core.js");
	}
	var imixs = IMIXS.org.imixs.core;
	// private properties
	var _not_used,

	/**
	 * converts a Imixs XML result set into an array of ImixsDocuments classes
	 * This method guarantees that an array of documents is returned even if the result
	 * colletion size is 0 or 1
	 * 
	 * The method supports the old and also new xml/data structure 
	 */
	xml2collection = function(xml) {
		if (!xml) {
			return {};
		}
		
		var docData;
		var json = xml2json(xml)
		
		// test if we have the lates xml/data format
		if (json.data) {
			if (json.data.document && !$.isArray(json.data.document))
				json.data.document = jQuery.makeArray(json.data.document);
			
			if (! json.data.document) {
				console.debug("no result");
				json.data.document = [];
			}
			
			docData= json.data.document;	
		} else {
			// test if we have the deprecated xml format (imixs-workflow < 4.0)
			if (json.collection.document) {
				if (!$.isArray(json.collection.document))
					json.collection.document = jQuery.makeArray(json.collection.document);
				docData=json.collection.document;			
			} else {
				// try to convert deprecated entity structure...
				if (!$.isArray(json.collection.entity))
					json.collection.entity = jQuery.makeArray(json.collection.entity);
				docData=json.collection.entity;
			}
		}		
		
		
		// convert docData into an array of ImixsDocuments
		var _result=[];
		$.each(docData, function (index, doc) {
    		var _doc=new imixs.ImixsDocument(doc.item);
        	_result.push(_doc);
		});
		return _result;
	}
	
	
	

	/**
	 * Converts a Imixs XML result of an document into an ImixsDocument array.
	 * The method supports the old and also new xml/data structure 
	 */
	xml2document = function(xml) {
		if (!xml) {
			return {};
		}
			
		var json = xml2json(xml)
		
		// test if json contains data root element
		if (json.data && json.data.document) {
			// take the first document from data....
			if (!$.isArray(json.data.document.item))
				json.data.document.item = jQuery.makeArray(json.data.document.item);
			return new imixs.ImixsDocument(json.data.document.item);
			
		} else {
			
			// test if we have the old xml format (imixs-workflow < 4.0)
			if (json.document) {
				if (!$.isArray(json.document.item))
					json.document.item = jQuery.makeArray(json.document.item);
				return new imixs.ImixsDocument(json.document.item);
			} else {
				// try to convert old entity structure...
				if (!$.isArray(json.entity.item))
					json.entity.item = jQuery.makeArray(json.entity.item);
				return new imixs.ImixsDocument(json.entity.item);
			}
		}
		
	}

		/**
		 * converts a XML result set form the Imixs Rest Service API into a JSON
		 * object. Based on the idears from David Walsh
		 * (http://davidwalsh.name/convert-xml-json)
		 * 
		 * 
		 * </code>
		 */
			xml2json = function(xml) {
				// Create the return object
				var obj = {};

				if (!xml) {
					return obj;
				}
				
				if (xml.nodeType == 1) { // element
					// do attributes
					if (xml.attributes.length > 0) {
						for (var j = 0; j < xml.attributes.length; j++) {
							var attribute = xml.attributes.item(j);
							obj[attribute.nodeName] = attribute.nodeValue;
						}
					}
				} else if (xml.nodeType == 3) { // text
					obj = xml.nodeValue;
				}

				// process item? in this case we construct the properties name,
				// value and
				// type...
				// example
				//	<item name="$uniqueidref">
				//       <value xsi:type="xs:string">a4cd63d6-66a9-4e66-964c-1fb6ba124bda</value>
				//       <value xsi:type="xs:string">f37237ca-ffcf-4706-8ca0-f688a1b9e15e</value>
				//       <value xsi:type="xs:string">49f41b8b-d3f2-4d9c-aa9d-12fc8fa04225</value>
				//  </item>
				if (xml.nodeName == "item") {
					if (xml.hasChildNodes()) {
						
						// find name....
						if (xml.attributes.length > 0) {
							for (var j = 0; j < xml.attributes.length; j++) {
								var attribute = xml.attributes.item(j);
								if (attribute.nodeName=="name") {
									obj.name = attribute.nodeValue;
									break;
								}								
							}
						}
						
						
						
						// parse values....
						for (var i = 0; i < xml.childNodes.length; i++) {
							var item = xml.childNodes.item(i);
							if (item.nodeName == 'value') {
								// value is an array
								if (typeof (obj['values']) == "undefined") {
									obj.values = new Array();
								}

								var valobj = {};
								
								// test for embedded xmlItemArray 
								if (item.attributes.item(0)) {
									var embeddedItem=item.attributes.item(0);
									if (embeddedItem.nodeValue=='xmlItemArray') {
										obj.values.push(xml2json(item));
										continue;
									}
								}
							
								// standard value structure..
								valobj['text'] = item.textContent;
								if (item.attributes.length > 0) {
									for (var j = 0; j < item.attributes.length; j++) {
										var attribute = item.attributes.item(j);
										valobj[attribute.nodeName] = attribute.nodeValue;
									}
								}
								obj.values.push(valobj);
							}
						}
					}
				} else {
					// do children
					if (xml.hasChildNodes()) {
						for (var i = 0; i < xml.childNodes.length; i++) {
							var item = xml.childNodes.item(i);
							var nodeName = item.nodeName;
	
							if (typeof (obj[nodeName]) == "undefined") {
								obj[nodeName] = xml2json(item);
							} else {
								if (typeof (obj[nodeName].push) == "undefined") {
									var old = obj[nodeName];
									obj[nodeName] = [];
									obj[nodeName].push(old);
								}
								obj[nodeName].push(xml2json(item));
							}
						}
					}
				}
				return obj;
			},

			/**
			 * converts a itemcollection into a imixs XML string. The result can
			 * be used to post the string to a Imixs Rest Service API
			 * 
			 * <code>
			 *   <entity xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
			 *      <item><name>namlasteditor</name><value xsi:type="xs:string">ralph.soika@imixs.com</value>
			 *      </item><item><name>$isauthor</name><value xsi:type="xs:boolean">true</value></item>
			 *      ....
			 *   </entity>
			 */
			json2xml = function(workitem) {
 				 var result = '<?xml version="1.0" encoding="UTF-8"?>\n<document xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">';
				
				 for(var itemname in workitem) {
				    // test if item with values...
					if (workitem[itemname].values) {
					    result = result + '<item name="' + itemname + '">';
						$.each(workitem[itemname], function(index, avalue) {
							// if the value is undefined we skip this entry
							if (avalue["text"]) {
								result = result + '<value xsi:type="'
										+ avalue["xsi:type"] + '">';
								/*  
								 * in case of xsi:type==xs:string we embed the
								 * value into a CDATA element
								 */
								if (avalue["xsi:type"]==="xs:string") {
									result = result + "<![CDATA[" + avalue["text"]
											+ "]]>";
								} else {
									result = result + avalue["text"];
								}
								result = result + '</value>';
							}
						});
						result = result + '</item>';
					}
				 }
				result = result + '</document>';
				return result;
			};
			
			
			/**
			 * converts a itemcollection into the deprecated imixs XML string with 'entity' tags. The result can
			 * be used to post the string to a Imixs Rest Service API
			 * 
			 * <code>
			 *   <entity xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
			 *      <item><name>namlasteditor</name><value xsi:type="xs:string">ralph.soika@imixs.com</value>
			 *      </item><item><name>$isauthor</name><value xsi:type="xs:boolean">true</value></item>
			 *      ....
			 *   </entity>
			 */
			json2xmlEntity = function(workitem) {
				var result = '<?xml version="1.0" encoding="UTF-8"?>\n<entity xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">';

				if (workitem && workitem.item) {
					$.each(workitem.item, function(index, aitem) {
						result = result + '<item><name>' + aitem.name
								+ '</name>';

						if (aitem.values) {
							$.each(aitem.values, function(index, avalue) {
								// if the value is undefined we skip this entry
								if (avalue["text"]) {
									result = result + '<value xsi:type="'
											+ avalue["xsi:type"] + '">';
									/*  
									 * in case of xsi:type==xs:string we embed the
									 * value into a CDATA element
									 */
									if (avalue["xsi:type"]==="xs:string") {
										result = result + "<![CDATA[" + avalue["text"]
												+ "]]>";
									} else {
										result = result + avalue["text"];
									}
									result = result + '</value>';
								}
							});
						}

						result = result + '</item>';
					});

				}

				result = result + '</entity>';
				return result;
			};

	// public API
	return {
		xml2json : xml2json,
		json2xml : json2xml,
		json2xmlEntity : json2xmlEntity,
		xml2collection : xml2collection,
		xml2document : xml2document
	};

}());
