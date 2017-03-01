/*
 * Copyright 2005 by Motive, Inc. All rights reserved.
 * 
 * This software is the confidential and proprietary information of Motive, Inc.
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Motive.
 */
package motive.reports.reportconsole.manage.actions;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.ArrayList;

import motive.reports.service.reportmanager.ReportGroup;
import motive.reports.service.reportmanager.ReportManagerService;
import motive.reports.reportconsole.ServiceUtil;
import motive.reports.reportconsole.UserContext;
import motive.reports.reportconsole.actions.ActionUtils;
import motive.reports.reportconsole.actions.MotiveDispatchAction;
import motive.trace.TraceLogger;

import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.util.MessageResources;

/**
 * ManageGroupAjax.java
 * 
 * CVS: $Id: ReportManagerAjax.java,v 1.5 2013/12/26 23:43:50 balraj Exp $
 * 
 * @author dpileggi
 * @version $Revision: 1.5 $
 * 
 */
public class ReportManagerAjax extends MotiveDispatchAction
{
    /** logger */
    private static final Logger logger = TraceLogger.getLogger(ReportManagerAjax.class);

	public ActionForward getReportsInGroup( ActionMapping mapping,
										    ActionForm form,
										    HttpServletRequest request,
										    HttpServletResponse response) throws Exception
	{
	   	ReportManagerService rms = ServiceUtil.getInstance().getReportManagerService();
	   	Set reportList = new HashSet(0);
		String groupIdStr = request.getParameter(ActionUtils.GROUP_ID);
		String persistedSelectedIndex = request.getParameter(ActionUtils.PERSISTED_SELECTED_INDEX);
		Boolean reportListFailure = new Boolean(false);
		Long groupID = new Long(groupIdStr);
		if (( groupID != null ) && (groupID.longValue() != -1))
		{
			try 
			{
				ReportGroup theGroup = rms.getReportGroupById(groupID);
				reportList = theGroup.getReports();
			
				UserContext.setCurrentGroupId(request, groupID);
			}
			catch (Exception e)
			{
				reportListFailure = new Boolean(true);
				logger.error("Error while attempting to get list of reports for group: " + groupIdStr, e);
			}
		}
		
		request.getSession().setAttribute(ActionUtils.PERSISTED_SELECTED_INDEX, persistedSelectedIndex);
		request.setAttribute(ActionUtils.GROUP_ID, groupID);
		request.setAttribute(ActionUtils.REPORT_LIST, reportList);
		request.setAttribute(ActionUtils.REPORT_LIST_LOAD_FAILURE, reportListFailure);
		return mapping.findForward("ajaxReportList");
	}
	
	public ActionForward getReportGroups( ActionMapping mapping,
									      ActionForm form,
									      HttpServletRequest request,
									      HttpServletResponse response) throws Exception
	{
        boolean getGroupsFailed = false;
	   	ReportManagerService rms = ServiceUtil.getInstance().getReportManagerService();
     	ReportGroup[] rg = new ReportGroup[0];
    	try
    	{
			rg = rms.getAllReportGroups();
    	}
    	catch ( Exception pe )
    	{
    		logger.error("Could not load report groups : " + pe.getMessage());
    		getGroupsFailed = true;
    	}
	   	Long currentGroupID = UserContext.getCurrentGroupId(request);
        if ( currentGroupID == null)
        {
        	if ( rg.length > 0)
        		currentGroupID = rg[0].getId();
        }	
        else
        {
        	try
        	{
        		ReportGroup theGroup = rms.getReportGroupById(currentGroupID);
        		if ( theGroup == null )
        		{
        			if ( rg.length > 0)
        				currentGroupID = rg[0].getId();
        		}
        	}
        	catch ( Exception e)
        	{
        		if ( rg.length > 0)
        			currentGroupID = rg[0].getId();
        		logger.warn("Default report group not found, reverting back to first report group.");
        	}
        	
        }
        String groupIDStr = "";
        if ( currentGroupID != null  )
        	groupIDStr = currentGroupID.toString();
        request.setAttribute(ActionUtils.CURRENT_GROUP_ID, groupIDStr);
        request.setAttribute("groupLoadFailed", new Boolean(getGroupsFailed));
        request.setAttribute(ActionUtils.GROUP_LIST, rg);
		return mapping.findForward("ajaxReportGroupSelect");
	}
    
	private String getMessage(String key, HttpServletRequest request)
	{
        Locale locale = ActionUtils.getLocale(request);
        MessageResources messages = ActionUtils.getMessageResources(request);
        return messages.getMessage(locale, key);
	}
	
}