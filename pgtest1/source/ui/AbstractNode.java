package ui;

import java.util.LinkedList;

import javax.swing.JComponent;

import common.ProofModel;
import common.ProofNode;

public abstract class AbstractNode extends JComponent {

	protected AbstractNode					parentNode 	= null;
	
	protected LinkedList<AbstractNode>		childNodes 	= new LinkedList<AbstractNode>();
	
	protected ProofNode						proofNode 	= null;
	
	protected ProofModel					model		= null;
	
	public AbstractNode () {
		super();
	}
	
	public ProofNode getProofNode() {
		return this.proofNode;
	}
	
	public void setParentNode(AbstractNode parentNode) {
		this.parentNode = parentNode;
	}
	
	public AbstractNode getParentNode() {
		return this.parentNode;
	}
	
	public void addChildNode (AbstractNode node, int index) {
		if (node == null) {
			return;
		}
		this.childNodes.add(index, node);
		node.setParentNode(this);
	}
	
	public void removeChildeNodes() {
		this.childNodes.clear();
	}
	
	public void removeChildNode(int index) {
		this.childNodes.remove(index);
	}
	
	public LinkedList<AbstractNode> getChildren() {
		return this.childNodes;
	}
	
	public boolean hasChildren() {
		return this.childNodes.size () >= 1;
	}
	
	public AbstractNode getFirstChild() {
		if (this.childNodes.size() >= 1) {
			return this.childNodes.getFirst();
		}
		return null;
	}
	
	public void setModel (ProofModel model) {
		this.model = model;
	}
	
	public void debug (String indentation) {
		System.out.println("" + indentation + ": " + this.proofNode.getExpression());
		for (AbstractNode n : this.childNodes) {
			n.debug(indentation + "  ");
		}
	}
}
