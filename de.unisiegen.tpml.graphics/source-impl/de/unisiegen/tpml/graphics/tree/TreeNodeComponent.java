package de.unisiegen.tpml.graphics.tree;

import java.awt.Dimension;
import java.awt.Point;

/**
 * Interface for an node that should layouted in a tree.<br>
 * @author marcell
 *
 */
public interface TreeNodeComponent {

	/**
	 * Should return the bottom position of the object where the
	 * arrow should be placed 
	 * @return
	 */
	public Point getBottomArrowConnection ();
	
	/**
	 * Should return the left position where the lines of the
	 * tree should be connected
	 * @return
	 */
	public Point getLeftArrowConnection ();
	
	/**
	 * Should trigger an update for the node with the given
	 * maximum width.
	 * 
	 * @param maxWidth
	 * @return
	 */
	public Dimension update (int maxWidth);
	
	/**
	 * Should return the number of pixels any child node should be
	 * indentated.
	 * 
	 * @return
	 */
	public int getIndentationWidth ();
	
	/**
	 * Should delegate the method over to the {@link Component#setBounds(int, int, int, int)}-Method
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void setBounds (int x, int y, int width, int height);
}
