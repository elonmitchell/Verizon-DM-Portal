/*
 * Copyright 2005 by Motive, Inc. All rights reserved. This software is the
 * confidential and proprietary information of Motive, Inc. ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Motive.
 */
package motive.reports.reportconsole.actions;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.text.ChoiceFormat;
import java.text.DateFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import motive.exceptions.InvalidArgumentException;
//import motive.exceptions.ObjectNotFoundException;
//import motive.exceptions.ProviderException;
//import motive.commons.util.DateUtil;
//import motive.managementconsole.ServiceUtil;
//import motive.service.configuration.ConfigurationHelper;
import motive.exceptions.ProviderException;
import motive.reports.service.reportmanager.ReportGroup;
import motive.reports.service.reportmanager.ReportManagerService;
import motive.reports.reportconsole.ServiceUtil;
import motive.trace.TraceLogger;

import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;
import org.apache.velocity.tools.struts.StrutsUtils;


/**
 * Provides common utilities for actions
 * 
 * @author kmckenzi
 * @version $Id: ActionUtils.java,v 1.7 2014/02/14 00:12:12 ffuentes Exp $
 */
public class ActionUtils
{
    private static Logger logger = TraceLogger.getLogger(ActionUtils.class);

    /** Attribute name for report group list */
	public static final String GROUP_LIST = "groupList";
	
	/** Attribute name for report group list failure flag */
	public static final String GROUP_LOAD_FAILED = "groupLoadFailed";

	/** Attribute name for report group list failure flag */
	public static final String REPORT_IMPORT_FAILED = "importError";

	/** Attribute name for currently selected report group */
	public static final String CURRENT_GROUP_ID = "currentGroupID";

    /** Attribute name for currently selected report group */
	public static final String REPORT_LIST = "reportList";
	
	/** Attribute name for report list failure flag */
	public static final String REPORT_LIST_LOAD_FAILURE = "reportListFailure";

	/** Attribute name for group id from form */
	public static final String GROUP_ID = "groupId";
	
	/** Attribute name for historical report list */
	
	public static final String HIST_REPORT_LIST = "histReportList";
	
	public static final String SUBSCRIPTION_COUNT = "subscriptionCount";
	
	/** Attribute int for the persisted group id index */
	public static final String PERSISTED_SELECTED_INDEX = "persistedSelectedIndex";
	
	/** Attribute name for group id from form */
	public static final String GROUP_NAME = "groupName";

	/** the name of the configuration properties file */
    public static final String CONFIG_PROPS_KEY = "configuration.properties";

    /** the path of the configuration properties file */
    public static final String CONFIG_PROPS_URL = "/WEB-INF/configuration.properties";


	/** Attribute name for group id from form */
	public static final String ETL_LIST = "etlList";

	/** Attribute name for group id from form */
	public static final String ETL_ERROR_MSG = "etlErrorMessage";

	/** Attribute name for group id from form */
	public static final String ETL_LOAD_ERROR = "etlLoadError";

    /** String constant for ActionForward of success */
    public static final String FORWARD_SUCCESS = "success";

    /** String constant for ActionForward of error */
    public static final String FORWARD_ERROR = "error";

    /** String constant for ActionForward of fail */
    public static final String FORWARD_FAIL = "fail";
    
    /** String constant for ActionForward of ETL refresh */
    public static final String FORWARD_ETL_REFRESH = "etlRefresh";

    /** String constant for character encoding */
    public static final String DEFAULT_ENCODING = "UTF-8";

   /** Strings for name of roles */
    
