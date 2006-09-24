package de.unisiegen.tpml.graphics.tree;

import java.awt.Dimension;
import java.awt.Point;

public interface TreeNodeComponent {

	public Point getBottomArrowConnection ();
	
	public Point getLeftArrowConnection ();
	
	public Dimension update (int maxWidth);
	
	public int getIndentationWidth ();
	
	public void setBounds (int x, int y, int width, int height);
}
