/*
 * Copyright 2005 by Motive, Inc. All rights reserved. This software is the
 * confidential and proprietary information of Motive, Inc. ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Motive.
 */

package motive.reports.reportconsole.tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import motive.trace.TraceLogger;
import motive.reports.reportconsole.util.URLEncodeCipher;
import motive.reports.service.reportmanager.Report;
import motive.reports.service.reportmanager.util.ReportURLEncoder;

import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.velocity.tools.view.context.ViewContext;

/**
 * A utility class to help with velocity views
 * 
 * @author kmckenzi
 * @version $Id: VelocityUtilityTool.java,v 1.12 2006/02/07 16:27:13 kmckenzi
 *          Exp $
 */
public class VelocityUtilityTool extends MotiveMessageTool
{
    private static Logger logger = TraceLogger.getLogger(VelocityUtilityTool.class);

    /** Reference to the HttpServletRequest */
    protected HttpServletRequest request;

    /** Reference to the HttpSesstion */
    protected HttpSession session;

    /**
     * Default constructor. Tool must be initialized before use.
     */
    public VelocityUtilityTool()
    {
    }

    /**
     * Initialize the tool. In this case, we need to setup the request and
     * session
     */
    public void init(Object initData)
    {
        super.init(initData);

        if (!(initData instanceof ViewContext))
        {
            throw new IllegalArgumentException("Tool can only be initialized with a ViewContext");
        }

        ViewContext context = (ViewContext) initData;
        this.request = context.getRequest();
        this.session = request.getSession();
    }

    /**
     * Returns the length of an array. We need this since we can't get
     * properties from velocity, only getters and setters
     * 
     * <pre>
     *                                              Example:
     *                                              #if ($util.size(someArray) &gt; 0) 
     *                                              ....
     *                                              #end
     *                                                                           
     *                                              #set ($size = $util.size(someArray))
     * </pre>
     * 
     * @param objectArray
     * @return an int representing the size of the array
     */
    public int size(Object[] objectArray)
    {
        return (objectArray != null) ? objectArray.length : 0;

    }

    /**
     * Returns true if the ArrayList contains the specified object.
     * 
     * @param arrayList
     * @param testObject
     * @return true if the testObject is contained within the arrayList
     */
    public boolean arrayListContains(ArrayList arrayList, Object testObject)
    {
        if (arrayList != null)
            return arrayList.contains(testObject);
        return false;
    }

    /**
     * Determines the class name to be returned to the template based on the
     * current position of the counter parameter
     * 
     * <pre>
     *                                              Example:
     *                                              #set ($rowClass = $util.alternateRows($velocityCount)
     *                                             
     * </pre>
     * 
     * @param counter - the current position of the table row counter which in
     *        most cases will be $velocitycount
     * @return the HTML class name for the indicated table row
     */
    public String alternateRows(int counter)
    {
        String className = "";
        if (counter % 2 == 0)
        {
            className = "even_table_row";            
        }
        else
        {
            className = "odd_table_row";
        }
        return className;
    }

    /**
     * Provides a means to easily set/default a velocity variable
     * 
     * @param value
     * @param defaultValue
     * @return a string which will be defaulted if the value is null or ""
     */
    public String setValue(String value, String defaultValue)
    {
        return setValue(value, defaultValue, true);
    }

    /**
     * Provides a means to easily set/default a velocity variable
     * 
     * @param value
     * @param defaultValue
     * @return a string which will be defaulted if the value is null or ""
     */
    public String setValue(String value, String defaultValue, boolean useDefaultValue)
    {
        return ((GenericValidator.isBlankOrNull(value) && useDefaultValue) ? defaultValue : value);

    }

    /**
     * Retrieves the error object from the request/session
     * 
     * @param errorKey
     * @return
     */
    public Object getError(String errorKey)
    {
        return this.session.getAttribute(errorKey);
    }

    /**
     * Removes error objects from the session once the error handler has been
     * called
     */
    public void cleanErrors()
    {
        this.session.removeAttribute(Globals.ERROR_KEY);
        this.session.removeAttribute("report.baseaction.action.errors");
        this.session.removeAttribute("report.baseaction.message");
        this.session.removeAttribute("report.baseaction.request.uri");
        this.session.removeAttribute("report.baseaction.exception");
        this.session.removeAttribute("report.baseaction.message");
        this.session.removeAttribute("report.baseaction.stack");
    }

    /**
     * Return true if there are errors in the session attribute
     * 
     * @return
     */
    public boolean hasErrors()
    {
        ActionErrors errors = (ActionErrors) this.session.getAttribute(Globals.ERROR_KEY);
        if (errors != null && !errors.isEmpty())
            return true;
        else
            return false;
    }



