/*
 * Copyright 2005 by Motive, Inc. All rights reserved. This software is the
 * confidential and proprietary information of Motive, Inc. ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Motive.
 */
package motive.reports.reportconsole.tools;

import org.apache.struts.util.MessageResources;
import org.apache.velocity.tools.struts.MessageTool;

/**
 * Velocity Struts tool that extends functionality of the standard velocity
 * Message Tool
 * 
 * @author kmckenzi
 * @version $Id: MotiveMessageTool.java,v 1.5 2013/12/26 23:43:50 balraj Exp $
 */
public class MotiveMessageTool extends MessageTool
{
    /**
     * Default constructor. Tool must be initialized before use.
     */
    public MotiveMessageTool()
    {
    }

    /**
     * @param key
     * @param defaultValue
     * @return the localized message for the specified key or default value or
     *         the key itself if no such message exists
     */
    public String get(String key, String defaultValue)
    {
        return get(key, null, (Object[]) null, defaultValue);
    }

    /**
     * @param key
     * @param bundle
     * @param args
     * @return the localized message for the specified key or <code>null</code>
     *         if no such message exists
     */
    public String get(String key, String bundle, Object[] args)
    {
        return get(key, bundle, args, null);
    }

    /**
     * @param key
     * @param bundle
     * @param defaultValue
     * @return the localized message for the specified key or default value or
     *         the key itself if no such message exists
     */
    public String get(String key, Object[] args, String defaultValue)
    {
        return get(key, null, args, defaultValue);
    }

    /**
     * @param key
     * @param bundle
     * @param defaultValue
     * @return the localized message for the specified key or default value or
     *         the key itself if no such message exists
     */
    public String get(String key, String bundle, String defaultValue)
    {
        return get(key, bundle, (Object[]) null, defaultValue);
    }

    /**
     * @param key
     * @param bundle
     * @param args
     * @param defaultValue
     * @return the localized message for the specified key or default value or
     *         the key itself if no such message exists
     */
    public String get(String key, String bundle, Object[] args,
                      String defaultValue)
    {
        String msg = null;
        MessageResources res = getResources(bundle);
        if (res != null)
        {
            if (exists(key, bundle))
            {
                if (args == null)
                {
                    msg = res.getMessage(this.locale, key);
                }
                else
                {
                    msg = res.getMessage(this.locale, key, args);
                }
            }

        }
        if (msg == null)
        {
            msg = (defaultValue != null) ? defaultValue : key;
        }

        return msg;
    }
}
