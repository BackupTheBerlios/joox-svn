/**
 * 
 */
package org.joox.cli;

import java.util.Locale;

public abstract class Option
{

	public Option(char shortForm, String longForm, boolean wantsValue)
	{
		if (longForm == null) throw new IllegalArgumentException("null arg forms not allowed");
		this.shortForm = new String(new char[] { shortForm });
		this.longForm = longForm;
		this.wantsValue = wantsValue;
	}

	public String shortForm()
	{
		return this.shortForm;
	}

	public String longForm()
	{
		return this.longForm;
	}

	public boolean wantsValue()
	{
		return this.wantsValue;
	}

	public final Object getValue(String arg, Locale locale) throws IllegalOptionValueException
	{
		if (this.wantsValue)
		{
			if (arg == null) { throw new IllegalOptionValueException(this, ""); }
			return this.parseValue(arg, locale);
		}
		else
		{
			return Boolean.TRUE;
		}
	}

	protected Object parseValue(String arg, Locale locale) throws IllegalOptionValueException
	{
		return null;
	}

	private String shortForm = null;

	private String longForm = null;

	private boolean wantsValue = false;
}