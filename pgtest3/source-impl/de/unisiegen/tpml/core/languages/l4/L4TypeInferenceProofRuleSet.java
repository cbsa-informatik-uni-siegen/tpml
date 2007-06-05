package de.unisiegen.tpml.core.languages.l4;

import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l2.L2Language;
import de.unisiegen.tpml.core.languages.l3.L3Language;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typeinference.DefaultTypeEquationProofNode;
import de.unisiegen.tpml.core.typeinference.DefaultTypeInferenceProofContext;
import de.unisiegen.tpml.core.typeinference.TypeEquation;
import de.unisiegen.tpml.core.typeinference.UnifyException;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.ListType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.RefType;
import de.unisiegen.tpml.core.types.TupleType;
import de.unisiegen.tpml.core.types.TypeVariable;

/**
 * The type proof rules for the <code>L4</code> language.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRuleSet
 */
public class L4TypeInferenceProofRuleSet extends L4TypeCheckerProofRuleSet {

  /**
	 * Allocates a new <code>L4TypeInferenceProofRuleSet</code> for the specified
	 * <code>language</code>.
	 * 
	 * @param language the <code>L4</code> or a derived language.
	 * @throws NullPointerException if <code>language</code> is
	 *           <code>null</code>.
	 */
	public L4TypeInferenceProofRuleSet(L4Language language) {
		super(language);
		
	//	unregister("P-LET");
	
	    unregister("ABSTR"); //$NON-NLS-1$
	    unregister("AND"); //$NON-NLS-1$
	    unregister("APP"); //$NON-NLS-1$
	    unregister("COND"); //$NON-NLS-1$
	    unregister ("LET"); //$NON-NLS-1$
	    unregister("OR"); //$NON-NLS-1$
	    unregister("REC"); //$NON-NLS-1$
	    unregister("LIST"); //$NON-NLS-1$
	    unregister("P-CONST"); //$NON-NLS-1$
	    unregister("P-ID"); //$NON-NLS-1$
	    unregister("P-LET"); //$NON-NLS-1$
	    unregister("TUPLE"); //$NON-NLS-1$
	    unregister("COND-1"); //$NON-NLS-1$
	    unregister("SEQ"); //$NON-NLS-1$
	    unregister("WHILE"); //$NON-NLS-1$
	    
//		 register the additional typeinference rule
	    registerByMethodName(L1Language.L1, "UNIFY", "applyUnify");  //$NON-NLS-1$ //$NON-NLS-2$
		
//		 register the type rules
	    registerByMethodName(L1Language.L1, "ABSTR", "applyAbstr");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L2Language.L2, "AND", "applyAnd");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L1Language.L1, "APP", "applyApp");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L1Language.L1, "COND", "applyCond");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L1Language.L1, "LET", "applyLet");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L2Language.L2, "OR", "applyOr");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L2Language.L2, "REC", "applyRec");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L3Language.L3, "LIST", "applyList");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L3Language.L3, "P-CONST", "applyPConst");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L3Language.L3, "P-ID", "applyPId");  //$NON-NLS-1$ //$NON-NLS-2$
	   // registerByMethodName(L3Language.L3, "P-LET", "applyPLet");
	    registerByMethodName(L3Language.L3, "TUPLE", "applyTuple");		  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L4Language.L4, "COND-1", "applyCond1");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L4Language.L4, "SEQ", "applySeq");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L4Language.L4, "WHILE", "applyWhile");  //$NON-NLS-1$ //$NON-NLS-2$
		
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
    DefaultTypeInferenceProofContext context = ( DefaultTypeInferenceProofContext ) pContext ;
    DefaultTypeEquationProofNode node = ( DefaultTypeEquationProofNode ) pNode ;
    TypeEquation eqn = node.getEquation ( ) ;
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
  public void unify ( DefaultTypeInferenceProofContext context ,
      DefaultTypeEquationProofNode node , TypeEquation eqn )
      throws UnifyException
  {
    // empty equation is not longer possible so this rule is not implemented
    MonoType left = eqn.getLeft ( ) ;
    MonoType right = eqn.getRight ( ) ;
    if ( left.equals ( right ) )
    {
      return ;
    }
    else if ( left instanceof TypeVariable || right instanceof TypeVariable )
    {
      // the left or right side of the equation is a type variable
      TypeVariable tvar = ( TypeVariable ) ( left instanceof TypeVariable ? left
          : right ) ;
      MonoType tau = ( left instanceof TypeVariable ? right : left ) ;
      // either tvar equals tau or tvar is not present in tau
      if ( ! tvar.equals ( tau )
          && ! tau.getTypeVariablesFree ( ).contains ( tvar ) )
      {
        DefaultTypeSubstitution s = new DefaultTypeSubstitution ( tvar , tau ) ;
        context.addSubstitution ( s ) ;
        return ;
      }
    }
    else if ( left instanceof ArrowType && right instanceof ArrowType )
    {
      ArrowType taul = ( ArrowType ) left ;
      ArrowType taur = ( ArrowType ) right ;
      // check which mode is choosen
      if ( node.getMode ( ) )
      {
        // advanced mode is choosen
        // unify tau1 = tau1', tau2 = tau2'
        unify ( context , node , new TypeEquation ( taul.getTau2 ( ) , taur
            .getTau2 ( ) ) ) ;
        TypeEquation eqn2 = new TypeEquation ( taul.getTau1 ( ) , taur
            .getTau1 ( ) ) ;
        eqn2 = ( TypeEquation ) eqn2.substitute ( context.getSubstitution ( ) ) ;
        unify ( context , node , eqn2 ) ;
      }
      else
      {
        // beginner mode is choosen
        // equations are added to list and will be unified later
        context.addEquation ( taul.getTau2 ( ) , taur.getTau2 ( ) ) ;
        context.addEquation ( taul.getTau1 ( ) , taur.getTau1 ( ) ) ;
      }
      return ;
    }
    else if ( left instanceof TupleType && right instanceof TupleType )
    {
      // cast to TupleType instances (tau and tau')
      TupleType taul = ( TupleType ) left ;
      TupleType taur = ( TupleType ) right ;
      // determine the sub types
      MonoType [ ] typesl = taul.getTypes ( ) ;
      MonoType [ ] typesr = taur.getTypes ( ) ;
      // check if the arities match
      if ( typesl.length == typesr.length )
      {
        // check all sub types
        // context.setEquations(eqns.getRemaining());
        for ( int n = 0 ; n < typesl.length ; ++ n )
        {
          context.addEquation ( typesl [ n ] , typesr [ n ] ) ;
        }
        return ;
      }
      // FALL-THROUGH: Otherwise it's a type error
    }
    else if ( left instanceof RefType && right instanceof RefType )
    {
      // cast to RefType instances (tau and tau')
      RefType taul = ( RefType ) left ;
      RefType taur = ( RefType ) right ;
      // we need to check {tau = tau'} as well
      context.addEquation ( taul.getTau ( ) , taur.getTau ( ) ) ;
      return ;
    }
    else if ( left instanceof ListType && right instanceof ListType )
    {
      // cast to ListType instances (tau and tau')
      ListType taul = ( ListType ) left ;
      ListType taur = ( ListType ) right ;
      // we need to check {tau = tau'} as well
      context.addEquation ( taul.getTau ( ) , taur.getTau ( ) ) ;
      return ;
    }
    throw new UnifyException ( eqn ) ;
  }
}

