package de.unisiegen.tpml.graphics.typechecker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.graphics.renderer.TreeArrowRenderer;
import de.unisiegen.tpml.graphics.tree.TreeNodeLayout;

public class TypeCheckerComponent extends JComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5184580585827680414L;

	private TypeCheckerProofModel 			model;
	
	private int													availableWidth;

	private int													index;
	

	private TreeNodeLayout							treeNodeLayout;
	
	public TypeCheckerComponent (TypeCheckerProofModel model) {
		super ();
		
		this.model = model;
		
		this.treeNodeLayout = new TreeNodeLayout (10);
		
		setLayout (null);
		
		this.model.addTreeModelListener(new TreeModelListener() {
			public void treeNodesChanged (TreeModelEvent e) {
				TypeCheckerComponent.this.relayout ();
			}
			public void treeNodesInserted (TreeModelEvent e) {
				TypeCheckerComponent.this.treeContentChanged ();
			}
			public void treeNodesRemoved (TreeModelEvent e) {
				TypeCheckerComponent.this.treeContentChanged ();
			}
			public void treeStructureChanged (TreeModelEvent e) {
				TypeCheckerComponent.this.treeContentChanged ();
			}
		});
		
		
		// initialy the tree content has changed
		treeContentChanged();
	}
	
	public void setSpacing (int spacing) {
		this.treeNodeLayout.setSpacing(spacing);
	}
	
	public void setAvailableWidth (int availableWidth) {
		this.availableWidth = availableWidth;
		relayout();
	}


	/**
	 * Recalculates the layout 
	 *
	 */
	private void relayout () {
		TypeCheckerProofNode rootNode = (TypeCheckerProofNode)this.model.getRoot();
		
		Point rightBottomPos = treeNodeLayout.placeNodes (rootNode, 20, 20, this.availableWidth);
		
		// lets add some border to the space
		
		rightBottomPos.x += 20;
		rightBottomPos.y += 20;
		
		Dimension size = new Dimension (rightBottomPos.x, rightBottomPos.y);
		
		// set all the sizes needed by the component
		setMaximumSize (size);
		setMinimumSize (size);
		setPreferredSize (size);
		setSize (size);
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
			nodeComponent = new TypeCheckerNodeComponent (node);
			node.setUserObject(nodeComponent);
			
			// when this node gets asked for the size next time it will
			// know that is has to recalculate its size
			nodeComponent.markChanged(true);
			
			// the newly created nodeComponent is a gui-element so
			// it needs to get added to the gui
			add (nodeComponent);
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
	private void treeContentChanged () {

		TypeCheckerProofNode rootNode = (TypeCheckerProofNode)this.model.getRoot();
		
		// initiate the index
		this.index = 1;
		checkForUserObject(rootNode);
		relayout ();
		
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
		ProofNode rootNode = (ProofNode)this.model.getRoot();
		TreeArrowRenderer.renderArrows (rootNode, treeNodeLayout.getSpacing (), gc);
		
		
	}
	

}
