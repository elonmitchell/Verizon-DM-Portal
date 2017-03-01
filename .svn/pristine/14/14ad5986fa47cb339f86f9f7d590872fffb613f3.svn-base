package motive.reports.reportconsole.tools;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import motive.trace.TraceLogger;

public class LoginRedirectServlet extends HttpServlet {

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
    String password = request.getParameter("password");
    
    logger.debug("Received username :" + dmUser );
    
    if(dmUser != null && password != null && !dmUser.isEmpty() && ! password.isEmpty() ){
    	isLoginRequired = Boolean.TRUE;
    	request.setAttribute("userName", dmUser);
    	request.setAttribute("password", password);
    	request.setAttribute("IsLoginRequired", isLoginRequired);
		action = "loginredirect.do";
    	
    } else {
    	action = "login.vm";        	
    }        
	logger.debug("isLogin Required " + isLoginRequired);
    
	RequestDispatcher rd = request.getRequestDispatcher(action);
	rd.forward(request, response);}	
}
