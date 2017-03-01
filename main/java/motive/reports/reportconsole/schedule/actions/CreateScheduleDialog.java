package motive.reports.reportconsole.schedule.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import motive.reports.reportconsole.ServiceUtil;
import motive.reports.reportconsole.UserContext;
import motive.reports.reportconsole.actions.ActionUtils;
import motive.reports.reportconsole.actions.BaseAction;
import motive.reports.service.reportmanager.ReportGroup;
import motive.reports.service.reportmanager.ReportManagerService;
import motive.trace.TraceLogger;

public class CreateScheduleDialog extends BaseAction {
	
    /** logger */
    private static final Logger logger = TraceLogger.getLogger(CreateScheduleDialog.class);

	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String forwardName = ActionUtils.FORWARD_SUCCESS;
        boolean getGroupsFailed = false;
	   	ReportManagerService rms = ServiceUtil.getInstance().getReportManagerService();
     	ReportGroup[] rg = new ReportGroup[0];
    	try
    	{
			rg = rms.getAllReportGroups();
			
    	}
    	catch ( Exception pe )
    	{
    		logger.error("Could not load report groups : " + pe.getMessage());
    		getGroupsFailed = true;
    	}
	   	Long currentGroupID = UserContext.getCurrentGroupId(request);
        if ( currentGroupID == null)
        {
        	if ( rg.length > 0)
        		currentGroupID = rg[0].getId();
        }	
        else
        {
        	try
        	{
        		ReportGroup theGroup = rms.getReportGroupById(currentGroupID);
        		if ( theGroup == null )
        		{
        			if ( rg.length > 0)
        				currentGroupID = rg[0].getId();
        		}
        	}
        	catch ( Exception e)
        	{
        		if ( rg.length > 0)
        			currentGroupID = rg[0].getId();
        		logger.warn("Default report group not found, reverting back to first report group.");
        	}
        	
        }
        String groupIDStr = "";
        if ( currentGroupID != null  )
        	groupIDStr = currentGroupID.toString();
        request.setAttribute(ActionUtils.CURRENT_GROUP_ID, groupIDStr);
        request.setAttribute("groupLoadFailed", new Boolean(getGroupsFailed));
        request.setAttribute(ActionUtils.GROUP_LIST, rg);
        return mapping.findForward(forwardName);
	}
	

}
