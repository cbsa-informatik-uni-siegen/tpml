package de.unisiegen.tpml.core.languages.l2;


import de.unisiegen.tpml.core.expressions.Recursion;
import de.unisiegen.tpml.core.languages.l1.L1TypeCheckerProofRuleSet;
import de.unisiegen.tpml.core.typechecker.TypeCheckerExpressionProofNode;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.types.MonoType;


/**
 * The type proof rules for the <code>L2</code> language.
 * 
 * @author Benedikt Meurer
 * @version $Id$
 * @see de.unisiegen.tpml.core.languages.l1.L1TypeCheckerProofRuleSet
 */
public class L2TypeCheckerProofRuleSet extends L1TypeCheckerProofRuleSet
{

  /**
   * Allocates a new <code>L2TypeCheckerProofRuleSet</code> for the specified
   * <code>language</code>.
   * 
   * @param language the <code>L2</code> or a derived language.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   */
  public L2TypeCheckerProofRuleSet ( L2Language language )
  {
    super ( language );
    // register the additional type rules
    registerByMethodName ( L2Language.L2, "REC", "applyRec" ); //$NON-NLS-1$ //$NON-NLS-2$
  }


  /**
   * Applies the <b>(REC)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param pNode the type checker proof node.
   */
  public void applyRec ( TypeCheckerProofContext context,
      TypeCheckerProofNode pNode )
  {
    TypeCheckerExpressionProofNode node = ( TypeCheckerExpressionProofNode ) pNode;
    // determine the type for the identifier
    Recursion recursion = ( Recursion ) node.getExpression ();
    MonoType tau1 = recursion.getTau ();
    if ( tau1 == null )
    {
      // need to generate a type variable
      tau1 = context.newTypeVariable ();
    }
    // add equation tau = tau1
    context.addEquation ( node.getType (), tau1 );
    // generate new child node
    TypeEnvironment environment = node.getEnvironment ();
    context.addProofNode ( node,
        environment.extend ( recursion.getId (), tau1 ), recursion.getE (),
        tau1 );
  }
}
