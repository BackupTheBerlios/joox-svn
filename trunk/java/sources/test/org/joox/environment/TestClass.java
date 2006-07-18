/* JOOX - The Java Toolbox
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
 * Created by hoevbert at Dec 28, 2004
 */
package org.joox.environment;


/**
 * @author hoevbert
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestClass implements TestClassInterface
{
	private String arg = null;
	private String brg = null;
	private Integer i;
	private Integer j;

	public TestClass()
	{
		// okay
	}

	public TestClass(String arg)
	{
		this.arg = arg;
	}

	public TestClass(Integer i)
	{
		this.i = i;
	}

	public TestClass(String arg, Integer i)
	{
		this.arg = arg;
		this.i = i;
	}

	public TestClass(Integer i, String arg)
	{
		this.i = i;
		this.arg = arg;
	}

	public TestClass(Integer i, String arg, Integer j)
	{
		this.i = i;
		this.arg = arg;
		this.j = j;
	}

	public TestClass(String arg, Integer i, String brg)
	{
		this.arg = arg;
		this.i = i;
		this.brg = brg;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "<" + this.arg + "\t" + this.i + "\t" + this.j + "\t" + this.brg + ">";   //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$//$NON-NLS-4$ //$NON-NLS-5$
		
	}
}
