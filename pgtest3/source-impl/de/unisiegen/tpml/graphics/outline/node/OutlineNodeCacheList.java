package de.unisiegen.tpml.graphics.outline.node ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;


/**
 * List of the {@link OutlineNodeCache}.
 * 
 * @author Christian Fehler
 */
public class OutlineNodeCacheList
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
  public void add ( OutlineNodeCache pOutlineNodeCache )
  {
    this.list.add ( pOutlineNodeCache ) ;
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
   * @param pUnbound Unbound {@link Identifier}s should be highlighted in all
   *          nodes.
   * @param pReplace The selected {@link Expression} should be replaced in
   *          higher nodes.
   * @param pBreakCount The break count.
   * @return The cached caption, or <code>null</code> if it was not cached.
   */
  public String getCaption ( int pSelectionStart , int pSelectionEnd ,
      boolean pSelection , boolean pBinding , boolean pUnbound ,
      boolean pReplace , int pBreakCount )
  {
    for ( OutlineNodeCache current : this.list )
    {
      if ( ( current.getSelectionStart ( ) == pSelectionStart )
          && ( current.getSelectionEnd ( ) == pSelectionEnd )
          && ( current.isSelection ( ) == pSelection )
          && ( current.isBinding ( ) == pBinding )
          && ( current.isUnbound ( ) == pUnbound )
          && ( current.isReplace ( ) == pReplace )
          && ( current.getBreakCount ( ) == pBreakCount ) )
      {
        return current.getCaption ( ) ;
      }
    }
    return null ;
  }
}
