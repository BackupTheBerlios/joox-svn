/***************************************************************************************************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************************************************************************/
/**
 * 
 */
package org.joox.core;

/**
 * @author bert
 * 
 */
public class RegEx //extends String
{
	/**
	 * 
	 */
	protected String regex = null;

	/**
	 * @param regex
	 */
	public RegEx(String regex)
	{
		this.regex = regex;
	}

	/**
	 * @return Returns the xml.
	 */
	public String getRegEx()
	{
		return this.regex;
	}

	/**
	 * @param regex
	 */
	public void setRegEx(String regex)
	{
		this.regex = regex;
	}

}
