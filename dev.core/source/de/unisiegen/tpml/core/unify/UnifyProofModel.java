package de.unisiegen.tpml.core.unify;


import java.awt.Color;

import org.apache.log4j.Logger;

import de.unisiegen.tpml.core.AbstractProofModel;
import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;
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
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList;
import de.unisiegen.tpml.core.util.Theme;


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
   * The {@link Logger} for this class.
   * 
   * @see Logger
   */
  private static final Logger logger = Logger
      .getLogger ( UnifyProofModel.class );


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
    guessInternal ( ( DefaultUnifyProofNode ) node );
  }


  /**
   * Implementation of the {@link #guess(ProofNode)} and
   * {@link #guess(ProofNode)} methods.
   * 
   * @param node the proof node for which to guess the next step.
   * @throws ProofGuessException
   */
  private void guessInternal ( DefaultUnifyProofNode node )
      throws ProofGuessException
  {
    if ( node == null )
      throw new NullPointerException ( "node is null" ); //$NON-NLS-1$
    if ( !this.root.isNodeRelated ( node ) )
    {
      throw new IllegalArgumentException ( "The node is invalid for the model" ); //$NON-NLS-1$
    }

    // try to guess the next rule
    logger.debug ( "Trying to guess a rule for " + node ); //$NON-NLS-1$

//    ProofRule[] rules = this.ruleSet.getRules ();
    for ( ProofRule rule : this.ruleSet.getRules () )
//    for(int i = 0; i < rules.length; ++i)
    {
      try
      {
        // try to apply the rule to the specified node
        applyInternal ( ( UnifyProofRule ) rule, node );

        // remember that we guessed
        setCheating ( true );

        logger.debug ( "Successfully applied (" + rule + ") to " + node ); //$NON-NLS-1$ //$NON-NLS-2$
        return;
      }
      catch ( ProofRuleException e )
      {
        // rule failed to apply... so, next one, please
        logger.debug ( "Failed to apply (" + rule + ") to " + node, e ); //$NON-NLS-1$ //$NON-NLS-2$
        continue;
      }
    }
    // unable to guess next step
    logger.debug ( "Failed to find rule to apply to " + node ); //$NON-NLS-1$
    throw new ProofGuessException ( node );
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
     * catch ( UnificationException e ) { // revert the actions performed so far
     * context.revert (); // re-throw the exception as proof rule exception
     * throw new ProofRuleException ( node, rule, e ); }
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
   * @param node the parent node to add the child node to
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
   * @param node the parent node to add the child node to
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
   * Adds a new child proof node below the <code>node</code> using the
   * <code>context</code>. Here the node is not provable
   * 
   * @param context the context calling this method
   * @param node the parent node to add the child node to
   */
  public void contextAddProofNode ( final DefaultUnifyProofContext context,
      final AbstractUnifyProofNode node )
  {
    if ( context == null )
      throw new NullPointerException ( "context is null" ); //$NON-NLS-1$
    if ( node == null )
      throw new NullPointerException ( "node is null" ); //$NON-NLS-1$

    final DefaultUnifyProofNode child = new DefaultUnifyProofNode ( null, null );
    child.setProvable ( false );

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


  // FIXME: implement
  /**
   * Perform the given substitution to the given node
   * 
   * @param context the proof context calling this mehtod
   * @param s the {@link TypeSubstitution} to apply to all nodes in the proof
   *          tree.
   * @throws NullPointerException if <code>s</code> is <code>null</code>.
   * @SuppressWarnings ( "unchecked" ) void contextApplySubstitution (
   *                   DefaultTypeCheckerProofContext context, TypeSubstitution
   *                   s ) { if ( s == null ) { throw new NullPointerException (
   *                   "s is null" ); //$NON-NLS-1$ } // apply the substitution
   *                   s to all nodes in the proof node Enumeration nodes =
   *                   this.root.postorderEnumeration (); while (
   *                   nodes.hasMoreElements () ) { // determine the previous
   *                   settings for the node final AbstractTypeCheckerProofNode
   *                   nextNode = ( AbstractTypeCheckerProofNode ) nodes
   *                   .nextElement (); if ( nextNode instanceof
   *                   DefaultTypeCheckerExpressionProofNode ) { final
   *                   DefaultTypeCheckerExpressionProofNode node = (
   *                   DefaultTypeCheckerExpressionProofNode ) nextNode; final
   *                   TypeEnvironment oldEnvironment = node.getEnvironment ();
   *                   final Expression oldExpression = node.getExpression ();
   *                   final MonoType oldType = node.getType (); // determine
   *                   the new settings for the node final TypeEnvironment
   *                   newEnvironment = oldEnvironment.substitute ( s ); final
   *                   Expression newExpression = oldExpression.substitute ( s );
   *                   final MonoType newType = oldType.substitute ( s ); //
   *                   check if the old and new settings differ if (
   *                   !oldEnvironment.equals ( newEnvironment ) ||
   *                   !oldExpression.equals ( newExpression ) ||
   *                   !oldType.equals ( newType ) ) { // add the redo action
   *                   for the substitution context.addRedoAction ( new Runnable () {
   * @SuppressWarnings ( "synthetic-access" ) public void run () {
   *                   node.setEnvironment ( newEnvironment );
   *                   node.setExpression ( newExpression ); node.setType (
   *                   newType ); nodeChanged ( node ); } } ); // add the undo
   *                   action for the substitution context.addUndoAction ( new
   *                   Runnable () {
   * @SuppressWarnings ( "synthetic-access" ) public void run () {
   *                   node.setEnvironment ( oldEnvironment );
   *                   node.setExpression ( oldExpression ); node.setType (
   *                   oldType ); nodeChanged ( node ); } } ); } } } }
   */

  /**
   * Used to implement the
   * {@link DefaultUnifyProofContext#apply(UnifyProofRule, UnifyProofNode)}
   * method of the {@link DefaultTypeCheckerProofContext} class.
   * 
   * @param context the type checker proof context.
   * @param node the type checker node.
   * @param rule the type checker rule.
   * @see DefaultUnifyProofContext#apply(UnifyProofRule, UnifyProofNode)
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
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_UNIFY, 0,
        "\\textbf{\\color{" + LATEX_COLOR_KEYWORD + "}{unify}}" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_UNIFY_LPAREN, 0,
        "\\textbf{\\color{" + LATEX_COLOR_NONE + "}{\\ \\{}}" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_UNIFY_RPAREN, 0,
        "\\textbf{\\color{" + LATEX_COLOR_NONE + "}{\\ \\}}}" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_UNIFY_SUBSTITUTIONS_BEGIN,
        0, "\\multicolumn{2}{p{23.5cm}}" ) ); //$NON-NLS-1$
    commands.add ( new DefaultLatexCommand ( LATEX_UNIFY_NEW_NODE, 0,
        "\\\\[10mm]" ) ); //$NON-NLS-1$
    commands.add ( new DefaultLatexCommand ( LATEX_UNIFY_NEW_EQUATION, 0,
        "\\\\" ) ); //$NON-NLS-1$
    commands.add ( new DefaultLatexCommand ( LATEX_UNIFY_RULES_COMPLETED, 0,
        "&" ) ); //$NON-NLS-1$
    commands
        .add ( new DefaultLatexCommand (
            LATEX_UNIFY_PROOF_MODEL,
            1,
            "\\begin{longtable}{p{2cm}@{}p{23.5cm}@{}}#1\\end{longtable}", "model" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_UNIFY_RULE, 1,
        "\\mbox{\\centerline{\\scriptsize{#1}}}", "rule" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_UNIFY_EQUAL, 0,
        "\\mbox{\\centerline{\\LARGE=}}" ) ); //$NON-NLS-1$
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
    instructions.add ( new DefaultLatexInstruction (
        "\\newenvironment{unifynode}" //$NON-NLS-1$
            + "{\\begin{tabular}[t]{p{1.7cm}@{}p{21.8cm}@{}}}{\\end{tabular}}", //$NON-NLS-1$
        "The environment of the unify nodes" ) ); //$NON-NLS-1$
    instructions.add ( new DefaultLatexInstruction (
        "\\newenvironment{unifyrule}" //$NON-NLS-1$
            + "{\\begin{tabular}[b]{p{2cm}@{}}}{\\end{tabular}}", //$NON-NLS-1$
        "The environment of the unify rule" ) ); //$NON-NLS-1$
    Color colorKeyword = Theme.currentTheme ().getKeywordColor ();
    float red = ( float ) Math
        .round ( ( ( float ) colorKeyword.getRed () ) / 255 * 100 ) / 100;
    float green = ( float ) Math
        .round ( ( ( float ) colorKeyword.getGreen () ) / 255 * 100 ) / 100;
    float blue = ( float ) Math
        .round ( ( ( float ) colorKeyword.getBlue () ) / 255 * 100 ) / 100;
    instructions.add ( new DefaultLatexInstruction (
        "\\definecolor{" + LATEX_COLOR_KEYWORD + "}{rgb}{" //$NON-NLS-1$ //$NON-NLS-2$
            + red + "," //$NON-NLS-1$
            + green + "," //$NON-NLS-1$
            + blue + "}", LATEX_COLOR_KEYWORD + ": color of keywords" ) ); //$NON-NLS-1$ //$NON-NLS-2$
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
    packages.add ( LatexPackage.AMSMATH );
    packages.add ( LatexPackage.LONGTABLE );
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
    commands.add ( getLatexCommandsInternal ( ( UnifyProofNode ) this.root ) );
    return commands;
  }


  /**
   * Returns a set of needed latex commands for the given latex printable
   * {@link ProofNode}.
   * 
   * @param pNode The input {@link ProofNode}.
   * @return A set of needed latex commands for the given latex printable
   *         {@link ProofNode}.
   */
  private LatexCommandList getLatexCommandsInternal ( UnifyProofNode pNode )
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( pNode );
    commands.add ( pNode.getRule () );
    for ( int i = 0 ; i < pNode.getChildCount () ; i++ )
    {
      commands.add ( getLatexCommandsInternal ( pNode.getChildAt ( i ) ) );
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
    instructions
        .add ( getLatexInstructionsInternal ( ( UnifyProofNode ) this.root ) );
    return instructions;
  }


  /**
   * Returns a set of needed latex instructions for the given latex printable
   * {@link ProofNode}.
   * 
   * @param pNode The input {@link ProofNode}.
   * @return A set of needed latex instructions for the given latex printable
   *         {@link ProofNode}.
   */
  private LatexInstructionList getLatexInstructionsInternal (
      UnifyProofNode pNode )
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( pNode );
    instructions.add ( pNode.getRule () );
    for ( int i = 0 ; i < pNode.getChildCount () ; i++ )
    {
      instructions
          .add ( getLatexInstructionsInternal ( pNode.getChildAt ( i ) ) );
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
    for ( LatexPackage pack : getLatexPackagesStatic () )
    {
      packages.add ( pack );
    }
    for ( LatexPackage pack : getLatexPackagesInternal ( ( UnifyProofNode ) this.root ) )
    {
      packages.add ( pack );
    }
    return packages;
  }


  /**
   * Returns a set of needed latex packages for the given latex printable
   * {@link ProofNode}.
   * 
   * @param pNode The input {@link ProofNode}.
   * @return A set of needed latex packages for the given latex printable
   *         {@link ProofNode}.
   */
  private LatexPackageList getLatexPackagesInternal ( UnifyProofNode pNode )
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( pNode.getLatexPackages () );
    packages.add ( pNode.getRule () );
    for ( int i = 0 ; i < pNode.getChildCount () ; i++ )
    {
      packages.add ( getLatexPackagesInternal ( pNode.getChildAt ( i ) ) );
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
   * @param latexStringBuilderFactory
   * @param indent
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#toLatexStringBuilder(de.unisiegen.tpml.core.latex.LatexStringBuilderFactory,
   *      int)
   */
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory, int indent )
  {
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0,
        LATEX_UNIFY_PROOF_MODEL, indent, this.toPrettyString ().toString () );
    builder.addBuilderBegin ();
    builder.addSourceCodeBreak ( 0 );
    builder.addComment ( "no unify rule in the first node" ); //$NON-NLS-1$
    builder.addText ( LATEX_PREFIX_COMMAND
        + LATEX_UNIFY_RULES_COMPLETED );
    builder.addSourceCodeBreak ( 0 );
    builder.addText ( "\\begin{unifynode}" ); //$NON-NLS-1$
    builder.addSourceCodeBreak ( 0 );
    builder.addText ( LATEX_PREFIX_COMMAND + LATEX_KEY_UNIFY );
    builder.addSourceCodeBreak ( 0 );
    builder.addText ( LATEX_PREFIX_COMMAND + LATEX_UNIFY_LPAREN );
    builder.addSourceCodeBreak ( 0 );
    builder.addText ( "&" ); //$NON-NLS-1$
    builder.addSourceCodeBreak ( 0 );
    builder.addText ( "$" ); //$NON-NLS-1$
    DefaultUnifyProofNode node = ( DefaultUnifyProofNode ) this.root;
    builder.addBuilderWithoutBrackets ( node.getTypeEquationList ().getFirst ()
        .toLatexStringBuilder ( pLatexStringBuilderFactory,
            indent + LATEX_INDENT ), 0 );
    builder.addSourceCodeBreak ( 0 );
    builder.addText ( LATEX_PREFIX_COMMAND + LATEX_UNIFY_RPAREN );
    builder.addSourceCodeBreak ( 0 );
    builder.addText ( "$" ); //$NON-NLS-1$
    builder.addSourceCodeBreak ( 0 );
    builder.addText ( "\\end{unifynode}" ); //$NON-NLS-1$
    if ( this.root.getChildCount () > 0 )
    {
      builder.addSourceCodeBreak ( 0 );
      builder.addText ( LATEX_PREFIX_COMMAND + LATEX_UNIFY_NEW_NODE );
      builder.addSourceCodeBreak ( 0 );
    }
    for ( int i = 0 ; i < this.root.getChildCount () ; i++ )
    {
      toLatexStringBuilderInternal ( pLatexStringBuilderFactory, builder,
          ( DefaultUnifyProofNode ) this.root,
          ( DefaultUnifyProofNode ) this.root.getChildAt ( i ), indent );
    }
    builder.addBuilderEnd ();
    return builder;
  }


  /**
   * Build the latex string for the given <code>pCurrentNode</code>.
   * 
   * @param pLatexStringBuilderFactory The factory which should be used.
   * @param pLatexStringBuilder The {@link LatexStringBuilder} which should be
   *          completed.
   * @param pParentNode The parent of the current {@link ProofNode}. This node
   *          is needed because of his {@link ProofNode}s.
   * @param pCurrentNode The current {@link ProofNode}.
   * @param pIndent The indent of this object.
   */
  public final void toLatexStringBuilderInternal (
      LatexStringBuilderFactory pLatexStringBuilderFactory,
      LatexStringBuilder pLatexStringBuilder,
      DefaultUnifyProofNode pParentNode, DefaultUnifyProofNode pCurrentNode,
      int pIndent )
  {
    // First column
    pLatexStringBuilder.addText ( "\\begin{unifyrule}" ); //$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
    if ( pParentNode.getRule () != null )
    {
      pLatexStringBuilder.addText ( LATEX_PREFIX_COMMAND + LATEX_UNIFY_RULE );
      pLatexStringBuilder.addBuilder ( pParentNode.getRule ()
          .toLatexStringBuilder ( pLatexStringBuilderFactory,
              pIndent + LATEX_INDENT * 2 ), 0 );
      pLatexStringBuilder.addSourceCodeBreak ( 0 );
    }
    // pLatexStringBuilder.addText ( "$" );//$NON-NLS-1$
    // pLatexStringBuilder.addSourceCodeBreak ( 0 );
    // pLatexStringBuilder.addText ( LATEX_PREFIX_COMMAND
    // + LATEX_TYPE_INFERENCE_NEW_FORMULA );
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "$" );//$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( LATEX_PREFIX_COMMAND + LATEX_UNIFY_EQUAL );
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "\\end{unifyrule}" );//$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( LATEX_PREFIX_COMMAND
        + LATEX_UNIFY_RULES_COMPLETED );
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    // Second column
    pLatexStringBuilder.addText ( "\\begin{unifynode}" ); //$NON-NLS-1$

    if ( pCurrentNode.getTypeSubstitutions () != TypeSubstitutionList.EMPTY_LIST )
    {
      pLatexStringBuilder.addText ( LATEX_PREFIX_COMMAND
          + LATEX_UNIFY_SUBSTITUTIONS_BEGIN );
      pLatexStringBuilder.addSourceCodeBreak ( 0 );
      pLatexStringBuilder.addText ( "{" ); //$NON-NLS-1$
      pLatexStringBuilder.addSourceCodeBreak ( 0 );
      pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
      pLatexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE );
      pLatexStringBuilder.addText ( DefaultLatexStringBuilder
          .getIndent ( pIndent + LATEX_INDENT ) );
      pLatexStringBuilder.addText ( LATEX_LBRACKET );

      TypeSubstitutionList ll = pCurrentNode.getTypeSubstitutions ();
      while ( ll.hasNext () )
      {
        pLatexStringBuilder.addBuilderWithoutBrackets ( ll.getFirst ()
            .toLatexStringBuilder ( pLatexStringBuilderFactory,
                pIndent + LATEX_INDENT ), 0 );
        pLatexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE );
        pLatexStringBuilder.addText ( DefaultLatexStringBuilder
            .getIndent ( pIndent + LATEX_INDENT ) );
        pLatexStringBuilder.addText ( LATEX_COMMA );
        pLatexStringBuilder.addText ( LATEX_SPACE );
        pLatexStringBuilder.addBreak ();
      }

      pLatexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE );
      pLatexStringBuilder.addText ( DefaultLatexStringBuilder
          .getIndent ( pIndent + LATEX_INDENT ) );
      pLatexStringBuilder.addText ( LATEX_RBRACKET );
      pLatexStringBuilder.addSourceCodeBreak ( 0 );
      pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
      pLatexStringBuilder.addSourceCodeBreak ( 0 );
      pLatexStringBuilder.addText ( "}" ); //$NON-NLS-1$
      pLatexStringBuilder.addSourceCodeBreak ( 0 );
      pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
      if ( pCurrentNode.getTypeEquationList ().size () > 0 )
      {
        pLatexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE );
        pLatexStringBuilder.addText ( DefaultLatexStringBuilder
            .getIndent ( pIndent + LATEX_INDENT ) );
        pLatexStringBuilder.addText ( "$" );//$NON-NLS-1$
        pLatexStringBuilder.addSourceCodeBreak ( 0 );
        pLatexStringBuilder.addText ( LATEX_PREFIX_COMMAND
            + LATEX_UNIFY_NEW_EQUATION );
        pLatexStringBuilder.addSourceCodeBreak ( 0 );
        pLatexStringBuilder.addText ( "$" );//$NON-NLS-1$
      }
    }
    else
    {
      pLatexStringBuilder.addSourceCodeBreak ( 0 );
      pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
    }

    if ( pCurrentNode.getTypeEquationList ().size () > 0 )
    {
      pLatexStringBuilder.addSourceCodeBreak ( 0 );
      pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$
      pLatexStringBuilder.addSourceCodeBreak ( 0 );
      pLatexStringBuilder.addText ( LATEX_PREFIX_COMMAND + LATEX_KEY_UNIFY );
      pLatexStringBuilder.addSourceCodeBreak ( 0 );
      pLatexStringBuilder.addText ( LATEX_PREFIX_COMMAND + LATEX_UNIFY_LPAREN );
    }

    for ( TypeEquation equation : pCurrentNode.getTypeEquationList () )
    {
      pLatexStringBuilder.addSourceCodeBreak ( 0 );
      pLatexStringBuilder.addText ( "&" ); //$NON-NLS-1$
      pLatexStringBuilder.addSourceCodeBreak ( 0 );
      pLatexStringBuilder.addText ( "$" ); //$NON-NLS-1$

      pLatexStringBuilder.addBuilderWithoutBrackets ( equation.getSeenTypes ()
          .toLatexStringBuilder ( pLatexStringBuilderFactory,
              pIndent + LATEX_INDENT ), 0 );
      pLatexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE );
      pLatexStringBuilder.addText ( DefaultLatexStringBuilder
          .getIndent ( pIndent + LATEX_INDENT ) );
      pLatexStringBuilder.addText ( LATEX_SPACE );
      pLatexStringBuilder.addText ( LATEX_NAIL );
      pLatexStringBuilder.addText ( LATEX_SPACE );
      pLatexStringBuilder.addText ( "\\linebreak[3]" ); //$NON-NLS-1$

      pLatexStringBuilder.addBuilderWithoutBrackets ( equation
          .toLatexStringBuilder ( pLatexStringBuilderFactory, pIndent
              + LATEX_INDENT ), 0 );

      pLatexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE );
      pLatexStringBuilder.addText ( DefaultLatexStringBuilder
          .getIndent ( pIndent + LATEX_INDENT ) );
      pLatexStringBuilder.addText ( "$" );//$NON-NLS-1$
      pLatexStringBuilder.addSourceCodeBreak ( 0 );
      pLatexStringBuilder.addText ( LATEX_PREFIX_COMMAND
          + LATEX_UNIFY_NEW_EQUATION );
    }

    if ( pCurrentNode.getTypeEquationList ().size () > 0 )
    {
      pLatexStringBuilder.addSourceCodeBreak ( 0 );
      pLatexStringBuilder.addText ( LATEX_PREFIX_COMMAND + LATEX_UNIFY_RPAREN );
    }
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "$" );//$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 );
    pLatexStringBuilder.addText ( "\\end{unifynode}" );//$NON-NLS-1$
    for ( int i = 0 ; i < pCurrentNode.getChildCount () ; i++ )
    {
      pLatexStringBuilder.addSourceCodeBreak ( 0 );
      pLatexStringBuilder
          .addText ( LATEX_PREFIX_COMMAND + LATEX_UNIFY_NEW_NODE );
      pLatexStringBuilder.addSourceCodeBreak ( 0 );
      toLatexStringBuilderInternal ( pLatexStringBuilderFactory,
          pLatexStringBuilder, pCurrentNode,
          ( DefaultUnifyProofNode ) pCurrentNode.getChildAt ( i ), pIndent );
    }
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
   * @param prettyStringBuilderFactory
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable#toPrettyStringBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory)
   */
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory prettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = prettyStringBuilderFactory.newBuilder ( this,
        0 );
    builder.addBuilder ( this.root
        .toPrettyStringBuilder ( prettyStringBuilderFactory ), 0 );
    builder.addText ( PRETTY_LINE_BREAK );
    builder.addText ( PRETTY_CONTINUATION );
    return builder;
  }

}
