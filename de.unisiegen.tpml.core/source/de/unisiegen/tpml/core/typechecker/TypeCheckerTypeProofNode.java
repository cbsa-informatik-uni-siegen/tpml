package de.unisiegen.tpml.core.typechecker ;


import de.unisiegen.tpml.core.types.MonoType ;


/**
 * The public interface for the nodes with two types in the type checker
 * algorithm to check subtype relations between this two given types.
 * 
 * @author Benjamin Mies
 */
public interface TypeCheckerTypeProofNode extends TypeCheckerProofNode
{
  /**
   * Get the second type (supertype) of this proof node
   * 
   * @return the second type of this proof node
   */
  public MonoType getType2 ( ) ;
}
