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
import motive.reports.service.reportmanager.Report;
import motive.reports.service.reportmanager.ReportGroup;
import motive.reports.service.reportmanager.ReportManagerService;
import motive.reports.reportconsole.ServiceUtil;
import motive.reports.reportconsole.UserContext;
import motive.reports.reportconsole.actions.ActionUtils;
import motive.reports.reportconsole.actions.BaseAction;
import motive.trace.TraceLogger;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;

/**
 * Populates the fields on the edit/new device panel.
 */
public class AssignRolesToReport extends BaseAction
{
    /** logger */
    private static final Logger logger = TraceLogger.getLogger(AssignRolesToReport.class);

    /**
     * Populates the fields on the edit/new device panel.
     */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.info("[executeAction]::Saving new configuration.");
		String forwardName = ActionUtils.FORWARD_SUCCESS;
		DynaActionForm theForm = (DynaActionForm) form;
		ReportManagerService rms = ServiceUtil.getInstance()
				.getReportManagerService();
		String reportId = (String) request.getParameter("reportId");
		String roles = (String) request.getParameter("roles");
		String numOfHistory = (String) request.getParameter("numofhist");
		String isEnabled = request.getParameter("ishistenabled");
		// Getting the num of schedules from the UI
		String numOfSchedules = (String) request.getParameter("numofsche");
		// Getting the value of Schedule Enabled from the UI
		String isScheduleEnabled = request.getParameter("isscheenabled");
		// Getting the value of report format
		String reportFormat = request.getParameter("reportFormat");
		String[] supportedFormatsArray = request
				.getParameterValues("suppRepFor");
		String supportedFormats = null;
		if (supportedFormatsArray != null && supportedFormatsArray.length > 0) {
			supportedFormats = "";
			for (int index = 0; index < supportedFormatsArray.length; index++) {
				supportedFormats = supportedFormats
						+ supportedFormatsArray[index] + ",";
			}
			supportedFormats = supportedFormats.substring(0,
					supportedFormats.length() - 1);
			logger.info("[executeAction]::SupportedFormats=" + supportedFormats);
		}

		// Saving the report using the report manager ejb
		Report report = rms.getReportById(Long.valueOf(reportId));
		report.setRoles(roles);
		report.setNum_Of_History(Integer.valueOf(numOfHistory));
		report.setHistory_Enabled(Boolean.valueOf(isEnabled));
		report.setNum_Of_Schedules(Integer.valueOf(numOfSchedules));
		report.setSchedule_Enabled(Boolean.valueOf(isScheduleEnabled));
		report.setReportFormat(reportFormat);
		report.setSupportedFormats(supportedFormats);
		rms.update(report);
		return mapping.findForward(forwardName);
	}
    
}
