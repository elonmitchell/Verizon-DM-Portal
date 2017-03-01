package motive.reports.reportconsole.welcome.actions;

/*
 * Copyright 2005 by Motive, Inc. All rights reserved. This software is the
 * confidential and proprietary information of Motive, Inc. ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Motive.
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import motive.exceptions.ObjectNotFoundException;
import motive.reports.reportconsole.ServiceUtil;
import motive.reports.reportconsole.UserContext;
import motive.reports.reportconsole.actions.ActionUtils;
import motive.reports.reportconsole.actions.BaseAction;
import motive.trace.TraceLogger;

import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.DynaActionForm;

/**
 * Populates the fields on the edit/new device panel.
 */
public class Welcome extends BaseAction
{
    /** logger */
    private static final Logger logger = TraceLogger.getLogger(Welcome.class);


    /**
     * Populates the fields on the edit/new device panel.
     */
    public ActionForward executeAction(ActionMapping mapping,
                                       ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws Exception
    {
        String forwardName = ActionUtils.FORWARD_SUCCESS;

        return mapping.findForward(forwardName);
    }
    
}