    /** Attribute name for schedule list */
	public static final String SCHEDULE_LIST = "scheduleList";
    
    
    /**
     * Pulls the configuration.properties file from the file system.
     * 
     * @param request
     * @return
     * @deprecated
     */
    protected static Properties loadConfigurationProperties(HttpServletRequest request)
    {
        ServletContext servletContext = request.getSession().getServletContext();
        Properties properties = (Properties) servletContext.getAttribute(CONFIG_PROPS_KEY);

        if (properties == null)
        {
            try
            {
                properties = new Properties();
                properties.load(servletContext.getResourceAsStream(CONFIG_PROPS_URL));
                logger.debug("Loaded configuration properties: " + CONFIG_PROPS_URL);
                servletContext.setAttribute(CONFIG_PROPS_KEY, properties);
            }
            catch (IOException e)
            {
                logger.error("Error: Could not load configuration properties file: " + CONFIG_PROPS_URL, e);
            }
        }

        return properties;
    }

//    /**
//     * getConfigurationProperty now uses the ConfigurationHelper, this method
//     * was left for backward compatibility
//     * 
//     * @param request
//     * @param key
//     * @return
//     * @deprecated
//     */
//    public static String getConfigurationProperty(HttpServletRequest request, String key)
//    {
//        return getConfigurationProperty(request, key, null);
//        // return getConfigurationProperty(key);
//    }

//    /**
//     * getConfigurationProperty now uses the ConfigurationHelper, this method
//     * was left for backward compatibility
//     * 
//     * @param request
//     * @param key
//     * @param defaultValue
//     * @return
//     * @deprecated
//     */
//    public static String getConfigurationProperty(HttpServletRequest request, String key, String defaultValue)
//    {
//        String value = getConfigurationProperty(key, defaultValue);
//        if (value == null)
//        {
//            Properties props = loadConfigurationProperties(request);
//            value = (defaultValue != null) ? props.getProperty(key, defaultValue) : props.getProperty(key);
//        }
//        return value;
//    }

//    /**
//     * Get a configuration property using the ConfigurationHelper
//     * 
//     * @param key
//     * @return
//     * @deprecated
//     */
//    protected static String getConfigurationProperty(String key)
//    {
//        return getConfigurationProperty(key, null);
//    }

    /**
     * Get a configuration property using the ConfigurationHelper. If
     * defaultValue is specified and a value is not found, the default value
     * will be returned
     * 
     * @param key
     * @param defaultValue
     * @return
     */
//    public static String getConfigurationProperty(String key, String defaultValue)
//    {
//        String value = null;
//
//        try
//        {
//            ConfigurationHelper helper = ServiceUtil.getInstance().getConfigurationHelper();
//            value = helper.getProperty(key, defaultValue);
//        }
//        catch (Exception e)
//        {
//            logger.error("Error calling getConfigurationProperty");
//        }
//        return (value == null) ? defaultValue : value;
//    }

