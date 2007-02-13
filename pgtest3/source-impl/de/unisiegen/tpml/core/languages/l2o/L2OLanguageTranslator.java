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
      return translateToCoreSyntaxCurriedMeth ( ( CurriedMeth ) pExpression ,
          pRecursive ) ;
    }
    else if ( pExpression instanceof ObjectExpr )
    {
      return translateToCoreSyntaxObjectExpr ( ( ObjectExpr ) pExpression ,
          pRecursive ) ;
    }
    else if ( pExpression instanceof Row )
    {
      return translateToCoreSyntaxRow ( ( Row ) pExpression , pRecursive ) ;
    }
    else if ( pExpression instanceof Attr )
    {
      return translateToCoreSyntaxAttr ( ( Attr ) pExpression , pRecursive ) ;
    }
    else if ( pExpression instanceof Meth )
    {
      return translateToCoreSyntaxMeth ( ( Meth ) pExpression , pRecursive ) ;
    }
    else if ( pExpression instanceof Message )
    {
      return translateToCoreSyntaxMessage ( ( Message ) pExpression ,
          pRecursive ) ;
    }
    else if ( pExpression instanceof Duplication )
    {
      return translateToCoreSyntaxDuplication ( ( Duplication ) pExpression ,
          pRecursive ) ;
    }
    return super.translateToCoreSyntax ( pExpression , pRecursive ) ;
  }


  /**
   * TODO
   * 
   * @param pAttr TODO
   * @param pRecursive TODO
   * @return TODO
   */
  private Expression translateToCoreSyntaxAttr ( Attr pAttr , boolean pRecursive )
  {
    if ( pRecursive )
    {
      return new Attr ( pAttr.getId ( ) , pAttr.getTau ( ) ,
          translateToCoreSyntax ( pAttr.getE ( ) , pRecursive ) ) ;
    }
    return pAttr ;
  }


  /**
   * TODO
   * 
   * @param pCurriedMeth TODO
   * @param pRecursive TODO
   * @return TODO
   */
  private Expression translateToCoreSyntaxCurriedMeth (
      CurriedMeth pCurriedMeth , boolean pRecursive )
  {
    Expression curriedMethE = pCurriedMeth.getE ( ) ;
    if ( pRecursive )
    {
      curriedMethE = translateToCoreSyntax ( curriedMethE , pRecursive ) ;
    }
    for ( int i = pCurriedMeth.getIdentifiers ( ).length - 1 ; i > 0 ; i -- )
    {
      curriedMethE = new Lambda ( pCurriedMeth.getIdentifiers ( i ) ,
          pCurriedMeth.getTypes ( i ) , curriedMethE ) ;
    }
    return new Meth ( pCurriedMeth.getIdentifiers ( 0 ) , pCurriedMeth
        .getTypes ( 0 ) , curriedMethE ) ;
  }


  /**
   * TODO
   * 
   * @param pDuplication TODO
   * @param pRecursive TODO
   * @return TODO
   */
  private Expression translateToCoreSyntaxDuplication (
      Duplication pDuplication , boolean pRecursive )
  {
    if ( pRecursive )
    {
      Expression [ ] newDuplicationE = new Expression [ pDuplication
          .getExpressions ( ).length ] ;
      for ( int i = 0 ; i < pDuplication.getExpressions ( ).length ; i ++ )
      {
        newDuplicationE [ i ] = translateToCoreSyntax ( pDuplication
            .getExpressions ( i ) , pRecursive ) ;
      }
      return new Duplication ( pDuplication.getE ( ) , pDuplication
          .getIdentifiers ( ) , newDuplicationE ) ;
    }
    return pDuplication ;
  }


  /**
   * TODO
   * 
   * @param pMessage TODO
   * @param pRecursive TODO
   * @return TODO
   */
  private Expression translateToCoreSyntaxMessage ( Message pMessage ,
      boolean pRecursive )
  {
    if ( pRecursive )
    {
      return new Message ( translateToCoreSyntax ( pMessage.getE ( ) ,
          pRecursive ) , pMessage.getId ( ) ) ;
    }
    return pMessage ;
  }


  /**
   * TODO
   * 
   * @param pMeth TODO
   * @param pRecursive TODO
   * @return TODO
   */
  private Expression translateToCoreSyntaxMeth ( Meth pMeth , boolean pRecursive )
  {
    if ( pRecursive )
    {
      return new Meth ( pMeth.getId ( ) , pMeth.getTau ( ) ,
          translateToCoreSyntax ( pMeth.getE ( ) , pRecursive ) ) ;
    }
    return pMeth ;
  }


  /**
   * TODO
   * 
   * @param pObjectExpr TODO
   * @param pRecursive TODO
   * @return TODO
   */
  private Expression translateToCoreSyntaxObjectExpr ( ObjectExpr pObjectExpr ,
      boolean pRecursive )
  {
    if ( pRecursive )
    {
      return new ObjectExpr ( pObjectExpr.getId ( ) , pObjectExpr.getTau ( ) ,
          ( Row ) translateToCoreSyntax ( pObjectExpr.getE ( ) , pRecursive ) ) ;
    }
    return pObjectExpr ;
  }


  /**
   * TODO
   * 
   * @param pRow TODO
   * @param pRecursive TODO
   * @return TODO
   */
  private Expression translateToCoreSyntaxRow ( Row pRow , boolean pRecursive )
  {
    if ( pRecursive )
    {
      Expression [ ] newRowE = new Expression [ pRow.getExpressions ( ).length ] ;
      for ( int i = 0 ; i < newRowE.length ; i ++ )
      {
        newRowE [ i ] = translateToCoreSyntax ( pRow.getExpressions ( i ) ,
            pRecursive ) ;
      }
      return new Row ( newRowE ) ;
    }
    return pRow ;
  }
}
