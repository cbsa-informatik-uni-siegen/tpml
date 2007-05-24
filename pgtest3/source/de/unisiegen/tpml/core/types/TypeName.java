package de.unisiegen.tpml.core.types ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Represents an identifier in the type hierarchy.
 * 
 * @author Christian Fehler
 * @version $Rev:1056 $
 */
public final class TypeName extends MonoType
{
  /**
   * The name of the {@link TypeName}.
   * 
   * @see #getName()
   */
  private String name ;


  /**
   * Allocates a new {@link TypeName} with the given <code>name</code>.
   * 
   * @param pName the name of the {@link TypeName}.
   */
  public TypeName ( String pName )
  {
    this.name = pName ;
  }


  /**
   * Allocates a new {@link TypeName} with the given <code>name</code>.
   * 
   * @param pName the name of the identifier.
   * @param pParserStartOffset The start offset of this {@link Type} in the
   *          source code.
   * @param pParserEndOffset The end offset of this {@link Type} in the source
   *          code.
   */
  public TypeName ( String pName , int pParserStartOffset , int pParserEndOffset )
  {
    this ( pName ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public TypeName clone ( )
  {
    return new TypeName ( this.name ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#equals(Object)
   */
  @ Override
  public boolean equals ( Object obj )
  {
    if ( obj instanceof TypeName )
    {
      TypeName other = ( TypeName ) obj ;
      return this.name.equals ( other.name ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Type-Name" ; //$NON-NLS-1$
  }


  /**
   * Returns the name of the {@link TypeName}.
   * 
   * @return the name of the {@link TypeName}.
   */
  public String getName ( )
  {
    return this.name ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.name.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#substitute(TypeSubstitution)
   */
  @ Override
  public TypeName substitute ( TypeSubstitution pTypeSubstitution )
  {
    if ( pTypeSubstitution == null )
    {
      throw new NullPointerException ( "Substitution is null" ) ; //$NON-NLS-1$
    }
    return this ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory factory )
  {
    if ( this.prettyStringBuilder == null )
    {
      // TODO PrettyPrintPriorities
      this.prettyStringBuilder = factory.newBuilder ( this , 0 ) ;
      this.prettyStringBuilder.addIdentifier ( this.name ) ;
    }
    return this.prettyStringBuilder ;
  }
}
