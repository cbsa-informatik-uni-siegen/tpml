package de.unisiegen.tpml.core.typechecker;

import java.util.TreeSet;

import de.unisiegen.tpml.core.expressions.Unify;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.LatexCommand;
import de.unisiegen.tpml.core.latex.LatexCommandNames;
import de.unisiegen.tpml.core.latex.LatexInstruction;
import de.unisiegen.tpml.core.latex.LatexPackage;
import de.unisiegen.tpml.core.latex.LatexPrintable;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * 
 * This nodes with two types are needed in the type checker algorithm to check
 * subtype relations between this two given types.
 *
 * @author Benjamin Mies
 *
 */
public class DefaultTypeCheckerTypeProofNode extends AbstractTypeCheckerProofNode implements TypeCheckerTypeProofNode, LatexPrintable, LatexCommandNames {

	//
	// Attributes
	//

	/**
	 * The second type for this proof node
	 */
	private MonoType type2;

	/**
	 * 
	 * Allocates a new <code>DefaultTypeCheckerTypeProofNode</code>
	 *
	 * @param pType the first type of this type checker proof node
	 * @param pType2 the second type of this type checker proof node
	 */
	public DefaultTypeCheckerTypeProofNode ( MonoType pType, MonoType pType2 ) {
		super ( new Unify ( ) );
		this.type = pType;
		this.type2 = pType2;
	}

	/**
	 * 
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerTypeProofNode#getType2()
	 */
	public MonoType getType2 ( ) {
		return this.type2;
	}

	/*	  public DefaultTypeCheckerTypeProofNode getChildAt(int childIndex){
	 return (DefaultTypeCheckerTypeProofNode) super.getChildAt ( childIndex );
	 }*/

	/**
	 * {@inheritDoc}
	 * 
	 * Mainly useful for debugging purposes.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString ( ) {
		StringBuilder builder = new StringBuilder ( );
		builder.append ( this.type );
		builder.append ( " <: " ); //$NON-NLS-1$
		builder.append ( this.type2 );
		if ( getRule ( ) != null ) {
			builder.append ( " (" + getRule ( ) + ")" ); //$NON-NLS-1$//$NON-NLS-2$
		}
		return builder.toString ( );
	}
	
	
	public TreeSet < LatexCommand > getLatexCommands ( ) {
		TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( );
		commands.add ( new DefaultLatexCommand ( LATEX_TYPE_CHECKER_TYPE_PROOF_NODE, 2, "#1\\ " //$NON-NLS-1$
				+ "<:\\ #2", "tau1", "tau2" ) );   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ 
		for ( LatexCommand command : this.type.getLatexCommands ( ) ) {
			commands.add ( command );
		}
		for ( LatexCommand command : this.type2.getLatexCommands ( ) ) {
			commands.add ( command );
		}

		return commands;
	}

	public TreeSet < LatexInstruction > getLatexInstructions ( ) {
		TreeSet < LatexInstruction > instructions = new TreeSet < LatexInstruction > ( );
		for ( LatexInstruction instruction : this.type.getLatexInstructions ( ) ) {
			instructions.add ( instruction );
		}
		for ( LatexInstruction instruction : this.type2.getLatexInstructions ( ) ) {
			instructions.add ( instruction );
		}

		return instructions;
	}

	public TreeSet < LatexPackage > getLatexPackages ( ) {
		TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( );
		for ( LatexPackage pack : this.type.getLatexPackages ( ) ) {
			packages.add ( pack );
		}
		for ( LatexPackage pack : this.type2.getLatexPackages ( ) ) {
			packages.add ( pack );
		}

		return packages;
	}

	public LatexString toLatexString ( ) {
		return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance ( ) ).toLatexString ( );
	}

	public LatexStringBuilder toLatexStringBuilder ( LatexStringBuilderFactory pLatexStringBuilderFactory ) {
		LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( this, 0, LATEX_TYPE_CHECKER_TYPE_PROOF_NODE );
		builder.addBuilder ( this.type.toLatexStringBuilder ( pLatexStringBuilderFactory ), 0 );
		builder.addBuilder ( this.type2.toLatexStringBuilder ( pLatexStringBuilderFactory ), 0 );

		return builder;
	}

}
