/***************************************************************************************************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************************************************************************/
// Fileheader
//
// TODO Documentation
package org.joox.environment;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.joox.commandline.CommandlineParser;
import org.joox.configuration.Configuration;
import org.joox.logging.Logger;
import org.joox.logging.StartupLogger;

/**
 * @author bert
 * 
 */
public abstract class Environment
{
	private static Environment environment = null;

	private static Configuration configuration = null;

	private static CommandlineParser parser = null;

	private static Logger logger = new StartupLogger(Environment.class, 0xF0);

	private static Map<Class, Logger> loggermap = new HashMap<Class, Logger>();

	/**
	 * 
	 */
	@SuppressWarnings("nls")
	protected static String environmentProperty = "org.joox.environment.DefaultEnvironment";

	/**
	 * 
	 */
	protected static String configurationProperty = null;

	private static String parserProperty = null;

	private static String loggerProperty = null;

	private static boolean initialized = false;

	/**
	 * @return The Environment (Singleton)
	 */
	@SuppressWarnings("nls")
	public static Environment getInstance()
	{
		logger.in(Logger.TRC, "getInstance", "");

		if (environment == null)
		{
			String environmentString = System.getProperty("org.joox.environment");
			if (environmentString == null || environmentString.length() == 0)
			{
				environmentString = environmentProperty;
			}

			try
			{
				environment = (Environment) Class.forName(environmentString).newInstance();
				environment.init(); // init constants staticly defined in the Environment implementation
				configuration = environment.getConfiguration("org.joox.environment");

				String environmentProp = configuration.get("org.joox.environment");
				// Reset if other environment is defined
				if (!environmentProperty.equals(environmentProp))
				{
					environmentProperty = environmentProp;
					getInstance();
				}
				// Reset if other configuration is defined
				String configurationProp = configuration.get("org.joox.configuration");
				if (!configurationProperty.equals(configurationProp))
				{
					configurationProperty = configurationProp;
					getInstance();
				}
				parserProperty = configuration.get("org.joox.commandline");
				loggerProperty = configuration.get("org.joox.logger");

				logger = environment.getLogger(Environment.class);

				configuration.getClass().getField("logger").set(null, getInstance().getLogger(configuration.getClass()));
			}
			catch (InstantiationException e)
			{
				throw new EnvironmentException(e);
			}
			catch (IllegalAccessException e)
			{
				throw new EnvironmentException(e);
			}
			catch (ClassNotFoundException e)
			{
				throw new EnvironmentException(e);
			}
			catch (IllegalArgumentException e)
			{
				throw new EnvironmentException(e);
			}
			catch (SecurityException e)
			{
				throw new EnvironmentException(e);
			}
			catch (NoSuchFieldException e)
			{
				throw new EnvironmentException(e);
			}
			initialized = true;
		}
		logger.out(Logger.TRC, environment);
		return environment;
	}

	public static boolean isInitialized()
	{
		return initialized ;
	}
	
	/**
	 * 
	 */
	protected Environment()
	{
		super();

	}

	/**
	 * 
	 */
	protected abstract void init();

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
	public <T> T generate(Class<T> clazz, Object... args) throws ClassNotFoundException,
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
	public <T> T generate(Class<T> clazz) throws InstantiationException, IllegalAccessException,
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
	
	/**
	 * @param clazz
	 *            The class which want to have logging output
	 * @return The Logger for this class
	 * @deprecated
	 */
	@SuppressWarnings("nls")
	public Logger getLogger(Class clazz)
	{
		logger.in(Logger.TRC, "getLogger", clazz);

		Logger log = loggermap.get(clazz);
		if (log == null)
		{
			String string = System.getProperty("org.joox.logger");
			if (string == null || string.length() == 0)
				string = loggerProperty;
			if (string != null)
			{
				log = (Logger) staticCall(string, null, clazz);
				loggermap.put(clazz, log);
				log.inf("logger initialised");
			}
		}

		logger.out(Logger.TRC, log);
		return log;
	}

