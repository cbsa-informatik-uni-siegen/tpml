package de.unisiegen.tpml;

import java.util.LinkedList;
import java.util.Properties;

public class DebugErrPrint
{
	
	private LinkedList<String> userDebug=new LinkedList<String>();

	
	public DebugErrPrint()
	{
		Properties prop = System.getProperties();
		userDebug.add(prop.getProperty("user.name"));
	}
	public void print(String toPrint,String username)
	{
		boolean print=false;
		
		for (int i=0; i< userDebug.size();i++)
		{
			if (username.equalsIgnoreCase(userDebug.get(i)))
			{
				print=true;
			}
		}
		if(print)
		System.err.print(toPrint);
		
	}
	
	public void println(String toPrint,String username)
	{
		boolean print=false;
		
		for (int i=0; i< userDebug.size();i++)
		{
			if (username.equalsIgnoreCase(userDebug.get(i)))
			{
				print=true;
			}
		}
		if(print)
		System.err.println(toPrint);
	
	}
	public LinkedList<String> getUserDebug()
	{
		return userDebug;
	}
}
