package motive.reports.reportconsole.tools;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import motive.reports.reportconsole.util.LoginCipherHelper;

import motive.trace.TraceLogger;

import org.apache.log4j.Logger;

public class ReportRedirectServlet extends HttpServlet {
	


	private static final Logger logger = TraceLogger.getLogger(LoginRedirectServlet.class);
	
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}
	
	/**
	 * http post
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(request, response);
	}
	
	/**
	 * http get
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		handleRequest(request, response);	
	}
	
	private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Boolean isLoginRequired = true;
		
		String action = "login.vm";
        
        String dmUser = request.getParameter("userName");
        String jsessioId = request.getParameter("sessionID");
       
        logger.debug("Received username :" + dmUser + " jsessionId : " + jsessioId);
        
        if(dmUser == null || jsessioId == null){
        	isLoginRequired = Boolean.TRUE;
        } else {
        	
        	 LoginCipherHelper lc = new LoginCipherHelper(dmUser);
             if (lc.isRequestValid()) {
            	isLoginRequired = Boolean.FALSE;
             	dmUser = lc.getUserName();
             	
             } else {
            	 isLoginRequired = Boolean.TRUE;
             }
             
            request.setAttribute("userName", dmUser);
         	request.setAttribute("IsLoginRequired", isLoginRequired);
        	        	
        }        
		logger.debug("isLogin Required " + isLoginRequired);
        
		if(isLoginRequired){
			action = "login.vm";
		}else {
			action = "loginredirect.do";			
		}
		
		RequestDispatcher rd = request.getRequestDispatcher(action);
		rd.forward(request, response);
	}

}
