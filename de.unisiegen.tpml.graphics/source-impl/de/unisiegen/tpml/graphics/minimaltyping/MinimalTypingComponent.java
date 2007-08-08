package de.unisiegen.tpml.graphics.minimaltyping;

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
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofModel;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode;
import de.unisiegen.tpml.graphics.AbstractProofComponent;
import de.unisiegen.tpml.graphics.renderer.EnvironmentRenderer;
import de.unisiegen.tpml.graphics.renderer.PrettyStringRenderer;
import de.unisiegen.tpml.graphics.renderer.TreeArrowRenderer;
import de.unisiegen.tpml.graphics.tree.TreeNodeLayout;

/**
 * Implementation of the graphics repsentation of the 
 * MinimalTyping<br>
 * <br>
 *  
 * The following image shows a usual look of a part of an TypeChckerComponent.
 * It contains a few nodes of the origin expression:
 * <code>let rec f = lambda x. if x = 0 then 1 else x * f (x-1) in f 3</code><br>
 * <img src="../../../../../../images/MinimalTyping.png" /><br>
 * <br>
 * The entire placing of the nodes is done within the method 
 * {@link #relayout()} but actualy the layouting is passed over
 * to the {@link de.unisiegen.tpml.graphics.tree.TreeNodeLayout} 
 * to place the nodes. <br>
 * <br>
 * The lines and arrows of the tree are rendered using the 
 * {@link de.unisiegen.tpml.graphics.renderer.TreeArrowRenderer},
 * so all nodes within the tree implement the 
 * {@link de.unisiegen.tpml.graphics.tree.TreeNodeComponent} interface.<br>
 * <br>
 * The nodes are not stored directly in the <i>MinimalTypingComponent</i>, they are
 * stored using the <i>Userobject</i> provided by the {@link de.unisiegen.tpml.core.ProofNode}.<br>
 * Everytime the content of the tree changes ({@link #treeContentChanged()} is called) the 
 * {@link #checkForUserObject(MinimalTypingProofNode)}-method is called. This causes a recursive traversing
 * of the entire tree to check if every node has its corresponding 
 * {@link de.unisiegen.tpml.graphics.minimaltyping.MinimalTypingNodeComponent}.<br>
 * <br>
 * When nodes get removed only the userobject of that nodes needs to get release.<br>
 * When nodes get inserted, the first of them is stored in the {@link #jumpNode} so the
 * next time the component gets layouted the {@link #jumpToNodeVisible()}-method is called
 * and the scrollview of the {@link de.unisiegen.tpml.graphics.bigstep.BigStepView} 
 * scrolls to a place the stored node gets visible.
 * 
 * 
 * 
 * @author Benjamin Mies
 * 
 * @see de.unisiegen.tpml.graphics.minimaltyping.MinimalTypingView
 * @see de.unisiegen.tpml.graphics.minimaltyping.MinimalTypingNodeComponent
 *
 */
