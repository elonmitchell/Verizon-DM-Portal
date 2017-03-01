package motive.reports.reportconsole.schedule.actions;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import motive.report.schedule.schedulemanager.ScheduleManagerService;
import motive.reports.reportconsole.ScheduleUtil;
import motive.reports.reportconsole.ServiceUtil;
import motive.reports.reportconsole.actions.ActionUtils;
import motive.reports.reportconsole.actions.BaseAction;
import motive.reports.service.reportmanager.ReportManagerService;
import motive.reports.service.reportmanager.Schedule;
import motive.trace.TraceLogger;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class UpdateSchedule extends BaseAction {
	
    /** logger */
    private static final Logger logger = TraceLogger.getLogger(UpdateSchedule.class);

    /**
     * 
     */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String forwardName = ActionUtils.FORWARD_SUCCESS;
		DynaActionForm theForm = (DynaActionForm) form;
		
		ReportManagerService rms = ServiceUtil.getInstance()
				.getReportManagerService();
		ScheduleManagerService sms = ScheduleUtil.getInstance()
				.getScheduleManagerService();
		
		String scheduleId = (String) request.getParameter("scheduleId");
		String freq = (String) request.getParameter("reportFrequency");
		
		String startTime = (String) request.getParameter("startTimeDate");
		String startHour = request.getParameter("startTimeHour");
		// Getting the num of schedules from the UI
		String startMin = (String) request.getParameter("startTimeMinute");
		String emailids = (String) request.getParameter("emailsId");
		String amValue = (String) request.getParameter("amvalueId");
		String meridian = "pm";
		if (amValue != null && amValue.equals("true")) {
			meridian = "am";
		}
		
		logger.info("scheduleId: " + scheduleId + "  freq: " + freq
				+ "  startTime: " + startTime + "  startHour: " + startHour
				+ "  startMin: " + startMin + "  meridian: " + meridian
				+ "  emailids: " + emailids);
		
		// Getting the value of Schedule Enabled from the UI
		Schedule schedule = rms.getScheduleById( Long.valueOf(scheduleId) );
		
		Date updated = new Date();
		Date date = new Date();
	
		if (startTime != null && startHour != null && startMin != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
			date = sdf.parse(startTime+" "+startHour+":"+startMin+":"+"00"+" "+ meridian);
			logger.info("... Start Date: " + date.toString());
		}
		
		schedule.setStartDate(date);
		schedule.setInterval( Integer.parseInt(freq) );
		schedule.setEmailto(emailids);
		schedule.setUpdated(updated);
		rms.update(schedule);
		
		//Unschedule/adding the job 
		sms.unschedule(new Long(scheduleId));
		sms.push(new Long(scheduleId));
		
		return mapping.findForward(forwardName);
	}

}
