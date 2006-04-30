package common;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import expressions.Expression;

/**
 * TODO Add documentation here.
 * 
 * @see javax.swing.tree.TreeNode
 * 
 * @author Benedikt Meurer
 * @version $Id$
 */
public abstract class ProofNode implements TreeNode {
  /**
   * An enumeration that is always empty. This is used when an enumeration of a
   * leaf node's children is requested.
   */
  public static final Enumeration EMPTY_ENUMERATION = new Enumeration() {
    public boolean hasMoreElements() { return false; }
    public Object nextElement() { throw new NoSuchElementException("No more elements"); }
  };

  /**
   * The child nodes of this node, or <code>null</code> if this node has no
   * children.
   * 
   * @see #children()
   * @see #getChildAt(int)
   * @see #getChildCount()
   */
  protected Vector<ProofNode> children;
  
  /**
   * The {@link Expression} associated with this proof node.
   * 
   * @see #getExpression()
   */
  protected Expression expression;

  /**
   * The parent node of this node, or <code>null</code> if this is the root
   * node of a proof tree.
   * 
   * @see #getParent()
   */
  protected ProofNode parent;

  
  
  //
  // Primitives
  //

  /**
   * Returns the {@link Expression} associated with this proof
   * node. This is garantied to never return <code>null</code>.
   * 
   * @return the {@link Expression} for this proof node.
   */
  public Expression getExpression() {
    return this.expression;
  }

  /**
   * Returns <code>true</code> if the {@link Expression} associated with this
   * proof node contains syntactic code at the top most level. That is the
   * surrounding expression is syntactic sugar that can be translated into the
   * core syntax.
   * 
   * @return <code>true</code> if the associated {@link Expression} contains
   *                           syntactic sugar.
   *                           
   * @see #getExpression()
   */
  public boolean containsSyntacticSugar() {
    return getExpression().containsSyntacticSugar();
  }

  /**
   * Creates and returns a forward-order enumeration of this node's children.
   * Modifying this node's child array invalidates any child enumerations
   * created before the modification.
   * 
   * @return an {@link Enumeration} of this node's children
   * 
   * @see javax.swing.tree.TreeNode#children()
   */
  public Enumeration children() {
    if (children == null) {
      return EMPTY_ENUMERATION;
    }
    else {
      return children.elements();
    }
  }

  /**
   * Returns the child of this node at the <code>childIndex</code>.
   * 
   * @param childIndex the index of the child node to return.
   * 
   * @return the child {@link ProofNode} at the given index
   *         <code>childIndex</code>.
   *         
   * @throws ArrayIndexOutOfBoundsException if <code>childIndex</code> is out
   *                                        of bounds.
   *                                        
   * @see javax.swing.tree.TreeNode#getChildAt(int)
   */
  public ProofNode getChildAt(int childIndex) {
    if (this.children == null) {
      throw new ArrayIndexOutOfBoundsException("node has no children");
    }
    return this.children.elementAt(childIndex);
  }

  /**
   * Returns the number of children of this node.
   * 
   * @return an integer giving the number of children of this node.
   * 
   * @see javax.swing.tree.TreeNode#getChildCount()
   */
  public int getChildCount() {
    if (this.children == null) {
      return 0;
    }
    else {
      return this.children.size();
    }
  }

  /**
   * Returns the node's parent or <code>null</code> if this node has no
   * parent.
   * 
   * @return this node's parent {@link ProofNode}, or <code>null</code> if
   *         this node has no parent.
   *         
   * @see javax.swing.tree.TreeNode#getParent()
   */
  public ProofNode getParent() {
    return this.parent;
  }

  /**
   * Returns the index of the specified child in this node's child array. If the
   * specified node is not a child of this node, returns <code>-1</code>.
   * This method performs a linear search and is O(n) where n is the number of
   * children.
   * 
   * @param aChild the {@link TreeNode} to search for among this node's
   *          children.
   *          
   * @return an integer giving the index of the node in this node's child array,
   *         or <code>-1</code> if the specified node is a not a child of this
   *         node.
   *         
   * @throws IllegalArgumentException if <code>aChild</code> is null.
   * 
   * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
   */
  public int getIndex(TreeNode aChild) {
    if (aChild == null) {
      throw new IllegalArgumentException("argument is null");
    }
    if (!isNodeChild(aChild)) {
      return -1;
    }
    return this.children.indexOf(aChild);
  }

