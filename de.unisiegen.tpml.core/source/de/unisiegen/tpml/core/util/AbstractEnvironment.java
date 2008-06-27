package de.unisiegen.tpml.core.util;


import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * Abstract implementation of the generic <code>Environment</code> interface.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Id$
 * @param <S> The symbol.
 * @param <E> The entry.
 * @see de.unisiegen.tpml.core.util.Environment
 */
public abstract class AbstractEnvironment < S, E > implements
    Environment < S, E >
{

  /**
   * The store mappings, mapping symbols to entries.
   * 
   * @param <S> The symbol.
   * @param <E> The entry.
   */
  public static class Mapping < S, E >
  {

    /**
     * The symbol.
     */
    private S symbol;


    /**
     * The entry.
     */
    private E entry;


    /**
     * Initializes the mapping.
     * 
     * @param pSymbol The symbol.
     * @param pEntry The entry.
     */
    public Mapping ( S pSymbol, E pEntry )
    {
      if ( pSymbol == null )
      {
        throw new NullPointerException ( "symbol is null" ); //$NON-NLS-1$
      }
      if ( pEntry == null )
      {
        throw new NullPointerException ( "entry is null" ); //$NON-NLS-1$
      }
      this.symbol = pSymbol;
      this.entry = pEntry;
    }


    /**
     * Returns the entry.
     * 
     * @return The entry.
     */
    public E getEntry ()
    {
      return this.entry;
    }


    /**
     * Returns the symbol.
     * 
     * @return The symbol.
     */
    public S getSymbol ()
    {
      return this.symbol;
    }
  }


  /**
   * The mappings, the symbol/entry pairs, within this environment.
   */
  protected LinkedList < Mapping < S, E >> mappings;


  /**
   * Default constructor, creates a new empty environment with no mappings.
   */
  protected AbstractEnvironment ()
  {
    // start with an empty mapping
    this.mappings = new LinkedList < Mapping < S, E >> ();
  }


  /**
   * Creates a new environment, based on the mappings from the specified
   * <code>environment</code>.
   * 
   * @param environment another <code>AbstractEnvironment</code> to inherit the
   *          mappings from.
   * @throws NullPointerException if the <code>environment</code> is
   *           <code>null</code>.
   */
  protected AbstractEnvironment ( AbstractEnvironment < S, E > environment )
  {
    if ( environment == null )
    {
      throw new NullPointerException ( "environment is null" ); //$NON-NLS-1$
    }
    // copy-on-write for the mappings
    this.mappings = environment.mappings;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Environment#containsSymbol(Object)
   */
  public boolean containsSymbol ( S symbol )
  {
    if ( symbol == null )
    {
      throw new NullPointerException ( "symbol is null" ); //$NON-NLS-1$
    }
    for ( Mapping < S, E > mapping : this.mappings )
    {
      if ( mapping.getSymbol ().equals ( symbol ) )
      {
        return true;
      }
    }
    return false;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Environment#get(Object)
   */
  public E get ( S symbol )
  {
    if ( symbol == null )
    {
      throw new NullPointerException ( "symbol is null" ); //$NON-NLS-1$
    }
    for ( Mapping < S, E > mapping : this.mappings )
    {
      if ( mapping.getSymbol ().equals ( symbol ) )
      {
        return mapping.getEntry ();
      }
    }
    throw new IllegalArgumentException ( "symbol is invalid" ); //$NON-NLS-1$
  }


  /**
   * Returns the mappings.
   * 
   * @return The mappings.
   * @see #mappings
   */
  public final LinkedList < Mapping < S, E >> getMappings ()
  {
    return this.mappings;
  }


  /**
   * Adds a new mapping from <code>symbol</code> to <code>entry</code>. Any
   * previous mapping for <code>symbol</code> will be removed.
   * 
   * @param symbol the symbol for the new mapping.
   * @param entry the entry for the new mapping.
   * @see #get(Object)
   */
  protected void put ( S symbol, E entry )
  {
    if ( symbol == null )
    {
      throw new NullPointerException ( "symbol is null" ); //$NON-NLS-1$
    }
    if ( entry == null )
    {
      throw new NullPointerException ( "entry is null" ); //$NON-NLS-1$
    }
    // copy-on-write semantics
    LinkedList < Mapping < S, E >> newMappings = new LinkedList < Mapping < S, E >> ();
    newMappings.add ( new Mapping < S, E > ( symbol, entry ) );
    for ( Mapping < S, E > mapping : this.mappings )
    {
      if ( !mapping.getSymbol ().equals ( symbol ) )
      {
        newMappings.add ( mapping );
      }
    }
    this.mappings = newMappings;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Environment#symbols()
   */
  public Enumeration < S > symbols ()
  {
    return new Enumeration < S > ()
    {

      private Iterator < Mapping < S, E >> iterator = AbstractEnvironment.this.mappings
          .iterator ();


      public boolean hasMoreElements ()
      {
        return this.iterator.hasNext ();
      }


      public S nextElement ()
      {
        return this.iterator.next ().getSymbol ();
      }
    };
  }


  /**
   * Returns the string representation for the mutable store. Should be used for
   * debugging purpose only.
   * 
   * @return the string representation.
   * @see Object#toString()
   */
  @Override
  public String toString ()
  {
    StringBuilder builder = new StringBuilder ();
    builder.append ( '[' );
    for ( Mapping < S, E > mapping : this.mappings )
    {
      if ( builder.length () > 1 )
        builder.append ( ", " ); //$NON-NLS-1$
      builder.append ( mapping.getSymbol () );
      builder.append ( ": " ); //$NON-NLS-1$
      builder.append ( mapping.getEntry () );
    }
    builder.append ( ']' );
    return builder.toString ();
  }
}
