package motive.reports.reportconsole.authentication;

import motive.exceptions.ProviderException;
//import motive.reports.reportconsole.ServiceUtil;
//import motive.reports.service.security.ReportSecurityService;
public class AuthenticationUtils {
	
	public static final String LOGIN_STATE_NAME = "LOGIN_STATE";
	public static final String LOGIN_STATE_PASS = "PASS";
	public static final String LOGIN_STATE_FIRST_LOGIN = "FIRST";
	public static final String LOGIN_STATE_RESET_BY_ADMIN = "RESET";
	public static final String LOGIN_STATE_EXPIRED = "EXPIRED";
    public static final String CURRENT_PRINCIPAL = "CURRENT_PRINCIPAL";
	
	public static final String ORIGINAL_REQUEST_URI = "URI";
	
	public static Long getUserState(String userName) throws ProviderException {
		
		try {
			//ReportSecurityService authenticatorSecurityService = ServiceUtil.getInstance().getAuthenticatorSecurityService();
		//	Long userState = authenticatorSecurityService.getUserState(userName);
			Long userState = new Long(0);
			return userState;
		} /*catch( ProviderException e ) {
			throw new ProviderException(e.getMessage());
		}*/ catch( Exception e ) {
			throw new ProviderException(e);
		}
	}
	
	/**
	 * Check all SecurityProviderMBeans
	 * If there is one that implements the method changeUserPasswordFromGUI, this method is called
	 * If there is none, an Exception is thrown
	 * 
	 * @param user
	 * @param oldPassword
	 * @param newPassword
	 * @param confirmPassword
	 * @throws Exception
	 */
	public static void changeUserPassword(String user, String oldPassword, String newPassword, String confirmPassword) throws ProviderException {		
		/*try {
			ReportSecurityService authenticatorSecurityService = ServiceUtil.getInstance().getAuthenticatorSecurityService();
			authenticatorSecurityService.changeUserPassword(user, oldPassword, newPassword, confirmPassword);
		} catch( ProviderException e ) {
			throw new ProviderException(e.getMessage());
		} catch( Exception e ) {
			throw new ProviderException(e);
		}*/
	}
	
}