	/**
	 * @param args
	 *            For parsing
	 * @return The configured Commandline Parser
	 * @deprecated
	 */
	@SuppressWarnings("nls")
	public CommandlineParser getCommandlineParser(String[] args)
	{
		logger.in(Logger.TRC, "getCommandlineParser", (Object[]) args);

		if (parser == null)
		{
			logger.inf("initializing parser");
			String string = System.getProperty("org.joox.commandline");
			if (string == null || string.length() == 0)
				string = parserProperty;
			String classname = string.split("#")[0];
			// String parameter = string.split("#")[1]; // will be optimized by compiler
			parser = (CommandlineParser) staticCall(classname, null, args);
		}

		logger.out(Logger.TRC, parser);
		return parser;
	}

	/**
	 * @param name
	 *            The name of the configuration
	 * @return The configuration
	 * @deprecated
	 */
	@SuppressWarnings("nls")
	public Configuration getConfiguration(String name)
	{
		logger.in(Logger.TRC, "getConfiguration", name);

		String string = System.getProperty("org.joox.configuration");
		if (string == null || string.length() == 0)
			string = configurationProperty;
		String[] token = string.split("#");
		String classname = token[0];
		String path = getClasspath();
		if (token.length > 1)
			path = string.split("#")[1];
		staticCall(classname, "setPath", path);

		Configuration result = (Configuration) staticCall(classname, "getInstance", name);
		
		logger.out(Logger.TRC, result);
		return result;
	}

	@SuppressWarnings("nls")
	private Object staticCall(String classname, String methodname, Object parameter)
	{
		logger.in(Logger.TRE, "instantiate", classname, methodname, parameter);

		Object result = null;
		try
		{
			Class objClass = Class.forName(classname);
			if (methodname != null)
			{
				Method method = objClass.getMethod(methodname, new Class[] { parameter.getClass() });
				result = method.invoke(null, new Object[] { parameter });
			}
			else
			{
				Constructor constructor = objClass.getConstructor(new Class[] { parameter.getClass() });
				result = constructor.newInstance(new Object[] { parameter });
			}
		}
		catch (NoSuchMethodException e) // This type of exception is hidden because it can't be recognized outside
		{
			throw new EnvironmentException(e);
		}
		catch (InvocationTargetException e) // This type of exception is hidden because it can't be recognized outside
		{
			throw new EnvironmentException(e);
		}
		catch (ClassNotFoundException e)
		{
			throw new EnvironmentException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new EnvironmentException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new EnvironmentException(e);
		}
		catch (InstantiationException e)
		{
			throw new EnvironmentException(e);
		}

		logger.out(Logger.TRE, result);
		return result;
	}

	/**
	 * @return The absolute path to the root package of this class-file
	 */
	@SuppressWarnings("nls")
	public String getClasspath()
	{ //
		logger.in(Logger.TRC, "getClasspath", "");

		String classpath = System.getProperty("java.class.path");
		String pathseparator = System.getProperty("path.separator");
		int cut = classpath.indexOf(pathseparator);
		if (cut > 0)
			classpath = classpath.substring(0, cut);

		logger.out(Logger.TRC, classpath);
		return classpath;
	}

	/**
	 * Hält den aktuellen Thread für die angegebene Zah lan Millisekunden an.
	 * 
	 * @param time
	 */
	@SuppressWarnings("nls")
	public void sleep(long time)
	{
		logger.in(Logger.TRC, "sleep", time);

		try
		{ //
			Thread.sleep(time);
		}
		catch (InterruptedException e)
		{ // no need for reaction
		}

		logger.out(Logger.TRC);
	}

	/**
	 * @param year
	 *            of Easter
	 * @return A Calendar with Easter as the given day
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings( { "nls" })
	public Calendar getEaster(int year)
	{
		logger.in(Logger.TRC, "getEaster", year);

		int century, n, k, i, j, l, month, day;
		century = year / 100;
		n = year - 19 * (year / 19);
		k = (century - 17) / 25;
		i = century - century / 4 - (century - k) / 3 + 19 * n + 15;
		i = i - 30 * (i / 30);
		i = i - (i / 28) * (1 - (i / 28) * (29 / (i + 1)) * ((21 - n) / 11));
		j = year + year / 4 + i + 2 - century + century / 4;
		j = j - 7 * (j / 7);
		l = i - j;
		month = 3 + (l + 40) / 44;
		day = l + 28 - 31 * (month / 4);

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, day);
		// new Date(year, month, day);

		logger.out(Logger.TRC, calendar);
		return calendar;
	}
}
