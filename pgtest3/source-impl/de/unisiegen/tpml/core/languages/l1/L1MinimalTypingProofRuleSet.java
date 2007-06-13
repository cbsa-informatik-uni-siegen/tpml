package de.unisiegen.tpml.core.languages.l1;

import de.unisiegen.tpml.core.expressions.And;
import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Condition;
import de.unisiegen.tpml.core.expressions.Constant;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.InfixOperation;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.expressions.Or;
import de.unisiegen.tpml.core.minimaltyping.AbstractMinimalTypingProofRuleSet;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingExpressionProofNode;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofContext;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingTypesProofNode;
import de.unisiegen.tpml.core.minimaltyping.TypeEnvironment;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.BooleanType;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * The minimal type proof rules for the <code>L1</code> language.
 * 
 * @author Benjamin Mies
 * 
 * @see de.unisiegen.tpml.core.minimaltyping.AbstractMinimalTypingProofRuleSet
 */
public class L1MinimalTypingProofRuleSet extends
		AbstractMinimalTypingProofRuleSet {
	/**
	 * Allocates a new <code>L1MinimalTypingProofRuleSet</code> for the specified
	 * <code>language</code>.
	 * 
	 * @param language the <code>L1</code> or a derived language.
	 * @throws NullPointerException if <code>language</code> is
	 *           <code>null</code>.
	 */
	public L1MinimalTypingProofRuleSet ( L1Language language ) {
		super ( language );
		// register the type rules
		registerByMethodName ( L1Language.L1, "SUBTYPE", "applySubtype" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1,
				"LET", "applyLet", "updateLet" );//$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1,
				"APP-SUBSUME", "applyAppSubsume", "updateAppSubsume" );//$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1,
				"COND-SUBSUME", "applyCondSubsume", "updateCondSubsume" );//$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "ABSTR", "applyAbstr", "updateAbstr" );
		registerByMethodName ( L1Language.L1, "AND", "applyAnd", "updateAnd" );//$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "OR", "applyOr", "updateOr" );//$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "CONST", "applyConst" );//$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "ID", "applyId" );//$NON-NLS-1$ //$NON-NLS-2$

	}

	/**
	 * Applies the <b>(ID)</b> rule to the
	 * <code>node</node> using the <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param node the minimal typing proof node.
	 */
	public void applyId ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		MonoType type = ( MonoType ) node.getEnvironment ( ).get (
				( ( Identifier ) node.getExpression ( ) ) );

		context.setNodeType ( node, type );
	}

	/**
	 * Applies the <b>(CONST)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param node the minimal typing proof node.
	 */
	public void applyConst ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		Constant constant = ( Constant ) node.getExpression ( );
		MonoType type = ( MonoType ) context.getTypeForExpression ( constant );

		context.setNodeType ( node, type );
	}
	
	/**
	 * Applies the <b>(AND)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param node the minimal typing proof node.
	 */
  public void applyAnd ( MinimalTypingProofContext context ,
      MinimalTypingProofNode pNode ) {
  	MinimalTypingExpressionProofNode node = (MinimalTypingExpressionProofNode) pNode;
    And and = ( And ) node.getExpression ( ) ;
    // generate new child node
    context.addProofNode ( node , node.getEnvironment ( ) , and.getE1 ( ) ) ;
    
  }
  
  public void updateAnd ( MinimalTypingProofContext context ,
      MinimalTypingProofNode pNode ) {
  	MinimalTypingExpressionProofNode node = (MinimalTypingExpressionProofNode) pNode;
  	And and = ( And ) node.getExpression ( ) ;
  	if ( node.getChildCount ( ) == 1 && node.getFirstChild ( ).isFinished ( ) ) {
  		context.addProofNode ( node , node.getEnvironment ( ) , and.getE2 ( ) ) ;
  	}
  	else if ( node.getChildCount ( ) == 2 && node.getChildAt ( 1 ).isFinished ( ) ) {
  		if ( node.getFirstChild ( ).getType ( ) instanceof BooleanType
  				&& node.getChildAt ( 1 ).getType ( ) instanceof BooleanType)
  			context.setNodeType ( node,  new BooleanType() );
  		else
  			throw new RuntimeException("types of children not boolean");
  	}
  		
  }
  
	/**
	 * Applies the <b>(OR)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param node the minimal typing proof node.
	 */
  public void applyOr ( MinimalTypingProofContext context ,
      MinimalTypingProofNode pNode ) {
  	MinimalTypingExpressionProofNode node = (MinimalTypingExpressionProofNode) pNode;
    Or or = ( Or ) node.getExpression ( ) ;
    // generate new child node
    context.addProofNode ( node , node.getEnvironment ( ) , or.getE1 ( ) ) ;
    
  }
  
  public void updateOr ( MinimalTypingProofContext context ,
      MinimalTypingProofNode pNode ) {
  	MinimalTypingExpressionProofNode node = (MinimalTypingExpressionProofNode) pNode;
  	Or or = ( Or ) node.getExpression ( ) ;
  	if ( node.getChildCount ( ) == 1 && node.getFirstChild ( ).isFinished ( ) ) {
  		context.addProofNode ( node , node.getEnvironment ( ) , or.getE2 ( ) ) ;
  	}
  	else if ( node.getChildCount ( ) == 2 && node.getChildAt ( 1 ).isFinished ( ) ) {
  		if ( node.getFirstChild ( ).getType ( ) instanceof BooleanType
  				&& node.getChildAt ( 1 ).getType ( ) instanceof BooleanType)
  			context.setNodeType ( node, new BooleanType() );
  		else
  			throw new RuntimeException("types of children not boolean");
  	}
  		
  }
  

	/**
	 * Applies the <b>(APP-SUBSUME)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param node the minimal typing proof node.
	 */
	public void applyAppSubsume ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		try {
			Application app = ( Application ) node.getExpression ( );
			context.addProofNode ( node, node.getEnvironment ( ), app.getE1 ( ) );
		} catch ( ClassCastException e ) {
			// generate new child nodes
			InfixOperation infixOperation = ( InfixOperation ) node.getExpression ( );
			Application application = new Application ( infixOperation.getOp ( ),
					infixOperation.getE1 ( ) );
			context.addProofNode ( node, node.getEnvironment ( ), application );

		}
	}

	public void updateAppSubsume ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;

		if ( node.getChildCount ( ) == 1 && node.getChildAt ( 0 ).isFinished ( ) ) {
			// Expression could be an Application or an InfixOperation, so we have to check both
			// First check if expression is an Application
			try {
				Application app = ( Application ) node.getExpression ( );
				context.addProofNode ( node, node.getEnvironment ( ), app.getE2 ( ) );
			}
			// otherwise check if it is an InfixOperation
			catch ( ClassCastException e ) {
				InfixOperation infixOperation = ( InfixOperation ) node
						.getExpression ( );
				context.addProofNode ( node, node.getEnvironment ( ), infixOperation
						.getE2 ( ) );
			}
		} else if ( node.getChildCount ( ) == 2
				&& node.getChildAt ( 1 ).isFinished ( ) ) {
			MonoType type = ( node.getChildAt ( 1 ) ).getType ( );
			ArrowType arrow = ( ArrowType ) node.getChildAt ( 0 ).getType ( );
			MonoType type2 = arrow.getTau1 ( );
			context.addProofNode ( node, type, type2 );
		} else if ( node.getChildCount ( ) == 3 && node.isFinished ( ) ) {
			ArrowType arrow = ( ArrowType ) node.getChildAt ( 0 ).getType ( );
			MonoType type = arrow.getTau2 ( );
			context.setNodeType ( node, type );
		}
	}

	/**
	 * Applies the <b>(ABSTR)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param node the minimal typing proof node.
	 */
	public void applyAbstr ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		Lambda lambda = ( Lambda ) node.getExpression ( );
		
		// check if the user entered a type
		if (lambda.getTau ( )== null)
			throw new RuntimeException("Please enter type for " +lambda.toString ( ));

		TypeEnvironment environment = node.getEnvironment ( );

		context.addProofNode ( node, environment.extend ( lambda.getId ( ), lambda
				.getTau ( ) ), lambda.getE ( ) );
	}

	public void updateAbstr ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		if ( node.getFirstChild ( ).isFinished ( ) ) {
			MonoType type = node.getFirstChild ( ).getType ( );
			context.setNodeType ( node, type );
		}
	}

	/**
	 * Applies the <b>(COND-SUBSUME)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param node the minimal typing proof node.
	 */
	public void applyCondSubsume ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		Condition cond = ( Condition ) node.getExpression ( );
		context.addProofNode ( node, node.getEnvironment ( ), cond.getE0 ( ) );
	}

	public void updateCondSubsume ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		Condition cond = ( Condition ) node.getExpression ( );
		if ( node.getChildCount ( ) == 1 && node.getChildAt ( 0 ).isFinished ( ) ) {
			if ( ! ( node.getChildAt ( 0 ).getType ( ) instanceof BooleanType ) )
				throw new RuntimeException ( "first type not instance of BooleanType" );
			context.addProofNode ( node, node.getEnvironment ( ), cond.getE1 ( ) );
		} else if ( node.getChildCount ( ) == 2
				&& node.getChildAt ( 1 ).isFinished ( ) ) {
			context.addProofNode ( node, node.getEnvironment ( ), cond.getE2 ( ) );
		} else if ( node.getChildCount ( ) == 3 ) {

			MonoType type = supremum ( node.getChildAt ( 1 ).getType ( ), node
					.getChildAt ( 2 ).getType ( ) );
			context.setNodeType ( node, type );
		}
	}

	/**
	 * Applies the <b>(LET)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param node the minimal typing proof node.
	 */
	public void applyLet ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		Let let = ( Let ) node.getExpression ( );
		
		// check if the user entered a type
		if (let.getTau ( )== null)
			throw new RuntimeException("Please enter type for " +let.toString ( ));
		context.addProofNode ( node, node.getEnvironment ( ), let.getE1 ( ) );
	}

	public void updateLet ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		if ( node.getChildCount ( )==1 && node.getFirstChild ( ).isFinished ( ) ) {
			Let let = ( Let ) node.getExpression ( );
			TypeEnvironment environment = node.getEnvironment ( );
			context.addProofNode ( node, environment.extend ( let.getId ( ), let
					.getTau ( ) ), let.getE2 ( ) );
		}
		else if ( node.getChildCount ( )==2 && node.getChildAt ( 1 ).isFinished ( ) ) {
			MonoType type = node.getChildAt ( 1 ).getType ( );
			context.setNodeType ( node, type );
		}
	}

	/**
	 * Applies the <b>(SUBTYPE)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param node the minimal typing proof node.
	 */
	public void applySubtype ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingTypesProofNode node = ( MinimalTypingTypesProofNode ) pNode;
		MonoType type = node.getType ( );
		MonoType type2 = node.getType2 ( );
		subtypeInternal ( type, type2 );

	}

	public void subtypeInternal ( MonoType type, MonoType type2 ) {
		if ( type.equals ( type2 ) )
			return;
		else if ( type instanceof ArrowType && type2 instanceof ArrowType ) {
			ArrowType arrow = ( ArrowType ) type;
			ArrowType arrow2 = ( ArrowType ) type2;

			MonoType taul = arrow.getTau1 ( );
			MonoType taur = arrow.getTau2 ( );

			MonoType tau2l = arrow2.getTau1 ( );
			MonoType tau2r = arrow2.getTau2 ( );

			subtypeInternal ( taul, tau2l );
			subtypeInternal ( taur, tau2r );
		}
	}

	private MonoType supremum ( MonoType type, MonoType type2 ) {
		if ( type.equals ( type2 ) )
			return type;
		if ( type instanceof ArrowType && type2 instanceof ArrowType ) {
			ArrowType arrow = ( ArrowType ) type;
			ArrowType arrow2 = ( ArrowType ) type2;
			return new ArrowType (
					supremum ( arrow.getTau1 ( ), arrow2.getTau1 ( ) ), infimum ( arrow
							.getTau2 ( ), arrow2.getTau2 ( ) ) );
		}

		throw new RuntimeException ( "supremum type error" );
	}

	private MonoType infimum ( MonoType type, MonoType type2 ) {
		if ( type.equals ( type2 ) )
			return type;
		if ( type instanceof ArrowType && type2 instanceof ArrowType ) {
			ArrowType arrow = ( ArrowType ) type;
			ArrowType arrow2 = ( ArrowType ) type2;
			return new ArrowType ( infimum ( arrow.getTau1 ( ), arrow2.getTau1 ( ) ),
					supremum ( arrow.getTau2 ( ), arrow2.getTau2 ( ) ) );
		}

		throw new RuntimeException ( "infimum type error" );
	}

}
