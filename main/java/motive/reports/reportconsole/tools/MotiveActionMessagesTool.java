/*
 * Copyright 2005 by Motive, Inc. All rights reserved. This software is the
 * confidential and proprietary information of Motive, Inc. ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Motive.
 */
package motive.reports.reportconsole.tools;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessages;
import org.apache.velocity.tools.struts.ActionMessagesTool;
import org.apache.velocity.tools.struts.StrutsUtils;

/**
 * 
 * MotiveActionMessagesTool.java
 * 
 * CVS: $Id: MotiveActionMessagesTool.java,v 1.5 2013/12/26 23:43:50 balraj Exp $
 *
 * @author kmckenzi
 * @version $Revision: 1.5 $
 *
 */
public class MotiveActionMessagesTool extends ActionMessagesTool
{
    protected ActionErrors actionErrors;

    /**
     * Default constructor. Tool must be initialized before use.
     */
    public MotiveActionMessagesTool()
    {
    }

    /**
     * Initializes this tool.
     * 
     * @param obj
     *            the current ViewContext
     * @throws IllegalArgumentException
     *             if the param is not a ViewContext
     */
    public void init(Object obj)
    {
        // setup superclass instance members
        super.init(obj);

        this.actionMsgs = StrutsUtils.getMessages(this.request);
        if (actionMsgs == null || actionMsgs.isEmpty())
        {
            this.actionMsgs = (ActionMessages) request.getSession().getAttribute(Globals.ERROR_KEY);
        }
    }

}
