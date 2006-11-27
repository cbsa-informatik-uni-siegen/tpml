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
import de.unisiegen.tpml.ui.abstractsyntaxtree.binding.ASTPair ;
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
   */
  private static boolean selection = true ;


  /**
   * The selected expression should be replaced in higher nodes.
   */
  private static boolean replace = true ;


  /**
   * Selected identifier and bindings should be highlighted in higher nodes.
   */
  private static boolean binding = true ;


  /**
   * TODO
   */
  private static boolean unbound = true ;


  /**
   * Sets the binding value. Selected identifier and bindings should be
   * highlighted in higher nodes.
   * 
   * @param pBinding Should be highlighted or should not be highlighted.
   */
  public static void setBinding ( boolean pBinding )
  {
    binding = pBinding ;
  }


  /**
   * Sets the replace value. The selected expression should be replaced in
   * higher nodes.
   * 
   * @param pReplace Should be replaced or should not be replaced.
   */
  public static void setReplace ( boolean pReplace )
  {
    replace = pReplace ;
  }


  /**
   * Sets the selection value. The selected expression should be highlighted in
   * higher nodes.
   * 
   * @param pSelection
   */
  public static void setSelection ( boolean pSelection )
  {
    selection = pSelection ;
  }


  /**
   * TODO
   * 
   * @param pUnbound
   */
  public static void setUnbound ( boolean pUnbound )
  {
    unbound = pUnbound ;
  }


  /**
   * The selected expression should be replaced in this node.
   */
  private boolean replaceInThisNode ;


  /**
   * The description of the node.
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
   */
  private Expression expression ;


  /**
   * The bindings in this node.
   */
  private ASTBinding aSTBinding ;


  /**
   * The ASTPair which repressents the start and the end offset of Identifiers
   * in the node.
   */
  private ASTPair aSTPair ;


  /**
   * TODO
   */
  private ASTUnbound aSTUnbound ;


  /**
   * The ResourceBundle, to set the description from the ast.properties.
   */
  private ResourceBundle resourceBundle ;


  /**
   * Initialies the values and loads the description.
   * 
   * @param pExpression The expression repressented by this node.
   * @param pASTUnbound TODO
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
    this.aSTPair = null ;
    this.aSTBinding = null ;
    this.aSTUnbound = pASTUnbound ;
    this.replaceInThisNode = false ;
    resetCaption ( ) ;
  }


  /**
   * Initialies the values and loads the description.
   * 
   * @param pDescription The description of this node.
   * @param pExpressionString The expression as a string.
   * @param pASTPair The ASTPair which repressent the start and the end offset
   *        of Identifiers in the node.
   * @param pASTBinding The bindings in this node.
   * @param pASTUnbound TODO
   */
  public ASTNode ( String pDescription , String pExpressionString ,
      ASTPair pASTPair , ASTBinding pASTBinding , ASTUnbound pASTUnbound )
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
    this.aSTPair = pASTPair ;
    this.aSTBinding = pASTBinding ;
    this.aSTUnbound = pASTUnbound ;
    this.replaceInThisNode = false ;
    resetCaption ( ) ;
  }


  /**
   * Insert a string before the current description.
   * 
   * @param pAppendDescription The string which should be inserted before the
   *        current description.
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
   */
  public ASTBinding getASTBinding ( )
  {
    return this.aSTBinding ;
  }


  /**
   * Returns the ASTPair which repressents the start and the end offset of
   * Identifiers in the node.
   * 
   * @return The ASTPair in this node.
   */
  public ASTPair getASTPair ( )
  {
    return this.aSTPair ;
  }


  /**
   * Returns the expression repressented by this node.
   * 
   * @return The expression in this node.
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
   * This method returns true if a given pCharIndex should be highlighted as a
   * binding. The pIdentifierIndex indicates in which list the pCharIndex should
   * be searched for. This is only used if an Expression has more than one
   * Identifier like MultiLet.
   * 
   * @param pIdentifierIndex The Identifier index in the expression.
   * @param pCharIndex The index of the char in the expression.
   * @return True, if a given pCharIndex should be highlighted as a binding.
   *         Otherwise false.
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
   * TODO
   * 
   * @param pCharIndex
   * @return TODO
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
   */
  public void setASTBinding ( ASTBinding pASTBinding )
  {
    this.aSTBinding = pASTBinding ;
  }


  /**
   * Set the value replaceInThisNode.
   * 
   * @param pReplaceInThisNode True, if the selected expression should be
   *        replaced in this node.
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
   *        which have more than more Identifier like MultiLet.
   */
  public void updateCaption ( int pSelectionStart , int pSelectionEnd ,
      int pIdentifierIndex )
  {
    PrettyCharIterator prettyCharIterator = this.expression.toPrettyString ( )
        .toCharacterIterator ( ) ;
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
    StringBuffer result = new StringBuffer ( ) ;
    result.append ( "<html>" ) ;
    result.append ( BEFORE_DESCRIPTION ) ;
    result.append ( this.description ) ;
    result.append ( AFTER_DESCRIPTION ) ;
    result.append ( BETWEEN ) ;
    result.append ( BEFORE_NAME ) ;
    result.append ( "<font color=\"#" + expressionColor + "\">" ) ;
    prettyCharIterator.first ( ) ;
    int charIndex = 0 ;
    while ( charIndex < this.expressionString.length ( ) )
    {
      // Selection
      if ( ( selection ) && ( charIndex == pSelectionStart ) )
      {
        result.append ( "<b><font color=\"#" + selectionColor + "\">" ) ;
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
      // No selection highlighting and replace the selection
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
