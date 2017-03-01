package motive.reports.reportconsole.authentication;

import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import motive.trace.TraceLogger;

import org.apache.log4j.Logger;

//import motive.reports.service.security.ReportSecurityUtil;

/**
 * @author kmckenzi
 *
 */
public class AuthenticationServletFilter implements Filter {
	private static final Logger logger = TraceLogger.getLogger(AuthenticationServletFilter.class);
	protected FilterConfig filterConfig;
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}
	
	public void destroy() {
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws java.io.IOException, ServletException   {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;	
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String requestURI = httpServletRequest.getRequestURI();
		Principal principal = httpServletRequest.getUserPrincipal();
        String cachedUserName = null;
		
		// If there's no compliant authenticator available, just continue with the request
		/*if( !ReportSecurityUtil.getInstance().isCompliantUserAuthenticatorAvailable()) {
			chain.doFilter(request, response);
			return;	
        }*/
		
        logger.debug("request = " + requestURI);
		if( principal != null && httpServletRequest.getSession(false) != null) { // The user has a session		
            logger.debug("principal = " + principal.getName());
            
            Long loginState = (Long)httpServletRequest.getSession().getAttribute(AuthenticationUtils.LOGIN_STATE_NAME);
			logger.debug("loginState from session = " + loginState);
            
            cachedUserName = (String) httpServletRequest.getSession().getAttribute("CACHED_USER_NAME");
            if (cachedUserName == null || !principal.getName().equals(cachedUserName)) 
            {
                loginState = null;
            }
            
			if( loginState == null ) { // No login state, the user hasn't been checked
				try {
					loginState = AuthenticationUtils.getUserState(principal.getName());
                    logger.debug("AuthenticationUtils.getUserState() = " + loginState);
				} catch (Exception e) {
					logger.error("Could not get the user's login state", e);
				}
                
                httpServletRequest.getSession(true).setAttribute("CACHED_USER_NAME", principal.getName());
				httpServletRequest.getSession(true).setAttribute(AuthenticationUtils.ORIGINAL_REQUEST_URI, requestURI);
				httpServletRequest.getSession(true).setAttribute(AuthenticationUtils.LOGIN_STATE_NAME, loginState);
			}
			
			if(( loginState != null ) && (loginState.longValue() != 0 )) { // 
				/**
				 * The user has a state which requires a change of the password.
				 * This is a condition were certain resources need to be filtered from the request so the
				 * page will display and function correctly.  
				 */
				boolean allowed = false;
				String[] exclusions = new String[]{"/images","/css","/js","/updatepw","/changePassword","/empty","/help","/login","/loginError","/index.html"};			
				for (int i = 0; i<exclusions.length; i++) {
					if (requestURI.indexOf(exclusions[i]) > 0) {
						allowed = true;
						break;
					}
				}
				
				/**
				 * If the filter condition is not found in the request, the user should be redirected
				 * to the change password page.  Otherwise, let the request continue.
				 */
				if (!allowed) {
					String redirectURI = ((HttpServletRequest)request).getContextPath() + "/changePassword.vm";
					logger.debug("Redirecting to: " + redirectURI);
					httpServletResponse.sendRedirect(redirectURI);
				}
                else if (allowed && requestURI.indexOf("/loginError") > 0) //defect 51711: user is unnecessarily asked to change password
                {
                    httpServletRequest.getSession().removeAttribute(AuthenticationUtils.LOGIN_STATE_NAME);
                    chain.doFilter(request, response);
                }
				else {
					chain.doFilter(request, response);
				}

			} else {
				chain.doFilter(request, response);
			}
		} else {
			chain.doFilter(request, response);
		}
    }
}
