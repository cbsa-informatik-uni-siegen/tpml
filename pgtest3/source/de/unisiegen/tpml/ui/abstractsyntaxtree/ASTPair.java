package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.util.LinkedList ;


public class ASTPair
{
  private int start ;


  private int end ;


  private LinkedList < ASTPair > list ;


  public ASTPair ( int pStart , int pEnd )
  {
    this.start = pStart ;
    this.end = pEnd ;
    this.list = new LinkedList < ASTPair > ( ) ;
  }


  public int getEnd ( )
  {
    return this.end ;
  }


  public int getStart ( )
  {
    return this.start ;
  }


  public void add ( ASTPair pASTPair )
  {
    this.list.add ( pASTPair ) ;
  }


  public int size ( )
  {
    return this.list.size ( ) ;
  }


  public ASTPair get ( int pIndex )
  {
    if ( ( pIndex < 0 ) || ( pIndex >= this.list.size ( ) ) )
    {
      return null ;
    }
    return this.list.get ( pIndex ) ;
  }


  @ Override
  public boolean equals ( Object o )
  {
    if ( o instanceof ASTPair )
    {
      ASTPair tmp = ( ASTPair ) o ;
      if ( ( this.start == tmp.getStart ( ) ) && ( this.end == tmp.getEnd ( ) ) )
      {
        return true ;
      }
    }
    return false ;
  }
}
