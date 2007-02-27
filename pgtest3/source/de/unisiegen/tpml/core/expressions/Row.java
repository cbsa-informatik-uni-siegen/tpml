package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.Arrays ;
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
      if ( ! ( ( expr instanceof Attribute ) || ( expr instanceof Method ) || ( expr instanceof CurriedMethod ) ) )
      {
        throw new IllegalArgumentException (
            "A child Expression is not an instance of Attribute, Method or CurriedMethod" ) ; //$NON-NLS-1$
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
      return Arrays.equals ( this.expressions , other.expressions ) ;
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
        free.addAll ( attribute.free ( ) ) ;
        bounded.add ( attribute.getId ( ) ) ;
      }
      else if ( ( expr instanceof Method ) || ( expr instanceof CurriedMethod ) )
      {
        TreeSet < String > freeMethod = new TreeSet < String > ( ) ;
        freeMethod.addAll ( expr.free ( ) ) ;
        freeMethod.removeAll ( bounded ) ;
        free.addAll ( freeMethod ) ;
      }
      else
      {
        /*
         * Programming error: The child of the Row is not an Attribute, Method
         * or CurriedMethod. This should not happen.
         */
        throw new IllegalStateException ( "Inconsistent Row class." ) ; //$NON-NLS-1$
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
    ArrayList < String > attributeIdList = new ArrayList < String > ( ) ;
    for ( Expression expr : this.expressions )
    {
      if ( expr instanceof Attribute )
      {
        Attribute attribute = ( Attribute ) expr ;
        /*
         * If an Attribute is not a value, this Row is not a value.
         */
        if ( ! attribute.isValue ( ) )
        {
          return false ;
        }
        /*
         * If there are Attributes with the same id, this Row is not a value.
         */
        if ( attributeIdList.contains ( attribute.getId ( ) ) )
        {
          return false ;
        }
        attributeIdList.add ( attribute.getId ( ) ) ;
      }
    }
    return true ;
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
   */
  @ Override
  // TODO check this
  public Row substitute ( String pID , Expression pExpression ,
      boolean pAttributeRename )
  {
    Expression [ ] tmp = this.expressions.clone ( ) ;
    boolean equalAttributeIdFound = false ;
    for ( int i = 0 ; i < tmp.length ; i ++ )
    {
      Expression currentChild = tmp [ i ] ;
      if ( currentChild instanceof Attribute )
      {
        Attribute attribute = ( Attribute ) currentChild ;
        /*
         * If the Attribute Identifier is equal to the Identifier, which should
         * be substituted, the Identifier has to be substituted in the Attribute
         * and the equalAttributeIdFound value has to be set to true, because
         * the Identifier can not be substituted in Methods or CurriedMethods in
         * the rest of the Row.
         */
        if ( attribute.getId ( ).equals ( pID ) )
        {
          tmp [ i ] = new Attribute ( attribute.getId ( ) ,
              attribute.getTau ( ) , attribute.getE ( ).substitute ( pID ,
                  pExpression , pAttributeRename ) ) ;
          equalAttributeIdFound = true ;
        }
        else
        {
          TreeSet < String > freeRestRow = new TreeSet < String > ( ) ;
          TreeSet < String > freeE = new TreeSet < String > ( ) ;
          freeE.addAll ( pExpression.free ( ) ) ;
          for ( int j = i + 1 ; j < tmp.length ; j ++ )
          {
            Set < String > freeRowE = tmp [ j ].free ( ) ;
            freeRestRow.addAll ( freeRowE ) ;
          }
          String newId = attribute.getId ( ) ;
          while ( ( freeE.contains ( newId ) )
              && ( freeRestRow.contains ( pID ) ) )
          {
            newId = newId + "'" ; //$NON-NLS-1$
          }
          tmp [ i ] = new Attribute ( newId , attribute.getTau ( ) , attribute
              .getE ( ).substitute ( pID , pExpression , pAttributeRename ) ) ;
          /*
           * Only substitute the old Identifier, if the new Identifier is not
           * equal to the old Identifier.
           */
          if ( ! attribute.getId ( ).equals ( newId ) )
          {
            for ( int j = i + 1 ; j < tmp.length ; j ++ )
            {
              tmp [ j ] = tmp [ j ].substitute ( attribute.getId ( ) ,
                  new Identifier ( newId ) , pAttributeRename ) ;
            }
          }
        }
      }
      else if ( currentChild instanceof Method )
      {
        /*
         * Only if no Attribute Identifier with the same name as the Identifier,
         * which should be substituted is found, the Identifier should be
         * substituted in the Method.
         */
        if ( ! equalAttributeIdFound )
        {
          Method method = ( Method ) currentChild ;
          tmp [ i ] = new Method ( method.getId ( ) , method.getTau ( ) ,
              method.getE ( )
                  .substitute ( pID , pExpression , pAttributeRename ) ) ;
        }
      }
      else if ( currentChild instanceof CurriedMethod )
      {
        /*
         * Only if no Attribute Identifier with the same name as the Identifier,
         * which should be substituted is found, the Identifier should be
         * substituted in the CurriedMethod.
         */
        if ( ! equalAttributeIdFound )
        {
          CurriedMethod curriedMethod = ( CurriedMethod ) currentChild ;
          tmp [ i ] = new CurriedMethod ( curriedMethod.getIdentifiers ( ) ,
              curriedMethod.getTypes ( ) , curriedMethod.getE ( ).substitute (
                  pID , pExpression , pAttributeRename ) ) ;
        }
      }
      else
      {
        /*
         * Programming error: The child of the Row is not an Attribute, Method
         * or CurriedMethod. This should not happen.
         */
        throw new IllegalStateException ( "Inconsistent Row class." ) ; //$NON-NLS-1$
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
    Expression [ ] newExpr = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      newExpr [ i ] = this.expressions [ i ].substitute ( pTypeSubstitution ) ;
    }
    return new Row ( newExpr ) ;
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
