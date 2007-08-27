package de.unisiegen.tpml.core.smallstep ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.AbstractProofRule ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexInstruction ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;


/**
 * Default implementation of the <code>SmallStepProofRule</code> interface.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev$
 * @see de.unisiegen.tpml.core.AbstractProofRule
 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofRule
 */
final class DefaultSmallStepProofRule extends AbstractProofRule implements
    SmallStepProofRule
{
  /**
   * <code>true</code> if this small step proof rule is an axiom, and as such,
   * has no premises. The opposite is a meta rule, which has exactly one
   * premise.
   * 
   * @see #isAxiom()
   */
  private boolean axiom ;


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
  DefaultSmallStepProofRule ( int group , String name , boolean pAxiom )
  {
    super ( group , name ) ;
    this.axiom = pAxiom ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_SMALL_STEP_PROOF_RULE , 1 ,
        "\\mbox{\\scriptsize{\\textbf{(#1)}}}" , "name" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
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
    return packages ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofRule#isAxiom()
   */
  public boolean isAxiom ( )
  {
    return this.axiom ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofRule#toExnRule()
   */
  public SmallStepProofRule toExnRule ( )
  {
    if ( ! isAxiom ( ) )
    {
      return new DefaultSmallStepProofRule ( getGroup ( ) , getName ( )
          + "-EXN" , false ) ; //$NON-NLS-1$
    }
    return this ;
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
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0 ,
        LATEX_SMALL_STEP_PROOF_RULE , pIndent ) ;
    builder
        .addText ( "{" + this.getName ( ).replaceAll ( "_" , "\\\\_" ) + "}" ) ; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    return builder ;
  }
}
