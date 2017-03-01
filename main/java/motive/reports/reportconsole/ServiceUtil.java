/*
 * Copyright 2005 by Motive, Inc. All rights reserved. This software is the
 * confidential and proprietary information of Motive, Inc. ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Motive.
 */
package motive.reports.reportconsole;

import javax.ejb.CreateException;
//import javax.naming.InitialContext;
import javax.naming.NamingException;

//import motive.reports.service.etl.ReportETLService;
import motive.reports.service.reportmanager.ReportManagerService;
//import motive.reports.service.security.ReportSecurityService;
//import motive.servicesupport.ejb.JNDIHelper;

public class ServiceUtil
{
	
    private ReportManagerService reportManagerService;
    
    //private ReportSecurityService reportSecurityService;
    
    //private ReportETLService reportETLService;

    private static ServiceUtil instance = null;

    public static ServiceUtil getInstance() throws CreateException, NamingException
    {
        if (instance == null)
        {
            instance = newInstance();
        }
        return instance;
    }

    private static ServiceUtil newInstance() throws CreateException, NamingException
    {
        ServiceUtil serviceUtil = new ServiceUtil();
        System.out.println("Value of serviceUtil is :"+serviceUtil);
        return serviceUtil;
    }

    protected ServiceUtil() throws CreateException, NamingException
    {
       
    	System.out.println("Value of class is :"+ReportManagerService.class);
    	System.out.println("Value of localinterface is :"+CustomJNDIHelper.getLocalInterface(ReportManagerService.class));
    	reportManagerService = (ReportManagerService)CustomJNDIHelper.getLocalInterface(ReportManagerService.class);
        //reportETLService = JNDIHelper.getLocalInterface(ReportETLService.class);
        //reportSecurityService = JNDIHelper.getLocalInterface(ReportSecurityService.class);
    }
    
	  /**
	  * Retrieve the ReportManagerService instance.
	  */
	public ReportManagerService getReportManagerService()
	{
		System.out.println("Value of reportManagerService from insdie get :"+reportManagerService.getClass());
		return reportManagerService;
	}    

	/**
	 * ReportSecurityService
	 * 
	 * @return an instance of the AuthenticatorSecurity
	 */
	/*public ReportSecurityService getAuthenticatorSecurityService()
	{
		System.out.println("Value of reportSecurityService from insdie get :"+reportSecurityService.getClass());
	    return reportSecurityService;
	}*/

	/**
	 * ReportETLService
	 * 
	 * @return an instance of the AuthenticatorSecurity
	 */
	/*public ReportETLService getReportETLService()
	{
	    return reportETLService;
	}*/

}
