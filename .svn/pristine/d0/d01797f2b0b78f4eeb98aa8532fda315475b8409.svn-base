$j(function () {
  initDashboard();
});

function launchReport( reportURL ) {
  var d = new Date();
  var runReportUrl = reportURL.replace("frameset","run");
  theWindow = window.open( runReportUrl+d.getTimezoneOffset(), "_blank", "width=600,height=600,toolbar=0,resizable=1,menubar=0,location=0");
}

function launchReportWithDownload( reportURL ) {
  var d = new Date();
  //var runReportUrl = reportURL.replace("frameset","run");
  theWindow = window.open( reportURL+d.getTimezoneOffset(), "_blank", "width=600,height=600,toolbar=0,resizable=1,menubar=0,location=0");
}

function initDashboard() {
	// Initialize dashboard carousel
	$j(document).ready(function(){
		// Create slider
		var jqBxPagerSetDefault;
		var defaultSlideIdx = $j.cookie('report-console_startSlide');
		if (defaultSlideIdx == null) defaultSlideIdx = 0;
		var bxSlider = $j('#dashboard-slider').bxSlider({
			slideWidth: 900,
			minSlides: 1,
			maxSlides: 1,
			slideMargin: 10,
			startSlide: defaultSlideIdx,
			onSlideBefore: function(jqSlideElement, oldIndex, newIndex) {
				if (newIndex == defaultSlideIdx) {
					// On default slide, hide default link
					jqBxPagerSetDefault.css('visibility','hidden');
				}
			},
			onSlideAfter: function(jqSlideElement, oldIndex, newIndex) {
				if (newIndex != defaultSlideIdx) {
					// Not on default slide, show default link
					jqBxPagerSetDefault.css('visibility','visible');
				}
			}
		});

		// Create "Set as default view" link
		var jqBxPager = $j('.bx-pager');
		var html = '';
		html += '<div class="bx-pager-set-default">';
		html += '<div style="font-size: 0.8em; text-align: right; text-align: center; margin-top: 10px;">';
		html += '[<a href="#" onClick="return false;" title="Click to set this dashboard slide as your default view" style="color: #000">Set as default view</a>]';
		html += '</div>';
		html += '</div>';
		jqBxPager.after(html);

		// Add onclick and hide initially
		jqBxPagerSetDefault = $j('.bx-pager-set-default');
		jqBxPagerSetDefault.find('a').click(function(evt) {
			// Get the current slide index
			var currentSlideIdx = bxSlider.getCurrentSlide();
			// Set as cookie
			$j.cookie('report-console_startSlide', currentSlideIdx, { expires: 50000 });
			// Update default slide index variable
			defaultSlideIdx = currentSlideIdx;
			// Update UI
			jqBxPagerSetDefault.css('visibility','hidden');
		});
		jqBxPagerSetDefault.css('visibility','hidden');

		// Populate dashboard data
		populateDashboardData();

	});
}

function populateDashboardData() {
	populateDashboardIframeData();
}

function populateDashboardIframeData() {
	// Load calendar data first since it loads quickly, then load chart data
	populateCalendar(function() {
		populateCampaignStatsData(function() {
			populateApnConfigurations();
		});
	});
}

function populateDataFromIframe(config, attemptNum) {
	if (attemptNum == null) attemptNum = 1;
	var jqCampaignStatsIframe = $j("iframe#"+config.iframeId);
	if (jqCampaignStatsIframe.length == 0) {
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
		jqCampaignStatsIframe = $j("iframe#"+config.iframeId);
	}
	// Copy chart output from campaignStatsIframe into dashboard-campaign-stats
	var jqContent = jqCampaignStatsIframe.contents().find(config.contentSelector);
	if (jqContent.length == 0) {
		if (attemptNum >= 200) { // Look for max attempts
			// Max attempts -> show error content
			$j(config.targetSelector).html(config.errorHtml);
			// Trigger callbacks
			if (config.doneCbFunc != null) config.doneCbFunc();
		} else {
			// Try again after a delay (it takes some time for the charts to load in the iframe)
			setTimeout(function() { populateDataFromIframe(config, attemptNum+1); }, 500);
		}
	} else {
		// Content found -> Load content into target
		var jqContentParent = jqContent.parent();
		$j(config.targetSelector).html(jqContentParent.html());
		// Trigger callbacks
		if (config.successCbFunc != null) config.successCbFunc();
		if (config.doneCbFunc != null) config.doneCbFunc();
	}
}

/******************/
/* CAMPAIGN STATS */
/******************/

function populateCampaignStatsData(doneCbFunc) {
	var config = {
		iframeId: "birtStatsIframe",
		contentSelector: 'table.style_2',
		targetSelector: 'div#dashboard-campaign-stats',
		errorHtml: "No data available",
		doneCbFunc: doneCbFunc
	};
	populateDataFromIframe(config);
}

/**********************/
/* APN CONFIGURATIONS */
/**********************/

function populateApnConfigurations(doneCbFunc) {
	var config = {
		iframeId: "birtStatsIframe",
		contentSelector: 'table.style_8',
		targetSelector: 'div#dashboard-apn-configs',
		errorHtml: "No data available",
		doneCbFunc: doneCbFunc
	};
	populateDataFromIframe(config);
}

/* ******** */
/* CALENDAR */
/* ******** */

function populateCalendar(doneCbFunc) {
	var config = {
		iframeId: "birtCalIframe",
		contentSelector: 'table#__bookmark_1',
		targetSelector: 'div.dashboard-calendar',
		errorHtml: "Cannot retrieve calendar data",
		successCbFunc: function() {
			// Update calendar row heights
			var newHeight = '0.6in';
			$j('table.style_12').find('tr[style*="height: 1in"]').css('height', newHeight);
			$j('td.style_13').find('div[style*="height: 1in"]').css('height', newHeight);
			//alert("attemptNum = " + attemptNum);
		},
		doneCbFunc: doneCbFunc
	};
	populateDataFromIframe(config);
}
