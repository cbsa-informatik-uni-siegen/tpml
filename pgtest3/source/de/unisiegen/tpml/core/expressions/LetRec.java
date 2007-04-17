package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.interfaces.BoundedIdentifiers ;
import de.unisiegen.tpml.core.interfaces.ChildrenExpressions ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.util.BoundRenaming ;


/**
 * Represents the <code>let rec</code> expression, which is syntactic sugar
 * for <b>(LET)</b> and <b>(REC)</b>. The expression
 * <code>let rec id = e1 in e2</code> is equal to
 * <code>let id = rec id.e1 in e2</code>
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1092 $
 * @see de.unisiegen.tpml.core.expressions.Let
 */
public final class LetRec extends Let implements BoundedIdentifiers ,
    DefaultTypes , ChildrenExpressions
{
  /**
   * Allocates a new <code>LetRec</code> with the given <code>id</code>,
   * <code>tau</code>, <code>e1</code> and <code>e2</code>.
   * 
   * @param pId the name of the identifier.
   * @param pTau the type for <code>id</code> or <code>null</code>.
   * @param pExpression1 the first expression.
   * @param pExpression2 the second expression.
   * @throws NullPointerException if <code>id</code>, <code>e1</code> or
   *           <code>e2</code> is <code>null</code>.
   */
  public LetRec ( final Identifier pId , final MonoType pTau ,
      final Expression pExpression1 , final Expression pExpression2 )
  {
    super ( pId , pTau , pExpression1 , pExpression2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Let#clone()
   */
  @ Override
  public LetRec clone ( )
  {
    return new LetRec ( this.identifiers [ 0 ].clone ( ) ,
        this.types [ 0 ] == null ? null : this.types [ 0 ].clone ( ) ,
        this.expressions [ 0 ].clone ( ) , this.expressions [ 1 ].clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Let#free()
   */
  @ Override
  public ArrayList < Identifier > free ( )
  {
    if ( this.free == null )
    {
      this.free = new ArrayList < Identifier > ( ) ;
      this.free.addAll ( this.expressions [ 0 ].free ( ) ) ;
      this.free.addAll ( this.expressions [ 1 ].free ( ) ) ;
      while ( this.free.remove ( this.identifiers [ 0 ] ) )
      {
        // Remove all Identifiers with the same name
      }
    }
    return this.free ;
  }


  /**
   * Returns a list of in this {@link Expression} bounded {@link Identifier}s.
   * 
   * @return A list of in this {@link Expression} bounded {@link Identifier}s.
   */
  @ Override
  public ArrayList < ArrayList < Identifier >> getBoundedIdentifiers ( )
  {
    if ( this.boundedIdentifiers == null )
    {
      this.boundedIdentifiers = new ArrayList < ArrayList < Identifier >> ( ) ;
      final ArrayList < Identifier > boundedIdList = new ArrayList < Identifier > ( ) ;
      final ArrayList < Identifier > boundedE1 = this.expressions [ 0 ].free ( ) ;
      for ( final Identifier freeId : boundedE1 )
      {
        if ( this.identifiers [ 0 ].equals ( freeId ) )
        {
          freeId.setBoundedToExpression ( this ) ;
          freeId.setBoundedToIdentifier ( this.identifiers [ 0 ] ) ;
          boundedIdList.add ( freeId ) ;
        }
      }
      final ArrayList < Identifier > boundedE2 = this.expressions [ 1 ].free ( ) ;
      for ( final Identifier freeId : boundedE2 )
      {
        if ( this.identifiers [ 0 ].equals ( freeId ) )
        {
          freeId.setBoundedToExpression ( this ) ;
          freeId.setBoundedToIdentifier ( this.identifiers [ 0 ] ) ;
          boundedIdList.add ( freeId ) ;
        }
      }
      this.boundedIdentifiers.add ( boundedIdList ) ;
    }
    return this.boundedIdentifiers ;
  }


  /**
   * Returns the <code>pIndex</code>th list of in this {@link Expression}
   * bounded {@link Identifier}s.
   * 
   * @param pIndex The index of the list of {@link Identifier}s to return.
   * @return A list of in this {@link Expression} bounded {@link Identifier}s.
   */
  @ Override
  public ArrayList < Identifier > getBoundedIdentifiers ( final int pIndex )
  {
    if ( this.boundedIdentifiers == null )
    {
      return this.getBoundedIdentifiers ( ).get ( pIndex ) ;
    }
    return this.boundedIdentifiers.get ( pIndex ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Let-Rec" ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public LetRec substitute ( final Identifier pId , final Expression pExpression )
  {
    return this.substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Let#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public LetRec substitute ( final Identifier pId ,
      final Expression pExpression , final boolean pAttributeRename )
  {
    /*
     * Do not substitute , if the Identifiers are equal.
     */
    if ( this.identifiers [ 0 ].equals ( pId ) )
    {
      return this.clone ( ) ;
    }
    /*
     * Perform the bound renaming if required.
     */
    final BoundRenaming boundRenaming = new BoundRenaming ( ) ;
    boundRenaming.add ( this.free ( ) ) ;
    boundRenaming.add ( pExpression.free ( ) ) ;
    boundRenaming.add ( pId ) ;
    final Identifier newId = boundRenaming.newId ( this.identifiers [ 0 ] ) ;
    /*
     * Substitute the old Identifier only with the new Identifier, if they are
     * different.
     */
    Expression newE1 = this.expressions [ 0 ] ;
    Expression newE2 = this.expressions [ 1 ] ;
    if ( ! this.identifiers [ 0 ].equals ( newId ) )
    {
      newE1 = newE1.substitute ( this.identifiers [ 0 ] , newId ,
          pAttributeRename ) ;
      newE2 = newE2.substitute ( this.identifiers [ 0 ] , newId ,
          pAttributeRename ) ;
    }
    /*
     * Perform the substitution.
     */
    newE1 = newE1.substitute ( pId , pExpression , pAttributeRename ) ;
    newE2 = newE2.substitute ( pId , pExpression , pAttributeRename ) ;
    return new LetRec ( newId , this.types [ 0 ] == null ? null
        : this.types [ 0 ].clone ( ) , newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Let#substitute(TypeSubstitution)
   */
  @ Override
  public LetRec substitute ( final TypeSubstitution pTypeSubstitution )
  {
    final MonoType pTau = ( this.types [ 0 ] == null ) ? null : this.types [ 0 ]
        .substitute ( pTypeSubstitution ) ;
    final Expression newE1 = this.expressions [ 0 ]
        .substitute ( pTypeSubstitution ) ;
    final Expression newE2 = this.expressions [ 1 ]
        .substitute ( pTypeSubstitution ) ;
    return new LetRec ( this.identifiers [ 0 ].clone ( ) , pTau , newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Let#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      final PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PrettyPrintPriorities.PRIO_LET ) ;
      this.prettyStringBuilder.addKeyword ( "let" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addKeyword ( "rec" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.identifiers [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PrettyPrintPriorities.PRIO_ID ) ;
      if ( this.types [ 0 ] != null )
      {
        this.prettyStringBuilder.addText ( ": " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PrettyPrintPriorities.PRIO_LET_TAU ) ;
      }
      this.prettyStringBuilder.addText ( " = " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PrettyPrintPriorities.PRIO_LET_E1 ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( "in" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.expressions [ 1 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PrettyPrintPriorities.PRIO_LET_E2 ) ;
    }
    return this.prettyStringBuilder ;
  }
}
