package de.unisiegen.tpml.core.bigstepclosure;


import de.unisiegen.tpml.core.ClosureEnvironment;
import de.unisiegen.tpml.core.expressions.Closure;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.DefaultStore;
import de.unisiegen.tpml.core.interpreters.Store;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction;
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
public class BigStepClosureProofResult implements PrettyPrintable,
    LatexPrintable
{

  public BigStepClosureProofResult ( Store store, Closure closure )
  {
    this.store = store;
    this.closure = closure;
  }


  public Store getStore ()
  {
    return new DefaultStore ( ( DefaultStore ) this.store );
  }


  public Closure getClosure ()
  {
    return this.closure;
  }


  public Expression getValue ()
  {
    return this.getClosure ().getExpression ();
  }


  public ClosureEnvironment getEnvironment ()
  {
    return this.getClosure ().getEnvironment ();
  }


  public LatexStringBuilder toLatexStringBuilder (
      final LatexStringBuilderFactory pLatexStringBuilderFactory,
      final int pIndent )
  {
    StringBuilder body = new StringBuilder ();
    body.append ( this.closure.toPrettyString ().toString () );

    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0,
        LATEX_BIG_STEP_PROOF_RESULT, pIndent, this.toPrettyString ()
            .toString (), body.toString (), this.closure.toPrettyString ()
            .toString (), LATEX_NO_STORE );
    builder.addBuilderBegin ();
    builder.addBuilderWithoutBrackets ( this.closure.toLatexStringBuilder (
        pLatexStringBuilderFactory, pIndent + LATEX_INDENT ), 0 );
    builder.addEmptyBuilder ();
    builder.addBuilderEnd ();
    return builder;
  }


  public LatexInstructionList getLatexInstructions ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( getLatexInstructionsStatic () );
    instructions.add ( this.closure );
    return instructions;
  }


  public LatexString toLatexString ()
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance (), 0 )
        .toLatexString ();
  }


  public LatexCommandList getLatexCommands ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( getLatexCommandsStatic () );
    commands.add ( this.closure );
    return commands;
  }


  public LatexPackageList getLatexPackages ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( getLatexPackagesStatic () );
    packages.add ( this.closure );
    return packages;
  }


  public PrettyString toPrettyString ()
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance () )
        .toPrettyString ();
  }


  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory factory )
  {
    PrettyStringBuilder builder = factory.newBuilder ( this, 0 );
    builder.addBuilder ( this.closure.toPrettyStringBuilder ( factory ), 0 );

    return builder;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( new DefaultLatexCommand ( LATEX_BIG_STEP_PROOF_RESULT, 1,
        "#1", "body" ) );//$NON-NLS-1$ //$NON-NLS-2$ 
    return commands;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public static LatexInstructionList getLatexInstructionsStatic ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( new DefaultLatexInstruction ( "\\definecolor{" //$NON-NLS-1$
        + LATEX_COLOR_NONE + "}{rgb}{0.0,0.0,0.0}", //$NON-NLS-1$
        LATEX_COLOR_NONE + ": color of normal text" ) ); //$NON-NLS-1$
    return instructions;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public static LatexPackageList getLatexPackagesStatic ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( LatexPackage.COLOR );
    packages.add ( LatexPackage.IFTHEN );
    return packages;
  }


  private Store store;


  private Closure closure;
}
