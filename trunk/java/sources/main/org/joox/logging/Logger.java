/***************************************************************************************************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************************************************************************/
/*
 * $Id: Logger.java 5 2004-01-27 11:14:40Z hoevbert $
 * 
 * File created by bert Last changed by: $Author: hoevbert $ Last changed at: $Date: 2004-01-27 12:14:40 +0100 (Tue, 27 Jan 2004) $ Build: $Rev: 5 $
 * 
 */

package org.joox.logging;

/**
 * @author bert
 * 
 * To change the template for this generated type comment go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface Logger
{
	/**
	 * 
	 */
	public static final int NUL = 0x00000000;

	/**
	 * 
	 */
	public static final int VAR = 0x00000001;

	/**
	 * 
	 */
	public static final int DBG = 0x00000002;

	/**
	 * 
	 */
	public static final int INF = 0x00000004;

	/**
	 * 
	 */
	public static final int MSG = 0x00000008;

	/**
	 * 
	 */
	public static final int PRF = 0x00000010;

	/**
	 * 
	 */
	public static final int WRN = 0x00000020;

	/**
	 * 
	 */
	public static final int ERR = 0x00000040;

	/**
	 * 
	 */
	public static final int FAT = 0x00000080;

	/**
	 * 
	 */
	public static final int TRC = 0x00000100;

	/**
	 * 
	 */
	public static final int TRD = 0x00000200;

	/**
	 * 
	 */
	public static final int TRT = 0x00000400;

	/**
	 * 
	 */
	public static final int TRE = 0x00000800;

	/**
	 * 
	 */
	public static final int ALL = 0x7FFFFFFF;

	/**
	 * @param priority
	 */
	public abstract void println(int priority);

	/**
	 * @param priority
	 * @param o
	 */
	public abstract void println(int priority, Object o);

	/**
	 * @param priority
	 * @param o
	 */
	public abstract void printToChannel(int priority, Object o);

	/**
	 * @param level
	 * @param method
	 */
	public abstract void in(int level, String method);

	/**
	 * @param method
	 */
	public abstract void in(String method);

	/**
	 * @param level
	 * @param method
	 * @param args
	 */
	public abstract void in(int level, String method, Object... args);

	/**
	 * @param method
	 * @param args
	 */
	public abstract void in(String method, Object... args);

	/**
	 * @param level
	 */
	public abstract void out(int level);

	/**
	 * 
	 */
	public abstract void out();

	/**
	 * @param level
	 * @param s
	 */
	public abstract void out(int level, Object s);

	/**
	 * @param s
	 */
	public abstract void out(Object s);

	/**
	 * @param method
	 * @param e
	 */
	public abstract void err(String method, Throwable e);

	/**
	 * 
	 */
	public abstract void ext();

	/**
	 * @param rtncode
	 */
	public abstract void ext(int rtncode);

	/**
	 * @param s
	 */
	public abstract void inf(String s);

	/**
	 * @param s
	 */
	public abstract void msg(String s);

	/**
	 * @param key
	 *            is a dot separated hierarchy of keys
	 * @param s
	 *            a logging comment
	 */
	public abstract void prf(String key, String s);

	/**
	 * @param lvl
	 */
	public abstract void score(int lvl);

	/**
	 * @param s
	 */
	public abstract void dbg(String s);

	/**
	 * @param s
	 */
	public abstract void wrn(String s);

	/**
	 * @param name
	 * @param o
	 */
	public abstract void var(String name, Object o);

	/**
	 * @param name
	 * @param i
	 */
	public abstract void var(String name, int i);

	/**
	 * @param name
	 * @param c
	 */
	public abstract void var(String name, char c);

	/**
	 * @param name
	 * @param b
	 */
	public abstract void var(String name, byte b);

	/**
	 * @param name
	 * @param b
	 */
	public abstract void var(String name, boolean b);

	/**
	 * @param name
	 * @param s
	 */
	public abstract void var(String name, short s);

	/**
	 * @param name
	 * @param l
	 */
	public abstract void var(String name, long l);

	/**
	 * @param name
	 * @param f
	 */
	public abstract void var(String name, float f);

	/**
	 * @param name
	 * @param d
	 */
	public abstract void var(String name, double d);
}
