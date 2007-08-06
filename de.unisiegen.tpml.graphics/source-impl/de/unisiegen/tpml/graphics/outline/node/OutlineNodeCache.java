package de.unisiegen.tpml.graphics.outline.node ;


import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineBinding ;


/**
 * Cache of the caption of a node.
 * 
 * @author Christian Fehler
 */
public final class OutlineNodeCache
{
  /**
   * The selected {@link Expression} should be highlighted in higher nodes.
   */
  private boolean selection ;


  /**
   * The selected {@link Expression} should be replaced in higher nodes.
   */
  private boolean replace ;


  /**
   * Selected {@link Identifier} and bindings should be highlighted in higher
   * nodes.
   */
  private boolean binding ;


  /**
   * Free {@link Identifier}s should be highlighted in all nodes.
   */
  private boolean free ;


  /**
   * The start offset of the selection in this node.
   */
  private int selectionStart ;


  /**
   * The end offset of the selection in this node.
   */
  private int selectionEnd ;


  /**
   * The start index of the {@link Identifier}.
   */
  private int boundStart ;


  /**
   * The end index of the {@link Identifier}.
   */
  private int boundEnd ;


  /**
   * The break count.
   */
  private int breakCount ;


  /**
   * The {@link OutlineBinding}.
   * 
   * @see #getOutlineBinding()
   */
  private OutlineBinding < ? > outlineBinding ;


  /**
   * The caption of the node.
   */
  private String caption ;


  /**
   * Initializes the <code>OutlineNodeCache</code>.
   * 
   * @param pSelectionStart The start offset of the selection in this node.
   * @param pSelectionEnd The end offset of the selection in this node.
   * @param pSelection The selected {@link Expression} should be highlighted in
   *          higher nodes.
   * @param pBinding Selected {@link Identifier} and bindings should be
   *          highlighted in higher nodes.
   * @param pFree Unbound {@link Identifier}s should be highlighted in all
   *          nodes.
   * @param pReplace The selected {@link Expression} should be replaced in
   *          higher nodes.
   * @param pBoundStart The start index of the {@link Identifier}.
   * @param pBoundEnd The end index of the {@link Identifier}.
   * @param pBreakCount The break count.
   * @param pOutlineBinding The {@link OutlineBinding}.
   * @param pCaption The caption of the node.
   */
  public OutlineNodeCache ( int pSelectionStart , int pSelectionEnd ,
      boolean pSelection , boolean pBinding , boolean pFree , boolean pReplace ,
      int pBoundStart , int pBoundEnd , int pBreakCount ,
      OutlineBinding < ? > pOutlineBinding , String pCaption )
  {
    this.selectionStart = pSelectionStart ;
    this.selectionEnd = pSelectionEnd ;
    this.selection = pSelection ;
    this.binding = pBinding ;
    this.free = pFree ;
    this.replace = pReplace ;
    this.boundStart = pBoundStart ;
    this.boundEnd = pBoundEnd ;
    this.breakCount = pBreakCount ;
    this.outlineBinding = pOutlineBinding ;
    this.caption = pCaption ;
  }


  /**
   * Returns the end index of the {@link Identifier}.
   * 
   * @return The end index of the {@link Identifier}..
   * @see #boundEnd
   */
  public final int getBoundEnd ( )
  {
    return this.boundEnd ;
  }


  /**
   * Returns the start index of the {@link Identifier}.
   * 
   * @return The start index of the {@link Identifier}..
   * @see #boundStart
   */
  public final int getBoundStart ( )
  {
    return this.boundStart ;
  }


  /**
   * Returns the breakCount.
   * 
   * @return The breakCount.
   * @see #breakCount
   */
  public final int getBreakCount ( )
  {
    return this.breakCount ;
  }


  /**
   * Returns the caption.
   * 
   * @return The caption.
   * @see #caption
   */
  public final String getCaption ( )
  {
    return this.caption ;
  }


  /**
   * Returns the {@link OutlineBinding}.
   * 
   * @return The {@link OutlineBinding}.
   * @see #outlineBinding
   */
  public final OutlineBinding < ? > getOutlineBinding ( )
  {
    return this.outlineBinding ;
  }


  /**
   * Returns the selectionEnd.
   * 
   * @return The selectionEnd.
   * @see #selectionEnd
   */
  public final int getSelectionEnd ( )
  {
    return this.selectionEnd ;
  }


  /**
   * Returns the selectionStart.
   * 
   * @return The selectionStart.
   * @see #selectionStart
   */
  public final int getSelectionStart ( )
  {
    return this.selectionStart ;
  }


  /**
   * Returns the binding.
   * 
   * @return The binding.
   * @see #binding
   */
  public final boolean isBinding ( )
  {
    return this.binding ;
  }


  /**
   * Returns the free.
   * 
   * @return The free.
   * @see #free
   */
  public final boolean isFree ( )
  {
    return this.free ;
  }


  /**
   * Returns the replace.
   * 
   * @return The replace.
   * @see #replace
   */
  public final boolean isReplace ( )
  {
    return this.replace ;
  }


  /**
   * Returns the selection.
   * 
   * @return The selection.
   * @see #selection
   */
  public final boolean isSelection ( )
  {
    return this.selection ;
  }
}
