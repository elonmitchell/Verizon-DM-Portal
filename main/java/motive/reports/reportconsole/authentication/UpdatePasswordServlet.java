package motive.reports.reportconsole.authentication;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import motive.trace.TraceLogger;

import org.apache.log4j.Logger;

public class UpdatePasswordServlet extends HttpServlet {

	private static final Logger logger = TraceLogger.getLogger(UpdatePasswordServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPasswordChange(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPasswordChange(request, response);
	}
	
	private void doPasswordChange(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		      
		String username = "";     
		String currentpassword = ""; 
		String newpassword = "";
		String confirmpassword = "";
		      
		username = request.getUserPrincipal().getName();
	    // Get the arguments
	    currentpassword = request.getParameter("currentpassword");
	    newpassword = request.getParameter("newpassword");
	    confirmpassword = request.getParameter("confirmpassword");

	    //Remove a possible previous error state from the session
    	request.getSession().removeAttribute("ERROR_MESSAGE");
    	
      	try {
      		// Try to change the password
            AuthenticationUtils.changeUserPassword(username,currentpassword,newpassword,confirmpassword);
            logger.debug("Changed password for user " + username);
            
        }  catch (Exception e) {
        	logger.error("Could not change password for user " + username, e);
        	response.sendRedirect(makeErrorURL(response, request, e.getMessage()));            
            return;
        }

        // Change the session state for the user
    	request.getSession().setAttribute(AuthenticationUtils.LOGIN_STATE_NAME, new Long(0));
    	    	  
        response.sendRedirect(makeSuccessURL(response, request));
	}
	
	private String makeErrorURL(HttpServletResponse response, HttpServletRequest request, String beaerrormessage) 
	{
		HttpSession session = request.getSession();
				
		// read the individual messages
		
		if( beaerrormessage != null ) {
			
			HashMap<String, Object[]> errors = new HashMap<String, Object[]>();
			
			int startIndex = -1;
			int endIndex = -1;
			while( (startIndex = beaerrormessage.indexOf("[", endIndex+1)) >= 0 ) {
				endIndex = beaerrormessage.indexOf("]", startIndex);
				
				if( endIndex > 0 ) { // add the message
					
					String msgbody = beaerrormessage.substring(startIndex + 1, endIndex);
					errors.putAll(mapErrorPropertiesForURL(msgbody));
					
				} else {
					break;
				}
			}
			session.setAttribute("ERROR_MESSAGE", errors);
		}
		
		return "changePassword.vm";
	}
	
	/**
	 * This method was introduced to be able to localize the BEA Authenticator error messages.
	 * 
	 * @param message
	 * @param errors
	 * @return
	 */
	private HashMap<String, Object[]> mapErrorPropertiesForURL(String message) {
		
		HashMap<String, Object[]> errormessage = new HashMap<String, Object[]>();
		String code = null;
		Object[] arg = null;
		
		//Only the last 4 messages in this list has an argument
		if(message.equalsIgnoreCase("Please enter your current password.")) {
			code = "changepassword.form.noCurrentPassword";
			errormessage.put(code, arg);
		}
		else if(message.equalsIgnoreCase("The entered 'current' password doesn't match.")) {
			code = "changepassword.form.currentPasswordInvalid";
			errormessage.put(code, arg);
		}
		else if(message.equalsIgnoreCase("Please enter non-empty passwords.")) {
			code = "changepassword.form.emptyPasssword";
			errormessage.put(code, arg);
		}
		else if(message.equalsIgnoreCase("Please confirm your new password with the same value.")) {
			code = "changepassword.form.invalidPasswordConfirmation";
			errormessage.put(code, arg);
		}
		else if(message.equalsIgnoreCase("You can not reuse your current password.")) {
			code = "changepassword.form.passwordEqualsCurrent";
			errormessage.put(code, arg);
		}
		else if(message.equalsIgnoreCase("A password can not contain a comma.")) {
			code = "changepassword.form.passwordHasComma";
			errormessage.put(code, arg);
		}
		else if(message.equalsIgnoreCase("A password should not start or end with whitespace.")) {
			code = "changepassword.form.passwordSurroundedWithWhitespace";
			errormessage.put(code, arg);
		}
		else if(message.equalsIgnoreCase("You can not reuse a password you recently used.")) {
			code = "changepassword.form.passwordReuseOldPassword";
			errormessage.put(code, arg);
		}
		else if(message.contains("Could not generate the hash of the password:")) {
			code = "changepassword.form.hashingProblem";
			
			//Here the arg follows the : in the "message"
			arg = new Object[] {message.substring(message.indexOf(':') + 1)};
			
			errormessage.put(code, arg);
		} 
		else if(message.contains("Your user account ")) {
			code = "changepassword.form.userNotFound";
			
			//Your user account ''{0}'' could not be found.
			arg = new Object[] {message.substring(16, message.indexOf("could not"))};
			
			errormessage.put(code, arg);
		}
		else if(message.contains("A password should contain a minimum of ")) {
			code = "changepassword.form.passwordMinimumLength";
			
			//A password should contain a minimum of {0} characters.
			arg = new Object[] {message.substring(38, message.indexOf("characters"))};
				
			errormessage.put(code, arg);
		}
		else if(message.contains("A general error occurred: ")) {
			code = "changepassword.form.lookupError";
			
			//Here the arg follows the : in the "message"
			arg = new Object[] {message.substring(message.indexOf(':') + 1)};
			
			errormessage.put(code, arg);
		}
		else {
			code = message;
			errormessage.put(code, arg);
		}
					
		return errormessage;
	}
	
	private String makeSuccessURL(HttpServletResponse response, HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		String requestURI = (String)session.getAttribute(AuthenticationUtils.ORIGINAL_REQUEST_URI);
		// If the original request page was the changePassword or logout page, we don't want to redirect here again, just redirect to the home page
		if( requestURI == null || requestURI.indexOf("/changePassword") > 0 || requestURI.indexOf("/logout") > 0) requestURI = request.getContextPath();
		
		session.removeAttribute(AuthenticationUtils.ORIGINAL_REQUEST_URI);
		return requestURI;
	}
}