    /**
     * Escapes special characters for XML processing
     * 
     * @param value
     * @return
     */
    public String escape(String value)
    {
        if (value == null)
            return "";
        Map replacements = new HashMap();
        replacements.put("<", "&lt;");
        replacements.put(">", "&gt;");
        replacements.put("\"", "&quot;");
        replacements.put("&", "&amp;");
        replacements.put("'", "&apos;");

        Pattern p = Pattern.compile("[<>\"]|&(?!#)");
        Matcher m = p.matcher(value);
        StringBuffer buffer = new StringBuffer();

        logger.debug("Escaping string: " + value);
        while (m.find())
        {
            String specialChar = m.group();
            String replacementChar = (String) replacements.get(specialChar);
            m.appendReplacement(buffer, replacementChar);
            logger.debug("Replacing special char " + specialChar + " with " + replacementChar);
        }
        m.appendTail(buffer);
        return buffer.toString();
    }

    /**
     * @param text
     * @param href
     * @return
     * @throws Exception
     */
    public String trimmedAnchor(String text, String href) throws Exception
    {
        return trimmedAnchor(text, text, href, 30);
    }

    /**
     * @param text
     * @param title
     * @param href
     * @return
     * @throws Exception
     */
    public String trimmedAnchor(String text, String title, String href) throws Exception
    {
        return trimmedAnchor(text, title, href, 30);
    }

    /**
     * @param text
     * @param title
     * @param href
     * @param trimLength
     * @return
     */
    public String trimmedAnchor(String text, String title, String href, int trimLength) throws Exception
    {
        StringBuffer buffer = new StringBuffer("<a");
        String newText = null;

        // build the inner text, trim if necessary
        if (!GenericValidator.isBlankOrNull(text) && text.length() > trimLength)
        {
            newText = text.substring(0, trimLength) + "...";
            href = (GenericValidator.isBlankOrNull(href)) ? "#" : href;
        }
        else
        {
            newText = text;
        }

        // build the href attribute
        if (!GenericValidator.isBlankOrNull(href))
        {
            buffer.append(" href=\"");
            buffer.append(href);
            buffer.append("\"");
        }

        // build the title attribute, this is the tooltip
        if (!GenericValidator.isBlankOrNull(title))
        {
            buffer.append(" title=\"");
            buffer.append(escape(title));
            buffer.append("\"");
        }

        buffer.append(">");
        buffer.append(newText);
        buffer.append("</a>");

        return buffer.toString();
    }

    /**
     * Returns the system date using the format specified. This could easily
     * have been done in the velocity template itself, but here is is easier to
     * maintain since the date format string is externalized in the message
     * resources
     * 
     * @return A string represening the current date and time on the server
     */
    public String getServerDateAndTime()
    {
        String dateFormat = get("calendar.java.dateFormat");
        return getServerDateAndTime(dateFormat);
    }

    /**
     * Returns the system date using the format specified. This could easily
     * have been done in the velocity template itself, but here is is easier to
     * maintain since the date format string is externalized in the message
     * resources
     * 
     * @param dateFormat
     * @return A string represening the current date and time on the server
     */
    public String getServerDateAndTime(String dateFormat)
    {
        String formattedDate = null;
        if (GenericValidator.isBlankOrNull(dateFormat))
        {
            //logger.warn("Date format cannot be null in method getServerDate(). Please verify that the date format string is correct.");
            return null;
        }

        try
        {
            SimpleDateFormat f = new SimpleDateFormat(dateFormat);
            formattedDate = f.format(Calendar.getInstance().getTime());
        }
        catch (Exception e)
        {
            //logger.error("An error occurred while getting the server date and time.", e);
            formattedDate = null;
        }
        return formattedDate;
    }

 
    /**
     * 
     * @param aBoolean
     * @return
     */
    public String booleanAsYesNo(boolean aBoolean)
    {
        String msg = null;
        if (aBoolean == true)
        {
            msg = "true.yesno.value";
        }
        else
        {
            msg = "false.yesno.value";
        }
        return get(msg);
    }

    /**
     * 
     * @return
     */
    public String space()
    {
        return "&#160;";
    }
    
    /**
     * 
     * @param value
     * @return
     */
    public String nvl(String value)
    {
        return nvl(value, space());
    }

    /**
     * 
     * @param value
     * @param defaultValue
     * @return
     */
    public String nvl(String value, String defaultValue)
    {
        if (GenericValidator.isBlankOrNull(value))
        {
            return defaultValue;
        }
        return value;
    }
    
    /*
     * get logged in user name
     */
    public String getUserName()
    {
       	if(request.getUserPrincipal() != null)    	
       		return this.request.getUserPrincipal().toString();
       	else 
       		return "";
    }
    
