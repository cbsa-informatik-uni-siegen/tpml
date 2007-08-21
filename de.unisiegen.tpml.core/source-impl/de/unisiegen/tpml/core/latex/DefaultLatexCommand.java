package de.unisiegen.tpml.core.latex ;


/**
 * TODO
 */
public final class DefaultLatexCommand implements LatexCommand
{
  /**
   * TODO
   */
  private String name ;


  /**
   * TODO
   */
  private int parameter ;


  /**
   * TODO
   */
  private String body ;


  /**
   * TODO
   * 
   * @param pName
   * @param pParameter
   * @param pBody
   */
  public DefaultLatexCommand ( String pName , int pParameter , String pBody )
  {
    this.name = pName ;
    this.parameter = pParameter ;
    if ( pParameter < 0 )
    {
      throw new IllegalArgumentException ( "parameter is less than zero" ) ; //$NON-NLS-1$
    }
    this.body = pBody ;
  }


  /**
   * TODO
   * 
   * @param pOther TODO
   * @return TODO
   */
  public int compareTo ( LatexCommand pOther )
  {
    return this.name.compareTo ( ( ( DefaultLatexCommand ) pOther ).name ) ;
  }


  /**
   * TODO
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof DefaultLatexCommand )
    {
      DefaultLatexCommand other = ( DefaultLatexCommand ) pObject ;
      return this.name.equals ( other.name )
          && ( this.parameter == other.parameter )
          && this.body.equals ( other.body ) ;
    }
    return false ;
  }


  /**
   * TODO
   */
  @ Override
  public int hashCode ( )
  {
    return this.name.hashCode ( ) + ( this.parameter << 5 )
        + this.body.hashCode ( ) ;
  }


  /**
   * TODO
   */
  @ Override
  public String toString ( )
  {
    String result = "\\newcommand{\\" ; //$NON-NLS-1$
    result += this.name ;
    result += "}" ; //$NON-NLS-1$
    if ( this.parameter > 0 )
    {
      result += "[" ; //$NON-NLS-1$
      result += this.parameter ;
      result += "]" ; //$NON-NLS-1$
    }
    result += "{" ; //$NON-NLS-1$
    result += this.body ;
    result += "}" ; //$NON-NLS-1$
    return result ;
  }
}
