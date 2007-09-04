package de.unisiegen.tpml.core.latex ;


/**
 * This class is used for latex commands.
 * 
 * @author Christian Fehler
 */
public final class DefaultLatexCommand implements LatexCommand ,
    LatexCommandNames
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
   * The array of parameter descriptions.
   */
  private String [ ] parameterDescriptions = null ;


  /**
   * Allocates a new <code>DefaultLatexCommand</code> for the specified
   * <code>pName</code>, <code>pParameterCount</code> and
   * <code>pBody</code>.
   * 
   * @param pName The name of the new latex command.
   * @param pParameterCount The parameter count of the new latex command.
   * @param pBody The body of the new latex command.
   * @param pParameterDescriptions The array of parameter descriptions.
   */
  public DefaultLatexCommand ( String pName , int pParameterCount ,
      String pBody , String ... pParameterDescriptions )
  {
    this.name = pName ;
    if ( pParameterCount < 0 )
    {
      throw new IllegalArgumentException ( "parameter is less than zero" ) ; //$NON-NLS-1$
    }
    this.parameterCount = pParameterCount ;
    this.body = pBody ;
    if ( pParameterCount != pParameterDescriptions.length )
    {
      throw new IllegalArgumentException (
          "parameter count is not equal to the description count" ) ; //$NON-NLS-1$
    }
    this.parameterDescriptions = pParameterDescriptions ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Comparable#compareTo(Object)
   */
  public int compareTo ( LatexCommand pOther )
  {
    DefaultLatexCommand other = ( DefaultLatexCommand ) pOther ;
    if ( this.name.startsWith ( LATEX_KEY ) )
    {
      // case 1
      if ( other.name.startsWith ( LATEX_KEY ) )
      {
        return this.name.compareTo ( other.name ) ;
      }
      // case 2
      return - 1 ;
    }
    // case 3
    if ( other.name.startsWith ( LATEX_KEY ) )
    {
      return 1 ;
    }
    // case 4
    return this.name.compareTo ( other.name ) ;
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
   * Returns the body.
   * 
   * @return The body.
   * @see #body
   */
  public String getBody ( )
  {
    return this.body ;
  }


  /**
   * Returns the name.
   * 
   * @return The name.
   * @see #name
   */
  public String getName ( )
  {
    return this.name ;
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
    StringBuilder result = new StringBuilder ( ) ;
    if ( this.parameterDescriptions != null )
    {
      result.append ( "% " ) ; //$NON-NLS-1$
      result.append ( this.name ) ;
      for ( String descriptions : this.parameterDescriptions )
      {
        result.append ( "{" ) ; //$NON-NLS-1$
        result.append ( descriptions ) ;
        result.append ( "}" ) ; //$NON-NLS-1$
      }
      result.append ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
    }
    else
    {
      result.append ( "% " ) ; //$NON-NLS-1$
      result.append ( this.name ) ;
      result.append ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
    }
    result.append ( "\\newcommand{\\" ) ; //$NON-NLS-1$
    result.append ( this.name ) ;
    result.append ( "}" ) ; //$NON-NLS-1$
    if ( this.parameterCount > 0 )
    {
      result.append ( "[" ) ; //$NON-NLS-1$
      result.append ( this.parameterCount ) ;
      result.append ( "]" ) ; //$NON-NLS-1$
    }
    result.append ( "{" ) ; //$NON-NLS-1$
    result.append ( this.body ) ;
    result.append ( "}" ) ; //$NON-NLS-1$
    return result.toString ( ) ;
  }
}
