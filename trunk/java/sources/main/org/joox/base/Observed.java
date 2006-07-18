/***************************************************************************************************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************************************************************************/
package org.joox.base;

import java.util.Observer;

/**
 * This is an interface for the java.util.Observable class.
 * 
 * @see java.util.Observable
 * @author hoevbert
 */
public interface Observed
{
	/**
	 * @param o
	 */
	public void addObserver(Observer o);

	/**
	 * Deletes an observer from the set of observers of this object. Passing <CODE>null</CODE> to this method will have no effect.
	 * 
	 * @param o
	 *            the observer to be deleted.
	 */
	public void deleteObserver(Observer o);

	/**
	 * If this object has changed, as indicated by the <code>hasChanged</code> method, then notify all of its observers and then call the
	 * <code>clearChanged</code> method to indicate that this object has no longer changed.
	 * <p>
	 * Each observer has its <code>update</code> method called with two arguments: this observable object and <code>null</code>. In other words, this
	 * method is equivalent to: <blockquote><tt>
	 * notifyObservers(null)</tt> </blockquote>
	 * 
	 * @see java.util.Observable#hasChanged()
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void notifyObservers();

	/**
	 * If this object has changed, as indicated by the <code>hasChanged</code> method, then notify all of its observers and then call the
	 * <code>clearChanged</code> method to indicate that this object has no longer changed.
	 * <p>
	 * Each observer has its <code>update</code> method called with two arguments: this observable object and the <code>arg</code> argument.
	 * 
	 * @param arg
	 *            any object.
	 * @see java.util.Observable#hasChanged()
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void notifyObservers(Object arg);

	/**
	 * Clears the observer list so that this object no longer has any observers.
	 */
	public void deleteObservers();

	/**
	 * Tests if this object has changed.
	 * 
	 * @return <code>true</code> if and only if the <code>setChanged</code> method has been called more recently than the <code>clearChanged</code> method
	 *         on this object; <code>false</code> otherwise.
	 */
	public boolean hasChanged();

	/**
	 * Returns the number of observers of this <tt>Observable</tt> object.
	 * 
	 * @return the number of observers of this object.
	 */
	public int countObservers();
}
