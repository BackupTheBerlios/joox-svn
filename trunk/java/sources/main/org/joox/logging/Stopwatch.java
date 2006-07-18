/* JOOX - The Java Toolbox ($Id: Stopwatch.java 23 2005-03-16 14:25:34Z bert $)
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
 * Created by bert
 * Last Changed by $Author: bert $
 */

package org.joox.logging;

import java.util.*;

public class Stopwatch
{ 
	/**
	 * Comment for <code>textout</code>
	 */
	protected boolean textout;

	/**
	 * Comment for <code>time</code>
	 */
	protected long time;

	/**
	 * @param textout
	 */
	public Stopwatch(boolean textout)
	{ //
		this.textout = textout;
		if (textout)
		{ //
			System.out.println("Stopwatch started:"); //$NON-NLS-1$
			System.out
							.println("|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|"); //$NON-NLS-1$
		}
		this.time = (new Date()).getTime();
	}

	/**
	 * ToDo_Kurze_Beschreibung_der_Methode. Beispiel: (C/M) <code> </code>.
	 * @param index 
	 * @param loops 
	 * @author (C)
	 * @version V0.00.00 (C)
	 * @since V.0.00.00(C/M/F)
	 */
	public void progress(int index, int loops)
	{ //
		if (this.textout)
		{ //
			if (index % (loops / 100) == 0)
				System.out.print("."); //$NON-NLS-1$
		}
	}

	/**
	 * ToDo_Kurze_Beschreibung_der_Methode. Beispiel: (C/M) <code> </code>.
	 * @param loops 
	 * @return needed time in milliseconds
	 * @author (C)
	 * @version V0.00.00 (C)
	 * @since V.0.00.00(C/M/F)
	 */
	public long score(int loops)
	{ //
		long result =  ((new Date()).getTime() - this.time);
		if (this.textout)
		{ //
			System.out.println("."); //$NON-NLS-1$
			System.out.println("Time used: " + result + " ms.");  //$NON-NLS-1$//$NON-NLS-2$
			System.out.printf("Average Time per loop: " + "%1$6f" + " ms.%n", new Double(result) / loops);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		}
		return  result;
	}
}
