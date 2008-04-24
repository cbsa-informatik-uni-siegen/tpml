package de.unisiegen.tpml.core.languages.l0;

import de.unisiegen.tpml.core.bigstepclosure.AbstractBigStepClosureProofRuleSet;
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofContext;
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofNode;
import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;

/**
 * TODO
 *
 */
public class L0BigStepClosureProofRuleSet extends AbstractBigStepClosureProofRuleSet
{
  public L0BigStepClosureProofRuleSet(L0Language language)
  {
    super(language);
   
    //registerByMethodName( L0Language.L0,
    //    "VAL", "applyVal", "updateVal");
    registerByMethodName( L0Language.L0,
        "ID", "applyId", "updateId");
    registerByMethodName( L0Language.L0,
        "APP", "applyApp", "updateApp");
    registerByMethodName( L0Language.L0,
        "BETA-V", "applyBetaV", "updateBetaV");
  }
  
  public void applyId(BigStepClosureProofContext context,
      BigStepClosureProofNode node)
  {
    Expression e = node.getExpression ();
    context.addProofNode ( node, e );
  }
  
  public void updateId(BigStepClosureProofContext context,
      BigStepClosureProofNode node)
  {
    Expression e = node.getExpression ();
    if((e instanceof Identifier)
        && (node.getChildCount() == 0
            || (node.getChildCount() == 1
                && node.getChildAt(0).isProven())))
    {
      Identifier id = (Identifier)e;
      node.getExpression ().substitute (id, node.getEnvironment ().get(id).getExpression() );
    }
  }
  
  public void applyApp(BigStepClosureProofContext context,
      BigStepClosureProofNode node)
  {
    Expression e = node.getExpression ();
    if (e instanceof Application)
    {
      Application application = ( Application ) e;
      context.addProofNode ( node, application.getE1 () );
    }
  }
  
  public void updateApp(BigStepClosureProofContext context,
      BigStepClosureProofNode node)
  {
    Expression e = node.getExpression ();
    if(e instanceof Application)
    {
      if(node.getChildCount() == 1 && node.getChildAt ( 0 ).isProven())
      {
        
      }
    }
  }
  
  public void applyBetaV(BigStepClosureProofContext context,
      BigStepClosureProofNode node)
  {
    
  }
  
  public void updateBetaV(BigStepClosureProofContext context,
      BigStepClosureProofNode node)
  {
    
  }
}