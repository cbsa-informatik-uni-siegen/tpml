package de.unisiegen.tpml;
import java.util.LinkedList;
import java.util.Properties;


public class Debug
{
	static LinkedList<String> userDebug=new LinkedList<String>();
	
	public static void print(String out,String name)
	{
		Properties prop = System.getProperties();
		
		userDebug.add(prop.getProperty("user.name"));
		
		for (int i=0; i< userDebug.size();i++)
		{
			if (name.equalsIgnoreCase(userDebug.get(i)))
			{
				System.out.print(out);
			}
		}
	}
	
	public static void println(String out,String name)
	{
		Properties prop = System.getProperties();
		
		userDebug.add(prop.getProperty("user.name"));
		
		for (int i=0; i< userDebug.size();i++)
		{
			if (name.equalsIgnoreCase(userDebug.get(i)))
			{
				System.out.println(out);
			}
		}
	}
	
	public static void printerr(String out,String name)
	{
		Properties prop = System.getProperties();
		
		userDebug.add(prop.getProperty("user.name"));
		
		for (int i=0; i< userDebug.size();i++)
		{
			if (name.equalsIgnoreCase(userDebug.get(i)))
			{
				System.err.print(out);
			}
		}
	}
	
	public static void printerrln(String out,String name)
	{
		Properties prop = System.getProperties();
		
		userDebug.add(prop.getProperty("user.name"));
		
		for (int i=0; i< userDebug.size();i++)
		{
			if (name.equalsIgnoreCase(userDebug.get(i)))
			{
				System.err.println(out);
			}
		}
	}
}
