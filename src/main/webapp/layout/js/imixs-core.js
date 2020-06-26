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
 * This library provides the core module functionality
 * 
 * Version 3.0.1
 */

var IMIXS = IMIXS || {};

// namespace function (by Stoyan Stefanov - JavaScript Patterns)
IMIXS.namespace = function(ns_string) {
	var parts = ns_string.split('.'), parent = IMIXS, i;

	// strip redundant leading global
	if (parts[0] === "IMIXS") {
		parts = parts.slice(1);
	}

	for (i = 0; i < parts.length; i += 1) {
		// create a property if it dosn't exist yet
		if (typeof parent[parts[i]] === "undefined") {
			parent[parts[i]] = {};
		}
		parent = parent[parts[i]];
	}
	return parent;

};

IMIXS.namespace("org.imixs.core");

IMIXS.org.imixs.core = (function() {

	// private properties
	var _not_used,

	/**
	 * Helper method to test for HTML 5 localStorage...
	 * 
	 * @returns {Boolean}
	 */
	hasLocalStorage = function() {
		try {
			return 'localStorage' in window && window['localStorage'] !== null;
		} catch (e) {
			return false;
		}
	},
	
	// ImixsDoc represents a document class with a dynamic list of items.
	// This class is used to display the search result
	ImixsDocument = function(items) {
		  if (items) {
			  var _items = items.slice(0);
			  var self=this;
			  $.each(_items, function (j, _item) {
				var _name=_item.name;
				var _val=_item.values;
      			self[_name]=_val;
      		  });
		    }
		 
			// returns the first text value of an item
			this.getItem = function(fieldName) {				
				var item=this[fieldName];
				if (item && item[0]) {
					return item[0].text;
				}
				return '';
			}
			
			// returns a list of all item names
			this.getItemNames = function() {
				var itemNameList = new Array();
				var self=this;
				for(var k in self) {
					if (k && k!='') {
						var item=self[k];
						if ($.isArray(item)) {
						   itemNameList.push(k);
						}
					}
				}				
				return itemNameList;
			}
			
			
			/**
			 * This method is used to return the value array of a name item inside
			 * the current Document. If no item with this name exists the
			 * method adds a new element with this name.
			 */
			this.getItemList = function(fieldName) {
				var valueList = new Array();
				var items=this[fieldName];
				if (items && items[0]) {
					$.each(items, function(index, _item) {
						valueList.push( _item['text']);
					});
				}
				return valueList;
			}
			
			
	      /**
	 	   * Adds a new item into the collection
		   */
	      this.setItem = function(fieldName, value, xsiType) {
			// test if item still exists?
			var _values = this[fieldName];

			if (_values) {
				 this.this[fieldName][0]['text']=value;
			} else { 
				// create item...
				if (!xsiType)
					xsiType = "xs:string";
				var valueObj = {
						"xsi:type" : xsiType,
						"text" : value
                         };
				this[fieldName]=new Array();
				this[fieldName].push(valueObj);
			}
		  }
		},
		
		
		// helper method to sort document collections by an item name
		// usage: 
		//         mycollection.sort(compareDocuments('txtname'));
		compareDocuments = function (itemName, order = 'asc') {
			  return function innerSort(a, b) {
				  
				const varA = a.getItem(itemName).toUpperCase();
				const varB = b.getItem(itemName).toUpperCase();
				  
			    let comparison = 0;
			    if (varA > varB) {
			      comparison = 1;
			    } else if (varA < varB) {
			      comparison = -1;
			    }
			    return (
			      (order === 'desc') ? (comparison * -1) : comparison
			    );
			  };
			};


	// public API
	return {
		hasLocalStorage : hasLocalStorage,
		ImixsDocument : ImixsDocument,
		compareDocuments : compareDocuments
	};

}());
