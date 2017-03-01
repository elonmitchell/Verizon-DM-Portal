package motive.reports.reportconsole.actions;

import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import motive.exceptions.ProviderException;
//import motive.managementconsole.LicenseNotFoundException;
//import motive.managementconsole.LicenseWarningException;
//import motive.managementconsole.LicensingUtil;
import motive.reports.reportconsole.DisplayableException;
import motive.reports.reportconsole.ServiceUtil;
import motive.reports.reportconsole.UserContext;
import motive.reports.reportconsole.license.LicenseHelper;
import motive.trace.TraceLogger;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

//import com.alcatel.slim.LicenseException;
//import com.alcatel.slim.MalformedLicenseException;

//import com.alcatel.service.licensing.Feature;
//import com.alcatel.service.licensing.FeatureInactiveException;
//import com.alcatel.service.licensing.HostBoundFeature;
//import com.alcatel.service.licensing.MalformedLicenseException;
//import com.alcatel.service.licensing.TemporaryFeature;
//import motive.reports.service.security.ReportSecurityUtil;
/**
 * BaseAction.java
 * 
 * CVS: $Id: BaseAction.java,v 1.5 2013/12/26 23:43:50 balraj Exp $
 * 
 * @author kmckenzi
 * @version $Revision: 1.5 $
 * 
 */
public abstract class BaseAction extends Action
{
    /** logger */
    private static final Logger logger = TraceLogger.getLogger(BaseAction.class);

    /** default string for error forward mapping */
    private String errorActionForwardString;

    /** Default string for error handling */
    private String defaultErrorMessage;

    /** Reference to action errors for all sub-classes */
    protected ActionErrors errors;

    /** Reference to services for all sub-classes */
//    protected static ServiceUtil serviceUtil;

    /** Maximum string length for exception messages */
    private static final int MAX_EXCEPTION_LENGTH = 255;

    /** Default error string */
    private static final String DEFAULT_ERROR_STRING = "unexpected_error";

    /** String constant for error forwards */
    public static final String ERROR_FORWARD_STRING = "error";

    /** String constant for success(ok) forwards */
    public static final String SUCCESS_FORWARD_STRING = "success";

    /** String constant for license failure forward */
    public static final String LIC_FAILURE_FORWARD_STRING = "licenseFailure";
    
    /** String constant for password changeable setting */
    public static final String PASSWORD_CHANGE_SUPPORTED_STRING = "passwordChangeable";

    /**
     * This method overrides all execute methods for it's sub-classes so that we
     * can do some common processing for all requests. Note that any class that
     * extends this class must implement an executeAction method.
     * 
     * License checking is done here as well. If a licensing exception is
     * caught, the action is forwarded to /licenseError.vm and the appropriate
     * actions errors are set on the request to be displayed in the page.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception
    {
        /*errors = new ActionErrors();
        errorActionForwardString = ERROR_FORWARD_STRING;
        defaultErrorMessage = DEFAULT_ERROR_STRING;
        
        LicenseHelper licenseHelper = null;
        boolean licenseWarning = false;
        boolean licenseWarningHostIDs = false;  
        
        try {        
        	licenseHelper = LicenseHelper.getInstance();
        }
        catch (Exception e){
        	logger.error("Licensing exception has occurred: " + e);
        }        
               
        request.getSession().setAttribute(PASSWORD_CHANGE_SUPPORTED_STRING, new Boolean(ReportSecurityUtil.getInstance().isCompliantUserAuthenticatorAvailable()));

        // set global variables for this request
        ActionForward forward = null;

        try
        {
        	// check licensing before all processing, we have to handle the
            // warning as s special case because the actions need to continue as
            // usual until the license actually expires
            
            if (!UserContext.isFeatureChecked(request, LicenseHelper.FEATURE_HOSTIDS))
            {
            	licenseWarningHostIDs = licenseHelper.checkLicensing(request, errors, LicenseHelper.FEATURE_HOSTIDS);
            }           
            
            if (errors.size() == 0 || licenseWarningHostIDs)
            {
                // this will check the license for the sub components eg. reporting
                licenseWarning = checkFeatureLicense(request, errors); 
                if (errors.size() == 0 || licenseWarning)
                {
                    forward = executeAction(mapping, form, request, response);
                }
            }
            else
            {                       
                setErrorActionForwardString(LIC_FAILURE_FORWARD_STRING);
            }
            UserContext.clearFeatureCheckList(request);
        }        
        catch (ProviderException e)
        {
            errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage(getDefaultErrorMessage()));
            logger.error("Error executing " + this.getClass().toString(), e);
            e.printStackTrace();
        }
        finally
        {
            if (errors.size() != 0)
            {
                request.setAttribute(Globals.ERROR_KEY, errors);
                handleErrors(request, errors);
                if (!licenseWarningHostIDs && !licenseWarning)
                {
                    forward = mapping.findForward(getErrorActionForwardString());
                }
            }           
        }   
        
        return (forward == null) ? mapping.findForward(SUCCESS_FORWARD_STRING) : forward;*/
    	this.errors = new ActionErrors();
        this.errorActionForwardString = "error";
        this.defaultErrorMessage = "unexpected_error";

