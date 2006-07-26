package ui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import common.ProofModel;
import common.ProofNode;

public abstract class AbstractView extends JComponent implements TreeModelListener {
	

	protected AbstractNode		rootNode;


	protected ProofModel			model = null;
	
	
	public AbstractView() {
		super ();
		setFocusable (true);
		
		new SettingsGUI ();
	}

	public void setModel (ProofModel model) {
		
		this.model = model;
		this.model.addTreeModelListener(this);
		
		reloadTree();
		
		relayout();
		
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized (ComponentEvent event) {
				doLayouting();
			}
		});
	}
	
	public void treeNodesChanged(TreeModelEvent e) {
		relayout();
	}

	public void treeNodesInserted(TreeModelEvent e) {
		
		ProofNode pnode = (ProofNode)e.getTreePath().getLastPathComponent();
		if (pnode == null) return;
		
		AbstractNode node = getNode (pnode);
		if (node == null) return;
				
		Object[] children = e.getChildren();
		int[] indices = e.getChildIndices();

		// save the last nod that should be inserted
		// it will be used to determin 
		AbstractNode lastNode = null;
		if (children.length != indices.length) return;
		for (int i=0; i<children.length; i++) {
			AbstractNode aNode = createNode ((ProofNode)children[i]);
			aNode.setModel(this.model);
			node.addChildNode(aNode, indices [i]);
			lastNode = aNode;
		}
		relayout();
		nodeAdded (lastNode);
	}

	public void treeNodesRemoved(TreeModelEvent e) {
		
		ProofNode pnode = (ProofNode)e.getTreePath().getLastPathComponent();
		if (pnode == null) return;
		
		AbstractNode node = getNode (pnode);
		if (node == null) return;
		
		int[] indices = e.getChildIndices();
		for (int i=indices.length-1; i >= 0; i--) {
					
			node.removeChildNode(indices[i]);
					
		}
		relayout();
	}
	
	public void treeStructureChanged(TreeModelEvent e) {
		ProofNode pnode = (ProofNode)e.getTreePath().getLastPathComponent();
		if (pnode == null) return;
		
		AbstractNode node = getNode (pnode);
		if (node == null) return;
		
		reloadNode (node);
	}
	
	private AbstractNode getNode (ProofNode node) {
		
		if (rootNode == null) {
			reloadTree ();
		}
		
		return getNode (rootNode, node);
	}
	
	
	private AbstractNode getNode (AbstractNode aNode, ProofNode node) {
		if (aNode == null) return null;
		
		if (aNode.getProofNode() == node) {
			return aNode;
		}
		
		for (AbstractNode child : aNode.getChildren()) {
			AbstractNode cNode = getNode (child, node);
			if (cNode != null) return cNode;
		}
		return null;
	}
	
	
	private void reloadNode (AbstractNode parent, ProofNode node) {
		
		for (int i=0; i<node.getChildCount(); i++) {
			ProofNode pnode = node.getChildAt(i);
			AbstractNode aNode = createNode (pnode);
			aNode.setModel(model);
			parent.addChildNode(aNode, i);
			
			reloadNode (aNode, pnode);
			
		}
	}

	protected void reloadNode (AbstractNode node) {
		if (node == null) {
			node = rootNode = createNode(model.getRoot());
			if (node != null) 
				node.setModel(model);
		}

//		
//		The nodes have been reloaded previously
//		I realy don't know why that was needed
//		
//		node.removeChildeNodes();
//		
//	
//		ProofNode pnode = node.getProofNode();
//		reloadNode (node, pnode);
		
		relayout();
	}
	
	protected void reloadTree () {
		reloadNode (null);
	}

	public ProofNode getRootNode() {
		return this.rootNode.getProofNode();
	}
	
	protected abstract AbstractNode createNode(ProofNode node);
	
	protected abstract void relayout();
	
	protected abstract void doLayouting();
	
	protected abstract void nodeAdded (AbstractNode node);
}
