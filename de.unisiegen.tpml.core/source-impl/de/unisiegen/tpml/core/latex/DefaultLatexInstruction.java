package de.unisiegen.tpml.core.latex;


/**
 * This class is used for latex instructions.
 * 
 * @author Christian Fehler
 * @version $Id$
 */
public final class DefaultLatexInstruction implements LatexInstruction,
    LatexCommandNames
{

  /**
   * The text of the latex instruction.
   */
  private String text;


  /**
   * The description of this instruction.
   */
  private String description = null;


  /**
   * Allocates a new <code>DefaultLatexInstruction</code> for the specified
   * <code>pText</code>.
   * 
   * @param pText The text of the new latex instruction.
   */
  public DefaultLatexInstruction ( String pText )
  {
    this.text = pText;
  }


  /**
   * Allocates a new <code>DefaultLatexInstruction</code> for the specified
   * <code>pText</code>.
   * 
   * @param pText The text of the new latex instruction.
   * @param pDescription The description of this instruction.
   */
  public DefaultLatexInstruction ( String pText, String pDescription )
  {
    this ( pText );
    this.description = pDescription;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof DefaultLatexInstruction )
    {
      DefaultLatexInstruction other = ( DefaultLatexInstruction ) pObject;
      return this.text.equals ( other.text );
    }
    return false;
  }


  /**
   * Returns the text.
   * 
   * @return The text.
   * @see #text
   */
  public String getText ()
  {
    return this.text;
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
    if ( this.description != null )
    {
      result.append ( "% " ); //$NON-NLS-1$
      result.append ( this.description );
      result.append ( LATEX_LINE_BREAK_SOURCE_CODE );
    }
    result.append ( this.text );
    return result.toString ();
  }
}
