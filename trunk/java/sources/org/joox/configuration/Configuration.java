/*	JOOX - The Java Toolbox ($Id$)
*
*	Copyright (C) 2004 by joox.org
*
*	This program is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License
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
 * Last Changed by $Author$
*/

//DONE: 1. Implement and test Observer-Pattern
//DONE: 2. Undo Function
//TODO: 3. Integrate Commandline Arguments
//TODO: 4. Implement XML-Properties
//TODO: 5. Implement DB based Properties
//TODO: 6. Implement EJB based Properties

package org.joox.configuration;

import java.util.Enumeration;

import org.joox.base.Observed;
import org.joox.base.Singleton;
//import org.joox.base.Singleton;

/** This class represent a persistent configuration for other classes.
 * 
 * Normally this class is used as a private aggregation of the class which wants to have a persistant configuration.
 * This class has to register itself as an observer for this configuration
 * 
 * @since V0.7.0
 * @author bert@joox.org
 */
public interface Configuration extends Observed, Singleton
{
	/** Resets the configuration to the last stored values. 
	 * 
	 * This method can be called manually but should also called periodically for updating the
	 * current configuration. This is used if the stored configuration is changed outside the running application.
	 * 
	 * The implementation should call the update() method of the observers of this class if the stored 
	 * values are different from the current ones.
	 */
	void rollback();
	
	/** Makes the current values persistant. 
	 * 
	 * If one or more current values are changed, then the current values will be stored and the update() method 
	 * of the observers will be called.
	 */
	void commit();

	/** Returns an enumeration of all keys of this configuration.
	 * 
	 * @return All keys of this configuration
	 */
	Enumeration<String> getKeys();

	/**Returns the value of a key.
	 * 
	 * @param key The key of the value
	 * @return The value or null if the key is not present
	 */
	String get(String key);
	
	/** Adds a new value for a key.
	 * 
	 * The value will be <b>not</b> stored persistant and the observers are not informed.
	 * 
	 * @param key
	 * @param value
	 */
	void add(String key, String value);
	
	/** Sets a value for a key.
	 * 
	 * The value will be <b>not</b> stored persistant and the observers are not informed.
	 * 
	 * @param key
	 * @param value
	 */
	void set(String key, String value);
	
	/** Sets all values from command line arguments.
	 * 
	 * The value will be <b>not</b> stored persistant and the observers are not informed.
	 * @param args 
	 */
	void set(String... args);
	
	/** Undo commited changes.
	 * There is now warranty to undo any depth of commits. 
	 * A concrete implementation can limit the depth of undoable commits.
	 */
	void undo();
	
	/** Redo undone changes.
	 */
	void redo();
	
	/** Returns the limit of the depth of undos.
	 * 
	 * @return the limit of the depth of undos; The value of -1 means that there is no limit.
	 */
	int getUndoLimit();
	
	/** Returns the current position in the undo stack.
	 * @return The current pointer in the undo stack (always between 0 and the UndoLimit).
	 */
	int getUndoPointer();
	
}