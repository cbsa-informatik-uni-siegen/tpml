package de.unisiegen.tpml.core.bigstepclosure;

import de.unisiegen.tpml.core.AbstractProofRule;
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
public abstract class AbstractBigStepClosureProofRule extends AbstractProofRule
implements BigStepClosureProofRule
{
  public AbstractBigStepClosureProofRule(final int group, final String name)
  {
    super (group, name);
  }
  
  public void apply(BigStepClosureProofContext context, BigStepClosureProofNode node)
  {
    try
    {
      applyInternal(context, node);
    }
    catch(Exception e)
    {
      // TODO
    }
  }
  
  protected abstract void applyInternal ( BigStepClosureProofContext context,
      BigStepClosureProofNode node ) throws Exception;
  
  
  public void update ( BigStepClosureProofContext context, BigStepClosureProofNode node )
  {
    try
    {
      updateInternal(context, node);
    }
    catch(Exception e)
    {
      // TODO
    }
  }
  
  protected abstract void updateInternal ( BigStepClosureProofContext context,
      BigStepClosureProofNode node ) throws Exception;
  
  public LatexCommandList getLatexCommands ()
  {
    return null;
  }
  
  public LatexInstructionList getLatexInstructions ()
  {
    return null;
  }
  
  public LatexPackageList getLatexPackages ()
  {
    return null;
  }
  
  public final LatexString toLatexString ()
  {
    return null;
  }
  
  public final LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory, int pIndent )
  {
    return null;
  }
  
  public final PrettyString toPrettyString ()
  {
    return null;
  }
  
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    return null;
  }
}
