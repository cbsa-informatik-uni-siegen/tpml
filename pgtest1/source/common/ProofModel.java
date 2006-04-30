package common;

import java.util.EventListener;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public abstract class ProofModel extends BeanSupport implements TreeModel {
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
  protected ProofNode root;
  
  
  
  //
  // Bean properties
  //
  
  public boolean isRedoable() {
    // FIXME
    return false;
  }
  
  public boolean isUndoable() {
    // FIXME
    return false;
  }

  
  
  //
  // Actions
  //
  
  public void redo() {
    // FIXME
  }
  
  public void undo() {
    // FIXME
  }
  
  public void translateToCoreSyntax(ProofNode node) {
    // FIXME
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
  public ProofNode getRoot() {
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
  public ProofNode getChild(Object parent, int index) {
    return ((ProofNode)parent).getChildAt(index);
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
   * Builds the parents of node up to and including the root node,
   * where the original node is the last element in the returned
   * array. The length of the returned array gives the node's depth
   * in the tree.
   * 
   * @param aNode the {@link TreeNode} to get the path for.
   * 
   * @return the parents of <code>aNode</code> up to and including
   *         the root node.
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
    TreeNode[] retNodes;
    
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
        retNodes = new TreeNode[depth];
    }
    else {
      depth += 1;
      if (aNode == root)
        retNodes = new TreeNode[depth];
      else
        retNodes = getPathToRoot(aNode.getParent(), depth);
      retNodes[retNodes.length - depth] = aNode;
    }
    return retNodes;
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
   * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
   */
  public void valueForPathChanged(TreePath path, Object newValue) {
    // TODO Auto-generated method stub
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
   * Returns an array of all the tree model listeners registered on this
   * model.
   * 
   * @return all of this model's {@link TreeModelListener}'s or an empty
   *         array if no tree model listeners are currently registered.
   *         
   * @see #addTreeModelListener(TreeModelListener)
   * @see #removeTreeModelListener(TreeModelListener)         
   */
  public TreeModelListener[] getTreeModelListeners() {
    return (TreeModelListener[])this.listenerList.getListeners(TreeModelListener.class);
  }

  /**
   * Returns an array of all the objects currently registered as <code><em>Foo</em>Listener</code>s
   * upon this model. <code><em>Foo</em>Listener</code>s are registered using the
   * <code>add<em>Foo</em>Listener</code> method.
   *
   * You can specify the <code>listenerType</code> argument with a class literal, such as
   * <code><em>Foo</em>Listener.class</code>. For example, you can query a <code>ProofModel</code>
   * <code>m</code> for its tree model listeners with the following code:
   *
   * <pre>TreeModelListener[] tmls = (TreeModelListener[])(m.getListeners(TreeModelListener.class));</pre>
   *
   * If no such listeners exist, this method returns an empty array.
   *
   * @param listenerType the type of listeners requested; this parameter should specify an interface
   *                     that descends from {@link EventListener}.
   *                     
   * @return an array of all objects registered as <code><em>Foo</em>Listener</code>s on this prover,
   *         or an empty array if no such listeners have been added.
   *         
   * @throws ClassCastException if <code>listenerType</code> doesn't specify a class or interface that
   *                            implements {@link EventListener}.
   *
   * @see #getTreeModelListeners()
   */
  public <T extends EventListener> T[] getListeners(Class<T> listenerType) { 
    return listenerList.getListeners(listenerType); 
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
