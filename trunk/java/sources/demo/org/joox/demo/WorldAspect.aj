/**
 * 
 */
package org.joox.demo;

/**
 * @author bert
 * 
 */
public aspect WorldAspect
{
	public pointcut mainOperation(): execution(public static void  World.main(String[]));
	
	before(): mainOperation()
	{
		System.out.println("Hello aspect orientated world!");
	}
}
