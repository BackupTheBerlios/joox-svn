/**
 * 
 */
package org.joox.cli;


public class IllegalOptionValueException extends OptionException
{
	public IllegalOptionValueException(Option opt, String value)
	{
		super("illegal value '" + value + "' for option -" + opt.shortForm() + "/--" + opt.longForm());
		this.option = opt;
		this.value = value;
	}

	public Option getOption()
	{
		return this.option;
	}

	public String getValue()
	{
		return this.value;
	}

	Option option;

	String value;
}