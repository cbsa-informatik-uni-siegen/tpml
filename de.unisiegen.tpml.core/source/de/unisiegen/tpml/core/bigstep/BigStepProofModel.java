package de.unisiegen.tpml.core.bigstep;

import java.text.MessageFormat;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofModel;
import de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofNode;
import de.unisiegen.tpml.core.interpreters.Store;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction;
import de.unisiegen.tpml.core.latex.DefaultLatexPackage;
import de.unisiegen.tpml.core.latex.LatexCommand;
import de.unisiegen.tpml.core.latex.LatexCommandNames;
import de.unisiegen.tpml.core.latex.LatexInstruction;
import de.unisiegen.tpml.core.latex.LatexPackage;
import de.unisiegen.tpml.core.latex.LatexPrintableNormal;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;

/**
 * This class is the heart of the big step interpreter. A big step proof model
 * consists of any number of
 * {@link de.unisiegen.tpml.core.bigstep.BigStepProofNode}s, which together
 * make up the big step proof.
 * 
 * @author Benedikt Meurer
 * @version $Rev:878 $
 * @see de.unisiegen.tpml.core.bigstep.BigStepProofNode
 * @see de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofModel
 */
public final class BigStepProofModel extends AbstractInterpreterProofModel implements LatexPrintableNormal, LatexCommandNames {
	//
	// Constants
	//
	/**
	 * The {@link Logger} for this class.
	 * 
	 * @see Logger
	 */
	private static final Logger logger = Logger.getLogger ( BigStepProofModel.class );

	//
	// Constructor
	//
	/**
	 * Allocates a new <code>BigStepProofModel</code> with the specified
	 * <code>expression</code> as its root node.
	 * 
	 * @param expression the {@link Expression} for the root node.
	 * @param pRuleSet the available big step proof rules for the model.
	 * @throws NullPointerException if either <code>expression</code> or
	 *           <code>ruleSet</code> is <code>null</code>.
	 * @see AbstractInterpreterProofModel#AbstractInterpreterProofModel(AbstractInterpreterProofNode,
	 *      AbstractProofRuleSet)
	 */
	public BigStepProofModel ( Expression expression, AbstractProofRuleSet pRuleSet ) {
		super ( new DefaultBigStepProofNode ( expression ), pRuleSet );
	}

