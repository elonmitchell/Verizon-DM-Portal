package motive.reports.reportconsole.rest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import motive.reports.reportconsole.rest.service.ReportService;
import motive.reports.reportconsole.rest.service.impl.ReportServiceImpl;
import motive.reports.reportconsole.rest.util.JSONScheduleVO;
import motive.reports.reportconsole.rest.util.ReportJSONParser;
import motive.reports.reportconsole.rest.util.ReportServiceException;
import motive.reports.reportconsole.rest.util.ReportServiceValidator;
import motive.trace.TraceLogger;

import org.apache.log4j.Logger;
import org.json.JSONObject;



@Path("/schedule")
public class ScheduleRestService {
	private static final Logger logger = TraceLogger.getLogger(ScheduleRestService.class);
	
	private ReportService reportService = new ReportServiceImpl();
	
	@POST
	@Path("/service")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response submitTransactionREST(InputStream incomingData,
			@Context HttpServletRequest request) {
		logger.info("[submitTransactionREST]:::: ---------------------------------------- ::::: ");
		logger.info("[submitTransactionREST]:::: ----------- Init REST Service ---------- ::::: ");
		logger.info("[submitTransactionREST]:::: ---------------------------------------- ::::: ");

		JSONScheduleVO jsonSchedule = new JSONScheduleVO();
		StringBuilder requestBuilder = new StringBuilder();
		StringBuilder responseBuilder = new StringBuilder();
		ReportJSONParser parser = new ReportJSONParser();
		
		try {
			if (incomingData == null) {
				responseBuilder.append("{\"VZResponse\":{\"ResultCode\":\"1\",\"ResultReason\":\"ERROR: Invalid Request\"}}");
				logger.info("[ReportService]: ERROR response: "
						+ responseBuilder.toString());
				return Response.status(200).entity(responseBuilder.toString()).build();
			}
			
			BufferedReader in = new BufferedReader(new InputStreamReader(
					incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				requestBuilder.append(line);
			}
			
			logger.info("[ReportService]::submitTransactionREST Data Received: "
					+ requestBuilder.toString());
			
			jsonSchedule = parser.parseJSONStrScheduleRequest(requestBuilder
					.toString());
			
			ReportServiceValidator validator = new ReportServiceValidator(); 
			validator.validateRequiredParametersForWebServiceReport(jsonSchedule);
			// create the schedule on db and quartz
			reportService.createSchedule(jsonSchedule);
			
			String response = getResponseMessage(jsonSchedule, 0, "SUCCESS");
			logger.info("[submitTransactionREST]:::: response :: " + response);
			logger.info("[submitTransactionREST]:::: ---------- End report service --------- ::::: ");
			
			// return HTTP response 200 in case of success
			return Response.status(200).entity(response).build();
		}
		catch (ReportServiceException e)
		{
			String responseMessage = getResponseMessage(jsonSchedule, e.getErrorCode(), e.getMessage());
			logger.error("[submitTransactionREST]: ERROR responseMessage :: " + responseMessage);
			return Response.status(200).entity(responseMessage).build();
		}
		catch (Exception e) 
		{
			responseBuilder.append("{\"VZResponse\":{\"RESULTCODE\":\"1\",\"RESULTREASON\":\"Internal Server Error: " + e.getMessage() + "\"}}");
			logger.info("[MotiveEFotaService]: ERROR response: "
					+ responseBuilder.toString());
			return Response.status(200).entity(responseBuilder.toString()).build();

		}
	}
	
	
	private String getResponseMessage(JSONScheduleVO jsonSchedule, int code, String message) 
	{   
        JSONObject response = new JSONObject();
		JSONObject vzwResponse = new JSONObject();
		JSONObject status = new JSONObject();
		vzwResponse.put("ReqId", jsonSchedule.getReqId());
		vzwResponse.put("UsrId", jsonSchedule.getUsrId());
		vzwResponse.put("Source", jsonSchedule.getSource());
		vzwResponse.put("TrnType", jsonSchedule.getTrnType());
		vzwResponse.put("ReportName", jsonSchedule.getReportName());
		vzwResponse.put("ScheduleTime", jsonSchedule.getStartTime());
		vzwResponse.put("EMailRecipient", jsonSchedule.getEMailRecipient());
		vzwResponse.put("ParticipantName", jsonSchedule.getParticipantName());
		
		status.put("ResultCode", String.valueOf(code));
		status.put("ResultReason", message);
		vzwResponse.put("Status", status);
		
		response.put("VZResponse", vzwResponse);
		return response.toString();
	}
}

