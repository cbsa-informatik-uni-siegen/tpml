package de.unisiegen.tpml.core.languages;


import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.types.MonoType;


/**
 * A helper class for the parser.
 * 
 * @author Christian Fehler
 */
public class IdentifierTypePair
{

  /**
   * The {@link Identifier}.
   */
  private Identifier id;


  /**
   * The {@link MonoType}.
   */
  private MonoType tau;


  /**
   * Initializes the {@link IdentifierTypePair} class.
   * 
   * @param pId The input {@link Identifier}.
   * @param pTau The input {@link MonoType}.
   */
  public IdentifierTypePair ( Identifier pId, MonoType pTau )
  {
    this.id = pId;
    this.tau = pTau;
  }


  /**
   * Returns the id.
   * 
   * @return The id.
   * @see #id
   */
  public Identifier getId ()
  {
    return this.id;
  }


  /**
   * Returns the tau.
   * 
   * @return The tau.
   * @see #tau
   */
  public MonoType getTau ()
  {
    return this.tau;
  }
}
