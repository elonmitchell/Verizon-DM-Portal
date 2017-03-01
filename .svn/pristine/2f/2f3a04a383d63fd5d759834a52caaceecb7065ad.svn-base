/**
 * Copyright 2003 by Motive Communications, Inc.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Motive Communications, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use it
 * only in accordance with the terms of the license agreement you
 * entered into with Motive.
 */
 
package motive.reports.reportconsole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import motive.service.configuration.ConfigurationHelper;
import motive.reports.reportconsole.license.LicenseHelper;
import motive.trace.TraceLogger;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class Logout extends Action {
	
	private static final Logger logger = TraceLogger.getLogger(Logout.class);
	
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
		throws Exception
	{
		
		//To reset the license string to null
		LicenseHelper.resetSingletonInstance();
		
		HttpSession session = request.getSession(false);
		if ( session != null )
			session.invalidate();
		
		return mapping.findForward("logout");
	}	
}
