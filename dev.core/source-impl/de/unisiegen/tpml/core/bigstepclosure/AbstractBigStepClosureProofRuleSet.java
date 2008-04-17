package de.unisiegen.tpml.core.bigstepclosure;

import java.lang.reflect.Method;

import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.bigstep.BigStepProofContext;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode;
import de.unisiegen.tpml.core.languages.Language;


/**
 * TODO
 *
 */
public class AbstractBigStepClosureProofRuleSet extends AbstractProofRuleSet
{
  public AbstractBigStepClosureProofRuleSet(Language language)
  {
    super(language);
  }
  
  protected void register( final int group, final String name, final Method applyMethod,
      final Method updateMethod)
  {
    register (new AbstractBigStepClosureProofRule (group, name)
    {
      protected void applyInternal( BigStepClosureProofContext context,
          BigStepClosureProofNode node) throws Exception
      {
        applyMethod.invoke ( AbstractBigStepClosureProofRuleSet.this, context, node );
      }
      
      protected void updateInternal(BigStepClosureProofContext context,
          BigStepClosureProofNode node) throws Exception
      {
        updateMethod.invoke(AbstractBigStepClosureProofRuleSet.this, context, node);
      }
    });
  }
}
