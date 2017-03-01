function PopupScreenRadioGroupHandler(screen) {
	var _screen;
	var _jqScreenDiv;

	_init(screen);

	function _init(screen) {
		_screen = screen;
	}

	function _updateScreen() {
		_jqScreenDiv = _screen.getScreenDiv();
		if ($('[tieToRadioId]', _jqScreenDiv).length > 0) {
			// Tied elements exist -> Loop over radio buttons on the screen
			var jqRadioButtons = $('input[type=radio]', _jqScreenDiv);
			$.each(jqRadioButtons, function() {
				var jqRadioButton = $(this);
				// Add change event handler
				jqRadioButton.change(function() {
					screen.hideAllMessages();
					_updateRadioGroup(jqRadioButton);
				});
				// Make initial update
				_updateRadioGroup(jqRadioButton);
			});
		}
	}

	function _updateRadioGroup(jqRadioButton) {
		// Enable/disable elements tied to the input radio button
		var radioId = jqRadioButton.attr('id');
		var bRadioChecked = jqRadioButton.is(':checked');
		var bUpdated = _enableDisableTiedElements(radioId, bRadioChecked);
		if (bUpdated) {
			// Work on other elements tied to radio buttons with the same name
			var radioName = jqRadioButton.attr('name');
			$('input[type=radio][name='+radioName+']', _jqScreenDiv).not(jqRadioButton).each(function() {
				var jqRadioButton2 = $(this);
				var radioId2 = jqRadioButton2.attr('id');
				if (radioId2 != null && radioId2 != "") {
					_enableDisableTiedElements(radioId2, !bRadioChecked);
				}
			});
		}
	}

	function _enableDisableTiedElements(radioId, bEnable) {
		var bUpdated = false;
		var jqTiedElements = $('[tieToRadioId='+radioId+']',_jqScreenDiv);
		jqTiedElements.each(function() {
			var jqTiedElement = $(this);
			if (bEnable) {
				_screen.enableFields([jqTiedElement], true);
				jqTiedElement.removeAttr("radioGroupDisabled");
			} else {
				_screen.disableFields([jqTiedElement], true, true);
				jqTiedElement.attr("radioGroupDisabled", "true");
			}
			bUpdated = true;
		});
		return bUpdated;
	}

	function _filterActiveFields(jqFields) {
		var jqActiveFields = jqFields.filter(':not([radioGroupDisabled])');
		return jqActiveFields;
	}

	// ******************************************* //
	// *** PUBLIC METHODS ************************ //
	// ******************************************* //

	return {
		updateScreen: _updateScreen,
		filterActiveFields: _filterActiveFields
	}

}
