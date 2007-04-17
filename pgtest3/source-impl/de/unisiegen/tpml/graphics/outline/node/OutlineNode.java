package de.unisiegen.tpml.graphics.outline.node ;


import java.awt.Color ;
import javax.swing.tree.DefaultMutableTreeNode ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle ;
import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.graphics.Theme ;
import de.unisiegen.tpml.graphics.outline.Outline ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineBinding ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineUnbound ;


/**
 * This class represents the nodes in the {@link Outline}.
 * 
 * @author Christian Fehler
 * @version $Rev: 1075 $
 */
public final class OutlineNode extends DefaultMutableTreeNode
{
  /**
   * The serial version UID.
   */
  private static final long serialVersionUID = 1611765250060174993L ;


  /**
   * The {@link Expression} should not be shown in this nodes.
   */
  private static final int NO_SELECTION = - 1 ;


  /**
   * Bindings should not be highlighted in higher nodes.
   */
  public static final int NO_BINDING = - 1 ;


  /**
   * The {@link Expression} has no child index.
   */
  public static final int NO_CHILD_INDEX = - 1 ;


  /**
   * The {@link Type} has no child types.
   */
  public static final int NOTHING_FOUND = - 2 ;


  /**
   * The selected {@link Expression} should be highlighted in higher nodes.
   * 
   * @see #setSelection(boolean)
   */
  private static boolean selection = true ;


  /**
   * The selected {@link Expression} should be replaced in higher nodes.
   * 
   * @see #setReplace(boolean)
   */
  private static boolean replace = true ;


  /**
   * Selected {@link Identifier} and bindings should be highlighted in higher
   * nodes.
   * 
   * @see #setBinding(boolean)
   */
  private static boolean binding = true ;


  /**
   * Unbound {@link Identifier}s should be highlighted in all nodes.
   * 
   * @see #setUnbound(boolean)
   */
  private static boolean unbound = true ;


  /**
   * The hex values.
   */
  private static final String [ ] HEX_VALUES =
  { "0" , "1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9" , "A" , "B" , //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$
      "C" , "D" , "E" , "F" , "00" } ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$


  /**
   * Lower than replace.
   */
  private static final String LOWER_THAN_REPLACE = "&lt" ; //$NON-NLS-1$


  /**
   * Greater than replace.
   */
  private static final String GREATER_THAN_REPLACE = "&gt" ; //$NON-NLS-1$


  /**
   * Ampersand replace.
   */
  private static final String AMPERSAND_THAN_REPLACE = "&amp" ; //$NON-NLS-1$


  /**
   * The end of the caption.
   */
  private static final String EXPRESSION_END = "</font>&nbsp;]</html>" ; //$NON-NLS-1$


  /**
   * String, if a {@link Expression} should be replaced and selection is active.
   */
  private static final String REPLACE_BOLD = "<b>&nbsp;...&nbsp;</b>" ; //$NON-NLS-1$


  /**
   * String, if a {@link Expression} should be replaced.
   */
  private static final String REPLACE = "&nbsp;...&nbsp;" ; //$NON-NLS-1$


  /**
   * String, between child index and description.
   */
  private static final String BETWEEN1 = "&nbsp;-&nbsp;" ; //$NON-NLS-1$


  /**
   * String, between description and {@link Expression}.
   */
  private static final String BETWEEN2 = "&nbsp;&nbsp;&nbsp;[&nbsp;" ; //$NON-NLS-1$


  /**
   * Begin of HTML.
   */
  private static final String HTML = "<html>" ; //$NON-NLS-1$


  /**
   * Begin of the smaller sub text.
   */
  private static final String SMALL_SUB_BEGIN = "<small><sub>" ; //$NON-NLS-1$


  /**
   * End of the smaller sub text.
   */
  private static final String SMALL_SUB_END = "</sub></small>" ; //$NON-NLS-1$


  /**
   * Before the description.
   */
  private static final String DESCRIPTION_BEGIN = "<font color=\"#0000FF\">" ; //$NON-NLS-1$


  /**
   * After the description.
   */
  private static final String DESCRIPTION_END = "</font>" ; //$NON-NLS-1$


  /**
   * The beginning of the caption.
   */
  private static final String FONT_BEGIN = "<font color=\"#" ; //$NON-NLS-1$


  /**
   * End of font.
   */
  private static final String FONT_END = "</font>" ; //$NON-NLS-1$


  /**
   * After the color.
   */
  private static final String FONT_AFTER_COLOR = "\">" ; //$NON-NLS-1$


  /**
   * String for a break.
   */
  private static final String BREAK = "<br>" ; //$NON-NLS-1$


  /**
   * Begin of font and bold.
   */
  private static final String FONT_BOLD_BEGIN = "<b><font color=\"#" ; //$NON-NLS-1$


  /**
   * End of font and bold.
   */
  private static final String FONT_BOLD_END = "</font></b>" ; //$NON-NLS-1$


