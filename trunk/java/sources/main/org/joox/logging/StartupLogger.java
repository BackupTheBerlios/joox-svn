/***************************************************************************************************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************************************************************************/
package org.joox.logging;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author bert
 * 
 */
public class StartupLogger extends AbstractLogger
{
	private Map primitives = new HashMap();

	private Map formats = new HashMap();

	/**
	 * @param debugged
	 * @param level
	 */
	@SuppressWarnings( { "boxing", "unchecked", "nls" })
	public StartupLogger(Class debugged, int level)
	{
		super(debugged);
		this.primitives.put("ALL", 0x7FFFFFFF); // $SUP-NLC$
		this.primitives.put("NUL", 0x00000000);
		this.primitives.put("VAR", 0x00000001);
		this.primitives.put("DBG", 0x00000002);// $SUP-NLC$
		this.primitives.put("TRC", 0x00000004);// $SUP-NLC$
		this.primitives.put("INF", 0x00000008);// $SUP-NLC$
		this.primitives.put("MSG", 0x00000010);// $SUP-NLC$
		this.primitives.put("WRN", 0x00000020);// $SUP-NLC$
		this.primitives.put("ERR", 0x00000040);// $SUP-NLC$
		this.primitives.put("FAT", 0x00000080);// $SUP-NLC$

		this.formats.put("VAR", "%s");
		this.formats.put("DBG", "%s");
		this.formats.put("TRC", "%m%s");
		this.formats.put("INF", "%s");
		this.formats.put("MSG", "%s");
		this.formats.put("WRN", "WARNING: %s");
		this.formats.put("ERR", ">>>>EXCEPTION: \"%e\"%n%oException catched by '%m'%n>>>>EXCEPTION STACKTRACE START<<<<%n>>>>%x%n>>>>EXCEPTION STACKTRACE END<<<<");
		this.formats.put("FAT", ">>>>FATAL ERROR: %e%n>>>>FATAL ERROR STACKTRACE START<<<<%n>>>>%x%n>>>>FATAL ERROR STACKTRACE END<<<<");

		this.mask = level;
	}

	/**
	 * @see org.joox.logging.Logger#printToChannel(int, java.lang.Object)
	 */
	@SuppressWarnings("nls")
	public void printToChannel(int priority, Object args)
	{ //
		if ((this.mask & priority) != 0)
		{
			String firstpart = getOutputprefix(); // First part is the same for every key
			synchronized (firstpart)
			{
				String output = format(priority, "", firstpart, args);

				// Searching the second part
				String key = null;
				int value = 0;
				String secondpart = "";
				Iterator iter = this.primitives.keySet().iterator();
				while (iter.hasNext())
				{
					key = (String) iter.next();
					value = ((Integer) this.primitives.get(key)).intValue(); // $SUP-MANR$
					if (((priority & value) != 0) && value < ((Integer) this.primitives.get("ALL")).intValue()) // $SUP-MANR$
					{
						secondpart = (String) this.formats.get(key); // The second part is added
						break; // Can now leave the loop
					}
				}
				// System.out.println( key + " = " + value + " : " + format);

				output += format(priority, firstpart, secondpart, args);
				printout(priority, output);
			}
		}
	}

	private void printout(int priority, String text)
	{
		if ((priority & (WRN | ERR | FAT)) != 0)
		{
			System.err.println(text);
			System.err.flush();
		}
		else
		{
			System.out.println(text);
			System.out.flush();
		}
	}

	/**
	 * @see org.joox.logging.AbstractLogger#getOutputprefix()
	 */
	@SuppressWarnings("nls")
	@Override
	protected String getOutputprefix()
	{
		return "[%d{LEFT, ,28,28,'yyyy.MM.dd HH:mm:ss,SSS z'} %g{RIGHT, ,20,20}.%t{LEFT, , 8,8} %C{LEFT, ,48,48}] %i";
	}

}