  /**
   * Returns <code>true</code> if this node is allowed to have children.
   * 
   * @return <code>true</code> if this node allows children, else
   *         <code>false</code>.
   *         
   * @see javax.swing.tree.TreeNode#getAllowsChildren()
   */
  public boolean getAllowsChildren() {
    return true;
  }

  
  
  //
  // Tree Queries
  //

  /**
   * Returns <code>true</code> if <code>anotherNode</code> is an ancestor of
   * this node -- if it is this node, this node's parent, or an ancestor of this
   * node's parent (note that a node is considered an ancestor of itself). If
   * <code>anotherNode</code> is null, this method returns false. This
   * operation is at worst O(h) where h is the distance from the root to this
   * node.
   * 
   * @param anotherNode node to test as an ancestor of this node
   * 
   * @return <code>true</code> if this node is a descendant of
   *         <code>anotherNode</code>.
   *         
   * @see #isNodeChild(TreeNode)
   * @see #isNodeDescendant(ProofNode)
   */
  public boolean isNodeAncestor(TreeNode anotherNode) {
    if (anotherNode == null) {
      return false;
    }

    TreeNode ancestor = this;

    do {
      if (ancestor == anotherNode) {
        return true;
      }
    } while ((ancestor = ancestor.getParent()) != null);

    return false;
  }

  /**
   * Returns <code>true</code> if <code>anotherNode</code> is a descendant
   * of this node -- if it is this node, one of this node's children, or a
   * descendant of one of this node's children. Note that a node is considered a
   * descendant of itself. If <code>anotherNode</code> is <code>null</code>,
   * returns <code>false</code>. This operation is at worst O(h) where h is
   * the distance from the root to <code>anotherNode</code>.
   * 
   * @param anotherNode node to test as descendant of this node
   * 
   * @return <code>true</code> if this node is an ancestor of
   *         <code>anotherNode</code>.
   *         
   * @see #isNodeAncestor(TreeNode)
   * @see #isNodeChild(TreeNode)
   */
  public boolean isNodeDescendant(ProofNode anotherNode) {
    if (anotherNode == null)
      return false;

    return anotherNode.isNodeAncestor(this);
  }

  /**
   * Returns <code>true</code> if and only if <code>aNode</code> is in the
   * same tree as this node. Returns <code>false</code> if <code>aNode</code>
   * is <code>null</code>.
   * 
   * @return <code>true if <code>aNode</code> is in the same tree as this node;
   *         <code>false</code> if <code>aNode</code> is <code>null</code>.
   * 
   * @see #getRoot()
   */
  public boolean isNodeRelated(ProofNode aNode) {
    return (aNode != null) && (getRoot() == aNode.getRoot());
  }

  /**
   * Returns the root of the tree that contains this node. The root is the
   * ancestor with a <code>null</code> parent.
   * 
   * @return the root of the tree that contains this node.
   * 
   * @see #isNodeAncestor(ProofNode)
   */
  public ProofNode getRoot() {
    ProofNode ancestor = this;
    ProofNode previous;

    do {
      previous = ancestor;
      ancestor = ancestor.getParent();
    } while (ancestor != null);

    return previous;
  }

  /**
   * Returns true if this node is the root of the tree. The root is the only
   * node in the tree with a <code>null</code> parent; every tree has exactly
   * one root.
   * 
   * @return <code>true</code> if this node is the root of its tree.
   */
  public boolean isRoot() {
    return (getParent() == null);
  }

  
  
  //
  // Child Queries
  //

  /**
   * Returns <code>true</code> if <code>aNode</code> is a child of this
   * node. If <code>aNode</code> is <code>null</code>, this method returns
   * <code>false</code>.
   * 
   * @return <code>true</code> if <code>aNode</code> is a child of this
   *         node; <code>false</code> if <code>aNode</code> is
   *         <code>null</code>.
   */
  public boolean isNodeChild(TreeNode aNode) {
    boolean retval;
    if (aNode == null) {
      retval = false;
    }
    else {
      if (getChildCount() == 0) {
        retval = false;
      }
      else {
        retval = (aNode.getParent() == this);
      }
    }
    return retval;
  }

