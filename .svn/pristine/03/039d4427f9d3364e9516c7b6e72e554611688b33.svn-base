/* global EstTimeCalculator, EJS, $, EventManager */
function FileUploadStatusController(_config) {
	"use strict";

	var _id;
	var _jqUploadWidgetList;
	var _jqUploadWidget;
	var _jqDataContainer;
	var _uploadState;
	var _uploadStateMessage;
	var _widgetId;
	var _kbDone;
	var _kbTotal;
	var _estTimeCalculator;
	var _bReplaceWidget;
	var _updateStatusTimeoutId;
	var _eventMgr;

	_init();

	function _init() {
		// Create a unique id
		_id = new Date().getTime() + "";

		// Init event manager
		_eventMgr = new EventManager();

		// Misc
		_estTimeCalculator = null;
	}

	function _showStatusWidget() {
		// Get the widget list
		_jqUploadWidgetList = $('.'+_UPLOAD_WIDGET_LIST_CLASS, _config.jqStatusContainer);
		if (_jqUploadWidgetList.length === 0) {
			// Widget list doesn't exist -> add it to the status container
			_config.jqStatusContainer.append('<div class="'+_UPLOAD_WIDGET_LIST_CLASS+'">');
			_jqUploadWidgetList = $('.'+_UPLOAD_WIDGET_LIST_CLASS, _config.jqStatusContainer);
		}

		// Create a unique id
		_widgetId = new Date().getTime();

		// Set the initial upload state
		_setUploadState(_STATE_IDLE);

		// Add widget
		_updateStatus();
	}

	function _setProgress(progressObj) {
		// Update progress meter
		_updateProgress(progressObj);

		// Set as cancelled or done
		if (progressObj.isCancelled()) {
			_cancel(progressObj);
		} else if (progressObj.isDone()) {
			_done(progressObj);
		}
	}

	function _updateProgress(progressObj) {
		if (progressObj.hasProgressData()) {
			// Save kbDone and kbTotal
			_kbDone = progressObj.getKbDone();
			_kbTotal = progressObj.getKbTotal();

			// Add snapshot for estimated time calculation
			if (_estTimeCalculator == null) {
				_estTimeCalculator = new EstTimeCalculator(_kbTotal);
			}
			_estTimeCalculator.addSnapshot(_kbDone);

			// Update upload state
			var uploadState = _STATE_UPLOADING;
			_setUploadState(uploadState);

			// Kick off status updates if necessary
			if (uploadState === _STATE_UPLOADING && _updateStatusTimeoutId == null) {
				_updateStatus();
			}
		}
	}

	function _done(progressObj) {
		// Cancel any pending status update
		if (_updateStatusTimeoutId != null) {
			window.clearTimeout(_updateStatusTimeoutId);
			_updateStatusTimeoutId = null;
		}

		var matchArray;
		if (progressObj.isSuccess()) {
			// Upload success -> update state
			_setUploadState(_STATE_SUCCESS);
			// Update filename if necessary
			var successStr = progressObj.getSuccessMessage();
			if (successStr != null) {
				matchArray = successStr.match(/^(Firmware imported successfully) - (.*)$/);
				if (matchArray != null && matchArray.length === 3) {
					_uploadStateMessage = matchArray[1];
					_config.filename = matchArray[2];
				}
			}
		} else if (progressObj.isError()) {
			// Upload error -> update state
			_setUploadState(_STATE_ERROR);
			// Update filename if necessary
			_uploadStateMessage = progressObj.getErrorMessage();
			if (_uploadStateMessage != null) {
				matchArray = _uploadStateMessage.match(/^(Firmware with the name already exists) - (.*)$/);
				if (matchArray != null && matchArray.length === 3) {
					_uploadStateMessage = matchArray[1];
					_config.filename = matchArray[2];
				}
			}
		}

		// Update status according to new state and filename
		_updateStatus();
	}

	function _cancel() {
		_setUploadState(_STATE_CANCELLED);
		_updateStatus();
		window.clearTimeout(_updateStatusTimeoutId); // cancel any pending status update
		_updateStatusTimeoutId = null;
	}

	function _clear() {
		_hideContainer();
	}

	// ******************************************* //
	// *** UPDATE STATUS ************************* //
	// ******************************************* //

	function _updateStatus() {
		// Set the render data
		var percentDone = null;
		var estSecondsLeft = null;
		if (_uploadState === _STATE_SUCCESS || _uploadState === _STATE_ERROR) {
			// Upload is done, set percent to 100
			percentDone = 100;
		} else if (_kbDone != null && _kbTotal != null) {
			percentDone = Math.round((_kbDone / _kbTotal) * 100);
			//estSecondsLeft = _getEstSecondsLeft();
			estSecondsLeft = null; // don't show estimated time for individual transfer
		}
		var renderData  = {
			id: _widgetId,
			filename: _config.filename,
			stateClass: _getStateClass(),
			stateMessage: _uploadStateMessage,
			statusDataClass: _UPLOAD_WIDGET_DATA_CLASS,
			percent: percentDone,
			kbDone: Math.round(_kbDone),
			kbTotal: Math.round(_kbTotal),
			estSecondsLeft: estSecondsLeft
		};

		// Update widget
		if (_bReplaceWidget) {
			// State changed recently -> Replace entire widget
			_replaceWidgetWithData(renderData)
		} else {
			// Replace data inside widget
			_updateWidgetData(renderData);
		}

		// Update status again after an interval if necessary
		if (_uploadState === _STATE_UPLOADING) {
			_updateStatusTimeoutId = window.setTimeout(function() { _updateStatus(); }, _UDPATE_STATUS_INTERVAL_MILLISECONDS);
		}
	}

	// ******************************************* //
	// *** ADD/UPDATE/REPLACE WIDGET FUNCTIONS *** //
	// ******************************************* //

	function _replaceWidgetWithData(renderData) {
		// Render new widget
		var widgetHTML = _renderWidgetHTML(renderData);
		// Replace old widget with this widget
		if (_jqUploadWidget == null) {
			_jqUploadWidgetList.prepend(widgetHTML);
		} else {
			_jqUploadWidget.replaceWith(widgetHTML);
			_jqUploadWidget.remove();
		}
		// Get new widget and fix content
		_jqUploadWidget = _getWidgetFromList();
		_fixWidgetContent();
		// Store widget's data container
		_jqDataContainer = _jqUploadWidget.find('.'+_UPLOAD_WIDGET_DATA_CLASS).parent();
		// Set replace widget flag to false
		_bReplaceWidget = false;
	}

	function _updateWidgetData(renderData) {
		// Render new progress data and insert it into data container
		var dataHTML = _renderWidgetDataHTML(renderData);
		_jqDataContainer.html(dataHTML);
	}

	function _fixWidgetContent() {
		// Make sure filename appears on one line
		_jqUploadWidget.find(".ellipsis").ellipsis({ lines: 1 });

		// Create jQuery UI buttons
		var jqCancelButton = _jqUploadWidget.find('button.status-widget-cancel');
		var jqCloseButton = _jqUploadWidget.find('button.status-widget-close');
		jqCancelButton.button({ icons: { primary: "ui-icon-cancel" }, text: false });
		jqCloseButton.button({ icons: { primary: "ui-icon-close" }, text: false });

		// Add functionality for close button
		var self = this;
		jqCancelButton.click(function() {
			// Dispatch status close event
			var eventObj = { type: "statusCancel" };
			_eventMgr.dispatchEvent(eventObj);
		});
		jqCloseButton.click(function() {
			// Dispatch status close event
			var eventObj = { type: "statusClose" };
			_eventMgr.dispatchEvent(eventObj);
		});
	}

	// ******************************************* //
	// *** RENDER FUNCTIONS ********************** //
	// ******************************************* //

	function _renderWidgetHTML(renderData) {
		var templateUrl = _getWidgetTemplateUrl();
		var widgetHTML = new EJS({url:templateUrl}).render(renderData);
		return widgetHTML;
	}

	function _renderWidgetDataHTML(renderData) {
		var templateUrl = _getWidgetDataTemplateUrl();
		var dataHTML = new EJS({url:templateUrl}).render(renderData);
		return dataHTML;
	}

	// ******************************************* //
	// *** HIDE/SHOW CONTAINER FUNCTIONS ********* //
	// ******************************************* //

	function _hideContainer() {
		_jqUploadWidget.remove();
	}

	function _showContainer() {
		_jqUploadWidget.show();
	}

	// ******************************************* //
	// *** ESTIMATED TIME LEFT FUNCTIONS ********* //
	// ******************************************* //

	function _getEstSecondsLeft() {
		var estSecondsLeft = null;
		if (_estTimeCalculator != null) {
			estSecondsLeft = _estTimeCalculator.getSecondsLeft();
		}
		return estSecondsLeft;
	}

	// ******************************************* //
	// *** GET/SET UPLOAD STATE ****************** //
	// ******************************************* //

	function _getStateClass() {
		var stateClass = "";
		if (_uploadState === _STATE_IDLE) stateClass = "idle";
		else if (_uploadState === _STATE_UPLOADING) stateClass = "uploading";
		else if (_uploadState === _STATE_SUCCESS) stateClass = "success";
		else if (_uploadState === _STATE_CANCELLED) stateClass = "cancelled";
		else if (_uploadState === _STATE_ERROR) stateClass = "error";
		return stateClass;
	}

	function _setUploadState(newState) {
		var bStateChanged = false;
		if (_uploadState != newState) {
			// State changed
			_uploadState = newState;
			_bReplaceWidget = true;
			bStateChanged = true;
			_uploadStateMessage = null;
		}
		return bStateChanged;
	}

	// ******************************************* //
	// *** UTILITY FUNCTIONS ********************* //
	// ******************************************* //

	function _getId() {
		return _id;
	}

	function _getWidgetFromList() {
		return _jqUploadWidgetList.find('#'+_UPLOAD_WIDGET_CLASS+'-'+_widgetId);
	}

	function _getWidgetTemplateUrl() {
		return _TEMPLATES_URL_PATH + _UPLOAD_WIDGET_CLASS + ".ejs"; // e.g. "js/fileupload/templates/status-widget.ejs"
	}

	function _getWidgetDataTemplateUrl() {
		return _TEMPLATES_URL_PATH + _UPLOAD_WIDGET_DATA_CLASS + ".ejs"; // e.g. "js/fileupload/templates/status-widget-data.ejs"
	}

	// ******************************************* //
	// *** CONSTANTS ***************************** //
	// ******************************************* //

	var _STATE_IDLE = 0;
	var _STATE_UPLOADING = 1;
	var _STATE_SUCCESS = 2;
	var _STATE_CANCELLED = 3;
	var _STATE_ERROR = 4;

	var _UPLOAD_WIDGET_LIST_CLASS = "status-widget-list";
	var _UPLOAD_WIDGET_CLASS = "status-widget";
	var _UPLOAD_WIDGET_DATA_CLASS = "status-widget-data";

	var _TEMPLATES_URL_PATH = "js/fileupload/templates/"; // TODO: pass in path via constructor
	var _UDPATE_STATUS_INTERVAL_MILLISECONDS = 1000;

	// ******************************************* //
	// *** PUBLIC METHODS ************************ //
	// ******************************************* //

	return $.extend({
		showStatusWidget: _showStatusWidget,
		setProgress: _setProgress,
		updateProgress: _updateProgress,
		done: _done,
		cancel: _cancel,
		clear: _clear,
		getId: _getId
	}, _eventMgr);
}