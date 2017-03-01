var currentHeight;
var endingHeight;
var newPolicyDialog;
var contentTable;
var currentOpacity = 0;
var dragObject;

      var helpStartingX = -1;
      var helpStartingY = -1;


    function saveDialogLocation( left, top)
    {
        //alert("saving location");
        // get the information
        //
        //var the_name = prompt("What's your name?","");
        var the_date = new Date("December 31, 2023");
        var the_cookie_date = the_date.toGMTString();
    
        // build and save the cookie
        //
        var cookieString = left.toString() + "," + top.toString();
        var the_cookie = "report_help_location=" + escape(cookieString);
        the_cookie = the_cookie + ";expires=" + the_cookie_date + "; path=/";
        document.cookie = the_cookie;
        //alert( the_cookie );
    
    }
        
    function wasContextHelpShowing() {
        showingValue = readCookie("report_context_help_showing");
        if ( showingValue == null ) 
            return false;
        return ( showingValue == "true" );
    }
    
    function getDialogLeft() {
        theCookie = unescape( readCookie( "report_help_location" ));
        
        return theCookie.split(',')[0];
    }
    
    function getDialogTop() {
        theCookie = unescape( readCookie( "report_help_location" ));
        
        return theCookie.split(',')[1];
    }
    

    function setHelpDialogSize() {
        setCookie( "report_context_help_showing", "true" );
        newPolicyDialog = parent.document.getElementById( "contextHelpFrame" );
        newPolicyDialog.style.visibility = "hidden";
        newPolicyDialog.style.display = "block";
        animateOpen = parent.animateOpen;
        dialogTableRef = document.getElementById( "dialogTable" );
        if ( dialogTableRef == null ) {
            closeHelpDialog();
        }
        else {
            newWidth = dialogTableRef.clientWidth + 5;
            newHeight = dialogTableRef.clientHeight + 5;
            
            newPolicyDialog.style.width = newWidth;
            newPolicyDialog.style.height = newHeight;
            windowWidth = parent.document.body.clientWidth;
            windowHeight = parent.document.body.clientHeight;
            getDialogLeft();
            if ( getDialogLeft() == -1 ) {
                newXPosition = (windowWidth - newWidth) - 5;
                newYPosition = 90; //(windowHeight - newHeight);
            }
            else {
                newXPosition = parseInt(getDialogLeft());
                newYPosition = parseInt(getDialogTop());
            }
            if (( newXPosition != -1 ) && ( newYPosition != -1 )) {
                try {
                    if (( parseInt( newXPosition ) + 40 ) > parseInt(parent.document.body.clientWidth ))
                        newXPosition = newXPosition - (( newXPosition + 40) - parseInt(parent.document.body.clientWidth));
                    if (( newYPosition  + 20 ) > parseInt(parent.document.body.clientHeight ))
                        newYPosition = newYPosition - (( newYPosition + 20 ) - parseInt(parent.document.body.clientHeight));
                    newPolicyDialog.style.left = newXPosition;
                    newPolicyDialog.style.top = newYPosition;
                } catch (e) {}
            }
            if ( animateOpen ) {
                helpButton = parent.document.getElementById( "contextHelpButton" );
                animateOpening(newPolicyDialog, ( parent.document.body.clientWidth -49), 67, 32, 14, dialogTableRef);
            }
            else
                newPolicyDialog.style.visibility = "";
            
            helpButton = parent.document.getElementById("contextHelpButton");
            if ( helpButton != null ) {
                setOpacity( helpButton, 35 );
            }
            
            headerRef = document.getElementById("dialogHeader");
            
            if ( headerRef != null ) {
                // add drag listener
                headerRef.onmousedown = startHelpDrag;
            }
        }
    }

var _zoomingObject, 
    _startingX, 
    _startingY, 
    _startingWidth, 
    _startingHeight,
    _endingX, 
    _endingY, 
    _endingWidth, 
    _endingHeight, 
    _innerContentObject,
    _contextHelpDialog;
