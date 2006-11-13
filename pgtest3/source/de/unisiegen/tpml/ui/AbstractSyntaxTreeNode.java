package de.unisiegen.tpml.ui ;


import java.awt.Color ;
import java.util.LinkedList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
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


  private Color SELECTED = new Color ( 255 , 0 , 0 ) ;


  private Color BINDING = new Color ( 255 , 127 , 127 ) ;


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
      updateHtml ( - 1 , - 1 , false ) ;
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


  public void updateHtml ( int pSelectionStart , int pSelectionEnd ,
      boolean pPrintBindings )
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
    while ( index < this.name.length ( ) )
    {
      // Selected
      if ( index == pSelectionStart )
      {
        result.append ( "<b><font color=\"#" + getHex ( SELECTED ) + "\">" ) ;
        while ( index <= pSelectionEnd )
        {
          result.append ( this.name.charAt ( index ) ) ;
          index ++ ;
          p.next ( ) ;
        }
        result.append ( "</font></b>" ) ;
      }
      // Binding
      else if ( ( this.relations != null ) && ( isInList ( index ) )
          && ( pPrintBindings ) )
      {
        result.append ( "<b><font color=\"#" + getHex ( BINDING ) + "\">" ) ;
        while ( isInList ( index ) )
        {
          result.append ( this.name.charAt ( index ) ) ;
          index ++ ;
          p.next ( ) ;
        }
        result.append ( "</font></b>" ) ;
      }
      // Binding - Lambda
      else if ( ( this.expression != null )
          && ( this.expression instanceof Lambda ) && ( index == 1 )
          && ( pPrintBindings ) )
      {
        Lambda l = ( Lambda ) this.expression ;
        result.append ( "<b><font color=\"#" + getHex ( BINDING ) + "\">" ) ;
        int j = 0 ;
        while ( j < l.getId ( ).length ( ) )
        {
          result.append ( this.name.charAt ( index ) ) ;
          index ++ ;
          j ++ ;
          p.next ( ) ;
        }
        result.append ( "</font></b>" ) ;
      }
      // Binding - LetRec
      else if ( ( this.expression != null )
          && ( this.expression instanceof LetRec ) && ( index == 8 ) )
      {
        LetRec l = ( LetRec ) this.expression ;
        result.append ( "<b><font color=\"#" + getHex ( BINDING ) + "\">" ) ;
        int j = 0 ;
        while ( j < l.getId ( ).length ( ) )
        {
          result.append ( this.name.charAt ( index ) ) ;
          index ++ ;
          j ++ ;
          p.next ( ) ;
        }
        result.append ( "</font></b>" ) ;
      }
      // Binding - Let
      else if ( ( this.expression != null )
          && ( this.expression instanceof Let )
          && ( ! ( this.expression instanceof LetRec ) ) && ( index == 4 ) )
      {
        Let l = ( Let ) this.expression ;
        result.append ( "<b><font color=\"#" + getHex ( BINDING ) + "\">" ) ;
        int j = 0 ;
        while ( j < l.getId ( ).length ( ) )
        {
          result.append ( this.name.charAt ( index ) ) ;
          index ++ ;
          j ++ ;
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
          result.append ( this.name.charAt ( index ) ) ;
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
          result.append ( this.name.charAt ( index ) ) ;
          index ++ ;
          p.next ( ) ;
        }
        result.append ( "</font></b>" ) ;
      }
      // Else
      else
      {
        result.append ( this.name.charAt ( index ) ) ;
        index ++ ;
        p.next ( ) ;
      }
    }
    result.append ( AFTER_NAME ) ;
    result.append ( "</html>" ) ;
    this.html = result.toString ( ) ;
  }
}
