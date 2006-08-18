package de.unisiegen.tpml.core;

import java.util.EventListener;
import java.util.LinkedList;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import de.unisiegen.tpml.core.beans.AbstractBean;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageTranslator;

/**
 * Abstract implementation of the {@link de.unisiegen.tpml.core.ProofModel} interface, which is used by prover
 * implementations. The user interface and other modules should never use this class (or a derived class)
 * directly, as methods are subject to change. Instead the upper layer should restrict usage to the
 * {@link de.unisiegen.tpml.core.ProofModel} interface.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.AbstractProofNode
 * @see de.unisiegen.tpml.core.ProofModel
 * @see de.unisiegen.tpml.core.ProofNode
 */
public abstract class AbstractProofModel extends AbstractBean implements ProofModel {
  //
  // Attributes
  //
  
  /**
   * The language of the expression for this model.
   */
  protected Language language;
  
  /**
   * Event listeners.
   * 
   * @see #addTreeModelListener(TreeModelListener)
   * @see #removeTreeModelListener(TreeModelListener)
   */
  protected EventListenerList listenerList = new EventListenerList();
  
  /**
   * The root node of the proof tree.
   * 
   * @see #getRoot()
   */
  protected AbstractProofNode root;
  
  /**
   * The language translator for this model.
   */
  protected LanguageTranslator translator;
  
  

  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>AbstractProofModel</code> using the given <code>root</code> item.
   * 
   * @param language the {@link Language} for this model.
   * @param root the new root item.
   * 
   * @?hrows NullPointerException if <code>language</code> or <code>root</code> is <code>null</code>.
   */
  protected AbstractProofModel(Language language, AbstractProofNode root) {
    if (language == null) {
      throw new NullPointerException("language is null");
    }
    if (root == null) {
      throw new NullPointerException("root is null");
    }

    this.language = language;
    this.root = root;
    this.translator = language.newTranslator();
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   * 
   * @see ProofModel#getRules()
   */
  public abstract ProofRule[] getRules();

  
  
  //
  // Actions
  //
  
  /**
   * {@inheritDoc}
   * 
   * @see ProofModel#guess(ProofNode)
   */
  public abstract void guess(ProofNode node) throws ProofGuessException;
  
  /**
   * {@inheritDoc}
   * 
   * @see ProofModel#prove(ProofRule, ProofNode)
   */
  public abstract void prove(ProofRule rule, ProofNode node) throws ProofRuleException;

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofModel#isSyntacticSugar(de.unisiegen.tpml.core.ProofNode)
   */
  public boolean isSyntacticSugar(ProofNode node) {
    if (!getRoot().isNodeRelated(node)) {
      throw new IllegalArgumentException("node is invalid");
    }
    
    // check if the translated expression is the same
    Expression expression = node.getExpression();
    Expression translated = this.translator.translateToCoreSyntax(expression);
    return (expression != translated);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofModel#containsSyntacticSugar(de.unisiegen.tpml.core.ProofNode)
   */
  public boolean containsSyntacticSugar(ProofNode node) {
    if (!getRoot().isNodeRelated(node)) {
      throw new IllegalArgumentException("node is invalid");
    }
    
    // check if the translated expression is the same
    Expression expression = node.getExpression();
    Expression translated = this.translator.translateToCoreSyntax(expression, true);
    return (expression != translated);
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see ProofModel#translateToCoreSyntax(ProofNode)
   */
  public void translateToCoreSyntax(ProofNode node) {
    translateToCoreSyntax(node, false);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofModel#translateToCoreSyntax(de.unisiegen.tpml.core.ProofNode, boolean)
   */
  public void translateToCoreSyntax(ProofNode node, boolean recursive) {
    // verify that the expression for the node actually contains or is syntactic sugar
    if ((recursive && !containsSyntacticSugar(node)) || (!recursive && !isSyntacticSugar(node))) {
      throw new IllegalArgumentException("node does not contain syntactic sugar");
    }
    
    // verify that no actions were performed on the node
    if (node.getSteps().length > 0) {
      throw new IllegalStateException("steps have been performed on node");
    }
    
    // cast the proof node to the appropriate type
    final AbstractProofNode abstractNode = (AbstractProofNode)node;

    // translate the expression to core syntax
    final Expression expression = node.getExpression();
    final Expression coreExpression = this.translator.translateToCoreSyntax(expression, recursive);
    
    // create the undoable edit
    UndoableTreeEdit edit = new UndoableTreeEdit() {
      public void redo() {
        // translate the expression of the node to core syntax
        abstractNode.setExpression(coreExpression);
        nodeChanged(abstractNode);
      }
      
      public void undo() {
        // restore the previous expression
        abstractNode.setExpression(expression);
        nodeChanged(abstractNode);
      }
    };
    
    // perform the redo operation
    edit.redo();
    
    // and record the edit
    addUndoableTreeEdit(edit);
  }
  
  
  
  //
  // Undo/Redo
  //
  
  /**
   * The base interface for all undoable edit actions
   * on the tree model. Derived classes will need to
   * implement this interface whenever any action on
   * the tree is to be performed.
   */
  protected static interface UndoableTreeEdit {
    /**
     * Performs the action of this tree edit. This method
     * is invoked initially, when the edit is added via
     * {@link AbstractProofModel#addUndoableTreeEdit(UndoableTreeEdit)},
     * and when a previously undone change is redone.
     */
    public void redo();
    
    /**
     * Undoes the effect of a previous call to {@link #redo()}.
     */
    public void undo();
  }

  /**
   * The list of redoable edits.
   * 
   * @see #isRedoable()
   * @see #redo()
   */
  private LinkedList<UndoableTreeEdit> redoEdits = new LinkedList<UndoableTreeEdit>();

  /**
   * The list of undoable edits.
   * 
   * @see #isUndoable()
   * @see #undo()
   */
  private LinkedList<UndoableTreeEdit> undoEdits = new LinkedList<UndoableTreeEdit>();
  
  /**
   * Adds the specified <code>edit</code> to the undo history. The
   * current redo history is cleared.
   * 
   * @param edit the {@link UndoableTreeEdit} to add.
   * 
   * @throws NullPointerException if <code>edit</code> is <code>null</code>.
   */
  protected void addUndoableTreeEdit(UndoableTreeEdit edit) {
    // remember the previous redoable/undoable properties
    boolean oldRedoable = isRedoable();
    boolean oldUndoable = isUndoable();
    
    // clear the redo history
    this.redoEdits.clear();
    
    // add to the undo history
    this.undoEdits.add(0, edit);
    
    // notify the redoable/undoable properties
    if (oldRedoable != isRedoable()) {
      firePropertyChange("redoable", oldRedoable, isRedoable());
    }
    if (oldUndoable != isUndoable()) {
      firePropertyChange("undoable", oldUndoable, isUndoable());
    }
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see ProofModel#isRedoable()
   */
  public boolean isRedoable() {
    return (!this.redoEdits.isEmpty());
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see ProofModel#isUndoable()
   */
  public boolean isUndoable() {
    return (!this.undoEdits.isEmpty());
  }

  /**
   * {@inheritDoc}
   *
   * @see common.ProofModel#redo()
   */
  public void redo() throws CannotRedoException {
    if (this.redoEdits.isEmpty()) {
      throw new CannotRedoException("nothing to redo");
    }
    
    // remember the previous redoable/undoable properties
    boolean oldRedoable = isRedoable();
    boolean oldUndoable = isUndoable();
    
    // redo the most recent tree edit
    UndoableTreeEdit edit = this.redoEdits.poll();
    this.undoEdits.add(0, edit);
    edit.redo();
    
    // notify the redoable/undoable properties
    if (oldRedoable != isRedoable()) {
      firePropertyChange("redoable", oldRedoable, isRedoable());
    }
    if (oldUndoable != isUndoable()) {
      firePropertyChange("undoable", oldUndoable, isUndoable());
    }
  }

  /**
   * {@inheritDoc}
   *
   * @see common.ProofModel#undo()
   */
  public void undo() throws CannotUndoException {
    if (this.undoEdits.isEmpty()) {
      throw new CannotUndoException("nothing to undo");
    }
    
    // remember the previous redoable/undoable properties
    boolean oldRedoable = isRedoable();
    boolean oldUndoable = isUndoable();
    
    // undo the most recent tree edit
    UndoableTreeEdit edit = this.undoEdits.poll();
    this.redoEdits.add(0, edit);
    edit.undo();
    
    // notify the redoable/undoable properties
    if (oldRedoable != isRedoable()) {
      firePropertyChange("redoable", oldRedoable, isRedoable());
    }
    if (oldUndoable != isUndoable()) {
      firePropertyChange("undoable", oldUndoable, isUndoable());
    }
  }
  
  
  
  //
  // Tree queries
  //

  /**
   * Returns the root of the tree. Returns <code>null</code>
   * only if the tree has no nodes.
   * 
   * @return the root of the proof tree.
   * 
   * @see javax.swing.tree.TreeModel#getRoot()
   */
  public AbstractProofNode getRoot() {
    return this.root;
  }

  /**
   * Returns the child of <code>parent</code> at index <code>index</code> in
   * the <code>parent</code>'s child array. <code>parent</code> must be a node
   * previously obtained from this data source. This should not return <code>null</code>
   * if <code>index</code> is a valid index for <code>parent</code> (that is
   * <code>index &gt;= 0 && index &lt; getChildCount(parent)</code>).
   * 
   * @param parent a node in the tree, obtained from this data source.
   * @param index the child index of a child node of <code>parent</code>.
   *  
   * @return the child of <code>parent</code> at index <code>index</code>.
   * 
   * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
   */
  public AbstractProofNode getChild(Object parent, int index) {
    return ((AbstractProofNode)parent).getChildAt(index);
  }

  /**
   * Returns the number of children of <code>parent</code>. Returns <code>0</code>
   * if the node is a leaf or if it has no children. <code>parent</code> must be a
   * node previously obtained from this data source.
   * 
   * @param parent a node in the tree, obtained from this data source
   * 
   * @return the number of children of the node <code>parent</code>.
   * 
   * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
   */
  public int getChildCount(Object parent) {
    return ((ProofNode)parent).getChildCount();
  }

  /**
   * Returns the index of child in parent. If either the parent or child is
   * <code>null</code>, returns -1.
   * 
   * @param parent a note in the tree, obtained from this data source.
   * @param child the node we are interested in.
   * 
   * @return the index of the <code>child</code> in the <code>parent</code>,
   *         or -1 if either the <code>parent</code> or the <code>child</code>
   *         is <code>null</code>.
   *
   * @see TreeNode#getIndex(javax.swing.tree.TreeNode)
   * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
   */
  public int getIndexOfChild(Object parent, Object child) {
    if (parent == null || child == null)
      return -1;
    return ((ProofNode)parent).getIndex((ProofNode)child);
  }

  /**
   * {@inheritDoc}
   * 
   * @see ProofModel#getPathToRoot(TreeNode)
   */
  public TreeNode[] getPathToRoot(TreeNode aNode) {
    return getPathToRoot(aNode, 0);
  }

  /**
   * Builds the parents of node up to and including the root node,
   * where the original node is the last element in the returned array.
   * The length of the returned array gives the node's depth in the
   * tree.
   * 
   * @param aNode the {@link TreeNode} to get the path for
   * @param depth an integer giving the number of steps already taken
   *        towards the root (on recursive calls), used to size the
   *        returned array.
   *        
   * @return an array of {@link TreeNode}s giving the path from the root
   *         to the specified node. 
   */
  protected TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
    TreeNode[] nodes;
    
    // This method recurses, traversing towards the root in order
    // size the array. On the way back, it fills in the nodes,
    // starting from the root and working back to the original node.

    /* Check for null, in case someone passed in a null node, or
     * they passed in an element that isn't rooted at root.
     */
    if (aNode == null) {
      if (depth == 0)
        return null;
      else
        nodes = new TreeNode[depth];
    }
    else {
      depth += 1;
      if (aNode == this.root)
        nodes = new TreeNode[depth];
      else
        nodes = getPathToRoot(aNode.getParent(), depth);
      nodes[nodes.length - depth] = aNode;
    }
    return nodes;
  }
  
  /**
   * Returns <code>true</code> if <code>node</code> is a leaf. It is possible for
   * this method to return <code>false</code> even if node has no children. A
   * directory in a filesystem, for example, may contain no files; the node
   * representing the directory is not a leaf, but it also has no children.
   * 
   * @param node a node in the tree, obtained from this data source.
   * 
   * @return <code>true</code> if the <code>node</code> is a leaf.
   * 
   * @see ProofNode#isLeaf()
   * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
   */
  public boolean isLeaf(Object node) {
    return ((ProofNode)node).isLeaf();
  }

  /**
   * This method is not implemented by the {@link ProofModel} class
   * and will an {@link UnsupportedOperationException} on every
   * invocation.
   * 
   * @param path path to the node that the user has altered.
   * @param newValue the new value from the {@link javax.swing.tree.TreeCellEditor}.
   * 
   * @throws UnsupportedOperationException on every invocation.
   * 
   * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
   */
  public void valueForPathChanged(TreePath path, Object newValue) {
    throw new UnsupportedOperationException("Method not implemented");
  }

  
  
  //
  // Events
  //
  
  /**
   * Adds a listener for the {@link TreeModelEvent} posted after the tree changes.
   * 
   * @param l the listener to be added.
   * 
   * @see #getTreeModelListeners()
   * @see #removeTreeModelListener(TreeModelListener)
   * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
   */
  public void addTreeModelListener(TreeModelListener l) {
    this.listenerList.add(TreeModelListener.class, l);
  }

  /**
   * Removes a listener previously added with {@link #addTreeModelListener(TreeModelListener)}.
   * 
   * @param l the listener to be removed.
   * 
   * @see #addTreeModelListener(TreeModelListener)
   * @see #getTreeModelListeners()
   * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
   */
  public void removeTreeModelListener(TreeModelListener l) {
    this.listenerList.remove(TreeModelListener.class, l);
  }

  /**
   * {@inheritDoc}
   * 
   * @see ProofModel#getTreeModelListeners()
   */
  public TreeModelListener[] getTreeModelListeners() {
    return this.listenerList.getListeners(TreeModelListener.class);
  }

  /**
   * {@inheritDoc}
   * 
   * @see ProofModel#getListeners(Class)
   */
  public <T extends EventListener> T[] getListeners(Class<T> listenerType) { 
    return this.listenerList.getListeners(listenerType); 
  }

  /**
   * Notifies all listeners that have registered interest for
   * notification on this event type. The event instance 
   * is lazily created using the parameters passed into 
   * the fire method.
   *
   * @param source the node being changed
   * @param path the path to the root node
   * @param childIndices the indices of the changed elements
   * @param children the changed elements
   * 
   * @see EventListenerList
   */
  protected void fireTreeNodesChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
    // Guaranteed to return a non-null array
    Object[] listeners = this.listenerList.getListenerList();
    TreeModelEvent e = null;
      
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == TreeModelListener.class) {
        // Lazily create the event:
        if (e == null)
          e = new TreeModelEvent(source, path, childIndices, children);
        ((TreeModelListener)listeners[i + 1]).treeNodesChanged(e);
      }          
    }
  }

  /**
   * Notifies all listeners that have registered interest for
   * notification on this event type. The event instance is 
   * lazily created using the parameters passed into the fire 
   * method.
   *
   * @param source the node where new elements are being inserted
   * @param path the path to the root node
   * @param childIndices the indices of the new elements
   * @param children the new elements
   * 
   * @see EventListenerList
   */
  protected void fireTreeNodesInserted(Object source, Object[] path, int[] childIndices, Object[] children) {
    // Guaranteed to return a non-null array
    Object[] listeners = this.listenerList.getListenerList();
    TreeModelEvent e = null;
    
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == TreeModelListener.class) {
        // Lazily create the event:
        if (e == null)
          e = new TreeModelEvent(source, path, childIndices, children);
        ((TreeModelListener)listeners[i + 1]).treeNodesInserted(e);
      }          
    }
  }

