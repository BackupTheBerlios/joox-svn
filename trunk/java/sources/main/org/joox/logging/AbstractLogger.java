/***************************************************************************************************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************************************************************************/
// TODO Different logging levels per thread
// TODO Configurable text blocks/i18n
package org.joox.logging;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * @author bert
 * 
 */
public abstract class AbstractLogger implements Logger
{
	// TODO private PrintStream consoleOut;
	// TODO private PrintStream consoleErr;

	/**
	 * 
	 */
	protected static Map<Thread, ArrayList> threads = new HashMap<Thread, ArrayList>();

	/**
	 * 
	 */
	protected static long REFERENCE_TIME = (new Date()).getTime();

	/**
	 * 
	 */
	protected Map<String, Long> profilerSummary = new TreeMap<String, Long>();

	/**
	 * 
	 */
	protected Map<String, Long> profilerCurrent = new HashMap<String, Long>();

	/**
	 * 
	 */
	protected Map<String, Long> profilerCounter = new HashMap<String, Long>();

	/**
	 * 
	 */
	protected int mask = 0;

	/**
	 * 
	 */
	protected Class clazz = null;

	/**
	 * @param debugged
	 */
	@SuppressWarnings("nls")
	protected AbstractLogger(Class debugged)
	{ //

		// TODO this.consoleOut = System.out;
		// TODO this.consoleErr = System.err;
		// System.setOut(new SysOutLogStream());
		// System.setErr(new SysErrLogStream());
		// System.out.println("marker = " + marker);
		this.clazz = debugged;
		// System.out.println(new Field(Justify.RIGHT,null, 16,16,marker) +": " + new Field(null,Format.BIN,0,-1,new
		// Integer(level)));

		if (threads.get(Thread.currentThread()) == null) // neuer Thread?
		{ //
			threads.put(Thread.currentThread(), newData()); // alle Informationen eines Threads
		}
	}

	/**
	 * @see org.joox.logging.Logger#in(java.lang.String)
	 */
	@SuppressWarnings("nls")
	synchronized public void in(String method)
	{ //
		this.key = this.clazz.getName() + "." + method;
		profiling();

		Stack<String> stack = getStack();
		stack.push(method);
		if ((this.mask & TRC) != 0)
		{
			StringBuffer sb = getInset();

			// String s = (m.getDeclaringClass()).getName() + "." + m.getName() + "(" + m.getParameterTypes() + ")";
			String arguments = "?";
			printToChannel(TRC, "+ start" + " [args: (" + arguments + ")]");

			sb.append("|\t");
		}
	}

	/**
	 * @see org.joox.logging.Logger#in(int, java.lang.String)
	 */
	@SuppressWarnings("nls")
	synchronized public void in(int level, String method)
	{ //
		this.key = this.clazz.getName() + "." + method;
		profiling();

		Stack<String> stack = getStack();
		stack.push(method);
		if ((this.mask & level) != 0)
		{
			StringBuffer sb = getInset();

			// String s = (m.getDeclaringClass()).getName() + "." + m.getName() + "(" + m.getParameterTypes() + ")";
			String arguments = "?";
			printToChannel(level, "+ start" + " [args: (" + arguments + ")]");

			sb.append("|\t");
		}
	}

	/**
	 * @see org.joox.logging.Logger#in(java.lang.String, java.lang.Object[])
	 */
	@SuppressWarnings("nls")
	synchronized public void in(String method, Object... args)
	{ //
		this.key = this.clazz.getName() + "." + method;
		profiling();

		Stack<String> stack = getStack();
		stack.push(method);
		if ((this.mask & TRC) != 0)
		{
			StringBuffer sb = getInset();

			// String s = (m.getDeclaringClass()).getName() + "." + m.getName() + "(" + m.getParameterTypes() + ")";
			String arguments = "";
			for (int i = 0; i < args.length; i++)
			{
				if (arguments.length() > 0)
					arguments += ", ";
				arguments += args[i];
			}
			printToChannel(TRC, "+ start" + " [args: (" + arguments + ")]");

			sb.append("|\t");
		}
	}