	//
	// Actions
	//
	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.AbstractProofModel#guess(de.unisiegen.tpml.core.ProofNode)
	 */
	@Override
	public void guess ( ProofNode node ) throws ProofGuessException {
		if ( node == null ) {
			throw new NullPointerException ( "node is null" ); //$NON-NLS-1$
		}
		if ( node.getRules ( ).length > 0 ) {
			throw new IllegalArgumentException ( Messages.getString ( "BigStep.0" ) ); //$NON-NLS-1$
		}
		if ( !this.root.isNodeRelated ( node ) ) {
			throw new IllegalArgumentException ( "The node is invalid for the model" ); //$NON-NLS-1$
		}
		// try to guess the next rule
		logger.debug ( "Trying to guess a rule for " + node ); //$NON-NLS-1$
		for ( ProofRule rule : this.ruleSet.getRules ( ) ) { // MUST be the getRules() from the ProofRuleSet
			try {
				// cast node to a DefaultBigStepProofNode
				DefaultBigStepProofNode current = ( DefaultBigStepProofNode ) node;
				// (APP) is a special case, because (APP) can always be applied to
				// applications,
				// which can turn into trouble for expressions such as "1 + true"
				if ( rule.getName ( ).equals ( "APP" ) ) //$NON-NLS-1$
				{
					// determine the parent node
					BigStepProofNode parent = current.getParent ( );
					// in order to avoid endless guessing of (APP) for said expressions,
					// we check if the
					// parent node has exactly the same expression and (APP) was applied
					// to it, and if
					// so, skip the (APP) rule here
					if ( parent != null && parent.getRule ( ).equals ( rule )
							&& parent.getExpression ( ).equals ( current.getExpression ( ) ) ) {
						// log the details of the problem...
						logger.debug ( MessageFormat.format ( "Detected endless guessing of ({0}) for {1}", rule, current ) ); //$NON-NLS-1$
						// ...and skip the (APP) rule for the guess operation
						continue;
					}
				}
				// try to apply the rule to the specified node
				apply ( ( BigStepProofRule ) rule, current );
				// remember that the user cheated
				setCheating ( true );
				// yep, we did it
				logger.debug ( MessageFormat.format ( "Successfully applied ({0}) to {1}", rule, node ) ); //$NON-NLS-1$
				return;
			} catch ( ProofRuleException e ) {
				// rule failed to apply... so, next one, please
				logger.debug ( MessageFormat.format ( "Failed to apply ({0}) to {1}", rule, node ), e ); //$NON-NLS-1$
				continue;
			}
		}
		// unable to guess next step
		logger.debug ( "Failed to find rule to apply to " + node ); //$NON-NLS-1$
		throw new ProofGuessException ( Messages.getString ( "InterpreterModel.0" ), node ); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.AbstractProofModel#prove(de.unisiegen.tpml.core.ProofRule,
	 *      de.unisiegen.tpml.core.ProofNode)
	 */
	@Override
	public void prove ( ProofRule rule, ProofNode node ) throws ProofRuleException {
		if ( rule == null ) {
			throw new NullPointerException ( "rule is null" ); //$NON-NLS-1$
		}
		if ( node == null ) {
			throw new NullPointerException ( "node is null" ); //$NON-NLS-1$
		}
		if ( !this.ruleSet.contains ( rule ) ) {
			throw new IllegalArgumentException ( "The rule is invalid for the model" ); //$NON-NLS-1$
		}
		if ( !this.root.isNodeRelated ( node ) ) {
			throw new IllegalArgumentException ( "The node is invalid for the model" ); //$NON-NLS-1$
		}
		if ( node.getRules ( ).length > 0 ) {
			throw new IllegalArgumentException ( Messages.getString ( "BigStep.0" ) ); //$NON-NLS-1$
		}
		// try to apply the rule to the specified node
		apply ( ( BigStepProofRule ) rule, ( DefaultBigStepProofNode ) node );
	}

	//
	// Rule application
	//
	/**
	 * Applies the <code>rule</code> to the <code>node</code>.
	 * 
	 * @param rule the big step proof rule to apply to <code>node</code>.
	 * @param node the big step proof node to which to apply the <code>rule</code>.
	 * @throws ProofRuleException if the <code>rule</code> could not be applied
	 *           to <code>node</code>.
	 */
	private void apply ( final BigStepProofRule rule, final DefaultBigStepProofNode node ) throws ProofRuleException {
		// allocate a new big step proof context
		DefaultBigStepProofContext context = new DefaultBigStepProofContext ( this );
		try {
			// try to apply the rule to the node
			context.apply ( rule, node );
			// check if we are finished
			final BigStepProofNode tmpRoot = ( BigStepProofNode ) getRoot ( );
			context.addRedoAction ( new Runnable ( ) {
				@SuppressWarnings ( "synthetic-access" )
				public void run ( ) {
					setFinished ( tmpRoot.isFinished ( ) );
				}
			} );
			context.addUndoAction ( new Runnable ( ) {
				@SuppressWarnings ( "synthetic-access" )
				public void run ( ) {
					setFinished ( false );
				}
			} );
			// determine the redo and undo actions from the context
			final Runnable redoActions = context.getRedoActions ( );
			final Runnable undoActions = context.getUndoActions ( );
			// record the undo edit action for this proof step
			addUndoableTreeEdit ( new UndoableTreeEdit ( ) {
				public void redo ( ) {
					redoActions.run ( );
				}

				public void undo ( ) {
					undoActions.run ( );
				}
			} );
		} catch ( ProofRuleException e ) {
			// revert the actions performed so far
			context.revert ( );
			// re-throw the exception
			throw e;
		}
	}

