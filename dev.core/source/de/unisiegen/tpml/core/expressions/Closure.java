package de.unisiegen.tpml.core.expressions;


import de.unisiegen.tpml.core.ClosureEnvironment;
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
 */
public class Closure implements PrettyPrintable, LatexPrintable
{

  public Closure ( Expression exp, ClosureEnvironment env )
  {
    this.exp = exp;
    this.env = env;
  }


  public Expression getExpression ()
  {
    return exp;
  }


  public ClosureEnvironment getEnvironment ()
  {
    return env;
  }


  public String toString ()
  {
    return exp.toString ();
  }


  private Expression exp;


  private ClosureEnvironment env;


  /**
   * TODO
   * 
   * @return
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable#toPrettyString()
   */
  public PrettyString toPrettyString ()
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance () )
    .toPrettyString ();
  }


  /**
   * TODO
   * 
   * @param prettyStringBuilderFactory
   * @return
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable#toPrettyStringBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory)
   */
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory prettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = prettyStringBuilderFactory.newBuilder (
        this, 0 );
    builder.addBuilder ( getExpression().toPrettyStringBuilder ( prettyStringBuilderFactory ), 0 );
    builder.addBuilder ( getEnvironment().toPrettyStringBuilder ( prettyStringBuilderFactory), 1 );
    return builder;
  }


  /**
   * TODO
   * 
   * @return
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#getLatexCommands()
   */
  public LatexCommandList getLatexCommands ()
  {
    return null;
  }


  /**
   * TODO
   * 
   * @return
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#getLatexInstructions()
   */
  public LatexInstructionList getLatexInstructions ()
  {
    return null;
  }


  /**
   * TODO
   * 
   * @return
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#getLatexPackages()
   */
  public LatexPackageList getLatexPackages ()
  {
    return null;
  }


  /**
   * TODO
   * 
   * @return
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#toLatexString()
   */
  public LatexString toLatexString ()
  {
    return null;
  }


  /**
   * TODO
   * 
   * @param latexStringBuilderFactory
   * @param indent
   * @return
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#toLatexStringBuilder(de.unisiegen.tpml.core.latex.LatexStringBuilderFactory,
   *      int)
   */
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory latexStringBuilderFactory, int indent )
  {
    return null;
  }
}
