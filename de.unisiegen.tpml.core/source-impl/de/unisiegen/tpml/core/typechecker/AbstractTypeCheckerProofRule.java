package de.unisiegen.tpml.core.typechecker ;


import java.lang.reflect.InvocationTargetException ;
import java.text.MessageFormat ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.AbstractProofRule ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.ProofRuleException ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexInstruction ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;


/**
 * Abstract base class for implementations of the
 * <code>TypeCheckerProofRule</code> interface.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:415M $
 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule
 * @see de.unisiegen.tpml.core.AbstractProofRule
 */
public abstract class AbstractTypeCheckerProofRule extends AbstractProofRule
    implements TypeCheckerProofRule
{
  /**
   * Allocates a new <code>AbstractTypeCheckerProofRule</code> of the
   * specified <code>name</code>.
   * 
   * @param group the group id of the type rule, see the description of the
   *          {@link AbstractProofRule#getGroup()} method for details.
   * @param name the name of the type rule to allocate.
   * @throws NullPointerException if <code>name</code> is <code>null</code>.
   */
  AbstractTypeCheckerProofRule ( int group , String name )
  {
    super ( group , name ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule#apply(de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext,
   *      de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode)
   */
  public void apply ( TypeCheckerProofContext context ,
      TypeCheckerProofNode node ) throws ProofRuleException
  {
    if ( node == null )
    {
      throw new NullPointerException ( "node is null" ) ; //$NON-NLS-1$
    }
    if ( context == null )
    {
      throw new NullPointerException ( "context is null" ) ; //$NON-NLS-1$
    }
    try
    {
      applyInternal ( context , node ) ;
    }
    catch ( ProofRuleException e )
    {
      throw e ;
    }
    catch ( InvocationTargetException e )
    {
      if ( e.getTargetException ( ) instanceof ClassCastException )
      {
        throw new ProofRuleException ( MessageFormat.format ( Messages
            .getString ( "ProofRuleException.0" ) , this , node ) , node , //$NON-NLS-1$
            this , e.getTargetException ( ) ) ;
      }
      if ( e.getTargetException ( ).getMessage ( ) == null )
      {
        throw new ProofRuleException ( node , this , e.getTargetException ( ) ) ;
      }
      throw new ProofRuleException ( e.getTargetException ( ).getMessage ( ) ,
          node , this , e.getTargetException ( ) ) ;
    }
    catch ( Exception e )
    {
      // check if e contains a usable error message
      for ( Throwable t = e ; t != null ; t = t.getCause ( ) )
      {
        if ( t instanceof IllegalArgumentException )
        {
          throw new ProofRuleException ( t.getMessage ( ) , node , this , e ) ;
        }
      }
      throw new ProofRuleException ( node , this , e ) ;
    }
  }


  /**
   * Abstract internal apply method, implemented by the
   * {@link AbstractTypeCheckerProofRuleSet} class while registering new proof
   * rules.
   * 
   * @param context see
   *          {@link #apply(TypeCheckerProofContext, TypeCheckerProofNode)}.
   * @param node see
   *          {@link #apply(TypeCheckerProofContext, TypeCheckerProofNode)}.
   * @throws Exception if an error occurs while applying the rule to the
   *           <code>node</code> using the <code>context</code>.
   * @see #apply(TypeCheckerProofContext, TypeCheckerProofNode)
   */
  protected abstract void applyInternal ( TypeCheckerProofContext context ,
      TypeCheckerProofNode node ) throws Exception ;


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_TYPE_CHECKER_PROOF_RULE , 1 ,
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
        LATEX_TYPE_CHECKER_PROOF_RULE , pIndent ) ;
    builder
        .addText ( "{" + this.getName ( ).replaceAll ( "_" , "\\\\_" ) + "}" ) ; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    return builder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule#update(de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext,
   *      de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode)
   */
  public void update ( TypeCheckerProofContext context ,
      TypeCheckerProofNode node )
  {
    if ( node == null )
    {
      throw new NullPointerException ( "node is null" ) ; //$NON-NLS-1$
    }
    if ( context == null )
    {
      throw new NullPointerException ( "context is null" ) ; //$NON-NLS-1$
    }
    try
    {
      updateInternal ( context , node ) ;
    }
    catch ( RuntimeException e )
    {
      throw e ;
    }
    catch ( InvocationTargetException e )
    {
      throw new RuntimeException ( e.getTargetException ( ) ) ;
    }
    catch ( Exception e )
    {
      throw new RuntimeException ( e ) ;
    }
  }


  /**
   * Abstract internal update method, implemented by the
   * {@link AbstractTypeCheckerProofRuleSet} class while registering new proof
   * rules.
   * 
   * @param context see
   *          {@link #update(TypeCheckerProofContext, TypeCheckerProofNode)}.
   * @param node see
   *          {@link #update(TypeCheckerProofContext, TypeCheckerProofNode)}.
   * @throws Exception if an error occurs while updating the <code>node</code>
   *           using the <code>context</code>.
   */
  protected abstract void updateInternal ( TypeCheckerProofContext context ,
      TypeCheckerProofNode node ) throws Exception ;
}
