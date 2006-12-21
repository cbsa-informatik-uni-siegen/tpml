package de.unisiegen.tpml.graphics.renderer;

import java.util.ArrayList;
/**
 * here, the mousepositions where the mous over effekt will be aktivated
 * are safed. <br>
 * this class privides the comunication between the PrittyStringRenderer and the 
 * CompoundExpression
 * 
 * @author Feivel
 * 
 * @see de.unisiegen.tpml.graphics.components.CompoundExpression
 * @see de.unisiegen.tpml.graphics.renderer.PrettyStringRenderer
 *
 */
public class ToListenForMouseContainer
{
	/**
	 * will contain the positions where bounded identifiers are
	 */
	private ArrayList<Integer> toListenForMouse;
	
	/**
	 * saves teh psoition, where the mouse pointer is.
	 * [0] represets the x-koordinate
	 * [1] represents the y-koordiante
	 */
	private int [] hereIam;
	
	/**
	 * shows if the highliter is aktive or not
	 */
	private boolean mark;
	//counts the marks: the first time, mark is set to true will be ignored
	private int markCount;
	
	/**
	 * saves informations about the right anootationlist in wich the chars are that shoeld be marked
	 */
	private int rightList;
	
	
	public ToListenForMouseContainer()
	{
		this.toListenForMouse = new ArrayList<Integer>();
        this.mark = false;
        this.markCount = 0;
        this.hereIam = new int [2];
        this.hereIam[0] = 0;
        this.hereIam[1] = 0;
        this.rightList = -2;
	}
	/**
	 * adds a position to the ArrayList
	 * TODO 4 ints heve to be added. mybe a method with 4 ints...
	 * @param toadd - int with x, y, x1 or y1 position
	 */
	public void add (int toadd)
	{
		toListenForMouse.add(toadd);
	}
	
	/**
	 * returns one part of a position of ArrayList
	 * @param i - int witch value to return
	 * @return int with value
	 */
	public int get (int i)
	{
		int result = 0;
		result = Integer.valueOf((Integer)toListenForMouse.get(i));
		return result;
	}
	
	/**
	 * get the infromation if the ArrayList ist empty or not
	 * @return - booelan, true if empty
	 */
	public boolean isEmpty ()
	{
		if (toListenForMouse.size()!= 0 ) return false;
		return true;
	}
	
	/**
	 * clears all entris from ArrayList
	 *
	 */
	public void reset ()
	{
		toListenForMouse = new ArrayList<Integer>();	
	}
	
	/**
	 * returns the size of the ArrayList
	 * @return
	 */
	public int size()
	{
		return toListenForMouse.size();
	}

	
	/**
	 * returns true if it should be marked teh bindings
	 * @return
	 */
	public boolean getMark()
	{
		return mark;
	}
	
	/**
	 * sets the boolean if ist should be marked or not
	 * CATION the first time, mark is set to true, it will be ignored. 
	 * @param b
	 */
	public void setMark(boolean b)
	{
		if (!b)
		{
			mark = b;
		}
		else if (markCount >= 1)
		{
			mark = b;
		}
		else
		{
			markCount++;
		}
	}
	
	/**
	 * sets the position where the pointer is
	 * @param x
	 * @param y
	 */
	public void setHereIam(int x, int y)
	{
		hereIam[0] = x;
		hereIam[1] = y;
	}
	
	/**
	 * returns the position where the pointer is
	 * @return
	 */
	public int [] getHereIam ()
	{
		return hereIam;
	}
	
	/**
	 * sets the list where the mouseover effect will be activated
	 * @param x
	 */
	public void setRightList (int x)
	{
		rightList = x;
  }
	
	/**
	 * returns the list where the mouseovereffect will be activated
	 * @return
	 */
	public int getRightList ()
	{
		return rightList;
	}
}
