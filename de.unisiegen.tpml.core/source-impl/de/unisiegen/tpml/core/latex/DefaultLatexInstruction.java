package de.unisiegen.tpml.core.latex ;


/**
 * TODO
 */
public final class DefaultLatexInstruction implements LatexInstruction
{
  /**
   * TODO
   */
  private String text ;


  /**
   * TODO
   * 
   * @param pText
   */
  public DefaultLatexInstruction ( String pText )
  {
    this.text = pText ;
  }


  /**
   * TODO
   * 
   * @param pOther TODO
   * @return TODO
   */
  public int compareTo ( LatexInstruction pOther )
  {
    return this.text.compareTo ( ( ( DefaultLatexInstruction ) pOther ).text ) ;
  }


  /**
   * TODO
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof DefaultLatexInstruction )
    {
      DefaultLatexInstruction other = ( DefaultLatexInstruction ) pObject ;
      return this.text.equals ( other.text ) ;
    }
    return false ;
  }


  /**
   * TODO
   */
  @ Override
  public int hashCode ( )
  {
    return this.text.hashCode ( ) ;
  }


  /**
   * TODO
   */
  @ Override
  public String toString ( )
  {
    return this.text ;
  }
}
