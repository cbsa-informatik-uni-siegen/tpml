package de.unisiegen.tpml.core.latex;


/**
 * This class is used for latex packages.
 * 
 * @author Christian Fehler
 * @version $Id$
 */
public final class DefaultLatexPackage implements LatexPackage
{

  /**
   * The name of the latex package.
   */
  private String name;


  /**
   * Allocates a new <code>DefaultLatexPackage</code> for the specified
   * <code>pName</code>.
   * 
   * @param pName The name of the new latex command.
   */
  public DefaultLatexPackage ( String pName )
  {
    this.name = pName;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof DefaultLatexPackage )
    {
      DefaultLatexPackage other = ( DefaultLatexPackage ) pObject;
      return this.name.equals ( other.name );
    }
    return false;
  }


  /**
   * Returns the name.
   * 
   * @return The name.
   * @see #name
   */
  public String getName ()
  {
    return this.name;
  }


  /**
   * Returns the string value of this <code>DefaultLatexInstruction</code>.
   * 
   * @return The string value of this <code>DefaultLatexInstruction</code>.
   */
  @Override
  public String toString ()
  {
    StringBuilder result = new StringBuilder ();
    result.append ( "\\usepackage" ); //$NON-NLS-1$
    result.append ( "{" ); //$NON-NLS-1$
    result.append ( this.name );
    result.append ( "}" ); //$NON-NLS-1$
    return result.toString ();
  }
}
