package de.unisiegen.tpml.graphics.typechecker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Enumeration;

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
	
	private ProofNode										jumpNode;
	
	
	public TypeCheckerComponent (TypeCheckerProofModel model) {
		super (model);
		
		this.treeNodeLayout			= new TreeNodeLayout (10);
		this.jumpNode						= null;
		
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
		Enumeration<ProofNode> enumeration = this.proofModel.getRoot().postorderEnumeration();
		while (enumeration.hasMoreElements()) {
			ProofNode node = enumeration.nextElement();
			if (!node.isProven()) {
				this.proofModel.guess(node);
				return;
			}
		}
    throw new IllegalStateException("Unable to find next node");
	}

	/**
	 * Recalculates the layout 
	 *
	 */
	@Override
	protected void relayout () {
		if (this.currentlyLayouting) {
			return;
		}
		
		this.currentlyLayouting = true;
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run () {
				TypeCheckerProofNode rootNode = (TypeCheckerProofNode)TypeCheckerComponent.this.proofModel.getRoot();
				
				Point rightBottomPos = TypeCheckerComponent.this.treeNodeLayout.placeNodes (rootNode, 20, 20, TypeCheckerComponent.this.availableWidth);
				
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
	@Override
	protected void treeContentChanged () {

		TypeCheckerProofNode rootNode = (TypeCheckerProofNode)this.proofModel.getRoot();
		
		// initiate the index
		this.index = 1;
		checkForUserObject(rootNode);
		relayout ();
		
	}
	
	@Override
	protected void nodesInserted (TreeModelEvent event) {
		if (this.jumpNode != null) {
			return;
		}
		
		Object [] children = event.getChildren();

		if (children != null) {
			
			// only problem with this could occure when
			// then children[0] element isn't the topmost element
			// in the tree that has been inserted. at this condition 
			// that behaviour is undefined
			this.jumpNode = (ProofNode)children [0];
		}
		else {
			this.jumpNode = null;
		}
	}
	
	
	@Override
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

	@Override
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
		ProofNode rootNode = this.proofModel.getRoot();
		TreeArrowRenderer.renderArrows (rootNode, this.treeNodeLayout.getSpacing (), gc);
		
		
	}
	
	private void jumpToNodeVisible () {
		if (this.jumpNode == null) {
			return;
		}
		
		// get the Component nodes to evaluate the positions
		// on the viewport
		TypeCheckerNodeComponent node = (TypeCheckerNodeComponent)this.jumpNode.getUserObject();
		if (node == null) {
			return;
		}
		
		// get the visible rect to ensure the x coordinate is in the 
		// visible area. only vertical scolling is requested
		Rectangle visibleRect = this.getVisibleRect();
		
		Rectangle rect = new Rectangle ();
		rect.x 			= visibleRect.x;
		rect.y 			= node.getY ();
		rect.width 	= 1;
		rect.height = node.getHeight ();
		
		this.scrollRectToVisible(rect);

		this.jumpNode = null;
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