	/**
	 * @see org.joox.logging.Logger#in(int, java.lang.String)
	 */
	@SuppressWarnings("nls")
	synchronized public void in(int level, String method, Object... args)
	{ //
		this.key = this.clazz.getName() + "." + method;
		profiling();

		Stack<String> stack = getStack();
		stack.push(method);
		if ((this.mask & level) != 0)
		{
			StringBuffer sb = getInset();

			// String s = (m.getDeclaringClass()).getName() + "." + m.getName() + "(" + m.getParameterTypes() + ")";
			String arguments = "";
			for (int i = 0; i < args.length; i++)
			{
				if (arguments.length() > 0)
					arguments += ", ";
				arguments += args[i];
			}
			printToChannel(level, "+ start" + " [args: (" + arguments + ")]");

			sb.append("|\t");
		}
	}

	/**
	 * @see org.joox.logging.Logger#out(int, java.lang.Object)
	 */
	@SuppressWarnings("nls")
	synchronized public void out(int level, Object s)
	{ //
		this.key = this.clazz.getName() + "." + getMethod();
		profiling();

		if ((this.mask & level) != 0)
		{
			String text = "" + s;
			StringBuffer sb = getInset();
			if (sb.length() > 0)
				sb.setLength(sb.length() - 2);
			printToChannel(level, "- returned with " + text);
		}
		getStack().pop();
	}

	/**
	 * @see org.joox.logging.Logger#out(java.lang.Object)
	 */
	@SuppressWarnings("nls")
	synchronized public void out(Object s)
	{ //
		this.key = this.clazz.getName() + "." + getMethod();
		profiling();

		if ((this.mask & TRC) != 0)
		{
			String text = "" + s;
			StringBuffer sb = getInset();
			if (sb.length() > 0)
				sb.setLength(sb.length() - 2);
			printToChannel(TRC, "- " + text);
		}
		getStack().pop();
	}

	/**
	 * @see org.joox.logging.Logger#err(java.lang.String, java.lang.Throwable)
	 */
	@SuppressWarnings("nls")
	synchronized public void err(String method, Throwable e)
	{ //
		this.key = this.clazz.getName() + "." + getMethod();
		profiling();

		if ((this.mask & ERR) != 0) // $SUP-IET$
		{
			Object[] array = new Object[] { method, e };
			printToChannel(ERR, array);
		}
	}

	/**
	 * @see org.joox.logging.Logger#ext()
	 */
	@SuppressWarnings("nls")
	synchronized public void ext()
	{ //
		this.key = this.clazz.getName() + "." + getMethod();
		profiling();

		Stack stack = getStack();
		while (!stack.empty())
		{ // ToDo_Beschreibung_dieses_Blockes
			out("ends");
			// System.out.println(txt + "//" + name);
		}
	}

	/**
	 * @see org.joox.logging.Logger#ext(int)
	 */
	@SuppressWarnings("nls")
	synchronized public void ext(int rtncode)
	{ //
		this.key = this.clazz.getName() + "." + getMethod();
		profiling();

		Stack stack = getStack();
		while (!stack.empty())
		{ // ToDo_Beschreibung_dieses_Blockes
			out("ends with returncode " + rtncode);
			// System.out.println(txt + "//" + name);
		}
	}

	/**
	 * @see org.joox.logging.Logger#println(int)
	 */
	@SuppressWarnings("nls")
	synchronized public void println(int priority)
	{ //
		printToChannel(priority, "");
	}

	/**
	 * @see org.joox.logging.Logger#println(int)
	 */
	@SuppressWarnings("nls")
	synchronized public void println(int priority, Object o)
	{ //
		if ((this.mask & priority) != 0)
		{
			printToChannel(priority, o);
		}
	}

	/**
	 * @see org.joox.logging.Logger#out()
	 */
	@SuppressWarnings("nls")
	synchronized public void out()
	{ //
		out("returned");
	}

	/**
	 * @see org.joox.logging.Logger#out(int)
	 */
	@SuppressWarnings("nls")
	synchronized public void out(int level)
	{ //
		out(level, "returned");
	}

	/**
	 * @see org.joox.logging.Logger#inf(java.lang.String)
	 */
	synchronized public void inf(String s)
	{ //
		println(INF, s);
	}

	/**
	 * @see org.joox.logging.Logger#msg(java.lang.String)
	 */
	synchronized public void msg(String s)
	{ //
		println(MSG, s);
	}