    /**
     * @param aFile
     * @return
     * @throws Exception
     */
    public static byte[] getBytes(FormFile aFile) throws Exception
    {
        InputStream inputStream = aFile.getInputStream();

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) inputStream.available()];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = inputStream.read(bytes, offset, bytes.length - offset)) >= 0)
        {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length)
        {
            // throw new IOException("Could not completely read file
            // "+file.getName());
        }
        return bytes;
    }



    // TODO: Remove this function when Appservices fix this in the their code
    private static String getTypeInString(int type)
    {
        // Simple types
        final String SIMPLE_TYPE_TEXT = "text";
        final String SIMPLE_TYPE_MASK_TEXT = "maskText";
        final String SIMPLE_TYPE_BOOLEAN = "boolean";
        final String SIMPLE_TYPE_INT = "int";
        final String SIMPLE_TYPE_DATETIME = "dateTime";
        final String SIMPLE_TYPE_MULTI_LINE_TEXT = "multiLineText";
        final String SIMPLE_TYPE_CHOICE_LIST = "choiceList";
        final String SIMPLE_TYPE_SET_PARAMS = "setParams";
        final String SIMPLE_TYPE_GET_PARAMS = "getParams";
        final String SIMPLE_TYPE_UNSIGNED_INT = "unsignedInt";
        final String SIMPLE_TYPE_PARAMETER_ATTRIBUTE = "parameterAttribute";
        final String SIMPLE_TYPE_SET_PARAMETER_ATTRIBUTE = "setParameterAttribute";
        final String SIMPLE_TYPE_GET_PARAMETER_NAMES = "getParameterNames";

        switch (type)
        {
            case 0:
                return SIMPLE_TYPE_TEXT;
            case 1:
                return SIMPLE_TYPE_MASK_TEXT;
            case 2:
                return SIMPLE_TYPE_BOOLEAN;
            case 3:
                return SIMPLE_TYPE_INT;
            case 4:
                return SIMPLE_TYPE_DATETIME;
            case 5:
                return SIMPLE_TYPE_MULTI_LINE_TEXT;
            case 6:
                return SIMPLE_TYPE_CHOICE_LIST;
            case 7:
                return SIMPLE_TYPE_SET_PARAMS;
            case 8:
                return SIMPLE_TYPE_GET_PARAMS;
            case 9:
                return SIMPLE_TYPE_UNSIGNED_INT;
            case 10:
                return SIMPLE_TYPE_PARAMETER_ATTRIBUTE;
            case 11:
                return SIMPLE_TYPE_SET_PARAMETER_ATTRIBUTE;
            case 12:
                return SIMPLE_TYPE_GET_PARAMETER_NAMES;
        }

        return null;
    }


    /**
     * Determines based on criteria if the forward name "success" or "fail"
     * should be returned
     * 
     * @param condition
     * @return a string representing the name to be passed to
     *         mapping.findForward()
     */
    public static String getForwardSuccessOrFail(boolean condition)
    {
        return ((condition) ? FORWARD_SUCCESS : FORWARD_FAIL);
    }

    /**
     * Determines based on criteria if the forward name "success" or "fail"
     * should be returned
     * 
     * @param condition
     * @return a string representing the name to be passed to
     *         mapping.findForward()
     */
    public static String getForwardSuccessOrError(boolean condition)
    {
        return ((condition) ? FORWARD_SUCCESS : FORWARD_ERROR);
    }

    /**
     * Uses StrutsUtils to get the MessageResources. Same method used by
     * velocity.
     * 
     * @param request
     * @return
     */
    public static MessageResources getMessageResources(HttpServletRequest request)
    {
        ServletContext ctx = request.getSession().getServletContext();
        return StrutsUtils.getMessageResources(request, ctx);
    }

    /**
     * Uses StrutsUtils to get the user's locale. Same method used by velocity
     * 
     * @param request
     * @return
     */
    public static Locale getLocale(HttpServletRequest request)
    {
        return StrutsUtils.getLocale(request, request.getSession());
    }

//    /**
//     * Returns a date string in a format specified by the message key
//     * MESSAGE_KEY_CRITERIA_CALENDAR_DATE_FORMAT. This is needed for places
//     * where we don't want to use the UTC date format
//     * 
//     * @param UTCDateString
//     * @param request
//     * @return
//     */
//    private static String makePrettyDateString(String UTCDateString, HttpServletRequest request)
//    {
//        DateFormat formatter = null;
//        Date date = null;
//        String prettyString = "";
//        String criteriaFormatString = null;
//        Locale locale = getLocale(request);
//        MessageResources messages = getMessageResources(request);
//
//        if (messages != null)
//        {
//            if (messages.isPresent(locale, MESSAGE_KEY_CRITERIA_CALENDAR_DATE_FORMAT))
//            {
//                criteriaFormatString = messages.getMessage(locale, MESSAGE_KEY_CRITERIA_CALENDAR_DATE_FORMAT);
//
//                try
//                {
//                    formatter = new SimpleDateFormat(criteriaFormatString);
//                    date = DateUtil.tr69StringToDateWithTimeZone(UTCDateString);
//                    prettyString = formatter.format(date);
//                }
//                catch (Exception e)
//                {
//                    logger.error("Could not parse utc date string into a valid date", e);
//                    prettyString = "";
//                }
//            }
//        }
//
//        return prettyString;
//    }

    /**
     * Creates a date string based on a UTC date string. The new date format is
     * based on the message key MESSAGE_KEY_CRITERIA_CALENDAR_DATE_FORMAT
     * 
     * @param prettyDateString
     * @return
     */