  /**
   * Returns this node's first child. If this node has no children, throws
   * [@link NoSuchElementException}.
   * 
   * @return the first child of this node.
   * 
   * @throws NoSuchElementException if this node has no children.
   */
  public ProofNode getFirstChild() {
    if (getChildCount() == 0) {
      throw new NoSuchElementException("node has no children");
    }
    return getChildAt(0);
  }

  /**
   * Returns this node's last child. If this node has no children, throws
   * {@link NoSuchElementException}.
   * 
   * @return the last child of this node.
   * 
   * @throws NoSuchElementException if this node has no children.
   */
  public ProofNode getLastChild() {
    if (getChildCount() == 0) {
      throw new NoSuchElementException("node has no children");
    }
    return getChildAt(getChildCount() - 1);
  }

  /**
   * Returns the child in this node's child array that immediately follows
   * <code>aChild</code>, which must be a child of this node. If <code>aChild</code>
   * is the last child, returns <code>null</code>. This method performs a linear
   * search of this node's children for <code>aChild</code> and is O(n) where n
   * is the number of children; to traverse the entire array of children, use an
   * enumeration instead.
   * 
   * @return the child of this node that immediately follows <code>aChild</code>.
   * 
   * @throws IllegalArgumentException if <code>aChild</code> is <code>null</code>
   *                                  or is not a child of this node.
   *
   * @see #children()
   */
  public ProofNode getChildAfter(TreeNode aChild) {
    if (aChild == null) {
      throw new IllegalArgumentException("argument is null");
    }

    int index = getIndex(aChild);

    if (index == -1) {
      throw new IllegalArgumentException("node is not a child");
    }

    if (index < getChildCount() - 1) {
      return getChildAt(index + 1);
    }
    else {
      return null;
    }
  }

  /**
   * Returns the child in this node's child array that immediately precedes
   * <code>aChild</code>, which must be a child of this node. If <code>aChild</code>
   * is the first child, returns <code>null</code>. This method performs a linear
   * search of this node's children for <code>aChild</code> and is O(n) where n
   * is the number of children.
   * 
   * @return the child of this node that immediately precedes <code>aChild</code>.
   * 
   * @throws IllegalArgumentException if <code>aChild</code> is <code>null</code>
   *                                  or is not a child of this node.
   */
  public ProofNode getChildBefore(TreeNode aChild) {
    if (aChild == null) {
      throw new IllegalArgumentException("argument is null");
    }

    int index = getIndex(aChild);

    if (index == -1) {
      throw new IllegalArgumentException("argument is not a child");
    }

    if (index > 0) {
      return getChildAt(index - 1);
    }
    else {
      return null;
    }
  }
  
  
  
  //
  // Leaf Queries
  //

  /**
   * Returns <code>true</code> if this node has no children. To distinguish
   * between nodes that have no children and nodes that <i>cannot</i> have
   * children (e.g. to distinguish files from empty directories), use this
   * method in conjunction with {@link #getAllowsChildren()}.
   * 
   * @return <code>true</code> if this node has no children.
   * 
   * @see #getAllowsChildren()
   * @see javax.swing.tree.TreeNode#isLeaf()
   */
  public boolean isLeaf() {
    return (getChildCount() == 0);
  }

  /**
   * Finds and returns the first leaf that is a descendant of this node --
   * either this node or its first child's first leaf. Returns this node if
   * it is a leaf.
   *
   * @return the first leaf in the subtree rooted at this node.
   * 
   * @see #isLeaf()
   * @see #isNodeDescendant(ProofNode)
   */
  public ProofNode getFirstLeaf() {
    ProofNode node = this;

    while (!node.isLeaf()) {
      node = node.getFirstChild();
    }

    return node;
  }

  /**
   * Finds and returns the last leaf that is a descendant of this node --
   * either this node or its last child's last leaf. Returns this node if
   * it is a leaf.
   * 
   * @return the last leaf in the subtree rooted at this node.
   *
   * @see #isLeaf()
   * @see #isNodeDescendant(ProofNode)
   */
  public ProofNode getLastLeaf() {
    ProofNode node = this;

    while (!node.isLeaf()) {
      node = node.getLastChild();
    }

    return node;
  }
}
