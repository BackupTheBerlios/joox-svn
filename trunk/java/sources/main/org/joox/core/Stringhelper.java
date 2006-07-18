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
public class Stringhelper
{

	/**
	 * 
	 */
	private Stringhelper()
	{
		super();
	}

	/**
	 * Hilfsfunktion, die einzelne Zeichen in einem String durch ein anderes Zeichen unter Berücksichtigung von zu ignorierenden Bereichen ersetzt.
	 * TODO comment
	 * @param source
	 *            Zu bearbeitender Text
	 * @param oldchar
	 *            das zu ersetzende Zeichen
	 * @param newchar
	 *            das neue Zeichen, welches das alte Zeichen ersetzt
	 * @param beginIgnoreSymbol
	 *            Zeichen, welches den Beginn des zu ignorierenden Bereichs markiert (Typisch: ' oder ( oder ")
	 * @param endIgnoreSymbol
	 *            Zeichen, welches das Ende des zu ignorierenden Bereichs markiert (Typisch: ' oder ) oder ")
	 * @return Gepatchter Text
	 */
	final public static String replace(String source, char oldchar, char newchar, String beginIgnoreSymbol, String endIgnoreSymbol)
	{
		char[] result = new char[source.length()];
		boolean ignore = false;
		int position = -1;

		for (int i = 0; i < source.length(); i++)
		{
			char c = source.charAt(i);
			if (ignore == false)
			{
				position = beginIgnoreSymbol.indexOf(c);
				if (position > -1)
					ignore = true;
				if (c == oldchar)
					result[i] = newchar;
				else
					result[i] = c;
			}
			else
			// if(ignore == true)
			{
				if (c == endIgnoreSymbol.charAt(position))
					ignore = false;
				result[i] = c;
			}
		}
		return new String(result);
	}

	/**
	 * Helpermethod to find the first char in a string with consideration of ignored areas marked by start chars and end chars. 
	 * TODO Comment
	 * 
	 * @param source
	 *            Zu bearbeitender Text
	 * @param firstchar
	 * @param newchar
	 *            das neue Zeichen, welches das alte Zeichen ersetzt
	 * @param beginIgnoreSymbol
	 *            Zeichen, welches den Beginn des zu ignorierenden Bereichs markiert (Typisch: ' oder ( oder ")
	 * @param endIgnoreSymbol
	 *            Zeichen, welches das Ende des zu ignorierenden Bereichs markiert (Typisch: ' oder ) oder ")
	 * @return Gepatchter Text
	 */
	final public static int indexOf(String source, char firstchar, String beginIgnoreSymbol, String endIgnoreSymbol)
	{
		int result = -1;
		boolean ignore = false;
		int position = -1;

		for (int i = 0; i < source.length(); i++)
		{
			char c = source.charAt(i);
			if (ignore == false)
			{
				position = beginIgnoreSymbol.indexOf(c);
				if (position > -1)
					ignore = true;
				if (c == firstchar)
				{
					result = i;
					break;
				}
			}
			else
			{
				if (c == endIgnoreSymbol.charAt(position))
					ignore = false;
			}
		}
		return result;
	}
}