var _zoomingOpacity = 50;
var _numberOfSteps = 20.0;
var _currentStep;
var _widthIncrement,
    _heightIncrement,
    _xPositionIncrement,
    _yPositionIncrement;


    function setOpacity( theObject, newOpacity ) {
        //_innerContentObject.style.display = "none";
        try {
            theObject.style.filter="alpha(opacity=" + newOpacity + ")";
        } catch (e) {} // Netscape my burp on this;

        try {
            theObject.style.opacity = newOpacity / 100;
        } catch (e) {} // I.E. burps on this, just ignore it.
    }

    function animateOpening(contextHelpDialog, startingX, startingY, startingWidth, startingHeight, innerContentObject) {
        _contextHelpDialog = contextHelpDialog;
        _zoomingObject      = parent.document.getElementById("contextZoomingDiv");
        _endingX          = convertPositionToNumber( _contextHelpDialog.style.left );
        _endingY          = convertPositionToNumber( _contextHelpDialog.style.top );
        _endingWidth      = convertPositionToNumber( _contextHelpDialog.style.width );
        _endingHeight     = convertPositionToNumber( _contextHelpDialog.style.height );
        _startingX            = startingX;
        _startingY            = startingY;
        _startingWidth        = startingWidth;
        _startingHeight       = startingHeight;
        _innerContentObject = innerContentObject;

        _numberOfSteps = Math.round( Math.max(Math.abs(_endingX - _startingX), Math.abs(_endingY - _startingY))/50);

        _xPositionIncrement = (_endingX - _startingX) / _numberOfSteps;
        _yPositionIncrement = (_endingY - _startingY) / _numberOfSteps;

        _widthIncrement = (_endingWidth - _startingWidth ) / _numberOfSteps;
        _heightIncrement = (_endingHeight - _startingHeight ) / _numberOfSteps;

        setOpacity( _zoomingObject, _zoomingOpacity );

        _currentStep = 1;
        setTimeout( "runOpenAnimation();", 10 );
    }

    function runOpenAnimation() {
        currentX = _startingX + ( _xPositionIncrement * _currentStep );
        currentY = _startingY + ( _yPositionIncrement * _currentStep );
        currentWidth = _startingWidth + ( _widthIncrement * _currentStep );
        currentHeight = _startingHeight + ( _heightIncrement * _currentStep );
        _zoomingObject.style.left = currentX;
        _zoomingObject.style.top = currentY;
        _zoomingObject.style.width = currentWidth;
        _zoomingObject.style.height = currentHeight;
        _zoomingObject.style.display = "block";

        if ( _currentStep >= _numberOfSteps )
            finishOpenAnimation();
        else {
            _currentStep++;
            setTimeout( "runOpenAnimation();", 10 );
        }
    }
    
    function finishOpenAnimation() {
        _contextHelpDialog.style.visibility = "";
        _zoomingOpacity = 75;
        setOpacity( _contextHelpDialog, _zoomingOpacity );
        _zoomingObject.style.display = "none";
        setTimeout( "finishDisplayingDialog();", 10 );
    }

    function finishDisplayingDialog() {
        _zoomingOpacity = 100;
        setOpacity( _contextHelpDialog, _zoomingOpacity );
    }

    function animateClosing(contextHelpDialog, endingX, endingY, endingWidth, endingHeight, innerContentObject) {
        _contextHelpDialog = contextHelpDialog;
        _zoomingObject      = parent.document.getElementById("contextZoomingDiv");
        _startingX          = convertPositionToNumber( _contextHelpDialog.style.left );
        _startingY          = convertPositionToNumber( _contextHelpDialog.style.top );
        _startingWidth      = convertPositionToNumber( _contextHelpDialog.style.width );
        _startingHeight     = convertPositionToNumber( _contextHelpDialog.style.height );
        _endingX            = endingX;
        _endingY            = endingY;
        _endingWidth        = endingWidth;
        _endingHeight       = endingHeight;
        _zoomingOpacity = 50;

        _numberOfSteps = Math.round( Math.max(Math.abs(_endingX - _startingX), Math.abs(_endingY - _startingY))/50);
//alert( Math.round( Math.max(Math.abs(_endingX - _startingX), Math.abs(_endingY - _startingY))/50) );
        _xPositionIncrement = (_endingX - _startingX) / _numberOfSteps;
        _yPositionIncrement = (_endingY - _startingY) / _numberOfSteps;

        _widthIncrement = (_endingWidth - _startingWidth ) / _numberOfSteps;
        _heightIncrement = (_endingHeight - _startingHeight ) / _numberOfSteps;

        //_innerContentObject.style.display = "none";
        try {
            _zoomingObject.style.filter="alpha(opacity=" + _zoomingOpacity + ")";
        } catch (e) {} // Netscape my burp on this;

        try {
            _zoomingObject.style.opacity = _zoomingOpacity / 100;
        } catch (e) {} // I.E. burps on this, just ignore it.


        _currentStep = 1;
        setTimeout( "runCloseAnimation();", 10 );
    }

    function runCloseAnimation() {

        currentX = _startingX + ( _xPositionIncrement * _currentStep );
        currentY = _startingY + ( _yPositionIncrement * _currentStep );
        currentWidth = _startingWidth + ( _widthIncrement * _currentStep );
        currentHeight = _startingHeight + ( _heightIncrement * _currentStep );
        _zoomingObject.style.left = currentX;
        _zoomingObject.style.top = currentY;
        _zoomingObject.style.width = currentWidth;
        _zoomingObject.style.height = currentHeight;
        _zoomingObject.style.display = "block";
        if ( _contextHelpDialog.style.display == "block" )
            _contextHelpDialog.style.display = "none";
        if ( _currentStep >= _numberOfSteps )
            finishCloseAnimation();
        else {
            _currentStep++;
            setTimeout( "runCloseAnimation();", 10 );
        }
    }
    
    function finishCloseAnimation() {
        _zoomingObject.style.display = "none";
        helpButton = parent.document.getElementById("contextHelpButton");
        if ( helpButton != null ) {
            setOpacity( helpButton, 100 );
        }

    }


    function closeHelpDialog() {
        setCookie( "report_context_help_showing", "false" );
//        alert("setting to false;");

        newPolicyDialog = parent.document.getElementById( "contextHelpFrame" );
        dialogTableRef = document.getElementById( "dialogTable" );
        animateClosing(newPolicyDialog, ( parent.document.body.clientWidth -49), 67, 32, 14, dialogTableRef);

    }

    function autoShowHelpDialog() {
        try {
            animateOpen = false;
            newPolicyDialog = document.getElementById( "contextHelpFrame" );
            if ( newPolicyDialog.style.display == "none")
                newPolicyDialog.src = helpDialogSrc;
//            else
//                closeHelpDialog();
        } catch (e) {
            alert( "Error loading dialog iFrame: " + e.toString());
        }
    }

    function showHelpDialog() {
        try {
            animateOpen = true;
            newPolicyDialog = document.getElementById( "contextHelpFrame" );
            if ( newPolicyDialog.style.display == "none")
                newPolicyDialog.src = helpDialogSrc;
//            else
//                closeHelpDialog();
        } catch (e) {
            alert( "Error loading dialog iFrame: " + e.toString());
        }
    }
    
    function startHelpDrag(evt) {
        if (evt == null )
            evt = window.event;
        dragObject = parent.document.getElementById( "contextHelpFrame" );

        parent.draggedFlag = true;
        helpStartingX = evt.screenX;
        helpStartingY = evt.screenY;
        addHelpMouseEventHandlers();
    }

    function addHelpMouseEventHandlers() {
        document.body.onmouseup = stopHelpDrag;
        document.body.onmousemove = doHelpDrag;
    }

    function doHelpDrag(evt) {
            currentLeftEdge = convertPositionToNumber( dragObject.style.left );
            currentTopEdge = convertPositionToNumber( dragObject.style.top );

            if ( !evt )
                evt = window.event;
            deltaX = evt.screenX - helpStartingX;
            deltaY = evt.screenY - helpStartingY;

            if ((currentTopEdge + deltaY) < 0 )
                newTop = 0;
            else
                newTop = (currentTopEdge + deltaY);
            dragObject.style.left = (currentLeftEdge + deltaX);
            dragObject.style.top = newTop;
            helpStartingX = evt.screenX;
            helpStartingY = evt.screenY;
    }
    
    function stopHelpDrag() {
        removeHelpMouseEventHandlers();
        newPolicyDialog = parent.document.getElementById( "contextHelpFrame" );
        if ( parent.draggedFlag ) {
            currentLeft = convertPositionToNumber(newPolicyDialog.style.left);
            currentTop = convertPositionToNumber(newPolicyDialog.style.top);
            
            saveDialogLocation( currentLeft, currentTop );
            
        }

    }


    function removeHelpMouseEventHandlers() {
        document.body.onmouseup = null;
        document.body.onmousemove = null;

    }

