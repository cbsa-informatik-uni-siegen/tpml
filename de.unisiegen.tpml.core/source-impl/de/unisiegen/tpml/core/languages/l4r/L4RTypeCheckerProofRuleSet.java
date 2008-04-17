package de.unisiegen.tpml.core.languages.l4r;


import de.unisiegen.tpml.core.expressions.Constant;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l2.L2Language;
import de.unisiegen.tpml.core.languages.l3.L3Language;
import de.unisiegen.tpml.core.languages.l4.L4Language;
import de.unisiegen.tpml.core.languages.l4.L4TypeCheckerProofRuleSet;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;


/**
 * The type proof rules for the <code>L4R</code> language.
 * 
 * @author Christian Fehler
 * @version $Id$
 * @see de.unisiegen.tpml.core.languages.l4.L4TypeCheckerProofRuleSet
 */
public class L4RTypeCheckerProofRuleSet extends L4TypeCheckerProofRuleSet
{

  /**
   * Allocates a new <code>L4RTypecheckerProofRuleSet</code> for the specified
   * <code>language</code>.
   * 
   * @param language the <code>L4R</code> or a derived language.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   */
  public L4RTypeCheckerProofRuleSet ( L4RLanguage language )
  {
    super ( language );

    // reorganize the registered rules

    unregister ( "ABSTR" ); //$NON-NLS-1$
    unregister ( "AND" ); //$NON-NLS-1$ 
    unregister ( "APP" ); //$NON-NLS-1$ 
    unregister ( "COND" ); //$NON-NLS-1$ 
    unregister ( "OR" ); //$NON-NLS-1$ 
    unregister ( "COERCE" ); //$NON-NLS-1$ 
    unregister ( "SUBTYPE" ); //$NON-NLS-1$ 
    unregister ( "REC" ); //$NON-NLS-1$ 
    unregister ( "LIST" ); //$NON-NLS-1$ 
    unregister ( "P-CONST" ); //$NON-NLS-1$ 
    unregister ( "P-ID" ); //$NON-NLS-1$ 
    unregister ( "P-LET" ); //$NON-NLS-1$ 
    unregister ( "TUPLE" ); //$NON-NLS-1$ 
    unregister ( "COND-1" ); //$NON-NLS-1$ 
    unregister ( "SEQ" ); //$NON-NLS-1$ 
    unregister ( "WHILE" ); //$NON-NLS-1$ 

    registerByMethodName ( L1Language.L1, "ABSTR", "applyAbstr" ); //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2Language.L2, "AND", "applyAnd" );//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1, "APP", "applyApp" );//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1, "COND", "applyCond" );//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1, "CONST", "applyConst" );//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1, "ID", "applyId" );//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1, "LET", "applyLet" );//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2Language.L2, "OR", "applyOr" );//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1, "COERCE", "applyCoercion" );//$NON-NLS-1$ //$NON-NLS-2$ 
    registerByMethodName ( L1Language.L1, "SUBTYPE", "applySubtype" ); //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2Language.L2, "REC", "applyRec" ); //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L3Language.L3, "LIST", "applyList" ); //$NON-NLS-1$//$NON-NLS-2$
    registerByMethodName ( L3Language.L3, "TUPLE", "applyTuple" ); //$NON-NLS-1$//$NON-NLS-2$
    registerByMethodName ( L4Language.L4, "COND-1", "applyCond1" ); //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L4Language.L4, "SEQ", "applySeq" ); //$NON-NLS-1$//$NON-NLS-2$
    registerByMethodName ( L4Language.L4, "WHILE", "applyWhile" ); //$NON-NLS-1$ //$NON-NLS-2$
  }


  /**
   * Applies the <b>(CONST)</b> rule to the <code>node</code> using the
   * <code>context</code> and instantiates poly types.
   * 
   * @param context the type checker proof context.
   * @param node the type checker proof node.
   */
  @Override
  public void applyConst ( TypeCheckerProofContext context,
      TypeCheckerProofNode node )
  {
    Constant constant = ( Constant ) node.getExpression ();
    context.addEquation ( node.getType (), context.instantiate ( context
        .getTypeForExpression ( constant ) ) );
  }
}
