package de.unisiegen.tpml.ui ;


import java.awt.Color ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle ;
import de.unisiegen.tpml.graphics.Theme ;


public class AbstractSyntaxTreeNode
{
  private static final String BEFOR_DESCRIPTION = "" ;


  private static final String AFTER_DESCRIPTION = "" ;


  private static final String BETWEEN = "&nbsp;&nbsp;&nbsp;&nbsp;" ;


  private static final String BEFOR_NAME = "[&nbsp;" ;


  private static final String AFTER_NAME = "&nbsp;]" ;


  private String description ;


  private String name ;


  private String html ;


  private Expression expression ;


  private int startIndex ;


  private int endIndex ;


  private Color CURRENT = new Color ( 225 , 0 , 0 ) ;


  public AbstractSyntaxTreeNode ( String pDescription , Expression pExpression )
  {
    this.description = pDescription ;
    this.name = pExpression.toPrettyString ( ).toString ( ) ;
    this.expression = pExpression ;
    this.startIndex = - 1 ;
    this.endIndex = - 1 ;
    resetHtml ( ) ;
  }


  public AbstractSyntaxTreeNode ( String pDescription , String pName ,
      int pStart , int pEnd )
  {
    this.description = pDescription ;
    this.name = pName ;
    this.expression = null ;
    this.startIndex = pStart ;
    this.endIndex = pEnd ;
    resetHtml ( ) ;
  }


  public int getEndIndex ( )
  {
    return endIndex ;
  }


  public Expression getExpression ( )
  {
    return expression ;
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
    return startIndex ;
  }


  public void resetHtml ( )
  {
    if ( this.expression == null )
    {
      StringBuffer result = new StringBuffer ( "<html>" ) ;
      result.append ( BEFOR_DESCRIPTION ) ;
      result.append ( this.description ) ;
      result.append ( AFTER_DESCRIPTION ) ;
      result.append ( BETWEEN ) ;
      result.append ( BEFOR_NAME ) ;
      result.append ( this.name ) ;
      result.append ( AFTER_NAME ) ;
      result.append ( "</html>" ) ;
      this.html = result.toString ( ) ;
    }
    else
    {
      updateHtml ( - 1 , - 1 ) ;
    }
  }


  public String toString ( )
  {
    return this.html ;
  }


  public void updateHtml ( int pStart , int pEnd )
  {
    PrettyCharIterator p = this.expression.toPrettyString ( )
        .toCharacterIterator ( ) ;
    Color keyword = Theme.currentTheme ( ).getKeywordColor ( ) ;
    Color constant = Theme.currentTheme ( ).getConstantColor ( ) ;
    Color comment = Theme.currentTheme ( ).getCommentColor ( ) ;
    Color type = Theme.currentTheme ( ).getTypeColor ( ) ;
    Color none = Theme.currentTheme ( ).getExpressionColor ( ) ;
    StringBuffer result = new StringBuffer ( "<html>" ) ;
    result.append ( BEFOR_DESCRIPTION ) ;
    result.append ( this.description ) ;
    result.append ( AFTER_DESCRIPTION ) ;
    result.append ( BETWEEN ) ;
    result.append ( BEFOR_NAME ) ;
    for ( int i = 0 ; i < this.name.length ( ) ; i ++ )
    {
      // None
      if ( p.getStyle ( ) == PrettyStyle.NONE )
      {
        result.append ( "<font color=\"#" + getHex ( none ) + "\">" ) ;
      }
      // Type
      if ( p.getStyle ( ) == PrettyStyle.TYPE )
      {
        result.append ( "<font color=\"#" + getHex ( type ) + "\">" ) ;
      }
      // Comment
      if ( p.getStyle ( ) == PrettyStyle.COMMENT )
      {
        result.append ( "<font color=\"#" + getHex ( comment ) + "\">" ) ;
      }
      // Constant
      if ( p.getStyle ( ) == PrettyStyle.CONSTANT )
      {
        result.append ( "<font color=\"#" + getHex ( constant ) + "\">" ) ;
      }
      // Keyword
      if ( p.getStyle ( ) == PrettyStyle.KEYWORD )
      {
        result.append ( "<b><font color=\"#" + getHex ( keyword ) + "\">" ) ;
      }
      // Current Expression
      if ( ( pStart == - 1 ) || ( pEnd == - 1 ) || ( i < pStart )
          || ( i > pEnd ) )
      {
        result.append ( this.name.charAt ( i ) ) ;
      }
      else if ( ( i >= pStart ) && ( i <= pEnd ) )
      {
        result.append ( "<b><font color=\"#" + getHex ( CURRENT ) + "\">" ) ;
        result.append ( this.name.charAt ( i ) ) ;
        result.append ( "</font></b>" ) ;
      }
      // Keyword
      if ( p.getStyle ( ) == PrettyStyle.KEYWORD )
      {
        result.append ( "</font></b>" ) ;
      }
      // Constant
      if ( p.getStyle ( ) == PrettyStyle.CONSTANT )
      {
        result.append ( "</font>" ) ;
      }
      // Comment
      if ( p.getStyle ( ) == PrettyStyle.COMMENT )
      {
        result.append ( "</font>" ) ;
      }
      // Type
      if ( p.getStyle ( ) == PrettyStyle.TYPE )
      {
        result.append ( "</font>" ) ;
      }
      // None
      if ( p.getStyle ( ) == PrettyStyle.NONE )
      {
        result.append ( "</font>" ) ;
      }
      p.next ( ) ;
    }
    result.append ( AFTER_NAME ) ;
    result.append ( "</html>" ) ;
    this.html = result.toString ( ) ;
  }
}
