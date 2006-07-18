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

/**
 * @author bert
 * 
 */
public class NetLogChannel extends AbstractLogChannel
{
	private String server = "Server"; //$NON-NLS-1$

	private String port = "Port"; //$NON-NLS-1$

	/**
	 * @see org.joox.logging.LogChannel#getParameters()
	 */
	@SuppressWarnings("nls")
	public List getParameters()
	{
		List<String> result = new ArrayList<String>();
		result.add(this.server);
		result.add(this.port);
		return result;
	}

	/**
	 * @see org.joox.logging.LogChannel#println(java.lang.String)
	 */
	@SuppressWarnings("nls")
	public void println(int level, String text)
	{
		// TODO Implement NetLogChannel
		System.out.println(this.server + ":" + this.port + "-" + text);
		System.out.flush();
	}

	/**
	 * @param port
	 *            The port to set.
	 */
	void setPort(String port)
	{
		this.port = port;
	}

	/**
	 * @param server
	 *            The server to set.
	 */
	void setServer(String server)
	{
		this.server = server;
	}
}
