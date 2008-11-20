package de.unisiegen.tpml.core.unify;


import de.unisiegen.tpml.core.UnifyProofStep;
import de.unisiegen.tpml.core.entities.TypeEquation;
import de.unisiegen.tpml.core.entities.TypeEquationList;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction;
import de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexPackage;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexPrintable;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList;


/**
 * this node is needed for the unification algorithm to get one most common
 * solution for a given type equation
 * 
 * @author Christian Uhrhan
 * @version $Id$
 * @see de.unisiegen.tpml.core.unify.AbstractUnifyProofNode
 */
public class DefaultUnifyProofNode extends AbstractUnifyProofNode
{

  /**
   * Allocates a new <code>DefaultUnifyProofNode</code>
   * 
   * @param substs a list of already collected type substitutions
   * @param eqns a list of type equations
   */
  public DefaultUnifyProofNode ( TypeSubstitutionList substs,
      TypeEquationList eqns )
  {
    super ( substs, eqns );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable#toPrettyString()
   */
  public PrettyString toPrettyString ()
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance () )
        .toPrettyString ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable#toPrettyStringBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory)
   */
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory prettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = prettyStringBuilderFactory.newBuilder ( this,
        0 );
    builder.addBuilder ( getTypeSubstitutions ().toPrettyStringBuilder (
        prettyStringBuilderFactory ), 0 );
    builder.addBuilder ( getTypeEquationList ().toPrettyStringBuilder (
        prettyStringBuilderFactory ), 1 );
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
    commands.add ( new DefaultLatexCommand ( LATEX_UNIFY_PROOF_NODE, 1,
        "#1", "body" ) ); //$NON-NLS-1$//$NON-NLS-2$
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
    packages.add ( LatexPackage.AMSSYMB );
    packages.add ( LatexPackage.COLOR );
    return packages;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#getLatexCommands()
   */
  public LatexCommandList getLatexCommands ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( getLatexCommandsStatic () );
    commands.add ( this.getTypeSubstitutions () );
    LatexPrintable workarround = this.getTypeEquationList ();
    commands.add ( workarround );
    for ( TypeEquation equation : this.getTypeEquationList () )
    {
      commands.add ( equation.getSeenTypes ().getLatexCommands () );
    }
    return commands;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#getLatexInstructions()
   */
  public LatexInstructionList getLatexInstructions ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( getLatexInstructionsStatic () );
    instructions.add ( this.getTypeSubstitutions () );
    LatexPrintable workarround = this.getTypeEquationList ();
    instructions.add ( workarround );
    for ( TypeEquation equation : this.getTypeEquationList () )
    {
      instructions.add ( equation.getSeenTypes ().getLatexInstructions () );
    }
    return instructions;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#getLatexPackages()
   */
  public LatexPackageList getLatexPackages ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( getLatexPackagesStatic () );
    packages.add ( this.getTypeSubstitutions () );
    LatexPrintable workarround = this.getTypeEquationList ();
    packages.add ( workarround );
    for ( TypeEquation equation : this.getTypeEquationList () )
    {
      packages.add ( equation.getSeenTypes ().getLatexPackages () );
    }
    return packages;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#toLatexString()
   */
  public LatexString toLatexString ()
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance (), 0 )
        .toLatexString ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#toLatexStringBuilder(de.unisiegen.tpml.core.latex.LatexStringBuilderFactory,
   *      int)
   */
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory latexStringBuilderFactory, int indent )
  {
    StringBuilder body1 = new StringBuilder ();
    body1.append ( PRETTY_LBRACKET );
    if ( this.getTypeSubstitutions () != TypeSubstitutionList.EMPTY_LIST )
    {
      TypeSubstitutionList ll = this.getTypeSubstitutions ();
      while ( ll != TypeSubstitutionList.EMPTY_LIST )
      {
        body1.append ( ll.getFirst ().toPrettyString ().toString () );
        if ( ll.hasNext () )
        {
          body1.append ( PRETTY_COMMA );
          body1.append ( PRETTY_SPACE );
          ll = ll.getRemaining ();
        }
      }
    }
    body1.append ( PRETTY_RBRACKET );

    StringBuilder body2 = new StringBuilder ();
    for ( TypeEquation equation : this.getTypeEquationList () )
    {
      body2.append ( equation.getSeenTypes ().toPrettyString ().toString () );
      body2.append ( PRETTY_COMMA );
    }
    body2.append ( PRETTY_LINE_BREAK );

    String descriptions[] = new String [ 3
        + this.getTypeSubstitutions ().size ()
        + this.getTypeEquationList ().size () ];
    descriptions [ 0 ] = this.toPrettyString ().toString ();
    descriptions [ 1 ] = body1.toString ();
    if ( this.getTypeSubstitutions () != TypeSubstitutionList.EMPTY_LIST )
    {
      TypeSubstitutionList ll = this.getTypeSubstitutions ();
      for ( int i = 0 ; i < ll.size () ; ++i )
      {
        descriptions [ 2 + i ] = ll.getFirst ().toPrettyString ().toString ();
        ll = ll.getRemaining ();
      }
    }

    descriptions [ 2 + this.getTypeSubstitutions ().size () ] = body2
        .toString ();
    int count = 0;
    for ( TypeEquation equation : this.getTypeEquationList () )
    {
      descriptions [ 3 + this.getTypeSubstitutions ().size () + count ] = equation
          .getSeenTypes ().toPrettyString ().toString ();
      ++count;
    }

    LatexStringBuilder builder = latexStringBuilderFactory.newBuilder ( 0,
        LATEX_UNIFY_PROOF_NODE, indent, descriptions );
    builder.addBuilderBegin ();
    
    if ( this.getTypeSubstitutions ().size () > 0 )
    {
      builder.addText ( "\\color{" + LATEX_COLOR_NONE + "}{" ); //$NON-NLS-1$ //$NON-NLS-2$
      builder.addText ( LATEX_LBRACKET );
      builder.addText ( "}" ); //$NON-NLS-1$

      TypeSubstitutionList ll = this.getTypeSubstitutions ();
      while ( ll.hasNext () )
      {
        builder.addBuilder ( this.getTypeSubstitutions ().getFirst ()
            .toLatexStringBuilder ( latexStringBuilderFactory,
                indent + LATEX_INDENT * 2 ), 0 );
        if ( this.getTypeSubstitutions ().getRemaining () != TypeSubstitutionList.EMPTY_LIST )
        {
          builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE );
          builder.addText ( DefaultLatexStringBuilder.getIndent ( indent
              + LATEX_INDENT ) );
          builder.addText ( "\\color{" + LATEX_COLOR_NONE + "}{" ); //$NON-NLS-1$ //$NON-NLS-2$
          builder.addText ( LATEX_COMMA );
          builder.addText ( LATEX_SPACE );
          builder.addText ( "}" ); //$NON-NLS-1$
        }
        ll = ll.getRemaining ();
      }

      builder.addText ( "\\color{" + LATEX_COLOR_NONE + "}{" ); //$NON-NLS-1$ //$NON-NLS-2$
      builder.addText ( LATEX_RBRACKET );
      builder.addText ( "}" ); //$NON-NLS-1$
      builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE );
      builder.addText ( DefaultLatexStringBuilder.getIndent ( indent
          + LATEX_INDENT ) );
      builder.addText ( LATEX_NEW_LINE );
    }

    for ( TypeEquation equation : this.getTypeEquationList () )
    {
      builder.addBuilder ( equation.getSeenTypes ().toLatexStringBuilder (
          latexStringBuilderFactory, indent + LATEX_INDENT * 2 ), 0 );
      builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE );
      builder.addText ( DefaultLatexStringBuilder.getIndent ( indent
          + LATEX_INDENT ) );

      builder.addBuilder ( equation.toLatexStringBuilder (
          latexStringBuilderFactory, indent + LATEX_INDENT * 2 ), 0 );
      builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE );
      builder.addText ( DefaultLatexStringBuilder.getIndent ( indent
          + LATEX_INDENT ) );
      builder.addText ( LATEX_NEW_LINE );
    }

    builder.addBuilderEnd ();

    return builder;
  }


  /**
   * @inheritDoc
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString ()
  {
    if ( !isProvable () )
      return "Nicht lösbar"; //$NON-NLS-1$
//    if (this.isProven ())
//      return getTypeSubstitutions().toString ();
//    if (getRule().getName ().equals ( "EMPTY" )) //$NON-NLS-1$
      return getTypeSubstitutions ().toString ()
          + " " + PRETTY_CONCAT + " unify(" + getTypeEquationList ().toPrettyString ().toString () + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//    return "unify(" + getTypeEquationList().toPrettyString ().toString () + ")";  //$NON-NLS-1$//$NON-NLS-2$
  }


  /**
   * @inheritDoc
   * @see de.unisiegen.tpml.core.unify.AbstractUnifyProofNode#getRule()
   */
  @Override
  public UnifyProofRule getRule ()
  {
    final UnifyProofStep [] newSteps = getSteps ();
    if ( newSteps.length > 0 )
    {
      return ( UnifyProofRule ) newSteps [ 0 ].getRule ();
    }
    return null;
  }
}
