package de.unisiegen.tpml.graphics.typechecker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.languages.LanguageTranslator;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.graphics.renderer.TreeArrowRenderer;
import de.unisiegen.tpml.graphics.tree.TreeNodeLayout;

public class TypeCheckerComponent extends JComponent implements Scrollable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5184580585827680414L;

	private TypeCheckerProofModel 			model;
	
	private LanguageTranslator					translator;
	
	private int													availableWidth;

	private int													index;

	private TreeNodeLayout							treeNodeLayout;
	
	private boolean											currentlyLayouting;
	
	public TypeCheckerComponent (TypeCheckerProofModel model) {
		super ();
		
		this.currentlyLayouting = false;
		this.model 							= model;
		this.translator 				= this.model.getLanguage().newTranslator();
		this.treeNodeLayout			= new TreeNodeLayout (10);
		
		
		setLayout (null);
		
		this.model.addTreeModelListener(new TreeModelListener() {
			public void treeNodesChanged (TreeModelEvent e) {
				TypeCheckerComponent.this.nodesChanged(e);
				TypeCheckerComponent.this.relayout ();
			}
			public void treeNodesInserted (TreeModelEvent e) {
				TypeCheckerComponent.this.treeContentChanged ();
			}
			public void treeNodesRemoved (TreeModelEvent e) {
				TypeCheckerComponent.this.nodesRemoved (e);
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
	 * Guesses the next unprooven node in the tree.
	 * 
	 * @throws IllegalStateException
	 * @throws ProofGuessException
	 */
	public void guess () throws IllegalStateException, ProofGuessException {
    LinkedList<ProofNode> nodes = new LinkedList<ProofNode>();
    nodes.add(model.getRoot());
    while (!nodes.isEmpty()) {
      ProofNode node = nodes.poll();
      if (node.getSteps().length == 0) {
      	
      	this.model.guess(node);
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
	private void relayout () {
		if (this.currentlyLayouting) {
			return;
		}
		
		this.currentlyLayouting = true;
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run () {
				TypeCheckerProofNode rootNode = (TypeCheckerProofNode)TypeCheckerComponent.this.model.getRoot();
				
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
			nodeComponent = new TypeCheckerNodeComponent (node, model, this.translator);
			node.setUserObject(nodeComponent);
			
			// when this node gets asked for the size next time it will
			// know that is has to recalculate its size
			nodeComponent.markChanged(true);
			
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
	private void treeContentChanged () {

		TypeCheckerProofNode rootNode = (TypeCheckerProofNode)this.model.getRoot();
		
		// initiate the index
		this.index = 1;
		checkForUserObject(rootNode);
		relayout ();
		
	}
	
	
	private void nodesChanged (TreeModelEvent event) {
		Object[] children = event.getChildren();
		if (children == null) {
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

	private void nodesRemoved (TreeModelEvent event) {
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
		ProofNode rootNode = (ProofNode)this.model.getRoot();
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
