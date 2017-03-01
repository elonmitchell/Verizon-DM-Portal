var redirectURL         = "";
var errorSource = document;

function setCookie( name, theValue ) {
  var the_date = new Date("December 31, 2023");
  var the_cookie_date = the_date.toGMTString();

  // build and save the cookie
  //
  var the_cookie = name + "=" + escape(theValue);
  the_cookie = the_cookie + ";expires=" + the_cookie_date + "; path=/";
  document.cookie = the_cookie;
}

function readCookie(name)
{
  var nameEQ = name + "=";
  //alert( "cookie = " + document.cookie );
  var ca = document.cookie.split(';');
  for(var i=0;i < ca.length;i++)
  {
    var c = ca[i];
    while (c.charAt(0)==' ') c = c.substring(1,c.length);
    if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
  }
  return null;
}

function getRealObject(o) {
  var obj;
  if (typeof(o) == "string") {
    obj = document.getElementById(o);
  } else {
    obj = o;
  }
  return obj;
}

/**
 * loadXMLFromString
 * Crates an XML document based on a string of XML data
 */
function loadXMLFromString(xmlString){
	var xmlDoc;
	try {
		var parser = new DOMParser();
		xmlDoc = parser.parseFromString(xmlString,"text/xml");
	}
	catch(e){
		xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
		xmlDoc.loadXML(xmlString);
	}
	return xmlDoc;
}

function trimFieldValue(theInputField) {
  tempString = theInputField.value;
  tempString = strtrim(tempString);
  theInputField.value = tempString;
}

function strtrim( theString ) {
  //Match spaces at beginning and end of text and replace
  //with null strings
  return theString.replace(/^\s+/,'').replace(/\s+$/,'');
}

function setTableHeight( usedHeight, divName ) {
  handleTableResize();
  _tableDiv = document.getElementById( divName );
  if ( _tableDiv != null ) {
    _tableDiv.style.height = document.body.clientHeight - usedHeight;
  }
}

function setContentWidth( extraWidth, miniumnWidth) {
  contentDiv = document.getElementById( "mainContentArea" );
  helpWidth = 0;
  windowWidth = document.body.clientWidth;
  newContentWidth = windowWidth - ( helpWidth + extraWidth );
  if ( newContentWidth < miniumnWidth ) {
    newContentWidth = miniumnWidth;
  }
  contentDiv.style.width = newContentWidth;
  dataTable = null; //document.getElementById( "data_table" );
  if ( dataTable != null ) {
    dataTableWidth = dataTable.clientWidth + 22;
    if ( miniumnWidth < dataTableWidth ) {
      miniumnWidth = dataTable.clientWidth + 22;
    }
  }
  if ( miniumnWidth > newContentWidth ) {
    newContentWidth = miniumnWidth;
    contentDiv.style.width = newContentWidth;
  }

}

function setButtonText( _button, _text ) {
	var button = getRealObject(_button);
  var span = document.getElementById(button.id + "_span");
  if (span) {
    span.innerText = _text;
  }
}

function isButtonEnabled( oButton ) {
	var button = getRealObject(oButton);
	var bIsEnabled = true;
	var disabledAttr = $j(button).attr("disabled");
	if (disabledAttr == null) {
		bIsEnabled = $j(button).hasClass("hdm-button");
	} else {
		if (disabledAttr === "" || disabledAttr === "true" || disabledAttr === "disabled" || disabledAttr === true) {
			bIsEnabled = false;
		} else {
			bIsEnabled = true;
		}
	}
  return bIsEnabled;
}

function isButtonLocked( oButton ) {
	var button = getRealObject(oButton);
  return ($j(button).hasClass("hdm-button-locked"));
}

function disableButton(oButton) {
	var button = getRealObject(oButton);
  if (isButtonEnabled(button) && !isButtonLocked(button)) {
    var span = document.getElementById(button.id + "_span");
		var jqButton = $j(button);
    if (span) {
      jqButton.removeClass("button");
      jqButton.addClass("disabledButton");
    } else {
      jqButton.removeClass("hdm-button");
			jqButton.addClass("hdm-button-disabled");
		}
		jqButton.attr("disabled", "disabled");
  }
}

function disableButtons( buttonIdArray ) {
  var currentButton = null;
  for ( i = 0; i < buttonIdArray.length; i++ ) {
    currentButton = document.getElementById( buttonIdArray[i] );
    if ( currentButton != null )
      disableButton( currentButton );
  }
}

