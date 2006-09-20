package de.unisiegen.tpml.graphics.renderer;

import java.awt.Graphics;
import java.awt.Point;

import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.graphics.tree.TreeNodeComponent;

public class TreeArrowRenderer {
	
	public static void renderArrows (ProofNode node, Graphics gc) {
		
		if (node.getChildCount() == 0) {
			return;
		}
		
		TreeNodeComponent nodeComponent = (TreeNodeComponent)node.getUserObject();
		Point top = nodeComponent.getBottomArrowConnection();
		
		for (int i=0; i<node.getChildCount(); i++) {
			ProofNode childNode = node.getChildAt(i);
			TreeNodeComponent childNodeComponent = (TreeNodeComponent)childNode.getUserObject();
			Point left = childNodeComponent.getLeftArrowConnection();
			
			gc.drawLine(top.x, top.y, top.x, left.y);
			gc.drawLine(top.x, left.y, left.x, left.y);
			
			top.y = left.y;
			
			renderArrows (childNode, gc);
			
		}
		
	}
}
