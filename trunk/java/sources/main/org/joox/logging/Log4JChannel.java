/***************************************************************************************************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************************************************************************/
/**
 * 
 */
package org.joox.logging;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * @author bert
 * 
 */
public class Log4JChannel extends AbstractLogChannel
{
	private String fatal = "Fatal"; //$NON-NLS-1$

	private String error = "Error"; //$NON-NLS-1$

	private String warn = "Warn"; //$NON-NLS-1$

	private String info = "Info"; //$NON-NLS-1$

	private String debug = "Debug"; //$NON-NLS-1$

	private String instance = "Instance"; //$NON-NLS-1$

	private static Logger logger = null;

	/**
	 * @see org.joox.logging.LogChannel#getParameters()
	 */
	@SuppressWarnings("nls")
	public List<String> getParameters()
	{
		List<String> result = new ArrayList<String>();
		result.add(this.fatal);
		result.add(this.error);
		result.add(this.warn);
		result.add(this.info);
		result.add(this.debug);
		result.add(this.instance);
		// result.add("");
		return result;
	}

	/**
	 * @see org.joox.logging.LogChannel#println(java.lang.String)
	 */
	public void println(int level, String text)
	{
		// TODO implement Log4JChannel.println
		logger.info(text);
	}

	/**
	 * @param debug
	 *            The debug to set.
	 */
	public void setDebug(String debug)
	{
		this.debug = debug;
	}

	/**
	 * @param error
	 *            The error to set.
	 */
	public void setError(String error)
	{
		this.error = error;
	}

	/**
	 * @param fatal
	 *            The fatal to set.
	 */
	public void setFatal(String fatal)
	{
		this.fatal = fatal;
	}

	/**
	 * @param info
	 *            The info to set.
	 */
	public void setInfo(String info)
	{
		this.info = info;
	}

	/**
	 * @param warn
	 *            The warn to set.
	 */
	public void setWarn(String warn)
	{
		this.warn = warn;
	}

	/**
	 * @param instance
	 *            The instance to set.
	 */
	public void setInstance(String instance)
	{
		logger = org.apache.log4j.Logger.getLogger(instance);
	}
}
