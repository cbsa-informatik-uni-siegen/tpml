package de.unisiegen.tpml.core.languages.l1;


import java.text.MessageFormat;

import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.expressions.And;
import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Coercion;
import de.unisiegen.tpml.core.expressions.Condition;
import de.unisiegen.tpml.core.expressions.Constant;
import de.unisiegen.tpml.core.expressions.CurriedLet;
import de.unisiegen.tpml.core.expressions.CurriedLetRec;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.InfixOperation;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.expressions.LetRec;
import de.unisiegen.tpml.core.expressions.MultiLambda;
import de.unisiegen.tpml.core.expressions.MultiLet;
import de.unisiegen.tpml.core.expressions.Or;
import de.unisiegen.tpml.core.expressions.Recursion;
import de.unisiegen.tpml.core.languages.l2.L2Language;
import de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRuleSet;
import de.unisiegen.tpml.core.typechecker.TypeCheckerExpressionProofNode;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeCheckerTypeProofNode;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.BooleanType;
import de.unisiegen.tpml.core.types.ListType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.ObjectType;
import de.unisiegen.tpml.core.types.RecType;
import de.unisiegen.tpml.core.types.RefType;
import de.unisiegen.tpml.core.types.RowType;
import de.unisiegen.tpml.core.types.TupleType;
import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.core.types.TypeVariable;


/**
 * The type proof rules for the <code>L1</code> language.
 * 
 * @author Benedikt Meurer
 * @author Benjamin Mies
 * @version $Id: L1TypeCheckerProofRuleSet.java 2796 2008-03-14 19:13:11Z fehler $
 * @see AbstractTypeCheckerProofRuleSet
 */
public class L1TypeCheckerProofRuleSet extends AbstractTypeCheckerProofRuleSet
{

  /**
   * Allocates a new <code>L1TypeCheckerProofRuleSet</code> for the specified
   * <code>language</code>.
   * 
   * @param language the <code>L1</code> or a derived language.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   */
  public L1TypeCheckerProofRuleSet ( L1Language language )
  {
    super ( language );
    // register the type rules
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

  }


  /**
   * Applies the <b>(AND)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param pNode the type checker proof node.
   */
  public void applyAnd ( TypeCheckerProofContext context,
      TypeCheckerProofNode pNode )
  {
    TypeCheckerExpressionProofNode node = ( TypeCheckerExpressionProofNode ) pNode;
    And and = ( And ) node.getExpression ();
    // generate new child nodes
    context.addProofNode ( node, node.getEnvironment (), and.getE1 (),
        new BooleanType () );
    context.addProofNode ( node, node.getEnvironment (), and.getE2 (),
        new BooleanType () );
    // add the {tau = bool} equation
    context.addEquation ( node.getType (), new BooleanType () );
  }


  /**
   * Applies the <b>(ABSTR)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param pNode the type checker proof node.
   */
  public void applyAbstr ( TypeCheckerProofContext context,
      TypeCheckerProofNode pNode )
  {
    TypeCheckerExpressionProofNode node = ( TypeCheckerExpressionProofNode ) pNode;
    // determine the type environment
    TypeEnvironment environment = node.getEnvironment ();
    // can be applied to both Lambda and MultiLambda
    Expression expression = node.getExpression ();
    if ( expression instanceof Lambda )
    {
      // determine the type for the parameter
      Lambda lambda = ( Lambda ) expression;
      MonoType tau1 = lambda.getTau ();
      if ( tau1 == null )
      {
        // need a new type variable
        tau1 = context.newTypeVariable ();
      }
      // generate a new type variable for the result
      TypeVariable tau2 = context.newTypeVariable ();
      // add type equations for tau and tau1->tau2
      context.addEquation ( node.getType (), new ArrowType ( tau1, tau2 ) );
      // generate a new child node
      context.addProofNode ( node,
          environment.extend ( lambda.getId (), tau1 ), lambda.getE (), tau2 );
    }
    else
    {
      // determine the type for the parameter
      MultiLambda multiLambda = ( MultiLambda ) expression;
      Identifier [] identifiers = multiLambda.getIdentifiers ();
      // generate the type for identifiers (tau1)
      TypeVariable [] typeVariables = new TypeVariable [ identifiers.length ];
      for ( int n = 0 ; n < identifiers.length ; ++n )
      {
        typeVariables [ n ] = context.newTypeVariable ();
      }
      TupleType tau1 = new TupleType ( typeVariables );
      // generate the type variable for the result
      TypeVariable tau2 = context.newTypeVariable ();
      // add type equations for tau and tau1->tau2
      context.addEquation ( node.getType (), new ArrowType ( tau1, tau2 ) );
      // generate the environment for e
      for ( int n = 0 ; n < identifiers.length ; ++n )
      {
        environment = environment.extend ( identifiers [ n ],
            typeVariables [ n ] );
      }
      // add the child nodes
      context.addProofNode ( node, environment, multiLambda.getE (), tau2 );
      // check if we have a type
      if ( multiLambda.getTau () != null )
      {
        // add an equation for tau1 = multiLet.getTau()
        context.addEquation ( tau1, multiLambda.getTau () );
      }
    }
  }


