/***************************************************************************************************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************************************************************************/
/**
 * 
 */
package org.joox.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author bert
 * 
 */
public class FileLogChannel extends AbstractLogChannel
{
	/**
	 * 
	 */
	@SuppressWarnings("nls")
	public final static String RESET_ON_START_MODE = "RESET_ON_START"; // $SUP-DCI$

	/**
	 * 
	 */
	@SuppressWarnings("nls")
	public final static String APPEND_ON_START_MODE = "APPEND_ON_START"; // $SUP-DCI$

	private String mode;

	private String name;

	private String path;

	private long maxFileSize;

	private int maxFiles;

	private File[] files;

	/**
	 * @see org.joox.logging.LogChannel#getParameters()
	 */
	@SuppressWarnings("nls")
	public List<String> getParameters()
	{
		List<String> result = new ArrayList<String>();
		result.add("Mode");
		result.add("Name");
		result.add("Path");
		result.add("MaxFileSize");
		result.add("MaxFiles");
		return result;
	}

	/**
	 * @see org.joox.logging.LogChannel#println(int, java.lang.String)
	 */
	@SuppressWarnings("nls")
	public void println(int level, String text)
	{
		FileWriter writer = null;
		try
		{
			File file = prepare(text);

			boolean append = true;
			if (this.mode.equalsIgnoreCase(RESET_ON_START_MODE))
			{
				// TODO Alte Datei archivieren
				this.mode = APPEND_ON_START_MODE;
				append = false;
			}

			writer = new FileWriter(file, append);
			// System.out.println(file.getAbsolutePath());
			writer.write(text + System.getProperty("line.separator"));

			writer.close();
			writer = null;
		}
		catch (IOException e)
		{
			throw new LoggingException(e);
		}
		finally
		{
			try
			{
				if (writer != null)
					writer.close();
			}
			catch (IOException e)
			{
				// Nothing to do here
			}
		}
	}

	/**
	 * @param text
	 * @return x
	 */
	@SuppressWarnings("nls")
	private File prepare(String text)
	{
		String currentname = patchFilename(this.name, 0);
		File result = new File(this.path + File.separator + currentname);
		// result.
		if (result.length() + text.length() + System.getProperty("line.separator").length() > this.maxFileSize)
		{ // Max size of the current file is reached.
			for (int i = this.maxFiles - 1; i > 0; i--)
			{
				File archive1 = new File(patchFilename(this.name, i));
				File archive0 = new File(patchFilename(this.name, i - 1));
				if (archive1.exists())
				{
					archive1.delete();
					archive0.renameTo(archive1);
				}
			}
		}
		return result;
	}

	/**
	 * Creates a concrete filename from the filenametemplate. Every placeholder is bounded with '/' signs. Parts of the filename, which are only used in
	 * archived filenames are bounded with '{}'<br>
	 * The following placeholder exist:<br>
	 * d: Date of the first Entry in Logfile (the date of the last entry will be the modification date of the file) in following format:
	 * yyyy-mm-dd-hh-mm-ss-nnn.<br>
	 * t: Time as given from (new Date()).<br>
	 * n: rolling number<br>
	 * Syntax of the template in examples:<br>
	 * 'standard_/d/.log' creates (for example) 'standard_2006-05-31-09-59-34-123.log'<br>
	 * 'standard{_/d/}.log' creates for the current logfile the name 'standard.log' . For archived files (for example) 'standard_2006-05-31-09-59-34-123.log'<br>
	 * 'standard-/t/.log chreates (for example) 'standard-354533566546456654.log'<br>
	 * 'standard-/n/.log chreates (for example) 'standard-00.log'<br>
	 * 
	 * @param template
	 * @param rolling
	 * @return x
	 */
	@SuppressWarnings("nls")
	private String patchFilename(String template, int rolling)
	{
		String filename = "";
		// StringTokenizer
		String[] token = template.split("/");

		for (int i = 0; i < token.length; i++)
		{
			if (i % 2 == 0)
				filename += token[i];
			else if (token[i].equalsIgnoreCase("n"))
			{
				int length = (new Double(Math.log10(this.maxFiles))).intValue() + 1;
				String format = "%1$0" + length + "d";
				filename += String.format(format, rolling);
			}
			else if (token[i].equalsIgnoreCase("d"))
			{
				Calendar date = Calendar.getInstance();
				filename += String.format("%1$tY-%1$tm-%1$td-%1$tH-%1$tM-%1$tS-%1$tL", date);
			}
			else if (token[i].equalsIgnoreCase("t"))
			{
				filename += (new Date()).getTime();
			}
			else
			{
				throw new LoggingException("Unknown token '" + token[i] + "' in template'" + template + "'");
			}
		}
		return filename;
	}

	/**
	 * @param maxFiles
	 *            The maxFiles to set.
	 */
	public void setMaxFiles(String maxFiles)
	{
		this.maxFiles = Integer.parseInt(maxFiles);
		this.files = new File[this.maxFiles];
	}

	/**
	 * @param maxFileSize
	 *            The maxFileSize to set.
	 */
	@SuppressWarnings("nls")
	public void setMaxFileSize(String maxFileSize)
	{
		long multiplier;
		if (maxFileSize.toLowerCase().endsWith("g"))
			multiplier = 1024L * 1024L * 1024L;
		else if (maxFileSize.toLowerCase().endsWith("m"))
			multiplier = 1024L * 1024L;
		else if (maxFileSize.toLowerCase().endsWith("k"))
			multiplier = 1024L;
		else
			multiplier = 1L;
		String number = maxFileSize.toLowerCase().split("[a-z]")[0];
		long size = Long.parseLong(number);
		this.maxFileSize = size * multiplier;
	}

	/**
	 * @param mode
	 *            The mode to set.
	 */
	public void setMode(String mode)
	{
		this.mode = mode;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @param path
	 *            The path to set.
	 */
	public void setPath(String path)
	{
		this.path = path;
	}
}
