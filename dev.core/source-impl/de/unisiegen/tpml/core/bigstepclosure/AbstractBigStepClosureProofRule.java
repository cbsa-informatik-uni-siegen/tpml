package de.unisiegen.tpml.core.bigstepclosure;


import java.awt.Color;
import java.lang.reflect.InvocationTargetException;

import de.unisiegen.tpml.core.AbstractProofRule;
import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException;
import de.unisiegen.tpml.core.exceptions.RowSubstitutionException;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexPackage;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.UnificationException;
import de.unisiegen.tpml.core.util.Theme;


/**
 * TODO
 */
public abstract class AbstractBigStepClosureProofRule extends AbstractProofRule
    implements BigStepClosureProofRule
{

  public AbstractBigStepClosureProofRule ( final int group, final String name )
  {
    super ( group, name );
  }


  public void apply ( BigStepClosureProofContext context,
      BigStepClosureProofNode node ) throws ProofRuleException
  {
    try
    {
      applyInternal ( context, node );
    }
    catch ( ProofRuleException e )
    {
      e.printStackTrace ();
      throw e;
    }
    catch ( InvocationTargetException e )
    {
      if ( ( e.getTargetException () instanceof NotOnlyFreeVariableException )
          || ( e.getTargetException () instanceof RowSubstitutionException ) )
      {
        throw new RuntimeException ( e.getTargetException ().getMessage () );
      }
      throw new ProofRuleException ( node, this, e.getTargetException () );
    }
    catch ( Exception e )
    {
      // check if e contains a usable error message
      for ( Throwable t = e ; t != null ; t = t.getCause () )
      {
        if ( t instanceof IllegalArgumentException
            && t instanceof UnificationException )
        {
          throw new ProofRuleException ( t.getMessage (), node, this, e );
        }
      }
      throw new ProofRuleException ( node, this, e );
    }
  }


  protected abstract void applyInternal ( BigStepClosureProofContext context,
      BigStepClosureProofNode node ) throws Exception;


  public void update ( BigStepClosureProofContext context,
      BigStepClosureProofNode node )
  {
    try
    {
      updateInternal ( context, node );
    }
    catch ( Exception e )
    {
      e.printStackTrace();
    }
  }


  protected abstract void updateInternal ( BigStepClosureProofContext context,
      BigStepClosureProofNode node ) throws Exception;


  BigStepClosureProofRule toExnRule ( int n )
  {
    if ( n < 0 )
    {
      throw new IllegalArgumentException ( "n is negative" ); //$NON-NLS-1$
    }
    return newNoopRule ( getName () + "-EXN-" + ( n + 1 ) ); //$NON-NLS-1$
  }


  public static BigStepClosureProofRule newNoopRule ( String name )
  {
    return new AbstractBigStepClosureProofRule ( -1, name )
    {

      @Override
      protected void applyInternal (
          @SuppressWarnings ( "unused" ) BigStepClosureProofContext context,
          @SuppressWarnings ( "unused" ) BigStepClosureProofNode node )
          throws Exception
      {
        throw new IllegalArgumentException ( "Cannot apply noop rules" ); //$NON-NLS-1$
      }


      @Override
      protected void updateInternal (
          @SuppressWarnings ( "unused" ) BigStepClosureProofContext context,
          @SuppressWarnings ( "unused" ) BigStepClosureProofNode node )
          throws Exception
      {
        // nothing to do here...
      }
    };
  }


  public LatexCommandList getLatexCommands ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( getLatexCommandsStatic () );
    return commands;
  }


  public static LatexCommandList getLatexCommandsStatic ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( new DefaultLatexCommand ( LATEX_BIG_STEP_CLOSURE_PROOF_RULE,
        1, "\\mbox{\\textbf{\\color{" + LATEX_COLOR_RULE + "}(#1)}}", //$NON-NLS-1$//$NON-NLS-2$
        "name" ) ); //$NON-NLS-1$
    return commands;
  }


  public LatexInstructionList getLatexInstructions ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( getLatexInstructionsStatic () );
    return instructions;
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


  public LatexPackageList getLatexPackages ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( getLatexPackagesStatic () );
    return packages;
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


  public final LatexString toLatexString ()
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance (), 0 )
        .toLatexString ();
  }


  public final LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory, int pIndent )
  {
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0,
        LATEX_BIG_STEP_CLOSURE_PROOF_RULE, pIndent, this.toPrettyString ()
            .toString () );
    builder.addText ( "{" + this.getName ().replaceAll ( "_", "\\\\_" ) + "}" ); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    return builder;
  }


  public final PrettyString toPrettyString ()
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance () )
        .toPrettyString ();
  }

  public final String toString ()
  {
    return toPrettyString ().toString ();
  }

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
}
