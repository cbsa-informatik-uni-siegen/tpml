package de.unisiegen.tpml.core.types ;


import java.util.ArrayList ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.interfaces.DefaultTypeNames ;
import de.unisiegen.tpml.core.interfaces.IdentifierOrTypeName ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Represents an type name in the type hierarchy.
 * 
 * @author Christian Fehler
 * @version $Rev:1056 $
 */
public final class TypeName extends MonoType implements IdentifierOrTypeName
{
  /**
   * String for the case that the type substitution is null.
   */
  private static final String TYPE_SUBSTITUTION_NULL = "type substitution is null" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Type}.
   */
  private static final String CAPTION = Type.getCaption ( TypeName.class ) ;


  /**
   * String for the case that the name is null.
   */
  private static final String NAME_NULL = "name is null" ; //$NON-NLS-1$


  /**
   * The {@link Type} in which this {@link TypeName} is bound.
   * 
   * @see #getBoundToType()
   * @see #setBoundTo(Type,TypeName)
   */
  private Type boundToType ;


  /**
   * The {@link TypeName} to which this {@link TypeName} is bound.
   * 
   * @see #getBoundToTypeName()
   * @see #setBoundTo(Type,TypeName)
   */
  private TypeName boundToTypeName ;


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
    if ( pName == null )
    {
      throw new NullPointerException ( NAME_NULL ) ;
    }
    this.name = pName ;
  }


  /**
   * Allocates a new {@link TypeName} with the given <code>name</code>.
   * 
   * @param pName The name of the type name.
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
   * Returns the {@link Type} in which this {@link TypeName} is bound.
   * 
   * @return The {@link Type} in which this {@link TypeName} is bound.
   * @see #boundToType
   * @see #setBoundTo(Type,TypeName)
   */
  public Type getBoundToType ( )
  {
    return this.boundToType ;
  }


  /**
   * Returns the {@link TypeName} to which this {@link TypeName} is bound.
   * 
   * @return The {@link TypeName} to which this {@link TypeName} is bound.
   * @see #boundToTypeName
   * @see #setBoundTo(Type,TypeName)
   */
  public TypeName getBoundToTypeName ( )
  {
    return this.boundToTypeName ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return CAPTION ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  @ Override
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = super.getLatexCommands ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_TYPE_NAME , 1 ,
        "\\textit{#1}" , "name" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    return commands ;
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
   * Returns the prefix of this {@link Expression}.
   * 
   * @return The prefix of this {@link Expression}.
   * @see #prefix
   */
  @ Override
  public String getPrefix ( )
  {
    if ( this.prefix == null )
    {
      if ( ( this.parent != null )
          && ( this.parent instanceof DefaultTypeNames ) )
      {
        TypeName [ ] typeNames = ( ( DefaultTypeNames ) this.parent )
            .getTypeNames ( ) ;
        for ( TypeName typeName : typeNames )
        {
          if ( typeName == this )
          {
            this.prefix = PREFIX_TYPE_NAME ;
            return this.prefix ;
          }
        }
      }
      this.prefix = PREFIX_TAU ;
    }
    return this.prefix ;
  }


  /**
   * Returns a list of the free {@link TypeName}s in this {@link Type}.
   * 
   * @return A list of the free {@link TypeName}s in this {@link Type}.
   */
  @ Override
  public ArrayList < TypeName > getTypeNamesFree ( )
  {
    if ( this.typeNamesFree == null )
    {
      this.typeNamesFree = new ArrayList < TypeName > ( ) ;
      this.typeNamesFree.add ( this ) ;
    }
    return this.typeNamesFree ;
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
   * Sets the {@link TypeName} to which this {@link TypeName} is bound and the
   * {@link Type} in which this {@link TypeName} is bound.
   * 
   * @param pBoundToType The {@link Type} in which this {@link Identifier} is
   *          bound.
   * @param pBoundToTypeName The {@link TypeName} to which this
   *          {@link Identifier} is bound.
   * @see #boundToTypeName
   * @see #getBoundToTypeName()
   */
  public void setBoundTo ( Type pBoundToType , TypeName pBoundToTypeName )
  {
    if ( ( this.boundToTypeName != null )
        && ( this.boundToTypeName != pBoundToTypeName ) )
    {
      System.err
          .println ( "An TypeName can not be bound to more than one Type!" ) ; //$NON-NLS-1$
      System.err.println ( "TypeName: " + this ) ; //$NON-NLS-1$
      System.err.println ( "Old boundToType: " + this.boundToType ) ; //$NON-NLS-1$
      System.err.println ( "New boundToType: " + pBoundToType ) ; //$NON-NLS-1$
      System.err.println ( "Old boundToTypeName: " + this.boundToTypeName ) ; //$NON-NLS-1$
      System.err.println ( "New boundToTypeName: " + pBoundToTypeName ) ; //$NON-NLS-1$
    }
    this.boundToType = pBoundToType ;
    this.boundToTypeName = pBoundToTypeName ;
  }


  /**
   * Substitutes the type <code>pTau</code> for the {@link TypeName}
   * <code>pTypeName</code> in this type, and returns the resulting type. The
   * resulting type may be a new <code>Type</code> object or if no
   * substitution took place, the same object. The method operates recursively.
   * 
   * @param pTypeName The {@link TypeName}.
   * @param pTau The {@link MonoType}.
   * @return The resulting {@link Type}.
   */
  @ Override
  public MonoType substitute ( TypeName pTypeName , MonoType pTau )
  {
    if ( pTypeName.equals ( this ) )
    {
      /*
       * We need to clone the type here to make sure we can distinguish an type
       * in the pretty printer that is substituted multiple times
       */
      return pTau.clone ( ) ;
    }
    return this ;
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
      throw new NullPointerException ( TYPE_SUBSTITUTION_NULL ) ;
    }
    return this ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  @ Override
  public final LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
  {
    if ( this.latexStringBuilder == null )
    {
      this.latexStringBuilder = pLatexStringBuilderFactory.newBuilder ( this ,
          PRIO_TYPE_NAME , LATEX_TYPE_NAME , pIndent , this.toPrettyString ( )
              .toString ( ) ) ;
      this.latexStringBuilder.addText ( "{" //$NON-NLS-1$
          + this.name.replaceAll ( "_" , "\\\\_" ) + "}" ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    return this.latexStringBuilder ;
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
      this.prettyStringBuilder = factory.newBuilder ( this , PRIO_TYPE_NAME ) ;
      this.prettyStringBuilder.addIdentifier ( this.name ) ;
    }
    return this.prettyStringBuilder ;
  }
}
