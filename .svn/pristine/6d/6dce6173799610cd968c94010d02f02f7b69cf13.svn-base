package motive.reports.reportconsole.rest.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import motive.exceptions.ObjectExistsException;

import motive.report.schedule.schedulemanager.ScheduleManagerService;
import motive.reports.reportconsole.ScheduleUtil;
import motive.reports.reportconsole.ServiceUtil;
import motive.reports.reportconsole.rest.service.ReportService;
import motive.reports.reportconsole.rest.util.JSONReportParamVO;
import motive.reports.reportconsole.rest.util.JSONScheduleVO;
import motive.reports.reportconsole.rest.util.ReportServiceException;
import motive.reports.service.reportmanager.DuplicateReportException;
import motive.reports.service.reportmanager.Report;
import motive.reports.service.reportmanager.ReportManagerService;
import motive.reports.service.reportmanager.ReportSystemProperty;
import motive.reports.service.reportmanager.Schedule;
import motive.reports.service.reportmanager.ScheduleCountByTime;
import motive.reports.service.reportmanager.ScheduleProperties;


public class ReportServiceImpl implements ReportService {
	
	private Logger logger = Logger.getLogger(ReportServiceImpl.class);
	
	private ReportManagerService rms;
	
	/**
	 * 
	 */
	public boolean createSchedule(JSONScheduleVO jsonSchedule)
			throws ReportServiceException
	{
    	String chargingGroupId = null;
    	
    	try 
    	{
    		rms = ServiceUtil.getInstance().getReportManagerService();
    		
    		ScheduleManagerService sms = ScheduleUtil.getInstance()
    				.getScheduleManagerService();
    		
    		logger.info("[createSchedule]:::: ...........................................");
        	logger.info("[createSchedule]:::: ... Creating Schedule from rest service ...");
        	logger.info("[createSchedule]:::: ...........................................");
		
			// Get the chargingGroupId of the user
			if (jsonSchedule.getUsrId() != null && jsonSchedule.getUsrId().length() != 0) 
			{
				chargingGroupId = rms.getChargingGroupIdByPartipantName(jsonSchedule.getParticipantName());
				logger.info("[createSchedule]:::: chargingGroupId :: " + chargingGroupId);
			}
			if (chargingGroupId == null) 
			{
				logger.error("[createSchedule]:::: ERROR :: Participant invalid, no charging groupd id");
				throw new ReportServiceException(4, "The participant is invalid, no chargingGroupId found for this participant name.");
			}
			
			// Get the report by report name
			Report report = rms.getReportByName(jsonSchedule.getReportName());
			if (report == null) 
			{
				throw new ReportServiceException(4, "Report doesn't exist on DM Reporting.");
			}
			String roles = report.getRoles();
			String[] rolesArray = roles.split(",");
			boolean hasValidRoles = false;
			if (rolesArray != null && rolesArray.length > 0) 
			{
				for (int index = 0; index < rolesArray.length; index++)
				{
					String rol = rolesArray[index];
					if ("RPT_FOR_HFOTA".equalsIgnoreCase(rol)) 
					{
						hasValidRoles = true;
						break;
					}
				}
			}
			if (!hasValidRoles) {
				throw new ReportServiceException(4, "Insufficent permission on the report to be executed.");
			}
			
			Schedule schedule = createScheduleObjectToSave(jsonSchedule, chargingGroupId, report.getReportFormat());

			Long scheduleId = rms.create(schedule);
			logger.info("[createSchedule]:::: Schedule created successfully on db - scheduleId :: " + scheduleId);
			
			sms.push(scheduleId);
			logger.info("[createSchedule]:::: Schedule added successfully to quartz");
		}
    	catch (ReportServiceException e) {
    		logger.error(e.getMessage());
    		throw new ReportServiceException(e.getErrorCode(), e.getMessage());
    	}
    	catch (DuplicateReportException e) 
		{
			logger.error("Error creating new schedule, name in use: "
					+ e.getMessage());
			throw new ReportServiceException(4, 
					"Unable to create the new schedule. The schedule is invalid or may already be in use.");
		} 
    	catch (ObjectExistsException e) 
		{
			logger.error("Error creating new report, name in use: "
					+ e.getMessage());
			throw new ReportServiceException(4, 
					"Error creating new schedule, name in use.");
		} 
    	catch (Exception e) 
		{
			logger.error("Error creating new schedule: " + e.getMessage());
			throw new ReportServiceException(1, "Internal server error");
		}
    	return true;
	}
	
