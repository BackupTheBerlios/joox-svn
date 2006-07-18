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

import java.util.ArrayList;
import java.util.List;

/**
 * @author bert
 * 
 */
public class SysErrLogChannel extends AbstractLogChannel
{

	/**
	 * @see org.joox.logging.LogChannel#getParameters()
	 */
	public List getParameters()
	{
		return new ArrayList();
	}

	/**
	 * @see org.joox.logging.LogChannel#println(java.lang.String)
	 */
	public void println(int level, String text)
	{
		System.out.flush();
		System.err.println(text);
		System.err.flush();
	}

}
