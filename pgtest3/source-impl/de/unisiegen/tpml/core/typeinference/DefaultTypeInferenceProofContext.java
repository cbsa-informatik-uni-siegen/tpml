package de.unisiegen.tpml.core.typeinference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.TreeSet;

import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.expressions.ArithmeticOperator;
import de.unisiegen.tpml.core.expressions.Assign;
import de.unisiegen.tpml.core.expressions.BinaryCons;
import de.unisiegen.tpml.core.expressions.BooleanConstant;
import de.unisiegen.tpml.core.expressions.Deref;
import de.unisiegen.tpml.core.expressions.EmptyList;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Hd;
import de.unisiegen.tpml.core.expressions.IntegerConstant;
import de.unisiegen.tpml.core.expressions.IsEmpty;
import de.unisiegen.tpml.core.expressions.Not;
import de.unisiegen.tpml.core.expressions.Projection;
import de.unisiegen.tpml.core.expressions.Ref;
import de.unisiegen.tpml.core.expressions.RelationalOperator;
import de.unisiegen.tpml.core.expressions.Tl;
import de.unisiegen.tpml.core.expressions.UnaryCons;
import de.unisiegen.tpml.core.expressions.Unify;
import de.unisiegen.tpml.core.expressions.UnitConstant;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeUtilities;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.BooleanType;
import de.unisiegen.tpml.core.types.IntegerType;
import de.unisiegen.tpml.core.types.ListType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.PolyType;
import de.unisiegen.tpml.core.types.RefType;
import de.unisiegen.tpml.core.types.TupleType;
import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.core.types.TypeVariable;
import de.unisiegen.tpml.core.types.UnifyType;
import de.unisiegen.tpml.core.types.UnitType;

/**
 * 
 *
 * @author Benjamin Mies
 *
 */
