package de.unisiegen.tpml.core.expressions ;


import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class ObjectExpr extends Expression
{
  /**
   * TODO
   * 
   * @see #getE()
   */
  private Expression expression ;


  /**
   * TODO
   * 
   * @see #getId()
   */
  private String identifier ;


  /**
   * TODO
   * 
   * @see #getTau()
   */
  private MonoType tau ;


  /**
   * TODO
   * 
   * @param pIdentifier TODO
   * @param pTau TODO
   * @param pExpression TODO
   */
  public ObjectExpr ( String pIdentifier , MonoType pTau ,
      Expression pExpression )
  {
    if ( pIdentifier == null )
    {
      throw new NullPointerException ( "Identifier is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression == null )
    {
      throw new NullPointerException ( "Expression is null" ) ; //$NON-NLS-1$
    }
    if ( ! ( pExpression instanceof Row ) )
    {
      throw new IllegalArgumentException ( "The expression must be a Row" ) ; //$NON-NLS-1$
    }
    this.identifier = pIdentifier ;
    this.tau = pTau ;
    this.expression = pExpression ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public ObjectExpr clone ( )
  {
    return new ObjectExpr ( this.identifier , this.tau , this.expression
        .clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof ObjectExpr )
    {
      ObjectExpr other = ( ObjectExpr ) pObject ;
      return ( this.expression.equals ( other.expression ) )
          && ( ( this.tau == null ) ? ( other.tau == null ) : ( this.tau
              .equals ( other.tau ) )
              && ( ( this.identifier == null ) ? ( other.identifier == null )
                  : ( this.identifier.equals ( other.identifier ) ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Set < String > free ( )
  {
    TreeSet < String > free = new TreeSet < String > ( ) ;
    free.addAll ( this.expression.free ( ) ) ;
    free.remove ( this.identifier ) ;
    return free ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Object" ; //$NON-NLS-1$
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #expression
   */
  public Expression getE ( )
  {
    return this.expression ;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #identifier
   */
  public String getId ( )
  {
    return this.identifier ;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #tau
   */
  public MonoType getTau ( )
  {
    return this.tau ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public int hashCode ( )
  {
    return this.identifier == null ? this.expression.hashCode ( )
        : this.identifier.hashCode ( ) + this.expression.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean isValue ( )
  {
    return this.expression.isValue ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public ObjectExpr substitute ( String pID , Expression pExpression )
  {
    if ( "self".equals ( pID ) ) //$NON-NLS-1$
    {
      return this ;
    }
    return new ObjectExpr ( this.identifier , this.tau , this.expression
        .substitute ( pID , pExpression ) ) ;
  }


  /**
   * TODO
   * 
   * @param pTypeSubstitution TODO
   * @return TODO
   */
  @ Override
  public ObjectExpr substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType tmp = ( this.tau != null ) ? this.tau
        .substitute ( pTypeSubstitution ) : null ;
    return new ObjectExpr ( this.identifier , tmp , this.expression
        .substitute ( pTypeSubstitution ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_OBJECTEXPR ) ;
    builder.addKeyword ( "object" ) ; //$NON-NLS-1$
    builder.addText ( " " ) ; //$NON-NLS-1$
    if ( this.identifier != null )
    {
      if ( this.identifier.equals ( "self" ) ) //$NON-NLS-1$
      {
        if ( this.tau != null )
        {
          builder.addText ( "( " ) ; //$NON-NLS-1$
          builder.addIdentifier ( this.identifier ) ;
          builder.addText ( ": " ) ; //$NON-NLS-1$
          builder.addBuilder ( this.tau
              .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
              PRIO_OBJECTEXPR_TAU ) ;
          builder.addText ( " ) " ) ; //$NON-NLS-1$
        }
      }
      else
      {
        builder.addText ( "( " ) ; //$NON-NLS-1$
        builder.addIdentifier ( this.identifier ) ;
        if ( this.tau != null )
        {
          builder.addText ( ": " ) ; //$NON-NLS-1$
          builder.addBuilder ( this.tau
              .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
              PRIO_OBJECTEXPR_TAU ) ;
        }
        builder.addText ( " ) " ) ; //$NON-NLS-1$
      }
    }
    builder.addBreak ( ) ;
    builder.addBuilder ( this.expression
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
        PRIO_OBJECTEXPR_E ) ;
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addBreak ( ) ;
    builder.addKeyword ( "end" ) ; //$NON-NLS-1$
    return builder ;
  }
}
