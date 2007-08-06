package de.unisiegen.tpml.core ;


import de.unisiegen.tpml.core.expressions.Expression ;


/**
 * A specialization of the {@link de.unisiegen.tpml.core.ProofModel} interface
 * for proofs based on {@link de.unisiegen.tpml.core.expressions.Expression}s.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.ExpressionProofNode
 * @see de.unisiegen.tpml.core.ProofModel
 */
public interface ExpressionProofModel extends ProofModel
{
  //
  // Actions
  //
  /**
   * Returns <code>true</code> if the expression for the <code>node</code>
   * contains syntactic sugar. If <code>recursive</code> is <code>true</code>
   * and the expression for the <code>node</code> is not syntactic sugar, its
   * sub expressions will also be checked.
   * 
   * @param node the proof node whose expression should be checked for syntactic
   *          sugar.
   * @param recursive check in sub nodes.
   * @return <code>true</code> if the expression of the <code>node</code>
   *         contains syntactic sugar according to the language for this model.
   * @throws IllegalArgumentException if the <code>node</code> is invalid for
   *           this proof model.
   * @throws NullPointerException if the <code>node</code> is
   *           <code>null</code>.
   * @see #translateToCoreSyntax(ExpressionProofNode, boolean)
   * @see de.unisiegen.tpml.core.languages.LanguageTranslator#containsSyntacticSugar(Expression,
   *      boolean)
   */
  public boolean containsSyntacticSugar ( ExpressionProofNode node ,
      boolean recursive ) ;


  /**
   * Translates the expression for the <code>node</code> to core syntax
   * according to the language for this model. If <code>recursive</code> is
   * <code>true</code>, all sub expressions will also be translated to core
   * syntax, otherwise only the outermost expression will be translated.
   * 
   * @param node the proof node whose expression should be translated to core
   *          syntax.
   * @param recursive whether to translate the expression recursively.
   * @throws IllegalArgumentException if the <code>node</code> is invalid for
   *           this proof model, or the <code>node</code>'s expression does
   *           not contain syntactic sugar.
   * @throws IllegalStateException if any steps were performed on the
   *           <code>node</code> already.
   * @throws NullPointerException if the <code>node</code> is
   *           <code>null</code>.
   * @see #containsSyntacticSugar(ExpressionProofNode, boolean)
   * @see de.unisiegen.tpml.core.languages.LanguageTranslator#translateToCoreSyntax(Expression,
   *      boolean)
   */
  public void translateToCoreSyntax ( ExpressionProofNode node ,
      boolean recursive ) ;


  //
  // Tree queries
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofModel#getRoot()
   */
  public ExpressionProofNode getRoot ( ) ;


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofModel#getChild(java.lang.Object, int)
   */
  public ExpressionProofNode getChild ( Object parent , int index ) ;
}
