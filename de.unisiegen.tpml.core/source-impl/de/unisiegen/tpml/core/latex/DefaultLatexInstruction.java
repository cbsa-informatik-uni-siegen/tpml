package de.unisiegen.tpml.core.latex ;


/**
 * This class is used for latex instructions.
 * 
 * @author Christian Fehler
 */
public final class DefaultLatexInstruction implements LatexInstruction
{
  /**
   * The text of the latex instruction.
   */
  private String text ;


  /**
   * Allocates a new <code>DefaultLatexInstruction</code> for the specified
   * <code>pText</code>.
   * 
   * @param pText The text of the new latex instruction.
   */
  public DefaultLatexInstruction ( String pText )
  {
    this.text = pText ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Comparable#compareTo(Object)
   */
  public int compareTo ( LatexInstruction pOther )
  {
    return this.text.compareTo ( ( ( DefaultLatexInstruction ) pOther ).text ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
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
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @ Override
  public int hashCode ( )
  {
    return this.text.hashCode ( ) ;
  }


  /**
   * Returns the string value of this <code>DefaultLatexInstruction</code>.
   * 
   * @return The string value of this <code>DefaultLatexInstruction</code>.
   */
  @ Override
  public String toString ( )
  {
    return this.text ;
  }
}
