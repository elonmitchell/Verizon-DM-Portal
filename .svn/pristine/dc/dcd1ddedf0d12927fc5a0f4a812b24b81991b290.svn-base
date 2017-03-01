/* global FileUploadProgress, $, EventManager */
function FakeFileUploadTransfer(uploadURL, jqFileElem, uploadData, progressURL) {
	var _id;

	var _uploadURL;
	var _jqFileElem;
	var _uploadData;
	var _progressURL;

	var _eventMgr;

	var _bIsUploading;

	var _currentProgressObj;

	var _progressDataArray;
	var _progressDataArrayIdx;

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
		// Send event saying upload has started
		var eventObj = {
			type: "uploadStart"
		};
		_eventMgr.dispatchEvent(eventObj, this);
		// Monitor progress after a delay
		window.setTimeout(function() { _monitorProgress(); }, MONITOR_PROGRESS_INTERVAL_MILLISECONDS);
	}

	function _cancel() {
		// Cancel transfer
		window.clearTimeout(_monitorProgressTimeoutId);

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
		if (_bIsUploading) {
			// Get fake progress data
			var progressData = _getFakeUploadProgressData();
			if (progressData == null) {
				// Upload is done
				_bIsUploading = false;
				// Create fake result data
				var resultData;
				//resultData = { "response": { "statusCode": "200" } }; // success
				resultData = {"response":{"statusCode":"301","statusMessage":"Internal server error. Please try again later"}}; // error
				// Update progress object
				if (_currentProgressObj == null) {
					_currentProgressObj = new FileUploadProgress(null, resultData);
				} else {
					_currentProgressObj.updateResultData(resultData);
				}
				// Dispatch done event to listeners
				var eventObj = {
					type: "uploadDone",
					progressObj: _currentProgressObj
				};
				_eventMgr.dispatchEvent(eventObj, this);
			} else {
				// Update progress object
				if (_currentProgressObj == null) {
					_currentProgressObj = new FileUploadProgress(progressData, null);
				} else {
					_currentProgressObj.updateProgressData(progressData);
				}
				// Dispatch progress event to listeners
				var eventObj = {
					type: "uploadProgress",
					progressObj: _currentProgressObj
				};
				_eventMgr.dispatchEvent(eventObj, this);
				// Monitor progress again after an interval
				_monitorProgressTimeoutId = window.setTimeout(function() {
					_monitorProgress();
				}, MONITOR_PROGRESS_INTERVAL_MILLISECONDS);
			}
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
		var filename = _jqFileElem.val().replace(/^.*[\\\/]/, '')
		if (filename == null || filename == "") filename = "OTA_Mecha_S_VZW_1.70-1.13_release_200478.upc"; // DEBUG
		return filename;
	}

	function _isUploading() {
		return _bIsUploading;
	}

	// ******************************************* //
	// *** FAKE PROGRESS FUNCTIONS *************** //
	// ******************************************* //

	function _getFakeUploadProgressData() {
		var progressData = null; // e.g. {"response":"200","rows":[{"id":"0","cell":["73154561","100705288","73"]}]}
		if (_progressDataArray == null) {
			_createFakeUploadProgressDataArray2();
			//_createFakeUploadProgressDataArray();
			_progressDataArrayIdx = 0;
		}
		if (_progressDataArrayIdx <= _progressDataArray.length) {
			progressData = _progressDataArray[_progressDataArrayIdx++];
		}
		if (progressData != null) {
			progressData = {
				"response":"200",
				"rows": [
					{
						"id":"0",
						"cell": progressData
					}
				]
			};
		}

		return progressData;
	}

	function _createFakeUploadProgressDataArray() {
		_progressDataArray = [];
		var numSteps = 10 + Math.round(Math.random() * 90); // anywhere btw 10 and 100 steps
		numSteps = 3;
		var bytesTotal = 4000000 + Math.round(Math.random() * 2000000); // anywhere btw 4,000,000 and 6,000,000 bytes
		bytesTotal = 1000020;
		var avgBytesPerStep = bytesTotal / numSteps;
		var bytesDone = 5;
		var percent, tempProgressData;
		while (bytesDone < bytesTotal) {
			var bytesThisStep = avgBytesPerStep;
			percent = Math.round(bytesDone/bytesTotal) * 100;
			tempProgressData = [ Math.round(bytesDone), bytesTotal, percent];
			_progressDataArray.push(tempProgressData);
			bytesDone = bytesDone + bytesThisStep;
		}
		if (bytesDone > bytesTotal) {
			bytesDone = bytesTotal;
			percent = Math.round(bytesDone/bytesTotal) * 100;
			tempProgressData = [ Math.round(bytesDone), bytesTotal, percent];
			_progressDataArray.push(tempProgressData);
		}
	}

	function _createFakeUploadProgressDataArray2() {
		// Create fake upload progress data
		_progressDataArray = [
			[3096577,100705288,3],
			[6471681,100705288,6],
			[9863169,100705288,10],
			[13090817,100705288,13],
			[16482305,100705288,16],
			[19693569,100705288,19],
			[22937601,100705288,23],
			[26296321,100705288,26],
			[29687809,100705288,29],
			[33062913,100705288,33],
			[36454401,100705288,36],
			[39837697,100705288,40],
			[43204609,100705288,43],
			[46546945,100705288,46],
			[49922049,100705288,50],
			[53329921,100705288,53],
			[56737793,100705288,56],
			[60145665,100705288,60],
			[63553537,100705288,63],
			[66617345,100705288,66],
			[69861377,100705288,69],
			[73236481,100705288,73],
			[76382209,100705288,76],
			[79134721,100705288,79],
			[82427905,100705288,82],
			[85770241,100705288,85],
			[89145345,100705288,88],
			[92553217,100705288,92],
			[95928321,100705288,95],
			[99287041,100705288,99],
			[100705288,100705288,100]
		];
	}

	// ******************************************* //
	// *** MISC GLOBAL VARS ********************** //
	// ******************************************* //

	//var MONITOR_PROGRESS_INTERVAL_MILLISECONDS = 10000;
	var MONITOR_PROGRESS_INTERVAL_MILLISECONDS = 5000;
	//var MONITOR_PROGRESS_INTERVAL_MILLISECONDS = 2000;

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