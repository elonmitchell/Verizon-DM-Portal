function DoubleHash(_bDeleteSecondaryHashIfValNull) {
	var _primaryHash;

	_init();

	function _init() {
		if (_bDeleteSecondaryHashIfValNull == null) _bDeleteSecondaryHashIfValNull = false;
		_primaryHash = {};
	}

	function _set(key1, key2, val) {
		if ((val == null) && _bDeleteSecondaryHashIfValNull) {
			_delete(key1, key2);
		} else {
			_setHelper(key1, key2, val);
		}
	}

	function _setHelper(key1, key2, val) {
		var bKey1Null = (key1 == null);

		if (bKey1Null) { // key1 == null
			$.each(_primaryHash, function (key1Loop) {
				_setHelper(key1Loop, key2, val);
			});
		} else { // key1 != null
			var bKey2Null = (key2 == null);
			if (_primaryHash.hasOwnProperty(key1)) {
				var secondaryHash = _primaryHash[key1];
				if (bKey2Null) { // key1 exists && key2 == null
					// Loop over secondary keys
					$.each(secondaryHash, function (key2Loop) {
						_set(key1, key2Loop, val);
					});
				} else {
					secondaryHash[key2] = val;
				}
			} else { // key1 doesn't exist
				if (bKey2Null) { // key1 doesn't exist && key2 == null
					// No existing secondary keys to loop over -> Do nothing
				} else { // key1 doesn't exist && key2 != null
					// Create secondary hash for key1
					_primaryHash[key1] = {};
					_primaryHash[key1][key2] = val;
				}
			}
		}
	}

	function _get(key1, key2) {
		var val = null;
		if (key1 == null) {
			// Return array of values
			val = [];
			$.each(_primaryHash, function(key1Loop) {
				val.push(_get(key1Loop, key2));
			});
		} else {
			if (_primaryHash.hasOwnProperty(key1)) {
				var secondaryHash = _primaryHash[key1];
				if (key2 == null) {
					val = secondaryHash;
				} else { // key2 != null
					if (secondaryHash.hasOwnProperty(key2)) {
						val = secondaryHash[key2];
					}
				}
			}
		}
		return val;
	}



	function _delete(key1, key2) {
		var bKey1Null = (key1 == null);
		var bKey2Null = (key2 == null);
		if (bKey1Null) { // key1 == null
			if (bKey2Null) { // key1 == null && key2 == null
				// Nothing to do
			} else { // key1 == null && key2 != null
				// Delete key2 in all primary keys
				$.each(_primaryHash, function(key1Loop) {
					_delete(key1Loop, key2);
				});
			}
		} else { // key1 != null
			if (_primaryHash.hasOwnProperty(key1)) {
				if (bKey2Null) { // key1 != null && key2 == null
					// Delete all secondary hashes under key1
					delete _primaryHash[key1];
				} else { // key1 != null && key2 != null
					var secondaryHash = _primaryHash[key1];
					if (secondaryHash.hasOwnProperty(key2)) {
						delete secondaryHash[key2];
						if ($.isEmptyObject(secondaryHash)) {
							delete _primaryHash[key1];
						}
					}
				}
			}
		}
	}

	return {
		set: _set,
		get: _get,
		delete: _delete
	};

}