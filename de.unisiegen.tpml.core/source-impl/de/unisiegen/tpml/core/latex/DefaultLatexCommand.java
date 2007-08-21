package de.unisiegen.tpml.core.latex ;


/**
 * This class is used for latex commands.
 * 
 * @author Christian Fehler
 */
public final class DefaultLatexCommand implements LatexCommand
{
  /**
   * The name of the latex command.
   */
  private String name ;


  /**
   * The parameter count of the new latex command.
   */
  private int parameterCount ;


  /**
   * The body of the new latex command.
   */
  private String body ;


  /**
   * Allocates a new <code>DefaultLatexCommand</code> for the specified
   * <code>pName</code>, <code>pParameterCount</code> and
   * <code>pBody</code>.
   * 
   * @param pName The name of the new latex command.
   * @param pParameterCount The parameter count of the new latex command.
   * @param pBody The body of the new latex command.
   */
  public DefaultLatexCommand ( String pName , int pParameterCount , String pBody )
  {
    this.name = pName ;
    if ( pParameterCount < 0 )
    {
      throw new IllegalArgumentException ( "parameter is less than zero" ) ; //$NON-NLS-1$
    }
    this.parameterCount = pParameterCount ;
    this.body = pBody ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Comparable#compareTo(Object)
   */
  public int compareTo ( LatexCommand pOther )
  {
    return this.name.compareTo ( ( ( DefaultLatexCommand ) pOther ).name ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof DefaultLatexCommand )
    {
      DefaultLatexCommand other = ( DefaultLatexCommand ) pObject ;
      return this.name.equals ( other.name )
          && ( this.parameterCount == other.parameterCount )
          && this.body.equals ( other.body ) ;
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
    return this.name.hashCode ( ) + ( this.parameterCount << 5 )
        + this.body.hashCode ( ) ;
  }


  /**
   * Returns the string value of this <code>DefaultLatexCommand</code>.
   * 
   * @return The string value of this <code>DefaultLatexCommand</code>.
   */
  @ Override
  public String toString ( )
  {
    String result = "\\newcommand{\\" ; //$NON-NLS-1$
    result += this.name ;
    result += "}" ; //$NON-NLS-1$
    if ( this.parameterCount > 0 )
    {
      result += "[" ; //$NON-NLS-1$
      result += this.parameterCount ;
      result += "]" ; //$NON-NLS-1$
    }
    result += "{" ; //$NON-NLS-1$
    result += this.body ;
    result += "}" ; //$NON-NLS-1$
    return result ;
  }
}
