/* global $ */
function EventManager() {
	/*
	 * _listenerHash looks like this:
	 * {
	 *   "eventType1": { (etListenerHash)
	 *     "_global_": [ func1, func2, func3 ], (idListenerArray)
	 *     "id1": [ func4 ],
	 *     "id2": [ func5, func6]
	 *   },
	 *   "eventType2": {
	 *     ...
	 *   }
	 *   ...
	 * }
	 */
	var _listenerHash;

	_init();

	function _init() {
		// Initialize
		_listenerHash = {};
	}

	// ******************************************* //
	// *** EVENT HANDLING ************************ //
	// ******************************************* //

	function _addEventListener(eventType, targetId, listenerFunc) {
		if (targetId == null) targetId = "_global_";
		_removeEventListener(eventType, targetId, listenerFunc, true);
		var etListenerHash = _listenerHash[eventType];
		if (!etListenerHash) { etListenerHash = _listenerHash[eventType] = {}; }
		var idListenerArray = etListenerHash[targetId];
		if (!idListenerArray) { idListenerArray = etListenerHash[targetId] = [] }
		idListenerArray.push(listenerFunc);
	}

	function _addIdEventListeners(targetId, eventTypeListenerFuncHash) {
		$.each(eventTypeListenerFuncHash, function(eventType, listenerFunc) {
			_addEventListener(eventType, targetId, listenerFunc);
		});
	}

	function _removeEventListener(eventType, targetId, listenerFunc, bFromAdd) {
		if (bFromAdd == null) bFromAdd = false;
		var etListenerHash = _listenerHash[eventType];
		if (!etListenerHash) { return; }
		if (targetId == null) targetId = "_global_";
		var idListenerArray = etListenerHash[targetId];
		if (!idListenerArray) { return; }
		var len = idListenerArray.length;
		for (var i = len - 1; i >= 0; i--) {
			// If listenerFunc is null then remove all functions, otherwise find the specific function
			if (listenerFunc == null || idListenerArray[i] == listenerFunc) {
				if (bFromAdd) console.log("removeEventListener(): duplicate add found");
				// Slice it out
				idListenerArray.splice(i, 1);
				if (len == 1) {
					// This is the only listener for the event type and target id
					// => Remove listener entry for this event type and target id
					delete(etListenerHash[targetId]);
					if ($.isEmptyObject(_listenerHash[eventType])) {
						// This event type has no more targets => Remove
						delete(_listenerHash[eventType]);
					}
				}
				if (listenerFunc != null) break;
			}
		}
	}

	function _removeAllEventListeners(eventType, targetId) {
	  if (!targetId) {
			if (!eventType) { // no eventType, no targetId
				_listenerHash = {}; // reset
			} else { // eventType X, no targetId
				delete(_listenerHash[eventType]); // remove all for this event type
			}
		} else {
			if (!eventType) { // no eventType, targetId Y
				$.each(_listenerHash, function(et, etListenerHash) {
					delete(etListenerHash[et][targetId]); // remove all for this event id
				});
			} else { // eventType X, targetId Y
				delete(_listenerHash[eventType][targetId]);
			}
		}
	}

	function _addSingleEventListener(eventType, targetId, listenerFunc) {
		// Add listener and remove it after the first event has dispatched
		var singleListenerFunc = function(eventObj) {
			_removeEventListener(eventType, targetId, singleListenerFunc);
			eventObj._removed = true;
			return listenerFunc(eventObj);
		};
		_addEventListener(eventType, targetId, singleListenerFunc);
	}

	function _dispatchEvent(eventObj, target) {
		var retObj = null;
		if (eventObj && eventObj.type) {
			// Get listener hash for the event type
			var eventType = eventObj.type;
			var etListenerHash = _listenerHash[eventType];
			if (!etListenerHash) { return retObj; }
			// Set event target, i.e. source item that triggered the event
			eventObj.target = target||eventObj.target||this;
			// Set targetIds, i.e. items that should receive the event
			var targetIdArray = [];
			if (eventObj.targetIds) targetIdArray = eventObj.targetIds.slice(0);
			targetIdArray.push("_global_");
			// Trigger listeners
			for (var targetIdx = 0; targetIdx < targetIdArray.length; targetIdx++) {
				var targetId = targetIdArray[targetIdx];
				var idListenerArray = etListenerHash[targetId];
				if (idListenerArray != null) {
					var removeListenerArray = [];
					for (var listenerIdx = 0; listenerIdx < idListenerArray.length; listenerIdx++) {
						var listener = idListenerArray[listenerIdx];
						if (listener == null) debugger;
						var obj = listener(eventObj);
						if (eventObj.hasOwnProperty("_removed") && eventObj._removed == true) {
							listenerIdx--;
							delete eventObj["_removed"];
						}
						if (obj != null) {
							var objTypeStr = $.type(obj);
							if (objTypeStr === "array" ||
								objTypeStr === "string" ||
								objTypeStr === "number" ||
								objTypeStr === "boolean" ||
								objTypeStr === "date") {
								if (retObj == null) retObj = new Array();
								retObj = retObj.concat(obj);
							} else { // extendable object
								if (retObj == null) retObj = {};
								$.extend(retObj, obj);
							}
						}
					}
					for (var i = 0; i < removeListenerArray.length; i++) {
						_removeEventListener(eventType, targetId, listener);
					}
				}
			}
		}
		return retObj;
	}

	// ******************************************* //
	// *** PUBLIC METHODS ************************ //
	// ******************************************* //

	function _getListenerHash() { return _listenerHash; } // DEBUG

	return { // Public methods go here
		addEventListener: _addEventListener,
		removeEventListener: _removeEventListener,
		removeAllEventListeners: _removeAllEventListeners,
		addIdEventListeners: _addIdEventListeners,
		addSingleEventListener: _addSingleEventListener,
		dispatchEvent: _dispatchEvent,
		_getListenerHash: _getListenerHash // DEBUG

	}
}


