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

/**
 * @author bert
 * 
 */
public class LoggingException extends RuntimeException
{

	/**
	 * 
	 */
	public LoggingException()
	{
		super();
	}

	/**
	 * @param arg0
	 */
	public LoggingException(String arg0)
	{
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public LoggingException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public LoggingException(Throwable arg0)
	{
		super(arg0);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