  /**
   * Returns the hex value of a given integer.
   * 
   * @param pNumber The input integer value.
   * @return The hex value of a given integer.
   */
  private static final String getHex ( final int pNumber )
  {
    final StringBuffer result = new StringBuffer ( ) ;
    int remainder = Math.abs ( pNumber ) ;
    int base = 0 ;
    if ( remainder > 0 )
    {
      while ( remainder > 0 )
      {
        base = remainder % 16 ;
        remainder = ( remainder / 16 ) ;
        result.insert ( 0 , OutlineNode.HEX_VALUES [ base ] ) ;
      }
    }
    else
    {
      return OutlineNode.HEX_VALUES [ 16 ] ;
    }
    return result.toString ( ) ;
  }


  /**
   * Returns the color in HTML formatting.
   * 
   * @param pColor The color which should be returned.
   * @return The color in HTML formatting.
   */
  public static final String getHTMLColor ( final Color pColor )
  {
    return ( OutlineNode.getHex ( pColor.getRed ( ) )
        + OutlineNode.getHex ( pColor.getGreen ( ) ) + OutlineNode
        .getHex ( pColor.getBlue ( ) ) ) ;
  }


  /**
   * Sets the binding value. Selected {@link Identifier} and bindings should be
   * highlighted in higher nodes.
   * 
   * @param pBinding Should or should not be highlighted.
   * @see #binding
   */
  public final static void setBinding ( final boolean pBinding )
  {
    OutlineNode.binding = pBinding ;
  }


  /**
   * Sets the replace value. The selected {@link Expression} should be replaced
   * in higher nodes.
   * 
   * @param pReplace Should or should not be replaced.
   * @see #replace
   */
  public final static void setReplace ( final boolean pReplace )
  {
    OutlineNode.replace = pReplace ;
  }


  /**
   * Sets the selection value. The selected {@link Expression} should be
   * highlighted in higher nodes.
   * 
   * @param pSelection Should or should not be highlighted.
   * @see #selection
   */
  public final static void setSelection ( final boolean pSelection )
  {
    OutlineNode.selection = pSelection ;
  }


  /**
   * Sets the unbound value. Unbound {@link Identifier}s should be highlighted
   * in all nodes.
   * 
   * @param pUnbound Should or should not be highlighted.
   * @see #unbound
   */
  public final static void setUnbound ( final boolean pUnbound )
  {
    OutlineNode.unbound = pUnbound ;
  }


  /**
   * The selected {@link Expression} should be replaced in this node.
   * 
   * @see #setReplaceInThisNode(boolean)
   */
  private boolean replaceInThisNode ;


  /**
   * The caption of the node.
   */
  private String caption ;


  /**
   * The child index of this {@link Expression}.
   */
  private String childIndex ;


  /**
   * The {@link Expression} or (@link Type} as a <code>String</code>.
   */
  private String prettyString ;


  /**
   * The hole caption in HTML format.
   * 
   * @see #getCaptionHTML()
   * @see #setCaptionHTML(String)
   */
  private String captionHTML ;


  /**
   * The {@link Expression} repressented by this node.
   */
  private Expression expression ;


  /**
   * The {@link Type} repressented by this node.
   */
  private Type type ;


  /**
   * The {@link OutlineBinding} in this node.
   * 
   * @see #getOutlineBinding()
   * @see #setOutlineBinding(OutlineBinding)
   */
  private OutlineBinding outlineBinding ;


  /**
   * The {@link OutlineUnbound} which repressents the unbound {@link Identifier}s
   * in all nodes.
   */
  private OutlineUnbound outlineUnbound ;


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
   * All breaks of this node.
   * 
   * @see #getOutlineBreak()
   */
  private OutlineBreak outlineBreak ;


  /**
   * The current breaks of this node.
   */
  private OutlineBreak currentOutlineBreak ;


  /**
   * Indicates, if this node is a {@link Expression}.
   */
  private boolean isExpression ;


  /**
   * Indicates, if this node is a {@link Type}.
   */
  private boolean isType ;


  /**
   * Indicates, if this node is a {@link Identifier}.
   */
  private boolean isIdentifier ;


  /**
   * The start index of the last selection.
   */
  private int lastSelectionStart ;


  /**
   * The end index of the last selection.
   */
  private int lastSelectionEnd ;


  /**
   * The {@link OutlineNodeCacheList}, which caches the caption.
   */
  private OutlineNodeCacheList outlineNodeCacheList ;


  /**
   * The expression color.
   */
  private String expressionColor ;


  /**
   * The keyword color.
   */
  private String keywordColor ;


  /**
   * The constant color.
   */
  private String constantColor ;


  /**
   * The type color.
   */
  private String typeColor ;


  /**
   * The selection color.
   */
  private String selectionColor ;


  /**
   * The binding color.
   */
  private String bindingColor ;


  /**
   * The unbound color.
   */
  private String unboundColor ;


