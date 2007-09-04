package de.unisiegen.tpml.core.typechecker ;


import java.util.ArrayList;
import java.util.TreeSet;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction;
import de.unisiegen.tpml.core.latex.DefaultLatexPackage;
import de.unisiegen.tpml.core.latex.LatexCommand;
import de.unisiegen.tpml.core.latex.LatexInstruction;
import de.unisiegen.tpml.core.latex.LatexPackage;
import de.unisiegen.tpml.core.latex.LatexPrintable;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.types.MonoType;


/**
 * The normal type checker proof node in the type checker algorithm, containing
 * an evironment. an expression and a type.
 * 
 * @author Benjamin Mies
 */
public class DefaultTypeCheckerExpressionProofNode extends
    AbstractTypeCheckerProofNode implements TypeCheckerExpressionProofNode
{
  /**
   * The type environment for this type checker proof node.
   * 
   * @see #getEnvironment()
   * @see #setEnvironment(TypeEnvironment)
   */
  protected TypeEnvironment environment ;


  /**
   * Allocates a new <code>DefaultTypeCheckerExpressionProofNode</code>
   * 
   * @param pEnvironment the type environment of this proof node
   * @param pExpression the expression of this proof node
   * @param pType the type of this proof node
   */
  public DefaultTypeCheckerExpressionProofNode ( TypeEnvironment pEnvironment ,
      Expression pExpression , MonoType pType )
  {
    super ( pExpression ) ;
    this.environment = pEnvironment ;
    this.type = pType ;
  }


  /**
   * Get the type environment of this proof node
   * 
   * @return the type environment of this proof node
   */
  public TypeEnvironment getEnvironment ( )
  {
    return this.environment ;
  }




  /**
   * Sets the type environment for this proof node to <code>environment</code>.
   * 
   * @param pEnvironment the new type environment for this node.
   * @throws NullPointerException if <code>environment</code> is
   *           <code>null</code>.
   * @see #getEnvironment()
   */
  void setEnvironment ( TypeEnvironment pEnvironment )
  {
    if ( pEnvironment == null )
    {
      throw new NullPointerException ( "environment is null" ) ; //$NON-NLS-1$
    }
    this.environment = pEnvironment ;
  }




  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable#toPrettyString()
   */
  public final PrettyString toPrettyString ( )
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance ( ) )
        .toPrettyString ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , 0 ) ;
    builder.addBuilder ( this.environment
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addText ( PRETTY_RIGHT_TRIANGLE ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addBuilder ( this.expression
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addText ( PRETTY_COLON ) ;
    builder.addText ( PRETTY_COLON ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addBuilder ( this.type
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    return builder ;
  }


  /**
   * Returns the string representation for this type checker expression proof
   * node. This method is mainly used for debugging.
   * 
   * @return The pretty printed string representation for this type checker
   *         expression proof node.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @ Override
  public final String toString ( )
  {
    return toPrettyString ( ).toString ( ) ;
  }
  
	/**
	 * Returns a set of needed latex commands for this latex printable object.
	 * 
	 * @return A set of needed latex commands for this latex printable object.
	 */
	public TreeSet < LatexCommand > getLatexCommands ( ) {

		TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( );

		commands.add ( new DefaultLatexCommand ( LATEX_BYRULE, 1, "\\hspace{-5mm}\\mbox{\\scriptsize\\ #1}", "rule" ) ); //$NON-NLS-1$ //$NON-NLS-2$

		commands
				.add ( new DefaultLatexCommand (
						LATEX_MINIMAL_TYPING_EXPRESSION_PROOF_NODE,
						7,
						"\\ifarrows" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
								+ "\\else \\refstepcounter{node}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
								+ "\\noindent\\hspace{\\treeindent}\\hspace{#2\\nodeindent}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
								+ "\\rnode{\\thetree.#1}{\\makebox[6mm]{(\\thenode)}}\\label{\\thetree.#1}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
								+ "$\\begin{tabular}[t]{p{#7}}$" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
								+ "{#3\\ #4\\color{" + LATEX_COLOR_NONE_STYLE //$NON-NLS-1$
								+ "}{\\ ::}\\ #5}$\\\\$" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
								+ "\\byrule{#6} " //$NON-NLS-1$
								+ "$\\end{tabular}$" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
								+ "\\vspace{\\nodesep}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
								+ "\\fi" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
						, "depth", "id", "evironment","expression", "type", "rule", "space" ) ); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ 
		for ( LatexCommand command : this.environment.getLatexCommands ( ) ) {
			commands.add ( command );
		}
		for ( LatexCommand command : this.expression.getLatexCommands ( ) ) {
			commands.add ( command );
		}
		for ( LatexCommand command : this.type.getLatexCommands ( ) ) {
			commands.add ( command );
		}
		if ( getRule ( ) != null ) {
			for ( LatexCommand command : getRule ( ).getLatexCommands ( ) ) {
				commands.add ( command );
			}
		}
		return commands;
	}

	/**
	 * Returns a set of needed latex instructions for this latex printable object.
	 * 
	 * @return A set of needed latex instructions for this latex printable object.
	 */
	public ArrayList < LatexInstruction > getLatexInstructions ( ) {
		ArrayList < LatexInstruction > instructions = new ArrayList < LatexInstruction > ( );
		instructions.add ( new DefaultLatexInstruction ( "\\newcounter{tree}" ) ); //$NON-NLS-1$
		instructions.add ( new DefaultLatexInstruction ( "\\newcounter{node}[tree]" ) ); //$NON-NLS-1$
		instructions.add ( new DefaultLatexInstruction ( "\\newlength{\\treeindent}" ) ); //$NON-NLS-1$
		instructions.add ( new DefaultLatexInstruction ( "\\newlength{\\nodeindent}" ) ); //$NON-NLS-1$
		instructions.add ( new DefaultLatexInstruction ( "\\newlength{\\nodesep}" ) ); //$NON-NLS-1$
		instructions.add ( new DefaultLatexInstruction ( "\\newif\\ifarrows  " + LATEX_LINE_BREAK_SOURCE_CODE //$NON-NLS-1$
				+ "\\arrowsfalse" ) ); //$NON-NLS-1$
		instructions.add ( new DefaultLatexInstruction ( "\\newcommand{\\blong}{\\!\\!\\begin{array}[t]{l}}" ) ); //$NON-NLS-1$
		instructions.add ( new DefaultLatexInstruction ( "\\newcommand{\\elong}{\\end{array}}" ) ); //$NON-NLS-1$
		instructions.add ( new DefaultLatexInstruction ( "\\definecolor{" //$NON-NLS-1$
				+ LATEX_COLOR_NONE_STYLE + "}{rgb}{0.0,0.0,0.0}", //$NON-NLS-1$
				LATEX_COLOR_NONE_STYLE + ": color of normal text" ) ); //$NON-NLS-1$
		for ( LatexInstruction instruction : this.environment.getLatexInstructions ( ) ) {
			if ( !instructions.contains ( instruction ) ) {
				instructions.add ( instruction );
			}
		}
		for ( LatexInstruction instruction : this.expression.getLatexInstructions ( ) ) {
			if ( !instructions.contains ( instruction ) ) {
				instructions.add ( instruction );
			}
		}
		for ( LatexInstruction instruction : this.type.getLatexInstructions ( ) ) {
			if ( !instructions.contains ( instruction ) ) {
				instructions.add ( instruction );
			}
		}
		if ( getRule ( ) != null ) {
			for ( LatexInstruction instruction : getRule ( ).getLatexInstructions ( ) ) {
				if ( !instructions.contains ( instruction ) ) {
					instructions.add ( instruction );
				}
			}
		}
		return instructions;
	}

	/**
	 * Returns a set of needed latex packages for this latex printable object.
	 * 
	 * @return A set of needed latex packages for this latex printable object.
	 */
	public TreeSet < LatexPackage > getLatexPackages ( ) {
		TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( );
		packages.add ( new DefaultLatexPackage ( "color" ) ); //$NON-NLS-1$
		packages.add ( new DefaultLatexPackage ( "amsmath" ) ); //$NON-NLS-1$
		packages.add ( new DefaultLatexPackage ( "pstricks" ) ); //$NON-NLS-1$
		packages.add ( new DefaultLatexPackage ( "pst-node" ) ); //$NON-NLS-1$
		packages.add ( new DefaultLatexPackage ( "color" ) ); //$NON-NLS-1$
		packages.add ( new DefaultLatexPackage ( "amstext" ) ); //$NON-NLS-1$
		for ( LatexPackage pack : this.environment.getLatexPackages ( ) ) {
			packages.add ( pack );
		}
		for ( LatexPackage pack : this.expression.getLatexPackages ( ) ) {
			packages.add ( pack );
		}
		for ( LatexPackage pack : this.type.getLatexPackages ( ) ) {
			packages.add ( pack );
		}
		if ( getRule ( ) != null ) {
			for ( LatexPackage pack : getRule ( ).getLatexPackages ( ) ) {
				packages.add ( pack );
			}
		}
		return packages;
	}
  
	 /**
	   * {@inheritDoc}
	   * 
	   * @see LatexPrintable#toLatexString()
	   */
	  public final LatexString toLatexString ( )
	  {
	    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance ( ) , 0 )
	        .toLatexString ( ) ;
	  }


	  /**
	   * {@inheritDoc}
	   * 
	   * @see LatexPrintable#toLatexStringBuilder(LatexStringBuilderFactory,int)
	   */
	  public final LatexStringBuilder toLatexStringBuilder (
	      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
	  {
		  int depth = 0;
			AbstractTypeCheckerProofNode myParent = this.getParent ( );
			while ( myParent != null ) {
				depth++ ;
				myParent = myParent.getParent ( );
			}
	    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0 ,
	        LATEX_MINIMAL_TYPING_EXPRESSION_PROOF_NODE , pIndent , this
	            .toPrettyString ( ).toString ( ) , this.environment
	            .toPrettyString ( ).toString ( ) , this.expression
	            .toPrettyString ( ).toString ( ) ,
	        this.type == null ? LATEX_EMPTY_STRING : this.type.toPrettyString ( )
	            .toString ( ),
	            this.getRule ( ) == null ? LATEX_EMPTY_STRING : this.getRule ( ).toPrettyString ( ).toString ( )) ;
	    builder.addText ( "{" + String.valueOf ( this.getId ( ) ) + "}" ); //$NON-NLS-1$//$NON-NLS-2$
			builder.addText ( "{" + String.valueOf ( depth ) + "}" ); //$NON-NLS-1$//$NON-NLS-2$
	    builder.addBuilder ( this.environment.toLatexStringBuilder (
	        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
	    builder.addBuilder ( this.expression.toLatexStringBuilder (
	        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
	    if ( this.type == null )
	    {
	      builder.addEmptyBuilder ( ) ;
	    }
	    else
	    {
	      builder.addBuilder ( this.type.toLatexStringBuilder (
	          pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
	    }
	    if (this.getRule ( )!=null)
	   	 builder.addBuilder ( this.getRule ( ).toLatexStringBuilder ( pLatexStringBuilderFactory, pIndent + LATEX_INDENT ), 0 );
	    else 
	   	 builder.addEmptyBuilder ( );
	    int indent = 245 - depth * 7;
			builder.addText ( "{" + indent + "mm}" ); //$NON-NLS-1$//$NON-NLS-2$
	    return builder ;
	  }
}
