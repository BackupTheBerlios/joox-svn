/**
 * TODO Exceptions filtern mittels Annotations welche Exceptions nicht als solche protokolliert werden sollen 
 * TODO Erkennen, ob returnwert ausgegeben werden muss
 * TODO System.out/err.print implementieren
 */
package org.joox.logging;

import java.io.PrintStream;

import org.joox.environment.Environment;

/**
 * @author bert
 * 
 */
public aspect LoggingAspect
{
	// private String __mEthOdnAmE__;

	// private static Map<Class, Logger> loggerMap = new HashMap();

	private static Logger initLogger(Class clazz)
	{
		return Environment.getInstance().getLogger(clazz);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 */
	public pointcut staticinit(): 
		staticinitialization(*..*)  && !within(LoggingAspect)
		&& !staticinitialization(org.joox.logging.*)
		&& !staticinitialization(org.joox.configuration.*)
		&& !staticinitialization(org.joox.environment.*);

	before(): staticinit()
	{

		Logger logger = initLogger(thisJoinPointStaticPart.getSignature().getDeclaringType());
		logger.in(thisJoinPointStaticPart.getSignature().getName()); //$NON-NLS-1$
	}

	after() returning: staticinit()
	{
		Logger logger = initLogger(thisJoinPointStaticPart.getSignature().getDeclaringType());
		logger.out();
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 */
	public pointcut publicinit(): 
		execution(public *..new(..))  && !within(LoggingAspect)
		&& !execution(public org.joox.logging..new(..))
		&& !execution(public org.joox.configuration..new(..))
		&& !execution(public org.joox.environment..new(..));

	before(): publicinit()
	{
		Logger logger = initLogger(thisJoinPointStaticPart.getSignature().getDeclaringType());
		logger.in(Logger.TRC, thisJoinPointStaticPart.getSignature().getName(), thisJoinPoint.getArgs()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	after() returning: publicinit()
	{
		Logger logger = initLogger(thisJoinPointStaticPart.getSignature().getDeclaringType());
		logger.out(Logger.TRC);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 */
	public pointcut protectedinit(): 
		execution(protected *..new(..))  && !within(LoggingAspect)
		&& !execution(protected org.joox.logging..new(..)) 
		&& !execution(protected org.joox.configuration..new(..)) 
		&& !execution(protected org.joox.environment..new(..));

	before(): protectedinit()
	{
		Logger logger = initLogger(thisJoinPointStaticPart.getSignature().getDeclaringType());
		logger.in(Logger.TRD, thisJoinPointStaticPart.getSignature().getName(), thisJoinPoint.getArgs()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	after() returning: protectedinit()
	{
		Logger logger = initLogger(thisJoinPointStaticPart.getSignature().getDeclaringType());
		logger.out(Logger.TRD);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 */
	public pointcut publicMethod(): 
		execution(public * *..*(..)) && !within(LoggingAspect)
		&& !execution(public * org.joox.logging..*(..))
		&& !execution(public * org.joox.configuration..*(..))
		&& !execution(public * org.joox.environment..*(..))
		;

	before(): publicMethod()
	{
		Logger logger = initLogger(thisJoinPointStaticPart.getSignature().getDeclaringType());
		logger.in(Logger.TRC, thisJoinPointStaticPart.getSignature().getName(), thisJoinPoint.getArgs()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	after() returning(Object o): publicMethod()
	{
		Logger logger = initLogger(thisJoinPointStaticPart.getSignature().getDeclaringType());
		logger.out(Logger.TRC, o);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 */
	public pointcut protectedMethod(): 
		execution(protected * *..*(..))  && !within(LoggingAspect)
		&& !execution(protected * org.joox.logging..*(..))
		&& !execution(protected * org.joox.configuration..*(..))
		&& !execution(protected * org.joox.environment..*(..));

	before(): protectedMethod()
	{
		Logger logger = initLogger(thisJoinPointStaticPart.getSignature().getDeclaringType());
		logger.in(Logger.TRD, thisJoinPointStaticPart.getSignature().getName(), thisJoinPoint.getArgs()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	after() returning(Object o): protectedMethod()
	{
		Logger logger = initLogger(thisJoinPointStaticPart.getSignature().getDeclaringType());
		logger.out(Logger.TRD, o);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 */
	public pointcut defaultMethod(): 
		execution(* *..*(..))  && !within(LoggingAspect)
		&& !execution(public * *..*(..)) 
		&& !execution(protected * *..*(..)) 
		&& !execution(private * *..*(..)) 
		&& !execution(* org.joox.logging..*(..))
		&& !execution(* org.joox.configuration..*(..))
		&& !execution(* org.joox.environment..*(..));

	before(): defaultMethod()
	{
		Logger logger = initLogger(thisJoinPointStaticPart.getSignature().getDeclaringType());
		logger.in(Logger.TRT, thisJoinPointStaticPart.getSignature().getName(), thisJoinPoint.getArgs()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	after() returning(Object o): defaultMethod()
	{
		Logger logger = initLogger(thisJoinPointStaticPart.getSignature().getDeclaringType());
		logger.out(Logger.TRT, o);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 */
	public pointcut privateMethod(): 
		execution(private * *..*(..))  && !within(LoggingAspect)
		&& !execution(private * org.joox.logging..*(..))
		&& !execution(private * org.joox.configuration..*(..))
		&& !execution(private * org.joox.environment..*(..));

	before(): privateMethod()
	{
		Logger logger = initLogger(thisJoinPointStaticPart.getSignature().getDeclaringType());
		logger.in(Logger.TRE, thisJoinPointStaticPart.getSignature().getName(), thisJoinPoint.getArgs()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	after() returning(Object o): privateMethod()
	{
		Logger logger = initLogger(thisJoinPointStaticPart.getSignature().getDeclaringType());
		logger.out(Logger.TRE, o);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 */
	public pointcut systemExit(): 
		call(public void System.exit(..)) && !within(LoggingAspect);

	before(): systemExit()
	{
		Logger logger = initLogger(thisJoinPointStaticPart.getSignature().getDeclaringType());
		logger.ext((Integer) thisJoinPoint.getArgs()[0]);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 */
	public pointcut exceptionCatcher(): 
		handler (Throwable+) && !within(LoggingAspect)
		&& !within(org.joox.logging..*)
		&& !within(org.joox.configuration..*)
		&& !within(org.joox.environment..*);

	before()  : exceptionCatcher()
	{
		Logger logger = initLogger(thisJoinPointStaticPart.getSignature().getDeclaringType());
		logger.err(thisEnclosingJoinPointStaticPart.getSignature().getName(), (Throwable) thisJoinPoint.getArgs()[0]); //$NON-NLS-1$
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 */
	public pointcut setter():
		set (* *..*) && !within(LoggingAspect)
		&& !within(org.joox.logging..*)
		&& !within(org.joox.configuration..*)
		&& !within(org.joox.environment..*);

	after(): setter()
	{
		Logger logger = initLogger(thisJoinPointStaticPart.getSignature().getDeclaringType());
		logger.var(thisJoinPointStaticPart.getSignature().getName(), thisJoinPoint.getArgs()[0]); //$NON-NLS-1$ //$NON-NLS-2$
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @param prntstrm
	 */
	public pointcut sysprintln(PrintStream prntstrm):
		call(public * java.io.PrintStream+.println(..)) && !within(LoggingAspect) && target(prntstrm)
		&& !within(org.joox.logging..*)
		&& !within(org.joox.configuration..*)
		&& !within(org.joox.environment..*);

	void around(PrintStream prntstrm) : sysprintln(prntstrm)
	{
		if(System.out == prntstrm)
		{
			Logger logger = initLogger(thisJoinPointStaticPart.getSignature().getDeclaringType());
			logger.msg("" + thisJoinPoint.getArgs()[0]);
		}
		else if(System.err == prntstrm)
		{
			Logger logger = initLogger(thisJoinPointStaticPart.getSignature().getDeclaringType());
			logger.wrn("" + thisJoinPoint.getArgs()[0]);
		}
		else
			proceed(prntstrm);
	}
}