  /**
   * This constructor initializes the values.
   */
  private OutlineNode ( )
  {
    this.expression = null ;
    this.type = null ;
    this.caption = null ;
    this.childIndex = "" ; //$NON-NLS-1$
    this.prettyString = null ;
    this.outlineBinding = null ;
    this.outlineUnbound = null ;
    this.replaceInThisNode = false ;
    this.outlineBreak = null ;
    this.currentOutlineBreak = null ;
    this.breakCount = 0 ;
    this.isExpression = false ;
    this.isIdentifier = false ;
    this.isType = false ;
    this.boundedStart = OutlineNode.NO_BINDING ;
    this.boundedEnd = OutlineNode.NO_BINDING ;
    this.lastSelectionStart = OutlineNode.NO_SELECTION ;
    this.lastSelectionEnd = OutlineNode.NO_SELECTION ;
    this.outlineNodeCacheList = new OutlineNodeCacheList ( ) ;
    this.propertyChanged ( ) ;
  }


  /**
   * This constructor initializes the values and loads the description. It is
   * used for {@link Expression}s.
   * 
   * @param pExpression The {@link Expression} repressented by this node.
   * @param pOutlineUnbound The {@link OutlineUnbound} which repressents the
   *          unbound {@link Identifier}s in all nodes.
   * @param pPrefix The prefix of the {@link Expression}.
   * @param pIndex The child index.
   */
  public OutlineNode ( final Expression pExpression ,
      final OutlineUnbound pOutlineUnbound , final String pPrefix ,
      final int pIndex )
  {
    this ( ) ;
    this.expression = pExpression ;
    this.caption = pExpression.getCaption ( ) ;
    this.prettyString = pExpression.toPrettyString ( ).toString ( ) ;
    this.outlineUnbound = pOutlineUnbound.reduce ( this.expression ) ;
    this.outlineBreak = new OutlineBreak ( this.expression ) ;
    this.currentOutlineBreak = new OutlineBreak ( ) ;
    this.isExpression = true ;
    this.childIndex = pPrefix
        + ( ( pIndex <= OutlineNode.NO_CHILD_INDEX ) ? OutlineNode.BETWEEN1
            : OutlineNode.SMALL_SUB_BEGIN + pIndex + OutlineNode.SMALL_SUB_END
                + OutlineNode.BETWEEN1 ) ;
  }


  /**
   * This constructor initializes the values and loads the description. It is
   * used for {@link Identifier}s.
   * 
   * @param pIdentifier The {@link Identifier} repressented by this node.
   * @param pOutlineBinding The bindings in this node.
   * @param pPrefix The prefix of the {@link Identifier}.
   * @param pIndex The child index.
   */
  public OutlineNode ( final Identifier pIdentifier , final String pPrefix ,
      final int pIndex , final OutlineBinding pOutlineBinding )
  {
    this ( ) ;
    this.expression = pIdentifier ;
    this.caption = pIdentifier.getCaption ( ) ;
    this.prettyString = pIdentifier.toPrettyString ( ).toString ( ) ;
    this.outlineBinding = pOutlineBinding ;
    this.outlineBreak = new OutlineBreak ( this.expression ) ;
    this.currentOutlineBreak = new OutlineBreak ( ) ;
    this.isIdentifier = true ;
    this.childIndex = pPrefix
        + ( ( pIndex <= OutlineNode.NO_CHILD_INDEX ) ? OutlineNode.BETWEEN1
            : OutlineNode.SMALL_SUB_BEGIN + pIndex + OutlineNode.SMALL_SUB_END
                + OutlineNode.BETWEEN1 ) ;
  }


  /**
   * This constructor initializes the values and loads the description. It is
   * used for {@link Type}s.
   * 
   * @param pType The {@link Type} repressented by this node.
   * @param pPrefix The prefix of the {@link Identifier}.
   * @param pChildIndex The child index.
   */
  public OutlineNode ( final Type pType , final String pPrefix ,
      final int pChildIndex )
  {
    this ( ) ;
    this.type = pType ;
    this.caption = pType.getCaption ( ) ;
    this.prettyString = pType.toPrettyString ( ).toString ( ) ;
    this.outlineBreak = new OutlineBreak ( this.type ) ;
    this.currentOutlineBreak = new OutlineBreak ( ) ;
    this.isType = true ;
    this.childIndex = pPrefix
        + ( ( pChildIndex <= OutlineNode.NO_CHILD_INDEX ) ? OutlineNode.BETWEEN1
            : OutlineNode.SMALL_SUB_BEGIN + pChildIndex
                + OutlineNode.SMALL_SUB_END + OutlineNode.BETWEEN1 ) ;
  }


  /**
   * Adds a break and resets the cpation.
   */
  public final void breakCountAdd ( )
  {
    if ( ( ! this.isExpression ) && ( ! this.isType ) )
    {
      return ;
    }
    if ( this.outlineBreak.getBreakCountAll ( ) == this.currentOutlineBreak
        .getBreakCountOwn ( ) )
    {
      return ;
    }
    this.breakCount ++ ;
    this.currentOutlineBreak = this.outlineBreak.getBreaks ( this.breakCount ) ;
    while ( ( this.currentOutlineBreak.getBreakCountOwn ( ) == 0 )
        && ( this.currentOutlineBreak.hasBreaksAll ( ) ) )
    {
      this.breakCount ++ ;
      this.currentOutlineBreak = this.outlineBreak.getBreaks ( this.breakCount ) ;
    }
    this.updateCaption ( this.lastSelectionStart , this.lastSelectionEnd ) ;
  }


