package de.unisiegen.tpml.core;
import java.util.Enumeration;

import de.unisiegen.tpml.core.expressions.Closure;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.util.AbstractEnvironment;

/**
 * TODO
 *
 */
public final class DefaultClosureEnvironment
  extends AbstractEnvironment<Identifier, Closure>
  implements ClosureEnvironment 
{
  public void put(final Identifier identifier, final Closure closure)
  {
    System.err.println("put " + identifier.toString());
    super.put ( identifier, closure );
  }
  
  public Closure get(final Identifier identifier)
  {
    System.err.println("get " + identifier.toString());
    if(!super.containsSymbol ( identifier ))
      throw new RuntimeException(identifier.toString() + " not found!");
    return super.get ( identifier );
  }
  
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
  
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append ( '[' );
    Enumeration<Identifier> e = super.symbols ();
    while(e.hasMoreElements())
    {
      final Identifier id = e.nextElement();
      builder.append(id.toString());
      builder.append ( ": " );
      builder.append ( super.get ( id ).toString());
      builder.append ( ' ' );
    }
    builder.append ( ']' );
    return builder.toString();
  }
}
