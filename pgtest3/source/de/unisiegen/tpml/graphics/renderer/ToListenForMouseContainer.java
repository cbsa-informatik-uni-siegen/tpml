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
	
	private int [] hereIam;
	
	private boolean mark;
	
	private int rightList;
	
	private static ToListenForMouseContainer myToListenForMouseContainer = null;
	
	private ToListenForMouseContainer()
	{
		toListenForMouse = new Vector();
		mark = false;
		hereIam = new int [2];
		hereIam[0] = 0;
		hereIam[1] = 0;
		rightList = -2;
		
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
	
	public void setHereIam(int x, int y)
	{
		hereIam[0] = x;
		hereIam[1] = y;
	}
	
	public int [] getHereIam ()
	{
		return hereIam;
	}
	
	public void setRightList (int x)
	{
		if (x == -1)
		{
			System.err.println("Der will verbotener weise -1 setzen, scheiﬂe!");
			//System.exit(125);
		}
			
		rightList = x;
  }
	
	public int getRightList ()
	{
		return rightList;
	}
	
}
