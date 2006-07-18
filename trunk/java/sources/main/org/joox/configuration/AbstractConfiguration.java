/* JOOX - The Java Toolbox ($Id: AbstractConfiguration.java 19 2005-01-11 19:59:30Z berth $)
 *
 *	Copyright (C) 2004 by joox.org
 *
 *	This program is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Created by berth at Dec 29, 2004
 * Last Changed by $Author: berth $ at $Date: 2005-01-11 19:59:30 +0000 (Tue, 11 Jan 2005) $
 */
package org.joox.configuration;

import java.util.Observable;
import java.util.Observer;

/**
 * @author hoevbert
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractConfiguration extends Observable implements Configuration
{
	/*
	 * TODO Correct sequence of searching and setting the configuration parameters 1. Hard coded in caller class 2. Global persistant configuration (File, DB
	 * etc.) 3. Local persistant configuration (all given parameters will be stored in the local file) 3. System-properties 4. Applet-parameters or application
	 * arguments
	 * 
	 * Remarks: 1. and 2. together must have a complete set of configurations, so that the local copy has always a complete set. It is possible that only one of
	 * them exist. 3. and 4. can only temporary overwrite the stored parameters. They are not automatically stored
	 */
	/**
	 * @see org.joox.base.Observed#addObserver(java.util.Observer)
	 */
	@Override
	public void addObserver(Observer o)
	{
		System.out.println(">>> addObserver("+ o + ") called <<<"); //$NON-NLS-1$ //TODO: Replace with Logging-Tool (Mode: Trace)
		super.addObserver(o);
	}
}
