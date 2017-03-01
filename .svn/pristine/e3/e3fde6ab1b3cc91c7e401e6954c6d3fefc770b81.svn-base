/* global jQuery, moment */
function ScreenValidator(fieldValueFunc, errorHandlers) {
	var $ = jQuery;
	var _jqDefaultContext = $('body');
	var _errorHandlers = {};
	var _fieldValueFunc = null;

	_init(fieldValueFunc, errorHandlers);

	function _init(fieldValueFunc, errorHandlers) {
		if (fieldValueFunc != null) _fieldValueFunc = fieldValueFunc;
		if (errorHandlers != null) _errorHandlers = errorHandlers;
	}

	function _validateFields(jqFields, bValidateDisabledFields, jqContext) {
		var bIsValid = true;
		if (jqContext == null) jqContext = _jqDefaultContext;
		if (bValidateDisabledFields == null) bValidateDisabledFields = true;
		_hideAllMessages(jqContext);

		if (jqFields == null) {
			// Get the fields to validate
			var fieldSelector;
			if (bValidateDisabledFields) {
				fieldSelector = "select[id], input[id], textarea[id]";
			} else {
				fieldSelector = "select[id]:enabled, input[id]:enabled, textarea[id]:enabled";
			}
			jqFields = $(fieldSelector, jqContext);
		} else {
			jqFields = $(jqFields);
		}

		// Blank
		var radioNamesHash = {};
		var cbNamesHash = {};
		jqFields.filter('[required]').each(function() {
			var jqField = $(this);
			try {
				if (jqField.is(':radio')) {
					var radioName = jqField.attr("name");
					if (radioName != null) radioNamesHash[radioName] = true;
				} else if (jqField.is(':checkbox')) {
					var cbName = jqField.attr("name");
					if (cbName != null) cbNamesHash[cbName] = true;
				} else {
					if (_isFieldBlank(jqField)) {
						_showFieldError(jqField, "required", null, jqContext);
						bIsValid = false;
					}
				}
			} catch(err) {
			}
		});
		$.each(radioNamesHash, function(radioName, val) {
			if (jqFields.filter('[name='+radioName+']:checked').length == 0) {
				bIsValid = false;
				_showFieldError(radioName, "required", null, jqContext);
			}
		});
		$.each(cbNamesHash, function(cbName, val) {
			if (jqFields.filter('[name='+cbName+']:checked').length == 0) {
				bIsValid = false;
				_showFieldError(cbName, "required", null, jqContext);
			}
		});
		if (!bIsValid) return bIsValid;

		// Exact size
		jqFields.filter('[valsize]').each(function() {
			try {
				var jqField = $(this);
				if (!_isFieldBlank(jqField)) {
					var valSizeArray = [];
					var valStr = jqField.attr('valsize');
					if (valStr.indexOf(',') >= 0) { // array of integers
						valSizeArray = valStr.split(',');
					} else { // integer
						valSizeArray.push(valStr);
					}
					var bLenMatches = false;
					var len = _getFieldVal(jqField).length;
					$.each(valSizeArray, function() {
						var valSize = this * 1;
						if (len == valSize) {
							bLenMatches = true;
							return false;
						}
					});
					if (!bLenMatches) {
						_showFieldError(jqField, "valsize", null, jqContext);
						bIsValid = false;
					}
				}
			} catch(err) {
			}
		});
		if (!bIsValid) return bIsValid;

		// Minimum size
		jqFields.filter('[valminsize]').each(function() {
			try {
				var jqField = $(this);
				var valMinSize = jqField.attr('valminsize') * 1;
				if (_getFieldVal(jqField).length < valMinSize) {
					_showFieldError(jqField, "valminsize", null, jqContext);
					bIsValid = false;
				}
			} catch(err) {
			}
		});
		if (!bIsValid) return bIsValid;

		// Regular expression
		jqFields.filter('[valregexp]').each(function() {
			try {
				var jqField = $(this);
				var valRegexp = jqField.attr('valregexp');
				var reObj = new RegExp(valRegexp);
				var val = _getFieldVal(jqField);
				if (!reObj.test(val)) {
					_showFieldError(jqField, "valregexp", null, jqContext);
					bIsValid = false;
				}
			} catch(err) {
			}
		});
		if (!bIsValid) return bIsValid;

		// Date field
		jqFields.filter('[checkdate="true"]').each(function() {
			try {
				var jqField = $(this);
				var val = _getFieldVal(jqField);
				if (!_isValidDate(val)) {
					_showFieldError(jqField, "checkdate", null, jqContext);
					bIsValid = false;
				} else {

				}
			} catch(err) {
			}
		});
		if (!bIsValid) return bIsValid;

		// Hour field
		jqFields.filter('[checkhour="true"]').each(function() {
			try {
				var jqField = $(this);
				var val = _getFieldVal(jqField);
				var isHourValid = false;
				if (_isAnInteger(val)) {
					var intVal = parseInt(val, 10);
					if (intVal >= 1 && intVal <= 12) {
						isHourValid = true;
					}
				}
				if (!isHourValid) {
					_showFieldError(jqField, "checkhour", null, jqContext);
					bIsValid = false;
				}
			} catch(err) {
			}
		});
		if (!bIsValid) return bIsValid;

		// Minute field
		jqFields.filter('[checkminute="true"]').each(function() {
			try {
				var jqField = $(this);
				var val = _getFieldVal(jqField);
				var isMinuteValid = false;
				if (_isAnInteger(val)) {
					var intVal = parseInt(val, 10);
					if (intVal >= 0 && intVal < 60) {
						isMinuteValid = true;
					}
				}
				if (!isMinuteValid) {
					_showFieldError(jqField, "checkminute", null, jqContext);
					bIsValid = false;
				}
			} catch(err) {
			}
		});
		if (!bIsValid) return bIsValid;

		// URL
		jqFields.filter('[checkurl="true"]').each(function() {
			try {
				var jqField = $(this);
				var reObj = new RegExp('^$|^((http|ftp|https)://)[a-z0-9\-_]+(\.[a-z0-9\-_]+)+([a-z0-9\-\.,@\?^=%&;:/~\+#]*[a-z0-9\-@\?^=%&;/~\+#])?$', 'i');
				var val = $.trim(_getFieldVal(jqField));
				if (!reObj.test(val)) {
					_showFieldError(jqField, "checkurl", null, jqContext);
					bIsValid = false;
				}
			} catch(err) {
			}
		});
		if (!bIsValid) return bIsValid;

		// Duplicates
		jqFields.filter('[uniquekey]').each(function(i, field1) {
			var jqField1 = $(this);
			var val1 = _getFieldVal(jqField1);
			var uniquekey1 = jqField1.attr("uniquekey");
			if (val1 != "") {
				jqFields.not(jqField1).filter('[uniquekey="'+uniquekey1+'"]').each(function(j, field2) {
					var jqField2 = $(this);
					var val2 = _getFieldVal(jqField2);
					if (val1 == val2) {
						bIsValid = false; // duplicate found
						jqField1.addClass('duplicateEntry');
						jqField2.addClass('duplicateEntry');
						$('.error-message', jqContext).html("Duplicate values found").show();
						return false; // exit inner loop
					}
				});
			}
			if (!bIsValid) return false; // exit inner loop
		});
		if (!bIsValid) return bIsValid;

		return bIsValid;
	}
	function _isFieldBlank(field) {
		var bBlank = false;
		var jqField = $(field);
		if ($.trim(_getFieldVal(jqField)).length == 0) {
			bBlank = true;
		}
		return bBlank;
	}

	function _isAnInteger(val) {
		var intRegex = /^\d+$/;
		return intRegex.test(val);
	}

	function _isValidDate(dateStr) {
		var currVal = dateStr;
		if (currVal == '') return false;
		// Checks for validate date in m/d/yyyy format or m-d-yyyy format
		var bIsValid = false;
		var validFormats = ["MM/DD/YYYY", "M/D/YYYY", "MM/D/YYYY", "M/DD/YYYY"];
		validFormats.push("MM-DD-YYYY", "M-D-YYYY", "MM-D-YYYY", "M-DD-YYYY");
		$.each(validFormats, function(index, formatStr) {
			var inputDate = moment(dateStr, formatStr, true);
			if (inputDate.isValid()) {
				bIsValid = true;
				return false;
			}
		});
		return bIsValid;
	}

	// ******************************************* //
	// *** SHOW/HIDE ERRORS ********************** //
	// ******************************************* //

	function _showScreenError(errorHTML, jqContext) {
		var func = _errorHandlers["screenError"];
		if ($.isFunction(func)) {
			func(errorHTML, jqContext);
		} else {
			if (jqContext == null) jqContext = _jqDefaultContext;
			var jqErrorElem = $('.error-message', jqContext);
			jqErrorElem.html(errorHTML).show();
		}
	}

	function _showScreenErrors(errorStrArray, jqContext) {
		var func = _errorHandlers["screenErrors"];
		if ($.isFunction(func)) {
			func(errorStrArray, jqContext);
		} else {
			if (errorStrArray.length > 0) {
				var errorHTML = "";
				if (errorStrArray.length == 1) {
					errorHTML = errorStrArray[0];
				} else {
					errorHTML = '<span align="left">';
					for (var i = 0; i < errorStrArray.length; i++) {
						errorHTML += "<li>" + errorStrArray[i] + "</li>";
					}
					errorHTML += '</span>';
				}
				_showScreenError(errorHTML, jqContext);
			}
		}
	}

	function _showScreenSuccess(successHTML, jqContext) {
		var func = _errorHandlers["screenSuccess"];
		if ($.isFunction(func)) {
			func(successHTML, jqContext);
		} else {
			if (jqContext == null) jqContext = _jqDefaultContext;
			var jqSuccessElem = $('.success-message', jqContext);
			jqSuccessElem.html(successHTML).show();
		}
	}

	function _showFieldError(field, errorClass, errorMessage, jqContext) {
		var func = _errorHandlers["fieldError"];
		if ($.isFunction(func)) {
			func(field, errorClass, null, jqContext);
		} else {
			if (jqContext == null) jqContext = _jqDefaultContext;
			var fieldId = ($.type(field) === "string") ? field : $(field).attr('id');
			if (fieldId != null) {
				var classArray = ["error", fieldId];
				if (errorClass != null) classArray.push(errorClass);
				var errorSelector = "."+classArray.join(".");
				var jqError = $(errorSelector, jqContext);
				if (errorMessage != null) jqError.html(errorMessage);
				jqError.show();
			}
		}
	}

	function _hideAllMessages(jqContext) {
		var func = _errorHandlers["hide"];
		if ($.isFunction(func)) {
			func(jqContext);
		} else {
			if (jqContext == null) jqContext = _jqDefaultContext;
			$('.success-message, .error-message, .error', jqContext).hide();
			$('.duplicateEntry', jqContext).each(function() {
				$(this).removeClass('duplicateEntry');
			});
		}
	}

	function _getFieldVal(jqField) {
		var val;
		if (_fieldValueFunc != null) {
			val = _fieldValueFunc(jqField);
		} else {
			val = jqField.val();
		}
		return val;
	}

	// Public methods go here
	return {
		validateFields: _validateFields,
		isAnInteger: _isAnInteger,
		isValidDate: _isValidDate,
		isFieldBlank: _isFieldBlank,
		showScreenError: _showScreenError,
		showScreenErrors: _showScreenErrors,
		showScreenSuccess: _showScreenSuccess,
		showFieldError: _showFieldError,
		hideAllMessages: _hideAllMessages
	}
}
