/***************************************************************************************************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************************************************************************/
/*
 * $Id: Format.java 5 2004-01-27 11:14:40Z hoevbert $
 * 
 * File created by bert Last changed by: $Author: hoevbert $ Last changed at: $Date: 2004-01-27 12:14:40 +0100 (Tue, 27 Jan 2004) $ Build: $Rev: 5 $
 * 
 */

package org.joox.logging;

/**
 * Short_Description_of_the_Class.
 * 
 * 
 * 
 * @since
 * @version
 * @author <a href='mailto:bert@joox.org'> bert </a>
 * @see (superclass)
 */
class Format
{
	/**
	 * 
	 */
	public final static Format CHR = new Format();

	/**
	 * 
	 */
	public final static Format DEC = new Format();

	/**
	 * 
	 */
	public final static Format HEX = new Format();

	/**
	 * 
	 */
	public final static Format OCT = new Format();

	/**
	 * 
	 */
	public final static Format BIN = new Format();

	private Format()
	{
		super();
	}

	@SuppressWarnings("nls")
	static Format getFormat(String text)
	{
		Format rtn = CHR;
		if ("DEC".equals(text.toUpperCase()))
			rtn = DEC;
		else if ("HEX".equals(text.toUpperCase()))
			rtn = HEX;
		else if ("OCT".equals(text.toUpperCase()))
			rtn = OCT;
		else if ("BIN".equals(text.toUpperCase()))
			rtn = BIN;
		return rtn;
	}

}