	/**
	 * @see org.joox.logging.Logger#dbg(java.lang.String)
	 */
	synchronized public void dbg(String s)
	{ //
		println(DBG, s);
	}

	/**
	 * @see org.joox.logging.Logger#wrn(java.lang.String)
	 */
	synchronized public void wrn(String s)
	{ //
		println(WRN, s);
	}

	/**
	 * @see org.joox.logging.Logger#var(java.lang.String, java.lang.Object)
	 */
	@SuppressWarnings("nls")
	synchronized public void var(String name, Object o)
	{ //
		println(VAR, name + " = " + o);
	}

	/**
	 * @see org.joox.logging.Logger#var(java.lang.String, int)
	 */
	@SuppressWarnings("nls")
	synchronized public void var(String name, int i)
	{ //
		Integer o = new Integer(i);
		println(VAR, name + " = " + o);
	}

	/**
	 * @see org.joox.logging.Logger#var(java.lang.String, char)
	 */
	@SuppressWarnings("nls")
	synchronized public void var(String name, char c)
	{ //
		Character o = new Character(c);
		println(VAR, name + " = " + o);
	}

	/**
	 * @see org.joox.logging.Logger#var(java.lang.String, byte)
	 */
	@SuppressWarnings("nls")
	synchronized public void var(String name, byte b)
	{ //
		Byte o = new Byte(b);
		println(VAR, name + " = " + o);
	}

	/**
	 * @see org.joox.logging.Logger#var(java.lang.String, boolean)
	 */
	@SuppressWarnings("nls")
	synchronized public void var(String name, boolean b)
	{ //
		Boolean o = new Boolean(b);
		println(VAR, name + " = " + o);
	}

	/**
	 * @see org.joox.logging.Logger#var(java.lang.String, short)
	 */
	@SuppressWarnings("nls")
	synchronized public void var(String name, short s)
	{ //
		Short o = new Short(s);
		println(VAR, name + " = " + o);
	}

	/**
	 * @see org.joox.logging.Logger#var(java.lang.String, long)
	 */
	@SuppressWarnings("nls")
	synchronized public void var(String name, long l)
	{ //
		Long o = new Long(l);
		println(VAR, name + " = " + o);
	}

	/**
	 * @see org.joox.logging.Logger#var(java.lang.String, float)
	 */
	@SuppressWarnings("nls")
	synchronized public void var(String name, float f)
	{ //
		Float o = new Float(f);
		println(VAR, name + " = " + o);
	}

	/**
	 * @see org.joox.logging.Logger#var(java.lang.String, double)
	 */
	@SuppressWarnings("nls")
	synchronized public void var(String name, double d)
	{ //
		Double o = new Double(d);
		println(VAR, name + " = " + o);
	}

	/**
	 * @see org.joox.logging.Logger#prf(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("nls")
	synchronized public void prf(String currkey, String text)
	{ //
		this.key = currkey;
		this.delta = 0;
		this.summary = 0;
		profiling();
		println(PRF, text);
	}

	/**
	 * @see org.joox.logging.Logger#score(int)
	 */
	synchronized public void score(int lvl)
	{
		println(lvl, this.profilerSummary);
		println(lvl, this.profilerCounter);
	}

	private void profiling()
	{
		if ((this.mask & PRF) != 0)
		{
			if (this.profilerSummary.get(this.key) == null)
				this.profilerSummary.put(this.key, 0L);

			Long storedtime = this.profilerCurrent.get(this.key);
			Long cntr = this.profilerCounter.get(this.key);
			if (storedtime == null || storedtime < 0) // negative time is the sign of a deltatime. This is replaced by a new starttime.
			{
				this.profilerCurrent.put(this.key, (new Date()).getTime()); // new starttime
				this.delta = 0;
				if (cntr != null)
					this.counter = this.profilerCounter.get(this.key) + 1;
				else
					this.counter = 1;
			}
			else
			{
				if (cntr == null)
					this.profilerCounter.put(this.key, 1L);
				else
					this.profilerCounter.put(this.key, cntr + 1);
				this.counter = this.profilerCounter.get(this.key);
				this.delta = (new Date()).getTime() - storedtime;
				this.profilerCurrent.put(this.key, -this.delta); // delta is stored as a negative time
				this.summary = this.profilerSummary.get(this.key) + this.delta;
				this.profilerSummary.put(this.key, this.summary);
			}
		}
	}