	//
	// Proof context support
	//
	/**
	 * Used to implement the
	 * {@link BigStepProofContext#addProofNode(BigStepProofNode, Expression, Store)}
	 * method of the {@link DefaultBigStepProofContext} class.
	 * 
	 * @param context the big step proof context.
	 * @param node the big step node.
	 * @param child the new big step node.
	 * @see BigStepProofContext#addProofNode(BigStepProofNode, Expression, Store)
	 */
	void contextAddProofNode ( DefaultBigStepProofContext context, final DefaultBigStepProofNode node,
			final DefaultBigStepProofNode child ) {
		context.addRedoAction ( new Runnable ( ) {
			@SuppressWarnings ( "synthetic-access" )
			public void run ( ) {
				node.add ( child );
				nodesWereInserted ( node, new int[] { node.getIndex ( child ) } );
			}
		} );
		context.addUndoAction ( new Runnable ( ) {
			@SuppressWarnings ( "synthetic-access" )
			public void run ( ) {
				int index = node.getIndex ( child );
				node.remove ( index );
				nodesWereRemoved ( node, new int[] { index }, new Object[] { child } );
			}
		} );
	}

	/**
	 * Used to implement the
	 * {@link BigStepProofContext#setProofNodeResult(BigStepProofNode, BigStepProofResult)}
	 * method of the {@link DefaultBigStepProofContext} class.
	 * 
	 * @param context the big step proof context.
	 * @param node the big step node.
	 * @param result the big step result.
	 * @see BigStepProofContext#setProofNodeResult(BigStepProofNode,
	 *      BigStepProofResult)
	 */
	void contextSetProofNodeResult ( DefaultBigStepProofContext context, final DefaultBigStepProofNode node,
			final BigStepProofResult result ) {
		final BigStepProofResult oldResult = node.getResult ( );
		context.addRedoAction ( new Runnable ( ) {
			@SuppressWarnings ( "synthetic-access" )
			public void run ( ) {
				node.setResult ( result );
				nodeChanged ( node );
			}
		} );
		context.addUndoAction ( new Runnable ( ) {
			@SuppressWarnings ( "synthetic-access" )
			public void run ( ) {
				node.setResult ( oldResult );
				nodeChanged ( node );
			}
		} );
	}

