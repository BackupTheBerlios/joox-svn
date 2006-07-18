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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.StringTokenizer;

import org.joox.configuration.Configuration;
import org.joox.environment.Environment;

/**
 * @author bert
 * 
 */
class LoggingConfiguration implements Observer
{
	@SuppressWarnings("unused")
	private org.joox.logging.SyslogChannel lnkSyslogChannel;

	@SuppressWarnings("unused")
	private org.joox.logging.JmsChannel lnkJmsChannel;

	/**
	 * 
	 */
	@SuppressWarnings("nls")
	protected static Configuration conf = Environment.getInstance().getConfiguration("logging");

	@SuppressWarnings("unused")
	private JavaLoggingChannel lnkJavaLoggingChannel;

	@SuppressWarnings("unused")
	private Log4JChannel lnkLog4JChannel;

	@SuppressWarnings("unused")
	private NetLogChannel lnkNetLogChannel;

	@SuppressWarnings("unused")
	private FileLogChannel lnkFileLogChannel;

	@SuppressWarnings("unused")
	private SysErrLogChannel lnkSysErrLogChannel;

	@SuppressWarnings("unused")
	private SysOutLogChannel lnkSysOutLogChannel;

	/**
	 * 
	 */
	@SuppressWarnings("nls")
	protected final static String BUNDLE_NAME = "logging";

	/** Keyword in logging.properties for primitive levels */
	@SuppressWarnings("nls")
	public final static String PRIMITIVE = "PRIMITIVE";

	/** Keyword in logging.properties for composed levels */
	@SuppressWarnings("nls")
	public final static String LEVEL = "LEVEL";

	/** Keyword in logging.properties for format informations */
	@SuppressWarnings("nls")
	public final static String FORMAT = "FORMAT";

	/** Keyword in logging.properties for output channels */
	@SuppressWarnings("nls")
	public final static String CHANNEL = "CHANNEL";

	/**
	 * 
	 */
	@SuppressWarnings("nls")
	protected final static String DIVIDER = "+, \t";

	/** List of all primitives */
	private final static Map<String, Integer> primitives = new HashMap<String, Integer>();

	private final static Map<Integer, String> primitivesinverse = new HashMap<Integer, String>();

	/** List of all primitives */
	private final static Map<Integer, List<LogChannel>> channels = new HashMap<Integer, List<LogChannel>>();

	/** List of all formats */
	private final static Map<String, String> formats = new HashMap<String, String>();

	/** List of all composed Levels */
	private final static Map<String, Comparable> levels = new HashMap<String, Comparable>();

	/** Logging rules for each class or package */
	// private static ResourceBundle rules = ResourceBundle.getBundle(BUNDLE_NAME);;
	private static LoggingConfiguration myself = null;

	/** Time of rereading the Property-File in Minutes. */
	private int seconds = 5;

	/**
	 * @return x
	 */
	@SuppressWarnings("nls")
	static public LoggingConfiguration createLogProperty()
	{
		if (conf == null)
			conf = Environment.getInstance().getConfiguration("logging");
		if (myself == null)
			myself = new LoggingConfiguration();
		return myself;
	}

	/**
	 * @param key
	 * @return x
	 */
	static protected String getProperty(String key)
	{
		return conf.get(key);
	}

	/**
	 * @return x
	 */
	public static Set<String> getPrimitiveKeys()
	{
		return primitives.keySet();
	}

	/**
	 * @param key
	 * @return x
	 */
	public static Integer getPrimitive(String key)
	{
		return primitives.get(key);
	}

	/**
	 * @param key
	 * @return x
	 */
	public static String getPrimitive(Integer key)
	{
		return primitivesinverse.get(key);
	}

	/**
	 * @param key
	 * @return x
	 */
	public static String getFormat(String key)
	{
		return formats.get(key);
	}

	/**
	 * @param key
	 * @return x
	 */
	public static List getChannel(Integer key)
	{
		return channels.get(key);
	}

	/**
	 * @param token
	 * @return x
	 */
	public static Integer getLevel(String token)
	{
		return (Integer) levels.get(token);
	}

	private LoggingConfiguration()
	{
		conf.addObserver(this);
		update(null, null);
	}

