package de.unisiegen.tpml.core.bigstep;

import java.util.TreeSet;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofNode;
import de.unisiegen.tpml.core.interpreters.DefaultStore;
import de.unisiegen.tpml.core.interpreters.Store;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.DefaultLatexPackage;
import de.unisiegen.tpml.core.latex.LatexCommand;
import de.unisiegen.tpml.core.latex.LatexCommandNames;
import de.unisiegen.tpml.core.latex.LatexInstruction;
import de.unisiegen.tpml.core.latex.LatexPackage;
import de.unisiegen.tpml.core.latex.LatexPrintable;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;

/**
 * Default implementation of the <code>BigStepProofNode</code> interface. The
 * class for nodes in a {@link de.unisiegen.tpml.core.bigstep.BigStepProofModel}.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.bigstep.BigStepProofNode
 * @see de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofNode
 */
public final class DefaultBigStepProofNode extends AbstractInterpreterProofNode implements BigStepProofNode,
		LatexPrintable, LatexCommandNames {
	//
	// Attributes
	//
	/**
	 * The result of the evaluation of the expression at this node. May be either
	 * <code>null</code> if the node is not yet proven, or a value (see
	 * {@link Expression#isValue()}) or an exception (see
	 * {@link Expression#isException()}) with a store.
	 * 
	 * @see #getResult()
	 * @see #setResult(BigStepProofResult)
	 */
	private BigStepProofResult result;

	//
	// Constructors (package)
	//
	/**
	 * Convenience wrapper for {@link #DefaultBigStepProofNode(Expression, Store)},
	 * which passes an empty {@link Store} for the <code>store</code> parameter.
	 * 
	 * @param pExpression the {@link Expression} for this node.
	 * @throws NullPointerException if <code>expression</code> is
	 *           <code>null</code>.
	 */
	public DefaultBigStepProofNode ( Expression pExpression ) {
		this ( pExpression, new DefaultStore ( ) );
	}

	/**
	 * Allocates a new <code>DefaultBigStepProofNode</code> with the specified
	 * <code>expression</code> and <code>store</code>.
	 * 
	 * @param pExpression the {@link Expression} for this node.
	 * @param store the {@link Store} for this node.
	 * @throws NullPointerException if either <code>expression</code> or
	 *           <code>store</code> is <code>null</code>.
	 */
	public DefaultBigStepProofNode ( Expression pExpression, Store store ) {
		super ( pExpression, store );
	}

	//
	// Accessors
	//
	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.bigstep.BigStepProofNode#isFinished()
	 */
	public boolean isFinished ( ) {
		if ( !isProven ( ) ) {
			return false;
		}
		for ( int n = 0; n < getChildCount ( ); ++n ) {
			if ( !getChildAt ( n ).isFinished ( ) ) {
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc} A big step proof node is proven if the {@link #result} is
	 * non-<code>null</code>.
	 * 
	 * @see de.unisiegen.tpml.core.ProofNode#isProven()
	 */
	public boolean isProven ( ) {
		return ( this.result != null );
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.bigstep.BigStepProofNode#getResult()
	 */
	public BigStepProofResult getResult ( ) {
		return this.result;
	}

	/**
	 * Sets the result for the expression at this node. The <code>result</code>
	 * must be either <code>null</code> or a {@link BigStepProofResult} with a
	 * valid store and a value or an exception (according to the semantics of the
	 * big step interpreter), otherwise an {@link IllegalArgumentException} will
	 * be thrown.
	 * 
	 * @param pResult the new result for this node, or <code>null</code> to
	 *          reset the result.
	 * @throws IllegalArgumentException if <code>result</code> is invalid.
	 * @see #getResult()
	 */
	void setResult ( BigStepProofResult pResult ) {
		if ( pResult != null && !pResult.getValue ( ).isException ( ) && !pResult.getValue ( ).isValue ( ) ) {
			throw new IllegalArgumentException ( "result is invalid" ); //$NON-NLS-1$
		}
		this.result = pResult;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.bigstep.BigStepProofNode#getRule()
	 */
	public BigStepProofRule getRule ( ) {
		ProofStep[] steps = getSteps ( );
		if ( steps.length > 0 ) {
			return ( BigStepProofRule ) steps[0].getRule ( );
		}
		return null;
	}

	//
	// Primitives
	//
	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.AbstractProofNode#getChildAt(int)
	 */
	@Override
	public DefaultBigStepProofNode getChildAt ( int childIndex ) {
		return ( DefaultBigStepProofNode ) super.getChildAt ( childIndex );
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.AbstractProofNode#getParent()
	 */
	@Override
	public DefaultBigStepProofNode getParent ( ) {
		return ( DefaultBigStepProofNode ) super.getParent ( );
	}

	//
	// Tree Queries
	//
	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.AbstractProofNode#getRoot()
	 */
	@Override
	public DefaultBigStepProofNode getRoot ( ) {
		return ( DefaultBigStepProofNode ) super.getRoot ( );
	}

	//
	// Child Queries
	//
	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.AbstractProofNode#getFirstChild()
	 */
	@Override
	public DefaultBigStepProofNode getFirstChild ( ) {
		return ( DefaultBigStepProofNode ) super.getFirstChild ( );
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.AbstractProofNode#getLastChild()
	 */
	@Override
	public DefaultBigStepProofNode getLastChild ( ) {
		return ( DefaultBigStepProofNode ) super.getLastChild ( );
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.AbstractProofNode#getChildAfter(javax.swing.tree.TreeNode)
	 */
	@Override
	public DefaultBigStepProofNode getChildAfter ( TreeNode aChild ) {
		return ( DefaultBigStepProofNode ) super.getChildAfter ( aChild );
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.AbstractProofNode#getChildBefore(javax.swing.tree.TreeNode)
	 */
	@Override
	public DefaultBigStepProofNode getChildBefore ( TreeNode aChild ) {
		return ( DefaultBigStepProofNode ) super.getChildBefore ( aChild );
	}

	//
	// Leaf Queries
	//
	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.AbstractProofNode#getFirstLeaf()
	 */
	@Override
	public DefaultBigStepProofNode getFirstLeaf ( ) {
		return ( DefaultBigStepProofNode ) super.getFirstLeaf ( );
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.AbstractProofNode#getLastLeaf()
	 */
	@Override
	public DefaultBigStepProofNode getLastLeaf ( ) {
		return ( DefaultBigStepProofNode ) super.getLastLeaf ( );
	}

	//
	// Base methods
	//
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString ( ) {
		StringBuilder builder = new StringBuilder ( );
		boolean memoryEnabled = getExpression ( ).containsMemoryOperations ( );
		if ( memoryEnabled ) {
			builder.append ( '(' );
		}
		builder.append ( getExpression ( ) );
		if ( memoryEnabled ) {
			builder.append ( ", " ); //$NON-NLS-1$
			builder.append ( getStore ( ) );
			builder.append ( ')' );
		}
		builder.append ( " \u21d3 " ); //$NON-NLS-1$
		if ( this.result != null ) {
			if ( memoryEnabled ) {
				builder.append ( '(' );
			}
			builder.append ( this.result.getValue ( ) );
			if ( memoryEnabled ) {
				builder.append ( ", " ); //$NON-NLS-1$
				builder.append ( this.result.getStore ( ) );
				builder.append ( ')' );
			}
		}
		return builder.toString ( );
	}

	public TreeSet < LatexCommand > getLatexCommands ( ) {
		TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( );
		commands.add ( new DefaultLatexCommand ( LATEX_BIG_STEP_PROOF_NODE, 3,
				"\\ifthenelse{\\equal{#3}{}}" + LATEX_LINE_BREAK_NEW_COMMAND   //$NON-NLS-1$ 
					+"{\\ifthenelse{\\equal{#2}{}}" + LATEX_LINE_BREAK_NEW_COMMAND   //$NON-NLS-1$ 
						+ "{#1}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
						+ "{(#1\\ #2)}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
					+"}" //$NON-NLS-1$
					+"{\\ifthenelse{\\equal{#2}{}}" + LATEX_LINE_BREAK_NEW_COMMAND   //$NON-NLS-1$ 
						+ "{#1\\ \\eval\\ #3}" //$NON-NLS-1$
						+ "{(#1\\ \\ #2)\\ \\eval\\ #3}" //$NON-NLS-1$
						+ "}" //$NON-NLS-1$
					, "e", "store", "tau" ) ); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ 

		for ( LatexCommand command : this.expression.getLatexCommands ( ) ) {
			commands.add ( command );
		}
		for (LatexCommand command : this.getStore().getLatexCommands ( )) {
			commands.add( command);
		}
		if ( this.result != null ) {
			for ( LatexCommand command : this.result.getLatexCommands ( ) ) {
				commands.add ( command );
			}
		}
		return commands;
	}

	public TreeSet < LatexInstruction > getLatexInstructions ( ) {
		TreeSet < LatexInstruction > instructions = new TreeSet < LatexInstruction > ( );
		for ( LatexInstruction instruction : this.expression.getLatexInstructions ( ) ) {
			instructions.add ( instruction );
		}
		for (LatexInstruction instruction : this.getStore().getLatexInstructions ( ) ) {
			instructions.add ( instruction );
		}
		if ( this.result != null ) {
			for ( LatexInstruction instruction : this.result.getLatexInstructions ( ) ) {
				instructions.add ( instruction );
			}
		}
		return instructions;
	}

	public TreeSet < LatexPackage > getLatexPackages ( ) {
		TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( );
		packages.add ( new DefaultLatexPackage ( "ifthen" ) ); //$NON-NLS-1$
		for ( LatexPackage pack : this.expression.getLatexPackages ( ) ) {
			packages.add ( pack );
		}
		
		for ( LatexPackage pack : this.getStore().getLatexPackages ( ) ) {
			packages.add ( pack );
		}
		
		if ( this.result != null ) {
			for ( LatexPackage pack : this.result.getLatexPackages ( ) ) {
				packages.add ( pack );
			}
		}
		return packages;
	}

	public LatexString toLatexString ( ) {
		return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance ( ) ).toLatexString ( );
	}

	public LatexStringBuilder toLatexStringBuilder ( LatexStringBuilderFactory pLatexStringBuilderFactory ) {
		LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( this, 0, LATEX_BIG_STEP_PROOF_NODE );
		builder.addBuilder ( this.expression.toLatexStringBuilder ( pLatexStringBuilderFactory ), 0 );
		if (this.expression.containsMemoryOperations ( ))
		builder.addBuilder ( getStore ( ).toLatexStringBuilder ( pLatexStringBuilderFactory ), 0 );
		else 
			builder.addEmptyBuilder ( );
		if ( this.result != null )
			builder.addBuilder ( this.result.toLatexStringBuilder ( pLatexStringBuilderFactory ), 0 );
		else
			builder.addEmptyBuilder ( );

		return builder;
	}
}