	/**
	 * 
	 * @param paramList
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Map<String, String> convertParamListToMap(List<JSONReportParamVO> paramList) 
	{
		Map<String, String> map = new HashMap<String, String>();
		if (paramList != null) 
		{
			for (Iterator it = paramList.iterator(); it.hasNext();) 
			{
				JSONReportParamVO parmVO = (JSONReportParamVO) it.next();
				map.put(parmVO.getName(), parmVO.getValue());
			}
		}
		return map;
	}
	
	/**
	 * 
	 * @param chargingGroupId
	 * @param scheduleArray
	 * @param jsonParamList
	 * @return
	 * @throws ReportServiceException
	 */
	@SuppressWarnings("rawtypes")
	public boolean isScheduleParamValid(String chargingGroupId,
			Schedule[] scheduleArray, List<JSONReportParamVO> jsonParamList)
			throws ReportServiceException
	{
        logger.info("[isScheduleParamValid]:::: ChargingGrroupId :: " + chargingGroupId);
		boolean scheduleExists = false;
		try
		{
			Map<String, String> paramMap = convertParamListToMap(jsonParamList);
			logger.info("[isScheduleParamValid]:::: Total of Schedules to validate: "
					+ scheduleArray.length);
			
			if (scheduleArray != null && scheduleArray.length > 0 ) 
			{
				for (int i = 0; i < scheduleArray.length; i++) 
				{
					Set propSet = scheduleArray[i].getScheduleProperties();
					if (propSet != null && propSet.size() > 0) 
					{
						int countEqualValues = 0;
						for (Iterator it = propSet.iterator(); it.hasNext();) 
						{
							ScheduleProperties scheProp = (ScheduleProperties)it.next();
							String inputValue = paramMap.get(scheProp.getName());
							if (!scheProp.getValue().equals(inputValue))
							{
								break;
							}
							countEqualValues++;
						}
						if (countEqualValues == propSet.size()) 
						{
							scheduleExists = true;
							break;
						}
					}
				}
			}	
		}
		catch(Exception e) 
		{
			logger.error("[isScheduleParamValid]:::: ERROR: " + e.getMessage());
			throw new ReportServiceException(1, "Internal server error");
		}
		return scheduleExists;
	}
	
