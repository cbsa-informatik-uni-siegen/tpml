package de.unisiegen.tpml.graphics.outline ;


import java.awt.Color ;
import de.unisiegen.tpml.core.expressions.BinaryOperator ;
import de.unisiegen.tpml.core.expressions.CurriedMethod ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.InfixOperation ;
import de.unisiegen.tpml.core.expressions.Method ;
import de.unisiegen.tpml.core.expressions.Value ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle ;
import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.graphics.Theme ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineBinding ;
import de.unisiegen.tpml.graphics.outline.binding.OutlinePair ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineUnbound ;


/**
 * This class represents the nodes in the {@link Outline}.
 * 
 * @author Christian Fehler
 * @version $Rev: 1075 $
 */
public final class OutlineNode
{
  /**
   * The {@link Expression} should not be shown in this nodes.
   */
  private static final int NO_SELECTION = - 1 ;


  /**
   * There is no {@link Identifier} in this {@link Expression}.
   */
  private static final int NO_IDENTIFIER = - 1 ;


  /**
   * Bindings should not be highlighted in higher nodes.
   */
  public static final int NO_BINDING = - 1 ;


  /**
   * The {@link Expression} has no child index.
   */
  public static final int NO_CHILD_INDEX = - 1 ;


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
   * Lower than.
   */
  private static final String LOWER_THAN = "<" ; //$NON-NLS-1$


  /**
   * Lower than replace.
   */
  private static final String LOWER_THAN_REPLACE = "&lt" ; //$NON-NLS-1$


  /**
   * Greater than.
   */
  private static final String GREATER_THAN = "<" ; //$NON-NLS-1$


  /**
   * Greater than replace.
   */
  private static final String GREATER_THAN_REPLACE = "&gt" ; //$NON-NLS-1$


  /**
   * Ampersand.
   */
  private static final String AMPERSAND_THAN = "&" ; //$NON-NLS-1$


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
   * Caption of the {@link Value}.
   */
  private static final String VALUE = "v" ; //$NON-NLS-1$


  /**
   * Caption of the {@link Expression}.
   */
  private static final String EXPRESSION = "e" ; //$NON-NLS-1$


  /**
   * Caption of the {@link Identifier}.
   */
  private static final String IDENTIFIER = "id" ; //$NON-NLS-1$


  /**
   * Caption of the {@link BinaryOperator}.
   */
  private static final String OP = "op" ; //$NON-NLS-1$


  /**
   * Caption of the {@link Type}.
   */
  private static final String TYPE = "\u03C4" ; //$NON-NLS-1$


  /**
   * Caption of the {@link Method} or {@link CurriedMethod}.
   */
  private static final String METH = "m" ; //$NON-NLS-1$


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
   * Sets the binding value. Selected {@link Identifier} and bindings should be
   * highlighted in higher nodes.
   * 
   * @param pBinding Should or should not be highlighted.
   * @see #binding
   */
  public final static void setBinding ( boolean pBinding )
  {
    binding = pBinding ;
  }


  /**
   * Sets the replace value. The selected {@link Expression} should be replaced
   * in higher nodes.
   * 
   * @param pReplace Should or should not be replaced.
   * @see #replace
   */
  public final static void setReplace ( boolean pReplace )
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
  public final static void setSelection ( boolean pSelection )
  {
    selection = pSelection ;
  }


  /**
   * Sets the unbound value. Unbound {@link Identifier}s should be highlighted
   * in all nodes.
   * 
   * @param pUnbound Should or should not be highlighted.
   * @see #unbound
   */
  public final static void setUnbound ( boolean pUnbound )
  {
    unbound = pUnbound ;
  }


  /**
   * The selected {@link Expression} should be replaced in this node.
   * 
   * @see #setReplaceInThisNode(boolean)
   */
  private boolean replaceInThisNode ;


  /**
   * The description of the node.
   */
  private String description ;


  /**
   * The child index of this {@link Expression}.
   */
  private String childIndex ;


  /**
   * The {@link Expression} as a <code>String</code>.
   */
  private String expressionString ;


  /**
   * The hole caption in HTML format.
   * 
   * @see #getCaption()
   * @see #setCaption(String)
   */
  private String caption ;


  /**
   * The {@link Expression} repressented by this node.
   * 
   * @see #getExpression()
   */
  private Expression expression ;


  /**
   * The {@link OutlineBinding} in this node.
   * 
   * @see #getOutlineBinding()
   * @see #setOutlineBinding(OutlineBinding)
   */
  private OutlineBinding outlineBinding ;


