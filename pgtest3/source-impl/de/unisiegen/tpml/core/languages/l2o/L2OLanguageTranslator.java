package de.unisiegen.tpml.core.languages.l2o ;


import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.CurriedMethod ;
import de.unisiegen.tpml.core.expressions.Duplication ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Method ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.expressions.Send ;
import de.unisiegen.tpml.core.languages.l2.L2LanguageTranslator ;
import de.unisiegen.tpml.core.types.MonoType ;


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
    if ( pExpression instanceof CurriedMethod )
    {
      return translateToCoreSyntaxCurriedMethod (
          ( CurriedMethod ) pExpression , pRecursive ) ;
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
    else if ( pExpression instanceof Attribute )
    {
      return translateToCoreSyntaxAttribute ( ( Attribute ) pExpression ,
          pRecursive ) ;
    }
    else if ( pExpression instanceof Method )
    {
      return translateToCoreSyntaxMethod ( ( Method ) pExpression , pRecursive ) ;
    }
    else if ( pExpression instanceof Send )
    {
      return translateToCoreSyntaxSend ( ( Send ) pExpression , pRecursive ) ;
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
   * @param pAttribute TODO
   * @param pRecursive TODO
   * @return TODO
   */
  private Expression translateToCoreSyntaxAttribute ( Attribute pAttribute ,
      boolean pRecursive )
  {
    if ( pRecursive )
    {
      return new Attribute ( pAttribute.getId ( ) , pAttribute.getTau ( ) ,
          translateToCoreSyntax ( pAttribute.getE ( ) , true ) ) ;
    }
    return pAttribute ;
  }


  /**
   * TODO
   * 
   * @param pCurriedMethod TODO
   * @param pRecursive TODO
   * @return TODO
   */
  private Expression translateToCoreSyntaxCurriedMethod (
      CurriedMethod pCurriedMethod , boolean pRecursive )
  {
    Expression curriedMethE = pCurriedMethod.getE ( ) ;
    if ( pRecursive )
    {
      curriedMethE = translateToCoreSyntax ( curriedMethE , true ) ;
    }
    Identifier [ ] identifier = pCurriedMethod.getIdentifiers ( ) ;
    MonoType [ ] types = pCurriedMethod.getTypes ( ) ;
    for ( int i = identifier.length - 1 ; i > 0 ; i -- )
    {
      curriedMethE = new Lambda ( identifier [ i ] , types [ i ] , curriedMethE ) ;
    }
    return new Method ( identifier [ 0 ] , types [ 0 ] , curriedMethE ) ;
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
      Expression [ ] duplicationExpressions = pDuplication.getExpressions ( ) ;
      Expression [ ] newDuplicationExpressions = new Expression [ duplicationExpressions.length ] ;
      for ( int i = 0 ; i < duplicationExpressions.length ; i ++ )
      {
        newDuplicationExpressions [ i ] = translateToCoreSyntax (
            duplicationExpressions [ i ] , true ) ;
      }
      return new Duplication ( pDuplication.getIdentifiers ( ) ,
          newDuplicationExpressions ) ;
    }
    return pDuplication ;
  }


  /**
   * TODO
   * 
   * @param pSend TODO
   * @param pRecursive TODO
   * @return TODO
   */
  private Expression translateToCoreSyntaxSend ( Send pSend , boolean pRecursive )
  {
    if ( pRecursive )
    {
      return new Send ( translateToCoreSyntax ( pSend.getE ( ) , true ) , pSend
          .getId ( ) ) ;
    }
    return pSend ;
  }


  /**
   * TODO
   * 
   * @param pMethod TODO
   * @param pRecursive TODO
   * @return TODO
   */
  private Expression translateToCoreSyntaxMethod ( Method pMethod ,
      boolean pRecursive )
  {
    if ( pRecursive )
    {
      return new Method ( pMethod.getId ( ) , pMethod.getTau ( ) ,
          translateToCoreSyntax ( pMethod.getE ( ) , true ) ) ;
    }
    return pMethod ;
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
          translateToCoreSyntax ( pObjectExpr.getE ( ) , true ) ) ;
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
      Expression [ ] rowExpressions = pRow.getExpressions ( ) ;
      Expression [ ] newRowExpressions = new Expression [ rowExpressions.length ] ;
      for ( int i = 0 ; i < newRowExpressions.length ; i ++ )
      {
        newRowExpressions [ i ] = translateToCoreSyntax ( rowExpressions [ i ] ,
            true ) ;
      }
      return new Row ( newRowExpressions ) ;
    }
    return pRow ;
  }
}
