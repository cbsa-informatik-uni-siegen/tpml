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


  private String description ;


  private String name ;


  private String html ;


  private Expression expression ;


  public AbstractSyntaxTreeNode ( String pDescription , String pName ,
      Expression pExpression )
  {
    this.description = pDescription ;
    if ( this.description == null )
    {
      this.description = "" ;
    }
    this.name = pName ;
    if ( this.name == null )
    {
      this.name = "" ;
    }
    resetHtml ( ) ;
    this.expression = pExpression ;
  }


  public String getDescription ( )
  {
    return description ;
  }


  public Expression getExpression ( )
  {
    return expression ;
  }


  public String getHtml ( )
  {
    return html ;
  }


  public String getName ( )
  {
    return name ;
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


  public void setDescription ( String description )
  {
    this.description = description ;
  }


  public void setExpression ( Expression expression )
  {
    this.expression = expression ;
  }


  public void setHtml ( String html )
  {
    this.html = html ;
  }


  public void setName ( String name )
  {
    this.name = name ;
  }


  public String toString ( )
  {
    return this.html ;
  }


  public void updateHtml ( int pStart , int pEnd )
  {
    StringBuffer s = new StringBuffer ( "<html>" ) ;
    s.append ( BEFOR_DESCRIPTION ) ;
    s.append ( this.description ) ;
    s.append ( AFTER_DESCRIPTION ) ;
    s.append ( BETWEEN ) ;
    s.append ( BEFOR_NAME ) ;
    s.append ( this.name.substring ( 0 , pStart ) ) ;
    s.append ( "<b>" ) ;
    s.append ( "<font color=\"#" + COLOR + "\">" ) ;
    s.append ( this.name.substring ( pStart , pEnd + 1 ) ) ;
    s.append ( "</b>" ) ;
    s.append ( "</font>" ) ;
    s.append ( this.name.substring ( pEnd + 1 , this.name.length ( ) ) ) ;
    s.append ( AFTER_NAME ) ;
    s.append ( "</html>" ) ;
    this.html = s.toString ( ) ;
  }
}
