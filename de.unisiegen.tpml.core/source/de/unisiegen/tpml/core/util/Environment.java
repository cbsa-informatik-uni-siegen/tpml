package de.unisiegen.tpml.core.util;


import java.util.Enumeration;

import de.unisiegen.tpml.core.latex.LatexPrintable;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;


/**
 * Generic base interface for all environments used while proving properties
 * about programs. For example the type environment used in the type checker
 * inherits this interface, and the store used in the big and small step
 * interpreters also inherits this interface.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Id$
 * @param <S> The symbol.
 * @param <E> The entry.
 * @see de.unisiegen.tpml.core.util.AbstractEnvironment
 */
public interface Environment < S, E > extends PrettyPrintable, LatexPrintable
{

  /**
   * Returns <code>true</code> if this environment contains an entry for the
   * specified <code>symbol</code>. Otherwise <code>false</code> is
   * returned. The meaning of the <code>symbol</code> depends on the actual
   * environment. For example, for stores, this is a
   * {@link de.unisiegen.tpml.core.expressions.Location}, and for the the type
   * environment, this is an identifier (a {@link String}).
   * 
   * @param symbol the symbol to check.
   * @return <code>true</code> if the environment contains an entry for the
   *         <code>symbol</code>, otherwise <code>false</code>.
   * @throws NullPointerException if <code>symbol</code> is <code>null</code>.
   * @see #get(Object)
   */
  public boolean containsSymbol ( S symbol );


  /**
   * Returns the entry that is stored for the given <code>symbol</code> in
   * this environment. Throws a {@link java.util.NoSuchElementException} if the
   * <code>symbol</code> is invalid for this environment.
   * 
   * @param symbol the symbol for which to return the entry. See the description
   *          of the {@link #containsSymbol(Object)} method for details about
   *          the <code>symbol</code>.
   * @return the entry for the <code>symbol</code>.
   * @throws java.util.NoSuchElementException if the <code>symbol</code> is
   *           invalid for the environment.
   * @throws NullPointerException if <code>symbol</code> is <code>null</code>.
   * @see #containsSymbol(Object)
   */
  public E get ( S symbol );


  /**
   * Returns an enumeration of the symbols within this store, in the reverse
   * order of the allocation. That is, the last inserted symbol is returned
   * first.
   * 
   * @return an enumeration of the symbols. See the description of the
   *         {@link #containsSymbol(Object)} method for details about the
   *         symbols.
   * @see #get(Object)
   */
  public Enumeration < S > symbols ();
}