  /**
   * Notifies all listeners that have registered interest for
   * notification on this event type. The event instance is 
   * lazily created using the parameters passed into the fire 
   * method.
   *
   * @param source the node where elements are being removed
   * @param path the path to the root node
   * @param childIndices the indices of the removed elements
   * @param children the removed elements
   * 
   * @see EventListenerList
   */
  protected void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices, Object[] children) {
    // Guaranteed to return a non-null array
    Object[] listeners = this.listenerList.getListenerList();
    TreeModelEvent e = null;
    
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == TreeModelListener.class) {
        // Lazily create the event:
        if (e == null)
          e = new TreeModelEvent(source, path,  childIndices, children);
        ((TreeModelListener)listeners[i + 1]).treeNodesRemoved(e);
      }          
    }
  }

  /**
   * Notifies all listeners that have registered interest for
   * notification on this event type. The event instance 
   * is lazily created using the parameters passed into 
   * the fire method.
   *
   * @param source the node where the tree model has changed
   * @param path the path to the root node
   * @param childIndices the indices of the affected elements
   * @param children the affected elements
   * 
   * @see EventListenerList
   */
  protected void fireTreeStructureChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
    // Guaranteed to return a non-null array
    Object[] listeners = this.listenerList.getListenerList();
    TreeModelEvent e = null;
    
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == TreeModelListener.class) {
        // Lazily create the event:
        if (e == null)
          e = new TreeModelEvent(source, path, childIndices, children);
        ((TreeModelListener)listeners[i + 1]).treeStructureChanged(e);
      }          
    }
  }
  
  
  
  //
  // Convenience methods for event handling
  //
  
  /**
    * Invoke this method after you've changed how node is to be
    * represented in the tree.
    * 
    * @param node the {@link TreeNode} that was altered.
    */
  protected void nodeChanged(TreeNode node) {
    if (this.listenerList != null && node != null) {
      // determine the parent node
      TreeNode parent = node.getParent();
      if (parent != null) {
        // determine the index of the node
        int anIndex = parent.getIndex(node);
        if (anIndex != -1) {
          int[] cIndexs = new int[1];
          cIndexs[0] = anIndex;
          nodesChanged(parent, cIndexs);
        }
      }
      else if (node == getRoot()) {
        nodesChanged(node, null);
      }
    }
  }
  
  /**
   * Invoke this method after you've changed how the children identified by
   * <code>childIndicies</code> are to be represented in the tree.
   * 
   * @param node a {@link TreeNode} within this proof model.
   * @param childIndices the indices of the children that changed.
   */
  protected void nodesChanged(TreeNode node, int[] childIndices) {
    if (node != null) {
      if (childIndices != null) {
        // check if any child indices were supplied
        int cCount = childIndices.length;
        if (cCount > 0) {
          // collect the child nodes
          Object[] cChildren = new Object[cCount];
          for (int counter = 0; counter < cCount; ++counter)
            cChildren[counter] = node.getChildAt(childIndices[counter]);

          // notify the view
          fireTreeNodesChanged(this, getPathToRoot(node), childIndices,
              cChildren);
        }
      }
      else if (node == getRoot()) {
        fireTreeNodesChanged(this, getPathToRoot(node), null, null);
      }
    }
  }

  /**
   * Invoke this method after you've inserted some TreeNodes into
   * node. <code>childIndices</code> should be the index of the new
   * elements and must be sorted in ascending order.
   * 
   * @param node a node in the proof model.
   * @param childIndices the indices of the children that were inserted.
   */
  protected void nodesWereInserted(TreeNode node, int[] childIndices) {
    if (this.listenerList != null && node != null && childIndices != null && childIndices.length > 0) {
      int cCount = childIndices.length;
      Object[] newChildren = new Object[cCount];

      for (int counter = 0; counter < cCount; ++counter)
        newChildren[counter] = node.getChildAt(childIndices[counter]);
      fireTreeNodesInserted(this, getPathToRoot(node), childIndices, newChildren);
    }
  }
  
  /**
   * Invoke this method after you've removed some TreeNodes from
   * node. <code>childIndices</code> should be the index of the
   * removed elements and must be sorted in ascending order.
   * And <code>removedChildren</code> should be the array of
   * the children objects that were removed.
   * 
   * @param node a node in the proof model.
   * @param childIndices the indices of the children that were removed.
   * @param removedChildren the removed children.
   */
  protected void nodesWereRemoved(TreeNode node, int[] childIndices, Object[] removedChildren) {
    if (node != null && childIndices != null) {
      fireTreeNodesRemoved(this, getPathToRoot(node), childIndices, removedChildren);
    }
  }
}
