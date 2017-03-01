/*
 * Copyright 2005 by Motive, Inc. All rights reserved.
 * 
 * This software is the confidential and proprietary information of Motive, Inc.
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Motive.
 */
package motive.reports.reportconsole.actions;

import javax.servlet.http.HttpServletResponse;

import motive.reports.reportconsole.ServiceUtil;
import motive.trace.TraceLogger;

import org.apache.log4j.Logger;
import org.apache.struts.actions.DispatchAction;


/**
 * MotiveDispatchAction.java
 * 
 * CVS: $Id: MotiveDispatchAction.java,v 1.5 2013/12/26 23:43:50 balraj Exp $
 * 
 * @author kmckenzi
 * @version $Revision: 1.5 $
 * 
 */
public class MotiveDispatchAction extends DispatchAction
{
    private static Logger logger = TraceLogger.getLogger(MotiveDispatchAction.class);

    /**
     * Takes the input string and writes it to the response
     * 
     * @param out
     */
    public void writeResponse(HttpServletResponse response, String out)
    {
        ActionUtils.writeResponse(response, out);
    }



}
