var XMLDOM_ELEMENT_NODE = 1;
var XMLDOM_ATTRIBUTE_NODE = 2;
var XMLDOM_TEXT_NODE = 3;
var XMLDOM_CDATA_SECTION_NODE = 4; 
var XMLDOM_ENTITY_REFERENCE_NODE = 5; 
var XMLDOM_ENTITY_NODE = 6;
var XMLDOM_PROCESSING_INSTRUCTION_NODE = 7; 
var XMLDOM_COMMENT_NODE = 8;
var XMLDOM_DOCUMENT_NODE = 9;
var XMLDOM_DOCUMENT_TYPE_NODE = 10; 
var XMLDOM_DOCUMENT_FRAGMENT_NODE = 11;
var XMLDOM_NOTATION_NODE = 12;


function xmldom__newText(name,value,doc) {
	var newElem = doc.createElement(name);
	var newText = doc.createTextNode(value);
	newElem.appendChild(newText);
	return newElem;
}

function xmldom__newCDATA(name,value,doc) {
	var newElem = doc.createElement(name);
	var newCDATA = doc.createCDATASection(value);
	newElem.appendChild(newCDATA);
	return newElem;
}

function xmldom__elementExists(nodeList, name, value, ignoreCase) {
	for (var i=0; i<nodeList.length; i++) {
		var node = nodeList[i];
		var test = xmldom__getValue(node,name);
		if (ignoreCase && test.toLowerCase() == value.toLowerCase()) {
			return true;	
		} else if (!ignoreCase && test == value) {
			return true;	
		}
	}
	return false;
}

function xmldom__getValue(node) {
	if (node.hasChildNodes()) {
    	var data = node.childNodes[0];
    	if (data.nodeType == XMLDOM_TEXT_NODE || 
    	    data.nodeType == XMLDOM_CDATA_SECTION_NODE) {
    		return data.data;
    	}
	}
	return "";
}

function xmldom__getChildValue(node, childName) {
	if (node.hasChildNodes()) {
		for (var i=0; i<node.childNodes.length; i++) {
			var child = node.childNodes[i];
			if (child.nodeName == childName) {
				return xmldom__getValue(child);
			}
		}
	}
	return "";
}

function xmldom__setCDATA(node, value, doc) {
	if (node.hasChildNodes()) {
		node.childNodes[0].data = value;
	} else {
		var newCDATA = doc.createCDATASection(value);
		node.appendChild(newCDATA);
	}
}

function xmldom__setText(node, value, doc) {
	if (node.hasChildNodes()) {
		node.childNodes[0].data = value;
	} else {
		var newText = doc.createTextNode(value);
		node.appendChild(newText);
	}
}