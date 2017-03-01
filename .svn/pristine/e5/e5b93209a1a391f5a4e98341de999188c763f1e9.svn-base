/* global ReportSchedulesManager, PopupManager */
var ReportManagementRolesAssign = (function () {
	"use strict";

	var _reportManagementDG;

	function _init() {
		_reportManagementDG = ReportManagementManager.getDataGrid();

		_initPopups();
	}

	function _initPopups() {

		PopupManager.addIdEventListeners("scr-ReportManagementRolesAssign-loading", {
			screenShown: function(eventObj) { // [[ Report Config | Loading screen | Screen shown ]]
				var lastClickedButtonId = PopupManager.getLastClickedButtonId();
				var jqLastClickedButton = $('#'+lastClickedButtonId);
				var reportId = jqLastClickedButton.data("reportid");
				_reportManagementDG.getReportConfig(reportId, function(configDataHash) {
					var fieldData = {};
					$.each(configDataHash, function(key, val) {
						if (key === "scheduleSupportedFormats") {
							fieldData[key] = { valueArray: val.split(",") };
						} else {
							fieldData[key] = { value: val };
						}
					});
					var screenRenderData = { formFields: { fieldData: fieldData}};
					PopupManager.gotoScreen(eventObj.popupId, "scr-ReportManagementRolesAssign-input", screenRenderData);
				});
			}
		});

		PopupManager.addIdEventListeners("scr-ReportManagementRolesAssign-input", {
			screenRendered: function(eventObj) { // [[ Screen rendered | Input screen | Button clicks ]]
				var screen = PopupManager.getScreen(eventObj);
				_linkCheckboxesToRadioOptions(screen);
			},
			buttonClick: function(eventObj) { // [[ Report Config | Input screen | Button clicks ]]
				if (eventObj.value === "edit") {
					// Validate
					var screen = PopupManager.getScreen(eventObj);
					var bIsValid = screen.validateFields();
					if (bIsValid) {
						var configData = {
							reportId: screen.getHiddenField("reportId").val(),
							reportRoles: screen.getTextareaField("reportRoles").val(),
							historicalReportsNum: screen.getTextField("historicalReportsNum").val(),
							historicalReportsEnabledDisabled: screen.getRadioGroupVal("historicalReportsEnabledDisabled"),
							scedulesNum: screen.getTextField("scedulesNum").val(),
							schedulesEnabledDisabled: screen.getRadioGroupVal("schedulesEnabledDisabled"),
							scheduleSupportedFormats: screen.getCheckboxGroupVal("scheduleSupportedFormats"),
							scheduleReportFormat: screen.getRadioGroupVal("scheduleReportFormat")
						};
						// Go to the wait screen
						PopupManager.gotoScreen(eventObj.popupId, "scr-ReportManagementRolesAssign-wait");
						// Delete the selected entry
						_reportManagementDG.updateReportConfig(configData, function(bIsSuccess) {
							if (bIsSuccess) {
								// Reload the page
								location.reload();
							} else {
								// Show error
								var screenRenderData = { resultText: "There was an error updating the report config.  Please contact your system administrator." };
								PopupManager.gotoScreen(eventObj.popupId, "scr-ReportManagementRolesAssign-result", screenRenderData);
							}
						});
					}
				}
			}
		});

	}

	function _linkCheckboxesToRadioOptions(screen) {
		var cbRadioOptionIdArray = [
			{ cbId: "supportedFormatPPT", radioOptionId: "reportFormatPPT" },
			{ cbId: "supportedFormatPDF", radioOptionId: "reportFormatPDF" },
			{ cbId: "supportedFormatXLS", radioOptionId: "reportFormatXLS" }
		];
		$.each(cbRadioOptionIdArray, function(idx, cbRadioOptionId) {
			var jqCb = screen.getCheckboxField(cbRadioOptionId.cbId);
			var jqRadioOption = screen.getRadioField(cbRadioOptionId.radioOptionId);
			jqCb.change(function() {
				jqRadioOption.prop("disabled", !this.checked);
				if (!this.checked) jqRadioOption.prop("checked", false);
			});
			jqCb.change();
		});
	}

	// ************************************ //
	// *** PUBLIC FUNCTIONS *************** //
	// ************************************ //

	return {
		init: _init
	};

})();
