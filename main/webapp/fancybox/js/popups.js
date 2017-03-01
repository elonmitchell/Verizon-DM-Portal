var onPopupStartFuncs = new Array();
var onPopupCompleteFuncs = new Array();
var onPopupClosedFuncs = new Array();
var jqPopupParentElem = null;
var jqLastPopup = null;

var fbOptions = {
  padding: 0,
  autoScale: false,
  overlayColor: '#000',
  overlayOpacity: 0.5,
  overlayShow: true,
  hideOnOverlayClick: false,
  hideOnContentClick: false,
  onStart: function (selectedArray, selectedIndex, selectedOpts) {
    fbOnStart(selectedArray, selectedIndex, selectedOpts);
  },
  onComplete: function (selectedArray, selectedIndex, selectedOpts) {
    fbOnComplete(selectedArray, selectedIndex, selectedOpts);
  },
  onClosed: function (selectedArray, selectedIndex, selectedOpts) {
    fbOnClosed(selectedArray, selectedIndex, selectedOpts);
  }
}

var modalFbOptions = {
  padding: 0,
  autoScale: false,
  overlayColor: '#000',
  overlayOpacity: 0.5,
  modal: true,
  onStart: function (selectedArray, selectedIndex, selectedOpts) {
    fbOnStart(selectedArray, selectedIndex, selectedOpts);
  },
  onComplete: function () {
    fbOnComplete();
  },
  onClosed: function () {
    fbOnClosed();
  }
}

$j(function () {
  addFancyboxClicks();
});

function addFancyboxClicks() {
  if ($j('a').fancybox) {
    $j('#single_image').fancybox(fbOptions); // to verify basic fancybox functionality

    // Tie button clicks to fancybox popups for regular popups
    $j('#fb-popup-link').fancybox(fbOptions);
    $j('.fb-button, .fb-button2, .fb-link').each(function (index) {
      var jqFancyButton = $j(this);
      if (jqFancyButton.attr('hasClick') != "true") {
        jqFancyButton.click(function (event) {
          //if (!isButtonEnabled(theButton)) return;
          event.preventDefault();
          var popupDivId;
          if ($j(this).attr('popupid')) {
            popupDivId = $j(this).attr('popupid');
          } else {
            popupDivId = $j(this).attr('id') + "-popup";
          }
          var hrefVal = $j(this).attr('href');
          // Show popup and blur button
          showFbPopup(popupDivId, hrefVal);
          $j(this).blur();
        });
        jqFancyButton.attr('hasClick', 'true');
      } // if
    });
    // Tie button clicks to fancybox popups for modal popups
    $j('#fb-modal-popup-link').fancybox(modalFbOptions);
    $j('.fb-modal-button, .fb-modal-link').each(function (index) {
      var jqFancyButton = $j(this);
      if (jqFancyButton.attr('hasClick') != "true") {
        jqFancyButton.click(function () {
          var popupDivId = $j(this).attr('id') + "-popup";
          var hrefVal = $j(this).attr('href');
          // Show popup and blur button
          showModalFbPopup(popupDivId, hrefVal);
          $j(this).blur();
        });
        jqFancyButton.attr('hasClick', 'true');
      }
    });
    $j('.popup-submit-button button#cancelButton').each(function (index) {
      var jqSubmitButton = $j(this);
      //alert(jqSubmitButton.attr("id"));
      if (jqSubmitButton.attr('hasClick') != "true") {
        jqSubmitButton.click(function (event) {
          $j.fancybox.close();
        });
        jqSubmitButton.attr('hasClick', 'true');
      }
    });
  }
}
function getJqPopup(popupDivId, sourceUrl) {
  var jqPopupRef = "#"+popupDivId;
  // Create this popup div if necessary
	var jqPopupWrap = $j('.fancybox-popup-wrap');
  var jqPopup = jqPopupWrap.find(jqPopupRef);
  if (jqPopup.length == 0) {
    //var jqPopup = $j(jqPopupRef);
    //if (jqPopup.length == 0) {
      jqPopupWrap.append('<div id="'+popupDivId+'" class="fb-popup">');
      jqPopup = jqPopupWrap.find(jqPopupRef);
    //}
  }
  // Get content from href of source and stuff it into the popup div
  // TODO do just once (set flag after first time)
  if (sourceUrl != null && sourceUrl != "" && sourceUrl != "#") {
		jqLastPopup = null;
    $j.ajax({
      url: sourceUrl,
      async: false,
      context: document.body,
      error: function (jqXHR, textStatus, errorThrown){
        if (errorThrown.indexOf("login") != -1) {
          location.reload();
        } else {
          alert("Exception occured " + textStatus);
        }
      }
    }).done(function (responseVal) {
      jqPopup.html(responseVal);
      addFancyboxClicks();
			jqLastPopup = jqPopup;
    });
  }
  return jqPopup;
}
function showFbPopup(popupDivId, hrefVal) {
  // Get the popup div (create if necessary)
  var jqPopup = getJqPopup(popupDivId, hrefVal);
  // Update link and launch popup
  $j('#fb-popup-link').attr("href", "#"+popupDivId).click();
  return jqPopup;
}
function showModalFbPopup(popupDivId, hrefVal) {
  // Get the popup div (create if necessary)
  var jqModalPopup = getJqPopup(popupDivId, hrefVal);
  // Update link and launch popup
  $j('#fb-modal-popup-link').attr("href", "#"+popupDivId).click();
  return jqModalPopup;
}

