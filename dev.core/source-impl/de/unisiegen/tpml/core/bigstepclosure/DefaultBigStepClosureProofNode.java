package de.unisiegen.tpml.core.bigstepclosure;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofNode;
import de.unisiegen.tpml.core.interpreters.DefaultStore;
import de.unisiegen.tpml.core.interpreters.Store;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;

/**
 * TODO
 *
 */
public class DefaultBigStepClosureProofNode extends AbstractInterpreterProofNode
    implements BigStepClosureProofNode
{
  public DefaultBigStepClosureProofNode ( Expression pExpression )
  {
    this ( pExpression, new DefaultStore () );
  }
  
  public DefaultBigStepClosureProofNode(Expression pExpression, Store store)
  {
    super(pExpression, store);
  }
  
  public boolean isProven()
  {
    return false;
  }
  
  public PrettyString toPrettyString()
  {
    return null;
  }
  
  public PrettyStringBuilder toPrettyStringBuilder(PrettyStringBuilderFactory fac)
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
  
  public LatexCommandList getLatexCommands()
  {
    return null;
  }
  
  public LatexString toLatexString()
  {
    return null;
  }
  
  public LatexPackageList getLatexPackages()
  {
    return null;
  }
}