function enableButton(oButton) {
	var button = getRealObject(oButton);
  if (!isButtonEnabled(button) && !isButtonLocked(button)) {
    var span = document.getElementById(button.id + "_span");
		var jqButton = $j(button);
    if (span) {
      jqButton.removeClass("disabledButton");
      jqButton.addClass("button");
    } else {
			jqButton.removeClass("hdm-button-disabled");
			jqButton.addClass("hdm-button");
		}
		jqButton.removeAttr("disabled");
  }
}


function checkForRedirect() {
  if ( redirectURL != "" ) {
    window.location = redirectURL;
  }
}

function logout() {
  alert( "logout goes here.");
}

function openBrWindow(theURL,winName,features) {
	window.open(theURL,winName,features);
}

function launchHelp( helpURL ) {
	window.open( helpURL, "report_help", "height=625,width=900,toolbar=no,status=no,location=no,resize=yes" );
}

var numb = '0123456789-';
var floatNumb = '0123456789.-';
var lwr = 'abcdefghijklmnopqrstuvwxyz';
var upr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

function isValid(parm,val) {
  if (parm == "") return true;
  for (i=0; i<parm.length; i++) {
    if (val.indexOf(parm.charAt(i),0) == -1) return false;
  }
  return true;
}

function isFloat(parm) {
  validInput = isValid(parm,floatNumb);
  if ( validInput && ( parm.indexOf(".") != parm.lastIndexOf("."))) {
    validInput = false;
  }
  return validInput;
}

function isNum(parm) {
  validInput = isValid(parm,numb);
  if ( validInput && ( parm.indexOf("-") > 0 ) && ( parm.lastIndexOf("-") > 0 )) {
    validInput = false;
  }
  return validInput;
}

function isLower(parm) {return isValid(parm,lwr);}
function isUpper(parm) {return isValid(parm,upr);}
function isAlpha(parm) {return isValid(parm,lwr+upr);}
function isAlphanum(parm) {return isValid(parm,lwr+upr+numb);}

function validateUnsignedInt( theValue, errorDivId ) {
  validInput = isNum( theValue );
  if (theValue == "" )
    theValue = "0";
  if ( validInput ) {
    intValue = parseInt( theValue );
    validInput = (intValue >= 0);
  }
  else if ((theValue.charAt(0) == '$' && theValue.charAt(theValue.length -1) == '$') && theValue.length > 2)
    validInput = true;

  theDiv = document.getElementById( errorDivId );
  if ( validInput )
    theDiv.style.display = "none";
  else
    theDiv.style.display = "block";
}

function validateInteger( theValue, errorDivId ) {
  theDiv = document.getElementById( errorDivId );
  if ( ((theValue.charAt(0) == '$' && theValue.charAt(theValue.length -1) == '$')  && theValue.length > 2)
    || isNum( theValue ) )
    theDiv.style.display = "none";
  else
    theDiv.style.display = "block";
}

function trim(str) {
  return str.replace(/^\s*|\s*$/g,"");
}

function validUTCDateTime( theValue, errorDivId )
{
  var isValid = false;
  theDiv = document.getElementById( errorDivId );
  var utcDateTimeRegEx = new RegExp(/(19|20)\d\d\-(0[1-9]|1[012])\-(0[1-9]|[12][0-9]|3[01])T(0[1-9]|1[0-9]|2[0-4]):[0-5][0-9]:[0-5][0-9]Z/);
  if ((theValue.charAt(0) == '$' && theValue.charAt(theValue.length -1) == '$') && theValue.length > 2) {
    isValid = true;
  } else if ( theValue == "" || theValue.match(utcDateTimeRegEx)) {
    isValid = true;
  } else {
    isValid = false;
  }

  theDiv.style.display = (isValid) ? "none" : "block";
}

