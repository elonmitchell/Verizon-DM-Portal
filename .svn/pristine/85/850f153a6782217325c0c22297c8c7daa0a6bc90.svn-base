package motive.reports.reportconsole.schedule.actions;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import motive.reports.reportconsole.ServiceUtil;
import motive.reports.reportconsole.actions.ActionUtils;
import motive.reports.reportconsole.actions.BaseAction;
//import motive.reports.service.reportmanager.Report;
import motive.reports.service.reportmanager.ReportManagerService;
import motive.reports.service.reportmanager.Schedule;
import motive.reports.service.reportmanager.ScheduleReport;
import motive.trace.TraceLogger;

public class ManageSchedules extends BaseAction {
	
	 /** logger */
    private static final Logger logger = TraceLogger.getLogger(ManageSchedules.class);


    /**
     * Populates the fields on the edit/new device panel.
     */
    public ActionForward executeAction(ActionMapping mapping,
                                       ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {
    	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm aa");
        String forwardName = ActionUtils.FORWARD_SUCCESS;
        boolean getSchedulesFailed = false;
	   	ReportManagerService rms = ServiceUtil.getInstance().getReportManagerService();
     	Schedule[] schArray = new Schedule[0];
     	ScheduleReport[] srArray = new ScheduleReport[0];
     	HttpSession session = request.getSession(true);
     	String userName = (String )session.getAttribute("userName");
    	try{
    		String chargingGroupId = rms.getChargingGroupIdByName(userName);
    		schArray = rms.getAllSchedulesByChargingGroupId(chargingGroupId);
			if (schArray != null && schArray.length > 0) {
				srArray = new ScheduleReport[schArray.length];
				int c = 0;
				for (int a = 0; a < schArray.length; a++) {
					ScheduleReport sr = new ScheduleReport();
					sr.setId(schArray[a].getId());
					sr.setScheduleName(schArray[a].getName());					
					//Report rep = rms.getReportById(new Long(schArray[a].getReportId()));
					//Report rep = rms.getReportByName(schArray[a].getReport());
					if (schArray[a].getReport() != null) {
						sr.setReportName(schArray[a].getReport());	
					} else {
						sr.setReportName("N/A");
					}
					
					if (schArray[a].getInterval() == 1) {
						sr.setFrequency("Daily");
					} else {
						sr.setFrequency("Weekly");
					}
					sr.setStartTime(sdf.format(schArray[a].getStartDate()));
					srArray[c] = sr;
					c++;
				}
			}
			
			
			
    	} catch ( Exception pe ) {
    		logger.error("Could not load report groups : " + pe.getMessage());
    		getSchedulesFailed = true;
    	}
    	
    	logger.info("Getting Schedules - Total: " + srArray.length);

        if ( request.getAttribute(ActionUtils.REPORT_IMPORT_FAILED) == null ) {
        	request.setAttribute(ActionUtils.REPORT_IMPORT_FAILED, "");
        }
        
        request.setAttribute("scheduleLoadFailed", new Boolean(getSchedulesFailed));
        request.setAttribute(ActionUtils.SCHEDULE_LIST, srArray);
        return mapping.findForward(forwardName);
    }

}
