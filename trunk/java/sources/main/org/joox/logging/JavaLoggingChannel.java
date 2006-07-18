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
public class JavaLoggingChannel extends AbstractLogChannel
{
	private String severe = "Severe"; //$NON-NLS-1$

	private String warning = "Warning"; //$NON-NLS-1$

	private String info = "Info"; //$NON-NLS-1$

	private String config = "Config"; //$NON-NLS-1$

	private String fine = "Fine"; //$NON-NLS-1$

	private String finer = "Finer"; //$NON-NLS-1$

	private String finest = "Finest"; //$NON-NLS-1$

	/**
	 * @param config
	 *            The config to set.
	 */
	public void setConfig(String config)
	{
		this.config = config;
	}

	/**
	 * @param fine
	 *            The fine to set.
	 */
	public void setFine(String fine)
	{
		this.fine = fine;
	}

	/**
	 * @param finer
	 *            The finer to set.
	 */
	public void setFiner(String finer)
	{
		this.finer = finer;
	}

	/**
	 * @param finest
	 *            The finest to set.
	 */
	public void setFinest(String finest)
	{
		this.finest = finest;
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
	 * @param severe
	 *            The severe to set.
	 */
	public void setSevere(String severe)
	{
		this.severe = severe;
	}

	/**
	 * @param warning
	 *            The warning to set.
	 */
	public void setWarning(String warning)
	{
		this.warning = warning;
	}

	/**
	 * @see org.joox.logging.LogChannel#getParameters()
	 */
	@SuppressWarnings("nls")
	public List<String> getParameters()
	{
		List<String> result = new ArrayList<String>();
		result.add(this.severe);
		result.add(this.warning);
		result.add(this.info);
		result.add(this.config);
		result.add(this.fine);
		result.add(this.finer);
		result.add(this.finest);
		return result;
	}

	/**
	 * @see org.joox.logging.LogChannel#println(int, java.lang.String)
	 */
	@SuppressWarnings("nls")
	public void println(int level, String text)
	{
		// TODO implement JavaLoggingChannel.println
		System.out.println("NAT-" + text);
		System.out.flush();
	}
}