  /**
   * Removes a break and resets the cpation.
   */
  public final void breakCountRemove ( )
  {
    if ( ( ! this.isExpression ) && ( ! this.isType ) )
    {
      return ;
    }
    if ( this.breakCount == 0 )
    {
      return ;
    }
    this.breakCount -- ;
    this.currentOutlineBreak = this.outlineBreak.getBreaks ( this.breakCount ) ;
    this.updateCaption ( this.lastSelectionStart , this.lastSelectionEnd ) ;
  }


  /**
   * Returns true, if not all possible breaks are applied, otherwise false.
   * 
   * @return True, if not all possible breaks are applied, otherwise false.
   */
  public final boolean breaksCanAdd ( )
  {
    if ( this.outlineBreak == null )
    {
      return false ;
    }
    if ( this.currentOutlineBreak == null )
    {
      return false ;
    }
    return ( this.outlineBreak.getBreakCountAll ( ) > this.currentOutlineBreak
        .getBreakCountOwn ( ) ) ;
  }


  /**
   * Returns true, if not all possible breaks are removed, otherwise false.
   * 
   * @return True, if not all possible breaks are remove, otherwise false.
   */
  public final boolean breaksCanRemove ( )
  {
    if ( this.outlineBreak == null )
    {
      return false ;
    }
    if ( this.currentOutlineBreak == null )
    {
      return false ;
    }
    return ( this.currentOutlineBreak.getBreakCountOwn ( ) > 0 ) ;
  }


  /**
   * This method returns the length of the bounded {@link Identifier}, if the
   * {@link Identifier} begins at the given pCharIndex.
   * 
   * @param pCharIndex The index of the char in the {@link Expression}.
   * @return The length of the {@link Identifier}, if the {@link Identifier}
   *         begins at the given pCharIndex.
   */
  private final int charIsBinding ( final int pCharIndex )
  {
    if ( this.outlineBinding == null )
    {
      return - 1 ;
    }
    PrettyAnnotation prettyAnnotation ;
    for ( int i = 0 ; i < this.outlineBinding.size ( ) ; i ++ )
    {
      try
      {
        prettyAnnotation = this.expression.toPrettyString ( )
            .getAnnotationForPrintable ( this.outlineBinding.get ( i ) ) ;
        if ( ( pCharIndex >= prettyAnnotation.getStartOffset ( ) )
            && ( pCharIndex <= prettyAnnotation.getEndOffset ( ) ) )
        {
          return prettyAnnotation.getEndOffset ( )
              - prettyAnnotation.getStartOffset ( ) + 1 ;
        }
      }
      catch ( final IllegalArgumentException e )
      {
        /*
         * Happens if the bounded Identifiers are not in this node.
         */
      }
    }
    return - 1 ;
  }


  /**
   * This method returns the length of the bounded {@link Identifier}, if the
   * {@link Identifier} begins at the given pCharIndex.
   * 
   * @param pCharIndex The index of the char in the {@link Expression}.
   * @return The length of the {@link Identifier}, if the {@link Identifier}
   *         begins at the given pCharIndex.
   */
  private final int charIsSelectedBounded ( final int pCharIndex )
  {
    if ( ( pCharIndex >= this.boundedStart )
        && ( pCharIndex <= this.boundedEnd ) )
    {
      return this.boundedEnd - this.boundedStart + 1 ;
    }
    return - 1 ;
  }


  /**
   * This method returns the length of the unbound {@link Identifier}, if the
   * {@link Identifier} begins at the given pCharIndex.
   * 
   * @param pCharIndex pCharIndex The index of the char in the
   *          {@link Expression}.
   * @return The length of the {@link Identifier}, if the {@link Identifier}
   *         begins at the given pCharIndex.
   */
  private final int charIsUnbound ( final int pCharIndex )
  {
    for ( int i = 0 ; i < this.outlineUnbound.size ( ) ; i ++ )
    {
      final PrettyAnnotation prettyAnnotation = this.expression
          .toPrettyString ( ).getAnnotationForPrintable (
              this.outlineUnbound.get ( i ) ) ;
      if ( ( pCharIndex >= prettyAnnotation.getStartOffset ( ) )
          && ( pCharIndex <= prettyAnnotation.getEndOffset ( ) ) )
      {
        return prettyAnnotation.getEndOffset ( )
            - prettyAnnotation.getStartOffset ( ) + 1 ;
      }
    }
    return - 1 ;
  }


  /**
   * Returns the caption.
   * 
   * @return The caption.
   * @see #captionHTML
   */
  public final String getCaptionHTML ( )
  {
    return this.captionHTML ;
  }


