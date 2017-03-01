/*
 * Copyright 2005 by Motive, Inc. All rights reserved.
 * 
 * This software is the confidential and proprietary information of Motive, Inc.
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Motive.
 */
package motive.reports.reportconsole.tools;

import org.apache.velocity.tools.view.context.ViewContext;

/**
 * ButtonTool.java
 * 
 * CVS: $Id: ButtonTool.java,v 1.5 2013/12/26 23:43:50 balraj Exp $
 * 
 * @author kmckenzi
 * @version $Revision: 1.5 $
 * 
 */
public class ButtonTool extends MotiveMessageTool
{
    /**
     * Tool must be initialized before use.
     */
    public ButtonTool()
    {
        super();
    }

    /**
     * Initializes this tool.
     * 
     * @param obj the current ViewContext
     * @throws IllegalArgumentException if the param is not a ViewContext
     */
    public void init(Object obj)
    {
        if (!(obj instanceof ViewContext))
        {
            throw new IllegalArgumentException("Tool can only be initialized with a ViewContext");
        }

        ViewContext context = (ViewContext) obj;
        this.request = context.getRequest();
        this.application = context.getServletContext();
    }

    /**
     * Legacy function for backward compatibility
     * 
     * @param id
     * @param key
     * @param textClass
     * @param onClick
     * @return
     */
    public String render(String id, String key, String textClass, String onClick)
    {
        return render(id, key, textClass, onClick, null);
    }

    /**
     * 
     * @param id
     * @param key
     * @param spanClass
     * @param onClick
     * @return
     */
    public String render(String id, String key, String spanClass, String onClick, String roles)
    {
        // looks to see if the user has at least one of the roles defined for
        // the button and if so assumes the user is authorized to use the button
        boolean authorized = false;
        if (roles != null && roles.length() > 0)
        {
            String[] rolesArray = roles.split(",");
            for (int i = 0; i < rolesArray.length; i++)
            {
                if (request.isUserInRole(rolesArray[i]))
                {
                    authorized = true;
                    break;
                }
            }
        }
        else
        {
            authorized = true;
        }
        String buttonClass = (spanClass.equals("disabledButton")) ? "hdm-button-disabled": "hdm-button";
        if (!authorized) buttonClass = "hdm-button-locked";
        spanClass = (authorized == true) ? spanClass : "disabledButton";

        StringBuffer b = new StringBuffer();
        b.append("<button class=\"");
        b.append(buttonClass);
        b.append("\" id=\"");
        b.append(id);
        b.append("\" onclick=\"");
        b.append(onClick);
        b.append("\">");
        b.append("<div>");
        b.append("<span id=\"");
        b.append(id);
        b.append("_span\" class=\"");
        b.append(spanClass);
        b.append("\">");
        b.append(get(key));
        b.append("</span>");
        b.append("</div>");
        b.append("</button>");

        return b.toString();
    }
}
