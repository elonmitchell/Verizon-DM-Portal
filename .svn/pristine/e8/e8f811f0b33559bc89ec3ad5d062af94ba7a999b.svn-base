/*
 * Copyright 2005 by Motive, Inc. All rights reserved.
 * 
 * This software is the confidential and proprietary information of Motive, Inc.
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Motive.
 */
package motive.reports.reportconsole.manage.actions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import motive.commons.exceptions.
import motive.reports.reportconsole.ServiceUtil;
import motive.reports.reportconsole.UserContext;
import motive.reports.reportconsole.actions.ActionUtils;
import motive.reports.reportconsole.actions.BaseAction;
import motive.trace.TraceLogger;

import motive.exceptions.ProviderException;

import motive.reports.service.reportmanager.ReportGroup;
import motive.reports.service.reportmanager.ReportManagerService;

import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;


/**
 * AjaxAction.java
 * 
 * CVS: $Id: GetReportGroups.java,v 1.5 2013/12/26 23:43:50 balraj Exp $
 * 
 * @author dpileggi
 * @version $Revision: 1.5 $
 * 
 */
public class GetReportGroups extends BaseAction
{
    private static Logger logger = TraceLogger.getLogger(GetReportGroups.class);

    public ActionForward executeAction(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception
{
    	ReportGroup[] rg = new ReportGroup[0];
    	Boolean loadFailed = new Boolean(false);
    	try
    	{
    		rg = ActionUtils.getReportGroups();
    	}
    	catch (Exception e)
    	{
    		loadFailed = new Boolean(true);
    	}
        request.setAttribute(ActionUtils.GROUP_LIST, rg);
        request.setAttribute(ActionUtils.GROUP_LOAD_FAILED, loadFailed);
	    return mapping.findForward("success");
	}
	
}
