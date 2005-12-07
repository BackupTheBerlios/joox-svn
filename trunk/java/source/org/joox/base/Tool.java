/* JOOX - The Java Toolbox ($Id: Tool.java 23 2005-03-16 14:25:34Z bert $)
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
 * Created by hoevbert at Dec 29, 2004
 * Last Changed by $Author: bert $ at $Date: 2005-03-16 14:25:34 +0000 (Wed, 16 Mar 2005) $
 */
package org.joox.base;

/**
 * @author hoevbert
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Tool
{
	final public static String getClasspath()
	{ //
		String classpath = System.getProperty("java.class.path"); //$NON-NLS-1$
		String pathseparator = System.getProperty("path.separator"); //$NON-NLS-1$
		//System.out.println("classpath (pre)= " + classpath);
		int cut = classpath.indexOf(pathseparator);
		//System.out.println("cut = " + cut);
		if (cut > 0) classpath = classpath.substring(0, cut);
		//System.out.println("classpath (post)= " + classpath);
		return classpath;
	}
}
