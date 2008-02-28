package de.unisiegen.tpml.core.languages.l0;


import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.languages.AbstractLanguageTranslator;


/**
 * Language translator for the <code>L0</code> language.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:304 $
 * @see AbstractLanguageTranslator
 * @see L0Language
 */
public class L0LanguageTranslator extends AbstractLanguageTranslator
{

  /**
   * Allocates a new <code>L0LanguageTranslator</code> instance.
   */
  public L0LanguageTranslator ()
  {
    super ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see AbstractLanguageTranslator#translateToCoreSyntax(Expression, boolean)
   */
  @Override
  public Expression translateToCoreSyntax ( final Expression pExpression,
      final boolean pRecursive )
  {
    if ( ( pExpression instanceof Application ) && pRecursive )
    {
      final Application application = ( Application ) pExpression;
      return new Application ( this.translateToCoreSyntax ( application
          .getE1 (), true ), this.translateToCoreSyntax ( application.getE2 (),
          true ) );
    }
    else if ( ( pExpression instanceof Lambda ) && pRecursive )
    {
      final Lambda lambda = ( Lambda ) pExpression;
      return new Lambda ( lambda.getId (), lambda.getTau (), this
          .translateToCoreSyntax ( lambda.getE (), true ) );
    }
    else
    {
      return super.translateToCoreSyntax ( pExpression, pRecursive );
    }
  }
}
