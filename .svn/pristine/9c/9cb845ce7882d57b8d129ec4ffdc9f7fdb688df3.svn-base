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
 * CVS: $Id: ManageGroupAjax.java,v 1.5 2013/12/26 23:43:50 balraj Exp $
 * 
 * @author dpileggi
 * @version $Revision: 1.5 $
 * 
 */
public class ManageGroupAjax extends MotiveDispatchAction
{
    /** logger */
    private static final Logger logger = TraceLogger.getLogger(ManageGroupAjax.class);

	public ActionForward createReportGroup( ActionMapping mapping,
										    ActionForm form,
										    HttpServletRequest request,
										    HttpServletResponse response) throws Exception
    {
	   	ReportManagerService rms = ServiceUtil.getInstance().getReportManagerService();
		String responseText = String.valueOf("");
	   	String groupName = request.getParameter(ActionUtils.GROUP_NAME);
	   	if ( groupName != null )
	   	{
		   	ReportGroup newGroup = new ReportGroup();
		   	newGroup.setName(groupName);
		   	try
		   	{
		   		rms.create( newGroup );
		   		responseText = getMessage("manageGroups.dialog.groupCreated", request);
		   	}
		   	catch (Exception e)
		   	{
		   		logger.error("Unable to create group: " + e.getMessage());
		   		responseText = getMessage("manageGroups.dialog.error.groupCreateError", request);
		   	}
		   	
		}
	   	else
	   	{
	   		logger.error("Unable to create group, group name is null.");
	   		responseText = getMessage("manageGroups.dialog.error.groupCreateError", request);
	   	}
		writeResponse(response, responseText);
		return null;
    }
	
	public ActionForward deleteReportGroups( ActionMapping mapping,
										     ActionForm form,
										     HttpServletRequest request,
										     HttpServletResponse response) throws Exception
	{
	   	ReportManagerService rms = ServiceUtil.getInstance().getReportManagerService();

        String ids = request.getParameter("ids");
        String responseText = "";
		int deleteCount = 0;
		int deleteFailedCount = 0;
		Long groupID = null;
        if (!GenericValidator.isBlankOrNull(ids))
        {
            String[] idsArray = ids.split(",");
            for (int i = 0; i < idsArray.length; i++)
            {
            	if ( idsArray[i] == null )
            		idsArray[i] = "";
				if ( idsArray[i].equals("on"))
				{
					// ignore invalid id.
				}
				else
				{
	            	// do work here
					groupID = new Long(idsArray[i]);
					if ( groupID != null )
					{
						ReportGroup theGroup = rms.getReportGroupById(groupID);
						if ( theGroup.getReports().size() == 0)
						{
							try
							{
								rms.deleteReportGroup(groupID);
								deleteCount++;
							}
							catch (Exception e )
							{
								deleteFailedCount++;
								logger.error("Error attempting to delete group " + theGroup.getName() + ": " + e.getMessage());
							}
						}
					}
					logger.warn("Attempt to delete an invalid group id:" + idsArray[i]);
				}
            }
        }
        Object[] args = null;
        if ( deleteFailedCount == 0)
        {
        	args = new Object[1];
        	args[0] = String.valueOf(deleteCount);
        	String messageText = "";
        	if (deleteCount == 1)
        		messageText = getMessage( "manageGroups.dialog.delete.pass.single", request );
        	else
        		messageText = getMessage( "manageGroups.dialog.delete.pass.multiple", request );
        	responseText = ActionUtils.formatMessage(messageText, args);
        }
        else
        {
        	args = new Object[2];
        	String messageText = "";
        	args[0] = String.valueOf(deleteCount);
        	args[1] = String.valueOf(deleteFailedCount);
        	if (( deleteCount == 1 ) && (deleteFailedCount == 1 ))
        		messageText = getMessage( "manageGroups.dialog.delete.partial.single.single", request);
        	else if (( deleteCount == 1 ) && (deleteFailedCount != 1 ))
        		messageText = getMessage( "manageGroups.dialog.delete.partial.single.multiple", request);
        	else if (( deleteCount != 1 ) && (deleteFailedCount == 1 ))
        		messageText = getMessage( "manageGroups.dialog.delete.partial.multiple.single", request);
        	else
        		messageText = getMessage( "manageGroups.dialog.delete.partial.multiple.multiple", request);
        	responseText = ActionUtils.formatMessage(messageText, args);
        }
        writeResponse(response, responseText);
		return null;
	}

	private String getMessage(String key, HttpServletRequest request)
	{
        Locale locale = ActionUtils.getLocale(request);
        MessageResources messages = ActionUtils.getMessageResources(request);
        return messages.getMessage(locale, key);
	}
	
}