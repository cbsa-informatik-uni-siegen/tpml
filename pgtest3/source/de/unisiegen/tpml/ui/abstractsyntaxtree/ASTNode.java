package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.awt.Color ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator ;
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


  private static boolean checkedSelected = true ;


  private static boolean checkedReplace = true ;


  private static boolean checkedBindings = true ;


  public static void setCheckedBindings ( boolean pCheckedBindings )
  {
    checkedBindings = pCheckedBindings ;
  }


  public static void setCheckedReplace ( boolean pCheckedReplace )
  {
    checkedReplace = pCheckedReplace ;
  }


  public static void setCheckedSelected ( boolean pCheckedSelected )
  {
    checkedSelected = pCheckedSelected ;
  }


  private boolean replaceExpression ;


  private String description ;


  private String expressionString ;


  private String caption ;


  private Expression expression ;


  private int selectedStartIndex ;


  private int selectedEndIndex ;


  private ASTBindings aSTBindings ;


  public ASTNode ( String pDescription , Expression pExpression )
  {
    this.description = pDescription ;
    this.expressionString = pExpression.toPrettyString ( ).toString ( ) ;
    this.expression = pExpression ;
    this.selectedStartIndex = - 1 ;
    this.selectedEndIndex = - 1 ;
    this.aSTBindings = null ;
    resetCaption ( ) ;
    this.replaceExpression = false ;
  }


  public ASTNode ( String pDescription , Expression pExpression ,
      ASTBindings pRelations )
  {
    this.description = pDescription ;
    this.expressionString = pExpression.toPrettyString ( ).toString ( ) ;
    this.expression = pExpression ;
    this.selectedStartIndex = - 1 ;
    this.selectedEndIndex = - 1 ;
    this.aSTBindings = pRelations ;
    resetCaption ( ) ;
    this.replaceExpression = false ;
  }


  public ASTNode ( String pDescription , String pExpressionString ,
      int pSelectedStartIndex , int pSelectedEndIndex )
  {
    this.description = pDescription ;
    this.expressionString = pExpressionString ;
    this.expression = null ;
    this.selectedStartIndex = pSelectedStartIndex ;
    this.selectedEndIndex = pSelectedEndIndex ;
    this.aSTBindings = null ;
    resetCaption ( ) ;
    this.replaceExpression = false ;
  }


  public void enableSelectedColor ( )
  {
    if ( this.expression == null )
    {
      StringBuffer result = new StringBuffer ( "<html>" ) ;
      result.append ( BEFOR_DESCRIPTION ) ;
      result.append ( this.description ) ;
      result.append ( AFTER_DESCRIPTION ) ;
      result.append ( BETWEEN ) ;
      result.append ( BEFOR_NAME ) ;
      if ( checkedSelected )
      {
        result.append ( "<b><font color=\"#"
            + getHex ( Theme.currentTheme ( ).getSelectedColor ( ) ) + "\">" ) ;
      }
      result.append ( this.expressionString ) ;
      if ( checkedSelected )
      {
        result.append ( "</font></b>" ) ;
      }
      result.append ( AFTER_NAME ) ;
      result.append ( "</html>" ) ;
      this.caption = result.toString ( ) ;
    }
    else
    {
      updateCaption ( - 1 , - 1 , - 1 ) ;
    }
  }


  public String getDescription ( )
  {
    return this.description ;
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


  public int getSelectedEndIndex ( )
  {
    return this.selectedEndIndex ;
  }


  public int getSelectedStartIndex ( )
  {
    return this.selectedStartIndex ;
  }


  private boolean isInList ( int pListIndex , int pIndex )
  {
    if ( ( this.aSTBindings == null ) || ( pListIndex < 0 )
        || ( pListIndex >= this.aSTBindings.size ( ) ) )
    {
      return false ;
    }
    for ( int i = 0 ; i < this.aSTBindings.size ( pListIndex ) ; i ++ )
    {
      Expression e = this.aSTBindings.get ( pListIndex , i ) ;
      PrettyAnnotation prettyAnnotation = this.expression.toPrettyString ( )
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


  public void setDescription ( String pDescription )
  {
    this.description = pDescription ;
  }


  public void setReplaceExpression ( boolean pReplaceExpression )
  {
    this.replaceExpression = pReplaceExpression ;
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
      if ( ( checkedSelected ) && ( index == pSelectionStart ) )
      {
        result.append ( "<b><font color=\"#"
            + getHex ( Theme.currentTheme ( ).getSelectedColor ( ) ) + "\">" ) ;
        if ( checkedReplace && this.replaceExpression )
        {
          result.append ( "&nbsp;" + REPLACE_STRING + "&nbsp;" ) ;
        }
        while ( index <= pSelectionEnd )
        {
          if ( ! ( checkedReplace && this.replaceExpression ) )
          {
            result.append ( this.expressionString.charAt ( index ) ) ;
          }
          index ++ ;
          p.next ( ) ;
        }
        result.append ( "</font></b>" ) ;
      }
      // Not Selected and should be replaced
      else if ( ! ( checkedSelected ) && ( checkedReplace )
          && ( this.replaceExpression ) && ( index == pSelectionStart ) )
      {
        result.append ( "<b>&nbsp;" + REPLACE_STRING + "&nbsp;" ) ;
        while ( index <= pSelectionEnd )
        {
          index ++ ;
          p.next ( ) ;
        }
        result.append ( "</b>" ) ;
      }
      // Binding
      else if ( ( checkedBindings ) && ( this.aSTBindings != null )
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
