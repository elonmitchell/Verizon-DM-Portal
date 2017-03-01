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
 * CVS: $Id: DeleteReportAjax.java,v 1.5 2013/12/26 23:43:50 balraj Exp $
 * 
 * @author dpileggi
 * @version $Revision: 1.5 $
 * 
 */
public class DeleteReportAjax extends MotiveDispatchAction
{
    /** logger */
    private static final Logger logger = TraceLogger.getLogger(DeleteReportAjax.class);
    
	public ActionForward deleteReports( ActionMapping mapping,
		     							ActionForm form,
									    HttpServletRequest request,
									    HttpServletResponse response) throws Exception
	{
		String ids = request.getParameter("ids");
	   	ReportManagerService rms = ServiceUtil.getInstance().getReportManagerService();

		String responseText = "";
		int deleteCount = 0;
		int deleteFailedCount = 0;
		Long currentID = null;
		if (!GenericValidator.isBlankOrNull(ids))
		{
			String[] idsArray = ids.split(",");
			for (int i = 0; i < idsArray.length; i++)
			{
				if ( idsArray[i].equals("on"))
				{
					// ignore invalid id.
				}
				else
				{
					try
					{
						currentID = new Long(idsArray[i]);
						if ( currentID != null )
						{
							rms.deleteReport(currentID);
							deleteCount++;
						}
					}
					catch (Exception e)
					{
						deleteFailedCount++;
						logger.warn("Report with id " + idsArray[i] + " was not deleted due to error: " + e.getMessage());
					}
				}
			}
		}
        MessageResources messages = ActionUtils.getMessageResources(request);
        Object[] args = null;
        if ( deleteFailedCount == 0)
        {
        	args = new Object[1];
        	args[0] = String.valueOf(deleteCount);
        	String messageText = "";
        	if (deleteCount == 1)
        		messageText = messages.getMessage( "manageGroups.dialog.delete.pass.single" );
        	else
        		messageText = messages.getMessage( "manageGroups.dialog.delete.pass.multiple" );
        	responseText = ActionUtils.formatMessage(messageText, args);
        }
        else
        {
        	args = new Object[2];
        	String messageText = "";
        	args[0] = String.valueOf(deleteCount);
        	args[1] = String.valueOf(deleteFailedCount);
        	if (( deleteCount == 1 ) && (deleteFailedCount == 1 ))
        		messageText = messages.getMessage( "manage.delete.partial.single.single");
        	else if (( deleteCount == 1 ) && (deleteFailedCount != 1 ))
        		messageText = messages.getMessage( "manage.delete.partial.single.multiple");
        	else if (( deleteCount != 1 ) && (deleteFailedCount == 1 ))
        		messageText = messages.getMessage( "manage.delete.partial.multiple.single");
        	else
        		messageText = messages.getMessage( "manage.delete.partial.multiple.multiple");
        	responseText = ActionUtils.formatMessage(messageText, args);
        }

		responseText = String.valueOf(deleteCount);
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