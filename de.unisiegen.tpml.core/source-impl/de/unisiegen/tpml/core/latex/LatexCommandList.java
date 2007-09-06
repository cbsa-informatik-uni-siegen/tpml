package de.unisiegen.tpml.core.latex ;


import java.util.ArrayList ;
import java.util.Collections ;
import java.util.Comparator ;
import java.util.Iterator ;


/**
 * Implements the {@link LatexCommand} list.
 * 
 * @author Christian Fehler
 */
public class LatexCommandList implements Iterable < LatexCommand >
{
  /**
   * This <code>Comparator</code> is used to sort the {@link LatexCommandList}.
   * 
   * @author Christian Fehler
   */
  protected class LatexCommandComparator implements LatexCommandNames ,
      Comparator < LatexCommand >
  {
    /**
     * Compares its two arguments for order. Returns a negative integer, zero,
     * or a positive integer as the first argument is less than, equal to, or
     * greater than the second.
     * 
     * @param pLatexCommand1 The first {@link LatexCommand}.
     * @param pLatexCommand2 The second {@link LatexCommand}.
     * @return A negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second.
     * @see Comparator#compare(Object, Object)
     */
    public int compare ( LatexCommand pLatexCommand1 ,
        LatexCommand pLatexCommand2 )
    {
      if ( pLatexCommand1.getName ( ).startsWith ( LATEX_KEY ) )
      {
        // case 1
        if ( pLatexCommand2.getName ( ).startsWith ( LATEX_KEY ) )
        {
          return pLatexCommand1.getName ( ).compareTo (
              pLatexCommand2.getName ( ) ) ;
        }
        // case 2
        return - 1 ;
      }
      // case 3
      if ( pLatexCommand2.getName ( ).startsWith ( LATEX_KEY ) )
      {
        return 1 ;
      }
      // case 4
      return pLatexCommand1.getName ( ).compareTo ( pLatexCommand2.getName ( ) ) ;
    }
  }


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
              sort ( ) ;
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
        sort ( ) ;
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
          sort ( ) ;
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
          sort ( ) ;
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
              sort ( ) ;
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


  /**
   * Sorts the list.
   */
  private void sort ( )
  {
    Collections.sort ( this.list , new LatexCommandComparator ( ) ) ;
  }
}
