package de.unisiegen.tpml.core.latex ;


import java.io.File ;
import java.io.StringReader ;
import java.util.ArrayList ;
import java.util.LinkedList ;
import de.unisiegen.tpml.core.ProofModel ;
import de.unisiegen.tpml.core.ProofNode ;
import de.unisiegen.tpml.core.bigstep.AbstractBigStepProofRule ;
import de.unisiegen.tpml.core.bigstep.BigStepProofContext ;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel ;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode ;
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
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofModel ;
import de.unisiegen.tpml.core.smallstep.DefaultSmallStepProofNode ;
import de.unisiegen.tpml.core.smallstep.DefaultSmallStepProofRule ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;
import de.unisiegen.tpml.core.subtyping.AbstractSubTypingProofRule ;
import de.unisiegen.tpml.core.subtyping.DefaultSubTypingProofNode ;
import de.unisiegen.tpml.core.subtyping.SubTypingProofContext ;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel ;
import de.unisiegen.tpml.core.subtyping.SubTypingProofNode ;
import de.unisiegen.tpml.core.subtypingrec.AbstractRecSubTypingProofRule ;
import de.unisiegen.tpml.core.subtypingrec.DefaultRecSubTypingProofNode ;
import de.unisiegen.tpml.core.subtypingrec.DefaultSubType ;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofContext ;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofModel ;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode ;
import de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRule ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerExpressionProofNode ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerTypeProofNode ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution ;
import de.unisiegen.tpml.core.typechecker.SeenTypes ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode ;
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
 * @author Benjamin Mies
 */
@ SuppressWarnings ( value =
{ "all" } )
public class LatexTest
{
  private static boolean error = false ;


  private static boolean all = false ;


  private static boolean console = true ;


  private static boolean compile = true ;


