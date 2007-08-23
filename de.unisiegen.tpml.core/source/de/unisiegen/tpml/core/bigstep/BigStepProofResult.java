package de.unisiegen.tpml.core.bigstep ;


import java.util.TreeSet;

import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.interpreters.DefaultStore ;
import de.unisiegen.tpml.core.interpreters.Store ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.LatexCommand;
import de.unisiegen.tpml.core.latex.LatexCommandNames;
import de.unisiegen.tpml.core.latex.LatexInstruction;
import de.unisiegen.tpml.core.latex.LatexPackage;
import de.unisiegen.tpml.core.latex.LatexPrintable;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;


/**
 * Instances of this class represent results for big step nodes in the big step
 * interpreter. A big step result consists of a value (an
 * {@link de.unisiegen.tpml.core.expressions.Expression}) and the resulting
 * {@link de.unisiegen.tpml.core.interpreters.Store}.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 */
public final class BigStepProofResult implements LatexPrintable, LatexCommandNames
{
  //
  // Attributes
  //
  /**
   * The resulting store of a proof node.
   * 
   * @see #getStore()
   */
  private Store store ;


  /**
   * The resulting value of a proof node.
   * 
   * @see #getValue()
   */
  private Expression value ;


  //
  // Constructor (package)
  //
  /**
   * Allocates a new <code>DefaultBigStepProofResult</code> with the specified
   * <code>store</code> and <code>value</code>.
   * 
   * @param pStore the resulting store of a big step node.
   * @param pValue the resulting value of a big step node.
   * @throws NullPointerException if <code>store</code> is <code>null</code>.
   * @see #getStore()
   * @see #getValue()
   */
  BigStepProofResult ( Store pStore , Expression pValue )
  {
    if ( pStore == null )
    {
      throw new NullPointerException ( "store is null" ) ; //$NON-NLS-1$
    }
    this.store = pStore ;
    this.value = pValue ;
  }


  //
  // Accessors
  //
  /**
   * Returns the {@link Store} that is part of the result of a proven big step
   * node. To be exact, a copy of the store is returned, that may then be
   * modified by the caller (the {@link Store} has copy-on-write semantics).
   * This is to ease the implementation of memory operations in the small and
   * big step interpreters.
   * 
   * @return the resulting store for a big step proof node.
   * @see Store
   */
  public Store getStore ( )
  {
    return new DefaultStore ( ( DefaultStore ) this.store ) ;
  }


  /**
   * Returns the value that is part of the result of a proven big step node.
   * 
   * @return the resulting value for a big step proof node.
   * @see Expression
   */
  public Expression getValue ( )
  {
    return this.value ;
  }
  
	public TreeSet < LatexCommand > getLatexCommands ( ) {
		TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( );
		commands.add ( new DefaultLatexCommand ( LATEX_BIG_STEP_PROOF_RESULT, 2, 
				"\\ifthenelse{\\equal{#2}{}}" + LATEX_LINE_BREAK_NEW_COMMAND   //$NON-NLS-1$
				+ "{#1}" //$NON-NLS-1$
				+"{((#1\\ #2)}", "e", "store" ) );//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
				   
		
		for ( LatexCommand command : this.value.getLatexCommands ( ) ) {
			commands.add ( command );
		}
		return commands;
	}

	public TreeSet < LatexInstruction > getLatexInstructions ( ) {
		TreeSet < LatexInstruction > instructions = new TreeSet < LatexInstruction > ( );
		for ( LatexInstruction instruction : this.value.getLatexInstructions ( ) ) {
			instructions.add ( instruction );
		}
		return instructions;
	}

	public TreeSet < LatexPackage > getLatexPackages ( ) {
		TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( );
		for ( LatexPackage pack : this.value.getLatexPackages ( ) ) {
			packages.add ( pack );
		}
		return packages;
	}

	public LatexString toLatexString ( ) {
		return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance ( ) ).toLatexString ( );
	}

	public LatexStringBuilder toLatexStringBuilder ( LatexStringBuilderFactory pLatexStringBuilderFactory ) {
		LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( this, 0, LATEX_BIG_STEP_PROOF_RESULT );
		builder.addBuilder ( this.value.toLatexStringBuilder ( pLatexStringBuilderFactory ), 0 );
		if (this.value.containsMemoryOperations ( ))
		builder.addBuilder ( getStore ( ).toLatexStringBuilder ( pLatexStringBuilderFactory ), 0 ) ;
		else {
			builder.addEmptyBuilder ( );
		}

		return builder;
	}
}
