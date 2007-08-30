package de.unisiegen.tpml.core ;


import java.util.Enumeration ;
import java.util.NoSuchElementException ;
import javax.swing.tree.TreeNode ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;


/**
 * Base interface for proof nodes that present the fundamental parts of the
 * {@link de.unisiegen.tpml.core.ProofModel}s.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev$
 * @see javax.swing.tree.TreeNode
 */
public interface ProofNode extends TreeNode , PrettyPrintable , LatexPrintable
{
  /**
   * Returns the child in this node's child array that immediately follows
   * <code>aChild</code>, which must be a child of this node. If
   * <code>aChild</code> is the last child, returns <code>null</code>. This
   * method performs a linear search of this node's children for
   * <code>aChild</code> and is <code>O(n)</code> where <code>n</code> is
   * the number of children; to traverse the entire array of children, use an
   * enumeration instead.
   * 
   * @param aChild The given node.
   * @return the child of this node that immediately follows <code>aChild</code>.
   * @throws IllegalArgumentException if <code>aChild</code> is
   *           <code>null</code> or is not a child of this node.
   * @see javax.swing.tree.TreeNode#children()
   * @see #getChildBefore(TreeNode)
   */
  public ProofNode getChildAfter ( TreeNode aChild ) ;


  /**
   * Returns the child of this node at the <code>childIndex</code>.
   * 
   * @param childIndex the index of the child node to return.
   * @return the child {@link ProofNode} at the given index
   *         <code>childIndex</code>.
   * @throws ArrayIndexOutOfBoundsException if <code>childIndex</code> is out
   *           of bounds.
   * @see javax.swing.tree.TreeNode#getChildAt(int)
   */
  public ProofNode getChildAt ( int childIndex ) ;


  /**
   * Returns the child in this node's child array that immediately precedes
   * <code>aChild</code>, which must be a child of this node. If
   * <code>aChild</code> is the first child, returns <code>null</code>.
   * This method performs a linear search of this node's children for
   * <code>aChild</code> and is O(n) where n is the number of children.
   * 
   * @param aChild The given node.
   * @return the child of this node that immediately precedes
   *         <code>aChild</code>.
   * @throws IllegalArgumentException if <code>aChild</code> is
   *           <code>null</code> or is not a child of this node.
   * @see javax.swing.tree.TreeNode#children()
   * @see #getChildAfter(TreeNode)
   */
  public ProofNode getChildBefore ( TreeNode aChild ) ;


  /**
   * Returns this node's first child. If this node has no children, throws
   * {@link NoSuchElementException}.
   * 
   * @return the first child of this node.
   * @throws NoSuchElementException if this node has no children.
   * @see #getLastChild()
   */
  public ProofNode getFirstChild ( ) ;


  /**
   * Finds and returns the first leaf that is a descendant of this node - either
   * this node or its first child's first leaf. Returns this node if it is a
   * leaf.
   * 
   * @return the first leaf in the subtree rooted at this node.
   * @see javax.swing.tree.TreeNode#isLeaf()
   * @see #isNodeDescendant(ProofNode)
   */
  public ProofNode getFirstLeaf ( ) ;


  /**
   * Returns this node's last child. If this node has no children, throws
   * {@link NoSuchElementException}.
   * 
   * @return the last child of this node.
   * @throws NoSuchElementException if this node has no children.
   * @see #getFirstChild()
   */
  public ProofNode getLastChild ( ) ;


  /**
   * Finds and returns the last leaf that is a descendant of this node - either
   * this node or its last child's last leaf. Returns this node if it is a leaf.
   * 
   * @return the last leaf in the subtree rooted at this node.
   * @see javax.swing.tree.TreeNode#isLeaf()
   * @see #isNodeDescendant(ProofNode)
   */
  public ProofNode getLastLeaf ( ) ;


  /**
   * Returns the node's parent or <code>null</code> if this node has no
   * parent.
   * 
   * @return this node's parent {@link ProofNode}, or <code>null</code> if
   *         this node has no parent.
   * @see javax.swing.tree.TreeNode#getParent()
   */
  public ProofNode getParent ( ) ;


  /**
   * Returns the root of the tree that contains this node. The root is the
   * ancestor with a <code>null</code> parent.
   * 
   * @return the root of the tree that contains this node.
   * @see #isNodeAncestor(TreeNode)
   * @see #isRoot()
   */
  public ProofNode getRoot ( ) ;


