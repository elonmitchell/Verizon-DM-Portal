/**
 * 
 */
package motive.reports.reportconsole.util;

import java.util.HashMap;

import motive.exceptions.InvalidArgumentException;
import motive.reports.reportconsole.ServiceUtil;
import motive.reports.service.reportmanager.ReportManagerService;

import org.apache.log4j.Logger;
import org.apache.velocity.runtime.resource.ResourceManager;


/**
 * @author arponnus
 *
 */
public class ReportSystemPropertyAdapter {
	
	private static Logger log = Logger.getLogger(ReportSystemPropertyAdapter.class);
	
	private  HashMap<String, String> propertyMap = null; 
	
	private ResourceManager rsm = null;
	
	private static ReportSystemPropertyAdapter  adapter = null;
	
	private ReportSystemPropertyAdapter()
	{		
		 if(loadProperty()) {
			 log.info("Property loaded successfully.");
		 } else {
			 log.error("Property loading failed.");
		 }
	}
	
	public static ReportSystemPropertyAdapter getInstance(){
		
		if(adapter == null) {
			adapter = new ReportSystemPropertyAdapter();
		} 
		return adapter;
	}
	
	public HashMap<String, String> getSystemProperty(){
		return propertyMap;
	}
	
	private boolean loadProperty(){
		try{
			ReportManagerService rms = ServiceUtil.getInstance().getReportManagerService();
			propertyMap = rms.getSystemProperties();
		}catch(Exception e){
			log.error(e);
			return false;
		}
		return true;
	}
	
	public String getPropValue(String key) {
		String value = null;
		
		if(key == null || key.length() < 0) {		
			log.error("The System property for " + key + " not configured in Environment properties. Please configure "
                    + key );
		} else {			
			if(propertyMap != null ){
				value = propertyMap.get(key);
				if(value != null && value.length() > 0) {
					log.info("System Property value for Key " + key + " is " + value);
				}else {
					log.error("The system property value for key " + key + " is not configured. Please configure "
                            + key + "correctly." );
				}
			}			
		}		
		return value;
	}

}
