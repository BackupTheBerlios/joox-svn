/* JOOX - The Java Toolbox ($Id$)
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
 * Created by hoevbert at Dec 22, 2004
 * Last Changed by $Author$
 */
package test.org.joox.generator;

import java.util.Date;

import org.joox.generator.Generator;
import org.joox.profiling.Stopwatch;

import junit.framework.TestCase;

/**
 * @author hoevbert
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GeneratorTest extends TestCase
{
	/**
	 * Comment for <code>dummy</code>
	 */
	public TestClassInterface dummy = null;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		junit.swingui.TestRunner.run(GeneratorTest.class);
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		String line = "====================================================================================================="; //$NON-NLS-1$
		System.out.println(line);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		super.tearDown();
		String line = "====================================================================================================="; //$NON-NLS-1$
		System.out.println(line);
	}

	/**
	 * Constructor for GeneratorTest.
	 * @param arg0
	 */
	public GeneratorTest(String arg0)
	{
		super(arg0);
	}

	/**
	 * Class under test for Object generate(Class)
	 */
	public final void testGenericGenerateClass()
	{
		try
		{
			Date date = Generator.generate(Date.class, new Object[0]);
			System.out.println("01. Result:" + date); //$NON-NLS-1$ //$NON-NLS-2$
			
			TestClass test = Generator.generate(TestClass.class, new Object[0]);
			System.out.println("02. Result:" + test); //$NON-NLS-1$ //$NON-NLS-2$
			
			test = Generator.generate(TestClass.class, "a"); //$NON-NLS-1$
			System.out.println("03. Result:" + test); //$NON-NLS-1$ //$NON-NLS-2$
			
			test = Generator.generate(TestClass.class, -7);
			System.out.println("04. Result:" + test); //$NON-NLS-1$ //$NON-NLS-2$
			
			test = Generator.generate(TestClass.class, "a", -7); //$NON-NLS-1$
			System.out.println("05. Result:" + test); //$NON-NLS-1$ //$NON-NLS-2$
			
			test = Generator.generate(TestClass.class, -7, "a"); //$NON-NLS-1$
			System.out.println("06. Result:" + test); //$NON-NLS-1$ //$NON-NLS-2$
			
			test = Generator.generate(TestClass.class, -7, "a", 7); //$NON-NLS-1$
			System.out.println("07. Result:" + test); //$NON-NLS-1$ //$NON-NLS-2$
			
			test = Generator.generate(TestClass.class, "a", -7, "b"); //$NON-NLS-1$//$NON-NLS-2$
			System.out.println("08. Result:" + test); //$NON-NLS-1$ //$NON-NLS-2$
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	/**
	 * Class under test for Object generate(Class)
	 */
	public final void testGenerateClass()
	{
		try
		{
			Date date = Generator.generate(Date.class);
			System.out.println("11. Result:" + date + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
			Integer integer = Generator.generate(Integer.class, 1);
			System.out.println("12. Result:" + integer + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	/**
	 * Class under test for Object generate(Class)
	 */
	public final void testGenerateClassTime_DB_noArg()
	{
		try
		{
			System.out.println("Class in Database/without args"); //$NON-NLS-1$
			Stopwatch watch = new Stopwatch(true);
			int loops = 1000000;
			for(int i = 0; i < loops; i++)
			{
				this.dummy = Generator.generate(TestClassInterface.class);
				watch.progress(i, loops);
			}
			long timeA = watch.score(loops);
			
			watch = new Stopwatch(true);
			for(int i = 0; i < loops; i++)
			{
				this.dummy = new TestClass();
				watch.progress(i, loops);
			}
			long timeB = watch.score(loops);
			
			long times = timeA / timeB;
			System.out.println("It is more than " + times + " times slower than native Object Creation. Limit is 100.");  //$NON-NLS-1$//$NON-NLS-2$
			if(times > 100) fail("Creation of Classes in Database without args is too slow. It is more than " + times + " times slower than native Object Creation.");  //$NON-NLS-1$//$NON-NLS-2$
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	/**
	 * 
	 */
	public final void testGenerateClassTime_DB_Arg()
	{
		try
		{
			System.out.println("Class in Database/with args"); //$NON-NLS-1$
			Stopwatch watch = new Stopwatch(true);
			int loops = 1000000;
			for(int i = 0; i < loops; i++)
			{
				this.dummy = Generator.generate(TestClassInterface.class, 0);
				watch.progress(i, loops);
			}
			long timeA = watch.score(loops);
			
			watch = new Stopwatch(true);
			for(int i = 0; i < loops; i++)
			{
				this.dummy =new TestClass(0);
				watch.progress(i, loops);
			}
			long timeB = watch.score(loops);
			
			long times = timeA / timeB;
			System.out.println("It is more than " + times + " times slower than native Object Creation. Limit is 150.");  //$NON-NLS-1$//$NON-NLS-2$
			if(times > 150) fail("Creation of Classes in Database with args is too slow. It is more than " + times + " times slower than native Object Creation.");  //$NON-NLS-1$//$NON-NLS-2$
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	/**
	 * 
	 */
	public final void testGenerateClassTime_noDB_noArg()
	{
		try
		{
			System.out.println("Class not in Database/without args"); //$NON-NLS-1$
			int loops = 1000000;
			Stopwatch watch = new Stopwatch(true);
			for(int i = 0; i < loops; i++)
			{
				this.dummy = Generator.generate(TestClass.class);
				watch.progress(i, loops);
			}
			long timeA = watch.score(loops);
			
			watch = new Stopwatch(true);
			for(int i = 0; i < loops; i++)
			{
				this.dummy = new TestClass();
				watch.progress(i, loops);
			}
			long timeB = watch.score(loops);
			
			long times = timeA / timeB;
			System.out.println("It is more than " + times + " times slower than native Object Creation. Limit is 10.");  //$NON-NLS-1$//$NON-NLS-2$
			if(times > 10) fail("Creation of Classes not in Database/without args is too slow. It is more than " + times + " times slower than native Object Creation.");  //$NON-NLS-1$//$NON-NLS-2$
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	/**
	 * 
	 */
	public final void testGenerateClassTime_noDB_Arg()
	{
		try
		{
			System.out.println("Class not in Database/with args"); //$NON-NLS-1$
			Stopwatch watch = new Stopwatch(true);
			int loops = 1000000;
			for(int i = 0; i < loops; i++)
			{
				this.dummy = Generator.generate(TestClass.class, 0);
				watch.progress(i, loops);
			}
			long timeA = watch.score(loops);
			
			watch = new Stopwatch(true);
			for(int i = 0; i < loops; i++)
			{
				this.dummy = new TestClass(0);
				watch.progress(i, loops);
			}
			long timeB = watch.score(loops);
			
			long times = timeA / timeB;
			System.out.println("It is more than " + times + " times slower than native Object Creation. Limit is 50.");  //$NON-NLS-1$//$NON-NLS-2$
			if(times > 50) fail("Creation of Classes not in Database with args is too slow. It is more than " + times + " times slower than native Object Creation."); //$NON-NLS-1$ //$NON-NLS-2$
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}

