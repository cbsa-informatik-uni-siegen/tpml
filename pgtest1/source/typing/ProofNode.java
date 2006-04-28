package typing;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import expressions.Expression;
import expressions.Let;


/**
 * A node in the proof tree represented by {@link ProofTree}.
 * Each node has an associated {@link typing.Judgement}.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class ProofNode implements TreeNode {
  /**
   * Convenience wrapper for {@link #ProofNode(Judgement, Rule)},
   * which passes <code>null</code> for <code>rule</code>.
   * 
   * @param judgement the type judgement for this proof node.
   */
  ProofNode(Judgement judgement) {
    this(judgement, null);
  }
  
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
  public ProofNode getChildAt(int childIndex) {
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
  public ProofNode getParent() {
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
   * Returns <code>true</code> if this node and all
   * sub nodes are finished. A node is finished if
   * a {@link Rule} was applied and thereby a proper
   * type is known.
   * 
   * @return <code>true</code> if finished.
   */
  public boolean isFinished() {
    // check if this node has a rule
    if (this.rule == null)
      return false;
    
    // check all children
    for (ProofNode node : this.children)
      if (!node.isFinished())
        return false;
    
    return true;
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
   * Convenience wrapper for the {@link #addChild(ProofNode)}
   * method.
   * 
   * @param judgement a {@link Judgement} to add.
   * 
   * @see #addChild(ProofNode)
   */
  void addChild(Judgement judgement) {
    addChild(new ProofNode(judgement));
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
  boolean containsChild(ProofNode node) {
    for (ProofNode child : this.children)
      if (child == node || child.containsChild(node))
        return true;
    return false;
  }

  /**
   * Returns <code>true</code> if this proof node or any of
   * its sub nodes contains a type, which in turn contains
   * a type variable of the given <code>name</code>.
   * 
   * @param name the name of the type variable to test.
   * 
   * @return <code>true</code> if a type variable of the
   *         given <code>name</code> is present for this
   *         node or any of its subnodes.
   */
  boolean containsTypeVariable(String name) {
    if (this.judgement.getType().containsFreeTypeVariable(name))
      return true;
    
    for (ProofNode child : this.children)
      if (child.containsTypeVariable(name))
        return true;
    
    return false;
  }
  
  /**
   * Clones this node and all subnodes. Replaces all occurances of
   * <code>oldNode</code> in the current tree below this node with
   * <code>newNode</code> in the cloned tree.
   * 
   * @param oldNode the old node in the current tree.
   * @param newNode the new node for <code>oldNode</code> in the
   *                cloned tree.
   *                
   * @return the resulting tree starting at this node.
   */
  ProofNode cloneSubstituteAndReplace(Substitution substitution, ProofNode oldNode, ProofNode newNode, TypeVariableAllocator typeVariableAllocator) {
    // check if this one should be replaced
    if (oldNode == this)
      return newNode.cloneSubstituteAndReplace(substitution, null, null, typeVariableAllocator);
    
    // allocate a new copy of the node
    ProofNode node = new ProofNode(this.judgement.substitute(substitution, typeVariableAllocator), this.rule);
    node.children = new Vector<ProofNode>();
    
    // clone/replace all children
    for (ProofNode oldChild : this.children) {
      ProofNode newChild = oldChild.cloneSubstituteAndReplace(substitution, oldNode, newNode, typeVariableAllocator);
      node.children.add(newChild);
      newChild.parent = node;
    }
    
    // special case (P-LET): We can only add the second child once the first
    // subtree is finished.
    if (node.rule == Rule.P_LET && node.children.size() == 1 && node.isFinished()) {
      Environment environment = node.judgement.getEnvironment();
      Let let = (Let)node.judgement.getExpression();
      MonoType tau = node.judgement.getType();
      MonoType tau1 = node.children.firstElement().judgement.getType();
      node.addChild(new Judgement(environment.extend(let.getId(), environment.closure(tau1)), let.getE2(), tau));
    }
    
    // and return the cloned node
    return node;
  }
  
  ProofNode findNodeByExpression(Expression expression) {
    if (this.judgement.getExpression() == expression) {
      return this;
    }
    else {
      for (ProofNode child : this.children) {
        ProofNode node = child.findNodeByExpression(expression);
        if (node != null)
          return node;
      }
    }
    return null;
  }
  
  // member attributes
  private Judgement judgement;
  private Rule rule;
  private ProofNode parent;
  private Vector<ProofNode> children = new Vector<ProofNode>();
}
