   		var mouseInMenuBarItem = false;
    		var mouseInMenu = false;
    		var activeMenuBarItem = "";
    		var activeMenu = "";
    		var activeMenuItem = "";
    		
    		function clickLink( linkID ) {
    			theLink = document.getElementById( linkID );
    			if ( theLink != null ) {
    				theLink.click();
    			}
    		}
    		
    		function setActiveMenuItem( theMenuItem ) {
    			if ( activeMenuItem != "" ) {
    				activeMenuItemDiv = document.getElementById( activeMenuItem );
    				activeMenuItemDiv.className = "menuItem";
    			}
    			if ( theMenuItem != "" ) {
	    			activeMenuItemDiv = document.getElementById( theMenuItem );
	    			activeMenuItemDiv.className = "menuItemSelected";
	    			activeMenuItem = theMenuItem;
	    		}
    		}
    		
    		function setMouseInMenu( isInMenu ) {
    			mouseInMenu = isInMenu;
   				setTimeout( "closeMenu();", 100 );
    		}
    	
			function showMenu(menuBarItem, theMenu) {
				if ( menuBarItem != activeMenuBarItem ) {
		    		mouseInMenuBarItem = false;
		    		mouseInMenu = false;
		    		closeMenu();
				}
				activeMenuBarItem = menuBarItem;
				activeMenu = theMenu;
	            userMenuItemDiv = document.getElementById(menuBarItem);
	            menuDivXOffset = Position.cumulativeOffset(userMenuItemDiv);
	            menuDivDim =  Element.Methods.getDimensions( userMenuItemDiv );
	            menuDiv = document.getElementById(theMenu);
	            menuItemDivDim =  Element.Methods.getDimensions( menuDiv );
	            menuLeft = ( menuDivXOffset[0] + menuDivDim.width ) - menuItemDivDim.width;
	            menuTop = menuDivXOffset[1] + menuDivDim.height;
	            menuDiv.style.left = menuLeft; 
	            menuDiv.style.top = menuTop;
	            //menuDiv.style.visibility = "";
	            menuDiv.className = "menuContainerVisible";
	            menuItemDivDim =  Element.Methods.getDimensions( menuDiv );
	            menuLeft = ( menuDivXOffset[0] + menuDivDim.width ) - menuItemDivDim.width;
	            menuDiv.style.left = menuLeft;
	            userMenuItemDiv.className = "user_name menuBarItemSelected";
	            mouseInMenuBarItem = true;
	        }
	        
			function mouseExitMenu() {
				mouseInMenuBarItem = false;
   				setTimeout( "closeMenu();", 100 );
			}
			
			function forceCloseMenu() {
	    		mouseInMenuBarItem = false;
				mouseInMenu = false;
				closeMenu();
			}
			
			function closeMenu() {
    			if ( !mouseInMenu && !mouseInMenuBarItem ) {
		            userMenuItemDiv = document.getElementById(activeMenuBarItem);
		            if ( userMenuItemDiv != null ) {
		            	userMenuItemDiv.className = "user_name menuBarItem";
		            }
		            menuDiv = document.getElementById(activeMenu);
		            if ( menuDiv != null ) {
		            	menuDiv.className = "menuContainerHidden";
		            }
	            }
			}