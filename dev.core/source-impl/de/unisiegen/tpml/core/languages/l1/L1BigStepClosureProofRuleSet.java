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
 * BigStep Closure proof rules for L1 and derived languages
 * 
 * @author Philipp Reh
 */
public class L1BigStepClosureProofRuleSet extends
    AbstractBigStepClosureProofRuleSet
{

  /**
   * Allocates a new <code>L1BigStepClosureProofRuleSet</code> with the
   * specified <code>language</code>, which is the <b>L1</b> or a derived
   * language.
   * 
   * @param language the language for the proof rule set
   */
  public L1BigStepClosureProofRuleSet ( final L1Language language )
  {
    super ( language );

    registerByMethodName ( L1Language.L1, "VAL", "applyVal" ); //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1, "ID", "applyId", "updateId" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    registerByMethodName ( L1Language.L1, "OP-1", "applyApp", "updateOP1" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    registerByMethodName ( L1Language.L1, "OP-2", "applyOP2", "updateOP2" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    registerByMethodName ( L1Language.L1, "BETA-V", "applyApp", "updateBetaV" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    registerByMethodName ( L1Language.L1, "COND-TRUE", "applyCond", //$NON-NLS-1$ //$NON-NLS-2$
        "updateCondT" ); //$NON-NLS-1$
    registerByMethodName ( L1Language.L1, "COND-FALSE", "applyCond", //$NON-NLS-1$ //$NON-NLS-2$
        "updateCondF" ); //$NON-NLS-1$
    registerByMethodName ( L1Language.L1, "LET", "applyLet", "updateLet" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    registerByMethodName ( L1Language.L1, "AND-TRUE", "applyAnd", "updateAndT" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    registerByMethodName ( L1Language.L1, "AND-FALSE", "applyAnd", "updateAndF" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    registerByMethodName ( L1Language.L1, "OR-TRUE", "applyOr", "updateOrT" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    registerByMethodName ( L1Language.L1, "OR-FALSE", "applyOr", "updateOrF" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }


  /**
   * Applies the rule (VAL) to a closure
   * 
   * @param context
   * @param node
   */
  public void applyVal ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node )
  {
    final Value val = ( Value ) node.getExpression ();
    context.setProofNodeResult ( node, new Closure ( val, node
        .getEnvironment () ) );
  }


  /**
   * Applies the rule (ID) to a closure Creates a new proof node with the
   * expression found for id in the closure
   * 
   * @param context
   * @param node
   */
  public void applyId ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node )
  {
    final Identifier id = ( Identifier ) node.getExpression ();
    final ClosureEnvironment env = node.getEnvironment ();
    context.addProofNode ( node, env.get ( id ) );
  }


  /**
   * Checks whether the first child of the (ID) rule is already a value
   * 
   * @param context
   * @param node
   */
  public void updateId ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node )
  {
    final BigStepClosureProofNode child0 = node.getChildAt ( 0 );
    if ( child0.isProven () )
      context.setProofNodeResult ( node, child0.getResult () );
  }


  /**
   * Applies a general application. It is used for (OP-1), (OP-2) (in case of a
   * prefix operator) and (BETA-V).
   * 
   * @param context
   * @param node
   */
  public void applyApp ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node )
  {
    Application app = ( Application ) node.getExpression ();
    context.addProofNode ( node, new Closure ( app.getE1 (), node
        .getEnvironment () ) );
    context.addProofNode ( node, new Closure ( app.getE2 (), node
        .getEnvironment () ) );
  }


  /**
   * Applies the rule (OP-2) to a closure. Uses special handling for an
   * InfixOperation.
   * 
   * @param context
   * @param node
   */
  public void applyOP2 ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node )
  {
    final Expression e = node.getExpression ();
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


  /**
   * Checks if both children an (OP-1) are already proven. Defers the evaluation
   * if the node's parent is an (OP-2) because the operator's result cannot be
   * calculated by (OP-1) in this case.
   * 
   * @param context
   * @param node
   * @throws UnaryOperatorException
   */
  public void updateOP1 ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node ) throws UnaryOperatorException
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


  /**
   * Cheks if both children if an (OP-2) are already proven. If so it simply
   * calculates the result.
   * 
   * @param context
   * @param node
   * @throws BinaryOperatorException
   */
  public void updateOP2 ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node ) throws BinaryOperatorException
  {
    if ( node.getChildCount () < 2 )
      return;

    final BigStepClosureProofNode child0 = node.getChildAt ( 0 ), child1 = node
        .getChildAt ( 1 );

    if ( ! ( child0.isProven () && child1.isProven () ) )
      return;

    final BinaryOpArgs args = new BinaryOpArgs ( node );

    context.setProofNodeResult ( node, new Closure ( args.getOperator ()
        .applyTo ( args.getOperand1 (), args.getOperand2 () ),
        DefaultClosureEnvironment.empty () ) );
  }


  /**
   * Tries to update (BETA-V). If the first two children are proven, the third
   * child is created which inserts the identifier of the lambda into the
   * closure of child1, which is then used to proof the final child.
   * 
   * @param context
   * @param node
   */
  public void updateBetaV ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node )
  {
    if ( node.getChildCount () < 2 )
      return;

    BigStepClosureProofNode child0 = node.getFirstChild (), child1 = node
        .getChildAt ( 1 );
    if ( node.getChildCount () == 2 && child0.isProven () && child1.isProven () )
    {
      final Closure result0 = child0.getResult ().getClosure ();
      final Lambda lambda = ( Lambda ) result0.getExpression ();
      final Closure closure = child1.getResult ().getClosure ();
      final ClosureEnvironment environment = result0.cloneEnvironment ();
      environment.put ( lambda.getId (), closure );
      context.addProofNode ( node, new Closure ( lambda.getE (), environment ) );
    }
    else if ( node.getChildCount () == 3 )
    {
      final BigStepClosureProofNode child2 = node.getChildAt ( 2 );
      if ( child2.isProven () )
        context.setProofNodeResult ( node, child2.getResult ().getClosure () );
    }
  }


  /**
   * Applies rule (COND-TRUE) or (COND-FALSE) to a closure. Can also be used if
   * the 'else part' is missing.
   * 
   * @param context
   * @param node
   */
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
          "applyCond can only be used with Condition or Condition1!" ); //$NON-NLS-1$
  }


  /**
   * Tries to update (COND-TRUE) If the result is actually false, it will call
   * delegate to updateCondF.
   * 
   * @param context
   * @param node
   */
  public void updateCondT ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node )
  {
    if ( node.getChildCount () == 1 && node.getChildAt ( 0 ).isProven () )
    {
      final BigStepClosureProofNode child0 = node.getChildAt ( 0 );
      if ( ! ( ( BooleanConstant ) child0.getResult ().getValue () )
          .booleanValue () )
      {
        updateCondF ( context, node );
        return;
      }

      final Expression expression = node.getExpression ();
      context.addProofNode ( node, new Closure (
          expression instanceof Condition ? ( ( Condition ) expression )
              .getE1 () : ( ( Condition1 ) expression ).getE1 (), node
              .getEnvironment () ) );
    }
    else if ( node.getChildCount () == 2 && node.getChildAt ( 1 ).isProven () )
      context.setProofNodeResult ( node, node.getChildAt ( 1 ).getResult () );
  }


  /**
   * Tries to update (COND-FALSE) If the result is actually true, it will call
   * delegate to updateCondT.
   * 
   * @param context
   * @param node
   */
  public void updateCondF ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node )
  {
    if ( node.getChildCount () == 1 && node.getChildAt ( 0 ).isProven () )
    {
      final BigStepClosureProofNode child0 = node.getChildAt ( 0 );
      if ( ( ( BooleanConstant ) child0.getResult ().getValue () )
          .booleanValue () )
      {
        updateCondT ( context, node );
        return;
      }

      final Expression expression = node.getExpression ();
      context.addProofNode ( node, new Closure (
          expression instanceof Condition ? ( ( Condition ) expression )
              .getE2 () : new UnitConstant (), node.getEnvironment () ) );
    }
    else if ( node.getChildCount () == 2 && node.getChildAt ( 1 ).isProven () )
      context.setProofNodeResult ( node, node.getChildAt ( 1 ).getResult () );
  }


  /**
   * Applies the rule (LET) to a closure. It only handles a normal Let
   * expression itself. CurriedLet, CurriedLetRec, MultiLet and LetRec are
   * delegated to other methods.
   * 
   * @param context
   * @param node
   */
  public void applyLet ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node )
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


  /**
   * Applies (LET) with a LetRec expression to a closure.
   * 
   * @param context
   * @param node
   */
  private void applyLetRec ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node )
  {
    final Expression e = node.getExpression ();
    final LetRec letRec = ( LetRec ) e;
    context.addProofNode ( node, new Closure ( new Recursion ( letRec.getId (),
        null, ( ( Let ) e ).getE1 () ), node.getEnvironment () ) );
  }


  /**
   * Applies (LET) with a CurriedLet or a CurriedLetRec expression to a closure.
   * 
   * @param context
   * @param node
   */
  private void applyCurriedLet ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node )
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


  /**
   * Applies (LET) with a MultiLet expression to a closure.
   * 
   * @param context
   * @param node
   */
  private void applyMultiLet ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node )
  {
    context.addProofNode ( node, new Closure ( ( ( MultiLet ) node
        .getExpression () ).getE1 (), node.getEnvironment () ) );
  }


  /**
   * Tries to update a (LET) node. If the first child is proven, it puts the
   * identifier into the result of the first child with which the second child
   * will be tried to be proven then. Delegates MultiLet, CurriedLet and
   * CurriedLetRec to other methods.
   * 
   * @param context
   * @param node
   */
  public void updateLet ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node )
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


  /**
   * Updates a (LET) from a MultiLet expression whose first child is proven.
   * 
   * @param context
   * @param node
   */
  private void updateMultiLet1 ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node )
  {
    final MultiLet multiLet = ( MultiLet ) node.getExpression ();
    // Expression e2 = multiLet.getE2 ();

    final Closure closure = node.getChildAt ( 0 ).getResult ().getClosure ();
    final ClosureEnvironment environment = closure.cloneEnvironment ();

    final Identifier [] identifiers = multiLet.getIdentifiers ();
    for ( int n = 0 ; n < identifiers.length ; ++n )
    {
      // substitute: (#l_n value0) for id
      // TODO: what to do here?
      environment.put ( identifiers [ n ], new Closure ( new Application (
          new Projection ( identifiers.length, n + 1 ), closure
              .getExpression () ), closure.getEnvironment () ) );
      // e2 = e2.substitute ( identifiers [ n ], new Application (
      // new Projection ( identifiers.length, n + 1 ), value0 ) );
    }
    // add a proof node for e2
    context
        .addProofNode ( node, new Closure ( multiLet.getE2 (), environment ) );
    // context.addProofNode ( node, e2 );*/
  }


  /**
   * Updates a (LET) from a CurriedLet or CurriedLetRect expression whose first
   * child is already proven.
   * 
   * @param context
   * @param node
   */
  private void updateCurriedLet1 ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node )
  {
    final BigStepClosureProofNode child0 = node.getChildAt ( 0 );

    final CurriedLet curriedLet = ( CurriedLet ) node.getExpression ();

    final Closure closure = child0.getResult ().getClosure ();
    final ClosureEnvironment environment = closure.cloneEnvironment ();
    environment.put ( curriedLet.getIdentifiers () [ 0 ], closure );
    context.addProofNode ( node,
        new Closure ( curriedLet.getE2 (), environment ) );
  }


  /**
   * Applies the rule (AND-TRUE) or (AND-FALSE) to a closure.
   * 
   * @param context
   * @param node
   */
  public void applyAnd ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node )
  {
    context.addProofNode ( node, new Closure ( ( ( And ) node.getExpression () )
        .getE1 (), node.getEnvironment () ) );
  }


  /**
   * Tries to update (AND-TRUE). If the first child's result is actually false
   * it delegates to updateAndF.
   * 
   * @param context
   * @param node
   */
  public void updateAndT ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node )
  {
    if ( node.getChildCount () == 1 )
    {
      final BigStepClosureProofNode child0 = node.getChildAt ( 0 );
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
      final BigStepClosureProofNode child1 = node.getChildAt ( 1 );
      if ( !child1.isProven () )
        return;

      context.setProofNodeResult ( node, child1.getResult ().getClosure () );
    }
  }


  /**
   * Tries to update (AND-FALSE). If the first child's result is actually true
   * it delegates to updateAndT.
   * 
   * @param context
   * @param node
   */
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


  /**
   * Applies the rule (OR-TRUE) or (OR-FALSE) to a closure.
   * 
   * @param context
   * @param node
   */
  public void applyOr ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node )
  {
    context.addProofNode ( node, new Closure ( ( ( Or ) node.getExpression () )
        .getE1 (), node.getEnvironment () ) );
  }


  /**
   * Tries to update (OR-TRUE). If the first child's result is actually false it
   * delegates to updateOrF.
   * 
   * @param context
   * @param node
   */
  public void updateOrT ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node )
  {
    if ( node.getChildCount () == 1 )
    {
      final BigStepClosureProofNode child0 = node.getChildAt ( 0 );
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
      final BigStepClosureProofNode child1 = node.getChildAt ( 1 );
      if ( !child1.isProven () )
        return;

      context.setProofNodeResult ( node, child1.getResult ().getClosure () );
    }
  }


  /**
   * Tries to update (OR-FALSE). If the first child's result is actually true it
   * delegates to updateOrT.
   * 
   * @param context
   * @param node
   */
  public void updateOrF ( final BigStepClosureProofContext context,
      final BigStepClosureProofNode node )
  {
    if ( node.getChildCount () == 1 )
    {
      final BigStepClosureProofNode child0 = node.getChildAt ( 0 );
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
      final BigStepClosureProofNode child1 = node.getChildAt ( 1 );
      if ( !child1.isProven () )
        return;

      context.setProofNodeResult ( node, child1.getResult ().getClosure () );
    }
  }
}
