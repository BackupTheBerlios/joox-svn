/***************************************************************************************************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************************************************************************/
package org.joox.test.environment;

import org.joox.commandline.CommandlineParser;
import org.joox.configuration.Configuration;
import org.joox.environment.Environment;
import org.joox.logging.Logger;

import junit.framework.TestCase;

/**
 * @author bert
 * 
 */
public class EnvironmentTest extends TestCase
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(EnvironmentTest.class);
	}

	/**
	 * @param name
	 */
	public EnvironmentTest(String name)
	{
		super(name);
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	/**
	 * Test method for 'org.joox.environment.AbstractEnvironment.getLogger()'
	 */
	@SuppressWarnings("nls")
	public void testGetLogger()
	{
		try
		{
			Environment env = Environment.getInstance();
			Logger log;
			log = env.getLogger(this.getClass());
			log.msg("Testausgabe");
			assertNotNull(log);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for 'org.joox.environment.AbstractEnvironment.getCommandlineParser()'
	 */
	public void testGetCommandlineParser()
	{
		try
		{
			Environment env = Environment.getInstance();
			CommandlineParser clp;
			String[] args = {};
			clp = env.getCommandlineParser(args);
			assertNotNull(clp);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for 'org.joox.environment.AbstractEnvironment.getConfiguration()'
	 */
	@SuppressWarnings("nls")
	public void testGetConfiguration()
	{
		try
		{
			Environment env = Environment.getInstance();
			Configuration conf;
			conf = env.getConfiguration("test");
			assertNotNull(conf);
			assertNotSame("value2", conf.get("key1"));
			System.out.println("key1: " + conf.get("key1"));
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for 'org.joox.environment.AbstractEnvironment.getClasspath()'
	 */
	@SuppressWarnings("nls")
	public void testGetClasspath()
	{
		try
		{
			Environment env = Environment.getInstance();
			String path;
			path = env.getClasspath();
			assertNotNull(path);
			System.out.println("path: " + path);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for 'org.joox.environment.AbstractEnvironment.sleep(long)'
	 */
	public void testSleep()
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Test method for 'org.joox.environment.AbstractEnvironment.getEaster(int)'
	 */
	public void testGetEaster()
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Test method for 'org.joox.environment.AbstractEnvironment.generate(Class)'
	 */
	public void testGenerate()
	{
		// TODO Auto-generated method stub

	}

}
