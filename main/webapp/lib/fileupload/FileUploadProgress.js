function FileUploadProgress(progressData, resultData) {
	var _progressData;
	var _resultData;
	var _bIsCancelled;

	_init(progressData, resultData);

	function _init(progressData, resultData) {
		_progressData = progressData; // e.g. {"response":"200","rows":[{"id":"0","cell":["73154561","100705288","73"]}]}
		_resultData = resultData; // e.g. {"response":{"statusCode":"301","statusMessage":"Internal server error. Please try again later"}};
		_bIsCancelled = false;
	}

	function _updateProgressData(progressData) {
		_progressData = progressData;
	}

	function _updateResultData(resultData) {
		_resultData = resultData;
	}

	function _setAsCancelled() {
		_bIsCancelled = true;
	}

	function _isCancelled() {
		return _bIsCancelled;
	}

	function _getProgressArray() {
		return _progressData.rows[0].cell;
	}
	function _getKbDone() {
		var kbDone = null;
		if (_isDone() && !_isCancelled()) {
			// Upload is completed, all bits transferred
			kbDone = _getKbTotal();
		} else {
			var progressArray = _getProgressArray();
			kbDone = progressArray[0] / 1000;
		}
		return kbDone;
	}
	function _getKbTotal() {
		var progressArray = _getProgressArray();
		var kbTotal = progressArray[1] / 1000;
		return kbTotal;
	}
	function _getPercentDone() {
		var progressArray = _getProgressArray();
		var percentDone = progressArray[2];
		return percentDone;
	}

	function _isDone() {
		var bIsDone = (_resultData == null) ? false : true;
		return bIsDone;
	}

	function _isSuccess() {
		var bIsSuccess;
		if (_isDone()) {
			bIsSuccess = (_resultData != null && _resultData.response != null && _resultData.response.statusCode == "200");
		} else {
			bIsSuccess = (_progressData.response == "200");
		}
		return bIsSuccess;
	}
	function _isError() {
		return !_isSuccess();
	}
	function _getSuccessMessage() {
		var successMsg;
		try {
			successMsg = _resultData.response.statusMessage;
		} catch(err) {
			successMsg = "";
		}
		return successMsg;
	}
	function _getErrorMessage() {
		var errorMsg;
		try {
			errorMsg = _resultData.response.statusMessage;
		} catch(err) {
			errorMsg = "An error occurred.";
		}
		return errorMsg;
	}

	function _hasProgressData() {
		var bHasProgressData = false;
		try {
			var tmp = _getKbDone();
			if (tmp != null) {
				bHasProgressData = true;
			}
		} catch(ex) {
		}
		return bHasProgressData;
	}

	function _hasResultData() {
		return (_resultData != null && _resultData.response != null);
	}

	function _getProgressData() {
		return _progressData;
	}

	function _getResultData() {
		return _resultData;
	}

	function _getFirmwareName() {
		return (_hasResultData()) ? _resultData.response.firmwareName : null;
	}

	// ******************************************* //
	// *** PUBLIC METHODS ************************ //
	// ******************************************* //

	return {
		updateProgressData:_updateProgressData,
		updateResultData:_updateResultData,
		setAsCancelled: _setAsCancelled,
		isCancelled: _isCancelled,
		getKbDone: _getKbDone,
		getKbTotal: _getKbTotal,
		getPercentDone: _getPercentDone,
		isDone: _isDone,
		isSuccess: _isSuccess,
		isError: _isError,
		getSuccessMessage: _getSuccessMessage,
		getErrorMessage: _getErrorMessage,
		hasProgressData: _hasProgressData,
		getProgressData: _getProgressData, // not ideal to make this public
		getResultData: _getResultData, // not ideal to make this public
		getFirmwareName: _getFirmwareName
	}
}