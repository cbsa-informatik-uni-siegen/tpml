package de.unisiegen.tpml.core.languages.l2o ;


import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.CurriedMethod ;
import de.unisiegen.tpml.core.expressions.Duplication ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Message ;
import de.unisiegen.tpml.core.expressions.Method ;
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
          translateToCoreSyntax ( pAttribute.getE ( ) , pRecursive ) ) ;
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
      curriedMethE = translateToCoreSyntax ( curriedMethE , pRecursive ) ;
    }
    for ( int i = pCurriedMethod.getIdentifiers ( ).length - 1 ; i > 0 ; i -- )
    {
      curriedMethE = new Lambda ( pCurriedMethod.getIdentifiers ( i ) ,
          pCurriedMethod.getTypes ( i ) , curriedMethE ) ;
    }
    return new Method ( pCurriedMethod.getIdentifiers ( 0 ) , pCurriedMethod
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
      return new Duplication ( translateToCoreSyntax ( pDuplication.getE ( ) ,
          pRecursive ) , pDuplication.getIdentifiers ( ) , newDuplicationE ) ;
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
          translateToCoreSyntax ( pMethod.getE ( ) , pRecursive ) ) ;
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
