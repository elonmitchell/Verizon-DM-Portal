/* global ScreenValidator, PopupScreenRadioGroupHandler, PopupScreenDynamicTableHandler, PopupManager, $, PopupViewer, EJS, CommonUtil, EventManager */
function PopupScreen(popupId, screenConfig) {
	"use strict";

	var _DEBUG_RACTIVE;

	var _popupId;
	var _screenId;
	var _screenConfig;
	var _eventTargetIds;
	var _renderData;
	var _screenRactive;
	var _jqScreenDiv;
	var _savedValsHash;
	var _radioGroupHandler;
	var _dynamicTableHandler;
	var _checkIfChangedHash;
	var _screenValidator;
	var _dependentFieldArrayHash;

	var _eventMgr;

	_init(popupId, screenConfig);

	function _init(popupId, screenConfig) {
		try { _DEBUG_RACTIVE = CommonUtil.DEBUG_FLAGS.POPUP_SCREEN_RACTIVE; } catch(e) {}

		// Initialize
		_popupId = popupId;
		_screenId = (screenConfig.hasOwnProperty("id")) ? screenConfig.id : _getUniqueId();
		_screenConfig = screenConfig;
		_eventTargetIds = [_screenId];
		_savedValsHash = {};
		_radioGroupHandler = null;
		_dynamicTableHandler = null;
		_checkIfChangedHash = null;
		var errorHandlers = {
			screenError: _showScreenError,
			screenErrors: _showScreenErrors,
			screenSuccess: _showScreenSuccess,
			fieldError: _showFieldError,
			hide: _hideAllMessages
		};
		_screenValidator = new ScreenValidator(errorHandlers);
	}

	function _show(inputRenderData) {
		// Initialize
		_eventMgr = new EventManager();
		try {
			_radioGroupHandler = new PopupScreenRadioGroupHandler(this);
		} catch (ex) {
		}
		try {
			_dynamicTableHandler = new PopupScreenDynamicTableHandler(this);
		} catch (ex) {
		}
		_savedValsHash = {};

		// Get the render data
		_renderData = _getDefaultRenderData();
		$.extend(true, _renderData, _screenConfig.content);
		var bIsModal = (_screenConfig.type === "modal");
		_renderData.bIsModal = bIsModal;
		_mergeInputRenderData(_renderData, inputRenderData);
		var eventObj = {
			type: "dataInit",
			renderData: _renderData,
			targetIds: _eventTargetIds, popupId: _popupId, screenId: _screenId
		};
		var eventRenderData = PopupManager.dispatchEvent(eventObj, this);
		if (eventRenderData != null) {
			if ($.inArray(false, eventRenderData) !== -1) return; // abort showing this screen
			$.extend(true, _renderData, eventRenderData);
		}
		_onScreenDataInit();

		// Render the data with the template
		var extraStyles = "";
		var possibleStyleNames = ["width", "minWidth", "maxWidth"];
		$.each(possibleStyleNames, function(idx) {
			var styleName = this;
			var styleVal = _screenConfig[styleName];
			if (styleVal != null) {
				styleName = styleName.replace(/([a-z])([A-Z])/g, '$1-$2').toLowerCase(); // convert camelCase to text-dash
				extraStyles += styleName+":"+styleVal+";"; // e.g. min-width:500px;
			}
		});
		_screenRactive = PopupViewer.getInstance().renderScreen(_popupId, _screenId, _screenConfig.template, _renderData, extraStyles);

		// Set the screen div
		var jqScreenDiv = PopupViewer.getInstance().getScreenDiv(_popupId, _screenId);
		_setScreenDiv(jqScreenDiv);

		// Update the screen with various handlers
		if (_radioGroupHandler != null) _radioGroupHandler.updateScreen();

		// Dispatch the screenRendered event
		eventObj = { type: "screenRendered", targetIds: _eventTargetIds, renderData: _renderData, popupId: _popupId, screenId: _screenId };
		PopupManager.dispatchEvent(eventObj, this);
		_onScreenRendered();

		// Dispatch buttonClick events when buttons are clicked
		var jqScreenButtons = $("button", _jqScreenDiv);
		_setButtonClickBehavior(jqScreenButtons);

		// Show the screen
		PopupViewer.getInstance().showScreen(_popupId, _screenId, bIsModal, _onPopupShown, _onPopupClosed);
	}

	function _onPopupShown() {
		// Screen shown -> Dispatch screenShown event
		var eventObj = { type: "screenShown", targetIds: _eventTargetIds, renderData: _renderData, popupId: _popupId, screenId: _screenId };
		PopupManager.dispatchEvent(eventObj, this);
	}

	function _onPopupClosed() {
		// Screen closed -> Dispatch screenClosed event
		var eventObj = { type: "screenClosed", targetIds: _eventTargetIds, renderData: _renderData, popupId: _popupId, screenId: _screenId };
		PopupManager.dispatchEvent(eventObj, this);
	}

	function _mergeInputRenderData(renderData, inputRenderData) {
		if (inputRenderData == null) return;
		if (inputRenderData != null) {
			$.extend(true, renderData, inputRenderData);
		}
	}

	function _setButtonClickBehavior(jqButtons) {
		$.each(jqButtons, function() {
			var jqButton = $(this);
			// Prevent multiple click handlers
			if (jqButton.attr('pm-has-click') == "true") return;
			// Attach click handler
			jqButton.click(function(evt) {
				var jqButton = $(this);
				var buttonVal = jqButton.val();
				// Dispatch event
				if (buttonVal != null && buttonVal !== "") {
					var eventObj = {
						type: "buttonClick",
						targetIds: _eventTargetIds,
						popupId: _popupId,
						screenId: _screenId,
						renderData: _renderData,
						value: buttonVal
					};
					PopupManager.dispatchEvent(eventObj, this);
				}

				// Close popup if necessary
				if (jqButton.data("dismiss") !== "modal") {
					var closePopupButtonVals = [null, "", "cancel", "ok"];
					if ($.inArray(buttonVal, closePopupButtonVals) >= 0) {
						_closePopup();
					}
				}
			});
			jqButton.attr('pm-has-click', 'true');
		});
	}

	// ******************************************* //
	// *** DYNAMIC ROW FUNCTIONS ***************** //
	// ******************************************* //

	function _addDynamicRow(jqAddRowButton) {
		if (_dynamicTableHandler != null) {
			var jqNewButtons = _dynamicTableHandler.addDynamicRow(jqAddRowButton);
			if (jqNewButtons != null) {
				_setButtonClickBehavior(jqNewButtons);
			}
		}
	}

	function _removeDynamicRow(jqRemoveRowButton) {
		if (_dynamicTableHandler != null) {
			_dynamicTableHandler.removeDynamicRow(jqRemoveRowButton);
		}
	}

	// ******************************************* //
	// *** FIELD FUNCTIONS *********************** //
	// ******************************************* //

	function _getField(fieldId, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		var jqField = $('#'+fieldId, jqContext);
		if (jqField.length == 0) {
			jqField = $('.fieldValue.'+fieldId, jqContext);
	}
		return jqField;
	}

	function _getFieldLabel(fieldId, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		return $('label[for="'+fieldId+'"]', jqContext);
	}

	function _getFieldHint(fieldId, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		return $('.'+fieldId+'-fieldHint', jqContext);
	}

	function _getTextField(fieldId, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		return $('input[type="text"]#'+fieldId, jqContext);
	}

	function _getTextFieldByName(name, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		return $('input[type="text"][name="'+name+'"]', jqContext);
	}

	function _getPasswordField(fieldId, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		return $('input[type="password"]#'+fieldId, jqContext);
	}

	function _getTextareaField(fieldId, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		return $('textarea#'+fieldId, jqContext);
	}

	function _getSelectField(fieldId, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		return $('select#'+fieldId, jqContext);
	}

	function _getSelectFieldOptions(field) {
		var fieldId = _getFieldId(field);
		return _getRactiveFieldProperty(fieldId, "options");
	}

	function _setSelectFieldOptions(selectField, optionsArray, selectedVal) {
		if (optionsArray == null) return;
		if ($.type(optionsArray) !== "array") optionsArray = [optionsArray];
		var fieldId = _getFieldId(selectField);
		var newOptionsArray = [];
		$.each(optionsArray, function(idx, optionsItem) {
			newOptionsArray.push(($.type(optionsItem) === "string") ? { "text": optionsItem, "value": optionsItem } : optionsItem);
		});
		_setRactiveFieldProperty(fieldId, "options", newOptionsArray);
	}

	function _getRadioField(fieldId, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		return $('input[type="radio"]#'+fieldId, jqContext);
	}

	function _getRadioGroup(groupId, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		return $('input[type="radio"][data-name="'+groupId+'"]', jqContext); // for some reason ractive chokes on "name" attributes for radio and checkbox inputs so we use "data-name" instead
	}

	function _getCheckedRadioField(groupId, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		return $('input[type="radio"][data-name="'+groupId+'"]:checked', jqContext); // for some reason ractive chokes on "name" attributes for radio and checkbox inputs so we use "data-name" instead
	}

	function _getRadioGroupVal(groupId, jqContext) {
		return _getCheckedRadioField(groupId, jqContext).val();
	}

	function _getCheckboxField(fieldId, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		return $('input[type="checkbox"]#'+fieldId, jqContext);
	}

	function _getCheckboxFieldVal(fieldId, jqContext) {
		var jqCB = _getCheckboxField(fieldId, jqContext);
		return (jqCB.is(':checked')) ? jqCB.val() : null;
	}

	function _getCheckedCheckboxFields(cbName, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		return (cbName == null) ? $('input[type="checkbox"]:checked', jqContext) : $('input[type="checkbox"][data-name="'+cbName+'"]:checked', jqContext); // for some reason ractive chokes on "name" attributes for radio and checkbox inputs so we use "data-name" instead
	}

	function _getCheckboxGroupVal(groupId, jqContext) {
		var jqCheckedFields = _getCheckedCheckboxFields(groupId, jqContext);
		return jqCheckedFields.map(function() { return this.value; }).get();
	}

	function _getFileField(fieldId, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		return $('input[type="file"]#'+fieldId, jqContext);
	}

	function _getViewField(fieldId, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		return $('span#'+fieldId+'_view', jqContext);
	}

	function _getHiddenField(fieldId, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		return $('input[type="hidden"]#'+fieldId, jqContext);
	}

	function _getFieldVal(fieldId) {
		return _getRactiveFieldProperty(fieldId, "value");
	}

	function _setFieldVal(fieldId, val) {
		_setRactiveFieldProperty(fieldId, val);
	}

	function _getFieldValueDiv(fieldId, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		var jqFieldValue = $('.fieldValue.'+fieldId, jqContext);
		return jqFieldValue;
	}

	function _getFieldValueWaitDiv(fieldId, jqContext) { // defunct
		if (jqContext == null) jqContext = _jqScreenDiv;
		var jqFieldValueWait = $('.fieldValueWait.'+fieldId, jqContext);
		return jqFieldValueWait;
	}

	function _getFieldValueNADiv(fieldId, jqContext) { // defunct
		if (jqContext == null) jqContext = _jqScreenDiv;
		var jqFieldValueNA = $('.fieldValueNA.'+fieldId, jqContext);
		return jqFieldValueNA;
	}

	function _getCheckboxChooserVals(fieldId, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		var jqCheckedFields = _getCheckedCheckboxFields(fieldId, jqContext);
		var sgVals = jqCheckedFields.map(function() { return this.value; }).get();
		return sgVals;
	}

	function _updateSpecialGroupChooserOptions(fieldId, options, value, tagAttributes, jqContext) {
		if (jqContext != null) debugger;
		var jqFieldValueDiv = _getFieldValueDiv(fieldId, jqContext);
		_setRactiveFieldProperty(fieldId, "options", options, function() {
			if (typeof(options) === "object") _updateSpecialGroupBehavior(jqFieldValueDiv);
			$.each(tagAttributes, function(attrName, attrVal) {
				_setRactiveFieldProperty(fieldId, "attrName", attrVal);
			});
		});
	}

	function _updateChildMultiStepPackageChooser(fieldId, value, errorMessage, cbFunc) {
		_setRactiveFieldProperty(fieldId, "value", value, cbFunc);
		if (errorMessage == null) {
			// Success -> Hide error message
			_hideFieldError(fieldId);
		} else {
			// Show error message
			_showFieldError(fieldId, null, errorMessage);
		}
	}

	function _getDateTimeMeridianDate(fieldId, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		// Parse date from fields
		var jqDateTime = _getTextField(fieldId);
		var dateStr = jqDateTime.val(); // e.g. "02/28/2013 8:45 PM"
		var dateObj = new Date(dateStr);
		return dateObj;
	}

	function _disableFields(fieldsArray, bClearVals, bSaveVals) {
		if (fieldsArray == null) return;
		if ($.type(fieldsArray) !== "array") fieldsArray = [fieldsArray];
		if (bClearVals == null) bClearVals = false;
		if (bSaveVals == null) bSaveVals = false;
		$.each(fieldsArray, function() {
			// Get the field id
			var fieldId = _getFieldId(this);
			// Save value if necessary
			if (bSaveVals) {
				_savedValsHash[fieldId] = _getRactiveFieldProperty(fieldId, "value");
			}
			// Clear value if necessary
			if (bClearVals) {
				_setRactiveFieldProperty(fieldId, "value", "", function() {
					// Disable
					_setRactiveFieldProperty(fieldId, "extra_attributes.disabled", "true");
				});
			} else {
				// Disable
				_setRactiveFieldProperty(fieldId, "extra_attributes.disabled", "true");
			}
		});
	}

	function _enableFields(fieldsArray, bRestoreVals) {
		if (fieldsArray == null) return;
		if ($.type(fieldsArray) !== "array") fieldsArray = [fieldsArray];
		if (bRestoreVals == null) bRestoreVals = false;
		$.each(fieldsArray, function() {
			// Get the field id
			var fieldId = _getFieldId(this);
			// Disable
			_setRactiveFieldProperty(fieldId, "extra_attributes.disabled", null);
			// Restore value if necessary
			if (bRestoreVals) {
				var val = _savedValsHash[fieldId];
				if (val != null) {
					try {
						_setRactiveFieldProperty(fieldId, "value", val);
					} catch(ex) {
						// Fails for file elements...ignore
					}
				}
			}

		});
	}

	function _makeFieldsRequired(fieldsArray) {
		if (fieldsArray == null) return;
		if ($.type(fieldsArray) !== "array") fieldsArray = [fieldsArray];
		$.each(fieldsArray, function() {
			// Get the field id
			var fieldId = _getFieldId(this);
			// Make required
			_setRactiveFieldProperty(fieldId, "required", true);
		});
	}

	function _makeFieldsNotRequired(fieldsArray) {
		if (fieldsArray == null) return;
		if ($.type(fieldsArray) !== "array") fieldsArray = [fieldsArray];
		$.each(fieldsArray, function() {
			// Get the field id
			var fieldId = _getFieldId(this);
			// Make required
			_setRactiveFieldProperty(fieldId, "required", null);
		});
	}

	function _disableButtons(buttonIdArray) {
		$.each(buttonIdArray, function(idx, buttonId) {
			_setRactiveButtonProperty(buttonId, "disabled", "true");
		});
	}

	function _disableButtons_OLD(buttonsArray) {
		$.each(buttonsArray, function() {
			// Get the button
			var jqButton = _getButton(this);
			// Disable
			jqButton.attr("disabled", "disabled");
		});
	}

	function _enableButtons(buttonIdArray) {
		$.each(buttonIdArray, function(idx, buttonId) {
			_setRactiveButtonProperty(buttonId, "disabled", null);
		});
	}

	function _enableButtons_OLD(buttonsArray, bRestoreVals) {
		$.each(buttonsArray, function() {
			// Get the button
			var jqButton = _getButton(this);
			// Enable
			jqButton.removeAttr("disabled");
		});
	}

	function _changeButtonText(btn, newTextVal) {
		// TODO: Consider using ractive instead of jquery
		var jqButton = _getButton(btn);
		jqButton.children("span").text(newTextVal);
	}

	function _getButton(btn) {
		return ($.type(btn) === "string") ? _jqScreenDiv.find('button[value="'+btn+'"]') : $(btn);
	}

	function _showFieldValueWait(fieldId) {
		_setRactiveFieldProperty(fieldId, "bShowWait", true);
	}

	function _hideFieldValueWait(fieldId) {
		_setRactiveFieldProperty(fieldId, "bShowWait", false);
	}

	function _showFieldValueNA(field, naMsg) {
		if (naMsg == null) naMsg = "N/A";
		var fieldId = _getFieldId(field);
		_setRactiveFieldProperty(fieldId, "fieldValueNA", naMsg);
	}

	function _hideFieldValueNA(field) {
		var fieldId = _getFieldId(field);
		_setRactiveFieldProperty(fieldId, "fieldValueNA", null);
	}

	function _showFieldValueNA_OLD(field, naMsg, jqContext) { // defunct
		if (naMsg == null) naMsg = "N/A";
		if (jqContext == null) jqContext = _jqScreenDiv;
		var fieldId = _getFieldId(field, jqContext);
		var jqFieldValueDiv = _getFieldValueDiv(fieldId, jqContext);
		// Remove existing N/A div
		var jqFieldValueNADiv = _getFieldValueNADiv(fieldId, jqContext);
		if (jqFieldValueNADiv.length > 0) jqFieldValueNADiv.remove();
		// Add N/A content
		var jqNAContent = $('<div>'+naMsg+'</div>').addClass('fieldValueNA').addClass(fieldId);
		jqNAContent.insertAfter(jqFieldValueDiv);
		jqFieldValueNADiv = _getFieldValueNADiv(fieldId, jqContext);
		// Hide field value div, show field value N/A div
		if (jqFieldValueNADiv.length !== 0) {
			jqFieldValueDiv.hide();
			jqFieldValueNADiv.show();
		}
	}

	function _hideFieldValueNA_OLD(field, jqContext) { // defunct
		if (jqContext == null) jqContext = _jqScreenDiv;
		var fieldId = _getFieldId(field, jqContext);
		var jqFieldValueDiv = _getFieldValueDiv(fieldId, jqContext);
		var jqFieldValueNADiv = _getFieldValueNADiv(fieldId, jqContext);
		jqFieldValueNADiv.hide();
		jqFieldValueDiv.show();
	}

	function _getFieldId(field, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		var fieldId;
		if ($.type(field) === "string") {
			fieldId = field;
		} else {
			var jqField = $(field, jqContext);
			if (jqField.is(':radio') || jqField.is(':checkbox')) {
				fieldId = jqField.attr("data-name"); // for some reason ractive chokes on "name" attributes for radio and checkbox inputs so we use "data-name" instead
			} else {
				fieldId = jqField.attr("id");
				if (fieldId == null) {
					if (jqField.hasClass("fieldValue")) {
						var arr = jqField.attr("class").split(" ");
						arr = $.grep(arr, function(value) {
							return value !== "fieldValue";
						});
						fieldId = arr[0];
					}
				}
			}
		}
		return fieldId;
	}

	function _getJqFieldValue(jqField) {
		var fieldVal;
		if ((jqField.is(':checkbox') || jqField.is(':radio')) && !jqField.is(':checked')) {
			fieldVal = null;
		} else {
			fieldVal = jqField.val();
		}
		return fieldVal;
	}

	// ******************************************* //
	// *** VALIDATION FUNCTIONS ****************** //
	// ******************************************* //
	function _validateFields(jqContext, bValidateDisabledFields) {
		var bIsValid = true;
		if (jqContext == null) jqContext = _jqScreenDiv;
		if (bValidateDisabledFields == null) bValidateDisabledFields = true;
		_hideAllMessages(jqContext);

		// Get the fields to validate
		var fieldSelector;
		if (bValidateDisabledFields) {
			fieldSelector = "select[id], input[id], textarea[id]";
		} else {
			fieldSelector = "select[id]:enabled, input[id]:enabled, textarea[id]:enabled";
		}
		var jqFields = $(fieldSelector, jqContext);
		if (_radioGroupHandler != null) {
			jqFields = _radioGroupHandler.filterActiveFields(jqFields);
		}

		// Validate
		bIsValid = _screenValidator.validateFields(jqFields, bValidateDisabledFields, jqContext);

		if (bIsValid) {
			// Check if field values changed
			var jqCheckIfChangedFields = jqFields.filter('[data-checkIfChanged="true"]');
			if (jqCheckIfChangedFields.length > 0) {
				bIsValid = false;
				jqCheckIfChangedFields.each(function(i, elem1) {
					var jqField = $(this);
					var fieldId = jqField.attr("id");
					var fieldVal = _getJqFieldValue(jqField);
					if (fieldVal !== _checkIfChangedHash[fieldId]) {
						// Value changed
						bIsValid = true;
						return false;
					}
				});
				if (!bIsValid) {
					_showScreenError("Nothing has been changed!", jqContext);
				}
			}
		}

		return bIsValid;
	}

	// ******************************************* //
	// *** DATA INIT AND SCREEN RENDERED ********* //
	// ******************************************* //

	function _onScreenDataInit() {
		_dependentFieldArrayHash = {};
		if (_renderData != null && _renderData.formFields != null && _renderData.formFields.fieldData != null) {
			// Store field dependencies
			$.each(_renderData.formFields.fieldData, function(fieldId, fieldDataItem) {
				if ($.type(fieldDataItem.dependsOnField) === "string") {
					var dependeeFieldId = fieldDataItem.dependsOnField;
					// Initialize dependencies for this field if necessary
					if (!_dependentFieldArrayHash.hasOwnProperty(dependeeFieldId)) {
						_dependentFieldArrayHash[dependeeFieldId] = [];
					}
					// Add to list of dependencies for the dependee field
					_dependentFieldArrayHash[dependeeFieldId].push(fieldId);
				}
			});
		}
	}

	function _onScreenRendered() {
		_listenForFieldChanges();

		// IE doesn't recognize the maxlength attribute of textareas so
		// we have to impose our own maxlength requirement
		// Get all textareas that have a "maxlength" property.
		$('textarea[maxlength]', _jqScreenDiv).on('keyup blur', function() {
			// Store the maxlength and value of the field.
			var jqField = $(this);
			var maxlength = jqField.attr('maxlength');
			var val = jqField.val();

			// Trim the field if it has content over the maxlength.
			if (val.length > maxlength) {
				//_showFieldError(jqField, "maxlength");
				$(this).val(val.slice(0, maxlength));
				//setTimeout(function() { _hideFieldError(jqField, "maxlength"); }, 4000);
			}
		});

		// Store original values for checkIfChanged fields
		var fieldSelector = "select[id], input[id], textarea[id]";
		var jqFields = $(fieldSelector, _jqScreenDiv);
		_checkIfChangedHash = {};
		jqFields.filter('[data-checkIfChanged="true"]').each(function(i, elem1) {
			var jqField = $(this);
			var fieldId = jqField.attr("id");
			var fieldVal = _getJqFieldValue(jqField);
			_checkIfChangedHash[fieldId] = fieldVal;
		});

		// Update behavior of special group checkboxes
		_updateSpecialGroupBehavior();

		// Dynamic nav pills
		var jqDynamicNavPills = _jqScreenDiv.find('ul.dynamic-nav-pills');
		if (jqDynamicNavPills.length > 0) {
			$.each(jqDynamicNavPills, function() {
				var jqDynamicNavPill = $(this);
				var fieldId = jqDynamicNavPill.data("fieldid");
				jqDynamicNavPill.find("li a").click(function() {
					_hideAllMessages();
					_renderData.formFields.fieldData[fieldId] = $(this).data("fieldval");
				});
			});
		}
	}

	function _listenForFieldChanges() {
		if (_renderData != null && _renderData.formFields != null && _renderData.formFields.fieldData != null) {
			$.each(_renderData.formFields.fieldData, function(fieldId, fieldData) {
				var bListenForChanges = _dependentFieldArrayHash.hasOwnProperty(fieldId);
				if (bListenForChanges) {
					var propPath = _getRactiveFieldPropPath(fieldId, "value");
					_screenRactive.observe(propPath, function(newValue, oldValue, keypath) {
						if (_DEBUG_RACTIVE) console.log("** " + keypath + " value changed from [" + oldValue + "] to [" + newValue + "]");
						// Value changed -> send change event for this field
						var eventObj = {
							type: "change",
							newValue: newValue,
							popupId: _popupId,
							screenId: _screenId,
							targetIds: [fieldId]
						};
						_eventMgr.dispatchEvent(eventObj);
						// Send dependeeChange event for all dependents
						var dependentFieldArray = _dependentFieldArrayHash[fieldId];
						if (dependentFieldArray != null) {
							$.each(dependentFieldArray, function(idx, dependentFieldId) {
								if (_DEBUG_RACTIVE) console.log("Sending dependeeChange to " + dependentFieldId);
								var eventObj = {
									type: "dependeeChange",
									dependeeValue: newValue,
									popupId: _popupId,
									screenId: _screenId,
									dependeeFieldId: fieldId,
									targetIds: [dependentFieldId]
								};
								_eventMgr.dispatchEvent(eventObj);
							});
						}
					});
				}
			});
		}
	}

	function _updateSpecialGroupBehavior(jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		var jqChooserDiv = $('.specialGroupChooser', jqContext);
		if (jqChooserDiv.length > 0) {
			var jqCBs = jqChooserDiv.find('input[type="checkbox"][data-defaultGroup!="true"]');
			if (jqCBs.length > 0) {
				var enableDisableCBs = function(jqCB) {
					if (jqCB.is(':checked')) {
						// This checkbox is checked -> Disable all other checkboxes
						jqCBs.not(jqCB).attr("disabled", "disabled");
					} else {
						// This checkbox is not checked -> Enable all other checkboxes
						jqCBs.not(jqCB).removeAttr("disabled");
					}
				};
				jqCBs.each(function() {
					var jqCB = $(this);
					if (jqCB.is(':checked')) enableDisableCBs(jqCB);
				});
				jqCBs.click(function(evt) {
					enableDisableCBs($(this));
				});
			}
		}
	}

	function _showScreenSuccess(successHTML, jqContext, bAllowClose) {
		_setRactiveBaseProperty("successMessage", successHTML);
	}

	function _showScreenError(errorHTML, jqContext, bAllowClose) {
		_setRactiveBaseProperty("errorMessage", errorHTML);
	}

	function _showScreenErrors(errorStrArray, jqContext, bAllowClose) {
		if (errorStrArray.length > 0) {
			var errorHTML = "";
			if (errorStrArray.length === 1) {
				errorHTML = errorStrArray[0];
			} else {
				errorHTML = '<span align="left"><ul>';
				for (var i = 0; i < errorStrArray.length; i++) {
					errorHTML += "<li>" + errorStrArray[i] + "</li>";
				}
				errorHTML += '</ul></span>';
			}
			_showScreenError(errorHTML, jqContext, bAllowClose);
		}
	}

	function _showFieldError(field, errorClass, errorMessage, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		var fieldId = _getFieldId(field);
		if (fieldId != null) {
			var classArray = ["help-block", fieldId];
			if (errorClass != null) classArray.push(errorClass);
			var errorSelector = "."+classArray.join(".");
			var jqHelpBlocks = $(errorSelector, jqContext);
			if (errorMessage != null) jqHelpBlocks.html(errorMessage);
			jqHelpBlocks.show();
		}
	}

	function _hideFieldError(field, errorClass, jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		var fieldId = _getFieldId(field);
		if (fieldId != null) {
			var classArray = ["help-block", fieldId];
			if (errorClass != null) classArray.push(errorClass);
			var errorSelector = "."+classArray.join(".");
			$(errorSelector, jqContext).hide();
		}
	}

	function _hideAllMessages(jqContext) {
		if (jqContext == null) jqContext = _jqScreenDiv;
		_setRactiveBaseProperty("errorMessage", "");
		_setRactiveBaseProperty("successMessage", "");
		$('.help-block', jqContext).hide();
		$('.duplicateEntry', jqContext).each(function() {
			$(this).removeClass('duplicateEntry');
		});
	}

	function _close() {
		// Close the screen
		PopupViewer.getInstance().closeScreen(_popupId, _screenId);
	}

	function _closePopup() {
		PopupViewer.getInstance().closePopup(_popupId, _screenId);
	}

	function _onPopupClose(cbFunc) {
		debugger; // switch to listen for screenClosed
	}

	function _resize() {
		PopupViewer.getInstance().resizePopup(_popupId, _screenId);
	}

	// ******************************************* //
	// *** UTILITY FUNCTIONS ********************* //
	// ******************************************* //

	function _getScreenDiv() {
		return _jqScreenDiv;
	}

	function _setScreenDiv(jqScreenDiv) {
		_jqScreenDiv = jqScreenDiv;
	}

	function _getContentDiv() {
		return _jqScreenDiv.find('.content-div');
	}

	function _getUniqueId() {
		return "scr-" + (new Date()).getTime();
	}

	function _renderContent(templateName, renderData) { // defunct
		var templateUrl = PopupManager.getScreenTemplateUrl(templateName);
		var content = new EJS({url:templateUrl}).render(renderData);
		return content;
	}

	function _setRactiveBaseProperty(prop, val) {
		if (_DEBUG_RACTIVE) console.log("_setRactiveBaseProperty(): "+prop+" to "+val);
		_screenRactive.set("g_RD." + prop, val);
	}

	function _getRactiveFieldProperty(fieldId, prop) {
		var propPath = _getRactiveFieldPropPath(fieldId, prop);
		var val = _screenRactive.get(propPath);
		if (_DEBUG_RACTIVE) console.log("_getRactiveFieldProperty(): "+fieldId+" = "+val);
		return val;
	}

	function _setRactiveFieldProperty(fieldId, prop, val, cbFunc) {
		var propPath = _getRactiveFieldPropPath(fieldId, prop);
		if (val == null) {
			if (_DEBUG_RACTIVE) console.log("_setRactiveFieldProperty(): Unsetting "+propPath);
			_screenRactive.unset(propPath).then(function () {
				if (cbFunc != null) cbFunc();
			});
		} else {
			if (_DEBUG_RACTIVE) console.log("_setRactiveFieldProperty(): Setting "+propPath+" to "+val);
			_screenRactive.set(propPath, val).then(function () {
				if (cbFunc != null) cbFunc();
			});
		}
	}

	function _setRactiveButtonProperty(buttonId, prop, val, cbFunc) {
		var propPath = _getRactiveButtonPropPath(buttonId, prop);
		if (propPath != null) {
			if (_DEBUG_RACTIVE) console.log("_setRactiveButtonProperty(): Setting "+propPath+" to "+val);
			_screenRactive.set(propPath, val).then(function () {
				if (cbFunc != null) cbFunc();
			});
		}
	}

	function _getRactiveFieldPropPath(fieldId, prop) {
		return "g_RD.formFields.fieldData."+fieldId+"." + prop;
	}

	function _getRactiveButtonPropPath(buttonId, prop) {
		var propPath = null;
		$.each(_renderData.buttons, function(idx, buttonConfig) {
			if (buttonConfig.value == buttonId) {
				propPath = "g_RD.buttons["+idx+"].disabled";
				return false;
			}
		});
		return propPath;
	}

	function _getDefaultRenderData() {
		var renderData = {
			title: "Default Title",
			waitText: "Please wait",
			screenId: _screenId,
			submitButtonText: "Submit",
			okButtonText: "OK",
			okButtonValue: "ok"
		};
		return renderData;
	}

	function _getScreenRactive() {
		return _screenRactive;
	}

	function _getScreenRenderData() {
		return _renderData;
	}

	function _addIdEventListeners(id, obj) {
		_eventMgr.addIdEventListeners(id, obj);
	}

	// ******************************************* //
	// *** PUBLIC METHODS ************************ //
	// ******************************************* //

	return {
		show: _show,
		renderContent: _renderContent,
		getScreenDiv: _getScreenDiv,
		getContentDiv: _getContentDiv,
		addDynamicRow: _addDynamicRow,
		removeDynamicRow: _removeDynamicRow,
		getField: _getField,
		getFieldLabel: _getFieldLabel,
		getFieldHint: _getFieldHint,
		getTextField: _getTextField,
		getTextFieldByName: _getTextFieldByName,
		getPasswordField: _getPasswordField,
		getTextareaField: _getTextareaField,
		getRadioField: _getRadioField,
		getRadioGroup: _getRadioGroup,
		getCheckedRadioField: _getCheckedRadioField,
		getRadioGroupVal: _getRadioGroupVal,
		getCheckboxField: _getCheckboxField,
		getCheckboxFieldVal: _getCheckboxFieldVal,
		getCheckboxGroupVal: _getCheckboxGroupVal,
		getSelectField: _getSelectField,
		getSelectFieldOptions: _getSelectFieldOptions,
		setSelectFieldOptions: _setSelectFieldOptions,
		getFileField: _getFileField,
		getViewField: _getViewField,
		getHiddenField: _getHiddenField,
		getFieldVal: _getFieldVal,
		setFieldVal: _setFieldVal,
		getCheckboxChooserVals: _getCheckboxChooserVals,
		updateSpecialGroupChooserOptions: _updateSpecialGroupChooserOptions,
		updateChildMultiStepPackageChooser: _updateChildMultiStepPackageChooser,
		getDateTimeMeridianDate: _getDateTimeMeridianDate,
		disableFields: _disableFields,
		enableFields: _enableFields,
		makeFieldsRequired: _makeFieldsRequired,
		makeFieldsNotRequired: _makeFieldsNotRequired,
		disableButtons: _disableButtons,
		enableButtons: _enableButtons,
		changeButtonText: _changeButtonText,
		showFieldValueWait: _showFieldValueWait,
		hideFieldValueWait: _hideFieldValueWait,
		showFieldValueNA: _showFieldValueNA,
		hideFieldValueNA: _hideFieldValueNA,
		validateFields: _validateFields,
		showScreenSuccess: _showScreenSuccess,
		showScreenError: _showScreenError,
		showScreenErrors: _showScreenErrors,
		showFieldError: _showFieldError,
		hideFieldError: _hideFieldError,
		hideAllMessages: _hideAllMessages,
		closePopup: _closePopup,
		close: _close,
		onPopupClose: _onPopupClose,
		resize: _resize,
		addIdEventListeners: _addIdEventListeners,
		_getScreenRactive: _getScreenRactive,
		_getScreenRenderData: _getScreenRenderData
	};

}
