package ui.smallstep;

import common.ProofNode;
import common.ProofRule;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import smallstep.SmallStepProofNode;

import ui.AbstractNode;
import ui.AbstractView;


public class SmallStepView extends AbstractView {
	
	private Dimension	requiredSize = new Dimension ();
	
	
	public SmallStepView () {
		super();
		this.setLayout(null);
		
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
		node.prepareExpressionSize(getWidth() - SmallStepNode.getCenter() - 50);
		while (node.hasChildren()) {
			node = (SmallStepNode)node.getFirstChild();
			node.prepareExpressionSize(getWidth() - SmallStepNode.getCenter() - 50);
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
	
	private void determineButtons() {
		SmallStepNode node = (SmallStepNode)rootNode;
		node.placeMenuButtons();
		while (node.hasChildren()) {
			node = (SmallStepNode)node.getFirstChild();
			node.placeMenuButtons();
		}
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
	}
	
	protected AbstractNode createNode (ProofNode node) {
		return new SmallStepNode (this, (SmallStepProofNode)node);
	}
	
	/**
	 * Paints the content of the SmallStepView
	 */
	public void paintComponent (Graphics g) {
		// clear the background 
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
	}
	
}
