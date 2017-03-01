/* global g_sessionTimeoutMsg, Cookies */
var LoginManager = (function () {
	"use strict";

	var _validInputLength;
	var _jqUsernameInput, _jqAllFormErrors, _jqUsernameError, _jqUsernameInstruction, _jqPasswordInput, _jqPasswordError, _jqPasswordInstruction, _jqLoginError, _jqSubmitButton, _jqLoginForm;

	function _init() {
		_validInputLength = 2;
		_jqUsernameInput = $('#userName');
		if (_jqUsernameInput.length > 0) {
			_jqAllFormErrors = $('.form-error');
			_jqUsernameError = $('#user_error');
			_jqUsernameInstruction = $('#user_instruction');
			_jqPasswordInput = $('#password');
			_jqPasswordError = $('#pass_error');
			_jqPasswordInstruction = $('#pass_instruction');
			_jqLoginError = $('#login_error');
			_jqSubmitButton = $('#submit-btn');
			_jqLoginForm = $("#login-form");

			_removeCredentials();

			if (parent.document.getElementById("dialogFrame") != null) {
				alert(g_sessionTimeoutMsg);
				parent.document.location = parent.document.location;
				return;
			}

			// Put their cursor in the login field
			_jqUsernameInput.focus();

			// Set cookies
			_setCookies();
		}

		// Set copyright end year
		_setCopyrightEndYear();
	}

	function _removeCredentials() {
		_jqUsernameInput.val("");
		_jqPasswordInput.val("");
	}

	function _onSubmitForm(){
		// Reset
		_jqAllFormErrors.hide();
		// Validate
		var bUsernameValid = _validateUsername();
		var bPasswordValid = _validatePassword();
		var bIsValid =  bUsernameValid && bPasswordValid;
		return bIsValid;
	}

	function _validateUsername() {
		var bIsValid = true;
		if (_isElemBlank(_jqUsernameInput)) {
			_jqUsernameInstruction.show();
			bIsValid = false;
		}
		return bIsValid;
	}

	function _validatePassword() {
		var bIsValid = true;
		if (_isElemBlank(_jqPasswordInput)) {
			_jqPasswordInstruction.show();
			bIsValid = false;
		}
		return bIsValid;
	}

	function _isElemBlank(jqElem) {
		return ($.trim(jqElem.val()) === "");
	}

	function _setCookies() {
		// No idea why we need these calls but they were there before -tw3
		Cookies.set("report_context_help_showing", "false");
		_saveDialogLocation("-1", "-1");
	}

	function _saveDialogLocation(left, top) {
		var futureDate = new Date("December 31, 2023");
		var cookieVal = left.toString() + "," + top.toString();
		Cookies.set("report_help_location", cookieVal, { expires: futureDate });
	}

	function _setCopyrightEndYear() {
		$("#copyrightEndYear").html(new Date().getFullYear());
	}

	// ******************************************* //
	// *** DOCUMENT READY INIT ******************* //
	// ******************************************* //

	$(document).ready(function() {
		_init();
	});

	// ******************************************* //
	// *** PUBLIC FUNCTIONS ********************** //
	// ******************************************* //

	return {
		onSubmitForm: _onSubmitForm
	};

})();
