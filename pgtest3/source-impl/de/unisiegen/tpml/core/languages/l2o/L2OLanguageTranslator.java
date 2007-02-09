package de.unisiegen.tpml.core.languages.l2o ;


import de.unisiegen.tpml.core.expressions.Attr ;
import de.unisiegen.tpml.core.expressions.CurriedMeth ;
import de.unisiegen.tpml.core.expressions.Duplication ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Message ;
import de.unisiegen.tpml.core.expressions.Meth ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.languages.l2.L2LanguageTranslator ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public class L2OLanguageTranslator extends L2LanguageTranslator
{
  /**
   * TODO
   */
  public L2OLanguageTranslator ( )
  {
    super ( ) ;
  }


  /**
   * {@inheritDoc}
   */
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
        e = new Lambda ( curriedMeth.getIdentifiers ( i ) , curriedMeth
            .getTypes ( i ) , e ) ;
      }
      return new Meth ( curriedMeth.getIdentifiers ( 0 ) , curriedMeth
          .getTypes ( 0 ) , e ) ;
    }
    else if ( pExpression instanceof ObjectExpr )
    {
      ObjectExpr objectExpr = ( ObjectExpr ) pExpression ;
      if ( pRecursive )
      {
        return new ObjectExpr ( objectExpr.getId ( ) , objectExpr.getTau ( ) ,
            translateToCoreSyntax ( objectExpr.getE ( ) , pRecursive ) ) ;
      }
      return pExpression ;
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
        return new Attr ( attr.getId ( ) , attr.getTau ( ) , attrE ) ;
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
        return new Meth ( meth.getId ( ) , meth.getTau ( ) , methE ) ;
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
        return new Message ( messageE , message.getId ( ) ) ;
      }
      return pExpression ;
    }
    else if ( pExpression instanceof Duplication )
    {
      if ( pRecursive )
      {
        Duplication duplication = ( Duplication ) pExpression ;
        Expression [ ] duplicatedRowE = new Expression [ duplication
            .getExpressions ( ).length ] ;
        for ( int i = 0 ; i < duplication.getExpressions ( ).length ; i ++ )
        {
          duplicatedRowE [ i ] = translateToCoreSyntax ( duplication
              .getExpressions ( i ) , pRecursive ) ;
        }
        return new Duplication ( duplication.getE ( ) , duplication
            .getIdentifiers ( ) , duplicatedRowE ) ;
      }
      return pExpression ;
    }
    return super.translateToCoreSyntax ( pExpression , pRecursive ) ;
  }
}
