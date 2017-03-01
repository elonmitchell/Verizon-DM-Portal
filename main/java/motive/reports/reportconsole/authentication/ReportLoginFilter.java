package motive.reports.reportconsole.authentication;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;



public class ReportLoginFilter implements Filter {

	private static Logger log = null; 

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse response,
			FilterChain next) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpSession session = request.getSession(false);

		String user = null;
		
		List<String> roles = null;
		if(session != null) {
			user = (String) session.getAttribute("userName");
			
			roles = (List<String>) session.getAttribute("UserRoles");
			if(user != null && roles != null)
				request = new UserRoleRequestWrapper(user,roles, request);
		}
		
		next.doFilter(request, response);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		log = Logger.getLogger(ReportLoginFilter.class);

	}

	protected static class UserRoleRequestWrapper extends HttpServletRequestWrapper {

		String user = null;  
		List<String> roles = null;  
		HttpServletRequest realRequest;

		public UserRoleRequestWrapper(String user, List<String> roles, HttpServletRequest request) {  
			super(request);  
			this.user = user;  
			this.roles = roles;  
			this.realRequest = request;  

		}
		
		@Override
		public boolean isUserInRole(String theRole){
			
			if(theRole != null && roles != null && roles.contains(theRole))
				return true; 
			else
				return false;
		}
		
		@Override  
		public Principal getUserPrincipal() {  
			if (this.user == null) {  
				return realRequest.getUserPrincipal();  
			}

			// make an anonymous implementation to just return our user  
			return new Principal() {  
				@Override
				public String toString() {					
					return getName();
				}
				
				@Override  
				public String getName() {       
					return user;  
				}
				
			};
		}

	}

}
