package de.unisiegen.tpml.core.languages.l2c ;


import de.unisiegen.tpml.core.expressions.Body ;
import de.unisiegen.tpml.core.expressions.Class ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.New ;
import de.unisiegen.tpml.core.interfaces.BodyOrRow ;
import de.unisiegen.tpml.core.languages.l2o.L2OLanguage ;
import de.unisiegen.tpml.core.languages.l2o.L2OLanguageTranslator ;


/**
 * Language translator for the <code>L2C</code> language.
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 * @see L2OLanguage
 */
public class L2CLanguageTranslator extends L2OLanguageTranslator
{
  /**
   * Allocates a new <code>L2CLanguageTranslator</code>.
   */
  public L2CLanguageTranslator ( )
  {
    super ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see L2OLanguageTranslator#translateToCoreSyntax(Expression, boolean)
   */
  @ Override
  public Expression translateToCoreSyntax ( Expression pExpression ,
      boolean pRecursive )
  {
    if ( pExpression instanceof Class )
    {
      return translateToCoreSyntaxClass ( ( Class ) pExpression , pRecursive ) ;
    }
    else if ( pExpression instanceof New )
    {
      return translateToCoreSyntaxNew ( ( New ) pExpression , pRecursive ) ;
    }
    else if ( pExpression instanceof Body )
    {
      return translateToCoreSyntaxBody ( ( Body ) pExpression , pRecursive ) ;
    }
    return super.translateToCoreSyntax ( pExpression , pRecursive ) ;
  }


  /**
   * Translates the {@link Body} to core syntax.
   * 
   * @param pBody The given {@link Body}.
   * @param pRecursive Translate recursive all children of the {@link Body}.
   * @return The translated {@link Expression}.
   */
  private Expression translateToCoreSyntaxBody ( Body pBody , boolean pRecursive )
  {
    if ( pRecursive )
    {
      return new Body ( pBody.getIdentifiers ( ) , translateToCoreSyntax (
          pBody.getE ( ) , true ) , ( BodyOrRow ) translateToCoreSyntax (
          ( Expression ) pBody.getBodyOrRow ( ) , true ) ) ;
    }
    return pBody ;
  }


  /**
   * Translates the {@link Class} to core syntax.
   * 
   * @param pClass The given {@link Class}.
   * @param pRecursive Translate recursive all children of the {@link Class}.
   * @return The translated {@link Expression}.
   */
  private Expression translateToCoreSyntaxClass ( Class pClass ,
      boolean pRecursive )
  {
    if ( pRecursive )
    {
      return new Class ( pClass.getId ( ) , pClass.getTau ( ) ,
          ( BodyOrRow ) translateToCoreSyntax ( ( Expression ) pClass
              .getBodyOrRow ( ) , true ) ) ;
    }
    return pClass ;
  }


  /**
   * Translates the {@link New} to core syntax.
   * 
   * @param pNew The given {@link New}.
   * @param pRecursive Translate recursive all children of the {@link New}.
   * @return The translated {@link Expression}.
   */
  private Expression translateToCoreSyntaxNew ( New pNew , boolean pRecursive )
  {
    if ( pRecursive )
    {
      return new New ( translateToCoreSyntax ( pNew.getE ( ) , true ) ) ;
    }
    return pNew ;
  }
}
