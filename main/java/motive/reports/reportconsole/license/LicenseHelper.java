package motive.reports.reportconsole.license;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import com.alcatel.slim.*;

import motive.exceptions.ObjectNotFoundException;
import motive.reports.commons.VersionUtil;
import motive.reports.reportconsole.UserContext;
import motive.reports.reportconsole.actions.ActionUtils;
import motive.service.configuration.ConfigurationHelper;
import motive.trace.TraceLogger;

public class LicenseHelper {

	//The singleton instance of this helper. 
    private static LicenseHelper singletonInstance = null;
    
	/** trace logger */
    private static Logger logger = TraceLogger.getLogger(LicenseHelper.class);
    
	public final static String FEATURE_REPORTING_CONSOLE = "reporting";
    public final static String FEATURE_HOSTIDS = "host-ids";
    
    final static String CFG_LICENSE_STRING = "Licensing.licenseString";
    final static byte[] ENCODED_PUBLIC_KEY = {48, -126, 1, -73, 48, -126, 1, 44, 6, 7, 42, -122, 72, -50, 56, 4, 1, 48, -126, 1, 31, 2, -127, -127, 0, -3, 127, 83, -127, 29, 117, 18, 41, 82, -33, 74, -100, 46, -20, -28, -25, -10, 17, -73, 82, 60, -17, 68, 0, -61, 30, 63, -128, -74, 81, 38, 105, 69, 93, 64, 34, 81, -5, 89, 61, -115, 88, -6, -65, -59, -11, -70, 48, -10, -53, -101, 85, 108, -41, -127, 59, -128, 29, 52, 111, -14, 102, 96, -73, 107, -103, 80, -91, -92, -97, -97, -24, 4, 123, 16, 34, -62, 79, -69, -87, -41, -2, -73, -58, 27, -8, 59, 87, -25, -58, -88, -90, 21, 15, 4, -5, -125, -10, -45, -59, 30, -61, 2, 53, 84, 19, 90, 22, -111, 50, -10, 117, -13, -82, 43, 97, -41, 42, -17, -14, 34, 3, 25, -99, -47, 72, 1, -57, 2, 21, 0, -105, 96, 80, -113, 21, 35, 11, -52, -78, -110, -71, -126, -94, -21, -124, 11, -16, 88, 28, -11, 2, -127, -127, 0, -9, -31, -96, -123, -42, -101, 61, -34, -53, -68, -85, 92, 54, -72, 87, -71, 121, -108, -81, -69, -6, 58, -22, -126, -7, 87, 76, 11, 61, 7, -126, 103, 81, 89, 87, -114, -70, -44, 89, 79, -26, 113, 7, 16, -127, -128, -76, 73, 22, 113, 35, -24, 76, 40, 22, 19, -73, -49, 9, 50, -116, -56, -90, -31, 60, 22, 122, -117, 84, 124, -115, 40, -32, -93, -82, 30, 43, -77, -90, 117, -111, 110, -93, 127, 11, -6, 33, 53, 98, -15, -5, 98, 122, 1, 36, 59, -52, -92, -15, -66, -88, 81, -112, -119, -88, -125, -33, -31, 90, -27, -97, 6, -110, -117, 102, 94, -128, 123, 85, 37, 100, 1, 76, 59, -2, -49, 73, 42, 3, -127, -124, 0, 2, -127, -128, 5, -71, 22, -114, 79, 127, 99, 69, 7, 47, -12, -66, -79, 68, -95, -109, -115, -15, 50, -56, -107, -52, -119, -63, -2, 17, 22, -33, -71, 69, -3, 64, -46, -104, -38, 33, 30, -3, 51, -126, -94, 104, -83, 109, 117, -107, -102, 23, 86, -69, -63, 126, 43, 4, 94, 117, 117, -14, -18, 115, 107, -78, -63, -69, 28, 72, 97, 84, -126, -48, 24, 9, -27, 76, -90, -74, -14, 25, 109, -15, 12, -111, -42, -65, -13, 121, 54, 112, 73, -40, -56, -77, -6, 65, -3, -78, -76, 98, 110, -72, -90, 51, 3, 89, 108, 112, -31, -46, -62, 122, 47, -71, -5, -42, 60, -117, 93, 104, 47, 95, 13, -74, 95, -56, 45, -112, -124, -124};

