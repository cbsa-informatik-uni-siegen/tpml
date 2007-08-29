package de.unisiegen.tpml.core ;


import java.util.Enumeration ;
import java.util.NoSuchElementException ;
import java.util.Vector ;
import javax.swing.tree.TreeNode ;


/**
 * Abstract base class for proof nodes that present the fundamental parts of the
 * {@link de.unisiegen.tpml.core.AbstractProofModel}s. The user interface and
 * other modules should never access methods of this class directly, as they are
 * subject to change, instead the upper layer should restrict usage to the
 * {@link de.unisiegen.tpml.core.ProofNode} interface.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see ProofNode
 * @see javax.swing.tree.TreeNode
 */
public abstract class AbstractProofNode implements ProofNode
{
  //
  // Constants
  //
  /**
   * Empty {@link ProofRule} array which is returned from {@link #getRules()}
   * when no steps have been added to a proof node.
   */
  private static final ProofRule [ ] EMPTY_ARRAY = new ProofRule [ 0 ] ;
  
  /**
   * The identifier counter.
   * Needed to give an unique id to every proof node.
   */
  private static int idCounter = 1;
  
  /**
   * The unique id of this proof node
   */
  private int id;
  


  /**
   * An enumeration that is always empty. This is used when an enumeration of a
   * leaf node's children is requested.
   */
  @ SuppressWarnings ( "unchecked" )
  protected static final Enumeration EMPTY_ENUMERATION = new Enumeration ( )
  {
    public boolean hasMoreElements ( )
    {
      return false ;
    }


    public Object nextElement ( )
    {
      throw new NoSuchElementException ( "No more elements" ) ; //$NON-NLS-1$
    }
  } ;


  //
  // Attributes
  //
  /**
   * The child nodes of this node, or <code>null</code> if this node has no
   * children.
   * 
   * @see #children()
   * @see #getChildAt(int)
   * @see #getChildCount()
   */
  protected Vector < AbstractProofNode > children ;


  /**
   * The parent node of this node, or <code>null</code> if this is the root
   * node of a proof tree.
   * 
   * @see #getParent()
   * @see #setParent(AbstractProofNode)
   */
  protected AbstractProofNode parent ;


  /**
   * The rules that have been applied to this proof node so far, or
   * <code>null</code> if no rules have been applied yet.
   * 
   * @see #getRules()
   * @see #setRules(ProofRule[])
   */
  private ProofRule [ ] rules ;


  /**
   * The user object associated with this {@link ProofNode}.
   * 
   * @see #getUserObject()
   * @see #setUserObject(Object)
   */
  private transient Object userObject ;


  //
  // Constructor
  //
  /**
   * Allocates a new {@link AbstractProofNode} without any children and no link
   * to a parent.
   */
  protected AbstractProofNode ( )
  {
    this.rules = EMPTY_ARRAY ;
    this.id = AbstractProofNode.idCounter++;
  }


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getRules()
   */
  public ProofRule [ ] getRules ( )
  {
    return this.rules ;
  }


  /**
   * Sets the {@link ProofRule}s that have been applied to this node so far to
   * the specified <code>rules</code>. If <code>rules</code> is
   * <code>null</code> it is assumed that no rules have been applied yet.
   * 
   * @param pRules the new proof rules for this node.
   */
  public void setRules ( ProofRule [ ] pRules )
  {
    this.rules = ( pRules != null ) ? pRules : EMPTY_ARRAY ;
  }


