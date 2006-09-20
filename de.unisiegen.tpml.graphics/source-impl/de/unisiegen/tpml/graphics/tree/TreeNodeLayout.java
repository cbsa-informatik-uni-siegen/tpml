package de.unisiegen.tpml.graphics.tree;

import java.awt.Dimension;
import java.awt.Point;

import de.unisiegen.tpml.core.ProofNode;

public class TreeNodeLayout {
	
	private int availableWidth;
	
	private int spacing;

	public TreeNodeLayout () {
		this.spacing = 10;
	}
	
	public TreeNodeLayout (int spacing) {
		this.spacing = spacing;
	}
	
	public void setSpacing (int spacing) {
		this.spacing = spacing;
	}
	
	public Point placeNodes (ProofNode root, int posX, int posY, int availableWidth) {
		this.availableWidth = availableWidth;
		
		return placeNode (root, posX, posY, new Point (-1, -1));
	}
	
	/**
	 * Calculates the size for a node and does the placing
	 * of the Component. 
	 * 
	 * @param node  The node that should be
	 * @param posX  The left position of the node
	 * @param posY	The top position of the node
	 * @return The right bottom pos of the needed width
	 */
	private Point placeNode (ProofNode node, int posX, int posY, Point rightBottomPos) {
		
		
		TreeNodeComponent nodeComponent = (TreeNodeComponent)node.getUserObject();
		
		if (nodeComponent == null) {
			// this should not happen
			return rightBottomPos;
		}
		
		// the available width is shrinked by the already spended
		// space by indentating the node
		int availableWidth = this.availableWidth - posX;
		Dimension size = nodeComponent.placeElementsForMaxWidth(availableWidth);
		
		
		// do the real positioning of the node
		nodeComponent.setBounds(posX, posY, size.width, size.height);
		
		// let some spacing between two nodes
		posY += this.spacing;
	
		//
		// change the resulting point
		//
		// check if this node widens the resulting area
		if (posX + size.width > rightBottomPos.x) {
			rightBottomPos.x = posX + size.width;
		}
	
		// we will always go down so the new yPos is always bigger
		rightBottomPos.y = posY + size.height;
		
		
		posY += size.height;
		posX += nodeComponent.getIndentationWidth();
		
		
		// delegate the rest to the other nodes
		for (int i=0; i<node.getChildCount(); i++) {
			ProofNode pNode = node.getChildAt(i);
			
			placeNode (pNode, posX, posY, rightBottomPos);
			
			// increment the posY for the next node 
			posY = rightBottomPos.y;
			
		}
		
		return rightBottomPos;
	}
	
}
