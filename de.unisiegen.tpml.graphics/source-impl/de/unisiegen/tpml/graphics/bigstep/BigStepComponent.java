package de.unisiegen.tpml.graphics.bigstep;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;

import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;

import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode;
import de.unisiegen.tpml.graphics.AbstractProofComponent;
import de.unisiegen.tpml.graphics.renderer.TreeArrowRenderer;
import de.unisiegen.tpml.graphics.tree.TreeNodeLayout;

public class BigStepComponent extends AbstractProofComponent implements Scrollable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3793854335585017325L;

	private TreeNodeLayout							treeNodeLayout;
	
	private int													index;
	
	public BigStepComponent (BigStepProofModel model) {
		super (model);
		this.treeNodeLayout 		= new TreeNodeLayout ();
		
		setLayout(null);
		
		// initialy the tree content has changed
		treeContentChanged();
	}
	
	public void setSpacing (int spacing) {
		this.treeNodeLayout.setSpacing(spacing);
	}
	
	public void guess () throws IllegalStateException, ProofGuessException {
    LinkedList<ProofNode> nodes = new LinkedList<ProofNode>();
    nodes.add(this.proofModel.getRoot());
    while (!nodes.isEmpty()) {
      ProofNode node = nodes.poll();
      if (node.getSteps().length == 0) {
      	
      	this.proofModel.guess(node);
      	return;
      }
      for (int n = 0; n < node.getChildCount(); ++n) {
        nodes.add(node.getChildAt(n));
      }
    }
    throw new IllegalStateException("Unable to find next node");

	}
	
	private void checkForUserObject (BigStepProofNode node) {
		if (node == null) {
			return;
		}
		
		BigStepNodeComponent nodeComponent = (BigStepNodeComponent)node.getUserObject();
		if (nodeComponent == null) {
			// if the node has no userobject it may be new in the
			// tree, so a new TypeCheckerNodeComponent will be created
			// and added to the TypeCheckerProofNode  
			nodeComponent = new BigStepNodeComponent (node, (BigStepProofModel)this.proofModel, this.translator);
			node.setUserObject(nodeComponent);
			
			// the newly created nodeComponent is a gui-element so
			// it needs to get added to the gui
			add (nodeComponent);
			
			// when the node changes the  gui needs to get updated
			nodeComponent.addBigStepNodeListener(new BigStepNodeListener () {
				public void nodeChanged (BigStepNodeComponent node) {
					BigStepComponent.this.relayout();
				}
			});
		}

		// set the index value for the node
		nodeComponent.setIndex(this.index);
		++this.index;
		
		
		// the typechecker is a real tree, so its needed to proceed with all
		// children of the node
		for (int i=0; i<node.getChildCount(); i++) {
			BigStepProofNode pNode = node.getChildAt(i);
			
			checkForUserObject (pNode);
		}
	}
	
	/*
	 * Implementation of the AbstractProofComponent interface 
	 */
	
	@Override
	protected void nodesChanged(TreeModelEvent event) {
		Object[] children = event.getChildren();
		if (children == null) {
			return;
		}
		for (int i=0; i<children.length; i++) {
			if (children[i] instanceof ProofNode) {
				BigStepProofNode proofNode = (BigStepProofNode)children[i];
				if (proofNode.getParent() == null) {
					System.out.println("rootNode Changed");
				}
				
				BigStepNodeComponent nodeComponent = (BigStepNodeComponent)proofNode.getUserObject();
				if (nodeComponent != null) {
					nodeComponent.changeNode ();
				}
			}
		}
	}

	@Override
	protected void nodesRemoved(TreeModelEvent event) {
		Object[] children = event.getChildren();
		if (children == null) {
			return;
		}
		for (int i=0; i<children.length; i++) {
			if (children[i] instanceof ProofNode) {
				BigStepProofNode proofNode = (BigStepProofNode)children[i];
				
				BigStepNodeComponent nodeComponent = (BigStepNodeComponent)proofNode.getUserObject();
				if (nodeComponent != null) {
					remove (nodeComponent);
					proofNode.setUserObject(null);
				}
			}
		}
	}

	@Override
	protected void treeContentChanged() {

		BigStepProofNode rootNode = (BigStepProofNode)this.proofModel.getRoot();
		
		// initiate the index
		this.index = 1;
		checkForUserObject(rootNode);
		relayout ();
		
	}

	@Override
	protected void relayout() {
		if (this.currentlyLayouting) {
			return;
		}
		
		this.currentlyLayouting = true;
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run () {
				BigStepProofNode rootNode = (BigStepProofNode)BigStepComponent.this.proofModel.getRoot();
				
				Point rightBottomPos = treeNodeLayout.placeNodes (rootNode, 20, 20, BigStepComponent.this.availableWidth);
				
				// lets add some border to the space
				
				rightBottomPos.x += 20;
				rightBottomPos.y += 20;
				
				Dimension size = new Dimension (rightBottomPos.x, rightBottomPos.y);
				
				// set all the sizes needed by the component
				setMaximumSize (size);
				setMinimumSize (size);
				setPreferredSize (size);
				setSize (size);
				
				BigStepComponent.this.currentlyLayouting = false;
			}
		});
	}
	
	/**
	 * 
	 * @param gc
	 */
	@Override
	protected void paintComponent (Graphics gc) {
		gc.setColor(Color.WHITE);
		gc.fillRect(0, 0, getWidth (), getHeight ());
		

		gc.setColor(Color.BLACK);
		ProofNode rootNode = (ProofNode)this.proofModel.getRoot();
		TreeArrowRenderer.renderArrows (rootNode, treeNodeLayout.getSpacing (), gc);
		
		
	}
	
	/*
	 * Implementation of the Scrollable interface
	 */
	public Dimension getPreferredScrollableViewportSize () {
		return getPreferredSize ();
	}
	
	public int getScrollableBlockIncrement (Rectangle visibleRect, int orientation, int direction) {
		// XXX: Dynamic block increment
		return 25;
	}
	
	public boolean getScrollableTracksViewportHeight () {
		return false;
	}
	
	public boolean getScrollableTracksViewportWidth () {
		return false;
	}
	
	public int getScrollableUnitIncrement (Rectangle visibleRect, int orientation, int direction) {
		//  XXX: Dynamic unit increment
		return 10;
	}	
}
