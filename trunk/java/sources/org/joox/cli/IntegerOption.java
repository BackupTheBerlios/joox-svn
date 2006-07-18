/**
 * 
 */
package org.joox.cli;

import java.util.Locale;

public class IntegerOption extends Option
{
	public IntegerOption(char shortForm, String longForm)
	{
		super(shortForm, longForm, true);
	}

	protected Object parseValue(String arg, Locale locale) throws IllegalOptionValueException
	{
		try
		{
			return new Integer(arg);
		}
		catch (NumberFormatException e)
		{
			throw new IllegalOptionValueException(this, arg);
		}
	}
}