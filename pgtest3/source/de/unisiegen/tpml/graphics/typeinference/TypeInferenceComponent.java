package de.unisiegen.tpml.graphics.typeinference;

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
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode;
import de.unisiegen.tpml.graphics.AbstractProofComponent;
import de.unisiegen.tpml.graphics.renderer.EnvironmentRenderer;
import de.unisiegen.tpml.graphics.renderer.PrettyStringRenderer;

/**
 * The layouting of the TypeInference seems to bee as complicated as the 
 * layouting of the Sammlstepper because of the same reasons.<br>
 * TODO Kommentare hinzufügen
 * When layouting this component (that is done in the {@link #relayout()}-method)
 * first all nodes within the tree are check if they have an 
 * {@link de.unisiegen.tpml.graphics.smallstep.SmallStepNodeComponent} assigned
 * to them. Then the tree is scanned for the node with the widest 
 * {@link de.unisiegen.tpml.graphics.smallstep.SmallStepNodeComponent#rules}.
 * This value is assigned to each node, this way all nodes know the maximum width
 * of all rules and they can use this for their own width. By doing so, all nodes
 * will be horizontaly aligned.<br>
 * <br>
 * When this all is done the actual placing of the nodes is done. 
 * {@see #placeNode(TypeInferenceProofNode, int, int)} for this.
 *   
 *  @author michael
 * 
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.graphics.AbstractProofComponent
 * @see de.unisiegen.tpml.graphics.TypeInference.TypeInferenceView
 * @see de.unisiegen.tpml.graphics.TypeInference.TypeInferenceNodeComponent
 * @see de.unisiegen.tpml.graphics.TypeInference.TypeInferenceRulesComponent
 * @see de.unisiegen.tpml.graphics.TypeInference.TypeInferenceRuleLabel
 */
