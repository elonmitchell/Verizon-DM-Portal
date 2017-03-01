package motive.reports.reportconsole.authentication;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class RestAuthenticationFilter implements Filter 
{
	
	private static Logger logger;
	public static final String AUTHENTICATION_HEADER = "Authorization";

	@Override
	public void destroy() 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filter) throws IOException, ServletException 
	{
		logger.info("[report-console]:::: request :: " + request);
		if (request instanceof HttpServletRequest) 
		{
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			String authCredentials = httpServletRequest
					.getHeader(AUTHENTICATION_HEADER);
			logger.info("[report-console]:::: authCredentials :: " + authCredentials);
			
			// retrieve the rol authorized to generate schedules
			String authorizedUserRol = ((HttpServletRequest) request)
					.getSession().getServletContext()
					.getInitParameter("RestAuthentication");
			logger.info("[report-console]:::: authorizedUserRol :: "
					+ authorizedUserRol);
			
			// Call service to validate credentials on ldap
			RestAuthenticationService authenticationService = new RestAuthenticationService();
			boolean authenticationStatus = authenticationService
					.authenticate(authCredentials, authorizedUserRol);
			
			logger.info("[report-console]:::: authenticationStatus :: " + authenticationStatus);
			
			if (authenticationStatus) 
			{
				filter.doFilter(request, response);
			} 
			else 
			{
				if (response instanceof HttpServletResponse) 
				{
					HttpServletResponse httpServletResponse = (HttpServletResponse) response;
					httpServletResponse
							.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				}
			}
			
			logger.info("[report-service]:::: AuthenticationFilter :::: done");
		}
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException 
	{
		logger = Logger.getLogger(RestAuthenticationFilter.class);
	}

}
