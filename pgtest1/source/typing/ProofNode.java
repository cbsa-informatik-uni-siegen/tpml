package typing;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.TreeNode;

/**
 * A node in the proof tree represented by {@link ProofTree}.
 * Each node has an associated {@link typing.Judgement}.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class ProofNode implements TreeNode {
  /**
   * Allocates a new <code>ProofNode</code> instance with the
   * given <code>judgement</code>.
   * 
   * @param judgement the type judgement for this proof node.
   * @param rule the type {@link Rule} that was applied for
   *             the newly allocated <code>ProofNode</code>
   *             or <code>null</code>.
   */
  ProofNode(Judgement judgement, Rule rule) {
    this.judgement = judgement;
    this.rule = rule;
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.tree.TreeNode#getChildAt(int)
   */
  public TreeNode getChildAt(int childIndex) {
    return this.children.elementAt(childIndex);
  }

  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.tree.TreeNode#getChildCount()
   */
  public int getChildCount() {
    return this.children.size();
  }

  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.tree.TreeNode#getParent()
   */
  public TreeNode getParent() {
    return this.parent;
  }

  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
   */
  public int getIndex(TreeNode node) {
    return this.children.indexOf(node);
  }

  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.tree.TreeNode#getAllowsChildren()
   */
  public boolean getAllowsChildren() {
    return !isLeaf();
  }

  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.tree.TreeNode#isLeaf()
   */
  public boolean isLeaf() {
    return (getChildCount() == 0);
  }

  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.tree.TreeNode#children()
   */
  public Enumeration children() {
    return this.children.elements();
  }

  /**
   * Returns the judgement associated with this
   * proof node in the proof tree.
   * 
   * @return Returns the judgement.
   */
  public Judgement getJudgement() {
    return this.judgement;
  }
  
  /**
   * Returns the {@link Rule} that was applied for this
   * proof node, or <code>null</code> if no rule was
   * applied yet.
   * 
   * @return the type rule that was applied or
   *         <code>null</code>.
   */
  public Rule getRule() {
    return this.rule;
  }

  /**
   * Adds <code>node</code> as child node to this
   * proof node. The parent of <code>node</code> will
   * be set to point to this {@link ProofNode}.
   * 
   * @param node the node to add to the children.
   * 
   * @see #children()
   * @see #getParent()
   */
  void addChild(ProofNode node) {
    this.children.add(node);
    node.parent = this;
  }
  
  /**
   * Returns <code>true</code> if this proof node or any of
   * its sub nodes contains the given <code>node</code> in
   * the list of children.
   * 
   * @param node the child node to look up.
   * 
   * @return <code>true</code> if <code>node</code> is a
   *         child of this proof node.
   */
  boolean hasChild(ProofNode node) {
    for (ProofNode child : this.children)
      if (child == node || child.hasChild(node))
        return true;
    return false;
  }

  // member attributes
  private Judgement judgement;
  private Rule rule;
  private ProofNode parent;
  private Vector<ProofNode> children = new Vector<ProofNode>();
}
