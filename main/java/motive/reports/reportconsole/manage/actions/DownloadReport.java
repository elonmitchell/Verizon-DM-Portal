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
import motive.reports.service.reportmanager.ReportManagerService;
import motive.reports.reportconsole.ServiceUtil;
import motive.trace.TraceLogger;

import java.io.OutputStream;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DownloadReport  extends Action {
    /** trace logger */
    private static Logger logger = TraceLogger.getLogger(DownloadReport.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception
    {
        try
        {
    	   	ReportManagerService rms = ServiceUtil.getInstance().getReportManagerService();
            String reportId = (String) request.getParameter("reportID");

            Report theReport = rms.getReportById(new Long( reportId));
 
            String filename = URLEncoder.encode( theReport.getName() + ".zip", "UTF-8");
            // Build a localized file name

            String contentDisp = "attachment; filename=\"" + filename + "\"";

            response.setContentLength( theReport.getReportResources().length );
            response.setContentType("application/x-download; charset=utf-8");
            response.setHeader("Content-Disposition", contentDisp);
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "must-revalidate");

            OutputStream out = response.getOutputStream();
            
            out.write(theReport.getReportResources());
        }
        catch (Exception e)
        {
            logger.error("An error occurred while downloading the report xml. Please see the server log for more details.");
            e.printStackTrace();
        }
        return null;
    }

}
