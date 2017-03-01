/*
 * Copyright 2005 by Motive, Inc. All rights reserved.
 * 
 * This software is the confidential and proprietary information of Motive, Inc.
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Motive.
 */
package motive.reports.reportconsole.tools;

import javax.servlet.http.HttpSession;

import motive.trace.TraceLogger;

import org.apache.log4j.Logger;
import org.apache.velocity.tools.view.context.ViewContext;

import motive.reports.service.security.ReportSecurityUtil;

/**
 * ButtonTool.java
 * 
 * CVS: $Id: SecurityTool.java,v 1.5 2013/12/26 23:43:50 balraj Exp $
 * 
 * @author kmckenzi
 * @version $Revision: 1.5 $
 * 
 */
public class SecurityTool extends MotiveMessageTool
{
    private static Logger logger = TraceLogger.getLogger(SecurityTool.class);
    
    /**
     * Initializes this tool.
     *
     * @param obj the current ViewContext
     * @throws IllegalArgumentException if the param is not a ViewContext
     */
    public void init(Object obj)
    {
        if (!(obj instanceof ViewContext))
        {
            throw new IllegalArgumentException("Tool can only be initialized with a ViewContext");
        }

        ViewContext context = (ViewContext)obj;
        this.request = context.getRequest();
        this.application = context.getServletContext();
    }

    /**
     * Tool must be initialized before use.
     */
    public SecurityTool()
    {
        super();
    }

    public boolean hasRole( String theRole )   {
    	//this.request.getUserPrincip
    	return this.request.isUserInRole( theRole );
    	}
    
    
    /**
     * 
     * @param hasPermission
     * @return
     */
    public String redirectOnInvalidPermission( boolean hasPermission )
    {
        StringBuffer s = new StringBuffer();
    	if ( !hasPermission)
    	{
	    	s.append( "<script type=\"text/javascript\" language=\"Javascript\">" );
	        s.append( "redirectURL = \'" );
	        s.append( request.getContextPath() );
	        s.append( "/insufficientPrivileges.vm\';");
	        s.append( "</script>");
    	}
        return s.toString();
    }
    
    public boolean isCompliantDataAccessAuthenticatorAvailable()
    {
    	try 
    	{
    		ReportSecurityUtil securityUtil = ReportSecurityUtil.getInstance();
	        return securityUtil.isCompliantDataAccessAuthenticatorAvailable();
    	}
    	catch ( Exception e)
    	{
    		return false;
    	}
    }
    
    /**
     * invalidateSession
     * Currently used to invalidate the session from the loginError page.
     * Fixes invalid sessions from the AuthenticationServlet.
     */
    public void invalidateSession() {
        logger.debug("in invalidateSession");
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
            logger.debug("session invalidated!");
        }
    }
}
