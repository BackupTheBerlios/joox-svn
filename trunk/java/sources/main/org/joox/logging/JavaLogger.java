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
public class JavaLogger extends AbstractLogger
{
	private JavaLoggingChannel channel = null;

	/**
	 * @param debugged
	 */
	@SuppressWarnings("nls")
	public JavaLogger(Class debugged)
	{
		super(debugged);
		this.channel = new JavaLoggingChannel();
		this.channel.setSevere("FAT ERR");
		this.channel.setWarning("WRN");
		this.channel.setInfo("MSG");
		this.channel.setConfig("INF");
		this.channel.setFine("VAR");
		this.channel.setFiner("DBG");
		this.channel.setFinest("ALL -FAT -ERR -WRN -MSG -INF -VAR -DBG");
	}

	/**
	 * @see org.joox.logging.Logger#printToChannel(int, java.lang.Object)
	 */
	public void printToChannel(int priority, Object args)
	{ //
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