public class DefaultTypeInferenceProofContext implements
		TypeCheckerProofContext {

	//
	// Attributes
	//

	/**
	 * The current offset for the <code>TypeVariable</code> allocation. The
	 * offset combined with the index from the {@link #model} will be used
	 * to generate a new type variable on every invocation of the method
	 * {@link #newTypeVariable()}. The offset will be incremented afterwards.
	 * 
	 * @see #newTypeVariable()
	 * @see TypeVariable
	 */
	private int offset = 0;

	/**
	 * The list of type equations that has been collected for this context
	 * 
	 * @see typeinference.TypeEquation
	 */
	private final ArrayList < TypeEquation > equations = new ArrayList < TypeEquation > ( );

	/**
	 * The list of all type substitutions that has been collected for this context
	 * 
	 */
	private ArrayList < DefaultTypeSubstitution > substitutions = new ArrayList < DefaultTypeSubstitution > ( );

	/**
	 * The newest added type substitutions
	 */
	private ArrayList < DefaultTypeSubstitution > substitution = new ArrayList < DefaultTypeSubstitution > ( );

	/**
	 * The type inference proof model with which this proof context is associated.
	 */
	TypeInferenceProofModel model;

	/**
	 * The actual used type inference proof node
	 */
	private DefaultTypeInferenceProofNode node;

	/**
	 * The list of redoable actions on the proof model.
	 * 
	 * @see #addRedoAction(Runnable)
	 * @see #getRedoActions()
	 */
	private final LinkedList < Runnable > redoActions = new LinkedList < Runnable > ( );

	/**
	 * The list of undoable actions on the proof model.
	 * 
	 * @see #addUndoAction(Runnable)
	 * @see #getUndoActions()
	 */
	private final LinkedList < Runnable > undoActions = new LinkedList < Runnable > ( );

	//
	// Constructor
	//

	/**
	 * 
	 *
	 * @param model the type inference proof model with which the context is associated.
	 * @param pNode the type inference proof node to apply a rule
	 * 
	 * @throws NullPointerException if <code>model</code> is <code>null</code>.
	 * 
	 * @see TypeInferenceProofModel#setIndex(int)
	 */
	public DefaultTypeInferenceProofContext (
			final TypeInferenceProofModel model,
			final DefaultTypeInferenceProofNode pNode ) {

		if ( model == null ) {
			throw new NullPointerException ( "model is null" ); //$NON-NLS-1$
		}
		this.model = model;
		this.node = pNode;
		//this.equations = pNode.getEquations();

		/*
		 // increment the model index
		 final int index = model.getIndex();
		 addRedoAction(new Runnable() { public void run() { model.setIndex(index + 1); } });
		 addUndoAction(new Runnable() { public void run() { model.setIndex(index); } });
		 modelIndex = model.getIndex();*/
	}

	//
	// Primitives
	//

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#addEquation(de.unisiegen.tpml.core.types.MonoType, de.unisiegen.tpml.core.types.MonoType)
	 */
	public void addEquation ( final MonoType left, final MonoType right ) {

		final TypeEquation eqn = new TypeEquation ( left, right );
		//equations = equations.extend(left, right);
		equations.add ( 0, eqn );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#addProofNode(de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode, de.unisiegen.tpml.core.typechecker.TypeEnvironment, de.unisiegen.tpml.core.expressions.Expression, de.unisiegen.tpml.core.types.MonoType)
	 */
	public void addProofNode ( final TypeCheckerProofNode pNode,
			final TypeEnvironment environment, final Expression expression,
			final MonoType type ) {

		final DefaultTypeCheckerProofNode child = new DefaultTypeCheckerProofNode (
				environment, expression, type );
		( ( DefaultTypeCheckerProofNode ) pNode ).add ( child );
	}

	/**
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#getTypeForExpression(de.unisiegen.tpml.core.expressions.Expression)
	 */
	public Type getTypeForExpression ( final Expression expression ) {

		if ( expression == null ) {
			throw new NullPointerException ( "expression is null" ); //$NON-NLS-1$
		}
		if ( expression instanceof BooleanConstant ) {
			return new BooleanType ( );
		} else if ( expression instanceof IntegerConstant ) {
			return new IntegerType ( );
		} else if ( expression instanceof UnitConstant ) {
			return new UnitType ( );
		} else if ( expression instanceof ArithmeticOperator ) {
			return new ArrowType ( new IntegerType ( ), new ArrowType (
					new IntegerType ( ), new IntegerType ( ) ) );
		} else if ( expression instanceof RelationalOperator ) {
			return new ArrowType ( new IntegerType ( ), new ArrowType (
					new IntegerType ( ), new BooleanType ( ) ) );
		} else if ( expression instanceof Not ) {
			return new ArrowType ( new BooleanType ( ), new BooleanType ( ) );
		} else if ( expression instanceof Assign ) {
			return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ),
					new ArrowType ( new RefType ( TypeVariable.ALPHA ), new ArrowType (
							TypeVariable.ALPHA, new UnitType ( ) ) ) );
		} else if ( expression instanceof Deref ) {
			return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ),
					new ArrowType ( new RefType ( TypeVariable.ALPHA ),
							TypeVariable.ALPHA ) );
		} else if ( expression instanceof Ref ) {
			return new PolyType (
					Collections.singleton ( TypeVariable.ALPHA ),
					new ArrowType ( TypeVariable.ALPHA, new RefType ( TypeVariable.ALPHA ) ) );
		} else if ( expression instanceof Projection ) {
			final Projection projection = ( Projection ) expression;
			final TypeVariable[] typeVariables = new TypeVariable[projection
					.getArity ( )];
			final TreeSet < TypeVariable > quantifiedVariables = new TreeSet < TypeVariable > ( );
			for ( int n = 0; n < typeVariables.length; ++n ) {
				typeVariables[n] = new TypeVariable ( n, 0 );
				quantifiedVariables.add ( typeVariables[n] );
			}
			return new PolyType ( quantifiedVariables, new ArrowType ( new TupleType (
					typeVariables ), typeVariables[projection.getIndex ( ) - 1] ) );
		} else if ( expression instanceof EmptyList ) {
			return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ),
					new ListType ( TypeVariable.ALPHA ) );
		} else if ( expression instanceof BinaryCons ) {
			return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ),
					new ArrowType ( TypeVariable.ALPHA, new ArrowType ( new ListType (
							TypeVariable.ALPHA ), new ListType ( TypeVariable.ALPHA ) ) ) );
		} else if ( expression instanceof UnaryCons ) {
			return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ),
					new ArrowType ( new TupleType ( new MonoType[] { TypeVariable.ALPHA,
							new ListType ( TypeVariable.ALPHA ) } ), new ListType (
							TypeVariable.ALPHA ) ) );
		} else if ( expression instanceof Hd ) {
			return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ),
					new ArrowType ( new ListType ( TypeVariable.ALPHA ),
							TypeVariable.ALPHA ) );
		} else if ( expression instanceof Tl ) {
			return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ),
					new ArrowType ( new ListType ( TypeVariable.ALPHA ), new ListType (
							TypeVariable.ALPHA ) ) );
		} else if ( expression instanceof IsEmpty ) {
			return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ),
					new ArrowType ( new ListType ( TypeVariable.ALPHA ),
							new BooleanType ( ) ) );
		} else {
			// not a simple expression
			throw new IllegalArgumentException ( "Cannot determine the type for " //$NON-NLS-1$
					+ expression );
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#instantiate(de.unisiegen.tpml.core.types.Type)
	 */
	public MonoType instantiate ( final Type type ) {

		if ( type == null ) {
			throw new NullPointerException ( "type is null" ); //$NON-NLS-1$
		}
		if ( type instanceof PolyType ) {
			final PolyType polyType = ( PolyType ) type;
			MonoType tau = polyType.getTau ( );
			for ( final TypeVariable tvar : polyType.getQuantifiedVariables ( ) ) {
				tau = tau.substitute ( TypeUtilities.newSubstitution ( tvar,
						newTypeVariable ( ) ) );
			}
			return tau;
		} else {
			return ( MonoType ) type;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#newTypeVariable()
	 */
	public TypeVariable newTypeVariable ( ) {
		//offset++;
		return new TypeVariable ( model.getIndex ( ), offset++ );

	}

	/**
	 * 
	 * Extend a new Substitution to the SubstitutionList
	 *
	 * @param s the new found substitution
	 */

	public void addSubstitution ( final DefaultTypeSubstitution s ) {
		substitution.add ( s );
	}

	//
	// Rule applicationxterm
	//
	/**
	 * Applies the specified proof <code>rule</code> to the actual <code>node</code>.
	 * 
	 * @param rule the proof rule to apply to the <code>node</code>.
	 * @param formula the TypeFormula to which to apply the <code>rule</code>.
	 * @param type the type the user guessed for the <code>node</code> or <code>null</code>
	 *             if the user didn't enter a type.
	 * 
	 * @throws ProofRuleException if the application of the <code>rule</code> to the
	 *                            <code>node</code> failed for some reason.
	 * @throws UnifyException if an error occurs while unifying the type equations that resulted
	 *                              from the application of <code>rule</code> to <code>node</code>.
	 */
	void apply ( final TypeCheckerProofRule rule, final TypeFormula formula,
			final MonoType type, boolean mode ) throws ProofRuleException,
			UnifyException {

		DefaultTypeCheckerProofNode typeNode = null;

		if ( formula.getExpression ( ) != null ) {
			typeNode = new DefaultTypeCheckerProofNode ( formula.getEnvironment ( ),
					formula.getExpression ( ), formula.getType ( ) );

		} else if ( rule.toString ( ).equals ( "UNIFY" ) ) { //$NON-NLS-1$

			typeNode = new DefaultTypeEquationProofNode ( formula.getEnvironment ( ),
					new Unify ( ), formula.getType ( ), ( TypeEquation ) formula, mode );

		} else {

			typeNode = new DefaultTypeCheckerProofNode ( formula.getEnvironment ( ),
					new Unify ( ), new UnifyType ( ) );
			throw new ProofRuleException ( typeNode, rule );

		}
		// try to apply the rule to the node
		rule.apply ( this, typeNode );

		// check if the user specified a type
		/**if (type != null) {
		 add an equation for { node.getType() = type }
		 addEquation(node.getType(), type);
		 }*/

		// Create a new List of type substitutions
		ArrayList < DefaultTypeSubstitution > newSubstitutions = new ArrayList < DefaultTypeSubstitution > ( );

		newSubstitutions.addAll ( substitution );

		newSubstitutions.addAll ( substitutions );

		// Create a new List of formulas and sort it
		ArrayList < TypeFormula > sortedFormulas = new ArrayList < TypeFormula > ( );
		//sortedFormulas.addAll ( node.getAllFormulas ( ) );

		for ( TypeFormula form : node.getAllFormulas ( ) ) {
			if ( form instanceof TypeJudgement )
				sortedFormulas.add ( form );
		}

		for ( TypeFormula form : node.getAllFormulas ( ) ) {
			if ( form instanceof TypeEquation )
				sortedFormulas.add ( form );
		}

		DefaultTypeCheckerProofNode child;
		for ( int i = 0; i < typeNode.getChildCount ( ); i++ ) {
			child = typeNode.getChildAt ( i );
			TypeJudgement insert = new TypeJudgement (
					( DefaultTypeEnvironment ) child.getEnvironment ( ), child
							.getExpression ( ), child.getType ( ) );
			for ( int j = 0; j < sortedFormulas.size ( ); j++ ) {
				if ( sortedFormulas.get ( j ) instanceof TypeEquation
						|| j == sortedFormulas.size ( ) - 1 ) {
					sortedFormulas.add ( j, insert );
					break;
				}
			}
		}
		for ( int j = 0; j < sortedFormulas.size ( ); j++ ) {
			if ( sortedFormulas.get ( j ) instanceof TypeEquation
					|| j == sortedFormulas.size ( ) - 1 ) {
				sortedFormulas.addAll ( j, equations );
				break;
			}
		}

		//	 Create a new List of formulas needed for new node
		ArrayList < TypeFormula > formulas = new ArrayList < TypeFormula > ( );
		for ( TypeFormula form : sortedFormulas ) {
			TypeFormula actual = form;
			actual = actual.substitute ( newSubstitutions );
			// don't add formula if it is already in list
			if ( !formulas.contains ( actual ) )
				formulas.add ( actual );
		}

		// create the new node
		this.model.contextAddProofNode ( this, node, formulas, newSubstitutions,
				rule, formula );
		if ( formulas.size ( ) < 1 ) {
			model.setFinished ( );
		}

	}

	/**
	 * Invokes all previously registered undo actions and clears the list of undo actions.
	 * 
	 * @see #addUndoAction(Runnable)
	 * @see #getUndoActions()
	 */
	void revert ( ) {

		// undo all already performed changes
		for ( final Runnable undoAction : this.undoActions ) {
			undoAction.run ( );
		}
		this.undoActions.clear ( );
	}

	//
	// Context action handling
	//

	/**
	 * Adds the specified <code>redoAction</code> to the internal list of redoable actions, and runs the
	 * <code>redoAction</code>.
	 * 
	 * This method should be called before adding the matching undo action via {@link #addUndoAction(Runnable)}.
	 * 
	 * @param redoAction the redoable action.
	 * 
	 * @see #addUndoAction(Runnable)
	 * @see #getRedoActions()
	 * 
	 * @throws NullPointerException if <code>redoAction</code> is <code>null</code>.
	 */
	void addRedoAction ( final Runnable redoAction ) {

		if ( redoAction == null ) {
			throw new NullPointerException ( "undoAction is null" ); //$NON-NLS-1$
		}

		// perform the action
		redoAction.run ( );

		// record the action
		this.redoActions.add ( redoAction );
	}

	/**
	 * Adds the specified <code>undoAction</code> to the internal list of undoable actions, and runs the
	 * <code>undoActions</code>.
	 * 
	 * This method should be called after adding the matching redo action via {@link #addRedoAction(Runnable)}.
	 * 
	 * @param undoAction the undoable action.
	 * 
	 * @see #addRedoAction(Runnable)
	 * @see #getUndoActions()
	 * 
	 * @throws NullPointerException if <code>undoAction</code> is <code>null</code>.
	 */
	void addUndoAction ( final Runnable undoAction ) {

		if ( undoAction == null ) {
			throw new NullPointerException ( "undoAction is null" ); //$NON-NLS-1$
		}

		// record the action
		this.undoActions.add ( 0, undoAction );
	}

	/**
	 * Returns a <code>Runnable</code>, which performs all the previously
	 * recorded redoable actions, added via {@link #addRedoAction(Runnable)}.
	 * 
	 * @return a <code>Runnable</code> for all recorded redoable actions.
	 * 
	 * @see #addRedoAction(Runnable)
	 * @see #getUndoActions()
	 */
	Runnable getRedoActions ( ) {

		return new Runnable ( ) {

			@SuppressWarnings ( "synthetic-access" )
			public void run ( ) {

				for ( final Runnable redoAction : DefaultTypeInferenceProofContext.this.redoActions ) {
					redoAction.run ( );
				}
			}
		};
	}

	/**
	 * Returns a <code>Runnable</code>, which performs all the previously
	 * recorded undoable actions, added via {@link #addUndoAction(Runnable)}.
	 * 
	 * @return a <code>Runnable</code> for all recorded undoable actions.
	 * 
	 * @see #addUndoAction(Runnable)
	 * @see #getRedoActions()
	 */
	Runnable getUndoActions ( ) {

		return new Runnable ( ) {

			@SuppressWarnings ( "synthetic-access" )
			public void run ( ) {

				for ( final Runnable undoAction : DefaultTypeInferenceProofContext.this.undoActions ) {
					undoAction.run ( );
				}
			}
		};
	}

	//
	// Accessors
	//

	/**
	 * 
	 * Set the TypeSubstitutions for this context
	 *
	 * @param subs the new list of type substitutions
	 */
	public void setSubstitutions (
			final ArrayList < DefaultTypeSubstitution > subs ) {
		this.substitutions = subs;

	}

	/**
	 * get a list of all substitutions
	 *
	 * @return ArrayList<DefaultTypeSubstitutions substitutions
	 */
	public ArrayList < DefaultTypeSubstitution > getSubstitution ( ) {
		return this.substitution;
	}

	/*
	 * Set the newes type substitution
	 * @param substitution DefaultTypeSubstitution
	 *
	 public void setSubstitution(TypeSubstitutionList s) {

	 this.substitution = s;
	 }*/
}
