package de.unisiegen.tpml.core.latex ;


import java.io.BufferedWriter ;
import java.io.FileOutputStream ;
import java.io.OutputStreamWriter ;
import java.io.PrintStream ;
import java.io.StringReader ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.Exn ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Location ;
import de.unisiegen.tpml.core.languages.Language ;
import de.unisiegen.tpml.core.languages.LanguageFactory ;
import de.unisiegen.tpml.core.types.IntegerType ;
import de.unisiegen.tpml.core.types.PolyType ;
import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.core.types.TypeVariable ;
import de.unisiegen.tpml.core.types.UnifyType ;
import de.unisiegen.tpml.core.types.UnitType ;


/**
 * A test class for the latex export.
 * 
 * @author Christian Fehler
 */
@ SuppressWarnings ( value =
{ "all" } )
public class LatexTest
{
  public static void main ( String [ ] args )
  {
    testExpression ( ) ;
    // testType ( ) ;
  }


  public static void testExpression ( )
  {
    try
    {
      String text = "let x: int = 1 in x + x" ;
      LanguageFactory factory = LanguageFactory.newInstance ( ) ;
      Language language = factory.getLanguageById ( "l4" ) ;
      Expression expression = language.newParser ( new StringReader ( text ) )
          .parse ( ) ;
      // Test outputs
      System.out.println ( expression.toPrettyString ( ) ) ;
      // Latex outputs
      BufferedWriter out = new BufferedWriter ( new OutputStreamWriter (
          new FileOutputStream ( "test.tex" ) , "UTF8" ) ) ;
      out.write ( "\\documentclass[a4paper,12pt]{report}" ) ;
      out.newLine ( ) ;
      out.newLine ( ) ;
      out.write ( "\\usepackage[utf8]{inputenc}" ) ;
      out.newLine ( ) ;
      out.write ( "\\usepackage{a4wide}" ) ;
      out.newLine ( ) ;
      out.newLine ( ) ;
      for ( LatexPackage pack : expression.getLatexPackages ( ) )
      {
        out.write ( pack.toString ( ) ) ;
        out.newLine ( ) ;
      }
      out.newLine ( ) ;
      out.write ( "\\setlength{\\parindent}{0pt}" ) ;
      out.newLine ( ) ;
      out.write ( "\\thispagestyle{myheadings}" ) ;
      out.newLine ( ) ;
      out.write ( "\\markright{TPML LaTeX Export}" ) ;
      out.newLine ( ) ;
      out.newLine ( ) ;
      for ( LatexCommand command : expression.getLatexCommands ( ) )
      {
        out.write ( command.toString ( ) ) ;
        out.newLine ( ) ;
      }
      out.newLine ( ) ;
      out.write ( "\\begin{document}" ) ;
      out.newLine ( ) ;
      out.newLine ( ) ;
      out.write ( "$" ) ;
      out.newLine ( ) ;
      out.write ( expression.toLatexString ( ).toString ( ) ) ;
      out.newLine ( ) ;
      out.write ( "$" ) ;
      out.newLine ( ) ;
      out.newLine ( ) ;
      out.write ( "\\end{document}" ) ;
      out.flush ( ) ;
      out.close ( ) ;
      Runtime.getRuntime ( ).exec ( "pdflatex test.tex" ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testType ( )
  {
    try
    {
      String text = "unit" ;
      LanguageFactory factory = LanguageFactory.newInstance ( ) ;
      Language language = factory.getLanguageById ( "l4" ) ;
      Type type = language.newTypeParser ( new StringReader ( text ) ).parse ( ) ;
      // Test outputs
      System.out.println ( type.toPrettyString ( ) ) ;
      // Latex outputs
      BufferedWriter out = new BufferedWriter ( new OutputStreamWriter (
          new FileOutputStream ( "test.tex" ) , "UTF8" ) ) ;
      out.write ( "\\documentclass[a4paper,12pt]{report}" ) ;
      out.newLine ( ) ;
      out.newLine ( ) ;
      out.write ( "\\usepackage[utf8]{inputenc}" ) ;
      out.newLine ( ) ;
      out.write ( "\\usepackage{a4wide}" ) ;
      out.newLine ( ) ;
      out.newLine ( ) ;
      for ( LatexPackage pack : type.getLatexPackages ( ) )
      {
        out.write ( pack.toString ( ) ) ;
        out.newLine ( ) ;
      }
      out.newLine ( ) ;
      out.write ( "\\setlength{\\parindent}{0pt}" ) ;
      out.newLine ( ) ;
      out.write ( "\\thispagestyle{myheadings}" ) ;
      out.newLine ( ) ;
      out.write ( "\\markright{TPML LaTeX Export}" ) ;
      out.newLine ( ) ;
      out.newLine ( ) ;
      for ( LatexCommand command : type.getLatexCommands ( ) )
      {
        out.write ( command.toString ( ) ) ;
        out.newLine ( ) ;
      }
      out.newLine ( ) ;
      out.write ( "\\begin{document}" ) ;
      out.newLine ( ) ;
      out.newLine ( ) ;
      out.write ( "$" ) ;
      out.newLine ( ) ;
      out.write ( type.toLatexString ( ).toString ( ) ) ;
      out.newLine ( ) ;
      out.write ( "$" ) ;
      out.newLine ( ) ;
      out.newLine ( ) ;
      out.write ( "\\end{document}" ) ;
      out.flush ( ) ;
      out.close ( ) ;
      Runtime.getRuntime ( ).exec ( "pdflatex test.tex" ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }
}
