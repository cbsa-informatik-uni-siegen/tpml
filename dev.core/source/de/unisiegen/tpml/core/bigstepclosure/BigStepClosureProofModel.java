package de.unisiegen.tpml.core.bigstepclosure;

import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofModel;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;

/**
 * TODO
 *
 */
public class BigStepClosureProofModel extends AbstractInterpreterProofModel
{
  public BigStepClosureProofModel( Expression expression,
      AbstractProofRuleSet pRuleSet)
  {
    super( new DefaultBigStepClosureProofNode (expression), pRuleSet);
  }
  
  public void setOverlap(int i)
  {
    
  }
  
  public void guess(ProofNode node)
  {
    
  }
  
  public void setPages(int i)
  {
    
  }
  
  public void prove(ProofRule node, ProofNode result)
  {
    
  }
  
  public LatexPackageList getLatexPackages()
  {
    return null;
  }
  
  public LatexCommandList getLatexCommands()
  {
    return null;
  }
  
  public LatexInstructionList getLatexInstructions()
  {
    return null;
  }
  
  public LatexStringBuilder toLatexStringBuilder(LatexStringBuilderFactory fac, int i)
  {
    return null;
  }
  
  public LatexString toLatexString()
  {
    return null;
  }
  
  public PrettyString toPrettyString()
  {
    return null;
  }
  
  public PrettyStringBuilder toPrettyStringBuilder(PrettyStringBuilderFactory fac)
  {
    return null;
  }
  //public toLatexStringBuilder()
}
