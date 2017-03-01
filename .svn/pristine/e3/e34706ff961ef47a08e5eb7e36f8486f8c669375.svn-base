/* global jQuery */
//var UPLOAD_DEBUG = true;

jQuery.extend({
	createUploadIframe: function (id, uri) {
		//create frame
		var frameId = 'jUploadFrame' + id;
		//var iframeHtml = '<iframe id="' + frameId + '" name="' + frameId + '" style="position:absolute; top:-9999px; left:-9999px"';
		var iframeHtml = '<iframe id="' + frameId + '" name="' + frameId + '"';
		if (typeof(UPLOAD_DEBUG) == "undefined" || !UPLOAD_DEBUG) {
			// Hide it
			iframeHtml += ' style="position:absolute; top:-9999px; left:-9999px"';
		}
		if (window.ActiveXObject) {
			if (typeof uri == 'boolean') {
				iframeHtml += ' src="' + 'javascript:false' + '"';
			} else if (typeof uri == 'string') {
				iframeHtml += ' src="' + uri + '"';
			}
		}
		iframeHtml += ' />';
		jQuery(iframeHtml).appendTo(document.body);

		return jQuery('#' + frameId).get(0);
	},
	createUploadForm: function (id, fileElement, data) {
		//create form
		var formId = 'jUploadForm' + id;
		var fileId = 'jUploadFile' + id;
		var form = jQuery('<form  action="" method="POST" name="' + formId + '" id="' + formId + '" enctype="multipart/form-data"></form>');
		if (data) {
			for (var i in data) {
				jQuery('<input type="hidden" name="' + i + '" value="' + data[i] + '" />').appendTo(form);
			}
		}
		var oldElement = fileElement;
		var newElement = jQuery(oldElement).clone();
		jQuery(oldElement).attr('id', fileId);
		jQuery(oldElement).before(newElement);
		jQuery(oldElement).appendTo(form);
		jQuery('<input type="submit" id="formSubmitButton" value="submit">Submit</input>').appendTo(form);

		//set attributes
		if (typeof(UPLOAD_DEBUG) == "undefined" || !UPLOAD_DEBUG) {
			jQuery(form).css('position', 'absolute');
			jQuery(form).css('top', '-1200px');
			jQuery(form).css('left', '-1200px');
		}
		jQuery(form).appendTo('body');
		return form;
	},

	ajaxFileUpload: function (s) {
		// TODO introduce global settings, allowing the client to modify them for all requests, not only timeout
		s = jQuery.extend({}, jQuery.ajaxSettings, s);
		var id = new Date().getTime();
		var jqFileElement;
		if (s.fileElement) {
			jqFileElement = jQuery(s.fileElement);
		} else {
			jqFileElement = jQuery('#' + s.fileElementId);
		}
		var form2 = jQuery.createUploadForm(id, jqFileElement, (typeof(s.data) == 'undefined' ? false : s.data));
		var iframeElem = jQuery.createUploadIframe(id, s.secureuri);
		var frameId = 'jUploadFrame' + id;
		var formId = 'jUploadForm' + id;
		// Watch for a new set of requests
		if (s.global && !jQuery.active++) {
			jQuery.event.trigger("ajaxStart");
		}
		var requestDone = false;
		// Create the request object
		var xml = {};
		if (s.global) jQuery.event.trigger("ajaxSend", [xml, s]);
		// Wait for a response to come back
		var uploadCallback = function (isTimeout) {
			var iframeElem = document.getElementById(frameId);
			try {
				if (iframeElem.contentWindow) {
					xml.responseText = iframeElem.contentWindow.document.body ? iframeElem.contentWindow.document.body.innerHTML : null;
					xml.responseXML = iframeElem.contentWindow.document.XMLDocument ? iframeElem.contentWindow.document.XMLDocument : iframeElem.contentWindow.document;
				} else if (iframeElem.contentDocument) {
					xml.responseText = iframeElem.contentDocument.document.body ? iframeElem.contentDocument.document.body.innerHTML : null;
					xml.responseXML = iframeElem.contentDocument.document.XMLDocument ? iframeElem.contentDocument.document.XMLDocument : iframeElem.contentDocument.document;
				}
			} catch (e) {
				jQuery.handleError(s, xml, null, e);
			}
			if (xml || isTimeout == "timeout") {
				requestDone = true;
				var status;
				try {
					status = isTimeout != "timeout" ? "success" : "error";
					// Make sure that the request was successful or notmodified
					if (status != "error") {
						// process the data (runs the xml through httpData regardless of callback)
						var data = jQuery.uploadHttpData(xml, s.dataType);
						// If a local callback was specified, fire it and pass it the data
						if (s.success) s.success(data, status);

						// Fire the global callback
						if (s.global) jQuery.event.trigger("ajaxSuccess", [xml, s]);
					} else {
						jQuery.handleError(s, xml, status);
					}
				} catch (e) {
					status = "error";
					jQuery.handleError(s, xml, status, e);
				}

				// The request was completed
				if (s.global) jQuery.event.trigger("ajaxComplete", [xml, s]);

				// Handle the global AJAX counter
				if (s.global && !--jQuery.active)
					jQuery.event.trigger("ajaxStop");

				// Process result
				if (s.complete)
					s.complete(xml, status);

				if (typeof(UPLOAD_DEBUG) == "undefined" || !UPLOAD_DEBUG) {
					// Remove iframe and form
					jQuery(iframeElem).unbind();
					window.setTimeout(function () {
						try {
							jQuery(iframeElem).remove();
							jQuery(form).remove();
						} catch (e) {
							jQuery.handleError(s, xml, null, e);
						}
					}, 100);
				}
				xml = null;
			}
		};
		// Timeout checker
		if (s.timeout > 0) {
			window.setTimeout(function () {
				// Check to see if the request is still happening
				if (!requestDone) uploadCallback("timeout");
			}, s.timeout);
		}
		try {
			var form = jQuery('#' + formId);
			jQuery(form).attr('action', s.url);
			jQuery(form).attr('method', 'POST');
			jQuery(form).attr('target', frameId);
			if (form.encoding) {
				jQuery(form).attr('encoding', 'multipart/form-data');
			} else {
				jQuery(form).attr('enctype', 'multipart/form-data');
			}
			if (typeof(UPLOAD_DEBUG) == "undefined" || !UPLOAD_DEBUG) {
				jQuery(form).submit();
			}

		} catch (e) {
			jQuery.handleError(s, xml, null, e);
		}

		if (typeof(UPLOAD_DEBUG) == "undefined" || !UPLOAD_DEBUG) {
			jQuery('#' + frameId).load(uploadCallback);
		}
		return {
			abort: function () {
				// Stop the upload
				try {
					iframeElem.contentWindow.document.execCommand('Stop');
				} catch (ex) {
					// Do nothing
				}
				try {
					iframeElem.contentWindow.stop();
				} catch (ex) {
					// Do nothing
				}
				// Unbind events and remove the iframe
				jQuery(iframeElem).unbind();
				window.setTimeout(function () {
					try {
						jQuery(iframeElem).remove();
						jQuery(form).remove();
					} catch (e) {
						// Do nothing
					}
				}, 100);
			}
		};

	},

	uploadHttpData: function (r, type) {
		var data = !type;
		data = type == "xml" || data ? r.responseXML : r.responseText;
		// If the type is "script", eval it in global context
		if (type == "script")
			jQuery.globalEval(data);
		// Get the JavaScript object, if JSON is used.
		if (type == "json")
			eval("data = " + data);
		// evaluate scripts within html
		if (type == "html")
			jQuery("<div>").html(data).evalScripts();

		return data;
	},

	handleError: function (s, xhr, status, e) {
		// If a local callback was specified, fire it
		if (s.error) {
			if (xhr.hasOwnProperty("responseXML")) {
				xhr.url = xhr.responseXML.URL;
				xhr.statusText = xhr.responseText;
			}
			s.error.call(s.context || window, xhr, status, e);
		}
		// Fire the global callback
		if (s.global) {
			(s.context ? jQuery(s.context) : jQuery.event).trigger("ajaxError", [xhr, s, e]);
		}
	}
});
