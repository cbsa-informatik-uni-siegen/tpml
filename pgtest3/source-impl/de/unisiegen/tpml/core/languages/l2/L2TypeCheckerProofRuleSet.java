package de.unisiegen.tpml.core.languages.l2 ;


import de.unisiegen.tpml.core.expressions.Recursion ;
import de.unisiegen.tpml.core.languages.l1.L1TypeCheckerProofRuleSet ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode ;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * The type proof rules for the <code>L2</code> language.
 * 
 * @author Benedikt Meurer
 * @version $Id:L2TypeCheckerProofRuleSet.java 419 2006-10-12 09:09:37Z benny $
 * @see de.unisiegen.tpml.core.languages.l1.L1TypeCheckerProofRuleSet
 */
public class L2TypeCheckerProofRuleSet extends L1TypeCheckerProofRuleSet
{
  //
  // Constructor
  //
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
    super ( language ) ;
    // register the additional type rules
    registerByMethodName ( L2Language.L2 , "REC" , "applyRec" ) ;
  }


  //
  // The (REC) rule
  //
  /**
   * Applies the <b>(REC)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param node the type checker proof node.
   */
  public void applyRec ( TypeCheckerProofContext context ,
      TypeCheckerProofNode node )
  {
    // determine the type for the identifier
    Recursion recursion = ( Recursion ) node.getExpression ( ) ;
    MonoType tau1 = recursion.getTau ( ) ;
    if ( tau1 == null )
    {
      // need to generate a type variable
      tau1 = context.newTypeVariable ( ) ;
    }
    // add equation tau = tau1
    context.addEquation ( node.getType ( ) , tau1 ) ;
    // generate new child node
    TypeEnvironment environment = node.getEnvironment ( ) ;
    context.addProofNode ( node , environment.extend ( recursion.getId ( )
        .getName ( ) , tau1 ) , recursion.getE ( ) , tau1 ) ;
  }
}
