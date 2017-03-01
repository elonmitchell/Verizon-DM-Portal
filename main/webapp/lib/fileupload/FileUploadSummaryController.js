/* global EstTimeCalculator, PopupManager, $, EventManager, CommonUtil */
function FileUploadSummaryController(_config) {
	"use strict";

	var _DEBUG;

	var _uploadState;

	var _kbDoneAll;
	var _kbTotalAll;
	var _kbTotalAllPrevious;
	var _estTimeCalculator;

	var _activeProgressObjHash;
	var _transferStateHash;

	var _updateSummaryTimeoutId;

	var _bReplaceWidget;

	var _eventMgr;

	_init();

	function _init() {
		try { _DEBUG = CommonUtil.DEBUG_FLAGS.UPLOAD_SUMMARY_CONTROLLER; } catch(e) {}

		// Initialize member variables
		_resetMemberVars();
		_estTimeCalculator = null;
		_activeProgressObjHash = {};
		_transferStateHash = {};

		// Initialize state
		_updateStateAndDisplay(_STATE_IDLE);

		// Init event manager
		_eventMgr = new EventManager();

		// Initialize widget
		_initWidget();
	}

	function _initWidget() { // good
		// Add widget
		var renderData = {
			stateClass: _getStateClass(),
			percent: null
		};
		_addWidgetWithData(renderData);

		// Hide initially
		_config.fileUploadSumamryView.hideContainer(false); // bHideGradually = false

		// Transfer events
		var eventTypeArray = ["summaryCancel", "summaryClose"];
		$.each(eventTypeArray, function(idx, eventType) {
			_config.fileUploadSumamryView.addEventListener(eventType, null, function() {
				_eventMgr.dispatchEvent({ type: eventType });
			});
		});
	}

	function _start(transferId) { // good
		if (_DEBUG) console.log("\nstart(): ["+transferId+"]");
		if (_uploadState !== _STATE_STARTING && _uploadState !== _STATE_UPLOADING) {
			_resetMemberVars();
		}
		_updateStateAndDisplay(_STATE_STARTING);
	}

	function _updateProgress(progressObj, transferId) { // good
		if (progressObj.hasProgressData()) {
			// Check if this is a new transfer
			var bIsNewTransfer = !_activeProgressObjHash.hasOwnProperty(transferId);

			// Update progress for this uploading transfer
			_activeProgressObjHash[transferId] = progressObj;

			// Sum up kbDone and kbTotal values for all active transfers
			var kbDoneAllTmp = 0;
			var kbTotalAllTmp = 0;
			$.each(_activeProgressObjHash, function(transferIdTmp, progressObjTmp) {
				if (progressObjTmp != null) {
					kbDoneAllTmp += progressObjTmp.getKbDone();
					kbTotalAllTmp += progressObjTmp.getKbTotal();
				}
			});
			_kbDoneAll = kbDoneAllTmp;
			_kbTotalAll = kbTotalAllTmp;
			if (_DEBUG) console.log("updateProgress(): ["+transferId+"] _kbDoneAll = " + _kbDoneAll + ", _kbTotalAll = " + _kbTotalAll);

			// Add snapshot for estimated time calculation
			if (_estTimeCalculator == null || bIsNewTransfer || _kbTotalAll !== _kbTotalAllPrevious) {
				if (_DEBUG) console.log("updateProgress(): ["+transferId+"] new EstTimeCalculator");
				_estTimeCalculator = new EstTimeCalculator(_kbTotalAll, _DEBUG);
				_kbTotalAllPrevious = _kbTotalAll;
			}
			_estTimeCalculator.addSnapshot(_kbDoneAll);

			// Update upload state
			_updateStateAndDisplay(_STATE_UPLOADING);
		}
	}

	function _done(progressObj, transferId) { // good
		if (_DEBUG) console.log("done(): ["+transferId+"]");

		// This upload is no longer active -> remove from active progress hash and kill calculator
		delete _activeProgressObjHash[transferId];
		_estTimeCalculator = null;

		// Increment number of failed uploads if necessary
		if (progressObj.isSuccess()) {
			_transferStateHash[transferId] = _STATE_SUCCESS;
		} else {
			_transferStateHash[transferId] = _STATE_ERROR;
		}

		// See if there are any other active uploads
		var bAnyUploadActive = _isAnyUploadActive();
		if (!bAnyUploadActive) {
			// No more active uploads -> Update state
			_updateStateAndDisplay(_STATE_FINISHED);
		}
	}

	function _cancel(progressObj, transferId) { // good
		if (_DEBUG) console.log("cancel(): ["+transferId+"]");
		// This upload is no longer active -> remove from active progress hash and kill calculator
		delete _activeProgressObjHash[transferId];
		_estTimeCalculator = null;

		// Mark transfer as cancelled
		_transferStateHash[transferId] = _STATE_CANCELLED;

		// See if there are any other active uploads
		var bAnyUploadActive = _isAnyUploadActive();
		if (!bAnyUploadActive) {
			// No more active uploads -> Update state
			_updateStateAndDisplay(_STATE_FINISHED);
		}
	}

	function _clear(transferId) { // good
		if (_DEBUG) console.log("clear(): ["+transferId+"]");
		delete _transferStateHash[transferId];
		if (!$.isEmptyObject(_transferStateHash)) {
			if (_uploadState === _STATE_SUCCESS ||
					_uploadState === _STATE_ERROR ||
					_uploadState === _STATE_CANCELLED) {
				_updateStateAndDisplay(_STATE_FINISHED);
			}
		}
	}

	function _clearAll() { // good
		if (_DEBUG) console.log("clearAll()");
		// Reset state
		_transferStateHash = {};
		// Hide container
		_hideContainer();
	}

	// ******************************************* //
	// *** GET/UPDATE UPLOAD STATE *************** //
	// ******************************************* //

	function _getStateClass() { // good
		var stateClass = "";
		if (_uploadState === _STATE_IDLE) stateClass = "idle";
		else if (_uploadState === _STATE_STARTING) stateClass = "uploading";
		else if (_uploadState === _STATE_UPLOADING) stateClass = "uploading";
		else if (_uploadState === _STATE_SUCCESS) stateClass = "success";
		else if (_uploadState === _STATE_CANCELLED) stateClass = "cancelled";
		else if (_uploadState === _STATE_ERROR) stateClass = "error";
		return stateClass;
	}

	function _updateStateAndDisplay(newState) { // good
		if (newState === _STATE_FINISHED) {
			// Add up number of cancelled and failed uploads
			var numCancelledUploads = 0;
			var numFailedUploads = 0;
			$.each(_transferStateHash, function(transferId, transferState) {
				if (transferState === _STATE_CANCELLED) numCancelledUploads++;
				else if (transferState === _STATE_ERROR) numFailedUploads++;
			});
			// Set state based on num counts
			if (numCancelledUploads > 0) {
				newState = _STATE_CANCELLED;
			} else if (numFailedUploads > 0) {
				newState = _STATE_ERROR;
			} else {
				newState = _STATE_SUCCESS;
			}
		}

		// See if state has changed
		var bStateChanged = false;
		if (newState !== _uploadState) {
			// State has changed
			if (_DEBUG) console.log("updateStateAndDisplay(): {"+_getDebugStateStr(newState)+"}");
			_uploadState = newState;
			_bReplaceWidget = true;
			bStateChanged = true;

			if (_uploadState === _STATE_IDLE) {
				_resetMemberVars();
			} else if (_uploadState === _STATE_STARTING || _uploadState === _STATE_UPLOADING) {
				// Show summary container
				_showContainer();
				// Start updating summary
				_updateSummary();
			} else if ((_uploadState === _STATE_SUCCESS) ||
								 (_uploadState === _STATE_ERROR) ||
								 (_uploadState === _STATE_CANCELLED)) {
				// Cancel any pending status update
				if (_updateSummaryTimeoutId != null) {
					window.clearTimeout(_updateSummaryTimeoutId);
				}
				// Update summary
				_updateSummary();
			}
		}
		return bStateChanged;
	}

	// ******************************************* //
	// *** UPDATE SUMMARY ************************ //
	// ******************************************* //

	function _updateSummary() { // good
		var renderData;
		if (_kbDoneAll == null || _kbTotalAll == null) {
			renderData = {
				stateClass: _getStateClass(),
				percent: null,
				kbDone: null,
				kbTotal: null,
				estSecondsLeft: null
			};
		} else {
			// Set render data
			var percentDone = Math.round((_kbDoneAll / _kbTotalAll) * 100);
			if (_DEBUG) console.log("updateSummary(): percentDone = ("+_kbDoneAll+"/"+_kbTotalAll+") = "+percentDone);
			var estSecondsLeft = _getEstTimeLeft();
			renderData = {
				stateClass: _getStateClass(),
				percent: percentDone,
				kbDone: Math.round(_kbDoneAll),
				kbTotal: Math.round(_kbTotalAll),
				estSecondsLeft: estSecondsLeft
			};
		}
		if (_bReplaceWidget) {
			// State changed recently -> Replace entire widget
			_replaceWidgetWithData(renderData);
		} else {
			// Replace data inside widget
			_config.fileUploadSumamryView.updateWidgetData(renderData);
		}
		if (_uploadState === _STATE_UPLOADING) {
			// Create timeout to update summary again
			_updateSummaryTimeoutId = window.setTimeout(function() { _updateSummary(); }, _UDPATE_SUMMARY_INTERVAL_MILLISECONDS);
		} else {
			_updateSummaryTimeoutId = null;
		}
	}

	// ******************************************* //
	// *** ADD/UPDATE/REPLACE WIDGET FUNCTIONS *** //
	// ******************************************* //

	function _addWidgetWithData(renderData) {
		_config.fileUploadSumamryView.addWidgetWithData(renderData);
		_bReplaceWidget = false;
	}

	function _replaceWidgetWithData(renderData) {
		_config.fileUploadSumamryView.replaceWidgetWithData(renderData);
		_bReplaceWidget = false;
	}

	// ******************************************* //
	// *** HIDE/SHOW CONTAINER FUNCTIONS ********* //
	// ******************************************* //

	function _hideContainer() {
		_config.fileUploadSumamryView.hideContainer();
		_updateStateAndDisplay(_STATE_IDLE);
	}

	function _showContainer() {
		_config.fileUploadSumamryView.showContainer();
	}

	// ******************************************* //
	// *** ESTIMATED TIME LEFT FUNCTIONS ********* //
	// ******************************************* //

	function _getEstTimeLeft(bIsNewTransfer) { // good
		var estSecondsLeft = null;
		if (_estTimeCalculator != null) {
			estSecondsLeft = _estTimeCalculator.getSecondsLeft();
		}
		return estSecondsLeft;
	}

	// ******************************************* //
	// *** UTILITY FUNCTIONS ********************* //
	// ******************************************* //

	function _resetMemberVars() { // good
		if (_DEBUG) console.log("resetMemberVars()");
		_kbDoneAll = null;
		_kbTotalAll = null;
		_kbTotalAllPrevious = null;
	}

	function _isAnyUploadActive() { // good
		return !$.isEmptyObject(_activeProgressObjHash);
	}

	function _getDebugStateStr(state) { // good
		var stateStrArray = ["STATE_IDLE", "STATE_STARTING", "STATE_UPLOADING", "STATE_CANCELLED", "STATE_SUCCESS", "STATE_ERROR", "STATE_FINISHED"];
		return stateStrArray[state];
	}

	// ******************************************* //
	// *** CONSTANTS ***************************** //
	// ******************************************* //

	var _STATE_IDLE = 0;
	var _STATE_STARTING = 1;
	var _STATE_UPLOADING = 2;
	var _STATE_CANCELLED = 3;
	var _STATE_SUCCESS = 4;
	var _STATE_ERROR = 5;
	var _STATE_FINISHED = 6;

	var _UDPATE_SUMMARY_INTERVAL_MILLISECONDS = 1000;

	// ******************************************* //
	// *** PUBLIC METHODS ************************ //
	// ******************************************* //

	return $.extend({
		start: _start,
		updateProgress: _updateProgress,
		done: _done,
		cancel: _cancel,
		clear: _clear,
		clearAll: _clearAll
	}, _eventMgr);

}
