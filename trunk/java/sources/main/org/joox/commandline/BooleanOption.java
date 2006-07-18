/**
 * 
 */
package org.joox.commandline;

public class BooleanOption extends Option
{
	public BooleanOption(char shortForm, String longForm)
	{
		super(shortForm, longForm, false);
	}
}