  /**
   * Returns the replaced <code>char</code>.
   * 
   * @param pChar Input <code>char</code>.
   * @return The replaced <code>char</code>.
   */
  private final String getHTMLCode ( final char pChar )
  {
    if ( pChar == '&' )
    {
      return OutlineNode.AMPERSAND_THAN_REPLACE ;
    }
    if ( pChar == '<' )
    {
      return OutlineNode.LOWER_THAN_REPLACE ;
    }
    if ( pChar == '>' )
    {
      return OutlineNode.GREATER_THAN_REPLACE ;
    }
    return String.valueOf ( pChar ) ;
  }


  /**
   * Returns the nodeString.
   * 
   * @return The nodeString.
   * @see #prettyString
   */
  public final String getPrettyString ( )
  {
    return this.prettyString ;
  }


  /**
   * Returns the binding in this node.
   * 
   * @return The binding in this node.
   * @see #outlineBinding
   * @see #setOutlineBinding(OutlineBinding)
   */
  public final OutlineBinding getOutlineBinding ( )
  {
    return this.outlineBinding ;
  }


  /**
   * Returns the outlineBreak.
   * 
   * @return The outlineBreak.
   * @see #outlineBreak
   */
  public final OutlineBreak getOutlineBreak ( )
  {
    return this.outlineBreak ;
  }


  /**
   * Returns the {@link PrettyPrintable} repressented by this node.
   * 
   * @return The {@link PrettyPrintable} in this node.
   * @see #expression
   * @see #type
   */
  public final PrettyPrintable getPrettyPrintable ( )
  {
    if ( this.isExpression )
    {
      return this.expression ;
    }
    if ( this.isType )
    {
      return this.type ;
    }
    if ( this.isIdentifier )
    {
      return this.expression ;
    }
    return null ;
  }


  /**
   * Return true, if this {@link Expression} has one or more breaks.
   * 
   * @return True, if this {@link Expression} has one or more breaks.
   */
  public final boolean hasBreaks ( )
  {
    return this.breakCount > 0 ;
  }


  /**
   * Returns the isExpression.
   * 
   * @return The isExpression.
   * @see #isExpression
   */
  public final boolean isExpression ( )
  {
    return this.isExpression ;
  }


  /**
   * Returns the isIdentifier.
   * 
   * @return The isIdentifier.
   * @see #isIdentifier
   */
  public final boolean isIdentifier ( )
  {
    return this.isIdentifier ;
  }


  /**
   * Returns the isType.
   * 
   * @return The isType.
   * @see #isType
   */
  public final boolean isType ( )
  {
    return this.isType ;
  }


  /**
   * Loads the current color settings.
   */
  public final void propertyChanged ( )
  {
    this.expressionColor = OutlineNode.getHTMLColor ( Theme.currentTheme ( )
        .getExpressionColor ( ) ) ;
    this.keywordColor = OutlineNode.getHTMLColor ( Theme.currentTheme ( )
        .getKeywordColor ( ) ) ;
    this.constantColor = OutlineNode.getHTMLColor ( Theme.currentTheme ( )
        .getConstantColor ( ) ) ;
    this.typeColor = OutlineNode.getHTMLColor ( Theme.currentTheme ( )
        .getTypeColor ( ) ) ;
    this.selectionColor = OutlineNode.getHTMLColor ( Theme.currentTheme ( )
        .getSelectionColor ( ) ) ;
    this.bindingColor = OutlineNode.getHTMLColor ( Theme.currentTheme ( )
        .getBindingColor ( ) ) ;
    this.unboundColor = OutlineNode.getHTMLColor ( Theme.currentTheme ( )
        .getUnboundColor ( ) ) ;
    this.outlineNodeCacheList.clear ( ) ;
  }


  /**
   * Sets the {@link Identifier} which should be highlighted.
   * 
   * @param pBoundedIdentifier The {@link Identifier} which should be
   *          highlighted.
   */
  public void setBoundedIdentifier ( final Identifier pBoundedIdentifier )
  {
    if ( ( this.isType ) || ( pBoundedIdentifier == null ) )
    {
      this.boundedStart = OutlineNode.NO_BINDING ;
      this.boundedEnd = OutlineNode.NO_BINDING ;
      return ;
    }
    final PrettyAnnotation prettyAnnotation = this.expression.toPrettyString ( )
        .getAnnotationForPrintable ( pBoundedIdentifier ) ;
    this.boundedStart = prettyAnnotation.getStartOffset ( ) ;
    this.boundedEnd = prettyAnnotation.getEndOffset ( ) ;
  }


  /**
   * Sets the caption of this node.
   * 
   * @param pCaption The caption of this node.
   * @see #captionHTML
   */
  public void setCaptionHTML ( final String pCaption )
  {
    this.captionHTML = pCaption ;
  }


  /**
   * Sets the {@link OutlineBinding} in this node.
   * 
   * @param pOutlineBinding The {@link OutlineBinding} in this node.
   * @see #outlineBinding
   * @see #getOutlineBinding()
   */
  public final void setOutlineBinding ( final OutlineBinding pOutlineBinding )
  {
    if ( ! this.isIdentifier )
    {
      this.outlineBinding = pOutlineBinding ;
    }
  }


