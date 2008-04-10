package de.unisiegen.tpml.core.unify;


import de.unisiegen.tpml.core.AbstractProofModel;
import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.entities.TypeEquationList;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList;


/**
 * The heart of unification.
 * 
 * @author Christian Uhrhan
 * @version $Id$
 * @see de.unisiegen.tpml.core.AbstractProofModel
 */
public class UnifyProofModel extends AbstractProofModel
{

  /**
   * The side overlapping for latex export
   */
  int overlap = 0;


  /**
   * The number of pages for latex export
   */
  int pages = 5;


  //
  // Attributes
  //
  /**
   * The current proof index, which indicates the number of steps that have been
   * performed on the proof model so far (starting with one), and is used to
   * generate new unique type variables in the associated contexts.
   * 
   * @see #getIndex()
   * @see TypeCheckerProofContext#newTypeVariable()
   * @see de.unisiegen.tpml.core.types.TypeVariable
   */
  private int index = 1;


  //
  // Accessors
  //
  /**
   * Returns the current proof model index, which is the number of steps already
   * performed on the model (starting with one) and used to allocate new, unique
   * {@link  de.unisiegen.tpml.core.types.TypeVariable}s. It is incremented
   * with every proof step performed on the model.
   * 
   * @return the current index of the proof model.
   * @see TypeCheckerProofContext#newTypeVariable()
   * @see de.unisiegen.tpml.core.types.TypeVariable
   */
  public int getIndex ()
  {
    return this.index;
  }


  /**
   * TODO
   * 
   * @param root
   * @param ruleSet
   */
  protected UnifyProofModel ( AbstractProofNode root,
      AbstractProofRuleSet ruleSet )
  {
    super ( root, ruleSet );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofModel#guess(de.unisiegen.tpml.core.ProofNode)
   */
  @Override
  public void guess ( ProofNode node ) throws ProofGuessException
  {
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofModel#prove(de.unisiegen.tpml.core.ProofRule,
   *      de.unisiegen.tpml.core.ProofNode)
   */
  @Override
  public void prove ( ProofRule rule, ProofNode node )
      throws ProofRuleException
  {
  }


  /**
   * Adds a new child proof node below the <code>node</code> using the
   * <code>context</code> for the <code>substs</code> and <code>eqns</code>
   * 
   * @param context the context calling this method
   * @param node the parent node to add the this child node to
   * @param substs already collected list of type substitutions (from earlier
   *          (VAR) rules)
   * @param eqns a list of type equations
   */
  public void contextAddProofNode ( DefaultUnifyProofContext context,
      final AbstractUnifyProofNode node, TypeSubstitutionList substs,
      TypeEquationList eqns )
  {
    if ( context == null )
      throw new NullPointerException ( "context is null" ); //$NON-NLS-1$
    if ( node == null )
      throw new NullPointerException ( "node is null" ); //$NON-NLS-1$
    if ( substs == null )
      throw new NullPointerException ( "substs is null" ); //$NON-NLS-1$
    if ( eqns == null )
      throw new NullPointerException ( "eqns is null" ); //$NON-NLS-1$

    final DefaultUnifyProofNode child = new DefaultUnifyProofNode ( substs,
        eqns );
    context.addRedoAction ( new Runnable ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void run ()
      {
        node.add ( child );
        nodesWereInserted ( node, new int []
        { node.getIndex ( child ) } );
      }
    } );
    context.addUndoAction ( new Runnable ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void run ()
      {
        int nodeIndex = node.getIndex ( child );
        node.remove ( nodeIndex );
        nodesWereRemoved ( node, new int []
        { nodeIndex }, new Object []
        { child } );
      }
    } );
  }


  /**
   * Adds a new child proof node below the <code>node</code> using the
   * <code>context</code> for the empty typesubstition
   * 
   * @param context the context calling this method
   * @param node the parent node to add the this child node to
   * @param substs already collected list of type substitutions (from earlier
   *          (VAR) rules)
   */
  public void contextAddProofNode ( DefaultUnifyProofContext context,
      final AbstractUnifyProofNode node, TypeSubstitutionList substs )
  {
    if ( context == null )
      throw new NullPointerException ( "context is null" ); //$NON-NLS-1$
    if ( node == null )
      throw new NullPointerException ( "node is null" ); //$NON-NLS-1$
    if ( substs == null )
      throw new NullPointerException ( "substs is null" ); //$NON-NLS-1$

    final DefaultUnifyProofNode child = new DefaultUnifyProofNode ( substs,
        null );

    context.addRedoAction ( new Runnable ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void run ()
      {
        node.add ( child );
        nodesWereInserted ( node, new int []
        { node.getIndex ( child ) } );
      }
    } );
    context.addUndoAction ( new Runnable ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void run ()
      {
        int nodeIndex = node.getIndex ( child );
        node.remove ( nodeIndex );
        nodesWereRemoved ( node, new int []
        { nodeIndex }, new Object []
        { child } );
      }
    } );
  }


  /**
   * Sets the current proof model index. This is a support operation, called by
   * {@link DefaultUnifyProofContext} whenever a new proof context is allocated.
   * 
   * @param pIndex the new index for the proof model.
   * @see #getIndex()
   * @see DefaultUnifyProofContext
   * @see DefaultUnifyProofContext#DefaultUnifyProofContext(UnifyProofModel)
   */
  public void setIndex ( int pIndex )
  {
    if ( pIndex < 1 )
    {
      throw new IllegalArgumentException ( "index is invalid" ); //$NON-NLS-1$
    }
    this.index = pIndex;
  }


  /**
   * Set the actual side overlap for the exported latex file
   * 
   * @param pOverlap the side overlapping
   */
  public void setOverlap ( int pOverlap )
  {
    this.overlap = pOverlap;
  }


  /**
   * Set the number of pages for the exported latex file
   * 
   * @param pPages number of pages
   */
  public void setPages ( int pPages )
  {
    this.pages = pPages;
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
   * @param latexStringBuilderFactory
   * @param indent
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#toLatexStringBuilder(de.unisiegen.tpml.core.latex.LatexStringBuilderFactory,
   *      int)
   */
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory latexStringBuilderFactory, int indent )
  {
    return null;
  }


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
   * @param prettyStringBuilderFactory
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable#toPrettyStringBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory)
   */
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory prettyStringBuilderFactory )
  {
    return null;
  }

}
