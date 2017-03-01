package motive.reports.reportconsole.schedule.actions;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import motive.report.schedule.schedulemanager.ScheduleManagerService;
import motive.reports.reportconsole.ScheduleUtil;
import motive.reports.reportconsole.ServiceUtil;
import motive.reports.reportconsole.actions.ActionUtils;
import motive.reports.reportconsole.actions.MotiveDispatchAction;
import motive.reports.service.reportmanager.ReportManagerService;
import motive.trace.TraceLogger;

import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

public class DeleteScheduleAjax extends MotiveDispatchAction {
    /** logger */
    private static final Logger logger = TraceLogger.getLogger(DeleteScheduleAjax.class);
    
	public ActionForward deleteSchedules( ActionMapping mapping,
		     							ActionForm form,
									    HttpServletRequest request,
									    HttpServletResponse response) throws Exception {
		String ids = request.getParameter("ids");
	   	ReportManagerService rms = ServiceUtil.getInstance().getReportManagerService();
	   	ScheduleManagerService sms = ScheduleUtil.getInstance().getScheduleManagerService();
	   	logger.info("...... Delete Schedules ......");
		String responseText = "";
		int deleteCount = 0;
		int deleteFailedCount = 0;
		Long currentID = null;
		if (!GenericValidator.isBlankOrNull(ids)) {
			String[] idsArray = ids.split(",");
			for (int i = 0; i < idsArray.length; i++) {
				if ( idsArray[i].equals("on")) {
					// ignore invalid id.
				} else {
					try {
						currentID = new Long(idsArray[i]);
						if ( currentID != null ) {
							logger.info("Deleting Job # " + currentID + " from the Scheduler");
							sms.unschedule(currentID);
							logger.info("Deleting Schedule # " + currentID + " from the DB");
							rms.deleteSchedule(currentID);
							deleteCount++;
						}
					} catch (Exception e) {
						deleteFailedCount++;
						logger.warn("Schedule with id " + idsArray[i] + " was not deleted due to error: " + e.getMessage());
					}
				}
			}
		}
        MessageResources messages = ActionUtils.getMessageResources(request);
        Object[] args = null;
        if ( deleteFailedCount == 0) {
        	args = new Object[1];
        	args[0] = String.valueOf(deleteCount);
        	String messageText = "";
        	if (deleteCount == 1)
        		messageText = messages.getMessage( "schedule.dialog.delete.pass.single" );
        	else
        		messageText = messages.getMessage( "schedule.dialog.delete.pass.multiple" );
        	responseText = ActionUtils.formatMessage(messageText, args);
        } else {
        	args = new Object[2];
        	String messageText = "";
        	args[0] = String.valueOf(deleteCount);
        	args[1] = String.valueOf(deleteFailedCount);
        	if (( deleteCount == 1 ) && (deleteFailedCount == 1 ))
        		messageText = messages.getMessage( "schedule.delete.partial.single.single");
        	else if (( deleteCount == 1 ) && (deleteFailedCount != 1 ))
        		messageText = messages.getMessage( "schedule.delete.partial.single.multiple");
        	else if (( deleteCount != 1 ) && (deleteFailedCount == 1 ))
        		messageText = messages.getMessage( "schedule.delete.partial.multiple.single");
        	else
        		messageText = messages.getMessage( "schedule.delete.partial.multiple.multiple");
        	responseText = ActionUtils.formatMessage(messageText, args);
        }

		responseText = String.valueOf(deleteCount);
		writeResponse(response, responseText);
		return null;
	}
	
	private String getMessage(String key, HttpServletRequest request) {
        Locale locale = ActionUtils.getLocale(request);
        MessageResources messages = ActionUtils.getMessageResources(request);
        return messages.getMessage(locale, key);
	}

}
