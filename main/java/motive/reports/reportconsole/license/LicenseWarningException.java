/*
 *  Copyright 2005 by Motive, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  Motive, Inc. ("Confidential Information").  You shall not disclose
 *  such Confidential Information and shall use it only in accordance
 *  with the terms of the license agreement you entered into with Motive.
 */
package motive.reports.reportconsole.license;

/**
 * 
 * LicenseWarningException.java
 * 
 * CVS: $Id: LicenseWarningException.java,v 1.5 2013/12/26 23:43:50 balraj Exp $
 *
 * @author kmckenzi
 * @version $Revision: 1.5 $
 *
 */
public class LicenseWarningException extends Exception
{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 8120848893526767135L;

    /**
     * 
     */
    public LicenseWarningException()
    {
        super();
    }

    /**
     * @param message
     */
    public LicenseWarningException(String message)
    {
        super(message);
    }

    /**
     * @param cause
     */
    public LicenseWarningException(Throwable cause)
    {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public LicenseWarningException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