  private final static void compile ( )
  {
    try
    {
      if ( console )
      {
        System.out.println ( ) ;
      }
      if ( console ) System.out.println ( "*** compile ***" ) ;
      Process p ;
      // latex 1
      if ( console ) System.out.println ( "latex 1" ) ;
      p = Runtime.getRuntime ( ).exec ( "latex test.tex" ) ;
      try
      {
        p.waitFor ( ) ;
      }
      catch ( InterruptedException e )
      {
        e.printStackTrace ( ) ;
        System.exit ( 1 ) ;
      }
      if ( p.exitValue ( ) != 0 )
      {
        System.err.println ( "LatexTest: latex 1 error" ) ;
        return ;
      }
      // latex 2
      if ( console ) System.out.println ( "latex 2" ) ;
      p = Runtime.getRuntime ( ).exec ( "latex test.tex" ) ;
      try
      {
        p.waitFor ( ) ;
      }
      catch ( InterruptedException e )
      {
        e.printStackTrace ( ) ;
        System.exit ( 1 ) ;
      }
      if ( p.exitValue ( ) != 0 )
      {
        System.err.println ( "LatexTest: latex 2 error" ) ;
        return ;
      }
      // dvips
      if ( console ) System.out.println ( "dvips" ) ;
      p = Runtime.getRuntime ( ).exec ( "dvips test.dvi" ) ;
      try
      {
        p.waitFor ( ) ;
      }
      catch ( InterruptedException e )
      {
        e.printStackTrace ( ) ;
        System.exit ( 1 ) ;
      }
      if ( p.exitValue ( ) != 0 )
      {
        System.err.println ( "LatexTest: dvips error" ) ;
        return ;
      }
      // ps2pdf
      if ( console ) System.out.println ( "ps2pdf" ) ;
      p = Runtime.getRuntime ( ).exec ( "ps2pdf test.ps" ) ;
      try
      {
        p.waitFor ( ) ;
      }
      catch ( InterruptedException e )
      {
        e.printStackTrace ( ) ;
        System.exit ( 1 ) ;
      }
      if ( p.exitValue ( ) != 0 )
      {
        System.err.println ( "LatexTest: ps2pdf error" ) ;
        return ;
      }
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  public final static void main ( String [ ] args )
  {
    File test = new File ( "test.tex" ) ;
    File tpml = new File ( "tpml.tex" ) ;
    System.out.println ( "*** started ***" ) ;
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
      if ( arg.equals ( "-all" ) ) all = true ;
    }
    if ( ! all )
    {
      int number = 0 ;
      // Expression, Type, Environment
      if ( number == 00 ) testExpression ( test , tpml ) ;
      if ( number == 01 ) testType ( test , tpml ) ;
      if ( number == 02 ) testTypeEnvironment ( test , tpml ) ;
      if ( number == 03 ) testStore ( test , tpml ) ;
      // TypeChecker
      if ( number == 10 ) testTypeCheckerProofRule ( test , tpml ) ;
      if ( number == 11 ) testSeenTypes ( test , tpml ) ;
      if ( number == 12 ) testTypeSubstitution ( test , tpml ) ;
      if ( number == 13 ) testTypeEquationTypeChecker ( test , tpml ) ;
      if ( number == 14 ) testTypeEquationListTypeChecker ( test , tpml ) ;
      if ( number == 15 ) testTypeCheckerExpressionProofNode ( test , tpml ) ;
      if ( number == 16 ) testTypeCheckerTypeProofNode ( test , tpml ) ;
      if ( number == 17 ) testTypeCheckerProofModel ( test , tpml ) ;
      // TypeInference
      if ( number == 20 ) testTypeEquationTypeInference ( test , tpml ) ;
      if ( number == 21 ) testTypeEquationListTypeInference ( test , tpml ) ;
      if ( number == 22 ) testTypeSubstitutionList ( test , tpml ) ;
      if ( number == 23 ) testTypeJudgement ( test , tpml ) ;
      if ( number == 24 ) testTypeSubType ( test , tpml ) ;
      if ( number == 25 ) testTypeInferenceProofNode ( test , tpml ) ;
      if ( number == 26 ) testTypeInferenceProofModel ( test , tpml ) ;
      // SmallStep
      if ( number == 30 ) testSmallStepProofRule ( test , tpml ) ;
      if ( number == 31 ) testSmallStepProofNode ( test , tpml ) ;
      if ( number == 32 ) testSmallStepProofModel ( test , tpml ) ;
      // BigStep
      if ( number == 40 ) testBigStepProofRule ( test , tpml ) ;
      if ( number == 41 ) testBigStepResult ( test , tpml ) ;
      if ( number == 42 ) testBigStepProofNode ( test , tpml ) ;
      if ( number == 43 ) testBigStepProofModel ( test , tpml ) ;
      // MinimalTyping
      if ( number == 50 ) testMinimalTypingProofRule ( test , tpml ) ;
      if ( number == 51 ) testMinimalTypingExpressionProofNode ( test , tpml ) ;
      if ( number == 52 ) testMinimalTypingTypesProofNode ( test , tpml ) ;
      if ( number == 53 ) testMinimalTypingProofModel ( test , tpml ) ;
      // SubTyping
      if ( number == 60 ) testSubTypingProofRule ( test , tpml ) ;
      if ( number == 61 ) testSubTypingProofNode ( test , tpml ) ;
      if ( number == 62 ) testSubTypingProofModel ( test , tpml ) ;
      // RecSubTyping
      if ( number == 70 ) testRecSubTypingProofRule ( test , tpml ) ;
      if ( number == 71 ) testSubType ( test , tpml ) ;
      if ( number == 72 ) testRecSubTypingProofNode ( test , tpml ) ;
      if ( number == 73 ) testRecSubTypingProofModel ( test , tpml ) ;
      // LatexExportAll
      if ( number == 80 ) testLatexExportTPML ( test , tpml ) ;
      if ( compile )
      {
        compile ( ) ;
      }
    }
    else
    {
      console = false ;
      // Expression, Type, Environment
      testExpression ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testExpression: success" ) ;
      else System.err.println ( "testExpression: failed" ) ;
      testType ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testType: success" ) ;
      else System.err.println ( "testType: failed" ) ;
      testTypeEnvironment ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testEnvironment: success" ) ;
      else System.err.println ( "testEnvironment: failed" ) ;
      testStore ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testStore: success" ) ;
      else System.err.println ( "testStore: failed" ) ;
      testTypeCheckerProofRule ( test , tpml ) ;
      compile ( ) ;
      // TypeChecker
      if ( removeFiles ( test ) )
        System.out.println ( "testTypeCheckerProofRule: success" ) ;
      else System.err.println ( "testTypeCheckerProofRule: failed" ) ;
      testSeenTypes ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testSeenTypes: success" ) ;
      else System.err.println ( "testSeenTypes: failed" ) ;
      testTypeSubstitution ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testTypeSubstitution: success" ) ;
      else System.err.println ( "testTypeSubstitution: failed" ) ;
      testTypeEquationTypeChecker ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testTypeEquationTypeChecker: success" ) ;
      else System.err.println ( "testTypeEquationTypeChecker: failed" ) ;
      testTypeEquationListTypeChecker ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testTypeEquationListTypeChecker: success" ) ;
      else System.err.println ( "testTypeEquationListTypeChecker: failed" ) ;
      testTypeCheckerExpressionProofNode ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testTypeCheckerExpressionProofNode: success" ) ;
      else System.err.println ( "testTypeCheckerExpressionProofNode: failed" ) ;
      testTypeCheckerTypeProofNode ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testTypeCheckerTypeProofNode: success" ) ;
      else System.err.println ( "testTypeCheckerTypeProofNode: failed" ) ;
      testTypeCheckerProofModel ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testTypeCheckerProofModel: success" ) ;
      else System.err.println ( "testTypeCheckerProofModel: failed" ) ;
      // TypeInference
      testTypeEquationTypeInference ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testTypeEquationTypeInference: success" ) ;
      else System.err.println ( "testTypeEquationTypeInference: failed" ) ;
      testTypeEquationListTypeInference ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testTypeEquationListTypeInference: success" ) ;
      else System.err.println ( "testTypeEquationListTypeInference: failed" ) ;
      testTypeSubstitutionList ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testTypeSubstitutionList: success" ) ;
      else System.err.println ( "testTypeSubstitutionList: failed" ) ;
      testTypeJudgement ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testTypeJudgement: success" ) ;
      else System.err.println ( "testTypeJudgement: failed" ) ;
      testTypeSubType ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testTypeSubType: success" ) ;
      else System.err.println ( "testTypeSubType: failed" ) ;
      testTypeInferenceProofNode ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testTypeInferenceProofNode: success" ) ;
      else System.err.println ( "testTypeInferenceProofNode: failed" ) ;
      testTypeInferenceProofModel ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testTypeInferenceProofModel: success" ) ;
      else System.err.println ( "testTypeInferenceProofModel: failed" ) ;
      // SmallStep
      testSmallStepProofRule ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testSmallStepProofRule: success" ) ;
      else System.err.println ( "testSmallStepProofRule: failed" ) ;
      testSmallStepProofNode ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testSmallStepProofNode: success" ) ;
      else System.err.println ( "testSmallStepProofNode: failed" ) ;
      testSmallStepProofModel ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testSmallStepProofModel: success" ) ;
      else System.err.println ( "testSmallStepProofModel: failed" ) ;
      // BigStep
      testBigStepProofRule ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testBigStepProofRule: success" ) ;
      else System.err.println ( "testBigStepProofRule: failed" ) ;
      testBigStepResult ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testBigStepResult: success" ) ;
      else System.err.println ( "testBigStepResult: failed" ) ;
      testBigStepProofNode ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testBigStepProofNode: success" ) ;
      else System.err.println ( "testBigStepProofNode: failed" ) ;
      testBigStepProofModel ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testBigStepProofModel: success" ) ;
      else System.err.println ( "testBigStepProofModel: failed" ) ;
      // MinimalTyping
      testMinimalTypingProofRule ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testMinimalTypingProofRule: success" ) ;
      else System.err.println ( "testMinimalTypingProofRule: failed" ) ;
      testMinimalTypingExpressionProofNode ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testMinimalTypingExpressionProofNode: success" ) ;
      else System.err.println ( "testMinimalTypingExpressionProofNode: failed" ) ;
      testMinimalTypingTypesProofNode ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testMinimalTypingTypesProofNode: success" ) ;
      else System.err.println ( "testMinimalTypingTypesProofNode: failed" ) ;
      testMinimalTypingProofModel ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testMinimalTypingProofModel: success" ) ;
      else System.err.println ( "testMinimalTypingProofModel: failed" ) ;
      // SubTyping
      testSubTypingProofRule ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testSubTypingProofRule: success" ) ;
      else System.err.println ( "testSubTypingProofRule: failed" ) ;
      testSubTypingProofNode ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testSubTypingProofNode: success" ) ;
      else System.err.println ( "testSubTypingProofNode: failed" ) ;
      testSubTypingProofModel ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testSubTypingProofModel: success" ) ;
      else System.err.println ( "testSubTypingProofModel: failed" ) ;
      // RecSubTyping
      testRecSubTypingProofRule ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testRecSubTypingProofRule: success" ) ;
      else System.err.println ( "testRecSubTypingProofRule: failed" ) ;
      testSubType ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testSubType: success" ) ;
      else System.err.println ( "testSubType: failed" ) ;
      testRecSubTypingProofNode ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testRecSubTypingProofNode: success" ) ;
      else System.err.println ( "testRecSubTypingProofNode: failed" ) ;
      testRecSubTypingProofModel ( test , tpml ) ;
      compile ( ) ;
      if ( removeFiles ( test ) )
        System.out.println ( "testRecSubTypingProofModel: success" ) ;
      else System.err.println ( "testRecSubTypingProofModel: failed" ) ;
      if ( error )
        System.err.println ( "Something gone wrong" ) ;
      else System.err.println ( "Done all without problems" ) ;
    }
    System.out.println ( "*** finished***" ) ;
  }


  private final static ProofNode nextNode ( ProofModel model )
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
      for ( int i = 0 ; i < node.getChildCount ( ) ; i ++ )
      {
        nodes.add ( node.getChildAt ( i ) ) ;
      }
    }
    throw new IllegalStateException ( "unable to find next node" ) ;
  }


  private static boolean removeFiles ( File pFile )
  {
    int index = pFile.toString ( ).indexOf ( '.' ) ;
    String fileName = pFile.toString ( ).substring ( 0 , index ) ;
    File dviFile = new File ( fileName + ".dvi" ) ;
    dviFile.delete ( ) ;
    File psFile = new File ( fileName + ".ps" ) ;
    psFile.delete ( ) ;
    File auxFile = new File ( fileName + ".aux" ) ;
    auxFile.delete ( ) ;
    File logFile = new File ( fileName + ".log" ) ;
    logFile.delete ( ) ;
    pFile.delete ( ) ;
    File pdfFile = new File ( fileName + ".pdf" ) ;
    if ( pdfFile.delete ( ) )
    {
      return true ;
    }
    error = true ;
    return false ;
  }


  private final static void testBigStepProofModel ( File pLatexFile ,
      File pTPMLFile )
  {
    try
    {
      Language language = LanguageFactory.newInstance ( ).getLanguageById (
          "l4" ) ;
      String text = "true || false" ;
      text = "let rec map f l = if is_empty l then [] else (f (hd l)) :: map f (tl l) in let rec append l1 l2 = if is_empty l1 then l2 else hd l1 :: append (tl l1) l2 in let rec power_set l = if is_empty l then [[]] else let p = power_set (tl l) in append p (map ((::) (hd l)) p) in power_set [1;2]" ;
      text = "let rec fact x = if x = 0 then 1 else x * (fact x-1) in fact 0" ;
      text = " let x: int = let x : int = let x : int ="
          + "3+2+4+5+6+7+8+9+11+2+3+4 in x+2 in x+3 in x+5" ;
      Expression expression = language.newParser ( new StringReader ( text ) )
          .parse ( ) ;
      BigStepProofModel model = language.newBigStepProofModel ( expression ) ;
      model.complete ( nextNode ( model ) ) ;
      LatexExport.export ( model , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testBigStepProofNode ( File pLatexFile ,
      File pTPMLFile )
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
      LatexExport.export ( node , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testBigStepProofRule ( File pLatexFile ,
      File pTPMLFile )
  {
    try
    {
      AbstractBigStepProofRule rule = new AbstractBigStepProofRule ( 0 ,
          "BIG-STEP-RULE" )
      {
        @ Override
        protected void applyInternal ( BigStepProofContext context ,
            BigStepProofNode node ) throws Exception
        {
        }


        @ Override
        protected void updateInternal ( BigStepProofContext context ,
            BigStepProofNode node ) throws Exception
        {
        }
      } ;
      LatexExport.export ( rule , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testBigStepResult ( File pLatexFile , File pTPMLFile )
  {
    try
    {
      DefaultStore store = new DefaultStore ( ) ;
      store.put ( new Location ( "X" ) , new IntegerConstant ( 3 ) ) ;
      store.put ( new Location ( "Y" ) , new IntegerConstant ( 2 ) ) ;
      store.put ( new Location ( "Z" ) , new IntegerConstant ( 1 ) ) ;
      BigStepProofResult result = new BigStepProofResult ( store , new Ref ( ) ) ;
      LatexExport.export ( result , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testExpression ( File pLatexFile , File pTPMLFile )
  {
    try
    {
      String text = "let rec map f l = if is_empty l then [] else (f (hd l)) :: map f (tl l) in let rec append l1 l2 = if is_empty l1 then l2 else hd l1 :: append (tl l1) l2 in let rec power_set l = if is_empty l then [[]] else let p = power_set (tl l) in append p (map ((::) (hd l)) p) in power_set [1;2]" ;
      text = "let x = 0 in 1" ;
      LanguageFactory factory = LanguageFactory.newInstance ( ) ;
      Language language = factory.getLanguageById ( "l4" ) ;
      Expression expression = language.newParser ( new StringReader ( text ) )
          .parse ( ) ;
      LatexExport.export ( expression , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testLatexExportTPML ( File pLatexFile ,
      File pTPMLFile )
  {
    try
    {
      LatexExport.export ( null , null , pTPMLFile ,
          LatexExport.Modus.TPML_FILE ) ;
      System.out.println ( "*** finished ***" ) ;
      System.exit ( 0 ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testMinimalTypingExpressionProofNode (
      File pLatexFile , File pTPMLFile )
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
      LatexExport.export ( node , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testMinimalTypingProofModel ( File pLatexFile ,
      File pTPMLFile )
  {
    try
    {
      Language language = LanguageFactory.newInstance ( ).getLanguageById (
          "l4" ) ;
      String text = "true || false" ;
      Expression expression = language.newParser ( new StringReader ( text ) )
          .parse ( ) ;
      MinimalTypingProofModel model = language.newMinimalTypingProofModel (
          expression , false ) ;
      model.complete ( nextNode ( model ) ) ;
      LatexExport.export ( model , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testMinimalTypingProofRule ( File pLatexFile ,
      File pTPMLFile )
  {
    try
    {
      AbstractBigStepProofRule rule = new AbstractBigStepProofRule ( 0 ,
          "BIG-STEP-RULE" )
      {
        @ Override
        protected void applyInternal ( BigStepProofContext context ,
            BigStepProofNode node ) throws Exception
        {
        }


        @ Override
        protected void updateInternal ( BigStepProofContext context ,
            BigStepProofNode node ) throws Exception
        {
        }
      } ;
      LatexExport.export ( rule , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testMinimalTypingTypesProofNode ( File pLatexFile ,
      File pTPMLFile )
  {
    try
    {
      DefaultMinimalTypingTypesProofNode node = new DefaultMinimalTypingTypesProofNode (
          new IntegerType ( ) , new BooleanType ( ) ) ;
      LatexExport.export ( node , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testRecSubTypingProofModel ( File pLatexFile ,
      File pTPMLFile )
  {
    try
    {
      Language language = LanguageFactory.newInstance ( ).getLanguageById (
          "l2sub" ) ;
      MonoType type = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      MonoType type2 = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      RecSubTypingProofModel model = language.newRecSubTypingProofModel ( type ,
          type2 , false ) ;
      model.complete ( nextNode ( model ) ) ;
      LatexExport.export ( model , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testRecSubTypingProofNode ( File pLatexFile ,
      File pTPMLFile )
  {
    try
    {
      MonoType type = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      MonoType type2 = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new BooleanType ( ) ) ) ;
      DefaultRecSubTypingProofNode node = new DefaultRecSubTypingProofNode (
          type , type2 , new SeenTypes < DefaultSubType > ( ) ) ;
      LatexExport.export ( node , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testRecSubTypingProofRule ( File pLatexFile ,
      File pTPMLFile )
  {
    try
    {
      AbstractRecSubTypingProofRule rule = new AbstractRecSubTypingProofRule (
          0 , "REC-SUB-TYPING-RULE" )
      {
        @ Override
        protected void applyInternal ( RecSubTypingProofContext context ,
            RecSubTypingProofNode node ) throws Exception
        {
        }
      } ;
      LatexExport.export ( rule , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testSeenTypes ( File pLatexFile , File pTPMLFile )
  {
    try
    {
      SeenTypes < TypeEquationTypeChecker > seenTypes1 = new SeenTypes < TypeEquationTypeChecker > ( ) ;
      SeenTypes < TypeEquationTypeChecker > seenTypes2 = new SeenTypes < TypeEquationTypeChecker > ( ) ;
      seenTypes1.add ( new TypeEquationTypeChecker ( new IntegerType ( ) ,
          new BooleanType ( ) , seenTypes2 ) ) ;
      seenTypes1.add ( new TypeEquationTypeChecker ( new BooleanType ( ) ,
          new UnitType ( ) , seenTypes2 ) ) ;
      LatexExport.export ( seenTypes1 , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testSmallStepProofModel ( File pLatexFile ,
      File pTPMLFile )
  {
    try
    {
      String text = "let x = 1 in x" ;
      text = "let rec map f l = if is_empty l then [] else (f (hd l)) :: map f (tl l) in let rec append l1 l2 = if is_empty l1 then l2 else hd l1 :: append (tl l1) l2 in let rec power_set l = if is_empty l then [[]] else let p = power_set (tl l) in append p (map ((::) (hd l)) p) in power_set [1;2]" ;
      text = "let rec f x = if x = 0 then 1 else x * f (x-1) in f 2" ;
      LanguageFactory factory = LanguageFactory.newInstance ( ) ;
      Language language = factory.getLanguageById ( "l4" ) ;
      Expression expression = language.newParser ( new StringReader ( text ) )
          .parse ( ) ;
      SmallStepProofModel model = language.newSmallStepProofModel ( expression ) ;
      model.complete ( nextNode ( model ) ) ;
      LatexExport.export ( model , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testSmallStepProofNode ( File pLatexFile ,
      File pTPMLFile )
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
      LatexExport.export ( node , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testSmallStepProofRule ( File pLatexFile ,
      File pTPMLFile )
  {
    try
    {
      DefaultSmallStepProofRule rule = new DefaultSmallStepProofRule ( 0 ,
          "SMALL-STEP-RULE" , true ) ;
      LatexExport.export ( rule , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testStore ( File pLatexFile , File pTPMLFile )
  {
    try
    {
      DefaultStore store = new DefaultStore ( ) ;
      store.put ( new Location ( "c" ) , new IntegerConstant ( 3 ) ) ;
      store.put ( new Location ( "b" ) , new IntegerConstant ( 2 ) ) ;
      store.put ( new Location ( "a" ) , new IntegerConstant ( 1 ) ) ;
      LatexExport.export ( store , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testSubType ( File pLatexFile , File pTPMLFile )
  {
    try
    {
      DefaultSubType subType = new DefaultSubType ( new IntegerType ( ) ,
          new BooleanType ( ) ) ;
      LatexExport.export ( subType , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testSubTypingProofModel ( File pLatexFile ,
      File pTPMLFile )
  {
    try
    {
      Language language = LanguageFactory.newInstance ( ).getLanguageById (
          "l2sub" ) ;
      MonoType type = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      MonoType type2 = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      SubTypingProofModel model = language.newSubTypingProofModel ( type ,
          type2 , false ) ;
      model.complete ( nextNode ( model ) ) ;
      LatexExport.export ( model , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testSubTypingProofNode ( File pLatexFile ,
      File pTPMLFile )
  {
    try
    {
      MonoType type = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      MonoType type2 = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      DefaultSubTypingProofNode node = new DefaultSubTypingProofNode ( type ,
          type2 ) ;
      LatexExport.export ( node , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testSubTypingProofRule ( File pLatexFile ,
      File pTPMLFile )
  {
    try
    {
      AbstractSubTypingProofRule rule = new AbstractSubTypingProofRule ( 0 ,
          "SUB-TYPING-RULE" )
      {
        @ Override
        protected void applyInternal ( SubTypingProofContext context ,
            SubTypingProofNode node ) throws Exception
        {
        }
      } ;
      LatexExport.export ( rule , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testType ( File pLatexFile , File pTPMLFile )
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
      LatexExport.export ( type , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testTypeCheckerExpressionProofNode (
      File pLatexFile , File pTPMLFile )
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
      LatexExport.export ( node , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testTypeCheckerProofModel ( File pLatexFile ,
      File pTPMLFile )
  {
    try
    {
      Language language = LanguageFactory.newInstance ( ).getLanguageById (
          "l4" ) ;
      String text = "true || false" ;
      Expression expression = language.newParser ( new StringReader ( text ) )
          .parse ( ) ;
      TypeCheckerProofModel model = language
          .newTypeCheckerProofModel ( expression ) ;
      model.complete ( nextNode ( model ) ) ;
      LatexExport.export ( model , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testTypeCheckerProofRule ( File pLatexFile ,
      File pTPMLFile )
  {
    try
    {
      AbstractTypeCheckerProofRule rule = new AbstractTypeCheckerProofRule ( 0 ,
          "TYPE-CHECKER-RULE" )
      {
        @ Override
        protected void applyInternal ( TypeCheckerProofContext context ,
            TypeCheckerProofNode node ) throws Exception
        {
        }


        @ Override
        protected void updateInternal ( TypeCheckerProofContext context ,
            TypeCheckerProofNode node ) throws Exception
        {
        }
      } ;
      LatexExport.export ( rule , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testTypeCheckerTypeProofNode ( File pLatexFile ,
      File pTPMLFile )
  {
    try
    {
      MonoType type = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      MonoType type2 = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      DefaultTypeCheckerTypeProofNode node = new DefaultTypeCheckerTypeProofNode (
          type , type2 ) ;
      LatexExport.export ( node , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testTypeEnvironment ( File pLatexFile ,
      File pTPMLFile )
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
      LatexExport.export ( environment , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testTypeEquationListTypeChecker ( File pLatexFile ,
      File pTPMLFile )
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
      LatexExport.export ( equationList , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testTypeEquationListTypeInference (
      File pLatexFile , File pTPMLFile )
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
      LatexExport.export ( equationList , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testTypeEquationTypeChecker ( File pLatexFile ,
      File pTPMLFile )
  {
    try
    {
      SeenTypes < TypeEquationTypeChecker > seenTypes = new SeenTypes < TypeEquationTypeChecker > ( ) ;
      TypeEquationTypeChecker typeEquation = new TypeEquationTypeChecker (
          new IntegerType ( ) , new BooleanType ( ) , seenTypes ) ;
      LatexExport.export ( typeEquation , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testTypeEquationTypeInference ( File pLatexFile ,
      File pTPMLFile )
  {
    try
    {
      SeenTypes < TypeEquationTypeInference > seenTypes = new SeenTypes < TypeEquationTypeInference > ( ) ;
      TypeEquationTypeInference typeEquation = new TypeEquationTypeInference (
          new IntegerType ( ) , new BooleanType ( ) , seenTypes ) ;
      LatexExport.export ( typeEquation , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testTypeInferenceProofModel ( File pLatexFile ,
      File pTPMLFile )
  {
    try
    {
      String text = "let rec map f l = if is_empty l then [] else (f (hd l)) :: map f (tl l) in let rec append l1 l2 = if is_empty l1 then l2 else hd l1 :: append (tl l1) l2 in let rec power_set l = if is_empty l then [[]] else let p = power_set (tl l) in append p (map ((::) (hd l)) p) in power_set [1;2]" ;
      text = "let rec fact x = if x = 0 then 1 else if x = 1 then 1 else if x = 2 then 2 else if x = 3 then 6 else x * fact (x-1) in fact 4" ;
      text = "1+1" ;
      LanguageFactory factory = LanguageFactory.newInstance ( ) ;
      Language language = factory.getLanguageById ( "l4" ) ;
      Expression expression = language.newParser ( new StringReader ( text ) )
          .parse ( ) ;
      TypeInferenceProofModel model = language
          .newTypeInferenceProofModel ( expression ) ;
      model.complete ( nextNode ( model ) ) ;
      LatexExport.export ( model , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testTypeInferenceProofNode ( File pLatexFile ,
      File pTPMLFile )
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
      LatexExport.export ( node , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testTypeJudgement ( File pLatexFile , File pTPMLFile )
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
      LatexExport.export ( judgement , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testTypeSubstitution ( File pLatexFile ,
      File pTPMLFile )
  {
    try
    {
      DefaultTypeSubstitution typeSubstitution = new DefaultTypeSubstitution (
          new TypeVariable ( 0 , 0 ) , new BooleanType ( ) ) ;
      LatexExport.export ( typeSubstitution , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testTypeSubstitutionList ( File pLatexFile ,
      File pTPMLFile )
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
      LatexExport.export ( substitionList , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }


  private final static void testTypeSubType ( File pLatexFile , File pTPMLFile )
  {
    try
    {
      TypeSubType typeSubType = new TypeSubType ( new IntegerType ( ) ,
          new BooleanType ( ) ) ;
      LatexExport.export ( typeSubType , pLatexFile , pTPMLFile ,
          LatexExport.Modus.ONE_FILE ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
      System.exit ( 1 ) ;
    }
  }
}
