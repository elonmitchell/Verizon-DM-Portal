function TextareaValidator(_jqTextareaElem, _typeValidator) {
	"use strict";

	_init();

	function _init() {
	}

	function _isKeypressValid(keypressEvent, errorStrArray) {
		// Get the keycode for the new char item
		var newCharCode = _getCharCode(keypressEvent);

		// Allow copy, cut, and paste
		if (TextareaCopyPasteListener.isCharCodeCopyOrPaste(newCharCode)) return true;

		// Allow navigation keypress, e.g. left, right, up, down, delete, etc
		if (_isNavCharCode(newCharCode)) return true;

		// Validate
		var bValidKeypress = false;
		if (_typeValidator.isValidCharCode(newCharCode)) {
			// Valid char code -> Verify the new line length is OK
			var newChar = String.fromCharCode(newCharCode);
			var currentLine = _getCurrentLine(newChar);
			if (!_typeValidator.isOverMaxLength(currentLine)) {
				bValidKeypress = true;
			}
		} else if (_isNewlineChar(newCharCode)) {
			// Newline char -> Check line length of previous line
			var previousLine = _getPreviousLine();
			if (_typeValidator.isTextValid(previousLine, errorStrArray)) {
				// Check for duplicates
				if (!_typeValidator.allowDuplicates() && CommonUtil.hasDuplicates(_jqTextareaElem, previousLine)) {
					if (errorStrArray != null) errorStrArray.push(ERROR_LAST_LINE_DUPLICATE);
				} else {
					bValidKeypress = true; // allow newline character
				}
			} else {
				bValidKeypress = false; // previous line length is not correct -> don't allow newline character
			}
		}
		return bValidKeypress;
	}

	function _isElemValid(errorStrArray) {
		return _typeValidator.isTextValid(_jqTextareaElem.val(), errorStrArray);
	}

	function _sanitizeText(errorStrArray) {
		// Get the number of lines
		var textElemVal = _jqTextareaElem.val();
		var lineArray = textElemVal.split("\n");
		var numLines = lineArray.length;
		if (numLines > 1) {
			// Only sanitize when there is a least one complete line
			var bCheckForDuplicates = !_typeValidator.allowDuplicates();
			var lineHash = {};
			var numLinesToSanitize = numLines - 1;
			var duplicateFound = false;
			var lineLengthErrorFound = false;
			var lineLengthErrorArray = [];
			var charErrorFound = false;
			var sanitizedLineArray = [];
			// Work line by line
			for (var i = 0; i < numLinesToSanitize; i++) {
				var lineStr = lineArray[i];
				lineStr = lineStr.replace("\r", ""); // Strip carriage return characters
				// Look for character errors
				var lineLength = lineStr.length;
				var charErrorFoundForThisLine = false;
				for (var j = 0; j < lineLength; j++) {
					var newChar = lineStr.charAt(j);
					var newCharCode = newChar.charCodeAt(0);
					if (!_typeValidator.isValidCharCode(newCharCode)) {
						charErrorFound = true;
						charErrorFoundForThisLine = true;
						break;
					}
				}
				if (!charErrorFoundForThisLine && lineLength > 0) {
					// Look for line length errors
					if (!_typeValidator.isValidLength(lineStr, lineLengthErrorArray)) { // bCheckMinLen = true
						lineLengthErrorFound = true;
					} else {
						// Look for duplicate values
						if (bCheckForDuplicates && lineHash[lineStr] != null) {
							duplicateFound = true;
						} else {
							// Everything checks out -> Add line
							sanitizedLineArray.push(lineStr);
							if (bCheckForDuplicates) lineHash[lineStr] = true;
						}
					}
				}
			}

			// Add last line
			sanitizedLineArray.push(lineArray[numLinesToSanitize]);
			var sanitizedVal = sanitizedLineArray.join("\n");

			// Update error str array
			if (errorStrArray != null) {
				if (charErrorFound)       errorStrArray.push(ERROR_PASTE_INVALID_CHARS);
				if (lineLengthErrorFound) errorStrArray.push(ERROR_PASTE_LENGTH + " " + lineLengthErrorArray[0]);
				if (duplicateFound)       errorStrArray.push(ERROR_PASTE_DUPLICATE_FOUND);
			}

			// Update text in text elem
			_jqTextareaElem.val(sanitizedVal);
		}
	}

	// ******************************************* //
	// ** LINE ACCESSOR METHODS ****************** //
	// ******************************************* //

	function _getCurrentLine(newChar) {
		var el = _jqTextareaElem[0];
		var caretPos = _getCaret(el);
		var elVal = el.value;
		if (newChar != null) {
			var preVal = elVal.substring(0, caretPos);
			var postVal = elVal.substring(caretPos, elVal.length);
			elVal = preVal + newChar + postVal;
		}

		var start, end;
		//alert(elVal.charCodeAt(caretPos));
		for (start = caretPos; start >= 0 && (elVal.charCodeAt(start) > 20); --start) {}   // Find start of the current line
		for (end = caretPos; end < elVal.length && (elVal.charCodeAt(end) > 20); ++end) {} // Find end of the current line
		start++;
		//alert(caretPos + ":" + start + " - " + end);
		return elVal.substring(start, end);
	}
	function _getPreviousLine() {
		var el = _jqTextareaElem[0];
		var previousLine = null;
		var caretPos = _getCaret(el);
		var elVal = el.value;
		var startCurrent;
		for (startCurrent = caretPos; startCurrent >= 0 && (elVal.charCodeAt(startCurrent) > 20); --startCurrent) {} // Find start of current line
		if (startCurrent > 0) {
			var startPrevious, endPrevious;
			for (endPrevious = startCurrent-1; endPrevious >= 0 && (elVal.charCodeAt(endPrevious) < 20); --endPrevious) {} // Find end of previous line
			endPrevious++;
			for (startPrevious = endPrevious-1; startPrevious >= 0 && (elVal.charCodeAt(startPrevious) > 20); --startPrevious) {} // Find start of this previous line
			startPrevious++;
			//alert(caretPos + ":" + start + " - " + end);
			previousLine = elVal.substring(startPrevious, endPrevious);
		}
		return previousLine;
	}
	function _getCaret(el) {
		var caretPos = 0;
		if (el.selectionStart) {
			caretPos = el.selectionStart;
		} else if (document.selection) {
			var sel = _getInputSelection(el);
			caretPos = sel.end;
		}
		return caretPos;
	}
	function _getInputSelection(el) {
		var start = 0, end = 0, normalizedValue, range, textInputRange, len, endRange;

		if (typeof el.selectionStart == "number" && typeof el.selectionEnd == "number") {
			start = el.selectionStart;
			end = el.selectionEnd;
		} else {
			range = document.selection.createRange();

			if (range && range.parentElement() == el) {
				len = el.value.length;
				normalizedValue = el.value.replace(/\r\n/g, "\n");

				// Create a working TextRange that lives only in the input
				textInputRange = el.createTextRange();
				textInputRange.moveToBookmark(range.getBookmark());

				// Check if the start and end of the selection are at the very end
				// of the input, since moveStart/moveEnd doesn't return what we want
				// in those cases
				endRange = el.createTextRange();
				endRange.collapse(false);

				if (textInputRange.compareEndPoints("StartToEnd", endRange) > -1) {
					start = end = len;
				} else {
					start = -textInputRange.moveStart("character", -len);
					start += normalizedValue.slice(0, start).split("\n").length - 1;

					if (textInputRange.compareEndPoints("EndToEnd", endRange) > -1) {
						end = len;
					} else {
						end = -textInputRange.moveEnd("character", -len);
						end += normalizedValue.slice(0, end).split("\n").length - 1;
					}
				}
			}
		}

		return { start: start, end: end };
	}

	// ******************************************* //
	// *** UTILITY FUNCTIONS ********************* //
	// ******************************************* //

	function _getCharCode(newCharItem) {
		// newCharItem is either a keycode or keypress event...get the keycode
		if ($.type(newCharItem) === "number") { // keycode
			return newCharItem;
		} else { // event object
			var evt = newCharItem;
			return (evt.which) ? evt.which : evt.keyCode;
		}
	}

	function _isNewlineChar(charCode) {
		return (charCode <= CHARCODE_NEWLINE);
	}

	function _isNavCharCode(charCode) {
		return ($.inArray(charCode, _navCharCodeArray) >= 0);
	}

	var CHARCODE_NEWLINE = 31;
	var CHARCODE_BACKSPACE = 8;
	var CHARCODE_PAGE_UP = 33;
	var CHARCODE_PAGE_DOWN = 34;
	var CHARCODE_END = 35;
	var CHARCODE_HOME = 36;
	var CHARCODE_LEFT = 37;
	var CHARCODE_RIGHT = 39;
	var CHARCODE_UP = 38;
	var CHARCODE_DOWN = 40;
	var CHARCODE_DELETE = 46;

	var _navCharCodeArray = [CHARCODE_BACKSPACE,
		CHARCODE_PAGE_UP, CHARCODE_PAGE_DOWN,
		CHARCODE_END, CHARCODE_HOME,
		CHARCODE_LEFT, CHARCODE_RIGHT, CHARCODE_UP, CHARCODE_DOWN,
		CHARCODE_DELETE];

	var ERROR_LAST_LINE_DUPLICATE = "The last line you entered is a duplicate.";
	var ERROR_PASTE_INVALID_CHARS = "Invalid characters found in the entries you pasted.";
	var ERROR_PASTE_LENGTH = "One or more of the entries you pasted had improper character length.";
	var ERROR_PASTE_DUPLICATE_FOUND = "One or more of the entries you pasted contained duplicates.";

	// ******************************************* //
	// *** PUBLIC METHODS ************************ //
	// ******************************************* //

	return {
		isKeypressValid: _isKeypressValid,
		isElemValid: _isElemValid,
		sanitizeText: _sanitizeText
	};

}

