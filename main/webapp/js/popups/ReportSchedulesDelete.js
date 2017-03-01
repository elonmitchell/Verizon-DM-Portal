/* global ReportSchedulesManager, PopupManager */
var ReportSchedulesDelete = (function () {
	"use strict";

	var _reportSchedulesDG;

	function _init() {
		_reportSchedulesDG = ReportSchedulesManager.getDataGrid();

		_initPopups();
	}

	function _initPopups() {

		PopupManager.addIdEventListeners("scr-ReportSchedulesDelete-confirm", {
			buttonClick: function(eventObj) { // [[ Delete Report Schedules | Confirm screen | Button clicks ]]
				if (eventObj.value === "delete") {
					// Go to the wait screen
					PopupManager.gotoScreen(eventObj.popupId, "scr-ReportSchedulesDelete-wait");
					// Delete the selected entry
					_reportSchedulesDG.deleteSelectedRows(function(bIsSuccess) {
						if (bIsSuccess) {
							// Reload the page
							location.reload();
						} else {
							// Show error
							var screenRenderData = { resultText: "There was an error deleting the selected report schedules.  Please contact your system administrator." };
							PopupManager.gotoScreen(eventObj.popupId, "scr-ReportSchedulesDelete-result", screenRenderData);
						}

					});
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
