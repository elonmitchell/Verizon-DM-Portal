var EXT_GIF = ".gif";
var EXT_JPG = ".jpg";
var EXT_PNG = ".png";

function findObjectFlex(obj) {
	var theObj;
	if (typeof obj == "string") {
		theObj = findObject(obj);
	} else {
		theObj = obj;
	}
	return theObj;
}

function setButtonPanelStatus(obj,msg) {
	oDiv = findObjectFlex(obj);
	if (msg != null && msg != "" && msg.length > 0) {
		oDiv.innerHTML = msg;
	} else {
		oDiv.innerHTML = "&nbsp";
	}
}

function _enableImage(imgSrc) {
	var replaceStr, searchStr;
	if (imgSrc.indexOf(EXT_GIF) == (imgSrc.length - EXT_GIF.length)) {
		searchStr = "_disabled" + EXT_GIF;
		replaceStr = EXT_GIF;
	} else if (imgSrc.indexOf(EXT_JPG) == (imgSrc.length - EXT_JPG.length)) {
		searchStr = "_disabled" + EXT_JPG;
		replaceStr = EXT_JPG;
	} else if (imgSrc.indexOf(EXT_PNG) == (imgSrc.length - EXT_PNG.length)) {
		searchStr = "_disabled" + EXT_PNG;
		replaceStr = EXT_PNG;
	}
	
	var regexpr = new RegExp(searchStr, "g");
	var result = imgSrc.replace(regexpr,replaceStr);
	return result;
}

function _disableImage(imgSrc) {
	var replaceStr, searchStr;
	if (imgSrc.indexOf(EXT_GIF) == (imgSrc.length - EXT_GIF.length)) {
		replaceStr = "_disabled" + EXT_GIF;
		searchStr = EXT_GIF;
	} else if (imgSrc.indexOf(EXT_JPG) == (imgSrc.length - EXT_JPG.length)) {
		replaceStr = "_disabled" + EXT_JPG;
		searchStr = EXT_JPG;
	} else if (imgSrc.indexOf(EXT_PNG) == (imgSrc.length - EXT_PNG.length)) {
		replaceStr = "_disabled" + EXT_PNG;
		searchStr = EXT_PNG;
	}
	
	var regexpr = new RegExp(searchStr, "g");
	var result = imgSrc.replace(regexpr,replaceStr);
	return result;
}

function disablePanelButton(obj,disableSrc,tooltip) {
	var oBtn = findObjectFlex(obj);
	if (oBtn && !isPanelButtonDisabled(oBtn)) {
		// see if the button has an image, if so set the src to the disabled one
		var oImg = findObjectFlex(oBtn.id + "Icon");
		if (oImg) {
			oImg.src = disableSrc;
		}
		// disable and set the style
		oBtn.title = (tooltip && tooltip != "") ? tooltip : oBtn.title;
		oBtn.className = "buttonPanelButtonDisabled";		
		oBtn.disabled = true;		
	}
}

function enablePanelButton(obj,enableSrc,tooltip) {
	var oBtn = findObjectFlex(obj);
	if (oBtn && isPanelButtonDisabled(oBtn)) {
		// enable and set the style
		oBtn.disabled = false;
		oBtn.className = "buttonPanelButton";
		oBtn.title = (tooltip && tooltip != "") ? tooltip : oBtn.title;		
		// see if the button has an image, if so set the src to the disabled one
		var oImg = findObjectFlex(oBtn.id + "Icon");
		if (oImg) {
			oImg.src = enableSrc;
		}
	}
}

function isPanelButtonDisabled(obj) {
	var oBtn = findObjectFlex(obj);
	if (oBtn && oBtn.disabled == true && oBtn.className == "buttonPanelButtonDisabled") {
		return true;
	}
	return false;
}