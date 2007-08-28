package de.unisiegen.tpml.core.types ;


import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.interfaces.DefaultIdentifiers ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.interfaces.ExpressionOrType ;
import de.unisiegen.tpml.core.interfaces.SortedChildren ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * This class represents {@link RowType} in our type systems.
 * 
 * @author Christian Fehler
 * @version $Rev:420 $
 * @see ObjectType
 */
public final class RowType extends MonoType implements DefaultIdentifiers ,
    DefaultTypes , SortedChildren
{
  /**
   * The row type union is not defined string.
   */
  private static final String ROW_TYPE_UNION = "row type union is not defined" ; //$NON-NLS-1$


  /**
   * String for the case that the type substitution is null.
   */
  private static final String TYPE_SUBSTITUTION_NULL = "type substitution is null" ; //$NON-NLS-1$


  /**
   * String for the case that the identifiers are null.
   */
  private static final String IDENTIFIERS_NULL = "identifiers is null" ; //$NON-NLS-1$


  /**
   * String for the case that one identifier are null.
   */
  private static final String IDENTIFIER_NULL = "one identifier is null" ; //$NON-NLS-1$


  /**
   * The identifier has the wrong set.
   */
  private static final String WRONG_SET = "the set of the identifier has to be 'attribute' or 'method'" ; //$NON-NLS-1$


  /**
   * String for the case that the types are null.
   */
  private static final String TYPES_NULL = "types is null" ; //$NON-NLS-1$


  /**
   * String for the case that one type is null.
   */
  private static final String TYPE_NULL = "one type is null" ; //$NON-NLS-1$


  /**
   * String for the case that the arity of identifiers and types doesn´t match.
   */
  private static final String ARITY = "the arity of identifiers and types must match" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Type}.
   */
  private static final String CAPTION = Type.getCaption ( RowType.class ) ;


  /**
   * Returns a new <code>RowType</code> which unions the method types from the
   * input <code>RowTypes</code>. The new <code>RowType</code> contains at
   * first the method types which are in both input <code>RowTypes</code>,
   * then the method types which are in phi1 and after than the method types
   * which are in phi2.
   * 
   * @param pPhi1 The first input <code>RowType</code>.
   * @param pPhi2 The second input <code>RowType</code>.
   * @return A new <code>RowType</code> which unions the method types from the
   *         input <code>RowTypes</code>.
   */
  public static RowType union ( RowType pPhi1 , RowType pPhi2 )
  {
    MonoType [ ] phi1Types = pPhi1.getTypes ( ) ;
    MonoType [ ] phi2Types = pPhi2.getTypes ( ) ;
    ArrayList < Identifier > phi1Identifiers = new ArrayList < Identifier > ( ) ;
    for ( Identifier id : pPhi1.getIdentifiers ( ) )
    {
      phi1Identifiers.add ( id ) ;
    }
    ArrayList < Identifier > phi2Identifiers = new ArrayList < Identifier > ( ) ;
    for ( Identifier id : pPhi2.getIdentifiers ( ) )
    {
      phi2Identifiers.add ( id ) ;
    }
    // Result
    ArrayList < Identifier > resultIdentifiers = new ArrayList < Identifier > ( ) ;
    ArrayList < MonoType > resultTypes = new ArrayList < MonoType > ( ) ;
    // Common method types
    for ( int i = phi1Identifiers.size ( ) - 1 ; i >= 0 ; i -- )
    {
      for ( int j = phi2Identifiers.size ( ) - 1 ; j >= 0 ; j -- )
      {
        if ( ( phi1Identifiers.get ( i ) != null )
            && ( phi2Identifiers.get ( j ) != null )
            && ( phi1Identifiers.get ( i ).equals ( phi2Identifiers.get ( j ) ) ) )
        {
          if ( ! ( phi1Types [ i ].equals ( phi2Types [ j ] ) ) )
          {
            throw new RuntimeException ( ROW_TYPE_UNION ) ;
          }
          resultIdentifiers.add ( 0 , phi1Identifiers.get ( i ) ) ;
          resultTypes.add ( 0 , phi1Types [ i ] ) ;
          phi1Identifiers.set ( i , null ) ;
          phi2Identifiers.set ( j , null ) ;
        }
      }
    }
    // Method types from phi1
    for ( int i = 0 ; i < phi1Identifiers.size ( ) ; i ++ )
    {
      if ( phi1Identifiers.get ( i ) != null )
      {
        resultIdentifiers.add ( phi1Identifiers.get ( i ) ) ;
        resultTypes.add ( phi1Types [ i ] ) ;
      }
    }
    // Method types from phi2
    for ( int i = 0 ; i < phi2Identifiers.size ( ) ; i ++ )
    {
      if ( phi2Identifiers.get ( i ) != null )
      {
        resultIdentifiers.add ( phi2Identifiers.get ( i ) ) ;
        resultTypes.add ( phi2Types [ i ] ) ;
      }
    }
    // Create the new RowType
    Identifier [ ] newIdentifiers = new Identifier [ resultIdentifiers.size ( ) ] ;
    MonoType [ ] newTypes = new MonoType [ resultTypes.size ( ) ] ;
    for ( int i = 0 ; i < resultIdentifiers.size ( ) ; i ++ )
    {
      newIdentifiers [ i ] = resultIdentifiers.get ( i ) ;
      newTypes [ i ] = resultTypes.get ( i ) ;
    }
    return new RowType ( newIdentifiers , newTypes ) ;
  }


  /**
   * The list of identifiers.
   * 
   * @see #getIdentifiers()
   */
  private Identifier [ ] identifiers ;


  /**
   * The children {@link Type}s of this {@link Type}.
   */
  private MonoType [ ] types ;


  /**
   * Indeces of the child {@link Identifier}s.
   */
  private int [ ] indicesId ;


  /**
   * Indeces of the child {@link Type}s.
   */
  private int [ ] indicesType ;


  /**
   * The remaining {@link RowType}.
   * 
   * @see #getRemainingRowType()
   */
  private MonoType remainingRowType = null ;


  /**
   * Allocates a new <code>RowType</code> with the specified
   * {@link Identifier}s and {@link Type}s.
   * 
   * @param pIdentifiers The {@link Identifier}s.
   * @param pTypes The {@link Type}s.
   */
  public RowType ( Identifier [ ] pIdentifiers , MonoType [ ] pTypes )
  {
    if ( pIdentifiers == null )
    {
      throw new NullPointerException ( IDENTIFIERS_NULL ) ;
    }
    for ( Identifier id : pIdentifiers )
    {
      if ( id == null )
      {
        throw new NullPointerException ( IDENTIFIER_NULL ) ;
      }
      if ( ( ! Identifier.Set.ATTRIBUTE.equals ( id.getSet ( ) ) )
          && ( ! Identifier.Set.METHOD.equals ( id.getSet ( ) ) ) )
      {
        throw new IllegalArgumentException ( WRONG_SET ) ;
      }
    }
    if ( pTypes == null )
    {
      throw new NullPointerException ( TYPES_NULL ) ;
    }
    if ( pIdentifiers.length != pTypes.length )
    {
      throw new IllegalArgumentException ( ARITY ) ;
    }
    for ( MonoType type : pTypes )
    {
      if ( type == null )
      {
        throw new NullPointerException ( TYPE_NULL ) ;
      }
    }
    // Identifier
    this.identifiers = pIdentifiers ;
    this.indicesId = new int [ this.identifiers.length ] ;
    for ( int i = 0 ; i < this.identifiers.length ; i ++ )
    {
      this.identifiers [ i ].setParent ( this ) ;
      this.indicesId [ i ] = i + 1 ;
    }
    // Type
    this.types = pTypes ;
    this.indicesType = new int [ this.types.length ] ;
    for ( int i = 0 ; i < this.types.length ; i ++ )
    {
      this.types [ i ].setParent ( this ) ;
      this.indicesType [ i ] = i + 1 ;
    }
  }


  /**
   * Allocates a new <code>RowType</code> with the specified
   * {@link Identifier}s and {@link Type}s.
   * 
   * @param pIdentifiers The {@link Identifier}s.
   * @param pTypes The {@link Type}s.
   * @param pParserStartOffset The start offset of this {@link Type} in the
   *          source code.
   * @param pParserEndOffset The end offset of this {@link Type} in the source
   *          code.
   */
  public RowType ( Identifier [ ] pIdentifiers , MonoType [ ] pTypes ,
      int pParserStartOffset , int pParserEndOffset )
  {
    this ( pIdentifiers , pTypes ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * Allocates a new <code>RowType</code> with the specified
   * {@link Identifier}s and {@link Type}s.
   * 
   * @param pIdentifiers The {@link Identifier}s.
   * @param pTypes The {@link Type}s.
   * @param pRemainingRowType The remaining {@link RowType}.
   */
  public RowType ( Identifier [ ] pIdentifiers , MonoType [ ] pTypes ,
      MonoType pRemainingRowType )
  {
    this ( pIdentifiers , pTypes ) ;
    if ( ( pRemainingRowType != null )
        && ( pRemainingRowType instanceof RowType ) )
    {
      RowType rowType = ( RowType ) pRemainingRowType ;
      this.identifiers = new Identifier [ pIdentifiers.length
          + rowType.getIdentifiers ( ).length ] ;
      this.indicesId = new int [ this.identifiers.length ] ;
      for ( int i = 0 ; i < pIdentifiers.length ; i ++ )
      {
        this.identifiers [ i ] = pIdentifiers [ i ] ;
        this.indicesId [ i ] = i + 1 ;
      }
      for ( int i = 0 ; i < rowType.getIdentifiers ( ).length ; i ++ )
      {
        this.identifiers [ pIdentifiers.length + i ] = rowType
            .getIdentifiers ( ) [ i ] ;
        this.indicesId [ pIdentifiers.length + i ] = pIdentifiers.length + i
            + 1 ;
      }
      this.types = new MonoType [ pTypes.length + rowType.getTypes ( ).length ] ;
      this.indicesType = new int [ this.types.length ] ;
      for ( int i = 0 ; i < pTypes.length ; i ++ )
      {
        this.types [ i ] = pTypes [ i ] ;
        this.indicesType [ i ] = i + 1 ;
      }
      for ( int i = 0 ; i < rowType.getTypes ( ).length ; i ++ )
      {
        this.types [ pTypes.length + i ] = rowType.getTypes ( ) [ i ] ;
        this.indicesType [ pTypes.length + i ] = pTypes.length + i + 1 ;
      }
      this.remainingRowType = rowType.getRemainingRowType ( ) ;
    }
    else
    {
      this.remainingRowType = pRemainingRowType ;
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public RowType clone ( )
  {
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ( ) ;
    }
    MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int i = 0 ; i < newTypes.length ; i ++ )
    {
      newTypes [ i ] = this.types [ i ].clone ( ) ;
    }
    MonoType newRemainingRowType = ( this.remainingRowType == null ) ? null
        : this.remainingRowType.clone ( ) ;
    return new RowType ( newIdentifiers , newTypes , newRemainingRowType ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof RowType )
    {
      RowType other = ( RowType ) pObject ;
      return Arrays.equals ( this.identifiers , other.identifiers )
          && Arrays.equals ( this.types , other.types )
          && ( ( this.remainingRowType == null ) ? other.remainingRowType == null
              : this.remainingRowType.equals ( other.remainingRowType ) ) ;
    }
    return false ;
  }


  /**
   * Returns true if the given {@link RowType} is equals to this {@link RowType}
   * ignoring the order of the method names. Otherwise false.
   * 
   * @param pRowType The given {@link RowType}.
   * @return True if the given {@link RowType} is equals to this {@link RowType}
   *         ignoring the order of the method names. Otherwise false.
   */
  public boolean equalsIgnoreOrder ( RowType pRowType )
  {
    if ( this.identifiers.length != pRowType.identifiers.length )
    {
      return false ;
    }
    for ( int i = 0 ; i < this.identifiers.length ; i ++ )
    {
      boolean found = false ;
      for ( int j = 0 ; j < pRowType.identifiers.length ; j ++ )
      {
        if ( this.identifiers [ i ].equals ( pRowType.identifiers [ j ] ) )
        {
          found = true ;
          if ( ! this.types [ i ].equals ( pRowType.types [ j ] ) )
          {
            return false ;
          }
          break ;
        }
      }
      if ( ! found )
      {
        return false ;
      }
    }
    return true ;
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
   * Returns the {@link Identifier}s of this {@link Expression}.
   * 
   * @return The {@link Identifier}s of this {@link Expression}.
   * @see #identifiers
   */
  public Identifier [ ] getIdentifiers ( )
  {
    return this.identifiers ;
  }


  /**
   * Returns the indices of the child {@link Identifier}s.
   * 
   * @return The indices of the child {@link Identifier}s.
   */
  public int [ ] getIdentifiersIndex ( )
  {
    return this.indicesId ;
  }


  /**
   * Returns the index of the given {@link Identifier}, if this {@link RowType}
   * contains an {@link Identifier} with the same name, otherwise -1.
   * 
   * @param pIdentifier The given {@link Identifier}.
   * @return The index of the given {@link Identifier}, if this {@link RowType}
   *         contains an {@link Identifier} with the same name, otherwise -1.
   */
  public int getIndexOfIdentifier ( Identifier pIdentifier )
  {
    for ( int i = 0 ; i < this.identifiers.length ; i ++ )
    {
      if ( pIdentifier.equals ( this.identifiers [ i ] ) )
      {
        return i ;
      }
    }
    return - 1 ;
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
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_ATTR , 0 ,
        "\\textbf{attr}" ) ) ; //$NON-NLS-1$ 
    commands.add ( new DefaultLatexCommand ( LATEX_ROW_TYPE , 1 , "#1" , //$NON-NLS-1$
        "epsilon | attr a : tau ; phi1 | m : tau ; phi1" ) ) ; //$NON-NLS-1$
    if ( this.remainingRowType != null )
    {
      for ( LatexCommand command : this.remainingRowType.getLatexCommands ( ) )
      {
        commands.add ( command ) ;
      }
    }
    return commands ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#getPrefix()
   */
  @ Override
  public String getPrefix ( )
  {
    if ( this.prefix == null )
    {
      this.prefix = PREFIX_PHI ;
    }
    return this.prefix ;
  }


  /**
   * Returns the remaining {@link RowType}.
   * 
   * @return The remaining {@link RowType}.
   * @see #remainingRowType
   */
  public MonoType getRemainingRowType ( )
  {
    return this.remainingRowType ;
  }


  /**
   * Returns the {@link Identifier}s and {@link Type}s in the right sorting.
   * 
   * @return The {@link Identifier}s and {@link Type}s in the right sorting.
   * @see SortedChildren#getSortedChildren()
   */
  public ExpressionOrType [ ] getSortedChildren ( )
  {
    ExpressionOrType [ ] result ;
    if ( this.remainingRowType == null )
    {
      result = new ExpressionOrType [ this.identifiers.length
          + this.types.length ] ;
    }
    else
    {
      result = new ExpressionOrType [ this.identifiers.length
          + this.types.length + 1 ] ;
    }
    for ( int i = 0 ; i < this.identifiers.length + this.types.length ; i ++ )
    {
      if ( i % 2 == 0 )
      {
        result [ i ] = this.identifiers [ i / 2 ] ;
      }
      else
      {
        result [ i ] = this.types [ i / 2 ] ;
      }
    }
    if ( this.remainingRowType != null )
    {
      result [ result.length - 1 ] = this.remainingRowType ;
    }
    return result ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#getTypeNamesFree()
   */
  @ Override
  public ArrayList < TypeName > getTypeNamesFree ( )
  {
    if ( this.typeNamesFree == null )
    {
      this.typeNamesFree = new ArrayList < TypeName > ( ) ;
      for ( MonoType type : this.types )
      {
        this.typeNamesFree.addAll ( type.getTypeNamesFree ( ) ) ;
      }
      if ( this.remainingRowType != null )
      {
        this.typeNamesFree.addAll ( this.remainingRowType.getTypeNamesFree ( ) ) ;
      }
    }
    return this.typeNamesFree ;
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
    return this.indicesType ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#getTypeVariablesFree()
   */
  @ Override
  public ArrayList < TypeVariable > getTypeVariablesFree ( )
  {
    if ( this.typeVariablesFree == null )
    {
      this.typeVariablesFree = new ArrayList < TypeVariable > ( ) ;
      for ( MonoType type : this.types )
      {
        this.typeVariablesFree.addAll ( type.getTypeVariablesFree ( ) ) ;
      }
      if ( this.remainingRowType != null )
      {
        this.typeVariablesFree.addAll ( this.remainingRowType
            .getTypeVariablesFree ( ) ) ;
      }
    }
    return this.typeVariablesFree ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return Arrays.hashCode ( this.identifiers ) + Arrays.hashCode ( this.types ) ;
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
  public RowType substitute ( TypeName pTypeName , MonoType pTau )
  {
    MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int i = 0 ; i < newTypes.length ; i ++ )
    {
      newTypes [ i ] = this.types [ i ].substitute ( pTypeName , pTau ) ;
    }
    MonoType newRemainingRowType = ( this.remainingRowType == null ) ? null
        : this.remainingRowType.substitute ( pTypeName , pTau ) ;
    return new RowType ( this.identifiers , newTypes , newRemainingRowType ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#substitute(TypeSubstitution)
   */
  @ Override
  public RowType substitute ( TypeSubstitution pTypeSubstitution )
  {
    if ( pTypeSubstitution == null )
    {
      throw new NullPointerException ( TYPE_SUBSTITUTION_NULL ) ;
    }
    MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int i = 0 ; i < newTypes.length ; i ++ )
    {
      newTypes [ i ] = this.types [ i ].substitute ( pTypeSubstitution ) ;
    }
    MonoType newRemainingRowType = ( this.remainingRowType == null ) ? null
        : this.remainingRowType.substitute ( pTypeSubstitution ) ;
    if ( newRemainingRowType instanceof RowType )
    {
      RowType r = ( RowType ) newRemainingRowType ;
      if ( r.getTypes ( ).length == 0 )
      {
        newRemainingRowType = null ;
      }
    }
    return new RowType ( this.identifiers , newTypes , newRemainingRowType ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  @ Override
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
  {
    if ( this.latexStringBuilder == null )
    {
      StringBuilder body = new StringBuilder ( ) ;
      for ( int i = 0 ; i < this.types.length ; i ++ )
      {
        if ( i != 0 )
        {
          body.append ( PRETTY_SPACE ) ;
        }
        if ( Identifier.Set.ATTRIBUTE
            .equals ( this.identifiers [ i ].getSet ( ) ) )
        {
          body.append ( PRETTY_ATTR ) ;
          body.append ( PRETTY_SPACE ) ;
        }
        body.append ( this.identifiers [ i ].toPrettyString ( ).toString ( ) ) ;
        body.append ( PRETTY_COLON ) ;
        body.append ( PRETTY_SPACE ) ;
        body.append ( this.types [ i ].toPrettyString ( ).toString ( ) ) ;
        body.append ( PRETTY_SPACE ) ;
        body.append ( PRETTY_SEMI ) ;
      }
      if ( this.remainingRowType != null )
      {
        body.append ( PRETTY_SPACE ) ;
        body.append ( this.remainingRowType.toPrettyString ( ).toString ( ) ) ;
      }
      if ( this.types.length == 0 )
      {
        body.append ( PRETTY_EMPTY_SET ) ;
      }
      String descriptions[] = new String [ 2 + this.identifiers.length
          + this.types.length + ( this.remainingRowType == null ? 0 : 1 ) ] ;
      descriptions [ 0 ] = this.toPrettyString ( ).toString ( ) ;
      descriptions [ 1 ] = body.toString ( ) ;
      for ( int i = 0 ; i < this.identifiers.length ; i ++ )
      {
        descriptions [ 2 + i * 2 ] = this.identifiers [ i ].toPrettyString ( )
            .toString ( ) ;
        descriptions [ 3 + i * 2 ] = this.types [ i ].toPrettyString ( )
            .toString ( ) ;
      }
      if ( this.remainingRowType != null )
      {
        descriptions [ descriptions.length - 1 ] = this.remainingRowType
            .toPrettyString ( ).toString ( ) ;
      }
      this.latexStringBuilder = pLatexStringBuilderFactory.newBuilder (
          PRIO_ROW , LATEX_ROW_TYPE , pIndent , descriptions ) ;
      this.latexStringBuilder.addBuilderBegin ( ) ;
      for ( int i = 0 ; i < this.types.length ; i ++ )
      {
        if ( i != 0 )
        {
          this.latexStringBuilder.addText ( LATEX_SPACE ) ;
        }
        if ( Identifier.Set.ATTRIBUTE
            .equals ( this.identifiers [ i ].getSet ( ) ) )
        {
          this.latexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
          this.latexStringBuilder.addText ( DefaultLatexStringBuilder
              .getIndent ( pIndent + LATEX_INDENT )
              + LATEX_PREFIX_COMMAND + LATEX_KEY_ATTR ) ;
          this.latexStringBuilder.addText ( LATEX_SPACE ) ;
        }
        this.latexStringBuilder.addBuilder ( this.identifiers [ i ]
            .toLatexStringBuilder ( pLatexStringBuilderFactory , pIndent
                + LATEX_INDENT * 2 ) , PRIO_ID ) ;
        this.latexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
        this.latexStringBuilder.addText ( DefaultLatexStringBuilder
            .getIndent ( pIndent + LATEX_INDENT )
            + LATEX_COLON ) ;
        this.latexStringBuilder.addText ( LATEX_SPACE ) ;
        this.latexStringBuilder.addBuilder ( this.types [ i ]
            .toLatexStringBuilder ( pLatexStringBuilderFactory , pIndent
                + LATEX_INDENT * 2 ) , PRIO_ROW_TAU ) ;
        this.latexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
        this.latexStringBuilder.addText ( DefaultLatexStringBuilder
            .getIndent ( pIndent + LATEX_INDENT )
            + LATEX_SPACE ) ;
        this.latexStringBuilder.addText ( LATEX_SEMI ) ;
      }
      if ( this.types.length == 0 )
      {
        this.latexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
        this.latexStringBuilder.addText ( DefaultLatexStringBuilder
            .getIndent ( pIndent + LATEX_INDENT )
            + LATEX_EMPTYSET ) ;
      }
      if ( this.remainingRowType != null )
      {
        this.latexStringBuilder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
        this.latexStringBuilder.addText ( DefaultLatexStringBuilder
            .getIndent ( pIndent + LATEX_INDENT )
            + LATEX_SPACE ) ;
        this.prettyStringBuilder.addBreak ( ) ;
        this.latexStringBuilder.addBuilder ( this.remainingRowType
            .toLatexStringBuilder ( pLatexStringBuilderFactory , pIndent
                + LATEX_INDENT * 2 ) , 0 ) ;
      }
      this.latexStringBuilder.addBuilderEnd ( ) ;
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
          PRIO_ROW ) ;
      for ( int i = 0 ; i < this.types.length ; i ++ )
      {
        if ( i != 0 )
        {
          this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
        }
        if ( Identifier.Set.ATTRIBUTE
            .equals ( this.identifiers [ i ].getSet ( ) ) )
        {
          this.prettyStringBuilder.addKeyword ( PRETTY_ATTR ) ;
          this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
        }
        this.prettyStringBuilder.addBuilder ( this.identifiers [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
        this.prettyStringBuilder.addText ( PRETTY_COLON ) ;
        this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
        this.prettyStringBuilder.addBuilder ( this.types [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_ROW_TAU ) ;
        this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
        this.prettyStringBuilder.addText ( PRETTY_SEMI ) ;
        if ( i != this.types.length - 1 )
        {
          this.prettyStringBuilder.addBreak ( ) ;
        }
      }
      if ( this.types.length == 0 )
      {
        this.prettyStringBuilder.addText ( PRETTY_EMPTY_SET ) ;
      }
      if ( this.remainingRowType != null )
      {
        this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
        this.prettyStringBuilder.addBreak ( ) ;
        this.prettyStringBuilder.addBuilder ( this.remainingRowType
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
      }
    }
    return this.prettyStringBuilder ;
  }
}
