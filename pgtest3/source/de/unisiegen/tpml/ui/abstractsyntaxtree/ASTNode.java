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


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTNode
{
  /**
   * TODO
   */
  public static final int NO_BINDING = - 1 ;


  /**
   * TODO
   */
  public static final int NO_SELECTION = - 1 ;


  /**
   * TODO
   */
  private static final String BEFORE_DESCRIPTION = "" ;


  /**
   * TODO
   */
  private static final String AFTER_DESCRIPTION = "" ;


  /**
   * TODO
   */
  private static final String BETWEEN = "&nbsp;&nbsp;&nbsp;&nbsp;" ;


  /**
   * TODO
   */
  private static final String BEFORE_NAME = "[&nbsp;" ;


  /**
   * TODO
   */
  private static final String AFTER_NAME = "&nbsp;]" ;


  /**
   * TODO
   */
  private static final String REPLACE_STRING = "..." ;


  /**
   * TODO
   */
  private static boolean selection = true ;


  /**
   * TODO
   */
  private static boolean replace = true ;


  /**
   * TODO
   */
  private static boolean binding = true ;


  /**
   * TODO
   * 
   * @param pBinding
   */
  public static void setBinding ( boolean pBinding )
  {
    binding = pBinding ;
  }


  /**
   * TODO
   * 
   * @param pReplace
   */
  public static void setReplace ( boolean pReplace )
  {
    replace = pReplace ;
  }


  /**
   * TODO
   * 
   * @param pSelection
   */
  public static void setSelection ( boolean pSelection )
  {
    selection = pSelection ;
  }


  /**
   * TODO
   */
  private boolean replaceInThisNode ;


  /**
   * TODO
   */
  private String description ;


  /**
   * TODO
   */
  private String expressionString ;


  /**
   * TODO
   */
  private String caption ;


  /**
   * TODO
   */
  private Expression expression ;


  /**
   * TODO
   */
  private ASTBinding aSTBinding ;


  /**
   * TODO
   */
  private ASTPair aSTPair ;


  /**
   * TODO
   */
  private ResourceBundle resourceBundle ;


  /**
   * TODO
   * 
   * @param pExpression
   */
  public ASTNode ( Expression pExpression )
  {
    // Preferences
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
          + pExpression.getClass ( ).getSimpleName ( ) , "christian" ) ;
      this.description = pExpression.getClass ( ).getSimpleName ( ) ;
    }
    this.expressionString = pExpression.toPrettyString ( ).toString ( ) ;
    this.expression = pExpression ;
    this.aSTPair = null ;
    this.aSTBinding = null ;
    this.replaceInThisNode = false ;
    resetCaption ( ) ;
  }


  /**
   * TODO
   * 
   * @param pDescription
   * @param pExpressionString
   * @param pASTPair
   * @param pASTBindings
   */
  public ASTNode ( String pDescription , String pExpressionString ,
      ASTPair pASTPair , ASTBinding pASTBindings )
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
          "christian" ) ;
      this.description = pDescription ;
    }
    this.expressionString = pExpressionString ;
    this.expression = null ;
    this.aSTPair = pASTPair ;
    this.aSTBinding = pASTBindings ;
    this.replaceInThisNode = false ;
    resetCaption ( ) ;
  }


  /**
   * TODO
   * 
   * @param pDescription
   */
  public void appendDescription ( String pDescription )
  {
    this.description = pDescription + this.description ;
  }


  /**
   * TODO
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
          + getHex ( Theme.currentTheme ( ).getExpressionColor ( ) ) + "\">" ) ;
      if ( selection )
      {
        result.append ( "<b><font color=\"#"
            + getHex ( Theme.currentTheme ( ).getSelectionColor ( ) ) + "\">" ) ;
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
   * TODO
   * 
   * @return TODO
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
  public ASTPair getASTPair ( )
  {
    return this.aSTPair ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public Expression getExpression ( )
  {
    return this.expression ;
  }


  /**
   * TODO
   * 
   * @param pColor
   * @return TODO
   */
  private String getHex ( Color pColor )
  {
    return ( getHex ( pColor.getRed ( ) ) + getHex ( pColor.getGreen ( ) ) + getHex ( pColor
        .getBlue ( ) ) ) ;
  }


  /**
   * TODO
   * 
   * @param pNumber
   * @return TODO
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
   * TODO
   * 
   * @param pBindingIndex
   * @param pCharIndex
   * @return TODO
   */
  private boolean isBinding ( int pBindingIndex , int pCharIndex )
  {
    if ( ( this.aSTBinding == null ) || ( pBindingIndex < 0 )
        || ( pBindingIndex >= this.aSTBinding.size ( ) ) )
    {
      return false ;
    }
    for ( int i = 0 ; i < this.aSTBinding.size ( pBindingIndex ) ; i ++ )
    {
      PrettyAnnotation prettyAnnotation = this.expression
          .toPrettyString ( )
          .getAnnotationForPrintable ( this.aSTBinding.get ( pBindingIndex , i ) ) ;
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
  @ SuppressWarnings ( "unused" )
  private boolean isNoBinding ( int pCharIndex )
  {
    if ( ( this.aSTBinding == null )
        || ( this.aSTBinding.getNoBindingSize ( ) == 0 ) )
    {
      return false ;
    }
    for ( int i = 0 ; i < this.aSTBinding.getNoBindingSize ( ) ; i ++ )
    {
      PrettyAnnotation prettyAnnotation = this.expression.toPrettyString ( )
          .getAnnotationForPrintable ( this.aSTBinding.getNoBinding ( i ) ) ;
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
          + getHex ( Theme.currentTheme ( ).getExpressionColor ( ) ) + "\">" ) ;
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
   * TODO
   * 
   * @param pASTBinding
   */
  public void setASTBinding ( ASTBinding pASTBinding )
  {
    this.aSTBinding = pASTBinding ;
  }


  /**
   * TODO
   * 
   * @param pReplaceInThisNode
   */
  public void setReplaceInThisNode ( boolean pReplaceInThisNode )
  {
    this.replaceInThisNode = pReplaceInThisNode ;
  }


  /**
   * TODO
   * 
   * @see java.lang.Object#toString()
   */
  @ Override
  public String toString ( )
  {
    return this.caption ;
  }


  /**
   * TODO
   * 
   * @param pSelectionStart
   * @param pSelectionEnd
   * @param pBindingIndex
   */
  public void updateCaption ( int pSelectionStart , int pSelectionEnd ,
      int pBindingIndex )
  {
    PrettyCharIterator prettyCharIterator = this.expression.toPrettyString ( )
        .toCharacterIterator ( ) ;
    String expressionColor = getHex ( Theme.currentTheme ( )
        .getExpressionColor ( ) ) ;
    String keywordColor = getHex ( Theme.currentTheme ( ).getKeywordColor ( ) ) ;
    String constantColor = getHex ( Theme.currentTheme ( ).getConstantColor ( ) ) ;
    String selectionColor = getHex ( Theme.currentTheme ( )
        .getSelectionColor ( ) ) ;
    String bindingColor = getHex ( Theme.currentTheme ( ).getBindingColor ( ) ) ;
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
          && ( pBindingIndex >= 0 )
          && ( isBinding ( pBindingIndex , charIndex ) ) )
      {
        result.append ( "<b><font color=\"#" + bindingColor + "\">" ) ;
        while ( isBinding ( pBindingIndex , charIndex ) )
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
      // Maybe later for highlighting unbound Indentifiers
      /*
       * else if ( ( this.aSTBinding != null ) && (
       * this.aSTBinding.getNoBindingSize ( ) > 0 ) && ( isNoBinding ( charIndex ) ) ) {
       * Debug.out.println ( charIndex , "christian" ) ; result.append ( "<b><font
       * color=\"#" + "00FF00" + "\">" ) ; while ( isNoBinding ( charIndex ) ) {
       * result.append ( this.expressionString.charAt ( charIndex ) ) ; // Next
       * character charIndex ++ ; prettyCharIterator.next ( ) ; } result.append ( "</font></b>" ) ; }
       */
      // Else
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
