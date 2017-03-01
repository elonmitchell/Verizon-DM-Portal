function EstTimeCalculator(kbTotal, bDebug) {
	var _DEBUG;
	var _kbTotal;
	var _kbDoneArray;
	var _uploadTimeArray;
	var _currentSpeed;

	_init(kbTotal, bDebug);

	function _init(kbTotal, bDebug) {
		if (bDebug == null) {
			_DEBUG = false;
		} else {
			_DEBUG = bDebug;
		}
		_kbTotal = kbTotal;
		_kbDoneArray = null;
		_uploadTimeArray = null;
	}

	function _addSnapshot(kbDone) {
		var currentKbDoneTime = ((new Date()).getTime()) / 1000;
		// Set as the start time if necessary
		if (_kbDoneArray == null || _uploadTimeArray == null) {
			_kbDoneArray = [];
			_uploadTimeArray = [];
		} else {
			// Get the last speed
			var lastKbDiff = kbDone - _kbDoneArray[_kbDoneArray.length - 1];
			var lastTimeDiff = currentKbDoneTime - _uploadTimeArray[_uploadTimeArray.length - 1];
			var lastSpeed = lastKbDiff/lastTimeDiff;
			// Get the average speed
			var totalKbDiff = kbDone - _kbDoneArray[0];
			var totalTimeDiff = currentKbDoneTime - _uploadTimeArray[0];
			var avgSpeed = totalKbDiff / totalTimeDiff;
			if (_DEBUG) try { console.log("addSnapshot(): ("+kbDone+" - "+_kbDoneArray[0]+") / ("+currentKbDoneTime+" - "+_uploadTimeArray[0]+") = "+totalKbDiff+" / "+totalTimeDiff+" = "+avgSpeed); } catch (ex) {}
			// Calculate a smoothed speed
			var smoothedSpeed = _SMOOTHING_FACTOR*lastSpeed + (1-_SMOOTHING_FACTOR)*avgSpeed;
			// Set as the current speed
			//_currentSpeed = smoothedSpeed;
			_currentSpeed = avgSpeed;
		}
		_kbDoneArray.push(kbDone);
		_uploadTimeArray.push(currentKbDoneTime);
	}

	function _getSecondsLeft() {
		var secondsLeft = null;
		// Grab the current time
		var currentTime = ((new Date()).getTime()) / 1000;
		// Try to calculate the seconds left
		if (_currentSpeed != null) {
			// Get the previous snapshots of kbDone and upload time
			var lastKbDone = _kbDoneArray[_kbDoneArray.length - 1];
			var lastUploadTime = _uploadTimeArray[_uploadTimeArray.length - 1];
			// Get the elapsed time since the last snapshot
			var timeDiff = currentTime - lastUploadTime;
			// Get the estimated kbDone over that period
			var estKbDoneDiff = timeDiff * _currentSpeed;
			// Add this to kbDone from last snapshot to get the estimated kbDone right now
			var estKbDone = Math.min(lastKbDone + estKbDoneDiff, kbTotal);
			// Get the remaining kb left
			var kbLeft = kbTotal - estKbDone;
			// Get the seconds left
			secondsLeft = Math.round(kbLeft / _currentSpeed);
		}
		return secondsLeft;
	}

	// ******************************************* //
	// *** CONSTANTS ***************************** //
	// ******************************************* //

	var _SMOOTHING_FACTOR = 0.005;

	// ******************************************* //
	// *** PUBLIC METHODS ************************ //
	// ******************************************* //

	return {
		addSnapshot: _addSnapshot,
		getSecondsLeft: _getSecondsLeft
	}

}