	/**
	 * 
	 * @param jsonSchedule
	 * @param reportName
	 * @param chargingGroupId
	 * @return
	 * @throws ReportServiceException
	 */
	private Schedule createScheduleObjectToSave(JSONScheduleVO jsonSchedule,
			String chargingGroupId, String defaultDocType) throws ReportServiceException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy_HHmmss_SSS");
		Date date = new Date();
		Date inserted = new Date();
		Schedule schedule = new Schedule();
		try 
		{
			schedule.setName("REST" + "_" + sdf.format(date));
			schedule.setStatus(3); // Set status = REST report
			
			if (jsonSchedule.getFrequency() == null)
			{
				schedule.setInterval(0); // Set interval = 0, only execute once	
			} 
			else 
			{
				if (jsonSchedule.getFrequency().equals("Daily")) 
				{
					schedule.setInterval(1); // Set interval = 1, execute daily 
				} 
				else if (jsonSchedule.getFrequency().equals("Weekly")) 
				{
					schedule.setInterval(2); // Set interval = 2, execute weekly
				} 
				else
				{
					schedule.setInterval(0); // Set interval = 0, only execute once
				}
			}
			
			schedule.setEmailto(jsonSchedule.getEMailRecipient());
			schedule.setChargingGroupId(chargingGroupId);
			
			Date startTime = getStartDate(date);
			logger.info("[createScheduleObjectToSave]:::: Start Date :: " + startTime);
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yy hh:mm:ss aa");
			jsonSchedule.setStartTime(sdf2.format(startTime));
			
			schedule.setStartDate(startTime);
			schedule.setInserted(inserted);
			schedule.setUpdated(inserted);
			schedule.setDeleted(Boolean.FALSE);
			schedule.setCreatedBy(jsonSchedule.getUsrId());
			schedule.setUpdatedBy(jsonSchedule.getUsrId());
			
			// setting the report format
			if (jsonSchedule.getDocType() == null) 
			{
				schedule.setFormat(defaultDocType);
			} 
			else 
			{
				if (jsonSchedule.getDocType().equals("pdf") || jsonSchedule.getDocType().equals("xls") || jsonSchedule.getDocType().equals("ppt")) 
				{
					schedule.setFormat(jsonSchedule.getDocType());
				}
			}
			
			schedule.setReport(jsonSchedule.getReportName());
			
			Set<ScheduleProperties> schedulePropSet = new HashSet<ScheduleProperties>();
			for (JSONReportParamVO params : jsonSchedule.getParameter()) 
			{
				if (params != null) 
				{
					logger.info("[createScheduleObjectToSave]:::: name :: "
							+ params.getName() + "  :: value :: "
							+ params.getValue());
					
					ScheduleProperties scheduleProp1 = new ScheduleProperties();
					scheduleProp1.setSchedule(schedule);
					scheduleProp1.setName(params.getName());
					scheduleProp1.setValue(params.getValue());
					scheduleProp1.setInserted(inserted);
					scheduleProp1.setUpdated(inserted);
					scheduleProp1.setDeleted(Boolean.FALSE);
					scheduleProp1.setCreatedBy(jsonSchedule.getUsrId());
					scheduleProp1.setUpdatedBy(jsonSchedule.getUsrId());
					schedulePropSet.add(scheduleProp1);	
				}
			}
			schedule.setScheduleProperties(schedulePropSet);	
		}
		catch (Exception e) 
		{
			logger.error("[createScheduleObjectToSave]:::: ERROR: " + e.getMessage());
			throw new ReportServiceException(1, "Internal server error");
		}
		return schedule;
	}
	
	
    private Date getStartDate(Date currentDate) 
    		throws Exception{
    	logger.info("[getStartDate]: Calculate Start Time");
    	GregorianCalendar gc = new GregorianCalendar();
    	int maxThreads = 5;
    	
    	gc.setTime(currentDate);
    	if (gc.get(GregorianCalendar.SECOND) > 30) {
    		gc.add(GregorianCalendar.MINUTE, 2);	
    	} else {
    		gc.add(GregorianCalendar.MINUTE, 1);
    	}
    	gc.set(GregorianCalendar.SECOND, 0);
    	
    	Date startTime = gc.getTime();
    	try {
    		ReportSystemProperty rsp = rms.getSystemPropertyBykey("SCHEDULE_MAX_THREADS");
    		if(rsp != null ) {
    			maxThreads = Integer.parseInt(rsp.getValue());
    		}
    		logger.info("[getStartDate]: max of threads " + maxThreads);
    		ScheduleCountByTime[] scheduleArray = rms.getCountScheduleByTime();
    		startTime = calculateStartTime(scheduleArray, gc, maxThreads);
    		
    	} catch(Exception e) {
    		logger.error("[getStartDate]: Error when trying to calculate the best startTime: " + e.getMessage());
    		throw new Exception(e.getMessage());
    	}
    	return startTime;
    }
    
    /**
     * 
     * @param scheduleArray
     * @param date
     * @param maxThreads
     * @return
     */
	private Date calculateStartTime(ScheduleCountByTime[] scheduleArray,
			GregorianCalendar date, int maxThreads) {
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
		String strHHmm = sdf2.format(date.getTime());
		Date startDate = null;
		boolean dateFound = false;
		if (scheduleArray != null) {
			for (int a = 0; a < scheduleArray.length; a++) {
				logger.info("[calculateStartTime]:::: Time :: " + scheduleArray[a].getTime()
						+ " Total: " + scheduleArray[a].getTotal());
				if (scheduleArray[a].getTime().equals(strHHmm)
						&& scheduleArray[a].getTotal() > maxThreads) {
					date.add(GregorianCalendar.MINUTE, 1);
					startDate = calculateStartTime(scheduleArray, date, maxThreads);
					dateFound = true;
					break;
				}
			}
		}
		if (!dateFound) {
			startDate = date.getTime();
		}
		return startDate;
	}
	

}
