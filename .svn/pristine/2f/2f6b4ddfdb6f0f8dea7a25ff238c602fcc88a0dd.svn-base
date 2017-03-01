/**
 * 
 */
package motive.reports.reportconsole.actions;

import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author arponnus
 *
 */
public abstract class JSONBaseAction extends BaseAction 
{
	private static Logger log = Logger.getLogger(JSONBaseAction.class);
	
	protected final static String SUCCESS = "200";

	protected final static String SUCCESS_MSG = "SUCCESS";
	
	protected static final String RESPONSE = "response";
	
	protected static final String STATUS_MESSAGE = "statusMessage";
	
	protected static final String STATUS_CODE = "statusCode";
	
	protected static final String EXCEPTION_STATUS_CODE = "106";
	
	protected static final String EXCEPTION = "401";
	
	protected static final String SERVER_EXCEPTION_MSG = "Internal server error.";

	/* (non-Javadoc)
	 * @see motive.reports.reportconsole.actions.BaseAction#executeAction(
	 * org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm,
	 *  javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

        JSONObject retObject = null;
        String user = null;
        try
        {
        	user = (String) request.getSession().getAttribute("userName");
            log.info("User Id: " + request.getRemoteUser());
            if (user == null || !request.isRequestedSessionIdValid())
            {
                log.error("Session Invalid .. route to login ");
                return mapping.findForward("login");
            }
           
            log.info("AJAX call succeeded");
            retObject = getJSON(request, response);
            if (retObject.get(RESPONSE) instanceof JSONObject)
            {
                JSONObject result = retObject.getJSONObject(RESPONSE);
                String statusMessage = null;
                if (result.has(STATUS_MESSAGE))
                {
                    statusMessage = result.getString(STATUS_MESSAGE);
                }
                else
                {
                    statusMessage = retObject.getString(STATUS_MESSAGE);
                }
                log.info("statusMessage in JSONBaseAction");
                log.info("Request Logging" + retObject.getJSONObject(RESPONSE).getString(STATUS_CODE)
                        + "statusMessaage" + statusMessage);
            }
        }catch (Exception e)
        {
            log.error(e);
            e.printStackTrace();
            retObject = createJSONStatusObject(EXCEPTION_STATUS_CODE, e.getMessage());
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print(retObject.toString());
        log.debug(retObject.toString());
        writer.flush();
        writer.close();

        return null;
    }
	
	/**
	 *  
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 * @throws NamingException 
	 * @throws CreateException 
	 */
	protected abstract JSONObject getJSON(HttpServletRequest request, HttpServletResponse response)
    throws JSONException, CreateException, NamingException;
	
	protected JSONObject createJSONStatusObject(String statusCode, String statusMessage) throws JSONException
    {
        JSONObject object = new JSONObject();
        object.put(STATUS_CODE, statusCode);
        object.put(STATUS_MESSAGE, statusMessage);

        return object;
    }
}
