package de.unisiegen.tpml.core.latex ;


import java.io.BufferedWriter ;
import java.io.FileOutputStream ;
import java.io.IOException ;
import java.io.OutputStreamWriter ;
import java.io.StringReader ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.IntegerConstant ;
import de.unisiegen.tpml.core.expressions.Location ;
import de.unisiegen.tpml.core.interpreters.DefaultStore ;
import de.unisiegen.tpml.core.languages.Language ;
import de.unisiegen.tpml.core.languages.LanguageFactory ;
import de.unisiegen.tpml.core.subtypingrec.DefaultSubType ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution ;
import de.unisiegen.tpml.core.typechecker.SeenTypes ;
import de.unisiegen.tpml.core.typechecker.TypeEquationListTypeChecker ;
import de.unisiegen.tpml.core.typechecker.TypeEquationTypeChecker ;
import de.unisiegen.tpml.core.typeinference.TypeEquationListTypeInference ;
import de.unisiegen.tpml.core.typeinference.TypeEquationTypeInference ;
import de.unisiegen.tpml.core.typeinference.TypeSubType ;
import de.unisiegen.tpml.core.types.BooleanType ;
import de.unisiegen.tpml.core.types.IntegerType ;
import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.core.types.TypeVariable ;
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
  private static BufferedWriter out ;


  public static void close ( )
  {
    try
    {
      out.close ( ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void compile ( )
  {
    try
    {
      Runtime.getRuntime ( ).exec ( "pdflatex test.tex" ) ;
    }
    catch ( IOException e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void main ( String [ ] args )
  {
    try
    {
      out = new BufferedWriter ( new OutputStreamWriter ( new FileOutputStream (
          "test.tex" ) , "UTF8" ) ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
    int number = 12 ;
    if ( number == 0 ) testExpression ( ) ;
    if ( number == 1 ) testType ( ) ;
    if ( number == 2 ) testTypeEnvironment ( ) ;
    if ( number == 3 ) testStore ( ) ;
    if ( number == 4 ) testSeenTypesTypeChecker ( ) ;
    if ( number == 5 ) testSeenTypesTypeInference ( ) ;
    if ( number == 6 ) testTypeEquationTypeChecker ( ) ;
    if ( number == 7 ) testTypeEquationTypeInference ( ) ;
    if ( number == 8 ) testSubType ( ) ;
    if ( number == 9 ) testTypeSubType ( ) ;
    if ( number == 10 ) testTypeSubstitution ( ) ;
    if ( number == 11 ) testTypeEquationListTypeChecker ( ) ;
    if ( number == 12 ) testTypeEquationListTypeInference ( ) ;
  }


  public static void printLine ( )
  {
    try
    {
      out.newLine ( ) ;
      System.out.println ( ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void printLine ( String pText )
  {
    try
    {
      out.write ( pText ) ;
      out.newLine ( ) ;
      System.out.println ( pText ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testExpression ( )
  {
    try
    {
      String text = "while 1 do 2" ;
      LanguageFactory factory = LanguageFactory.newInstance ( ) ;
      Language language = factory.getLanguageById ( "l4" ) ;
      Expression expression = language.newParser ( new StringReader ( text ) )
          .parse ( ) ;
      testLatexPrintable ( expression ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testLatexPrintable ( LatexPrintable pLatexPrintable )
  {
    // document class and packages
    printLine ( "\\documentclass[a4paper,12pt]{report}" ) ;
    printLine ( "\\usepackage[utf8]{inputenc}" ) ;
    printLine ( "\\usepackage{a4wide}" ) ;
    printLine ( ) ;
    // packages
    TreeSet < LatexPackage > packages = pLatexPrintable.getLatexPackages ( ) ;
    for ( LatexPackage pack : packages )
    {
      printLine ( pack.toString ( ) ) ;
    }
    if ( packages.size ( ) > 0 )
    {
      printLine ( ) ;
    }
    // instructions
    TreeSet < LatexInstruction > instructions = pLatexPrintable
        .getLatexInstructions ( ) ;
    for ( LatexInstruction instruction : instructions )
    {
      printLine ( instruction.toString ( ) ) ;
    }
    if ( instructions.size ( ) > 0 )
    {
      printLine ( ) ;
    }
    // commands
    TreeSet < LatexCommand > commands = pLatexPrintable.getLatexCommands ( ) ;
    for ( LatexCommand command : commands )
    {
      printLine ( command.toString ( ) ) ;
    }
    if ( commands.size ( ) > 0 )
    {
      printLine ( ) ;
    }
    // document
    printLine ( "\\begin{document}" ) ;
    printLine ( ) ;
    printLine ( "$" ) ;
    printLine ( pLatexPrintable.toLatexString ( ).toString ( ) ) ;
    printLine ( "$" ) ;
    printLine ( ) ;
    printLine ( "\\end{document}" ) ;
    close ( ) ;
    compile ( ) ;
  }


  public static void testSeenTypesTypeChecker ( )
  {
    try
    {
      SeenTypes < TypeEquationTypeChecker > seenTypes1 = new SeenTypes < TypeEquationTypeChecker > ( ) ;
      SeenTypes < TypeEquationTypeChecker > seenTypes2 = new SeenTypes < TypeEquationTypeChecker > ( ) ;
      seenTypes1.add ( new TypeEquationTypeChecker ( new IntegerType ( ) ,
          new BooleanType ( ) , seenTypes2 ) ) ;
      seenTypes1.add ( new TypeEquationTypeChecker ( new BooleanType ( ) ,
          new UnitType ( ) , seenTypes2 ) ) ;
      testLatexPrintable ( seenTypes1 ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testSeenTypesTypeInference ( )
  {
    try
    {
      SeenTypes < TypeEquationTypeInference > seenTypes1 = new SeenTypes < TypeEquationTypeInference > ( ) ;
      SeenTypes < TypeEquationTypeInference > seenTypes2 = new SeenTypes < TypeEquationTypeInference > ( ) ;
      seenTypes1.add ( new TypeEquationTypeInference ( new IntegerType ( ) ,
          new BooleanType ( ) , seenTypes2 ) ) ;
      seenTypes1.add ( new TypeEquationTypeInference ( new BooleanType ( ) ,
          new UnitType ( ) , seenTypes2 ) ) ;
      testLatexPrintable ( seenTypes1 ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testStore ( )
  {
    try
    {
      DefaultStore store = new DefaultStore ( ) ;
      store.put ( new Location ( "c" ) , new IntegerConstant ( 3 ) ) ;
      store.put ( new Location ( "b" ) , new IntegerConstant ( 2 ) ) ;
      store.put ( new Location ( "a" ) , new IntegerConstant ( 1 ) ) ;
      testLatexPrintable ( store ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testSubType ( )
  {
    try
    {
      DefaultSubType subType = new DefaultSubType ( new IntegerType ( ) ,
          new BooleanType ( ) ) ;
      testLatexPrintable ( subType ) ;
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
      String text = "int ref" ;
      LanguageFactory factory = LanguageFactory.newInstance ( ) ;
      Language language = factory.getLanguageById ( "l4" ) ;
      Type type = language.newTypeParser ( new StringReader ( text ) ).parse ( ) ;
      testLatexPrintable ( type ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testTypeEnvironment ( )
  {
    try
    {
      DefaultTypeEnvironment environment = new DefaultTypeEnvironment ( ) ;
      environment = ( DefaultTypeEnvironment ) environment
          .extend ( new Identifier ( "c" , Identifier.Set.VARIABLE ) ,
              new IntegerType ( ) ) ;
      environment = ( DefaultTypeEnvironment ) environment
          .extend ( new Identifier ( "b" , Identifier.Set.VARIABLE ) ,
              new BooleanType ( ) ) ;
      environment = ( DefaultTypeEnvironment ) environment.extend (
          new Identifier ( "a" , Identifier.Set.VARIABLE ) , new UnitType ( ) ) ;
      testLatexPrintable ( environment ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testTypeEquationListTypeChecker ( )
  {
    try
    {
      TypeEquationListTypeChecker equationList = TypeEquationListTypeChecker.EMPTY_LIST ;
      SeenTypes < TypeEquationTypeChecker > seenTypes = new SeenTypes < TypeEquationTypeChecker > ( ) ;
      TypeEquationTypeChecker typeEquation1 = new TypeEquationTypeChecker (
          new IntegerType ( ) , new BooleanType ( ) , seenTypes ) ;
      TypeEquationTypeChecker typeEquation2 = new TypeEquationTypeChecker (
          new BooleanType ( ) , new UnitType ( ) , seenTypes ) ;
      equationList = equationList.extend ( typeEquation1 ) ;
      equationList = equationList.extend ( typeEquation2 ) ;
      testLatexPrintable ( equationList ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testTypeEquationListTypeInference ( )
  {
    try
    {
      TypeEquationListTypeInference equationList = TypeEquationListTypeInference.EMPTY_LIST ;
      SeenTypes < TypeEquationTypeInference > seenTypes = new SeenTypes < TypeEquationTypeInference > ( ) ;
      TypeEquationTypeInference typeEquation1 = new TypeEquationTypeInference (
          new IntegerType ( ) , new BooleanType ( ) , seenTypes ) ;
      TypeEquationTypeInference typeEquation2 = new TypeEquationTypeInference (
          new BooleanType ( ) , new UnitType ( ) , seenTypes ) ;
      equationList = equationList.extend ( typeEquation1 ) ;
      equationList = equationList.extend ( typeEquation2 ) ;
      testLatexPrintable ( equationList ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testTypeEquationTypeChecker ( )
  {
    try
    {
      SeenTypes < TypeEquationTypeChecker > seenTypes = new SeenTypes < TypeEquationTypeChecker > ( ) ;
      TypeEquationTypeChecker typeEquation = new TypeEquationTypeChecker (
          new IntegerType ( ) , new BooleanType ( ) , seenTypes ) ;
      testLatexPrintable ( typeEquation ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testTypeEquationTypeInference ( )
  {
    try
    {
      SeenTypes < TypeEquationTypeInference > seenTypes = new SeenTypes < TypeEquationTypeInference > ( ) ;
      TypeEquationTypeInference typeEquation = new TypeEquationTypeInference (
          new IntegerType ( ) , new BooleanType ( ) , seenTypes ) ;
      testLatexPrintable ( typeEquation ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testTypeSubstitution ( )
  {
    try
    {
      DefaultTypeSubstitution typeSubstitution = new DefaultTypeSubstitution (
          new TypeVariable ( 0 , 0 ) , new BooleanType ( ) ) ;
      testLatexPrintable ( typeSubstitution ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testTypeSubType ( )
  {
    try
    {
      TypeSubType typeSubType = new TypeSubType ( new IntegerType ( ) ,
          new BooleanType ( ) ) ;
      testLatexPrintable ( typeSubType ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }
}
