/* global $, Popup, MakesAndModelsDepot, SpecialGroupsDepot, LraGroupsDepot, g_PERMISSIONS, EventManager, HFotaGroupsDepot, SdmTypesDepot */
var PopupManager = (function () {
	"use strict";

	var _popupHash;
	var _lastClickedId;
	var _eventMgr = new EventManager();

	_init();

	function _init() {
		// Initialize
		_popupHash = {};
		_initEventListeners();

		$(document).ready(function () {
			// Register popup buttons once document is ready
			_registerPopupButtons();
		});
	}

	function _registerPopupButtons(jqContext) {
		if (jqContext == null) jqContext = $('body');
		$('.pm-button, .pm-link', jqContext).each(function() {
			var jqPopupButton = $(this);
			if (jqPopupButton.attr('pm-has-click') !== "true") { // prevent multiple click handlers
				// Get popup id
				var popupId = _getPopupId(jqPopupButton);
				if (popupId != null) {
					// Create popup and add to hash
					_popupHash[popupId] = new Popup(popupId);
				}
				// Show popup when button is clicked
				jqPopupButton.click(function (evt) {
					var jqClickedPopupButton = $(this);
					// Get the popup id
					var popupId = _getPopupId(jqClickedPopupButton);
					if (popupId == null) {
						alert("This button/link is missing an id or data-popupid attribute.")
					} else {
						// Set the last clicked id
						_lastClickedId = jqClickedPopupButton.attr("id");
						// Open the popup
						_openPopup(popupId);
						// Remove focus from the button
						jqClickedPopupButton.blur();
					}
					// Prevent any real action
					evt.preventDefault();
				});
				jqPopupButton.attr('pm-has-click', 'true');
			} // end if
		});
	}

	function _getPopupId(jqPopupButton) {
		var popupId = jqPopupButton.data('popupid');
		if (popupId == null) popupId = jqPopupButton.attr('id');
		return popupId;
	}

	function _openPopup(popupId, screenRenderData, customData) {
		// Get popup from popup hash
		var popup = _popupHash[popupId];
		if (popup == null) {
			// Create new instance of popup and add to hash
			popup = new Popup(popupId);
			_popupHash[popupId] = popup;
		}
		// Set custom data
		if (customData != null) popup.setCustomData(customData);
		// Show it
		popup.showFirstScreen(screenRenderData);
	}

	function _initEventListeners() {
		_eventMgr.addEventListener("dataInit", null, function(eventObj) {
			try {
				var chooseOneOptionData = _getChooseOneOptionData();
				var fieldIdsToRemove = [];
				$.each(eventObj.renderData.formFields.fieldData, function(fieldId, fieldDataItem) {
					// PM_ChooseOneOption
					if (fieldDataItem.options === "PM_ChooseOneOption") {
						fieldDataItem.options = [ chooseOneOptionData ];
					}
					// PM_DeviceMakeOptions
					else if (fieldDataItem.options === "PM_DeviceMakeOptions") {
						var makesArray = MakesAndModelsDepot.getInstance().getMakesArray();
						var makeOptions = $.map(makesArray, function(deviceMake) {
							return { "text": deviceMake, "value": deviceMake };
						});
						makeOptions.unshift( chooseOneOptionData );
						fieldDataItem.options = makeOptions;
					}
					// PM_SpecialGroupOptions
					else if (fieldDataItem.options === "PM_SpecialGroupOptions") {
						var sgArray = SpecialGroupsDepot.getInstance().getGroupsArray();
						var sgOptions = $.map(sgArray, function(sg) {
							return { "text": sg.text, "value": sg.value };
						});
						if (this.type === "select") {
							sgOptions.unshift( chooseOneOptionData );
						}
						fieldDataItem.options = sgOptions;
					}
					// PM_LraGroupOptions
					else if (fieldDataItem.options === "PM_LraGroupOptions") {
						var bIsLraUser = LraGroupsDepot.getInstance().getIsLraUser();
						if (bIsLraUser) {
							// Get array of lra groups
							var lraGroupsData = LraGroupsDepot.getInstance().getGroupsData();
							// Map to options array and update fieldDataItem
							var lraOptions = $.map(lraGroupsData, function(lra) {
								return { "text": lra.MVNO_NAME, "value": lra.CHARGING_ID };
							});
							fieldDataItem.options = lraOptions;
							if (fieldDataItem.value === "PM_SELECT_ALL") {
								// Set value so all options are checked
								fieldDataItem.value = $.map(lraOptions, function(option) {
									return option.value;
								});
							}
						} else {
							// Not an LRA user -> remove this field
							fieldIdsToRemove.push(fieldId);
						}
					}
					// PM_HFotaGroupOptions
					else if (fieldDataItem.options === "PM_HFotaGroupOptions") {
						var bIsHFotaUser = HFotaGroupsDepot.getInstance().getIsHFotaUser();
						if (bIsHFotaUser) {
							// Get array of hfota groups
							var hfotaGroupsData = HFotaGroupsDepot.getInstance().getGroupsData();
							// Map to options array and update fieldDataItem
							var hfotaOptions = $.map(hfotaGroupsData, function(hfota) {
								return { "text": hfota.MVNO_NAME, "value": hfota.CHARGING_ID };
							});
							fieldDataItem.options = hfotaOptions;
							if (fieldDataItem.value === "PM_SELECT_ALL") {
								// Set value so all options are checked
								fieldDataItem.value = $.map(hfotaOptions, function(option) {
									return option.value;
								});
							}
						} else {
							// Not an HFOTA user -> remove this field
							fieldIdsToRemove.push(fieldId);
						}
					}
					// PM_SdmTypeOptions
					else if (fieldDataItem.options === "PM_SdmTypeOptions") {
						var campaignTypeOptions = SdmTypesDepot.getInstance().getTypes();
						fieldDataItem.options = campaignTypeOptions;
					}
					// PM_SeverityOptions
					else if (fieldDataItem.options === "PM_SeverityOptions") {
						var severityOptions = [
							{ text: "Mandatory", value: "Mandatory" },
							{ text: "Optional", value: "Optional" }
						];
						fieldDataItem.options = severityOptions;
					}
				});
				// Remove designated fields from formFields
				$.each(fieldIdsToRemove, function(idx, fieldId) {
					delete eventObj.renderData.formFields.fieldData[fieldId];
				});
			} catch (ex) {
				// Ignore
			}
		});

		_eventMgr.addEventListener("screenRendered", null, function(eventObj) {
			var screen = _getScreen(eventObj);
			var jqScreenDiv = screen.getScreenDiv();
			// Submit default action if <Enter> is hit in text inputs
			try {
				jqScreenDiv.find("input:text").keypress(function (evt) {
					if (evt.keyCode === 10 || evt.keyCode === 13) {
						jqScreenDiv.find("button.default").click();
					}
				});
			} catch (ex) {
				console.log(ex);
			}
			// Set behavior for scrollingDivArea header checkbox
			var jqScrollingDivArea = jqScreenDiv.find("div.scrollingDivArea:has(div.scrollingDivHeader)");
			jqScrollingDivArea.each(function(areaIdx) {
				var jqThisArea = $(this);
				var jqScrollingDivHeaderCB = jqThisArea.find("div.scrollingDivHeader input:checkbox");
				var jqScrollingDivCB = jqThisArea.find("div.scrollingDiv input:checkbox");
				var updateHeaderCBFunc = function() {
					// Update checked property of header checkbox depending if all checkboxes are checked
					jqScrollingDivHeaderCB.prop("checked", (jqScrollingDivCB.length === jqScrollingDivCB.filter(":checked").length));
				};
				jqScrollingDivCB.change(function() {
					updateHeaderCBFunc();
				});
				jqScrollingDivHeaderCB.change(function() {
					jqScrollingDivCB.prop("checked", $(this).is(":checked"));
				});
				updateHeaderCBFunc();
			});

			// Date/Time picker
			var jqDateTimePickers = jqScreenDiv.find('.date-time-picker');
			if (jqDateTimePickers.length > 0) {
				$.each(jqDateTimePickers, function() {
					var jqDateTimePicker = $(this);
					var dateTimePickerOptions = {
						sideBySide: true,
						icons: {
							time: "fa fa-clock-o",
							date: "fa fa-calendar",
							up: "fa fa-chevron-up",
							down: "fa fa-chevron-down",
							previous: "fa fa-chevron-left",
							next: "fa fa-chevron-right",
							today: "fa fa-crosshairs",
							clear: "fa fa-trash-o",
							close: "fa fa-times-circle"
						},
						toolbarPlacement: "top",
						showTodayButton: false,
						showClear: false,
						showClose: true,
						hideOnDateChange: true,
						allowInputToggle: false,
						focusOnShow: false
					};
					if (jqDateTimePicker.data("dtp-future") === true) {
						// Only allow dates/times in the future
						//dateTimePickerOptions.useCurrent = false;
						//dateTimePickerOptions.minDate = moment(new Date()).startOf('day'); // today at 12am
					}
					//var jqDateTimePickerInput = jqDateTimePicker.find("input.date-time-picker-input");
					//var initDateTimeStr = jqDateTimePickerInput.val();
					//if (initDateTimeStr != null && initDateTimeStr !== "") {
					//	dateTimePickerOptions.defaultDate = new Date("12/13/2016 03:42 AM");
					//}
					jqDateTimePicker.datetimepicker(dateTimePickerOptions);
					//jqDateTimePicker.on("dp.change", function(evt) {
						//debugger;
						//$(this).data("DateTimePicker").hide();
					//});
				});
			}
		});

		_eventMgr.addEventListener("screenShown", null, function(eventObj) {
			var screen = _getScreen(eventObj);
			var jqScreenDiv = screen.getScreenDiv();
			// Set width for scrollingDivArea headers
			var jqScrollingDivArea = jqScreenDiv.find("div.scrollingDivArea:has(div.scrollingDivHeader)");
			jqScrollingDivArea.each(function(areaIdx) {
				var jqThisArea = $(this);
				var jqScrollingDivHeaderTableFirstCols = jqThisArea.find("div.scrollingDivHeader table tr:first td");
				var jqScrollingDivTable = jqThisArea.find("div.scrollingDiv table");
				jqScrollingDivTable.find("tr:first").find("td:not(:last-child)").each(function(colIdx) {
					var jqThisCol = $(this);
					$(jqScrollingDivHeaderTableFirstCols[colIdx]).width(jqThisCol.width());
				});
				jqScrollingDivHeaderTableFirstCols.css({opacity: 0, visibility: "visible"}).animate({opacity: 1}, 50);
			});
		});
	}

	function _getPopup(popupIdOrEventObj) {
		var popupId = ($.type(popupIdOrEventObj) === "string") ? popupIdOrEventObj : popupIdOrEventObj.popupId;
		return _popupHash[popupId];
	}

	function _gotoScreen(popupId, screenId, screenRenderData) {
		var popup = _popupHash[popupId];
		if (popup != null) {
			popup.gotoScreen(screenId, screenRenderData);
		}
	}

	function _getScreen(popupIdOrEventObj, screenId) {
		var popup = _getPopup(popupIdOrEventObj);
		if ($.type(popupIdOrEventObj) === "object" && screenId == null) {
			screenId = popupIdOrEventObj.screenId;
		}
		return popup.getScreen(screenId);
	}

	function _showGlobalError(errorContent, bIsModal, errorCallback) {
		if (bIsModal == null) bIsModal = false;
		var popupId = (bIsModal) ? "global-error-modal" : "global-error";
		var screenRenderData = ($.type(errorContent) === "string") ? { resultText: errorContent } : errorContent;
		_openPopup(popupId, screenRenderData);
		if (errorCallback != null) {
			// Trigger errorCallback when user clicks "OK" in error popup
			var pmObj = this;
			var eventType = "buttonClick";
			var targetId = "scr-global-error";
			var listenerFunc = function (eventObj) {
				if (eventObj.value === "ok") {
					pmObj.removeEventListener(eventType, targetId, listenerFunc);
					errorCallback();
				}
			};
			pmObj.addEventListener(eventType, targetId, listenerFunc);
		}
	}

	function _getLastClickedButtonId() {
		return _lastClickedId;
	}

	// ******************************************* //
	// *** EVENT HANDLING ************************ //
	// ******************************************* //

	// Now handled in EventManager.js

	// ******************************************* //
	// *** UTILITY FUNCTIONS ********************* //
	// ******************************************* //

	function _getPopupConfigUrl(popupId) {
		return _CONFIGS_URL_PATH + popupId + ".json"; // e.g. "js/popups/configs/delete-tester-device.json"
	}

	function _getScreenTemplateUrl(templateName) {
		return _TEMPLATES_URL_PATH + templateName + ".ejs"; // e.g. "js/popups/templates/two-button.ejs"
	}

	function _getScreenTemplateUrl2(templateName) {
		return _TEMPLATES_URL_PATH + templateName + ".html"; // e.g. "js/popups/templates/two-button.html"
	}

	function _getScreenPartialUrl(partialName) {
		return _TEMPLATES_URL_PATH + "partials/" + partialName + ".html"; // e.g. "js/popups/templates/partials/two-button-body.html"
	}

	function _getScreenTemplateText(templateName, cbFunc) {
		var templateUrl = _getScreenTemplateUrl2(templateName);
		$.ajax({
			url: templateUrl,
			method: 'GET',
			dataType: 'html',
			success: function (responseText) {
				cbFunc(responseText);
			}
		});
	}

	function _getScreenPartialText(partialName, cbFunc) {
		var partialUrl = _getScreenPartialUrl(partialName);
		$.ajax({
			url: partialUrl,
			method: 'GET',
			dataType: 'html',
			success: function (responseText) {
				cbFunc(responseText);
			}
		});
	}

 	function _getPopupContainerId() {
		return _POPUP_WRAP_ID;
	}

	function _getChooseOneOptionData() {
		return { "text": "- Choose One -", "value": "" };
	}

	// ******************************************* //
	// *** CONSTANTS ***************************** //
	// ******************************************* //

	var _CONFIGS_URL_PATH = "../js/popups/configs/";
	var _TEMPLATES_URL_PATH = "../js/popups/templates/";
	var _POPUP_WRAP_ID = "popupviewer-popup-wrap";

	// ******************************************* //
	// *** PUBLIC METHODS ************************ //
	// ******************************************* //

	return $.extend({
		registerPopupButtons: _registerPopupButtons,
		openPopup: _openPopup,
		getPopup: _getPopup,
		gotoScreen: _gotoScreen,
		getScreen: _getScreen,
		showGlobalError: _showGlobalError,
		getPopupConfigUrl: _getPopupConfigUrl,
		getScreenTemplateUrl: _getScreenTemplateUrl,
		getScreenTemplateText: _getScreenTemplateText,
		getScreenPartialText: _getScreenPartialText,
		getPopupContainerId: _getPopupContainerId,
		getChooseOneOptionData: _getChooseOneOptionData,
		getLastClickedButtonId: _getLastClickedButtonId
	}, _eventMgr);

})();
