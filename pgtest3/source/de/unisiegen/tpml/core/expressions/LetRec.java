package de.unisiegen.tpml.core.expressions ;


import java.util.TreeSet ;
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
public final class LetRec extends Let
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
  public LetRec ( String pId , MonoType pTau , Expression pExpression1 ,
      Expression pExpression2 )
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
    return new LetRec ( this.id , this.tau == null ? null : this.tau.clone ( ) ,
        this.e1.clone ( ) , this.e2.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Let#free()
   */
  @ Override
  public TreeSet < String > free ( )
  {
    if ( this.free == null )
    {
      this.free = new TreeSet < String > ( ) ;
      this.free.addAll ( this.e1.free ( ) ) ;
      this.free.addAll ( this.e2.free ( ) ) ;
      this.free.remove ( this.id ) ;
    }
    return this.free ;
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
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public LetRec substitute ( String pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Let#substitute(String, Expression, boolean)
   */
  @ Override
  public LetRec substitute ( String pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    /*
     * Do not substitute , if the Identifiers are equal.
     */
    if ( this.id.equals ( pId ) )
    {
      return this.clone ( ) ;
    }
    /*
     * Perform the bound renaming if required.
     */
    String newId = this.id ;
    BoundRenaming boundRenaming = new BoundRenaming ( ) ;
    boundRenaming.add ( this.free ( ) ) ;
    boundRenaming.add ( pExpression.free ( ) ) ;
    boundRenaming.add ( pId ) ;
    newId = boundRenaming.newIdentifier ( this.id ) ;
    /*
     * Substitute the old Identifier only with the new Identifier, if they are
     * different.
     */
    Expression newE1 = this.e1 ;
    Expression newE2 = this.e2 ;
    if ( ! this.id.equals ( newId ) )
    {
      newE1 = newE1.substitute ( this.id , new Identifier ( newId ) ,
          pAttributeRename ) ;
      newE2 = newE2.substitute ( this.id , new Identifier ( newId ) ,
          pAttributeRename ) ;
    }
    /*
     * Perform the substitution.
     */
    newE1 = newE1.substitute ( pId , pExpression , pAttributeRename ) ;
    newE2 = newE2.substitute ( pId , pExpression , pAttributeRename ) ;
    return new LetRec ( newId , this.tau == null ? null : this.tau.clone ( ) ,
        newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Let#substitute(TypeSubstitution)
   */
  @ Override
  public LetRec substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType pTau = ( this.tau == null ) ? null : this.tau
        .substitute ( pTypeSubstitution ) ;
    Expression newE1 = this.e1.substitute ( pTypeSubstitution ) ;
    Expression newE2 = this.e2.substitute ( pTypeSubstitution ) ;
    return new LetRec ( this.id , pTau , newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Let#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_LET ) ;
      this.prettyStringBuilder.addKeyword ( "let" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addKeyword ( "rec" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addIdentifier ( this.id ) ;
      if ( this.tau != null )
      {
        this.prettyStringBuilder.addText ( ": " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.tau
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_LET_TAU ) ;
      }
      this.prettyStringBuilder.addText ( " = " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.e1
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E1 ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( "in" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.e2
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E2 ) ;
    }
    return this.prettyStringBuilder ;
  }
}
