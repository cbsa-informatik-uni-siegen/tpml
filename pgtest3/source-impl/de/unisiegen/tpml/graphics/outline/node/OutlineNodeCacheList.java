package de.unisiegen.tpml.graphics.outline.node ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineBinding ;


/**
 * List of the {@link OutlineNodeCache}.
 * 
 * @author Christian Fehler
 */
public final class OutlineNodeCacheList
{
  /**
   * The list of {@link OutlineNodeCache}.
   */
  private ArrayList < OutlineNodeCache > list ;


  /**
   * Initializes the <code>OutlineNodeCacheList</code>.
   */
  public OutlineNodeCacheList ( )
  {
    this.list = new ArrayList < OutlineNodeCache > ( ) ;
  }


  /**
   * Adds a new {@link OutlineNodeCache}.
   * 
   * @param pOutlineNodeCache The new {@link OutlineNodeCache}.
   */
  public final void add ( OutlineNodeCache pOutlineNodeCache )
  {
    this.list.add ( pOutlineNodeCache ) ;
  }


  /**
   * Removes all of the elements from this list. The list will be empty after
   * this call returns.
   */
  public final void clear ( )
  {
    this.list.clear ( ) ;
  }


  /**
   * Returns the cached caption, or <code>null</code> if it was not cached.
   * 
   * @param pSelectionStart The start offset of the selection in this node.
   * @param pSelectionEnd The end offset of the selection in this node.
   * @param pSelection The selected {@link Expression} should be highlighted in
   *          higher nodes.
   * @param pBinding Selected {@link Identifier} and bindings should be
   *          highlighted in higher nodes.
   * @param pFree Free {@link Identifier}s should be highlighted in all nodes.
   * @param pReplace The selected {@link Expression} should be replaced in
   *          higher nodes.
   * @param pBoundedStart The start index of the {@link Identifier}.
   * @param pBoundedEnd The end index of the {@link Identifier}.
   * @param pBreakCount The break count.
   * @param pOutlineBinding The {@link OutlineBinding}.
   * @return The cached caption, or <code>null</code> if it was not cached.
   */
  public final String getCaption ( int pSelectionStart , int pSelectionEnd ,
      boolean pSelection , boolean pBinding , boolean pFree , boolean pReplace ,
      int pBoundedStart , int pBoundedEnd , int pBreakCount ,
      OutlineBinding pOutlineBinding )
  {
    for ( OutlineNodeCache current : this.list )
    {
      if ( ( current.getSelectionStart ( ) == pSelectionStart )
          && ( current.getSelectionEnd ( ) == pSelectionEnd )
          && ( current.isSelection ( ) == pSelection )
          && ( current.isBinding ( ) == pBinding )
          && ( current.isFree ( ) == pFree )
          && ( current.isReplace ( ) == pReplace )
          && ( current.getBoundedStart ( ) == pBoundedStart )
          && ( current.getBoundedEnd ( ) == pBoundedEnd )
          && ( current.getBreakCount ( ) == pBreakCount )
          && ( ( current.getOutlineBinding ( ) == null ) ? ( pOutlineBinding == null )
              : current.getOutlineBinding ( ).equals ( pOutlineBinding ) ) )
      {
        return current.getCaption ( ) ;
      }
    }
    return null ;
  }
}
