package de.unisiegen.tpml.core.minimaltyping ;


import java.util.Collections ;
import java.util.LinkedList ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.ProofRuleException ;
import de.unisiegen.tpml.core.expressions.ArithmeticOperator ;
import de.unisiegen.tpml.core.expressions.Assign ;
import de.unisiegen.tpml.core.expressions.BinaryCons ;
import de.unisiegen.tpml.core.expressions.BooleanConstant ;
import de.unisiegen.tpml.core.expressions.Deref ;
import de.unisiegen.tpml.core.expressions.EmptyList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Hd ;
import de.unisiegen.tpml.core.expressions.IntegerConstant ;
import de.unisiegen.tpml.core.expressions.IsEmpty ;
import de.unisiegen.tpml.core.expressions.Not ;
import de.unisiegen.tpml.core.expressions.Projection ;
import de.unisiegen.tpml.core.expressions.Ref ;
import de.unisiegen.tpml.core.expressions.RelationalOperator ;
import de.unisiegen.tpml.core.expressions.Tl ;
import de.unisiegen.tpml.core.expressions.UnaryCons ;
import de.unisiegen.tpml.core.expressions.UnitConstant ;
import de.unisiegen.tpml.core.types.ArrowType ;
import de.unisiegen.tpml.core.types.BooleanType ;
import de.unisiegen.tpml.core.types.IntegerType ;
import de.unisiegen.tpml.core.types.ListType ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.PolyType ;
import de.unisiegen.tpml.core.types.RefType ;
import de.unisiegen.tpml.core.types.TupleType ;
import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.core.types.TypeVariable ;
import de.unisiegen.tpml.core.types.UnitType ;


/**
 * Default implementation of the <code>TypeCheckerProofContext</code>
 * interface.
 * 
 * @author Benedikt Meurer
 * @version $Rev:511M $
 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext
 */
