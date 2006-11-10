package de.unisiegen.tpml.ui ;


import de.unisiegen.tpml.core.expressions.Expression ;


public class AbstractSyntaxTreeNode
{
  private static final String BEFOR_DESCRIPTION = "" ;


  private static final String AFTER_DESCRIPTION = "" ;


  private static final String BETWEEN = "&nbsp;&nbsp;&nbsp;&nbsp;" ;


  private static final String BEFOR_NAME = "[&nbsp;" ;


  private static final String AFTER_NAME = "&nbsp;]" ;


  private static final String COLOR = "0000FF" ;


  private static final boolean BOLD = true ;


  private String description ;


  private String name ;


  private String html ;


  private Expression expression ;


  private int startIndex ;


  private int endIndex ;


  public AbstractSyntaxTreeNode ( String pDescription , String pName ,
      Expression pExpression )
  {
    this.description = pDescription ;
    this.name = pName ;
    this.expression = pExpression ;
    this.startIndex = - 1 ;
    this.endIndex = - 1 ;
    resetHtml ( ) ;
  }


  public AbstractSyntaxTreeNode ( String pDescription , String pName ,
      Expression pExpression , int pStart , int pEnd )
  {
    this.description = pDescription ;
    this.name = pName ;
    this.expression = pExpression ;
    this.startIndex = pStart ;
    this.endIndex = pEnd ;
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


  public int getStartIndex ( )
  {
    return startIndex ;
  }


  public void resetHtml ( )
  {
    StringBuffer s = new StringBuffer ( "<html>" ) ;
    s.append ( BEFOR_DESCRIPTION ) ;
    s.append ( this.description ) ;
    s.append ( AFTER_DESCRIPTION ) ;
    s.append ( BETWEEN ) ;
    s.append ( BEFOR_NAME ) ;
    s.append ( this.name ) ;
    s.append ( AFTER_NAME ) ;
    s.append ( "</html>" ) ;
    this.html = s.toString ( ) ;
  }


  public String toString ( )
  {
    return this.html ;
  }


  private String keyword ;


  public void setKeyword ( String pKeyword )
  {
    this.keyword = pKeyword ;
  }


  public void updateHtml ( int pStart , int pEnd )
  {
    StringBuffer s = new StringBuffer ( "<html>" ) ;
    s.append ( BEFOR_DESCRIPTION ) ;
    s.append ( this.description ) ;
    s.append ( AFTER_DESCRIPTION ) ;
    s.append ( BETWEEN ) ;
    s.append ( BEFOR_NAME ) ;
    for ( int i = 0 ; i < this.name.length ( ) ; i ++ )
    {
      if ( this.keyword.charAt ( i ) == 'X' )
      {
        s.append ( "<font color=\"#" + COLOR + "\">" ) ;
      }
      if ( i < pStart )
      {
        s.append ( this.name.charAt ( i ) ) ;
      }
      else if ( ( i >= pStart ) && ( i <= pEnd ) )
      {
        s.append ( "<b>" ) ;
        s.append ( this.name.charAt ( i ) ) ;
        s.append ( "</b>" ) ;
      }
      else if ( i > pEnd )
      {
        s.append ( this.name.charAt ( i ) ) ;
      }
      if ( this.keyword.charAt ( i ) == 'X' )
      {
        s.append ( "</font>" ) ;
      }
    }
    s.append ( AFTER_NAME ) ;
    s.append ( "</html>" ) ;
    this.html = s.toString ( ) ;
  }


  @ SuppressWarnings ( "unused" )
  private void oldupdateHtml ( int pStart , int pEnd )
  {
    StringBuffer s = new StringBuffer ( "<html>" ) ;
    s.append ( BEFOR_DESCRIPTION ) ;
    s.append ( this.description ) ;
    s.append ( AFTER_DESCRIPTION ) ;
    s.append ( BETWEEN ) ;
    s.append ( BEFOR_NAME ) ;
    s.append ( this.name.substring ( 0 , pStart ) ) ;
    if ( BOLD ) s.append ( "<b>" ) ;
    s.append ( "<font color=\"#" + COLOR + "\">" ) ;
    s.append ( this.name.substring ( pStart , pEnd + 1 ) ) ;
    s.append ( "</font>" ) ;
    if ( BOLD ) s.append ( "</b>" ) ;
    s.append ( this.name.substring ( pEnd + 1 , this.name.length ( ) ) ) ;
    s.append ( AFTER_NAME ) ;
    s.append ( "</html>" ) ;
    this.html = s.toString ( ) ;
  }
}
