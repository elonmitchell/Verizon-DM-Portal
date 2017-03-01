function ScreenValidator(errorHandlers, jqContext) {
	"use strict";

	var _jqDefaultContext = $('body');
	var _errorHandlers = {};

	_init(errorHandlers, jqContext);

	function _init(errorHandlers, jqContext) {
		if (errorHandlers != null) _errorHandlers = errorHandlers;
		if (jqContext != null) _jqDefaultContext = jqContext;
	}

	// Deprecated...use validateFields instead
	function _checkBlankFields(blankFieldCheckIds) {
		var errorsFound = false;
		$('.help-block').hide();
		for (var i = 0; i < blankFieldCheckIds.length; i++) {
			var blankFieldCheckId = blankFieldCheckIds[i];
			var fieldSelector = "#" + blankFieldCheckId;
			var errorSelector = ".help-block.required." + blankFieldCheckId;
			if (_isElemBlank(fieldSelector)) {
				// Show error
				$(errorSelector).show();
				// Signal error
				errorsFound = true;
			}
		}
		return errorsFound;
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
					var radioName = jqField.attr("data-name"); // for some reason ractive chokes on "name" attributes for radio and checkbox inputs so we use "data-name" instead
					if (radioName != null) radioNamesHash[radioName] = true;
				} else if (jqField.is(':checkbox')) {
					var cbName = jqField.attr("data-name"); // for some reason ractive chokes on "name" attributes for radio and checkbox inputs so we use "data-name" instead
					if (cbName != null) cbNamesHash[cbName] = true;
				} else {
					if (_isElemBlank(jqField)) {
						_showFieldError(jqField, "required", null, jqContext);
						bIsValid = false;
					}
				}
			} catch(err) {
			}
		});
		$.each(radioNamesHash, function(radioName, val) {
			if (jqFields.filter('[data-name='+radioName+']:checked').length == 0) { // for some reason ractive chokes on "name" attributes for radio and checkbox inputs so we use "data-name" instead
				bIsValid = false;
				_showFieldError(radioName, "required", null, jqContext);
			}
		});
		$.each(cbNamesHash, function(cbName, val) {
			if (jqFields.filter('[data-name='+cbName+']:checked').length == 0) { // for some reason ractive chokes on "name" attributes for radio and checkbox inputs so we use "data-name" instead
				bIsValid = false;
				_showFieldError(cbName, "required", null, jqContext);
			}
		});
		if (!bIsValid) return bIsValid;

		// Exact size
		jqFields.filter('[data-valsize]').each(function() {
			try {
				var jqField = $(this);
				if (!_isElemBlank(jqField)) {
					var valSizeArray = [];
					var valStr = jqField.attr("data-valsize");
					if (valStr.indexOf(',') >= 0) { // array of integers
						valSizeArray = valStr.split(',');
					} else { // integer
						valSizeArray.push(valStr);
					}
					var bLenMatches = false;
					var len = jqField.val().length;
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
		jqFields.filter('[data-valminsize]').each(function() {
			try {
				var jqField = $(this);
				var valMinSize = jqField.attr("data-valminsize") * 1;
				if (jqField.val().length < valMinSize) {
					_showFieldError(jqField, "valminsize", null, jqContext);
					bIsValid = false;
				}
			} catch(err) {
			}
		});
		if (!bIsValid) return bIsValid;

		// Regular expression
		jqFields.filter('[data-valregexp]').each(function() {
			try {
				var jqField = $(this);
				var valRegexp = jqField.attr("data-valregexp");
				var reObj = new RegExp(valRegexp);
				var val = jqField.val();
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
				var val = jqField.val();
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
				var val = jqField.val();
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
				var val = jqField.val();
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
				var val = $.trim(jqField.val());
				if (!reObj.test(val)) {
					_showFieldError(jqField, "checkurl", null, jqContext);
					bIsValid = false;
				}
			} catch(err) {
			}
		});
		if (!bIsValid) return bIsValid;

		// Duplicates
		jqFields.filter('[uniquekey]').each(function(i, elem1) {
			var jqField1 = $(this);
			var val1 = jqField1.val();
			var uniquekey1 = jqField1.attr("uniquekey");
			if (val1 != "") {
				jqFields.not(jqField1).filter('[uniquekey="'+uniquekey1+'"]').each(function(j, elem2) {
					var jqField2 = $(this);
					var val2 = jqField2.val();
					if (val1 == val2) {
						bIsValid = false; // duplicate found
						jqField1.addClass('duplicateEntry');
						jqField2.addClass('duplicateEntry');
						_showScreenError("Duplicate values found", jqContext);
						return false; // exit inner loop
					}
				});
			}
			if (!bIsValid) return false; // exit inner loop
		});
		if (!bIsValid) return bIsValid;

		return bIsValid;
	}
	function _isElemBlank(elem) {
		var bBlank = false;
		var jqElem = $(elem);
		if ($.trim(jqElem.val()).length == 0) {
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
		var rxDatePattern = /^(\d{1,2})(\/|-)(\d{1,2})(\/|-)(\d{4})$/;
		var dtArray = currVal.match(rxDatePattern);
		if (dtArray == null) return false;
		// Checks for mm/dd/yyyy format.
		var dtMonth = dtArray[1];
		var dtDay= dtArray[3];
		var dtYear = dtArray[5];
		if (dtMonth < 1 || dtMonth > 12) return false;
		else if (dtDay < 1 || dtDay> 31) return false;
		else if ((dtMonth==4 || dtMonth==6 || dtMonth==9 || dtMonth==11) && dtDay ==31) return false;
		else if (dtMonth == 2) {
			var isleap = (dtYear % 4 == 0 && (dtYear % 100 != 0 || dtYear % 400 == 0));
			if (dtDay> 29 || (dtDay ==29 && !isleap)) return false;
		}
		return true;
	}

	// ******************************************* //
	// *** SHOW/HIDE ERRORS ********************** //
	// ******************************************* //

	function _showScreenError(errorHTML, jqContext, bAllowClose) {
		var func = _errorHandlers["screenError"];
		if ($.isFunction(func)) {
			func(errorHTML, jqContext, bAllowClose);
		} else {
			if (jqContext == null) jqContext = _jqDefaultContext;
			if (bAllowClose == null) bAllowClose = true;
			var jqAlertsArea = $('.alerts-area', jqContext);
			var classArray = ["alert", "alert-danger"];
			if (bAllowClose) classArray.push("alert-dismissable");
			var newDiv = $(document.createElement('div')).attr("class", classArray.join(" "));
			if (bAllowClose) newDiv.append('<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>');
			newDiv.append('<span>'+errorHTML+'</span>');
			jqAlertsArea.html(newDiv).show();
		}
	}

	function _showScreenErrors(errorStrArray, jqContext, bAllowClose) {
		var func = _errorHandlers["screenErrors"];
		if ($.isFunction(func)) {
			func(errorStrArray, jqContext, bAllowClose);
		} else {
			if (errorStrArray.length > 0) {
				if (jqContext == null) jqContext = _jqDefaultContext;
				if (bAllowClose == null) bAllowClose = true;
				var errorHTML = "";
				if (errorStrArray.length == 1) {
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
	}

	function _showScreenSuccess(successHTML, jqContext, bAllowClose) {
		var func = _errorHandlers["screenSuccess"];
		if ($.isFunction(func)) {
			func(successHTML, jqContext, bAllowClose);
		} else {
			if (jqContext == null) jqContext = _jqDefaultContext;
			if (bAllowClose == null) bAllowClose = true;
			var jqAlertsArea = $('.alerts-area', jqContext);
			var classArray = ["alert", "alert-success"];
			if (bAllowClose) classArray.push("alert-dismissable");
			var newDiv = $(document.createElement('div')).attr("class", classArray.join(" "));
			if (bAllowClose) newDiv.append('<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>');
			newDiv.append('<span>'+successHTML+'</span>');
			jqAlertsArea.html(newDiv).show();
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
				var classArray = ["help-block", fieldId];
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
			$('.alerts-area, .help-block', jqContext).hide();
			$('.duplicateEntry', jqContext).each(function() {
				$(this).removeClass('duplicateEntry');
			});
			$('.alert').alert("close");
		}
	}

	// ******************************************* //
	// *** PUBLIC FUNCTIONS ********************** //
	// ******************************************* //

	return {
		checkBlankFields: _checkBlankFields,
		validateFields: _validateFields,
		isAnInteger: _isAnInteger,
		isElemBlank: _isElemBlank,
		showScreenError: _showScreenError,
		showScreenErrors: _showScreenErrors,
		showScreenSuccess: _showScreenSuccess,
		hideAllMessages: _hideAllMessages
	};
}