package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public class Self extends Identifier
{
  /**
   * TODO
   */
  private final static String NAME = "self" ; //$NON-NLS-1$


  /**
   * TODO
   */
  public Self ( )
  {
    super ( NAME ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Self" ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory factory )
  {
    PrettyStringBuilder builder = factory.newBuilder ( this , PRIO_IDENTIFIER ) ;
    builder.addKeyword ( NAME ) ;
    return builder ;
  }
}
