package de.unisiegen.tpml.graphics.outline.node ;


import java.awt.Color ;
import javax.swing.tree.DefaultMutableTreeNode ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.interfaces.ExpressionOrType ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle ;
import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.core.types.TypeName ;
import de.unisiegen.tpml.core.util.Theme ;
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
   * @see #setFree(boolean)
   */
  private static boolean free = true ;


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
  private static final String PREFIX_BEGIN = "<font color=\"#FFFFFF\">" ; //$NON-NLS-1$


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
   * Returns the color in hexadecimal formatting.
   * 
   * @param pColor The color which should be returned.
   * @return The color in hexadecimal formatting.
   */
  private static final String getHexadecimalColor ( Color pColor )
  {
    String red = Integer.toHexString ( pColor.getRed ( ) ) ;
    red = red.length ( ) == 1 ? red = "0" + red : red ; //$NON-NLS-1$
    String green = Integer.toHexString ( pColor.getGreen ( ) ) ;
    green = green.length ( ) == 1 ? green = "0" + green : green ; //$NON-NLS-1$
    String blue = Integer.toHexString ( pColor.getBlue ( ) ) ;
    blue = blue.length ( ) == 1 ? blue = "0" + blue : blue ; //$NON-NLS-1$
    return red + green + blue ;
  }


  /**
   * Returns the replaced <code>char</code>.
   * 
   * @param pChar Input <code>char</code>.
   * @return The replaced <code>char</code>.
   */
  private static final String getHTMLCode ( char pChar )
  {
    if ( pChar == '&' )
    {
      return AMPERSAND_THAN_REPLACE ;
    }
    if ( pChar == '<' )
    {
      return LOWER_THAN_REPLACE ;
    }
    if ( pChar == '>' )
    {
      return GREATER_THAN_REPLACE ;
    }
    return String.valueOf ( pChar ) ;
  }


  /**
   * Sets the binding value. Selected {@link Identifier} and bindings should be
   * highlighted in higher nodes.
   * 
   * @param pBinding Should or should not be highlighted.
   * @see #binding
   */
  public static final void setBinding ( boolean pBinding )
  {
    binding = pBinding ;
  }


  /**
   * Sets the free value. Free {@link Identifier}s should be highlighted in all
   * nodes.
   * 
   * @param pFree Should or should not be highlighted.
   * @see #free
   */
  public static final void setFree ( boolean pFree )
  {
    free = pFree ;
  }


  /**
   * Sets the replace value. The selected {@link Expression} should be replaced
   * in higher nodes.
   * 
   * @param pReplace Should or should not be replaced.
   * @see #replace
   */
  public static final void setReplace ( boolean pReplace )
  {
    replace = pReplace ;
  }


  /**
   * Sets the selection value. The selected {@link Expression} should be
   * highlighted in higher nodes.
   * 
   * @param pSelection Should or should not be highlighted.
   * @see #selection
   */
  public static final void setSelection ( boolean pSelection )
  {
    selection = pSelection ;
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
   * The {@link ExpressionOrType} repressented by this node.
   */
  private ExpressionOrType expressionOrType ;


  /**
   * The {@link OutlineBinding} in this node.
   * 
   * @see #getOutlineBinding()
   * @see #setOutlineBinding(OutlineBinding)
   */
  private OutlineBinding < ? > outlineBinding ;


  /**
   * The {@link OutlineUnbound} which repressents the free {@link Identifier}s
   * in all nodes.
   */
  private OutlineUnbound outlineUnbound ;


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
   * Indicates, if this node is a {@link Identifier}.
   */
  private boolean isIdentifier ;


  /**
   * Indicates, if this node is a {@link Type}.
   */
  private boolean isType ;


  /**
   * Indicates, if this node is a {@link TypeName}.
   */
  private boolean isTypeName ;


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
   * The {@link Expression} color.
   */
  private String expressionColor ;


  /**
   * The {@link Identifier} color.
   */
  private String identifierColor ;


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
   * The bound {@link Identifier} color.
   */
  private String boundIdColor ;


  /**
   * The binding {@link Identifier} color.
   */
  private String bindingIdColor ;


  /**
   * The free {@link Identifier} color.
   */
  private String freeIdColor ;


  /**
   * The prefix after a break.
   */
  private String prefix ;


  /**
   * This constructor initializes the values.
   */
  private OutlineNode ( )
  {
    this.expressionOrType = null ;
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
    this.isTypeName = false ;
    this.boundStart = NO_BINDING ;
    this.boundEnd = NO_BINDING ;
    this.lastSelectionStart = NO_SELECTION ;
    this.lastSelectionEnd = NO_SELECTION ;
    this.outlineNodeCacheList = new OutlineNodeCacheList ( ) ;
    propertyChanged ( ) ;
  }


  /**
   * This constructor initializes the values and loads the description. It is
   * used for {@link Expression}s.
   * 
   * @param pExpression The {@link Expression} repressented by this node.
   * @param pOutlineUnbound The {@link OutlineUnbound} which repressents the
   *          free {@link Identifier}s in all nodes.
   * @param pChildIndex The child index.
   */
  public OutlineNode ( Expression pExpression , OutlineUnbound pOutlineUnbound ,
      int pChildIndex )
  {
    this ( ) ;
    this.expressionOrType = pExpression ;
    this.caption = pExpression.getCaption ( ) ;
    this.prettyString = pExpression.toPrettyString ( ).toString ( ) ;
    this.outlineUnbound = pOutlineUnbound.reduce ( this.expressionOrType ) ;
    this.outlineBreak = new OutlineBreak ( this.expressionOrType ) ;
    this.currentOutlineBreak = new OutlineBreak ( ) ;
    this.isExpression = true ;
    this.childIndex = pExpression.getPrefix ( )
        + ( ( pChildIndex <= OutlineNode.NO_CHILD_INDEX ) ? BETWEEN1
            : SMALL_SUB_BEGIN + pChildIndex + SMALL_SUB_END + BETWEEN1 ) ;
    this.prefix = PREFIX_BEGIN + this.childIndex + this.caption + BETWEEN2
        + FONT_END ;
  }


  /**
   * This constructor initializes the values and loads the description. It is
   * used for {@link Identifier}s.
   * 
   * @param pIdentifier The {@link Identifier} repressented by this node.
   * @param pOutlineBinding The bindings in this node.
   * @param pChildIndex The child index.
   */
  public OutlineNode ( Identifier pIdentifier , int pChildIndex ,
      OutlineBinding < Identifier > pOutlineBinding )
  {
    this ( ) ;
    this.expressionOrType = pIdentifier ;
    this.caption = pIdentifier.getCaption ( ) ;
    this.prettyString = pIdentifier.toPrettyString ( ).toString ( ) ;
    this.outlineBinding = pOutlineBinding ;
    this.outlineBreak = new OutlineBreak ( this.expressionOrType ) ;
    this.currentOutlineBreak = new OutlineBreak ( ) ;
    this.isIdentifier = true ;
    this.childIndex = pIdentifier.getPrefix ( )
        + ( ( pChildIndex <= OutlineNode.NO_CHILD_INDEX ) ? BETWEEN1
            : SMALL_SUB_BEGIN + pChildIndex + SMALL_SUB_END + BETWEEN1 ) ;
    this.prefix = PREFIX_BEGIN + this.childIndex + this.caption + BETWEEN2
        + FONT_END ;
  }


  /**
   * This constructor initializes the values and loads the description. It is
   * used for {@link Type}s.
   * 
   * @param pType The {@link Type} repressented by this node.
   * @param pOutlineUnbound The {@link OutlineUnbound} which repressents the
   *          free {@link TypeName}s in all nodes.
   * @param pChildIndex The child index.
   */
  public OutlineNode ( Type pType , OutlineUnbound pOutlineUnbound ,
      int pChildIndex )
  {
    this ( ) ;
    this.expressionOrType = pType ;
    this.caption = pType.getCaption ( ) ;
    this.prettyString = pType.toPrettyString ( ).toString ( ) ;
    this.outlineUnbound = pOutlineUnbound.reduce ( this.expressionOrType ) ;
    this.outlineBreak = new OutlineBreak ( this.expressionOrType ) ;
    this.currentOutlineBreak = new OutlineBreak ( ) ;
    this.isType = true ;
    this.childIndex = pType.getPrefix ( )
        + ( ( pChildIndex <= OutlineNode.NO_CHILD_INDEX ) ? BETWEEN1
            : SMALL_SUB_BEGIN + pChildIndex + SMALL_SUB_END + BETWEEN1 ) ;
    this.prefix = PREFIX_BEGIN + this.childIndex + this.caption + BETWEEN2
        + FONT_END ;
  }


  /**
   * This constructor initializes the values and loads the description. It is
   * used for {@link TypeName}s.
   * 
   * @param pTypeName The {@link TypeName} repressented by this node.
   * @param pChildIndex The child index.
   * @param pOutlineBinding The bindings in this node.
   */
  public OutlineNode ( TypeName pTypeName , int pChildIndex ,
      OutlineBinding < TypeName > pOutlineBinding )
  {
    this ( ) ;
    this.expressionOrType = pTypeName ;
    this.caption = pTypeName.getCaption ( ) ;
    this.prettyString = pTypeName.toPrettyString ( ).toString ( ) ;
    this.outlineBinding = pOutlineBinding ;
    this.outlineBreak = new OutlineBreak ( this.expressionOrType ) ;
    this.currentOutlineBreak = new OutlineBreak ( ) ;
    this.isTypeName = true ;
    this.childIndex = pTypeName.getPrefix ( )
        + ( ( pChildIndex <= OutlineNode.NO_CHILD_INDEX ) ? BETWEEN1
            : SMALL_SUB_BEGIN + pChildIndex + SMALL_SUB_END + BETWEEN1 ) ;
    this.prefix = PREFIX_BEGIN + this.childIndex + this.caption + BETWEEN2
        + FONT_END ;
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
    updateCaption ( this.lastSelectionStart , this.lastSelectionEnd ) ;
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
    updateCaption ( this.lastSelectionStart , this.lastSelectionEnd ) ;
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
   * This method returns the length of the binding {@link Identifier} or
   * {@link TypeName}, if the {@link Identifier} or {@link TypeName} begins at
   * the given pCharIndex.
   * 
   * @param pCharIndex The index of the char.
   * @return The length of the {@link Identifier} or {@link TypeName}, if the
   *         {@link Identifier} or {@link TypeName} begins at the given
   *         pCharIndex.
   */
  private final int charIsBinding ( int pCharIndex )
  {
    if ( ( pCharIndex >= this.boundStart ) && ( pCharIndex <= this.boundEnd ) )
    {
      return this.boundEnd - this.boundStart + 1 ;
    }
    return - 1 ;
  }


  /**
   * This method returns the length of the bound {@link Identifier} or
   * {@link TypeName}, if the {@link Identifier} or {@link TypeName} begins at
   * the given pCharIndex.
   * 
   * @param pCharIndex The index of the char in the {@link Expression}.
   * @return The length of the {@link Identifier} or {@link TypeName}, if the
   *         {@link Identifier} or {@link TypeName} begins at the given
   *         pCharIndex.
   */
  private final int charIsBound ( int pCharIndex )
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
        prettyAnnotation = this.expressionOrType.toPrettyString ( )
            .getAnnotationForPrintable (
                ( ExpressionOrType ) this.outlineBinding.get ( i ) ) ;
        if ( ( pCharIndex >= prettyAnnotation.getStartOffset ( ) )
            && ( pCharIndex <= prettyAnnotation.getEndOffset ( ) ) )
        {
          return prettyAnnotation.getEndOffset ( )
              - prettyAnnotation.getStartOffset ( ) + 1 ;
        }
      }
      catch ( IllegalArgumentException e )
      {
        /*
         * Happens if the bound Identifiers are not in this node.
         */
      }
    }
    return - 1 ;
  }


  /**
   * This method returns the length of the free {@link Identifier}, if the
   * {@link Identifier} begins at the given pCharIndex.
   * 
   * @param pCharIndex pCharIndex The index of the char in the
   *          {@link Expression}.
   * @return The length of the {@link Identifier}, if the {@link Identifier}
   *         begins at the given pCharIndex.
   */
  private final int charIsFreeIdentifier ( int pCharIndex )
  {
    PrettyAnnotation prettyAnnotation ;
    for ( int i = 0 ; i < this.outlineUnbound.size ( ) ; i ++ )
    {
      prettyAnnotation = this.expressionOrType.toPrettyString ( )
          .getAnnotationForPrintable ( this.outlineUnbound.get ( i ) ) ;
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
   * Returns the {@link ExpressionOrType} repressented by this node.
   * 
   * @return The {@link ExpressionOrType} in this node.
   * @see #expressionOrType
   */
  public final ExpressionOrType getExpressionOrType ( )
  {
    return this.expressionOrType ;
  }


  /**
   * Returns the binding in this node.
   * 
   * @return The binding in this node.
   * @see #outlineBinding
   * @see #setOutlineBinding(OutlineBinding)
   */
  public final OutlineBinding < ? > getOutlineBinding ( )
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
   * Returns the prettyString.
   * 
   * @return The prettyString.
   * @see #prettyString
   */
  public final String getPrettyString ( )
  {
    return this.prettyString ;
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
   * Returns the isTypeName.
   * 
   * @return The isTypeName.
   * @see #isTypeName
   */
  public final boolean isTypeName ( )
  {
    return this.isTypeName ;
  }


  /**
   * Loads the current color settings.
   */
  public final void propertyChanged ( )
  {
    this.expressionColor = getHexadecimalColor ( Theme.currentTheme ( )
        .getExpressionColor ( ) ) ;
    this.identifierColor = getHexadecimalColor ( Theme.currentTheme ( )
        .getIdentifierColor ( ) ) ;
    this.keywordColor = getHexadecimalColor ( Theme.currentTheme ( )
        .getKeywordColor ( ) ) ;
    this.constantColor = getHexadecimalColor ( Theme.currentTheme ( )
        .getConstantColor ( ) ) ;
    this.typeColor = getHexadecimalColor ( Theme.currentTheme ( )
        .getTypeColor ( ) ) ;
    this.selectionColor = getHexadecimalColor ( Theme.currentTheme ( )
        .getSelectionColor ( ) ) ;
    this.boundIdColor = getHexadecimalColor ( Theme.currentTheme ( )
        .getBoundIdColor ( ) ) ;
    this.bindingIdColor = getHexadecimalColor ( Theme.currentTheme ( )
        .getBindingIdColor ( ) ) ;
    this.freeIdColor = getHexadecimalColor ( Theme.currentTheme ( )
        .getFreeIdColor ( ) ) ;
    this.outlineNodeCacheList.clear ( ) ;
  }


  /**
   * Sets the {@link Identifier} which should be highlighted.
   * 
   * @param pBindingIdentifier The {@link Identifier} which should be
   *          highlighted.
   */
  public final void setBindingIdentifier ( Identifier pBindingIdentifier )
  {
    if ( pBindingIdentifier == null )
    {
      this.boundStart = NO_BINDING ;
      this.boundEnd = NO_BINDING ;
      return ;
    }
    try
    {
      PrettyAnnotation prettyAnnotation ;
      prettyAnnotation = this.expressionOrType.toPrettyString ( )
          .getAnnotationForPrintable ( pBindingIdentifier ) ;
      this.boundStart = prettyAnnotation.getStartOffset ( ) ;
      this.boundEnd = prettyAnnotation.getEndOffset ( ) ;
    }
    catch ( IllegalArgumentException e )
    {
      /*
       * Happens if the bound Identifiers are not in this node.
       */
      this.boundStart = NO_BINDING ;
      this.boundEnd = NO_BINDING ;
    }
  }


  /**
   * Sets the {@link TypeName} which should be highlighted.
   * 
   * @param pBindingTypeName The {@link TypeName} which should be highlighted.
   */
  public final void setBindingTypeName ( TypeName pBindingTypeName )
  {
    if ( pBindingTypeName == null )
    {
      this.boundStart = NO_BINDING ;
      this.boundEnd = NO_BINDING ;
      return ;
    }
    try
    {
      PrettyAnnotation prettyAnnotation = this.expressionOrType
          .toPrettyString ( ).getAnnotationForPrintable ( pBindingTypeName ) ;
      this.boundStart = prettyAnnotation.getStartOffset ( ) ;
      this.boundEnd = prettyAnnotation.getEndOffset ( ) ;
    }
    catch ( IllegalArgumentException e )
    {
      /*
       * Happens if the bound TypeNames are not in this node.
       */
      this.boundStart = NO_BINDING ;
      this.boundEnd = NO_BINDING ;
    }
  }


  /**
   * Sets the caption of this node.
   * 
   * @param pCaption The caption of this node.
   * @see #captionHTML
   */
  public final void setCaptionHTML ( String pCaption )
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
  public final void setOutlineBinding ( OutlineBinding < ? > pOutlineBinding )
  {
    if ( ( ! this.isIdentifier ) && ( ! this.isTypeName ) )
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
  public final void setReplaceInThisNode ( boolean pReplaceInThisNode )
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
    updateCaption ( NO_SELECTION , NO_SELECTION ) ;
  }


  /**
   * Updates the caption of the node. This method checks each character of the
   * name, if it is a keyword, a constant, a binding, selected or normal.
   * 
   * @param pSelectionStart The start offset of the selection in this node.
   * @param pSelectionEnd The end offset of the selection in this node.
   */
  @ SuppressWarnings ( "unqualified-field-access" )
  public final void updateCaption ( int pSelectionStart , int pSelectionEnd )
  {
    int selectionStart = pSelectionStart ;
    int selectionEnd = pSelectionEnd ;
    if ( selectionStart > selectionEnd )
    {
      selectionStart = NO_SELECTION ;
      selectionEnd = NO_SELECTION ;
    }
    this.lastSelectionStart = selectionStart ;
    this.lastSelectionEnd = selectionEnd ;
    String cache = this.outlineNodeCacheList.getCaption ( selectionStart ,
        selectionEnd , selection , binding , free ,
        ( replace && this.replaceInThisNode ) , this.boundStart ,
        this.boundEnd , this.breakCount , this.outlineBinding ) ;
    if ( cache != null )
    {
      this.captionHTML = cache ;
      return ;
    }
    // Load the PrettyCharIterator
    PrettyCharIterator prettyCharIterator = this.expressionOrType
        .toPrettyString ( ).toCharacterIterator ( ) ;
    // Initialize the result as a StringBuilder
    StringBuilder result = new StringBuilder ( ) ;
    // Build the first part of the node caption
    result.append ( HTML ) ;
    result.append ( this.childIndex ) ;
    result.append ( DESCRIPTION_BEGIN ) ;
    result.append ( this.caption ) ;
    result.append ( DESCRIPTION_END ) ;
    result.append ( BETWEEN2 ) ;
    result.append ( FONT_BEGIN ) ;
    result.append ( this.expressionColor ) ;
    result.append ( FONT_AFTER_COLOR ) ;
    int count = - 1 ;
    int charIndex = 0 ;
    final int length = this.prettyString.length ( ) ;
    while ( charIndex < length )
    {
      /*
       * Selection
       */
      if ( ( charIndex == selectionStart ) && ( selection ) )
      {
        charIndex = updateCaptionSelection ( charIndex , selectionEnd ,
            prettyCharIterator , result , this.selectionColor ) ;
      }
      /*
       * No selection and binding.
       */
      else if ( ( charIndex == selectionStart ) && ( ! selection )
          && ( binding ) && ( this.outlineBinding != null )
          && ( this.outlineBinding.size ( ) > 0 ) )
      {
        charIndex = updateCaptionSelection ( charIndex , selectionEnd ,
            prettyCharIterator , result , this.selectionColor ) ;
      }
      /*
       * No selection and replace.
       */
      else if ( ( charIndex == selectionStart ) && ( ! selection )
          && ( replace ) && ( this.replaceInThisNode ) )
      {
        result.append ( REPLACE_BOLD ) ;
        while ( charIndex <= selectionEnd )
        {
          // Next character
          charIndex ++ ;
          prettyCharIterator.next ( ) ;
        }
      }
      /*
       * Bound Identifier or bound TypeName
       */
      else if ( ( binding ) && ( this.isExpression || this.isType )
          && ( this.outlineBinding != null )
          && ( ( count = charIsBound ( charIndex ) ) >= 0 ) )
      {
        charIndex = updateCaptionBinding ( charIndex , count ,
            prettyCharIterator , result , this.boundIdColor ) ;
      }
      /*
       * Binding Identifier or binding TypeName
       */
      else if ( ( binding ) && ( ( count = charIsBinding ( charIndex ) ) >= 0 ) )
      {
        charIndex = updateCaptionBinding ( charIndex , count ,
            prettyCharIterator , result , this.bindingIdColor ) ;
      }
      /*
       * The selected Identifier-Expression is bound in this Expression or Type,
       * but should not be selected or the selected TypeName-Type is bound in
       * this Expression or Type, but should not be selected.
       */
      else if ( ( ! selection ) && ( binding )
          && ( this.boundStart != NO_BINDING )
          && ( this.boundEnd != NO_BINDING ) && ( charIndex == selectionStart ) )
      {
        charIndex = updateCaptionSelection ( charIndex , selectionEnd ,
            prettyCharIterator , result , this.selectionColor ) ;
      }
      /*
       * Unbound Identifier
       */
      else if ( ( free ) && ( this.outlineUnbound != null )
          && ( ( count = charIsFreeIdentifier ( charIndex ) ) >= 0 ) )
      {
        charIndex = updateCaptionBinding ( charIndex , count ,
            prettyCharIterator , result , this.freeIdColor ) ;
      }
      /*
       * Keyword
       */
      else if ( PrettyStyle.KEYWORD.equals ( prettyCharIterator.getStyle ( ) ) )
      {
        charIndex = updateCaptionStyle ( charIndex , true ,
            PrettyStyle.KEYWORD , prettyCharIterator , result ,
            this.keywordColor ) ;
      }
      /*
       * Identifier
       */
      else if ( PrettyStyle.IDENTIFIER
          .equals ( prettyCharIterator.getStyle ( ) ) )
      {
        charIndex = updateCaptionStyle ( charIndex , false ,
            PrettyStyle.IDENTIFIER , prettyCharIterator , result ,
            this.identifierColor ) ;
      }
      /*
       * Constant
       */
      else if ( PrettyStyle.CONSTANT.equals ( prettyCharIterator.getStyle ( ) ) )
      {
        charIndex = updateCaptionStyle ( charIndex , true ,
            PrettyStyle.CONSTANT , prettyCharIterator , result ,
            this.constantColor ) ;
      }
      /*
       * Type
       */
      else if ( PrettyStyle.TYPE.equals ( prettyCharIterator.getStyle ( ) ) )
      {
        charIndex = updateCaptionStyle ( charIndex , true , PrettyStyle.TYPE ,
            prettyCharIterator , result , this.typeColor ) ;
      }
      /*
       * Normal character
       */
      else
      {
        if ( this.currentOutlineBreak.isBreak ( charIndex ) )
        {
          result.append ( BREAK ) ;
          result.append ( prefix ) ;
        }
        result.append ( getHTMLCode ( this.prettyString.charAt ( charIndex ) ) ) ;
        // Next character
        charIndex ++ ;
        prettyCharIterator.next ( ) ;
      }
    }
    result.append ( EXPRESSION_END ) ;
    OutlineNodeCache outlineNodeCache = new OutlineNodeCache ( selectionStart ,
        selectionEnd , selection , binding , free ,
        ( replace && this.replaceInThisNode ) , this.boundStart ,
        this.boundEnd , this.breakCount , this.outlineBinding , result
            .toString ( ) ) ;
    this.outlineNodeCacheList.add ( outlineNodeCache ) ;
    this.captionHTML = result.toString ( ) ;
  }


  /**
   * Updates the caption of the node.
   * 
   * @param pCharIndex The char index.
   * @param pCount The number of characters.
   * @param pPrettyCharIterator The {@link PrettyCharIterator}.
   * @param pResult The result {@link StringBuilder}.
   * @param pColor The {@link Color} of the characters.
   * @return The charIndex at the end of this method.
   */
  private final int updateCaptionBinding ( int pCharIndex , int pCount ,
      PrettyCharIterator pPrettyCharIterator , StringBuilder pResult ,
      String pColor )
  {
    int charIndex = pCharIndex ;
    pPrettyCharIterator.setIndex ( pPrettyCharIterator.getIndex ( ) + pCount ) ;
    pResult.append ( FONT_BOLD_BEGIN ) ;
    pResult.append ( pColor ) ;
    pResult.append ( FONT_AFTER_COLOR ) ;
    while ( charIndex < pCharIndex + pCount )
    {
      if ( this.currentOutlineBreak.isBreak ( charIndex ) )
      {
        pResult.append ( FONT_BOLD_END ) ;
        pResult.append ( BREAK ) ;
        pResult.append ( this.prefix ) ;
        pResult.append ( FONT_BOLD_BEGIN ) ;
        pResult.append ( pColor ) ;
        pResult.append ( FONT_AFTER_COLOR ) ;
      }
      pResult.append ( getHTMLCode ( this.prettyString.charAt ( charIndex ) ) ) ;
      // Next character
      charIndex ++ ;
    }
    pResult.append ( FONT_BOLD_END ) ;
    return charIndex ;
  }


  /**
   * Updates the caption of the node.
   * 
   * @param pCharIndex The char index.
   * @param pSelectionEnd The end index of the selection.
   * @param pPrettyCharIterator The {@link PrettyCharIterator}.
   * @param pResult The result {@link StringBuilder}.
   * @param pColor The {@link Color} of the characters.
   * @return The charIndex at the end of this method.
   */
  private final int updateCaptionSelection ( int pCharIndex ,
      int pSelectionEnd , PrettyCharIterator pPrettyCharIterator ,
      StringBuilder pResult , String pColor )
  {
    int charIndex = pCharIndex ;
    pPrettyCharIterator.setIndex ( pSelectionEnd + 1 ) ;
    pResult.append ( FONT_BOLD_BEGIN ) ;
    pResult.append ( pColor ) ;
    pResult.append ( FONT_AFTER_COLOR ) ;
    // Replace selected Expression
    if ( ( replace ) && ( this.replaceInThisNode ) )
    {
      pResult.append ( REPLACE ) ;
    }
    while ( charIndex <= pSelectionEnd )
    {
      if ( ( this.currentOutlineBreak.isBreak ( charIndex ) )
          && ( ! ( replace && this.replaceInThisNode ) ) )
      {
        pResult.append ( FONT_BOLD_END ) ;
        pResult.append ( BREAK ) ;
        pResult.append ( this.prefix ) ;
        pResult.append ( FONT_BOLD_BEGIN ) ;
        pResult.append ( pColor ) ;
        pResult.append ( FONT_AFTER_COLOR ) ;
      }
      if ( ! ( replace && this.replaceInThisNode ) )
      {
        pResult
            .append ( getHTMLCode ( this.prettyString.charAt ( charIndex ) ) ) ;
      }
      // Next character
      charIndex ++ ;
    }
    pResult.append ( FONT_BOLD_END ) ;
    return charIndex ;
  }


  /**
   * Updates the caption of the node.
   * 
   * @param pCharIndex The char index.
   * @param pBold True if bold is active.
   * @param pPrettyStyle The {@link PrettyStyle}.
   * @param pPrettyCharIterator The {@link PrettyCharIterator}.
   * @param pResult The result {@link StringBuilder}.
   * @param pColor The {@link Color} of the characters.
   * @return The charIndex at the end of this method.
   */
  private final int updateCaptionStyle ( int pCharIndex , boolean pBold ,
      PrettyStyle pPrettyStyle , PrettyCharIterator pPrettyCharIterator ,
      StringBuilder pResult , String pColor )
  {
    int charIndex = pCharIndex ;
    if ( pBold )
    {
      pResult.append ( FONT_BOLD_BEGIN ) ;
    }
    else
    {
      pResult.append ( FONT_BEGIN ) ;
    }
    pResult.append ( pColor ) ;
    pResult.append ( FONT_AFTER_COLOR ) ;
    while ( pPrettyStyle.equals ( pPrettyCharIterator.getStyle ( ) ) )
    {
      if ( this.currentOutlineBreak.isBreak ( charIndex ) )
      {
        pResult.append ( FONT_BOLD_END ) ;
        pResult.append ( BREAK ) ;
        pResult.append ( this.prefix ) ;
        pResult.append ( FONT_BOLD_BEGIN ) ;
        pResult.append ( pColor ) ;
        pResult.append ( FONT_AFTER_COLOR ) ;
      }
      pResult.append ( getHTMLCode ( this.prettyString.charAt ( charIndex ) ) ) ;
      // Next character
      charIndex ++ ;
      pPrettyCharIterator.next ( ) ;
    }
    if ( pBold )
    {
      pResult.append ( FONT_BOLD_END ) ;
    }
    else
    {
      pResult.append ( FONT_END ) ;
    }
    return charIndex ;
  }
}