public class MinimalTypingComponent extends AbstractProofComponent implements Scrollable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5184580585827680414L;

	/**
	 * Index that will be incremented during the layouting. 
	 */
	private int													index;

	/**
	 * TreeNodeLayout will be used to do the layouting of the tree.
	 */
	private TreeNodeLayout							treeNodeLayout;
	
	/**
	 * The ProofNode where the scrollView will scroll to when
	 * an new node will be inserted.
	 */
	private ProofNode										jumpNode;

	/**
	 * 
	 * Allocates a new <code>MinimalTypingComponent</code> o
	 *
	 * @param model the proof model for this component
	 */
	public MinimalTypingComponent (MinimalTypingProofModel model) {
		super (model);
		
		this.treeNodeLayout			= new TreeNodeLayout (10);
		this.jumpNode						= null;
		
		setLayout (null);
		
		// initialy the tree content has changed
		treeContentChanged();
	}
	
	/**
	 * 
	 * Set a new spacing for this component.
	 *
	 * @param spacing the new spacing value
	 */
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
    throw new IllegalStateException("Unable to find next node"); //$NON-NLS-1$
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
			@SuppressWarnings("synthetic-access")
			public void run () {
				MinimalTypingProofNode rootNode = (MinimalTypingProofNode)MinimalTypingComponent.this.proofModel.getRoot();
				
				Point rightBottomPos = MinimalTypingComponent.this.treeNodeLayout.placeNodes (rootNode, 20, 20, MinimalTypingComponent.this.availableWidth, MinimalTypingComponent.this.availableHeight);
				
				// lets add some border to the space
				
				rightBottomPos.x += 20;
				rightBottomPos.y += 20;
				
				Dimension size = new Dimension (rightBottomPos.x, rightBottomPos.y);
				
				// set all the sizes needed by the component
				setMaximumSize (size);
				setMinimumSize (size);
				setPreferredSize (size);
				setSize (size);
				
				MinimalTypingComponent.this.currentlyLayouting = false;
				MinimalTypingComponent.this.jumpToNodeVisible ();
			}
		});
	}
	
	/**
	 * Causes every {@link PrettyStringRenderer} and 
	 * {@link EnvironmentRenderer} to recalculate thier
	 * layout.
	 */
	@Override
	protected void resetLayout () {
		// apply the reset on the root node
		resetUserObject((MinimalTypingProofNode)this.proofModel.getRoot());
	}
	
	
	/**
	 * Checks the entire tree if every {@link MinimalTypingProofNode} contains an Userobject.
	 * 
	 * @param node
	 */
	private void checkForUserObject (MinimalTypingProofNode node) {
		
		if (node == null) {
			return;
		}
		MinimalTypingNodeComponent nodeComponent = (MinimalTypingNodeComponent)node.getUserObject();
		if (nodeComponent == null) {
			// if the node has no userobject it may be new in the
			// tree, so a new MinimalTypingNodeComponent will be created
			// and added to the MinimalTypingProofNode  
			nodeComponent = new MinimalTypingNodeComponent (node, (MinimalTypingProofModel)this.proofModel, this.translator);
			node.setUserObject(nodeComponent);
			
			// the newly created nodeComponent is a gui-element so
			// it needs to get added to the gui
			add (nodeComponent);
			
			// when the node changes the  gui needs to get updated
			nodeComponent.addMinimalTypingNodeListener(new MinimalTypingNodeListener () {
				public void nodeChanged (@SuppressWarnings("unused")
				MinimalTypingNodeComponent pNode) {
					MinimalTypingComponent.this.relayout();
				}
				public void requestTypeEnter (@SuppressWarnings("unused")
						MinimalTypingNodeComponent pNode) {
					// Nothing to do
				}
				@SuppressWarnings("synthetic-access")
				public void requestJumpToNode (ProofNode pNode) {
					MinimalTypingComponent.this.jumpNode = pNode;
				}
			});
		}
		
		// set the index value for the node
		nodeComponent.setIndex(this.index);
		++this.index;
		
		
		// the MinimalTyping is a real tree, so its needed to proceed with all
		// children of the node
		for (int i=0; i<node.getChildCount(); i++) {
			MinimalTypingProofNode pNode = node.getChildAt(i);
			
			checkForUserObject (pNode);
		}
		
	}

	/**
	 * Causes all userobject from all nodes to reset the layout.<br>
	 * <br>
	 * Resetting means that every {@link PrettyStringRenderer} and 
	 * {@link EnvironmentRenderer} recalculates their needed font sizes.
	 * @param node the node to reset
	 */
	private void resetUserObject (MinimalTypingProofNode node) {
		if (node == null) {
			return;
		}
		
		MinimalTypingNodeComponent nodeComponent = (MinimalTypingNodeComponent)node.getUserObject();
		if (nodeComponent == null) {
			return;
		}
		
		nodeComponent.reset ();
		
		for (int i=0; i<node.getChildCount (); i++) {
			MinimalTypingProofNode pNode = node.getChildAt(i);
			
			resetUserObject (pNode);
		}
	}

	/**
	 * Gets called when the content of the tree has changed.
	 * If nodes are newly inserted or nodes got removed.<br>
	 * <br>
	 * The tree will be checked for userobject and than get
	 * relayouted.
	 * 
	 */
	@Override
	protected void treeContentChanged () {

		MinimalTypingProofNode rootNode = (MinimalTypingProofNode)this.proofModel.getRoot();
		
		// initiate the index
		this.index = 1;
		checkForUserObject(rootNode);
		relayout ();
		
	}
	
	/**
	 * Saves the first of the newly inserted nodes for the node
	 * to jump to later.
	 */
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
	
	
	/**
	 * Delegates a {@link MinimalTypingNodeComponent#changeNode()} to every nodes
	 * that have changed.
	 */
	@Override
	protected void nodesChanged (TreeModelEvent event) {
		Object[] children = event.getChildren();
		if (children == null) {
			
			// if the children are null and the path only contains one element
			// this element is the root node.
			if (event.getPath().length == 1) {
				MinimalTypingProofNode proofNode = (MinimalTypingProofNode)event.getPath()[0];
				MinimalTypingNodeComponent nodeComponent = (MinimalTypingNodeComponent)proofNode.getUserObject();
				if (nodeComponent != null) {
					nodeComponent.changeNode ();
				}
				
			}
			return;
		}
		for (int i=0; i<children.length; i++) {
			if (children[i] instanceof ProofNode) {
				MinimalTypingProofNode proofNode = (MinimalTypingProofNode)children[i];
				
				MinimalTypingNodeComponent nodeComponent = (MinimalTypingNodeComponent)proofNode.getUserObject();
				if (nodeComponent != null) {
					nodeComponent.changeNode ();
				}
			}
		}
		
	}

	/**
	 * Removes the userobject from the {@link MinimalTypingProofNode}.
	 */
	@Override
	protected void nodesRemoved (TreeModelEvent event) {
		Object[] children = event.getChildren();
		for (int i=0; i<children.length; i++) {
			if (children[i] instanceof ProofNode) {
				MinimalTypingProofNode proofNode = (MinimalTypingProofNode)children[i];
				
				MinimalTypingNodeComponent nodeComponent = (MinimalTypingNodeComponent)proofNode.getUserObject();
				if (nodeComponent != null) {
					remove (nodeComponent);
					proofNode.setUserObject(null);
				}
			}
		}
	}
	
	/**
	 * Just renders the tree using the {@link TreeArrowRenderer#renderArrows(ProofNode, int, Graphics)}-Method
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
	
	/**
	 * Scroll the Viewport to the rect of the previously saved node. 
	 *
	 */
	private void jumpToNodeVisible () {
		if (this.jumpNode == null) {
			return;
		}
		
		// get the Component nodes to evaluate the positions
		// on the viewport
		MinimalTypingNodeComponent node = (MinimalTypingNodeComponent)this.jumpNode.getUserObject();
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
