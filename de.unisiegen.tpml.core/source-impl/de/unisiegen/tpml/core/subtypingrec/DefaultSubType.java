package de.unisiegen.tpml.core.subtypingrec ;


import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction ;
import de.unisiegen.tpml.core.latex.LatexCommandList ;
import de.unisiegen.tpml.core.latex.LatexInstructionList ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPackageList ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * The Default Subtype is needed for the subtyping algorithm. This object
 * contains a left and an right.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 */
public class DefaultSubType implements PrettyPrintable , LatexPrintable
{
  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ( )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_SUB_TYPE , 2 ,
        "#1\\ \\color{" + LATEX_COLOR_NONE + "}{<:}\\ #2" , "tau1" , //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
        "tau2" ) ) ; //$NON-NLS-1$
    return commands ;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public static LatexInstructionList getLatexInstructionsStatic ( )
  {
    LatexInstructionList instructions = new LatexInstructionList ( ) ;
    instructions.add ( new DefaultLatexInstruction ( "\\definecolor{" //$NON-NLS-1$
        + LATEX_COLOR_NONE + "}{rgb}{0.0,0.0,0.0}" , //$NON-NLS-1$
        LATEX_COLOR_NONE + ": color of normal text" ) ) ; //$NON-NLS-1$
    return instructions ;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public static LatexPackageList getLatexPackagesStatic ( )
  {
    LatexPackageList packages = new LatexPackageList ( ) ;
    packages.add ( LatexPackage.COLOR ) ;
    return packages ;
  }


  /**
   * The left type (subtype) of this subtype object
   */
  private MonoType left ;


  /**
   * The right type (supertype) of this subtype object
   */
  private MonoType right ;


  /**
   * Allocates a new default left with the given types.
   * 
   * @param pLeft the left of this object
   * @param pRight the right of this object
   */
  public DefaultSubType ( MonoType pLeft , MonoType pRight )
  {
    this.left = pLeft ;
    this.right = pRight ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof DefaultSubType )
    {
      DefaultSubType other = ( DefaultSubType ) pObject ;
      return ( this.left.equals ( other.left ) && this.right
          .equals ( other.right ) ) ;
    }
    return false ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public LatexCommandList getLatexCommands ( )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( getLatexCommandsStatic ( ) ) ;
    commands.add ( this.left ) ;
    commands.add ( this.right ) ;
    return commands ;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public LatexInstructionList getLatexInstructions ( )
  {
    LatexInstructionList instructions = new LatexInstructionList ( ) ;
    instructions.add ( getLatexInstructionsStatic ( ) ) ;
    instructions.add ( this.left ) ;
    instructions.add ( this.right ) ;
    return instructions ;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public LatexPackageList getLatexPackages ( )
  {
    LatexPackageList packages = new LatexPackageList ( ) ;
    packages.add ( getLatexPackagesStatic ( ) ) ;
    packages.add ( this.left ) ;
    packages.add ( this.right ) ;
    return packages ;
  }


  /**
   * Returns the left type of this object
   * 
   * @return the left type (subtype) of this object
   */
  public MonoType getLeft ( )
  {
    return this.left ;
  }


  /**
   * Returns the right type of this object
   * 
   * @return the right (type) overtype of this object
   */
  public MonoType getRight ( )
  {
    return this.right ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @ Override
  public int hashCode ( )
  {
    return this.left.hashCode ( ) + this.right.hashCode ( ) ;
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
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0 ,
        LATEX_SUB_TYPE , pIndent , this.toPrettyString ( ).toString ( ) ,
        this.left.toPrettyString ( ).toString ( ) , this.right
            .toPrettyString ( ).toString ( ) ) ;
    builder.addBuilder ( this.left.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    builder.addBreak ( ) ;
    builder.addBuilder ( this.right.toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    return builder ;
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
    builder.addBuilder ( this.left
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addText ( PRETTY_SUBTYPE ) ;
    builder.addText ( PRETTY_SPACE ) ;
    builder.addBuilder ( this.right
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    return builder ;
  }


  /**
   * Returns the string representation for this sub type. This method is mainly
   * used for debugging.
   * 
   * @return The pretty printed string representation for this sub type.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @ Override
  public final String toString ( )
  {
    return toPrettyString ( ).toString ( ) ;
  }
}