  /**
   * The start index of the {@link Identifier}.
   * 
   * @see #getEndIndex()
   */
  private int startIndex ;


  /**
   * The end index of the {@link Identifier}.
   * 
   * @see #getStartIndex()
   */
  private int endIndex ;


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
   * Indicates, if this node is a {@link Type}.
   */
  private boolean isType ;


  /**
   * Indicates, if this node is a {@link Identifier}.
   */
  private boolean isIdentifier ;


  /**
   * Indicates, if this node is a {@link InfixOperation}.
   */
  private boolean isInfixOperation ;


  /**
   * Indicates, if this node is a {@link Expression}.
   */
  private boolean isExpression ;


  /**
   * The start index of the last selection.
   */
  private int lastSelectionStart ;


  /**
   * The end index of the last selection.
   */
  private int lastSelectionEnd ;


  /**
   * This constructor initializes the values and loads the description. It is
   * used for {@link Expression}s.
   * 
   * @param pExpression The {@link Expression} repressented by this node.
   * @param pOutlineUnbound The {@link OutlineUnbound} which repressents the
   *          unbound {@link Identifier}s in all nodes.
   */
  public OutlineNode ( Expression pExpression , OutlineUnbound pOutlineUnbound )
  {
    this.expression = pExpression ;
    this.description = pExpression.getCaption ( ) ;
    this.childIndex = "" ; //$NON-NLS-1$
    this.expressionString = pExpression.toPrettyString ( ).toString ( ) ;
    this.startIndex = NO_IDENTIFIER ;
    this.endIndex = NO_IDENTIFIER ;
    this.outlineBinding = null ;
    this.outlineUnbound = pOutlineUnbound ;
    this.replaceInThisNode = false ;
    this.boundedStart = NO_BINDING ;
    this.boundedEnd = NO_BINDING ;
    this.outlineBreak = new OutlineBreak ( this.expression ) ;
    this.currentOutlineBreak = new OutlineBreak ( ) ;
    this.breakCount = 0 ;
    this.isType = false ;
    this.isIdentifier = false ;
    this.isInfixOperation = false ;
    this.isExpression = true ;
    this.lastSelectionStart = NO_SELECTION ;
    this.lastSelectionEnd = NO_SELECTION ;
  }


  /**
   * This constructor initializes the values and loads the description. It is
   * used for {@link InfixOperation}.
   * 
   * @param pExpression The {@link Expression} repressented by this node.
   * @param pExpressionString The {@link Expression} as a <code>String</code>.
   * @param pStartIndex The start index of the {@link Identifier}.
   * @param pEndIndex The end index of the {@link Identifier}.
   * @param pOutlineUnbound The {@link OutlineUnbound} which repressents the
   *          unbound {@link Identifier}s in all nodes
   */
  public OutlineNode ( Expression pExpression , String pExpressionString ,
      int pStartIndex , int pEndIndex , OutlineUnbound pOutlineUnbound )
  {
    this.expression = null ;
    this.description = pExpression.getCaption ( ) ;
    this.childIndex = "" ; //$NON-NLS-1$
    this.expressionString = pExpressionString ;
    this.startIndex = pStartIndex ;
    this.endIndex = pEndIndex ;
    this.outlineBinding = null ;
    this.outlineUnbound = pOutlineUnbound ;
    this.replaceInThisNode = false ;
    this.boundedStart = NO_BINDING ;
    this.boundedEnd = NO_BINDING ;
    this.outlineBreak = null ;
    this.currentOutlineBreak = null ;
    this.breakCount = 0 ;
    this.isType = false ;
    this.isIdentifier = false ;
    this.isInfixOperation = true ;
    this.isExpression = false ;
    this.lastSelectionStart = NO_SELECTION ;
    this.lastSelectionEnd = NO_SELECTION ;
  }


