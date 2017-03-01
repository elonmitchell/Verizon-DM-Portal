/* global ReportSchedulesManager, PopupManager, Bloodhound */
var ReportSchedulesEdit = (function () {
	"use strict";

	var _reportSchedulesDG;
	var _initRecipientsList;

	function _init() {
		_reportSchedulesDG = ReportSchedulesManager.getDataGrid();

		_initPopups();
	}

	function _initPopups() {

		PopupManager.addIdEventListeners("scr-ReportSchedulesEdit-loading", {
			screenShown: function(eventObj) { // [[ Edit Report Schedules | Loading screen | Screen shown ]]
				var lastClickedButtonId = PopupManager.getLastClickedButtonId();
				var jqLastClickedButton = $('#'+lastClickedButtonId);
				var scheduleId = jqLastClickedButton.data("scheduleid");
				_reportSchedulesDG.getReportScheduleData(scheduleId, function(scheduleDataHash) {
					var fieldData = {};
					$.each(scheduleDataHash, function(key, val) {
						if (key === "recipients") {
							_initRecipientsList = val;
						} else {
							fieldData[key] = { value: val };
						}
					});
					var screenRenderData = { formFields: { fieldData: fieldData}};
					PopupManager.gotoScreen(eventObj.popupId, "scr-ReportSchedulesEdit-input", screenRenderData)
				});
			}
		});

		PopupManager.addIdEventListeners("scr-ReportSchedulesEdit-input", {
			screenShown: function(eventObj) { // [[ Edit Report Schedules | Input screen | Screen shown ]]
				var screen = PopupManager.getScreen(eventObj);
				_populateRecipients(screen, "recipients");
			},
			buttonClick: function(eventObj) { // [[ Edit Report Schedules | Input screen | Button clicks ]]
				if (eventObj.value === "edit") {
					// Validate
					var screen = PopupManager.getScreen(eventObj);
					var jqScreenDiv = screen.getScreenDiv();
					var jqRecipientsTF = screen.getTextField("recipients");
					var jqRecipientsHidden = jqScreenDiv.find('input[type="hidden"][name="recipients"]');
					jqRecipientsTF.removeAttr("required");
					var bIsValid = screen.validateFields();
					if (jqRecipientsHidden.val() === "[]") {
						bIsValid = false;
						screen.showFieldError("recipients", "required");
					}

					if (bIsValid) {
						//Split the date time field into date, hour, minute and am/pm fields.
						var splitDateTime = document.getElementById("startDateTime").value;
						var dateTimeAmPm = splitDateTime.split(" ");
						var rptTime = dateTimeAmPm[1].split(":");

						// Loop through the UI reponse to extract only the recipient id out of the UI response.
						var reportRecipients = document.querySelectorAll('input[type="hidden"][name="recipients"]')[0].value;
						var workReport = JSON.parse(reportRecipients);
						var emailId ='';
						var emailIdComma = ",";
						var x = 0;
						var times = 0;
						for (x in workReport) {
							if (x > 0) {
									emailId += emailIdComma;
								    								}
							emailId += (workReport[x].id );
						}

						var editData = {
							scheduleId: document.getElementById("scheduleIdEP").value,
							reportFrequency: document.getElementById("frequency").value,
							startTimeDate: dateTimeAmPm[0],
							startTimeHour: rptTime[0],
							startTimeMinute: rptTime[1],
							emailsId: emailId,
							amvalueId: dateTimeAmPm[2]
						}

						// Go to the wait screen
						PopupManager.gotoScreen(eventObj.popupId, "scr-ReportSchedulesEdit-wait");
                                                
                                                 _reportSchedulesDG.editSelectedRows(editData, function(bIsSuccess) {
                                                       if (bIsSuccess) {
								// Reload the page
								location.reload();
							} else {
								// Show error
								var screenRenderData = { resultText: "There was an error updating the report config.  Please contact your system administrator." };
								PopupManager.gotoScreen(eventObj.popupId, "scr-ReportSchedulesEdit-result", screenRenderData);
							}
                                                 });
					}
				}
			}
		});

	}

	function _populateRecipients(screen, recipientsFieldId) {
		debugger
		var recipientsAutocompleteUrl = "/report-viewer/email";
		var jqRecipientsTF = screen.getTextField(recipientsFieldId);
		jqRecipientsTF.textext({
			plugins: 'autocomplete filter tags ajax', // prompt
			prompt: 'Type one or more recipients...',
			ajax: {
				type: 'POST',
				url: recipientsAutocompleteUrl,
				dataType: 'json',
				cacheResults: false
			},
			tags: { items: _initRecipientsList },
			filterItems: _initRecipientsList,
			ext: {
				itemManager: {
					stringToItem: function(str) { return { email: str, name: str }; },
					itemToString: function(item) { return item.name; },
					itemContains: function(item, needle) { return item.name.toLowerCase().indexOf(needle.toLowerCase()) == 0; },
					compareItems: function(item1, item2) { return item1.name == item2.name; }
				}
			}
		}).bind('isTagAllowed', function(e, data) {
			data.result = true;
			var errorStr;
			// Make sure new tag is a valid email address
			if (_isValidEmail(data.tag.email)) {
				// Check for duplicates
				var formDataStr = jQuery(e.target).textext()[0].tags()._formData;
				if (formDataStr.length) {
					var formDataArray = eval(formDataStr);
					for (var i = 0; i < formDataArray.length; i++) {
						var formData = formDataArray[i];
						if (formData.name == data.tag.name) {
							errorStr = "'" + data.tag.name + "' is already listed.";
							screen.showFieldError(recipientsFieldId, "tagError", errorStr);
							data.result = false;
							break;
						}
					}
				}
			} else {
				errorStr = "'" + data.tag.name + "' is not in the address book and is not a valid email address.";
				screen.showFieldError(recipientsFieldId, "tagError", errorStr);
				data.result = false;
			}
		}).keypress(function() {
			screen.hideFieldError(recipientsFieldId);
		}).blur(function() {
			$(this).val("");
		});
	}

	function _populateRecipients2(screen, recipientsFieldId) {
		var jqRecipients = screen.getTextField(recipientsFieldId);
		var queryVal;
		var engine = new Bloodhound({
			//local: [{value: 'red'}, {value: 'blue'}, {value: 'green'} , {value: 'yellow'}, {value: 'violet'}, {value: 'brown'}, {value: 'purple'}, {value: 'black'}, {value: 'white'}],
			remote: {
				url: "/report-viewer/email",
				replace: function(url, query) {
					queryVal = query;
					return url;
				},
				ajax : {
					beforeSend: function(jqXhr, settings){
						settings.data = $.param({q: queryVal})
					},
					type: "POST"
				}
			},
			datumTokenizer: function(d) {
				debugger;
				return Bloodhound.tokenizers.whitespace(d.value);
			},
			queryTokenizer: Bloodhound.tokenizers.whitespace
		});

		engine.initialize();

		jqRecipients.tokenfield({
			typeahead: [null, { source: engine.ttAdapter() }]
		});

	}

	function _isValidEmail(email) {
		var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		return re.test(email);
	}

	function getFieldVal(jqField) {
		var val;
		if (jqField.attr("id") == "scheduleRecipients") {
			var jqHidden = jqField.siblings("input[type='hidden']");
			val = jqHidden.val();
			if (val == "[]") val = "";
		} else {
			val = jqField.val();
		}
		return val;
	}

	// ************************************ //
	// *** PUBLIC FUNCTIONS *************** //
	// ************************************ //

	return {
		init: _init
	};

})();
