/* global g_iframeReportConfig */
var $j = jQuery.noConflict();

var DashboardManager = (function () {
	"use strict";

	function _init() {
		// Set copyright end year
		_initCopyrightEndYear();

		// Add click handlers for fota details buttons
		_initFotaDetailsButtons();

		// Populate iframe data
		_populateDashboardIframeData();

		// Toggle View Buttons
		_toggleViewButtons();

		// Toggle View
		_toggleView();
	}

	function _initCopyrightEndYear() {
		$j("#copyrightEndYear").html(new Date().getFullYear());
	}

	function _initFotaDetailsButtons() {
		// Set behavior for "View APN/FOTA Details" button
		$j('#dashboard-fota-details-view').click(function() {
			var reportUrl = $j(this).data("reporturl");
			var runReportUrl = reportUrl.replace("frameset","run");
			var fullUrl = runReportUrl + (new Date()).getTimezoneOffset().toString();
			window.open(fullUrl, "_blank", "width=600,height=600,toolbar=0,resizable=1,menubar=0,location=0");
		});

		$j('#dashboard-fota-details-download').click(function() {
			var reportUrl = $j(this).data("reporturl");
			var fullUrl = reportUrl + (new Date()).getTimezoneOffset().toString();
			window.open(fullUrl, "_blank", "width=600,height=600,toolbar=0,resizable=1,menubar=0,location=0");
		});
	}

	function _toggleView(){
		localStorage.getDashboardView = localStorage.getDashboardView || 'dashboard-charts-area';
		$j('.links li[view="' + localStorage.getDashboardView + '"]').addClass('active').siblings().removeClass('active');
		$j('#' + localStorage.getDashboardView).addClass('active').siblings('.tab-pane').removeClass('active');
		$j('#' + localStorage.getDashboardView).find('.default-view').hide();
	}

	function _toggleViewButtons(){
		$j('.links li').on('click',function(){
			var view = $j(this).attr('view');
			$j(this).addClass('active').siblings().removeClass('active');
			$j('#' + view).addClass('active').siblings('.tab-pane').removeClass('active');
		})

		$j('.default-view').on('click',function(){
            var view = $j(this).attr('view');
            localStorage.getDashboardView = view;
            $j('#' + localStorage.getDashboardView).find('.default-view').hide()
            $j('#' + localStorage.getDashboardView).siblings('.tab-pane').find('.default-view').show();
        })
	}

	// ******************************************* //
	// *** POPULATE IFRAME DATA FUNCTIONS ******** //
	// ******************************************* //

	function _populateDashboardIframeData() {
		// Load calendar data first since it loads quickly, then load chart data
		_populateCalendar(function() {
			_populateCampaignStatsData(function() {
				_populateApnConfigs();
			});
		});
	}

	function _populateCalendar(doneCbFunc) {
		var config = {
			iframeId: "birtCalIframe",
			contentSelector: 'table#__bookmark_1',
			targetSelector: 'div#dashboard-calendar-area > div.dashboard-calendar',
			errorHtml: "Cannot retrieve calendar data",
			successCbFunc: function(config, jqTarget) {
				// Update calendar row heights
				var newHeight = '0.8in';
				jqTarget.find('table.style_12').find('tr[style*="height: 1in"]').css('height', '');
				//jqTarget.find('table.style_12').find('tr[style*="height: 1in"]').css('minHeight', newHeight);
				jqTarget.find('td.style_13').find('div[style*="height: 1in"]').css('height', '');
				//jqTarget.find('td.style_13').find('div[style*="height: 1in"]').css('minHeight', newHeight);
			},
			doneCbFunc: doneCbFunc
		};
		_addIframeAndPopulateData(config);
	}

	function _populateCampaignStatsData(doneCbFunc) {
		var config = {
			iframeId: "birtStatsIframe",
			contentSelector: 'table.style_2',
			targetSelector: 'div#dashboard-campaign-stats > div.dashboard-chart',
			errorHtml: "No data available",
			doneCbFunc: doneCbFunc
		};
		_addIframeAndPopulateData(config);
	}

	function _populateApnConfigs(doneCbFunc) {
		var config = {
			iframeId: "birtStatsIframe",
			contentSelector: 'table.style_8',
			targetSelector: 'div#dashboard-apn-configs > div.dashboard-chart',
			errorHtml: "No data available",
			successCbFunc: function(config, jqTarget) {
				jqTarget.find(config.contentSelector).addClass("table-condensed").addClass("table-striped");
			},
			doneCbFunc: doneCbFunc
		};
		_addIframeAndPopulateData(config);
	}

	// Generic function add an iframe and copy contents in iframe into a target element
	function _addIframeAndPopulateData(config, attemptNum) {
		if (attemptNum == null) attemptNum = 1;
		var jqIframe = $j("iframe#"+config.iframeId);
		if (jqIframe.length === 0) {
			// Iframe doesn't exist -> Add it
			var iframeConfig = g_iframeReportConfig[config.iframeId];
			// Get src value
			var params = {};
			params["__report"] = iframeConfig.reportIdValue;
			params["__locale"] = "en_US";
			params["__clean"] = "true";
			params["__fittopage"] = "true";
			params["__nocache"] = "true";
			params["__tzoffset"] = "300";
			params["__schedule"] = "true";
			var paramStr = $j.param( params );
			var srcVal = '/report-viewer/run?'+paramStr+"&__user="+g_iframeReportConfig.encryptedUserName;
			// Set misc params
			var miscParams = 'width="0" height="0" marginwidth="0" marginheight="0" frameborder="no" tabindex="-1" title="empty" style="display: none"';
			// Add iframe
			var iframeHtml = '<iframe id="'+config.iframeId+'" src="'+srcVal+'" '+miscParams+'></iframe>';
			//console.log("iframeHtml = " + iframeHtml);
			$j("body").append(iframeHtml);
			jqIframe = $j("iframe#"+config.iframeId);
		}
		// Copy chart output from iframe into target element
		var jqContent = jqIframe.contents().find(config.contentSelector);
		if (jqContent.length === 0) {
			if (attemptNum >= POPULATE_IFRAME_MAX_ATTEMPTS) { // Look for max attempts
				// Max attempts -> show error content
				$j(config.targetSelector).html(config.errorHtml);
				// Trigger callbacks
				if (config.doneCbFunc != null) config.doneCbFunc(config);
			} else {
				// Try again after a delay (it takes some time for the charts to load in the iframe)
				setTimeout(function() { _addIframeAndPopulateData(config, attemptNum+1); }, POPULATE_IFRAME_TIMEOUT_MILLISECONDS);
			}
		} else {
			// Content found -> Load content into target
			var jqContentParent = jqContent.parent();
			var jqTarget = $j(config.targetSelector);
			jqTarget.html(jqContentParent.html());
			// Trigger callbacks
			if (config.successCbFunc != null) config.successCbFunc(config, jqTarget);
			if (config.doneCbFunc != null) config.doneCbFunc(config);
		}
	}

	var POPULATE_IFRAME_TIMEOUT_MILLISECONDS = 2000;
	var POPULATE_IFRAME_MAX_ATTEMPTS = 30;

	// ******************************************* //
	// *** DOCUMENT READY INIT ******************* //
	// ******************************************* //

	// ******************************************* //
	// *** PUBLIC FUNCTIONS ********************** //
	// ******************************************* //

	return {
		init: _init
	};

})();
