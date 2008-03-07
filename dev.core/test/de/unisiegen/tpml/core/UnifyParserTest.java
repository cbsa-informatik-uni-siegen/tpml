package de.unisiegen.tpml.core;


import java.io.StringReader;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageFactory;
import de.unisiegen.tpml.core.typechecker.TypeEquationListTypeChecker;


/**
 * A test class for the unify parsers.
 * 
 * @author Christian Fehler
 */
@SuppressWarnings ( value =
{ "all" } )
public class UnifyParserTest
{

  public static void main ( String [] pArguments )
  {
    try
    {
      LanguageFactory factory = LanguageFactory.newInstance ();
      Language language = factory.getLanguageById ( "l1unify" );

      String text = "{int = bool, 'a -> int = 'b -> bool}";

      TypeEquationListTypeChecker t = language.newUnifyParser (
          new StringReader ( text ) ).parse ();

      System.out.println ( t.toPrettyString () );
    }
    catch ( Exception exc )
    {
      exc.printStackTrace ();
    }
  }
}
