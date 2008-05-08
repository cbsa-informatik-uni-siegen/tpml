package de.unisiegen.tpml.core.languages.l1;

import de.unisiegen.tpml.core.ClosureEnvironment;
import de.unisiegen.tpml.core.bigstepclosure.AbstractBigStepClosureProofRuleSet;
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofContext;
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofNode;
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofResult;
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofRule;
import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.BinaryOperator;
import de.unisiegen.tpml.core.expressions.BinaryOperatorException;
import de.unisiegen.tpml.core.expressions.BooleanConstant;
import de.unisiegen.tpml.core.expressions.Closure;
import de.unisiegen.tpml.core.expressions.Condition;
import de.unisiegen.tpml.core.expressions.Condition1;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.UnaryOperator;
import de.unisiegen.tpml.core.expressions.UnaryOperatorException;
import de.unisiegen.tpml.core.expressions.Value;

/**
 * TODO
 *
 */
public final class L1BigStepClosureProofRuleSet extends AbstractBigStepClosureProofRuleSet
{
  public L1BigStepClosureProofRuleSet(L1Language language)
  {
    super(language);
   
    registerByMethodName( L1Language.L1,
        "VAL", "applyVal");
    registerByMethodName( L1Language.L1,
        "ID", "applyId");
    registerByMethodName( L1Language.L1,
        "OP-1", "applyOP1", "updateOP1");
    registerByMethodName (L1Language.L1,
        "OP-2", "applyOP2", "updateOP2");
    registerByMethodName( L1Language.L1,
        "BETA-V", "applyBetaV", "updateBetaV");
    registerByMethodName( L1Language.L1,
        "COND-T", "applyCond", "updateCondT");
    registerByMethodName( L1Language.L1,
        "COND-F", "applyCond", "updateCondF");
  }
  
  public void applyVal(BigStepClosureProofContext context,
      BigStepClosureProofNode node)
  {
    Value val = (Value)node.getExpression();
    // TODO: do we have to add a new node here?
  }
  
  public void applyId(BigStepClosureProofContext context,
      BigStepClosureProofNode node)
  {
    Identifier id = (Identifier)node.getExpression ();
    ClosureEnvironment env = node.getEnvironment ();
    if(env.containsSymbol ( id ))
      throw new RuntimeException("Identifier not applicable!");
    context.setProofNodeResult ( node, new Closure(id, env) );
  }
  
  public void applyOP1(BigStepClosureProofContext context,
      BigStepClosureProofNode node)
  {
    Application app = (Application)node.getExpression ();
    context.addProofNode ( node, app.getE1() );
    context.addProofNode ( node, app.getE2() );
  }
  
  public void updateOP1(BigStepClosureProofContext context,
      BigStepClosureProofNode node) throws UnaryOperatorException
  {
    BigStepClosureProofNode child0 = node.getChildAt ( 0 ),
                            child1 = node.getChildAt ( 1 );
   
    if(!child0.isFinished() || !child1.isFinished())
      return;
    
    UnaryOperator op1 = (UnaryOperator)child0.getExpression ();
    Expression operand = child1.getExpression();
    
    // shouldn't we extract the identifier here?
   // context.setProofNodeResult ( node, op1.applyTo ( operand ), new ClosureEnvironment());
  }
  
  public void applyOP2(BigStepClosureProofContext context,
      BigStepClosureProofNode node)
  {
    applyOP1(context, node);
    //Application app = (Application)node.getExpression ();
    //context.addProofNode ( node, app.getE1() );
    //context.addProofNode ( node, app.getE2() );
  }
  
  public void updateOP2(BigStepClosureProofContext context,
      BigStepClosureProofNode node) throws BinaryOperatorException
  {
    BigStepClosureProofNode child0 = node.getChildAt ( 0 ),
                            child1 = node.getChildAt ( 1 );
    if(!child0.isFinished() || !child1.isFinished())
      return;
    
    BinaryOperator op2 = (BinaryOperator)child0.getExpression ();
    Expression operand1 = child0.getChildAt(0).getExpression (),
               operand2 = child1.getExpression ();
    
    context.setProofNodeResult ( node, op2.applyTo ( operand1, operand2 ) );
  }
  
  public void applyBetaV(BigStepClosureProofContext context,
      BigStepClosureProofNode node)
  {
    applyOP1(context, node);
    
  }
  
  public void updateBetaV(BigStepClosureProofContext context,
      BigStepClosureProofNode node)
  {
    
  }
 
  public void applyCond(BigStepClosureProofContext context,
      BigStepClosureProofNode node)
  {
    Expression e = node.getExpression ();
    if(!(e instanceof Condition)
    && !(e instanceof Condition1))
        throw new RuntimeException("applyCond can only be used with Condition or Condition1!");
    context.addProofNode(node, e);
  }
  
  public void updateCondT(BigStepClosureProofContext context,
      BigStepClosureProofNode node)
  {
    
  }
  
  public void updateCondF(BigStepClosureProofContext context,
      BigStepClosureProofNode node)
  {
    if (!(node.getChildCount () == 1 && node.getChildAt ( 0 ).isProven () ))
    {
      BigStepClosureProofNode child = node.getChildAt ( 1 );
      context.setProofNodeResult ( node, new Closure(
          child.getExpression (), child.getEnvironment() ));
      return;
    }
    
    BigStepClosureProofResult result0 = node.getChildAt ( 0 ).getResult ();
    try
    {
      BooleanConstant value0 = ( BooleanConstant ) result0.getValue ();
      if ( value0.booleanValue () )
      {
        context.setProofNodeRule ( node,
            ( BigStepClosureProofRule ) getRuleByName ( "COND-T" ) ); //$NON-NLS-1$
        updateCondT ( context, node );
      }
      else
      {
        
      }
    }
    catch(ClassCastException e)
    {
      
    }
  }
}
