package de.unisiegen.tpml.graphics.outline ;


import java.awt.Color ;
import de.unisiegen.tpml.core.expressions.BinaryOperator ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.MultiLet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle ;
import de.unisiegen.tpml.graphics.Theme ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineBinding ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineUnbound ;


/**
 * This class represents the nodes in the {@link Outline}.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class OutlineNode
{
  /**
   * No bindings should be shown in the nodes.
   */
  public static final int NO_BINDING = - 1 ;


  /**
   * The {@link Expression} should not be shown in this nodes.
   */
  public static final int NO_SELECTION = - 1 ;


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
   * The beginning of the caption.
   */
  private static final String BEGIN = "&nbsp;&nbsp;&nbsp;&nbsp;[&nbsp;<font color=\"#" ; //$NON-NLS-1$


  /**
   * The end of the caption.
   */
  private static final String END = "</font>&nbsp;]</html>" ; //$NON-NLS-1$


  /**
   * String, if a {@link Expression} should be replaced and selection is active.
   */
  private static final String REPLACE_BOLD = "<b>&nbsp;...&nbsp;</b>" ; //$NON-NLS-1$


  /**
   * String, if a {@link Expression} should be replaced.
   */
  private static final String REPLACE = "&nbsp;...&nbsp;" ; //$NON-NLS-1$


  /**
   * Begin of HTML.
   */
  private static final String HTML = "<html>" ; //$NON-NLS-1$


  /**
   * After the color.
   */
  private static final String FONT_AFTER_COLOR = "\">" ; //$NON-NLS-1$


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
  public static void setBinding ( boolean pBinding )
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
  public static void setReplace ( boolean pReplace )
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
  public static void setSelection ( boolean pSelection )
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
  public static void setUnbound ( boolean pUnbound )
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
   * 
   * @see #appendDescription(String)
   */
  private String description ;


  /**
   * The {@link Expression} as a <code>String</code>.
   */
  private String expressionString ;


  /**
   * The hole caption in HTML format.
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
   * This constructor initializes the values and loads the description.
   * 
   * @param pExpression The {@link Expression} repressented by this node.
   * @param pOutlineUnbound The {@link OutlineUnbound} which repressents the
   *          unbound {@link Identifier}s in all nodes.
   */
  public OutlineNode ( Expression pExpression , OutlineUnbound pOutlineUnbound )
  {
    this.description = pExpression.getClass ( ).getSimpleName ( ) ;
    this.expressionString = pExpression.toPrettyString ( ).toString ( ) ;
    this.expression = pExpression ;
    this.startIndex = - 1 ;
    this.endIndex = - 1 ;
    this.outlineBinding = null ;
    this.outlineUnbound = pOutlineUnbound ;
    this.replaceInThisNode = false ;
    resetCaption ( ) ;
  }


  /**
   * This constructor initializes the values and loads the description.
   * 
   * @param pExpression The {@link Expression} repressented by this node.
   * @param pDescription The description of this node.
   * @param pExpressionString The {@link Expression} as a <code>String</code>.
   * @param pStartIndex The start index of the {@link Identifier}.
   * @param pEndIndex The end index of the {@link Identifier}.
   * @param pOutlineBinding The bindings in this node.
   * @param pOutlineUnbound The {@link OutlineUnbound} which repressents the
   *          unbound {@link Identifier}s in all nodes
   */
  public OutlineNode ( Expression pExpression , String pDescription ,
      String pExpressionString , int pStartIndex , int pEndIndex ,
      OutlineBinding pOutlineBinding , OutlineUnbound pOutlineUnbound )
  {
    this.expression = pExpression ;
    this.description = pDescription ;
    this.expressionString = pExpressionString ;
    this.startIndex = pStartIndex ;
    this.endIndex = pEndIndex ;
    this.outlineBinding = pOutlineBinding ;
    this.outlineUnbound = pOutlineUnbound ;
    this.replaceInThisNode = false ;
    resetCaption ( ) ;
  }


  /**
   * This constructor initializes the values and loads the description.
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
    this.description = pDescription ;
    this.expressionString = pExpressionString ;
    this.expression = null ;
    this.startIndex = pStartIndex ;
    this.endIndex = pEndIndex ;
    this.outlineBinding = pOutlineBinding ;
    this.outlineUnbound = pOutlineUnbound ;
    this.replaceInThisNode = false ;
    resetCaption ( ) ;
  }


  /**
   * Insert a <code>String</code> before the current description.
   * 
   * @param pAppendDescription The <code>String</code> which should be
   *          inserted before the current description.
   * @see #description
   */
  public void appendDescription ( String pAppendDescription )
  {
    this.description = pAppendDescription + this.description ;
  }


  /**
   * Highlight the selected {@link Identifier}.
   */
  public void enableSelectionColor ( )
  {
    if ( this.startIndex != - 1 )
    {
      StringBuffer result = new StringBuffer ( HTML ) ;
      result.append ( this.description ) ;
      result.append ( BEGIN ) ;
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
      else if ( this.expression instanceof BinaryOperator )
      {
        result.append ( FONT_BOLD_BEGIN ) ;
        result.append ( getHTMLFormat ( Theme.currentTheme ( )
            .getConstantColor ( ) ) ) ;
        result.append ( FONT_AFTER_COLOR ) ;
      }
      result.append ( getHTMLCode ( this.expressionString ) ) ;
      if ( ( selection ) || ( this.expression instanceof BinaryOperator ) )
      {
        result.append ( FONT_BOLD_END ) ;
      }
      result.append ( END ) ;
      this.caption = result.toString ( ) ;
    }
    else
    {
      updateCaption ( OutlineNode.NO_SELECTION , OutlineNode.NO_SELECTION ,
          OutlineNode.NO_BINDING ) ;
    }
  }


  /**
   * Returns the binding in this node.
   * 
   * @return The binding in this node.
   * @see #outlineBinding
   * @see #setOutlineBinding(OutlineBinding)
   */
  public OutlineBinding getOutlineBinding ( )
  {
    return this.outlineBinding ;
  }


  /**
   * Returns the end index of the {@link Identifier}.
   * 
   * @return The end index of the {@link Identifier}.
   * @see #endIndex
   */
  public int getEndIndex ( )
  {
    return this.endIndex ;
  }


  /**
   * Returns the {@link Expression} repressented by this node.
   * 
   * @return The {@link Expression} in this node.
   * @see #expression
   */
  public Expression getExpression ( )
  {
    return this.expression ;
  }


  /**
   * Returns the expressionString.
   * 
   * @return The expressionString.
   * @see #expressionString
   */
  public String getExpressionString ( )
  {
    return this.expressionString ;
  }


  /**
   * Returns the hex value of a given integer.
   * 
   * @param pNumber The input integer value.
   * @return The hex value of a given integer.
   */
  private String getHex ( int pNumber )
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
   * Returns the HTML code of the given character.
   * 
   * @param pChar The character.
   * @return The HTML code of the given character.
   */
  private String getHTMLCode ( char pChar )
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
  private String getHTMLCode ( String pText )
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
  private String getHTMLFormat ( Color pColor )
  {
    return ( getHex ( pColor.getRed ( ) ) + getHex ( pColor.getGreen ( ) ) + getHex ( pColor
        .getBlue ( ) ) ) ;
  }


  /**
   * Returns the start index of the {@link Identifier}.
   * 
   * @return The start index of the {@link Identifier}.
   * @see #startIndex
   */
  public int getStartIndex ( )
  {
    return this.startIndex ;
  }


  /**
   * This method returns true if a given pCharIndex should be highlighted as a
   * binding. The pIdentifierIndex indicates in which list the pCharIndex should
   * be searched for. This is only used if an {@link Expression} has more than
   * one {@link Identifier} like {@link MultiLet}.
   * 
   * @param pIdentifierIndex The {@link Identifier} index in the
   *          {@link Expression}.
   * @param pCharIndex The index of the char in the {@link Expression}.
   * @return True, if a given pCharIndex should be highlighted as a binding,
   *         otherwise false.
   */
  private boolean isBinding ( int pIdentifierIndex , int pCharIndex )
  {
    if ( ( this.outlineBinding == null ) || ( pIdentifierIndex < 0 )
        || ( pIdentifierIndex >= this.outlineBinding.size ( ) ) )
    {
      return false ;
    }
    for ( int i = 0 ; i < this.outlineBinding.size ( pIdentifierIndex ) ; i ++ )
    {
      PrettyAnnotation prettyAnnotation = this.expression.toPrettyString ( )
          .getAnnotationForPrintable (
              this.outlineBinding.get ( pIdentifierIndex , i ) ) ;
      if ( ( pCharIndex >= prettyAnnotation.getStartOffset ( ) )
          && ( pCharIndex <= prettyAnnotation.getEndOffset ( ) ) )
      {
        return true ;
      }
    }
    return false ;
  }


  /**
   * This method returns true if a given pCharIndex should be highlighted as a
   * unbound {@link Identifier}.
   * 
   * @param pCharIndex pCharIndex The index of the char in the
   *          {@link Expression}.
   * @return True, if a given pCharIndex should be highlighted as a unbound
   *         Identifier, otherwise false.
   */
  private boolean isUnbound ( int pCharIndex )
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
          return true ;
        }
      }
      catch ( IllegalArgumentException e )
      {
        /*
         * Happens if the unbound Identifiers are not placed in this node.
         */
      }
    }
    return false ;
  }


  /**
   * Resets the caption of the node.
   */
  public final void resetCaption ( )
  {
    if ( this.startIndex != - 1 )
    {
      StringBuffer result = new StringBuffer ( HTML ) ;
      result.append ( this.description ) ;
      result.append ( BEGIN ) ;
      result.append ( getHTMLFormat ( Theme.currentTheme ( )
          .getExpressionColor ( ) ) ) ;
      result.append ( FONT_AFTER_COLOR ) ;
      if ( this.expression instanceof BinaryOperator )
      {
        result.append ( FONT_BOLD_BEGIN ) ;
        result.append ( getHTMLFormat ( Theme.currentTheme ( )
            .getConstantColor ( ) ) ) ;
        result.append ( FONT_AFTER_COLOR ) ;
        result.append ( getHTMLCode ( this.expressionString ) ) ;
        result.append ( FONT_BOLD_END ) ;
      }
      else
      {
        result.append ( getHTMLCode ( this.expressionString ) ) ;
      }
      result.append ( END ) ;
      this.caption = result.toString ( ) ;
    }
    else
    {
      updateCaption ( OutlineNode.NO_SELECTION , OutlineNode.NO_SELECTION ,
          OutlineNode.NO_BINDING ) ;
    }
  }


  /**
   * Sets the {@link OutlineBinding} in this node.
   * 
   * @param pOutlineBinding The {@link OutlineBinding} in this node.
   * @see #outlineBinding
   * @see #getOutlineBinding()
   */
  public void setOutlineBinding ( OutlineBinding pOutlineBinding )
  {
    this.outlineBinding = pOutlineBinding ;
  }


  /**
   * Set the value replaceInThisNode.
   * 
   * @param pReplaceInThisNode True, if the selected {@link Expression} should
   *          be replaced in this node.
   * @see #replaceInThisNode
   */
  public void setReplaceInThisNode ( boolean pReplaceInThisNode )
  {
    this.replaceInThisNode = pReplaceInThisNode ;
  }


  /**
   * Returns this <code>Object</code> as a <code>String</code>.
   * 
   * @see Object#toString()
   */
  @ Override
  public String toString ( )
  {
    return this.caption ;
  }


  /**
   * Updates the caption of the node. This method checks each character of the
   * name, if it is a keyword, a constant, a binding, selected or normal.
   * 
   * @param pSelectionStart The start offset of the selection in this node.
   * @param pSelectionEnd The end offset of the selection in this node.
   * @param pIdentifierIndex The index of the {@link Identifier}, used by
   *          {@link Expression}s which have more than more {@link Identifier}
   *          like {@link MultiLet}.
   */
  public void updateCaption ( int pSelectionStart , int pSelectionEnd ,
      int pIdentifierIndex )
  {
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
    result.append ( this.description ) ;
    result.append ( BEGIN ) ;
    result.append ( expressionColor ) ;
    result.append ( FONT_AFTER_COLOR ) ;
    int charIndex = 0 ;
    while ( charIndex < this.expressionString.length ( ) )
    {
      /*
       * Selection
       */
      if ( ( selection ) && ( charIndex == pSelectionStart ) )
      {
        result.append ( FONT_BOLD_BEGIN ) ;
        result.append ( selectionColor ) ;
        result.append ( FONT_AFTER_COLOR ) ;
        // Replace selected Expression
        if ( replace && this.replaceInThisNode )
        {
          result.append ( REPLACE ) ;
        }
        while ( charIndex <= pSelectionEnd )
        {
          // Do not replace selected Expression
          if ( ! ( replace && this.replaceInThisNode ) )
          {
            result.append ( getHTMLCode ( this.expressionString
                .charAt ( charIndex ) ) ) ;
          }
          // Next character
          charIndex ++ ;
          prettyCharIterator.next ( ) ;
        }
        result.append ( FONT_BOLD_END ) ;
      }
      /*
       * No selection and binding
       */
      else if ( ( ! selection ) && ( binding )
          && ( this.outlineBinding != null ) && ( pIdentifierIndex >= 0 )
          && ( this.outlineBinding.size ( pIdentifierIndex ) > 0 )
          && ( charIndex == pSelectionStart ) )
      {
        result.append ( FONT_BOLD_BEGIN ) ;
        result.append ( selectionColor ) ;
        result.append ( FONT_AFTER_COLOR ) ;
        // Replace selected Expression
        if ( replace && this.replaceInThisNode )
        {
          result.append ( REPLACE ) ;
        }
        while ( charIndex <= pSelectionEnd )
        {
          // Do not replace selected Expression
          if ( ! ( replace && this.replaceInThisNode ) )
          {
            result.append ( getHTMLCode ( this.expressionString
                .charAt ( charIndex ) ) ) ;
          }
          // Next character
          charIndex ++ ;
          prettyCharIterator.next ( ) ;
        }
        result.append ( FONT_BOLD_END ) ;
      }
      /*
       * No selection highlighting and displacement of the selected Expression
       * in higher nodes.
       */
      else if ( ( ! selection ) && ( replace ) && ( this.replaceInThisNode )
          && ( charIndex == pSelectionStart ) )
      {
        result.append ( REPLACE_BOLD ) ;
        while ( charIndex <= pSelectionEnd )
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
          && ( pIdentifierIndex >= 0 )
          && ( isBinding ( pIdentifierIndex , charIndex ) ) )
      {
        result.append ( FONT_BOLD_BEGIN ) ;
        result.append ( bindingColor ) ;
        result.append ( FONT_AFTER_COLOR ) ;
        while ( isBinding ( pIdentifierIndex , charIndex ) )
        {
          result.append ( getHTMLCode ( this.expressionString
              .charAt ( charIndex ) ) ) ;
          // Next character
          charIndex ++ ;
          prettyCharIterator.next ( ) ;
        }
        result.append ( FONT_BOLD_END ) ;
      }
      /*
       * Unbound
       */
      else if ( ( unbound ) && ( this.outlineUnbound != null )
          && ( isUnbound ( charIndex ) ) )
      {
        result.append ( FONT_BOLD_BEGIN ) ;
        result.append ( unboundColor ) ;
        result.append ( FONT_AFTER_COLOR ) ;
        while ( isUnbound ( charIndex ) )
        {
          result.append ( getHTMLCode ( this.expressionString
              .charAt ( charIndex ) ) ) ;
          // Next character
          charIndex ++ ;
          prettyCharIterator.next ( ) ;
        }
        result.append ( FONT_BOLD_END ) ;
      }
      /*
       * Keyword
       */
      else if ( PrettyStyle.KEYWORD.equals ( prettyCharIterator.getStyle ( ) ) )
      {
        result.append ( FONT_BOLD_BEGIN ) ;
        result.append ( keywordColor ) ;
        result.append ( FONT_AFTER_COLOR ) ;
        while ( PrettyStyle.KEYWORD.equals ( prettyCharIterator.getStyle ( ) ) )
        {
          result.append ( getHTMLCode ( this.expressionString
              .charAt ( charIndex ) ) ) ;
          // Next character
          charIndex ++ ;
          prettyCharIterator.next ( ) ;
        }
        result.append ( FONT_BOLD_END ) ;
      }
      /*
       * Constant
       */
      else if ( PrettyStyle.CONSTANT.equals ( prettyCharIterator.getStyle ( ) ) )
      {
        result.append ( FONT_BOLD_BEGIN ) ;
        result.append ( constantColor ) ;
        result.append ( FONT_AFTER_COLOR ) ;
        while ( PrettyStyle.CONSTANT.equals ( prettyCharIterator.getStyle ( ) ) )
        {
          result.append ( getHTMLCode ( this.expressionString
              .charAt ( charIndex ) ) ) ;
          // Next character
          charIndex ++ ;
          prettyCharIterator.next ( ) ;
        }
        result.append ( FONT_BOLD_END ) ;
      }
      /*
       * Type
       */
      else if ( PrettyStyle.TYPE.equals ( prettyCharIterator.getStyle ( ) ) )
      {
        result.append ( FONT_BOLD_BEGIN ) ;
        result.append ( typeColor ) ;
        result.append ( FONT_AFTER_COLOR ) ;
        while ( PrettyStyle.TYPE.equals ( prettyCharIterator.getStyle ( ) ) )
        {
          result.append ( getHTMLCode ( this.expressionString
              .charAt ( charIndex ) ) ) ;
          // Next character
          charIndex ++ ;
          prettyCharIterator.next ( ) ;
        }
        result.append ( FONT_BOLD_END ) ;
      }
      /*
       * Normal Character
       */
      else
      {
        result
            .append ( getHTMLCode ( this.expressionString.charAt ( charIndex ) ) ) ;
        // Next character
        charIndex ++ ;
        prettyCharIterator.next ( ) ;
      }
    }
    result.append ( END ) ;
    this.caption = result.toString ( ) ;
  }
}