	/**
	 * @param observable
	 * @param args
	 * 
	 */
	@SuppressWarnings("nls")
	synchronized public void update(Observable observable, Object args)
	{
		// reloadClass(ResourceBundle.class.getName(), true);
		// rules = ResourceBundle.getBundle(BUNDLE_NAME);
		// System.out.println(rules.hashCode() + " --- " + new Date());

		// /////////////////////////////////////////////////////////////////////
		// misc
		// this.seconds = Integer.valueOf(conf.get("LOGCONFIG.REFRESHTIME")).intValue();

		// /////////////////////////////////////////////////////////////////////
		// primitives
		String primitive = conf.get(PRIMITIVE);
		// System.out.println(primitive);
		StringTokenizer tokens = new StringTokenizer(primitive, DIVIDER);
		while (tokens.hasMoreElements())
		{
			String element = (String) tokens.nextElement();
			Integer value = Integer.valueOf(conf.get(PRIMITIVE + "." + element), 16);
			primitives.put(element, value);
			primitivesinverse.put(value, element);
			// levels.put(element, value);
		}
		// System.out.println(PRIMITIVE + " = " + primitives); //$NON-NLS-1$

		// /////////////////////////////////////////////////////////////////////
		// levels
		levels.clear();
		String level = conf.get(LEVEL);
		// System.out.println(level);
		tokens = new StringTokenizer(level, DIVIDER);
		while (tokens.hasMoreElements())
		{
			String element = (String) tokens.nextElement();
			levels.put(element, conf.get(LEVEL + "." + element));
		}

		Set<String> keys = levels.keySet();
		// System.out.println("All defined keys: " + keys);
		Iterator<String> iter = keys.iterator();
		while (iter.hasNext())
		{
			String key = iter.next();
			String value = (String) levels.get(key);
			StringTokenizer tokenizer = new StringTokenizer(value, DIVIDER);
			int lvl = 0;
			while (tokenizer.hasMoreElements())
			{
				String token = (String) tokenizer.nextElement();
				// System.out.println(token + " = " + primitives.get(token));
				lvl |= primitives.get(token).intValue();
			}
			levels.put(key, new Integer(lvl));
		}
		levels.putAll(primitives);

		// /////////////////////////////////////////////////////////////////////
		// channels
		String channel = conf.get(CHANNEL);
		// System.out.println(channel);
		tokens = new StringTokenizer(channel, DIVIDER);
		while (tokens.hasMoreElements())
		{
			String element = (String) tokens.nextElement();
			String channelclassname = conf.get(CHANNEL + "." + element);

			List<Integer> levellist = getLevelList(conf.get(CHANNEL + "." + element + "." + LEVEL));
			for (int i = 0; i < levellist.size(); i++)
			{
				Integer key = levellist.get(i); // key is a primitive bitmask
				List<LogChannel> channellist = channels.get(key);
				if (channellist == null)
				{
					channellist = new ArrayList<LogChannel>();
					channels.put(key, channellist);
				}
				LogChannel channelinstance;
				try
				{
					channelinstance = (LogChannel) Class.forName(channelclassname).newInstance();
				}
				catch (InstantiationException e1)
				{
					throw new LoggingException(e1);
				}
				catch (IllegalAccessException e1)
				{
					throw new LoggingException(e1);
				}
				catch (ClassNotFoundException e1)
				{
					throw new LoggingException(e1);
				}
				List parameters = channelinstance.getParameters();
				for (int j = 0; j < parameters.size(); j++)
				{
					try
					{
						Method setter = channelinstance.getClass().getMethod("set" + parameters.get(j), new Class[] { String.class });
						String value = conf.get(CHANNEL + "." + element + "." + parameters.get(j));
						setter.invoke(channelinstance, new Object[] { value });
					}
					catch (SecurityException e)
					{
						throw new LoggingException("Not allowed to execute 'set" + parameters.get(j) + "()' for class '" + channelinstance.getClass() + "'");
					}
					catch (NoSuchMethodException e)
					{
						throw new LoggingException("Unknown name of parameter '" + parameters.get(j) + "' found in configuration");
					}
					catch (IllegalAccessException e)
					{
						throw new LoggingException("Not allowed to execute 'set" + parameters.get(j) + "()' for class '" + channelinstance.getClass() + "'");
					}
					catch (Exception e)
					{
						throw new LoggingException("Unknown exception during invoking of 'set" + parameters.get(j) + "()' in class '" + channelinstance.getClass() + "'");
					}
				}
				channellist.add(channelinstance);
			}
		}
		// System.out.println(CHANNEL + " = " + channels); //$NON-NLS-1$

		// /////////////////////////////////////////////////////////////////////
		// format
		String format = conf.get(PRIMITIVE);
		// System.out.println(level);
		tokens = new StringTokenizer(format, DIVIDER);
		while (tokens.hasMoreElements())
		{
			String element = (String) tokens.nextElement();
			formats.put(element, conf.get(FORMAT + "." + element));
		}
		// System.out.println("Loggingformat = " + formats); //$NON-NLS-1$

		// System.out.println("Logginglevel = " + levels); //$NON-NLS-1$ //$NON-NLS-2$

		// System.out.println("All defined keys: " + keys);
		// for (Iterator iter = keys.iterator(); iter.hasNext();)
		// {
		// String key = (String) iter.next();
		// System.out.println(
		// new Field(Justify.RIGHT, null, 16, 16, key) + ": " + new Field(null, Format.BIN, 0, -1, levels.get(key)));
		// }
	}

	/**
	 * @param levelstring
	 * @return x
	 */
	@SuppressWarnings("nls")
	private List<Integer> getLevelList(String levelstring)
	{
		// TODO kann derzeit nur primitives verarbeiten. Erweitern auf levels
		// TODO kann derzeit nur primitives verarbeiten. Erweitern Subtraktion

		List<Integer> result = new ArrayList<Integer>();
		StringTokenizer leveltokens = new StringTokenizer(levelstring, DIVIDER);
		while (leveltokens.hasMoreElements())
		{
			String token = leveltokens.nextToken();
			if (!token.startsWith("-"))
			{
				Integer bitmask = primitives.get(token);
				result.add(bitmask);
				// result.add(token);
			}
		}
		return result;
	}
}
