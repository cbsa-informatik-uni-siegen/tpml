package de.unisiegen.tpml.core.types ;


import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.interfaces.DefaultIdentifiers ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.interfaces.SortedChildren ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
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
            System.err.println ( "RowType union not defined" ) ; //$NON-NLS-1$
            throw new RuntimeException ( "RowType union not defined" ) ; //$NON-NLS-1$
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
      throw new NullPointerException ( "Identifiers is null" ) ; //$NON-NLS-1$
    }
    if ( pTypes == null )
    {
      throw new NullPointerException ( "Types is null" ) ; //$NON-NLS-1$
    }
    if ( pIdentifiers.length != pTypes.length )
    {
      throw new IllegalArgumentException (
          "The arity of method names and types must match" ) ; //$NON-NLS-1$
    }
    for ( MonoType type : pTypes )
    {
      if ( type == null )
      {
        throw new NullPointerException ( "One type is null" ) ; //$NON-NLS-1$
      }
    }
    // Identifier
    this.identifiers = pIdentifiers ;
    this.indicesId = new int [ this.identifiers.length ] ;
    for ( int i = 0 ; i < this.identifiers.length ; i ++ )
    {
      if ( this.identifiers [ i ].getParent ( ) != null )
      {
        // this.identifiers [ i ] = this.identifiers [ i ].clone ( ) ;
      }
      this.identifiers [ i ].setParent ( this ) ;
      this.indicesId [ i ] = i + 1 ;
    }
    // Type
    this.types = pTypes ;
    this.indicesType = new int [ this.types.length ] ;
    for ( int i = 0 ; i < this.types.length ; i ++ )
    {
      if ( this.types [ i ].getParent ( ) != null )
      {
        // this.types [ i ] = this.types [ i ].clone ( ) ;
      }
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
    /*
     * Identifier [ ] id1 = new Identifier [ 5 ] ; id1 [ 0 ] = new Identifier (
     * "a" ) ; id1 [ 1 ] = new Identifier ( "b" ) ; id1 [ 2 ] = new Identifier (
     * "c" ) ; id1 [ 3 ] = new Identifier ( "x" ) ; id1 [ 4 ] = new Identifier (
     * "y" ) ; Identifier [ ] id2 = new Identifier [ 5 ] ; id2 [ 0 ] = new
     * Identifier ( "c" ) ; id2 [ 1 ] = new Identifier ( "d" ) ; id2 [ 2 ] = new
     * Identifier ( "e" ) ; id2 [ 3 ] = new Identifier ( "y" ) ; id2 [ 4 ] = new
     * Identifier ( "x" ) ; MonoType [ ] tau1 = new MonoType [ 5 ] ; tau1 [ 0 ] =
     * new IntegerType ( ) ; tau1 [ 1 ] = new UnitType ( ) ; tau1 [ 2 ] = new
     * IntegerType ( ) ; tau1 [ 3 ] = new IntegerType ( ) ; tau1 [ 4 ] = new
     * BooleanType ( ) ; MonoType [ ] tau2 = new MonoType [ 5 ] ; tau2 [ 0 ] =
     * new IntegerType ( ) ; tau2 [ 1 ] = new BooleanType ( ) ; tau2 [ 2 ] = new
     * IntegerType ( ) ; tau2 [ 3 ] = new BooleanType ( ) ; tau2 [ 4 ] = new
     * IntegerType ( ) ; RowType r1 = new RowType ( id1 , tau1 ) ; RowType r2 =
     * new RowType ( id2 , tau2 ) ; System.out.println ( RowType.union ( r1 , r2
     * ).toPrettyString ( ) ) ;
     */
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
    this.remainingRowType = pRemainingRowType ;
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
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Row-Type" ; //$NON-NLS-1$
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
  public PrettyPrintable [ ] getSortedChildren ( )
  {
    PrettyPrintable [ ] result = new PrettyPrintable [ this.identifiers.length
        + this.types.length ] ;
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
    return result ;
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
  public TreeSet < TypeVariable > getTypeVariablesFree ( )
  {
    if ( this.free == null )
    {
      this.free = new TreeSet < TypeVariable > ( ) ;
      for ( MonoType type : this.types )
      {
        this.free.addAll ( type.getTypeVariablesFree ( ) ) ;
      }
      if ( this.remainingRowType != null )
      {
        this.free.addAll ( this.remainingRowType.getTypeVariablesFree ( ) ) ;
      }
    }
    return this.free ;
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
   * {@inheritDoc}
   * 
   * @see Type#substitute(TypeSubstitution)
   */
  @ Override
  public RowType substitute ( TypeSubstitution pTypeSubstitution )
  {
    if ( pTypeSubstitution == null )
    {
      throw new NullPointerException ( "Substitution is null" ) ; //$NON-NLS-1$
    }
    MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int i = 0 ; i < newTypes.length ; i ++ )
    {
      newTypes [ i ] = this.types [ i ].substitute ( pTypeSubstitution ) ;
    }
    MonoType newRemainingRowType = ( this.remainingRowType == null ) ? null
        : this.remainingRowType.substitute ( pTypeSubstitution ) ;
    return new RowType ( this.identifiers , newTypes , newRemainingRowType ) ;
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
          this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
        }
        this.prettyStringBuilder.addBuilder ( this.identifiers [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
        this.prettyStringBuilder.addText ( ": " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.types [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_ROW_TAU ) ;
        this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addKeyword ( ";" ) ; //$NON-NLS-1$
        if ( i != this.types.length - 1 )
        {
          this.prettyStringBuilder.addBreak ( ) ;
        }
      }
      if ( this.remainingRowType != null )
      {
        this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.remainingRowType
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
      }
      if ( this.types.length == 0 )
      {
        this.prettyStringBuilder.addText ( "\u00D8" ) ; //$NON-NLS-1$
      }
    }
    return this.prettyStringBuilder ;
  }
}
