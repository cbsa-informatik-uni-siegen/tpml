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
import de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerExpressionProofNode;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerTypeProofNode;
import de.unisiegen.tpml.core.typechecker.SeenTypes;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
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
 * Default implementation of the <code>TypeInferenceProofContext</code>
 * interface.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 * @see TypeCheckerProofContext
 */
public class DefaultTypeInferenceProofContext implements
    TypeInferenceProofContext
{

  //
  // Attributes
  //
  /**
   * The current offset for the <code>TypeVariable</code> allocation. The
   * offset combined with the index from the {@link #model} will be used to
   * generate a new type variable on every invocation of the method
   * {@link #newTypeVariable()}. The offset will be incremented afterwards.
   * 
   * @see #newTypeVariable()
   * @see TypeVariable
   */
  private int offset = 0;


  /**
   * The list of type equations that has been collected for this context
   * 
   * @see TypeEquationTypeInference
   */
  private final ArrayList < TypeEquationTypeInference > equationList = new ArrayList < TypeEquationTypeInference > ();


  /**
   * The list of all type substitutions that has been collected
   * 
   * @see TypeSubstitution
   */
  private ArrayList < TypeSubstitution > substitutionList = new ArrayList < TypeSubstitution > ();


  /**
   * The list of all type judgements that has been collected
   * 
   * @see TypeJudgement
   */
  private ArrayList < TypeJudgement > judgementList = new ArrayList < TypeJudgement > ();


  /**
   * The list of all type subtypes that has been collected
   * 
   * @see TypeSubType
   */
  private ArrayList < TypeSubType > subTypeList = new ArrayList < TypeSubType > ();


  /**
   * The type inference proof model with which this proof context is associated.
   */
  TypeInferenceProofModel model;


  /**
   * The list of redoable actions on the proof model.
   * 
   * @see #addRedoAction(Runnable)
   * @see #getRedoActions()
   */
  private final LinkedList < Runnable > redoActions = new LinkedList < Runnable > ();


  /**
   * The list of undoable actions on the proof model.
   * 
   * @see #addUndoAction(Runnable)
   * @see #getUndoActions()
   */
  private final LinkedList < Runnable > undoActions = new LinkedList < Runnable > ();


  //
  // Constructor
  //
  /**
   * Allocates a new <code>NewDefaultTypeInferenceProofContext</code> with the
   * specified <code>proof model</code> and <code>proof node</code>.
   * 
   * @param pModel the type inference proof model with which the context is
   *          associated.
   * @param node the type inference proof node for this context
   * @throws NullPointerException if <code>model</code> is <code>null</code>.
   * @see TypeInferenceProofModel#setIndex(int)
   */
  public DefaultTypeInferenceProofContext (
      final TypeInferenceProofModel pModel, DefaultTypeInferenceProofNode node )
  {
    if ( pModel == null )
    {
      throw new NullPointerException ( "model is null" ); //$NON-NLS-1$
    }
    this.model = pModel;

    // collect all type formulas of the parent node
    getOldFormulas ( node );
  }


  /**
   * Add a new collected type equation to the list of type equations
   * 
   * @param left the left MonoType of this type equation
   * @param right the right MonoType of this type equation
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#addEquation(de.unisiegen.tpml.core.types.MonoType,
   *      de.unisiegen.tpml.core.types.MonoType)
   */
  public void addEquation ( MonoType left, MonoType right )
  {
    addEquation ( new TypeEquationTypeInference ( left, right,
        new SeenTypes < TypeEquationTypeInference > () ) );
  }


  /**
   * Add a new collected type equation to the list of type equations
   * 
   * @param pTypeEquationTypeInference the new collected
   *          TypeEquationTypeInference
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#addEquation(de.unisiegen.tpml.core.types.MonoType,
   *      de.unisiegen.tpml.core.types.MonoType)
   */
  public void addEquation ( TypeEquationTypeInference pTypeEquationTypeInference )
  {
    this.equationList.remove ( pTypeEquationTypeInference );
    this.equationList.add ( 0, pTypeEquationTypeInference );

  }


  /**
   * Extend a new Substitution to the SubstitutionList
   * 
   * @param s the new found substitution
   */
  public void addSubstitution ( TypeSubstitution s )
  {
    this.substitutionList.remove ( s );
    this.substitutionList.add ( 0, s );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#addProofNode(de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode,
   *      de.unisiegen.tpml.core.typechecker.TypeEnvironment,
   *      de.unisiegen.tpml.core.expressions.Expression,
   *      de.unisiegen.tpml.core.types.MonoType)
   */
  public void addProofNode ( @SuppressWarnings ( "unused" )
  TypeCheckerProofNode node, TypeEnvironment environment,
      Expression expression, MonoType type )
  {
    this.judgementList
        .add ( new TypeJudgement ( environment, expression, type ) );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#addProofNode(
   *      de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode,
   *      de.unisiegen.tpml.core.types.MonoType,
   *      de.unisiegen.tpml.core.types.MonoType)
   */
  public void addProofNode ( @SuppressWarnings ( "unused" )
  TypeCheckerProofNode node, MonoType left, MonoType right )
  {
    this.subTypeList.add ( new TypeSubType ( left, right ) );
  }


  //
  // Accessors
  //
  /**
   * Set the TypeSubstitutions for this context
   * 
   * @return this list of type substitutions
   */
  public ArrayList < TypeSubstitution > getSubstitution ()
  {
    return this.substitutionList;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#getTypeForExpression(de.unisiegen.tpml.core.expressions.Expression)
   */
  public Type getTypeForExpression ( final Expression expression )
  {
    if ( expression == null )
    {
      throw new NullPointerException ( "expression is null" ); //$NON-NLS-1$
    }
    if ( expression instanceof BooleanConstant )
    {
      return new BooleanType ();
    }
    else if ( expression instanceof IntegerConstant )
    {
      return new IntegerType ();
    }
    else if ( expression instanceof UnitConstant )
    {
      return new UnitType ();
    }
    else if ( expression instanceof ArithmeticOperator )
    {
      return new ArrowType ( new IntegerType (), new ArrowType (
          new IntegerType (), new IntegerType () ) );
    }
    else if ( expression instanceof RelationalOperator )
    {
      return new ArrowType ( new IntegerType (), new ArrowType (
          new IntegerType (), new BooleanType () ) );
    }
    else if ( expression instanceof Not )
    {
      return new ArrowType ( new BooleanType (), new BooleanType () );
    }
    else if ( expression instanceof Assign )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ),
          new ArrowType ( new RefType ( TypeVariable.ALPHA ), new ArrowType (
              TypeVariable.ALPHA, new UnitType () ) ) );
    }
    else if ( expression instanceof Deref )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ),
          new ArrowType ( new RefType ( TypeVariable.ALPHA ),
              TypeVariable.ALPHA ) );
    }
    else if ( expression instanceof Ref )
    {
      return new PolyType (
          Collections.singleton ( TypeVariable.ALPHA ),
          new ArrowType ( TypeVariable.ALPHA, new RefType ( TypeVariable.ALPHA ) ) );
    }
    else if ( expression instanceof Projection )
    {
      final Projection projection = ( Projection ) expression;
      final TypeVariable [] typeVariables = new TypeVariable [ projection
          .getArity () ];
      final TreeSet < TypeVariable > quantifiedVariables = new TreeSet < TypeVariable > ();
      for ( int n = 0 ; n < typeVariables.length ; ++n )
      {
        typeVariables [ n ] = new TypeVariable ( n, 0 );
        quantifiedVariables.add ( typeVariables [ n ] );
      }
      return new PolyType ( quantifiedVariables, new ArrowType ( new TupleType (
          typeVariables ), typeVariables [ projection.getIndex () - 1 ] ) );
    }
    else if ( expression instanceof EmptyList )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ),
          new ListType ( TypeVariable.ALPHA ) );
    }
    else if ( expression instanceof BinaryCons )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ),
          new ArrowType ( TypeVariable.ALPHA, new ArrowType ( new ListType (
              TypeVariable.ALPHA ), new ListType ( TypeVariable.ALPHA ) ) ) );
    }
    else if ( expression instanceof UnaryCons )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ),
          new ArrowType ( new TupleType ( new MonoType []
          { TypeVariable.ALPHA, new ListType ( TypeVariable.ALPHA ) } ),
              new ListType ( TypeVariable.ALPHA ) ) );
    }
    else if ( expression instanceof Hd )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ),
          new ArrowType ( new ListType ( TypeVariable.ALPHA ),
              TypeVariable.ALPHA ) );
    }
    else if ( expression instanceof Tl )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ),
          new ArrowType ( new ListType ( TypeVariable.ALPHA ), new ListType (
              TypeVariable.ALPHA ) ) );
    }
    else if ( expression instanceof IsEmpty )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ),
          new ArrowType ( new ListType ( TypeVariable.ALPHA ),
              new BooleanType () ) );
    }
    else
    {
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
  public MonoType instantiate ( final Type type )
  {
    if ( type == null )
    {
      throw new NullPointerException ( "type is null" ); //$NON-NLS-1$
    }
    if ( type instanceof PolyType )
    {
      final PolyType polyType = ( PolyType ) type;
      MonoType tau = polyType.getTau ();
      for ( final TypeVariable tvar : polyType.getQuantifiedVariables () )
      {
        tau = tau.substitute ( TypeUtilities.newSubstitution ( tvar,
            newTypeVariable () ) );
      }
      return tau;
    }
    return ( MonoType ) type;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#newTypeVariable()
   */
  public TypeVariable newTypeVariable ()
  {
    // offset++;
    return new TypeVariable ( this.model.getIndex (), this.offset++ );
  }


  //
  // Context action handling
  //

  /**
   * Appends the <code>redoAction</code> to the list of redoable actions and
   * runs the <code>redoAction</code>.
   * 
   * @param redoAction the redo action to add.
   * @throws NullPointerException if <code>redoAction</code> is
   *           <code>null</code>.
   * @see #addUndoAction(Runnable)
   * @see #getRedoActions()
   */
  void addRedoAction ( Runnable redoAction )
  {
    if ( redoAction == null )
    {
      throw new NullPointerException ( "redoAction is null" ); //$NON-NLS-1$
    }

    // perform the action
    redoAction.run ();

    // record the action
    this.redoActions.add ( redoAction );
  }


  /**
   * Prepends the <code>undoAction</code> to the list of undoable actions.
   * 
   * @param undoAction the undo action to add.
   * @throws NullPointerException if <code>undoAction</code> is
   *           <code>null</code>.
   * @see #addRedoAction(Runnable)
   * @see #getUndoActions()
   */
  void addUndoAction ( Runnable undoAction )
  {
    if ( undoAction == null )
    {
      throw new NullPointerException ( "undoAction is null" ); //$NON-NLS-1$
    }

    // record the action
    this.undoActions.add ( 0, undoAction );
  }


  /**
   * Returns a single <code>Runnable</code> that runs all previously
   * registered redo actions.
   * 
   * @return a single <code>Runnable</code> to run all redo actions.
   * @see #addRedoAction(Runnable)
   * @see #getUndoActions()
   */
  Runnable getRedoActions ()
  {
    return new Runnable ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void run ()
      {
        for ( Runnable redoAction : DefaultTypeInferenceProofContext.this.redoActions )
        {
          redoAction.run ();
        }
      }
    };
  }


  /**
   * Returns a single <code>Runnable</code> that runs all previously
   * registered undo actions.
   * 
   * @return a single <code>Runnable</code> to run all undo actions.
   * @see #addUndoAction(Runnable)
   * @see #getRedoActions()
   */
  Runnable getUndoActions ()
  {
    return new Runnable ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void run ()
      {
        for ( Runnable undoAction : DefaultTypeInferenceProofContext.this.undoActions )
        {
          undoAction.run ();
        }
      }
    };
  }


  /**
   * Invokes all previously registered undo actions and clears the list of undo
   * actions.
   * 
   * @see #addUndoAction(Runnable)
   * @see #getUndoActions()
   */
  void revert ()
  {
    // undo all already performed changes
    for ( final Runnable undoAction : this.undoActions )
    {
      undoAction.run ();
    }
    this.undoActions.clear ();
  }


  //
  // Rule applicationxterm
  //
  /**
   * Applies the specified proof <code>rule</code> to the actual
   * <code>node</code>.
   * 
   * @param rule the proof rule to apply to the <code>node</code>.
   * @param formula the TypeFormula to which to apply the <code>rule</code>.
   * @param type the type the user guessed for the <code>node</code> or
   *          <code>null</code> if the user didn't enter a type.
   * @param mode The choosen mode.
   * @param node The {@link DefaultTypeInferenceProofNode}.
   * @throws ProofRuleException if the application of the <code>rule</code> to
   *           the <code>node</code> failed for some reason.
   */
  void apply ( final TypeCheckerProofRule rule, final TypeFormula formula,
      @SuppressWarnings ( "unused" )
      final MonoType type, boolean mode, DefaultTypeInferenceProofNode node )
      throws ProofRuleException
  {

    // collect all type substitutions of the parent node
    for ( TypeSubstitution sub : node.getSubstitution () )
    {
      addSubstitution ( sub );
    }

    // create a typechecker proof node with the actual formula
    AbstractTypeCheckerProofNode typeNode = null;

    if ( formula instanceof TypeJudgement )
    {
      typeNode = new DefaultTypeCheckerExpressionProofNode ( formula
          .getEnvironment (), formula.getExpression (), formula.getType () );
    }
    else if ( formula instanceof TypeSubType )
    {
      TypeSubType subType = ( TypeSubType ) formula;
      typeNode = new DefaultTypeCheckerTypeProofNode ( subType.getType (),
          subType.getType2 () );
    }
    else if ( formula instanceof TypeEquationTypeInference )
    {
      typeNode = new DefaultTypeEquationProofNode ( formula.getEnvironment (),
          new Unify (), new UnifyType (),
          ( TypeEquationTypeInference ) formula, mode );
    }
    else
    {
      typeNode = new DefaultTypeCheckerExpressionProofNode ( formula
          .getEnvironment (), new Unify (), new UnifyType () );
      throw new ProofRuleException ( typeNode, rule );
    }

    // try to apply the rule to the node
    rule.apply ( this, typeNode );
    // check if the user specified a type

    // if (type != null) { //add an equation for { node.getType() = type }
    // addEquation(node.getType(), type); }

    // apply the substitution to all type formulas
    ArrayList < TypeFormula > newFormulas = getNewFormulas ( formula );

    if ( newFormulas.size () == 0 )
    {
      DefaultTypeInferenceProofNode root = node.getRoot ();

      TypeSubstitution first = null;
      MonoType rootType = root.getFirstFormula ().getType ();
      ArrayList < TypeSubstitution > subs = this.substitutionList;
      for ( TypeSubstitution s : subs )
      {
        if ( s.getTvar ().equals ( rootType ) )
          first = s;
      }
      int index = subs.indexOf ( first );
      if ( index > 0 )
      {
        subs.remove ( index );
        subs.add ( 0, first );
      }
    }

    // create the new node
    this.model.contextAddProofNode ( newFormulas, this.substitutionList );
  }


  /**
   * Collect the formulas of the parent node, and sort them to the corresponding
   * list
   * 
   * @param node the parent DefaultTypeInferenceProofNode
   */
  private void getOldFormulas ( DefaultTypeInferenceProofNode node )
  {
    for ( TypeFormula formula : node.getAllFormulas () )
    {
      if ( formula instanceof TypeJudgement )
      {
        this.judgementList.add ( ( TypeJudgement ) formula );
      }
      else if ( formula instanceof TypeEquationTypeInference )
      {
        this.equationList.add ( ( TypeEquationTypeInference ) formula );
      }
      else
      {
        this.subTypeList.add ( ( TypeSubType ) formula );
      }
    }
  }


  /**
   * Build the new type formula list, handle all collected substitutions, and
   * remove the actual formula from list.
   * 
   * @param actual The actual handled type formula
   * @return the new list of type formulas for the child node
   */
  private ArrayList < TypeFormula > getNewFormulas ( TypeFormula actual )
  {
    ArrayList < TypeFormula > formulas = new ArrayList < TypeFormula > ();

    for ( TypeJudgement judgement : this.judgementList )
    {
      formulas.add ( judgement.substitute ( this.substitutionList ) );
    }
    for ( TypeSubType subType : this.subTypeList )
    {
      formulas.add ( subType.substitute ( this.substitutionList ) );
    }
    for ( TypeEquationTypeInference teqn : this.equationList )
    {
      if ( !formulas.contains ( teqn.substitute ( this.substitutionList ) ) )
        formulas.add ( teqn.substitute ( this.substitutionList ) );
    }

    // remove the actual handled type formula
    formulas.remove ( actual.substitute ( this.substitutionList ) );

    return formulas;
  }
}
