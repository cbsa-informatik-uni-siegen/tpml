package de.unisiegen.tpml.ui.abstractsyntaxtree.binding ;


import java.util.ArrayList ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTPair
{
  /**
   * TODO
   */
  private int start ;


  /**
   * TODO
   */
  private int end ;


  /**
   * TODO
   */
  private ArrayList < ASTPair > list ;


  /**
   * TODO
   * 
   * @param pStart
   * @param pEnd
   */
  public ASTPair ( int pStart , int pEnd )
  {
    this.start = pStart ;
    this.end = pEnd ;
    this.list = new ArrayList < ASTPair > ( ) ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public int getEnd ( )
  {
    return this.end ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public int getStart ( )
  {
    return this.start ;
  }


  /**
   * TODO
   * 
   * @param pASTPair
   */
  public void add ( ASTPair pASTPair )
  {
    this.list.add ( pASTPair ) ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public int size ( )
  {
    return this.list.size ( ) ;
  }


  /**
   * TODO
   * 
   * @param pIndex
   * @return TODO
   */
  public ASTPair get ( int pIndex )
  {
    if ( ( pIndex < 0 ) || ( pIndex >= this.list.size ( ) ) )
    {
      return null ;
    }
    return this.list.get ( pIndex ) ;
  }


  /**
   * TODO
   * 
   * @param pASTPair
   * @return TODO
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @ Override
  public boolean equals ( Object pASTPair )
  {
    if ( pASTPair instanceof ASTPair )
    {
      ASTPair tmp = ( ASTPair ) pASTPair ;
      if ( ( this.start == tmp.getStart ( ) ) && ( this.end == tmp.getEnd ( ) ) )
      {
        return true ;
      }
    }
    return false ;
  }
}