  /**
   * Creates and returns a forward-order enumeration of this node's children.
   * Modifying this node's child array invalidates any child enumerations
   * created before the modification.
   * 
   * @return an {@link Enumeration} of this node's children
   * @see javax.swing.tree.TreeNode#children()
   */
  @ SuppressWarnings ( "unchecked" )
  public Enumeration children ( )
  {
    if ( this.children == null )
    {
      return EMPTY_ENUMERATION ;
    }
    return this.children.elements ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#getChildAt(int)
   */
  public AbstractProofNode getChildAt ( int childIndex )
  {
    if ( this.children == null )
    {
      throw new ArrayIndexOutOfBoundsException ( "node has no children" ) ; //$NON-NLS-1$
    }
    return this.children.elementAt ( childIndex ) ;
  }


  /**
   * Returns the number of children of this node.
   * 
   * @return an integer giving the number of children of this node.
   * @see javax.swing.tree.TreeNode#getChildCount()
   */
  public int getChildCount ( )
  {
    if ( this.children == null )
    {
      return 0 ;
    }
    return this.children.size ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#getParent()
   */
  public AbstractProofNode getParent ( )
  {
    return this.parent ;
  }


  /**
   * Sets this node's parent to <code>parent</code> but does not change the
   * <code>parent</code>'s child array. This method is called from the
   * {@link #insert(AbstractProofNode, int)} and
   * {@link #remove(AbstractProofNode)} methods toreassign a child's parent, it
   * should not be messaged from anywhere else.
   * 
   * @param pParent this node's new parent.
   */
  public void setParent ( AbstractProofNode pParent )
  {
    this.parent = pParent ;
  }


  /**
   * Returns the index of the specified child in this node's child array. If the
   * specified node is not a child of this node, returns <code>-1</code>.
   * This method performs a linear search and is O(n) where n is the number of
   * children.
   * 
   * @param aChild the {@link TreeNode} to search for among this node's
   *          children.
   * @return an integer giving the index of the node in this node's child array,
   *         or <code>-1</code> if the specified node is a not a child of this
   *         node.
   * @throws IllegalArgumentException if <code>aChild</code> is null.
   * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
   */
  public int getIndex ( TreeNode aChild )
  {
    if ( aChild == null )
    {
      throw new IllegalArgumentException ( "argument is null" ) ; //$NON-NLS-1$
    }
    if ( ! isNodeChild ( aChild ) )
    {
      return - 1 ;
    }
    return this.children.indexOf ( aChild ) ;
  }


  /**
   * Returns <code>true</code> if this node is allowed to have children.
   * 
   * @return <code>true</code> if this node allows children, else
   *         <code>false</code>.
   * @see javax.swing.tree.TreeNode#getAllowsChildren()
   */
  public boolean getAllowsChildren ( )
  {
    return true ;
  }


  //
  // User Objects
  //
  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#getUserObject()
   */
  public Object getUserObject ( )
  {
    return this.userObject ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#setUserObject(java.lang.Object)
   */
  public void setUserObject ( Object pUserObject )
  {
    this.userObject = pUserObject ;
  }


  //
  // Tree Queries
  //
  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#isNodeAncestor(TreeNode)
   */
  public boolean isNodeAncestor ( TreeNode anotherNode )
  {
    if ( anotherNode == null )
    {
      return false ;
    }
    TreeNode ancestor = this ;
    do
    {
      if ( ancestor == anotherNode )
      {
        return true ;
      }
    }
    while ( ( ancestor = ancestor.getParent ( ) ) != null ) ;
    return false ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#isNodeDescendant(ProofNode)
   */
  public boolean isNodeDescendant ( ProofNode anotherNode )
  {
    if ( anotherNode == null ) return false ;
    return anotherNode.isNodeAncestor ( this ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#isNodeRelated(ProofNode)
   */
  public boolean isNodeRelated ( ProofNode aNode )
  {
    return ( aNode != null ) && ( getRoot ( ) == aNode.getRoot ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#getRoot()
   */
  public AbstractProofNode getRoot ( )
  {
    AbstractProofNode ancestor = this ;
    AbstractProofNode previous ;
    do
    {
      previous = ancestor ;
      ancestor = ancestor.getParent ( ) ;
    }
    while ( ancestor != null ) ;
    return previous ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#isRoot()
   */
  public boolean isRoot ( )
  {
    return ( getParent ( ) == null ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see PostorderEnumeration
   * @see ProofNode#postorderEnumeration()
   */
  public Enumeration < ProofNode > postorderEnumeration ( )
  {
    return new PostorderEnumeration ( this ) ;
  }


  //
  // Child Queries
  //
  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#isNodeChild(TreeNode)
   */
  public boolean isNodeChild ( TreeNode aNode )
  {
    boolean retval ;
    if ( aNode == null )
    {
      retval = false ;
    }
    else
    {
      if ( getChildCount ( ) == 0 )
      {
        retval = false ;
      }
      else
      {
        retval = ( aNode.getParent ( ) == this ) ;
      }
    }
    return retval ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#getFirstChild()
   */
  public AbstractProofNode getFirstChild ( )
  {
    if ( getChildCount ( ) == 0 )
    {
      throw new NoSuchElementException ( "node has no children" ) ; //$NON-NLS-1$
    }
    return getChildAt ( 0 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#getLastChild()
   */
  public AbstractProofNode getLastChild ( )
  {
    if ( getChildCount ( ) == 0 )
    {
      throw new NoSuchElementException ( "node has no children" ) ; //$NON-NLS-1$
    }
    return getChildAt ( getChildCount ( ) - 1 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#getChildAfter(TreeNode)
   */
  public AbstractProofNode getChildAfter ( TreeNode aChild )
  {
    if ( aChild == null )
    {
      throw new IllegalArgumentException ( "argument is null" ) ; //$NON-NLS-1$
    }
    int index = getIndex ( aChild ) ;
    if ( index == - 1 )
    {
      throw new IllegalArgumentException ( "node is not a child" ) ; //$NON-NLS-1$
    }
    if ( index < getChildCount ( ) - 1 )
    {
      return getChildAt ( index + 1 ) ;
    }
    return null ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#getChildBefore(TreeNode)
   */
  public AbstractProofNode getChildBefore ( TreeNode aChild )
  {
    if ( aChild == null )
    {
      throw new IllegalArgumentException ( "argument is null" ) ; //$NON-NLS-1$
    }
    int index = getIndex ( aChild ) ;
    if ( index == - 1 )
    {
      throw new IllegalArgumentException ( "argument is not a child" ) ; //$NON-NLS-1$
    }
    if ( index > 0 )
    {
      return getChildAt ( index - 1 ) ;
    }
    return null ;
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
   * @see #getAllowsChildren()
   * @see javax.swing.tree.TreeNode#isLeaf()
   */
  public boolean isLeaf ( )
  {
    return ( getChildCount ( ) == 0 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#getFirstLeaf()
   */
  public AbstractProofNode getFirstLeaf ( )
  {
    AbstractProofNode node = this ;
    while ( ! node.isLeaf ( ) )
    {
      node = node.getFirstChild ( ) ;
    }
    return node ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#getLastLeaf()
   */
  public AbstractProofNode getLastLeaf ( )
  {
    AbstractProofNode node = this ;
    while ( ! node.isLeaf ( ) )
    {
      node = node.getLastChild ( ) ;
    }
    return node ;
  }


  //
  // Insertion / Removal
  //
  /**
   * Removes <code>newChild</code> from its parent and makes it a child of
   * this node by adding it to the end of this node's child array.
   * 
   * @param newChild node to add as a child of this node.
   * @throws IllegalArgumentException if <code>newChild</code> is
   *           <code>null</code>.
   * @see #insert(AbstractProofNode, int)
   */
  public void add ( AbstractProofNode newChild )
  {
    if ( newChild != null && newChild.getParent ( ) == this )
    {
      insert ( newChild , getChildCount ( ) - 1 ) ;
    }
    else
    {
      insert ( newChild , getChildCount ( ) ) ;
    }
  }


  /**
   * Removes <code>newChild</code> from its present parent (if it has a
   * parent), sets the child's parent to this node, and then adds the child to
   * this node's child array at index <code>childIndex</code>.
   * <code>newChild</code> must not be <code>null</code> and must not be an
   * ancestor of this node.
   * 
   * @param newChild the {@link ProofNode} to insert under this node.
   * @param childIndex the index in this node's child array where this node is
   *          to be inserted.
   * @throw ArrayIndexOutOfBoundsException if <code>childIndex</code> is out
   *        of bounds.
   * @throws IllegalArgumentException if <code>newChild</code> is null or is
   *           an ancestor of this node.
   * @see #isNodeDescendant(ProofNode)
   */
  public void insert ( AbstractProofNode newChild , int childIndex )
  {
    if ( newChild == null )
    {
      throw new IllegalArgumentException ( "new child is null" ) ; //$NON-NLS-1$
    }
    else if ( isNodeAncestor ( newChild ) )
    {
      throw new IllegalArgumentException ( "new child is an ancestor" ) ; //$NON-NLS-1$
    }
    // unlink from the old parent
    AbstractProofNode oldParent = newChild.getParent ( ) ;
    if ( oldParent != null )
    {
      oldParent.remove ( newChild ) ;
    }
    // link to the new parent
    newChild.setParent ( this ) ;
    // allocate the list of children on demand
    if ( this.children == null )
    {
      this.children = new Vector < AbstractProofNode > ( ) ;
    }
    // add to the list of children
    this.children.insertElementAt ( newChild , childIndex ) ;
  }


  /**
   * Removes the child at the specified index from this node's children and sets
   * that node's parent to <code>null</code>. The child node to remove must
   * be a <code>ProofNode</code>.
   * 
   * @param childIndex the index in this node's child array of the child to
   *          remove.
   * @throws ArrayIndexOutOfBoundsException if <code>childIndex</code> is out
   *           of bounds.
   */
  public void remove ( int childIndex )
  {
    AbstractProofNode child = getChildAt ( childIndex ) ;
    this.children.removeElementAt ( childIndex ) ;
    child.setParent ( null ) ;
  }


  /**
   * Removes <code>aChild</code> from this node's child array, giving it a
   * <code>null</code> parent.
   * 
   * @param aChild a child of this node to remove.
   * @throws IllegalArgumentException if <code>aChild</code> is
   *           <code>null</code> or is not a child of this node.
   */
  public void remove ( AbstractProofNode aChild )
  {
    if ( aChild == null )
    {
      throw new IllegalArgumentException ( "argument is null" ) ; //$NON-NLS-1$
    }
    if ( ! isNodeChild ( aChild ) )
    {
      throw new IllegalArgumentException ( "argument is not a child" ) ; //$NON-NLS-1$
    }
    remove ( getIndex ( aChild ) ) ;
  }


  /**
   * Removes all of this node's children, setting their parents to
   * <code>null</code>. If this node has no children, this method does
   * nothing.
   */
  public void removeAllChildren ( )
  {
    for ( int i = getChildCount ( ) - 1 ; i >= 0 ; -- i )
    {
      remove ( i ) ;
    }
  }


  /**
   * Removes the subtree rooted at this node from the tree, giving this node a
   * <code>null</code> parent. Does nothing if this node is the root of its
   * tree.
   */
  public void removeFromParent ( )
  {
    AbstractProofNode tmpParent = getParent ( ) ;
    if ( tmpParent != null )
    {
      tmpParent.remove ( this ) ;
    }
  }


  //
  // Internal classes
  //
  /**
   * Implementation class for the
   * {@link AbstractProofNode#postorderEnumeration()} method, which enumerates
   * the nodes below a given abstract proof node in post-order.
   * 
   * @see AbstractProofNode#postorderEnumeration()
   */
  private static final class PostorderEnumeration implements
      Enumeration < ProofNode >
  {
    /**
     * The root.
     */
    protected AbstractProofNode root ;


    /**
     * The children.
     */
    @ SuppressWarnings ( "unchecked" )
    protected Enumeration children ;


    /**
     * the sub tree.
     */
    @ SuppressWarnings ( "unchecked" )
    protected Enumeration subtree ;


    /**
     * Initializes the PostorderEnumeration.
     * 
     * @param pRoot The root node.
     */
    PostorderEnumeration ( AbstractProofNode pRoot )
    {
      super ( ) ;
      this.root = pRoot ;
      this.children = pRoot.children ( ) ;
      this.subtree = EMPTY_ENUMERATION ;
    }


    /**
     * Returns true, if the enumeration has more elements.
     * 
     * @return True, if the enumeration has more elements.
     * @see java.util.Enumeration#hasMoreElements()
     */
    public boolean hasMoreElements ( )
    {
      return ( this.root != null ) ;
    }


    /**
     * Returns the next AbstractProofNode.
     * 
     * @return The next AbstractProofNode.
     * @see java.util.Enumeration#nextElement()
     */
    public AbstractProofNode nextElement ( )
    {
      AbstractProofNode node ;
      if ( this.subtree.hasMoreElements ( ) )
      {
        node = ( AbstractProofNode ) this.subtree.nextElement ( ) ;
      }
      else if ( this.children.hasMoreElements ( ) )
      {
        this.subtree = new PostorderEnumeration (
            ( AbstractProofNode ) this.children.nextElement ( ) ) ;
        node = ( AbstractProofNode ) this.subtree.nextElement ( ) ;
      }
      else
      {
        node = this.root ;
        this.root = null ;
      }
      return node ;
    }
  }

/**
 * 
 * Get the unique id of this proof node
 *
 * @return int unique id
 */
public int getId ( ) {
	return this.id;
}
}