  /**
   * This constructor initializes the values and loads the description. It is
   * used for {@link Type}s.
   * 
   * @param pDescription The description of this node.
   * @param pExpressionString The {@link Expression} as a <code>String</code>.
   * @param pStartIndex The start index of the {@link Identifier}.
   * @param pEndIndex The end index of the {@link Identifier}.
   * @param pOutlineBinding The bindings in this node.
   * @param pOutlineUnbound The {@link OutlineUnbound} which repressents the
   *          unbound {@link Identifier}s in all nodes
   */
  public OutlineNode ( String pDescription , String pExpressionString ,
      int pStartIndex , int pEndIndex , OutlineBinding pOutlineBinding ,
      OutlineUnbound pOutlineUnbound )
  {
    this.expression = null ;
    this.description = pDescription ;
    this.childIndex = "" ; //$NON-NLS-1$
    this.expressionString = pExpressionString ;
    this.startIndex = pStartIndex ;
    this.endIndex = pEndIndex ;
    this.outlineBinding = pOutlineBinding ;
    this.outlineUnbound = pOutlineUnbound ;
    this.replaceInThisNode = false ;
    this.boundedStart = NO_BINDING ;
    this.boundedEnd = NO_BINDING ;
    this.outlineBreak = null ;
    this.currentOutlineBreak = null ;
    this.breakCount = 0 ;
    this.isType = true ;
    this.isIdentifier = false ;
    this.isInfixOperation = false ;
    this.isExpression = false ;
    this.lastSelectionStart = NO_SELECTION ;
    this.lastSelectionEnd = NO_SELECTION ;
  }


  /**
   * This constructor initializes the values and loads the description. It is
   * used for {@link Identifier}s.
   * 
   * @param pDescription The description of this node.
   * @param pExpressionString The {@link Expression} as a <code>String</code>.
   * @param pOutlinePair The start and the end index of the {@link Identifier}.
   * @param pOutlineBinding The bindings in this node.
   * @param pOutlineUnbound The {@link OutlineUnbound} which repressents the
   *          unbound {@link Identifier}s in all nodes
   */
  public OutlineNode ( String pDescription , String pExpressionString ,
      OutlinePair pOutlinePair , OutlineBinding pOutlineBinding ,
      OutlineUnbound pOutlineUnbound )
  {
    this.expression = null ;
    this.description = pDescription ;
    this.childIndex = "" ; //$NON-NLS-1$
    this.expressionString = pExpressionString ;
    this.startIndex = pOutlinePair.getStart ( ) ;
    this.endIndex = pOutlinePair.getEnd ( ) ;
    this.outlineBinding = pOutlineBinding ;
    this.outlineUnbound = pOutlineUnbound ;
    this.replaceInThisNode = false ;
    this.boundedStart = NO_BINDING ;
    this.boundedEnd = NO_BINDING ;
    this.outlineBreak = null ;
    this.currentOutlineBreak = null ;
    this.breakCount = 0 ;
    this.isType = false ;
    this.isIdentifier = true ;
    this.isInfixOperation = false ;
    this.isExpression = false ;
    this.lastSelectionStart = NO_SELECTION ;
    this.lastSelectionEnd = NO_SELECTION ;
  }


  /**
   * Decrements the break count and resets the cpation.
   * 
   * @return True, if something has changed.
   */
  public final boolean breakCountDec ( )
  {
    if ( ! this.isExpression )
    {
      return false ;
    }
    if ( this.breakCount == 0 )
    {
      return false ;
    }
    this.breakCount -- ;
    this.currentOutlineBreak = this.outlineBreak.getBreaks ( this.breakCount ) ;
    updateCaption ( this.lastSelectionStart , this.lastSelectionEnd ) ;
    return true ;
  }


