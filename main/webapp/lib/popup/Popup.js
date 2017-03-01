/* global PopupManager, CommonUtil, PopupScreen, $ */
function Popup(btnId) {
	var _popupId;
	var _popupConfig;
	var _popupConfigUrl;
	var _popupScreenArray;
	var _popupScreenHash;
	var _screenNum;
	var _currentScreen;
	var _customData;

	_init(btnId);

	function _init(btnId) {
		// Initialize
		_popupId = btnId;
		_popupConfig = null;
		_popupScreenArray = [];
		_popupScreenHash = {};
		_screenNum = 0;
		_currentScreen = null;
	}

	function _showFirstScreen(screenRenderData) {
		if (_popupConfig == null) {
			_initPopupConfig(_showFirstScreen2, [screenRenderData]);
		} else {
			_showFirstScreen2(screenRenderData);
		}
	}
	function _showFirstScreen2(screenRenderData) {
		// Show the first screen
		_screenNum = 0;
		_gotoNextScreen(screenRenderData);
	}

  function _initPopupConfig(cbFunc, paramArray) {
		_popupConfigUrl = PopupManager.getPopupConfigUrl(btnId);
		//$.getJSON(popupConfigUrl, function(popupConfig) {
		$.ajax({
			dataType: "json",
			url: _popupConfigUrl
		}).fail(CommonUtil.onAjaxFail).done(function (popupConfig) {
			_popupConfig = popupConfig;

			// Initialize screens
			var screenConfigArray = popupConfig.screens;
			if (screenConfigArray.length > 0) {
				// Create screens and add them to popupScreenArray and popupScreenHash
				$.each(screenConfigArray, function() {
					var screenConfig = this;
					var popupScreen = new PopupScreen(_popupId, screenConfig);
					_popupScreenArray.push(popupScreen);
					_popupScreenHash[screenConfig.id] = popupScreen;
				});
			}

			// Trigger callback
			cbFunc.apply(this, paramArray);
		});
	}

	function _gotoScreen(screenId, screenRenderData) {
		var oldScreen = _currentScreen;
		_currentScreen = _popupScreenHash[screenId];
		if (_currentScreen == null) {
			alert("Screen '"+screenId+ "' doesn't exist in the '"+_popupId+"' popup");
		} else {
			// Show the new screen
			oldScreen.close();
			_currentScreen.show(screenRenderData);
		}
	}

	function _gotoNextScreen(screenRenderData) {
		// Increment screen number and see if there is a next screen
		_screenNum++;
		if (_screenNum <= _popupScreenArray.length) {
			// There is a next screen -> Get it
			var screenIdx = _screenNum - 1;
			var oldScreen = _currentScreen;
			_currentScreen = _popupScreenArray[screenIdx];
			// Close the old screen and show the new one
			if (oldScreen != null) oldScreen.close();
			_currentScreen.show(screenRenderData);
		} else {
			// There is no next screen, close the popup
		}
	}

	function _getScreen(screenId) {
		return _popupScreenHash[screenId];
	}

	function _setCustomData(customData) {
		_customData = customData;
	}

	function _getCustomData() {
		return _customData;
	}

	return { // Public methods go here
		showFirstScreen: _showFirstScreen,
		gotoScreen: _gotoScreen,
		gotoNextScreen: _gotoNextScreen,
		getScreen: _getScreen,
		setCustomData: _setCustomData,
		getCustomData: _getCustomData
	}

}