  /**
   * Applies the <b>(APP)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param pNode the type checker proof node.
   */
  public void applyApp ( TypeCheckerProofContext context,
      TypeCheckerProofNode pNode )
  {
    TypeCheckerExpressionProofNode node = ( TypeCheckerExpressionProofNode ) pNode;
    // split into tau1 and tau2 for the application
    TypeVariable tau2 = context.newTypeVariable ();
    ArrowType tau1 = new ArrowType ( tau2, node.getType () );
    // can be either an application or an infix operation
    try
    {
      // generate new child nodes
      Application application = ( Application ) node.getExpression ();
      context.addProofNode ( node, node.getEnvironment (),
          application.getE1 (), tau1 );
      context.addProofNode ( node, node.getEnvironment (),
          application.getE2 (), tau2 );
    }
    catch ( ClassCastException e )
    {
      // generate new child nodes
      InfixOperation infixOperation = ( InfixOperation ) node.getExpression ();
      Application application = new Application ( infixOperation.getOp (),
          infixOperation.getE1 () );
      context.addProofNode ( node, node.getEnvironment (), application, tau1 );
      context.addProofNode ( node, node.getEnvironment (), infixOperation
          .getE2 (), tau2 );
    }
  }


  /**
   * Applies the <b>(COND)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param pNode the type checker proof node.
   */
  public void applyCond ( TypeCheckerProofContext context,
      TypeCheckerProofNode pNode )
  {
    TypeCheckerExpressionProofNode node = ( TypeCheckerExpressionProofNode ) pNode;
    Condition condition = ( Condition ) node.getExpression ();
    context.addProofNode ( node, node.getEnvironment (), condition.getE0 (),
        new BooleanType () );
    context.addProofNode ( node, node.getEnvironment (), condition.getE1 (),
        node.getType () );
    context.addProofNode ( node, node.getEnvironment (), condition.getE2 (),
        node.getType () );
  }


  /**
   * Applies the <b>(CONST)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param node the type checker proof node.
   */
  public void applyConst ( TypeCheckerProofContext context,
      TypeCheckerProofNode node )
  {
    Constant constant = ( Constant ) node.getExpression ();
    context.addEquation ( node.getType (), ( MonoType ) context
        .getTypeForExpression ( constant ) );
  }


  /**
   * Applies the <b>(ID)</b> rule to the
   * <code>node</node> using the <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param pNode the type checker proof node.
   */
  public void applyId ( TypeCheckerProofContext context,
      TypeCheckerProofNode pNode )
  {
    TypeCheckerExpressionProofNode node = ( TypeCheckerExpressionProofNode ) pNode;
    Type type = node.getEnvironment ().get (
        ( ( Identifier ) node.getExpression () ) );
    context.addEquation ( node.getType (), ( MonoType ) type );
  }


