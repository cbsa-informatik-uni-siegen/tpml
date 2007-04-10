package de.unisiegen.tpml.graphics.outline.node ;


import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineBinding ;


/**
 * Cache of the caption of a node.
 * 
 * @author Christian Fehler
 */
public class OutlineNodeCache
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
   * Unbound {@link Identifier}s should be highlighted in all nodes.
   */
  private boolean unbound ;


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
  private int boundedStart ;


  /**
   * The end index of the {@link Identifier}.
   */
  private int boundedEnd ;


  /**
   * The break count.
   */
  private int breakCount ;


  /**
   * The {@link OutlineBinding}.
   * 
   * @see #getOutlineBinding()
   */
  private OutlineBinding outlineBinding ;


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
   * @param pUnbound Unbound {@link Identifier}s should be highlighted in all
   *          nodes.
   * @param pReplace The selected {@link Expression} should be replaced in
   *          higher nodes.
   * @param pBoundedStart The start index of the {@link Identifier}.
   * @param pBoundedEnd The end index of the {@link Identifier}.
   * @param pBreakCount The break count.
   * @param pOutlineBinding The {@link OutlineBinding}.
   * @param pCaption The caption of the node.
   */
  public OutlineNodeCache ( int pSelectionStart , int pSelectionEnd ,
      boolean pSelection , boolean pBinding , boolean pUnbound ,
      boolean pReplace , int pBoundedStart , int pBoundedEnd , int pBreakCount ,
      OutlineBinding pOutlineBinding , String pCaption )
  {
    this.selectionStart = pSelectionStart ;
    this.selectionEnd = pSelectionEnd ;
    this.selection = pSelection ;
    this.binding = pBinding ;
    this.unbound = pUnbound ;
    this.replace = pReplace ;
    this.boundedStart = pBoundedStart ;
    this.boundedEnd = pBoundedEnd ;
    this.breakCount = pBreakCount ;
    this.outlineBinding = pOutlineBinding ;
    this.caption = pCaption ;
  }


  /**
   * Returns the end index of the {@link Identifier}.
   * 
   * @return The end index of the {@link Identifier}..
   * @see #boundedEnd
   */
  public int getBoundedEnd ( )
  {
    return this.boundedEnd ;
  }


  /**
   * Returns the start index of the {@link Identifier}.
   * 
   * @return The start index of the {@link Identifier}..
   * @see #boundedStart
   */
  public int getBoundedStart ( )
  {
    return this.boundedStart ;
  }


  /**
   * Returns the breakCount.
   * 
   * @return The breakCount.
   * @see #breakCount
   */
  public int getBreakCount ( )
  {
    return this.breakCount ;
  }


  /**
   * Returns the caption.
   * 
   * @return The caption.
   * @see #caption
   */
  public String getCaption ( )
  {
    return this.caption ;
  }


  /**
   * Returns the {@link OutlineBinding}.
   * 
   * @return The {@link OutlineBinding}.
   * @see #outlineBinding
   */
  public final OutlineBinding getOutlineBinding ( )
  {
    return this.outlineBinding ;
  }


  /**
   * Returns the selectionEnd.
   * 
   * @return The selectionEnd.
   * @see #selectionEnd
   */
  public int getSelectionEnd ( )
  {
    return this.selectionEnd ;
  }


  /**
   * Returns the selectionStart.
   * 
   * @return The selectionStart.
   * @see #selectionStart
   */
  public int getSelectionStart ( )
  {
    return this.selectionStart ;
  }


  /**
   * Returns the binding.
   * 
   * @return The binding.
   * @see #binding
   */
  public boolean isBinding ( )
  {
    return this.binding ;
  }


  /**
   * Returns the replace.
   * 
   * @return The replace.
   * @see #replace
   */
  public boolean isReplace ( )
  {
    return this.replace ;
  }


  /**
   * Returns the selection.
   * 
   * @return The selection.
   * @see #selection
   */
  public boolean isSelection ( )
  {
    return this.selection ;
  }


  /**
   * Returns the unbound.
   * 
   * @return The unbound.
   * @see #unbound
   */
  public boolean isUnbound ( )
  {
    return this.unbound ;
  }
}