	private String key = "profile"; //$NON-NLS-1$

	private long delta = 0;

	private long summary = 0;

	private long counter = 0;

	/**
	 * @param level
	 * @param format
	 * @param args
	 * @return formated string
	 */
	@SuppressWarnings( { "nls", "boxing" })
	protected String format(int level, String prefix, String format, Object args)
	{
		String output = "";
		// Parse the format string for needed parameters
		StringTokenizer tokens = new StringTokenizer(format, "%", true);
		while (tokens.hasMoreElements()) // for each % in String format
		{
			char type = ' '; // marks what value should be formatted
			String formatstring = ""; // one formatstring
			String rest = ""; // text between end of a formatstring and the next begin of a formatstring
			String element = (String) tokens.nextElement();
			if ("%".equals(element)) // formatstring found
			{
				formatstring += element;
				element = tokens.nextToken(); // String between two '%' or until end of line
				type = element.charAt(0);
				formatstring += type + "{";
				StringTokenizer arguments = new StringTokenizer(element, "{}");
				arguments.nextToken();
				try
				{
					formatstring += arguments.nextToken();
				}
				catch (RuntimeException e)
				{
					rest = element.substring(1);
				}
				formatstring += "}";
				while (arguments.hasMoreTokens())
					rest += arguments.nextToken();
				// if (priority == ERR) System.out.println(element + "[" + rest + "]");
			}
			else
			// element is a normal text
			{
				output += element;
			}
			// System.out.println(type + ": " + formatstring);
			switch (type)
			{
				case 'C':
				{
					// System.out.println("# %C Fullqualified classname");
					output += new Field(formatstring, this.clazz.getName());
					output += rest;
					break;
				}
				case 'c':
				{
					String classname = this.clazz.getName().substring(this.clazz.getName().lastIndexOf(".") + 1);
					output += new Field(formatstring, classname);
					output += rest;
					break;
				}
				case 'd':
				{
					String timeformat = formatstring.substring(formatstring.indexOf('\'') + 1, formatstring.lastIndexOf('\''));
					formatstring = formatstring.substring(0, formatstring.indexOf('\'')) + "}";
					// System.out.println("# %d Date and time: " + formatstring + "\\" + timeformat);
					SimpleDateFormat formatter = new SimpleDateFormat(timeformat);
					String time = new String(formatter.format(new Date()));
					output += new Field(formatstring, time);
					output += rest;
					break;
				}
				case 'T':
				{
					output += LoggingConfiguration.getPrimitive(level);
					output += rest;
					break;
				}
				case 'm':
				{
					// System.out.println("# %m Methodname (used from the in() method)");
					output += new Field(formatstring, getMethod());
					output += rest;
					// text += formatstring + rest;
					break;
				}
				case 'n':
				{
					// printout(priority, output);
					output += System.getProperty("line.separator");
					String firstpart = getOutputprefix(); // First part is the same for every key
					output += format(level, prefix, firstpart, args);
					output += rest;
					break;
				}
				case 's':
				{
					// System.out.println("# %s The user defined message");
					StringTokenizer myTokenizer = new StringTokenizer("" + args, "\n\r"); //$NON-NLS-1$
					int i = 0;
					while (myTokenizer.hasMoreElements())
					{ // writes every line of the stacktrace
						if(i>0)
							output += System.getProperty("line.separator") + prefix;
						output += myTokenizer.nextToken();
						i++;
						//System.out.println();
					}
					//output += args;
					output += rest;
					break;
				}
				case 't':
				{
					// System.out.println("# %t Threadname");
					output += new Field(formatstring, Thread.currentThread().getName());
					output += rest;
					break;
				}
				case 'g':
				{
					// System.out.println("# %g Threadgroupname");
					output += new Field(formatstring, Thread.currentThread().getThreadGroup().getName());
					output += rest;
					break;
				}
				case '%':
				{
					// TODOSystem.out.println("# %% the percent sign");
					output += formatstring;
					output += rest;
					break;
				}
				case 'i':
				{
					// System.out.println("# %i The indent of the tracing feature");
					output += getInset();
					output += rest;
					break;
				}
				case 'e':
				{
					// System.out.println( key + " = " + value + " : " + format);
					Throwable exception = (Throwable) ((Object[]) args)[1];
					output += exception.toString();
					// printout(priority, output);
					output += rest;
					// output=rest;
					break;
				}
				case 'o':
				{
					String method = (String) ((Object[]) args)[0];
					for (String txt = getStack().peek(); !txt.equals(method); txt = getStack().peek())
					{ // remove the method-stack from the uncatching methods
						String methodname = getStack().pop();
						if ((this.mask & TRC) != 0)
						{
							StringBuffer sb = getInset();
							if (sb.length() > 0)
								sb.setLength(sb.length() - 2);
							output = output.substring(0, output.length() - 2);
							output += methodname + " - aborted!" + System.getProperty("line.separator");
							String firstpart = getOutputprefix(); // First part is the same for every key
							output += format(level, prefix, firstpart, args);
						}
					}
					output += rest;
					break;
				}
				case 'x':
				{
					String header = output.substring(output.lastIndexOf(System.getProperty("line.separator")));
					// System.out.println(header);
					Throwable exception = (Throwable) ((Object[]) args)[1];
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					exception.printStackTrace(new PrintWriter(stream, true));
					String exceptionTrace = "" + stream; //$NON-NLS-1$
					StringTokenizer myTokenizer = new StringTokenizer(exceptionTrace, "\n\r"); //$NON-NLS-1$
					while (myTokenizer.hasMoreElements())
					{ // writes every line of the stacktrace
						output += header + myTokenizer.nextToken();
					}
					output += rest;
					break;
				}
				case 'p':
				{
					if (this.delta < REFERENCE_TIME) // real delta
						output += this.delta;
					else
						output += "" + 0;
					output += rest;
					break;
				}
				case 'P':
				{
					output += this.profilerSummary.get(this.key);
					output += rest;
					break;
				}
				case 'k':
				{
					output += this.key;
					output += rest;
					break;
				}
				case 'A':
				{
					Long cntr = this.profilerCounter.get(this.key);
					if (cntr != null && cntr != 0)
						output += this.profilerSummary.get(this.key) / cntr;
					else
						output += "---";
					output += rest;
					break;
				}
				case 'N':
				{
					output += this.counter;
					output += rest;
					break;
				}
				default:
				{
					output += rest;
					break;
				}
			}
		}
		return output;
	}

