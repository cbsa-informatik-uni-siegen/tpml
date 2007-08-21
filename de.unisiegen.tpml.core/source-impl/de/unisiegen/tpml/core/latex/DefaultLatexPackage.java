package de.unisiegen.tpml.core.latex ;


/**
 * This class is used for latex packages.
 * 
 * @author Christian Fehler
 */
public final class DefaultLatexPackage implements LatexPackage
{
  /**
   * The name of the latex package.
   */
  private String name ;


  /**
   * Allocates a new <code>DefaultLatexPackage</code> for the specified
   * <code>pName</code>.
   * 
   * @param pName The name of the new latex command.
   */
  public DefaultLatexPackage ( String pName )
  {
    this.name = pName ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Comparable#compareTo(Object)
   */
  public int compareTo ( LatexPackage pOther )
  {
    return this.name.compareTo ( ( ( DefaultLatexPackage ) pOther ).name ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof DefaultLatexPackage )
    {
      DefaultLatexPackage other = ( DefaultLatexPackage ) pObject ;
      return this.name.equals ( other.name ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @ Override
  public int hashCode ( )
  {
    return this.name.hashCode ( ) ;
  }


  /**
   * Returns the string value of this <code>DefaultLatexInstruction</code>.
   * 
   * @return The string value of this <code>DefaultLatexInstruction</code>.
   */
  @ Override
  public String toString ( )
  {
    String result = "\\usepackage{" ; //$NON-NLS-1$
    result += this.name ;
    result += "}" ; //$NON-NLS-1$
    return result ;
  }
}
