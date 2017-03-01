/* global */

function ReportSchedulesDataGrid(_config) {
	"use strict";

	var _jqCbSelectAll;
	var _jqCbSelect;
	var _jqMultiSelButtons;

	_init();

	function _init() {
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

	// ******************************************* //
	// *** EDIT FUNCTIONS ************************ //
	// ******************************************* //

	function _getReportScheduleData(scheduleId, resultCallback) {
		// Set a cookie so we return a JSON instead of the old HTML content
		Cookies.set("ReportSchedulesEdit-ReturnJSON", "true");
		var dataToSend = {
			scheduleId: scheduleId
		};
		// Make AJAX call
		$.ajax({
			url: _config.contextPath + "schedule/editSchedulePopup.do",
			type: 'GET',
			dataType: 'json',
			data: dataToSend
		}).fail(CommonUtil.onAjaxFail).done(function (resultData) {
			Cookies.set("ReportSchedulesEdit-ReturnJSON", null);
			// Sample response:
			// {
			// 		"scheduleId": "2170",
			// 		"scheduleName": "D_CASI_C781_CA2",
			// 		"reportName": "SU Multiple Campaigns",
			// 		"frequency": "1",
			// 		"startDateTime": "2016-04-15 15:08:00.0",
			// 		"recipients": [{"name":"dmportal","id":"1142","email":"baskar.dilli@motive.com"},{"name":"dmpvzw","id":"2883","email":"baskar.dilli@motive.com"}]
			// }
			var scheduleData = {
				scheduleName: resultData.scheduleName,
				reportName: resultData.reportName,
				scheduleIdEP: resultData.scheduleId,
				frequency: resultData.frequency,
				startDateTime: moment(resultData.startDateTime).format("MM/DD/YYYY h:mm a"),
				recipients: resultData.recipients
			};
			//resultData.startDateTime

			// Success -> Trigger callback
			resultCallback(scheduleData);
		});
	}

	// *******************************************//
	// *** EDIT THE REPORT SCHEDULE FUNCTIONS ***//
	// ******************************************//
	function _editSelectedRows(editData) {
		var useremids = "";
		var jsonrec = "";
		$('div.text-wrap').each(function(index) {
			$(this).find('input').each(function() {
				jsonrec = $(this).val();
			});
		});
		if (jsonrec != "") {
			var jsonrec_parsed = jsonrec.evalJSON();
			for (var i=0; i < jsonrec_parsed.length; i++) {
				if (i != 0) {
					useremids = useremids + ",";
				}
				useremids = useremids + jsonrec_parsed[i].id;
			}
		}

		// Make AJAX call
		$.ajax({
			url: _config.contextPath + "schedule/updateSchedule.do",
			type: 'POST',
			dataType: 'html',
			data: editData
                }).fail(CommonUtil.onAjaxFail).done(function (resultData) {
                      location.reload();
		});


	}


	// ******************************************* //
	// *** DELETE FUNCTIONS ********************** //
	// ******************************************* //

	function _deleteSelectedRows(resultCallback) {
		var cbValArray = _jqCbSelect.map(function() { 
                                      //alert($(this));
                                      if ($(this).is(':checked')) {
                                          return $(this).val(); 
                                      }
                                 }).get();
		var dataToSend = {
			operation: "deleteSchedules",
			ids: cbValArray.join(",")
		};
		// Make AJAX call
		$.ajax({
			url: _config.contextPath + "schedule/deleteScheduleAjax.do",
			type: 'GET',
			dataType: 'json',
			data: dataToSend
		}).fail(CommonUtil.onAjaxFail).done(function (resultData) {
			// Success -> Trigger callback
			//var bIsSuccess = (resultData.responseText > 0);
                        var bIsSuccess = (resultData > 0);
			resultCallback(bIsSuccess);
		});
	}

	// ******************************************* //
	// *** PUBLIC METHODS ************************ //
	// ******************************************* //

	return {
		getReportScheduleData: _getReportScheduleData,
		deleteSelectedRows:    _deleteSelectedRows,
		editSelectedRows:      _editSelectedRows,
		getNumSelected:        _getNumSelected
	};
}