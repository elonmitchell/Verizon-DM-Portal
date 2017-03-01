/* global $, EventManager, FileUploadTransfer, FakeFileUploadTransfer, FileUploadStatusController, FileUploadSummaryController, PopupManager */
var FileUploadManager = (function () {
	"use strict";

	var _config;

	var _summaryArtifact;
	var _fuTransferHash;
	var _progressArtifactObjHash;

	function _init(config) {
		// Store config
		_config = config;

		// Init hashes
		_fuTransferHash = {};
		_progressArtifactObjHash = {};

		// Init summary artifact
		_initSummaryArtifact();
	}

	function _initSummaryArtifact() {
		_summaryArtifact = new FileUploadSummaryController(_config);

		// Listen for when summary object is cancelled
		_summaryArtifact.addEventListener("summaryCancel", null, function(eventObj) {
			_cancelAllUploads();
		});

		// Listen for when summary object is closed
		_summaryArtifact.addEventListener("summaryClose", null, function(eventObj) {
			_clearAllUploads();
		});
	}

	// ******************************************* //
	// *** ADD/CANCEL/CLEAR UPLOAD FUNCTIONS ***** //
	// ******************************************* //

	function _addUpload(uploadURL, jqFileElem, uploadData, progressURL, inputProgressArtifactObj) {
		// Create upload transfer object
		var fuTransfer = new FileUploadTransfer(uploadURL, jqFileElem, uploadData, progressURL);
		//var fuTransfer = new FakeFileUploadTransfer(uploadURL, jqFileElem, uploadData, progressURL); // DEBUG

		// Add to hash
		var transferId = fuTransfer.getId();
		_fuTransferHash[transferId] = fuTransfer;

		// Register transfer listeners
		_registerTransferListeners(_summaryArtifact, fuTransfer);
		if (inputProgressArtifactObj != null) {
			_registerTransferListeners(inputProgressArtifactObj, fuTransfer);
		}

		// Start upload
		fuTransfer.start();
	}

	function _getUploadTransferHash() {
		return _fuTransferHash;
	}

	function _cancelUpload(progressArtifactObj) {
		if ($.isFunction(progressArtifactObj.getId)) {
			// Get the transfer associated with this progress artifact object
			var progressArtifactObjId = progressArtifactObj.getId();
			var fuTransferId = _progressArtifactObjHash[progressArtifactObjId];
			var fuTransfer = _fuTransferHash[fuTransferId];

			// Cancel the transfer
			fuTransfer.cancel();
		}
	}

	function _cancelAllUploads() {
		$.each(_fuTransferHash, function(fuTransferId, fuTransfer) {
			if (fuTransfer.isUploading()) {
				// Cancel the transfer
				fuTransfer.cancel();
			}
		});
	}

	function _clearUpload(progressArtifactObj) {
		if ($.isFunction(progressArtifactObj.getId)) {
			// Get the transfer associated with this progress artifact object
			var progressArtifactObjId = progressArtifactObj.getId();
			var fuTransferId = _progressArtifactObjHash[progressArtifactObjId];
			var fuTransfer = _fuTransferHash[fuTransferId];

			// Dispatch event to clear transfer
			var eventObj = { type: "uploadClear" };
			fuTransfer.dispatchEvent(eventObj, progressArtifactObjId);

			// Delete the assocatiated transfer from the transfer hash
			delete _fuTransferHash[fuTransferId];

			if ($.isEmptyObject(_fuTransferHash)) {
				// No more transfers -> Send clear all event to summary artifact
				_summaryArtifact.clearAll();
			}
		}
	}

	function _clearAllUploads() {
		// Loop over all transfers
		$.each(_fuTransferHash, function(fuTransferId, fuTransfer) {
			// Dispatch event to clear transfer
			var eventObj = { type: "uploadClear" };
			fuTransfer.dispatchEvent(eventObj);
		});

		// Clear summary artifact
		if ($.isFunction(_summaryArtifact.clearAll)) {
			_summaryArtifact.clearAll();
		}

		// Reset the transfer hash
		_fuTransferHash = {};
	}

	function _isUploadActive() {
		var bIsUploadActive = false;
		$.each(_fuTransferHash, function(fuTransferId, fuTransfer) {
			if (fuTransfer.isUploading()) {
				bIsUploadActive = true;
				return false;
			}
		});
		return bIsUploadActive;
	}

	// ******************************************* //
	// *** REGISTER TRANSFER LISTENERS *********** //
	// ******************************************* //

	function _registerTransferListeners(progressArtifactObj, fuTransfer) {
		var transferId = fuTransfer.getId();

		// Listener for when transfer has started
		fuTransfer.addEventListener("uploadStart", null, function(eventObj) {
			// Relay progress event
			if ($.isFunction(progressArtifactObj.start)) {
				progressArtifactObj.start(transferId);
			}
		});

		// Listener for when transfer has progress
		fuTransfer.addEventListener("uploadProgress", null, function(eventObj) {
			// Relay progress event
			if ($.isFunction(progressArtifactObj.updateProgress)) {
				progressArtifactObj.updateProgress(eventObj.progressObj, transferId);
			}
		});

		// Listener for when transfer is done
		fuTransfer.addEventListener("uploadDone", null, function(eventObj) {
			// Relay done event
			if ($.isFunction(progressArtifactObj.done)) {
				progressArtifactObj.done(eventObj.progressObj, transferId);
			}
		});

		// Listen for when transfer is cancelled
		fuTransfer.addEventListener("uploadCancel", null, function(eventObj) {
			// Relay cancel event
			if ($.isFunction(progressArtifactObj.cancel)) {
				progressArtifactObj.cancel(eventObj.progressObj, transferId);
			}
		});
		// Listen for when transfer is cleared
		fuTransfer.addEventListener("uploadClear", null, function(eventObj) {
			// Relay clear event
			if ($.isFunction(progressArtifactObj.clear)) {
				progressArtifactObj.clear(transferId);
			}
			// Remove progress artifact obj from hash
			if ($.isFunction(progressArtifactObj.getId)) {
				var progressArtifactObjId = progressArtifactObj.getId();
				delete _progressArtifactObjHash[progressArtifactObjId];
			}
		});

		// Store association btw progress artifact object and its transfer object
		if ($.isFunction(progressArtifactObj.getId)) {
			var progressArtifactObjId = progressArtifactObj.getId();
			_progressArtifactObjHash[progressArtifactObjId] = transferId;
		}
	}

	// ******************************************* //
	// *** PUBLIC METHODS ************************ //
	// ******************************************* //

	return { // Public methods go here
		init: _init,
		addUpload: _addUpload,
		getUploadTransferHash: _getUploadTransferHash,
		cancelUpload: _cancelUpload,
		clearUpload: _clearUpload,
		clearAllUploads: _clearAllUploads,
		isUploadActive: _isUploadActive,
		registerTransferListeners: _registerTransferListeners
	};

})();
