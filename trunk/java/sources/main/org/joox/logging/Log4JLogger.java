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

/**
 * @author bert
 * 
 */
public class Log4JLogger extends AbstractLogger
{
	private Log4JChannel channel = null;

	/**
	 * @param debugged
	 */
	@SuppressWarnings("nls")
	public Log4JLogger(Class debugged)
	{
		super(debugged);
		this.channel = new Log4JChannel();
		this.channel.setFatal("FAT");
		this.channel.setError("ERR");
		this.channel.setWarn("WRN");
		this.channel.setInfo("INF MSG");
		this.channel.setDebug("ALL -FAT -ERR -WRN -MSG -INF");
	}

	/**
	 * @see org.joox.logging.Logger#printToChannel(int, java.lang.Object)
	 */
	public void printToChannel(int priority, Object args)
	{
		if ((this.mask & priority) != 0)
		{
			this.channel.println(priority, args.toString());
		}
	}

	/**
	 * @see org.joox.logging.AbstractLogger#getOutputprefix()
	 */
	@SuppressWarnings("nls")
	@Override
	protected String getOutputprefix()
	{
		return "";
	}
}