var TextareaCopyPasteListener = (function() {
	var _ctrlDown;
	var _copyPasteCharCodes;

	_init();

	function _init() {
		_ctrlDown = false;

		var CHARCODE_CTRL_KEY = 17;
		var CHARCODE_UPPER_C = 67;
		var CHARCODE_UPPER_V = 86;
		var CHARCODE_UPPER_X = 88;
		var CHARCODE_LOWER_C = 99;
		var CHARCODE_LOWER_V = 118;
		var CHARCODE_LOWER_X = 120;
		_copyPasteCharCodes = [CHARCODE_LOWER_C, CHARCODE_UPPER_C, CHARCODE_LOWER_X, CHARCODE_UPPER_X, CHARCODE_LOWER_V, CHARCODE_UPPER_V];

		$(function () {
			// Watch for ctrl keydown/keyup
			$(document).keydown(function (e) {
				if (e.keyCode == CHARCODE_CTRL_KEY) _ctrlDown = true;
			}).keyup(function (e) {
				if (e.keyCode == CHARCODE_CTRL_KEY) _ctrlDown = false;
			});
		});
	}

	function _isCharCodeCopyOrPaste(charCode) {
		return (_ctrlDown == true && $.inArray(charCode, _copyPasteCharCodes) >= 0);
	}

	// ******************************************* //
	// *** PUBLIC METHODS ************************ //
	// ******************************************* //

	return {
		isCharCodeCopyOrPaste: _isCharCodeCopyOrPaste
	};

})();