  /**
   * Increments the break count and resets the cpation.
   * 
   * @return True, if something has changed.
   */
  public final boolean breakCountInc ( )
  {
    if ( ! this.isExpression )
    {
      return false ;
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
    if ( this.outlineBreak.getBreakCountAll ( ) == this.currentOutlineBreak
        .getBreakCountOwn ( ) )
    {
      return false ;
    }
    return true ;
  }


  /**
   * Highlight the selected {@link Identifier}.
   */
  public final void enableBindingColor ( )
  {
    StringBuffer result = new StringBuffer ( HTML ) ;
    result.append ( this.childIndex ) ;
    result.append ( DESCRIPTION_BEGIN ) ;
    result.append ( this.description ) ;
    result.append ( DESCRIPTION_END ) ;
    result.append ( BETWEEN2 ) ;
    result.append ( FONT_BEGIN ) ;
    result.append ( getHTMLFormat ( Theme.currentTheme ( )
        .getExpressionColor ( ) ) ) ;
    result.append ( FONT_AFTER_COLOR ) ;
    if ( binding )
    {
      result.append ( FONT_BOLD_BEGIN ) ;
      result
          .append ( getHTMLFormat ( Theme.currentTheme ( ).getBindingColor ( ) ) ) ;
      result.append ( FONT_AFTER_COLOR ) ;
      result.append ( getHTMLCode ( this.expressionString ) ) ;
      result.append ( FONT_BOLD_END ) ;
    }
    else
    {
      result.append ( getHTMLCode ( this.expressionString ) ) ;
    }
    result.append ( EXPRESSION_END ) ;
    this.caption = result.toString ( ) ;
  }


  /**
   * Highlight the selected {@link Identifier}.
   */
  public final void enableSelectionColor ( )
  {
    if ( ! this.isExpression )
    {
      StringBuffer result = new StringBuffer ( HTML ) ;
      result.append ( this.childIndex ) ;
      result.append ( DESCRIPTION_BEGIN ) ;
      result.append ( this.description ) ;
      result.append ( DESCRIPTION_END ) ;
      result.append ( BETWEEN2 ) ;
      result.append ( FONT_BEGIN ) ;
      result.append ( getHTMLFormat ( Theme.currentTheme ( )
          .getExpressionColor ( ) ) ) ;
      result.append ( FONT_AFTER_COLOR ) ;
      if ( selection )
      {
        result.append ( FONT_BOLD_BEGIN ) ;
        result.append ( getHTMLFormat ( Theme.currentTheme ( )
            .getSelectionColor ( ) ) ) ;
        result.append ( FONT_AFTER_COLOR ) ;
      }
      else if ( this.isInfixOperation )
      {
        result.append ( FONT_BOLD_BEGIN ) ;
        result.append ( getHTMLFormat ( Theme.currentTheme ( )
            .getConstantColor ( ) ) ) ;
        result.append ( FONT_AFTER_COLOR ) ;
      }
      result.append ( getHTMLCode ( this.expressionString ) ) ;
      if ( ( selection ) || ( this.isInfixOperation ) )
      {
        result.append ( FONT_BOLD_END ) ;
      }
      result.append ( EXPRESSION_END ) ;
      this.caption = result.toString ( ) ;
    }
    /*
     * else { updateCaption ( NO_SELECTION , NO_SELECTION ) ; }
     */
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
   * Returns the end index of the {@link Identifier}.
   * 
   * @return The end index of the {@link Identifier}.
   * @see #endIndex
   */
  public final int getEndIndex ( )
  {
    return this.endIndex ;
  }


  /**
   * Returns the {@link Expression} repressented by this node.
   * 
   * @return The {@link Expression} in this node.
   * @see #expression
   */
  public final Expression getExpression ( )
  {
    return this.expression ;
  }


  /**
   * Returns the expressionString.
   * 
   * @return The expressionString.
   * @see #expressionString
   */
  public final String getExpressionString ( )
  {
    return this.expressionString ;
  }


  /**
   * Returns the hex value of a given integer.
   * 
   * @param pNumber The input integer value.
   * @return The hex value of a given integer.
   */
  private static final String getHex ( int pNumber )
  {
    StringBuffer result = new StringBuffer ( ) ;
    int remainder = Math.abs ( pNumber ) ;
    int base = 0 ;
    if ( remainder > 0 )
    {
      while ( remainder > 0 )
      {
        base = remainder % 16 ;
        remainder = ( remainder / 16 ) ;
        result.insert ( 0 , HEX_VALUES [ base ] ) ;
      }
    }
    else
    {
      return HEX_VALUES [ 16 ] ;
    }
    return result.toString ( ) ;
  }


  /**
   * Returns the replaced <code>char</code>.
   * 
   * @param pChar Input <code>char</code>.
   * @return The replaced <code>char</code>.
   */
  private final String getHTMLCode ( char pChar )
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
   * Returns the replaced <code>String</code>.
   * 
   * @param pText Input <code>String</code>.
   * @return The replaced <code>String</code>.
   */
  private final String getHTMLCode ( String pText )
  {
    String s = pText.replaceAll ( AMPERSAND_THAN , AMPERSAND_THAN_REPLACE ) ;
    s = s.replaceAll ( LOWER_THAN , LOWER_THAN_REPLACE ) ;
    s = s.replaceAll ( GREATER_THAN , GREATER_THAN_REPLACE ) ;
    return s ;
  }


  /**
   * Returns the color in HTML formatting.
   * 
   * @param pColor The color which should be returned.
   * @return The color in HTML formatting.
   */
  public static final String getHTMLFormat ( Color pColor )
  {
    return ( getHex ( pColor.getRed ( ) ) + getHex ( pColor.getGreen ( ) ) + getHex ( pColor
        .getBlue ( ) ) ) ;
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
   * Returns the start index of the {@link Identifier}.
   * 
   * @return The start index of the {@link Identifier}.
   * @see #startIndex
   */
  public final int getStartIndex ( )
  {
    return this.startIndex ;
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
   * This method returns the length of the bounded {@link Identifier}, if the
   * {@link Identifier} begins at the given pCharIndex.
   * 
   * @param pCharIndex The index of the char in the {@link Expression}.
   * @return The length of the {@link Identifier}, if the {@link Identifier}
   *         begins at the given pCharIndex.
   */
  private final int isBinding ( int pCharIndex )
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
      catch ( IllegalArgumentException e )
      {
        /*
         * Happens if the bounded Identifiers are not in this node.
         */
      }
    }
    return - 1 ;
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
   * Returns the isInfixOperation.
   * 
   * @return The isInfixOperation.
   * @see #isInfixOperation
   */
  public final boolean isInfixOperation ( )
  {
    return this.isInfixOperation ;
  }


  /**
   * This method returns the length of the bounded {@link Identifier}, if the
   * {@link Identifier} begins at the given pCharIndex.
   * 
   * @param pCharIndex The index of the char in the {@link Expression}.
   * @return The length of the {@link Identifier}, if the {@link Identifier}
   *         begins at the given pCharIndex.
   */
  private final int isSelectedBounded ( int pCharIndex )
  {
    if ( ( pCharIndex >= this.boundedStart )
        && ( pCharIndex <= this.boundedEnd ) )
    {
      return this.boundedEnd - this.boundedStart + 1 ;
    }
    return - 1 ;
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
   * This method returns the length of the unbound {@link Identifier}, if the
   * {@link Identifier} begins at the given pCharIndex.
   * 
   * @param pCharIndex pCharIndex The index of the char in the
   *          {@link Expression}.
   * @return The length of the {@link Identifier}, if the {@link Identifier}
   *         begins at the given pCharIndex.
   */
  private final int isUnbound ( int pCharIndex )
  {
    for ( int i = 0 ; i < this.outlineUnbound.size ( ) ; i ++ )
    {
      try
      {
        PrettyAnnotation prettyAnnotation = this.expression.toPrettyString ( )
            .getAnnotationForPrintable ( this.outlineUnbound.get ( i ) ) ;
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
         * Happens if the unbound Identifiers are not in this node.
         */
      }
    }
    return - 1 ;
  }


  /**
   * Resets the caption of the node.
   */
  public final void resetCaption ( )
  {
    if ( this.startIndex != NO_IDENTIFIER )
    {
      StringBuffer result = new StringBuffer ( HTML ) ;
      result.append ( this.childIndex ) ;
      result.append ( DESCRIPTION_BEGIN ) ;
      result.append ( this.description ) ;
      result.append ( DESCRIPTION_END ) ;
      result.append ( BETWEEN2 ) ;
      result.append ( FONT_BEGIN ) ;
      result.append ( getHTMLFormat ( Theme.currentTheme ( )
          .getExpressionColor ( ) ) ) ;
      result.append ( FONT_AFTER_COLOR ) ;
      if ( this.isInfixOperation )
      {
        result.append ( FONT_BOLD_BEGIN ) ;
        result.append ( getHTMLFormat ( Theme.currentTheme ( )
            .getConstantColor ( ) ) ) ;
        result.append ( FONT_AFTER_COLOR ) ;
        result.append ( getHTMLCode ( this.expressionString ) ) ;
        result.append ( FONT_BOLD_END ) ;
      }
      else if ( this.isType )
      {
        result.append ( FONT_BOLD_BEGIN ) ;
        result
            .append ( getHTMLFormat ( Theme.currentTheme ( ).getTypeColor ( ) ) ) ;
        result.append ( FONT_AFTER_COLOR ) ;
        result.append ( getHTMLCode ( this.expressionString ) ) ;
        result.append ( FONT_BOLD_END ) ;
      }
      else
      {
        result.append ( getHTMLCode ( this.expressionString ) ) ;
      }
      result.append ( EXPRESSION_END ) ;
      this.caption = result.toString ( ) ;
    }
    else
    {
      updateCaption ( NO_SELECTION , NO_SELECTION ) ;
    }
  }


  /**
   * Sets the end index of the {@link Identifier}.
   * 
   * @param pBoundEnd The end index of the {@link Identifier}.
   */
  public final void setBoundedEnd ( int pBoundEnd )
  {
    this.boundedEnd = pBoundEnd ;
  }


  /**
   * Sets the start index of the {@link Identifier}.
   * 
   * @param pBoundStart The start index of the {@link Identifier}.
   */
  public final void setBoundedStart ( int pBoundStart )
  {
    this.boundedStart = pBoundStart ;
  }


  /**
   * Sets the caption of this node.
   * 
   * @param pCaption The caption of this node.
   * @see #caption
   */
  public void setCaption ( String pCaption )
  {
    this.caption = pCaption ;
  }


  /**
   * Sets the child index of the {@link Expression}.
   * 
   * @see #childIndex
   */
  public final void setChildIndexExpression ( )
  {
    setChildIndexExpression ( NO_CHILD_INDEX ) ;
  }


  /**
   * Sets the child index of the {@link Expression}.
   * 
   * @param pChildIndexExpression The child index of the {@link Expression}.
   * @see #childIndex
   */
  public final void setChildIndexExpression ( int pChildIndexExpression )
  {
    if ( this.expression.isValue ( ) )
    {
      this.childIndex = VALUE ;
    }
    else
    {
      this.childIndex = EXPRESSION ;
    }
    if ( pChildIndexExpression == NO_CHILD_INDEX )
    {
      this.childIndex += BETWEEN1 ;
    }
    else
    {
      this.childIndex += SMALL_SUB_BEGIN + pChildIndexExpression
          + SMALL_SUB_END + BETWEEN1 ;
    }
  }


  /**
   * Sets the child index of the {@link Identifier}.
   * 
   * @see #childIndex
   */
  public final void setChildIndexIdentifier ( )
  {
    setChildIndexIdentifier ( NO_CHILD_INDEX ) ;
  }


  /**
   * Sets the child index of the {@link Identifier}.
   * 
   * @param pChildIndexIdentifier The child index of the {@link Identifier}.
   * @see #childIndex
   */
  public final void setChildIndexIdentifier ( int pChildIndexIdentifier )
  {
    if ( pChildIndexIdentifier == OutlineNode.NO_CHILD_INDEX )
    {
      this.childIndex = IDENTIFIER + BETWEEN1 ;
    }
    else
    {
      this.childIndex = IDENTIFIER + SMALL_SUB_BEGIN + pChildIndexIdentifier
          + SMALL_SUB_END + BETWEEN1 ;
    }
  }


  /**
   * Sets the child index of the {@link Method} or {@link CurriedMethod}.
   * 
   * @see #childIndex
   */
  public final void setChildIndexMeth ( )
  {
    setChildIndexMeth ( NO_CHILD_INDEX ) ;
  }


  /**
   * Sets the child index of the {@link Method} or {@link CurriedMethod}.
   * 
   * @param pChildIndexMeth The child index of the {@link Method} or
   *          {@link CurriedMethod}.
   * @see #childIndex
   */
  public final void setChildIndexMeth ( int pChildIndexMeth )
  {
    if ( pChildIndexMeth == OutlineNode.NO_CHILD_INDEX )
    {
      this.childIndex = METH + BETWEEN1 ;
    }
    else
    {
      this.childIndex = METH + SMALL_SUB_BEGIN + pChildIndexMeth
          + SMALL_SUB_END + BETWEEN1 ;
    }
  }


  /**
   * Sets the child index of the {@link BinaryOperator}.
   * 
   * @see #childIndex
   */
  public final void setChildIndexOp ( )
  {
    setChildIndexOp ( NO_CHILD_INDEX ) ;
  }


  /**
   * Sets the child index of the {@link BinaryOperator}.
   * 
   * @param pChildIndexOp The child index of the {@link BinaryOperator}.
   * @see #childIndex
   */
  public final void setChildIndexOp ( int pChildIndexOp )
  {
    if ( pChildIndexOp == OutlineNode.NO_CHILD_INDEX )
    {
      this.childIndex = OP + BETWEEN1 ;
    }
    else
    {
      this.childIndex = OP + SMALL_SUB_BEGIN + pChildIndexOp + SMALL_SUB_END
          + BETWEEN1 ;
    }
  }


  /**
   * Sets the child index of the {@link Type}.
   * 
   * @see #childIndex
   */
  public final void setChildIndexType ( )
  {
    setChildIndexType ( NO_CHILD_INDEX ) ;
  }


  /**
   * Sets the child index of the {@link Type}.
   * 
   * @param pChildIndexType The child index of the {@link Type}.
   * @see #childIndex
   */
  public final void setChildIndexType ( int pChildIndexType )
  {
    if ( pChildIndexType == OutlineNode.NO_CHILD_INDEX )
    {
      this.childIndex = TYPE + BETWEEN1 ;
    }
    else
    {
      this.childIndex = TYPE + SMALL_SUB_BEGIN + pChildIndexType
          + SMALL_SUB_END + BETWEEN1 ;
    }
  }


  /**
   * Sets the {@link OutlineBinding} in this node.
   * 
   * @param pOutlineBinding The {@link OutlineBinding} in this node.
   * @see #outlineBinding
   * @see #getOutlineBinding()
   */
  public final void setOutlineBinding ( OutlineBinding pOutlineBinding )
  {
    if ( this.isExpression )
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
    return this.caption ;
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
    // Load the PrettyCharIterator
    PrettyCharIterator prettyCharIterator = this.expression.toPrettyString ( )
        .toCharacterIterator ( ) ;
    // Load the current color settings
    String expressionColor = getHTMLFormat ( Theme.currentTheme ( )
        .getExpressionColor ( ) ) ;
    String keywordColor = getHTMLFormat ( Theme.currentTheme ( )
        .getKeywordColor ( ) ) ;
    String constantColor = getHTMLFormat ( Theme.currentTheme ( )
        .getConstantColor ( ) ) ;
    String typeColor = getHTMLFormat ( Theme.currentTheme ( ).getTypeColor ( ) ) ;
    String selectionColor = getHTMLFormat ( Theme.currentTheme ( )
        .getSelectionColor ( ) ) ;
    String bindingColor = getHTMLFormat ( Theme.currentTheme ( )
        .getBindingColor ( ) ) ;
    String unboundColor = getHTMLFormat ( Theme.currentTheme ( )
        .getUnboundColor ( ) ) ;
    // Initialize the result as a StringBuffer
    StringBuffer result = new StringBuffer ( ) ;
    // Build the first part of the node caption
    result.append ( HTML ) ;
    result.append ( this.childIndex ) ;
    result.append ( DESCRIPTION_BEGIN ) ;
    result.append ( this.description ) ;
    result.append ( DESCRIPTION_END ) ;
    result.append ( BETWEEN2 ) ;
    result.append ( FONT_BEGIN ) ;
    result.append ( expressionColor ) ;
    result.append ( FONT_AFTER_COLOR ) ;
    int count = - 1 ;
    int charIndex = 0 ;
    StringBuffer prefix = new StringBuffer ( ) ;
    prefix.append ( "<font color=\"#FFFFFF\">" ) ; //$NON-NLS-1$
    prefix.append ( this.childIndex ) ;
    prefix.append ( this.description ) ;
    prefix.append ( BETWEEN2 ) ;
    prefix.append ( FONT_END ) ;
    final int length = this.expressionString.length ( ) ;
    while ( charIndex < length )
    {
      /*
       * Selection
       */
      if ( ( charIndex == selectionStart ) && ( selection ) )
      {
        charIndex = updateCaptionSelection ( charIndex , selectionEnd ,
            prettyCharIterator , result , prefix , selectionColor ) ;
      }
      /*
       * No selection and binding.
       */
      else if ( ( charIndex == selectionStart ) && ( ! selection )
          && ( binding ) && ( this.outlineBinding != null )
          && ( this.outlineBinding.size ( ) > 0 ) )
      {
        charIndex = updateCaptionSelection ( charIndex , selectionEnd ,
            prettyCharIterator , result , prefix , selectionColor ) ;
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
       * Binding
       */
      else if ( ( binding ) && ( this.outlineBinding != null )
          && ( ( count = isBinding ( charIndex ) ) >= 0 ) )
      {
        charIndex = updateCaptionBinding ( charIndex , count ,
            prettyCharIterator , result , prefix , bindingColor ) ;
      }
      /*
       * The selected Identifier-Expression is bounded in this Expression.
       */
      else if ( ( binding )
          && ( ( count = isSelectedBounded ( charIndex ) ) >= 0 ) )
      {
        charIndex = updateCaptionBinding ( charIndex , count ,
            prettyCharIterator , result , prefix , bindingColor ) ;
      }
      /*
       * The selected Identifier-Expression is bounded in this Expression, but
       * should not be selected
       */
      else if ( ( ! selection ) && ( binding )
          && ( this.boundedStart != NO_BINDING )
          && ( this.boundedEnd != NO_BINDING )
          && ( charIndex == selectionStart ) )
      {
        charIndex = updateCaptionSelection ( charIndex , selectionEnd ,
            prettyCharIterator , result , prefix , selectionColor ) ;
      }
      /*
       * Unbound Identifier
       */
      else if ( ( unbound ) && ( this.outlineUnbound != null )
          && ( ( count = isUnbound ( charIndex ) ) >= 0 ) )
      {
        charIndex = updateCaptionBinding ( charIndex , count ,
            prettyCharIterator , result , prefix , unboundColor ) ;
      }
      /*
       * Keyword
       */
      else if ( PrettyStyle.KEYWORD.equals ( prettyCharIterator.getStyle ( ) ) )
      {
        charIndex = updateCaptionStyle ( charIndex , PrettyStyle.KEYWORD ,
            prettyCharIterator , result , prefix , keywordColor ) ;
      }
      /*
       * Constant
       */
      else if ( PrettyStyle.CONSTANT.equals ( prettyCharIterator.getStyle ( ) ) )
      {
        charIndex = updateCaptionStyle ( charIndex , PrettyStyle.CONSTANT ,
            prettyCharIterator , result , prefix , constantColor ) ;
      }
      /*
       * Type
       */
      else if ( PrettyStyle.TYPE.equals ( prettyCharIterator.getStyle ( ) ) )
      {
        charIndex = updateCaptionStyle ( charIndex , PrettyStyle.TYPE ,
            prettyCharIterator , result , prefix , typeColor ) ;
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
        result
            .append ( getHTMLCode ( this.expressionString.charAt ( charIndex ) ) ) ;
        // Next character
        charIndex ++ ;
        prettyCharIterator.next ( ) ;
      }
    }
    result.append ( EXPRESSION_END ) ;
    this.caption = result.toString ( ) ;
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
  private final int updateCaptionBinding ( int pCharIndex , int pCount ,
      PrettyCharIterator pPrettyCharIterator , StringBuffer pResult ,
      StringBuffer pPrefix , String pColor )
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
        pResult.append ( pPrefix ) ;
        pResult.append ( FONT_BOLD_BEGIN ) ;
        pResult.append ( pColor ) ;
        pResult.append ( FONT_AFTER_COLOR ) ;
      }
      pResult
          .append ( getHTMLCode ( this.expressionString.charAt ( charIndex ) ) ) ;
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
   * @param pResult The result {@link StringBuffer}.
   * @param pPrefix The prefix before a break.
   * @param pColor The {@link Color} of the characters.
   * @return The charIndex at the end of this method.
   */
  private final int updateCaptionSelection ( int pCharIndex ,
      int pSelectionEnd , PrettyCharIterator pPrettyCharIterator ,
      StringBuffer pResult , StringBuffer pPrefix , String pColor )
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
        pResult.append ( pPrefix ) ;
        pResult.append ( FONT_BOLD_BEGIN ) ;
        pResult.append ( pColor ) ;
        pResult.append ( FONT_AFTER_COLOR ) ;
      }
      if ( ! ( replace && this.replaceInThisNode ) )
      {
        pResult.append ( getHTMLCode ( this.expressionString
            .charAt ( charIndex ) ) ) ;
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
   * @param pPrettyStyle The {@link PrettyStyle}.
   * @param pPrettyCharIterator The {@link PrettyCharIterator}.
   * @param pResult The result {@link StringBuffer}.
   * @param pPrefix The prefix before a break.
   * @param pColor The {@link Color} of the characters.
   * @return The charIndex at the end of this method.
   */
  private final int updateCaptionStyle ( int pCharIndex ,
      PrettyStyle pPrettyStyle , PrettyCharIterator pPrettyCharIterator ,
      StringBuffer pResult , StringBuffer pPrefix , String pColor )
  {
    int charIndex = pCharIndex ;
    pResult.append ( FONT_BOLD_BEGIN ) ;
    pResult.append ( pColor ) ;
    pResult.append ( FONT_AFTER_COLOR ) ;
    while ( pPrettyStyle.equals ( pPrettyCharIterator.getStyle ( ) ) )
    {
      if ( this.currentOutlineBreak.isBreak ( charIndex ) )
      {
        pResult.append ( FONT_BOLD_END ) ;
        pResult.append ( BREAK ) ;
        pResult.append ( pPrefix ) ;
        pResult.append ( FONT_BOLD_BEGIN ) ;
        pResult.append ( pColor ) ;
        pResult.append ( FONT_AFTER_COLOR ) ;
      }
      pResult
          .append ( getHTMLCode ( this.expressionString.charAt ( charIndex ) ) ) ;
      // Next character
      charIndex ++ ;
      pPrettyCharIterator.next ( ) ;
    }
    pResult.append ( FONT_BOLD_END ) ;
    return charIndex ;
  }
}
