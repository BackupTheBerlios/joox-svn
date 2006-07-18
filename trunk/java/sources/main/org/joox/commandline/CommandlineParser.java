/*	JOOX - The Java Toolbox ($Id: CommandlineParser.java 23 2005-03-16 14:25:34Z bert $)
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
* Created by bert
 * Last Changed by $Author: bert $
*/

//TODO: Arrays

package org.joox.commandline;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

/**
 * @author bert
 *
 */
public class CommandlineParser
{
	private String[] remainingArgs = null;

	private Hashtable options = new Hashtable(10);

	private Hashtable values = new Hashtable(10);

	/**
	 * @param opt
	 * @return
	 */
	private final Option addOption(Option opt)
	{
		this.options.put("-" + opt.shortForm(), opt);
		this.options.put("--" + opt.longForm(), opt);
		return opt;
	}

	/**
	 * @param shortForm
	 * @param longForm
	 * @return
	 */
	public final Option addStringOption(char shortForm, String longForm)
	{
		Option opt = new StringOption(shortForm, longForm);
		addOption(opt);
		return opt;
	}

	/**
	 * @param shortForm
	 * @param longForm
	 * @return
	 */
	public final Option addIntegerOption(char shortForm, String longForm)
	{
		Option opt = new IntegerOption(shortForm, longForm);
		addOption(opt);
		return opt;
	}

	/**
	 * @param shortForm
	 * @param longForm
	 * @return
	 */
	public final Option addDoubleOption(char shortForm, String longForm)
	{
		Option opt = new DoubleOption(shortForm, longForm);
		addOption(opt);
		return opt;
	}

	/**
	 * @param shortForm
	 * @param longForm
	 * @return
	 */
	public final Option addBooleanOption(char shortForm, String longForm)
	{
		Option opt = new BooleanOption(shortForm, longForm);
		addOption(opt);
		return opt;
	}

	/**
	 * @param o
	 * @return
	 */
	public final Object getOptionValue(Option o)
	{
		return this.values.get(o.longForm());
	}

	/**
	 * @return
	 */
	public final String[] getRemainingArgs()
	{
		return this.remainingArgs;
	}

	/**
	 * @param argv
	 * @throws IllegalOptionValueException
	 * @throws UnknownOptionException
	 */
	public final void parse(String[] argv) throws IllegalOptionValueException, UnknownOptionException
	{
		parse(argv, Locale.getDefault());
	}

	/**
	 * @param argv
	 * @param locale
	 * @throws IllegalOptionValueException
	 * @throws UnknownOptionException
	 */
	public final void parse(String[] argv, Locale locale) throws IllegalOptionValueException, UnknownOptionException
	{
		Vector otherArgs = new Vector();
		int position = 0;
		this.values = new Hashtable(10);
		while (position < argv.length)
		{
			String curArg = argv[position];
			if (curArg.startsWith("-"))
			{
				if (curArg.equals("--"))
				{ // end of options
					position += 1;
					break;
				}
				String valueArg = null;
				if (curArg.startsWith("--"))
				{ // handle --arg=value
					int equalsPos = curArg.indexOf("=");
					if (equalsPos != -1)
					{
						valueArg = curArg.substring(equalsPos + 1);
						curArg = curArg.substring(0, equalsPos);
					}
				}
				Option opt = (Option) this.options.get(curArg);
				if (opt == null) { throw new UnknownOptionException(curArg); }
				Object value = null;
				if (opt.wantsValue())
				{
					if (valueArg == null)
					{
						position += 1;
						valueArg = null;
						if (position < argv.length)
						{
							valueArg = argv[position];
						}
					}
					value = opt.getValue(valueArg, locale);
				}
				else
				{
					value = opt.getValue(null, locale);
				}
				this.values.put(opt.longForm(), value);
				position += 1;
			}
			else
			{
				break;
			}
		}
		for (; position < argv.length; ++position)
		{
			otherArgs.addElement(argv[position]);
		}

		this.remainingArgs = new String[otherArgs.size()];
		int i = 0;
		for (Enumeration e = otherArgs.elements(); e.hasMoreElements(); ++i)
		{
			this.remainingArgs[i] = (String) e.nextElement();
		}
	}

}