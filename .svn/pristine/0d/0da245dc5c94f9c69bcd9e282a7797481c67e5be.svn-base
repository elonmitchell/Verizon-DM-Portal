/* global ReportManagementManager, PopupManager */
var ReportManagementImport = (function () {
	"use strict";

	var _reportManagementDG;

	function _init() {
		_reportManagementDG = ReportManagementManager.getDataGrid();

		_initPopups();
	}

	function _initPopups() {

		PopupManager.addIdEventListeners("scr-ReportSchedulesImport-input", {
			dataInit: function(eventObj) { // [[ Import Report | Input screen | Data init ]]
				// Populate report groups
				debugger
				$.each(eventObj.renderData.formFields.fieldData, function(fieldId, fieldData) {
					if (fieldId === "reportGroup") {
						fieldData.options = _reportManagementDG.getReportGroupOptions();
						fieldData.value = _reportManagementDG.getCurrentReportGroupVal();
					}
				});
			},
			buttonClick: function(eventObj) { // [[ Import Report | Input screen | Button clicks ]]
				debugger;
				if (eventObj.value === "import") {
					// Validate
					var screen = PopupManager.getScreen(eventObj);
					var bIsValid = screen.validateFields();
					if (bIsValid) {
						var f_repname = eventObj.renderData.formFields.fieldData.reportName.value;
						var f_repgroup = eventObj.renderData.formFields.fieldData.reportGroup.value;
						var f_desc = eventObj.renderData.formFields.fieldData.reportDescription.value;
						var f_file = eventObj.renderData.formFields.fieldData.importFile.value[0];
						var f_fileName = f_file.name;
                                                var newform = new FormData();
                                                newform.append("name", f_repname);
                                                newform.append("reportGroup", f_repgroup);
						newform.append("description", f_desc);
						newform.append("importFile",f_file, f_fileName);
						// Go to the wait screen
						PopupManager.gotoScreen(eventObj.popupId, "scr-ReportSchedulesImport-wait");
						//var formData = document.createElement("form");
						//var formx = document.getElementById("scr-ReportSchedulesImport-input");
						//var rg = document.getElementById("reportGroup");
						//var rn = document.getElementById("reportName");
						//var rd = document.getElementById("reportDescription");
						//var formValue = new FormData(formx);

					//	var formName = document.getElementsByTagName("form");
					//	var importData = {};
						// Delete the selected entry
						_reportManagementDG.upLoadReport(newform, function(resultMessage) {
						//	_reportManagementDG.importReport(importData, function(bIsSuccess) {
							if (resultMessage == null || resultMessage == '') {
								// Reload the page
								location.reload();
							} else {
								// Show error
								//var screenRenderData = { resultText: "There was an error updating the report config.  Please contact your system administrator." };
								var screenRenderData = { resultText: resultMessage};
								PopupManager.gotoScreen(eventObj.popupId, "scr-ReportSchedulesImport-result", screenRenderData);
							}
						});
					}
				}
			}
		});

	}

	// ************************************ //
	// *** PUBLIC FUNCTIONS *************** //
	// ************************************ //

	return {
		init: _init
	};

})();
