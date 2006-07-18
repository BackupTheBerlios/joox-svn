/***************************************************************************************************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************************************************************************/
package org.joox.logging;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author bert
 * 
 */
public class DefaultLogger extends AbstractLogger
{
	/**
	 * 
	 */
	protected static LoggingConfiguration property;

	/**
	 * @param debugged
	 */
	@SuppressWarnings("nls")
	public DefaultLogger(Class debugged)
	{
		super(debugged);
		String rule = null;
		property = LoggingConfiguration.createLogProperty();
		String marker = debugged.getName();
		try
		{
			for (String parent = marker; parent.length() > 0; parent = parent.substring(0, parent.lastIndexOf(".")))
			{
				// System.out.println("parent = " + parent);
				try
				{
					rule = LoggingConfiguration.getProperty(parent);
					// System.out.println(parent + " = " + rule);
					break;
				}
				catch (RuntimeException e)
				{ // Nothing to do: No rule found!
					// System.out.println(parent + " = " + rule);
				}
			}
		}
		catch (StringIndexOutOfBoundsException e)
		{
			rule = LoggingConfiguration.getProperty("DEFAULT");
		}
		// System.out.println(marker + "=" + rule);

		if (rule == null)
			rule = LoggingConfiguration.getProperty("DEFAULT");
		StringTokenizer tokenizer = new StringTokenizer(rule, "-+, \t", true);
		int level = 0;
		boolean flag = false;

		while (tokenizer.hasMoreElements())
		{
			String token = (String) tokenizer.nextElement();
			// System.out.println("Token: [" + token + "]");
			if (token.equals("-"))
			{
				flag = true;
			}
			else if (token.equals("+"))
			{
				flag = false;
			}
			else
			{
				if (!(token.equals(",") || token.equals(" ")))
				{
					Integer integer = LoggingConfiguration.getLevel(token);
					if (flag)
					{
						level ^= integer.intValue();
					}
					else
					{
						level |= integer.intValue();
					}
					flag = false;
				}
				this.mask = level;
				// System.out.println(token + " " + level);
			}
		}
	}

	/**
	 * @see org.joox.logging.Logger#printToChannel(int, java.lang.Object)
	 */
	@SuppressWarnings("nls")
	public void printToChannel(int priority, Object args)
	{ //
		String firstpart = LoggingConfiguration.getFormat("ALL"); // First part is the same for every key
		synchronized (firstpart)
		{
			String prefixInset = format(priority, "", firstpart, args);
			// String prefixNoInset = format(priority & TRC, firstpart, args);

			// Searching the second part
			String key; // $SUP-EIAV$
			int value = 0;
			String secondpart = "";
			Iterator iter = LoggingConfiguration.getPrimitiveKeys().iterator();
			while (iter.hasNext())
			{
				key = (String) iter.next();
				value = LoggingConfiguration.getPrimitive(key).intValue(); // $SUP-MANR$
				if (((priority & value) != 0) && value < LoggingConfiguration.getPrimitive("ALL").intValue()) // $SUP-MANR$
				{
					secondpart = LoggingConfiguration.getFormat(key); // The second part is added
					break; // Can leave the loop
				}
			}
			// System.out.println( key + " = " + value + " : " + format);

			String text = format(priority, prefixInset, secondpart, args);
			printout(priority, prefixInset, "", text);
		}
	}

	private void printout(int priority, String prefix, String indent, String text)
	{
		// TODO Different output channels per thread
		Integer key = new Integer(priority);
		List channellist = LoggingConfiguration.getChannel(key);
		// System.out.println(LogProperty.channels);
		// System.out.println(channellist);
		if (channellist != null)
		{
			for (int i = 0; i < channellist.size(); i++)
			{
				LogChannel channel = (LogChannel) channellist.get(i);
				channel.println(priority, prefix + indent + text);
			}
		}
	}

	/**
	 * @see org.joox.logging.AbstractLogger#getOutputprefix()
	 */
	@SuppressWarnings("nls")
	@Override
	protected String getOutputprefix()
	{
		return LoggingConfiguration.getFormat("ALL");
	}
}
