package de.unisiegen.tpml.ui ;


import de.unisiegen.tpml.core.expressions.Expression ;


public class AbstractSyntaxTreeNode
{
  private String name ;


  private String html ;


  private Expression expression ;


  public AbstractSyntaxTreeNode ( String pName , Expression pExpression )
  {
    this.name = pName ;
    this.html = "<html>" + this.name + "</html>" ;
    this.expression = pExpression ;
  }


  private String bold ( String pText , int pStart , int pEnd )
  {
    String s = "<html>" ;
    s += pText.substring ( 0 , pStart ) ;
    s += "<b>" ;
    s += "<font color=\"#FF0000\">" ;
    s += pText.substring ( pStart , pEnd + 1 ) ;
    s += "</b>" ;
    s += "</font>" ;
    s += pText.substring ( pEnd + 1 , pText.length ( ) ) ;
    s += "</html>" ;
    return s ;
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
    this.html = bold ( name , pStart , pEnd ) ;
  }


  public void resetHtml ( )
  {
    this.html = "<html>" + name + "</html>" ;
  }
}
