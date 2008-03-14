package de.unisiegen.tpml.core.subtypingrec;


import de.unisiegen.tpml.core.types.MonoType;


/**
 * The proof context for the subtyping. The proof context acts as an interface
 * to the proof model for the type rules.
 * 
 * @author Benjamin Mies
 * @version $Id$
 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofModel
 */
public interface RecSubTypingProofContext
{

  /**
   * Adds a new child node below the <code>node</code>, to prove if the first
   * <code>type</code> is a subtype of the second <code>type</code>
   * 
   * @param node the parent node.
   * @param type the first type (subtype)
   * @param type2 the second type (general type)
   * @throws IllegalArgumentException if the <code>node</code> is invalid for
   *           the proof model that is associated with this proof context.
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   */
  public void addProofNode ( RecSubTypingProofNode node, MonoType type,
      MonoType type2 );


  /**
   * Add a new default subtype to the list of already seen default subtypes
   * 
   * @param type the subtype of this object
   * @param type2 the overtype of this object
   */
  public void addSeenType ( MonoType type, MonoType type2 );
}
