package de.unisiegen.tpml.core.typeinference ;


import java.util.ArrayList ;
import java.util.TreeSet ;
import org.apache.log4j.Logger ;
import de.unisiegen.tpml.core.AbstractProofModel ;
import de.unisiegen.tpml.core.AbstractProofNode ;
import de.unisiegen.tpml.core.AbstractProofRuleSet ;
import de.unisiegen.tpml.core.CannotRedoException ;
import de.unisiegen.tpml.core.CannotUndoException ;
import de.unisiegen.tpml.core.ProofGuessException ;
import de.unisiegen.tpml.core.ProofNode ;
import de.unisiegen.tpml.core.ProofRule ;
import de.unisiegen.tpml.core.ProofRuleException ;
import de.unisiegen.tpml.core.ProofStep ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.IsEmpty ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexPackage ;
import de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexInstruction ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPrintableNormal ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.TypeVariable ;


/**
 * The heart of the type inference. Type inference proof rules are supplied via
 * an {@link de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRuleSet}
 * that is passed to the constructor.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 * @see de.unisiegen.tpml.core.AbstractProofModel
 * @see de.unisiegen.tpml.core.typeinference.DefaultTypeInferenceProofContext
 * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode
 */
public final class TypeInferenceProofModel extends AbstractProofModel implements
    LatexPrintableNormal
{
  /**
   * The {@link Logger} for this class.
   * 
   * @see Logger
   */
  private static final Logger logger = Logger
      .getLogger ( TypeInferenceProofModel.class ) ;


  /**
   * The current proof index, which indicates the number of steps that have been
   * performed on the proof model so far (starting with one), and is used to
   * generate new unique type variables in the associated contexts.
   * 
   * @see #getIndex()
   * @see TypeCheckerProofContext#newTypeVariable()
   * @see TypeVariable
   */
  private int index = 1 ;


  /**
   * The new child to add
   */
  DefaultTypeInferenceProofNode child ;


  /**
   * Allocates a new <code>TypeInferenceProofModel</code> with the specified
   * <code>expression</code> as its root node.
   * 
   * @param expression the {@link Expression} for the root node.
   * @param pRuleSet the available type rules for the model.
   * @throws NullPointerException if either <code>expression</code> or
   *           <code>ruleSet</code> is <code>null</code>.
   * @see AbstractProofModel#AbstractProofModel(AbstractProofNode,
   *      AbstractProofRuleSet)
   */
  public TypeInferenceProofModel ( Expression expression ,
      AbstractProofRuleSet pRuleSet )
  {
    super ( new DefaultTypeInferenceProofNode (
        new TypeJudgement ( new DefaultTypeEnvironment ( ) , expression ,
            new TypeVariable ( 1 , 0 ) ) ,
        new ArrayList < TypeSubstitution > ( ) ) , pRuleSet ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofModel#addUndoableTreeEdit(de.unisiegen.tpml.core.AbstractProofModel.UndoableTreeEdit)
   */
  @ Override
  public void addUndoableTreeEdit ( UndoableTreeEdit edit )
  {
    // perform the redo of the edit
    edit.redo ( ) ;
    // add to the undo history
    super.addUndoableTreeEdit ( edit ) ;
  }


  /**
   * Applies the specified proof <code>rule</code> to the given
   * <code>node</code> in this proof model.
   * 
   * @param rule the type proof rule to apply.
   * @param node the type proof node to which to apply the <code>rule</code>.
   * @param type the type the user guessed for the <code>node</code> or
   *          <code>null</code> if the user didn't enter a type.
   * @param form The {@link TypeFormula}.
   * @param mode The choosen mode.
   * @throws ProofRuleException if the application of the <code>rule</code> to
   *           the <code>node</code> failed.
   * @see #guess(ProofNode)
   * @see #prove(ProofRule, ProofNode)
   */
  private void applyInternal ( TypeCheckerProofRule rule ,
      DefaultTypeInferenceProofNode node , MonoType type , TypeFormula form ,
      boolean mode ) throws ProofRuleException
  {
    // allocate a new TypeCheckerContext
    NewDefaultTypeInferenceProofContext context = new NewDefaultTypeInferenceProofContext (
        this , node ) ;
    this.index ++ ;
    DefaultTypeInferenceProofNode typeNode = node ;
    Exception e = null ;
    if ( form != null )
    {
      // try {
      // try to apply the rule to the specified node
      // context.setSubstitutions ( node.getSubstitution ( ) ) ;
      context.apply ( rule , form , type , mode , node ) ;
      ProofStep [ ] newSteps = new ProofStep [ 1 ] ;
      newSteps [ 0 ] = new ProofStep ( new IsEmpty ( ) , rule ) ;
      setUndoActions ( node , this.child , newSteps ) ;
      return ;
      /*
       * } catch (UnifyException e1){ context.revert ( ); throw new
       * ProofRuleException(e1.getMessage ( ), node, rule, e); }
       */
    }
    // Try actual Rule with all formulas of the actual node
    for ( TypeFormula formula : typeNode.getFormula ( ) )
    {
      try
      {
        // try to apply the rule to the specified node
        // context.setSubstitutions ( node.getSubstitution ( ) ) ;
        context.apply ( rule , formula , type , mode , node ) ;
        ProofStep [ ] newSteps = new ProofStep [ 1 ] ;
        newSteps [ 0 ] = new ProofStep ( new IsEmpty ( ) , rule ) ;
        setUndoActions ( node , this.child , newSteps ) ;
        return ;
      }
      catch ( ProofRuleException e1 )
      {
        // revert the actions performed so far
        context.revert ( ) ;
        // rembember first exception to rethrow
        if ( e == null ) e = e1 ;
        continue ;
      } /*
         * catch ( UnifyException e1 ) { context.revert ( ); if (e == null)
         * e=e1; }
         */
      /*
       * catch ( RuntimeException e1 ) { // revert the actions performed so far
       * context.revert ( ) ; // rembember first exception to rethrow if ( e ==
       * null ) e = e1 ; continue ; }
       */
    }
    this.index -- ;
    if ( e instanceof ProofRuleException )
    {
      // rethrow exception
      throw ( ProofRuleException ) e ;
    }
    else if ( e instanceof RuntimeException )
    {
      // rethrow exception
      throw ( RuntimeException ) e ;
    }
    else
    {
      // re-throw the exception as proof rule exception
      throw new ProofRuleException ( node , rule , e ) ;
    }
  }


  /**
   * Returns <code>true</code> if the expression for the <code>node</code>
   * contains syntactic sugar. If <code>recursive</code> is <code>true</code>
   * and the expression for the <code>node</code> is not syntactic sugar, its
   * sub expressions will also be checked.
   * 
   * @param node the proof node whose expression should be checked for syntactic
   *          sugar.
   * @param expression of the actual type formula which should be checke for
   *          syntactic sugar.
   * @param recursive signals if the expression should be checked recursive
   * @return <code>true</code> if the expression of the <code>node</code>
   *         contains syntactic sugar according to the language for this model.
   * @throws IllegalArgumentException if the <code>node</code> is invalid for
   *           this proof model.
   * @throws NullPointerException if the <code>node</code> is
   *           <code>null</code>.
   * @see #translateToCoreSyntax(TypeInferenceProofNode, boolean, boolean)
   * @see de.unisiegen.tpml.core.languages.LanguageTranslator#containsSyntacticSugar(Expression,
   *      boolean)
   */
  public boolean containsSyntacticSugar ( TypeInferenceProofNode node ,
      Expression expression , boolean recursive )
  {
    if ( node == null )
    {
      throw new NullPointerException ( "node is null" ) ; //$NON-NLS-1$
    }
    if ( ! this.root.isNodeRelated ( node ) )
    {
      throw new IllegalArgumentException ( "node is invalid" ) ; //$NON-NLS-1$
    }
    if ( this.translator == null )
    {
      this.translator = this.ruleSet.getLanguage ( ).newTranslator ( ) ;
    }
    return this.translator.containsSyntacticSugar ( expression , recursive ) ;
  }


  /**
   * Adds a new child proof node below the <code>node</code> using the
   * <code>context</code>, for the <code>environment</code>,
   * <code>expression</code> and <code>type</code>.
   * 
   * @param formulas the <code>TypeFormula</code>s of the new node.
   * @param subs The {@link DefaultTypeSubstitution}s.
   * @throws IllegalArgumentException if <code>node</code> is invalid for this
   *           tree.
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   */
  void contextAddProofNode ( final ArrayList < TypeFormula > formulas ,
      final ArrayList < TypeSubstitution > subs )
  {
    this.child = new DefaultTypeInferenceProofNode ( formulas , subs ) ;
  }


  /**
   * Used to implement the
   * {@link DefaultTypeInferenceProofContext#apply(TypeCheckerProofRule, TypeFormula, MonoType, boolean,DefaultTypeInferenceProofNode)}
   * method of the {@link DefaultTypeInferenceProofContext} class.
   * 
   * @param context the type inference proof context.
   * @param node the type inference node.
   * @param rule the type checker rule.
   * @param formula The {@link TypeFormula}.
   */
  void contextSetProofNodeRule ( @ SuppressWarnings ( "unused" )
  DefaultTypeInferenceProofContext context ,
      final DefaultTypeInferenceProofNode node ,
      final TypeCheckerProofRule rule , @ SuppressWarnings ( "unused" )
      TypeFormula formula )
  {
    node.setSteps ( new ProofStep [ ]
    { new ProofStep ( new IsEmpty ( ) , rule ) } ) ;
    nodeChanged ( node ) ;
  }


  /**
   * Returns the current proof model index, which is the number of steps already
   * performed on the model (starting with one) and used to allocate new, unique
   * {@link TypeVariable}s. It is incremented with every proof step performed
   * on the model.
   * 
   * @return the current index of the proof model.
   * @see TypeCheckerProofContext#newTypeVariable()
   * @see TypeVariable
   */
  public int getIndex ( )
  {
    return this.index ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.LatexPrintableNormal#getLatexCommands()
   */
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands
        .add ( new DefaultLatexCommand ( LATEX_TYPE_INFERENCE_PROOF_MODEL , 1 ,
            "\\begin{longtable}{p{2cm}p{23.5cm}}$#1$\\end{longtable}" , "model" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_TYPE_INFERENCE_EQUAL , 0 ,
        "\\mbox{\\centerline{\\LARGE=}}" ) ) ; //$NON-NLS-1$
    for ( LatexCommand command : getLatexCommandsInternal ( ( TypeInferenceProofNode ) this.root ) )
    {
      commands.add ( command ) ;
    }
    return commands ;
  }


  /**
   * Returns a set of needed latex commands for the given latex printable
   * {@link ProofNode}.
   * 
   * @param pNode The input {@link ProofNode}.
   * @return A set of needed latex commands for the given latex printable
   *         {@link ProofNode}.
   */
  private TreeSet < LatexCommand > getLatexCommandsInternal (
      TypeInferenceProofNode pNode )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    for ( LatexCommand command : pNode.getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    if ( pNode.getRule ( ) != null )
    {
      for ( LatexCommand command : pNode.getRule ( ).getLatexCommands ( ) )
      {
        commands.add ( command ) ;
      }
    }
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      for ( LatexCommand command : getLatexCommandsInternal ( pNode
          .getChildAt ( i ) ) )
      {
        commands.add ( command ) ;
      }
    }
    return commands ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.LatexPrintableNormal#getLatexInstructions()
   */
  public TreeSet < LatexInstruction > getLatexInstructions ( )
  {
    TreeSet < LatexInstruction > instructions = new TreeSet < LatexInstruction > ( ) ;
    for ( LatexInstruction instruction : getLatexInstructionsInternal ( ( TypeInferenceProofNode ) this.root ) )
    {
      instructions.add ( instruction ) ;
    }
    return instructions ;
  }


  /**
   * Returns a set of needed latex instructions for the given latex printable
   * {@link ProofNode}.
   * 
   * @param pNode The input {@link ProofNode}.
   * @return A set of needed latex instructions for the given latex printable
   *         {@link ProofNode}.
   */
  private TreeSet < LatexInstruction > getLatexInstructionsInternal (
      TypeInferenceProofNode pNode )
  {
    TreeSet < LatexInstruction > instructions = new TreeSet < LatexInstruction > ( ) ;
    for ( LatexInstruction pack : pNode.getLatexInstructions ( ) )
    {
      instructions.add ( pack ) ;
    }
    if ( pNode.getRule ( ) != null )
    {
      for ( LatexInstruction instruction : pNode.getRule ( )
          .getLatexInstructions ( ) )
      {
        instructions.add ( instruction ) ;
      }
    }
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      for ( LatexInstruction instruction : getLatexInstructionsInternal ( pNode
          .getChildAt ( i ) ) )
      {
        instructions.add ( instruction ) ;
      }
    }
    return instructions ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.LatexPrintableNormal#getLatexPackages()
   */
  public TreeSet < LatexPackage > getLatexPackages ( )
  {
    TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( ) ;
    packages.add ( new DefaultLatexPackage ( "longtable" ) ) ; //$NON-NLS-1$
    packages.add ( new DefaultLatexPackage ( "amsmath" ) ) ; //$NON-NLS-1$
    for ( LatexPackage pack : getLatexPackagesInternal ( ( TypeInferenceProofNode ) this.root ) )
    {
      packages.add ( pack ) ;
    }
    return packages ;
  }


  /**
   * Returns a set of needed latex packages for the given latex printable
   * {@link ProofNode}.
   * 
   * @param pNode The input {@link ProofNode}.
   * @return A set of needed latex packages for the given latex printable
   *         {@link ProofNode}.
   */
  private TreeSet < LatexPackage > getLatexPackagesInternal (
      TypeInferenceProofNode pNode )
  {
    TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( ) ;
    for ( LatexPackage pack : pNode.getLatexPackages ( ) )
    {
      packages.add ( pack ) ;
    }
    if ( pNode.getRule ( ) != null )
    {
      for ( LatexPackage pack : pNode.getRule ( ).getLatexPackages ( ) )
      {
        packages.add ( pack ) ;
      }
    }
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      for ( LatexPackage pack : getLatexPackagesInternal ( pNode
          .getChildAt ( i ) ) )
      {
        packages.add ( pack ) ;
      }
    }
    return packages ;
  }


  /**
   * get the rules of the actual proof rule set
   * 
   * @return ProofRuleSet[] with all rules
   * @see de.unisiegen.tpml.core.AbstractProofModel#getRules()
   */
  @ Override
  public ProofRule [ ] getRules ( )
  {
    return this.ruleSet.getRules ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see #guess(ProofNode, boolean)
   * @see de.unisiegen.tpml.core.AbstractProofModel#guess(de.unisiegen.tpml.core.ProofNode)
   */
  @ Override
  public void guess ( ProofNode node ) throws ProofGuessException
  {
    guessInternal ( ( DefaultTypeInferenceProofNode ) node , null , false ) ;
  }


  /**
   * guess method with additional boolean to signal which mode is chosen
   * 
   * @param node the actual subtyping proof node
   * @param mode the actual chosen mode
   * @throws ProofGuessException
   * @see de.unisiegen.tpml.core.AbstractProofModel#guess(de.unisiegen.tpml.core.ProofNode)
   */
  public void guess ( ProofNode node , boolean mode )
      throws ProofGuessException
  {
    guessInternal ( ( DefaultTypeInferenceProofNode ) node , null , mode ) ;
  }


  /**
   * Implementation of the {@link #guess(ProofNode)} and
   * {@link #guess(ProofNode, boolean)} methods.
   * 
   * @param node the proof node for which to guess the next step.
   * @param type the type that the user entered for this <code>node</code> or
   *          <code>null</code> to let the type inference algorithm guess the
   *          type.
   * @param mode The choosen mode.
   * @throws IllegalArgumentException if the <code>node</code> is invalid for
   *           this model.
   * @throws IllegalStateException if for some reason <code>node</code> cannot
   *           be proven.
   * @throws NullPointerException if <code>node</code> is <code>null</code>.
   * @throws ProofGuessException if the next proof step could not be guessed.
   * @see #guess(ProofNode)
   * @see #guess(ProofNode, boolean)
   */
  private void guessInternal ( DefaultTypeInferenceProofNode node ,
      MonoType type , boolean mode ) throws ProofGuessException
  {
    if ( node == null )
    {
      throw new NullPointerException ( "node is null" ) ; //$NON-NLS-1$
    }
    if ( node.getSteps ( ).length > 0 )
    {
      throw new IllegalArgumentException ( "The node is already completed" ) ; //$NON-NLS-1$
    }
    if ( ! this.root.isNodeRelated ( node ) )
    {
      throw new IllegalArgumentException ( "The node is invalid for the model" ) ; //$NON-NLS-1$
    }
    // try to guess the next rule
    logger.debug ( "Trying to guess a rule for " + node ) ; //$NON-NLS-1$
    for ( ProofRule rule : this.ruleSet.getRules ( ) )
    {
      try
      {
        // try to apply the rule to the specified node
        applyInternal ( ( TypeCheckerProofRule ) rule , node , type , null ,
            mode ) ;
        // remember that the user cheated
        setCheating ( true ) ;
        // yep, we did it
        logger.debug ( "Successfully applied (" + rule + ") to " + node ) ; //$NON-NLS-1$ //$NON-NLS-2$
        return ;
      }
      catch ( ProofRuleException e )
      {
        // rule failed to apply... so, next one, please
        logger.debug ( "Failed to apply (" + rule + ") to " + node , e ) ; //$NON-NLS-1$ //$NON-NLS-2$
        continue ;
      }
    }
    // unable to guess next step
    logger.debug ( "Failed to find rule to apply to " + node ) ; //$NON-NLS-1$
    throw new ProofGuessException ( node ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofModel#prove(de.unisiegen.tpml.core.ProofRule,
   *      de.unisiegen.tpml.core.ProofNode)
   */
  @ Override
  public void prove ( ProofRule rule , ProofNode pNode )
      throws ProofRuleException
  {
    if ( ! this.ruleSet.contains ( rule ) )
    {
      throw new IllegalArgumentException ( "The rule is invalid for the model" ) ; //$NON-NLS-1$
    }
    if ( ! this.root.isNodeRelated ( pNode ) )
    {
      throw new IllegalArgumentException ( "The node is invalid for the model" ) ; //$NON-NLS-1$
    }
    if ( pNode.getRules ( ).length > 0 )
    {
      throw new IllegalArgumentException ( "The node is already completed" ) ; //$NON-NLS-1$
    }
    DefaultTypeInferenceProofNode node = ( DefaultTypeInferenceProofNode ) pNode ;
    // try to apply the rule to the specified node
    applyInternal ( ( TypeCheckerProofRule ) rule , node , null , node
        .getFirstFormula ( ) , false ) ;
  }


  /**
   * prove method with additional boolean to signal which mode is chosen
   * 
   * @param rule the {@link ProofRule} to apply.
   * @param pNode the actual subtyping proof node
   * @param mode the actual chosen mode
   * @throws ProofRuleException
   * @see de.unisiegen.tpml.core.AbstractProofModel#prove(de.unisiegen.tpml.core.ProofRule,
   *      de.unisiegen.tpml.core.ProofNode)
   */
  public void prove ( ProofRule rule , ProofNode pNode , boolean mode )
      throws ProofRuleException
  {
    if ( ! this.ruleSet.contains ( rule ) )
    {
      throw new IllegalArgumentException ( "The rule is invalid for the model" ) ; //$NON-NLS-1$
    }
    if ( ! this.root.isNodeRelated ( pNode ) )
    {
      throw new IllegalArgumentException ( "The node is invalid for the model" ) ; //$NON-NLS-1$
    }
    if ( pNode.getRules ( ).length > 0 )
    {
      throw new IllegalArgumentException ( "The node is already completed" ) ; //$NON-NLS-1$
    }
    DefaultTypeInferenceProofNode node = ( DefaultTypeInferenceProofNode ) pNode ;
    // try to apply the rule to the specified node
    if ( mode )
      applyInternal ( ( TypeCheckerProofRule ) rule , node , null , node
          .getFirstFormula ( ) , mode ) ;
    else applyInternal ( ( TypeCheckerProofRule ) rule , node , null , null ,
        mode ) ;
  }


  /**
   * Added an the formula to apply the rule
   * 
   * @param rule proof rule to apply to the node
   * @param node the actual type inference proof node
   * @param formula choosen type formula for next step
   * @throws ProofRuleException
   * @see de.unisiegen.tpml.core.AbstractProofModel#prove(de.unisiegen.tpml.core.ProofRule,
   *      de.unisiegen.tpml.core.ProofNode)
   */
  public void prove ( ProofRule rule , ProofNode node ,
      @ SuppressWarnings ( "unused" )
      TypeFormula formula ) throws ProofRuleException
  {
    if ( ! this.ruleSet.contains ( rule ) )
    {
      throw new IllegalArgumentException ( "The rule is invalid for the model" ) ; //$NON-NLS-1$
    }
    if ( ! this.root.isNodeRelated ( node ) )
    {
      throw new IllegalArgumentException ( "The node is invalid for the model" ) ; //$NON-NLS-1$
    }
    if ( node.getRules ( ).length > 0 )
    {
      throw new IllegalArgumentException ( "The node is already completed" ) ; //$NON-NLS-1$
    }
    // try to apply the rule to the specified node
    applyInternal ( ( TypeCheckerProofRule ) rule ,
        ( DefaultTypeInferenceProofNode ) node , null , null , false ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofModel#redo()
   */
  @ Override
  public void redo ( ) throws CannotRedoException
  {
    super.redo ( ) ;
    this.index ++ ;
  }


  /**
   * mehtod used for DnD in the gui.
   * 
   * @param node type inference proof node which ownes the formula list to
   *          resort
   * @param move the type formula which should be moved
   * @param pos the new position of the moved type formula
   */
  public void resort ( final DefaultTypeInferenceProofNode node ,
      final TypeFormula move , final int pos )
  {
    final int oldPos = node.getFormula ( ).indexOf ( move ) ;
    addUndoableTreeEdit ( new UndoableTreeEdit ( )
    {
      public void redo ( )
      {
        node.getFormula ( ).remove ( move ) ;
        if ( pos < oldPos )
          node.getFormula ( ).add ( pos , move ) ;
        else node.getFormula ( ).add ( pos - 1 , move ) ;
      }


      public void undo ( )
      {
        node.getFormula ( ).remove ( move ) ;
        node.getFormula ( ).add ( oldPos , move ) ;
      }
    } ) ;
  }


  /**
   * Sets the current proof model index. This is a support operation, called by
   * {@link DefaultTypeInferenceProofContext} whenever a new proof context is
   * allocated.
   * 
   * @param pIndex the new index for the proof model.
   * @see #getIndex()
   * @see DefaultTypeInferenceProofContext
   */
  void setIndex ( int pIndex )
  {
    if ( pIndex < 1 )
    {
      throw new IllegalArgumentException ( "index is invalid" ) ; //$NON-NLS-1$
    }
    this.index = pIndex ;
  }


  /**
   * Set a new undo action for the added child
   * 
   * @param pNode the parent node to add the child
   * @param pChild the child which is added
   * @param newSteps the new proof steps for the parent node
   */
  private void setUndoActions ( final DefaultTypeInferenceProofNode pNode ,
      final DefaultTypeInferenceProofNode pChild , final ProofStep [ ] newSteps )
  {
    final ProofStep [ ] oldSteps = pNode.getSteps ( ) ;
    // add redo and undo options
    addUndoableTreeEdit ( new UndoableTreeEdit ( )
    {
      @ SuppressWarnings ( "synthetic-access" )
      public void redo ( )
      {
        pNode.add ( pChild ) ;
        // contextSetProofNodeRule ( context , pNode , rule , formula ) ;
        pNode.setSteps ( newSteps ) ;
        nodesWereInserted ( pNode , new int [ ]
        { pNode.getIndex ( pChild ) } ) ;
        setFinished ( ( ( DefaultTypeInferenceProofNode ) TypeInferenceProofModel.this.root )
            .isFinished ( ) ) ;
        nodeChanged ( pNode ) ;
      }


      @ SuppressWarnings ( "synthetic-access" )
      public void undo ( )
      {
        // update the "finished" state
        setFinished ( false ) ;
        // remove the child and revert the steps
        int [ ] indices =
        { pNode.getIndex ( pChild ) } ;
        pNode.removeAllChildren ( ) ;
        nodesWereRemoved ( pNode , indices , new Object [ ]
        { pChild } ) ;
        pNode.setSteps ( oldSteps ) ;
        nodeChanged ( pNode ) ;
      }
    } ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintableNormal#toLatexString()
   */
  public final LatexString toLatexString ( )
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance ( ) , 0 )
        .toLatexString ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintableNormal#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  public final LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
  {
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0 ,
        LATEX_TYPE_INFERENCE_PROOF_MODEL , pIndent ) ;
    builder.addBuilderBegin ( ) ;
    // First column
    builder.addSourceCodeBreak ( 0 ) ;
    builder.addText ( "$&$" ) ; //$NON-NLS-1$
    builder.addSourceCodeBreak ( 0 ) ;
    // Second column
    builder.addText ( "$\\begin{tabular}{p{23.5cm}}$" ) ; //$NON-NLS-1$
    builder.addSourceCodeBreak ( 0 ) ;
    builder.addText ( "$\\\\$" ) ; //$NON-NLS-1$
    DefaultTypeInferenceProofNode node = ( DefaultTypeInferenceProofNode ) this.root ;
    for ( int i = 0 ; i < node.getFormula ( ).size ( ) ; i ++ )
    {
      if ( node.getFormula ( ).get ( i ) instanceof TypeEquationTypeInference )
      {
        TypeEquationTypeInference equation = ( TypeEquationTypeInference ) node
            .getFormula ( ).get ( i ) ;
        builder.addBuilderWithoutBrackets ( equation.getSeenTypes ( )
            .toLatexStringBuilder ( pLatexStringBuilderFactory ,
                pIndent + LATEX_INDENT ) , 0 ) ;
        builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
        builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
            + LATEX_INDENT )
            + LATEX_SPACE ) ;
        builder.addText ( LATEX_NAIL ) ;
        builder.addText ( LATEX_SPACE ) ;
      }
      builder.addBuilderWithoutBrackets ( node.getFormula ( ).get ( i )
          .toLatexStringBuilder ( pLatexStringBuilderFactory ,
              pIndent + LATEX_INDENT ) , 0 ) ;
      if ( i < node.getFormula ( ).size ( ) - 1 )
      {
        builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
        builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
            + LATEX_INDENT ) ) ;
        builder.addText ( "$\\\\$" ) ;//$NON-NLS-1$
      }
    }
    builder.addSourceCodeBreak ( 0 ) ;
    builder.addText ( "$\\end{tabular}$" ) ; //$NON-NLS-1$
    builder.addSourceCodeBreak ( 0 ) ;
    builder.addText ( "$\\\\[5mm]$" ) ; //$NON-NLS-1$
    builder.addSourceCodeBreak ( 0 ) ;
    for ( int i = 0 ; i < this.root.getChildCount ( ) ; i ++ )
    {
      toLatexStringBuilderInternal ( pLatexStringBuilderFactory , builder ,
          ( DefaultTypeInferenceProofNode ) this.root ,
          ( DefaultTypeInferenceProofNode ) this.root.getChildAt ( i ) ,
          pIndent ) ;
    }
    builder.addBuilderEnd ( ) ;
    return builder ;
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
      LatexStringBuilderFactory pLatexStringBuilderFactory ,
      LatexStringBuilder pLatexStringBuilder ,
      DefaultTypeInferenceProofNode pParentNode ,
      DefaultTypeInferenceProofNode pCurrentNode , int pIndent )
  {
    // First column
    pLatexStringBuilder.addText ( "$\\begin{tabular}{p{2cm}}$" ) ; //$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 ) ;
    if ( pParentNode.getRule ( ) != null )
    {
      pLatexStringBuilder.addText ( pParentNode.getRule ( ).toLatexString ( )
          .toString ( ) ) ;
      pLatexStringBuilder.addSourceCodeBreak ( 0 ) ;
    }
    pLatexStringBuilder.addText ( "$\\\\$" ) ;//$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 ) ;
    pLatexStringBuilder.addText ( LATEX_PREFIX_COMMAND
        + LATEX_TYPE_INFERENCE_EQUAL ) ;
    pLatexStringBuilder.addSourceCodeBreak ( 0 ) ;
    pLatexStringBuilder.addText ( "$\\end{tabular}$" ) ;//$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 ) ;
    pLatexStringBuilder.addText ( "$&$" ) ;//$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 ) ;
    // Second column
    pLatexStringBuilder.addText ( "$\\begin{tabular}{p{23.5cm}}$" ) ;//$NON-NLS-1$
    pLatexStringBuilder.addSourceCodeBreak ( 0 ) ;
    pLatexStringBuilder.addText ( "$\\\\$" ) ;//$NON-NLS-1$
    if ( pCurrentNode.getSubstitution ( ).size ( ) > 0 )
    {
      pLatexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
      pLatexStringBuilder.addText ( DefaultLatexStringBuilder
          .getIndent ( pIndent + LATEX_INDENT ) ) ;
      pLatexStringBuilder.addText ( LATEX_LBRACKET ) ;
      for ( int i = 0 ; i < pCurrentNode.getSubstitution ( ).size ( ) ; i ++ )
      {
        pLatexStringBuilder.addBuilderWithoutBrackets ( pCurrentNode
            .getSubstitution ( ).get ( i ).toLatexStringBuilder (
                pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
        if ( i < pCurrentNode.getSubstitution ( ).size ( ) - 1 )
        {
          pLatexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
          pLatexStringBuilder.addText ( DefaultLatexStringBuilder
              .getIndent ( pIndent + LATEX_INDENT ) ) ;
          pLatexStringBuilder.addText ( LATEX_COMMA ) ;
          pLatexStringBuilder.addText ( LATEX_SPACE ) ;
          pLatexStringBuilder.addBreak ( ) ;
        }
      }
      pLatexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
      pLatexStringBuilder.addText ( DefaultLatexStringBuilder
          .getIndent ( pIndent + LATEX_INDENT ) ) ;
      pLatexStringBuilder.addText ( LATEX_RBRACKET ) ;
      if ( pCurrentNode.getFormula ( ).size ( ) > 0 )
      {
        pLatexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
        pLatexStringBuilder.addText ( DefaultLatexStringBuilder
            .getIndent ( pIndent + LATEX_INDENT ) ) ;
        pLatexStringBuilder.addText ( "$\\\\$" ) ;//$NON-NLS-1$
      }
    }
    for ( int i = 0 ; i < pCurrentNode.getFormula ( ).size ( ) ; i ++ )
    {
      if ( pCurrentNode.getFormula ( ).get ( i ) instanceof TypeEquationTypeInference )
      {
        TypeEquationTypeInference equation = ( TypeEquationTypeInference ) pCurrentNode
            .getFormula ( ).get ( i ) ;
        pLatexStringBuilder.addBuilderWithoutBrackets ( equation
            .getSeenTypes ( ).toLatexStringBuilder (
                pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
        pLatexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
        pLatexStringBuilder.addText ( DefaultLatexStringBuilder
            .getIndent ( pIndent + LATEX_INDENT ) ) ;
        pLatexStringBuilder.addText ( LATEX_SPACE ) ;
        pLatexStringBuilder.addText ( LATEX_NAIL ) ;
        pLatexStringBuilder.addText ( LATEX_SPACE ) ;
        pLatexStringBuilder.addBreak ( ) ;
      }
      pLatexStringBuilder.addBuilderWithoutBrackets ( pCurrentNode
          .getFormula ( ).get ( i ).toLatexStringBuilder (
              pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
      if ( i < pCurrentNode.getFormula ( ).size ( ) - 1 )
      {
        pLatexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
        pLatexStringBuilder.addText ( DefaultLatexStringBuilder
            .getIndent ( pIndent + LATEX_INDENT ) ) ;
        pLatexStringBuilder.addText ( "$\\\\$" ) ;//$NON-NLS-1$
      }
    }
    pLatexStringBuilder.addSourceCodeBreak ( 0 ) ;
    pLatexStringBuilder.addText ( "$\\end{tabular}$" ) ;//$NON-NLS-1$
    for ( int i = 0 ; i < pCurrentNode.getChildCount ( ) ; i ++ )
    {
      pLatexStringBuilder.addSourceCodeBreak ( 0 ) ;
      pLatexStringBuilder.addText ( "$\\\\[10mm]$" ) ; //$NON-NLS-1$
      pLatexStringBuilder.addSourceCodeBreak ( 0 ) ;
      toLatexStringBuilderInternal ( pLatexStringBuilderFactory ,
          pLatexStringBuilder , pCurrentNode , pCurrentNode.getChildAt ( i ) ,
          pIndent ) ;
    }
  }


  /**
   * Translates the expression for the <code>node</code> to core syntax
   * according to the language for this model. If <code>recursive</code> is
   * <code>true</code>, all sub expressions will also be translated to core
   * syntax, otherwise only the outermost expression will be translated.
   * 
   * @param pNode the proof node whose expression should be translated to core
   *          syntax.
   * @param recursive whether to translate the expression recursively.
   * @param all signals if user wants to translate one expression or all
   * @throws IllegalArgumentException if the <code>node</code> is invalid for
   *           this proof model, or the <code>node</code>'s expression does
   *           not contain syntactic sugar.
   * @throws IllegalStateException if any steps were performed on the
   *           <code>node</code> already.
   * @throws NullPointerException if the <code>node</code> is
   *           <code>null</code>.
   * @see #containsSyntacticSugar(TypeInferenceProofNode, Expression,boolean)
   * @see de.unisiegen.tpml.core.languages.LanguageTranslator#translateToCoreSyntax(Expression,
   *      boolean)
   */
  public void translateToCoreSyntax ( TypeInferenceProofNode pNode ,
      final boolean recursive , final boolean all )
  {
    final DefaultTypeInferenceProofNode node = ( DefaultTypeInferenceProofNode ) pNode ;
    final ArrayList < TypeFormula > oldFormulas = pNode.getAllFormulas ( ) ;
    if ( all )
    {
      final ArrayList < TypeFormula > newFormulas = new ArrayList < TypeFormula > ( ) ;
      for ( TypeFormula formula : node.getAllFormulas ( ) )
      {
        if ( formula instanceof TypeJudgement )
        {
          try
          {
            Expression expression = translateToCoreSyntaxInternal ( node ,
                ( TypeJudgement ) formula , recursive ) ;
            newFormulas.add ( new TypeJudgement ( formula.getEnvironment ( ) ,
                expression , formula.getType ( ) ) ) ;
          }
          catch ( IllegalStateException e )
          {
            // Nothing to do here
          }
        }
      }
      addUndoableTreeEdit ( new UndoableTreeEdit ( )
      {
        @ SuppressWarnings ( "synthetic-access" )
        public void redo ( )
        {
          node.setFormula ( newFormulas ) ;
          nodeChanged ( node ) ;
        }


        @ SuppressWarnings ( "synthetic-access" )
        public void undo ( )
        {
          node.setFormula ( oldFormulas ) ;
          nodeChanged ( node ) ;
        }
      } ) ;
    }
    else
    {
      if ( node.getFirstFormula ( ) instanceof TypeJudgement )
      {
        final TypeJudgement judgement = ( TypeJudgement ) node
            .getFirstFormula ( ) ;
        final Expression oldExpression = judgement.getExpression ( ) ;
        final Expression newExpression = translateToCoreSyntaxInternal ( node ,
            ( TypeJudgement ) node.getFirstFormula ( ) , recursive ) ;
        addUndoableTreeEdit ( new UndoableTreeEdit ( )
        {
          @ SuppressWarnings ( "synthetic-access" )
          public void redo ( )
          {
            judgement.setExpression ( newExpression ) ;
            nodeChanged ( node ) ;
          }


          @ SuppressWarnings ( "synthetic-access" )
          public void undo ( )
          {
            judgement.setExpression ( oldExpression ) ;
            nodeChanged ( node ) ;
          }
        } ) ;
      }
    }
  }


  /**
   * Translates to core syntax.
   * 
   * @param node The {@link TypeInferenceProofNode}.
   * @param judgement The {@link TypeJudgement}.
   * @param recursive True, if recursive.
   * @return The translated {@link Expression}.
   */
  Expression translateToCoreSyntaxInternal ( TypeInferenceProofNode node ,
      TypeJudgement judgement , boolean recursive )
  {
    // verify that the node actually contains syntactic sugar
    if ( ! containsSyntacticSugar ( node , judgement.getExpression ( ) ,
        recursive ) )
    {
      throw new IllegalArgumentException (
          "node does not contain syntactic sugar" ) ; //$NON-NLS-1$
    }
    // verify that no actions were performed on the node
    if ( node.getSteps ( ).length > 0 )
    {
      throw new IllegalStateException ( "steps have been performed on node" ) ; //$NON-NLS-1$
    }
    // translate the expression to core syntax
    return this.translator.translateToCoreSyntax ( judgement.getExpression ( ) ,
        recursive ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofModel#undo()
   */
  @ Override
  public void undo ( ) throws CannotUndoException
  {
    super.undo ( ) ;
    this.index -- ;
  }
}
