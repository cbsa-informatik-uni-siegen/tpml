package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class Row extends Expression
{
  /**
   * TODO
   * 
   * @see #getExpressions()
   * @see #getExpressions(int)
   */
  private Expression [ ] expressions ;


  /**
   * TODO
   * 
   * @param pExpressions TODO
   */
  public Row ( Expression [ ] pExpressions )
  {
    if ( pExpressions == null )
    {
      throw new NullPointerException ( "Expressions is null" ) ; //$NON-NLS-1$
    }
    this.expressions = pExpressions ;
    for ( Expression expr : this.expressions )
    {
      if ( ( ! ( expr instanceof Attribute ) )
          && ( ! ( expr instanceof Method ) )
          && ( ! ( expr instanceof CurriedMethod ) ) )
      {
        throw new IllegalArgumentException (
            "A Expression is not an instance of Attribute, Method or CurriedMethod" ) ; //$NON-NLS-1$
      }
    }
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Row clone ( )
  {
    return new Row ( this.expressions.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Row )
    {
      Row other = ( Row ) pObject ;
      return this.expressions.equals ( other.expressions ) ;
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
    TreeSet < String > bounded = new TreeSet < String > ( ) ;
    for ( Expression expr : this.expressions )
    {
      if ( expr instanceof Attribute )
      {
        Attribute attribute = ( Attribute ) expr ;
        TreeSet < String > freeExpr = new TreeSet < String > ( ) ;
        freeExpr.addAll ( attribute.free ( ) ) ;
        free.addAll ( freeExpr ) ;
        bounded.add ( attribute.getId ( ) ) ;
      }
      else
      {
        TreeSet < String > freeExpr = new TreeSet < String > ( ) ;
        freeExpr.addAll ( expr.free ( ) ) ;
        freeExpr.removeAll ( bounded ) ;
        free.addAll ( freeExpr ) ;
      }
    }
    return free ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public Set < String > freeVal ( )
  {
    TreeSet < String > freeVal = new TreeSet < String > ( ) ;
    for ( Expression expr : this.expressions )
    {
      if ( expr instanceof Attribute )
      {
        freeVal.addAll ( expr.free ( ) ) ;
      }
    }
    return freeVal ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Row" ; //$NON-NLS-1$
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #expressions
   * @see #getExpressions(int)
   */
  public Expression [ ] getExpressions ( )
  {
    return this.expressions ;
  }


  /**
   * TODO
   * 
   * @param pIndex TODO
   * @return TODO
   * @see #expressions
   * @see #getExpressions()
   */
  public Expression getExpressions ( int pIndex )
  {
    return this.expressions [ pIndex ] ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public int hashCode ( )
  {
    return this.expressions.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean isValue ( )
  {
    ArrayList < String > attrNames = new ArrayList < String > ( ) ;
    for ( Expression e : this.expressions )
    {
      if ( e instanceof Attribute )
      {
        Attribute attribute = ( Attribute ) e ;
        if ( ! attribute.isValue ( ) )
        {
          return false ;
        }
        if ( attrNames.contains ( attribute.getId ( ) ) )
        {
          return false ;
        }
        attrNames.add ( attribute.getId ( ) ) ;
      }
    }
    return true ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Row substitute ( String pID , Expression pExpression )
  {
    Expression [ ] tmp = this.expressions.clone ( ) ;
    boolean equalIdFound = false ;
    for ( int i = 0 ; i < tmp.length ; i ++ )
    {
      Expression e = tmp [ i ] ;
      if ( e instanceof Attribute )
      {
        Attribute attribute = ( Attribute ) e ;
        // first case id == id'
        if ( attribute.getId ( ).equals ( pID ) )
        {
          tmp [ i ] = new Attribute ( attribute.getId ( ) ,
              attribute.getTau ( ) , attribute.getE ( ).substitute ( pID ,
                  pExpression ) ) ;
          equalIdFound = true ;
        }
        else
        {
          // second case id != id'
          TreeSet < String > freeRow = new TreeSet < String > ( ) ;
          TreeSet < String > freeE = new TreeSet < String > ( ) ;
          freeE.addAll ( pExpression.free ( ) ) ;
          for ( int j = i + 1 ; j < tmp.length ; j ++ )
          {
            Set < String > freeRowE = tmp [ j ].free ( ) ;
            freeRow.addAll ( freeRowE ) ;
          }
          String newId = attribute.getId ( ) ;
          while ( ( freeE.contains ( newId ) ) && ( freeRow.contains ( pID ) ) )
          {
            newId = newId + "'" ; //$NON-NLS-1$
          }
          tmp [ i ] = new Attribute ( newId , attribute.getTau ( ) , attribute
              .getE ( ).substitute ( pID , pExpression ) ) ;
          if ( ! attribute.getId ( ).equals ( newId ) )
          {
            for ( int j = i + 1 ; j < tmp.length ; j ++ )
            {
              tmp [ j ] = tmp [ j ].substitute ( attribute.getId ( ) ,
                  new Identifier ( newId ) ) ;
            }
          }
        }
      }
      else if ( e instanceof Method )
      {
        if ( ! equalIdFound )
        {
          Method method = ( Method ) e ;
          tmp [ i ] = new Method ( method.getId ( ) , method.getTau ( ) ,
              method.getE ( ).substitute ( pID , pExpression ) ) ;
        }
      }
      else if ( e instanceof CurriedMethod )
      {
        if ( ! equalIdFound )
        {
          CurriedMethod curriedMethod = ( CurriedMethod ) e ;
          tmp [ i ] = new CurriedMethod ( curriedMethod.getIdentifiers ( ) ,
              curriedMethod.getTypes ( ) , curriedMethod.getE ( ).substitute (
                  pID , pExpression ) ) ;
        }
      }
    }
    return new Row ( tmp ) ;
  }


  /**
   * TODO
   * 
   * @param pTypeSubstitution TODO
   * @return TODO
   */
  @ Override
  public Row substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression [ ] tmp = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      tmp [ i ] = this.expressions [ i ].substitute ( pTypeSubstitution ) ;
    }
    return ( this.expressions.equals ( tmp ) ) ? this : new Row ( tmp ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    /*
     * System.out.println ( "Free Row:" ) ; for ( String s : free ( ) ) {
     * System.out.print ( s + " " ) ; } System.out.println ( ) ;
     */
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_ROW ) ;
    if ( this.expressions.length == 0 )
    {
      builder.addText ( "\u03B5" ) ; //$NON-NLS-1$
    }
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      builder.addBuilder ( this.expressions [ i ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ROW_E ) ;
      if ( i != this.expressions.length - 1 )
      {
        builder.addText ( " " ) ; //$NON-NLS-1$
        builder.addBreak ( ) ;
      }
    }
    return builder ;
  }
}
