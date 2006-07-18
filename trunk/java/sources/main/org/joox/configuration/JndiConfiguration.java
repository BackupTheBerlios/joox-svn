/***************************************************************************************************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************************************************************************/
/**
 * 
 */
package org.joox.configuration;

import java.util.Enumeration;

import org.joox.logging.Logger;
import org.joox.logging.StartupLogger;

/**
 * @author bert
 * 
 */
public class JndiConfiguration extends AbstractConfiguration
{
	/**
	 * 
	 */
	public static Logger logger = new StartupLogger(JndiConfiguration.class, 0xF0);

	/**
	 * @see org.joox.configuration.Configuration#refresh()
	 */
	@SuppressWarnings("nls")
	public void refresh()
	{
		logger.in(Logger.TRC, "refresh", "");
		logger.out(Logger.TRC, "");
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.joox.configuration.Configuration#getKeys()
	 */
	@SuppressWarnings("nls")
	public Enumeration<String> getKeys()
	{
		// TODO Auto-generated method stub
		logger.in(Logger.TRC, "getKeys", "");
		logger.out(Logger.TRC, null);
		return null;
	}

	/**
	 * @see org.joox.configuration.Configuration#get(java.lang.String)
	 */
	@SuppressWarnings("nls")
	public String get(String key)
	{
		// TODO Auto-generated method stub
		logger.in(Logger.TRC, "get", "");
		logger.out(Logger.TRC, null);
		return null;
	}

	/**
	 * @see org.joox.configuration.Configuration#set(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("nls")
	public void set(String key, String value)
	{
		// TODO Auto-generated method stub
		logger.in(Logger.TRC, "set", "");
		logger.out(Logger.TRC);
	}

	/**
	 * @see org.joox.configuration.Configuration#store()
	 */
	@SuppressWarnings("nls")
	public void store()
	{
		// TODO Auto-generated method stub
		logger.in(Logger.TRC, "store", "");
		logger.out(Logger.TRC);
	}

	/**
	 * @see org.joox.configuration.Configuration#add(java.lang.String, java.lang.String)
	 */
	public void add(String key, String value)
	{
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.joox.configuration.Configuration#commit()
	 */
	public void commit()
	{
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.joox.configuration.Configuration#getUndoLimit()
	 */
	public int getUndoLimit()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see org.joox.configuration.Configuration#getUndoPointer()
	 */
	public int getUndoPointer()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see org.joox.configuration.Configuration#redo()
	 */
	public void redo()
	{
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.joox.configuration.Configuration#rollback()
	 */
	public void rollback()
	{
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.joox.configuration.Configuration#set(java.lang.String[])
	 */
	public void set(String... args)
	{
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.joox.configuration.Configuration#undo()
	 */
	public void undo()
	{
		// TODO Auto-generated method stub
		
	}
}
