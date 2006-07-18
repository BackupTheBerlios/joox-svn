/**
 * 
 */
package org.joox.cli;


public class UnknownOptionException extends OptionException
{
	UnknownOptionException(String optionName)
	{
		super("unknown option '" + optionName + "'");
		this.optionName = optionName;
	}

	public String getOptionName()
	{
		return this.optionName;
	}

	String optionName = null;
}