package motive.reports.reportconsole.authentication;

import java.io.IOException;
import java.security.Principal;
//import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
//import java.util.List;
import java.util.StringTokenizer;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import motive.reports.security.BasicPrincipal;
import motive.reports.security.ReportCallbackHandler;
import motive.reports.security.UserGroup;

import org.apache.log4j.Logger;

import com.sun.jersey.core.util.Base64;

public class RestAuthenticationService 
{
	
	private static final Logger logger = Logger.getLogger(RestAuthenticationService.class); 
	
	public boolean authenticate(String authCredentials, String authorizedRol) 
	{
		LoginContext loginContext = null;
		//List<String> usrRolesLst = new ArrayList<String>();
		boolean authenticationStatus = false;
		logger.info("[report-console]:::: authCredentials :: "+ authCredentials);

		if (null == authCredentials) 
		{
			logger.info("[report-console]:::: authCredentials is null");
			return false;
		}
		if (null == authorizedRol) {
			logger.info("[report-console]:::: authorizedRol is null");
			return false;
		}
		
		// header value format will be "Basic encodedstring" for Basic
		// authentication. Example "Basic YWRtaW46YWRtaW4="
		final String encodedUserPassword = authCredentials.replaceFirst("Basic"
				+ " ", "");
		logger.info("[report-console]:::: encodedUserPassword :: " + encodedUserPassword);
		String usernameAndPassword = null;
		try 
		{
			byte[] decodedBytes = Base64.decode(encodedUserPassword);
			usernameAndPassword = new String(decodedBytes, "UTF-8");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			
		}
		logger.info("[report-console]:::: usernameAndPassword :: " + usernameAndPassword);
		final StringTokenizer tokenizer = new StringTokenizer(
				usernameAndPassword, ":");
		final String username = tokenizer.nextToken();
		final String password = tokenizer.nextToken();
		logger.info("[report-console]:::: username :: " + username);
		logger.info("[report-console]:::: password :: " + password);

		// LDAP validation
		try 
		{
			loginContext = new LoginContext("dm-reporting",
					new ReportCallbackHandler(username, password, "reportservice"));
			loginContext.login();
	
			// let's see what Principals we have
			Iterator principalIterator = loginContext.getSubject().getPrincipals()
					.iterator();
			logger.info("[report-console]:::: Authenticated user has the following Principals :: ");
			UserGroup usrGrp = null;
			while (principalIterator.hasNext()) 
			{
				logger.info("[report-console]:::: usrGrp :: " + usrGrp);
				usrGrp = (UserGroup) principalIterator.next();
				if (usrGrp != null) 
				{
					Enumeration usrValue = usrGrp.members();
					logger.info("[report-console]:::: Authenticated user has the following Principals :: ");
					while (usrValue.hasMoreElements()) 
					{
						Principal bsicPc = (BasicPrincipal) usrValue.nextElement();
						String roles = bsicPc.getName();
						//usrRolesLst.add(roles);
						logger.info("[report-console]:::: Roles :: " + roles);
						
						if (authorizedRol.equalsIgnoreCase(roles)) 
						{
							authenticationStatus = true;
							break;
						}
					}
				}
				if (authenticationStatus) 
				{
					break;
				}
			}
				
			return authenticationStatus;
		}
		catch (LoginException e) 
		{
			  e.printStackTrace();
			  logger.error("KEY_FOR_LOGIN_FAILURE");
			  return false;
		}
	}

}