    /** key to the configuration property */
    private static final String CONFIG_WARNING_DAYS_KEY = "reporting.managementconsole.licensing.warning.days";

    /** number of days to check for displaying a licensing warning */
    private static final int WARNING_DAYS = 14;
    /**version file */
    private static final String VERSION_FILE = "reporting.version"; 
    private static String version;
    
    private String licenseData;
    private PublicKey publicKey;
    private License license = null;
    
    /* 
     * Initialize the license data and get the features
     */
    private LicenseHelper() throws LicenseException, MalformedLicenseException, ObjectNotFoundException {
        /*
         * Reporting stores license data in a Configuration property. It
         * uses the default public key generated from the ENCODED_PUBLIC_KEY
         * bytes above.
         */ 
        this.licenseData =  new ConfigurationHelper().getProperty(CFG_LICENSE_STRING, null);
        if (licenseData.length() == 0)
        	throw new ObjectNotFoundException("Licensing string not present in database. Please check configuration properties (property:" +
                    new Object[] {CFG_LICENSE_STRING});
        
        try {
        	KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
        	X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(ENCODED_PUBLIC_KEY);
        	this.publicKey  = keyFactory.generatePublic(pubKeySpec);
        }
        catch (Exception E) {
		    throw new RuntimeException("LicensingConstants: Can't initialize public key!");
		}
        
       //Get the decrypted license out and map the features.    	    	
    	this.license = LicenseUtil.parseLicense(this.licenseData, this.publicKey);    	
    }
    
    /**
     * The static method which returns the singleton instance of this class.
     * @return LicensingHelper instance.
     * @throws java.lang.Exception
     */
    public static LicenseHelper getInstance() throws Exception
    {    	
    	if(null == singletonInstance)
    	{
            try
            {            	
                singletonInstance = new LicenseHelper();
            }
            catch(Exception e)
            {            	
                throw new Exception(e.getMessage());            	
            }
    	}
    	return singletonInstance;
    }
    
    /**
     * This method sets the singletonInstance to null so that a fresh instance
     * is created when someone looks for it the next time. 
     */
    public static void resetSingletonInstance()
    {
        singletonInstance = null;
     }
    
   /**
	 * Get feature information or throw and exception if expiration date is passed
	 * 
	 */
    public Feature checkFeature(String name) throws ObjectNotFoundException, FeatureInactiveException, MalformedLicenseException {
    	
    	Feature feature = (FeatureImpl) this.license.getFeature(name);
    	if (feature == null)
			throw new ObjectNotFoundException("Feature not found. (name: " + new Object[] { name});
    	
    	logger.debug("Found feature. Name = " + feature.getName() + " is " + (feature.getFeaturePermission().isActivated() ? "active":"INACTIVE"));
    	if (!feature.getFeaturePermission().isActivated())
			throw new FeatureInactiveException(feature);
    	
    	return feature;
    }
    
    /* 
     * This returns the public key being used by Reporting Application for the license.
     */
    public PublicKey getPublicKey() {
    	return this.publicKey;
    }
    