  /**
   * Applies the <b>(LET)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param pNode the type checker proof node.
   */
  public void applyLet ( TypeCheckerProofContext context,
      TypeCheckerProofNode pNode )
  {
    TypeCheckerExpressionProofNode node = ( TypeCheckerExpressionProofNode ) pNode;
    // determine the type environment
    TypeEnvironment environment = node.getEnvironment ();
    // can be applied to LetRec, Let, MultiLet, CurriedLetRec and CurriedLet
    Expression expression = node.getExpression ();
    if ( expression instanceof Let )
    {
      // determine the first sub expression
      Let let = ( Let ) expression;
      Expression e1 = let.getE1 ();
      // check if a type was specified
      MonoType tau1 = let.getTau ();
      if ( tau1 == null )
      {
        tau1 = context.newTypeVariable ();
      }
      // add the recursion for let rec
      if ( expression instanceof LetRec )
      {
        // add the recursion for e1
        e1 = new Recursion ( let.getId (), tau1, e1 );
      }
      // add the child nodes
      context.addProofNode ( node, environment, e1, tau1 );
      context.addProofNode ( node, environment.extend ( let.getId (), tau1 ),
          let.getE2 (), node.getType () );
    }
    else if ( expression instanceof MultiLet )
    {
      // determine the identifiers of the multi let
      MultiLet multiLet = ( MultiLet ) expression;
      Identifier [] identifiers = multiLet.getIdentifiers ();
      // generate the type for e1
      TypeVariable [] typeVariables = new TypeVariable [ identifiers.length ];
      for ( int n = 0 ; n < identifiers.length ; ++n )
      {
        typeVariables [ n ] = context.newTypeVariable ();
      }
      TupleType tau = new TupleType ( typeVariables );
      // generate the environment for e2
      TypeEnvironment environment2 = environment;
      for ( int n = 0 ; n < identifiers.length ; ++n )
      {
        environment2 = environment2.extend ( identifiers [ n ],
            typeVariables [ n ] );
      }
      // add the child nodes
      context.addProofNode ( node, environment, multiLet.getE1 (), tau );
      context.addProofNode ( node, environment2, multiLet.getE2 (), node
          .getType () );
      // check if we have a type
      if ( multiLet.getTau () != null )
      {
        // add an equation for tau = multiLet.getTau()
        context.addEquation ( tau, multiLet.getTau () );
      }
    }
    else
    {
      // determine the first sub expression
      CurriedLet curriedLet = ( CurriedLet ) expression;
      Expression e1 = curriedLet.getE1 ();
      // generate the appropriate lambda abstractions
      MonoType [] types = curriedLet.getTypes ();
      Identifier [] identifiers = curriedLet.getIdentifiers ();
      for ( int n = identifiers.length - 1 ; n > 0 ; --n )
      {
        e1 = new Lambda ( identifiers [ n ], types [ n ], e1 );
      }
      // generate the type of the function
      MonoType tau1 = types [ 0 ];
      if ( tau1 == null )
      {
        tau1 = context.newTypeVariable ();
      }
      for ( int n = types.length - 1 ; n > 0 ; --n )
      {
        tau1 = new ArrowType ( ( types [ n ] != null ) ? types [ n ] : context
            .newTypeVariable (), tau1 );
      }
      // add the recursion for let rec
      if ( expression instanceof CurriedLetRec )
      {
        // add the recursion
        e1 = new Recursion ( identifiers [ 0 ], tau1, e1 );
      }
      // add the child nodes
      context.addProofNode ( node, environment, e1, tau1 );
      context.addProofNode ( node, environment
          .extend ( identifiers [ 0 ], tau1 ), curriedLet.getE2 (), node
          .getType () );
    }
  }


  /**
   * Applies the <b>(OR)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param pNode the type checker proof node.
   */
  public void applyOr ( TypeCheckerProofContext context,
      TypeCheckerProofNode pNode )
  {
    TypeCheckerExpressionProofNode node = ( TypeCheckerExpressionProofNode ) pNode;
    Or or = ( Or ) node.getExpression ();
    // generate new child nodes
    context.addProofNode ( node, node.getEnvironment (), or.getE1 (),
        new BooleanType () );
    context.addProofNode ( node, node.getEnvironment (), or.getE2 (),
        new BooleanType () );
    // add the {tau = bool} equation
    context.addEquation ( node.getType (), new BooleanType () );
  }


  /**
   * Applies the <b>(SUBTYPE)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param pNode the type checker proof node.
   */
  public void applySubtype ( @SuppressWarnings ( "unused" )
  TypeCheckerProofContext context, TypeCheckerProofNode pNode )
  {
    TypeCheckerTypeProofNode node = ( TypeCheckerTypeProofNode ) pNode;
    MonoType type = node.getType ();
    MonoType type2 = node.getType2 ();
    subtypeInternal ( type, type2 );

  }


