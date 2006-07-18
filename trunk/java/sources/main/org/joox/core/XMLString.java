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
public class XMLString //extends String
{
	/**
	 * 
	 */
	protected String xml = null;

	/**
	 * @param xml
	 */
	public XMLString(String xml)
	{
		this.xml = xml;
	}

	/**
	 * @return Returns the xml.
	 */
	public String getXml()
	{
		return this.xml;
	}

	/**
	 * @param xml
	 *            The xml to set.
	 */
	public void setXml(String xml)
	{
		this.xml = xml;
	}

}