    protected void checkLicenseWarning(HttpServletRequest request, Feature licenseFeature) throws LicenseWarningException
    {
    	//Return if the feature is not a temporary feature
        if (!(licenseFeature.getFeaturePermission() instanceof TemporaryFeaturePermission))
            return;
        
        ConfigurationHelper config = new ConfigurationHelper();
        Date expirationDate = null;
        int warningDays = WARNING_DAYS;
        try
        {
            // look for a configuration property for the number of warning
            // days
            warningDays = config.getIntProperty(CONFIG_WARNING_DAYS_KEY, WARNING_DAYS);
            warningDays = 0 - Math.abs(warningDays);
        }
        catch (Exception e)
        {
            // don't care what happens here just use the hard-coded default
            logger.debug("Unable to retrieve the warning days configuration property. Using the default...", e);
            warningDays = WARNING_DAYS;
        }

        // get the expiration date for the management-console
        expirationDate = licenseFeature.getFeaturePermission().getExpirationDate();
        
        UserContext.setLicenseExpiryDate(request, expirationDate);

        // create a calendar object representing the date to start warnings
        Calendar warningDate = Calendar.getInstance();
        warningDate.setTime(expirationDate);
        warningDate.set(Calendar.HOUR, 0);
        warningDate.set(Calendar.MINUTE, 0);
        warningDate.set(Calendar.SECOND, 0);
        warningDate.set(Calendar.MILLISECOND, 0);
        warningDate.add(Calendar.DATE, warningDays);

        // create another calendar object for today's date
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        // throw a LicenseWarningException if were inside the warning date
        if (today.equals(warningDate) || today.after(warningDate))
        {
            int daysLeft = actualDifference(today.getTime(), expirationDate);
            logger.warn("feature '" + licenseFeature.getName() + "' will expire in " + daysLeft + " day(s)");
            throw new LicenseWarningException(String.valueOf(daysLeft));
        }
    }

    /**
     * Returns the actual difference between the two dates in days. If date1 is
     * before date 2, the result will be a positive int. If date1 is after
     * date2, the result will be a negative int.
     * 
     * @param date1
     *            the first date
     * @param date2
     *            the second date
     * @return the number of days between the two dates.
     */
    public static int actualDifference(Date date1, Date date2)
    {
        GregorianCalendar gc1 = new GregorianCalendar();
        GregorianCalendar gc2 = new GregorianCalendar();
        gc1.setTime(date1);
        gc2.setTime(date2);
        long millies = gc2.getTimeInMillis() - gc1.getTimeInMillis();
        return (int) (millies / 1000 / 24 / 60 / 60);
    }

