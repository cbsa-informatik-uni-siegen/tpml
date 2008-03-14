package de.unisiegen.tpml.core.languages.l2c;


import de.unisiegen.tpml.core.expressions.Class;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Inherit;
import de.unisiegen.tpml.core.expressions.New;
import de.unisiegen.tpml.core.languages.l2o.L2OLanguage;
import de.unisiegen.tpml.core.languages.l2o.L2OLanguageTranslator;


/**
 * Language translator for the <code>L2C</code> language.
 * 
 * @author Christian Fehler
 * @version $Rev$
 * @see L2OLanguage
 */
public class L2CLanguageTranslator extends L2OLanguageTranslator
{

  /**
   * Allocates a new <code>L2CLanguageTranslator</code>.
   */
  public L2CLanguageTranslator ()
  {
    super ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see L2OLanguageTranslator#translateToCoreSyntax(Expression, boolean)
   */
  @Override
  public Expression translateToCoreSyntax ( Expression pExpression,
      boolean pRecursive )
  {
    if ( pExpression instanceof Class )
    {
      return translateToCoreSyntaxClass ( ( Class ) pExpression, pRecursive );
    }
    else if ( pExpression instanceof New )
    {
      return translateToCoreSyntaxNew ( ( New ) pExpression, pRecursive );
    }
    else if ( pExpression instanceof Inherit )
    {
      return translateToCoreSyntaxInherit ( ( Inherit ) pExpression, pRecursive );
    }
    return super.translateToCoreSyntax ( pExpression, pRecursive );
  }


  /**
   * Translates the {@link Class} to core syntax.
   * 
   * @param pClass The given {@link Class}.
   * @param pRecursive Translate recursive all children of the {@link Class}.
   * @return The translated {@link Expression}.
   */
  private Expression translateToCoreSyntaxClass ( Class pClass,
      boolean pRecursive )
  {
    if ( pRecursive )
    {
      return new Class ( pClass.getId (), pClass.getTau (),
          translateToCoreSyntax ( pClass.getBody (), true ) );
    }
    return pClass;
  }


  /**
   * Translates the {@link Inherit} to core syntax.
   * 
   * @param pInherit The given {@link Inherit}.
   * @param pRecursive Translate recursive all children of the {@link Inherit}.
   * @return The translated {@link Expression}.
   */
  private Expression translateToCoreSyntaxInherit ( Inherit pInherit,
      boolean pRecursive )
  {
    if ( pRecursive )
    {
      return new Inherit ( pInherit.getIdentifiers (), translateToCoreSyntax (
          pInherit.getE (), true ), translateToCoreSyntax (
          pInherit.getBody (), true ) );
    }
    return pInherit;
  }


  /**
   * Translates the {@link New} to core syntax.
   * 
   * @param pNew The given {@link New}.
   * @param pRecursive Translate recursive all children of the {@link New}.
   * @return The translated {@link Expression}.
   */
  private Expression translateToCoreSyntaxNew ( New pNew, boolean pRecursive )
  {
    if ( pRecursive )
    {
      return new New ( translateToCoreSyntax ( pNew.getE (), true ) );
    }
    return pNew;
  }
}
