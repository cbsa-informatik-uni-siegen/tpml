package de.unisiegen.tpml.graphics.typechecker;

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
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.graphics.AbstractProofComponent;
import de.unisiegen.tpml.graphics.renderer.TreeArrowRenderer;
import de.unisiegen.tpml.graphics.tree.TreeNodeLayout;

public class TypeCheckerComponent extends AbstractProofComponent implements Scrollable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5184580585827680414L;

	private int													index;

	private TreeNodeLayout							treeNodeLayout;
	
	private ProofNode										newNodeTop;
	
	private ProofNode										newNodeBottom;
	
	public TypeCheckerComponent (TypeCheckerProofModel model) {
		super (model);
		
		this.treeNodeLayout			= new TreeNodeLayout (10);
		this.newNodeTop					= null;
		this.newNodeBottom			= null;
		
		setLayout (null);
		
		// initialy the tree content has changed
		treeContentChanged();
	}
	
	public void setSpacing (int spacing) {
		this.treeNodeLayout.setSpacing(spacing);
	}
	

	/**
	 * Guesses the next unprooven node in the tree.
	 * 
	 * @throws IllegalStateException
	 * @throws ProofGuessException
	 */
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

	/**
	 * Recalculates the layout 
	 *
	 */
	protected void relayout () {
		if (this.currentlyLayouting) {
			return;
		}
		
		this.currentlyLayouting = true;
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run () {
				TypeCheckerProofNode rootNode = (TypeCheckerProofNode)TypeCheckerComponent.this.proofModel.getRoot();
				
				Point rightBottomPos = treeNodeLayout.placeNodes (rootNode, 20, 20, TypeCheckerComponent.this.availableWidth);
				
				// lets add some border to the space
				
				rightBottomPos.x += 20;
				rightBottomPos.y += 20;
				
				Dimension size = new Dimension (rightBottomPos.x, rightBottomPos.y);
				
				// set all the sizes needed by the component
				setMaximumSize (size);
				setMinimumSize (size);
				setPreferredSize (size);
				setSize (size);
				
				TypeCheckerComponent.this.currentlyLayouting = false;
				TypeCheckerComponent.this.jumpToNodeVisible ();
			}
		});
	}
	
	private void checkForUserObject (TypeCheckerProofNode node) {
		
		if (node == null) {
			return;
		}
		TypeCheckerNodeComponent nodeComponent = (TypeCheckerNodeComponent)node.getUserObject();
		if (nodeComponent == null) {
			// if the node has no userobject it may be new in the
			// tree, so a new TypeCheckerNodeComponent will be created
			// and added to the TypeCheckerProofNode  
			nodeComponent = new TypeCheckerNodeComponent (node, (TypeCheckerProofModel)this.proofModel, this.translator);
			node.setUserObject(nodeComponent);
			
			// the newly created nodeComponent is a gui-element so
			// it needs to get added to the gui
			add (nodeComponent);
			
			// when the node changes the  gui needs to get updated
			nodeComponent.addTypeCheckerNodeListener(new TypeCheckerNodeListener () {
				public void nodeChanged (TypeCheckerNodeComponent node) {
					TypeCheckerComponent.this.relayout();
				}
				public void requestTypeEnter (TypeCheckerNodeComponent node) {
					
				}
			});
		}
		
		// set the index value for the node
		nodeComponent.setIndex(this.index);
		++this.index;
		
		
		// the typechecker is a real tree, so its needed to proceed with all
		// children of the node
		for (int i=0; i<node.getChildCount(); i++) {
			TypeCheckerProofNode pNode = node.getChildAt(i);
			
			checkForUserObject (pNode);
		}
		
	}

	/**
	 * Gets called when the content of the tree has changed.
	 * 
	 * If nodes are newly inserted or nodes got removed.
	 */
	protected void treeContentChanged () {

		TypeCheckerProofNode rootNode = (TypeCheckerProofNode)this.proofModel.getRoot();
		
		// initiate the index
		this.index = 1;
		checkForUserObject(rootNode);
		relayout ();
		
	}
	
	protected void nodesInserted (TreeModelEvent event) {
		Object [] children = event.getChildren();

		// find the bottom and the top element that have been
		// inserted. when getting next to the relayout function
		// it gets tried to scroll this area visible
		if (children != null) {
			
			// only problem with this could occure when
			// then children[0] element isn't the topmost element
			// in the tree that has been inserted and childre[x-1] isn't
			// the last. at this condition that behaviour is undefined
			this.newNodeTop = (ProofNode)children [0];
			this.newNodeBottom = (ProofNode)children[children.length-1];
		}
		else {
			this.newNodeTop = null;
			this.newNodeBottom = null;
		}
	}
	
	
	protected void nodesChanged (TreeModelEvent event) {
		Object[] children = event.getChildren();
		if (children == null) {
			
			// if the children are null and the path only contains one element
			// this element is the root node.
			if (event.getPath().length == 1) {
				TypeCheckerProofNode proofNode = (TypeCheckerProofNode)event.getPath()[0];
				TypeCheckerNodeComponent nodeComponent = (TypeCheckerNodeComponent)proofNode.getUserObject();
				if (nodeComponent != null) {
					nodeComponent.changeNode ();
				}
				
			}
			return;
		}
		for (int i=0; i<children.length; i++) {
			if (children[i] instanceof ProofNode) {
				TypeCheckerProofNode proofNode = (TypeCheckerProofNode)children[i];
				
				TypeCheckerNodeComponent nodeComponent = (TypeCheckerNodeComponent)proofNode.getUserObject();
				if (nodeComponent != null) {
					nodeComponent.changeNode ();
				}
			}
		}
		
	}

	protected void nodesRemoved (TreeModelEvent event) {
		Object[] children = event.getChildren();
		for (int i=0; i<children.length; i++) {
			if (children[i] instanceof ProofNode) {
				TypeCheckerProofNode proofNode = (TypeCheckerProofNode)children[i];
				
				TypeCheckerNodeComponent nodeComponent = (TypeCheckerNodeComponent)proofNode.getUserObject();
				if (nodeComponent != null) {
					remove (nodeComponent);
					proofNode.setUserObject(null);
				}
			}
		}
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
	
	private void jumpToNodeVisible () {
		if (this.newNodeTop == null || this.newNodeBottom == null) {
			return;
		}
		
		// get the Component nodes to evaluate the positions
		// on the viewport
		TypeCheckerNodeComponent topNode = (TypeCheckerNodeComponent)this.newNodeTop.getUserObject();
		if (topNode == null) {
			return;
		}
		TypeCheckerNodeComponent bottomNode = (TypeCheckerNodeComponent)this.newNodeBottom.getUserObject();
		if (bottomNode == null) {
			return;
		}
		
		// get the visible rect to ensure the x coordinate is in the 
		// visible area. only vertical scolling is requested
		Rectangle visibleRect = this.getVisibleRect();
		
		Rectangle rect = new Rectangle ();
		rect.x = visibleRect.x;
		rect.width = 1;
		
		// the visible height is from the top of the topElement
		// top element to the bottom of the bottomElement
		rect.y = topNode.getBounds().y;
		rect.height = (bottomNode.getBounds().y + bottomNode.getBounds().height) - rect.y;
		
		this.scrollRectToVisible(rect);

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
