/* JOOX - The Java Toolbox ($Id$)
 *
 *	Copyright (C) 2004 by joox.org
 *
 *	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Created by bert at Dec 22, 2004
 * Last Changed by $Author$
 */
package org.joox.generator;

import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * @author hoevbert
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Generator
{
	private final static String PROPERTYFILE = "generator.properties"; //$NON-NLS-1$

	private static Properties config;
	
	static
	{
		try
		{
			config = new Properties();
			config.load(new FileInputStream(PROPERTYFILE));
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(-1); //TODO Make constants for exit codes
		}
	}

	private Generator()
	{
		//Nothing to do
	}

	/** Creates a new object of a class.
	 * @param <T> Type 
	 * @param clazz the type of the new object
	 * @param args Arguments for the Constructor method
	 * @return The created new object which will be an instance of clazz
	 * @throws ClassNotFoundException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws SecurityException 
	 */
	public static <T> T generate(Class<T> clazz, Object... args) throws ClassNotFoundException,
					IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException,
					SecurityException
	{
		T result = null;
		Class<T> type;
		
		//Find the configured implementation of clazz or initiate it
		if(clazz.isInterface())
		{
			String key = clazz.getName();
			String implementation = config.getProperty(key);
			if (implementation == null) //if nothing configured...
			{
				throw new ClassNotFoundException("Try to find '" + clazz.getName() + "' but interfaces cannot be instanciated if there are not configured with an implementation."); //$NON-NLS-1$ //$NON-NLS-2$
			}
			type = (Class<T>) Class.forName(implementation);
		}
		else
		{
			type = clazz;
		}

		//find the correct constructor and use them for initialisation
//		if (type.isAssignableFrom(Singleton.class))
//		{
//			type.getMethod("instance", null); //$NON-NLS-1$
//		}
//		else
		{
			Constructor<T>[] constructors = type.getDeclaredConstructors();
			for (int i = 0; i < constructors.length; i++)
			{
				Class[] constargs = constructors[i].getParameterTypes();
				if (constargs.length == args.length)
				{
					boolean found = true;
					for (int j = 0; j < constargs.length; j++)
					{
						if (constargs[j].isAssignableFrom(args[j].getClass()) || constargs[j].isPrimitive())
						{
							continue;
						}
						found = false;
						break;
					}
					if (found)
					{
						result = constructors[i].newInstance(args);
						break;
					}
				}
			}
		}
		return result;
	}

	/** Creates a new object of clazz.
	 * @param <T> 
	 * @param clazz
	 * @return The created new object which will be an instance of clazz
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static <T> T generate(Class<T> clazz) throws InstantiationException, IllegalAccessException,
					ClassNotFoundException
	{
		T result;
		if(clazz.isInterface())
		{
			String key = clazz.getName();
			String implementation = config.getProperty(key);
			if (implementation == null)
			{
				throw new ClassNotFoundException("Try to find '" + clazz.getName() + "' but interfaces cannot be instanciated if there are not configured with an implementation."); //$NON-NLS-1$
			}
			result = (T) Class.forName(implementation).newInstance();
		}
		else
		{
			result = clazz.newInstance();
		}
		return result;
	}
}
