package motive.reports.reportconsole;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import motive.report.schedule.schedulemanager.ScheduleManagerService;

import org.apache.log4j.Logger;

public class ScheduleUtil {
	
	private static final Logger logger = Logger.getLogger(ScheduleUtil.class);
	private static final String SCHEDULE_MANAGER_MODULE_NAME = "schedule-manager-service-ejb";
	
	private ScheduleManagerService scheduleManagerService;

	private static ScheduleUtil instance = null;

	public static ScheduleUtil getInstance() throws CreateException,
			NamingException {
		if (instance == null) {
			instance = newInstance();
		}
		return instance;
	}

	private static ScheduleUtil newInstance() throws CreateException,
			NamingException {
		ScheduleUtil scheduleUtil = new ScheduleUtil();
		logger.info("Value of serviceUtil is :" + scheduleUtil);
		return scheduleUtil;
	}

	protected ScheduleUtil() throws CreateException, NamingException {

		logger.info("Value of class is :" + ScheduleManagerService.class);
		logger.info("Value of localinterface is :"
				+ CustomJNDIHelper.getLocalInterface(
						ScheduleManagerService.class,
						SCHEDULE_MANAGER_MODULE_NAME));
		scheduleManagerService = (ScheduleManagerService) CustomJNDIHelper
				.getLocalInterface(ScheduleManagerService.class,
						SCHEDULE_MANAGER_MODULE_NAME);
	}

	/**
	 * Retrieve the ScheduleManagerService instance.
	 */
	public ScheduleManagerService getScheduleManagerService() {
		logger.info("Value of scheduleManagerService from insdie get :"
				+ scheduleManagerService.getClass());
		return scheduleManagerService;
	}


}