	/**
	 * @return indent
	 */
	protected StringBuffer getInset()
	{ //
		StringBuffer text;
		text = (StringBuffer) (threads.get(Thread.currentThread()).get(0));
		return text;
	}

	/**
	 * @return Stack of methodnames
	 */
	@SuppressWarnings("unchecked")
	protected Stack<String> getStack()
	{ //
		ArrayList<Object> list = threads.get(Thread.currentThread());
		if (list == null) // neuer Thread?
			list = newData();
		threads.put(Thread.currentThread(), list);
		Stack<String> stack = (Stack<String>) list.get(1);
		return stack;
	}

	/**
	 * @return Array of 3 Values: indent (StringBuffer), methodame stack (Stack<String>), Threadname (String)
	 */
	@SuppressWarnings("nls")
	protected ArrayList<Object> newData()
	{
		String thread = null;
		thread = new Field(Justify.RIGHT, null, 12, 12, Thread.currentThread().getThreadGroup().getName()) + "." + new Field(Justify.LEFT, null, 0, 8, Thread.currentThread().getName());
		ArrayList<Object> liste = new ArrayList<Object>(6);
		liste.add(new StringBuffer()); // 0 TextEinschub
		liste.add(new Stack<String>()); // 1 MethodenStack
		liste.add(thread); // 2 ThreadName
		// liste.addElement(file); // 3 FileDecriptor
		return liste;
	}

	/**
	 * @return current method
	 */
	protected String getMethod()
	{ //
		return getStack().peek();
	}

	/**
	 * @return Threadname
	 */
	protected String getThread1()
	{ //
		return (String) (threads.get(Thread.currentThread()).get(2));
	}

	/**
	 * @return Prefix
	 */
	protected abstract String getOutputprefix();

}