  /**
   * Set the value replaceInThisNode.
   * 
   * @param pReplaceInThisNode True, if the selected {@link Expression} should
   *          be replaced in this node.
   * @see #replaceInThisNode
   */
  public final void setReplaceInThisNode ( final boolean pReplaceInThisNode )
  {
    this.replaceInThisNode = pReplaceInThisNode ;
  }


  /**
   * Returns this <code>Object</code> as a <code>String</code>.
   * 
   * @see Object#toString()
   */
  @ Override
  public final String toString ( )
  {
    return this.captionHTML ;
  }


  /**
   * Updates the caption of the node. This method checks each character of the
   * name, if it is a keyword, a constant, a binding, selected or normal. This
   * method updates the caption without selection.
   */
  public final void updateCaption ( )
  {
    this.updateCaption ( OutlineNode.NO_SELECTION , OutlineNode.NO_SELECTION ) ;
  }


  /**
   * Updates the caption of the node. This method checks each character of the
   * name, if it is a keyword, a constant, a binding, selected or normal.
   * 
   * @param pSelectionStart The start offset of the selection in this node.
   * @param pSelectionEnd The end offset of the selection in this node.
   */
  @ SuppressWarnings ( "unqualified-field-access" )
  public final void updateCaption ( final int pSelectionStart ,
      final int pSelectionEnd )
  {
    int selectionStart = pSelectionStart ;
    int selectionEnd = pSelectionEnd ;
    if ( selectionStart > selectionEnd )
    {
      selectionStart = OutlineNode.NO_SELECTION ;
      selectionEnd = OutlineNode.NO_SELECTION ;
    }
    this.lastSelectionStart = selectionStart ;
    this.lastSelectionEnd = selectionEnd ;
    final String cache = this.outlineNodeCacheList.getCaption ( selectionStart ,
        selectionEnd , OutlineNode.selection , OutlineNode.binding ,
        OutlineNode.unbound ,
        ( OutlineNode.replace && this.replaceInThisNode ) , this.boundedStart ,
        this.boundedEnd , this.breakCount , this.outlineBinding ) ;
    if ( cache != null )
    {
      this.captionHTML = cache ;
      // TODO Only for testing
      // return ;
    }
    // Load the PrettyCharIterator
    PrettyCharIterator prettyCharIterator ;
    if ( this.expression != null )
    {
      prettyCharIterator = this.expression.toPrettyString ( )
          .toCharacterIterator ( ) ;
    }
    else if ( this.type != null )
    {
      prettyCharIterator = this.type.toPrettyString ( ).toCharacterIterator ( ) ;
    }
    else
    {
      throw new IllegalArgumentException ( "Not an Expression and not a Type" ) ; //$NON-NLS-1$
    }
    // Initialize the result as a StringBuffer
    final StringBuffer result = new StringBuffer ( ) ;
    // Build the first part of the node caption
    result.append ( OutlineNode.HTML ) ;
    result.append ( this.childIndex ) ;
    result.append ( OutlineNode.DESCRIPTION_BEGIN ) ;
    result.append ( this.caption ) ;
    result.append ( OutlineNode.DESCRIPTION_END ) ;
    result.append ( OutlineNode.BETWEEN2 ) ;
    result.append ( OutlineNode.FONT_BEGIN ) ;
    result.append ( this.expressionColor ) ;
    result.append ( OutlineNode.FONT_AFTER_COLOR ) ;
    int count = - 1 ;
    int charIndex = 0 ;
    final StringBuffer prefix = new StringBuffer ( ) ;
    prefix.append ( "<font color=\"#FFFFFF\">" ) ; //$NON-NLS-1$
    prefix.append ( this.childIndex ) ;
    prefix.append ( this.caption ) ;
    prefix.append ( OutlineNode.BETWEEN2 ) ;
    prefix.append ( OutlineNode.FONT_END ) ;
    final int length = this.prettyString.length ( ) ;
    while ( charIndex < length )
    {
      /*
       * Selection
       */
      if ( ( charIndex == selectionStart ) && ( OutlineNode.selection  ) )
      {
        charIndex = this.updateCaptionSelection ( charIndex , selectionEnd ,
            prettyCharIterator , result , prefix , this.selectionColor ) ;
      }
      /*
       * No selection and binding.
       */
      else if ( ( charIndex == selectionStart ) && ( ! OutlineNode.selection )
          && ( OutlineNode.binding  ) && ( this.outlineBinding != null )
          && ( this.outlineBinding.size ( ) > 0 ) )
      {
        charIndex = this.updateCaptionSelection ( charIndex , selectionEnd ,
            prettyCharIterator , result , prefix , this.selectionColor ) ;
      }
      /*
       * No selection and replace.
       */
      else if ( ( charIndex == selectionStart ) && ( ! OutlineNode.selection )
          && ( OutlineNode.replace  ) && ( this.replaceInThisNode ) )
      {
        result.append ( OutlineNode.REPLACE_BOLD ) ;
        while ( charIndex <= selectionEnd )
        {
          // Next character
          charIndex ++ ;
          prettyCharIterator.next ( ) ;
        }
      }
      /*
       * Binding
       */
      else if ( ( OutlineNode.binding  ) && ( this.isExpression )
          && ( this.outlineBinding != null )
          && ( ( count = this.charIsBinding ( charIndex ) ) >= 0 ) )
      {
        charIndex = this.updateCaptionBinding ( charIndex , count ,
            prettyCharIterator , result , prefix , this.bindingColor ) ;
      }
      /*
       * The selected Identifier is bounded in this Expression.
       */
      else if ( ( OutlineNode.binding  )
          && ( ( count = this.charIsSelectedBounded ( charIndex ) ) >= 0 ) )
      {
        charIndex = this.updateCaptionBinding ( charIndex , count ,
            prettyCharIterator , result , prefix , this.bindingColor ) ;
      }
      /*
       * The selected Identifier-Expression is bounded in this Expression, but
       * should not be selected
       */
      else if ( ( ! OutlineNode.selection ) && ( OutlineNode.binding  )
          && ( this.boundedStart != OutlineNode.NO_BINDING )
          && ( this.boundedEnd != OutlineNode.NO_BINDING )
          && ( charIndex == selectionStart ) )
      {
        charIndex = this.updateCaptionSelection ( charIndex , selectionEnd ,
            prettyCharIterator , result , prefix , this.selectionColor ) ;
      }
      /*
       * Unbound Identifier
       */
      else if ( ( OutlineNode.unbound  ) && ( this.outlineUnbound != null )
          && ( ( count = this.charIsUnbound ( charIndex ) ) >= 0 ) )
      {
        charIndex = this.updateCaptionBinding ( charIndex , count ,
            prettyCharIterator , result , prefix , this.unboundColor ) ;
      }
      /*
       * Keyword
       */
      else if ( PrettyStyle.KEYWORD.equals ( prettyCharIterator.getStyle ( ) ) )
      {
        charIndex = this.updateCaptionStyle ( charIndex , PrettyStyle.KEYWORD ,
            prettyCharIterator , result , prefix , this.keywordColor ) ;
      }
      /*
       * Constant
       */
      else if ( PrettyStyle.CONSTANT.equals ( prettyCharIterator.getStyle ( ) ) )
      {
        charIndex = this.updateCaptionStyle ( charIndex , PrettyStyle.CONSTANT ,
            prettyCharIterator , result , prefix , this.constantColor ) ;
      }
      /*
       * Type
       */
      else if ( PrettyStyle.TYPE.equals ( prettyCharIterator.getStyle ( ) ) )
      {
        charIndex = this.updateCaptionStyle ( charIndex , PrettyStyle.TYPE ,
            prettyCharIterator , result , prefix , this.typeColor ) ;
      }
      /*
       * Normal character
       */
      else
      {
        if ( this.currentOutlineBreak.isBreak ( charIndex ) )
        {
          result.append ( OutlineNode.BREAK ) ;
          result.append ( prefix ) ;
        }
        result.append ( this.getHTMLCode ( this.prettyString
            .charAt ( charIndex ) ) ) ;
        // Next character
        charIndex ++ ;
        prettyCharIterator.next ( ) ;
      }
    }
    result.append ( OutlineNode.EXPRESSION_END ) ;
    final OutlineNodeCache outlineNodeCache = new OutlineNodeCache (
        selectionStart , selectionEnd , OutlineNode.selection ,
        OutlineNode.binding , OutlineNode.unbound ,
        ( OutlineNode.replace && this.replaceInThisNode ) , this.boundedStart ,
        this.boundedEnd , this.breakCount , this.outlineBinding , result
            .toString ( ) ) ;
    this.outlineNodeCacheList.add ( outlineNodeCache ) ;
    this.captionHTML = result.toString ( ) ;
    // TODO Only for testing
    if ( ( cache != null ) && ( ! this.captionHTML.equals ( cache ) ) )
    {
      System.err.println ( "OutlineNode - Cache:   " + cache ) ; //$NON-NLS-1$
      System.err.println ( "OutlineNode - Caption: " + this.captionHTML ) ; //$NON-NLS-1$
    }
  }


