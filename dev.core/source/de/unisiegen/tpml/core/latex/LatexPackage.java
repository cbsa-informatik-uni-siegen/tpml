package de.unisiegen.tpml.core.latex;


/**
 * This interface is used for latex packages.
 * 
 * @author Christian Fehler
 * @version $Id: LatexPackage.java 2796 2008-03-14 19:13:11Z fehler $
 */
public interface LatexPackage
{

  /**
   * The description.
   */
  public static final String DESCRIPTION = "Needed latex packages"; //$NON-NLS-1$


  /**
   * The <code>amsmath</code> package.
   */
  public static final LatexPackage AMSMATH = new DefaultLatexPackage (
      "amsmath" ); //$NON-NLS-1$


  /**
   * The <code>amssymb</code> package.
   */
  public static final LatexPackage AMSSYMB = new DefaultLatexPackage (
      "amssymb" ); //$NON-NLS-1$


  /**
   * The <code>amstext</code> package.
   */
  public static final LatexPackage AMSTEXT = new DefaultLatexPackage (
      "amstext" ); //$NON-NLS-1$


  /**
   * The <code>color</code> package.
   */
  public static final LatexPackage COLOR = new DefaultLatexPackage ( "color" ); //$NON-NLS-1$


  /**
   * The <code>ifthen</code> package.
   */
  public static final LatexPackage IFTHEN = new DefaultLatexPackage ( "ifthen" ); //$NON-NLS-1$


  /**
   * The <code>xifthen</code> package. 
   */
  public static final LatexPackage XIFTHEN = new DefaultLatexPackage ( "xifthen"); //$NON-NLS-1$
  
  
  /**
   * The <code>longtable</code> package.
   */
  public static final LatexPackage LONGTABLE = new DefaultLatexPackage (
      "longtable" ); //$NON-NLS-1$


  /**
   * The <code>pst-node</code> package.
   */
  public static final LatexPackage PSTNODE = new DefaultLatexPackage (
      "pst-node" ); //$NON-NLS-1$


  /**
   * The <code>pstricks</code> package.
   */
  public static final LatexPackage PSTRICKS = new DefaultLatexPackage (
      "pstricks" ); //$NON-NLS-1$


  /**
   * Returns the name.
   * 
   * @return The name.
   */
  public String getName ();


  /**
   * Returns the string value of this <code>LatexInstruction</code>.
   * 
   * @return The string value of this <code>LatexInstruction</code>.
   */
  public String toString ();
}