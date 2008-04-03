package de.unisiegen.tpml.core.unify;


import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

import de.unisiegen.tpml.core.AbstractProofRule;
import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;


/**
 * Abstract base class for implementation of the <code>UnifyProofRule</code>
 * 
 * @author Christian Uhrhan
 * @version $Id: AbstractTypeCheckerProofRule.java 2796 2008-03-14 19:13:11Z
 *          fehler $
 * @see de.unisiegen.tpml.core.unify.UnifyProofRule
 * @see de.unisiegen.tpml.core.AbstractProofRule
 */
public abstract class AbstractUnifyProofRule extends AbstractProofRule
    implements UnifyProofRule
{

  /**
   * Allocates a new <code>AbstractUnifyProofRule</code> of the specified
   * <code>name</code>.
   * 
   * @param group the group id of the type rule, see the description of the
   *          {@link AbstractProofRule#getGroup()} method for details.
   * @param name the name of the unify rule to allocate.
   * @throws NullPointerException if <code>name</code> is <code>null</code>.
   */
  public AbstractUnifyProofRule ( int group, String name )
  {
    super ( group, name );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.unify.UnifyProofRule#apply(de.unisiegen.tpml.core.unify.UnifyProofContext,
   *      de.unisiegen.tpml.core.unify.UnifyProofNode)
   */
  public void apply ( UnifyProofContext context, UnifyProofNode node )
      throws ProofRuleException
  {
    if ( context == null )
      throw new NullPointerException ( " context is null " ); //$NON-NLS-1$
    if ( node == null )
      throw new NullPointerException ( " node is null " ); //$NON-NLS-1$

    try
    {
      applyInternal ( context, node );
    }
    catch ( ProofRuleException e )
    {
      throw e;
    }
    catch ( InvocationTargetException e )
    {
      if ( e.getTargetException () instanceof ClassCastException )
      {
        throw new ProofRuleException ( MessageFormat.format ( Messages
            .getString ( "ProofRuleException.0" ), this, node ), node, //$NON-NLS-1$
            this, e.getTargetException () );
      }
      if ( e.getTargetException ().getMessage () == null )
      {
        throw new ProofRuleException ( node, this, e.getTargetException () );
      }
      throw new ProofRuleException ( e.getTargetException ().getMessage (),
          node, this, e.getTargetException () );
    }
    catch ( Exception e )
    {
      // check if e contains a usable error message
      for ( Throwable t = e ; t != null ; t = t.getCause () )
      {
        if ( t instanceof IllegalArgumentException )
        {
          throw new ProofRuleException ( t.getMessage (), node, this, e );
        }
      }
      throw new ProofRuleException ( node, this, e );
    }
  }


  /**
   * Abstract internal apply method, implemented by the
   * {@link AbstractUnifyProofRuleSet} class while registering new proof rules.
   * 
   * @param context see {@link #apply(UnifyProofContext, UnifyProofNode)}.
   * @param node see {@link #apply(UnifyProofContext, UnifyProofNode)}.
   * @throws Exception if an error occurs while applying the rule to the
   *           <code>node</code> using the <code>context</code>.
   * @see #apply(UnifyProofContext, UnifyProofNode)
   */
  protected abstract void applyInternal ( UnifyProofContext context,
      UnifyProofNode node ) throws Exception;


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable#toPrettyString()
   */
  public PrettyString toPrettyString ()
  {
    return null;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable#toPrettyStringBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory)
   */
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory prettyStringBuilderFactory )
  {
    return null;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#getLatexCommands()
   */
  public LatexCommandList getLatexCommands ()
  {
    return null;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#getLatexInstructions()
   */
  public LatexInstructionList getLatexInstructions ()
  {
    return null;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#getLatexPackages()
   */
  public LatexPackageList getLatexPackages ()
  {
    return null;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#toLatexString()
   */
  public LatexString toLatexString ()
  {
    return null;
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
    return null;
  }

}
