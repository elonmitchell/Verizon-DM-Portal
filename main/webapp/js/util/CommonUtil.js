/* global console, alert, PopupManager, Ractive, Cookies */
var CommonUtil = (function () {
	"use strict";

	var _jqClockBox;
	var _DEBUG_MODE = false;

	var DEBUG_FLAGS = {};

	_init();

	function _init() {
		// Define DEBUG values
		DEBUG_FLAGS = {};
		var debugFlagNames = ["POPUP_SCREEN_RACTIVE"];
		$.each(debugFlagNames, function(idx, debugFlagName) {
			DEBUG_FLAGS[debugFlagName] = (Cookies.get('DEBUG_'+debugFlagName) === "true");
		});

		// Define console.log if necessary (stupid IE)
		if (typeof console === "undefined" || typeof console.log === "undefined") {
			window.console = {};
			window.console.log = function() {};
		}
		// Avoid caching of AJAX requests, add url to ajax requests
		jQuery.ajaxSetup({
			cache: false,
			beforeSend: function(jqXHR, settings) {
				jqXHR.url = settings.url; // store url for better error handling
			}
		});

		$(document).ready(function() {
			_docReady();
		});
	}

	function _docReady() {
		// Allow unset
		Ractive.prototype.unset = function(keypath){
			var parent = '',
					property = keypath,
					lastDot = keypath.lastIndexOf( '.' );

			if(lastDot!==-1){
				parent = keypath.substr( 0, lastDot );
				property = keypath.substring( lastDot + 1 );
			}

			this.set(keypath);
			if (this.get(parent) != null) delete this.get(parent)[property];
			return this.update(keypath);
		};
	}

	// ******************************************* //
	// *** STRING FUNCTIONS ********************** //
	// ******************************************* //

	function _hasDuplicates(text, comparisonLine) {
		var duplicateFound = false;

		var val = ($.type(text) === "string") ? text : text.val();
		var lineArray = val.split("\n");
		var lineHash = {};
		for (var i = 0; i < lineArray.length; i++) {
			var lineStr = lineArray[i];
			lineStr = lineStr.replace("\r", ""); // Strip carriage return characters
			if (lineHash[lineStr] == null) {
				// Unique line (so far)
				lineHash[lineStr] = 1;
			} else {
				// Duplicate found
				lineHash[lineStr] = lineHash[lineStr] + 1;
				duplicateFound = true;
			}
		}

		if (comparisonLine != null) { // compare only the input line
			duplicateFound = (lineHash[comparisonLine] > 1) ? true : false;
		}

		return duplicateFound;
	}

	// ******************************************* //
	// *** AJAX FUNCTIONS ************************ //
	// ******************************************* //

	function _beforeAjaxSend(jqXHR, settings) {
		if (g_SessionId === "") {
			// Local development, update URL for .do requests
			if (settings.url.indexOf(".do") >= 0) {
				settings.url = "sample_do_files/" + settings.url;
			}
		}
		jqXHR.url = settings.url; // store url for better error handling
	}

	var uniqueLoginPageText = "<h1>Login</h1>";
	function _onAjaxFail(jqXHR, textStatus, errorThrown) {
		var errorContent = null;
		if (jqXHR != null && jqXHR.responseText != null && jqXHR.responseText.indexOf(uniqueLoginPageText) !== -1 ) {
			//Session Time out reload the page to go to login.
			window.location.reload();
		} else if (textStatus === "parsererror") {
			var errorStr = "Parse error";
			if (jqXHR.url != null) {
				errorStr = "Parse error in " + jqXHR.url;
				if (jqXHR.responseText != null && jqXHR.responseText.indexOf(uniqueLoginPageText) !== -1) {
					window.location.reload();
				}
			}
			alert(errorStr);
		} else if (errorThrown.indexOf("login") !== -1) {
			window.location.reload();
		} else if (jqXHR != null) {
			errorContent = "<p style=\"text-align: left\">An error occured when trying to access <span style='font-family: \"Courier New\", Courier, monospace\'>" + jqXHR["url"] + "</span></p>\n";
			errorContent += "<p style=\"text-align: left; padding-left: 20px\">";
			var keys = ["status", "statusText"];
			$.each(keys, function(idx, key) {
				if (jqXHR.hasOwnProperty(key)) {
					errorContent += "<span style='font-family: \"Courier New\", Courier, monospace\'>" + key + ": " + jqXHR[key] + "</span><br/>\n";
				}
			});
			errorContent += "</p>";
		}
		if (errorContent != null) {
			if (jqXHR["url"].indexOf("global-error") >= 0) {
				var str = "Server is not accessible (restarting?)";
				if (_DEBUG_MODE) {
					alert(str);
				} else {
					console.log(str);
				}
			} else {
				if (_DEBUG_MODE) {
					PopupManager.showGlobalError(errorContent);
				} else {
					// Strip tags
					try {
						var div = document.createElement("div");
						div.innerHTML = errorContent;
						errorContent = div.textContent || div.innerText || "";
					} catch(ex) {
					}
					// Show in console
					console.log(errorContent);
				}
			}
		}
	}

	// ******************************************* //
	// *** MAX CONTAINER FUNCTIONS *************** //
	// ******************************************* //

	function _addMaxSizePopupContainer(jqContentDiv) {
		var windowWidth = parseInt($(window).width());
		var maxWidth = parseInt(windowWidth*0.85);
		jqContentDiv.prepend('<div class="max-size-container"></div>');
		var jqMaxSizeContainer = $('.max-size-container', jqContentDiv);
		jqMaxSizeContainer.css("width", maxWidth+"px"); // set width
		return jqMaxSizeContainer;
	}

	function _setMaxHeight(screen, jqMaxSizeContainer) {
		var windowHeight = parseInt($(window).height());
		var maxHeight = windowHeight - screen.getScreenDiv().height() - 280;
		jqMaxSizeContainer.css("height", maxHeight);
	}

	// ******************************************* //
	// *** CLOCK FUNCTIONS *********************** //
	// ******************************************* //

	var _tday = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
	var _tmonth = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

	//Auto Refresh Timer
	function _startTopClock() {
		_jqClockBox = $("#clockbox");
		if (_jqClockBox.length > 0) _updateTopClock();
	}
	function _updateTopClock() {
    var d = new Date();
    var nday = d.getDay();
    var nmonth = d.getMonth();
    var ndate = d.getDate();
    var nyear = d.getYear();
    var nhour = d.getHours();
    var nmin = d.getMinutes();
    var nsec = d.getSeconds();
		var ap = " AM";
    if (nyear < 1000) nyear = nyear + 1900;
    if (nhour === 0) {
      nhour = 12;
    } else if (nhour >= 12) {
      ap = " PM";
			if (nhour >= 13) nhour -= 12;
    }
    if (nmin < 10) nmin = "0" + nmin;
    if (nsec < 10) nsec = "0" + nsec;
    var timeTodisplay = _tday[nday] + ", " + _tmonth[nmonth] + " " + ndate + ", " + nyear + " " + nhour + ":" + nmin + ":" + nsec + ap + "";
    _jqClockBox.text(timeTodisplay);
    setTimeout(_updateTopClock, 1000); //"GetClock()"
	}

	// ******************************************* //
	// *** YEAR FUNCTIONS *********************** //
	// ******************************************* //

	function _setCopyrightYear() {
		$(".footer #spanYear").text(new Date().getFullYear());
	}

	// ******************************************* //
	// *** BROWSER DETECTION FUNCTIONS *********** //
	// ******************************************* //

	function _detectIE() {
		var ua = window.navigator.userAgent;

		// Test values; Uncomment to check result …

		// IE 10
		// ua = 'Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0)';

		// IE 11
		// ua = 'Mozilla/5.0 (Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko';

		// IE 12 / Spartan
		// ua = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36 Edge/12.0';

		// Edge (IE 12+)
		// ua = 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586';

		var msie = ua.indexOf('MSIE ');
		if (msie > 0) {
			// IE 10 or older => return version number
			return parseInt(ua.substring(msie + 5, ua.indexOf('.', msie)), 10);
		}

		var trident = ua.indexOf('Trident/');
		if (trident > 0) {
			// IE 11 => return version number
			var rv = ua.indexOf('rv:');
			return parseInt(ua.substring(rv + 3, ua.indexOf('.', rv)), 10);
		}

		var edge = ua.indexOf('Edge/');
		if (edge > 0) {
			// Edge (IE 12+) => return version number
			return parseInt(ua.substring(edge + 5, ua.indexOf('.', edge)), 10);
		}

		// other browser
		return false;
	}

	// ******************************************* //
	// *** DOCUMENT READY FUNCTION *************** //
	// ******************************************* //

	jQuery(document).ready(function () {
		_startTopClock();
		_setCopyrightYear();
	});

	// ******************************************* //
	// *** PUBLIC METHODS ************************ //
	// ******************************************* //

	// Public methods go here (none)
	return {
		hasDuplicates: _hasDuplicates,
		beforeAjaxSend: _beforeAjaxSend,
		onAjaxFail: _onAjaxFail,
		detectIE: _detectIE,
		addMaxSizePopupContainer: _addMaxSizePopupContainer,
		setMaxHeight: _setMaxHeight,
		DEBUG_FLAGS: DEBUG_FLAGS
	};

})();