function validateRequiredFields(requiredFields) {
  var errorFields = new Array();
  var success = true;

  // loop accross the array of required fields and perform validation tests
  for (var i=0; i<requiredFields.length; i++) {
    var field = document.getElementById(requiredFields[i]);
    var fieldErrorDiv = document.getElementById("inputError_" + requiredFields[i]);
    if (field != null) {
      // check some of the various field types
      try {
        if (field.type == "text" || field.type == "textarea") {
          success = (field.value != "" &&  trim(field.value).length != 0);
        }
        else if (field.type == "select-one") {
          success = (field.options[field.selectedIndex].value != "");
        }
      }
      catch (e) {
        success = true;
      }

      // add the field object to the error array if it fails validation
      // this also gives us a means to loop through all of the invalid fields if needed
      if (!success) {
        errorFields.push(field);
      }
      success = true;
    }
  }
  globals_errorFields = errorFields;
  return(errorFields.length == 0);
}

var CLOCK_DAYS = new Array("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat");
var CLOCK_MONTHS = new Array("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");

function GetClock() {
  var d = new Date();
  var nday = d.getDay();
  var nmonth = d.getMonth();
  var ndate = d.getDate();
  var nyear = d.getYear();
  var nhour = d.getHours();
  var nmin = d.getMinutes();
  var nsec = d.getSeconds();

  if (nyear < 1000) nyear = nyear + 1900;

	var ap = "AM";
  if (nhour == 0) {
    ap = " AM";
    nhour = 12;
  } else if (nhour <= 11) {
    ap = " AM";
  } else if (nhour == 12) {
    ap = " PM";
  } else if (nhour >= 13) {
    ap = " PM";
    nhour -= 12;
  }

  if (nmin <= 9) {
    nmin = "0" + nmin;
  }
  if (nsec <= 9) {
    nsec = "0" + nsec;
  }
  var timeTodisplay = "" + CLOCK_DAYS[nday] + ", " + CLOCK_MONTHS[nmonth] + " " + ndate + ", " + nyear + " " + nhour + ":" + nmin + ":" + nsec + ap + "";
  $j("#clockbox").text(timeTodisplay);
  setTimeout("GetClock()", 1000);
}

function validateRequiredParameters(formName, requiredFieldsName) {

  var errorFields = new Array();
  var success = true;
	try
	{
		requiredFieldsElement = document.forms[formName].elements[requiredFieldsName];
		if (requiredFieldsElement)
		{
			requiredFieldsValue = document.forms[formName].elements[requiredFieldsName].value;

			requiredFields = requiredFieldsValue.split(",");

		  // loop accross the array of required fields and perform validation tests
		  for (var i=0; i < requiredFields.length; i++)
		  {
		    if (requiredFields[i].indexOf("firmwareCombo:") != -1)
		    {
		      success = validateFirmware();
		    }
		    else if (requiredFields[i].indexOf("parameterData") == -1)
		    {
			    var field = document.forms[formName].elements[requiredFields[i]];
			    var fieldErrorDiv = document.getElementById("inputError_" + requiredFields[i]);
			    if (field != null)
			    {
			      if (!field.disabled)
				    {
				      // check some of the various field types
				      try
				      {
				        if (field.type == "text" || field.type == "textarea") {
				          success = (field.value != "" &&  trim(field.value).length != 0);
				        }
				        else if (field.type == "select-one") {
									success = (field.options[field.selectedIndex].value != "");
				        }
				        else if (field.type == "hidden") { // this is for type "list"
				          success = (field.value != "" &&  trim(field.value).length != 0);
				        }
				      }
				      catch (e)
				      {
				        success = true;
				      }
				    }
			    }
		    }
		    else
		    {
		      var parameterData = document.getElementById(requiredFields[i]);
		      var argumentIndex = requiredFields[i].substring(requiredFields[i].indexOf(":") + 1);
		      argumentIndex = argumentIndex * 1;
		      if (!parameterData.disabled && getParameterListLength(argumentIndex) == 0)
		      {
		        success = false;
		      }
		    }
		    // add the field object to the error array if it fails validation
		    // this also gives us a means to loop through all of the invalid fields if needed
		    if (!success) {
		      errorFields.push(field);
		    }
		    success = true;
		  }
	  }
	}
	catch(e)
	{

	}
  globals_errorFields = errorFields;
  return(errorFields.length == 0);
}

function setErrorSource(myIFrameDocument)
{
  errorSource = myIFrameDocument;
}

function getErrorSource()
{
  return errorSource;
}

