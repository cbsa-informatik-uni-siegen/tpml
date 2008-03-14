package de.unisiegen.tpml.core.util;


import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * This is a decorator class for the {@link org.w3c.dom.NodeList} class, which
 * represents a list of {@link org.w3c.dom.Node}s below a parent node in a DOM
 * {@link org.w3c.dom.Document}. Using this class one can easily iterate over
 * the list of child nodes below a given document node. For example the
 * following code fragment
 * 
 * <pre>
 * NodeList childNodes = parent.getChildNodes ();
 * for ( int i = 0 ; i &lt; childNodes ; ++i )
 * {
 *   Node node = childNodes.item ( i );
 *   // process the node...
 * }
 * </pre>
 * 
 * can be transform into
 * 
 * <pre>
 * for ( Node node : new IterableNodeList ( parent ) )
 * {
 *   // process the node...
 * }
 * </pre>
 * 
 * which is not only shorter, but also easier to understand.
 * 
 * @author Benedikt Meurer
 * @version $Id$
 * @see org.w3c.dom.Node
 * @see org.w3c.dom.NodeList
 */
public final class IterableNodeList implements Iterable < Node >, NodeList
{

  /**
   * Allocates a new <code>IterableNodeList</code>, that decorates the
   * specified <code>NodeList</code> and provides an implementation of the
   * <code>Iterable</code>, so developers no longer need to write ugly code
   * to iterate over node lists, but can use the Java5 <code>foreach</code>
   * construct.
   * 
   * @param pDecorated the <code>NodeList</code> to be decorated by this
   *          <code>IterableNodeList</code> instance.
   */
  public IterableNodeList ( NodeList pDecorated )
  {
    this.decorated = pDecorated;
  }


  /**
   * Convenience wrapper for the default constructor
   * {@link #IterableNodeList(NodeList)}, which determines the child nodes for
   * <code>parent</code> and decorates the child nodes list.
   * 
   * @param parent the parent <code>Node</code>, whose child nodes should be
   *          decorated by this <code>IterableNodeList</code> instance.
   * @see #IterableNodeList(NodeList)
   */
  public IterableNodeList ( Node parent )
  {
    this ( parent.getChildNodes () );
  }


  /**
   * Returns an iterator that can be used to iterate over the <code>Node</code>s
   * within the decorated <code>NodeList</code>.
   * 
   * @return an iterator for the decorated <code>NodeList</code>.
   * @see java.lang.Iterable#iterator()
   */
  @SuppressWarnings ( "synthetic-access" )
  public Iterator < Node > iterator ()
  {
    return new NodeListIterator ();
  }


  /**
   * Returns the <code>Node</code> at the given <code>index</code>.
   * 
   * @param index the position of the <code>Node</code> in the list.
   * @return the <code>Node</code> at <code>index</code> in the list, or
   *         <code>null</code> if the <code>index</code> points outside the
   *         list.
   * @see org.w3c.dom.NodeList#item(int)
   */
  public Node item ( int index )
  {
    return this.decorated.item ( index );
  }


  /**
   * Returns the number of <code>Node</code>s in the list.
   * 
   * @return the number of <code>Node</code>s in the list.
   * @see org.w3c.dom.NodeList#getLength()
   */
  public int getLength ()
  {
    return this.decorated.getLength ();
  }


  /**
   * The {@link NodeList}.
   */
  // member attributes
  private NodeList decorated;


  /**
   * The {@link NodeList} iterator.
   * 
   * @author Benedikt Meurer
   */
  // iterator implementation
  private class NodeListIterator implements Iterator < Node >
  {

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext ()
    {
      return ( this.index < getLength () - 1 );
    }


    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#next()
     */
    public Node next ()
    {
      // advance to the next item
      this.index += 1;
      // determine the node for that index
      Node node = item ( this.index );
      if ( node != null )
        return node;
      // the index is invalid
      throw new NoSuchElementException ();
    }


    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#remove()
     */
    public void remove ()
    {
      throw new UnsupportedOperationException ();
    }


    /**
     * The index of this iterator.
     */
    private int index = -1;
  }
}