	/**
	 * Used to implement the
	 * {@link BigStepProofContext#setProofNodeRule(BigStepProofNode, BigStepProofRule)}
	 * method of the {@link DefaultBigStepProofContext} class.
	 * 
	 * @param context the big step proof context.
	 * @param node the big step node.
	 * @param rule the big step rule.
	 * @see BigStepProofContext#setProofNodeRule(BigStepProofNode,
	 *      BigStepProofRule)
	 */
	void contextSetProofNodeRule ( DefaultBigStepProofContext context, final DefaultBigStepProofNode node,
			final BigStepProofRule rule ) {
		final ProofStep[] oldSteps = node.getSteps ( );
		context.addRedoAction ( new Runnable ( ) {
			@SuppressWarnings ( "synthetic-access" )
			public void run ( ) {
				node.setSteps ( new ProofStep[] { new ProofStep ( node.getExpression ( ), rule ) } );
				nodeChanged ( node );
			}
		} );
		context.addUndoAction ( new Runnable ( ) {
			@SuppressWarnings ( "synthetic-access" )
			public void run ( ) {
				node.setSteps ( oldSteps );
				nodeChanged ( node );
			}
		} );
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.latex.LatexPrintableNormal#getLatexCommands()
	 */
	public TreeSet < LatexCommand > getLatexCommands ( ) {
		TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( );
		commands.add ( new DefaultLatexCommand ( LATEX_MKTREE, 1,
				"\\stepcounter{tree} #1 \\arrowstrue #1 \\arrowsfalse", "tree" ) ); //$NON-NLS-1$//$NON-NLS-2$

		commands.add ( new DefaultLatexCommand ( LATEX_ARROW, 3, "\\ifarrows" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
				+ "\\ncangle[angleA=-90,angleB=#1]{<-}{\\thetree.#2}{\\thetree.#3}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
				+ "\\else" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
				+ "\\fi", "bli", "bla", "blub" ) ); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$

		for ( LatexCommand command : getLatexCommandsInternal ( this.root ) ) {
			commands.add ( command );
		}
		return commands;
	}

	private TreeSet < LatexCommand > getLatexCommandsInternal ( ProofNode pNode ) {
		TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( );
		for ( LatexCommand command : pNode.getLatexCommands ( ) ) {
			commands.add ( command );
		}
		for ( int i = 0; i < pNode.getChildCount ( ); i++ ) {
			for ( LatexCommand command : getLatexCommandsInternal ( pNode.getChildAt ( i ) ) ) {
				commands.add ( command );
			}
		}
		return commands;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.latex.LatexPrintableNormal#getLatexInstructions()
	 */
	public TreeSet < LatexInstruction > getLatexInstructions ( ) {
		TreeSet < LatexInstruction > instructions = new TreeSet < LatexInstruction > ( );

		instructions.add ( new DefaultLatexInstruction ( "\\newcounter{tree}" ) ); //$NON-NLS-1$
		instructions.add ( new DefaultLatexInstruction ( "\\newcounter{node}[tree]" ) ); //$NON-NLS-1$
		instructions.add ( new DefaultLatexInstruction ( "\\newlength{\\treeindent}" ) ); //$NON-NLS-1$
		instructions.add ( new DefaultLatexInstruction ( "\\newlength{\\nodeindent}" ) ); //$NON-NLS-1$
		instructions.add ( new DefaultLatexInstruction ( "\\newlength{\\nodesep}" ) ); //$NON-NLS-1$
		instructions.add ( new DefaultLatexInstruction ( "\\definecolor" + LATEX_DEFINE_COLOR_DARKGREEN ) ); //$NON-NLS-1$
		instructions.add ( new DefaultLatexInstruction ( "\\newcommand" + LATEX_RESULT_COLOR ) ); //$NON-NLS-1$
		instructions.add ( new DefaultLatexInstruction ( "\\newcommand" + LATEX_RESULT_TYPE_COLOR ) ); //$NON-NLS-1$
		instructions.add ( new DefaultLatexInstruction ( "\\newcommand" + LATEX_RULE_COLOR ) ); //$NON-NLS-1$
		instructions.add ( new DefaultLatexInstruction ( "\\newif\\ifarrows  " + LATEX_LINE_BREAK_SOURCE_CODE //$NON-NLS-1$
				+ "\\arrowsfalse" ) ); //$NON-NLS-1$
		instructions.add ( new DefaultLatexInstruction ( "\\newcommand{\\blong}{\\!\\!\\begin{array}[t]{l}}" ) ); //$NON-NLS-1$
		instructions.add ( new DefaultLatexInstruction ( "\\newcommand{\\elong}{\\end{array}}" ) ); //$NON-NLS-1$

		for ( LatexInstruction instruction : getLatexInstructionsInternal ( this.root ) ) {
			instructions.add ( instruction );
		}
		return instructions;
	}

	/**
	 * TODO
	 * 
	 * @param pNode TODO
	 * @return TODO
	 */
	private TreeSet < LatexInstruction > getLatexInstructionsInternal ( ProofNode pNode ) {
		TreeSet < LatexInstruction > packages = new TreeSet < LatexInstruction > ( );
		for ( LatexInstruction pack : pNode.getLatexInstructions ( ) ) {
			packages.add ( pack );
		}
		for ( int i = 0; i < pNode.getChildCount ( ); i++ ) {
			for ( LatexInstruction pack : getLatexInstructionsInternal ( pNode.getChildAt ( i ) ) ) {
				packages.add ( pack );
			}
		}
		return packages;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.latex.LatexPrintableNormal#getLatexPackages()
	 */
	public TreeSet < LatexPackage > getLatexPackages ( ) {
		TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( );
		packages.add ( new DefaultLatexPackage ( "longtable" ) ); //$NON-NLS-1$
		packages.add ( new DefaultLatexPackage ( "amsmath" ) ); //$NON-NLS-1$
		packages.add ( new DefaultLatexPackage ( "pstricks" ) ); //$NON-NLS-1$
		packages.add ( new DefaultLatexPackage ( "pst-node" ) ); //$NON-NLS-1$
		packages.add ( new DefaultLatexPackage ( "color" ) ); //$NON-NLS-1$
		packages.add ( new DefaultLatexPackage ( "amstext" ) ); //$NON-NLS-1$
		for ( LatexPackage pack : getLatexPackagesInternal ( this.root ) ) {
			packages.add ( pack );
		}
		return packages;
	}

	/**
	 * TODO
	 * 
	 * @param pNode TODO
	 * @return TODO
	 */
	private TreeSet < LatexPackage > getLatexPackagesInternal ( ProofNode pNode ) {
		TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( );
		for ( LatexPackage pack : pNode.getLatexPackages ( ) ) {
			packages.add ( pack );
		}
		for ( int i = 0; i < pNode.getChildCount ( ); i++ ) {
			for ( LatexPackage pack : getLatexPackagesInternal ( pNode.getChildAt ( i ) ) ) {
				packages.add ( pack );
			}
		}
		return packages;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.latex.LatexPrintableNormal#toLatexString()
	 */
	public LatexString toLatexString ( ) {
		return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance ( ), 0 ).toLatexString ( );
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see LatexPrintableNormal#toLatexStringBuilder(LatexStringBuilderFactory,int)
	 */
	public final LatexStringBuilder toLatexStringBuilder ( LatexStringBuilderFactory pLatexStringBuilderFactory,
			int pIndent ) {
		LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0, pIndent );
		builder.addBuilderBegin ( );
		{
			builder.addText ( "\\treeindent=0mm" );
			builder.addSourceCodeBreak ( 0 );
			builder.addText ( "\\nodeindent=7mm" );
			builder.addSourceCodeBreak ( 0 );
			builder.addText ( "\\nodesep=2mm" );
			
			builder.addSourceCodeBreak ( 0 );
			builder.addText ( "\\newcommand{\\longtext}[1]{\\topmargin=#1\\enlargethispage{840mm}" );
			builder.addSourceCodeBreak ( 0 );

			builder.addText ( "\\mktree{" );
			toLatexStringBuilderInternal ( pLatexStringBuilderFactory, builder, this.root, pIndent + LATEX_INDENT, -1 );
		}
		builder.addText ( "}" );
		builder.addText ( "}" );

		builder.addText ( "\\longtext{-14mm}" );
		builder.addText ( "\\newpage" );
		builder.addText ( "\\longtext{-294mm}" );
		builder.addBuilderEnd ( );
		return builder;
	}

	/**
	 * Build the latex string for the given <code>pCurrentNode</code>.
	 * 
	 * @param pLatexStringBuilderFactory The factory which should be used.
	 * @param pLatexStringBuilder The {@link LatexStringBuilder} which should be
	 *          completed.
	 *          is needed because of his {@link ProofNode}s.
	 * @param pCurrentNode The current {@link ProofNode}.
	 * @param pIndent The indent of this object.
	 */
	public final void toLatexStringBuilderInternal ( LatexStringBuilderFactory pLatexStringBuilderFactory,
			LatexStringBuilder pLatexStringBuilder, ProofNode pCurrentNode, int pIndent, int pDepth ) {
		int depth = pDepth + 1;
		pLatexStringBuilder.addBuilder ( pCurrentNode.toLatexStringBuilder ( pLatexStringBuilderFactory, pIndent
				+ LATEX_INDENT, depth, pCurrentNode.getId ( ) ), 0 );
		int value = 90;
		if ( pCurrentNode.getChildCount ( ) > 1 )
			value = 180;

		for ( int i = 0; i < pCurrentNode.getChildCount ( ); i++ ) {
			pLatexStringBuilder.addText ( "\\arrow{" + value + "}{" + pCurrentNode.getId ( ) + "}{"
					+ pCurrentNode.getChildAt ( i ).getId ( ) + "}" );
			pLatexStringBuilder.addSourceCodeBreak ( 0 );
		}
		pLatexStringBuilder.addSourceCodeBreak ( 0 );

		for ( int i = 0; i < pCurrentNode.getChildCount ( ); i++ ) {
			toLatexStringBuilderInternal ( pLatexStringBuilderFactory, pLatexStringBuilder, pCurrentNode.getChildAt ( i ),
					pIndent, depth );
		}
	}
}
