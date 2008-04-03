package de.unisiegen.tpml.core.environmentsemantics;
import de.unisiegen.tpml.core.util.AbstractEnvironment;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;

/**
 * TODO
 *
 */
public final class DefaultClosureEnvironment
  extends AbstractEnvironment<Identifier, Closure>
  implements ClosureEnvironment 
{
  public PrettyString toPrettyString()
  {
    return null; // FIXME
  }
 
  public PrettyStringBuilder toPrettyStringBuilder(PrettyStringBuilderFactory fac)
  {
    return null; // FIXME
  }
  
  public LatexString toLatexString()
  {
    return null; // FIXME
  }
  
  public LatexCommandList getLatexCommands()
  {
    return null; // FIXME
  }
  
  public LatexStringBuilder toLatexStringBuilder(LatexStringBuilderFactory fac, int i)
  {
    return null; // FIXME
  }
  
  public LatexPackageList getLatexPackages()
  {
    return null; // FIXME
  }
  
  public LatexInstructionList getLatexInstructions()
  {
    return null; // FIXME
  }
}
