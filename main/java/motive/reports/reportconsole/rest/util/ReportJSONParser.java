package motive.reports.reportconsole.rest.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReportJSONParser 
{
	private Logger logger = Logger.getLogger(ReportJSONParser.class);
	
	public JSONScheduleVO parseJSONStrScheduleRequest(String strObject)
			throws ReportServiceException  
	{
		JSONScheduleVO schedule = new JSONScheduleVO();
		try 
		{
			JSONObject jsonObject = convertStringToJSONObject(strObject);
			
			if (jsonObject != null) 
			{
				JSONObject parentJSON = jsonObject.getJSONObject("VZRequest");
				logger.info("[parseJSONSchedule]:::: parentJSON :: " + parentJSON);
				
				try
				{
					schedule.setReqId(parentJSON.getString("ReqId"));
				} catch(JSONException e) 
				{
					logger.info("[parseJSONSchedule]:::: ReqId is null");
				}
				
				try
				{
					schedule.setUsrId(parentJSON.getString("UsrId"));
				} catch(JSONException e) 
				{
					logger.info("[parseJSONSchedule]:::: UsrId is null");
				}
				
				try
				{
					schedule.setSource(parentJSON.getString("Source"));
				} catch(JSONException e) 
				{
					logger.info("[parseJSONSchedule]:::: Source is null");
				}
				
				try
				{
					schedule.setTrnType(parentJSON.getString("TrnType"));
				} catch(JSONException e) 
				{
					logger.info("[parseJSONSchedule]:::: TrnType is null");
				}
				
				try
				{
					schedule.setScheduleName(parentJSON.getString("ScheduleName"));
				} catch(JSONException e) 
				{
					logger.info("[parseJSONSchedule]:::: ScheduleName is null");
				}
				
				try
				{
					schedule.setFrequency(parentJSON.getString("Frequency"));
				} catch(JSONException e) 
				{
					logger.info("[parseJSONSchedule]:::: Frequency is null");
				}
				
				try
				{
					schedule.setStartTime(parentJSON.getString("StartTime"));
				} catch(JSONException e) 
				{
					logger.info("[parseJSONSchedule]:::: StartTime is null");
				}

				try
				{
					schedule.setDocType(parentJSON.getString("DocType"));
				} catch(JSONException e) 
				{
					logger.info("[parseJSONSchedule]:::: DocType is null");
				}
				
				try
				{
					schedule.setReportName(parentJSON.getString("ReportName"));
				} catch(JSONException e) 
				{
					logger.info("[parseJSONSchedule]:::: ReportName is null");
				}
				
				try
				{
					schedule.setParticipantName(parentJSON.getString("ParticipantName"));
				} catch(JSONException e) 
				{
					logger.info("[parseJSONSchedule]:::: ParticipantName is null");
				}
				
				try
				{
					schedule.setEMailRecipient(parentJSON.getString("EMailRecipient"));	
				} catch(JSONException e) 
				{
					logger.info("[parseJSONSchedule]:::: EMailRecipient is null");
				}
				
				logger.info("[parseJSONSchedule]:::: reqId :: " + schedule.getReqId());
				logger.info("[parseJSONSchedule]:::: usrId :: " + schedule.getUsrId());
				logger.info("[parseJSONSchedule]:::: source :: " + schedule.getSource());
				logger.info("[parseJSONSchedule]:::: trnType :: " + schedule.getTrnType());
				logger.info("[parseJSONSchedule]:::: scheduleName :: " + schedule.getScheduleName());
				logger.info("[parseJSONSchedule]:::: frequency :: " + schedule.getFrequency());
				logger.info("[parseJSONSchedule]:::: startTime :: " + schedule.getStartTime());
				logger.info("[parseJSONSchedule]:::: docType :: " + schedule.getDocType());
				logger.info("[parseJSONSchedule]:::: reportName :: " + schedule.getReportName());
				logger.info("[parseJSONSchedule]:::: eMailRecipient :: " + schedule.getEMailRecipient());
				
				
				
				JSONObject paramsJSON = null;
				try
				{
					paramsJSON = parentJSON.getJSONObject("ReportParameters");
				} catch(JSONException e) 
				{
					logger.info("[parseJSONSchedule]:::: ReportParameters is null");
				}
				if (paramsJSON != null)
				{
					logger.info("[parseJSONSchedule]:::: paramsJSON :: " + paramsJSON.toString());
					JSONArray paramArray = null;
					try
					{
						paramArray = paramsJSON.getJSONArray("parameter");
					} catch(JSONException e) 
					{
						logger.info("[parseJSONSchedule]:::: parameter is null");
					}
					if (paramArray != null)
					{
						logger.info("[parseJSONSchedule]:::: paramArray :: " + paramArray.toString());
						
						List<JSONReportParamVO> paramsList = new ArrayList<JSONReportParamVO>();
						for (int i = 0; i < paramArray.length(); i++) 
						{
							JSONReportParamVO param = new JSONReportParamVO();
							JSONObject pJSON = paramArray.getJSONObject(i);
							String sname = null;
							String svalue = null;
							
							try
							{
								sname = pJSON.getString("name");
							} catch(JSONException e) 
							{
								logger.info("[parseJSONSchedule]:::: name is null");
							}
							
							try
							{
								svalue = pJSON.getString("value");
							} catch(JSONException e) 
							{
								logger.info("[parseJSONSchedule]:::: value is null");
							}
							
							if (sname == null || svalue == null) {
								continue;
							}
							
							param.setName(sname);
							param.setValue(svalue);
							logger.info("[parseJSONSchedule]:::: sname :: " + sname);
							logger.info("[parseJSONSchedule]:::: svalue :: " + svalue);
							paramsList.add(param);
						}
						
						schedule.setParameter(paramsList);
					}
					
				}
			}

		} 
		catch (ReportServiceException re) 
		{
			throw new ReportServiceException(re.getMessage());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new ReportServiceException("Some parameters on the JSON String are missed");
		}
		return schedule;
	}
	
	public JSONObject convertStringToJSONObject(String strObj) throws ReportServiceException 
	{
		JSONObject jsonObj = null;
		try 
		{
			jsonObj = new JSONObject(strObj);
		} catch(Exception e) {
			logger.info("[parseJSONSchedule]:::: error :: " + e.getMessage()); 
			throw new ReportServiceException("JSON Request format is not correct");
		}
		return jsonObj;
	}
	
	
	public static void main(String... args) 
	{
		String jsonString = "{\"VZRequest\": {\"ReqId\": \"REQ-61215-1457-FF\",\"UsrId\": \"francifu\",\"Source\": \"DMPortal\",\"TrnType\": \"AddReportToSchedule\",\"ScheduleName\": \"Schedule12\",\"Frequency\": \"Daily\",\"StartTime\": \"09/15/2015 12:30 AM\",\"ReportName\": \"Software Update Report-New Format\",\"ReportParameters\": {\"parameter\": [ {\"name\": \"Manufacturer\",\"value\": \"HTC\"},{\"name\": \"Model\",\"value\": \"HTC6525LVW\"},{\"name\": \"SourceFirmware\",\"value\": \"FGGFr1\"},{\"name\": \"DestinationFirmware\",\"value\": \"FGGFr2\"} ]	} } }";
		try 
		{
			System.out.println(jsonString);
			
			ReportJSONParser parser = new ReportJSONParser();
			JSONScheduleVO schedule = parser
					.parseJSONStrScheduleRequest(jsonString);
			
			System.out.println("schedule..........");
			System.out.println(schedule);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
