/* global ReportManagementGrid */
var ReportManagementManager = (function () {
	"use strict";

	var _pageData;
	var _reportManagementDG;

	var _jqReportGroupFilter;
	var _jqForm;

	var _bGroupLoadFailed;

	function _init(pageData) {
		_pageData = pageData;

		// Set copyright end year
		_initCopyrightEndYear();

		// Init import errors
		_initImportError(_pageData.importErrorMessage);

		// Init datagrid
		_initReportManagementDataGrid();

		// Init group load error
		_bGroupLoadFailed = (_pageData.groupLoadFailed === "true");
		_initGroupLoadError();

		// Init group filter
		_jqReportGroupFilter = $('#report-group-filter');
		_jqReportGroupFilter.change(function() {
			_handleSelectGroup(false);
		});
		_handleSelectGroup(true);

		// Init buttons/popups
		ReportManagementRolesAssign.init();
		ReportManagementImport.init();
		ReportManagementDelete.init();
	}

	function _initCopyrightEndYear() {
		$("#copyrightEndYear").html(new Date().getFullYear());
	}

	// ******************************************* //
	// *** DATA GRID FUNCTIONS ******************* //
	// ******************************************* //

	function _initReportManagementDataGrid() {
		_jqForm = $('#report-management-form');

		// Create data grid
		var dataGridConfig = {
			jqReportGroupFilter: $('#report-group-filter'),
			jqForm: _jqForm,
			jqTable: $('#report-management-table'),
			jqButtonBar: $('.button-bar'),
			contextPath: _pageData.contextPath
		};
		_reportManagementDG = new ReportManagementDataGrid(dataGridConfig);
	}

	function _getDataGrid() {
		return _reportManagementDG;
	}

	// ******************************************* //
	// *** REPORT GROUPS FUNCTIONS *************** //
	// ******************************************* //

	function _handleSelectGroup(bIsInit) {
		if (!_bGroupLoadFailed) {
			// Enable/disable import button
			if (_jqReportGroupFilter.is(":disabled")) {
				_reportManagementDG.disableImportButton();
				return;
			} else {
				_reportManagementDG.enableImportButton();
			}

			var groupID = _jqReportGroupFilter.val();
			var initReportGroup = null;
			if (bIsInit) {
				// Set initial group from persisted value
				initReportGroup = Cookies.get("ReportManagement-ReportGroup");
				if (initReportGroup != null) {
					_jqReportGroupFilter.val(initReportGroup);
				}
				groupID = _jqReportGroupFilter.val();
			} else {
				// Persist new value
				Cookies.set("ReportManagement-ReportGroup", groupID);
				groupID = _jqReportGroupFilter.val()
			}

			var dataToSend = {
				operation: "getReportsInGroup",
				groupId: groupID,
				persistedSelectedIndex: _pageData.persistedSelectedIndex
			};
			// Set a cookie so we return the new UI instead of the old HTML content
			Cookies.set("ReportManagement-NewUI", "true");
			$.ajax({
				url: _pageData.contextPath + "manage/reportManagerAjax.do",
				type: 'GET',
				dataType: 'html',
				data: dataToSend
			}).fail(CommonUtil.onAjaxFail).done(function (responseHtml) {
				Cookies.set("ReportManagement-NewUI", null);
				if (_isResponseLoginPage(responseHtml)) {
					location.reload();
					return;
				}

				// Update form
				_jqForm.html(responseHtml);

				// Refresh data grid
				_reportManagementDG.refresh();

				// Re-initialize buttons
				_initDownloadReports();
				PopupManager.registerPopupButtons(_jqForm);
			});
		}
	}

	function _initDownloadReports() {
		$('.report-management-download').unbind('click').click(function() {
			var reportId = $(this).data("reportid");
			window.location = _pageData.contextPath + "manage/downloadReport.do?reportID="+reportId;
		});
	}

	// ******************************************* //
	// *** ERROR HANDLING FUNCTIONS ************** //
	// ******************************************* //

	function _initImportError(errorMessage) {
		if (errorMessage !== "") {
			// TODO: handle error
			alert("Import error: " + errorMessage);
			////showCriticalPopup(importErrorMessage, "Import Error");
		}
	}

	function _initGroupLoadError() {
		if (_bGroupLoadFailed) {
			// Disable Import and Manage Groups buttons
			_reportManagementDG.disableImportButton();
			_reportManagementDG.disableManageGroupsButton();
			// TODO: show error
			alert(_pageData.groupLoadFailedMessage);
			$('#emptyReportListMsg').text(_pageData.errorLoadingListMsg);
		}
	}

	// ******************************************* //
	// *** UTILITY FUNCTIONS ********************** //
	// ******************************************* //

	function _isResponseLoginPage(responseHtml) {
		return (responseHtml.indexOf("<h1>Login</h1>") > -1) ? true : false;
	}

	// ******************************************* //
	// *** PUBLIC FUNCTIONS ********************** //
	// ******************************************* //

	return {
		init: _init,
		getDataGrid: _getDataGrid
	};

})();
