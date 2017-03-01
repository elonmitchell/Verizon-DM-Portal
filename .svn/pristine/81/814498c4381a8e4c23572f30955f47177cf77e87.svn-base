/**
 * 
 */
package motive.reports.reportconsole.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import javax.crypto.Cipher;

import org.apache.log4j.Logger;


/**
 * @author arponnus
 *
 */
public class LoginCipherHelper {
	
	private String dataPassed = null;
	
	private String userName = null;
	
	private String timeStamp = null;
	
	private static Logger log = Logger.getLogger(LoginCipherHelper.class);
	
	private final String SESSION_INTERVAL = "session_interval";
	
	/**
	 * 
	 */
	public LoginCipherHelper() {
		
	}
	
	/**
	 * @param dataPassed
	 * @throws UnsupportedEncodingException 
	 */
	public LoginCipherHelper(String dataPassed) throws UnsupportedEncodingException {
		this.dataPassed = decodeUserName(dataPassed);
		parseDataPassed();		
	}

	private String decodeUserName(String dataPassed) throws UnsupportedEncodingException {
		if(dataPassed != null){
			String urlDecodedData = URLDecoder.decode(dataPassed, "UTF-8");
			return dataPassed.replaceAll(" ", "+");
		}
		return null;
	}
	
	private void parseDataPassed(){
		
		LoginCipher lc = new LoginCipherImpl(Cipher.DECRYPT_MODE, this.dataPassed);
		String finalString = lc.decrypt();
		String dataArry[] = null;
		if(finalString != null && finalString.contains("#")){
			dataArry = finalString.split("#");
		} else {
			log.error("Decryption Failed.");
		}
		log.debug("userName " + dataArry[1]);
		if(dataArry.length == 2) {
			this.userName = dataArry[0];
			this.timeStamp = dataArry[1];
		} else {
			log.error("dataArry length is less" + dataArry.length);
		}
	}
	
	public String getUserName(){
		return userName;
	}
	
	public String getTimeStamp(){
		return timeStamp;
	}
	
	@SuppressWarnings("deprecation")
	public boolean isRequestValid(){
		ReportSystemPropertyAdapter prop = ReportSystemPropertyAdapter.getInstance();
		long sessionInterval = 0; 
		boolean withinRange = false;
		Date currentDate = new Date();
		Date tokenReceivedDate = new Date(timeStamp);
		long millis = Math.abs(currentDate.getTime() - tokenReceivedDate.getTime());
		if(prop.getPropValue(SESSION_INTERVAL)!= null && prop.getPropValue(SESSION_INTERVAL).length() > 0)
			sessionInterval = Long.parseLong(prop.getPropValue(SESSION_INTERVAL));
		else 
			sessionInterval = 30000; // default it to 30 sec.
		
		log.debug("CurrentDate : " + currentDate.toLocaleString());
		log.debug("Received Date : " + tokenReceivedDate.toLocaleString());
		log.debug("Millis: " + millis);
		
	  //1000ms * 60s * 2m
	    if (millis <= (1000 * 60 * 1)) {
	        withinRange = true;
	    }else {
	    	log.error("Token exceeds the time limit.");
	    }
		
		return withinRange;
	}
	
	
}
