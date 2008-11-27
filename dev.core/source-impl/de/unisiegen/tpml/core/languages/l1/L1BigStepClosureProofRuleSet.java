package de.unisiegen.tpml.core.languages.l1;


import de.unisiegen.tpml.core.ClosureEnvironment;
import de.unisiegen.tpml.core.DefaultClosureEnvironment;
import de.unisiegen.tpml.core.bigstepclosure.AbstractBigStepClosureProofRuleSet;
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofContext;
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofNode;
import de.unisiegen.tpml.core.bigstepclosure.BinaryOpArgs;
import de.unisiegen.tpml.core.expressions.And;
import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.BinaryOperatorException;
import de.unisiegen.tpml.core.expressions.BooleanConstant;
import de.unisiegen.tpml.core.expressions.Closure;
import de.unisiegen.tpml.core.expressions.Condition;
import de.unisiegen.tpml.core.expressions.Condition1;
import de.unisiegen.tpml.core.expressions.CurriedLet;
import de.unisiegen.tpml.core.expressions.CurriedLetRec;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.InfixOperation;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.expressions.LetRec;
import de.unisiegen.tpml.core.expressions.MultiLet;
import de.unisiegen.tpml.core.expressions.Or;
import de.unisiegen.tpml.core.expressions.Projection;
import de.unisiegen.tpml.core.expressions.Recursion;
import de.unisiegen.tpml.core.expressions.UnaryOperator;
import de.unisiegen.tpml.core.expressions.UnaryOperatorException;
import de.unisiegen.tpml.core.expressions.UnitConstant;
import de.unisiegen.tpml.core.expressions.Value;
import de.unisiegen.tpml.core.types.MonoType;


/**
 * TODO
 */
