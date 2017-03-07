package engine;

/**
 * Utility class for logging and debugging.
 */
public class Debug
{
	public static void log(Object sender, String toLog)
	{
		System.out.println("[" + sender.getClass().getName() + "] " + toLog);
	}
	
	public static void log(Class<?> aClass, String toLog)
	{
		System.out.println("[" + aClass.getName() + "] " + toLog);
	}
	
	public static void logError(Object sender, String toLog)
	{
		System.err.println("[" + sender.getClass().getName() + "] " + toLog);
	}
	
	public static void logError(Class<?> aClass, String toLog)
	{
		System.err.println("[" + aClass.getName() + "] " + toLog);
	}
}
