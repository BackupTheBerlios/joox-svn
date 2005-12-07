/*
 * JOOX - The Java Toolbox ($Id$) Copyright (C) 2004 by joox.org This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either
 * version 2 of the License. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA Created by bert
 */
package org.joox.tutorial;

import java.io.File;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;

import org.joox.configuration.Configuration;
import org.joox.generator.Generator;

/**
 * @author Berthold Hoevel
 */
public final class ConfigurationDemo implements Observer
{

	/**
	 * The Configuration of this Class. If you want to use the same configuration for each instance of this class the
	 * Configuration should be static. In other cases think of the the memory resources.
	 */
	protected static Configuration conf;

	static
	{
		// We have to initiate the Configuration only one time.
		try
		{
			// We use the Generator for get a Configuration instance.
			conf = Generator.generate(Configuration.class, new File("config.properties")); // TODO: Schwachsinn!! File und
																							// Generator gleichzeitig!
		}
		catch (Exception e)
		{
			// Normally this is a very serious problem but here we use only a Stacktrace as output.
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// First we iterate through all persistant values
		for (Enumeration<String> e = conf.getKeys(); e.hasMoreElements();)
		{
			String key = e.nextElement();
			System.out.println("Key: " + key + " = " + conf.get(key)); //$NON-NLS-1$//$NON-NLS-2$
		}

		// Now we instantiate this class and make this instance to an observer of the configuration.
		// The best way to do this is in the constuctor of this class.
		new ConfigurationDemo();

		// The Observer now registrate any changes from outside
		// (for example from other classes that use our configuration or manually changes in the Configuration datas)
		//
		// We simulate this now with the following code
		try
		{
			// First we get the configuration a second time (as an other class would do it)
			Configuration conf2 = Generator.generate(Configuration.class, new File("config.properties"));// TODO:
																											// Schwachsinn!!
																											// File und
																											// Generator
																											// gleichzeitig!
			// Changing a value
			conf2.set("test-key", "new-value");
			// Store the changes
			conf2.commit();
			// Give time for the system to react...
			Thread.sleep(1000 * 5);
			// In this time the update method should be called.

			// The value of the key is now changed, also in the persistant data pool (whatever it is).
			// Now we want to undo the changes.
			conf2.undo();
			// Give time for the system to react...
			Thread.sleep(1000 * 5);
			// In this time the update method should be called again.

			// Now we add the commandline arguments into the configuration
			conf.set(args);
			// The rule is that commandline arguments override the stored configuration
			System.out.println("cli value of 'test-key' is: " + conf.get("test-key"));
			Thread.sleep(1000 * 5);
			// Remember: we didn't commit the args. So they are only temporary active.
			// Without commiting we can use the values but they are not stored and they can be rolled back everytime.
			// Here we decided that the stored values should not be changed.
			// Without a commit, the changed values are not persistant.

			// Now we make a rollback to the persistant values
			conf.rollback();
			System.out.println("Stored value of 'test-key' is: " + conf.get("test-key"));
			Thread.sleep(1000 * 5);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		System.exit(0);
	}

	/**
	 * 
	 */
	protected ConfigurationDemo()
	{
		conf.addObserver(this);
	}

	/**
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable arg0, Object arg1)
	{
		System.out.println("The new value of 'test-key' is: " + conf.get("test-key"));
	}
}