//    private static String makeUTCDateString(String prettyDateString, HttpServletRequest request)
//    {
//        Date date = null;
//        String criteriaFormatString = null;
//        String utcString = null;
//        Locale locale = getLocale(request);
//        MessageResources messages = getMessageResources(request);
//        if (messages != null)
//        {
//            if (messages.isPresent(locale, MESSAGE_KEY_CRITERIA_CALENDAR_DATE_FORMAT))
//            {
//                criteriaFormatString = messages.getMessage(locale, MESSAGE_KEY_CRITERIA_CALENDAR_DATE_FORMAT);
//                DateFormat prettyFormat = new SimpleDateFormat(criteriaFormatString);
//                try
//                {
//                    date = prettyFormat.parse(prettyDateString);
//                    utcString = DateUtil.dateToTr69String(date);
//                }
//                catch (ParseException pe)
//                {
//                    logger.error("Could not parse criteria date format into a valid date", pe);
//                }
//
//            }
//        }
//
//        return utcString;
//    }

    /**
     * This function creates a message formatted based on the value of the
     * number passed in for the args. This is helpful where we have plurals and
     * want to avoid having the infamous "(s)". This is also very beneficial for
     * non-English translations
     * 
     * @param request
     * @param baseMessageKey
     * @param choices
     * @param args
     * @return
     */
    public static String formatNumberChoiceMessage(HttpServletRequest request,
                                                   String baseMessageKey,
                                                   double[] choices,
                                                   Object[] args)
    {
        String choiceMessage = "";

        MessageResources messages = getMessageResources(request);
        Locale locale = getLocale(request);

        try
        {
            MessageFormat messageForm = new MessageFormat("");
            messageForm.setLocale(locale);

            // warn if the base message key isnt specified
            if (!messages.isPresent(locale, baseMessageKey))
            {
                logger.warn("The base message key \""
                        + baseMessageKey
                        + "\" is missing from the message resources.  As a result, the choice formatted message will not display correctly.");
            }

            String pattern = messages.getMessage(locale, baseMessageKey);
            messageForm.applyPattern(pattern);

            String[] parts = new String[choices.length];
            for (int i = 0; i < choices.length; i++)
            {
                Double dbl = new Double(choices[i]);
                String partMessageKey = baseMessageKey + ".part." + dbl.intValue();
                parts[i] = messages.getMessage(locale, partMessageKey);

                // warn if the part message key isnt specified
                if (!messages.isPresent(locale, partMessageKey))
                {
                    logger.warn("The part message key \""
                            + partMessageKey
                            + "\" is missing from the message resources.  As a result, the choice formatted message will not display correctly.");
                }
            }

            ChoiceFormat choiceForm = new ChoiceFormat(choices, parts);
            Format[] formats = { choiceForm, NumberFormat.getInstance() };
            messageForm.setFormats(formats);
            choiceMessage = messageForm.format(args);
        }
        catch (IllegalArgumentException e)
        {
            logger.error("Caught IllegalArgumentException when trying to format the choice message for: "
                    + baseMessageKey, e);
            choiceMessage = messages.getMessage(locale, "choice.unspecified.message");
        }

        return choiceMessage;
    }

    public static String formatMessage( String message, Object[] args)
    {
    	
    	MessageFormat mf = new MessageFormat(message);
    	String result = mf.format(args);
    	try
    	{
    		if ( result == null )
    			result = message;
    	}
    	catch (Exception e)
    	{
    		result = message;
    	}
    	return result;
    }
    
    /**
     * @param str
     * @param pattern
     * @param replace
     * @return
     */
    public static String replace(String str, String pattern, String replace)
    {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();
        while ((e = str.indexOf(pattern, s)) >= 0)
        {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e + pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }


    /**
     * 
     * @param response
     * @param out
     */
    public static void writeResponse(HttpServletResponse response, String out)
    {
        try
        {
            if (out != null)
            {   
                response.setContentType("text/text; charset=utf-8");
                Writer writer = response.getWriter();
                writer.write(out);
            }
        }
        catch (Exception e)
        {
            // eat this
            logger.warn("The method writeResponse failed!", e);
        }
    }
    
	public static ReportGroup[] getReportGroups() throws Exception
	{
	   	ReportManagerService rms = ServiceUtil.getInstance().getReportManagerService();

     	ReportGroup[] rg = new ReportGroup[0];
    	try
    	{
			rg = rms.getAllReportGroups();
    	}
    	catch ( Exception pe )
    	{
    		logger.error("Could not load report groups : " + pe.getMessage());
    	}
		return rg;
	}


}