  /**
   * Updates the caption of the node.
   * 
   * @param pCharIndex The char index.
   * @param pCount The number of characters.
   * @param pPrettyCharIterator The {@link PrettyCharIterator}.
   * @param pResult The result {@link StringBuffer}.
   * @param pPrefix The prefix before a break.
   * @param pColor The {@link Color} of the characters.
   * @return The charIndex at the end of this method.
   */
  private final int updateCaptionBinding ( final int pCharIndex ,
      final int pCount , final PrettyCharIterator pPrettyCharIterator ,
      final StringBuffer pResult , final StringBuffer pPrefix ,
      final String pColor )
  {
    int charIndex = pCharIndex ;
    pPrettyCharIterator.setIndex ( pPrettyCharIterator.getIndex ( ) + pCount ) ;
    pResult.append ( OutlineNode.FONT_BOLD_BEGIN ) ;
    pResult.append ( pColor ) ;
    pResult.append ( OutlineNode.FONT_AFTER_COLOR ) ;
    while ( charIndex < pCharIndex + pCount )
    {
      if ( this.currentOutlineBreak.isBreak ( charIndex ) )
      {
        pResult.append ( OutlineNode.FONT_BOLD_END ) ;
        pResult.append ( OutlineNode.BREAK ) ;
        pResult.append ( pPrefix ) ;
        pResult.append ( OutlineNode.FONT_BOLD_BEGIN ) ;
        pResult.append ( pColor ) ;
        pResult.append ( OutlineNode.FONT_AFTER_COLOR ) ;
      }
      pResult.append ( this
          .getHTMLCode ( this.prettyString.charAt ( charIndex ) ) ) ;
      // Next character
      charIndex ++ ;
    }
    pResult.append ( OutlineNode.FONT_BOLD_END ) ;
    return charIndex ;
  }


