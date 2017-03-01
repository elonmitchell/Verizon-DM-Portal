package motive.reports.reportconsole;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import motive.trace.TraceLogger;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ExceptionConfig;

public class ExceptionHandler extends org.apache.struts.action.ExceptionHandler 
{
    private static final Logger logger = TraceLogger.getLogger(ExceptionHandler.class); 
  
	public ActionForward execute(Exception ex,ExceptionConfig ae,
                                 ActionMapping mapping,
	                             ActionForm formInstance,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
		throws ServletException
	{
        request.setAttribute("exception", ex);
               
        if (ex instanceof DisplayableException)
            request.setAttribute("message", ex.getMessage());
        
        request.setAttribute("stack", ex.getStackTrace());
        ex.printStackTrace();
        
        // log this exception as well
        logger.warn("caught an exception that wasn't handled by the action", ex);		      
        
        return new ActionForward(ae.getPath());
	}

}