        request.getSession().setAttribute("passwordChangeable", false);

        ActionForward forward = null;

        forward = executeAction(mapping, form, request, response);

        return forward == null ? mapping.findForward(SUCCESS_FORWARD_STRING) : forward;
    }

    /**
     *  Check for reporting console license here
     * @throws LicenseException 
     * @throws MalformedLicenseException 
     */
    public boolean checkFeatureLicense(HttpServletRequest request, ActionErrors licenseErrors) {       
        boolean licenseWarning = false;
        LicenseHelper licenseHelper = null;
        
        // check licensing before all processing, we have to handle the
        // warning as s special case because the actions need to continue as
        // usual until the license actually expires
        
        try {        
        	licenseHelper = LicenseHelper.getInstance();
        }
        catch (Exception e){
        	logger.error("Licensing exception has occurred: " + e);
        }   
        
        if (!UserContext.isFeatureChecked(request, LicenseHelper.FEATURE_REPORTING_CONSOLE))
        {
        	licenseWarning = licenseHelper.checkLicensing(request, licenseErrors, LicenseHelper.FEATURE_REPORTING_CONSOLE);
            if (licenseErrors.size() != 0 && !licenseWarning)            
            {                 
                setErrorActionForwardString(LIC_FAILURE_FORWARD_STRING);
            } 
        }
        return licenseWarning;
    }        
    
    /**
     * Looks up the app.name message property
     * 
     * @return
     */
    protected String getApplicationName(HttpServletRequest request)
    {
        Locale locale = ActionUtils.getLocale(request);
        MessageResources messages = ActionUtils.getMessageResources(request);
        return messages.getMessage(locale, "app.name");
    }

    /**
     * Each action that extends BaseAction will override this function which
     * will get called from the execute function of the BaseAction
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public abstract ActionForward executeAction(ActionMapping mapping,
                                                ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws Exception;

    /**
     * Return the string for forward mapping for error
     * 
     * @return
     */
    public String getErrorActionForwardString()
    {
        return errorActionForwardString;
    }

    /**
     * @param errorActionForwardString
     */
    public void setErrorActionForwardString(String errorActionForwardString)
    {
        this.errorActionForwardString = errorActionForwardString;
    }

    /**
     * @return
     */
    public String getDefaultErrorMessage()
    {
        return defaultErrorMessage;
    }

    /**
     * @param defaultErrorMessage
     */
    public void setDefaultErrorMessage(String defaultErrorMessage)
    {
        this.defaultErrorMessage = defaultErrorMessage;
    }

    protected void prepareHandleError()
    {
    }

    /**
     * @param mapping
     * @param request
     * @param response
     * @param forwardName
     * @param params
     * @return ActionForward object with the parameters specified in the map
     * @throws IOException
     */
    protected ActionForward forwardWithParams(ActionMapping mapping,
                                              HttpServletRequest request,
                                              HttpServletResponse response,
                                              String forwardName,
                                              Map params) throws IOException
    {
        ActionForward forward = mapping.findForward(forwardName);
        if (forward.getRedirect())
        {
            String forwardPath = forward.getPath();
            String uri = null;

            if (forwardPath.startsWith("/"))
                uri = RequestUtils.forwardURL(request, forward);
            else
                uri = forwardPath;

            if (uri.startsWith("/"))
                uri = request.getContextPath() + uri;

            boolean first = true;
            Iterator i = params.entrySet().iterator();
            while (i.hasNext())
            {
                Map.Entry entry = (Map.Entry) i.next();
                if (first)
                {
                    uri += '?';
                    first = false;
                }
                else
                {
                    uri += '&';
                }

                uri += entry.getKey().toString() + '=' + entry.getValue().toString();
            }

            response.sendRedirect(response.encodeRedirectURL(uri));

            return (null);
        }
        return forward;
    }

    /**
     * Looks for an object in the request parameters and attributes for an
     * object referenced by the specified key
     * 
     * @param request
     * @param name
     * @return object in the request stored by the specified key
     */
    protected Object findObject(HttpServletRequest request, String name)
    {
        Object answer = request.getParameter(name);
        if (answer == null)
            answer = request.getAttribute(name);
        return answer;
    }

    /**
     * Looks for an object in the request parameters and attributes for an
     * object referenced by the specified key and converts to string
     * 
     * @param request
     * @param name
     * @return string in the request stored by the specified key
     */
    protected String findString(HttpServletRequest request, String name)
    {
        Object answer = findObject(request, name);
        if (answer != null)
            answer = answer.toString();
        return (String) answer;
    }

    /**
     * Looks for an object in the request parameters and attributes for an
     * object referenced by the specified key and converts to boolean
     * 
     * @param request
     * @param name
     * @return boolean in the request stored by the specified key
     */
    protected boolean findBoolean(HttpServletRequest request, String name)
    {
        Object o = findObject(request, name);
        if (o != null)
        {
            if (o instanceof Boolean)
                return ((Boolean) o).booleanValue();
            String s = o.toString();
            return (s != null && (s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("true")));
        }
        return false;
    }

    /**
     * Takes the specified error and places it into the request to be retrieved
     * by the errorDialog
     * 
     * @param request
     * @param errors
     */
    protected void handleErrors(HttpServletRequest request, ActionErrors errors)
    {
        HttpSession session = request.getSession();

        session.setAttribute("report.baseaction.action.errors", errors);
        session.setAttribute("report.baseaction.message", "exception.general.message");
        session.setAttribute("report.baseaction.request.uri", request.getRequestURI());
        session.setAttribute(Globals.ERROR_KEY, errors);
    }

    /**
     * Takes the specified error and places it into the request to be retrieved
     * by the errorDialog
     * 
     * @param request
     * @param e
     */
    protected void handleErrors(HttpServletRequest request, Exception e)
    {
        MessageResources messages = getResources(request);
        DisplayableException dex = new DisplayableException("exception.general.message", e);
        Throwable cause = dex.getCause();

        HttpSession session = request.getSession();

        // set the exception attribute
        if (cause.getMessage() != null && messages.isPresent(cause.getMessage()))
        {
            String cClazz = cause.getClass().getName();
            String cMessage = messages.getMessage(cause.getMessage());
            session.setAttribute("report.baseaction.exception", cClazz + ": " + cMessage);
        }
        else
        {
            String exString = dex.getCause().toString();
            if (exString.length() > MAX_EXCEPTION_LENGTH)
            {
                exString = exString.substring(0, MAX_EXCEPTION_LENGTH) + "...";
            }
            session.setAttribute("report.baseaction.exception", exString);
        }

        // set the message attribute
        if (cause instanceof ProviderException)
        {
            session.setAttribute("report.baseaction.message", "exception.message");
        }
        else if (cause instanceof NamingException)
        {
            session.setAttribute("report.baseaction.message", "exception.message");
        }
        else
        {
            String msg = (dex.getMessage() != null) ? dex.getMessage() : "exception.message";
            session.setAttribute("report.baseaction.message", msg);
        }

        // set the stack trace attribute
        if (cause.getStackTrace().length > 0)
        {
            session.setAttribute("report.baseaction.stack", cause.getStackTrace());
            cause.printStackTrace();
        }

        // set the request url attribute
        session.setAttribute("report.baseaction.request.uri", request.getRequestURI());

        // log the real cause of the exception so the DisplayableException
        // doesn't show in the log
        logger.error("Logging error:", cause);
    }

    protected String getMessage(String key, HttpServletRequest request)
	{
        Locale locale = ActionUtils.getLocale(request);
        MessageResources messages = ActionUtils.getMessageResources(request);
        return messages.getMessage(locale, key);
	}
	
    protected String getMessage(String key, HttpServletRequest request, Object[] msgArgs)
	{
        Locale locale = ActionUtils.getLocale(request);
        MessageResources messages = ActionUtils.getMessageResources(request);
        return messages.getMessage(locale, key, msgArgs);
	}

}
