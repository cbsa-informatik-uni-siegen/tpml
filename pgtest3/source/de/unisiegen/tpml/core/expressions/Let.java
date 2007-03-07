package de.unisiegen.tpml.core.expressions ;


import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.util.Free ;


/**
 * Represents the simple binding mechanism <code>let</code>. The string
 * representation is <code>let id = e1 in e2</code>.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1092 $
 * @see Expression
 * @see Lambda
 */
public class Let extends Expression
{
  /**
   * The identifier of the <code>Let</code> expression.
   * 
   * @see #getId()
   */
  protected String id ;


  /**
   * The type for the <code>id</code> (and thereby for <code>e1</code>) or
   * <code>null</code>.
   * 
   * @see #getTau()
   */
  protected MonoType tau ;


  /**
   * The first expression.
   * 
   * @see #getE1()
   */
  protected Expression e1 ;


  /**
   * The second expression.
   * 
   * @see #getE2()
   */
  protected Expression e2 ;


  /**
   * Allocates a new <code>Let</code> with the specified <code>id</code>,
   * <code>e1</code> and <code>e2</code>.
   * 
   * @param pId the name of the identifier.
   * @param pTau the type for the <code>id</code> (and thereby for
   *          <code>e1</code>) or <code>null</code>.
   * @param pExpression1 the first expression.
   * @param pExpression2 the second expression.
   * @throws NullPointerException if <code>id</code>, <code>e1</code> or
   *           <code>e2</code> is <code>null</code>.
   */
  public Let ( String pId , MonoType pTau , Expression pExpression1 ,
      Expression pExpression2 )
  {
    if ( pId == null )
    {
      throw new NullPointerException ( "id is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression1 == null )
    {
      throw new NullPointerException ( "e1 is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression2 == null )
    {
      throw new NullPointerException ( "e2 is null" ) ; //$NON-NLS-1$
    }
    this.id = pId ;
    this.tau = pTau ;
    this.e1 = pExpression1 ;
    this.e2 = pExpression2 ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Let" ; //$NON-NLS-1$
  }


  /**
   * Returns the identifier of the <code>Let</code> expression.
   * 
   * @return the identifier of the <code>Let</code> expression.
   */
  public String getId ( )
  {
    return this.id ;
  }


  /**
   * Returns the type for the identifier (and thereby the type for
   * <code>e1</code>) or <code>null</code> if no type was specified by the
   * user or the translation to core syntax.
   * 
   * @return the type for <code>id</code> or <code>null</code>.
   */
  public MonoType getTau ( )
  {
    return this.tau ;
  }


  /**
   * Returns the first expression.
   * 
   * @return the first expression.
   */
  public Expression getE1 ( )
  {
    return this.e1 ;
  }


  /**
   * Returns the second expression.
   * 
   * @return the second expression.
   */
  public Expression getE2 ( )
  {
    return this.e2 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Let clone ( )
  {
    return new Let ( this.id , this.tau , this.e1.clone ( ) , this.e2.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#free()
   */
  @ Override
  public Set < String > free ( )
  {
    TreeSet < String > free = new TreeSet < String > ( ) ;
    free.addAll ( this.e2.free ( ) ) ;
    free.remove ( this.id ) ;
    free.addAll ( this.e1.free ( ) ) ;
    return free ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Let substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType newTau = ( this.tau != null ) ? this.tau
        .substitute ( pTypeSubstitution ) : null ;
    return new Let ( this.id , newTau ,
        this.e1.substitute ( pTypeSubstitution ) , this.e2
            .substitute ( pTypeSubstitution ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public Expression substitute ( String pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public Expression substitute ( String pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    Expression newE2 = this.e2 ;
    String newId = this.id ;
    if ( ! this.id.equals ( pId ) )
    {
      TreeSet < String > free = new TreeSet < String > ( ) ;
      free.addAll ( this.e2.free ( ) ) ;
      free.remove ( this.id ) ;
      free.addAll ( pExpression.free ( ) ) ;
      free.add ( pId ) ;
      newId = Free.newIdentifier ( this.id , free ) ;
      if ( ! this.id.equals ( newId ) )
      {
        newE2 = newE2.substitute ( this.id , new Identifier ( newId ) ,
            pAttributeRename ) ;
      }
      newE2 = newE2.substitute ( pId , pExpression , pAttributeRename ) ;
    }
    return new Let ( newId , this.tau , this.e1.substitute ( pId , pExpression ,
        pAttributeRename ) , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  public @ Override
  PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_LET ) ;
    builder.addKeyword ( "let" ) ; //$NON-NLS-1$
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addIdentifier ( this.id ) ;
    if ( this.tau != null )
    {
      builder.addText ( ": " ) ; //$NON-NLS-1$
      builder
          .addBuilder ( this.tau
              .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
              PRIO_LET_TAU ) ;
    }
    builder.addText ( " = " ) ; //$NON-NLS-1$
    builder.addBuilder ( this.e1
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E1 ) ;
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addBreak ( ) ;
    builder.addKeyword ( "in" ) ; //$NON-NLS-1$
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addBuilder ( this.e2
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E2 ) ;
    return builder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( ( pObject instanceof Let )
        && ( this.getClass ( ).equals ( pObject.getClass ( ) ) ) )
    {
      Let other = ( Let ) pObject ;
      return ( ( this.id.equals ( other.id ) )
          && ( this.e1.equals ( other.e1 ) ) && ( this.e2.equals ( other.e2 ) ) && ( ( this.tau == null ) ? ( other.tau == null )
          : this.tau.equals ( other.tau ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.id.hashCode ( ) + this.e1.hashCode ( ) + this.e2.hashCode ( ) ;
  }
}
