package motive.reports.reportconsole.schedule.actions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import motive.reports.reportconsole.ServiceUtil;
import motive.reports.reportconsole.actions.ActionUtils;
//import motive.reports.service.reportmanager.Report;
import motive.reports.service.reportmanager.ReportManagerService;
import motive.reports.service.reportmanager.Schedule;
import motive.reports.service.reportmanager.User;
import motive.trace.TraceLogger;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

public class EditSchedulePopup  extends Action {
    /** trace logger */
    private static Logger logger = TraceLogger.getLogger(EditSchedulePopup.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			Pattern pattern = Pattern.compile("[0-9]{1,10}");
			String scheduleId = (String) request.getParameter("scheduleId");
			String reportName = "N/A";
			ReportManagerService rms = ServiceUtil.getInstance()
					.getReportManagerService();
			Schedule schedule = rms.getScheduleById(Long.valueOf(scheduleId));
			if (schedule != null) {
				schedule.setName(schedule.getName().replace("\"", "").replace("'", ""));
				JSONArray array = new JSONArray();
				String[] emailids = schedule.getEmailto().split(",");
				for (int i = 0; i < emailids.length; i++) {
					Matcher matcher = pattern.matcher( emailids[i] );
					if (matcher.matches()) {
						User user = rms.getUserByOltpid( new Long( emailids[i] ) );
						JSONObject json = new JSONObject();
						json.put( "id", user.getId() );
		                json.put( "email", user.getEmail() );
		                json.put( "name", user.getName() );
		                array.put(json);	
					} else {
						JSONObject json = new JSONObject();
		                json.put( "id", emailids[i] );
		                json.put( "email", emailids[i] );
		                json.put( "name", emailids[i] );
		                array.put(json);
					}
				}
				schedule.setEmailto( array.toString() );
				logger.info("User-jason: " + array.toString());
				
				reportName = schedule.getReport();
			}
			request.setAttribute("schedule", schedule);
			request.setAttribute("reportSchName", reportName);
			String forwardName = ActionUtils.FORWARD_SUCCESS;
			return mapping.findForward(forwardName);
		} catch (Exception e) {
			logger.error("An error occurred while getting the schedule information. Please see the server log for more details.");
			e.printStackTrace();
		}
		return null;
	}

}