/**
 * 
 */
package org.joox.cli;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class DoubleOption extends Option
{
	public DoubleOption(char shortForm, String longForm)
	{
		super(shortForm, longForm, true);
	}

	protected Object parseValue(String arg, Locale locale) throws IllegalOptionValueException
	{
		try
		{
			NumberFormat format = NumberFormat.getNumberInstance(locale);
			Number num = (Number) format.parse(arg);
			return new Double(num.doubleValue());
		}
		catch (ParseException e)
		{
			throw new IllegalOptionValueException(this, arg);
		}
	}
}