public class L1BigStepClosureProofRuleSet extends
    AbstractBigStepClosureProofRuleSet
{

  public L1BigStepClosureProofRuleSet ( L1Language language )
  {
    super ( language );

    registerByMethodName ( L1Language.L1, "VAL", "applyVal" );
    registerByMethodName ( L1Language.L1, "ID", "applyId", "updateId" );
    registerByMethodName ( L1Language.L1, "OP-1", "applyApp", "updateOP1" );
    registerByMethodName ( L1Language.L1, "OP-2", "applyOP2", "updateOP2" );
    registerByMethodName ( L1Language.L1, "BETA-V", "applyApp", "updateBetaV" );
    registerByMethodName ( L1Language.L1, "COND-TRUE", "applyCond",
        "updateCondT" );
    registerByMethodName ( L1Language.L1, "COND-FALSE", "applyCond",
        "updateCondF" );
    registerByMethodName ( L1Language.L1, "LET", "applyLet", "updateLet" );
    registerByMethodName ( L1Language.L1, "AND-TRUE", "applyAnd", "updateAndT" );
    registerByMethodName ( L1Language.L1, "AND-FALSE", "applyAnd", "updateAndF" );
    registerByMethodName ( L1Language.L1, "OR-TRUE", "applyOr", "updateOrT" );
    registerByMethodName ( L1Language.L1, "OR-FALSE", "applyOr", "updateOrF" );
  }


  public void applyVal ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    final Expression expression = node.getExpression ();
    if ( expression instanceof Identifier )
      throw new RuntimeException ( "An Identifier is not a Value!" );
    final Value val = ( Value ) expression;
    context.setProofNodeResult ( node, new Closure ( val, node
        .getEnvironment () ) );
  }


  public void applyId ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    final Identifier id = ( Identifier ) node.getExpression ();
    final ClosureEnvironment env = node.getEnvironment ();
    context.addProofNode ( node, env.get ( id ) );
  }


  public void updateId ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    final BigStepClosureProofNode child0 = node.getChildAt ( 0 );
    if ( child0.isProven () )
      context.setProofNodeResult ( node, child0.getResult () );
  }


  public void applyApp ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    Application app = ( Application ) node.getExpression ();
    context.addProofNode ( node, new Closure ( app.getE1 (), node
        .getEnvironment () ) );
    context.addProofNode ( node, new Closure ( app.getE2 (), node
        .getEnvironment () ) );
  }


  public void applyOP2 ( BigStepClosureProofContext context,
      BigStepClosureProofNode node ) throws BinaryOperatorException
  {
    Expression e = node.getExpression ();
    if ( e instanceof Application )
    {
      applyApp ( context, node );
      return;
    }

    final InfixOperation io = ( InfixOperation ) e;
    context.addProofNode ( node, new Closure ( io.getE1 (), node
        .getEnvironment () ) );
    context.addProofNode ( node, new Closure ( io.getE2 (), node
        .getEnvironment () ) );
  }


  public void updateOP1 ( BigStepClosureProofContext context,
      BigStepClosureProofNode node ) throws UnaryOperatorException
  {
    if ( node.getChildCount () < 2 )
      return;

    final BigStepClosureProofNode child0 = node.getChildAt ( 0 ), child1 = node
        .getChildAt ( 1 );

    if ( ! ( child0.isProven () && child1.isProven () ) )
      return;

    // if the parent is a BinaryOperator we have to defer the evaluation
    try
    {
      final Expression parentExp = node.getParent ().getExpression ();
      if ( parentExp instanceof Application
          || parentExp instanceof InfixOperation )
      {
        context.setProofNodeResult ( node, new Closure ( new Application (
            child0.getExpression (), child1.getResult ().getClosure ()
                .getExpression () ), DefaultClosureEnvironment.empty () ) );
        return;
      }
    }
    catch ( final Exception e )
    {
      e.printStackTrace ();
    }

    final UnaryOperator uop = ( UnaryOperator ) child0.getExpression ();
    context.setProofNodeResult ( node, new Closure ( uop.applyTo ( child1
        .getExpression () ), DefaultClosureEnvironment.empty () ) );
  }


  public void updateOP2 ( BigStepClosureProofContext context,
      BigStepClosureProofNode node ) throws BinaryOperatorException
  {
    if ( node.getChildCount () < 2 )
      return;

    BigStepClosureProofNode child0 = node.getChildAt ( 0 ), child1 = node
        .getChildAt ( 1 );

    if ( ! ( child0.isProven () && child1.isProven () ) )
      return;

    final BinaryOpArgs args = new BinaryOpArgs ( node );

    context.setProofNodeResult ( node, new Closure ( args.getOperator ()
        .applyTo ( args.getOperand1 (), args.getOperand2 () ),
        DefaultClosureEnvironment.empty () ) );
  }


  public void updateBetaV ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    if ( node.getChildCount () < 2 )
      return;

    BigStepClosureProofNode child0 = node.getFirstChild (), child1 = node
        .getChildAt ( 1 );
    if ( node.getChildCount () == 2 && child0.isProven () && child1.isProven () )
    {
      Closure result0 = child0.getResult ().getClosure ();
      Lambda lambda = ( Lambda ) result0.getExpression ();
      Closure closure = child1.getResult ().getClosure ();
      ClosureEnvironment environment = result0.cloneEnvironment ();
      environment.put ( lambda.getId (), closure );
      context.addProofNode ( node, new Closure ( lambda.getE (), environment ) );
    }
    else if ( node.getChildCount () == 3 )
    {
      BigStepClosureProofNode child2 = node.getChildAt ( 2 );
      if ( child2.isProven () )
        context.setProofNodeResult ( node, child2.getResult ().getClosure () );
    }
  }


  public void applyCond ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node )
  {
    final Expression e = node.getExpression ();
    if ( e instanceof Condition )
    {
      final Condition cond = ( Condition ) e;
      context.addProofNode ( node, new Closure ( cond.getE0 (), node
          .getEnvironment () ) );
    }
    else if ( e instanceof Condition1 )
    {
      final Condition1 cond = ( Condition1 ) e;
      context.addProofNode ( node, new Closure ( cond.getE0 (), node
          .getEnvironment () ) );
    }
    else
      throw new RuntimeException (
          "applyCond can only be used with Condition or Condition1!" );
  }


  public void updateCondT ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    if ( node.getChildCount () == 1 && node.getChildAt ( 0 ).isProven () )
    {
      BigStepClosureProofNode child0 = node.getChildAt ( 0 );
      if ( ! ( ( BooleanConstant ) child0.getResult ().getValue () )
          .booleanValue () )
      {
        updateCondF ( context, node );
        return;
      }

      Expression expression = node.getExpression ();
      context.addProofNode ( node, new Closure (
          expression instanceof Condition ? ( ( Condition ) expression )
              .getE1 () : ( ( Condition1 ) expression ).getE1 (), node
              .getEnvironment () ) );
    }
    else if ( node.getChildCount () == 2 && node.getChildAt ( 1 ).isProven () )
      context.setProofNodeResult ( node, node.getChildAt ( 1 ).getResult () );
  }


  public void updateCondF ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    if ( node.getChildCount () == 1 && node.getChildAt ( 0 ).isProven () )
    {
      BigStepClosureProofNode child0 = node.getChildAt ( 0 );
      if ( ( ( BooleanConstant ) child0.getResult ().getValue () )
          .booleanValue () )
      {
        updateCondT ( context, node );
        return;
      }

      Expression expression = node.getExpression ();
      context.addProofNode ( node, new Closure (
          expression instanceof Condition ? ( ( Condition ) expression )
              .getE2 () : new UnitConstant (), node.getEnvironment () ) );
    }
    else if ( node.getChildCount () == 2 && node.getChildAt ( 1 ).isProven () )
      context.setProofNodeResult ( node, node.getChildAt ( 1 ).getResult () );
  }


  public void applyLet ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    final Expression e = node.getExpression ();
    if ( e instanceof CurriedLet || e instanceof CurriedLetRec )
    {
      applyCurriedLet ( context, node );
      return;
    }

    if ( e instanceof MultiLet )
    {
      applyMultiLet ( context, node );
      return;
    }

    if ( e instanceof LetRec )
    {
      applyLetRec ( context, node );
      return;
    }

    final Let let = ( Let ) node.getExpression ();
    context.addProofNode ( node, new Closure ( let.getE1 (), node
        .getEnvironment () ) );
  }


  private void applyLetRec ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    final Expression e = node.getExpression ();
    LetRec letRec = ( LetRec ) e;
    context.addProofNode ( node, new Closure ( new Recursion ( letRec.getId (),
        null, ( ( Let ) e ).getE1 () ), node.getEnvironment () ) );
  }


  private void applyCurriedLet ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    // determine the first sub expression
    final Expression e = node.getExpression ();
    final CurriedLet curriedLet = ( CurriedLet ) e;
    Expression e1 = curriedLet.getE1 ();
    // generate the appropriate lambda abstractions
    final Identifier [] identifiers = curriedLet.getIdentifiers ();
    final MonoType [] types = curriedLet.getTypes ();
    for ( int n = identifiers.length - 1 ; n > 0 ; --n )
      e1 = new Lambda ( identifiers [ n ], types [ n ], e1 );

    // add the recursion for letrec
    if ( e instanceof CurriedLetRec )
      e1 = new Recursion ( identifiers [ 0 ], null, e1 );

    // TODO: is this right?
    context.addProofNode ( node, new Closure ( e1, node.getEnvironment () ) );
  }


  private void applyMultiLet ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    context.addProofNode ( node, new Closure ( ( ( MultiLet ) node
        .getExpression () ).getE1 (), node.getEnvironment () ) );
  }


  public void updateLet ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    final Expression e = node.getExpression ();
    if ( node.getChildCount () == 1 )
    {
      final BigStepClosureProofNode child0 = node.getChildAt ( 0 );
      if ( !child0.isProven () )
        return;

      if ( e instanceof MultiLet )
      {
        updateMultiLet1 ( context, node );
        return;
      }

      if ( e instanceof CurriedLet )
      {
        updateCurriedLet1 ( context, node );
        return;
      }

      final Let let = ( Let ) e;

      final Closure closure = child0.getResult ().getClosure ();
      final ClosureEnvironment environment = closure.cloneEnvironment ();
      environment.put ( let.getId (), closure );
      context.addProofNode ( node, new Closure ( let.getE2 (), environment ) );
      return;
    }

    if ( node.getChildCount () == 2 )
    {
      final BigStepClosureProofNode child1 = node.getChildAt ( 1 );
      if ( child1.isProven () )
        context.setProofNodeResult ( node, child1.getResult ().getClosure () );
    }
  }


  private void updateMultiLet1 ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    final MultiLet multiLet = ( MultiLet ) node.getExpression ();
    // Expression e2 = multiLet.getE2 ();

    final Closure closure = node.getChildAt ( 0 ).getResult ().getClosure ();
    final ClosureEnvironment environment = closure.cloneEnvironment ();
    // environment.put ( let.getId (), closure );
    // context.addProofNode ( node, new Closure ( let.getE2 (), environment ) );

    Identifier [] identifiers = multiLet.getIdentifiers ();
    for ( int n = 0 ; n < identifiers.length ; ++n )
    {
      // substitute: (#l_n value0) for id
      // TODO: what to do here?
      environment.put ( identifiers [ n ], closure );
      // e2 = e2.substitute ( identifiers [ n ], new Application (
      // new Projection ( identifiers.length, n + 1 ), value0 ) );
    }
    // add a proof node for e2
    context
        .addProofNode ( node, new Closure ( multiLet.getE2 (), environment ) );
    // context.addProofNode ( node, e2 );*/
  }


  private void updateCurriedLet1 ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    final BigStepClosureProofNode child0 = node.getChildAt ( 0 );
    final Expression value0 = child0.getResult ().getValue ();

    final CurriedLet curriedLet = ( CurriedLet ) node.getExpression ();

    final Closure closure = child0.getResult ().getClosure ();
    final ClosureEnvironment environment = closure.cloneEnvironment ();
    environment.put ( curriedLet.getIdentifiers () [ 0 ], closure );
    context.addProofNode ( node,
        new Closure ( curriedLet.getE2 (), environment ) );
  }


  public void applyAnd ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    context.addProofNode ( node, new Closure ( ( ( And ) node.getExpression () )
        .getE1 (), node.getEnvironment () ) );
  }


  public void updateAndT ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    if ( node.getChildCount () == 1 )
    {
      BigStepClosureProofNode child0 = node.getChildAt ( 0 );
      if ( !child0.isProven () )
        return;

      if ( ! ( ( BooleanConstant ) child0.getResult ().getValue () )
          .booleanValue () )
      {
        updateAndF ( context, node );
        return;
      }

      final And expr = ( And ) node.getExpression ();
      context.addProofNode ( node, new Closure ( expr.getE2 (), child0
          .getEnvironment () ) );

      return;
    }

    if ( node.getChildCount () == 2 )
    {
      BigStepClosureProofNode child1 = node.getChildAt ( 1 );
      if ( !child1.isProven () )
        return;

      context.setProofNodeResult ( node, child1.getResult ().getClosure () );
    }
  }


  public void updateAndF ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    if ( node.getChildCount () == 1 )
    {
      BigStepClosureProofNode child0 = node.getChildAt ( 0 );
      if ( !child0.isProven () )
        return;

      if ( ( ( BooleanConstant ) child0.getResult ().getValue () )
          .booleanValue () )
      {
        updateAndT ( context, node );
        return;
      }

      context.addProofNode ( node, new Closure ( new BooleanConstant ( false ),
          child0.getEnvironment () ) );
      return;
    }

    if ( node.getChildCount () == 2 )
    {
      BigStepClosureProofNode child1 = node.getChildAt ( 1 );
      if ( !child1.isProven () )
        return;

      context.setProofNodeResult ( node, child1.getResult ().getClosure () );
    }
  }


  public void applyOr ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    context.addProofNode ( node, new Closure ( ( ( Or ) node.getExpression () )
        .getE1 (), node.getEnvironment () ) );
  }


  public void updateOrT ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    if ( node.getChildCount () == 1 )
    {
      BigStepClosureProofNode child0 = node.getChildAt ( 0 );
      if ( !child0.isProven () )
        return;

      if ( ! ( ( BooleanConstant ) child0.getResult ().getValue () )
          .booleanValue () )
      {
        updateOrF ( context, node );
        return;
      }

      context.addProofNode ( node, new Closure ( new BooleanConstant ( true ),
          child0.getEnvironment () ) );

      return;
    }

    if ( node.getChildCount () == 2 )
    {
      BigStepClosureProofNode child1 = node.getChildAt ( 1 );
      if ( !child1.isProven () )
        return;

      context.setProofNodeResult ( node, child1.getResult ().getClosure () );
    }
  }


  public void updateOrF ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    if ( node.getChildCount () == 1 )
    {
      BigStepClosureProofNode child0 = node.getChildAt ( 0 );
      if ( !child0.isProven () )
        return;

      if ( ( ( BooleanConstant ) child0.getResult ().getValue () )
          .booleanValue () )
      {
        updateOrT ( context, node );
        return;
      }

      final Or expr = ( Or ) node.getExpression ();
      context.addProofNode ( node, new Closure ( expr.getE2 (), child0
          .getEnvironment () ) );
      return;
    }

    if ( node.getChildCount () == 2 )
    {
      BigStepClosureProofNode child1 = node.getChildAt ( 1 );
      if ( !child1.isProven () )
        return;

      context.setProofNodeResult ( node, child1.getResult ().getClosure () );
    }
  }
}
