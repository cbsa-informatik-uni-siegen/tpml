package ui.bigstep;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import javax.swing.Scrollable;
import javax.swing.event.TreeModelEvent;

import bigstep.BigStepProofNode;

import common.ProofNode;

import ui.AbstractNode;
import ui.AbstractView;

public class BigStepView extends AbstractView implements Scrollable {

	private	int		availableWidth;
	
	/**
	 * Only used temporarily
	 */
	private int 	maxWidth;
	
	/**
	 * Only used temporarily
	 */
	private int 	maxHeight;
	
	/**
	 * Only used temporarily
	 */
	private int		nodeId;
	
	public BigStepView () {
		super ();
		
		setLayout (null);
	}
	
	public int getAvailableWidth() {
		return availableWidth;
	}

	public void setAvailableWidth(int availableWidth) {
		this.availableWidth = availableWidth;
	}
	
	private Dimension layout (ProofNode node, int inset) {
		int margin = 25;
		int spacing = 10;
		int realSize = this.availableWidth - inset - margin;
		
		BigStepNode bigStepNode = (BigStepNode)node.getUserObject();
		if (bigStepNode == null) {
			bigStepNode = new BigStepNode ((BigStepProofNode)node);
			bigStepNode.setModel(this.model);
			bigStepNode.buildMenu();
			node.setUserObject(bigStepNode);
			
		}
		bigStepNode.setLocationId(nodeId++);
		add (bigStepNode);
		Dimension d = bigStepNode.layout(this.availableWidth - inset);
		
		if (d.width + inset + margin> maxWidth) {
			maxWidth = d.width + inset + margin;
		}
		
		bigStepNode.setBounds(inset + margin, maxHeight + spacing, d.width, d.height);
		
		maxHeight += d.height + spacing;
		
		for (int i=0; i<node.getChildCount(); i++) {
			
			layout (node.getChildAt(i), inset + 25);
			
		}
		
		
		
		return new Dimension (maxWidth, maxHeight);
	}
	
	public void layout(int availableWidth) {
		removeAll ();
		this.availableWidth = availableWidth;
		maxWidth 	= 0;
		maxHeight = 25;
		nodeId		= 1;
		Dimension size = layout (this.model.getRoot(), 0);
		
		setSize (size);
		setPreferredSize (size);
		
		setMinimumSize (size);
		setMaximumSize (size);
	}
	
	private void paintNodeArrow(ProofNode node, Graphics g) {
		
		FontMetrics fm = getFontMetrics (getFont ());

		g.setColor(Color.BLACK);
		if (node.getChildCount() == 0) return;
		
		BigStepNode rootNode = (BigStepNode)node.getUserObject();
		if (rootNode == null) return;
		
		Point rootPoint = rootNode.getArrowPoint();
		rootPoint.x += rootNode.getX();
		rootPoint.y += rootNode.getY();
		
		int a, b;
		a = fm.getHeight();
		b = a / 2;
		Polygon arrow = new Polygon();
		arrow.addPoint(rootPoint.x, rootPoint.y);
		arrow.addPoint(rootPoint.x + b, rootPoint.y + a);
		arrow.addPoint(rootPoint.x, rootPoint.y + b);
		arrow.addPoint(rootPoint.x - b, rootPoint.y + a);
		g.fillPolygon(arrow);
		
		for (int i=0; i<node.getChildCount(); i++) {
			ProofNode childNode = node.getChildAt(i);
			
			BigStepNode bigStepNode = (BigStepNode)childNode.getUserObject();
			if (bigStepNode == null) continue;
			
			
			Point jointPoint = bigStepNode.getJointPoint();
			jointPoint.x += bigStepNode.getX();
			jointPoint.y += bigStepNode.getY();
			
			g.drawLine(rootPoint.x, rootPoint.y, rootPoint.x, jointPoint.y);
			g.drawLine(rootPoint.x, jointPoint.y, jointPoint.x - 2, jointPoint.y);
			
			paintNodeArrow (childNode, g);
		}
	}
	
	
	@Override
	protected AbstractNode createNode(ProofNode node) {
		return null;
	}

	@Override
	protected void relayout() {
		doLayouting();
	}

	@Override
	protected void doLayouting() {
		layout (this.availableWidth);
	}	

	@Override
	protected void nodeAdded(AbstractNode node) {
	}
	
	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		
		Object[] children = e.getChildren();
		if (children == null) {
			BigStepNode node = (BigStepNode)this.model.getRoot().getUserObject();
			if (node != null) {
				node.updateNode();
				System.out.println("nodeChanged: " + model.getRoot().getExpression ());
			}
		}
		else {
			for (Object o : children) {
				ProofNode pNode = (ProofNode)o;
				BigStepNode node = (BigStepNode)pNode.getUserObject();
				if (node != null) {
					node.updateNode();
				}
				System.out.println("nodeChanged: " + pNode.getExpression ());
			}
		}
		doLayouting();
	}

	/**
	 * Paints the content of the BigStepView
	 */
	@Override
	public void paintComponent (Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());		
		
		g.drawLine(0, 0, getWidth() - 1, getHeight() - 1);
		g.drawLine(0, getHeight() - 1, getWidth() - 1, 0);
		
		paintNodeArrow (this.model.getRoot(), g);
	}


	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 75;
	}

	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 75;
	}


}