  /**
   * Returns the {@link ProofRule}s which were already applied to this proof
   * node. If no rules have been applied so far, the empty array is returned
   * instead.
   * 
   * @return the already applied {@link ProofRule}s.
   * @see ProofRule
   */
  public ProofRule [ ] getRules ( ) ;


  /**
   * Returns the user object associated with this proof node, or
   * <code>null</code> if no user object was previously set via
   * {@link #setUserObject(Object)}.
   * 
   * @return the user object set for this proof node or <code>null</code>.
   * @see #setUserObject(Object)
   */
  public Object getUserObject ( ) ;


  /**
   * Returns <code>true</code> if <code>anotherNode</code> is an ancestor of
   * this node - if it is this node, this node's parent, or an ancestor of this
   * node's parent (note that a node is considered an ancestor of itself). If
   * <code>anotherNode</code> is null, this method returns false. This
   * operation is at worst <code>O(h)</code> where <code>h</code> is the
   * distance from the root to this node.
   * 
   * @param anotherNode node to test as an ancestor of this node
   * @return <code>true</code> if this node is a descendant of
   *         <code>anotherNode</code>.
   * @see #isNodeChild(TreeNode)
   * @see #isNodeDescendant(ProofNode)
   */
  public boolean isNodeAncestor ( TreeNode anotherNode ) ;


  /**
   * Returns <code>true</code> if <code>aNode</code> is a child of this
   * node. If <code>aNode</code> is <code>null</code>, this method returns
   * <code>false</code>.
   * 
   * @param aNode The node which should be tested.
   * @return <code>true</code> if <code>aNode</code> is a child of this
   *         node; <code>false</code> if <code>aNode</code> is
   *         <code>null</code>.
   * @see #isNodeAncestor(TreeNode)
   */
  public boolean isNodeChild ( TreeNode aNode ) ;


  /**
   * Returns <code>true</code> if <code>anotherNode</code> is a descendant
   * of this node - if it is this node, one of this node's children, or a
   * descendant of one of this node's children. Note that a node is considered a
   * descendant of itself. If <code>anotherNode</code> is <code>null</code>,
   * returns <code>false</code>. This operation is at worst O(h) where h is
   * the distance from the root to <code>anotherNode</code>.
   * 
   * @param anotherNode node to test as descendant of this node
   * @return <code>true</code> if this node is an ancestor of
   *         <code>anotherNode</code>.
   * @see #isNodeAncestor(TreeNode)
   * @see #isNodeChild(TreeNode)
   */
  public boolean isNodeDescendant ( ProofNode anotherNode ) ;


  /**
   * Returns <code>true</code> if and only if <code>aNode</code> is in the
   * same tree as this node. Returns <code>false</code> if <code>aNode</code>
   * is <code>null</code>.
   * 
   * @param aNode The node which should be tested.
   * @return <code>true if <code>aNode</code> is in the same tree as this node, <code>false</code> if
   *         <code>aNode</code> is <code>null</code>.
   * 
   * @see #getRoot()
   */
  public boolean isNodeRelated ( ProofNode aNode ) ;


  /**
   * Returns <code>true</code> if this node is already proven, that is,
   * whether no more rules can be applied to this node. If <code>false</code>
   * is returned the user must still apply additional rules to complete this
   * node.
   * 
   * @return <code>true</code> if this node is already proven.
   * @see #getRules()
   */
  public boolean isProven ( ) ;


  /**
   * Returns true if this node is the root of the tree. The root is the only
   * node in the tree with a <code>null</code> parent; every tree has exactly
   * one root.
   * 
   * @return <code>true</code> if this node is the root of its tree.
   * @see #getRoot()
   */
  public boolean isRoot ( ) ;


  /**
   * Creates and returns an enumeration that traverses the subtree rooted at
   * this node in postorder. The first node returned by the enumeration's
   * <code>nextElement()</code> method is the leftmost leaf. This is the same
   * as a depth-first traversal. Modifying the tree by inserting, removing, or
   * moving a node invalidates any enumerations created before the modification.
   * 
   * @return an enumeration for traversing the tree in post-order.
   */
  public Enumeration < ProofNode > postorderEnumeration ( ) ;


  /**
   * Associates the specified <code>userObject</code> with this proof node.
   * The <code>userObject</code> may be <code>null</code>, in which case
   * only the previously set user object will be reset.
   * 
   * @param userObject the new user object to associate with this proof node.
   * @see #getUserObject()
   */
  public void setUserObject ( Object userObject ) ;


  /**
   * Get the unique id of this proof node
   * 
   * @return int unique id
   */
  public int getId ( ) ;
}
