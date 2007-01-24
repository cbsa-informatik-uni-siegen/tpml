package de.unisiegen.tpml.core.languages.l2o ;


import de.unisiegen.tpml.core.expressions.Attr ;
import de.unisiegen.tpml.core.expressions.CurriedMeth ;
import de.unisiegen.tpml.core.expressions.DuplicatedRow ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Message ;
import de.unisiegen.tpml.core.expressions.Meth ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.languages.l2.L2LanguageTranslator ;


public class L2OLanguageTranslator extends L2LanguageTranslator
{
  public L2OLanguageTranslator ( )
  {
    super ( ) ;
  }


  @ Override
  public Expression translateToCoreSyntax ( Expression pExpression ,
      boolean pRecursive )
  {
    if ( pExpression instanceof CurriedMeth )
    {
      CurriedMeth curriedMeth = ( CurriedMeth ) pExpression ;
      Expression e = curriedMeth.getE ( ) ;
      if ( pRecursive )
      {
        e = translateToCoreSyntax ( e , pRecursive ) ;
      }
      for ( int i = curriedMeth.getIdentifiers ( ).length - 1 ; i > 0 ; i -- )
      {
        e = new Lambda ( curriedMeth.getIdentifiers ( i ) , null , e ) ;
      }
      return new Meth ( curriedMeth.getIdentifiers ( 0 ) , e ) ;
    }
    else if ( pExpression instanceof ObjectExpr )
    {
      // TODO Translate to core syntax
      ObjectExpr objectExpr = ( ObjectExpr ) pExpression ;
      Row row = ( Row ) objectExpr.getE ( ) ;
      if ( pRecursive )
      {
        row = ( Row ) translateToCoreSyntax ( row , pRecursive ) ;
        if ( objectExpr.getIdentifier ( ) == null )
        {
          return new ObjectExpr ( row ) ;
        }
        return new ObjectExpr ( objectExpr.getIdentifier ( ) , row ) ;
      }
      return pExpression.clone ( ) ;
    }
    else if ( pExpression instanceof Row )
    {
      if ( pRecursive )
      {
        Row row = ( Row ) pExpression ;
        Expression [ ] e = row.getExpressions ( ) ;
        Expression [ ] newE = new Expression [ e.length ] ;
        for ( int i = 0 ; i < newE.length ; i ++ )
        {
          newE [ i ] = translateToCoreSyntax ( e [ i ] , pRecursive ) ;
        }
        return new Row ( newE ) ;
      }
      return pExpression ;
    }
    else if ( pExpression instanceof Attr )
    {
      if ( pRecursive )
      {
        Attr attr = ( Attr ) pExpression ;
        Expression attrE = attr.getE ( ) ;
        attrE = translateToCoreSyntax ( attrE , pRecursive ) ;
        return new Attr ( attr.getIdentifier ( ) , attrE ) ;
      }
      return pExpression ;
    }
    else if ( pExpression instanceof Meth )
    {
      if ( pRecursive )
      {
        Meth meth = ( Meth ) pExpression ;
        Expression methE = meth.getE ( ) ;
        methE = translateToCoreSyntax ( methE , pRecursive ) ;
        return new Meth ( meth.getIdentifier ( ) , methE ) ;
      }
      return pExpression ;
    }
    else if ( pExpression instanceof Message )
    {
      if ( pRecursive )
      {
        Message message = ( Message ) pExpression ;
        Expression messageE = message.getE ( ) ;
        messageE = translateToCoreSyntax ( messageE , pRecursive ) ;
        return new Message ( messageE , message.getIdentifier ( ) ) ;
      }
      return pExpression ;
    }
    else if ( pExpression instanceof DuplicatedRow )
    {
      if ( pRecursive )
      {
        DuplicatedRow duplicatedRow = ( DuplicatedRow ) pExpression ;
        Expression [ ] duplicatedRowE = new Expression [ duplicatedRow
            .getExpressions ( ).length ] ;
        for ( int i = 0 ; i < duplicatedRow.getExpressions ( ).length ; i ++ )
        {
          duplicatedRowE [ i ] = translateToCoreSyntax ( duplicatedRow
              .getExpressions ( i ) , pRecursive ) ;
        }
        return new DuplicatedRow ( duplicatedRow.getIdentifiers ( ) ,
            duplicatedRowE ) ;
      }
      return pExpression ;
    }
    return super.translateToCoreSyntax ( pExpression , pRecursive ) ;
  }
}
