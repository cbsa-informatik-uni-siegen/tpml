package de.unisiegen.tpml.core.latex;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;


/**
 * Implements the {@link LatexPackage} list.
 * 
 * @author Christian Fehler
 * @version $Id$
 */
public class LatexPackageList implements Iterable < LatexPackage >
{

  /**
   * This <code>Comparator</code> is used to sort the {@link LatexPackageList}.
   * 
   * @author Christian Fehler
   */
  protected class LatexPackageComparator implements LatexCommandNames,
      Comparator < LatexPackage >
  {

    /**
     * Compares its two arguments for order. Returns a negative integer, zero,
     * or a positive integer as the first argument is less than, equal to, or
     * greater than the second.
     * 
     * @param pLatexPackage1 The first {@link LatexPackage}.
     * @param pLatexPackage2 The second {@link LatexPackage}.
     * @return A negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second.
     * @see Comparator#compare(Object, Object)
     */
    public int compare ( LatexPackage pLatexPackage1,
        LatexPackage pLatexPackage2 )
    {
      return pLatexPackage1.getName ().compareTo ( pLatexPackage2.getName () );
    }
  }


  /**
   * The internal used list.
   */
  private ArrayList < LatexPackage > list;


  /**
   * Allocates a new <code>LatexPackageList</code>
   */
  public LatexPackageList ()
  {
    this.list = new ArrayList < LatexPackage > ();
  }


  /**
   * Adds the {@link LatexPackage}s of the {@link LatexPrintable}s to the
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
          for ( LatexPackage pack : latexPrintable.getLatexPackages () )
          {
            if ( !this.list.contains ( pack ) )
            {
              this.list.add ( pack );
              sort ();
            }
          }
        }
      }
    }
  }


  /**
   * Adds the {@link LatexPackage} to the list.
   * 
   * @param pLatexPackage The {@link LatexPackage}.
   */
  public void add ( LatexPackage pLatexPackage )
  {
    if ( pLatexPackage != null )
    {
      if ( !this.list.contains ( pLatexPackage ) )
      {
        this.list.add ( pLatexPackage );
        sort ();
      }
    }
  }


  /**
   * Adds the {@link LatexPackage}s of the {@link LatexPackageList} to the
   * list.
   * 
   * @param pLatexPackageList The {@link LatexPackageList}.
   */
  public void add ( LatexPackageList pLatexPackageList )
  {
    if ( pLatexPackageList != null )
    {
      for ( LatexPackage pack : pLatexPackageList )
      {
        if ( !this.list.contains ( pack ) )
        {
          this.list.add ( pack );
          sort ();
        }
      }
    }
  }


  /**
   * Adds the {@link LatexPackage}s of the {@link LatexPrintable} to the list.
   * 
   * @param pLatexPrintable The {@link LatexPrintable}.
   */
  public void add ( LatexPrintable pLatexPrintable )
  {
    if ( pLatexPrintable != null )
    {
      for ( LatexPackage pack : pLatexPrintable.getLatexPackages () )
      {
        if ( !this.list.contains ( pack ) )
        {
          this.list.add ( pack );
          sort ();
        }
      }
    }
  }


  /**
   * Adds the {@link LatexPackage}s of the {@link LatexPrintable}s to the
   * list.
   * 
   * @param pLatexPrintables The {@link LatexPrintable}s.
   */
  public void add ( LatexPrintable [] pLatexPrintables )
  {
    if ( pLatexPrintables != null )
    {
      for ( LatexPrintable latexPrintable : pLatexPrintables )
      {
        if ( latexPrintable != null )
        {
          for ( LatexPackage pack : latexPrintable.getLatexPackages () )
          {
            if ( !this.list.contains ( pack ) )
            {
              this.list.add ( pack );
              sort ();
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
  public Iterator < LatexPackage > iterator ()
  {
    return this.list.iterator ();
  }


  /**
   * Returns the number of elements in this list.
   * 
   * @return the number of elements in this list.
   */
  public int size ()
  {
    return this.list.size ();
  }


  /**
   * Sorts the list.
   */
  private void sort ()
  {
    Collections.sort ( this.list, new LatexPackageComparator () );
  }
}
