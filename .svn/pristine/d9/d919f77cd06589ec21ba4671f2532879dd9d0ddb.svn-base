/*
 * Copyright 2005 by Motive, Inc. All rights reserved. This software is the
 * confidential and proprietary information of Motive, Inc. ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Motive.
 */
package motive.reports.reportconsole.manage.actions;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import motive.exceptions.ObjectNotFoundException;
import motive.exceptions.ObjectExistsException;
import motive.reports.service.reportmanager.DuplicateReportException;
import motive.reports.service.reportmanager.InvalidReportException;
import motive.reports.service.reportmanager.Report;
import motive.reports.service.reportmanager.ReportGroup;
import motive.reports.service.reportmanager.ReportManagerService;
import motive.reports.reportconsole.ServiceUtil;
import motive.reports.reportconsole.UserContext;
//import motive.reports.reportconsole.actions.ActionUtils;
import motive.reports.reportconsole.actions.BaseAction;
import motive.trace.TraceLogger;

//import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
//import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
//import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;
//import org.apache.struts.util.MessageResources;


/**
 * Populates the fields on the edit/new device panel.
 */
public class ImportReport extends BaseAction
{
    /** logger */
    private static final Logger logger = TraceLogger.getLogger(ImportReport.class);

    /** name of the file input field */
    public static final String IMPORT_FILE_FIELD = "importFile";

    /** name of the file input field */
    public static final String REPORT_GROUP_FIELD = "reportGroup";

    /**
     * Populates the fields on the edit/new device panel.
     */
    public ActionForward executeAction(ActionMapping mapping,
                                       ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws Exception
    {
        //String forwardName = ActionUtils.FORWARD_SUCCESS;
        DynaActionForm theForm = (DynaActionForm) form;
        //MessageResources messages = ActionUtils.getMessageResources(request);
        FormFile xmlDataFile = null;
        String importErrorMsg = "";
        logger.info("***************** FILE: " + theForm.get(IMPORT_FILE_FIELD));
        xmlDataFile = (FormFile) theForm.get(IMPORT_FILE_FIELD);
        try
        {
		   	ReportManagerService rms = ServiceUtil.getInstance().getReportManagerService();
        	String groupID = (String)theForm.get("reportGroup");
        	logger.info("***************** groupID: " + groupID);
	        ReportGroup rg = rms.getReportGroupById( new Long( groupID ));
	        logger.info("***************** ReportGroup: " + rg.toString());
//	        String reportDesign = new String( xmlDataFile.getFileData());
	        logger.info("***************** name: " + (String)theForm.get("name"));
	        logger.info("***************** description: " + (String)theForm.get("description"));
	        logger.info("***************** FILE: " + theForm.get(IMPORT_FILE_FIELD));
	        Report newReport = rms.loadReportFromZipArchive(xmlDataFile.getFileData());
		   	newReport.setDisplayName((String)theForm.get("name"));
		   	newReport.setDescription((String)theForm.get("description"));
		   	newReport.setReportGroup(rg);
		   	rms.create(newReport);
		   	UserContext.setCurrentGroupId(request, new Long( groupID ));
        }
        catch( DuplicateReportException e)
        {
        	logger.error("Error creating new report, name in use: " + e.getMessage());
        	String[] msgArray = {e.getReportName(),e.getReportDisplayName(), e.getReportGroupName()};
        	importErrorMsg = getMessage( "importReport.dialog.error.nameInUseLong", request, msgArray );
        }
        catch( ObjectExistsException e)
        {
        	logger.error("Error creating new report, name in use: " + e.getMessage());
        	String[] msgArray = {(String)theForm.get("name")};
        	importErrorMsg = getMessage( "importReport.dialog.error.nameInUse", request, msgArray );
        }
        catch( InvalidReportException e)
        {
        	logger.error("Error creating new report, invalid zip file: " + e.getMessage());
        	String[] msgArray = {(String)theForm.get("name")};
        	importErrorMsg = getMessage( "importReport.dialog.error.invalidZip", request, msgArray );
        }
        catch( Exception e)
        {
        	logger.error("Error creating new report: " + e.getMessage());
        	importErrorMsg = getMessage( "importReport.dialog.error.unknown", request );
        }
        
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print(importErrorMsg);
        writer.flush();
        writer.close();
        return null;
        
        
        //request.setAttribute(ActionUtils.REPORT_IMPORT_FAILED, importErrorMsg);
	   	//return mapping.findForward(forwardName);
    }
    
}
