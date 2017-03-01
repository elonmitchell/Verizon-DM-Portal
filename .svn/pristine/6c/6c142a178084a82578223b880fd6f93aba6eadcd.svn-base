/* global */

function ReportManagementDataGrid(_config) {
	"use strict";

	var _jqCbSelectAll;
	var _jqCbSelect;
	var _jqMultiSelButtons;

	var _jqImportButton;
	var _jqManageGroupsButton;

	_init();

	function _init(bFirstInit) {
		if (bFirstInit == null) bFirstInit = true;
		if (bFirstInit) {
			_jqImportButton = $('#report-management-import');
			_jqManageGroupsButton = $('#report-management-groups-manage');
		}

		// Set selection buttons
		_jqMultiSelButtons = $('.selection-button', _config.jqButtonBar);

		// Init checkbox behavior
		_initCheckboxes();
	}

	function _initCheckboxes() {
		// Handle checkboxes
		_jqCbSelectAll = $('.cb-select-all', _config.jqForm);
		_jqCbSelect = $('.cb-select', _config.jqForm);
		_jqCbSelectAll.change(function() {
			// Update all checkboxes
			var bIsSelected = $(this).is(":checked");
			_jqCbSelect.prop("checked", bIsSelected);
			// Enable/disable selection buttons
			_enableDisableSelectionButtons();
		});
		_jqCbSelect.change(function() {
			var bAllChecked = (_getNumSelected() === _jqCbSelect.length);
			// Check/unchecked select all checkbox
			_jqCbSelectAll.prop("checked", bAllChecked);
			_enableDisableSelectionButtons();
		});
		_enableDisableSelectionButtons();
	}

	function _getNumSelected() {
		return _jqCbSelect.filter(":checked").length;
	}

	// ******************************************* //
	// *** GET/SET REPORT CONFIG ***************** //
	// ******************************************* //

	function _getReportConfig(reportId, resultCallback) {
		// Set a cookie so we return a JSON instead of the old HTML content
		Cookies.set("ReportManagement-ReturnJSON", "true");
		var dataToSend = {
			reportId: reportId
		};
		// Make AJAX call
		$.ajax({
			url: _config.contextPath + "manage/assignRolesToReportPopup.do",
			type: 'GET',
			dataType: 'json',
			data: dataToSend
		}).fail(CommonUtil.onAjaxFail).done(function (resultData) {
			Cookies.set("ReportManagement-ReturnJSON", null);
			// Sample response:
			// {
			// 		"reportId": "2111",
			// 		"reportName": "First Day Campaign Report",
			// 		"reportRoles": "$report.roles",
			// 		"historicalReportsNum": "10",
			// 		"historicalReportsEnabledDisabled": "true",
			// 		"scedulesNum": "10",
			// 		"schedulesEnabledDisabled": "true",
			// 		"scheduleSupportedFormats": "pdf,xls",
			// 		"scheduleReportFormat": "xls"
			// }
			var scheduleData = resultData;
			if (scheduleData.reportRoles === ("$" + "report.roles")) scheduleData.reportRoles = "";

			// Success -> Trigger callback
			resultCallback(scheduleData);
		});
	}

	function _updateReportConfig(configData, resultCallback) {
		// Input configData will look like:
		// var configData = {
		//  reportId: "2111",
		// 	reportRoles: screen.getTextareaField("reportRoles").val(),
		// 	historicalReportsNum: screen.getTextField("historicalReportsNum").val(),
		// 	historicalReportsEnabledDisabled: screen.getRadioGroupVal("historicalReportsEnabledDisabled"),
		// 	scedulesNum: screen.getTextField("scedulesNum").val(),
		// 	schedulesEnabledDisabled: screen.getRadioGroupVal("schedulesEnabledDisabled"),
		// 	scheduleSupportedFormats: screen.getCheckboxGroupVal("scheduleSupportedFormats"),
		// 	scheduleReportFormat: screen.getRadioGroupVal("scheduleReportFormat")
		// };
		var dataToSend = {
			roles: configData.reportRoles, // e.g. "RPT_FOR_VZW,RPT_FOR_LRA"
			reportId: configData.reportId, // e.g. "2111"
			numofhist: configData.historicalReportsNum, // e.g. "10"
			ishistenabled: configData.historicalReportsEnabledDisabled, // e.g. "true"
			numofsche: configData.scedulesNum, // e.g. "10"
			isscheenabled: configData.schedulesEnabledDisabled, // e.g. "true"
			reportFormat: configData.scheduleReportFormat // e.g. "xls"
		};
		var dataToSendStr = $.param(dataToSend);
		$.each(configData.scheduleSupportedFormats, function(idx, val) {
			dataToSendStr = dataToSendStr + "&suppRepFor=" + val; // e.g. "&suppRepFor=pdf&suppRepFor=xls"
		});
		// Make AJAX call
		$.ajax({
			url: _config.contextPath + "manage/assignRolesToReport.do",
			type: 'GET',
			dataType: 'html',
			data: dataToSendStr
		}).fail(CommonUtil.onAjaxFail).done(function (resultData) {
			var bSuccess = true;
			resultCallback(bSuccess);
			// Reload the page
			location.reload();
		});
	}

	function _getReportGroupOptions() {
		var reportGroupOptions = [];
		// Get the report groups from the report group filter
		_config.jqReportGroupFilter.find("option").each(function() {
			var reportGroupOption = {
				text: $(this).data("optionname"),
				value: $(this).val()
			};
			reportGroupOptions.push(reportGroupOption);
		});
		return reportGroupOptions;
	}

	function _getCurrentReportGroupVal() {
		return _config.jqReportGroupFilter.val();
	}
	
	// ******************************************* //
	// *** UPLOAD REPORT FUNCTION **************** //
	// ******************************************* //
	function _upLoadReport(formData, resultCallback) {
		$.ajax({
			url: _config.contextPath + "manage/importReport.do",
			type: 'POST',
			contentType: false,
			processData: false,
			data: formData
		}).fail(CommonUtil.onAjaxFail).done(function (resultData) {
			resultCallback(resultData);
		});

	}
	
	// ******************************************* //
	// *** DELETE REPORT ************************* //
	// ******************************************* //

	function _deleteSelectedRows(resultCallback) {
		var cbValArray = _jqCbSelect.map(function() { 
							if ($(this).is(':checked')) {
								return $(this).val(); 
							}
						}).get();
		var dataToSend = {
			operation: "deleteReports",
			ids: cbValArray.join(",")
		};
		// Make AJAX call
		$.ajax({
			url: _config.contextPath + "manage/deleteReportAjax.do",
			type: 'GET',
			dataType: 'json',
			data: dataToSend
		}).fail(CommonUtil.onAjaxFail).done(function (resultData) {
			var bIsSuccess = (resultData > 0);
			resultCallback(bIsSuccess);
		});
	}

	// ******************************************* //
	// *** SELECTION BUTTONS ********************* //
	// ******************************************* //

	function _enableSelectionButtons() {
		_enableDisableSelectionButtons(true);
	}

	function _disableSelectionButtons() {
		_enableDisableSelectionButtons(false);
	}

	function _enableDisableSelectionButtons(bEnable) {
		if (bEnable == null) bEnable = (_getNumSelected() > 0);
		_jqMultiSelButtons.prop("disabled", !bEnable);
	}

	function _enableImportButton() {
		_jqImportButton.prop("disabled", false);
	}

	function _disableImportButton() {
		_jqImportButton.prop("disabled", true);
	}

	function _disableManageGroupsButton() {
		_jqManageGroupsButton.prop("disabled", true);
	}

	function _refresh() {
		_init(false);
	}

	// ******************************************* //
	// *** PUBLIC METHODS ************************ //
	// ******************************************* //

	return {
		refresh: _refresh,
		getNumSelected: _getNumSelected,
		enableImportButton: _enableImportButton,
		disableImportButton: _disableImportButton,
		disableManageGroupsButton: _disableManageGroupsButton,
		getReportConfig: _getReportConfig,
		updateReportConfig: _updateReportConfig,
		getReportGroupOptions: _getReportGroupOptions,
		getCurrentReportGroupVal: _getCurrentReportGroupVal,
		upLoadReport: _upLoadReport,
		deleteSelectedRows: _deleteSelectedRows
	};
}