public class DefaultMinimalTypingProofContext implements MinimalTypingProofContext
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
  private int offset = 0 ;


  /**
   * The type checker proof model with which this proof context is associated.
   * 
   * @see #DefaultTypeCheckerProofContext(TypeCheckerProofModel)
   */
  private MinimalTypingProofModel model ;


  /**
   * The list of redoable actions on the proof model.
   * 
   * @see #addRedoAction(Runnable)
   * @see #getRedoActions()
   */
  private LinkedList < Runnable > redoActions = new LinkedList < Runnable > ( ) ;


  /**
   * The list of undoable actions on the proof model.
   * 
   * @see #addUndoAction(Runnable)
   * @see #getUndoActions()
   */
  private LinkedList < Runnable > undoActions = new LinkedList < Runnable > ( ) ;


  //
  // Constructor (package)
  //
  /**
   * Allocates a new <code>DefaultMinimalTypingProofContext</code> with the
   * specified <code>model</code>. This constructor automatically increments
   * the index of the <code>model</code> using a redo/undo pair, via the
   * {@link MinimalTypingProofModel#setIndex(int)} method.
   * 
   * @param model the minimal typing proof model with which the context is
   *          associated.
   * @throws NullPointerException if <code>model</code> is <code>null</code>.
   * @see MinimalTypingProofModel#setIndex(int)
   */
  public DefaultMinimalTypingProofContext ( final MinimalTypingProofModel model )
  {
    if ( model == null )
    {
      throw new NullPointerException ( "model is null" ) ;
    }
    this.model = model ;
    // increment the model index
    final int index = model.getIndex ( ) ;
    addRedoAction ( new Runnable ( )
    {
      public void run ( )
      {
        model.setIndex ( index + 1 ) ;
      }
    } ) ;
    addUndoAction ( new Runnable ( )
    {
      public void run ( )
      {
        model.setIndex ( index ) ;
      }
    } ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingrProofContext#addProofNode(de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode,
   *      de.unisiegen.tpml.core.minimaltyping.TypeEnvironment,
   *      de.unisiegen.tpml.core.expressions.Expression,
   *      de.unisiegen.tpml.core.types.MonoType)
   */
  public void addProofNode ( MinimalTypingProofNode node ,
      TypeEnvironment environment , Expression expression )
  {
    this.model.contextAddProofNode ( this ,
        ( AbstractMinimalTypingProofNode ) node , environment , expression  ) ;
  }
  
	public void addProofNode ( MinimalTypingProofNode node, MonoType type, MonoType type2 ) {
		this.model.contextAddProofNode ( this, (AbstractMinimalTypingProofNode) node, type, type2 );
		
	}
	
	public void setNodeType(MinimalTypingProofNode node, MonoType type){
		this.model.contextSetProofNodeType ( this, (AbstractMinimalTypingProofNode) node, type );
	}


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofContext#getTypeForExpression(de.unisiegen.tpml.core.expressions.Expression)
   */
  public Type getTypeForExpression ( Expression expression )
  {
    if ( expression == null )
    {
      throw new NullPointerException ( "expression is null" ) ;
    }
    if ( expression instanceof BooleanConstant )
    {
      return new BooleanType ( ) ;
    }
    else if ( expression instanceof IntegerConstant )
    {
      return new IntegerType ( ) ;
    }
    else if ( expression instanceof UnitConstant )
    {
      return new UnitType ( ) ;
    }
    else if ( expression instanceof ArithmeticOperator )
    {
      return new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
    }
    else if ( expression instanceof RelationalOperator )
    {
      return new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new BooleanType ( ) ) ) ;
    }
    else if ( expression instanceof Not )
    {
      return new ArrowType ( new BooleanType ( ) , new BooleanType ( ) ) ;
    }
    else if ( expression instanceof Assign )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ) ,
          new ArrowType ( new RefType ( TypeVariable.ALPHA ) , new ArrowType (
              TypeVariable.ALPHA , new UnitType ( ) ) ) ) ;
    }
    else if ( expression instanceof Deref )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ) ,
          new ArrowType ( new RefType ( TypeVariable.ALPHA ) ,
              TypeVariable.ALPHA ) ) ;
    }
    else if ( expression instanceof Ref )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ) ,
          new ArrowType ( TypeVariable.ALPHA ,
              new RefType ( TypeVariable.ALPHA ) ) ) ;
    }
    else if ( expression instanceof Projection )
    {
      Projection projection = ( Projection ) expression ;
      TypeVariable [ ] typeVariables = new TypeVariable [ projection
          .getArity ( ) ] ;
      TreeSet < TypeVariable > quantifiedVariables = new TreeSet < TypeVariable > ( ) ;
      for ( int n = 0 ; n < typeVariables.length ; ++ n )
      {
        typeVariables [ n ] = new TypeVariable ( n , 0 ) ;
        quantifiedVariables.add ( typeVariables [ n ] ) ;
      }
      return new PolyType ( quantifiedVariables , new ArrowType (
          new TupleType ( typeVariables ) , typeVariables [ projection
              .getIndex ( ) - 1 ] ) ) ;
    }
    else if ( expression instanceof EmptyList )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ) ,
          new ListType ( TypeVariable.ALPHA ) ) ;
    }
    else if ( expression instanceof BinaryCons )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ) ,
          new ArrowType ( TypeVariable.ALPHA , new ArrowType ( new ListType (
              TypeVariable.ALPHA ) , new ListType ( TypeVariable.ALPHA ) ) ) ) ;
    }
    else if ( expression instanceof UnaryCons )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ) ,
          new ArrowType ( new TupleType ( new MonoType [ ]
          { TypeVariable.ALPHA , new ListType ( TypeVariable.ALPHA ) } ) ,
              new ListType ( TypeVariable.ALPHA ) ) ) ;
    }
    else if ( expression instanceof Hd )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ) ,
          new ArrowType ( new ListType ( TypeVariable.ALPHA ) ,
              TypeVariable.ALPHA ) ) ;
    }
    else if ( expression instanceof Tl )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ) ,
          new ArrowType ( new ListType ( TypeVariable.ALPHA ) , new ListType (
              TypeVariable.ALPHA ) ) ) ;
    }
    else if ( expression instanceof IsEmpty )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ) ,
          new ArrowType ( new ListType ( TypeVariable.ALPHA ) ,
              new BooleanType ( ) ) ) ;
    }
    else
    {
      // not a simple expression
      throw new IllegalArgumentException ( "Cannot determine the type for "
          + expression ) ;
    }
  }


  //
  // Rule application
  //
  /**
   * Applies the specified proof <code>rule</code> to the given
   * <code>node</code>.
   * 
   * @param rule the proof rule to apply to the <code>node</code>.
   * @param node the proof node to which to apply the <code>rule</code>.
   * @param type the type the user guessed for the <code>node</code> or
   *          <code>null</code> if the user didn't enter a type.
   * @throws NullPointerException if <code>rule</code> or <code>node</code>
   *           is <code>null</code>.
   * @throws ProofRuleException if the application of the <code>rule</code> to
   *           the <code>node</code> failed for some reason.
   */
  void apply ( MinimalTypingProofRule rule , MinimalTypingProofNode node ,
      MonoType type ) throws ProofRuleException
  {
    // record the proof step for the node
    this.model.contextSetProofNodeRule ( this ,
        ( AbstractMinimalTypingProofNode ) node , rule ) ;
    // try to apply the rule to the node
    rule.apply ( this , node ) ;
    // check if the user specified a type
    if ( type != null )
    {
      // add an equation for { node.getType() = type }
     // addEquation ( node.getType ( ) , type ) ;
    }
       // update all super nodes
    for ( ; ; )
    {
      // determine the parent node
    	MinimalTypingProofNode parentNode = node.getParent ( ) ;
      if ( parentNode == null )
      {
        break ;
      }
      // update the parent node (using the previously applied rule)
      parentNode.getRule ( ).update ( this , parentNode ) ;
           // continue with the next one
      node = parentNode ;
    }
  }


  /**
   * Invokes all previously registered undo actions and clears the list of undo
   * actions.
   * 
   * @see #addUndoAction(Runnable)
   * @see #getUndoActions()
   */
  void revert ( )
  {
    // undo all already performed changes
    for ( Runnable undoAction : this.undoActions )
    {
      undoAction.run ( ) ;
    }
    this.undoActions.clear ( ) ;
  }


  //
  // Context action handling
  //
  /**
   * Adds the specified <code>redoAction</code> to the internal list of
   * redoable actions, and runs the <code>redoAction</code>. This method
   * should be called before adding the matching undo action via
   * {@link #addUndoAction(Runnable)}.
   * 
   * @param redoAction the redoable action.
   * @see #addUndoAction(Runnable)
   * @see #getRedoActions()
   * @throws NullPointerException if <code>redoAction</code> is
   *           <code>null</code>.
   */
  public void addRedoAction ( Runnable redoAction )
  {
    if ( redoAction == null )
    {
      throw new NullPointerException ( "undoAction is null" ) ;
    }
    // perform the action
    redoAction.run ( ) ;
    // record the action
    this.redoActions.add ( redoAction ) ;
  }


  /**
   * Adds the specified <code>undoAction</code> to the internal list of
   * undoable actions, and runs the <code>undoActions</code>. This method
   * should be called after adding the matching redo action via
   * {@link #addRedoAction(Runnable)}.
   * 
   * @param undoAction the undoable action.
   * @see #addRedoAction(Runnable)
   * @see #getUndoActions()
   * @throws NullPointerException if <code>undoAction</code> is
   *           <code>null</code>.
   */
  void addUndoAction ( Runnable undoAction )
  {
    if ( undoAction == null )
    {
      throw new NullPointerException ( "undoAction is null" ) ;
    }
    // record the action
    this.undoActions.add ( 0 , undoAction ) ;
  }


  /**
   * Returns a <code>Runnable</code>, which performs all the previously
   * recorded redoable actions, added via {@link #addRedoAction(Runnable)}.
   * 
   * @return a <code>Runnable</code> for all recorded redoable actions.
   * @see #addRedoAction(Runnable)
   * @see #getUndoActions()
   */
  Runnable getRedoActions ( )
  {
    return new Runnable ( )
    {
      public void run ( )
      {
        for ( Runnable redoAction : DefaultMinimalTypingProofContext.this.redoActions )
        {
          redoAction.run ( ) ;
        }
      }
    } ;
  }


  /**
   * Returns a <code>Runnable</code>, which performs all the previously
   * recorded undoable actions, added via {@link #addUndoAction(Runnable)}.
   * 
   * @return a <code>Runnable</code> for all recorded undoable actions.
   * @see #addUndoAction(Runnable)
   * @see #getRedoActions()
   */
  Runnable getUndoActions ( )
  {
    return new Runnable ( )
    {
      public void run ( )
      {
        for ( Runnable undoAction : DefaultMinimalTypingProofContext.this.undoActions )
        {
          undoAction.run ( ) ;
        }
      }
    } ;
  }

}
