package de.unisiegen.tpml.core.exceptions ;


import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.Duplication ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Row ;


/**
 * This <code>RuntimeException</code> is thrown if an {@link Identifier} of
 * the {@link Duplication} is not an {@link Identifier} of an {@link Attribute}
 * in the {@link Row}.
 * 
 * @author Christian Fehler
 */
public final class RowSubstitutionException extends RuntimeException
{
  /**
   * The serial version UID.
   */
  private static final long serialVersionUID = 4717084402322482294L ;


  /**
   * Initializes the exception.
   */
  public RowSubstitutionException ( )
  {
    super ( Messages.getString ( "Exception.1" ) ) ; //$NON-NLS-1$
  }
}
