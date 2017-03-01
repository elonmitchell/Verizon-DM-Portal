package motive.reports.reportconsole.tools;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import motive.reports.reportconsole.actions.ActionUtils;
import motive.trace.TraceLogger;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.tools.view.servlet.VelocityViewServlet;

public class MotiveVelocityViewServlet extends VelocityViewServlet 
{	 
	  // logger
      private static final Logger logger = TraceLogger.getLogger(MotiveVelocityViewServlet.class);
	
      /* Invoked when there is an error thrown in any part of doRequest() processing. 
       * <br><br> 
       * Default will send a simple HTML response indicating there was a problem. 
       *  
       * @param request original HttpServletRequest from servlet container. 
       * @param response HttpServletResponse object from servlet container. 
       * @param e  Exception that was thrown by some other part of process. 
      */ 
      protected void error(HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException 
      { 
    	  if (e instanceof ResourceNotFoundException)
    	  {    
    		  try 
    		  {
    			  ServletOutputStream out = response.getOutputStream(); 				    
 				  try 
 				  {	    		   				
	    			    /*  first, get and initialize an engine  */	
	    		        VelocityEngine ve = new VelocityEngine();
	    		        ve.init();
	
	    		        //String resourceNotFoundTemplate = ActionUtils.getConfigurationProperty("resourceNotFoundTemplate", "/resourceNotFound.vm");
	    		        String resourceNotFoundTemplate =  "/resourceNotFound.vm";
	    		        
	    		        /*  next, get the Template  */    		      
	    		        Template t = getTemplate( resourceNotFoundTemplate );
	    		        
	    		        Context context = createContext(request, response);
	    		        
	    		        StringWriter writer = new StringWriter();
	
	    		        t.merge( context, writer );
	
	    		        out.write(writer.toString().getBytes());
		      	    	 		        
				    }	        	  
					catch  (Exception exp) 
					{ 	     
						logger.error("Error executing " + this.getClass().toString(), exp);
						exp.printStackTrace();              
					} 
					finally
				    {
				        try 
				        {
							out.close();
						} 
				        catch (IOException e1)
				        {
				        	logger.error("Error executing " + this.getClass().toString(), e1);
							e1.printStackTrace();
						}
				    }
			} 
    		catch (Exception e1) 
    		{				
    			logger.error("Error executing " + this.getClass().toString(), e1);
				e1.printStackTrace();
			}
          }      
    	  else
    		  	super.error(request, response, e);
      }  
}
