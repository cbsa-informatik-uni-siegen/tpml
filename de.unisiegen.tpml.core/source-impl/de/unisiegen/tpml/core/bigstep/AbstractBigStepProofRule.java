package de.unisiegen.tpml.core.bigstep ;


import java.lang.reflect.InvocationTargetException ;
import de.unisiegen.tpml.core.AbstractProofRule ;
import de.unisiegen.tpml.core.ProofRuleException ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.exceptions.RowSubstitutionException ;
import de.unisiegen.tpml.core.typechecker.UnificationException ;


/**
 * Abstract base class for implementations of the <code>BigStepProofRule</code>
 * interface.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:415M $
 * @see de.unisiegen.tpml.core.bigstep.BigStepProofRule
 * @see de.unisiegen.tpml.core.AbstractProofRule
 */
abstract class AbstractBigStepProofRule extends AbstractProofRule implements
    BigStepProofRule
{
  //
  // Constructor (package)
  //
  /**
   * Allocates a new <code>AbstractBigStepProofRule</code> of the specified
   * <code>name</code>.
   * 
   * @param group the group id of this big step rule, see the description of the
   *          {@link AbstractProofRule#getGroup()} for details.
   * @param name the name of the big step proof rule to allocate.
   * @throws NullPointerException if <code>name</code> is <code>null</code>.
   */
  AbstractBigStepProofRule ( int group , String name )
  {
    super ( group , name ) ;
  }


  //
  // Primitives
  //
  /**
   * Applies this big step proof rule to the specified <code>node</code> via
   * the given <code>context</code>.
   * 
   * @param context the big step proof context via which the application of this
   *          rule to the <code>node</code> should be performed.
   * @param node the big step proof node to which to apply this rule.
   * @throws ProofRuleException if this rule cannot be applied to the
   *           <code>node</code>.
   * @throws NullPointerException if either <code>context</code> or
   *           <code>node</code> is <code>null</code>.
   */
  public void apply ( BigStepProofContext context , BigStepProofNode node )
      throws ProofRuleException
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
      if ( ( e.getTargetException ( ) instanceof NotOnlyFreeVariableException )
          || ( e.getTargetException ( ) instanceof RowSubstitutionException ) )
      {
        throw new RuntimeException ( e.getTargetException ( ).getMessage ( ) ) ;
      }
      throw new ProofRuleException ( node , this , e.getTargetException ( ) ) ;
    }
    catch ( Exception e )
    {
      // check if e contains a usable error message
      for ( Throwable t = e ; t != null ; t = t.getCause ( ) )
      {
        if ( t instanceof IllegalArgumentException
            && t instanceof UnificationException )
        {
          throw new ProofRuleException ( t.getMessage ( ) , node , this , e ) ;
        }
      }
      throw new ProofRuleException ( node , this , e ) ;
    }
  }


  /**
   * Updates the specified <code>node</code> as part of a previous rule
   * application for <code>context</code>. This method is only interesting
   * for non-axiom rules, like <b>(APP)</b> or <b>(LET)</b>, that need to
   * update their created proof nodes even after applications of other proof
   * rules to subtrees. This method is only invoked for proof nodes that are not
   * already proven (see {@link de.unisiegen.tpml.core.ProofNode#isProven()}).
   * If <code>node</code> is proven, this represents a bug in the big step
   * proof model logic.
   * 
   * @param context the main proof context, which was previously specified as
   *          parameter to an
   *          {@link #apply(BigStepProofContext, BigStepProofNode)} invokation
   *          on another proof node, possibly with another proof rule.
   * @param node the {@link BigStepProofNode} that may need to be updated.
   * @throws NullPointerException if <code>context</code> or <code>node</code>
   *           is <code>null</code>.
   */
  public void update ( BigStepProofContext context , BigStepProofNode node )
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
      throw new RuntimeException ( e.getTargetException ( ).getMessage ( ) ) ;
    }
    catch ( Exception e )
    {
      throw new RuntimeException ( e ) ;
    }
  }


  //
  // Abstract methods
  //
  /**
   * Abstract internal apply method, implemented by the
   * {@link AbstractBigStepProofRuleSet} class while registering new proof
   * rules.
   * 
   * @param context see {@link #apply(BigStepProofContext, BigStepProofNode)}.
   * @param node see {@link #apply(BigStepProofContext, BigStepProofNode)}.
   * @throws Exception if an error occurs while applying the rule to the
   *           <code>node</code> using the <code>context</code>.
   * @see #apply(BigStepProofContext, BigStepProofNode)
   */
  protected abstract void applyInternal ( BigStepProofContext context ,
      BigStepProofNode node ) throws Exception ;


  /**
   * Abstract internal update method, implemented by the
   * {@link AbstractBigStepProofRuleSet} class while registering new proof
   * rules.
   * 
   * @param context see {@link #update(BigStepProofContext, BigStepProofNode)}.
   * @param node see {@link #update(BigStepProofContext, BigStepProofNode)}.
   * @throws Exception if an error occurs while updating the <code>node</code>
   *           using the <code>context</code>.
   */
  protected abstract void updateInternal ( BigStepProofContext context ,
      BigStepProofNode node ) throws Exception ;


  //
  // Rule allocation
  //
  /**
   * Allocates a new <code>BigStepProofRule</code> with the specified
   * <code>name</code>, that does nothing, meaning that
   * {@link #apply(BigStepProofContext, BigStepProofNode)} and
   * {@link #update(BigStepProofContext, BigStepProofNode)} are noops.
   * 
   * @param name the name of the rule to allocate.
   * @return a newly allocated <code>BigStepProofRule</code> with the
   *         specified <code>name</code>, that does nothing.
   * @see #toExnRule(int)
   */
  static BigStepProofRule newNoopRule ( String name )
  {
    return new AbstractBigStepProofRule ( - 1 , name )
    {
      @ Override
      protected void applyInternal ( @ SuppressWarnings ( "unused" )
      BigStepProofContext context , @ SuppressWarnings ( "unused" )
      BigStepProofNode node ) throws Exception
      {
        throw new IllegalArgumentException ( "Cannot apply noop rules" ) ; //$NON-NLS-1$
      }


      @ Override
      protected void updateInternal ( @ SuppressWarnings ( "unused" )
      BigStepProofContext context , @ SuppressWarnings ( "unused" )
      BigStepProofNode node ) throws Exception
      {
        // nothing to do here...
      }
    } ;
  }


  /**
   * Translates this big step proof rule to an appropriate exception rule, with
   * the given sub node index <code>n</code>. For example, for <b>(APP)</b>,
   * this generates <b>(APP-EXN-n)</b>.
   * 
   * @param n the index of the sub node, starting at <code>0</code>.
   * @return the new {@link BigStepProofRule} for the exception.
   * @throws IllegalArgumentException if <code>n</code> is negative.
   */
  BigStepProofRule toExnRule ( int n )
  {
    if ( n < 0 )
    {
      throw new IllegalArgumentException ( "n is negative" ) ; //$NON-NLS-1$
    }
    return newNoopRule ( getName ( ) + "-EXN-" + ( n + 1 ) ) ; //$NON-NLS-1$
  }
}
