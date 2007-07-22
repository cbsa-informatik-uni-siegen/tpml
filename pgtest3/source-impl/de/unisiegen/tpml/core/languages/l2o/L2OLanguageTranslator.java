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
import de.unisiegen.tpml.core.languages.l2.L2Language ;
import de.unisiegen.tpml.core.languages.l2.L2LanguageTranslator ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * Language translator for the <code>L2O</code> language.
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 * @see L2Language
 */
public class L2OLanguageTranslator extends L2LanguageTranslator
{
  /**
   * Allocates a new <code>L2OLanguageTranslator</code>.
   */
  public L2OLanguageTranslator ( )
  {
    super ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see L2LanguageTranslator#translateToCoreSyntax(Expression, boolean)
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
   * Translates the {@link Attribute} to core syntax.
   * 
   * @param pAttribute The given {@link Attribute}.
   * @param pRecursive Translate recursive all children of the {@link Attribute}.
   * @return The translated {@link Expression}.
   */
  private Expression translateToCoreSyntaxAttribute ( Attribute pAttribute ,
      boolean pRecursive )
  {
    if ( pRecursive )
    {
      return new Attribute ( pAttribute.getId ( ) , translateToCoreSyntax (
          pAttribute.getE ( ) , true ) ) ;
    }
    return pAttribute ;
  }


  /**
   * Translates the {@link CurriedMethod} to core syntax.
   * 
   * @param pCurriedMethod The given {@link CurriedMethod}.
   * @param pRecursive Translate recursive all children of the
   *          {@link CurriedMethod}.
   * @return The translated {@link Expression}.
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
   * Translates the {@link Duplication} to core syntax.
   * 
   * @param pDuplication The given {@link Duplication}.
   * @param pRecursive Translate recursive all children of the
   *          {@link Duplication}.
   * @return The translated {@link Expression}.
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
   * Translates the {@link Send} to core syntax.
   * 
   * @param pSend The given {@link Send}.
   * @param pRecursive Translate recursive all children of the {@link Send}.
   * @return The translated {@link Expression}.
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
   * Translates the {@link Method} to core syntax.
   * 
   * @param pMethod The given {@link Method}.
   * @param pRecursive Translate recursive all children of the {@link Method}.
   * @return The translated {@link Expression}.
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
   * Translates the {@link ObjectExpr} to core syntax.
   * 
   * @param pObjectExpr The given {@link ObjectExpr}.
   * @param pRecursive Translate recursive all children of the
   *          {@link ObjectExpr}.
   * @return The translated {@link Expression}.
   */
  private Expression translateToCoreSyntaxObjectExpr ( ObjectExpr pObjectExpr ,
      boolean pRecursive )
  {
    if ( pRecursive )
    {
      return new ObjectExpr ( pObjectExpr.getId ( ) , pObjectExpr.getTau ( ) ,
          ( Row ) translateToCoreSyntax ( pObjectExpr.getRow ( ) , true ) ) ;
    }
    return pObjectExpr ;
  }


  /**
   * Translates the {@link Row} to core syntax.
   * 
   * @param pRow The given {@link Row}.
   * @param pRecursive Translate recursive all children of the {@link Row}.
   * @return The translated {@link Expression}.
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
