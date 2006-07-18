/**
 * 
 */
package org.joox.commandline;

import java.util.Locale;

public class StringOption extends Option
{
	public StringOption(char shortForm, String longForm)
	{
		super(shortForm, longForm, true);
	}

	protected Object parseValue(String arg, Locale locale)
	{
		return arg;
	}
}