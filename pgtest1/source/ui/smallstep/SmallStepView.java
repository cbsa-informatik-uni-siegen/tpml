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
		center = node.getRuleSize().width;
		while (node.hasChildren()) {
			node = (SmallStepNode)node.getFirstChild();
			Dimension size = node.getRuleSize();
			if (size.width > center) {
				center = size.width;
			}
		}
		SmallStepNode.setCenter(center);
	}
	
	private void determineBounds (SmallStepNode node, int top) {
		// the required size only has to checked for the size available
		// from this View minus the size needed for the left part of the 
		// rules minus the indentation of 25 pixels infront of all and minus
		// 25 pixels on the right
		Dimension size = node.getRequiredSize(getWidth() - SmallStepNode.getCenter() - 50);
		int left = 25;
		int right = left + size.width;
		int bottom = top + size.height;
		
		int width = right - left;
		int height = bottom - top;
		
		node.setBounds(left, top, width, height);
		
		if (node.hasChildren()) {
			determineBounds ((SmallStepNode)node.getFirstChild(), bottom + 10);
		}
		
	}
	
	private void determineBounds() {
		SmallStepNode node = (SmallStepNode)rootNode;
		determineBounds (node, 25);
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
		doLayouting();
		repaint();
	}
	
	protected void doLayouting() {
		this.requiredSize.width 	= 0;
		this.requiredSize.height 	= 0;
		determineCenter();
		determineBounds();
		determineButtons();
	}
	
	protected AbstractNode createNode (ProofNode node) {
		return new SmallStepNode ((SmallStepProofNode)node);
	}
	
	/**
	 * Paints the content of the SmallStepView
	 */
	public void paintComponent (Graphics g) {
		Graphics2D g2d = (Graphics2D)g.create();
		
		doLayouting();
		
		// clear the background 
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		// TODO now do the rendering
		
		// this is a debug rendering to proove the right size
		g2d.setColor(Color.BLACK);
		g2d.drawRect(0, 0, getWidth(), getHeight());
	}
	
}
