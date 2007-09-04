package de.unisiegen.tpml.core.typeinference ;


import java.util.ArrayList ;
import java.util.TreeSet ;
import javax.swing.tree.TreeNode ;
import de.unisiegen.tpml.core.AbstractProofNode ;
import de.unisiegen.tpml.core.ProofStep ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction ;
import de.unisiegen.tpml.core.latex.DefaultLatexPackage ;
import de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexInstructionList ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Default implementation of the <code>TypeInferenceProofNode</code>
 * interface. The class for nodes in a
 * {@link de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel}.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 * @see de.unisiegen.tpml.core.AbstractProofNode
 * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode
 */
public class DefaultTypeInferenceProofNode extends AbstractProofNode implements
    TypeInferenceProofNode
{
  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static TreeSet < LatexCommand > getLatexCommandsStatic ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_TYPE_INFERENCE_PROOF_NODE ,
        1 , "#1" , "body" ) ) ; //$NON-NLS-1$//$NON-NLS-2$
    return commands ;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public static LatexInstructionList getLatexInstructionsStatic ( )
  {
    LatexInstructionList instructions = new LatexInstructionList ( ) ;
    instructions.add ( new DefaultLatexInstruction ( "\\definecolor{" //$NON-NLS-1$
        + LATEX_COLOR_NONE + "}{rgb}{0.0,0.0,0.0}" , //$NON-NLS-1$
        LATEX_COLOR_NONE + ": color of normal text" ) ) ; //$NON-NLS-1$
    return instructions ;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public static TreeSet < LatexPackage > getLatexPackagesStatic ( )
  {
    TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( ) ;
    packages.add ( new DefaultLatexPackage ( "color" ) ) ; //$NON-NLS-1$
    packages.add ( new DefaultLatexPackage ( "amssymb" ) ) ; //$NON-NLS-1$
    return packages ;
  }


  /**
   * list of all formulas of this node
   */
  private ArrayList < TypeFormula > formula = new ArrayList < TypeFormula > ( ) ;


  /**
   * list of the collected type substitutions of this node
   */
  private ArrayList < TypeSubstitution > substitutions = new ArrayList < TypeSubstitution > ( ) ;


  /**
   * list of proof steps of this node
   */
  private ProofStep [ ] steps = new ProofStep [ 0 ] ;


  /**
   * Allocates a new <code>DefaultTypeEquationProofNode</code>
   * 
   * @param judgement type formulas of the node
   * @param subs subs substitutions of the node
   */
  public DefaultTypeInferenceProofNode (
      final ArrayList < TypeFormula > judgement ,
      final ArrayList < TypeSubstitution > subs )
  {
    // equations = eqns;
    this.formula = judgement ;
    this.substitutions = subs ;
  }


  /**
   * Allocates a new <code>DefaultTypeEquationProofNode</code>
   * 
   * @param judgement type judgement of the node which will be added to formula
   * @param subs substitutions of the node
   */
  public DefaultTypeInferenceProofNode ( final TypeJudgement judgement ,
      final ArrayList < TypeSubstitution > subs )
  {
    // equations = eqns;
    this.formula.add ( judgement ) ;
    this.substitutions = subs ;
  }


  /**
   * get a list of all type formulas of this node
   * 
   * @return ArraList containing all type formulas
   * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode#getAllFormulas()
   */
  public ArrayList < TypeFormula > getAllFormulas ( )
  {
    return this.formula ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildAfter(javax.swing.tree.TreeNode)
   */
  @ Override
  public DefaultTypeInferenceProofNode getChildAfter ( final TreeNode aChild )
  {
    return ( DefaultTypeInferenceProofNode ) super.getChildAfter ( aChild ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildAt(int)
   */
  @ Override
  public DefaultTypeInferenceProofNode getChildAt ( final int childIndex )
  {
    return ( DefaultTypeInferenceProofNode ) super.getChildAt ( childIndex ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildBefore(javax.swing.tree.TreeNode)
   */
  @ Override
  public DefaultTypeInferenceProofNode getChildBefore ( final TreeNode aChild )
  {
    return ( DefaultTypeInferenceProofNode ) super.getChildBefore ( aChild ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getFirstChild()
   */
  @ Override
  public DefaultTypeInferenceProofNode getFirstChild ( )
  {
    return ( DefaultTypeInferenceProofNode ) super.getFirstChild ( ) ;
  }


  /**
   * get the first type formula of the type formula list of this node
   * 
   * @return the first type formula of the list or null if list is empty
   * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode#getFirstFormula()
   */
  public TypeFormula getFirstFormula ( )
  {
    if ( ! this.formula.isEmpty ( ) )
    {
      return this.formula.get ( 0 ) ;
    }
    return null ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getFirstLeaf()
   */
  @ Override
  public DefaultTypeInferenceProofNode getFirstLeaf ( )
  {
    return ( DefaultTypeInferenceProofNode ) super.getFirstLeaf ( ) ;
  }


  /**
   * get the list of type formulas of this node
   * 
   * @return LinkedList<TypeFormula> formula
   */
  public ArrayList < TypeFormula > getFormula ( )
  {
    return this.formula ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getLastChild()
   */
  @ Override
  public DefaultTypeInferenceProofNode getLastChild ( )
  {
    return ( DefaultTypeInferenceProofNode ) super.getLastChild ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getLastLeaf()
   */
  @ Override
  public DefaultTypeInferenceProofNode getLastLeaf ( )
  {
    return ( DefaultTypeInferenceProofNode ) super.getLastLeaf ( ) ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    for ( LatexCommand command : getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( TypeSubstitution substitution : this.substitutions )
    {
      for ( LatexCommand command : substitution.getLatexCommands ( ) )
      {
        commands.add ( command ) ;
      }
    }
    for ( TypeFormula form : this.formula )
    {
      for ( LatexCommand command : form.getLatexCommands ( ) )
      {
        commands.add ( command ) ;
      }
      if ( form instanceof TypeEquationTypeInference )
      {
        for ( LatexCommand command : ( ( TypeEquationTypeInference ) form )
            .getSeenTypes ( ).getLatexCommands ( ) )
        {
          commands.add ( command ) ;
        }
      }
    }
    return commands ;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public LatexInstructionList getLatexInstructions ( )
  {
    LatexInstructionList instructions = new LatexInstructionList ( ) ;
    instructions.add ( getLatexInstructionsStatic ( ) ) ;
    instructions.add ( this.substitutions ) ;
    for ( TypeFormula form : this.formula )
    {
      instructions.add ( form ) ;
      if ( form instanceof TypeEquationTypeInference )
      {
        instructions.add ( ( ( TypeEquationTypeInference ) form )
            .getSeenTypes ( ).getLatexInstructions ( ) ) ;
      }
    }
    return instructions ;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public TreeSet < LatexPackage > getLatexPackages ( )
  {
    TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( ) ;
    for ( LatexPackage pack : getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( TypeSubstitution substitution : this.substitutions )
    {
      for ( LatexPackage pack : substitution.getLatexPackages ( ) )
      {
        packages.add ( pack ) ;
      }
    }
    for ( TypeFormula form : this.formula )
    {
      for ( LatexPackage pack : form.getLatexPackages ( ) )
      {
        packages.add ( pack ) ;
      }
      if ( form instanceof TypeEquationTypeInference )
      {
        for ( LatexPackage pack : ( ( TypeEquationTypeInference ) form )
            .getSeenTypes ( ).getLatexPackages ( ) )
        {
          packages.add ( pack ) ;
        }
      }
    }
    return packages ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getParent()
   */
  @ Override
  public DefaultTypeInferenceProofNode getParent ( )
  {
    return ( DefaultTypeInferenceProofNode ) super.getParent ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getRoot()
   */
  @ Override
  public DefaultTypeInferenceProofNode getRoot ( )
  {
    return ( DefaultTypeInferenceProofNode ) super.getRoot ( ) ;
  }


  /**
   * get the rules applied to this node
   * 
   * @return ProofStep[] steps or null
   * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode#getRule()
   */
  public TypeCheckerProofRule getRule ( )
  {
    final ProofStep [ ] newSteps = getSteps ( ) ;
    if ( newSteps.length > 0 )
    {
      return ( TypeCheckerProofRule ) newSteps [ 0 ].getRule ( ) ;
    }
    return null ;
  }


  /**
   * get the proof steps of this node
   * 
   * @return ProofStep[] steps
   */
  public ProofStep [ ] getSteps ( )
  {
    return this.steps ;
  }


  /**
   * add a new type substitution to the list of substitutions of this node
   * 
   * @param s1 DefaultTypeSubstitution to add to list public void
   *          addSubstitution(DefaultTypeSubstitution s1) {
   *          substitutions.add(s1); }
   */
  /**
   * get the type substitution list of this node
   * 
   * @return TypeSubstitutionList substitutions
   */
  public ArrayList < TypeSubstitution > getSubstitution ( )
  {
    return this.substitutions ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see TypeInferenceProofNode#isFinished()
   */
  public boolean isFinished ( )
  {
    return getLastLeaf ( ).getFormula ( ).size ( ) < 1 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#isProven()
   */
  public boolean isProven ( )
  {
    return ( getSteps ( ).length > 0 ) ;
  }


  /**
   * set the type formulas for this node
   * 
   * @param pFormula ArrayList of the new type formulas
   */
  public void setFormula ( ArrayList < TypeFormula > pFormula )
  {
    this.formula = pFormula ;
  }


  /**
   * set the proof steps of this node
   * 
   * @param pSteps new proof steps for this node
   */
  public void setSteps ( final ProofStep [ ] pSteps )
  {
    this.steps = pSteps ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexString()
   */
  public final LatexString toLatexString ( )
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance ( ) , 0 )
        .toLatexString ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  public final LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
  {
    int countEquation = 0 ;
    for ( int i = 0 ; i < this.formula.size ( ) ; i ++ )
    {
      if ( this.formula.get ( i ) instanceof TypeEquationTypeInference )
      {
        countEquation ++ ;
      }
    }
    StringBuilder body1 = new StringBuilder ( ) ;
    if ( this.substitutions.size ( ) > 0 )
    {
      body1.append ( PRETTY_LBRACKET ) ;
      for ( int i = 0 ; i < this.substitutions.size ( ) ; i ++ )
      {
        body1.append ( this.substitutions.get ( i ).toPrettyString ( )
            .toString ( ) ) ;
        if ( i < this.substitutions.size ( ) - 1 )
        {
          body1.append ( PRETTY_COMMA ) ;
          body1.append ( PRETTY_SPACE ) ;
        }
      }
      body1.append ( PRETTY_RBRACKET ) ;
    }
    StringBuilder body2 = new StringBuilder ( ) ;
    for ( int i = 0 ; i < this.formula.size ( ) ; i ++ )
    {
      if ( this.formula.get ( i ) instanceof TypeEquationTypeInference )
      {
        TypeEquationTypeInference equation = ( TypeEquationTypeInference ) this.formula
            .get ( i ) ;
        body2
            .append ( equation.getSeenTypes ( ).toPrettyString ( ).toString ( ) ) ;
        body2.append ( PRETTY_SPACE ) ;
        body2.append ( PRETTY_NAIL ) ;
        body2.append ( PRETTY_SPACE ) ;
      }
      body2.append ( this.formula.get ( i ).toPrettyString ( ).toString ( ) ) ;
      if ( i < this.formula.size ( ) - 1 )
      {
        body2.append ( PRETTY_LINE_BREAK ) ;
      }
    }
    String descriptions[] = new String [ 3 + this.substitutions.size ( )
        + this.formula.size ( ) + countEquation ] ;
    descriptions [ 0 ] = this.toPrettyString ( ).toString ( ) ;
    descriptions [ 1 ] = body1.toString ( ) ;
    for ( int i = 0 ; i < this.substitutions.size ( ) ; i ++ )
    {
      descriptions [ 2 + i ] = this.substitutions.get ( i ).toPrettyString ( )
          .toString ( ) ;
    }
    descriptions [ 2 + this.substitutions.size ( ) ] = body2.toString ( ) ;
    int tmpCountEquation = 0 ;
    for ( int i = 0 ; i < this.formula.size ( ) ; i ++ )
    {
      if ( this.formula.get ( i ) instanceof TypeEquationTypeInference )
      {
        TypeEquationTypeInference equation = ( TypeEquationTypeInference ) this.formula
            .get ( i ) ;
        descriptions [ 3 + this.substitutions.size ( ) + i + tmpCountEquation ] = equation
            .getSeenTypes ( ).toPrettyString ( ).toString ( ) ;
        tmpCountEquation ++ ;
      }
      descriptions [ 3 + this.substitutions.size ( ) + i + tmpCountEquation ] = this.formula
          .get ( i ).toPrettyString ( ).toString ( ) ;
    }
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0 ,
        LATEX_TYPE_INFERENCE_PROOF_NODE , pIndent , descriptions ) ;
    builder.addBuilderBegin ( ) ;
    if ( this.substitutions.size ( ) > 0 )
    {
      builder.addText ( "\\color{" + LATEX_COLOR_NONE + "}{" ) ; //$NON-NLS-1$ //$NON-NLS-2$
      builder.addText ( LATEX_LBRACKET ) ;
      builder.addText ( "}" ) ; //$NON-NLS-1$
      for ( int i = 0 ; i < this.substitutions.size ( ) ; i ++ )
      {
        builder.addBuilder ( this.substitutions.get ( i ).toLatexStringBuilder (
            pLatexStringBuilderFactory , pIndent + LATEX_INDENT * 2 ) , 0 ) ;
        if ( i < this.substitutions.size ( ) - 1 )
        {
          builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
          builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
              + LATEX_INDENT ) ) ;
          builder.addText ( "\\color{" + LATEX_COLOR_NONE + "}{" ) ; //$NON-NLS-1$ //$NON-NLS-2$
          builder.addText ( LATEX_COMMA ) ;
          builder.addText ( LATEX_SPACE ) ;
          builder.addText ( "}" ) ; //$NON-NLS-1$
        }
      }
      builder.addText ( "\\color{" + LATEX_COLOR_NONE + "}{" ) ; //$NON-NLS-1$ //$NON-NLS-2$
      builder.addText ( LATEX_RBRACKET ) ;
      builder.addText ( "}" ) ; //$NON-NLS-1$
      builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
      builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
          + LATEX_INDENT ) ) ;
      builder.addText ( LATEX_NEW_LINE ) ;
    }
    for ( int i = 0 ; i < this.formula.size ( ) ; i ++ )
    {
      if ( this.formula.get ( i ) instanceof TypeEquationTypeInference )
      {
        TypeEquationTypeInference equation = ( TypeEquationTypeInference ) this.formula
            .get ( i ) ;
        builder.addBuilder ( equation.getSeenTypes ( ).toLatexStringBuilder (
            pLatexStringBuilderFactory , pIndent + LATEX_INDENT * 2 ) , 0 ) ;
        builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
        builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
            + LATEX_INDENT ) ) ;
        builder.addText ( "\\color{" + LATEX_COLOR_NONE + "}{" ) ; //$NON-NLS-1$ //$NON-NLS-2$
        builder.addText ( LATEX_SPACE ) ;
        builder.addText ( LATEX_NAIL ) ;
        builder.addText ( LATEX_SPACE ) ;
        builder.addText ( "}" ) ; //$NON-NLS-1$
      }
      builder.addBuilder ( this.formula.get ( i ).toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent + LATEX_INDENT * 2 ) , 0 ) ;
      if ( i < this.formula.size ( ) - 1 )
      {
        builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
        builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
            + LATEX_INDENT ) ) ;
        builder.addText ( LATEX_NEW_LINE ) ;
      }
    }
    builder.addBuilderEnd ( ) ;
    return builder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyString()
   */
  public PrettyString toPrettyString ( )
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance ( ) )
        .toPrettyString ( ) ;
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
        this , 0 ) ;
    if ( this.substitutions.size ( ) > 0 )
    {
      builder.addText ( PRETTY_LBRACKET ) ;
      for ( int i = 0 ; i < this.substitutions.size ( ) ; i ++ )
      {
        builder.addBuilder ( this.substitutions.get ( i )
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
        if ( i < this.substitutions.size ( ) - 1 )
        {
          builder.addText ( PRETTY_COMMA ) ;
          builder.addText ( PRETTY_SPACE ) ;
        }
      }
      builder.addText ( PRETTY_RBRACKET ) ;
      builder.addText ( PRETTY_LINE_BREAK ) ;
    }
    for ( int i = 0 ; i < this.formula.size ( ) ; i ++ )
    {
      if ( this.formula.get ( i ) instanceof TypeEquationTypeInference )
      {
        TypeEquationTypeInference equation = ( TypeEquationTypeInference ) this.formula
            .get ( i ) ;
        builder.addBuilder ( equation.getSeenTypes ( ).toPrettyStringBuilder (
            pPrettyStringBuilderFactory ) , 0 ) ;
        builder.addText ( PRETTY_SPACE ) ;
        builder.addText ( PRETTY_NAIL ) ;
        builder.addText ( PRETTY_SPACE ) ;
      }
      builder.addBuilder ( this.formula.get ( i ).toPrettyStringBuilder (
          pPrettyStringBuilderFactory ) , 0 ) ;
      if ( i < this.formula.size ( ) - 1 )
      {
        builder.addText ( PRETTY_LINE_BREAK ) ;
      }
    }
    return builder ;
  }


  /**
   * Returns the string representation for this type inference proof node. This
   * method is mainly used for debugging.
   * 
   * @return The pretty printed string representation for thistype inference
   *         proof node.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @ Override
  public final String toString ( )
  {
    return toPrettyString ( ).toString ( ) ;
  }
}
