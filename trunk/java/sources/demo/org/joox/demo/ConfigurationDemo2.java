/***************************************************************************************************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************************************************************************/
package org.joox.demo;

import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;

import org.joox.configuration.Configuration;
import org.joox.environment.Environment;

/**
 * @author Berthold Hoevel
 */
public class ConfigurationDemo2 implements Observer
{
	/**
	 * Comment for <code>conf</code>
	 */
	protected static Configuration conf;

	/**
	 * @param args
	 */
	@SuppressWarnings("nls")
	public static void main(String[] args)
	{
		int rtncode = 0;
		try
		{
			conf = Environment.getInstance().getConfiguration("config");

			ConfigurationDemo2 myself = new ConfigurationDemo2();

			// Shows the persistant values
			for (Enumeration<String> e = conf.getKeys(); e.hasMoreElements();)
			{
				String key = e.nextElement();
				System.out.println("Key: " + key + " = " + conf.get(key)); //$NON-NLS-1$//$NON-NLS-2$
			}

			Environment.getInstance().sleep(7000);
			// Shows the persistant values
			for (Enumeration<String> e = conf.getKeys(); e.hasMoreElements();)
			{
				String key = e.nextElement();
				System.out.println("Key: " + key + " = " + conf.get(key)); //$NON-NLS-1$//$NON-NLS-2$
			}

			// TODO implement some feature demos
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			rtncode = -1;
		}
		finally
		{
			System.exit(rtncode);
		}
	}

	/**
	 * 
	 */
	protected ConfigurationDemo2()
	{
		conf.addObserver(this);
	}

	/**
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@SuppressWarnings("nls")
	public void update(Observable arg0, Object arg1)
	{
		// TODO Auto-generated method stub
		System.out.println("ConfigurationDemo.update(" + arg0 + ", " + arg1 + ")");
	}
}
