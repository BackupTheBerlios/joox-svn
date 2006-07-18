/***************************************************************************************************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************************************************************************/
/*
 * $Id: Justify.java 5 2004-01-27 11:14:40Z hoevbert $
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
// To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
// To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
class Justify
{
	/**
	 * 
	 */
	public final static Justify LEFT = new Justify();

	/**
	 * 
	 */
	public final static Justify RIGHT = new Justify();

	/**
	 * 
	 */
	public final static Justify CENTER = new Justify();

	/**
	 * 
	 */
	public final static Justify BLOCK = new Justify();

	private Justify()
	{
		super();
	}

	@SuppressWarnings("nls")
	static Justify getJustify(String text)
	{
		Justify rtn = LEFT;
		if ("RIGHT".equals(text.toUpperCase()))
			rtn = RIGHT;
		else if ("CENTER".equals(text.toUpperCase()))
			rtn = CENTER;
		else if ("BLOCK".equals(text.toUpperCase()))
			rtn = BLOCK;
		return rtn;
	}
}
