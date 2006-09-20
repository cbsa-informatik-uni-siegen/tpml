package de.unisiegen.tpml.graphics.renderer;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.graphics.tree.TreeNodeComponent;

public class TreeArrowRenderer {
	
	public static void renderArrows (ProofNode node, int spacing, Graphics gc) {
		
		if (node.getChildCount() == 0) {
			return;
		}
		
		TreeNodeComponent nodeComponent = (TreeNodeComponent)node.getUserObject();
		Point top = nodeComponent.getBottomArrowConnection();
		
		Polygon arrow = new Polygon ();
		arrow.addPoint(top.x, top.y);
		arrow.addPoint(top.x + spacing/2, top.y + spacing);
		arrow.addPoint(top.x, top.y + spacing/2);
		arrow.addPoint(top.x - spacing/2, top.y + spacing);
		gc.fillPolygon(arrow);
		
		
		
		for (int i=0; i<node.getChildCount(); i++) {
			ProofNode childNode = node.getChildAt(i);
			TreeNodeComponent childNodeComponent = (TreeNodeComponent)childNode.getUserObject();
			Point left = childNodeComponent.getLeftArrowConnection();
			
			gc.drawLine(top.x, top.y, top.x, left.y);
			gc.drawLine(top.x, left.y, left.x, left.y);
			
			top.y = left.y;
			
			renderArrows (childNode, spacing, gc);
			
		}
		
	}
}
