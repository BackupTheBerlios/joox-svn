/*	JOOX - The Java Toolbox ($Id$)
*
*	Copyright (C) 2004 by joox.org
*
*	This program is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License
* as published by the Free Software Foundation; either version 2 of the License.
*
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied 
* warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
* See the GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
* Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
* 
* Created by bert
 * Last Changed by $Author$
*/
package org.joox.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author Berthold Hoevel
 * 
 * 
 */
final public class PropertyConfiguration extends AbstractConfiguration implements Runnable
{
	/**
	 * Comment for <code>configurations</code>
	 */
	protected static HashMap<File, PropertyConfiguration> configurations = new HashMap<File, PropertyConfiguration>();
	
	/**
	 * @param name
	 * @return a Singleton
	 */
	public static Configuration instance(File file)
	{
		//File file = new File(name + ".properties"); //$NON-NLS-1$
		PropertyConfiguration instance = null;
		if(configurations.containsKey(file))
			instance = configurations.get(file);
		else
		{
			instance = new PropertyConfiguration(file);
			configurations.put(file, instance);
			instance.file = file;
			instance.fileupdatetime = file.lastModified();
			instance.rollback();
			(new Thread(instance)).start();
		}
		return instance;
	}
	
	private static Configuration instance(File file, PropertyConfiguration myself)
	{
		//File file = new File(name + ".properties"); //$NON-NLS-1$
		PropertyConfiguration instance = null;
		if(configurations.containsKey(file))
		{
			instance = configurations.get(file);
			myself.file = instance.file;
			myself.conf = instance.conf;
		}
		else
		{
			instance = myself;
			configurations.put(file, instance);
			instance.file = file;
			instance.rollback();
			(new Thread(instance)).start();
		}
		return instance;
	}
	
	/**
	 * Comment for <code>conf</code>
	 */
	protected Properties conf = new Properties();
	
	/**
	 * Comment for <code>file</code>
	 */
	protected File file;
	protected File backup;
	
	protected long fileupdatetime;
	
	private int seconds = 2; //TODO make it configurable
	private int undoLimit = 1;

	/**
	 * @param file
	 */
	public PropertyConfiguration(File file)
	{
		instance(file, this);
		this.file = file;
	}
	
	/**
	 * @param filename
	 */
	public PropertyConfiguration(String filename)
	{
		this(new File(filename + ".properties")); //$NON-NLS-1$
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		boolean looping = true;
		while(looping)
		{
			try
			{ //
				Thread.sleep(1000 * this.seconds);
				rollback();
				//System.out.println("PING");//TO DO delete this line
			}
			catch (InterruptedException e)
			{ 
				looping = false;
			}
		}
	}

	/**
	 * @see org.joox.configuration.Configuration#rollback()
	 */
	public synchronized void rollback()
	{
		try
		{
			if(this.fileupdatetime != this.file.lastModified())
			{
				this.setChanged();
				this.fileupdatetime = this.file.lastModified();
			}
			//System.out.println(file.getAbsolutePath());
			this.file.createNewFile();
			FileInputStream istream = new FileInputStream(this.file);
			this.conf.load(istream);
			this.notifyObservers();
			this.clearChanged();
			istream.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @see org.joox.configuration.Configuration#get(java.lang.String)
	 */
	public String get(String key)
	{
		return this.conf.getProperty(key);
	}

	/**
	 * @see org.joox.configuration.Configuration#set(java.lang.String, java.lang.String)
	 */
	public void set(String key, String value)
	{
		if(key != null && this.conf.get(key) != null && (!this.conf.get(key).equals(value)))
		{
			if(value==null)
				this.conf.remove(key);
			else
				this.conf.setProperty(key, value);
			this.setChanged();
		}
	}

	/** @return
	 * @see org.joox.configuration.Configuration#getKeys()
	 */
	@SuppressWarnings("unchecked")
	public Enumeration  getKeys()
	{
		return this.conf.keys();
	}

	/**
	 * @see org.joox.configuration.Configuration#commit()
	 */
	public void commit()
	{
		FileOutputStream ostream = null;
		try
		{
			String filename = this.file.getName();
			this.backup = new File(filename+".bak");			
			if(this.backup.exists()) this.backup.delete();
			this.file.renameTo(this.backup);
			this.file = new File(filename);
			ostream = new FileOutputStream(this.file);
			this.conf.store(ostream, filename);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ostream.close();
			}
			catch (Throwable e1)
			{
				e1.printStackTrace();
			}
		}
		this.notifyObservers();
	}

	/**
	 * @see org.joox.configuration.Configuration#add(java.lang.String, java.lang.String)
	 */
	public void add(String key, String value)
	{
		if(key != null && this.conf.get(key) == null)
		{
			if(value!=null)
				this.conf.setProperty(key, value);
			this.setChanged();
		}
	}

	/**
	 * @see org.joox.configuration.Configuration#undo()
	 */
	public void undo()
	{
		String filename = this.file.getName();
		
		this.backup.renameTo(new File(filename+".tmp"));
		this.file.renameTo(new File(filename+".bak"));
		(new File(filename+".tmp")).renameTo(new File(filename));
		(new File(filename+".tmp")).delete();
		
		this.file = new File(filename);
		this.backup = new File(filename+".bak");
		
		File swap = this.backup;
		this.backup = this.file;
		this.file = swap;
		//this.fileupdatetime = this.file.lastModified();
		rollback();
	}

	/**
	 * @see org.joox.configuration.Configuration#redo()
	 */
	public void redo()
	{
		undo();
	}

	/**
	 * @see org.joox.configuration.Configuration#getUndoLimit()
	 */
	public int getUndoLimit()
	{
		return this.undoLimit;
	}

	/**
	 * @see org.joox.configuration.Configuration#getUndoPointer()
	 */
	public int getUndoPointer()
	{
		return 0;
	}

	/**
	 * @see org.joox.configuration.Configuration#refresh()
	 */
	public void refresh()
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
	 * @see org.joox.configuration.Configuration#store()
	 */
	public void store()
	{
		// TODO Auto-generated method stub
		
	}
}
