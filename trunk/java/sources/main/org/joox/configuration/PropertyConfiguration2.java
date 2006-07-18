/***************************************************************************************************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************************************************************************/
package org.joox.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import org.joox.environment.Environment;
import org.joox.logging.Logger;
import org.joox.logging.StartupLogger;

/**
 * 
 * 
 * @author Berthold Hoevel
 */
public class PropertyConfiguration2 extends AbstractConfiguration
{
	/**
	 * 
	 */
	public static Logger logger = new StartupLogger(PropertyConfiguration2.class, 0xF0);

	private long milliseconds = 5000; // TODO make it configurable

	private long timemillis = 0;

	/**
	 * Comment for <code>configurations</code>
	 */
	protected static HashMap<File, PropertyConfiguration2> configurations = new HashMap<File, PropertyConfiguration2>();

	/**
	 * 
	 */
	protected static String path = null;

	/**
	 * Comment for <code>conf</code>
	 */
	protected Properties conf = new Properties();

	/**
	 * Comment for <code>file</code>
	 */
	protected File file;

	/**
	 * @param newpath
	 */
	@SuppressWarnings("nls")
	public static void setPath(String newpath)
	{
		logger.in("setPath", newpath);
		path = newpath;
		logger.out();
	}

	/**
	 * @param name
	 * @return a Singleton
	 */
	@SuppressWarnings("nls")
	public static Configuration getInstance(String name)
	{
		logger.in("getInstance", name);
		if (path == null)
			path = Environment.getInstance().getClasspath();
		File file = new File(path + "/" + name + ".properties");
		PropertyConfiguration2 instance = null; // $SUP-TVUSP$
		if (configurations.containsKey(file))
		{
			instance = configurations.get(file);
		}
		else
		{
			instance = new PropertyConfiguration2(file, name);
			configurations.put(file, instance);
			// Logger logr = Environment.getInstance().getLogger(PropertyConfiguration.class);
			// if(logr != null)
			// logger = logr;
		}
		logger.out(instance);
		return instance;
	}

	@SuppressWarnings("nls")
	private PropertyConfiguration2(File file, String name)
	{
		logger.in(Logger.TRE, "<init>", file, name);
		this.file = file;
		refresh();

		// Create copy for local user
		String filename = System.getProperty("user.home") + "/.joox/" + name + ".properties";
		this.file = new File(filename);
		refresh();
		// (new Thread(this)).start();
		logger.out(Logger.TRE);
	}

	/**
	 * @see org.joox.configuration.Configuration#refresh()
	 */
	@SuppressWarnings("nls")
	public synchronized void refresh()
	{
		logger.in("refresh", "");
		logger.dbg("" + this.file);
		long newtime = (new Date()).getTime();
		if ((this.timemillis + this.milliseconds) < newtime && this.file.lastModified() > this.timemillis)
		{
			this.timemillis = newtime;

			FileInputStream istream = null;
			try
			{
				logger.inf("Refreshing '" + this.file.getName() + "'");
				if (!this.file.exists())
				{
					(new File(this.file.getParent())).mkdirs();
					this.file.createNewFile();
					store();
				}
				else
				{
					istream = new FileInputStream(this.file);
					this.conf.load(istream);
					this.setChanged();
					this.notifyObservers();
					istream.close();
					istream = null;
				}
			}
			catch (FileNotFoundException e)
			{
				logger.err("refresh", e);
			}
			catch (IOException e)
			{
				logger.err("refresh", e);
			}
			finally
			{
				if (istream != null)
					try
					{
						istream.close();
					}
					catch (IOException e)
					{
						// Nothing to do here
					}
			}
		}
		else
		{
			logger.dbg("Not refreshed.");
		}
		logger.out();
	}

	/**
	 * @see org.joox.configuration.Configuration#store()
	 */
	@SuppressWarnings("nls")
	public void store()
	{
		logger.in("store", "");
		FileOutputStream ostream = null;
		try
		{
			ostream = new FileOutputStream(this.file);
			logger.dbg("Storing " + this.file.getName());
			this.conf.store(ostream, this.file.getName());
			ostream.close();
			ostream = null;
			this.notifyObservers();
		}
		catch (FileNotFoundException e)
		{
			logger.err("store", e);
		}
		catch (IOException e)
		{
			logger.err("store", e);
		}
		finally
		{
			if (ostream != null)
			{
				try
				{
					ostream.close();
				}
				catch (IOException e)
				{
					// Nothing to do here
				}
			}
		}
		logger.out();
	}

	/**
	 * @see org.joox.configuration.Configuration#get(java.lang.String)
	 */
	@SuppressWarnings("nls")
	public String get(String key)
	{
		logger.in("get", key);
		refresh();
		String result = this.conf.getProperty(key);
		logger.out(result);
		return result;
	}

	/**
	 * @see org.joox.configuration.Configuration#set(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("nls")
	public void set(String key, String value)
	{
		logger.in("set", key, value);
		if (key != null && this.conf.get(key) != null && (!this.conf.get(key).equals(value)))
		{
			refresh();
			if (value == null)
			{
				this.conf.remove(key);
			}
			else
			{
				this.conf.setProperty(key, value);
			}
			this.setChanged();
		}
		logger.out();
	}

	/**
	 * @return The keys of all properties
	 * @see org.joox.configuration.Configuration#getKeys()
	 */
	@SuppressWarnings( { "unchecked", "nls" })
	public Enumeration getKeys()
	{
		logger.in("getKeys", "");
		refresh();
		Enumeration result = this.conf.keys();
		logger.out(result);
		return result;
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
