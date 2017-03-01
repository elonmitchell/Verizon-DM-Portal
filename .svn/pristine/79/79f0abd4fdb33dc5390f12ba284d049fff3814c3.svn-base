

package motive.reports.reportconsole.authentication;

import java.io.IOException;
import java.net.InetAddress;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import motive.exceptions.InvalidArgumentException;
import motive.reports.reportconsole.util.ReportSystemPropertyAdapter;

import org.apache.log4j.Logger;

/**
 * Accept or deny a request based on the IP address of the client who made the
 * request.  JDK 1.4 or higher is required.
 *
 * This filter is configured by setting the "allow" and/or "deny" properties to a comma-delimited list of regular expressions 
 * (in the syntax supported by the java.util.regex package) to which the client IP address will be compared. 
 *
 * <filter>
 *    <filter-name>RemoteHostFilter</filter-name>
 *    <filter-class>org.jboss.remotehostfilter.RemoteHostFilter</filter-class>
 *    <init-param>        
 *       <param-name>deny</param-name>
 *       <param-value>128.0.*,192.4.5.7</param-value>
 *    </init-param>
 *    <init-param>        
 *       <param-name>allow</param-name>
 *       <param-value>192.4.5.6,127.0.0.*</param-value>
 *    </init-param>
 * </filter>
 *
 * Evaluation proceeds as follows: 
 *
 * If there are any deny expressions configured, the IP will be compared to each expression. If a match is found, this request will be rejected with a "Forbidden" HTTP response. 
 * If there are any allow expressions configured, the IP will be compared to each such expression. If a match is NOT found, this request will be rejected with a "Forbidden" HTTP response. 
 * Otherwise, the request will continue normally. 
 *
 * @author  Stan Silvert
 */

public class RemoteHostFilter implements Filter {
    private static final Logger LOG = Logger.getLogger(RemoteHostFilter.class);
    
    private String[] allow;
    private String[] deny;
    
    private FilterConfig filterConfig = null;
    
    public RemoteHostFilter() {
    }
    
    /**
     *
     * @param request The servlet request we are processing
     * @param result The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, 
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        String clientAddr = request.getRemoteAddr();
        
        LOG.debug("Client addres is: " + clientAddr);
        
        if (hasMatch(clientAddr, deny)) {
            handleInvalidAccess(request, response, clientAddr);
            return;
        }
        
        if ((allow.length > 0) && !hasMatch(clientAddr, allow)) {
            handleInvalidAccess(request, response, clientAddr);
            return;
        }
        
        chain.doFilter(request, response);
    }
    
    private void handleInvalidAccess(ServletRequest request, 
                                     ServletResponse response, 
                                     String clientAddr) throws IOException {
        String url = ((HttpServletRequest)request).getRequestURL().toString();
        LOG.info("Invalid access attempt to " + url + " from " + clientAddr);
        ((HttpServletResponse)response).sendError(HttpServletResponse.SC_FORBIDDEN);
    }
    
    private boolean hasMatch(String clientAddr, String[] regExps) {
        for (int i=0; i < regExps.length; i++) {
            if (clientAddr.matches(regExps[i])) return true;
        }
        
        return false;
    }
    
    /**
     * Destroy method for this filter
     *
     */
    public void destroy() {
        this.filterConfig = null;
        this.allow = null;
        this.deny = null;
    }
    
    
    /**
     * Init method for this filter
     *
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        ReportSystemPropertyAdapter prop = ReportSystemPropertyAdapter.getInstance();
        this.allow = extractRegExps(prop.getPropValue("allow"));
		this.deny = extractRegExps(prop.getPropValue("deny"));
		
        /*this.allow = extractRegExps(filterConfig.getInitParameter("allow"));
        this.deny = extractRegExps(filterConfig.getInitParameter("deny"));*/
    }
    
    private String[] extractRegExps(String initParam) {
        if (initParam == null) {
            return new String[0];
        } else {
            return initParam.split(",");
        }
    }
        
    /**
     * Return a String representation of this object.
     */
    public String toString() {
        if (filterConfig == null) return ("ClientAddrFilter()");
        StringBuffer sb = new StringBuffer("ClientAddrFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

}