public class TypeInferenceComponent extends AbstractProofComponent implements Scrollable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1022005553523956037L;


	/**
	 * The origin {@link TypeInferenceProofModel}
	 */
	private TypeInferenceProofModel			model;
	
	/**
	 * The border in pixels around the nodes 
	 */
	private int											border;
	
	/**
	 * The spacing between the nodes
	 */
	private int											spacing;
	
	/**
	 * The visible width is set via {@link #setAvailableWidth(int)}
	 * by the ScrollPane of {@link TypeInferenceView}.
	 */
	private int											availableWidth;
	
	/**
	 * Contains the <i>ProofNode</i> that has been inserted
	 * last. 
	 */
	private ProofNode								jumpNode;
	
	private boolean									advanced;
	
	/**
	 * Sets the default values.<br>
	 * <br>
	 * A border of 20 pixels and spacing of 10 pixels.<br>
	 * <br>
	 * To get a propper start a {@link #relayout()} is called 
	 * manually.
	 * 
	 * @param proofModel
	 * @param advanced
	 */
	public TypeInferenceComponent (TypeInferenceProofModel proofModel, boolean advancedP) {
		super (proofModel);
		
		
		this.currentlyLayouting	= false;
		
		setLayout (null);
		
		this.model 		= proofModel;
		this.border		= 20;
		this.spacing	= 10;
		this.advanced = advancedP;
		
		
		// trigger the first layouting
		relayout ();
	}
	
	/**
	 * Returns <code>true</code> if the small step component is in advanced mode, <code>false</code>
	 * if its in beginner mode.
	 * 
	 * @return <code>true</code> if advanced mode is active.
	 * 
	 * @see #setAdvanced(boolean)
	 */
	boolean isAdvanced() 
	{
		return this.advanced;
	}
	
	/**
	 * If <code>advanced</code> is <code>true</code>, the small step component will display
	 * only axiom rules in the rule menu, otherwise, in beginner mode, meta rules will also
	 * be displayed.
	 * 
	 * @param advanced <code>true</code> to display only axiom rules.
	 * 
	 * @see #isAdvanced()
	 */
	void setAdvanced(boolean advanced) {
		// check if we have a new setting
		if (this.advanced != advanced) 
		{
			System.out.println("TypeinferencComponent bekommt nun den Wahrheitswert: "+advanced);
			// remember the new setting
			this.advanced = advanced;
			
			//inform the nodevomponent
			
		
			// make sure all nodes have valid user objects
			checkForUserObject((TypeInferenceProofNode)this.proofModel.getRoot());
			
			// update all active nodes
			Enumeration<ProofNode> enumeration = this.proofModel.getRoot().postorderEnumeration();
			while (enumeration.hasMoreElements()) {
				// tell the component belonging to this node, that we have a new advanced state
				TypeInferenceProofNode node = (TypeInferenceProofNode)enumeration.nextElement();
				TypeInferenceNodeComponent component = (TypeInferenceNodeComponent)node.getUserObject();
				component.setAdvanced(advanced);
			}
		}
	}
	
	/**
	 * Sets the width that is available by the {@link TypeInferenceView}.
	 */
	@Override
	public void setAvailableWidth (int availableWidth) {
		this.availableWidth = availableWidth;
		relayout ();
	}
	
	
	
	/**
	 * Returns the first child of the given node.<br>
	 * <br>
	 * If the node has no children, <i>null</i> is returned.
	 * 
	 * @param node
	 * @return
	 */
	private TypeInferenceProofNode getFirstChild (TypeInferenceProofNode node) {
		try {
			return node.getFirstChild();
		}
		catch (Exception e) { }
		return null;
	}
	
	/**
	 * Traversing the ProofTree recursivly and adds a TypeInferenceNodeComponent
	 * where none is.<br>
	 * <br>
	 * Usualy only at newly added nodes the TypeInferenceNodeComponent is missing.
	 * 
	 * @param node When calling this method: the rootNode of the tree.
	 */
	private void checkForUserObject (TypeInferenceProofNode node) {
		if (node == null) {
			return;
		}
		
		TypeInferenceNodeComponent nodeComponent = (TypeInferenceNodeComponent)node.getUserObject();
		if (nodeComponent == null) {
			
			// create the noded that has not been there yet
			//TODO 
			nodeComponent = new TypeInferenceNodeComponent (node, this.model, this.translator, this.spacing, advanced);
			
			// add the needed listener
			nodeComponent.addTypeInferenceNodeListener(new TypeInferenceNodeListener() {
				public void nodeChanged (TypeInferenceNodeComponent node) {
					TypeInferenceComponent.this.relayout();
				}
				public void repaintAll () {
					TypeInferenceComponent.this.repaint();
				}
				public void requestJumpToNode (ProofNode node) {
					TypeInferenceComponent.this.jumpNode = node;
				}
			});
			
			nodeComponent.update();
			
			// save it to the node
			node.setUserObject(nodeComponent);
			
			// and add the TypeInferenceNodeComponent to the gui
			add (nodeComponent);
		}
		
		checkForUserObject (getFirstChild (node));
	}
	
	/**
	 * Causes all userobject from all nodes to reset the layout.<br>
	 * <br>
	 * Resetting means that every {@link PrettyStringRenderer} and 
	 * {@link EnvironmentRenderer} recalculates their needed font sizes.
	 */
	private void resetUserObject (TypeInferenceProofNode node) {
		if (node == null) {
			return;
		}
		
		TypeInferenceNodeComponent nodeComponent = (TypeInferenceNodeComponent)node.getUserObject();
		if (nodeComponent == null) {
			return;
		}
		
		nodeComponent.reset ();
		
		resetUserObject (getFirstChild (node));
	}
	
	
	/**
	 * Traverses the ProofTree recursivly and checks the needed size for
	 * the rule combo on the left-hand-side.<br>
	 * <br>
	 * The <i>currentWidth</i> is the current maximum width that has been
	 * evaluated. When calling this function this should be somthing small.
	 * Just set it to <b>0</b>. 
	 * 
	 * @param node When calling this method: the rootNode of the tree.
	 * @param currentWidth Used internaly. Should be set to <b>0</b>.
	 * @return
	 */
	private int checkMaxRuleWidth (TypeInferenceProofNode node, int currentWidth) {
		if (node == null) {
			return currentWidth;
		}
		
		// get the size of the current node
		TypeInferenceNodeComponent nodeComponent = (TypeInferenceNodeComponent)node.getUserObject();
		int nodeWidth = nodeComponent.getMinRuleSize().width;
		
		// only the maximum width is of interest
		currentWidth = Math.max(currentWidth, nodeWidth);

		// return the recursive result of the next node
		
		return checkMaxRuleWidth (getFirstChild (node), currentWidth);
	}
	
	/**
	 * Traverses the ProofTree recursivly and informing every node for
	 * the maximum size of the rule combo on the left-hand-side.
	 *  
	 * @param node When calling this method: the rootNode of the tree.
	 * @param maxRuleWidth The maximum Width of the rules.
	 */
	private void updateMaxRuleWidth (TypeInferenceProofNode node, int maxRuleWidth) {
		if (node == null) {
			return;
		}
		
		// inform the node of the max rule width
		TypeInferenceNodeComponent nodeComponent = (TypeInferenceNodeComponent)node.getUserObject();
		nodeComponent.setMaxRuleWidth(maxRuleWidth);
		
		// proceed with the next child node
		updateMaxRuleWidth (getFirstChild (node), maxRuleWidth);
	}
	
	/**
	 * Traverses the ProofTree recursivly and checks the size of
	 * the expression for every node.
	 * 
	 * @param node When calling this method: the rootNode of the tree.
	 */
	private void checkExpressionSize (TypeInferenceProofNode node) {
		if (node == null) {
			return;
		}
		
		TypeInferenceNodeComponent nodeComponent = (TypeInferenceNodeComponent)node.getUserObject();
		nodeComponent.checkNeededExpressionSize(this.availableWidth - this.border);

		
		// proceed with the next child
		checkExpressionSize (getFirstChild (node));
	}
	
	
	/**
	 * Iterates through the entire tree an places every node.<br>
	 * <br>
	 * There are some things to take care of, when the nodes get placed:<br>
	 * <br>
	 * The expression and the rules of the parent node are in one row so
	 * when the nodes get placed the actualHeight of each node must be the 
	 * maximum of both. They are placed together in on step. So the rules
	 * of each node are placed together with the expression of its child node,
	 * if there is one.<br>
	 * If there is no parent node (that would be the first node, the root node),
	 * only the expression needs to get places.<br>
	 * If the node has no child (that would be the last node in the tree), 
	 * the rules must be placed directly because there is no child node that
	 * would place them.
	 * 
	 * @param node The rootNode
	 * @param x The horizontal start position
	 * @param y Ther vertical start position
	 * @return The size needed to show all the nodes.
	 */
	private Dimension placeNode (TypeInferenceProofNode node, int x, int y) {
		Dimension size = new Dimension (0, 0);

		while (node != null) {
			TypeInferenceNodeComponent nodeComponent = (TypeInferenceNodeComponent)node.getUserObject();
			
			// set the origin of this node
			nodeComponent.setOrigion(new Point (x, y));
			
			// if the node has no parent node it appears to be the rootNode
			// 
			// the expression of the rootNode can be placed without checking anything
			if (node.getParent() == null) {
				nodeComponent.placeExpression();
				
				
				// move the positioning
				y += nodeComponent.getRuleTop();
				
				// evaluate the new dimensions
				size.height		= y;
				
			}
			else {
				// evaluate the max size of this nodes expression and the parent
				// nodes rules
				TypeInferenceProofNode 			parentNode = node.getParent();
				TypeInferenceNodeComponent 	parentNodeComponent = (TypeInferenceNodeComponent)parentNode.getUserObject();
				
				
				Dimension expSize 	= nodeComponent.getExpressionSize();
				Dimension ruleSize 	= parentNodeComponent.getRuleSize();
	
				int maxHeight = Math.max(expSize.height, ruleSize.height);
				
				// inform both component about the actual height they should use to
				// place them
				parentNodeComponent.setActualRuleHeight(maxHeight);
				nodeComponent.setActualExpressionHeight(maxHeight);
				
				// let both components place theire elements
				parentNodeComponent.placeRules();
				nodeComponent.placeExpression();
				
				
				// this finishes the parentNode so it can be placed
				parentNodeComponent.setBounds();
				
				// the additional height come from the actual node
				y += nodeComponent.getRuleTop();
				
				// evaluate the new dimensions
				size.height = y;

				// the actual width of the entire component can now be checked
				// on the finshed node. the parent node
				size.width	= Math.max(size.width, x + parentNodeComponent.getSize().width);
			}
		
			// if the node has no children the rules need to get
			// placed here with the expression
			if (getFirstChild (node) == null) {
				
				if (this.model.isFinished()) {
					nodeComponent.hideRules ();
					nodeComponent.setBounds();
					
					size.width	= Math.max(size.width, x + nodeComponent.getSize().width);
				}
				else {
					// the rules can savely be positioned
					nodeComponent.placeRules();
					
					// and the node itself can be placed
					nodeComponent.setBounds();
					
					// evaluate the new dimension
					size.height 	+= nodeComponent.getActualRuleHeight();
					
					// the actual width of the entire component can now be checked
					// on the finshed node. 
					size.width	= Math.max(size.width, x + nodeComponent.getSize().width);
				}
				
				return size;
			}
			
			node = node.getFirstChild();
		}
		
		return size;
	}
	
	/**
	 * Does the entire layouting of the TypeInferenceComponent.<br>
	 * <br>
	 * All nodes in the tree will get a TypeInferenceNodeComponent, the size 
	 * of the widest rule combo on the left-hand-site is evaluated. <br>
	 * In order to render all expression alligned every node is informed of
	 * this width.
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
		
				// get the rootNode it will be used many time
				TypeInferenceProofNode rootNode = (TypeInferenceProofNode)TypeInferenceComponent.this.proofModel.getRoot();
				
				
				// check if all nodes have a propper TypeInferenceNodeComponent
				checkForUserObject (rootNode);
				
				// find the maximum width of the rules and inform the entire tree
				int maxRuleWidth = checkMaxRuleWidth (rootNode, 0);
				updateMaxRuleWidth (rootNode, maxRuleWidth);
				
				// evaluate the the sizes of the expression 
				checkExpressionSize(rootNode);
				
				// now that the basics for the nodes are found, 
				// they can be placed
				Dimension size = placeNode (rootNode, TypeInferenceComponent.this.border, TypeInferenceComponent.this.border);
				
				// the needed size evaluaded by placing the nodes gets
				// widened a bit to have a nice border around the component
				size.width 	+= TypeInferenceComponent.this.border;
				size.height += TypeInferenceComponent.this.border;
				
				// this size is used to determin all the sizes of the component
				setPreferredSize (size);
				setSize (size);
				setMinimumSize (size);
				setMaximumSize (size);
				
				TypeInferenceComponent.this.currentlyLayouting = false;
				TypeInferenceComponent.this.jumpToNodeVisible();
			}
		});
	}
	
	/**
	 * Resets all user objects by calling {@link #resetUserObject(TypeInferenceProofNode)}
	 * on the rootNode of the model. <br>
	 * This will cause the {@link PrettyStringRenderer} to update all fonts an to 
	 * recalculate the line wrappings etc...
	 */
	@Override
	protected void resetLayout () {
		resetUserObject((TypeInferenceProofNode)this.proofModel.getRoot());
	}
	
	/**
	 * Just saves the node that has been inserted.
	 */
	@Override
	protected void nodesInserted (TreeModelEvent event) {
		Object [] children = event.getChildren();
		
		if (children != null) {
			this.jumpNode = (ProofNode)children [0];
		}
		else {
			this.jumpNode = null;
		}
	}
	
	
	/**
	 * Causes an {@link TypeInferenceNodeComponent#update() on all
	 * nodes that have changed.<br>
	 * <br>
	 * When all updates are done relayouts the View.
	 * 
	 * @see #relayout()
	 */
	@Override
	protected void nodesChanged (TreeModelEvent event) {
		boolean relayout = false;
		Object[] children = event.getChildren();
		if (children == null) {
			
			// if the children are null and the path only contains one element
			// this element is the root node.
			if (event.getPath().length == 1) {
				TypeInferenceProofNode proofNode = (TypeInferenceProofNode)event.getPath()[0];
				TypeInferenceNodeComponent nodeComponent = (TypeInferenceNodeComponent)proofNode.getUserObject();
				if (nodeComponent != null) {
					nodeComponent.update ();
					relayout = true;
				}
				
			}
		}
		else {
			for (int i=0; i<children.length; i++) {
				if (children[i] instanceof ProofNode) {
					TypeInferenceProofNode proofNode = (TypeInferenceProofNode)children[i];
					
					TypeInferenceNodeComponent nodeComponent = (TypeInferenceNodeComponent)proofNode.getUserObject();
					if (nodeComponent != null) {
						nodeComponent.update ();
						relayout = true;
					}
				}
			}
		}
		if (relayout) {
			relayout ();
		}
	}
	
	/**
	 * Removes all userobjects from the nodes that will be removed
	 * by the {@link TypeInferenceProofModel} later. 
	 */
	@Override
	protected void nodesRemoved (TreeModelEvent event) { 
		Object[] children = event.getChildren();
		if (children == null) {
			return;
		}
		for (int i=0; i<children.length; i++) {
			if (children[i] instanceof ProofNode) {
				TypeInferenceProofNode proofNode = (TypeInferenceProofNode)children[i];
				
				TypeInferenceNodeComponent nodeComponent = (TypeInferenceNodeComponent)proofNode.getUserObject();
				if (nodeComponent != null) {
					remove (nodeComponent);
					proofNode.setUserObject(null);
				}
			}
		}
		
	}
	
	/**
	 * Just causes a relayout.
	 * 
	 * @see #relayout()
	 */
	@Override
	protected void treeContentChanged () {
		relayout ();
	}
	
	/**
	 * Finds the last unprooven node within the ProofTree und calls a guess on
	 * the proofModel with the node.
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
	 * Scroll the current visible area to the rectangle where
	 * the saved {@link #jumpNode} lays.
	 *
	 */
	private void jumpToNodeVisible () {
		if (this.jumpNode == null) {
			return;
		}
		
		// get the Component nodes to evaluate the positions
		// on the viewport
		TypeInferenceNodeComponent node = (TypeInferenceNodeComponent)this.jumpNode.getUserObject();
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
	//
	// Methods for painting purposes
	//
	@Override
	protected void paintComponent (Graphics gc) {
		//TODO wieder weiß machen...
		//gc.setColor (Color.BLUE);
		gc.setColor (Color.WHITE);
		gc.fillRect(0, 0, getWidth () - 1, getHeight () - 1);
	}
	
	
	// 
	// Implementation of the Scrollable interface
	//

	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize ();
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 10;
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 25;
	}

	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	
}
