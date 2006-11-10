package de.unisiegen.tpml.ui ;


public class AbstractSyntaxTreeIndices
{
  private int start ;


  private int end ;


  public AbstractSyntaxTreeIndices ( int pStart , int pEnd )
  {
    this.start = pStart ;
    this.end = pEnd ;
  }


  public int getEnd ( )
  {
    return end ;
  }


  public int getStart ( )
  {
    return start ;
  }
}
