/* global */
var $j = jQuery.noConflict();

var ReportTemplatesManager = (function () {
	"use strict";

	function _init() {
		// Set copyright end year
		_initCopyrightEndYear();
	}

	function _initCopyrightEndYear() {
		$j("#copyrightEndYear").html(new Date().getFullYear());
	}

	function _launchReportPost(user, reportURL) {
		var d = new Date();
		var currentURL = "/report-viewer/frameset?" + reportURL + d.getTimezoneOffset() + "&__user=" + user;
		window.open(currentURL, "_blank", "toolbar=0,resizable=1,menubar=0,location=0,status=0");
	}

	// ******************************************* //
	// *** PUBLIC FUNCTIONS ********************** //
	// ******************************************* //

	return {
		init: _init,
		launchReportPost: _launchReportPost
	};

})();