  /**
   * Check if type is subtype of type2, neede for advanced mode
   * 
   * @param type MonoType subtype
   * @param type2 MonoType general type
   */
  public void subtypeInternal ( MonoType type, MonoType type2 )
  {
    if ( type.equals ( type2 ) )
    {
      return;
    }
    else if ( type instanceof ArrowType && type2 instanceof ArrowType )
    {
      ArrowType arrow = ( ArrowType ) type;
      ArrowType arrow2 = ( ArrowType ) type2;

      MonoType taul = arrow.getTau1 ();
      MonoType taur = arrow.getTau2 ();

      MonoType tau2l = arrow2.getTau1 ();
      MonoType tau2r = arrow2.getTau2 ();

      subtypeInternal ( tau2l, taul );
      subtypeInternal ( taur, tau2r );
      return;
    }
    else if ( type instanceof RefType && type2 instanceof RefType )
    {
      RefType ref = ( RefType ) type;
      RefType ref2 = ( RefType ) type2;

      subtypeInternal ( ref.getTau (), ref2.getTau () );
      return;
    }
    else if ( type instanceof TupleType && type2 instanceof TupleType )
    {
      TupleType tuple = ( TupleType ) type;
      TupleType tuple2 = ( TupleType ) type2;

      MonoType [] types = tuple.getTypes ();
      MonoType [] types2 = tuple2.getTypes ();
      for ( int i = 0 ; i < types.length ; i++ )
      {
        subtypeInternal ( types [ i ], types2 [ i ] );
        return;
      }
    }
    else if ( type instanceof ListType && type2 instanceof ListType )
    {
      ListType list = ( ListType ) type;
      ListType list2 = ( ListType ) type2;

      subtypeInternal ( list.getTau (), list2.getTau () );
      return;
    }
    else if ( type instanceof ObjectType && type2 instanceof ObjectType )
    {
      ObjectType object = ( ObjectType ) type;
      ObjectType object2 = ( ObjectType ) type2;
      subtypeInternal ( object.getPhi (), object2.getPhi () );
      return;
    }
    else if ( type instanceof RowType && type2 instanceof RowType )
    {
      RowType row = ( RowType ) type;
      RowType row2 = ( RowType ) type2;

      Identifier [] ids = row.getIdentifiers ();
      MonoType [] types = row.getTypes ();
      MonoType [] types2 = row2.getTypes ();

      int index;
      for ( int i = 0 ; i < ids.length ; i++ )
      {
        index = row2.getIndexOfIdentifier ( ids [ i ] );
        if ( index >= 0 )
          subtypeInternal ( types [ i ], types2 [ index ] );
        else
          throw new RuntimeException ( MessageFormat.format ( Messages
              .getString ( "MinimalTypingException.3" ), type, type2 ) ); //$NON-NLS-1$

      }
      return;
    }

    else if ( type instanceof RecType )
    {
      RecType rec = ( RecType ) type;

      subtypeInternal ( rec.getTau ().substitute ( rec.getTypeName (), rec ),
          type2 );
      return;
    }
    else if ( type2 instanceof RecType )
    {
      RecType rec = ( RecType ) type2;

      subtypeInternal ( type, rec.getTau ().substitute ( rec.getTypeName (),
          rec ) );
      return;
    }

    throw new RuntimeException ( MessageFormat.format ( Messages
        .getString ( "MinimalTypingException.3" ), type, type2 ) ); //$NON-NLS-1$
  }


  /**
   * Supremum means intersection of type and type2
   * 
   * @param type first type for supremum operation
   * @param type2 second type for supremum operation
   * @return intersection of the two types
   */
  private MonoType supremum ( MonoType type, MonoType type2 )
  {
    if ( type.equals ( type2 ) )
      return type;
    if ( type instanceof ArrowType && type2 instanceof ArrowType )
    {
      ArrowType arrow = ( ArrowType ) type;
      ArrowType arrow2 = ( ArrowType ) type2;
      return new ArrowType ( supremum ( arrow.getTau1 (), arrow2.getTau1 () ),
          infimum ( arrow.getTau2 (), arrow2.getTau2 () ) );
    }

    throw new RuntimeException ( "supremum type error" ); //$NON-NLS-1$
  }


  /**
   * Infimum means union of type and type2
   * 
   * @param type first type for infimum operation
   * @param type2 second type for infimum operation
   * @return union of the two types
   */
  private MonoType infimum ( MonoType type, MonoType type2 )
  {
    if ( type.equals ( type2 ) )
      return type;
    if ( type instanceof ArrowType && type2 instanceof ArrowType )
    {
      ArrowType arrow = ( ArrowType ) type;
      ArrowType arrow2 = ( ArrowType ) type2;
      return new ArrowType ( infimum ( arrow.getTau1 (), arrow2.getTau1 () ),
          supremum ( arrow.getTau2 (), arrow2.getTau2 () ) );
    }

    throw new RuntimeException ( "infimum type error" ); //$NON-NLS-1$
  }


  /**
   * Applies the <b>(COERCE)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param pNode the node to apply the <b>(COERCE)</b> rule to.
   */
  public void applyCoercion ( TypeCheckerProofContext context,
      TypeCheckerProofNode pNode )
  {
    TypeCheckerExpressionProofNode node = ( TypeCheckerExpressionProofNode ) pNode;
    Coercion coercion = ( Coercion ) node.getExpression ();
    // add the first proof node
    context.addEquation ( node.getType (), coercion.getTau2 () );
    context.addProofNode ( node, node.getEnvironment (), coercion.getE (),
        coercion.getTau1 () );
    context.addProofNode ( node, coercion.getTau1 (), coercion.getTau2 () );
  }

}
