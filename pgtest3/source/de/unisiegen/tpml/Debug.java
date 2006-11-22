package de.unisiegen.tpml;
import java.util.LinkedList;
import java.util.Properties;


public class Debug
{
	public static DebugOutPrint out = new DebugOutPrint();
	public static DebugErrPrint err = new DebugErrPrint();
	


	public static void addUser(String name)
	{
		out.getUserDebug().add(name);
		err.getUserDebug().add(name);
	}
}
