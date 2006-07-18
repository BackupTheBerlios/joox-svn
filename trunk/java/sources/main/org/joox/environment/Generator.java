/* JOOX - The Java Toolbox ($Id$)
 *
 *	Copyright (C) 2004 by joox.org
 *
 *	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Created by bert at Dec 22, 2004
 * Last Changed by $Author$
 */
package org.joox.environment;

import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * @author hoevbert
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Generator
{
	private final static String PROPERTYFILE = "generator.properties"; //$NON-NLS-1$

	private static Properties config;
	
	static
	{
		try
		{
			config = new Properties();
			config.load(new FileInputStream(PROPERTYFILE));
			//config.list(System.out);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(-1); //TODO Make constants for exit codes
		}
	}

	private Generator()
	{
		//Nothing to do
	}

}