  /**
   * Updates the caption of the node.
   * 
   * @param pCharIndex The char index.
   * @param pSelectionEnd The end index of the selection.
   * @param pPrettyCharIterator The {@link PrettyCharIterator}.
   * @param pResult The result {@link StringBuffer}.
   * @param pPrefix The prefix before a break.
   * @param pColor The {@link Color} of the characters.
   * @return The charIndex at the end of this method.
   */
  private final int updateCaptionSelection ( final int pCharIndex ,
      final int pSelectionEnd , final PrettyCharIterator pPrettyCharIterator ,
      final StringBuffer pResult , final StringBuffer pPrefix ,
      final String pColor )
  {
    int charIndex = pCharIndex ;
    pPrettyCharIterator.setIndex ( pSelectionEnd + 1 ) ;
    pResult.append ( OutlineNode.FONT_BOLD_BEGIN ) ;
    pResult.append ( pColor ) ;
    pResult.append ( OutlineNode.FONT_AFTER_COLOR ) ;
    // Replace selected Expression
    if ( ( OutlineNode.replace  ) && ( this.replaceInThisNode ) )
    {
      pResult.append ( OutlineNode.REPLACE ) ;
    }
    while ( charIndex <= pSelectionEnd )
    {
      if ( ( this.currentOutlineBreak.isBreak ( charIndex ) )
          && ( ! ( OutlineNode.replace && this.replaceInThisNode ) ) )
      {
        pResult.append ( OutlineNode.FONT_BOLD_END ) ;
        pResult.append ( OutlineNode.BREAK ) ;
        pResult.append ( pPrefix ) ;
        pResult.append ( OutlineNode.FONT_BOLD_BEGIN ) ;
        pResult.append ( pColor ) ;
        pResult.append ( OutlineNode.FONT_AFTER_COLOR ) ;
      }
      if ( ! ( OutlineNode.replace && this.replaceInThisNode ) )
      {
        pResult.append ( this.getHTMLCode ( this.prettyString
            .charAt ( charIndex ) ) ) ;
      }
      // Next character
      charIndex ++ ;
    }
    pResult.append ( OutlineNode.FONT_BOLD_END ) ;
    return charIndex ;
  }


  /**
   * Updates the caption of the node.
   * 
   * @param pCharIndex The char index.
   * @param pPrettyStyle The {@link PrettyStyle}.
   * @param pPrettyCharIterator The {@link PrettyCharIterator}.
   * @param pResult The result {@link StringBuffer}.
   * @param pPrefix The prefix before a break.
   * @param pColor The {@link Color} of the characters.
   * @return The charIndex at the end of this method.
   */
  private final int updateCaptionStyle ( final int pCharIndex ,
      final PrettyStyle pPrettyStyle ,
      final PrettyCharIterator pPrettyCharIterator ,
      final StringBuffer pResult , final StringBuffer pPrefix ,
      final String pColor )
  {
    int charIndex = pCharIndex ;
    pResult.append ( OutlineNode.FONT_BOLD_BEGIN ) ;
    pResult.append ( pColor ) ;
    pResult.append ( OutlineNode.FONT_AFTER_COLOR ) ;
    while ( pPrettyStyle.equals ( pPrettyCharIterator.getStyle ( ) ) )
    {
      if ( this.currentOutlineBreak.isBreak ( charIndex ) )
      {
        pResult.append ( OutlineNode.FONT_BOLD_END ) ;
        pResult.append ( OutlineNode.BREAK ) ;
        pResult.append ( pPrefix ) ;
        pResult.append ( OutlineNode.FONT_BOLD_BEGIN ) ;
        pResult.append ( pColor ) ;
        pResult.append ( OutlineNode.FONT_AFTER_COLOR ) ;
      }
      pResult.append ( this
          .getHTMLCode ( this.prettyString.charAt ( charIndex ) ) ) ;
      // Next character
      charIndex ++ ;
      pPrettyCharIterator.next ( ) ;
    }
    pResult.append ( OutlineNode.FONT_BOLD_END ) ;
    return charIndex ;
  }
}
