package ui.smallstep;

import common.ProofNode;
import common.ProofRule;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JViewport;
import javax.swing.Scrollable;

import smallstep.SmallStepProofNode;

import ui.AbstractNode;
import ui.AbstractView;


public class SmallStepView extends AbstractView implements Scrollable {
	
	private Dimension	requiredSize 	= new Dimension ();
	
	private Dimension	availableSize	= new Dimension();
	
	public SmallStepView () {
		super();
		this.setLayout(null);
		
	}
	
	public void setAvailableSize (Dimension size) {
		this.availableSize = size;
		relayout();
	}
		
	private void determineCenter () {
		int center = 0;
		SmallStepNode node = (SmallStepNode)rootNode;
		node.prepareRuleSize();
		center = node.getRuleSize().width;
		while (node.hasChildren()) {
			node = (SmallStepNode)node.getFirstChild();
			node.prepareRuleSize();
			Dimension size = node.getRuleSize();
			if (size.width > center) {
				center = size.width;
			}
		}
		SmallStepNode.setCenter(center);
	}
	
	private void determineNodeExpressionSize () {
		SmallStepNode node = (SmallStepNode)rootNode;
		node.prepareExpressionSize(this.availableSize.width - SmallStepNode.getCenter() - 50);
		while (node.hasChildren()) {
			node = (SmallStepNode)node.getFirstChild();
			node.prepareExpressionSize(this.availableSize.width - SmallStepNode.getCenter() - 50);
		}
	}
		
	private void determineBounds() {
		SmallStepNode node = (SmallStepNode)rootNode;
		int newY = node.setTop (25);
		while (node.hasChildren()) {
			node = (SmallStepNode)node.getFirstChild();
			newY = node.setTop(newY);
		}
	}
	
	private void determinePreferredSize() {
		int maxWidth = 0, currentWidth = 0;
		SmallStepNode node = (SmallStepNode)rootNode;
		maxWidth = currentWidth = node.getX() + node.getWidth();
		while (node.hasChildren()) {
			node = (SmallStepNode)node.getFirstChild();
			currentWidth = node.getX() + node.getWidth();
			if (currentWidth > maxWidth) {
				maxWidth = currentWidth;
			}
		}
		
		Dimension size = new Dimension (maxWidth + 25, node.getY () + node.getHeight () + 25);
		setPreferredSize (size);
		setSize(size);
	}
	
	private void determineButtons() {
		SmallStepNode node = (SmallStepNode)rootNode;
		node.placeMenuButtons();
		while (node.hasChildren()) {
			node = (SmallStepNode)node.getFirstChild();
			node.placeMenuButtons();
		}
	}
	
	private int countNodes() {
		int numNodes = 1;
		AbstractNode aNode = rootNode;
		while (aNode.hasChildren()) {
			aNode = aNode.getFirstChild();
			numNodes++;
		}
		return numNodes;
	}
	
	private void addNodes() {
		removeAll();
		SmallStepNode node = (SmallStepNode)rootNode;
		node.reset();
		add(node);
		while (node.hasChildren()) {
			node = (SmallStepNode)node.getFirstChild();
			node.reset();
			add(node);
		}
		
	}

	
	protected void relayout() {
		addNodes();
		determineCenter();
		doLayouting();
		repaint();
	}
	
	protected void doLayouting() {
		this.requiredSize.width 	= 0;
		this.requiredSize.height 	= 0;
		determineNodeExpressionSize();
		determineBounds();
		determineButtons();
		determinePreferredSize();
	}
	
	protected AbstractNode createNode (ProofNode node) {
		return new SmallStepNode (this, (SmallStepProofNode)node);
	}
	
	/**
	 * Paints the content of the SmallStepView
	 */
	public void paintComponent (Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
	}
	
	protected void nodeAdded (AbstractNode node) {
		if (getParent () instanceof JViewport) {
			JViewport vp = (JViewport)getParent();
			vp.scrollRectToVisible(node.getBounds());
		}
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
		return 25;
	}
	
}
