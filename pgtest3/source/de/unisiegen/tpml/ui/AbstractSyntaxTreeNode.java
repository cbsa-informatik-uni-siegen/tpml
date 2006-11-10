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


  private AbstractSyntaxTreeIndices abstractSyntaxTreeIndices ;


  public AbstractSyntaxTreeNode ( String pDescription , String pName ,
      Object pObject )
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
    if ( pObject instanceof Expression )
    {
      this.expression = ( Expression ) pObject ;
      this.abstractSyntaxTreeIndices = null ;
    }
    else if ( pObject instanceof AbstractSyntaxTreeIndices )
    {
      this.expression = null ;
      this.abstractSyntaxTreeIndices = ( AbstractSyntaxTreeIndices ) pObject ;
    }
  }


  public AbstractSyntaxTreeIndices getAbstractSyntaxTreeIndices ( )
  {
    return this.abstractSyntaxTreeIndices ;
  }


  public Expression getExpression ( )
  {
    return expression ;
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
    s.append ( "</font>" ) ;
    s.append ( "</b>" ) ;
    s.append ( this.name.substring ( pEnd + 1 , this.name.length ( ) ) ) ;
    s.append ( AFTER_NAME ) ;
    s.append ( "</html>" ) ;
    this.html = s.toString ( ) ;
  }
}
