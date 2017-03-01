/* global */
var $j = jQuery.noConflict();

var HistoricalReportsManager = (function () {
	"use strict";

	var SIMULATE_UPDATE_MODE = false;

	var _jqTableContainer;
	var _ractiveObj;

	var _reportRenderDataArray;
	var _bTableShown;

	var _simulationResultData;

	function _init() {
		// Set copyright end year
		_initCopyrightEndYear();

		_initHistoricalReportsTable();
	}

	function _initCopyrightEndYear() {
		$j("#copyrightEndYear").html(new Date().getFullYear());
	}

	function _initHistoricalReportsTable() {
		// Set container
		_jqTableContainer = $j('#historical-reports-content-container');

		// Create ractive
		_bTableShown = false;
		_reportRenderDataArray = [];
		_ractiveObj = new Ractive({
			el: _jqTableContainer,
			template: '#historical-reports-table-template',
			data: {
				bLoading: true,
				bTableShown: _bTableShown,
				reportDataArray: _reportRenderDataArray
			}
		});
		// Populate
		_populateHistoricalReportsTable();
	}

	function _populateHistoricalReportsTable() {
		if (SIMULATE_UPDATE_MODE) {
			if (_simulationResultData == null) {
				var simulationDataArray = _getSimulationData();
				_populateHistoricalReportsTable2(simulationDataArray);
			}
		} else {
			jQuery.ajax({
				url: 'historicalReportAction.do',
				dataType: 'json',
				success:function(result) {
					var reportResultDataArray = result['histReportList'];
					_populateHistoricalReportsTable2(reportResultDataArray);
				},
				error: function (jqXHR, textStatus, errorThrown){
					if(errorThrown.indexOf("login") != -1){
						// location.href = 'login.vm'
						location.reload();
					} else{
						alert("Exception occured " + textStatus);
					}
				}
			});
		}

	}

	function _populateHistoricalReportsTable2(reportResultDataArray) {
		var newReportRenderDataArray = [];
		var downloadHistUrlPrefix = "/report-console/view/downloadHistReport.do?histReportID=";
		// Add data to newReportRenderDataArray
		jQuery.each(reportResultDataArray, function (idx, reportResultData) {
			if (idx !== "statusCode" && idx !== "statusMessage") {
				// Determine if this is a new report
				var reportDescription = reportResultData.description;
				var fileDescriptionRegexp = new RegExp("run (.*) (for|by) (.*) on (.*)", "i"); // e.g. "run Software Update Report by dmportal on Tue Jul 22 15:22:41 CDT 2014"
				var matchArray = reportDescription.match(fileDescriptionRegexp);
				var bIsNew = false;
				if (matchArray != null && matchArray.length >= 5) {
					var insertedDateStr = matchArray[4];
					var insertedDate = new Date(insertedDateStr);
					if (insertedDate != null) {
						var insertedTime = insertedDate.getTime();
						var nowTime = (new Date()).getTime();
						var diff = nowTime - insertedTime;
						var thresholdDiff = 10 * 60 * 1000; // 10 minutes
						if (diff <= thresholdDiff) {
							bIsNew = true;
						}
					}
				}
				// Add data
				newReportRenderDataArray.push({
					bIsNew: bIsNew,
					id: reportResultData.id,
					url: downloadHistUrlPrefix + reportResultData.id,
					title: reportResultData.fileName,
					description: reportDescription
				});
			}
		});

		// Loop over _reportRenderDataArray from bottom up
		var currentIdx = _reportRenderDataArray.length - 1;
		var newIdx = newReportRenderDataArray.length - 1;
		while (currentIdx >= 0) {
			var currentReportRenderData = _reportRenderDataArray[currentIdx];
			var currentId = currentReportRenderData.id;
			var bItemFound = false;
			// Loop over newReportRenderDataArray from bottom up
			var tmpNewIdx = newIdx;
			while (tmpNewIdx >= 0 && !bItemFound) {
				// Try to match items by id
				var newReportRenderData = newReportRenderDataArray[tmpNewIdx];
				if (currentId === newReportRenderData.id) {
					// Id's match -> Test if any properties differ
					var bPropertiesDiffer = _doObjectsPropertiesDiffer(newReportRenderData, currentReportRenderData);
					if (bPropertiesDiffer) {
						// Properties differ -> Update corresponding item in _reportRenderDataArray
						_reportRenderDataArray[currentIdx] = newReportRenderData;
					}
					// Update found flag and indexes
					bItemFound = true;
					currentIdx--;
					newIdx = tmpNewIdx - 1;
					break;
				} else {
					// Id's don't match -> Add new item to bottom of _reportRenderDataArray
					_reportRenderDataArray.push(newReportRenderData);
					// Loop again
					tmpNewIdx--;
				}
			}
			if (!bItemFound) {
				// Remove corresponding item from _reportRenderDataArray and update index
				_reportRenderDataArray.slice(currentIdx);
				currentIdx--;
			}
		}
		if (newIdx >= 0) {
			// Add remaining items to _reportRenderDataArray from bottom up
			while (newIdx >= 0) {
				_reportRenderDataArray.unshift(newReportRenderDataArray[newIdx]);
				newIdx--;
			}
		}

		// No longer loading
		_ractiveObj.set("bLoading", false);

		// Table is shown
		_ractiveObj.set("bTableShown", _bTableShown);
		_bTableShown = true;

		// Load again after a delay of 5 minutes
		window.setTimeout(function() {
			_populateHistoricalReportsTable();
		}, 300000);
	}

	function _launchReport(histReportURL) {
		window.open(histReportURL, "_blank", "toolbar=0,resizable=1,menubar=0,location=0");
	}

	// ******************************************* //
	// *** UTILITY FUNCTIONS ********************* //
	// ******************************************* //

	function _doObjectsPropertiesDiffer(o1, o2) {
		// Compare number of keys
		if (Object.keys(o1).length !== Object.keys(o2).length) return true;
		// Compare values of keys
		var bPropertiesDiffer = false;
		for (var key in o1) {
			if (o1.hasOwnProperty(key)) {
				if (o1[key] !== o2[key]) {
					bPropertiesDiffer = true;
					break;
				}
			}
		}
		return bPropertiesDiffer;
	}

	var numResults = 2;
	function _getSimulationData() {
		var simulationDataArray = [];
		for (var i = 0; i < numResults; i++) {
			simulationDataArray.unshift(SIMULATE_DATA_ARRAY[i]);
		}
		if (numResults < SIMULATE_DATA_ARRAY.length) numResults++;
		return simulationDataArray;
	}

	var SIMULATE_DATA_ARRAY = [
		{
			"fileName": "HTC",
			"roles": "RPT_FOR_VZW",
			"description": "run EUX Properties Beta by dmpadmin on Thu Jul 21 14:30:43 CDT 2016",
			"id": "4355"
		},
		{
			"fileName": "HTC_ADR6400L <b>(Offline Report)<\/b>",
			"roles": "RPT_FOR_VZW",
			"description": "run Device Manufacturer Model FW Statistics for dmpvzw on Thu Jul 21 11:43:04 CDT 2016",
			"id": "4354"
		},
		{
			"fileName": "SAMSUNG_SCH-I435 <b>(Offline Report)<\/b>",
			"roles": "RPT_FOR_VZW",
			"description": "run Device Manufacturer Model FW Statistics for dmportal on Thu Jul 21 11:40:02 CDT 2016",
			"id": "4353"
		},
		{
			"fileName": "HTC_ADR6400L <b>(Offline Report)<\/b>",
			"roles": "RPT_FOR_VZW",
			"description": "run Device Campaign Results for Short Time Period for dmportal on Thu Jul 21 11:33:12 CDT 2016",
			"id": "4352"
		},
		{
			"fileName": "HTC_ADR6400L <b>(Offline Report)<\/b>",
			"roles": "RPT_FOR_VZW",
			"description": "run Device Manufacturer Model FW Statistics for dmpvzw on Thu Jul 21 11:17:07 CDT 2016",
			"id": "4351"
		},
		{
			"fileName": "HTC_ADR6400L",
			"roles": "RPT_FOR_VZW",
			"description": "run Device Manufacturer Model FW Statistics by dmpvzw on Thu Jul 21 11:14:15 CDT 2016",
			"id": "4350"
		},
		{
			"fileName": "HTC_ADR6400L <b>(Offline Report)<\/b>",
			"roles": "RPT_FOR_VZW",
			"description": "run Device Manufacturer Model FW Statistics for dmpvzw on Thu Jul 21 10:54:15 CDT 2016",
			"id": "4349"
		},
		{
			"fileName": "HTC_ADR6400L <b>(Offline Report)<\/b>",
			"roles": "RPT_FOR_VZW",
			"description": "run Device List for Specified Failure Code  for dmpvzw on Thu Jul 21 10:27:15 CDT 2016",
			"id": "4348"
		},
		{
			"fileName": "Tester Device Report - 7-19-2016 <b>(Offline Report)<\/b>",
			"roles": "RPT_FOR_VZW",
			"description": "run Tester Device Report Beta for dmportal on Tue Jul 19 10:24:35 CDT 2016",
			"id": "4337"
		},
		{
			"fileName": "Tester Device Report <b>(Offline Report)<\/b>",
			"roles": "RPT_FOR_VZW",
			"description": "run Tester Device Report Beta for dmportal on Mon Jul 18 18:28:46 CDT 2016",
			"id": "4334"
		},
		{
			"fileName": "dmportal <b>(Offline Report)<\/b>",
			"roles": "RPT_FOR_VZW",
			"description": "run Tester Device Report Beta for dmportal on Mon Jul 18 18:06:47 CDT 2016",
			"id": "4333"
		},
		{
			"fileName": "Tester_Devices_Beta <b>(Offline Report)<\/b>",
			"roles": "RPT_FOR_VZW",
			"description": "run Tester Device Report Beta for dmportal on Mon Jul 18 17:59:12 CDT 2016",
			"id": "4332"
		},
		{
			"fileName": "dmportal <b>(Offline Report)<\/b>",
			"roles": "RPT_FOR_VZW",
			"description": "run Tester Device Report Beta for dmportal on Mon Jul 18 17:53:28 CDT 2016",
			"id": "4331"
		}
	];

	// ******************************************* //
	// *** PUBLIC FUNCTIONS ********************** //
	// ******************************************* //

	return {
		init: _init,
		launchReport: _launchReport
	};

})();
