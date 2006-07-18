/**
 * 
 */
package org.joox.commandline;

public abstract class OptionException extends Exception
{
	OptionException(String msg)
	{
		super(msg);
	}
}