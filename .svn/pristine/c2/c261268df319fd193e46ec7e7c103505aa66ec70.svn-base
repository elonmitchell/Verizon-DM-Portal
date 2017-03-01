/*
 * Copyright 2005 by Motive, Inc. All rights reserved. This software is the
 * confidential and proprietary information of Motive, Inc. ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Motive.
 */
package motive.reports.reportconsole.manage.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import motive.exceptions.ObjectNotFoundException;
import motive.reports.service.reportmanager.ReportGroup;
import motive.reports.service.reportmanager.ReportManagerService;
import motive.reports.reportconsole.ServiceUtil;
import motive.reports.reportconsole.UserContext;
import motive.reports.reportconsole.actions.ActionUtils;
import motive.reports.reportconsole.actions.BaseAction;
import motive.trace.TraceLogger;

import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.DynaActionForm;

/**
 * Populates the fields on the edit/new device panel.
 */
public class ManageReports extends BaseAction
{
    /** logger */
    private static final Logger logger = TraceLogger.getLogger(ManageReports.class);


    /**
     * Populates the fields on the edit/new device panel.
     */
    public ActionForward executeAction(ActionMapping mapping,
                                       ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws Exception
    {
        String forwardName = ActionUtils.FORWARD_SUCCESS;
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
        if ( request.getAttribute(ActionUtils.REPORT_IMPORT_FAILED) == null )
        	request.setAttribute(ActionUtils.REPORT_IMPORT_FAILED, "");
        request.setAttribute(ActionUtils.CURRENT_GROUP_ID, groupIDStr);
        request.setAttribute("groupLoadFailed", new Boolean(getGroupsFailed));
        request.setAttribute(ActionUtils.GROUP_LIST, rg);
        return mapping.findForward(forwardName);
    }
    
}
