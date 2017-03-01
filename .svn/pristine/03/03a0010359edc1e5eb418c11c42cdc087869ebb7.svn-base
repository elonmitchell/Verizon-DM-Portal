function ObjectList(objListFormName) {
  this.objListFormName = objListFormName;
  this.enableOnItemSectionButtons = null;
  this.disableOnMultipleSelectionButtons = null;
}

ObjectList.prototype.getFormObj = function() {
  this.formObj = document.getElementById(this.objListFormName);
  return this.formObj;
}
ObjectList.prototype.getFormElements = function() {
  return this.getFormObj().elements;
}

ObjectList.prototype.setEnableOnItemSelectionButtons = function( buttonList ) {
  this.enableOnItemSectionButtons = buttonList;
}

ObjectList.prototype.setDisableOnMultipleSelectionButtons = function( buttonList ) {
  this.disableOnMultipleSelectionButtons = buttonList;
}
    
ObjectList.prototype.checkForSelectedItems = function() {
  var returnValue = false;
  var formElements = this.getFormElements();
  for ( i = 0; i < formElements.length; i++ ) {
    if (formElements[i].type == "checkbox" && formElements[i].name.indexOf( "objectItem" ) == 0 ) {
      if ( formElements[i].checked == true ) {
        returnValue = true;
        break;
      }
    }
  }
  return returnValue;
}

ObjectList.prototype.getSelectedItems = function() {
  var returnValue = new Array(0);
  var formElements = this.getFormElements();
  for ( i = 0; i < formElements.length; i++ ) {
    if (formElements[i].type == "checkbox" && formElements[i].name.indexOf( "objectItem" ) == 0 ) {
      if ( formElements[i].checked == true ) {
        returnValue[ returnValue.length ] = formElements[i].value;
      }
    }
  }
  return returnValue;
}

ObjectList.prototype.selectItems = function(theCheckbox) {
  var formElements = this.getFormElements();
  for ( i = 0; i < formElements.length; i++ ) {
    if (formElements[i].type == "checkbox" && formElements[i].name.indexOf( "objectItem" ) == 0 ) {
      if ( !formElements[i].disabled )
        formElements[i].checked = theCheckbox.checked;
    }
  }
  this.handleObjectSelection( true );
}

ObjectList.prototype.clearColumnCheckbox = function() {
  var formElements = this.getFormElements();
  for ( i = 0; i < formElements.length; i++ ) {
    if (formElements[i].id == "selectAllObjects") {
      var columnCheckbox = formElements[i];
      columnCheckbox.checked = false;
    }
  }
}

ObjectList.prototype.isMultipleObjectChecked = function() {
  var numberOfCheckedObject = 0;
  var formElements = this.getFormElements();
  for ( i = 0; i < formElements.length; i++ ) {
    if (formElements[i].type == "checkbox" && formElements[i].name.indexOf( "objectItem" ) == 0 ) {
      if ( formElements[i].checked == true ) {
        numberOfCheckedObject++;
      }
    }
  }
  return (numberOfCheckedObject > 1);
}
    
ObjectList.prototype.handleObjectSelection = function( isSelectAllCheckbox ) {
  if ( !isSelectAllCheckbox ) {
    this.clearColumnCheckbox();
  }
  
  var itemsSelected = this.checkForSelectedItems();
  if ( this.enableOnItemSectionButtons != null ) {
    for ( i = 0; i < this.enableOnItemSectionButtons.length; i++ ) {
      var theButton = document.getElementById( this.enableOnItemSectionButtons[i] );
      if ( theButton != null ) {
        disableButton( theButton );
        if ( itemsSelected ) {
          enableButton( theButton );
        }
      }
    }
  }
  
  var multipleObjectsSelected = this.isMultipleObjectChecked();
  if ( this.disableOnMultipleSelectionButtons != null ) {
    for ( i = 0; i < this.disableOnMultipleSelectionButtons.length; i++ ) {
      var theButton = document.getElementById( this.disableOnMultipleSelectionButtons[i] );
      if ((( theButton != null ) && multipleObjectsSelected)) {
        disableButton( theButton );
        if (  multipleObjectsSelected ) {
          disableButton( theButton );
        }
      }
    }
  }
}

