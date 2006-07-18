/***************************************************************************************************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************************************************************************/
/**
 * 
 */
package org.joox.demo;

import org.joox.environment.Environment;
import org.joox.logging.Logger;

/**
 * @author bert
 * 
 */
public class LoggingDemo
{
	/**
	 * 
	 */
	protected static Logger logger;
	protected static final int BRT = 0x20000000; 
	static
	{
		logger = Environment.getInstance().getLogger(LoggingDemo.class);
	}

	/**
	 * @param args
	 */
	@SuppressWarnings("nls")
	public static void main(String[] args)
	{
		// logger.in("main", args);
		int rtncode = 0;
		try
		{
			System.out.println("This is the text of the call of System.out.println() in LoggingDemo.class\nwith multiple lines");
			LoggingDemo logdemo = new LoggingDemo();
			System.out.println("This is the text of the call of System.out.println() in LoggingDemo.class");
			logdemo.work();
		}
		catch (RuntimeException e)
		{
			// logger.err("main", e);
			System.err.println("This is the text of the call of System.err.println() in LoggingDemo.class");
			rtncode = -1;
		}
		finally
		{
			logger.score(Logger.INF);
			// logger.ext(rtncode);
			System.exit(rtncode);
		}
	}

	/**
	 * @param logdemo
	 * 
	 */
	@SuppressWarnings("nls")
	private void work()
	{
		// logger.in("work", logdemo);
		Computer computer = new Computer();

		logger.prf("profile1", "Start");
		Environment.getInstance().sleep(150);
		computer.prepare("test1", 7);
		logger.prf("profile2", "Start");
		computer.compute();
		Environment.getInstance().sleep(150);
		logger.prf("profile1", "Stop");

		logger.prf("profile1", "Restart");
		logger.println(BRT, "personal message");
		Environment.getInstance().sleep(500);
		logger.prf("profile1", "Stop");

		computer.prepare("test2", 0);
		Environment.getInstance().sleep(150);
		computer.compute();
		logger.prf("profile2", "Stop");

		// logger.out();
	}

	@SuppressWarnings("nls")
	private LoggingDemo()
	{
		// logger.in("<init>", "");
		logger.msg("Initializing the status of this class ...");

		logger.msg("Initialising successfull.");
		// logger.out();
	}
}

class Computer
{
	private int value;

	/**
	 * @return result
	 * 
	 */
	@SuppressWarnings("nls")
	protected int compute()
	{
		// logger.in("compute","");

		int result = 84 / this.value;

		// logger.out();
		return result;
	}

	/**
	 * @param text
	 * @param newvalue
	 * 
	 */
	@SuppressWarnings("nls")
	void prepare(String text, final int newvalue)
	{
		// logger.in("prepare", text, newvalue);

		this.value = newvalue;

		// logger.out();
	}

}
