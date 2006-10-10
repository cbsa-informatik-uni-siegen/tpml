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

/**
 * Implementation of the graphical representation of the BigStep-Interpreter.
 * 
 * @author marcell
 *
 */
public class BigStepComponent extends AbstractProofComponent implements Scrollable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3793854335585017325L;

	/**
	 * Handles the layouting of the BigStepNodeComponents to a tree. 
	 */
	private TreeNodeLayout							treeNodeLayout;
	
	/**
	 * When checking all nodes within the tree, this <i>index</i> is used to
	 * count them top to bottom.
	 */
	private int													index;
	
	/**
	 * Contains the <i>ProofNode</i> where to scroll. <br>
	 * When new nodes get inserted, the first of those nodes is
	 * assigned to the jumpNode. When the layout is changing (that
	 * is always happening when nodes get inserted) the <i>BigStepComponent</i>
	 * scrolls itself, so that the <i>jumpNode</i> is visible. 
	 */
	private ProofNode										jumpNode;
	
	
	/**
	 * The border around the <i>BigStepComponent</i> in pixels.<br>
	 * This are 20 pixels per default.
	 */
	private int													border;
	
	
	/**
	 * Constructor.<br>
	 * <br>
	 * The first <i>treeContentChanged</i> is called manualy at the end of
	 * the constructor to get started. 
	 * 
	 * @param model The model the <i>BigStepComponent</i> should visualise.
	 */
	public BigStepComponent (BigStepProofModel model) {
		super (model);
		this.treeNodeLayout 		= new TreeNodeLayout ();
		this.border							= 20;
		this.jumpNode						= null;
		
		setLayout(null);
		
		// initialy the tree content has changed
		treeContentChanged();
	}
	

	/**
	 * Sets the spacing of the layout.
	 * 
	 * @param spacing The spacing.
	 */
	public void setSpacing (int spacing) {
		this.treeNodeLayout.setSpacing(spacing);
	}
	
	
	/**
	 * Guesses the first unprooven node within the tree. 
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
	 * Checks all nodes below the given node for an UserObject.<br>
	 * All <i>BigStepProofNode</i>s are provided with an UserObject.
	 * The UserObject is the <i>BigStepNodeComponent</i>, that is the actual
	 * node the BigStepGUI works with.<br>
	 * <br>
	 * This function goes recursive over the tree and checks every <i>BigStepProofNode</i>
	 * whether an userObject is already present. If none is there, a new one created.<br>
	 * <br>
	 * Here the nodes get there index value.
	 * 
	 * @param node The node which tree should be checked. One should only give the rootNode here.
	 */
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
	/**
	 * Assigns the first, newly inserted node to the jumpNode. When
	 * the next relayout is called, the <i>BigStepComponent</i> is able to
	 * scroll to a propper position.<br>
	 * <br>
	 * Reimplementation of the {@link AbstractProofComponent#nodesInserted(TreeModelEvent)}
	 * Method.<br>
	 *  
	 * @param event 
	 */
	@Override
	protected void nodesInserted(TreeModelEvent event) {
		if (this.jumpNode != null) {
			return;
		}
			
		Object [] children = event.getChildren();

		// find the bottom and the top element that have been
		// inserted. when getting next to the relayout function
		// it gets tried to scroll this area visible
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
	 * Delegates the nodesChanged Events.<br>
	 * <br>
	 * Reimplementation of the {@link AbstractProofComponent#nodesChanged(TreeModelEvent)}
	 * Method.<br> 
	 * <br>
	 * The userobject of all <i>ProofNodes</i> that have changed
	 * is informed about the change.
	 * 
	 * @param event The TreeModelEvent
	 */
	@Override
	protected void nodesChanged(TreeModelEvent event) {
		Object[] children = event.getChildren();
		if (children == null) {
			// if the children are null and the path only contains one element
			// this element is the root node.
			if (event.getPath().length == 1) {
				BigStepProofNode proofNode = (BigStepProofNode)event.getPath()[0];
				BigStepNodeComponent nodeComponent = (BigStepNodeComponent)proofNode.getUserObject();
				if (nodeComponent != null) {
					nodeComponent.changeNode ();
				}
			}
			return;
		}
		for (int i=0; i<children.length; i++) {
			if (children[i] instanceof ProofNode) {
				BigStepProofNode proofNode = (BigStepProofNode)children[i];
				
				BigStepNodeComponent nodeComponent = (BigStepNodeComponent)proofNode.getUserObject();
				if (nodeComponent != null) {
					nodeComponent.changeNode ();
				}
			}
		}
	}

	/**
	 * Remove the userobjects from the nodes that have been removed.<br>
	 * <br>
	 * Reimplementation of the {@link AbstractProofComponent#nodesRemoved(TreeModelEvent)}.
	 * Method.<br>
	 * <br>
	 * All nodes that have been removed within the origin <i>ProofTree</i>
	 * from the <i>ProoModel</i> still need to get rid of thire useObject
	 * that represents the graphical part of the node.<br>
	 * <br>
	 * All those <i>SmallStepNodeComponent</i> objects, that are 
	 * userobjects from each node, will be removed from this JComponent and
	 * will be detached from the <i>ProofNode</i>.
	 * 
	 * @param event The TreeModelEvent
	 */
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

	/**
	 * Checks if all nodes in the tree have an useobject and riggers a relayout.<br>
	 * <br>
	 * Reimplementation of the {@link AbstractProofComponent#treeContentChanged()}
	 * Method.<br> 
	 */
	@Override
	protected void treeContentChanged() {

		BigStepProofNode rootNode = (BigStepProofNode)this.proofModel.getRoot();
		
		// initiate the index
		this.index = 1;
		checkForUserObject(rootNode);
		relayout ();
		
	}

	/**
	 * Does the layouting of the tree.<br>
	 * <br>
	 * Reimplementation of the {@link AbstractProofComponent#relayout()} Method.<br>
	 * <br>
	 * The actual placement of the nodes is done by the {@link TreeNodeLayout.
	 * The <i>TreeNodeLayout</i> returns the bottom-right-point of the entire
	 * layout. Size position widened by the border used to determine the size 
	 * of the component.<br>
	 * Right after setting the size of the component the jump to a possible new
	 * node takes place. (See {@link #jumpToNodeVisible()})<br>
	 * 
	 */
	@Override
	protected void relayout() {
		if (this.currentlyLayouting) {
			return;
		}
		
		this.currentlyLayouting = true;
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run () {
				BigStepProofNode rootNode = (BigStepProofNode)BigStepComponent.this.proofModel.getRoot();
				
				Point rightBottomPos = BigStepComponent.this.treeNodeLayout.placeNodes (rootNode, BigStepComponent.this.border, BigStepComponent.this.border, BigStepComponent.this.availableWidth);
				
				// lets add some border to the space
				
				rightBottomPos.x += BigStepComponent.this.border;
				rightBottomPos.y += BigStepComponent.this.border;
				
				Dimension size = new Dimension (rightBottomPos.x, rightBottomPos.y);
				
				// set all the sizes needed by the component
				setMaximumSize (size);
				setMinimumSize (size);
				setPreferredSize (size);
				setSize (size);
				
				BigStepComponent.this.currentlyLayouting = false;
				BigStepComponent.this.jumpToNodeVisible();
			}
		});
	}
	
	/**
	 * Renders the decoration of the BigStepComponent.<br>
	 * <br>
	 * Reimplementation of {@link javax.swing.JComponent#paint(java.awt.Graphics)}
	 * Method.<br>
	 * <br>
	 * Simple fills the entire background with white color and
	 * lets the {@link TreeArrowRenderer#renderArrows(ProofNode, int, Graphics)} 
	 * render the lines and the arrows of the tree structur.
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
	 * Scroll the vieport so that the first of least added nodes becomes visible.<br>
	 */
	private void jumpToNodeVisible () {
		if (this.jumpNode == null) {
			return;
		}
		
		// get the Component nodes to evaluate the positions
		// on the viewport
		BigStepNodeComponent node = (BigStepNodeComponent)this.jumpNode.getUserObject();
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
