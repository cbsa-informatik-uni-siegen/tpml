package de.unisiegen.tpml.core.latex ;


import java.util.ArrayList ;
import java.util.Iterator ;


/**
 * Implements the {@link LatexCommand} list.
 * 
 * @author Christian Fehler
 */
public class LatexCommandList implements Iterable < LatexCommand >
{
  /**
   * The internal used list.
   */
  private ArrayList < LatexCommand > list ;


  /**
   * Allocates a new <code>LatexCommandList</code>
   */
  public LatexCommandList ( )
  {
    this.list = new ArrayList < LatexCommand > ( ) ;
  }


  /**
   * Adds the {@link LatexCommand}s of the {@link LatexPrintable}s to the
   * list.
   * 
   * @param pLatexPrintables The {@link LatexPrintable}s.
   */
  public void add ( Iterable < ? extends LatexPrintable > pLatexPrintables )
  {
    if ( pLatexPrintables != null )
    {
      for ( LatexPrintable latexPrintable : pLatexPrintables )
      {
        if ( latexPrintable != null )
        {
          for ( LatexCommand command : latexPrintable.getLatexCommands ( ) )
          {
            if ( ! this.list.contains ( command ) )
            {
              this.list.add ( command ) ;
            }
          }
        }
      }
    }
  }


  /**
   * Adds the {@link LatexCommand} to the list.
   * 
   * @param pLatexCommand The {@link LatexCommand}.
   */
  public void add ( LatexCommand pLatexCommand )
  {
    if ( pLatexCommand != null )
    {
      if ( ! this.list.contains ( pLatexCommand ) )
      {
        this.list.add ( pLatexCommand ) ;
      }
    }
  }


  /**
   * Adds the {@link LatexCommand}s of the {@link LatexCommandList} to the
   * list.
   * 
   * @param pLatexCommandList The {@link LatexCommandList}.
   */
  public void add ( LatexCommandList pLatexCommandList )
  {
    if ( pLatexCommandList != null )
    {
      for ( LatexCommand command : pLatexCommandList )
      {
        if ( ! this.list.contains ( command ) )
        {
          this.list.add ( command ) ;
        }
      }
    }
  }


  /**
   * Adds the {@link LatexCommand}s of the {@link LatexPrintable} to the list.
   * 
   * @param pLatexPrintable The {@link LatexPrintable}.
   */
  public void add ( LatexPrintable pLatexPrintable )
  {
    if ( pLatexPrintable != null )
    {
      for ( LatexCommand command : pLatexPrintable.getLatexCommands ( ) )
      {
        if ( ! this.list.contains ( command ) )
        {
          this.list.add ( command ) ;
        }
      }
    }
  }


  /**
   * Adds the {@link LatexCommand}s of the {@link LatexPrintable}s to the
   * list.
   * 
   * @param pLatexPrintables The {@link LatexPrintable}s.
   */
  public void add ( LatexPrintable [ ] pLatexPrintables )
  {
    if ( pLatexPrintables != null )
    {
      for ( LatexPrintable latexPrintable : pLatexPrintables )
      {
        if ( latexPrintable != null )
        {
          for ( LatexCommand command : latexPrintable.getLatexCommands ( ) )
          {
            if ( ! this.list.contains ( command ) )
            {
              this.list.add ( command ) ;
            }
          }
        }
      }
    }
  }


  /**
   * Returns an iterator over the elements in this list in proper sequence.
   * 
   * @return An iterator over the elements in this list in proper sequence.
   * @see Iterable#iterator()
   */
  public Iterator < LatexCommand > iterator ( )
  {
    return this.list.iterator ( ) ;
  }


  /**
   * Returns the number of elements in this list.
   * 
   * @return the number of elements in this list.
   */
  public int size ( )
  {
    return this.list.size ( ) ;
  }
}
