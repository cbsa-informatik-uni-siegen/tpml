package de.unisiegen.tpml.core.latex ;


import java.util.ArrayList ;
import java.util.Iterator ;


/**
 * Implements the {@link LatexInstruction} list.
 * 
 * @author Christian Fehler
 */
public class LatexInstructionList implements Iterable < LatexInstruction >
{
  /**
   * The internal used list.
   */
  private ArrayList < LatexInstruction > list ;


  /**
   * Allocates a new <code>LatexInstructionList</code>
   */
  public LatexInstructionList ( )
  {
    this.list = new ArrayList < LatexInstruction > ( ) ;
  }


  /**
   * Adds the {@link LatexInstruction}s of the {@link LatexPrintable}s to the
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
          for ( LatexInstruction instruction : latexPrintable
              .getLatexInstructions ( ) )
          {
            if ( ! this.list.contains ( instruction ) )
            {
              this.list.add ( instruction ) ;
            }
          }
        }
      }
    }
  }


  /**
   * Adds the {@link LatexInstruction} to the list.
   * 
   * @param pLatexInstruction The {@link LatexInstruction}.
   */
  public void add ( LatexInstruction pLatexInstruction )
  {
    if ( pLatexInstruction != null )
    {
      if ( ! this.list.contains ( pLatexInstruction ) )
      {
        this.list.add ( pLatexInstruction ) ;
      }
    }
  }


  /**
   * Adds the {@link LatexInstruction}s of the {@link LatexInstructionList} to
   * the list.
   * 
   * @param pLatexInstructionList The {@link LatexInstructionList}.
   */
  public void add ( LatexInstructionList pLatexInstructionList )
  {
    if ( pLatexInstructionList != null )
    {
      for ( LatexInstruction instruction : pLatexInstructionList )
      {
        if ( ! this.list.contains ( instruction ) )
        {
          this.list.add ( instruction ) ;
        }
      }
    }
  }


  /**
   * Adds the {@link LatexInstruction}s of the {@link LatexPrintable} to the
   * list.
   * 
   * @param pLatexPrintable The {@link LatexPrintable}.
   */
  public void add ( LatexPrintable pLatexPrintable )
  {
    if ( pLatexPrintable != null )
    {
      for ( LatexInstruction instruction : pLatexPrintable
          .getLatexInstructions ( ) )
      {
        if ( ! this.list.contains ( instruction ) )
        {
          this.list.add ( instruction ) ;
        }
      }
    }
  }


  /**
   * Adds the {@link LatexInstruction}s of the {@link LatexPrintable}s to the
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
          for ( LatexInstruction instruction : latexPrintable
              .getLatexInstructions ( ) )
          {
            if ( ! this.list.contains ( instruction ) )
            {
              this.list.add ( instruction ) ;
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
  public Iterator < LatexInstruction > iterator ( )
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
