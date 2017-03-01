package motive.reports.reportconsole.actions;


import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import motive.reports.security.BasicPrincipal;
import motive.reports.security.ReportCallbackHandler;
import motive.reports.security.UserGroup;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ReportLoginAction extends BaseAction {
	
	private static Logger log = Logger.getLogger(ReportLoginAction.class);

	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String forwardName = ActionUtils.FORWARD_SUCCESS;
		Boolean isLoginSuccess = false;
		
		log.debug("InitializeAction:: ReportLoginAction : executeAction - entering");
		String userName = null;
		String password = null;
		Boolean isLoginRequired = true;
		LoginContext loginContext = null;
		String url = null;
		HttpSession session = null;
		List<String> usrRolesLst = new ArrayList<String>();
		
		/*userName = request.getParameter("userName");
        jsessionId = request.getParameter("sessionID");
        password = request.getParameter("password");
        jsessionId = "12345";*/
        
       /* if(userName == null && jsessionId == null){
        	isLoginRequired = Boolean.TRUE;
        } else {
        	isLoginRequired = Boolean.FALSE;
        	        	
        }*/
        
		log.debug("isLogin Required " + isLoginRequired);
		
		userName = (String) request.getAttribute("userName");
		password = (String) request.getAttribute("password");
		isLoginRequired = (Boolean) request.getAttribute("IsLoginRequired");
		
		log.debug("Parameters Received :" );
		log.debug("UserName :" + userName);
		log.debug("Password " + password); // Need to remove after testing.
		
		try {
			if(!isLoginRequired && userName != null ) {
				url = "reportlogin";
				
			} else {
				url = "login.vm";				
				
			}
			
			loginContext = new LoginContext("dm-reporting", new ReportCallbackHandler(userName,password, url));
			loginContext.login();
			session = request.getSession(true);
						
			// let's see what Principals we have
			Iterator principalIterator = loginContext.getSubject().getPrincipals().iterator();
			log.debug("Authenticated user has the following Principals:");
			UserGroup usrGrp = null;
			while (principalIterator.hasNext()) {
			    usrGrp = (UserGroup) principalIterator.next();
			    if(usrGrp != null){
			    	Enumeration usrValue = usrGrp.members();
				    while(usrValue.hasMoreElements()){
				    	Principal bsicPc = (BasicPrincipal) usrValue.nextElement(); 
				    	String roles = bsicPc.getName();
				    	usrRolesLst.add(roles);
				    	log.debug("Roles :" + roles);
				    }
			    }			    
			}
				
			log.debug("UserPrincipal :" + request.getUserPrincipal());
			isLoginSuccess = true;
			session.setAttribute("UserRoles", usrRolesLst);
			session.setAttribute("userName", userName);
			request.setAttribute("UserRoles", usrRolesLst);
		} catch (LoginException e) {
		  e.printStackTrace();
		  super.errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("KEY_FOR_LOGIN_FAILURE"));
		  forwardName = ActionUtils.FORWARD_ERROR;		  
		}		
		request.setAttribute("IsLoginSuccess", new Boolean(isLoginSuccess));
        return mapping.findForward(forwardName);
	}

}
