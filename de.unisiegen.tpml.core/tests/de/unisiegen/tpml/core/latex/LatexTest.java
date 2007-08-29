package de.unisiegen.tpml.core.latex ;


import java.io.BufferedWriter ;
import java.io.File ;
import java.io.FileOutputStream ;
import java.io.IOException ;
import java.io.OutputStreamWriter ;
import java.io.StringReader ;
import java.util.ArrayList ;
import java.util.LinkedList ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.ProofModel ;
import de.unisiegen.tpml.core.ProofNode ;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel ;
import de.unisiegen.tpml.core.bigstep.BigStepProofResult ;
import de.unisiegen.tpml.core.bigstep.DefaultBigStepProofNode ;
import de.unisiegen.tpml.core.expressions.ArithmeticOperator ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.InfixOperation ;
import de.unisiegen.tpml.core.expressions.IntegerConstant ;
import de.unisiegen.tpml.core.expressions.Location ;
import de.unisiegen.tpml.core.expressions.Ref ;
import de.unisiegen.tpml.core.interpreters.DefaultStore ;
import de.unisiegen.tpml.core.languages.Language ;
import de.unisiegen.tpml.core.languages.LanguageFactory ;
import de.unisiegen.tpml.core.minimaltyping.DefaultMinimalTypingExpressionProofNode ;
import de.unisiegen.tpml.core.minimaltyping.DefaultMinimalTypingTypesProofNode ;
import de.unisiegen.tpml.core.smallstep.DefaultSmallStepProofNode ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;
import de.unisiegen.tpml.core.subtyping.DefaultSubTypingProofNode ;
import de.unisiegen.tpml.core.subtypingrec.DefaultRecSubTypingProofNode ;
import de.unisiegen.tpml.core.subtypingrec.DefaultSubType ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerExpressionProofNode ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerTypeProofNode ;
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
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel ;
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
  private static boolean console = true ;


  private static boolean compile = true ;


  public static void compile ( )
  {
    if ( ! compile )
    {
      return ;
    }
    try
    {
      if ( console )
      {
        System.out.println ( ) ;
      }
      System.out.println ( "*** compile ***" ) ;
      Process p ;
      // latex 1
      System.out.println ( "latex 1" ) ;
      p = Runtime.getRuntime ( ).exec ( "latex test.tex" ) ;
      try
      {
        p.waitFor ( ) ;
      }
      catch ( InterruptedException e )
      {
        e.printStackTrace ( ) ;
      }
      if ( p.exitValue ( ) != 0 )
      {
        System.err.println ( "LatexTest: latex 1 error" ) ;
      }
      // latex 2
      System.out.println ( "latex 2" ) ;
      p = Runtime.getRuntime ( ).exec ( "latex test.tex" ) ;
      try
      {
        p.waitFor ( ) ;
      }
      catch ( InterruptedException e )
      {
        e.printStackTrace ( ) ;
      }
      if ( p.exitValue ( ) != 0 )
      {
        System.err.println ( "LatexTest: latex 2 error" ) ;
      }
      // dvips
      System.out.println ( "dvips" ) ;
      p = Runtime.getRuntime ( ).exec ( "dvips test.dvi" ) ;
      try
      {
        p.waitFor ( ) ;
      }
      catch ( InterruptedException e )
      {
        e.printStackTrace ( ) ;
      }
      if ( p.exitValue ( ) != 0 )
      {
        System.err.println ( "LatexTest: dvips error" ) ;
      }
      // ps2pdf
      System.out.println ( "ps2pdf" ) ;
      p = Runtime.getRuntime ( ).exec ( "ps2pdf test.ps" ) ;
      try
      {
        p.waitFor ( ) ;
      }
      catch ( InterruptedException e )
      {
        e.printStackTrace ( ) ;
      }
      if ( p.exitValue ( ) != 0 )
      {
        System.err.println ( "LatexTest: ps2pdf error" ) ;
      }
    }
    catch ( IOException e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void exportLatexPrintable ( LatexPrintable pLatexPrintable ,
      File pFile )
  {
    BufferedWriter writer ;
    try
    {
      writer = new BufferedWriter ( new OutputStreamWriter (
          new FileOutputStream ( pFile ) , "UTF8" ) ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      return ;
    }
    println ( writer , "%%" ) ;
    println ( writer , "%% TPML LaTeX Export" ) ;
    println ( writer , "%%" ) ;
    println ( writer ) ;
    println ( writer , "\\documentclass[a4paper,12pt]{report}" ) ;
    println ( writer , "\\usepackage[utf8]{inputenc}" ) ;
    println ( writer , "\\usepackage{lscape}" ) ;
    println ( writer , "\\setlength{\\parindent}{0pt}" ) ;
    println ( writer , "\\pagestyle{empty}" ) ;
    println ( writer , "\\oddsidemargin=-30pt" ) ;
    println ( writer , "\\topmargin=-60pt" ) ;
    println ( writer , "\\textwidth=510pt" ) ;
    println ( writer , "\\textheight=750pt" ) ;
    println ( writer ) ;
    // packages
    TreeSet < LatexPackage > packages = pLatexPrintable.getLatexPackages ( ) ;
    if ( packages.size ( ) > 0 )
    {
      println ( writer , "%%" ) ;
      println ( writer , "%% " + LatexPackage.DESCRIPTION ) ;
      println ( writer , "%%" ) ;
      println ( writer ) ;
    }
    for ( LatexPackage pack : packages )
    {
      println ( writer , pack ) ;
    }
    if ( packages.size ( ) > 0 )
    {
      println ( writer ) ;
    }
    // instructions
    TreeSet < LatexInstruction > instructions = pLatexPrintable
        .getLatexInstructions ( ) ;
    if ( instructions.size ( ) > 0 )
    {
      println ( writer , "%%" ) ;
      println ( writer , "%% " + LatexInstruction.DESCRIPTION ) ;
      println ( writer , "%%" ) ;
      println ( writer ) ;
    }
    for ( LatexInstruction instruction : instructions )
    {
      println ( writer , instruction ) ;
    }
    if ( instructions.size ( ) > 0 )
    {
      println ( writer ) ;
    }
    // commands
    TreeSet < LatexCommand > commands = pLatexPrintable.getLatexCommands ( ) ;
    if ( commands.size ( ) > 0 )
    {
      println ( writer , "%%" ) ;
      println ( writer , "%% " + LatexCommand.DESCRIPTION ) ;
      println ( writer , "%%" ) ;
      println ( writer ) ;
    }
    for ( LatexCommand command : commands )
    {
      println ( writer , command ) ;
    }
    if ( commands.size ( ) > 0 )
    {
      println ( writer ) ;
    }
    // document
    println ( writer , "%%" ) ;
    println ( writer , "%% Document" ) ;
    println ( writer , "%%" ) ;
    println ( writer ) ;
    println ( writer , "\\begin{document}" ) ;
    println ( writer , "\\begin{landscape}" ) ;
    println ( writer ) ;
    println ( writer , "$" ) ;
    println ( writer , pLatexPrintable.toLatexString ( ).toString ( ) ) ;
    println ( writer , "$" ) ;
    println ( writer ) ;
    println ( writer , "\\end{landscape}" ) ;
    println ( writer , "\\end{document}" ) ;
    // close
    try
    {
      writer.close ( ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void main ( String [ ] args )
  {
    for ( String arg : args )
    {
      if ( arg.equals ( "-no-console" ) )
      {
        console = false ;
      }
      if ( arg.equals ( "-no-compile" ) )
      {
        compile = false ;
      }
    }
    int number = 25 ;
    if ( number == 0 ) testExpression ( ) ;
    if ( number == 1 ) testType ( ) ;
    if ( number == 2 ) testTypeEnvironment ( ) ;
    if ( number == 3 ) testStore ( ) ;
    if ( number == 4 ) testSeenTypes ( ) ;
    if ( number == 5 ) testTypeEquationTypeChecker ( ) ;
    if ( number == 6 ) testTypeEquationTypeInference ( ) ;
    if ( number == 7 ) testSubType ( ) ;
    if ( number == 8 ) testTypeSubType ( ) ;
    if ( number == 9 ) testTypeSubstitution ( ) ;
    if ( number == 10 ) testTypeEquationListTypeChecker ( ) ;
    if ( number == 11 ) testTypeEquationListTypeInference ( ) ;
    if ( number == 12 ) testTypeSubstitutionList ( ) ;
    if ( number == 13 ) testTypeJudgement ( ) ;
    if ( number == 14 ) testSmallStepProofNode ( ) ;
    if ( number == 15 ) testTypeCheckerExpressionProofNode ( ) ;
    if ( number == 16 ) testTypeCheckerTypeProofNode ( ) ;
    if ( number == 17 ) testBigStepProofNode ( ) ;
    if ( number == 18 ) testTypeInferenceProofNode ( ) ;
    if ( number == 19 ) testSubTypingProofNode ( ) ;
    if ( number == 20 ) testRecSubTypingProofNode ( ) ;
    if ( number == 21 ) testMinimalTypingTypesProofNode ( ) ;
    if ( number == 22 ) testMinimalTypingExpressionProofNode ( ) ;
    if ( number == 23 ) testBigStepProofModel ( ) ;
    if ( number == 24 ) testSmallStepProofModel ( ) ;
    if ( number == 25 ) testTypeInferenceProofModel ( ) ;
    if ( ! compile )
    {
      System.out.println ( "*** latex export done ***" ) ;
    }
  }


  private static ProofNode nextNode ( ProofModel model )
  {
    LinkedList < ProofNode > nodes = new LinkedList < ProofNode > ( ) ;
    nodes.add ( model.getRoot ( ) ) ;
    while ( ! nodes.isEmpty ( ) )
    {
      ProofNode node = nodes.poll ( ) ;
      if ( node.getRules ( ).length == 0 )
      {
        return node ;
      }
      for ( int n = 0 ; n < node.getChildCount ( ) ; ++ n )
      {
        nodes.add ( node.getChildAt ( n ) ) ;
      }
    }
    throw new IllegalStateException ( "Unable to find next node" ) ;
  }


  public static void printLatexPrintable ( LatexPrintable pLatexPrintable )
  {
    BufferedWriter writer ;
    try
    {
      writer = new BufferedWriter ( new OutputStreamWriter (
          new FileOutputStream ( "test.tex" ) , "UTF8" ) ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      return ;
    }
    // document class and needed packages
    println ( writer , "%%" ) ;
    println ( writer , "%% TPML LaTeX Export" ) ;
    println ( writer , "%%" ) ;
    println ( writer ) ;
    println ( writer , "\\documentclass[a4paper,12pt]{report}" ) ;
    println ( writer , "\\usepackage[utf8]{inputenc}" ) ;
    println ( writer , "\\usepackage{lscape}" ) ;
    println ( writer , "\\setlength{\\parindent}{0pt}" ) ;
    println ( writer , "\\pagestyle{empty}" ) ;
    println ( writer , "\\oddsidemargin=-30pt" ) ;
    println ( writer , "\\topmargin=-60pt" ) ;
    println ( writer , "\\textwidth=510pt" ) ;
    println ( writer , "\\textheight=750pt" ) ;
    println ( writer ) ;
    // packages
    TreeSet < LatexPackage > packages = pLatexPrintable.getLatexPackages ( ) ;
    if ( packages.size ( ) > 0 )
    {
      println ( writer , "%%" ) ;
      println ( writer , "%% " + LatexPackage.DESCRIPTION ) ;
      println ( writer , "%%" ) ;
      println ( writer ) ;
    }
    for ( LatexPackage pack : packages )
    {
      println ( writer , pack ) ;
    }
    if ( packages.size ( ) > 0 )
    {
      println ( writer ) ;
    }
    // instructions
    TreeSet < LatexInstruction > instructions = pLatexPrintable
        .getLatexInstructions ( ) ;
    if ( instructions.size ( ) > 0 )
    {
      println ( writer , "%%" ) ;
      println ( writer , "%% " + LatexInstruction.DESCRIPTION ) ;
      println ( writer , "%%" ) ;
      println ( writer ) ;
    }
    for ( LatexInstruction instruction : instructions )
    {
      println ( writer , instruction ) ;
    }
    if ( instructions.size ( ) > 0 )
    {
      println ( writer ) ;
    }
    // commands
    TreeSet < LatexCommand > commands = pLatexPrintable.getLatexCommands ( ) ;
    if ( commands.size ( ) > 0 )
    {
      println ( writer , "%%" ) ;
      println ( writer , "%% " + LatexCommand.DESCRIPTION ) ;
      println ( writer , "%%" ) ;
      println ( writer ) ;
    }
    for ( LatexCommand command : commands )
    {
      println ( writer , command ) ;
    }
    if ( commands.size ( ) > 0 )
    {
      println ( writer ) ;
    }
    // document
    println ( writer , "%%" ) ;
    println ( writer , "%% Document" ) ;
    println ( writer , "%%" ) ;
    println ( writer ) ;
    println ( writer , "\\begin{document}" ) ;
    println ( writer , "\\begin{landscape}" ) ;
    println ( writer ) ;
    println ( writer , "$" ) ;
    println ( writer , pLatexPrintable.toLatexString ( ).toString ( ) ) ;
    println ( writer , "$" ) ;
    println ( writer ) ;
    println ( writer , "\\end{landscape}" ) ;
    println ( writer , "\\end{document}" ) ;
    // close
    try
    {
      writer.close ( ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
    // compile
    compile ( ) ;
  }


  public static void printLatexPrintable ( LatexPrintableNode pLatexPrintable )
  {
    BufferedWriter writer ;
    try
    {
      writer = new BufferedWriter ( new OutputStreamWriter (
          new FileOutputStream ( "test.tex" ) , "UTF8" ) ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      return ;
    }
    // document class and needed packages
    println ( writer , "%%" ) ;
    println ( writer , "%% TPML LaTeX Export" ) ;
    println ( writer , "%%" ) ;
    println ( writer ) ;
    println ( writer , "\\documentclass[a4paper,12pt]{report}" ) ;
    println ( writer , "\\usepackage[utf8]{inputenc}" ) ;
    println ( writer , "\\usepackage{lscape}" ) ;
    println ( writer , "\\setlength{\\parindent}{0pt}" ) ;
    println ( writer , "\\pagestyle{empty}" ) ;
    println ( writer , "\\oddsidemargin=-30pt" ) ;
    println ( writer , "\\topmargin=-60pt" ) ;
    println ( writer , "\\textwidth=510pt" ) ;
    println ( writer , "\\textheight=750pt" ) ;
    println ( writer ) ;
    // packages
    TreeSet < LatexPackage > packages = pLatexPrintable.getLatexPackages ( ) ;
    if ( packages.size ( ) > 0 )
    {
      println ( writer , "%%" ) ;
      println ( writer , "%% " + LatexPackage.DESCRIPTION ) ;
      println ( writer , "%%" ) ;
      println ( writer ) ;
    }
    for ( LatexPackage pack : packages )
    {
      println ( writer , pack ) ;
    }
    if ( packages.size ( ) > 0 )
    {
      println ( writer ) ;
    }
    // instructions
    TreeSet < LatexInstruction > instructions = pLatexPrintable
        .getLatexInstructions ( ) ;
    if ( instructions.size ( ) > 0 )
    {
      println ( writer , "%%" ) ;
      println ( writer , "%% " + LatexInstruction.DESCRIPTION ) ;
      println ( writer , "%%" ) ;
      println ( writer ) ;
    }
    for ( LatexInstruction instruction : instructions )
    {
      println ( writer , instruction ) ;
    }
    if ( instructions.size ( ) > 0 )
    {
      println ( writer ) ;
    }
    // commands
    TreeSet < LatexCommand > commands = pLatexPrintable.getLatexCommands ( ) ;
    if ( commands.size ( ) > 0 )
    {
      println ( writer , "%%" ) ;
      println ( writer , "%% " + LatexCommand.DESCRIPTION ) ;
      println ( writer , "%%" ) ;
      println ( writer ) ;
    }
    for ( LatexCommand command : commands )
    {
      println ( writer , command ) ;
    }
    if ( commands.size ( ) > 0 )
    {
      println ( writer ) ;
    }
    // document
    println ( writer , "%%" ) ;
    println ( writer , "%% Document" ) ;
    println ( writer , "%%" ) ;
    println ( writer ) ;
    println ( writer , "\\begin{document}" ) ;
    println ( writer , "\\begin{landscape}" ) ;
    println ( writer ) ;
    println ( writer , "$" ) ;
    println ( writer , pLatexPrintable.toLatexString ( 0 , 0 ).toString ( ) ) ;
    println ( writer , "$" ) ;
    println ( writer ) ;
    println ( writer , "\\end{landscape}" ) ;
    println ( writer , "\\end{document}" ) ;
    // close
    try
    {
      writer.close ( ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
    // compile
    compile ( ) ;
  }


  public static void println ( BufferedWriter pBufferedWriter )
  {
    try
    {
      pBufferedWriter.newLine ( ) ;
      if ( console )
      {
        System.out.println ( ) ;
      }
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void println ( BufferedWriter pBufferedWriter ,
      LatexCommand pLatexCommand )
  {
    try
    {
      pBufferedWriter.write ( pLatexCommand.toString ( ) ) ;
      pBufferedWriter.newLine ( ) ;
      if ( console )
      {
        System.out.println ( pLatexCommand.toString ( ) ) ;
      }
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void println ( BufferedWriter pBufferedWriter ,
      LatexInstruction pLatexInstruction )
  {
    try
    {
      pBufferedWriter.write ( pLatexInstruction.toString ( ) ) ;
      pBufferedWriter.newLine ( ) ;
      if ( console )
      {
        System.out.println ( pLatexInstruction.toString ( ) ) ;
      }
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void println ( BufferedWriter pBufferedWriter ,
      LatexPackage pLatexPackage )
  {
    try
    {
      pBufferedWriter.write ( pLatexPackage.toString ( ) ) ;
      pBufferedWriter.newLine ( ) ;
      if ( console )
      {
        System.out.println ( pLatexPackage.toString ( ) ) ;
      }
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void println ( BufferedWriter pBufferedWriter , String pText )
  {
    try
    {
      pBufferedWriter.write ( pText ) ;
      pBufferedWriter.newLine ( ) ;
      if ( console )
      {
        System.out.println ( pText ) ;
      }
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testBigStepProofModel ( )
  {
    try
    {
      Expression expression = new InfixOperation ( ArithmeticOperator
          .newPlus ( ) , new IntegerConstant ( 1 ) , new InfixOperation (
          ArithmeticOperator.newPlus ( ) , new IntegerConstant ( 2 ) ,
          new IntegerConstant ( 3 ) ) ) ;
      Language language = LanguageFactory.newInstance ( ).getLanguageById (
          "l2o" ) ;
      BigStepProofModel model = language.newBigStepProofModel ( expression ) ;
      model.guess ( nextNode ( model ) ) ;
      printLatexPrintable ( model ) ;
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
          new Ref ( ) ) ;
      DefaultStore store1 = new DefaultStore ( ) ;
      store1.put ( new Location ( "f" ) , new IntegerConstant ( 6 ) ) ;
      store1.put ( new Location ( "e" ) , new IntegerConstant ( 5 ) ) ;
      store1.put ( new Location ( "d" ) , new IntegerConstant ( 4 ) ) ;
      DefaultBigStepProofNode node = new DefaultBigStepProofNode ( expression ,
          store1 ) ;
      // with result
      DefaultStore store2 = new DefaultStore ( ) ;
      store2.put ( new Location ( "c" ) , new IntegerConstant ( 3 ) ) ;
      store2.put ( new Location ( "b" ) , new IntegerConstant ( 2 ) ) ;
      store2.put ( new Location ( "a" ) , new IntegerConstant ( 1 ) ) ;
      BigStepProofResult result = new BigStepProofResult ( store2 , new Ref ( ) ) ;
      node.setResult ( result ) ;
      printLatexPrintable ( node ) ;
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
      String text = "let x = 0 in 1" ;
      text = "let rec map f l = if is_empty l then [] else (f (hd l)) :: map f (tl l) in let rec append l1 l2 = if is_empty l1 then l2 else hd l1 :: append (tl l1) l2 in let rec power_set l = if is_empty l then [[]] else let p = power_set (tl l) in append p (map ((::) (hd l)) p) in power_set [1;2]" ;
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


  public static void testMinimalTypingExpressionProofNode ( )
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
      DefaultMinimalTypingExpressionProofNode node = new DefaultMinimalTypingExpressionProofNode (
          environment , expression ) ;
      node.setType ( type ) ;
      printLatexPrintable ( node ) ;
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


  public static void testRecSubTypingProofNode ( )
  {
    try
    {
      MonoType type = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      MonoType type2 = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new BooleanType ( ) ) ) ;
      DefaultRecSubTypingProofNode node = new DefaultRecSubTypingProofNode (
          type , type2 , new SeenTypes < DefaultSubType > ( ) ) ;
      printLatexPrintable ( node ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public static void testSeenTypes ( )
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


  public static void testSmallStepProofModel ( )
  {
    try
    {
      String text = "let x = 1 in x" ;
      // text = "let rec map f l = if is_empty l then [] else (f (hd l)) :: map
      // f (tl l) in let rec append l1 l2 = if is_empty l1 then l2 else hd l1 ::
      // append (tl l1) l2 in let rec power_set l = if is_empty l then [[]] else
      // let p = power_set (tl l) in append p (map ((::) (hd l)) p) in power_set
      // [1;2]" ;
      text = "let rec f x = if x = 0 then 1 else x * f (x-1) in f 2" ;
      LanguageFactory factory = LanguageFactory.newInstance ( ) ;
      Language language = factory.getLanguageById ( "l4" ) ;
      Expression expression = language.newParser ( new StringReader ( text ) )
          .parse ( ) ;
      SmallStepProofModel model = language.newSmallStepProofModel ( expression ) ;
      model.complete ( nextNode ( model ) ) ;
      printLatexPrintable ( model ) ;
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
      Expression expression = new InfixOperation ( ArithmeticOperator
          .newPlus ( ) , new IntegerConstant ( 1 ) , new InfixOperation (
          ArithmeticOperator.newPlus ( ) , new IntegerConstant ( 2 ) ,
          new IntegerConstant ( 3 ) ) ) ;
      DefaultSmallStepProofNode node = new DefaultSmallStepProofNode (
          expression ) ;
      printLatexPrintable ( node ) ;
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


  public static void testSubTypingProofNode ( )
  {
    try
    {
      MonoType type = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      MonoType type2 = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      DefaultSubTypingProofNode node = new DefaultSubTypingProofNode ( type ,
          type2 ) ;
      printLatexPrintable ( node ) ;
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
      String text = "int * bool * unit" ;
      LanguageFactory factory = LanguageFactory.newInstance ( ) ;
      Language language = factory.getLanguageById ( "l4" ) ;
      Type type = language.newTypeParser ( new StringReader ( text ) ).parse ( ) ;
      // PolyType
      /*
       * TreeSet < TypeVariable > quantified = new TreeSet < TypeVariable > ( ) ;
       * quantified.add ( new TypeVariable ( 0 , 0 ) ) ; quantified.add ( new
       * TypeVariable ( 0 , 1 ) ) ; quantified.add ( new TypeVariable ( 0 , 2 ) ) ;
       * type = new PolyType ( quantified , new IntegerType ( ) ) ;
       */
      printLatexPrintable ( type ) ;
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


  public static void testTypeInferenceProofModel ( )
  {
    try
    {
      String text = "let rec map f l = if is_empty l then [] else (f (hd l)) :: map f (tl l) in let rec append l1 l2 = if is_empty l1 then l2 else hd l1 :: append (tl l1) l2 in let rec power_set l = if is_empty l then [[]] else let p = power_set (tl l) in append p (map ((::) (hd l)) p) in power_set [1;2]" ;
      // text = "let rec f x = if x = 0 then 1 else x * f (x-1) in f 2" ;
      LanguageFactory factory = LanguageFactory.newInstance ( ) ;
      Language language = factory.getLanguageById ( "l4" ) ;
      Expression expression = language.newParser ( new StringReader ( text ) )
          .parse ( ) ;
      TypeInferenceProofModel model = language
          .newTypeInferenceProofModel ( expression ) ;
      model.complete ( nextNode ( model ) ) ;
      printLatexPrintable ( model ) ;
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
      SeenTypes < TypeEquationTypeInference > seenTypes2 = new SeenTypes < TypeEquationTypeInference > ( ) ;
      seenTypes.add ( new TypeEquationTypeInference (
          new TypeVariable ( 0 , 0 ) , new BooleanType ( ) , seenTypes2 ) ) ;
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
}
