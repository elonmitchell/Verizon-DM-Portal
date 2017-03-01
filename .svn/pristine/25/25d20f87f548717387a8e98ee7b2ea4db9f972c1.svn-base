    var startingX = -1;
    var startingY = -1;
    var draggingDialog;
    
    var DIALOG_SRC = globals_contextPath + "errorDialog.vm";
    var DIALOG_TYPE_INFORMATION = 0;
    var DIALOG_TYPE_QUESTION = 1;
    var DIALOG_TYPE_EXCLAMATION = 2;
    var DIALOG_TYPE_CRITICAL = 3;
    
    var questionDialogOKFunction = null;
    
    // dialog functions
    function showDialog( frameSrc ) {
        try {
            newPolicyDialog = document.getElementById( "dialogFrame" );
            newPolicyDialog.src = frameSrc;
        } catch (e) {
            //alert( "Error loading dialog iFrame: " + e.toString());
        }
    }
    
    /**
     * displays an information popup dialog with the specified message
     * @param message - the message to be displayed     
     */     
    function showInformationDialog(message, callbackFunction) {
        if ( callbackFunction != undefined )
            questionDialogOKFunction = callbackFunction;
        else
            questionDialogOKFunction = null;
        showMessageDialog(message,DIALOG_TYPE_INFORMATION); 
    }
    
    /**
     * displays a question popup dialog with the specified message
     * @param message - the message to be displayed     
     */      
    function showQuestionDialog(message, callbackFunction) {
        if ( callbackFunction != undefined )
            questionDialogOKFunction = callbackFunction;
        else
            questionDialogOKFunction = null;
        showMessageDialog(message,DIALOG_TYPE_QUESTION);    
    }
    
    
    /**
     * displays an exclamation popup dialog with the specified message
     * @param message - the message to be displayed     
     */    
    function showExclamationDialog(message) {
        showMessageDialog(message,DIALOG_TYPE_EXCLAMATION); 
    }
    
    /**
     * displays a critical popup dialog with the specified message
     * @param message - the message to be displayed     
     */
    function showCriticalDialog(message) {
        showMessageDialog(message,DIALOG_TYPE_CRITICAL);    
    }
    
    /**
     * displays a popup dialog with the specified message
     * @param message - the message to be displayed
     * @param type - the type of dialog to display
     *               (0)DIALOG_TYPE_INFORMATION
     *               (1)DIALOG_TYPE_QUESTION
     *               (2)DIALOG_TYPE_EXCLAMATION
     *               (3)DIALOG_TYPE_CRITICAL 
     */
    function showMessageDialog(message,type) {
        message = (message) ? message : globals_unspecifiedError;
        type = (type) ? type : DIALOG_TYPE_INFORMATION;
        var oDiv = document.getElementById("dialogFramesMessageDiv");
        if (oDiv != null) {
            oDiv.innerHTML = message;
            showDialog(DIALOG_SRC + "?type=" + type);
        }
    }

    function closeDialog() {
        _newDisableDialog = parent.document.getElementById( "emptyFrame" );
        _newDisableDialog.style.display = "none";
        _newDisableDialog = parent.document.getElementById( "dialogFrame" );
        _newDisableDialog.style.display = "none";
        location = globals_contextPath + "/empty.vm";
    }

    function closeMyDialog() {
        _newDisableDialog = document.getElementById( "emptyFrame" );
        _newDisableDialog.style.display = "none";
        _newDisableDialog = document.getElementById( "dialogFrame" );
        _newDisableDialog.style.display = "none";
        _newDisableDialog.location = globals_contextPath + "/empty.vm";
    }

    function resizeDialog() {
        mainTableRef = document.getElementById("mainTable");
        emptyFrameRef = document.getElementById( "emptyFrame" );
        emptyFrameRef.style.width = mainTableRef.clientWidth;
        emptyFrameRef.style.height = mainTableRef.clientHeight;
    }

    function setDialogSize(doResize) {
      if (parent != window) {
        newPolicyDialog = parent.document.getElementById( "dialogFrame" );
        emptyCoverFrame = parent.document.getElementById( "emptyFrame" );
        newPolicyDialog.style.visibility = "hidden";
        emptyCoverFrame.style.display = "block";
        newPolicyDialog.style.display = "block";
        if ( doResize == undefined ) {
            resetDialogSize();
        }
        else if ( doResize ) {
            resetDialogSize();
        }
        newPolicyDialog.style.visibility = "";
        headerRef = document.getElementById("dialogHeader");
        
        if ( headerRef != null ) {
            // add drag listener
            headerRef.onmousedown = startDrag;
        }
      }
    }
            
    function resetDialogSize() {
        newPolicyDialog = parent.document.getElementById( "dialogFrame" );
        dialogTableRef = document.getElementById( "dialogTable" );
        if ( dialogTableRef == null ) {
            closeDialog();
        }
        else {
            emptyFrameRef = document.getElementById( "emptyFrame" );
            newWidth = dialogTableRef.clientWidth + 5;
            newHeight = dialogTableRef.clientHeight + 5;

            if ( convertPositionToNumber(newPolicyDialog.style.width) != newWidth)
                newPolicyDialog.style.width = newWidth;
            if ( convertPositionToNumber(newPolicyDialog.style.height) != newHeight)
                newPolicyDialog.style.height = newHeight;
            //
            windowWidth = parent.document.body.clientWidth;
            windowHeight = parent.document.body.clientHeight;
            
            verticalScrollAdjustment = parent.document.body.scrollTop;
            hortzScrollAdjustment = parent.document.body.scrollLeft;
            
            newXPosition = ((windowWidth - newWidth) / 2) + hortzScrollAdjustment;
            newYPosition = ((windowHeight - newHeight) / 2) + verticalScrollAdjustment;
            
            if (( newXPosition >= 0 ) && ( newYPosition >= 0 )) {
                if(  convertPositionToNumber(newPolicyDialog.style.left ) != newXPosition )
                    newPolicyDialog.style.left = newXPosition;
                if(  convertPositionToNumber(newPolicyDialog.style.top ) != newYPosition )
                    newPolicyDialog.style.top = newYPosition;
            }
    
        }
    }
            
    function getDocumentForFrame( frameID ) {
        frameRef = parent.document.getElementById( frameID );
        theDocument = frameRef.contentWindow.document;
        if ( theDocument == null )
            theDocument = frameRef.contentDocument;
        return theDocument;
    }
                
    function startDrag(evt) {
        //alert("starting drag");
        if (evt == null )
            evt = window.event;
        startingX = evt.screenX;
        startingY =  evt.screenY;
        addMouseEventHandlers();
        draggingDialog = parent.document.getElementById( "dialogFrame" );
    }

    function stopDrag() {
        removeMouseEventHandlers();
    }


    function addMouseEventHandlers() {
        document.body.onmouseup = stopDrag;
        document.body.onmousemove = doDrag;
    }

    function removeMouseEventHandlers() {
        document.body.onmouseup = null;
        document.body.onmousemove = null;
    }


    function doDrag(evt) {
            //windowWidth = parent.document.body.clientWidth;
            //windowHeight = parent.document.body.clientHeight;
            
            //dialogWidth = document.body.clientWidth;
            //dialogHeight = document.body.clientHeight;
            
            
            currentLeftEdge = convertPositionToNumber( draggingDialog.style.left );
            currentTopEdge = convertPositionToNumber( draggingDialog.style.top );

            //currentRightEdge = newPolicyDialog.clientWidth + currentLeftEdge;
            //currentBottomEdge = newPolicyDialog.clientHeight + currentTopEdge;
            if ( !evt )
                evt = window.event;
            deltaX = evt.screenX - startingX;
            deltaY = evt.screenY - startingY;

            //alert( "old: " + currentBottomEdge + ", new = " + (currentBottomEdge + deltaY) );
            // make sure we dont exceed the window size
            if ((currentTopEdge + deltaY) < 0 )
                newTop = 0;
            else
                newTop = (currentTopEdge + deltaY);
            draggingDialog.style.left = (currentLeftEdge + deltaX);
            draggingDialog.style.top = newTop;
            startingX = evt.screenX;
            startingY = evt.screenY;
            //alert( newPolicyDialog.style.top );
    }

    function convertPositionToNumber( posAsString ) {
        if ( posAsString.indexOf("px") > 0 )
            return parseInt( posAsString.substring( 0, posAsString.length - 2 ));
        return posAsString;
    }
