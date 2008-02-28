package de.unisiegen.tpml.core.smallstep;


import java.awt.Color;

import de.unisiegen.tpml.core.AbstractProofRule;
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
import de.unisiegen.tpml.core.util.Theme;


/**
 * Default implementation of the <code>SmallStepProofRule</code> interface.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev$
 * @see de.unisiegen.tpml.core.AbstractProofRule
 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofRule
 */
public final class DefaultSmallStepProofRule extends AbstractProofRule
    implements SmallStepProofRule
{

  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( new DefaultLatexCommand ( LATEX_SMALL_STEP_PROOF_RULE, 1,
        "\\mbox{\\textbf{\\color{" + LATEX_COLOR_RULE + "}(#1)}}", //$NON-NLS-1$//$NON-NLS-2$
        "name" ) ); //$NON-NLS-1$
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
    Color colorRule = Theme.currentTheme ().getRuleColor ();
    float red = ( float ) Math
        .round ( ( ( float ) colorRule.getRed () ) / 255 * 100 ) / 100;
    float green = ( float ) Math
        .round ( ( ( float ) colorRule.getGreen () ) / 255 * 100 ) / 100;
    float blue = ( float ) Math
        .round ( ( ( float ) colorRule.getBlue () ) / 255 * 100 ) / 100;
    instructions.add ( new DefaultLatexInstruction (
        "\\definecolor{" + LATEX_COLOR_RULE + "}{rgb}{" //$NON-NLS-1$ //$NON-NLS-2$
            + red + "," //$NON-NLS-1$
            + green + "," //$NON-NLS-1$
            + blue + "}", LATEX_COLOR_RULE + ": color of proof rules" ) ); //$NON-NLS-1$ //$NON-NLS-2$
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
    return packages;
  }


  /**
   * <code>true</code> if this small step proof rule is an axiom, and as such,
   * has no premises. The opposite is a meta rule, which has exactly one
   * premise.
   * 
   * @see #isAxiom()
   */
  private boolean axiom;


  /**
   * Allocates a new <code>DefaultSmallStepProofRule</code> with the specified
   * <code>name</code>. If <code>axiom</code> is <code>true</code>, the
   * new rule has no premises, otherwise it has exactly one premise.
   * 
   * @param group the group id of the small step rule, see the description of
   *          the {@link AbstractProofRule#getGroup()} method for details.
   * @param name the name of the rule.
   * @param pAxiom <code>true</code> if the rule has no premises.
   * @throws NullPointerException if <code>name</code> is <code>null</code>.
   * @see #isAxiom()
   */
  public DefaultSmallStepProofRule ( int group, String name, boolean pAxiom )
  {
    super ( group, name );
    this.axiom = pAxiom;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public LatexCommandList getLatexCommands ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( getLatexCommandsStatic () );
    return commands;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public LatexInstructionList getLatexInstructions ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( getLatexInstructionsStatic () );
    return instructions;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public LatexPackageList getLatexPackages ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( getLatexPackagesStatic () );
    return packages;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofRule#isAxiom()
   */
  public boolean isAxiom ()
  {
    return this.axiom;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofRule#toExnRule()
   */
  public SmallStepProofRule toExnRule ()
  {
    if ( !isAxiom () )
    {
      return new DefaultSmallStepProofRule ( getGroup (),
          getName () + "-EXN", false ); //$NON-NLS-1$
    }
    return this;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexString()
   */
  public final LatexString toLatexString ()
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance (), 0 )
        .toLatexString ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  public final LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory, int pIndent )
  {
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0,
        LATEX_SMALL_STEP_PROOF_RULE, pIndent, this.toPrettyString ()
            .toString () );
    builder.addText ( "{" + this.getName ().replaceAll ( "_", "\\\\_" ) + "}" ); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    return builder;
  }


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyString()
   */
  public final PrettyString toPrettyString ()
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance () )
        .toPrettyString ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this, 0 );
    builder.addText ( PRETTY_LPAREN );
    builder.addText ( this.getName () );
    builder.addText ( PRETTY_RPAREN );
    return builder;
  }


  /**
   * Returns the string representation for this proof rule. This method is
   * mainly used for debugging.
   * 
   * @return The pretty printed string representation for this proof rule.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @Override
  public final String toString ()
  {
    return toPrettyString ().toString ();
  }
}
