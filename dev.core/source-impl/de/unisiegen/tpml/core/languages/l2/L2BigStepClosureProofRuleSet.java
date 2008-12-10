package de.unisiegen.tpml.core.languages.l2;


import de.unisiegen.tpml.core.ClosureEnvironment;
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofContext;
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofNode;
import de.unisiegen.tpml.core.expressions.Closure;
import de.unisiegen.tpml.core.expressions.Recursion;
import de.unisiegen.tpml.core.languages.l1.L1BigStepClosureProofRuleSet;


/**
 * TODO
 */
public class L2BigStepClosureProofRuleSet extends L1BigStepClosureProofRuleSet
{

  public L2BigStepClosureProofRuleSet ( L2Language language )
  {
    super ( language );
    // register the big step rules (order is important for guessing!)
    registerByMethodName ( L2Language.L2, "UNFOLD", "applyUnfold", //$NON-NLS-1$//$NON-NLS-2$
        "updateUnfold" ); //$NON-NLS-1$
  }


  public void applyUnfold ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
      final Recursion e = (Recursion)node.getExpression ();
      final ClosureEnvironment env = context.cloneEnvironment(node.getClosure ().getEnvironment());
      env.put ( e.getId(), node.getClosure());
      context.addProofNode ( node, new Closure(e.getE(), env ));
  }
  
  public void updateUnfold(BigStepClosureProofContext context,
      BigStepClosureProofNode node)
  {
    final BigStepClosureProofNode child0 = node.getChildAt ( 0 );
    if(child0.isProven())
      context.setProofNodeResult ( node, child0.getResult());
  }
}
