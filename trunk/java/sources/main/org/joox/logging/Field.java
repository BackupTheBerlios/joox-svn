/***************************************************************************************************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************************************************************************/
/*
 * $Id: Field.java 5 2004-01-27 11:14:40Z hoevbert $
 * 
 * File created by bert Last changed by: $Author: hoevbert $ Last changed at: $Date: 2004-01-27 12:14:40 +0100 (Tue, 27 Jan 2004) $ Build: $Rev: 5 $
 * 
 */

package org.joox.logging;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Short_Description_of_the_Class.
 * 
 * 
 * 
 * @since
 * @version
 * @author <a href='mailto:bert@joox.org'> bert </a>
 * @see (superclass)
 */
class Field
{
	/**
	 * 
	 */
	protected String result = ""; //$NON-NLS-1$

	/**
	 * @param formatstring
	 * @param object
	 */
	@SuppressWarnings("nls")
	public Field(String formatstring, Object object)
	{
		String string = formatstring.substring(formatstring.indexOf('{') + 1, formatstring.indexOf('}'));
		// System.out.println(string);
		if (string.trim().length() > 0)
		{
			StringTokenizer tokens = new StringTokenizer(string, ",");

			Justify justify = Justify.getJustify(tokens.nextToken()); // Justification
			Format format = Format.getFormat(tokens.nextToken()); // Format
			int min = (new Integer(tokens.nextToken().trim())).intValue(); // Minimal Size
			int max = (new Integer(tokens.nextToken().trim())).intValue(); // Maximal Size

			format(justify, format, min, max, object);
		}
		else
			this.result = object.toString();
	}

	/**
	 * @param justification
	 * @param format
	 * @param min
	 * @param max
	 * @param object
	 */
	public Field(Justify justification, Format format, int min, int max, Object object)
	{
		format(justification, format, min, max, object);
	}

	@SuppressWarnings( { "nls", "unchecked" })
	private void format(Justify justification, Format format, int min, int max, Object object)
	{
		// System.out.println("min=" + min + "\tmax=" + max);
		char fillchar = ' ';

		if (justification == null)
			justification = Justify.LEFT;
		if (format == null)
			format = Format.DEC;
		if (max < min)
			max = Integer.MAX_VALUE;

		if (object instanceof Array)
		{
			this.result = "" + new Vector(Arrays.asList((Object[]) object));
		}
		else if (object instanceof Number || object instanceof Character || object instanceof String)
		{
			if (format == Format.CHR)
			{
				if (object instanceof Number)
				{
					Number number = (Number) object;
					this.result = "" + new Character((char) number.shortValue());
				}
				else
					this.result = "" + object;
			}
			else if (format == Format.BIN)
			{
				if (object instanceof Number)
				{
					if (object instanceof Byte)
						min = 8;
					else if (object instanceof Short)
						min = 16;
					else if (object instanceof Integer)
						min = 32;
					else if (object instanceof Long)
						min = 64;
					fillchar = '0';
					justification = Justify.RIGHT;
					Number number = (Number) object;
					this.result = Long.toBinaryString(number.longValue());
				}
				else
					this.result = "" + object;
			}
			else if (format == Format.OCT)
			{
				if (object instanceof Number)
				{
					if (object instanceof Byte)
						min = 3;
					else if (object instanceof Short)
						min = 6;
					else if (object instanceof Integer)
						min = 12;
					else if (object instanceof Long)
						min = 24;
					fillchar = '0';
					justification = Justify.RIGHT;
					Number number = (Number) object;
					this.result = Long.toOctalString(number.longValue());
				}
				else
					this.result = "" + object;
			}
			else if (format == Format.HEX)
			{
				if (object instanceof Number)
				{
					if (object instanceof Byte)
						min = 2;
					else if (object instanceof Short)
						min = 4;
					else if (object instanceof Integer)
						min = 8;
					else if (object instanceof Long)
						min = 16;
					fillchar = '0';
					justification = Justify.RIGHT;
					Number number = (Number) object;
					this.result = Long.toHexString(number.longValue());
				}
				else
					this.result = "" + object;
			}
			else if (format == Format.DEC)
			{
				this.result = "" + object;
			}
		}
		else
		{
			this.result = "" + object;
		}
		this.result.trim();
		// if(result.length()<min)
		{
			StringBuffer spaces = new StringBuffer();
			for (int i = 0; i < min - this.result.length(); i++)
			{
				spaces.append(fillchar);
			}

			if (justification == Justify.LEFT || justification == Justify.BLOCK)
			{
				if (this.result.length() > max)
					this.result = this.result.substring(0, max - 1) + ">";
				this.result = this.result + spaces;
			}
			else if (justification == Justify.RIGHT)
			{
				// System.out.println("result=" + result + "\tlength=" + result.length());

				if (this.result.length() > max)
				{
					// System.out.println("max=" + max);
					this.result = "<" + this.result.substring(this.result.length() - (max - 1));
				}
				this.result = spaces + this.result;
				// System.out.println("result=" + result + "\tlength=" + result.length());
			}
			else if (justification == Justify.CENTER)
			{
				if (this.result.length() > max)
					this.result = "<" + this.result.substring(1, max - 1) + ">";
				String pre = spaces.substring(0, spaces.length() / 2);
				String post = spaces.substring(pre.length());
				this.result = pre + this.result + post;
			}
			// else if(justification == Justify.BLOCK)
			// {
			// }
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return this.result;
	}
}
