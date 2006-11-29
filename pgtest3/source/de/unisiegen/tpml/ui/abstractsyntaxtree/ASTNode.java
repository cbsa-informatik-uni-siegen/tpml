package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.awt.Color ;
import java.util.MissingResourceException ;
import java.util.ResourceBundle ;
import de.unisiegen.tpml.Debug ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle ;
import de.unisiegen.tpml.graphics.Theme ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.binding.ASTBinding ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.binding.ASTUnbound ;


/**
 * This class represents the nodes in the AbstractSyntaxTree.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTNode
{
  /**
   * No bindings should be shown in the nodes.
   */
  public static final int NO_BINDING = - 1 ;


  /**
   * Expressions should not be shown in the nodes.
   */
  public static final int NO_SELECTION = - 1 ;


  /**
   * String, placed before the description in the nodes.
   */
  private static final String BEFORE_DESCRIPTION = "" ;


  /**
   * String, placed after the description in the nodes.
   */
  private static final String AFTER_DESCRIPTION = "" ;


  /**
   * String, placed between the description and the name in the nodes.
   */
  private static final String BETWEEN = "&nbsp;&nbsp;&nbsp;&nbsp;" ;


  /**
   * String, placed before the name in the nodes.
   */
  private static final String BEFORE_NAME = "[&nbsp;" ;


  /**
   * String, placed after the name in the nodes.
   */
  private static final String AFTER_NAME = "&nbsp;]" ;


  /**
   * String, placed when a expression should be replaced
   */
  private static final String REPLACE_STRING = "..." ;


  /**
   * The selected expression should be highlighted in higher nodes.
   * 
   * @see #setSelection(boolean)
   */
  private static boolean selection = true ;


  /**
   * The selected expression should be replaced in higher nodes.
   * 
   * @see #setReplace(boolean)
   */
  private static boolean replace = true ;


  /**
   * Selected identifier and bindings should be highlighted in higher nodes.
   * 
   * @see #setBinding(boolean)
   */
  private static boolean binding = true ;


  /**
   * Unbound Identifiers should be highlighted in all nodes.
   * 
   * @see #setUnbound(boolean)
   */
  private static boolean unbound = true ;


  /**
   * Sets the binding value. Selected identifier and bindings should be
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
   * Sets the replace value. The selected expression should be replaced in
   * higher nodes.
   * 
   * @param pReplace Should or should not be replaced.
   * @see #replace
   */
  public static void setReplace ( boolean pReplace )
  {
    replace = pReplace ;
  }


  /**
   * Sets the selection value. The selected expression should be highlighted in
   * higher nodes.
   * 
   * @param pSelection Should or should not be highlighted.
   * @see #selection
   */
  public static void setSelection ( boolean pSelection )
  {
    selection = pSelection ;
  }


  /**
   * Sets the unbound value. Unbound Identifiers should be highlighted in all
   * nodes.
   * 
   * @param pUnbound Should or should not be highlighted.
   * @see #unbound
   */
  public static void setUnbound ( boolean pUnbound )
  {
    unbound = pUnbound ;
  }


  /**
   * The selected expression should be replaced in this node.
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
   * The expression as a string.
   */
  private String expressionString ;


  /**
   * The hole caption in html format.
   */
  private String caption ;


  /**
   * The expression repressented by this node.
   * 
   * @see #getExpression()
   */
  private Expression expression ;


  /**
   * The bindings in this node.
   * 
   * @see #getASTBinding()
   * @see #setASTBinding(ASTBinding)
   */
  private ASTBinding aSTBinding ;


  /**
   * TODO
   */
  private int startIndex ;


  /**
   * TODO
   */
  private int endIndex ;


  /**
   * The ASTUnbound which repressents the unbound Identifiers in all nodes.
   */
  private ASTUnbound aSTUnbound ;


  /**
   * The ResourceBundle, to set the description from the ast.properties.
   */
  private ResourceBundle resourceBundle ;


  /**
   * This method initializes the values and loads the description.
   * 
   * @param pExpression The expression repressented by this node.
   * @param pASTUnbound The ASTUnbound which repressents the unbound Identifiers
   *          in all nodes.
   */
  public ASTNode ( Expression pExpression , ASTUnbound pASTUnbound )
  {
    // Load the description
    this.resourceBundle = ResourceBundle
        .getBundle ( "de/unisiegen/tpml/ui/abstractsyntaxtree/ast" ) ;
    try
    {
      this.description = this.resourceBundle.getString ( pExpression
          .getClass ( ).getSimpleName ( ) ) ;
    }
    catch ( MissingResourceException e )
    {
      Debug.err.println ( "MissingResourceException: "
          + pExpression.getClass ( ).getSimpleName ( ) , Debug.CHRISTIAN ) ;
      this.description = pExpression.getClass ( ).getSimpleName ( ) ;
    }
    // Initialies the values
    this.expressionString = pExpression.toPrettyString ( ).toString ( ) ;
    this.expression = pExpression ;
    this.startIndex = - 1 ;
    this.endIndex = - 1 ;
    this.aSTBinding = null ;
    this.aSTUnbound = pASTUnbound ;
    this.replaceInThisNode = false ;
    resetCaption ( ) ;
  }


  /**
   * This method initializes the values and loads the description.
   * 
   * @param pDescription The description of this node.
   * @param pExpressionString The expression as a string.
   * @param pStartIndex TODO
   * @param pEndIndex TODO
   * @param pASTBinding The bindings in this node.
   * @param pASTUnbound The ASTUnbound which repressents the unbound Identifiers
   *          in all nodes
   */
  public ASTNode ( String pDescription , String pExpressionString ,
      int pStartIndex , int pEndIndex , ASTBinding pASTBinding ,
      ASTUnbound pASTUnbound )
  {
    // Preferences
    this.resourceBundle = ResourceBundle
        .getBundle ( "de/unisiegen/tpml/ui/abstractsyntaxtree/ast" ) ;
    try
    {
      this.description = this.resourceBundle.getString ( pDescription ) ;
    }
    catch ( MissingResourceException e )
    {
      Debug.err.println ( "MissingResourceException: " + pDescription ,
          Debug.CHRISTIAN ) ;
      this.description = pDescription ;
    }
    this.expressionString = pExpressionString ;
    this.expression = null ;
    this.startIndex = pStartIndex ;
    this.endIndex = pEndIndex ;
    this.aSTBinding = pASTBinding ;
    this.aSTUnbound = pASTUnbound ;
    this.replaceInThisNode = false ;
    resetCaption ( ) ;
  }


  /**
   * Insert a string before the current description.
   * 
   * @param pAppendDescription The string which should be inserted before the
   *          current description.
   * @see #description
   */
  public void appendDescription ( String pAppendDescription )
  {
    this.description = pAppendDescription + this.description ;
  }


  /**
   * Highlight the selected Identifier.
   */
  public void enableSelectedColor ( )
  {
    if ( this.expression == null )
    {
      StringBuffer result = new StringBuffer ( "<html>" ) ;
      result.append ( BEFORE_DESCRIPTION ) ;
      result.append ( this.description ) ;
      result.append ( AFTER_DESCRIPTION ) ;
      result.append ( BETWEEN ) ;
      result.append ( BEFORE_NAME ) ;
      result.append ( "<font color=\"#"
          + getHTMLFormat ( Theme.currentTheme ( ).getExpressionColor ( ) )
          + "\">" ) ;
      if ( selection )
      {
        result.append ( "<b><font color=\"#"
            + getHTMLFormat ( Theme.currentTheme ( ).getSelectionColor ( ) )
            + "\">" ) ;
      }
      result.append ( this.expressionString ) ;
      if ( selection )
      {
        result.append ( "</font></b>" ) ;
      }
      result.append ( "</font>" ) ;
      result.append ( AFTER_NAME ) ;
      result.append ( "</html>" ) ;
      this.caption = result.toString ( ) ;
    }
    else
    {
      updateCaption ( ASTNode.NO_SELECTION , ASTNode.NO_SELECTION ,
          ASTNode.NO_BINDING ) ;
    }
  }


  /**
   * Returns the binding in this node.
   * 
   * @return The binding in this node.
   * @see #aSTBinding
   * @see #setASTBinding(ASTBinding)
   */
  public ASTBinding getASTBinding ( )
  {
    return this.aSTBinding ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public int getEndIndex ( )
  {
    return this.endIndex ;
  }


  /**
   * Returns the expression repressented by this node.
   * 
   * @return The expression in this node.
   * @see #expression
   */
  public Expression getExpression ( )
  {
    return this.expression ;
  }


  /**
   * Returns the hex value of a given integer.
   * 
   * @param pNumber The input integer value.
   * @return The hex value of a given integer.
   */
  private String getHex ( int pNumber )
  {
    String result = "" ;
    String hex[] =
    { "0" , "1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9" , "A" , "B" ,
        "C" , "D" , "E" , "F" } ;
    int remainder = Math.abs ( pNumber ) ;
    int base = 0 ;
    if ( remainder > 0 )
    {
      while ( remainder > 0 )
      {
        base = remainder % 16 ;
        remainder = ( remainder / 16 ) ;
        result = hex [ base ] + result ;
      }
    }
    else
    {
      result = "00" ;
    }
    return result ;
  }


  /**
   * Returns the color in HTML formatting.
   * 
   * @param pColor The Color which should be returned.
   * @return The color in HTML formatting.
   */
  private String getHTMLFormat ( Color pColor )
  {
    return ( getHex ( pColor.getRed ( ) ) + getHex ( pColor.getGreen ( ) ) + getHex ( pColor
        .getBlue ( ) ) ) ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public int getStartIndex ( )
  {
    return this.startIndex ;
  }


  /**
   * This method returns true if a given pCharIndex should be highlighted as a
   * binding. The pIdentifierIndex indicates in which list the pCharIndex should
   * be searched for. This is only used if an Expression has more than one
   * Identifier like MultiLet.
   * 
   * @param pIdentifierIndex The Identifier index in the expression.
   * @param pCharIndex The index of the char in the expression.
   * @return True, if a given pCharIndex should be highlighted as a binding,
   *         otherwise false.
   */
  private boolean isBinding ( int pIdentifierIndex , int pCharIndex )
  {
    if ( ( this.aSTBinding == null ) || ( pIdentifierIndex < 0 )
        || ( pIdentifierIndex >= this.aSTBinding.size ( ) ) )
    {
      return false ;
    }
    for ( int i = 0 ; i < this.aSTBinding.size ( pIdentifierIndex ) ; i ++ )
    {
      PrettyAnnotation prettyAnnotation = this.expression.toPrettyString ( )
          .getAnnotationForPrintable (
              this.aSTBinding.get ( pIdentifierIndex , i ) ) ;
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
   * unbound Identifier.
   * 
   * @param pCharIndex pCharIndex The index of the char in the expression.
   * @return True, if a given pCharIndex should be highlighted as a unbound
   *         Identifier, otherwise false.
   */
  private boolean isUnbound ( int pCharIndex )
  {
    for ( int i = 0 ; i < this.aSTUnbound.size ( ) ; i ++ )
    {
      try
      {
        PrettyAnnotation prettyAnnotation = this.expression.toPrettyString ( )
            .getAnnotationForPrintable ( this.aSTUnbound.get ( i ) ) ;
        if ( ( pCharIndex >= prettyAnnotation.getStartOffset ( ) )
            && ( pCharIndex <= prettyAnnotation.getEndOffset ( ) ) )
        {
          return true ;
        }
      }
      catch ( IllegalArgumentException e )
      {
        // Happens if the unbound Identifiers are not placed in this node.
      }
    }
    return false ;
  }


  /**
   * Resets the caption of the node.
   */
  public void resetCaption ( )
  {
    if ( this.expression == null )
    {
      StringBuffer result = new StringBuffer ( "<html>" ) ;
      result.append ( BEFORE_DESCRIPTION ) ;
      result.append ( this.description ) ;
      result.append ( AFTER_DESCRIPTION ) ;
      result.append ( BETWEEN ) ;
      result.append ( BEFORE_NAME ) ;
      result.append ( "<font color=\"#"
          + getHTMLFormat ( Theme.currentTheme ( ).getExpressionColor ( ) )
          + "\">" ) ;
      result.append ( this.expressionString ) ;
      result.append ( "</font>" ) ;
      result.append ( AFTER_NAME ) ;
      result.append ( "</html>" ) ;
      this.caption = result.toString ( ) ;
    }
    else
    {
      updateCaption ( ASTNode.NO_SELECTION , ASTNode.NO_SELECTION ,
          ASTNode.NO_BINDING ) ;
    }
  }


  /**
   * Sets the binding in this node.
   * 
   * @param pASTBinding The ASTBinding in this node.
   * @see #aSTBinding
   * @see #getASTBinding()
   */
  public void setASTBinding ( ASTBinding pASTBinding )
  {
    this.aSTBinding = pASTBinding ;
  }


  /**
   * Set the value replaceInThisNode.
   * 
   * @param pReplaceInThisNode True, if the selected expression should be
   *          replaced in this node.
   * @see #replaceInThisNode
   */
  public void setReplaceInThisNode ( boolean pReplaceInThisNode )
  {
    this.replaceInThisNode = pReplaceInThisNode ;
  }


  /**
   * Returns this Object as a String.
   * 
   * @see java.lang.Object#toString()
   */
  @ Override
  public String toString ( )
  {
    return this.caption ;
  }


  /**
   * Updates the caption of the node. This methode checks each character of the
   * name, if it is a keyword, a constant, a binding, selected or normal.
   * 
   * @param pSelectionStart The start offset of the selection in this node.
   * @param pSelectionEnd The end offset of the selection in this node.
   * @param pIdentifierIndex The index of the Identifier, used by Expressions
   *          which have more than more Identifier like MultiLet.
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
    result.append ( "<html>" ) ;
    result.append ( BEFORE_DESCRIPTION ) ;
    result.append ( this.description ) ;
    result.append ( AFTER_DESCRIPTION ) ;
    result.append ( BETWEEN ) ;
    result.append ( BEFORE_NAME ) ;
    result.append ( "<font color=\"#" + expressionColor + "\">" ) ;
    // Set the position to the first character
    prettyCharIterator.first ( ) ;
    int charIndex = 0 ;
    while ( charIndex < this.expressionString.length ( ) )
    {
      // Selection
      if ( ( selection || binding ) && ( charIndex == pSelectionStart ) )
      {
        /*
         * Highlight Identifier in bindingColor, if selection is deactivated.
         * Otherwise in selectionColor.
         */
        if ( ( ! selection ) && ( binding ) )
        {
          result.append ( "<b><font color=\"#" + bindingColor + "\">" ) ;
        }
        else
        {
          result.append ( "<b><font color=\"#" + selectionColor + "\">" ) ;
        }
        // Replace selected Expression
        if ( replace && this.replaceInThisNode )
        {
          result.append ( "&nbsp;" + REPLACE_STRING + "&nbsp;" ) ;
        }
        while ( charIndex <= pSelectionEnd )
        {
          // Do not replace selected Expression
          if ( ! ( replace && this.replaceInThisNode ) )
          {
            result.append ( this.expressionString.charAt ( charIndex ) ) ;
          }
          // Next character
          charIndex ++ ;
          prettyCharIterator.next ( ) ;
        }
        result.append ( "</font></b>" ) ;
      }
      /*
       * No selection highlighting and displacement of the selected Expression
       * in higher nodes.
       */
      else if ( ! ( selection ) && ( replace ) && ( this.replaceInThisNode )
          && ( charIndex == pSelectionStart ) )
      {
        result.append ( "<b>&nbsp;" + REPLACE_STRING + "&nbsp;</b>" ) ;
        while ( charIndex <= pSelectionEnd )
        {
          // Next character
          charIndex ++ ;
          prettyCharIterator.next ( ) ;
        }
      }
      // Binding
      else if ( ( binding ) && ( this.aSTBinding != null )
          && ( pIdentifierIndex >= 0 )
          && ( isBinding ( pIdentifierIndex , charIndex ) ) )
      {
        result.append ( "<b><font color=\"#" + bindingColor + "\">" ) ;
        while ( isBinding ( pIdentifierIndex , charIndex ) )
        {
          result.append ( this.expressionString.charAt ( charIndex ) ) ;
          // Next character
          charIndex ++ ;
          prettyCharIterator.next ( ) ;
        }
        result.append ( "</font></b>" ) ;
      }
      // Unbound
      else if ( ( unbound ) && ( this.aSTUnbound != null )
          && ( isUnbound ( charIndex ) ) )
      {
        result.append ( "<b><font color=\"#" + unboundColor + "\">" ) ;
        while ( isUnbound ( charIndex ) )
        {
          result.append ( this.expressionString.charAt ( charIndex ) ) ;
          // Next character
          charIndex ++ ;
          prettyCharIterator.next ( ) ;
        }
        result.append ( "</font></b>" ) ;
      }
      // Keyword
      else if ( prettyCharIterator.getStyle ( ) == PrettyStyle.KEYWORD )
      {
        result.append ( "<b><font color=\"#" + keywordColor + "\">" ) ;
        while ( prettyCharIterator.getStyle ( ) == PrettyStyle.KEYWORD )
        {
          result.append ( this.expressionString.charAt ( charIndex ) ) ;
          // Next character
          charIndex ++ ;
          prettyCharIterator.next ( ) ;
        }
        result.append ( "</font></b>" ) ;
      }
      // Constant
      else if ( prettyCharIterator.getStyle ( ) == PrettyStyle.CONSTANT )
      {
        result.append ( "<b><font color=\"#" + constantColor + "\">" ) ;
        while ( prettyCharIterator.getStyle ( ) == PrettyStyle.CONSTANT )
        {
          result.append ( this.expressionString.charAt ( charIndex ) ) ;
          // Next character
          charIndex ++ ;
          prettyCharIterator.next ( ) ;
        }
        result.append ( "</font></b>" ) ;
      }
      // Type
      else if ( prettyCharIterator.getStyle ( ) == PrettyStyle.TYPE )
      {
        result.append ( "<b><font color=\"#" + typeColor + "\">" ) ;
        while ( prettyCharIterator.getStyle ( ) == PrettyStyle.TYPE )
        {
          result.append ( this.expressionString.charAt ( charIndex ) ) ;
          // Next character
          charIndex ++ ;
          prettyCharIterator.next ( ) ;
        }
        result.append ( "</font></b>" ) ;
      }
      // Normal Character
      else
      {
        result.append ( this.expressionString.charAt ( charIndex ) ) ;
        // Next character
        charIndex ++ ;
        prettyCharIterator.next ( ) ;
      }
    }
    result.append ( "</font>" ) ;
    result.append ( AFTER_NAME ) ;
    result.append ( "</html>" ) ;
    this.caption = result.toString ( ) ;
  }
}
