package de.unisiegen.tpml.core.latex ;


import java.io.BufferedWriter ;
import java.io.FileOutputStream ;
import java.io.IOException ;
import java.io.OutputStreamWriter ;
import java.io.StringReader ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.bigstep.DefaultBigStepProofNode ;
import de.unisiegen.tpml.core.expressions.And ;
import de.unisiegen.tpml.core.expressions.ArithmeticOperator ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.InfixOperation ;
import de.unisiegen.tpml.core.expressions.IntegerConstant ;
import de.unisiegen.tpml.core.expressions.Location ;
import de.unisiegen.tpml.core.interpreters.DefaultStore ;
import de.unisiegen.tpml.core.languages.Language ;
import de.unisiegen.tpml.core.languages.LanguageFactory ;
import de.unisiegen.tpml.core.smallstep.DefaultSmallStepProofNode ;
import de.unisiegen.tpml.core.subtypingrec.DefaultSubType ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerExpressionProofNode ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerTypeProofNode ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution ;
import de.unisiegen.tpml.core.typechecker.SeenTypes ;
import de.unisiegen.tpml.core.typechecker.TypeEquationListTypeChecker ;
import de.unisiegen.tpml.core.typechecker.TypeEquationTypeChecker ;
import de.unisiegen.tpml.core.typeinference.TypeEquationListTypeInference ;
import de.unisiegen.tpml.core.typeinference.TypeEquationTypeInference ;
import de.unisiegen.tpml.core.typeinference.TypeJudgement ;
import de.unisiegen.tpml.core.typeinference.TypeSubType ;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList ;
import de.unisiegen.tpml.core.types.ArrowType ;
import de.unisiegen.tpml.core.types.BooleanType ;
import de.unisiegen.tpml.core.types.IntegerType ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.core.types.TypeVariable ;
import de.unisiegen.tpml.core.types.UnitType ;
import java.io.BufferedWriter ;
import java.io.FileOutputStream ;
import java.io.IOException ;
import java.io.OutputStreamWriter ;
import java.io.StringReader ;
import java.util.ArrayList ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.And ;
import de.unisiegen.tpml.core.expressions.ArithmeticOperator ;
import de.unisiegen.tpml.core.expressions.Exn ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.InfixOperation ;
import de.unisiegen.tpml.core.expressions.IntegerConstant ;
import de.unisiegen.tpml.core.expressions.Location ;
import de.unisiegen.tpml.core.expressions.Ref ;
import de.unisiegen.tpml.core.interpreters.DefaultStore ;
import de.unisiegen.tpml.core.languages.Language ;
import de.unisiegen.tpml.core.languages.LanguageFactory ;
import de.unisiegen.tpml.core.minimaltyping.DefaultMinimalTypingTypesProofNode ;
import de.unisiegen.tpml.core.smallstep.DefaultSmallStepProofNode ;
import de.unisiegen.tpml.core.subtypingrec.DefaultSubType ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution ;
import de.unisiegen.tpml.core.typechecker.SeenTypes ;
import de.unisiegen.tpml.core.typechecker.TypeEquationListTypeChecker ;
import de.unisiegen.tpml.core.typechecker.TypeEquationTypeChecker ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.typeinference.DefaultTypeInferenceProofNode ;
import de.unisiegen.tpml.core.typeinference.TypeEquationListTypeInference ;
import de.unisiegen.tpml.core.typeinference.TypeEquationTypeInference ;
import de.unisiegen.tpml.core.typeinference.TypeFormula ;
import de.unisiegen.tpml.core.typeinference.TypeJudgement ;
import de.unisiegen.tpml.core.typeinference.TypeSubType ;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList ;
import de.unisiegen.tpml.core.types.ArrowType ;
import de.unisiegen.tpml.core.types.BooleanType ;
import de.unisiegen.tpml.core.types.IntegerType ;
import de.unisiegen.tpml.core.types.MonoType ;
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
  private static BufferedWriter bufferedWriter ;


  public static void close ( )
  {
    try
    {
      bufferedWriter.close ( ) ;
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
      bufferedWriter = new BufferedWriter ( new OutputStreamWriter (
          new FileOutputStream ( "test.tex" ) , "UTF8" ) ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
    int number = 21 ;
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
    if ( number == 13 ) testTypeSubstitutionList ( ) ;
    if ( number == 14 ) testTypeJudgement ( ) ;
    if ( number == 15 ) testSmallStepProofNode ( ) ;
    if ( number == 16 ) testTypeCheckerExpressionProofNode ( ) ;
    if ( number == 17 ) testTypeCheckerTypeProofNode ( ) ;
    if ( number == 18 ) testBigStepProofNode ( ) ;
    if ( number == 19 ) testTypeInferenceProofNode ( ) ;
    if ( number == 21 ) testMinimalTypingTypesProofNode ( ) ;
  }


  public static void printLatexPrintable ( LatexPrintable pLatexPrintable )
  {
    // document class and needed packages
    println ( "%%" ) ;
    println ( "%% TPML LaTeX Export" ) ;
    println ( "%%" ) ;
    println ( ) ;
    println ( "\\documentclass[a4paper,12pt]{report}" ) ;
    println ( "\\usepackage[utf8]{inputenc}" ) ;
    println ( "\\usepackage{a4wide}" ) ;
    println ( "\\setlength{\\parindent}{0pt}" ) ;
    println ( ) ;
    // packages
    TreeSet < LatexPackage > packages = pLatexPrintable.getLatexPackages ( ) ;
    if ( packages.size ( ) > 0 )
    {
      println ( "%%" ) ;
      println ( "%% " + LatexPackage.DESCRIPTION ) ;
      println ( "%%" ) ;
      println ( ) ;
    }
    for ( LatexPackage pack : packages )
    {
      println ( pack ) ;
    }
    if ( packages.size ( ) > 0 )
    {
      println ( ) ;
    }
    // instructions
    TreeSet < LatexInstruction > instructions = pLatexPrintable
        .getLatexInstructions ( ) ;
    if ( instructions.size ( ) > 0 )
    {
      println ( "%%" ) ;
      println ( "%% " + LatexInstruction.DESCRIPTION ) ;
      println ( "%%" ) ;
      println ( ) ;
    }
    for ( LatexInstruction instruction : instructions )
    {
      println ( instruction ) ;
    }
    if ( instructions.size ( ) > 0 )
    {
      println ( ) ;
    }
    // commands
    TreeSet < LatexCommand > commands = pLatexPrintable.getLatexCommands ( ) ;
    if ( commands.size ( ) > 0 )
    {
      println ( "%%" ) ;
      println ( "%% " + LatexCommand.DESCRIPTION ) ;
      println ( "%%" ) ;
      println ( ) ;
    }
    for ( LatexCommand command : commands )
    {
      println ( command ) ;
    }
    if ( commands.size ( ) > 0 )
    {
      println ( ) ;
    }
    // document
    println ( "%%" ) ;
    println ( "%% Document" ) ;
    println ( "%%" ) ;
    println ( ) ;
    println ( "\\begin{document}" ) ;
    println ( ) ;
    println ( "$" ) ;
    println ( pLatexPrintable.toLatexString ( ).toString ( ) ) ;
    println ( "$" ) ;
    println ( ) ;
    println ( "\\end{document}" ) ;
    // close and compile
    close ( ) ;
    compile ( ) ;
  }


  public static void println ( )
  {
    try
    {
      bufferedWriter.newLine ( ) ;
      System.out.println ( ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void println ( LatexCommand pLatexCommand )
  {
    try
    {
      bufferedWriter.write ( pLatexCommand.toString ( ) ) ;
      bufferedWriter.newLine ( ) ;
      System.out.println ( pLatexCommand.toString ( ) ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void println ( LatexInstruction pLatexInstruction )
  {
    try
    {
      bufferedWriter.write ( pLatexInstruction.toString ( ) ) ;
      bufferedWriter.newLine ( ) ;
      System.out.println ( pLatexInstruction.toString ( ) ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void println ( LatexPackage pLatexPackage )
  {
    try
    {
      bufferedWriter.write ( pLatexPackage.toString ( ) ) ;
      bufferedWriter.newLine ( ) ;
      System.out.println ( pLatexPackage.toString ( ) ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void println ( String pText )
  {
    try
    {
      bufferedWriter.write ( pText ) ;
      bufferedWriter.newLine ( ) ;
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
      String text = "lambda x.x+x" ;
      LanguageFactory factory = LanguageFactory.newInstance ( ) ;
      Language language = factory.getLanguageById ( "l4" ) ;
      Expression expression = language.newParser ( new StringReader ( text ) )
          .parse ( ) ;
      printLatexPrintable ( expression ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
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
      printLatexPrintable ( seenTypes1 ) ;
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
      printLatexPrintable ( seenTypes1 ) ;
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
      printLatexPrintable ( store ) ;
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
      printLatexPrintable ( subType ) ;
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
      String text = "int -> int -> int" ;
      LanguageFactory factory = LanguageFactory.newInstance ( ) ;
      Language language = factory.getLanguageById ( "l4" ) ;
      Type type = language.newTypeParser ( new StringReader ( text ) ).parse ( ) ;
      printLatexPrintable ( type ) ;
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
      printLatexPrintable ( environment ) ;
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
      printLatexPrintable ( equationList ) ;
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
      printLatexPrintable ( equationList ) ;
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
      printLatexPrintable ( typeEquation ) ;
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
      printLatexPrintable ( typeEquation ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testTypeJudgement ( )
  {
    try
    {
      DefaultTypeEnvironment environment = new DefaultTypeEnvironment ( ) ;
      environment = ( DefaultTypeEnvironment ) environment
          .extend ( new Identifier ( "b" , Identifier.Set.VARIABLE ) ,
              new BooleanType ( ) ) ;
      environment = ( DefaultTypeEnvironment ) environment.extend (
          new Identifier ( "a" , Identifier.Set.VARIABLE ) , new UnitType ( ) ) ;
      Expression expression = new InfixOperation ( ArithmeticOperator
          .newPlus ( ) , new Identifier ( "a" , Identifier.Set.VARIABLE ) ,
          new Identifier ( "b" , Identifier.Set.VARIABLE ) ) ;
      MonoType type = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      TypeJudgement judgement = new TypeJudgement ( environment , expression ,
          type ) ;
      printLatexPrintable ( judgement ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testMinimalTypingTypesProofNode ( )
  {
    try
    {
      DefaultMinimalTypingTypesProofNode node = new DefaultMinimalTypingTypesProofNode (
          new IntegerType ( ) , new BooleanType ( ) ) ;
      printLatexPrintable ( node ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testSmallStepProofNode ( )
  {
    try
    {
      DefaultStore store = new DefaultStore ( ) ;
      store.put ( new Location ( "c" ) , new IntegerConstant ( 3 ) ) ;
      store.put ( new Location ( "b" ) , new IntegerConstant ( 2 ) ) ;
      store.put ( new Location ( "a" ) , new IntegerConstant ( 1 ) ) ;
      DefaultSmallStepProofNode node = new DefaultSmallStepProofNode ( new And (
          new Ref ( ) , new IntegerConstant ( 1 ) ) , store ) ;
      printLatexPrintable ( node ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testTypeInferenceProofNode ( )
  {
    try
    {
      SeenTypes < TypeEquationTypeInference > seenTypes = new SeenTypes < TypeEquationTypeInference > ( ) ;
      TypeEquationTypeInference typeEquation1 = new TypeEquationTypeInference (
          new TypeVariable ( 0 , 0 ) , new BooleanType ( ) , seenTypes ) ;
      TypeEquationTypeInference typeEquation2 = new TypeEquationTypeInference (
          new TypeVariable ( 0 , 0 ) , new TypeVariable ( 0 , 1 ) , seenTypes ) ;
      TypeEquationTypeInference typeEquation3 = new TypeEquationTypeInference (
          new BooleanType ( ) , new BooleanType ( ) , seenTypes ) ;
      TypeSubType typeSubType = new TypeSubType ( new IntegerType ( ) ,
          new BooleanType ( ) ) ;
      ArrayList < TypeFormula > formulas = new ArrayList < TypeFormula > ( ) ;
      DefaultTypeEnvironment environment = new DefaultTypeEnvironment ( ) ;
      environment = ( DefaultTypeEnvironment ) environment
          .extend ( new Identifier ( "b" , Identifier.Set.VARIABLE ) ,
              new BooleanType ( ) ) ;
      environment = ( DefaultTypeEnvironment ) environment.extend (
          new Identifier ( "a" , Identifier.Set.VARIABLE ) , new UnitType ( ) ) ;
      Expression expression = new InfixOperation ( ArithmeticOperator
          .newPlus ( ) , new Identifier ( "a" , Identifier.Set.VARIABLE ) ,
          new Identifier ( "b" , Identifier.Set.VARIABLE ) ) ;
      MonoType type = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      TypeJudgement judgement = new TypeJudgement ( environment , expression ,
          type ) ;
      formulas.add ( typeEquation1 ) ;
      formulas.add ( typeEquation2 ) ;
      formulas.add ( typeEquation3 ) ;
      formulas.add ( typeSubType ) ;
      formulas.add ( judgement ) ;
      DefaultTypeSubstitution typeSubstitution1 = new DefaultTypeSubstitution (
          new TypeVariable ( 0 , 0 ) , new BooleanType ( ) ) ;
      DefaultTypeSubstitution typeSubstitution2 = new DefaultTypeSubstitution (
          new TypeVariable ( 0 , 1 ) , new BooleanType ( ) ) ;
      DefaultTypeSubstitution typeSubstitution3 = new DefaultTypeSubstitution (
          new TypeVariable ( 0 , 2 ) , new BooleanType ( ) ) ;
      ArrayList < TypeSubstitution > substitutions = new ArrayList < TypeSubstitution > ( ) ;
      substitutions.add ( typeSubstitution1 ) ;
      substitutions.add ( typeSubstitution2 ) ;
      substitutions.add ( typeSubstitution3 ) ;
      DefaultTypeInferenceProofNode node = new DefaultTypeInferenceProofNode (
          formulas , substitutions ) ;
      printLatexPrintable ( node ) ;
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
      printLatexPrintable ( typeSubstitution ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testTypeSubstitutionList ( )
  {
    try
    {
      TypeSubstitutionList substitionList = TypeSubstitutionList.EMPTY_LIST ;
      DefaultTypeSubstitution typeSubstitution1 = new DefaultTypeSubstitution (
          new TypeVariable ( 0 , 0 ) , new BooleanType ( ) ) ;
      DefaultTypeSubstitution typeSubstitution2 = new DefaultTypeSubstitution (
          new TypeVariable ( 0 , 1 ) , new IntegerType ( ) ) ;
      substitionList = substitionList.extend ( typeSubstitution1 ) ;
      substitionList = substitionList.extend ( typeSubstitution2 ) ;
      printLatexPrintable ( substitionList ) ;
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
      printLatexPrintable ( typeSubType ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testTypeCheckerExpressionProofNode ( )
  {
    try
    {
      DefaultTypeEnvironment environment = new DefaultTypeEnvironment ( ) ;
      environment = ( DefaultTypeEnvironment ) environment
          .extend ( new Identifier ( "b" , Identifier.Set.VARIABLE ) ,
              new BooleanType ( ) ) ;
      environment = ( DefaultTypeEnvironment ) environment.extend (
          new Identifier ( "a" , Identifier.Set.VARIABLE ) , new UnitType ( ) ) ;
      Expression expression = new InfixOperation ( ArithmeticOperator
          .newPlus ( ) , new Identifier ( "a" , Identifier.Set.VARIABLE ) ,
          new Identifier ( "b" , Identifier.Set.VARIABLE ) ) ;
      MonoType type = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      DefaultTypeCheckerExpressionProofNode node = new DefaultTypeCheckerExpressionProofNode (
          environment , expression , type ) ;
      printLatexPrintable ( node ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testTypeCheckerTypeProofNode ( )
  {
    try
    {
      MonoType type = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      MonoType type2 = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      DefaultTypeCheckerTypeProofNode node = new DefaultTypeCheckerTypeProofNode (
          type , type2 ) ;
      printLatexPrintable ( node ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testBigStepProofNode ( )
  {
    try
    {
      Expression expression = new InfixOperation ( ArithmeticOperator
          .newPlus ( ) , new Identifier ( "a" , Identifier.Set.VARIABLE ) ,
          new Identifier ( "b" , Identifier.Set.VARIABLE ) ) ;
      DefaultBigStepProofNode node = new DefaultBigStepProofNode ( expression ) ;
      printLatexPrintable ( node ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }
}
