package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


public class Self extends Identifier
{
  private final static String NAME = "self" ;


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


  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory factory )
  {
    PrettyStringBuilder builder = factory.newBuilder ( this , PRIO_IDENTIFIER ) ;
    builder.addKeyword ( NAME ) ;
    return builder ;
  }
}
