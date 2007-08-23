package de.unisiegen.tpml.core.typeinference ;


import java.util.ArrayList ;
import java.util.TreeSet ;
import javax.swing.tree.TreeNode ;
import de.unisiegen.tpml.core.AbstractProofNode ;
import de.unisiegen.tpml.core.ProofStep ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexPackage ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexInstruction ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
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
    commands.add ( new DefaultLatexCommand ( LATEX_TYPE_INFERENCE_PROOF_NODE ,
        2 , "\\ifthenelse{\\equal{#1}{}}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
            + "{#2}" + LATEX_LINE_BREAK_NEW_COMMAND + "{[#1]\\\\[1mm]#2}" , //$NON-NLS-1$ //$NON-NLS-2$
        "substitutions" , "formulas" ) ) ; //$NON-NLS-1$//$NON-NLS-2$
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
  public TreeSet < LatexInstruction > getLatexInstructions ( )
  {
    TreeSet < LatexInstruction > instructions = new TreeSet < LatexInstruction > ( ) ;
    for ( TypeSubstitution substitution : this.substitutions )
    {
      for ( LatexInstruction instruction : substitution.getLatexInstructions ( ) )
      {
        instructions.add ( instruction ) ;
      }
    }
    for ( TypeFormula form : this.formula )
    {
      for ( LatexInstruction instruction : form.getLatexInstructions ( ) )
      {
        instructions.add ( instruction ) ;
      }
      if ( form instanceof TypeEquationTypeInference )
      {
        for ( LatexInstruction instruction : ( ( TypeEquationTypeInference ) form )
            .getSeenTypes ( ).getLatexInstructions ( ) )
        {
          instructions.add ( instruction ) ;
        }
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
    packages.add ( new DefaultLatexPackage ( "ifthen" ) ) ; //$NON-NLS-1$
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
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( this ,
        0 , LATEX_TYPE_INFERENCE_PROOF_NODE , pIndent ) ;
    builder.addBuilderBegin ( ) ;
    for ( int i = 0 ; i < this.substitutions.size ( ) ; i ++ )
    {
      builder.addBuilder ( this.substitutions.get ( i ).toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
      if ( i < this.substitutions.size ( ) - 1 )
      {
        builder.addText ( LATEX_COMMA ) ;
        builder.addText ( LATEX_SPACE ) ;
      }
    }
    builder.addBuilderEnd ( ) ;
    builder.addBuilderBegin ( ) ;
    for ( int i = 0 ; i < this.formula.size ( ) ; i ++ )
    {
      if ( this.formula.get ( i ) instanceof TypeEquationTypeInference )
      {
        TypeEquationTypeInference equation = ( TypeEquationTypeInference ) this.formula
            .get ( i ) ;
        builder.addBuilder ( equation.getSeenTypes ( ).toLatexStringBuilder (
            pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
        builder.addText ( "\\ \\vdash\\ " ) ; //$NON-NLS-1$
      }
      builder.addBuilder ( this.formula.get ( i ).toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
      if ( i < this.formula.size ( ) - 1 )
      {
        builder.addText ( "\\newline" ) ; //$NON-NLS-1$
      }
    }
    builder.addBuilderEnd ( ) ;
    return builder ;
  }


  /**
   * {@inheritDoc} Mainly useful for debugging purposes.
   * 
   * @see java.lang.Object#toString()
   */
  @ Override
  public String toString ( )
  {
    final StringBuilder builder = new StringBuilder ( ) ;
    builder.append ( "<html>" ) ; //$NON-NLS-1$
    builder.append ( "S: " + this.substitutions ) ; //$NON-NLS-1$
    builder.append ( "<br>" ) ; //$NON-NLS-1$
    for ( int i = 0 ; i < this.formula.size ( ) ; i ++ )
    {
      if ( i != 0 )
      {
        builder.append ( "<br>" ) ; //$NON-NLS-1$
      }
      builder.append ( this.formula.get ( i ) ) ;
    }
    if ( getRule ( ) != null )
    {
      builder.append ( " (" + getRule ( ) + ")" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    }
    builder.append ( "</html>" ) ; //$NON-NLS-1$
    return builder.toString ( ) ;
  }
}
