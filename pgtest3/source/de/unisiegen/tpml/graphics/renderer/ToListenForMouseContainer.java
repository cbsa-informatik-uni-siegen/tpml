package de.unisiegen.tpml.graphics.renderer;

import java.util.Vector;
/**
 * TODO Kommentare schreiebn
 * @author Feivel
 *
 */
public class ToListenForMouseContainer
{
	private Vector toListenForMouse;
	
	private boolean mark;
	
	private static ToListenForMouseContainer myToListenForMouseContainer = null;
	
	private ToListenForMouseContainer()
	{
		toListenForMouse = new Vector();
		mark = false;
	}
	
	public static ToListenForMouseContainer getInstanceOf()
	{
		if (myToListenForMouseContainer == null)
		{
			myToListenForMouseContainer = new ToListenForMouseContainer();
		return myToListenForMouseContainer;
		}
		else
		{
			return myToListenForMouseContainer;	
		}
		
	}
	
	public void add (int toadd)
	{
		toListenForMouse.add(toadd);
	}
	
	public int get (int i)
	{
		int result = 0;
		result = Integer.valueOf((Integer)toListenForMouse.get(i));
		return result;
	}
	
	public boolean isEmpty ()
	{
		if (toListenForMouse.size()!= 0 ) return false;
		return true;
	}
	
	public void reset ()
	{
		toListenForMouse.removeAllElements();
	}
	
	public int size()
	{
		return toListenForMouse.size();
	}
	
	public void setElementAt (int pos, int val)
	{
		toListenForMouse.setElementAt(val, pos);
	}
	
	public boolean getMark()
	{
		return mark;
	}
	
	public void setMark(boolean b)
	{
		mark = b;
	}
	
}
