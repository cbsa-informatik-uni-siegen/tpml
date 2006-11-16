package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.awt.Color ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator ;
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle ;
import de.unisiegen.tpml.graphics.Theme ;


public class ASTNode
{
  private static final String BEFOR_DESCRIPTION = "" ;


  private static final String AFTER_DESCRIPTION = "" ;


  private static final String BETWEEN = "&nbsp;&nbsp;&nbsp;&nbsp;" ;


  private static final String BEFOR_NAME = "[&nbsp;" ;


  private static final String AFTER_NAME = "&nbsp;]" ;


  private static final String REPLACE_STRING = "..." ;


  private static boolean replaceGeneral = true ;


  private static boolean showBindings = true ;


  public static void setReplaceGeneral ( boolean pReplaceGeneral )
  {
    replaceGeneral = pReplaceGeneral ;
  }


  public static void setShowBindings ( boolean pShowBindings )
  {
    ASTNode.showBindings = pShowBindings ;
  }


  private boolean replace ;


  private String description ;


  private String expressionString ;


  private String caption ;


  private Expression expression ;


  private int startIndex ;


  private int endIndex ;


  private ASTBindings aSTBindings ;


  public ASTNode ( String pDescription , Expression pExpression )
  {
    this.description = pDescription ;
    this.expressionString = pExpression.toPrettyString ( ).toString ( ) ;
    this.expression = pExpression ;
    this.startIndex = - 1 ;
    this.endIndex = - 1 ;
    this.aSTBindings = null ;
    resetCaption ( ) ;
    this.replace = false ;
  }


  public ASTNode ( String pDescription , Expression pExpression ,
      ASTBindings pRelations )
  {
    this.description = pDescription ;
    this.expressionString = pExpression.toPrettyString ( ).toString ( ) ;
    this.expression = pExpression ;
    this.startIndex = - 1 ;
    this.endIndex = - 1 ;
    this.aSTBindings = pRelations ;
    resetCaption ( ) ;
    this.replace = false ;
  }


  public ASTNode ( String pDescription , String pExpressionString , int pStart ,
      int pEnd )
  {
    this.description = pDescription ;
    this.expressionString = pExpressionString ;
    this.expression = null ;
    this.startIndex = pStart ;
    this.endIndex = pEnd ;
    this.aSTBindings = null ;
    resetCaption ( ) ;
    this.replace = false ;
  }


