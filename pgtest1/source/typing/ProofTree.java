package typing;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class ProofTree implements TreeModel {
  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
   */
  public void addTreeModelListener(TreeModelListener l) {
    this.listenerList.add(TreeModelListener.class, l);
  }

  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
   */
  public void removeTreeModelListener(TreeModelListener l) {
    this.listenerList.remove(TreeModelListener.class, l);
  }

  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
   */
  public Object getChild(Object parent, int index) {
    return ((ProofNode)parent).getChildAt(index);
  }

  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
   */
  public int getChildCount(Object parent) {
    return ((ProofNode)parent).getChildCount();
  }

  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
   */
  public int getIndexOfChild(Object parent, Object child) {
    return ((ProofNode)parent).getIndex((ProofNode)child);
  }

  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.tree.TreeModel#getRoot()
   */
  public Object getRoot() {
    return this.root;
  }

  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
   */
  public boolean isLeaf(Object node) {
    return (getChildCount(node) == 0);
  }

  /**
   * {@inheritDoc}
   *
   * This method is not implemented for the {@link ProofTree} class,
   * and will simply throw and {@link UnsupportedOperationException}
   * exception when being invoked.
   * 
   * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
   */
  public void valueForPathChanged(TreePath path, Object newValue) {
    throw new UnsupportedOperationException("valueForPathChanged is not supported for ProofTree");
  }
  
  /**
   * Applies <code>rule</code> for the <code>node</code> in
   * this proof tree and returns the new proof tree that is
   * the result of applying <code>rule</code> at <code>node</code>.
   * 
   * The <code>node</code> must be a valid node for the proof tree,
   * and no type rule must have been applied to <code>node</code>
   * already, that is {@link ProofNode#getRule()} must return
   * <code>null</code> for <code>node</code>.
   * 
   * @param rule the {@link Rule} to apply at <code>node</code>
   * @param node the {@link ProofNode} at which to apply <code>rule</code>.
   * 
   * @return the resulting {@link ProofTree}.
   * 
   * @throws IllegalArgumentException if the <code>node</code> is not
   *                                  valid for the tree or the <code>node</code>
   *                                  is already proven.
   *                                  
   * @see ProofNode#getRule()                                  
   */
  public ProofTree apply(Rule rule, ProofNode node) {
    // verify that the node isn't already proven
    if (node.getRule() != null)
      throw new IllegalArgumentException("A type rule was already applied for the proof node");
    
    // verify that the node is valid for the tree
    if (this.root != node && !this.root.hasChild(node))
      throw new IllegalArgumentException("The proof node is not valid for the proof tree");
    
    // FIXME
    return null;
  }
  
  /**
   * Returns the {@link Judgement} for the <code>node</code> in this
   * proof tree.
   * 
   * @param node a {@link ProofNode} in this proof tree.
   * 
   * @return the {@link Judgement} for the <code>node</code>.
   */
  public Judgement getJudgementForNode(Object node) {
    return ((ProofNode)node).getJudgement();
  }
  
  /**
   * Returns {@link Rule} for the <code>node</code> in this
   * proof tree.
   * 
   * @param node a {@link ProofNode} in this proof tree.
   * 
   * @return the {@link Rule} for the <code>node</code>.
   */
  public Rule getRuleForNode(Object node) {
    return ((ProofNode)node).getRule();
  }
  
  // member attributes
  private EventListenerList listenerList = new EventListenerList();
  private ProofNode root;
}