function fbOnStart(selectedArray, selectedIndex, selectedOpts) {
  // Set parent elem
  var jqParentElem = $j('body');
  var selectedItem = selectedArray[selectedIndex];
  if (selectedItem != null) {
    var href = selectedItem.nodeName ? $j(selectedItem).attr('href') : selectedItem.href;
    var popupId = href.substr(href.indexOf("#"));
    if (popupId != "" && popupId.length > 0) {
      jqParentElem = $j(popupId);
    }
  }
  jqPopupParentElem = jqParentElem;

  // Change enter keydown to trigger default button
  $j('body').unbind('keydown', handlePopupEnterKeydown);
  $j('body').bind('keydown', handlePopupEnterKeydown);

  // Hide/show elements
  jqParentElem.find('.errorRow').hide();
  jqParentElem.find('.hideMeOnInit').hide();
  jqParentElem.find('.showMeOnInit').show();

  // Set initial states/values
  jqParentElem.find('*[initdisabled]').each(function () {
    var bDisabled = ($j(this).attr('initdisabled') == "true");
    $j(this).attr('disabled', bDisabled);
  });
  jqParentElem.find('input[initvalue], select[initvalue], textarea[initvalue]').each(function () {
    $j(this).val($j(this).attr('initvalue'));
  });
  jqParentElem.find('input[initchecked]').each(function () {
    var bChecked = ($j(this).attr('initchecked') == "true");
    $j(this).attr('checked', bChecked);
  });
  jqParentElem.find('*[initclone]').each(function () {
    var real = $j(this);
    var cloned = real.clone(true);
    // Put the cloned element directly after the real element
    cloned.insertAfter(real);
    // Remove the real element
    real.remove();
  });
  ////initSavedVals();

  // Trigger change for radio inputs that have elements tied to them
  var tiedRadioIds = {};
  jqParentElem.find('[tieToRadioId]').each(function() {
    var radioId = $j(this).attr('tieToRadioId');
    tiedRadioIds[radioId] = 1;
  });
  $j.each(tiedRadioIds, function(radioId, value) {
    $j('#'+radioId).trigger('change');
  });

  // Custom popup start functions
  for (var i = 0; i < onPopupStartFuncs.length; i++) {
    var onPopupStartFunc = onPopupStartFuncs[i];
    onPopupStartFunc();
  }
}

function fbOnComplete() {
  // Custom popup complete functions
  for (var i = 0; i < onPopupCompleteFuncs.length; i++) {
    var onPopupCompleteFunc = onPopupCompleteFuncs[i];
    onPopupCompleteFunc();
  }
}

function fbOnClosed() {
	// Delete popup content
	if (jqLastPopup != null) {
  	jqLastPopup.remove();
  }

  // Custom popup start functions
  for (var i = 0; i < onPopupClosedFuncs.length; i++) {
    var onPopupClosedFunc = onPopupClosedFuncs[i];
    onPopupClosedFunc();
  }
}

function handlePopupEnterKeydown(event) {
  var bAllowKeydown = true;

  var keycode = (event.keyCode ? event.keyCode : (event.which ? event.which : event.charCode));
  if (keycode == 13) { // keycode for enter key
    bAllowKeydown = false; // by default don't allow enter keydown
    var targetElem = event.target;
    if (targetElem != null) {
      if (targetElem.type == "textarea") {
        bAllowKeydown = true; // inside textarea so allow enter keydown
      } else {
        // Get jqPopupParentElem from target element if necessary
        if (jqPopupParentElem == null || jqPopupParentElem.length == 0) {
          jqPopupParentElem = $j(targetElem).parents('.fb-popup');
        }
      }
    }

    if (bAllowKeydown == false) {
      // Force the 'Enter Key' to implicitly click the default button
      if (jqPopupParentElem != null) {
        var jqBtnDefault = jqPopupParentElem.find('button.default');
        if (jqBtnDefault.length > 0) {
          jqBtnDefault.click();
        }
      }
    }
  }

  return bAllowKeydown;
}

function showCriticalPopup(errMsg, titleStr) {
  // Show popup
  var jqPopup = showFbPopup('error-popup');
  // Update title and error message
  if (titleStr != null) jqPopup.find('.title').html(titleStr);
  jqPopup.find('.errMsg').html(errMsg);
}

function showQuestionPopup(questionStr, titleStr, callbackFunc) {
  // Show popup
  var jqPopup = showFbPopup('question-popup');
  // Update title and error message
  if (titleStr != null) jqPopup.find('.title').html(titleStr);
  jqPopup.find('.questionStr').html(questionStr);
  if (callbackFunc != null) {
    jqPopup.find('#okButton').click(function() {
      callbackFunc();
      $j.fancybox.close();
    });
  }
}
function showWaitPopup(titleStr, waitStr) {
  // Show popup
  var jqPopup = showModalFbPopup('wait-popup');
  // Update title and wait message
  if (titleStr != null) jqPopup.find('.titleText').html(titleStr);
  jqPopup.find('.waitText').html(waitStr);
}
function closePopup() {
  $j.fancybox.close();
}