    public String getEncryptedUserName()
    {
    	String userEncodedCrypted = "";
       	if(request.getUserPrincipal() != null) {
       		try {
       			//Date d = new Date();
       			//logger.info("*** encrypted time " + d + " ***");
       			//GregorianCalendar gc = new GregorianCalendar();
       			//gc.setTime(d);
       			//gc.set(GregorianCalendar.SECOND, 0);
       			//gc.set(GregorianCalendar.MILLISECOND, 0);
       			//Date d2 = gc.getTime();
       			//Long time = d2.getTime();
       			//logger.info("*** time " + time + " ***");
       			URLEncodeCipher URLcipher = new URLEncodeCipher(URLEncodeCipher.ENCRYPTION_KEY);
       			userEncodedCrypted = URLcipher.encrypt(this.request.getUserPrincipal().toString());
       		} catch(Exception e) {
       			logger.error("The user is not in session");
       			return "";
       		}
       		return userEncodedCrypted;
       	} else { 
       		return "";
       	}
    }
    
    
    /*
     * Returns formatted License Expiry Date
     */
    public String getLicenseExpiryDate()
    {
//        String dateFormat = get("calendar.java.dateFormat");
//        SimpleDateFormat f = new SimpleDateFormat(dateFormat);
//        Date expiryDate = UserContext.getLicenseExpiryDate(this.request);    
        String formattedDate = "";
//        if (expiryDate != null)
//            formattedDate = f.format(expiryDate);
        
        return formattedDate;
    }
    
    /*
     *  Returns the replaced string
     */
    public String replaceAll(String inputString, String regex, String replacement)
    {
        String result = "";
        result = inputString.replaceAll(regex, replacement);
        return result;
    }
 
    public Set getReportSets( Set fullReportSet )
    {
    	int listSize = fullReportSet.size();
    	Object[] reportArray = fullReportSet.toArray();
    	boolean isOdd = (listSize % 2) > 0;
    	HashSet returnSet = new HashSet(2);
    	HashSet firstHalf = new HashSet((listSize / 2));
    	HashSet secondHalf = new HashSet((listSize / 2));

    	for (int i = 0; i < (listSize / 2); i++ )
			firstHalf.add( reportArray[i] );

		if ( isOdd )
			firstHalf.add( reportArray[firstHalf.size()] );

		for (int i = firstHalf.size(); i < listSize; i++ )
			secondHalf.add( reportArray[i] );
    	returnSet.add( firstHalf );
    	returnSet.add( secondHalf );
    	return returnSet;
    }
    /**
     * Returns the system date using the format specified. This could easily
     * have been done in the velocity template itself, but here is is easier to
     * maintain since the date format string is externalized in the message
     * resources
     * 
     * @param dateFormat
     * @return A string represening the current date and time on the server
     */
    public String formatDate(Date theDate, Locale locale)
    {
    	String dateFormat = get("log.dateFormat");
        String formattedDate = null;
        if (GenericValidator.isBlankOrNull(dateFormat))
        {
            //logger.warn("Date format cannot be null in method getServerDate(). Please verify that the date format string is correct.");
            return null;
        }

        try
        {
            SimpleDateFormat f = new SimpleDateFormat(dateFormat, locale);
            formattedDate = f.format(theDate);
        }
        catch (Exception e)
        {
            //logger.error("An error occurred while getting the server date and time.", e);
            formattedDate = null;
        }
        return formattedDate;
    }
    
    public String limitedString(String theString, int length, String ellipsis)
    {
    	String returnString = null;
    	if ( theString.length() > length )
    		returnString = theString.substring(0, length ) + ellipsis;
    	else
    		returnString = theString;
    	return returnString;
    }

    public String getReportURL( Report theReport )
    {
    	String reportURL = null;
    	try
    	{
			reportURL = ReportURLEncoder.getResourceURL(theReport, request
					.getLocale().toString(), request.getUserPrincipal()
					.getName());
    	} catch (Exception e )
    	{	
    		logger.error("An error occurred while attempting to build url for report: ", e );
    	}
    	if ( reportURL == null )
    		reportURL = "#";
    	return reportURL;
    }
    
    public String getReportURLUserEnc( Report theReport )
    {
    	String reportURL = null;
    	try
    	{
			reportURL = ReportURLEncoder.getResourceURL(theReport, request
					.getLocale().toString());
			reportURL = reportURL + "&__user=" + getEncryptedUserName();
    	} catch (Exception e )
    	{	
    		logger.error("An error occurred while attempting to build url for report: ", e );
    	}
    	if ( reportURL == null )
    		reportURL = "#";
    	return reportURL;
    }
    
    public String getReportParamsURL( Report theReport )
    {
    	String reportURL = null;
    	try
    	{
			reportURL = ReportURLEncoder.getParamsURL(theReport, request
					.getLocale().toString());
    	} catch (Exception e )
    	{	
    		logger.error("An error occurred while attempting to build url for report: ", e );
    	}
    	if ( reportURL == null )
    		reportURL = "#";
    	return reportURL;
    }
}

