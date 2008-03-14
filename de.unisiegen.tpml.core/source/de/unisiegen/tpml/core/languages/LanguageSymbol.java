package de.unisiegen.tpml.core.languages;


import java_cup.runtime.Symbol;


/**
 * Instances of this class represent symbols that will be returned from the
 * {@link de.unisiegen.tpml.core.languages.LanguageScanner}s.
 * 
 * @author Benedikt Meurer
 * @version $Id$
 * @see java_cup.runtime.Symbol
 * @see de.unisiegen.tpml.core.languages.LanguageScanner
 */
public final class LanguageSymbol extends Symbol
{

  //
  // Attributes
  //
  /**
   * The name of the symbol, i.e. <code>"START"</code> for the start symbol.
   * 
   * @see #getName()
   */
  private String name;


  //
  // Constructors
  //
  /**
   * Allocates a new <code>LanguageSymbol</code> with the specified
   * <code>name</code> and the specified symbol <code>id</code>. The
   * <code>left</code> and <code>right</code> values specify character
   * offsets in the source stream. They may be <code>-1</code> if the offsets
   * aren't available. The <code>value</code> is the value associated with
   * this symbol, for example, the value assigned to a symbol by the lexer. May
   * be <code>null</code> if no value is available.
   * 
   * @param pName the name of the symbol.
   * @param pId the unique identifier of the symbol.
   * @param pLeft the left position in the source, or <code>-1</code>.
   * @param pRight the right position in the source, or <code>-1</code>.
   * @param pValue the value assigned to the symbol, or <code>null</code>.
   * @see #getLeft()
   * @see #getRight()
   * @see #getName()
   */
  LanguageSymbol ( String pName, int pId, int pLeft, int pRight, Object pValue )
  {
    super ( pId, pLeft, pRight, pValue );
    this.name = pName;
  }


  //
  // Accessors
  //
  /**
   * Returns the unique identifier of this symbol.
   * 
   * @return the unique identifier of this symbol.
   */
  public int getId ()
  {
    return this.sym;
  }


  /**
   * Returns the left position of the symbol in the source stream or
   * <code>-1</code> if the left position is not known.
   * 
   * @return the left position of the symbol, or <code>-1</code>.
   * @see #getRight()
   */
  public int getLeft ()
  {
    return this.left;
  }


  /**
   * Returns the right position of the symbol in the source stream or
   * <code>-1</code> if the right position is not known.
   * 
   * @return the right position of the symbol, or <code>-1</code>.
   * @see #getLeft()
   */
  public int getRight ()
  {
    return this.right;
  }


  /**
   * Returns the name of the symbol, i.e. <code>"START"</code> in case of the
   * start symbol of the parser.
   * 
   * @return the name of the symbol.
   */
  public String getName ()
  {
    return this.name;
  }


  //
  // Overwritten methods
  //
  /**
   * Returns the string representation for this <code>LanguageSymbol</code>,
   * which is simply the name of the symbol.
   * 
   * @return the string representation for this symbol.
   * @see Object#toString()
   */
  @Override
  public String toString ()
  {
    return this.name;
  }
}
