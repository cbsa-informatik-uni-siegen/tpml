package de.unisiegen.tpml.core.unify;


import java.util.Enumeration;

import de.unisiegen.tpml.core.AbstractProofModel;
import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.UnifyProofStep;
import de.unisiegen.tpml.core.entities.TypeEquationList;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerExpressionProofNode;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.typechecker.UnificationException;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList;
import de.unisiegen.tpml.core.types.MonoType;


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
  public UnifyProofModel ( AbstractProofNode root, AbstractProofRuleSet ruleSet )
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
    if ( !this.ruleSet.contains ( rule ) )
    {
      throw new IllegalArgumentException ( "The rule is invalid for the model" ); //$NON-NLS-1$
    }
    if ( !this.root.isNodeRelated ( node ) )
    {
      throw new IllegalArgumentException ( "The node is invalid for the model" ); //$NON-NLS-1$
    }
    if ( node.getRules ().length > 0 )
    {
      throw new IllegalArgumentException ( "The node is already completed" ); //$NON-NLS-1$
    }
    applyInternal ( ( UnifyProofRule ) rule, ( AbstractUnifyProofNode ) node );
  }


  /**
   * Applies the specified proof <code>rule</code> to the given
   * <code>node</code> in this proof model.
   * 
   * @param rule the type proof rule to apply.
   * @param node the type proof node to which to apply the <code>rule</code>.
   * @throws ProofRuleException if the application of the <code>rule</code> to
   *           the <code>node</code> failed.
   * @see #guess(ProofNode)
   * @see #prove(ProofRule, ProofNode)
   */
  private void applyInternal ( UnifyProofRule rule, AbstractUnifyProofNode node )
      throws ProofRuleException
  {
    // allocate a new TypeCheckerContext
    DefaultUnifyProofContext context;
    context = new DefaultUnifyProofContext ( this );
    try
    {
      // try to apply the rule to the node
      context.apply ( rule, node );
      // check if we are finished
      final UnifyProofNode rootNode = ( UnifyProofNode ) getRoot ();
      context.addRedoAction ( new Runnable ()
      {

        @SuppressWarnings ( "synthetic-access" )
        public void run ()
        {
          setFinished ( rootNode.isFinished () );
        }
      } );
      context.addUndoAction ( new Runnable ()
      {

        @SuppressWarnings ( "synthetic-access" )
        public void run ()
        {
          setFinished ( false );
        }
      } );
      // determine the redo and undo actions from the context
      final Runnable redoActions = context.getRedoActions ();
      final Runnable undoActions = context.getUndoActions ();
      // record the undo edit action for this proof step
      addUndoableTreeEdit ( new UndoableTreeEdit ()
      {

        public void redo ()
        {
          redoActions.run ();
        }


        public void undo ()
        {
          undoActions.run ();
        }
      } );
    }
    catch ( ProofRuleException e )
    {
      // revert the actions performed so far
      context.revert ();
      // re-throw the exception
      throw e;
    }
    /*
    catch ( UnificationException e )
    {
      // revert the actions performed so far
      context.revert ();
      // re-throw the exception as proof rule exception
      throw new ProofRuleException ( node, rule, e );
    }
    */
    catch ( RuntimeException e )
    {
      // revert the actions performed so far
      context.revert ();
      // re-throw the exception
      throw e;
    }
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
  
  //FIXME: implement
  /**
   * Perform the given substitution to the given node
   * 
   * @param context the proof context calling this mehtod
   * @param s the {@link TypeSubstitution} to apply to all nodes in the proof
   *          tree.
   * @throws NullPointerException if <code>s</code> is <code>null</code>.
   
  @SuppressWarnings ( "unchecked" )
  void contextApplySubstitution ( DefaultTypeCheckerProofContext context,
      TypeSubstitution s )
  {
    if ( s == null )
    {
      throw new NullPointerException ( "s is null" ); //$NON-NLS-1$
    }
    // apply the substitution s to all nodes in the proof node
    Enumeration nodes = this.root.postorderEnumeration ();
    while ( nodes.hasMoreElements () )
    {
      // determine the previous settings for the node
      final AbstractTypeCheckerProofNode nextNode = ( AbstractTypeCheckerProofNode ) nodes
          .nextElement ();
      if ( nextNode instanceof DefaultTypeCheckerExpressionProofNode )
      {
        final DefaultTypeCheckerExpressionProofNode node = ( DefaultTypeCheckerExpressionProofNode ) nextNode;
        final TypeEnvironment oldEnvironment = node.getEnvironment ();
        final Expression oldExpression = node.getExpression ();
        final MonoType oldType = node.getType ();
        // determine the new settings for the node
        final TypeEnvironment newEnvironment = oldEnvironment.substitute ( s );
        final Expression newExpression = oldExpression.substitute ( s );
        final MonoType newType = oldType.substitute ( s );
        // check if the old and new settings differ
        if ( !oldEnvironment.equals ( newEnvironment )
            || !oldExpression.equals ( newExpression )
            || !oldType.equals ( newType ) )
        {
          // add the redo action for the substitution
          context.addRedoAction ( new Runnable ()
          {

            @SuppressWarnings ( "synthetic-access" )
            public void run ()
            {
              node.setEnvironment ( newEnvironment );
              node.setExpression ( newExpression );
              node.setType ( newType );
              nodeChanged ( node );
            }
          } );
          // add the undo action for the substitution
          context.addUndoAction ( new Runnable ()
          {

            @SuppressWarnings ( "synthetic-access" )
            public void run ()
            {
              node.setEnvironment ( oldEnvironment );
              node.setExpression ( oldExpression );
              node.setType ( oldType );
              nodeChanged ( node );
            }
          } );
        }
      }
    }
  }
  */
  
  
  /**
   * Used to implement the
   * {@link DefaultUnifyProofContext#apply(UnifyProofRule, UnifyProofNode)}
   * method of the {@link DefaultTypeCheckerProofContext} class.
   * 
   * @param context the type checker proof context.
   * @param node the type checker node.
   * @param rule the type checker rule.
   * @see DefaultUnifyProofContext#apply(UnifyProofRule,
   *      UnifyProofNode)
   */
  public void contextSetProofNodeRule ( DefaultUnifyProofContext context,
      final AbstractUnifyProofNode node, final UnifyProofRule rule )
  {
    final UnifyProofStep [] oldSteps = node.getSteps ();
    context.addRedoAction ( new Runnable ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void run ()
      {
        node.setSteps ( new UnifyProofStep []
        { new UnifyProofStep ( node.getTypeEquationList (), rule ) } );
        nodeChanged ( node );
      }
    } );
    context.addUndoAction ( new Runnable ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void run ()
      {
        node.setSteps ( oldSteps );
        nodeChanged ( node );
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
