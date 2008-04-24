package de.unisiegen.tpml.core.bigstepclosure;

import de.unisiegen.tpml.core.ClosureEnvironment;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.DefaultStore;
import de.unisiegen.tpml.core.interpreters.Store;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexPrintable;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;


/**
 * TODO
 *
 */
public class BigStepClosureProofResult implements PrettyPrintable, LatexPrintable
{
  public BigStepClosureProofResult(Store store,
      Expression expression, ClosureEnvironment environment)
  {
    this.store = store;
    this.expression = expression;
    this.environment = environment;
  }
  
  public Store getStore ()
  {
    return new DefaultStore ( ( DefaultStore ) this.store );
  }
  
  public Expression getExpression()
  {
    return expression;
  }
  
  public ClosureEnvironment getEnvironment()
  {
    return environment;
  }
  
  public LatexStringBuilder toLatexStringBuilder(LatexStringBuilderFactory factory, int i)
  {
    return null;
  }
  
  public LatexInstructionList getLatexInstructions()
  {
    return null;
  }
  
  public LatexString toLatexString()
  {
    return null;
  }
  
  public LatexCommandList getLatexCommands()
  {
    return null;
  }
  
  public LatexPackageList getLatexPackages()
  {
    return null;
  }
  
  public PrettyString toPrettyString()
  {
    return null;
  }
  
  public PrettyStringBuilder toPrettyStringBuilder(PrettyStringBuilderFactory factory)
  {
    return null;
  }
  

  
  private Store store;
  private Expression expression;
  private ClosureEnvironment environment;
}