    /**
     * @return
     * @throws LicenseException 
     */
    public boolean checkLicensing(HttpServletRequest request, ActionErrors errors, String featureName) {
    	
    	logger.debug("in checkLicensing()...");
        Feature feature = null;
        
        boolean licenseWarning = false;
        try
        {        	//Check version
        	if(!isLicenseVersionValid())
        	{
        		String licVer = (String)license.getProperties().get("version");
        		if(licVer == null || licVer.trim().equals(""))
        		{
        			logger.error("Product license does not include the release parameter (version)");
                    request.setAttribute("licenseException", "license.invalid.version.exception");
                    errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("license.missing.version.exception"));
        		}
        		else
        		{
        			//check if version info could not be read from the reporting.version file
           			if(version == null || version.trim().equals(""))
            		{
                		logger.error("Application license version could not be retrieved from reporting.version file. File might be missing.");
                        request.setAttribute("licenseException", "license.invalid.version.exception");
                        errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("license.missing.versionfile.exception"));
            		}
            		else
            		{
            			//if we are here, it means we could read the version info and it didn't match the one in the license
                		logger.error("License version ("+licVer+") is not valid for deployed application version ("
                				+VersionUtil.getReleaseVersion(version)+").");
                        request.setAttribute("licenseException", "license.invalid.version.exception");
                        errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("license.invalid.version.exception", 
                        		new Object[] {licVer, VersionUtil.getReleaseVersion(version) }));
            		}
        		}
        		
        		
        	}
        	// throws an FeatureInactiveException if host ID not in license
        	feature = checkFeature(featureName);
            
            // put the feature name in the Hashmap, later we will use this to determine if the 
            // feature has been checked before
            UserContext.setLicenseWarning(request, featureName, false);
            
            //If the feature is a temporary feature
            if (feature.getFeaturePermission().getExpirationDate() != null)
                checkLicenseWarning(request, feature);
            
            logger.debug("checkLicensing() succeeded...");
             
        }
        catch(FeatureInactiveException fie) {
        	
        	Feature f = fie.getFeature();
            if (f.getFeaturePermission() instanceof TemporaryFeaturePermission)
            {               
                MessageResources messages = ActionUtils.getMessageResources(request);
                String format = messages.getMessage(request.getLocale(), "java.dateFormat");
                DateFormat df = new SimpleDateFormat(format); 
                
                String expDate = df.format(f.getFeaturePermission().getExpirationDate());
                
                logger.error("License has expired for feature: " + featureName, fie);
                errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("license.expired.exception", new Object[] { getFeatureMessageString(request, featureName), expDate }));                
            }
            else if (f.getFeaturePermission() instanceof HostBoundFeaturePermission)
            {
                logger.error("License does not include this host: " + featureName, fie);
                
                request.setAttribute("licenseException", "license.invalid.host.exception");
                errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("license.invalid.host.exception", new Object[] {
                        getFeatureMessageString(request, featureName)}));                
            }
        } 
        catch (ObjectNotFoundException lnfe) {
        	logger.error("License not found for feature: " + featureName, lnfe);
            String messagekey = "license.missing.exception";            
            if (featureName.indexOf(FEATURE_HOSTIDS) == -1) // this is a feature check
            {
                messagekey = "feature.license.missing.exception";
            }
            errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage(messagekey, new Object[] { getFeatureMessageString(request, featureName) }));
		}
        catch (MalformedLicenseException mle) {
        	
        	logger.error("License is invalid feature: " + featureName, mle);
            errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("license.invalid.exception", new Object[] { getFeatureMessageString(request, featureName) }));
		} 
        catch (LicenseWarningException lwe) {
        	// only show the warning message once
            if (!UserContext.hasLicenseWarning(request, featureName))
            {
            	Long daysLeft = Long.valueOf(lwe.getMessage());
                // this creates a pretty message based on the choices
                // defined in the limits. message keys must exist for each
                // limit, i.e. reporting.licensing.warning.pattern.part.(i)
                double[] limits = { 1, 2 };
                Object[] args = { daysLeft, daysLeft };
                String message = ActionUtils.formatNumberChoiceMessage(request, "reporting.licensing.warning.pattern", limits, args);

                errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("reporting.licensing.warning.msg", getFeatureMessageString(request, featureName), message));
                UserContext.setLicenseWarning(request, featureName, true);
                
                licenseWarning = true;
            }
		} 
                
        return licenseWarning;    	
    }
   
    /**
     * Looks up the {feature}.name message property
     * 
     * @return
     */
    protected static String getFeatureMessageString(HttpServletRequest request, String featureName)
    {
        Locale locale = ActionUtils.getLocale(request);
        MessageResources messages = ActionUtils.getMessageResources(request);
        return messages.getMessage(locale, featureName + ".name");
    }

    /**
	 * This methods reads the version info from the license file and compares
	 * it with the product version that is retrieved from hdm.version file (which 
	 * is generated by maven and located into /WEB-INF/classes directory
	 * @return isValid
	 */
	public boolean isLicenseVersionValid(){
		boolean isValid = false;
		String ver = getVersionInfo();
		
		if(ver != null && !ver.trim().equals(""))
		{	
	    	
	    	if(license.getProperties().get("version") != null)
	    	{
	    		if(((String)license.getProperties().get("version")).trim().equals(ver))
	    	    isValid = true;
	    	}
	    }
		
		return isValid; 
	}

	/**
	 * This method will parse the extract the 'release' version (e.g : 3.11)
	 * from the product version (e.g : 3.11.0.7-SNAPSHOT)
	 * @param product version
	 * @return releaseVersion
	 */

	private String getVersionInfo() {
		this.version = readVersionInfo();
		return this.version;
	}

	/**
	 * This methods retrieves the value of hdm.version property.
	 * @return hdm.version
	 */
	private String readVersionInfo() {
		return VersionUtil.getReleaseVersion();
	}
    
}
