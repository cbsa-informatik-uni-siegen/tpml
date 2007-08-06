package de.unisiegen.tpml.core.minimaltyping ;


import de.unisiegen.tpml.core.subtypingrec.DefaultSubType ;
import de.unisiegen.tpml.core.typechecker.SeenTypes ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * This node type represents the subtype relations in the Minimal Typing
 * Algorithm. The node contains to {@link MonoType}s, the first of these is the
 * subtype, and the second the overtype.
 * 
 * @author Benjamin Mies
 */
public interface MinimalTypingTypesProofNode extends MinimalTypingProofNode
{
  /**
   * Returns the second type for this type node, which is either a type variable
   * or a concrete type.
   * 
   * @return the second monomorphic type for this type node.
   */
  public MonoType getType2 ( ) ;


  /**
   * Get the already seen types for this proof node
   * 
   * @return all already seen types
   */
  public SeenTypes < DefaultSubType > getSeenTypes ( ) ;


  /**
   * Get the subtype object of this node
   * 
   * @return the subtype object of this node
   */
  public DefaultSubType getSubType ( ) ;
}
