/* global FileUploadProgress, $, EventManager, CommonUtil */
function FileUploadTransfer(uploadURL, jqFileElem, uploadData, progressURL) {
	var _id;

	var _uploadURL;
	var _jqFileElem;
	var _uploadData;
	var _progressURL;

	var _eventMgr;

	var _bIsUploading;

	var _ajaxFileUploadObj;

	var _currentProgressObj;

	var _monitorProgressTimeoutId;

	_init(uploadURL, jqFileElem, uploadData, progressURL);

	function _init(uploadURL, jqFileElem, uploadData, progressURL) {
		// Create a unique id
		_id = new Date().getTime() + "";
		// Cache input params
		_uploadURL = uploadURL;
		_jqFileElem = jqFileElem;
		_uploadData = uploadData;
		_progressURL = progressURL;

		// Init current progress object
		_currentProgressObj = new FileUploadProgress(null, null);

		// Init event manager
		_eventMgr = new EventManager();

		// Set upload flag to false
		_bIsUploading = false;
	}

	function _start() {
		_bIsUploading = true;
		_startUpload();
		window.setTimeout(function() { _monitorProgress(); }, MONITOR_PROGRESS_INTERVAL_MILLISECONDS);
		//_monitorProgress();
	}

	function _startUpload() {
		// Start upload
		_ajaxFileUploadObj = $.ajaxFileUpload({
			url: _uploadURL,
			dataType: 'json',
			type: 'POST',
			data: _uploadData,
			fileElement: _jqFileElem,
			error: function (jqXHR, textStatus, errorThrown) {
				_bIsUploading = false;
				CommonUtil.onAjaxFail(jqXHR, textStatus, errorThrown.toString());
			},
			success: function (resultData, status) {
				_bIsUploading = false;

				// Update progress object
				_currentProgressObj.updateResultData(resultData);

				// Dispatch progress to listeners
				var eventObj = {
					type: "uploadDone",
					progressObj: _currentProgressObj
				};
				_eventMgr.dispatchEvent(eventObj);
			}
		});
		// Send event saying upload has started
		var eventObj = {
			type: "uploadStart"
		};
		_eventMgr.dispatchEvent(eventObj, this);
	}

	function _cancel() {
		// Cancel progress polling
		window.clearTimeout(_monitorProgressTimeoutId);

		// Cancel upload
		_ajaxFileUploadObj.abort();

		// Set upload flag to false
		_bIsUploading = false;

		// Update progress
		_currentProgressObj.setAsCancelled();

		// Dispatch cancel event to listeners
		var eventObj = {
			type: "uploadCancel",
			progressObj: _currentProgressObj
		};
		_eventMgr.dispatchEvent(eventObj, this);
	}

	// ******************************************* //
	// *** PROGRESS FUNCTIONS ******************** //
	// ******************************************* //

	function _monitorProgress() {
		if (_bIsUploading && _progressURL != null) {
			// Get progress data
			$.ajax({
				url: _progressURL,
				dataType: "json",
				type: 'POST',
				data: ""
			}).fail(CommonUtil.onAjaxFail).done(function (progressData) {
				// Update progress object
				_currentProgressObj.updateProgressData(progressData);

				// Dispatch progress to listeners
				if (_currentProgressObj.isSuccess()) {
					var eventObj = {
						type: "uploadProgress",
						progressObj: _currentProgressObj
					};
					_eventMgr.dispatchEvent(eventObj, this);
				}

				// Monitor progress again after an interval
				if (_bIsUploading) {
					_monitorProgressTimeoutId = window.setTimeout(function() {
																				_monitorProgress();
																			}, MONITOR_PROGRESS_INTERVAL_MILLISECONDS);
				}
			});
		}
	}

	function _getCurrentProgress() {
		return _currentProgressObj;
	}

	// ******************************************* //
	// *** UTILITY FUNCTIONS ********************* //
	// ******************************************* //

	function _getId() {
		return _id;
	}

	function _getFilename() {
		var filename = _jqFileElem.val().replace(/^.*[\\\/]/, '');
		//if (filename == null || filename == "") filename = "OTA_Mecha_S_VZW_1.70-1.13_release_200478.upc"; // DEBUG
		return filename;
	}

	function _isUploading() {
		return _bIsUploading;
	}

	// ******************************************* //
	// *** MISC GLOBAL VARS ********************** //
	// ******************************************* //

	//var MONITOR_PROGRESS_INTERVAL_MILLISECONDS = 10000;
	var MONITOR_PROGRESS_INTERVAL_MILLISECONDS = 5000;

	// ******************************************* //
	// *** PUBLIC METHODS ************************ //
	// ******************************************* //

	return $.extend({
		start: _start,
		cancel: _cancel,
		getCurrentProgress: _getCurrentProgress,
		getId: _getId,
		getFilename: _getFilename,
		isUploading: _isUploading
	}, _eventMgr);
}