  public int getEndIndex ( )
  {
    return this.endIndex ;
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


  public int getStartIndex ( )
  {
    return this.startIndex ;
  }


  private boolean isInList ( int pList , int pIndex )
  {
    if ( this.aSTBindings == null )
    {
      return false ;
    }
    if ( ( pList < 0 ) || ( pList >= this.aSTBindings.size ( ) ) )
    {
      return false ;
    }
    for ( int i = 0 ; i < this.aSTBindings.size ( pList ) ; i ++ )
    {
      Expression e = this.aSTBindings.get ( pList , i ) ;
      PrettyString prettyString = this.expression.toPrettyString ( ) ;
      PrettyAnnotation prettyAnnotation = prettyString
          .getAnnotationForPrintable ( e ) ;
      if ( ( pIndex >= prettyAnnotation.getStartOffset ( ) )
          && ( pIndex <= prettyAnnotation.getEndOffset ( ) ) )
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
      result.append ( BEFOR_DESCRIPTION ) ;
      result.append ( this.description ) ;
      result.append ( AFTER_DESCRIPTION ) ;
      result.append ( BETWEEN ) ;
      result.append ( BEFOR_NAME ) ;
      result.append ( this.expressionString ) ;
      result.append ( AFTER_NAME ) ;
      result.append ( "</html>" ) ;
      this.caption = result.toString ( ) ;
    }
    else
    {
      updateCaption ( - 1 , - 1 , - 1 ) ;
    }
  }


  public void setReplace ( boolean pReplace )
  {
    this.replace = pReplace ;
  }


  public void setSelectedCaption ( )
  {
    if ( this.expression == null )
    {
      StringBuffer result = new StringBuffer ( "<html>" ) ;
      result.append ( BEFOR_DESCRIPTION ) ;
      result.append ( this.description ) ;
      result.append ( AFTER_DESCRIPTION ) ;
      result.append ( BETWEEN ) ;
      result.append ( BEFOR_NAME ) ;
      result.append ( "<b><font color=\"#"
          + getHex ( Theme.currentTheme ( ).getSelectedColor ( ) ) + "\">" ) ;
      result.append ( this.expressionString ) ;
      result.append ( "</font></b>" ) ;
      result.append ( AFTER_NAME ) ;
      result.append ( "</html>" ) ;
      this.caption = result.toString ( ) ;
    }
    else
    {
      updateCaption ( - 1 , - 1 , - 1 ) ;
    }
  }


  @ Override
  public String toString ( )
  {
    return this.caption ;
  }


  public void updateCaption ( int pSelectionStart , int pSelectionEnd ,
      int pPrintBindings )
  {
    PrettyCharIterator p = this.expression.toPrettyString ( )
        .toCharacterIterator ( ) ;
    Color keywordColor = Theme.currentTheme ( ).getKeywordColor ( ) ;
    Color constantColor = Theme.currentTheme ( ).getConstantColor ( ) ;
    StringBuffer result = new StringBuffer ( ) ;
    result.append ( "<html>" ) ;
    result.append ( BEFOR_DESCRIPTION ) ;
    result.append ( this.description ) ;
    result.append ( AFTER_DESCRIPTION ) ;
    result.append ( BETWEEN ) ;
    result.append ( BEFOR_NAME ) ;
    p.first ( ) ;
    int index = 0 ;
    while ( index < this.expressionString.length ( ) )
    {
      // Selected
      if ( index == pSelectionStart )
      {
        result.append ( "<b><font color=\"#"
            + getHex ( Theme.currentTheme ( ).getSelectedColor ( ) ) + "\">" ) ;
        if ( replaceGeneral && this.replace )
        {
          result.append ( "&nbsp;" + REPLACE_STRING + "&nbsp;" ) ;
        }
        while ( index <= pSelectionEnd )
        {
          if ( ! ( replaceGeneral && this.replace ) )
          {
            result.append ( this.expressionString.charAt ( index ) ) ;
          }
          index ++ ;
          p.next ( ) ;
        }
        result.append ( "</font></b>" ) ;
      }
      // Binding
      else if ( ( showBindings ) && ( this.aSTBindings != null )
          && ( pPrintBindings >= 0 ) && ( isInList ( pPrintBindings , index ) ) )
      {
        result.append ( "<b><font color=\"#"
            + getHex ( Theme.currentTheme ( ).getBindingColor ( ) ) + "\">" ) ;
        while ( isInList ( pPrintBindings , index ) )
        {
          result.append ( this.expressionString.charAt ( index ) ) ;
          index ++ ;
          p.next ( ) ;
        }
        result.append ( "</font></b>" ) ;
      }
      // Keyword
      else if ( p.getStyle ( ) == PrettyStyle.KEYWORD )
      {
        result.append ( "<b><font color=\"#" + getHex ( keywordColor ) + "\">" ) ;
        while ( p.getStyle ( ) == PrettyStyle.KEYWORD )
        {
          result.append ( this.expressionString.charAt ( index ) ) ;
          index ++ ;
          p.next ( ) ;
        }
        result.append ( "</font></b>" ) ;
      }
      // Constant
      else if ( p.getStyle ( ) == PrettyStyle.CONSTANT )
      {
        result
            .append ( "<b><font color=\"#" + getHex ( constantColor ) + "\">" ) ;
        while ( p.getStyle ( ) == PrettyStyle.CONSTANT )
        {
          result.append ( this.expressionString.charAt ( index ) ) ;
          index ++ ;
          p.next ( ) ;
        }
        result.append ( "</font></b>" ) ;
      }
      // Else
      else
      {
        result.append ( this.expressionString.charAt ( index ) ) ;
        index ++ ;
        p.next ( ) ;
      }
    }
    result.append ( AFTER_NAME ) ;
    result.append ( "</html>" ) ;
    this.caption = result.toString ( ) ;
  }
}
