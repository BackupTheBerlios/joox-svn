/***************************************************************************************************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************************************************************************/
/**
 * 
 */
package org.joox.logging;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;

/**
 * @author bert
 * 
 */
class SysOutLogStream extends PrintStream
{
	// TODO Implement the methods within logging
	/**
	 * @param arg0
	 * @throws FileNotFoundException
	 */
	SysOutLogStream(String arg0) throws FileNotFoundException
	{
		super(arg0);
	}

	/**
	 * @see java.io.PrintStream#print(boolean)
	 */
	@Override
	public void print(boolean arg0)
	{
		super.print(arg0);
	}

	/**
	 * @see java.io.PrintStream#print(char)
	 */
	@Override
	public void print(char arg0)
	{
		super.print(arg0);
	}

	/**
	 * @see java.io.PrintStream#print(char[])
	 */
	@Override
	public void print(char[] arg0)
	{
		super.print(arg0);
	}

	/**
	 * @see java.io.PrintStream#print(double)
	 */
	@Override
	public void print(double arg0)
	{
		super.print(arg0);
	}

	/**
	 * @see java.io.PrintStream#print(float)
	 */
	@Override
	public void print(float arg0)
	{
		super.print(arg0);
	}

	/**
	 * @see java.io.PrintStream#print(int)
	 */
	@Override
	public void print(int arg0)
	{
		super.print(arg0);
	}

	/**
	 * @see java.io.PrintStream#print(long)
	 */
	@Override
	public void print(long arg0)
	{
		super.print(arg0);
	}

	/**
	 * @see java.io.PrintStream#print(java.lang.Object)
	 */
	@Override
	public void print(Object arg0)
	{
		super.print(arg0);
	}

	/**
	 * @see java.io.PrintStream#print(java.lang.String)
	 */
	@Override
	public void print(String arg0)
	{
		super.print(arg0);
	}

	/**
	 * @see java.io.PrintStream#printf(java.util.Locale, java.lang.String, java.lang.Object...)
	 */
	@Override
	public PrintStream printf(Locale arg0, String arg1, Object... arg2)
	{
		return super.printf(arg0, arg1, arg2);
	}

	/**
	 * @see java.io.PrintStream#printf(java.lang.String, java.lang.Object...)
	 */
	@Override
	public PrintStream printf(String arg0, Object... arg1)
	{
		return super.printf(arg0, arg1);
	}

	/**
	 * @see java.io.PrintStream#println()
	 */
	@Override
	public void println()
	{
		super.println();
	}

	/**
	 * @see java.io.PrintStream#println(boolean)
	 */
	@Override
	public void println(boolean arg0)
	{
		super.println(arg0);
	}

	/**
	 * @see java.io.PrintStream#println(char)
	 */
	@Override
	public void println(char arg0)
	{
		super.println(arg0);
	}

	/**
	 * @see java.io.PrintStream#println(char[])
	 */
	@Override
	public void println(char[] arg0)
	{
		super.println(arg0);
	}

	/**
	 * @see java.io.PrintStream#println(double)
	 */
	@Override
	public void println(double arg0)
	{
		super.println(arg0);
	}

	/**
	 * @see java.io.PrintStream#println(float)
	 */
	@Override
	public void println(float arg0)
	{
		super.println(arg0);
	}

	/**
	 * @see java.io.PrintStream#println(int)
	 */
	@Override
	public void println(int arg0)
	{
		super.println(arg0);
	}

	/**
	 * @see java.io.PrintStream#println(long)
	 */
	@Override
	public void println(long arg0)
	{
		super.println(arg0);
	}

	/**
	 * @see java.io.PrintStream#println(java.lang.Object)
	 */
	@Override
	public void println(Object arg0)
	{
		super.println(arg0);
	}

	/**
	 * @see java.io.PrintStream#println(java.lang.String)
	 */
	@Override
	public void println(String arg0)
	{
		super.println(arg0);
	}

	/**
	 * @see java.io.PrintStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] arg0, int arg1, int arg2)
	{
		super.write(arg0, arg1, arg2);
	}

	/**
	 * @see java.io.PrintStream#write(int)
	 */
	@Override
	public void write(int arg0)
	{
		super.write(arg0);
	}

	/**
	 * @see java.io.FilterOutputStream#write(byte[])
	 */
	@Override
	public void write(byte[] arg0) throws IOException
	{
		super.write(arg0);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return super.toString();
	}
}