function handleErrors(myDocument) {

	var doc = document;
	if (myDocument)
		doc = myDocument;

	//alert("handleErrors");
	var _dialogFramesActionErrorsDiv = doc.getElementById("dialogFramesActionErrorsDiv");
	var _dialogFramesExceptionDiv = doc.getElementById("dialogFramesExceptionDiv");
	var _dialogFramesExceptionMessageDiv = doc.getElementById("dialogFramesExceptionMessageDiv");
	var _dialogFramesExceptionStackTraceDiv = doc.getElementById("dialogFramesExceptionStackTraceDiv");

	var errorsFound = false;
	if (_dialogFramesActionErrorsDiv) {
		errorsFound = trim(_dialogFramesActionErrorsDiv.innerHTML) != "";
	}
	else if (_dialogFramesExceptionMessageDiv) {
		errorsFound = trim(_dialogFramesExceptionMessageDiv.innerHTML) != "";
	}

	// if either of the hidden div tags have innerHTML, then we have an exception
	// that needs to be displayed
	if (errorsFound) {
		if (parent)
			parent.showDialog(globals_contextPath + "/errorDialog.vm");
		else
			showDialog(globals_contextPath + "/errorDialog.vm");
	}
	return errorsFound;
}

function enableFieldset( theCheckbox, fieldsetId ) {
  theFieldset = document.getElementById( fieldsetId );
  if ( theFieldset != null ) {
    fieldsetElements = theFieldset.all;
    if (( fieldsetElements != null ) && ( fieldsetElements.length > 3)){
      for ( i = 3; i < fieldsetElements.length; i++ ) {
        currentField = fieldsetElements[i];
        if ( currentField.id != theCheckbox.id ) {
					currentField.disabled = !theCheckbox.checked;
        }
      }
    }
  }
}

function disableFieldset( fieldsetId ) {
  theFieldset = document.getElementById( fieldsetId );
  if ( theFieldset != null ) {
    fieldsetElements = theFieldset.all;
    if (( fieldsetElements != null ) && ( fieldsetElements.length > 3)){
      for ( i = 0; i < fieldsetElements.length; i++ ) {
        currentField = fieldsetElements[i];
        currentField.disabled = true;
      }
    }
  }
}


/**
 * lettersOnly
 * Validates whether the key pressed is an upper or lower-case ASCII character
 */
function lettersOnly(evt) {
  evt = (evt) ? evt : event;
  var charCode = (evt.charCode) ? evt.charCode : ((evt.keyCode) ? evt.keyCode : ((evt.which) ? evt.which : 0));
  if (charCode > 31 && (charCode < 65 || charCode > 90) && (charCode < 97 || charCode > 122)) {
    return false;
  }
  return true;
}

/**
 * numbersOnly
 * Validates whether the key pressed is ASCII numeral
 */
function numbersOnly(evt) {
  evt = (evt) ? evt : event;
  var charCode = (evt.charCode) ? evt.charCode : ((evt.keyCode) ? evt.keyCode : ((evt.which) ? evt.which : 0));
  if (charCode > 31 && (charCode < 48 || charCode > 57)) {
    return false;
  }
  return true;
}

/**
 * lettersAndNumbersOnly
 * Validates whether the key pressed is ASCII numeral or an upper or lower-case ASCII character
 */
function lettersAndNumbersOnly(evt) {
  return(lettersOnly(evt) || numbersOnly(evt))
}

/**
 * trimFields
 * Trims leading/trailing spaces from fields specified in the array "fields"
 */
function trimFields(fields) {
	for (var i=0; i<fields.length; i++) {
		try {
			var e = document.getElementById(fields[i]);
			if (e != null && e.type == "text") {
				e.value = trim(e.value);
			}
		}
		catch (e) {
		}
	}

}

/**
 * getSourceElement
 * Determines the source element of a window envent. Returns a
 * reference to the object or null if the source cannot be determined
 */
function getSourceElement(evt) {
	var elem = null;
	var evt = (evt) ? evt : ((window.event) ? event : null);
	if (evt) {
		elem = (evt.target) ? evt.target :
			((evt.srcElement) ? evt.srcElement : null);
	}
	return elem;
}

/**
 * Shows a message in the status bar and then clears the
 * value after the specified interval.
 *
 * msg - the string message to show in the status bar
 * len - the length in seconds to display the message
 */
function showStatusMessage(msg,len) {
	len = (len) ? len*1000 : 2000;
	window.status = msg;
	setTimeout("window.status='';",len);
}

/**
 * Script to resize the tree and table div's so the page will
 * scroll correctly.
 */
