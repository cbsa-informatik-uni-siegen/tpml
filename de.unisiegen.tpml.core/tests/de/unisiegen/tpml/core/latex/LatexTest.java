package de.unisiegen.tpml.core.latex ;


import java.io.File ;
import java.io.IOException ;
import java.io.StringReader ;
import java.util.ArrayList ;
import java.util.LinkedList ;
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
 * @author Benjamin Mies
 */
@ SuppressWarnings ( value =
{ "all" } )
public class LatexTest
{
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
        return ;
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
        return ;
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
        return ;
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
        return ;
      }
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  public final static void main ( String [ ] args )
  {
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
    }
    File file = new File ( "test.tex" ) ;
    int number = 25 ;
    if ( number == 0 ) testExpression ( file ) ;
    if ( number == 1 ) testType ( file ) ;
    if ( number == 2 ) testTypeEnvironment ( file ) ;
    if ( number == 3 ) testStore ( file ) ;
    if ( number == 4 ) testSeenTypes ( file ) ;
    if ( number == 5 ) testTypeEquationTypeChecker ( file ) ;
    if ( number == 6 ) testTypeEquationTypeInference ( file ) ;
    if ( number == 7 ) testSubType ( file ) ;
    if ( number == 8 ) testTypeSubType ( file ) ;
    if ( number == 9 ) testTypeSubstitution ( file ) ;
    if ( number == 10 ) testTypeEquationListTypeChecker ( file ) ;
    if ( number == 11 ) testTypeEquationListTypeInference ( file ) ;
    if ( number == 12 ) testTypeSubstitutionList ( file ) ;
    if ( number == 13 ) testTypeJudgement ( file ) ;
    if ( number == 14 ) testSmallStepProofNode ( file ) ;
    if ( number == 15 ) testTypeCheckerExpressionProofNode ( file ) ;
    if ( number == 16 ) testTypeCheckerTypeProofNode ( file ) ;
    if ( number == 17 ) testBigStepProofNode ( file ) ;
    if ( number == 18 ) testTypeInferenceProofNode ( file ) ;
    if ( number == 19 ) testSubTypingProofNode ( file ) ;
    if ( number == 20 ) testRecSubTypingProofNode ( file ) ;
    if ( number == 21 ) testMinimalTypingTypesProofNode ( file ) ;
    if ( number == 22 ) testMinimalTypingExpressionProofNode ( file ) ;
    if ( number == 23 ) testBigStepProofModel ( file ) ;
    if ( number == 24 ) testSmallStepProofModel ( file ) ;
    if ( number == 25 ) testTypeInferenceProofModel ( file ) ;
    if ( compile )
    {
      compile ( ) ;
    }
    System.out.println ( "*** finished ***" ) ;
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


  private final static void testBigStepProofModel ( File pFile )
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
      LatexExport.export ( model , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testBigStepProofNode ( File pFile )
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
      LatexExport.export ( node , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testExpression ( File pFile )
  {
    try
    {
      String text = "let x = 0 in 1" ;
      text = "let rec map f l = if is_empty l then [] else (f (hd l)) :: map f (tl l) in let rec append l1 l2 = if is_empty l1 then l2 else hd l1 :: append (tl l1) l2 in let rec power_set l = if is_empty l then [[]] else let p = power_set (tl l) in append p (map ((::) (hd l)) p) in power_set [1;2]" ;
      LanguageFactory factory = LanguageFactory.newInstance ( ) ;
      Language language = factory.getLanguageById ( "l4" ) ;
      Expression expression = language.newParser ( new StringReader ( text ) )
          .parse ( ) ;
      LatexExport.export ( expression , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testMinimalTypingExpressionProofNode ( File pFile )
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
      LatexExport.export ( node , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testMinimalTypingTypesProofNode ( File pFile )
  {
    try
    {
      DefaultMinimalTypingTypesProofNode node = new DefaultMinimalTypingTypesProofNode (
          new IntegerType ( ) , new BooleanType ( ) ) ;
      LatexExport.export ( node , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testRecSubTypingProofNode ( File pFile )
  {
    try
    {
      MonoType type = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      MonoType type2 = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new BooleanType ( ) ) ) ;
      DefaultRecSubTypingProofNode node = new DefaultRecSubTypingProofNode (
          type , type2 , new SeenTypes < DefaultSubType > ( ) ) ;
      LatexExport.export ( node , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testSeenTypes ( File pFile )
  {
    try
    {
      SeenTypes < TypeEquationTypeChecker > seenTypes1 = new SeenTypes < TypeEquationTypeChecker > ( ) ;
      SeenTypes < TypeEquationTypeChecker > seenTypes2 = new SeenTypes < TypeEquationTypeChecker > ( ) ;
      seenTypes1.add ( new TypeEquationTypeChecker ( new IntegerType ( ) ,
          new BooleanType ( ) , seenTypes2 ) ) ;
      seenTypes1.add ( new TypeEquationTypeChecker ( new BooleanType ( ) ,
          new UnitType ( ) , seenTypes2 ) ) ;
      LatexExport.export ( seenTypes1 , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testSmallStepProofModel ( File pFile )
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
      LatexExport.export ( model , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testSmallStepProofNode ( File pFile )
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
      LatexExport.export ( node , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testStore ( File pFile )
  {
    try
    {
      DefaultStore store = new DefaultStore ( ) ;
      store.put ( new Location ( "c" ) , new IntegerConstant ( 3 ) ) ;
      store.put ( new Location ( "b" ) , new IntegerConstant ( 2 ) ) ;
      store.put ( new Location ( "a" ) , new IntegerConstant ( 1 ) ) ;
      LatexExport.export ( store , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testSubType ( File pFile )
  {
    try
    {
      DefaultSubType subType = new DefaultSubType ( new IntegerType ( ) ,
          new BooleanType ( ) ) ;
      LatexExport.export ( subType , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testSubTypingProofNode ( File pFile )
  {
    try
    {
      MonoType type = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      MonoType type2 = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      DefaultSubTypingProofNode node = new DefaultSubTypingProofNode ( type ,
          type2 ) ;
      LatexExport.export ( node , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testType ( File pFile )
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
      LatexExport.export ( type , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testTypeCheckerExpressionProofNode ( File pFile )
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
      LatexExport.export ( node , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testTypeCheckerTypeProofNode ( File pFile )
  {
    try
    {
      MonoType type = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      MonoType type2 = new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
      DefaultTypeCheckerTypeProofNode node = new DefaultTypeCheckerTypeProofNode (
          type , type2 ) ;
      LatexExport.export ( node , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testTypeEnvironment ( File pFile )
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
      LatexExport.export ( environment , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testTypeEquationListTypeChecker ( File pFile )
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
      LatexExport.export ( equationList , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testTypeEquationListTypeInference ( File pFile )
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
      LatexExport.export ( equationList , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testTypeEquationTypeChecker ( File pFile )
  {
    try
    {
      SeenTypes < TypeEquationTypeChecker > seenTypes = new SeenTypes < TypeEquationTypeChecker > ( ) ;
      TypeEquationTypeChecker typeEquation = new TypeEquationTypeChecker (
          new IntegerType ( ) , new BooleanType ( ) , seenTypes ) ;
      LatexExport.export ( typeEquation , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testTypeEquationTypeInference ( File pFile )
  {
    try
    {
      SeenTypes < TypeEquationTypeInference > seenTypes = new SeenTypes < TypeEquationTypeInference > ( ) ;
      TypeEquationTypeInference typeEquation = new TypeEquationTypeInference (
          new IntegerType ( ) , new BooleanType ( ) , seenTypes ) ;
      LatexExport.export ( typeEquation , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testTypeInferenceProofModel ( File pFile )
  {
    try
    {
      String text = "let rec map f l = if is_empty l then [] else (f (hd l)) :: map f (tl l) in let rec append l1 l2 = if is_empty l1 then l2 else hd l1 :: append (tl l1) l2 in let rec power_set l = if is_empty l then [[]] else let p = power_set (tl l) in append p (map ((::) (hd l)) p) in power_set [1;2]" ;
      text = "let rec fact x = if x = 0 then 1 else if x = 1 then 1 else if x = 2 then 2 else if x = 3 then 6 else x * fact (x-1) in fact 4" ;
      LanguageFactory factory = LanguageFactory.newInstance ( ) ;
      Language language = factory.getLanguageById ( "l4" ) ;
      Expression expression = language.newParser ( new StringReader ( text ) )
          .parse ( ) ;
      TypeInferenceProofModel model = language
          .newTypeInferenceProofModel ( expression ) ;
      model.complete ( nextNode ( model ) ) ;
      LatexExport.export ( model , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testTypeInferenceProofNode ( File pFile )
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
      LatexExport.export ( node , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testTypeJudgement ( File pFile )
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
      LatexExport.export ( judgement , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testTypeSubstitution ( File pFile )
  {
    try
    {
      DefaultTypeSubstitution typeSubstitution = new DefaultTypeSubstitution (
          new TypeVariable ( 0 , 0 ) , new BooleanType ( ) ) ;
      LatexExport.export ( typeSubstitution , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testTypeSubstitutionList ( File pFile )
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
      LatexExport.export ( substitionList , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }


  private final static void testTypeSubType ( File pFile )
  {
    try
    {
      TypeSubType typeSubType = new TypeSubType ( new IntegerType ( ) ,
          new BooleanType ( ) ) ;
      LatexExport.export ( typeSubType , pFile ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }
}
