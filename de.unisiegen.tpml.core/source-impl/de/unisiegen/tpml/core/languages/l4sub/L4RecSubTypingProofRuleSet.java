package de.unisiegen.tpml.core.languages.l4sub;


import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l3sub.L3RecSubTypingProofRuleSet;
import de.unisiegen.tpml.core.languages.l4.L4Language;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofContext;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.RefType;


/**
 * The subtype proof rules for the <code>L1</code> language.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.subtyping.AbstractSubTypingProofRuleSet
 */
public class L4RecSubTypingProofRuleSet extends L3RecSubTypingProofRuleSet
{

  /**
   * Allocates a new <code>L1SubTypingProofRuleSet</code> for the specified
   * <code>language</code>.
   * 
   * @param language the <code>L1</code> or a derived language.
   * @param mode the mode chosen by the user
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   */
  public L4RecSubTypingProofRuleSet ( Language language, boolean mode )
  {
    super ( language, mode );

    unregister ( "REFL" ); //$NON-NLS-1$
    unregister ( "S-ASSUME" ); //$NON-NLS-1$

    // register the type rules
    registerByMethodName ( L4Language.L4, "REF", "applyRef" ); //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1, "REFL", "applyRefl" ); //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1, "S-ASSUME", "applyAssume" ); //$NON-NLS-1$ //$NON-NLS-2$
  }


  /**
   * Applies the <b>(REF)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the subtyping proof context.
   * @param node the subtyping proof node.
   */
  public void applyRef ( RecSubTypingProofContext context,
      RecSubTypingProofNode node )
  {
    RefType type;
    RefType type2;

    type = ( RefType ) node.getLeft ();
    type2 = ( RefType ) node.getRight ();

    MonoType tau = type.getTau ();
    MonoType tau2 = type2.getTau ();

    context.addProofNode ( node, tau, tau2 );
    context.addSeenType ( node.getLeft (), node.getRight () );

  }
}
