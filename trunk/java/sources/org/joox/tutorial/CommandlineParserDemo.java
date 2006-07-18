/**
 * 
 */
package org.joox.tutorial;

import org.joox.cli.CommandlineParser;
import org.joox.cli.IllegalOptionValueException;
import org.joox.cli.Option;
import org.joox.cli.UnknownOptionException;


/**
 * @author bert
 *
 */
public final class CommandlineParserDemo
{

	/**
	 * @param args
	 */
	@SuppressWarnings("nls")
	public static void main(String[] args)
	{
		try
		{
			CommandlineParser clp = new CommandlineParser();
			
			Option help = clp.addBooleanOption('h', "help");
			Option help2 = clp.addBooleanOption('2', "help2");
			Option xtend = clp.addBooleanOption('x', "xtend");
			Option files = clp.addStringOption('f', "files");
			Option date = clp.addStringOption('d', "date");
			Option version = clp.addBooleanOption('v', "version");
			
			clp.parse(args);
			
			Boolean ishelp = (Boolean) clp.getOptionValue(help);
			Boolean ishelp2 = (Boolean) clp.getOptionValue(help2);
			String filesS = (String) clp.getOptionValue(files);
			
			System.out.println("Help: " + ishelp);
			System.out.println("Help2: " + ishelp2);
			System.out.println("files: " + filesS);
		}
		catch (IllegalOptionValueException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnknownOptionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		System.exit(0);
	}

}
