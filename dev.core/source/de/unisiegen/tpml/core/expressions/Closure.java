package de.unisiegen.tpml.core.expressions;


import de.unisiegen.tpml.core.ClosureEnvironment;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexPackage;
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

  public Closure ( final Expression exp, final ClosureEnvironment env )
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


  public ClosureEnvironment cloneEnvironment ()
  {
    return ( ClosureEnvironment ) getEnvironment ().clone ();
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
    PrettyStringBuilder builder = prettyStringBuilderFactory.newBuilder ( this,
        0 );
    builder.addText(PRETTY_LPAREN);
    builder.addBuilder ( getExpression ().toPrettyStringBuilder (
        prettyStringBuilderFactory ), 0 );
    builder.addText(PRETTY_COMMA);
    builder.addBuilder ( getEnvironment ().toPrettyStringBuilder (
        prettyStringBuilderFactory ), 0 );
    builder.addText(PRETTY_RPAREN);
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
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( getLatexCommandsStatic () );
    commands.add ( this.getExpression () );
    commands.add ( this.getEnvironment () );
    return commands;
  }


  /**
   * TODO
   * 
   * @return
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#getLatexInstructions()
   */
  public LatexInstructionList getLatexInstructions ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( getLatexInstructionsStatic () );
    instructions.add ( this.getExpression () );
    instructions.add ( this.getEnvironment () );
    return instructions;
  }


  /**
   * TODO
   * 
   * @return
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#getLatexPackages()
   */
  public LatexPackageList getLatexPackages ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( getLatexPackagesStatic () );
    packages.add ( this.getExpression () );
    packages.add ( this.getEnvironment () );
    return packages;
  }


  /**
   * TODO
   * 
   * @return
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#toLatexString()
   */
  public LatexString toLatexString ()
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance (), 0 )
        .toLatexString ();
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
      final LatexStringBuilderFactory pLatexStringBuilderFactory,
      final int pIndent )
  {
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0,
        LATEX_CLOSURE, pIndent, this.toPrettyString ().toString () );
    builder.addBuilder ( this.getExpression ().toLatexStringBuilder (
        pLatexStringBuilderFactory, pIndent + LATEX_INDENT ), 0 );
    builder.addBuilder ( this.getEnvironment ().toLatexStringBuilder (
        pLatexStringBuilderFactory, pIndent + LATEX_INDENT ), 0 );
    return builder;
  }


  public static LatexCommandList getLatexCommandsStatic ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( new DefaultLatexCommand ( LATEX_CLOSURE, 2, LATEX_LPAREN
        + "#1" + LATEX_SPACE + "#2" + LATEX_RPAREN, "expression", "environment" ));
    return commands;
  }


  public static LatexInstructionList getLatexInstructionsStatic ()
  {
    // FIXME
    LatexInstructionList instructions = new LatexInstructionList();
    return instructions;
  }


  public static LatexPackageList getLatexPackagesStatic ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( LatexPackage.AMSMATH );
    packages.add ( LatexPackage.AMSTEXT );
    packages.add ( LatexPackage.COLOR );
    packages.add ( LatexPackage.IFTHEN );
    packages.add ( LatexPackage.PSTNODE );
    packages.add ( LatexPackage.PSTRICKS );
    return packages;
  }
}
