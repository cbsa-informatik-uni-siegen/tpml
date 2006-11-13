package de.unisiegen.tpml.ui ;


import java.awt.Color ;
import java.util.LinkedList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator ;
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;
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


  private LinkedList < Expression > relations ;


  private Color CURRENT = new Color ( 0 , 0 , 255 ) ;


  public AbstractSyntaxTreeNode ( String pDescription , Expression pExpression )
  {
    this.description = pDescription ;
    this.name = pExpression.toPrettyString ( ).toString ( ) ;
    this.expression = pExpression ;
    this.startIndex = - 1 ;
    this.endIndex = - 1 ;
    resetHtml ( ) ;
  }


  public AbstractSyntaxTreeNode ( String pDescription , Expression pExpression ,
      LinkedList < Expression > pRelations )
  {
    this.description = pDescription ;
    this.name = pExpression.toPrettyString ( ).toString ( ) ;
    this.expression = pExpression ;
    this.startIndex = - 1 ;
    this.endIndex = - 1 ;
    this.relations = pRelations ;
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


  public AbstractSyntaxTreeNode ( String pDescription , String pName ,
      int pStart , int pEnd , LinkedList < Expression > pRelations )
  {
    this.description = pDescription ;
    this.name = pName ;
    this.expression = null ;
    this.startIndex = pStart ;
    this.endIndex = pEnd ;
    this.relations = pRelations ;
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


  private boolean isInList ( int pIndex )
  {
    if ( this.relations == null ) return false ;
    for ( Expression e : this.relations )
    {
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


  public void updateHtml ( int pStart , int pEnd )
  {
    PrettyCharIterator p = this.expression.toPrettyString ( )
        .toCharacterIterator ( ) ;
    Color keywordColor = Theme.currentTheme ( ).getKeywordColor ( ) ;
    Color constantColor = Theme.currentTheme ( ).getConstantColor ( ) ;
    StringBuffer result = new StringBuffer ( "<html>" ) ;
    result.append ( BEFOR_DESCRIPTION ) ;
    result.append ( this.description ) ;
    result.append ( AFTER_DESCRIPTION ) ;
    result.append ( BETWEEN ) ;
    result.append ( BEFOR_NAME ) ;
    boolean constant = false ;
    boolean keyword = false ;
    boolean current = false ;
    p.first ( ) ;
    for ( int i = 0 ; i < this.name.length ( ) ; i ++ )
    {
      if ( ( this.expression instanceof Lambda )
          &&( ( i == 1 ) || ( isInList ( i ) ) ) )
      {
        Color c = new Color ( 175 , 175 , 255 ) ;
        
        if ((i>=pStart)&&(i<=pEnd))
          
        {
          c= CURRENT ;
        }
          
        
        result.append ( "<b><font color=\"#"
            + getHex ( c ) + "\">" ) ;
        
        result.append ( this.name.charAt ( i ) ) ;
        result.append ( "</font></b>" ) ;
      }
      else
      {
        if ( p.getStyle ( ) == PrettyStyle.KEYWORD )
        {
          if ( keyword == false && ! current )
          {
            result.append ( "<b><font color=\"#" + getHex ( keywordColor )
                + "\">" ) ;
            keyword = true ;
          }
        }
        else if ( keyword == true )
        {
          result.append ( "</font></b>" ) ;
          keyword = false ;
        }
        if ( p.getStyle ( ) == PrettyStyle.CONSTANT )
        {
          if ( constant == false && ! current )
          {
            result.append ( "<b><font color=\"#" + getHex ( constantColor )
                + "\">" ) ;
            constant = true ;
          }
        }
        else if ( constant == true )
        {
          result.append ( "</font></b>" ) ;
          constant = false ;
        }
        if ( ( ( pStart == - 1 ) && ( pEnd == - 1 ) ) || ( i > pEnd )
            || ( i < pStart ) || ( ( i > pStart ) && ( i < pEnd ) ) )
        {
          result.append ( this.name.charAt ( i ) ) ;
        }
        else if ( i == pStart )
        {
          if ( keyword == true )
          {
            result.append ( "</font></b>" ) ;
            keyword = false ;
          }
          if ( constant == true )
          {
            result.append ( "</font></b>" ) ;
            constant = false ;
          }
          current = true ;
          result.append ( "<b><font color=\"#" + getHex ( CURRENT ) + "\">" ) ;
          result.append ( this.name.charAt ( i ) ) ;
          if ( i == pEnd )
          {
            result.append ( "</font></b>" ) ;
            current = false ;
          }
        }
        else if ( i == pEnd )
        {
          result.append ( this.name.charAt ( i ) ) ;
          result.append ( "</font></b>" ) ;
          current = false ;
        }
        if ( i == this.name.length ( ) - 1 )
        {
          if ( keyword == true )
          {
            result.append ( "</font></b>" ) ;
          }
          if ( constant == true )
          {
            result.append ( "</font></b>" ) ;
          }
        }
      }
      p.next ( ) ;
    }
    result.append ( AFTER_NAME ) ;
    result.append ( "</html>" ) ;
    this.html = result.toString ( ) ;
  }


  public void updateHtmlOld2 ( int pStart , int pEnd )
  {
    PrettyCharIterator p = this.expression.toPrettyString ( )
        .toCharacterIterator ( ) ;
    Color keywordColor = Theme.currentTheme ( ).getKeywordColor ( ) ;
    Color constantColor = Theme.currentTheme ( ).getConstantColor ( ) ;
    StringBuffer result = new StringBuffer ( "<html>" ) ;
    result.append ( BEFOR_DESCRIPTION ) ;
    result.append ( this.description ) ;
    result.append ( AFTER_DESCRIPTION ) ;
    result.append ( BETWEEN ) ;
    result.append ( BEFOR_NAME ) ;
    boolean constant = false ;
    boolean keyword = false ;
    boolean current = false ;
    p.first ( ) ;
    for ( int i = 0 ; i < this.name.length ( ) ; i ++ )
    {
      if ( p.getStyle ( ) == PrettyStyle.KEYWORD )
      {
        if ( keyword == false && ! current )
        {
          result.append ( "<b><font color=\"#" + getHex ( keywordColor )
              + "\">" ) ;
          keyword = true ;
        }
      }
      else if ( keyword == true )
      {
        result.append ( "</font></b>" ) ;
        keyword = false ;
      }
      if ( p.getStyle ( ) == PrettyStyle.CONSTANT )
      {
        if ( constant == false && ! current )
        {
          result.append ( "<b><font color=\"#" + getHex ( constantColor )
              + "\">" ) ;
          constant = true ;
        }
      }
      else if ( constant == true )
      {
        result.append ( "</font></b>" ) ;
        constant = false ;
      }
      if ( ( ( pStart == - 1 ) && ( pEnd == - 1 ) ) || ( i > pEnd )
          || ( i < pStart ) || ( ( i > pStart ) && ( i < pEnd ) ) )
      {
        result.append ( this.name.charAt ( i ) ) ;
      }
      else if ( i == pStart )
      {
        if ( keyword == true )
        {
          result.append ( "</font></b>" ) ;
          keyword = false ;
        }
        if ( constant == true )
        {
          result.append ( "</font></b>" ) ;
          constant = false ;
        }
        current = true ;
        result.append ( "<b><font color=\"#" + getHex ( CURRENT ) + "\">" ) ;
        result.append ( this.name.charAt ( i ) ) ;
        if ( i == pEnd )
        {
          result.append ( "</font></b>" ) ;
          current = false ;
        }
      }
      else if ( i == pEnd )
      {
        result.append ( this.name.charAt ( i ) ) ;
        result.append ( "</font></b>" ) ;
        current = false ;
      }
      if ( i == this.name.length ( ) - 1 )
      {
        if ( keyword == true )
        {
          result.append ( "</font></b>" ) ;
        }
        if ( constant == true )
        {
          result.append ( "</font></b>" ) ;
        }
      }
      p.next ( ) ;
    }
    result.append ( AFTER_NAME ) ;
    result.append ( "</html>" ) ;
    this.html = result.toString ( ) ;
  }


  public void updateHtmlOld ( int pStart , int pEnd )
  {
    PrettyCharIterator p = this.expression.toPrettyString ( )
        .toCharacterIterator ( ) ;
    Color keywordColor = Theme.currentTheme ( ).getKeywordColor ( ) ;
    Color constantColor = Theme.currentTheme ( ).getConstantColor ( ) ;
    StringBuffer result = new StringBuffer ( "<html>" ) ;
    result.append ( BEFOR_DESCRIPTION ) ;
    result.append ( this.description ) ;
    result.append ( AFTER_DESCRIPTION ) ;
    result.append ( BETWEEN ) ;
    result.append ( BEFOR_NAME ) ;
    boolean constant = false ;
    boolean keyword = false ;
    p.first ( ) ;
    for ( int i = 0 ; i < this.name.length ( ) ; i ++ )
    {
      if ( p.getStyle ( ) == PrettyStyle.CONSTANT )
      {
        if ( constant == false )
        {
          result.append ( "<b><font color=\"#" + getHex ( constantColor )
              + "\">" ) ;
          constant = true ;
        }
      }
      else if ( constant == true )
      {
        result.append ( "</font></b>" ) ;
        constant = false ;
      }
      if ( p.getStyle ( ) == PrettyStyle.KEYWORD )
      {
        if ( keyword == false )
        {
          result.append ( "<b><font color=\"#" + getHex ( keywordColor )
              + "\">" ) ;
          keyword = true ;
        }
      }
      else if ( keyword == true )
      {
        result.append ( "</font></b>" ) ;
        keyword = false ;
      }
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
      p.next ( ) ;
    }
    if ( keyword == true )
    {
      result.append ( "</font></b>" ) ;
    }
    if ( constant == true )
    {
      result.append ( "</font></b>" ) ;
    }
    result.append ( AFTER_NAME ) ;
    result.append ( "</html>" ) ;
    this.html = result.toString ( ) ;
  }
}
