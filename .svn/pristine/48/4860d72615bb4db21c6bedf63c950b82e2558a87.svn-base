/* global jQuery */
var DatePicker = (
function () {
	var _datePickerOptions;
	var _datePickerArray;

	_init();

	function _init() {
		// Specify date picker options
		_datePickerOptions = {
			minDate: new Date()
		};

		// Initialize date picker array
		_datePickerArray = new Array();

		// Add date pickers
		jQuery(document).ready(function () {
			_addDatePickers();
		});
	}

	function _addDatePickers(jqContext) {
		if (jqContext == null) jqContext = jQuery('body');
		jQuery('.date-picker', jqContext).each(function() {
			var jqDatePicker = jQuery(this);
			_datePickerArray.push(jqDatePicker);
			jqDatePicker.datepicker(_datePickerOptions);
		});
	}

	// Hide all date pickers when window is resized
	jQuery(window).resize(function() {
		if (_datePickerArray != null) {
			for (var i = (_datePickerArray.length-1); i >= 0; i--) {
				var jqDatePicker = _datePickerArray[i];
				if (jqDatePicker.length == 0) {
					// Date picker no longer exists -> remove it from array
					_datePickerArray.splice(i, 1);
				} else {
					jqDatePicker.datepicker('hide');
				}
			}
		}
	});

	// ******************************************* //
	// *** PUBLIC METHODS ************************ //
	// ******************************************* //

	return {
		addDatePickers: _addDatePickers
	}

})();
