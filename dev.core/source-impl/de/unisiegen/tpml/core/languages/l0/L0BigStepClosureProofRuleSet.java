package de.unisiegen.tpml.core.languages.l0;

import de.unisiegen.tpml.core.bigstepclosure.AbstractBigStepClosureProofRuleSet;
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofContext;
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofNode;


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
  
  public void applyId(BigStepClosureProofContext context, BigStepClosureProofNode node)
  {
    
  }
  
  public void updateId(BigStepClosureProofContext context, BigStepClosureProofNode node)
  {
    
  }
  
  public void applyApp(BigStepClosureProofContext context, BigStepClosureProofNode node)
  {
    
  }
  
  public void updateApp(BigStepClosureProofContext context, BigStepClosureProofNode node)
  {
    
  }
  
  public void applyBetaV(BigStepClosureProofContext context, BigStepClosureProofNode node)
  {
    
  }
  
  public void updateBetaV(BigStepClosureProofContext context, BigStepClosureProofNode node)
  {
    
  }
}