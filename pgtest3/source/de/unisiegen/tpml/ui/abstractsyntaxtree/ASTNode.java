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


public class ASTNode
{
  public static final int NO_BINDING = - 1 ;


  public static final int NO_SELECTION = - 1 ;


  private static final String BEFORE_DESCRIPTION = "" ;


  private static final String AFTER_DESCRIPTION = "" ;


  private static final String BETWEEN = "&nbsp;&nbsp;&nbsp;&nbsp;" ;


  private static final String BEFOR_NAME = "[&nbsp;" ;


  private static final String AFTER_NAME = "&nbsp;]" ;


  private static final String REPLACE_STRING = "..." ;


  private static boolean selection = true ;


  private static boolean replace = true ;


  private static boolean binding = true ;


  public static void setBinding ( boolean pBinding )
  {
    binding = pBinding ;
  }


  public static void setReplace ( boolean pReplace )
  {
    replace = pReplace ;
  }


  public static void setSelection ( boolean pSelection )
  {
    selection = pSelection ;
  }


  private boolean replaceInThisNode ;


  private String description ;


  private String expressionString ;


  private String caption ;


  private Expression expression ;


  private ASTBinding aSTBinding ;


  private ASTPair aSTPair ;


  private ResourceBundle resourceBundle ;


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


  public void appendDescription ( String pDescription )
  {
    this.description = pDescription + this.description ;
  }


  public void enableSelectedColor ( )
  {
    if ( this.expression == null )
    {
      StringBuffer result = new StringBuffer ( "<html>" ) ;
      result.append ( BEFORE_DESCRIPTION ) ;
      result.append ( this.description ) ;
      result.append ( AFTER_DESCRIPTION ) ;
      result.append ( BETWEEN ) ;
      result.append ( BEFOR_NAME ) ;
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


  public ASTBinding getASTBinding ( )
  {
    return this.aSTBinding ;
  }


  public ASTPair getASTPair ( )
  {
    return this.aSTPair ;
  }


  public Expression getExpression ( )
  {
    return this.expression ;
  }


  private String getHex ( Color pColor )
  {
    return ( getHex ( pColor.getRed ( ) ) + getHex ( pColor.getGreen ( ) ) + getHex ( pColor
        .getBlue ( ) ) ) ;
  }


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


  public void resetCaption ( )
  {
    if ( this.expression == null )
    {
      StringBuffer result = new StringBuffer ( "<html>" ) ;
      result.append ( BEFORE_DESCRIPTION ) ;
      result.append ( this.description ) ;
      result.append ( AFTER_DESCRIPTION ) ;
      result.append ( BETWEEN ) ;
      result.append ( BEFOR_NAME ) ;
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


  public void setASTBinding ( ASTBinding pASTBinding )
  {
    this.aSTBinding = pASTBinding ;
  }


  public void setReplaceInThisNode ( boolean pReplaceExpression )
  {
    this.replaceInThisNode = pReplaceExpression ;
  }


  @ Override
  public String toString ( )
  {
    return this.caption ;
  }


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
    result.append ( BEFOR_NAME ) ;
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
