package de.unisiegen.tpml.core.languages.l2 ;



import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typeinference.TypeEquationProofNode;
import de.unisiegen.tpml.core.typeinference.TypeEquationTypeInference;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofContext;
import de.unisiegen.tpml.core.typeinference.UnifyException;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.RecType;
import de.unisiegen.tpml.core.types.TypeVariable;
import de.unisiegen.tpml.core.util.Debug;

/**
 * The type proof rules for the <code>L2</code> language.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 * @see de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRuleSet
 */
public class L2TypeInferenceProofRuleSet extends L2TypeCheckerProofRuleSet
{
  /**
   * Allocates a new <code>L2TypeInferenceProofRuleSet</code> for the
   * specified <code>language</code>.
   * 
   * @param language the <code>L2</code> or a derived language.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   */
  public L2TypeInferenceProofRuleSet ( L2Language language )
  {
    super ( language ) ;
    // unregister the type rules
    unregister ( "ABSTR" ) ; //$NON-NLS-1$
    unregister ( "AND" ) ; //$NON-NLS-1$
    unregister ( "APP" ) ; //$NON-NLS-1$
    unregister ( "COND" ) ; //$NON-NLS-1$
    unregister ( "CONST" ) ; //$NON-NLS-1$
    unregister ( "ID" ) ; //$NON-NLS-1$
    unregister ( "LET" ) ; //$NON-NLS-1$
    unregister ( "OR" ) ; //$NON-NLS-1$
    unregister ( "REC" ) ; //$NON-NLS-1$
    // register the additional typeinference rule
    registerByMethodName ( L1Language.L1 , "UNIFY" , "applyUnify" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    // register the type rules
    registerByMethodName ( L1Language.L1 , "ABSTR" , "applyAbstr" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2Language.L2 , "AND" , "applyAnd" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1 , "APP" , "applyApp" ) ;//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1 , "COND" , "applyCond" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1 , "CONST" , "applyConst" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1 , "ID" , "applyId" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1 , "LET" , "applyLet" ) ;//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2Language.L2 , "OR" , "applyOr" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2Language.L2 , "REC" , "applyRec" ) ; //$NON-NLS-1$ //$NON-NLS-2$
  }



	  /**
	   * Applies the <b>(UNIFY)</b> rule to the <code>node</code> using the
	   * <code>context</code>.
	   * 
	   * @param pContext the type inference proof context.
	   * @param pNode the type inference proof node.
	   * @throws UnifyException
	   */
	  public void applyUnify ( TypeCheckerProofContext pContext ,
	      TypeCheckerProofNode pNode ) throws UnifyException
	  {
	    // convert in needed types
	    TypeInferenceProofContext context = ( TypeInferenceProofContext ) pContext ;
	    TypeEquationProofNode node = ( TypeEquationProofNode ) pNode ;
	    TypeEquationTypeInference eqn = node.getEquation ( ) ;
	    unify ( context , node , eqn ) ;
	  }




	  /**
	   * internal implementation of the unify rule now we are able to call unify
	   * recursive if needed so we get different handling for beginner or advanced
	   * user 
	   * 
	   * @param context the casted default type inference proof context.
	   * @param node the casted type equation proof node.
	   * @param eqn the actual type equation
	   * @throws UnifyException
	   */
	  public void unify ( TypeInferenceProofContext context ,
	      TypeEquationProofNode node , TypeEquationTypeInference eqn )
	      throws UnifyException
	  {
    // empty equation is not longer possible so this rule is not implemented
    MonoType left = eqn.getLeft ( ) ;
    MonoType right = eqn.getRight ( ) ;
    // ASSUME
    if ( context.getSeenTypes ( ).contains ( eqn ) )
    {
      Debug.out.println (
          "Unify - ASSUME      " + left + " = " + right , Debug.CHRISTIAN ) ; //$NON-NLS-1$//$NON-NLS-2$
      return ;
    }
    // TRIV
    else if ( left.equals ( right ) )
    {
      Debug.out.println (
          "Unify - TRIV        " + left + " = " + right , Debug.CHRISTIAN ) ; //$NON-NLS-1$//$NON-NLS-2$
      context.addSeenType ( eqn ) ;
      return ;
    }
    // MU-LEFT
    else if ( left instanceof RecType )
    {
      RecType recType = ( RecType ) left ;
      Debug.out.println (
          "Unify - MU-LEFT     " + left + " = " + right , Debug.CHRISTIAN ) ; //$NON-NLS-1$//$NON-NLS-2$
      context.addEquation ( recType.getTau ( ).substitute (
          recType.getTypeName ( ) , recType ) , right ) ;
      context.addSeenType ( eqn ) ;
      return ;
    }
    // MU-RIGHT
    else if ( right instanceof RecType )
    {
      RecType recType = ( RecType ) right ;
      Debug.out.println (
          "Unify - MU-RIGHT    " + left + " = " + right , Debug.CHRISTIAN ) ; //$NON-NLS-1$//$NON-NLS-2$
      context.addEquation ( left , recType.getTau ( ).substitute (
          recType.getTypeName ( ) , recType ) ) ;
      context.addSeenType ( eqn ) ;
      return ;
    }
    // VAR
    else if ( left instanceof TypeVariable || right instanceof TypeVariable )
    {
      // the left or right side of the equation is a type variable
      TypeVariable tvar = ( TypeVariable ) ( left instanceof TypeVariable ? left
          : right ) ;
      MonoType tau = ( left instanceof TypeVariable ? right : left ) ;
      Debug.out.println (
          "Unify - VAR         " + tvar + " = " + tau , Debug.CHRISTIAN ) ; //$NON-NLS-1$//$NON-NLS-2$
      // either tvar equals tau or tvar is not present in tau
      if ( ! tvar.equals ( tau )
          && ! tau.getTypeVariablesFree ( ).contains ( tvar ) )
      {
        context.addSeenType ( eqn ) ;
        DefaultTypeSubstitution s = new DefaultTypeSubstitution ( tvar , tau ) ;
        context.addSubstitution ( s ) ;
        return ;
      }
      // Error, because of a recursive type like: alpha1 = int -> alpha1
    }
    // ARROW
    else if ( left instanceof ArrowType && right instanceof ArrowType )
    {
      ArrowType taul = ( ArrowType ) left ;
      ArrowType taur = ( ArrowType ) right ;
      Debug.out.println (
          "Unify - ARROW       " + left + " = " + right , Debug.CHRISTIAN ) ; //$NON-NLS-1$//$NON-NLS-2$
      // check which mode is choosen
      if ( node.getMode ( ) )
      {
        // advanced mode is choosen
        // unify tau1 = tau1', tau2 = tau2'
        context.addSeenType ( eqn ) ;
        unify ( context , node , new TypeEquationTypeInference ( taul
            .getTau2 ( ) , taur.getTau2 ( ) ) ) ;
        TypeEquationTypeInference eqn2 = new TypeEquationTypeInference ( taul
            .getTau1 ( ) , taur.getTau1 ( ) ) ;
        eqn2 = ( TypeEquationTypeInference ) eqn2.substitute ( context
            .getSubstitution ( ) ) ;
        unify ( context , node , eqn2 ) ;
      }
      else
      {
        // beginner mode is choosen
        // equations are added to list and will be unified later
        context.addSeenType ( eqn ) ;
        context.addEquation ( taul.getTau2 ( ) , taur.getTau2 ( ) ) ;
        context.addEquation ( taul.getTau1 ( ) , taur.getTau1 ( ) ) ;
      }
      return ;
    }
    throw new UnifyException ( eqn ) ;
  }
}
