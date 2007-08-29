package de.unisiegen.tpml.core.latex ;


import java.util.TreeSet ;


/**
 * Base interface for classes whose instances can be printed in latex.
 * 
 * @author Christian Fehler
 */
public interface LatexPrintable extends LatexCommandNames
{
  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public TreeSet < LatexCommand > getLatexCommands ( ) ;


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public TreeSet < LatexInstruction > getLatexInstructions ( ) ;


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public TreeSet < LatexPackage > getLatexPackages ( ) ;
}
