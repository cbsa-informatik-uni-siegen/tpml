package de.unisiegen.tpml.core.latex ;


/**
 * TODO
 */
public final class DefaultLatexPackage implements LatexPackage
{
  /**
   * TODO
   */
  private String name ;


  /**
   * TODO
   * 
   * @param pName
   */
  public DefaultLatexPackage ( String pName )
  {
    this.name = pName ;
  }


  /**
   * TODO
   * 
   * @param pOther TODO
   * @return TODO
   */
  public int compareTo ( LatexPackage pOther )
  {
    return this.name.compareTo ( ( ( DefaultLatexPackage ) pOther ).name ) ;
  }


  /**
   * TODO
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
   * TODO
   */
  @ Override
  public int hashCode ( )
  {
    return this.name.hashCode ( ) ;
  }


  /**
   * TODO
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
