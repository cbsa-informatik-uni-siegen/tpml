package de.unisiegen.tpml.core.languages.l1;

import de.unisiegen.tpml.core.ClosureEnvironment;
import de.unisiegen.tpml.core.bigstepclosure.AbstractBigStepClosureProofRuleSet;
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofContext;
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofNode;
import de.unisiegen.tpml.core.expressions.Closure;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.Value;

/**
 * TODO
 *
 */
public class L1BigStepClosureProofRuleSet extends AbstractBigStepClosureProofRuleSet
{
  public L1BigStepClosureProofRuleSet(L1Language language)
  {
    super(language);
   
    registerByMethodName( L1Language.L1,
        "VAL", "applyVal");
    registerByMethodName( L1Language.L1,
        "ID", "applyId");
    registerByMethodName( L1Language.L1,
        "BETA-V", "applyBetaV", "updateBetaV");
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
    context.addProofNode ( node, new Closure(id, env ));
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
