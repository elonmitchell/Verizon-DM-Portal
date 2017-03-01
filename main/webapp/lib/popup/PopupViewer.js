/* global $, PopupManager, DoubleHash, Ractive */

var PopupViewer = (function () {
	"use strict";

	var _instance; // Stores a reference to the Singleton
	var _ractiveTemplatesHash;
	var _ractivePartialsHash;

	_initObject();

	function _initObject() {
		// Initialize ractive templates
		_ractiveTemplatesHash = {};
		var templateNames = ["wait", "two-button", "simple"];
		$.each(templateNames, function(idx, templateName) {
			PopupManager.getScreenTemplateText(templateName, function(templateText) {
				_ractiveTemplatesHash[templateName] = Ractive.parse(templateText);
			});
		});

		// Initialize ractive partials
		_ractivePartialsHash = {};
		var partialNames = ["two-button-body",
			"field-layout-loop", "field-group", "alerts-area", "bottom-note",
			"note", "text", "password", "hidden", "view", "select", "textarea", "checkbox", "file",
			"checkboxGroup", "radioGroup", "dateTimeMeridian",
			"field-attributes", "help-blocks"];
		$.each(partialNames, function(idx, partialName) {
			PopupManager.getScreenPartialText(partialName, function(partialText) {
				//_ractivePartialsHash[partialName] = Ractive.parse(partialText);
				Ractive.partials[partialName] = Ractive.parse(partialText);
			});
		});
	}

	function _initInstance() { // Singleton
		var _popupScreenWinDataDoubleHash; // maps popupId + screenId -> winData
		var _jqPopupContainer;

		var _popupOptions;
		var _popupModalOptions;

		_init();

		function _init() {
			// Initialize member vars
			_popupScreenWinDataDoubleHash = new DoubleHash();

			$.fn.popup.defaults.autoopen = true;
			$.fn.popup.defaults.transition = 'all 0.3s';
			$.fn.popup.defaults.autoopen = true;
			$.fn.popup.defaults.detach = true;
			_popupOptions = {};
			_popupModalOptions = $.extend({}, _popupOptions, {
				escape: false,
				blur: false
			});

			//$.fn.popup.defaults.pagecontainer = '.page-container';

			// Set the popup container
			var popupContainerId = PopupManager.getPopupContainerId();
			_jqPopupContainer = $('#'+popupContainerId);
			if (_jqPopupContainer.length === 0) { // Container doesn't exist -> create it
				$('body').append('<div id="'+popupContainerId+'"></div>');
				_jqPopupContainer = $('#'+popupContainerId);
			}

		}

		function _renderScreen(inputPopupId, inputScreenId, screenTemplateName, screenRenderData, extraStyles) {
			// Get the popup div
			var domPopupId = _getPopupDomId(inputPopupId); // e.g. "delete-tester-device__pm-popup"
			//var jqPopupDiv = _getPopupDiv(domPopupId);

			// Get the screen container div
			//var domScreenContainerId = _getPopupScreenContainerDomId(domPopupId, inputScreenId); // e.g. "delete-tester-device__pm-popup__scr-DTD-confirm__container"
			//var jqScreenContainer = _getPopupScreenContainerDiv(domPopupId, domScreenContainerId, jqPopupDiv);
			var jqScreenContainer = _jqPopupContainer;

			// Render the screen content
			var domScreenId = _getPopupScreenDomId(domPopupId, inputScreenId); // e.g. "delete-tester-device__pm-popup__scr-DTD-confirm"
			//var globalRenderData = {
			//	domPopupId: domPopupId,
			//	domScreenId: domScreenId,
			//	extraStyles: extraStyles
			//};
			//$.extend(globalRenderData, screenRenderData);
			screenRenderData.domPopupId = domPopupId;
			screenRenderData.domScreenId = domScreenId;
			screenRenderData.extraStyles = extraStyles;
			//var screenHTML = _renderTemplate(screenTemplateName, { g_RD: globalRenderData });
			//jqScreenContainer.html(screenHTML);
			var getLinearFieldLayoutArray = function(fieldLayoutArray, colWidth) {
				if (colWidth == null) colWidth = 12;
				var linearFieldLayoutArray = [];
				var fieldIdArrayForCurrentColWidth = [];
				$.each(fieldLayoutArray, function (idx, fieldLayoutItem) {
					if (typeof(fieldLayoutItem) === "string") {
						fieldIdArrayForCurrentColWidth.push(fieldLayoutItem);
					} else { // new column
						if (fieldIdArrayForCurrentColWidth.length > 0) {
							var colWidthFieldIdHash = {};
							colWidthFieldIdHash[colWidth] = fieldIdArrayForCurrentColWidth;
							linearFieldLayoutArray.push(colWidthFieldIdHash);
						}
						var newArray = getLinearFieldLayoutArray(fieldLayoutItem, colWidth / 2);
						if (newArray.length > 0) linearFieldLayoutArray = linearFieldLayoutArray.concat(newArray);
						fieldIdArrayForCurrentColWidth = [];
					}
				});
				if (fieldIdArrayForCurrentColWidth.length > 0) {
					var colWidthFieldIdHash = {};
					colWidthFieldIdHash[colWidth] = fieldIdArrayForCurrentColWidth;
					linearFieldLayoutArray.push(colWidthFieldIdHash);
				}
				return linearFieldLayoutArray;
			};
			var renderDataParam = {
				g_RD: screenRenderData,
				getLinearFieldLayoutArray: function(fieldLayoutArray) {
					var arr = getLinearFieldLayoutArray(fieldLayoutArray);
					return arr;
				}
			};
			var screenRactive = _renderTemplate2(jqScreenContainer, screenTemplateName, renderDataParam);

			// Store data
			_setWinData(inputPopupId, inputScreenId, domPopupId, domScreenId);

			return screenRactive;
		}

		function _showScreen(popupId, screenId, bIsModal, screenShownCbFunc, screenClosedCbFunc) {
			// Lookup the popup screen div
			var jqPopupScreenDiv = _getScreenDiv(popupId, screenId);

			var options = (bIsModal) ? _popupModalOptions : _popupOptions;
			// Add screen shown handler
			options.opentransitionend = function(evt) {
				_onScreenShown(evt);
				if (screenShownCbFunc != null) screenShownCbFunc(evt);
			};
			// Add screen closed handler
			options.closetransitionend = function(evt) {
				if (screenClosedCbFunc != null) screenClosedCbFunc(evt);
				_onScreenClosed(evt);
			};
			jqPopupScreenDiv.popup(options);
		}

		function _onScreenShown(evt) {
			// Nothing to do
		}

		function _closePopup(popupId, screenId) {
			var jqPopupScreenDiv = _getScreenDiv(popupId, screenId);
			jqPopupScreenDiv.popup('hide');
		}

		function _closeScreen(popupId, screenId) {
			var jqPopupScreenDiv = _getScreenDiv(popupId, screenId);
			jqPopupScreenDiv.popup('hideKeepBackground');
		}

		function _onScreenClosed(evt) {
			// Remove popup div
			//$(evt.target).parents('.pm-parent').remove();
		}

		function _resizePopup(popupId, screenId) {
			alert("_resizePopup(): TODO");
		}

		function _getScreenDiv(popupId, screenId) {
			var jqScreenDiv = $();
			if (popupId != null && screenId != null) {
				var winData = _getWinData(popupId, screenId);
				if (winData != null) {
					jqScreenDiv = $('#'+winData.domScreenId);
				}
			}
			return jqScreenDiv;
		}

		// ******************************************* //
		// *** UTILITY FUNCTIONS ********************* //
		// ******************************************* //

		function _getPopupDomId(popupId) {
			return popupId + "__pm-popup"; // e.g. "delete-tester-device__pm-popup"
		}

		function _getPopupDiv(domPopupId) {
			var jqPopupDiv = $('#'+domPopupId, _jqPopupContainer);
			if (jqPopupDiv.length === 0) { // Div doesn't exist -> create it
				_jqPopupContainer.append('<div id="'+domPopupId+'" class="pm-parent"></div>');
				jqPopupDiv = _getPopupDiv(domPopupId);
			}
			return jqPopupDiv;
		}

		function _getPopupScreenContainerDomId(domPopupId, screenId) {
			return _getPopupScreenDomId(domPopupId, screenId) + "__container"; // e.g. "delete-tester-device__pm-popup__scr-DTD-confirm__container"
		}

		function _getPopupScreenContainerDiv(domPopupId, domScreenContainerId, jqPopupDiv) {
			var jqScreenDiv = $('#'+domScreenContainerId, jqPopupDiv);
			if (jqScreenDiv.length === 0) { // Div doesn't exist -> create it
				jqPopupDiv.append('<div id="'+domScreenContainerId+'" data-popupId="'+domPopupId+'"></div>');
				jqScreenDiv = _getPopupScreenContainerDiv(domPopupId, domScreenContainerId, jqPopupDiv);
			}
			return jqScreenDiv;
		}

		function _getPopupScreenDomId(domPopupId, screenId) {
			return domPopupId + "__" + screenId; // e.g. "delete-tester-device__pm-popup__scr-DTD-confirm"
		}


		function _setWinData(popupId, screenId, domPopupId, domScreenId) {
			_popupScreenWinDataDoubleHash.set(popupId, screenId, {
				domPopupId: domPopupId,
				domScreenId: domScreenId
			});
		}

		function _getWinData(popupId, screenId) {
			return _popupScreenWinDataDoubleHash.get(popupId, screenId);
		}

		function _renderTemplate(templateName, ejsRenderData) {
			var templateUrl = PopupManager.getScreenTemplateUrl(templateName);
			return new EJS({url: templateUrl }).render(ejsRenderData);
		}

		function _renderTemplate2(jqScreenContainer, templateName, renderData) {
			var templateContent = _ractiveTemplatesHash[templateName];
			if (templateContent == null) {
				alert("Template '"+templateName+"' does not exist");
			} else {
				return new Ractive({
					el: jqScreenContainer,
					template: templateContent,
					data: renderData,
					magic: true
				});
			}
		}

		// ******************************************* //
		// *** PUBLIC METHODS ************************ //
		// ******************************************* //

		return {
			renderScreen: _renderScreen,
			showScreen: _showScreen,
			closePopup: _closePopup,
			closeScreen: _closeScreen,
			resizePopup: _resizePopup,
			getScreenDiv: _getScreenDiv
		};
	}

  return {
    // Get the Singleton _instance or create one if it doesn't exit
    getInstance: function () {
      if (!_instance) {
        _instance = _initInstance();
      }
      return _instance;
    }
  };

})();