function EventManagerPrototype() {
	/*
	 * _listenerHash looks like this:
	 * {
	 *   "eventType1": { (etListenerHash)
	 *     "_global_": [ func1, func2, func3 ], (idListenerArray)
	 *     "id1": [ func4 ],
	 *     "id2": [ func5, func6]
	 *   },
	 *   "eventType2": {
	 *     ...
	 *   }
	 *   ...
	 * }
	 */
	this.listenerHash = {};
}
/*
EventManagerPrototype.prototype.addEventListener = function(eventType, targetId, listenerFunc) {
	if (targetId == null) targetId = "_global_";
	this.removeEventListener(eventType, targetId, listenerFunc, true);
	var etListenerHash = this.listenerHash[eventType];
	if (!etListenerHash) { etListenerHash = this.listenerHash[eventType] = {}; }
	var idListenerArray = etListenerHash[targetId];
	if (!idListenerArray) { idListenerArray = etListenerHash[targetId] = [] }
	idListenerArray.push(listenerFunc);
};

EventManagerPrototype.prototype.addIdEventListeners = function(targetId, eventTypeListenerFuncHash) {
	var pThis = this;
	$.each(eventTypeListenerFuncHash, function(eventType, listenerFunc) {
		pThis.addEventListener(eventType, targetId, listenerFunc);
	});
};

EventManagerPrototype.prototype.removeEventListener = function(eventType, targetId, listenerFunc, bFromAdd) {
	if (bFromAdd == null) bFromAdd = false;
	var etListenerHash = this.listenerHash[eventType];
	if (!etListenerHash) return;
	if (targetId == null) targetId = "_global_";
	var idListenerArray = etListenerHash[targetId];
	if (!idListenerArray) return;
	var len = idListenerArray.length;
	for (var i = len - 1; i >= 0; i--) {
		// If listenerFunc is null then remove all functions, otherwise find the specific function
		if (listenerFunc == null || idListenerArray[i] == listenerFunc) {
			if (bFromAdd) console.log("removeEventListener(): duplicate add found");
			// Slice it out
			idListenerArray.splice(i, 1);
			if (len == 1) {
				// This is the only listener for the event type and target id => Remove listener entry
				delete(etListenerHash[targetId]);
				if ($.isEmptyObject(this.listenerHash[eventType])) {
					// This event type has no more targets => Remove
					delete(this.listenerHash[eventType]);
				}
			}
			if (listenerFunc != null) break;
		}
	}
};

EventManagerPrototype.prototype.removeAllEventListeners = function(eventType, targetId) {
	if (!targetId) {
		if (!eventType) { // no eventType, no targetId
			this.listenerHash = {}; // reset
		} else { // eventType X, no targetId
			delete(this.listenerHash[eventType]); // remove all for this event type
		}
	} else {
		if (!eventType) { // no eventType, targetId Y
			$.each(this.listenerHash, function(et, etListenerHash) {
				delete(etListenerHash[et][targetId]); // remove all for this event id
			});
		} else { // eventType X, targetId Y
			delete(this.listenerHash[eventType][targetId]);
		}
	}
};

EventManagerPrototype.prototype.addSingleEventListener = function(eventType, targetId, listenerFunc) {
	// Add listener and remove it after the first event has dispatched
	var singleListenerFunc = function(eventObj) {
		this.removeEventListener(eventType, targetId, singleListenerFunc);
		eventObj._removed = true;
		return listenerFunc(eventObj);
	};
	this.addEventListener(eventType, targetId, singleListenerFunc);
};

EventManagerPrototype.prototype.dispatchEvent = function(eventObj, target) {
	var retObj = null;
	if (eventObj && eventObj.type) {
		// Get listener hash for the event type
		var eventType = eventObj.type;
		var etListenerHash = this.listenerHash[eventType];
		if (!etListenerHash) { return retObj; }
		// Set event target, i.e. source item that triggered the event
		eventObj.target = target||eventObj.target||this;
		// Set targetIds, i.e. items that should receive the event
		var targetIdArray = [];
		if (eventObj.targetIds) targetIdArray = eventObj.targetIds.slice(0);
		targetIdArray.push("_global_");
		// Trigger listeners
		for (var targetIdx = 0; targetIdx < targetIdArray.length; targetIdx++) {
			var targetId = targetIdArray[targetIdx];
			var idListenerArray = etListenerHash[targetId];
			if (idListenerArray != null) {
				var removeListenerArray = [];
				for (var listenerIdx = 0; listenerIdx < idListenerArray.length; listenerIdx++) {
					var listener = idListenerArray[listenerIdx];
					if (listener == null) debugger;
					var obj = listener(eventObj);
					if (eventObj.hasOwnProperty("_removed") && eventObj._removed == true) {
						listenerIdx--;
						delete eventObj["_removed"];
					}
					if (obj != null) {
						var objTypeStr = $.type(obj);
						if (objTypeStr === "array" ||
								objTypeStr === "string" ||
								objTypeStr === "number" ||
								objTypeStr === "boolean" ||
								objTypeStr === "date") {
							if (retObj == null) retObj = new Array();
							retObj = retObj.concat(obj);
						} else { // extendable object
							if (retObj == null) retObj = {};
							$.extend(retObj, obj);
						}
					}
				}
				for (var i = 0; i < removeListenerArray.length; i++) {
					this.removeEventListener(eventType, targetId, listener);
				}
			}
		}
	}
	return retObj;
};

EventManagerPrototype.prototype.getListenerHash = function() {
	return this.listenerHash;
};
*/