function resizeTreeDivs( tableWidth ) {
  treeDivRef = document.getElementById("treeDiv");
  tableDivRef = document.getElementById("baselineParameter_Value" );
  if (( treeDivRef != null ) && ( tableDivRef != null )) {
    treeDivRef.style.width = (tableWidth * .30) - 10;
    tableDivRef.style.width = tableWidth * .70;
  }
}

/**
 * showPolicyDetailsDialog
 * Opens a dialog containg policy details
 */
function showPolicyDetailsDialog(policyId, deviceId) {
	var url = globals_contextPath + "/policy/showPolicyDetails.do?policyID=" + policyId;
	if (deviceId && deviceId != "") {
		url += "&deviceID=" + deviceId;
	}
	showDialog(url);
}

/**
 * findObject
 * Uses document.getElementById to return an instance of
 * the object specified by the id parameter
 */
function findObject(id) {
	return document.getElementById(id);
}

/**
 * sortSelectOptions
 * Sorts items alphabetically in a <select> object
 */
function sortSelectOptions(listName, keepFirst) {
	var oList = findObject(listName);
	var selectedOptionValue = oList.options[oList.selectedIndex].value;

	var startIdx = (keepFirst) ? 1:0;
	var endIdx = oList.options.length;
	var oListArray = new Array();
	var newSelectedOptionIndex = 0;
	var valueMatched = false;
	for (var i=startIdx; i<endIdx; i++) {
		oListArray.push(new Array(oList.options[i].innerText, oList.options[i]));
	}
	oListArray.sort(compareSelectOption);
	removeAllOptions(oList,keepFirst);
	for (var j=0; j<oListArray.length; j++) {
		var key, opt, val, lbl = "";
		key = oListArray[j][0];
		val = oListArray[j][1].value;
		if (selectedOptionValue == val)
		{
			newSelectedOptionIndex = j;
			valueMatched = true;
		}
		addOption(oList,key,val);

	}
	if (keepFirst && valueMatched)
		newSelectedOptionIndex++;

	oList.options[newSelectedOptionIndex].selected = true;
}


/**
 * removeAllOptions
 * Removes all existing options from a list
 */
function removeAllOptions(selectObj, keepFirst) {
	var startIdx = selectObj.options.length-1;
	var endIdx = (keepFirst) ? 1:0;
	for (var j=startIdx; j>=endIdx; j--) {
		selectObj.remove(j);
	}
}


/**
 * addOption
 * Dynamically create select list options
 */
function addOption(selectObj, oText, oValue, select) {
	var oOption = document.createElement("OPTION");
	selectObj.options.add(oOption);
	oOption.innerText = oText;
	oOption.value = oValue;
	if (select)
		oOption.selected = true;
}

/**
 * generateOptionList
 * Dynamically create select list options from an Array
 */
function generateOptionList(selectObj, optionsArray, selectValue) {

	removeAllOptions(selectObj, false);
	for (var j = 0; j < optionsArray.length; j++) {
		var selectOption = false;
		if (selectValue && selectValue != -1 && optionsArray[j][0] == selectValue)
			selectOption = true;
		addOption(selectObj, optionsArray[j][1], optionsArray[j][0], selectOption );
	}
}


/**
 * Encodes the specified string using HTML character codes
 */
