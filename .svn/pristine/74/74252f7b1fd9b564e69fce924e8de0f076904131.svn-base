/**
 * 
 */
package motive.reports.reportconsole.view.actions;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import motive.reports.reportconsole.ServiceUtil;

import motive.reports.reportconsole.actions.ActionUtils;

import motive.reports.reportconsole.actions.JSONBaseAction;
import motive.reports.service.reportmanager.HistoricalReport;

import motive.reports.service.reportmanager.ReportManagerService;

import motive.trace.TraceLogger;

/**
 * @author arponnus
 *
 */
public class HistoricalReportAction extends JSONBaseAction 
{
	
	/** logger */
	private static final Logger logger = TraceLogger.getLogger(HistoricalReportAction.class);
	
	
	@Override
	protected JSONObject getJSON(HttpServletRequest request,
			HttpServletResponse response) throws JSONException, CreateException, NamingException {
		logger.info("Calling getJSON of HistoricalReport");
		HistoricalReport[] histRptArray = null;
		JSONArray jsonArray = null;
		JSONObject returnObj = null;
        String statusCode = null;
        String statusMsg = null;
        
	   	
        try {
        	
        	ReportManagerService rms = ServiceUtil.getInstance().getReportManagerService();
        	
        	String userName = (String) request.getSession().getAttribute("userName");
        	
    	   	if(userName == null || !request.isRequestedSessionIdValid()){
    	   		logger.error("Session Invalid .. route to login ");
                throw new JSONException("Session Invalid");
    	   	}
    	   	
    	   	String chargingGrpId = null;
    	   	
        	try
        	{
        		JSONObject histJson = null;
    			chargingGrpId = (String) rms.getChargingGroupIdByName(userName);
    			if(chargingGrpId != null)
    				histRptArray = rms.getHistReportByChargGrpId(chargingGrpId);
    			returnObj = new JSONObject();
    			jsonArray = new JSONArray();
    			for(HistoricalReport histReport: histRptArray){
    				histJson = new JSONObject();
    	            String id = histReport.getId() + "";
    	            String fileName = histReport.getHistReportName();
    	            String description = histReport.getDescription();
    	            String roles = histReport.getRoles();
    	           // String[] rolesArray = null;
    	            if(id != null && fileName != null && description != null && roles != null) {
    	            	histJson.put("id", id);
    	            	histJson.put("fileName", fileName);
    	            	histJson.put("description", description);
    	            	histJson.put("roles", roles);
    	            	jsonArray.put(histJson);    	            		
    	            }					
    	        }
        	}
        	catch ( Exception pe )
        	{
        		logger.error("Could not load historical report : " + pe.getMessage());
        		super.errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("manage.error.reportGroupLoadError"));
        	}
        	
        	returnObj.put(ActionUtils.HIST_REPORT_LIST, jsonArray);
        	statusCode = JSONBaseAction.SUCCESS;
            statusMsg = JSONBaseAction.SUCCESS_MSG;
        	
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getCause());
            logger.error(e);
            statusCode = JSONBaseAction.EXCEPTION;
            statusMsg = JSONBaseAction.SERVER_EXCEPTION_MSG;
        }
        logger.info("Status Code >>>>>>>>>>>  " + statusCode);
        logger.info("Status Message >>>>>>>>>>>  " + statusMsg);
        returnObj.put(RESPONSE, createJSONStatusObject(statusCode, statusMsg));
        return returnObj;
	}
}
