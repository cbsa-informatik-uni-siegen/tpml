package de.unisiegen.tpml.core.util ;


import java.util.Set ;
import java.util.TreeSet ;


/**
 * TODO
 * 
 * @author Christian Fehler
 */
public class Free
{
  /**
   * TODO
   */
  private TreeSet < String > free ;


  /**
   * TODO
   */
  public Free ( )
  {
    this.free = new TreeSet < String > ( ) ;
  }


  /**
   * TODO
   * 
   * @param pIdentifiers TODO
   */
  public void add ( Set < String > pIdentifiers )
  {
    this.free.addAll ( pIdentifiers ) ;
  }


  /**
   * TODO
   * 
   * @param pId TODO
   */
  public void add ( String pId )
  {
    this.free.add ( pId ) ;
  }


  /**
   * TODO
   */
  public void clear ( )
  {
    this.free.clear ( ) ;
  }


  /**
   * TODO
   * 
   * @param pOldIdentifier TODO
   * @return TODO
   */
  public String newIdentifier ( String pOldIdentifier )
  {
    String newIdentifier = pOldIdentifier ;
    while ( this.free.contains ( newIdentifier ) )
    {
      newIdentifier = newIdentifier + "'" ; //$NON-NLS-1$
    }
    return newIdentifier ;
  }


  /**
   * TODO
   * 
   * @param pIdentifiers TODO
   */
  public void remove ( Set < String > pIdentifiers )
  {
    this.free.removeAll ( pIdentifiers ) ;
  }


  /**
   * TODO
   * 
   * @param pId TODO
   */
  public void remove ( String pId )
  {
    this.free.remove ( pId ) ;
  }
}
