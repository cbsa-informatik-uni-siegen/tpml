package de.unisiegen.tpml.core.languages.l1;


import de.unisiegen.tpml.core.ClosureEnvironment;
import de.unisiegen.tpml.core.DefaultClosureEnvironment;
import de.unisiegen.tpml.core.bigstepclosure.AbstractBigStepClosureProofRuleSet;
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofContext;
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofNode;
import de.unisiegen.tpml.core.bigstepclosure.BinaryOpArgs;
import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.BinaryOperatorException;
import de.unisiegen.tpml.core.expressions.BooleanConstant;
import de.unisiegen.tpml.core.expressions.Closure;
import de.unisiegen.tpml.core.expressions.Condition;
import de.unisiegen.tpml.core.expressions.Condition1;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.InfixOperation;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.expressions.UnaryOperator;
import de.unisiegen.tpml.core.expressions.UnaryOperatorException;
import de.unisiegen.tpml.core.expressions.UnitConstant;
import de.unisiegen.tpml.core.expressions.Value;


/**
 * TODO
 */
public final class L1BigStepClosureProofRuleSet extends
    AbstractBigStepClosureProofRuleSet
{

  public L1BigStepClosureProofRuleSet ( L1Language language )
  {
    super ( language );

    registerByMethodName ( L1Language.L1, "VAL", "applyVal" );
    registerByMethodName ( L1Language.L1, "ID", "applyId" );
    registerByMethodName ( L1Language.L1, "OP-1", "applyApp", "updateOP1" );
    registerByMethodName ( L1Language.L1, "OP-2", "applyOP2", "updateOP2" );
    registerByMethodName ( L1Language.L1, "BETA-V", "applyApp", "updateBetaV" );
    registerByMethodName ( L1Language.L1, "COND-T", "applyCond", "updateCondT" );
    registerByMethodName ( L1Language.L1, "COND-F", "applyCond", "updateCondF" );
    registerByMethodName ( L1Language.L1, "LET", "applyLet", "updateLet" );
  }


  public void applyVal ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    Expression expression = node.getExpression ();
    if ( expression instanceof Identifier )
      throw new RuntimeException ( "An Identifier is not a Value!" );
    Value val = ( Value ) expression;
    context.setProofNodeResult ( node, new Closure ( val, node
        .getEnvironment () ) );
  }


  public void applyId ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    Identifier id = ( Identifier ) node.getExpression ();
    ClosureEnvironment env = node.getEnvironment ();
    context.setProofNodeResult ( node, env.get ( id ) );
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

    InfixOperation io = ( InfixOperation ) e;
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
      final Expression parentExp = node.getParent ().getExpression();
      if(parentExp instanceof Application || parentExp instanceof InfixOperation)
      {
        context.setProofNodeResult ( node, new Closure (
          new Application ( child0.getExpression (), child1.getResult ()
              .getClosure ().getExpression () ), DefaultClosureEnvironment
              .empty () ) );
        return;
      }
    }
    catch(final Exception e)
    {
      e.printStackTrace();
    }
   
    final UnaryOperator uop = (UnaryOperator)child0.getExpression ();
    context.setProofNodeResult(
        node, new Closure(uop.applyTo(child1.getExpression()), DefaultClosureEnvironment.empty())
      );
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

    final BinaryOpArgs args = new BinaryOpArgs(node);

    context.setProofNodeResult ( node, new Closure ( args.getOperator().applyTo ( args.getOperand1(),
        args.getOperand2() ), DefaultClosureEnvironment.empty () ) );
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
  
  public void applyLet(
      BigStepClosureProofContext context, BigStepClosureProofNode node)
  {
    final Let let = (Let)node.getExpression ();
    context.addProofNode ( node, new Closure(let.getE1 (), node.getEnvironment() ));
  }
  
  public void updateLet(
      BigStepClosureProofContext context, BigStepClosureProofNode node)
  {
    final Let let = (Let)node.getExpression ();
    if(node.getChildCount() == 1)
    {
      final BigStepClosureProofNode child0 = node.getChildAt ( 0 );
      if(child0.isProven())
      {
        Closure closure = child0.getResult ().getClosure ();
        ClosureEnvironment environment = child0.getClosure ().cloneEnvironment();
        environment.put ( let.getId(), closure );
        context.addProofNode ( node, new Closure( let.getE2 (), environment ) );
      }
      return;
    }
    
    if(node.getChildCount() == 2)
    {
      final BigStepClosureProofNode child1 = node.getChildAt ( 1 );
      if(child1.isProven())
        context.setProofNodeResult ( node, child1.getResult().getClosure());
    }
  }
}
