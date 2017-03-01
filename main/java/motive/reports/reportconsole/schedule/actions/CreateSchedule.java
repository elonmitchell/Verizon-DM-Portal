package motive.reports.reportconsole.schedule.actions;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
//import org.apache.struts.util.MessageResources;

import motive.exceptions.ObjectExistsException;
import motive.reports.reportconsole.ServiceUtil;
import motive.reports.reportconsole.actions.ActionUtils;
import motive.reports.reportconsole.actions.BaseAction;
import motive.reports.service.reportmanager.DuplicateReportException;
import motive.reports.service.reportmanager.ReportManagerService;
import motive.reports.service.reportmanager.Schedule;
import motive.reports.service.reportmanager.ScheduleProperties;
import motive.trace.TraceLogger;

public class CreateSchedule extends BaseAction {
	
	 /** logger */
    private static final Logger logger = TraceLogger.getLogger(CreateSchedule.class);

    /** name of the file input field */
    public static final String SCHEDULE_FIELD = "schedule";

	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Date inserted = new Date();
        String forwardName = ActionUtils.FORWARD_SUCCESS;
        DynaActionForm theForm = (DynaActionForm) form;
        //MessageResources messages = ActionUtils.getMessageResources(request);
        String errorMsg = "";
        try {
		   	ReportManagerService rms = ServiceUtil.getInstance().getReportManagerService();
        	//String scheduleID = (String)theForm.get("reportGroup");
	        Schedule schedule = new Schedule(); 
    		schedule.setName((String)theForm.get("name"));
    		schedule.setStatus(0);
    		schedule.setInterval(0);
    		schedule.setEmailto("ffuenthez@gmail.com");
    		schedule.setInserted(inserted);
    		schedule.setDeleted(Boolean.FALSE);
    		schedule.setCreatedBy("francifu");	        		
		   	
    		ScheduleProperties scheduleProp1 = new ScheduleProperties();
    		scheduleProp1.setSchedule(schedule);
    		scheduleProp1.setName("Prop_1");
    		scheduleProp1.setValue("Value_1");
    		scheduleProp1.setInserted(inserted);
    		scheduleProp1.setDeleted(Boolean.FALSE);
    		scheduleProp1.setCreatedBy("francifu");
    		scheduleProp1.setUpdatedBy("francifu");
    		
    		ScheduleProperties scheduleProp2 = new ScheduleProperties();
    		scheduleProp2.setSchedule(schedule);
    		scheduleProp2.setName("Prop_2");
    		scheduleProp2.setValue("Value_2");
    		scheduleProp2.setInserted(inserted);
    		scheduleProp2.setDeleted(Boolean.FALSE);
    		scheduleProp2.setCreatedBy("francifu");
    		scheduleProp2.setUpdatedBy("francifu");
    		
    		Set schedulePropSet = new HashSet();
    		schedulePropSet.add(scheduleProp1);
    		schedulePropSet.add(scheduleProp2);
    		
    		schedule.setScheduleProperties(schedulePropSet);
    					 
    		rms.create(schedule);
		   	
		   	//UserContext.setCurrentGroupId(request, new Long( groupID ));
        } catch( DuplicateReportException e) {
        	logger.error("Error creating new schedule, name in use: " + e.getMessage());
        	String[] msgArray = {e.getReportName(),e.getReportDisplayName(), e.getReportGroupName()};
        	errorMsg = getMessage( "importReport.dialog.error.nameInUseLong", request, msgArray );
        } catch( ObjectExistsException e) {
        	logger.error("Error creating new report, name in use: " + e.getMessage());
        	String[] msgArray = {(String)theForm.get("name")};
        	errorMsg = getMessage( "importReport.dialog.error.nameInUse", request, msgArray );
        } /*catch( InvalidReportException e) {
        	logger.error("Error creating new report, invalid zip file: " + e.getMessage());
        	String[] msgArray = {(String)theForm.get("name")};
        	errorMsg = getMessage( "importReport.dialog.error.invalidZip", request, msgArray );
        } */catch( Exception e) {
        	logger.error("Error creating new schedule: " + e.getMessage());
        	errorMsg = getMessage( "importReport.dialog.error.unknown", request );
        }
        request.setAttribute(ActionUtils.REPORT_IMPORT_FAILED, errorMsg);
	   	return mapping.findForward(forwardName);
    }
}
