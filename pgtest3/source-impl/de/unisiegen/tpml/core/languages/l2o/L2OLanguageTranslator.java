package de.unisiegen.tpml.core.languages.l2o ;


import de.unisiegen.tpml.core.expressions.CurriedMeth ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Lambda ;
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
        e = translateToCoreSyntax ( e , true ) ;
      }
      for ( int i = curriedMeth.getIdentifiers ( ).length - 1 ; i >= 0 ; i -- )
      {
        e = new Lambda ( curriedMeth.getIdentifiers ( i ) , null , e ) ;
      }
      return new Meth ( curriedMeth.getName ( ) , e ) ;
    }
    else if ( pExpression instanceof ObjectExpr )
    {
      ObjectExpr objectExpr = ( ObjectExpr ) pExpression ;
      return new ObjectExpr ( ( Row ) translateToCoreSyntax ( objectExpr
          .getE ( ) , pRecursive ) ) ;
    }
    else if ( pExpression instanceof Row )
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
    else
    {
      return super.translateToCoreSyntax ( pExpression , pRecursive ) ;
    }
  }
}
