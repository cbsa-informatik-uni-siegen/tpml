package de.unisiegen.tpml.core.languages.l1;


import java.text.MessageFormat;
import java.util.ArrayList;

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
import de.unisiegen.tpml.core.expressions.MultiLambda;
import de.unisiegen.tpml.core.expressions.MultiLet;
import de.unisiegen.tpml.core.expressions.Or;
import de.unisiegen.tpml.core.expressions.Recursion;
import de.unisiegen.tpml.core.minimaltyping.AbstractMinimalTypingProofRuleSet;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingExpressionProofNode;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofContext;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingTypesProofNode;
import de.unisiegen.tpml.core.subtypingrec.DefaultSubType;
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


/**
 * The minimal type proof rules for the <code>L1</code> language.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.minimaltyping.AbstractMinimalTypingProofRuleSet
 */
public class L1MinimalTypingProofRuleSet extends
    AbstractMinimalTypingProofRuleSet
{

  /**
   * Allocates a new <code>L1MinimalTypingProofRuleSet</code> for the
   * specified <code>language</code>.
   * 
   * @param language the <code>L1</code> or a derived language.
   * @param mode the actual choosen mode
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   */
  public L1MinimalTypingProofRuleSet ( L1Language language, boolean mode )
  {
    super ( language );
    // register the type rules
    if ( mode ) // advanced mode
      registerByMethodName ( L1Language.L1, "SUBTYPE", "applySubtype" ); //$NON-NLS-1$ //$NON-NLS-2$
    else
    { // beginner mode
      registerByMethodName ( L1Language.L1, "ARROW", "applyArrow" ); //$NON-NLS-1$ //$NON-NLS-2$
      registerByMethodName ( L1Language.L1, "REFL", "applyRefl" ); //$NON-NLS-1$ //$NON-NLS-2$
    }
    registerByMethodName ( L1Language.L1, "LET", "applyLet", "updateLet" );//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    registerByMethodName ( L1Language.L1,
        "APP-SUBSUME", "applyAppSubsume", "updateAppSubsume" );//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    registerByMethodName ( L1Language.L1,
        "COND-SUBSUME", "applyCondSubsume", "updateCondSubsume" );//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    registerByMethodName ( L1Language.L1, "ABSTR", "applyAbstr", //$NON-NLS-1$//$NON-NLS-2$
        "updateAbstr" ); //$NON-NLS-1$
    registerByMethodName ( L1Language.L1, "AND", "applyAnd", "updateAnd" );//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    registerByMethodName ( L1Language.L1, "OR", "applyOr", "updateOr" );//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    registerByMethodName ( L1Language.L1, "CONST", "applyConst" );//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1, "ID", "applyId" );//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1,
        "COERCE", "applyCoercion", "updateCoercion" );//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

  }


  /**
   * Applies the <b>(ID)</b> rule to the
   * <code>node</node> using the <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applyId ( MinimalTypingProofContext context,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
    Identifier id = ( Identifier ) node.getExpression ();
    TypeEnvironment environment = node.getEnvironment ();
    MonoType type = ( MonoType ) environment.get ( id );

    // set the type of this node
    context.setNodeType ( node, type );
  }


  /**
   * Applies the <b>(CONST)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applyConst ( MinimalTypingProofContext context,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
    Constant constant = ( Constant ) node.getExpression ();
    MonoType type = ( MonoType ) context.getTypeForExpression ( constant );

    // set the type for this node
    context.setNodeType ( node, type );
  }


  /**
   * Applies the <b>(AND)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applyAnd ( MinimalTypingProofContext context,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
    And and = ( And ) node.getExpression ();
    // generate new child node
    context.addProofNode ( node, node.getEnvironment (), and.getE1 () );

  }


  /**
   * Updates the <code>node</code> to which <b>(AND)</b> was applied
   * previously.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the node to update according to <b>(AND)</b>.
   */
  public void updateAnd ( MinimalTypingProofContext context,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
    And and = ( And ) node.getExpression ();
    if ( node.getChildCount () == 1 && node.getFirstChild ().isFinished () )
    {
      // generate new child node
      context.addProofNode ( node, node.getEnvironment (), and.getE2 () );
    }
    else if ( node.getChildCount () == 2 && node.getChildAt ( 1 ).isFinished () )
    {
      // check if e0 of the Condition is of type boolean
      if ( node.getFirstChild ().getType () instanceof BooleanType
          && node.getChildAt ( 1 ).getType () instanceof BooleanType )
        // generate new child node
        context.setNodeType ( node, new BooleanType () );
      else
        throw new RuntimeException ( MessageFormat.format ( Messages
            .getString ( "MinimalTypingException.0" ), and.toString () ) ); //$NON-NLS-1$
    }

  }


  /**
   * Applies the <b>(OR)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applyOr ( MinimalTypingProofContext context,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
    Or or = ( Or ) node.getExpression ();
    // generate new child node
    context.addProofNode ( node, node.getEnvironment (), or.getE1 () );

  }


  /**
   * Updates the <code>node</code> to which <b>(OR)</b> was applied
   * previously.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the node to update according to <b>(OR)</b>.
   */
  public void updateOr ( MinimalTypingProofContext context,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
    Or or = ( Or ) node.getExpression ();
    if ( node.getChildCount () == 1 && node.getFirstChild ().isFinished () )
    {
      // generate new child node
      context.addProofNode ( node, node.getEnvironment (), or.getE2 () );
    }
    else if ( node.getChildCount () == 2 && node.getChildAt ( 1 ).isFinished () )
    {
      if ( node.getFirstChild ().getType () instanceof BooleanType
          && node.getChildAt ( 1 ).getType () instanceof BooleanType )
        // set the type for this node
        context.setNodeType ( node, new BooleanType () );
      else
        throw new RuntimeException ( MessageFormat.format ( Messages
            .getString ( "MinimalTypingException.0" ), or.toString () ) ); //$NON-NLS-1$
    }

  }


  /**
   * Applies the <b>(APP-SUBSUME)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applyAppSubsume ( MinimalTypingProofContext context,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
    try
    {
      Application app = ( Application ) node.getExpression ();
      // generate new child node
      context.addProofNode ( node, node.getEnvironment (), app.getE1 () );
    }
    catch ( ClassCastException e )
    { // catch exception and check if expression is an Infix Operation
      // generate new child nodes
      InfixOperation infixOperation = ( InfixOperation ) node.getExpression ();
      Application application = new Application ( infixOperation.getOp (),
          infixOperation.getE1 () );
      // generate new child node
      context.addProofNode ( node, node.getEnvironment (), application );

    }
  }


  /**
   * Updates the <code>node</code> to which <b>(APP-SUBSUME)</b> was applied
   * previously.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the node to update according to <b>(APP-SUBSUME)</b>.
   */
  public void updateAppSubsume ( MinimalTypingProofContext context,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;

    if ( node.getChildCount () == 1 && node.getChildAt ( 0 ).isFinished () )
    {
      // Expression could be an Application or an InfixOperation, so we have to
      // check both
      // First check if expression is an Application
      try
      {
        Application app = ( Application ) node.getExpression ();
        // generate new child node
        context.addProofNode ( node, node.getEnvironment (), app.getE2 () );
      }
      // otherwise check if it is an InfixOperation
      catch ( ClassCastException e )
      {
        InfixOperation infixOperation = ( InfixOperation ) node
            .getExpression ();
        // generate new child node
        context.addProofNode ( node, node.getEnvironment (), infixOperation
            .getE2 () );
      }
    }
    else if ( node.getChildCount () == 2 && node.getChildAt ( 1 ).isFinished () )
    {
      MonoType type = ( node.getChildAt ( 1 ) ).getType ();
      MonoType childType = node.getChildAt ( 0 ).getType ();
      // if type is a rectype, unfold to get an arrow type
      if ( childType instanceof RecType )
      {
        RecType rec = ( RecType ) childType;
        childType = rec.getTau ().substitute ( rec.getTypeName (), rec );
      }
      if ( childType instanceof ArrowType )
      {
        ArrowType arrow = ( ArrowType ) childType;
        MonoType type2 = arrow.getTau1 ();
        // generate new child node
        context.addProofNode ( node, type, type2 );
        return;
      }
      throw new RuntimeException (
          MessageFormat
              .format (
                  Messages.getString ( "MinimalTypingException.1" ), node.getExpression ().toString () ) ); //$NON-NLS-1$
    }
    else if ( node.getChildCount () == 3 && node.isFinished () )
    {
      MonoType childType = node.getChildAt ( 0 ).getType ();
      // if type is a rec type, unfold to get an arrow type
      if ( childType instanceof RecType )
      {
        RecType rec = ( RecType ) childType;
        childType = rec.getTau ().substitute ( rec.getTypeName (), rec );
      }
      if ( childType instanceof ArrowType )
      {
        ArrowType arrow = ( ArrowType ) childType;
        MonoType type = arrow.getTau2 ();
        // set the type of this node
        context.setNodeType ( node, type );
      }
    }
  }


  /**
   * Applies the <b>(ABSTR)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applyAbstr ( MinimalTypingProofContext context,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
    Expression expression = node.getExpression ();

    if ( expression instanceof Lambda )
    {
      Lambda lambda = ( Lambda ) expression;
      // check if the user entered a type
      if ( lambda.getTau () == null )
        throw new RuntimeException (
            MessageFormat
                .format (
                    Messages.getString ( "MinimalTypingException.2" ), lambda.getIdentifiers () [ 0 ].toString () ) ); //$NON-NLS-1$

      TypeEnvironment environment = node.getEnvironment ();

      // generate new child node
      context.addProofNode ( node, environment.extend ( lambda.getId (), lambda
          .getTau () ), lambda.getE () );
    }
    else
    {
      // determine the type for the parameter
      MultiLambda multiLambda = ( MultiLambda ) expression;
      Identifier [] identifiers = multiLambda.getIdentifiers ();
      TupleType type = ( TupleType ) multiLambda.getTau ();
      MonoType [] types = type.getTypes ();

      TypeEnvironment environment = node.getEnvironment ();

      for ( int i = 0 ; i < identifiers.length ; i++ )
      {
        if ( types [ i ] == null )
          throw new RuntimeException (
              MessageFormat
                  .format (
                      Messages.getString ( "MinimalTypingException.2" ), identifiers [ i ].toString () ) ); //$NON-NLS-1$
        environment = environment.extend ( identifiers [ i ], types [ i ] );
      }

      // generate new child node
      context.addProofNode ( node, environment, multiLambda.getE () );
    }

  }


  /**
   * Updates the <code>node</code> to which <b>(ABSTR)</b> was applied
   * previously.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the node to update according to <b>(ABSTR)</b>.
   */
  public void updateAbstr ( MinimalTypingProofContext context,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
    Expression expression = node.getExpression ();

    if ( node.getFirstChild ().isFinished () )
    {

      if ( expression instanceof Lambda )
      {
        Lambda lambda = ( Lambda ) expression;
        MonoType type = node.getFirstChild ().getType ();
        ArrowType arrow = new ArrowType ( lambda.getTau (), type );
        // set the type of this node
        context.setNodeType ( node, arrow );
      }

      else
      {
        MultiLambda lambda = ( MultiLambda ) expression;
        MonoType type = node.getFirstChild ().getType ();
        ArrowType arrow = new ArrowType ( lambda.getTau (), type );
        // set the type of this node
        context.setNodeType ( node, arrow );
      }
    }
  }


  /**
   * Applies the <b>(COND-SUBSUME)</b> rule to the <code>node</code> using
   * the <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applyCondSubsume ( MinimalTypingProofContext context,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
    Condition cond = ( Condition ) node.getExpression ();
    // generate new child node
    context.addProofNode ( node, node.getEnvironment (), cond.getE0 () );
  }


  /**
   * Updates the <code>node</code> to which <b>(COND-SUBSUME)</b> was applied
   * previously.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the node to update according to <b>(COND-SUBSUME)</b>.
   */
  public void updateCondSubsume ( MinimalTypingProofContext context,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
    Condition cond = ( Condition ) node.getExpression ();
    if ( node.getChildCount () == 1 && node.getChildAt ( 0 ).isFinished () )
    {
      if ( ! ( node.getChildAt ( 0 ).getType () instanceof BooleanType ) )
        throw new RuntimeException (
            MessageFormat
                .format (
                    Messages.getString ( "MinimalTypingException.0" ), node.getExpression ().toString () ) ); //$NON-NLS-1$
      // generate new child node
      context.addProofNode ( node, node.getEnvironment (), cond.getE1 () );
    }
    else if ( node.getChildCount () == 2 && node.getChildAt ( 1 ).isFinished () )
    {
      // generate new child node
      context.addProofNode ( node, node.getEnvironment (), cond.getE2 () );
    }
    else if ( node.getChildCount () == 3 && node.isFinished () )
    {
      try
      {
        MonoType type = supremum ( node.getChildAt ( 1 ).getType (), node
            .getChildAt ( 2 ).getType () );
        // set the type of this node
        context.setNodeType ( node, type );
      }
      catch ( Exception e )
      {
        throw new RuntimeException (
            MessageFormat
                .format (
                    Messages.getString ( "MinimalTypingException.4" ), node.getExpression ().toString () ) ); //$NON-NLS-1$
      }
    }

  }


  /**
   * Applies the <b>(LET)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applyLet ( MinimalTypingProofContext context,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;

    Expression expression = node.getExpression ();

    if ( expression instanceof Let )
    {
      Let let = ( Let ) node.getExpression ();

      // check if the user entered a type
      if ( ( let.getTau () == null ) && false )
        throw new RuntimeException ( MessageFormat.format ( Messages
            .getString ( "MinimalTypingException.2" ), let.toString () ) ); //$NON-NLS-1$
      context.addProofNode ( node, node.getEnvironment (), let.getE1 () );
    }
    else if ( expression instanceof MultiLet )
    {
      MultiLet let = ( MultiLet ) expression;
      // generate new child node
      context.addProofNode ( node, node.getEnvironment (), let.getE1 () );
    }
    else
    {
      CurriedLet let = ( CurriedLet ) expression;
      Expression e1 = let.getE1 ();
      // generate the appropriate lambda abstractions
      MonoType [] types = let.getTypes ();
      Identifier [] identifiers = let.getIdentifiers ();
      for ( int n = identifiers.length - 1 ; n > 0 ; --n )
      {
        e1 = new Lambda ( identifiers [ n ], types [ n ], e1 );
      }

      // add the recursion for let rec
      if ( expression instanceof CurriedLetRec )
      {
        CurriedLetRec letRec = ( CurriedLetRec ) expression;
        // generate the type of the function
        MonoType tau1 = types [ 0 ];
        if ( tau1 == null )
          throw new RuntimeException (
              MessageFormat
                  .format (
                      Messages.getString ( "MinimalTypingException.2" ), letRec.getIdentifiers () [ 0 ].toString () ) ); //$NON-NLS-1$

        for ( int n = types.length - 1 ; n > 0 ; --n )
        {
          if ( types [ n ] == null )
            throw new RuntimeException (
                MessageFormat
                    .format (
                        Messages.getString ( "MinimalTypingException.2" ), letRec.getIdentifiers () [ n ].toString () ) ); //$NON-NLS-1$

          tau1 = new ArrowType ( types [ n ], tau1 );
        }
        // add the recursion
        e1 = new Recursion ( identifiers [ 0 ], tau1, e1 );
      }
      // generate new child node
      context.addProofNode ( node, node.getEnvironment (), e1 );
    }
  }


  /**
   * Updates the <code>node</code> to which <b>(LET)</b> was applied
   * previously.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the node to update according to <b>(LET)</b>.
   */
  public void updateLet ( MinimalTypingProofContext context,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
    Expression expression = node.getExpression ();

    if ( node.getChildCount () == 1 && node.getFirstChild ().isFinished () )
    {
      if ( expression instanceof Let )
      {
        Let let = ( Let ) node.getExpression ();
        TypeEnvironment environment = node.getEnvironment ();
        // generate new child node
        context.addProofNode ( node, environment.extend ( let.getId (), node
            .getFirstChild ().getType () ), let.getE2 () );
      }
      else if ( expression instanceof MultiLet )
      {
        MultiLet let = ( MultiLet ) expression;
        MinimalTypingExpressionProofNode child = ( MinimalTypingExpressionProofNode ) node
            .getFirstChild ();
        Identifier [] ids = let.getIdentifiers ();
        TypeEnvironment environment = node.getEnvironment ();
        for ( int i = 0 ; i < child.getChildCount () ; i++ )
        {
          environment = environment.extend ( ids [ i ], child.getChildAt ( i )
              .getType () );
        }
        // generate new child node
        context.addProofNode ( node, environment, let.getE2 () );
      }
      else if ( expression instanceof CurriedLet )
      {
        CurriedLet let = ( CurriedLet ) expression;
        TypeEnvironment environment = node.getEnvironment ();

        Identifier [] identifiers = let.getIdentifiers ();

        /*
         * MonoType[] types = let.getTypes ( ); MonoType tau1 = types[0]; for (
         * int n = types.length - 1; n > 0; --n ) { tau1 = new ArrowType (
         * types[n], tau1 ); }
         */
        environment = environment.extend ( identifiers [ 0 ], node
            .getFirstChild ().getType () );

        // generate new child node
        context.addProofNode ( node, environment, let.getE2 () );
      }
    }
    else if ( node.getChildCount () == 2 && node.getChildAt ( 1 ).isFinished () )
    {
      if ( expression instanceof Let || expression instanceof CurriedLetRec )
      {
        MonoType type = node.getChildAt ( 1 ).getType ();
        context.setNodeType ( node, type );
      }
      else if ( expression instanceof CurriedLet )
      {
        CurriedLet let = ( CurriedLet ) expression;
        MonoType type = node.getChildAt ( 1 ).getType ();
        MonoType type2 = let.getTypes () [ 0 ];
        if ( type2 == null )
        {

          // set the type of this node
          context.setNodeType ( node, type );
        }
        else
        {
          // generate new child node
          context.addProofNode ( node, type, type2 );
        }
      }
      else
      {
        context.setNodeType ( node, node.getLastChild ().getType () );
      }
    }
    else if ( node.getChildCount () == 3 && node.getChildAt ( 2 ).isFinished () )
    {
      MonoType type = node.getChildAt ( 1 ).getType ();
      // set the type of this node
      context.setNodeType ( node, type );
    }
  }


  /**
   * Applies the <b>(SUBTYPE)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applySubtype ( @SuppressWarnings ( "unused" )
  MinimalTypingProofContext context, MinimalTypingProofNode pNode )
  {
    MinimalTypingTypesProofNode node = ( MinimalTypingTypesProofNode ) pNode;
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

      subtypeInternal ( taul, tau2l );
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
    if (type instanceof ObjectType && type2 instanceof ObjectType)
    {
      ArrayList < Identifier > ids = new ArrayList < Identifier >();
      ArrayList < MonoType > types = new ArrayList < MonoType >();
      
      // Get the containing row types
      RowType rowType1 = ( RowType ) ( ( ObjectType ) type ).getPhi ();
      RowType rowType2 = ( RowType ) ( ( ObjectType ) type2 ).getPhi ();
      
      // Search for equal method identifier with compatible types
      for (Identifier id1 : rowType1.getIdentifiers ())
      {
        for (Identifier id2 : rowType2.getIdentifiers ())
        {
          if (id1.equals ( id2 ))
          {
            int indexType1 = rowType1.getIndexOfIdentifier ( id1 );
            int indexType2 = rowType2.getIndexOfIdentifier ( id2 );
            try {
              MonoType resultType = supremum(rowType1.getTypes ()[indexType1], rowType2.getTypes ()[indexType2]);
              // Add the id and type to create the new object type as result
              ids.add ( id1 );
              types.add ( resultType );
            }
            catch (Exception e)
            {
              // Types not compatible. Do nothing!
            }
          }
        }
      }
      Identifier [] idArray = new Identifier [ids.size ()];
      for ( int i = 0 ; i < ids.size (); i++)
      {
        idArray[i] = ids.get ( i );
      }
      MonoType [] typeArray = new MonoType [types.size ()];
      for ( int i = 0 ; i < types.size (); i++)
      {
        typeArray[i] = types.get ( i );
      }
      return new ObjectType(new RowType(idArray, typeArray));
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
   * Applies the <b>(REFL)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applyRefl ( @SuppressWarnings ( "unused" )
  MinimalTypingProofContext context, MinimalTypingProofNode pNode )
  {
    MinimalTypingTypesProofNode node = ( MinimalTypingTypesProofNode ) pNode;
    MonoType type;
    MonoType type2;

    type = node.getType ();
    type2 = node.getType2 ();

    if ( type.equals ( type2 ) )
      return;

    throw new IllegalArgumentException ( MessageFormat.format ( Messages
        .getString ( "SubTypingException.5" ), type, type2 ) ); //$NON-NLS-1$

  }


  /**
   * Applies the <b>(ARROW)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applyArrow ( MinimalTypingProofContext context,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingTypesProofNode node = ( MinimalTypingTypesProofNode ) pNode;
    ArrowType type;
    ArrowType type2;
    type = ( ArrowType ) node.getType ();
    type2 = ( ArrowType ) node.getType2 ();

    MonoType taul = type.getTau1 ();
    MonoType taur = type.getTau2 ();

    MonoType tau2l = type2.getTau1 ();
    MonoType tau2r = type2.getTau2 ();

    context.addProofNode ( node, tau2l, taul );
    context.addProofNode ( node, taur, tau2r );

    node.getSeenTypes ().add (
        new DefaultSubType ( node.getType (), node.getType2 () ) );
  }


  /**
   * Applies the <b>(S-ASSUME)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applyAssume ( @SuppressWarnings ( "unused" )
  MinimalTypingProofContext context, MinimalTypingProofNode pNode )
  {
    MinimalTypingTypesProofNode node = ( MinimalTypingTypesProofNode ) pNode;
    if ( node.getSeenTypes ().contains ( node.getSubType () ) )
      return;
    throw new IllegalArgumentException ( Messages
        .getString ( "MinimalTypingException.5" ) ); //$NON-NLS-1$
  }


  /**
   * Applies the <b>(S-MU-LEFT)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applyMuLeft ( MinimalTypingProofContext context,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingTypesProofNode node = ( MinimalTypingTypesProofNode ) pNode;
    RecType rec = ( RecType ) node.getType ();

    // generate new child node
    context.addProofNode ( node, rec.getTau ().substitute ( rec.getTypeName (),
        rec ), node.getType2 () );

    node.getSeenTypes ().add (
        new DefaultSubType ( node.getType (), node.getType2 () ) );

  }


  /**
   * Applies the <b>(S-MU-RIGHT)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applyMuRight ( MinimalTypingProofContext context,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingTypesProofNode node = ( MinimalTypingTypesProofNode ) pNode;
    RecType rec = ( RecType ) node.getType2 ();

    // generate new child node
    context.addProofNode ( node, node.getType (), rec.getTau ().substitute (
        rec.getTypeName (), rec ) );

    node.getSeenTypes ().add (
        new DefaultSubType ( node.getType (), node.getType2 () ) );

  }


  /**
   * Applies the <b>(COERCE)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param node the node to apply the <b>(COERCE)</b> rule to.
   */
  public void applyCoercion ( MinimalTypingProofContext context,
      MinimalTypingProofNode node )
  {
    // add the first proof node
    context.addProofNode ( node, node.getEnvironment (), ( ( Coercion ) node
        .getExpression () ).getE () );
  }


  /**
   * Updates the <code>node</code> to which <b>(COERCE)</b> was applied
   * previously.
   * 
   * @param context the minimal typing proof context.
   * @param node the node to update according to <b>(COERCE)</b>.
   */
  public void updateCoercion ( MinimalTypingProofContext context,
      MinimalTypingProofNode node )
  {
    if ( node.getChildCount () == 1 && node.getFirstChild ().isFinished () )
    {
      Coercion coercion = ( Coercion ) node.getExpression ();
      try
      {
        MonoType type = node.getFirstChild ().getType ();
        MonoType type2 = coercion.getTau1 ();
        subtypeInternal ( type, type2 );
        subtypeInternal ( type2, type );
      }
      catch ( Exception e )
      {
        throw new RuntimeException ( "type of e not equal the given type" ); //$NON-NLS-1$
      }
      context.addProofNode ( node, coercion.getTau1 (), coercion.getTau2 () );
    }
  }
}
