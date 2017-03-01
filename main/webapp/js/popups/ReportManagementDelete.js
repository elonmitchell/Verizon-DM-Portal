/* global ReportManagementManager, PopupManager */
var ReportManagementDelete = (function () {
	"use strict";

	var _reportManagementDG;

	function _init() {
		_reportManagementDG = ReportManagementManager.getDataGrid();

		_initPopups();
	}

	function _initPopups() {

		PopupManager.addIdEventListeners("scr-ReportManagementDelete-confirm", {
			buttonClick: function(eventObj) { // [[ Delete Report | Confirm screen | Button clicks ]]
				if (eventObj.value === "delete") {
					// Go to the wait screen
					PopupManager.gotoScreen(eventObj.popupId, "scr-ReportManagementDelete-wait");
					// Delete the selected entry
					_reportManagementDG.deleteSelectedRows(function(bIsSuccess) {
						if (bIsSuccess) {
							// Reload the page
							location.reload();
						} else {
							// Show error
							var screenRenderData = { resultText: "There was an error deleting the selected report schedules.  Please contact your system administrator." };
							PopupManager.gotoScreen(eventObj.popupId, "scr-ReportManagementDelete-result", screenRenderData);
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
