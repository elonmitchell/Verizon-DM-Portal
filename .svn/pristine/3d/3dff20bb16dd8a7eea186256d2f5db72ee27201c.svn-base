/* global ReportSchedulesGrid */
var ReportSchedulesManager = (function () {
	"use strict";

	var _pageData;
	var _reportSchedulesDG;
	var _jqDeleteButton;

	function _init(pageData) {
		_pageData = pageData;

		// Set copyright end year
		_initCopyrightEndYear();

		// Init datagrid
		_initReportSchedulesDataGrid();

		// Init popups
		ReportSchedulesEdit.init();
		ReportSchedulesDelete.init();

		// Show load failed message if necessary
		if (_pageData.scheduleLoadFailed === "true") {
			$("#emptyScheduleListMsg").html(_pageData.scheduleLoadFailedMessage);
		}
	}

	function _initCopyrightEndYear() {
		$("#copyrightEndYear").html(new Date().getFullYear());
	}

	function _initReportSchedulesDataGrid() {
		// Create data grid
		var dataGridConfig = {
			jqForm: $('#report-schedules-form'),
			jqTable: $('#report-schedules-table'),
			jqButtonBar: $('.button-bar'),
			contextPath: _pageData.contextPath
		};
		_reportSchedulesDG = new ReportSchedulesDataGrid(dataGridConfig);
	}

	function _getDataGrid() {
		return _reportSchedulesDG;
	}

	// ******************************************* //
	// *** PUBLIC FUNCTIONS ********************** //
	// ******************************************* //

	return {
		init: _init,
		getDataGrid: _getDataGrid
	};

})();
