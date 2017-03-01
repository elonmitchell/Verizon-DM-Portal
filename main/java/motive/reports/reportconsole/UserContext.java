/*
 * Copyright 2005 by Motive, Inc. All rights reserved. This software is the
 * confidential and proprietary information of Motive, Inc. ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Motive.
 */
package motive.reports.reportconsole;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.apache.struts.upload.FormFile;

public class UserContext
{
    private static final Integer ZERO = new Integer(0);

    private static final Integer ONE = new Integer(1);


    /** Attribute key for license warning */
    public static final String HAS_LICENSE_WARNING = "hasLicenseWarning";
    
    /** Attribute key for license expiry date */
    public static final String LICENSE_EXPIRY_DATE = "licenseExpiryDate";
    
    /** Attribute key for license warning */
    public static final String LICENSE_WARNING_FOR_FEATURE = "licenseWarningForFeature";
    
    public static final String CURRENT_GROUP_ID = "currentGroupId";
    
    public UserContext()
    {
    }

    public static void setCurrentGroupId( HttpServletRequest request, Long _currentGroupId) 
    { 
    	request.getSession().setAttribute(CURRENT_GROUP_ID, _currentGroupId ); 
    }
    public static Long getCurrentGroupId( HttpServletRequest request ) 
    {
        Long currentGroupId = (Long) request.getSession().getAttribute(CURRENT_GROUP_ID);
    	return currentGroupId; 
    }
    
    /**
     * Looks in the session to see if the feature has been checked before
     * 
     * @param featureName
     * @return
     */    
    public static boolean isFeatureChecked(HttpServletRequest request, String featureName)
    {        
        Map features = (HashMap) request.getSession().getAttribute(LICENSE_WARNING_FOR_FEATURE);
        if (features != null && features.get(featureName) != null)
        {
          //logger.debug("License for feature - " + featureName + " has been checked earlier. No need to check again");
          return true;   
        }
        return false;
    }
    
    /**
     * @param request
     * @return
     */
    public static boolean hasLicenseWarning(HttpServletRequest request)
    {
        Boolean warned = (Boolean) request.getSession().getAttribute(HAS_LICENSE_WARNING);
        warned = (warned == null) ? Boolean.FALSE : warned;
        return (warned.equals(Boolean.TRUE)) ? true : false;
    }
    
     /**
     * @param request
     * @param warned
     */
    public static void setLicenseWarning(HttpServletRequest request, boolean warned)
    {
        request.getSession().setAttribute(HAS_LICENSE_WARNING, Boolean.valueOf(warned));
    }

    /**
     * @param request
     * @param warned
     */
    public static void setLicenseExpiryDate(HttpServletRequest request, Date expiryDate)
    {
        request.getSession().setAttribute(LICENSE_EXPIRY_DATE, expiryDate);
    }
    
    /**
     * @param request
     * @return
     */
    public static Date getLicenseExpiryDate(HttpServletRequest request)
    {
        return (Date) request.getSession().getAttribute(LICENSE_EXPIRY_DATE);     
    }
   
    /**
     * @param request
     * @param warned
     */
    public static void setLicenseWarning(HttpServletRequest request, String featureName, boolean warned)
    {
        Map featureWarned = (HashMap) request.getSession().getAttribute(LICENSE_WARNING_FOR_FEATURE);
        if (featureWarned == null)
            featureWarned = new HashMap();
        
        featureWarned.put(featureName, Boolean.valueOf(warned));
        
        request.getSession().setAttribute(LICENSE_WARNING_FOR_FEATURE, featureWarned);
    }
    
    /**
     * @param request
     * @return
     */
    public static boolean hasLicenseWarning(HttpServletRequest request, String featureName)
    {
        Boolean warned = false;
        Map featureWarned = (HashMap) request.getSession().getAttribute(LICENSE_WARNING_FOR_FEATURE);
        if (featureWarned != null)
            warned = (Boolean) featureWarned.get(featureName);
        
        warned = (warned == null) ? Boolean.FALSE : warned;
        return (warned.equals(Boolean.TRUE)) ? true : false;
    }    
    
    public static void clearFeatureCheckList(HttpServletRequest request){
   	 Map features = (HashMap) request.getSession().getAttribute(LICENSE_WARNING_FOR_FEATURE);
   	 if (features != null || features.size() !=0)
        {
   		 features.clear();
   		 request.getSession().setAttribute(LICENSE_WARNING_FOR_FEATURE, features);
        }
   }
}