function HTMLEncode(t) {
  return t.toString().replace(/&/g,"&amp;").replace(/"/g,"&quot;").replace(/</g,"&lt;").replace(/>/g,"&gt;").replace(/'/g,"&apos;"); //"
}


function handleTableResize() {
  tableDivRef = document.getElementById("table_div");
  if ( tableDivRef != null )
    tableDivRef.style.width = document.body.clientWidth - 20;
}

/**
 * Compare for sorting an array of objects
 */
function compareSelectOption(a,b) {
	try {
    if (a[0] < b[0]) {return -1}
    if (a[0] > b[0]) {return 1}
	} catch (ex) {
		//do nothing and keep the list the same
	}
	return 0;
}

/**
 * Compare for sorting an array of objects
 */
function compareMultiColumnData(a,b) {
	try {
	  if (typeof a.name != "undefined" && typeof b.name != "undefined") {
	    var aName = a.name.toLowerCase();
	    var bName = b.name.toLowerCase();
	    if (aName < bName) {return -1}
	    if (aName > bName) {return 1}
	  }
	} catch (ex) {
		//do nothing and keep the list the same
	}
	return 0;
}

/**
 * Removes the specifed object from the array
 */
function removeMultiColumnData (oArrayList,oContainerName,oStylesheetName,oIndexIdentifier,oRemoveButton) {
	if (oRemoveButton && !isButtonEnabled(oRemoveButton)) {
		return;
	}

	var oForm = oRemoveButton.form;
	for (var i=0; i<oForm.elements.length; i++) {
		var elem = oForm.elements[i];
		if (elem && elem.id && elem.type == "checkbox") {
			if (elem.id.indexOf(oIndexIdentifier) == 0 && elem.checked) {
				for (var j=0; j<oArrayList.length; j++) {
					var selectedIndex = parseInt(elem.value);
					if (oArrayList[j].index == selectedIndex) {
						if (oArrayList.length == 1 || oArrayList[oArrayList.length-1].index == selectedIndex) {
							oArrayList.pop();
						} else {
							var temp = oArrayList[oArrayList.length-1];
							oArrayList[oArrayList.length-1] = oArrayList[j];
							oArrayList[j] = temp;
							oArrayList.pop();
						}
					}
				}
			}
		}
	}
	showMultiColumnData(oArrayList,oContainerName,oStylesheetName);

}

/**
 * Renders an array of objects as XML and transforms using the
 * specified stylesheet.
 */
function showMultiColumnData(oArrayList,oContainerName,oStylesheetName) {
	oArrayList.sort(compareMultiColumnData);
	var oContainer = document.getElementById(oContainerName);
	var xsl = document.getElementById(oStylesheetName);
	var xml = "";
	if (oArrayList.length > 0) {
		xml += "<multi-column-data>";
		for (var i=0; i<oArrayList.length; i++) {
			var obj = oArrayList[i];
			xml += "<multi-column-data-element>";
			for (var f in obj) {
				xml += "<" + f + ">" + HTMLEncode(obj[f]) + "</" + f + ">";
			}
			xml += "</multi-column-data-element>";
		}
		xml += "</multi-column-data>"
	}

	var xmlDoc = loadXMLFromString(xml);
	oContainer.innerHTML = new String(xmlDoc.transformNode(xsl));
}

/**
 * Converts an object to string
 */
function object2String(obj) {
	var val, output = "";
	if (obj) {
	  output += "{";
	  for (var i in obj) {
	    val = obj[i];
	    switch (typeof val) {
	      case ("object"):
	      if (val[0]) {
	        output += i + ":" + array2String(val) + ",";
	      } else {
	        output += i + ":" + object2String(val) + ",";
	      }
	      break;
	      case ("string"):
	      output += i + ":'" + escape(val) + "',";
	      break;
	      case ("function"):
	      break;
	      default:
	      output += i + ":" + val + ",";
	    }
	  }
	  output = output.substring(0, output.length-1) + "}";
	}
	return output;
}

/**
 * Converts an array to string
 */
function array2String(array) {
	var output = "";
	if (array) {
	  output += "[";
	  for (var i in array) {
	    val = array[i];
	    switch (typeof val) {
	      case ("object"):
	      if (val[0]) {
	        output += array2String(val) + ",";
	      } else {
	        output += object2String(val) + ",";
	      }
	      break;
	      case ("string"):
	      output += "'" + escape(val) + "',";
	      break;
	      case ("function"):
	      break;
	      default:
	      output += val + ",";
	    }
	  }
	  output = output.substring(0, output.length-1) + "]";
	}
	return output;
}

/**
 * Converts a string to an object
 */
function string2Object(string) {
	eval("var result = " + string);
	return result;
}

/**
 * Converts a string to an array
 */
function string2Array(string) {
	eval("var result = " + string);
	return result;
}

/*
 * Copy options from first select to second Select element
 * keepSelected from the second select
 */
function copyOptions(firstSelectEleName, secondSelectEleName, keepSelected, selectValue)
{
  var firstSelectEle = document.getElementById( firstSelectEleName).options;
  var secondSelectEle = document.getElementById(secondSelectEleName);

  if (selectValue && selectValue != "")
    selectedValue = selectValue;
  else if (keepSelected && secondSelectEle.length != 0)
	  selectedValue = secondSelectEle.options[ secondSelectEle.selectedIndex].value;
	else
		selectedValue = "";

  secondSelectEle.length = 0;
  for ( i = 0; i < firstSelectEle.length; i++ ) {
    len = secondSelectEle.length ++;
    secondSelectEle.options[len].value = firstSelectEle[i].value;
    secondSelectEle.options[len].text = firstSelectEle[i].text;
    if (firstSelectEle[i].value == selectedValue ) {
      secondSelectEle.selectedIndex = i;
    }
  }
}

function selectOptionValue(selectEleName, value)
{
	var selectEle = document.getElementById( selectEleName).options;
	for ( i = 0; i < selectEle.length; i++ ) {
    if (selectEle[i].value == value ) {
      selectEle[i].selected = true;
    }
  }
}

function validateFirmware()
{
  var index = 0;
  while (true)
  {
    var firmwareComboElem = document.getElementById( "firmwareCombo:" + index++ );
    if (firmwareComboElem)
    {
    	if (( firmwareComboElem.options.length == 1 ) && ( firmwareComboElem.options[0].value == "-1"))
    	{
				return false;
			}
    }
    else
      break;
	}
	return true;
}

function hasFirmwareUpdate(functionID)
{
  hasFirmwareUpdateElem = document.getElementById( "hasFirmwareUpdate" + functionID );
  if (hasFirmwareUpdateElem && hasFirmwareUpdateElem.value == "true")
    return true;
  else
    return false;
}


function getFirmwareData(deviceTypeId, selectedFirmwareId, errorMessage)
{
  var req = XmlHttp.create();
  var async = false;
  req.open("POST", globals_contextPath + "/firmware/getFirmwares.do?searchDeviceTypeId=" + deviceTypeId, async);
  req.onreadystatechange = function () {
		if (req.readyState == 4) {
			if (req.status == 200) {
				// ...processing statements go here...
				document.getElementById("firmwaresDIV").innerHTML = req.responseText;
				//setFirmwareData(selectedFirmwareId);
			} else {
				alert(errorMessage + "\n" + req.statusText);
			}
		}
	}
  req.send("");
}

function setFirmwareData(selectedFirmwareId, fimwareComboID, argumentIndex)
{

  if (!argumentIndex)
    argumentIndex = fimwareComboID.substring(fimwareComboID.indexOf( ":") + 1);

  copyOptions("firmwares", fimwareComboID, false, selectedFirmwareId);
  var firmwareComboEle = document.getElementById(fimwareComboID);

  firmwareComboEle.disabled = false;

  if (document.getElementById("firmwareDataLoadingMsg"))
	  document.getElementById("firmwareDataLoadingMsg").style.display = "none";

  var metaDataDiv = document.getElementById("firmwareMetaData:" + argumentIndex)
  if (firmwareComboEle.length != 0)
  {
    selectedFirmwareID = firmwareComboEle.options[firmwareComboEle.selectedIndex].value;
    if (metaDataDiv)
      if (selectedFirmwareID != -1)
	      metaDataDiv.innerHTML =  document.getElementById("Firmware" + selectedFirmwareID).innerHTML ;
	  else
	    metaDataDiv.innerHTML = "";
  }
  else
  {
    if (metaDataDiv)
      metaDataDiv.innerHTML = "";
  }
}

function handleFirmwareChange(firmwareComboEle, argumentIndex)
{
  selectedFirmwareID = firmwareComboEle.options[firmwareComboEle.selectedIndex].value;
  if (selectedFirmwareID == -1)
    return;

  var metaDataDiv = document.getElementById("firmwareMetaData:" + argumentIndex)
  if (metaDataDiv)
	{
	  metaDataDiv.innerHTML =  document.getElementById("Firmware" + selectedFirmwareID).innerHTML;
		try
		{
			validate();
		}
		catch (err) {}
	}
}

function StringBuffer() {
  this.buffer = [];
}

StringBuffer.prototype.append = function append(string) {
  this.buffer.push(string);
  return this;
};

StringBuffer.prototype.toString = function toString() {
  return this.buffer.join("");
};
// example:
//var buf = new StringBuffer();

//buf.append("hello");
//buf.append("world");

//alert(buf.toString());

function containsInvalidHTMLCharacters( theString ) {
  returnValue = false;
  if ( theString.indexOf(">") >= 0 )
    returnValue = true;
  else if ( theString.indexOf("<") >= 0 )
    returnValue = true;
  else if ( theString.indexOf("&") >= 0 )
    returnValue = true;
  return returnValue;
}

function convertSpecialHTMLCharacters( theString ) {
  theString = theString.replace( "&", "&amp;" );
  theString = theString.replace( "<", "&lt;" );
  theString = theString.replace( ">", "&gt;" );
  return theString;
}

function showInfo( infoID, iconID, showIcon, hideIcon ) {
  document.body.focus();
  _infoArea = document.getElementById( infoID );
  _infoIcon = document.getElementById( iconID );
  if ( _infoArea.style.display == "none" ) {
    // closed
    _infoArea.style.display = "block";
    _infoIcon.src = hideIcon;
  }
  else {
    // open
    _infoArea.style.display = "none";
    _infoIcon.src = showIcon;
  }
}


function downloadVoucher(button, voucherId, frameName, contextPath) {
	if (!isButtonEnabled(button)) {
		return;
	}

	var oDownloadFrame = findObject(frameName);
	if (oDownloadFrame) {
		oDownloadFrame.src = contextPath + "/device/downloadVoucher.do?voucherId=" + voucherId;
	}
}

/**
 * Fix accent character to prevent IE from breaking HTML display
 */
function fixAccent(s) {
	s = (s) ? s : "";
	return s.replace(/`/g,"&#96;");
}
var detect = navigator.userAgent.toLowerCase();
var cookie,OS,browser,version,total,thestring;
var validBrowser = true;

function checkBrowser() {

	if (checkIt('konqueror')) {
		browser = "Konqueror";
		OS = "Linux";
	}
	else if (checkIt('safari')) {
		browser = "Safari";
	}
	else if (checkIt('firefox')) {
		browser = "FireFox";
	}
	else if (checkIt('omniweb')) {
		browser = "OmniWeb";
	}
	else if (checkIt('opera')) {
		browser = "Opera";
	}
	else if (checkIt('webtv')) {
		browser = "WebTV";
	}
	else if (checkIt('icab')) {
		browser = "iCab";
	}
	else if (checkIt('msie')) {
		browser = "Internet Explorer";
		version = parseFloat ( detect.substring ( place + thestring.length, detect.indexOf ( ";", place + thestring.length ) ) );
	}
	else if (!checkIt('compatible')) {
		browser = "Netscape Navigator";
		version = detect.charAt(8);
	}
	else {
		browser = "An unknown browser";
	}

	if (!version) version = detect.charAt(place + thestring.length);

	if (!OS) {
		if (checkIt('linux')) OS = "Linux";
		else if (checkIt('x11')) OS = "Unix";
		else if (checkIt('mac')) OS = "Mac"
		else if (checkIt('win')) OS = "Windows"
		else OS = "an unknown operating system";
	}

	if ( !cookie ) cookie = navigator.cookieEnabled;
}

function checkIt( string ) {
  place = detect.indexOf(string) + 1;
  thestring = string;
  return place;
}


function isIE() {
  checkBrowser();
  return (browser == "Internet Explorer");
}


function validateFields(jqFields) {
  var isValid = true;
  var jqElems = $j(jqFields);

  // Blank
  jqElems.filter('[checkblank="true"]').each(function() {
																				try {
																					var jqElem = $j(this);
																					if (isElemBlank(jqElem)) {
																						showErrorDiv(jqElem, "BlankErrorRow");
																						isValid = false;
																					}
																				} catch(err) {
																				}
																			});

  if (!isValid) return isValid;

  return isValid;
}

function isElemBlank(elem) {
  var bBlank = false;
  var jqElem = $j(elem);
  if ($j.trim(jqElem.val()).length == 0) {
		bBlank = true;
  }
  return bBlank;
}

function showErrorDiv(jqElem, errorClassSuffix) {
  var elemId = jqElem.attr('id');
  if (elemId != null) {
    var errorSelector = "." + elemId + errorClassSuffix;
    $j(errorSelector).show();
  }
}

function isResponseLoginPage(responseText) {
  var isLoginPage = (responseText.indexOf("<h1>Login</h1>") > -1) ? true : false;
  return isLoginPage;
}

function numberWithCommas(x) {
  return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

String.prototype.trunc = function(n){
	return this.substr(0,n-1)+(this.length>n?'&hellip;':'');
};
