package de.unisiegen.tpml.core.types ;


import java.text.MessageFormat ;
import java.util.ArrayList ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException ;
import de.unisiegen.tpml.core.interfaces.BoundTypeNames ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.languages.LanguageParserException ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.util.BoundRenaming ;


/**
 * This class represents {@link RecType} in our type systems.
 * 
 * @author Christian Fehler
 * @version $Rev:420 $
 * @see #typeNames
 */
public final class RecType extends MonoType implements DefaultTypes ,
    BoundTypeNames
{
  /**
   * String for the case that the type substitution is null.
   */
  private static final String TYPE_SUBSTITUTION_NULL = "type substitution is null" ; //$NON-NLS-1$


  /**
   * Indeces of the child {@link Type}s.
   */
  private static final int [ ] INDICES_TYPE = new int [ ]
  { - 1 } ;


  /**
   * Indeces of the child {@link TypeName}s.
   */
  private static final int [ ] INDICES_TYPE_NAME = new int [ ]
  { - 1 } ;


  /**
   * String for the case that tau is null.
   */
  private static final String TAU_NULL = "tau is null" ; //$NON-NLS-1$


  /**
   * String for the case that type name is null.
   */
  private static final String TYPE_NAME_NULL = "type name is null" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Type}.
   */
  private static final String CAPTION = Type.getCaption ( RecType.class ) ;


  /**
   * The list of {@link TypeName}s.
   * 
   * @see #getTypeNames()
   */
  private TypeName [ ] typeNames ;


  /**
   * The children {@link Type}s of this {@link Type}.
   */
  private MonoType [ ] types ;


  /**
   * Allocates a new <code>RecType</code> with the specified {@link TypeName}
   * and {@link Type}.
   * 
   * @param pTypeName The {@link TypeName}.
   * @param pTau The {@link Type}.
   */
  public RecType ( TypeName pTypeName , MonoType pTau )
  {
    if ( pTypeName == null )
    {
      throw new NullPointerException ( TYPE_NAME_NULL ) ;
    }
    if ( pTau == null )
    {
      throw new NullPointerException ( TAU_NULL ) ;
    }
    // TypeName
    this.typeNames = new TypeName [ ]
    { pTypeName } ;
    this.typeNames [ 0 ].setParent ( this ) ;
    // Type
    this.types = new MonoType [ ]
    { pTau } ;
    this.types [ 0 ].setParent ( this ) ;
  }


  /**
   * Allocates a new <code>RecType</code> with the specified {@link TypeName}
   * and {@link Type}.
   * 
   * @param pTypeName The {@link TypeName}.
   * @param pTau The {@link Type}.
   * @param pParserStartOffset The start offset of this {@link Type} in the
   *          source code.
   * @param pParserEndOffset The end offset of this {@link Type} in the source
   *          code.
   */
  public RecType ( TypeName pTypeName , MonoType pTau , int pParserStartOffset ,
      int pParserEndOffset )
  {
    this ( pTypeName , pTau ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
    if ( pTau.equals ( pTypeName ) )
    {
      String message = MessageFormat.format ( Messages
          .getString ( "Exception.6" ) , pTypeName ) ; //$NON-NLS-1$
      throw new LanguageParserMultiException ( new String [ ]
      { message , message } , new int [ ]
      { pTypeName.getParserStartOffset ( ) , pTau.getParserStartOffset ( ) } ,
          new int [ ]
          { pTypeName.getParserEndOffset ( ) , pTau.getParserEndOffset ( ) } ) ;
    }
    if ( pTau instanceof RecType )
    {
      throw new LanguageParserException ( MessageFormat.format ( Messages
          .getString ( "Exception.7" ) , this.toPrettyString ( ) ) , //$NON-NLS-1$
          pParserStartOffset , pParserEndOffset ) ;
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public RecType clone ( )
  {
    return new RecType ( this.typeNames [ 0 ].clone ( ) , this.types [ 0 ]
        .clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof RecType )
    {
      RecType other = ( RecType ) pObject ;
      return ( this.typeNames [ 0 ].equals ( other.typeNames [ 0 ] ) )
          && ( this.types [ 0 ].equals ( other.types [ 0 ] ) ) ;
    }
    return false ;
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
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_KEYWORD_MU , 0 ,
        "\\textbf{$\\mu$}" ) ) ; //$NON-NLS-1$
    commands.add ( new DefaultLatexCommand ( LATEX_REC_TYPE , 2 , "\\" //$NON-NLS-1$
        + LATEX_KEYWORD_MU + "#1.#2" ) ) ; //$NON-NLS-1$
    for ( LatexCommand command : this.typeNames [ 0 ].getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : this.types [ 0 ].getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    return commands ;
  }


  /**
   * Returns the sub {@link Type}.
   * 
   * @return The sub {@link Type}.
   */
  public MonoType getTau ( )
  {
    return this.types [ 0 ] ;
  }


  /**
   * Returns the {@link TypeName} of this {@link Type}.
   * 
   * @return The {@link TypeName} of this {@link Type}.
   * @see #typeNames
   */
  public TypeName getTypeName ( )
  {
    return this.typeNames [ 0 ] ;
  }


  /**
   * Returns the {@link TypeName}s of this {@link Type}.
   * 
   * @return The {@link TypeName}s of this {@link Type}.
   * @see #typeNames
   */
  public TypeName [ ] getTypeNames ( )
  {
    return this.typeNames ;
  }


  /**
   * Returns a list of lists of in this {@link Type} bound {@link TypeName}s.
   * 
   * @return A list of lists of in this {@link Type} bound {@link TypeName}s.
   */
  public ArrayList < ArrayList < TypeName >> getTypeNamesBound ( )
  {
    if ( this.boundTypeNames == null )
    {
      this.boundTypeNames = new ArrayList < ArrayList < TypeName >> ( 1 ) ;
      ArrayList < TypeName > boundTypeNamesList = new ArrayList < TypeName > ( ) ;
      ArrayList < TypeName > boundTau = this.types [ 0 ].getTypeNamesFree ( ) ;
      for ( TypeName freeTypeName : boundTau )
      {
        if ( this.typeNames [ 0 ].equals ( freeTypeName ) )
        {
          freeTypeName.setBoundTo ( this , this.typeNames [ 0 ] ) ;
          boundTypeNamesList.add ( freeTypeName ) ;
        }
      }
      this.boundTypeNames.add ( boundTypeNamesList ) ;
    }
    return this.boundTypeNames ;
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
      this.typeNamesFree.addAll ( this.types [ 0 ].getTypeNamesFree ( ) ) ;
      while ( this.typeNamesFree.remove ( this.typeNames [ 0 ] ) )
      {
        // Remove all TypeNames with the same name
      }
    }
    return this.typeNamesFree ;
  }


  /**
   * Returns the indices of the child {@link Type}s.
   * 
   * @return The indices of the child {@link Type}s.
   */
  public int [ ] getTypeNamesIndex ( )
  {
    return INDICES_TYPE_NAME ;
  }


  /**
   * Returns the sub {@link Type}s.
   * 
   * @return the sub {@link Type}s.
   */
  public MonoType [ ] getTypes ( )
  {
    return this.types ;
  }


  /**
   * Returns the indices of the child {@link Type}s.
   * 
   * @return The indices of the child {@link Type}s.
   */
  public int [ ] getTypesIndex ( )
  {
    return INDICES_TYPE ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.typeNames [ 0 ].hashCode ( ) + this.types [ 0 ].hashCode ( ) ;
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
  public RecType substitute ( TypeName pTypeName , MonoType pTau )
  {
    /*
     * Do not substitute, if the TypeNames are equal.
     */
    if ( this.typeNames [ 0 ].equals ( pTypeName ) )
    {
      return this ;
    }
    /*
     * Perform the bound renaming if required.
     */
    BoundRenaming < TypeName > boundRenaming = new BoundRenaming < TypeName > ( ) ;
    boundRenaming.add ( this.getTypeNamesFree ( ) ) ;
    boundRenaming.add ( pTau.getTypeNamesFree ( ) ) ;
    boundRenaming.add ( pTypeName ) ;
    TypeName newTypeName = boundRenaming.newTypeName ( this.typeNames [ 0 ] ) ;
    /*
     * Substitute the old Identifier only with the new Identifier, if they are
     * different.
     */
    MonoType newTau = this.types [ 0 ] ;
    if ( ! this.typeNames [ 0 ].equals ( newTypeName ) )
    {
      newTau = newTau.substitute ( this.typeNames [ 0 ] , newTypeName ) ;
    }
    /*
     * Perform the substitution.
     */
    newTau = newTau.substitute ( pTypeName , pTau ) ;
    return new RecType ( newTypeName , newTau ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#substitute(TypeSubstitution)
   */
  @ Override
  public RecType substitute ( TypeSubstitution pTypeSubstitution )
  {
    if ( pTypeSubstitution == null )
    {
      throw new NullPointerException ( TYPE_SUBSTITUTION_NULL ) ;
    }
    return new RecType ( this.typeNames [ 0 ] , this.types [ 0 ]
        .substitute ( pTypeSubstitution ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#toLatexStringBuilder(LatexStringBuilderFactory)
   */
  @ Override
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory )
  {
    if ( this.latexStringBuilder == null )
    {
      this.latexStringBuilder = pLatexStringBuilderFactory.newBuilder ( this ,
          PRIO_REC_TYPE , LATEX_REC_TYPE ) ;
      this.latexStringBuilder.addBuilder ( this.typeNames [ 0 ]
          .toLatexStringBuilder ( pLatexStringBuilderFactory ) ,
          PRIO_REC_TYPE_TYPE_NAME ) ;
      this.latexStringBuilder.addBreak ( ) ;
      this.latexStringBuilder.addBuilder ( this.types [ 0 ]
          .toLatexStringBuilder ( pLatexStringBuilderFactory ) ,
          PRIO_REC_TYPE_TAU ) ;
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
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_REC_TYPE ) ;
      this.prettyStringBuilder.addKeyword ( PRETTY_MU ) ;
      this.prettyStringBuilder.addBuilder ( this.typeNames [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_REC_TYPE_TYPE_NAME ) ;
      this.prettyStringBuilder.addText ( PRETTY_DOT ) ;
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_REC_TYPE_TAU ) ;
    }
    return this.prettyStringBuilder ;
  }
}
