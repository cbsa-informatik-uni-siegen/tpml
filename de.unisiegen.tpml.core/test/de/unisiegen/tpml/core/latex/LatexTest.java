package de.unisiegen.tpml.core.latex;


import java.io.File;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import de.unisiegen.tpml.core.ProofModel;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.bigstep.AbstractBigStepProofRule;
import de.unisiegen.tpml.core.bigstep.BigStepProofContext;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode;
import de.unisiegen.tpml.core.bigstep.BigStepProofResult;
import de.unisiegen.tpml.core.bigstep.DefaultBigStepProofNode;
import de.unisiegen.tpml.core.entities.DefaultTypeEquation;
import de.unisiegen.tpml.core.entities.TypeEquation;
import de.unisiegen.tpml.core.entities.TypeEquationList;
import de.unisiegen.tpml.core.entities.DefaultTypeEquationList;
import de.unisiegen.tpml.core.expressions.ArithmeticOperator;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.InfixOperation;
import de.unisiegen.tpml.core.expressions.IntegerConstant;
import de.unisiegen.tpml.core.expressions.Location;
import de.unisiegen.tpml.core.expressions.Ref;
import de.unisiegen.tpml.core.interpreters.DefaultStore;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageFactory;
import de.unisiegen.tpml.core.minimaltyping.DefaultMinimalTypingExpressionProofNode;
import de.unisiegen.tpml.core.minimaltyping.DefaultMinimalTypingTypesProofNode;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofModel;
import de.unisiegen.tpml.core.smallstep.DefaultSmallStepProofNode;
import de.unisiegen.tpml.core.smallstep.DefaultSmallStepProofRule;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;
import de.unisiegen.tpml.core.subtyping.AbstractSubTypingProofRule;
import de.unisiegen.tpml.core.subtyping.DefaultSubTypingProofNode;
import de.unisiegen.tpml.core.subtyping.SubTypingProofContext;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel;
import de.unisiegen.tpml.core.subtyping.SubTypingProofNode;
import de.unisiegen.tpml.core.subtypingrec.AbstractRecSubTypingProofRule;
import de.unisiegen.tpml.core.subtypingrec.DefaultRecSubTypingProofNode;
import de.unisiegen.tpml.core.subtypingrec.DefaultSubType;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofContext;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofModel;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode;
import de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRule;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerExpressionProofNode;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerTypeProofNode;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.SeenTypes;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.typeinference.DefaultTypeInferenceProofNode;
import de.unisiegen.tpml.core.typeinference.TypeFormula;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel;
import de.unisiegen.tpml.core.typeinference.TypeJudgement;
import de.unisiegen.tpml.core.typeinference.TypeSubType;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.BooleanType;
import de.unisiegen.tpml.core.types.IntegerType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.core.types.TypeVariable;
import de.unisiegen.tpml.core.types.UnitType;


/**
 * A test class for the latex export.
 * 
 * @author Christian Fehler
 * @author Benjamin Mies
 * @version $Id$
 */
@SuppressWarnings ( value =
{ "all" } )
public class LatexTest
{

  private static boolean all = false;


  private static boolean console = true;


  private static boolean compile = true;


  private static boolean oneFile = true;


  private static boolean tpmlExport = false;


  private final static int compile ()
  {
    try
    {
      if ( console )
      {
        System.out.println ( "*** compile ***" );
      }
      Process p;
      // latex 1
      if ( console )
      {
        System.out.println ( "latex 1" );
      }
      p = Runtime.getRuntime ().exec ( "latex -halt-on-error test.tex" );
      try
      {
        p.waitFor ();
      }
      catch ( InterruptedException e )
      {
        e.printStackTrace ();
        return 1;
      }
      if ( p.exitValue () != 0 )
      {
        System.err.println ( "LatexTest: latex 1 error" );
        return 1;
      }
      // latex 2
      if ( console )
      {
        System.out.println ( "latex 2" );
      }
      p = Runtime.getRuntime ().exec ( "latex -halt-on-error test.tex" );
      try
      {
        p.waitFor ();
      }
      catch ( InterruptedException e )
      {
        e.printStackTrace ();
        return 1;
      }
      if ( p.exitValue () != 0 )
      {
        System.err.println ( "LatexTest: latex 2 error" );
        return 1;
      }
      // dvips
      if ( console )
      {
        System.out.println ( "dvips" );
      }
      p = Runtime.getRuntime ().exec ( "dvips test.dvi" );
      try
      {
        p.waitFor ();
      }
      catch ( InterruptedException e )
      {
        e.printStackTrace ();
        return 1;
      }
      if ( p.exitValue () != 0 )
      {
        System.err.println ( "LatexTest: dvips error" );
        return 1;
      }
      // ps2pdf
      if ( console )
      {
        System.out.println ( "ps2pdf" );
      }
      p = Runtime.getRuntime ().exec ( "ps2pdf test.ps" );
      try
      {
        p.waitFor ();
      }
      catch ( InterruptedException e )
      {
        e.printStackTrace ();
        return 1;
      }
      if ( p.exitValue () != 0 )
      {
        System.err.println ( "LatexTest: ps2pdf error" );
        return 1;
      }
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      return 1;
    }
    return 0;
  }


  private final static void latexExportTPML ( File pTPMLFile )
  {
    try
    {
      LatexExport.exportTPML ( pTPMLFile );
      System.out.println ( "*** finished ***" );
      System.exit ( 0 );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  public final static void main ( String [] args )
  {
    System.out.println ( "*** started ***" );
    for ( String arg : args )
    {
      if ( arg.equals ( "-no-console" ) )
      {
        console = false;
      }
      if ( arg.equals ( "-no-compile" ) )
      {
        compile = false;
      }
      if ( arg.equals ( "-all" ) )
      {
        all = true;
      }
      if ( arg.equals ( "-input" ) )
      {
        oneFile = false;
      }
      if ( arg.equals ( "-tpml-export" ) )
      {
        tpmlExport = true;
      }
    }
    if ( tpmlExport )
    {
      File tpml = new File ( new File ( "" ).getAbsolutePath () + "/" );
      latexExportTPML ( tpml );
    }
    else if ( !all )
    {
      File file = new File ( "test.tex" );
      int number = 17;
      // Expression, Type, Environment
      if ( number == 00 )
        testExpression ( file );
      if ( number == 01 )
        testType ( file );
      if ( number == 02 )
        testTypeEnvironment ( file );
      if ( number == 03 )
        testStore ( file );
      // TypeChecker
      if ( number == 10 )
        testTypeCheckerProofRule ( file );
      if ( number == 11 )
        testSeenTypes ( file );
      if ( number == 12 )
        testTypeSubstitution ( file );
      if ( number == 13 )
        testTypeEquationTypeChecker ( file );
      if ( number == 14 )
        testTypeEquationListTypeChecker ( file );
      if ( number == 15 )
        testTypeCheckerExpressionProofNode ( file );
      if ( number == 16 )
        testTypeCheckerTypeProofNode ( file );
      if ( number == 17 )
        testTypeCheckerProofModel ( file );
      // TypeInference
      if ( number == 20 )
        testTypeEquationTypeInference ( file );
      if ( number == 21 )
        testTypeEquationListTypeInference ( file );
      if ( number == 22 )
        testTypeSubstitutionList ( file );
      if ( number == 23 )
        testTypeJudgement ( file );
      if ( number == 24 )
        testTypeSubType ( file );
      if ( number == 25 )
        testTypeInferenceProofNode ( file );
      if ( number == 26 )
        testTypeInferenceProofModel ( file );
      // SmallStep
      if ( number == 30 )
        testSmallStepProofRule ( file );
      if ( number == 31 )
        testSmallStepProofNode ( file );
      if ( number == 32 )
        testSmallStepProofModel ( file );
      // BigStep
      if ( number == 40 )
        testBigStepProofRule ( file );
      if ( number == 41 )
        testBigStepResult ( file );
      if ( number == 42 )
        testBigStepProofNode ( file );
      if ( number == 43 )
        testBigStepProofModel ( file );
      // MinimalTyping
      if ( number == 50 )
        testMinimalTypingProofRule ( file );
      if ( number == 51 )
        testMinimalTypingExpressionProofNode ( file );
      if ( number == 52 )
        testMinimalTypingTypesProofNode ( file );
      if ( number == 53 )
        testMinimalTypingProofModel ( file );
      // SubTyping
      if ( number == 60 )
        testSubTypingProofRule ( file );
      if ( number == 61 )
        testSubTypingProofNode ( file );
      if ( number == 62 )
        testSubTypingProofModel ( file );
      // RecSubTyping
      if ( number == 70 )
        testRecSubTypingProofRule ( file );
      if ( number == 71 )
        testSubType ( file );
      if ( number == 72 )
        testRecSubTypingProofNode ( file );
      if ( number == 73 )
        testRecSubTypingProofModel ( file );
      if ( compile )
      {
        if ( compile () != 0 )
        {
          System.exit ( 1 );
        }
      }
    }
    else
    {
      File test = new File ( "test.tex" );
      console = false;
      boolean okayAll = true;
      boolean okayCurrent = true;
      ArrayList < java.lang.reflect.Method > list = new ArrayList < java.lang.reflect.Method > ();
      for ( java.lang.reflect.Method method : LatexTest.class
          .getDeclaredMethods () )
      {
        list.add ( method );
      }
      Collections.sort ( list, new Comparator < java.lang.reflect.Method > ()
      {

        public int compare ( java.lang.reflect.Method pMethod1,
            java.lang.reflect.Method pMethod2 )
        {
          return pMethod1.getName ().compareTo ( pMethod2.getName () );
        }
      } );
      for ( java.lang.reflect.Method method : list )
      {
        if ( method.getName ().startsWith ( "test" ) )
        {
          try
          {
            okayCurrent = true;
            method.invoke ( null, new Object []
            { test } );
            if ( compile () != 0 )
            {
              okayAll = false;
              okayCurrent = false;
            }
            if ( okayCurrent )
            {
              System.out.println ( method.getName () + ": success" );
            }
            else
            {
              System.err.println ( method.getName () + ": failed" );
            }
          }
          catch ( IllegalArgumentException e )
          {
            e.printStackTrace ();
            System.exit ( 1 );
          }
          catch ( IllegalAccessException e )
          {
            e.printStackTrace ();
            System.exit ( 1 );
          }
          catch ( InvocationTargetException e )
          {
            e.printStackTrace ();
            System.exit ( 1 );
          }
        }
      }
      if ( okayAll )
      {
        System.out.println ( "=> no problems found" );
      }
      else
      {
        System.err.println ( "=> problems found" );
      }
    }
    System.out.println ( "*** finished***" );
  }


  private final static ProofNode nextNode ( ProofModel model )
  {
    LinkedList < ProofNode > nodes = new LinkedList < ProofNode > ();
    nodes.add ( model.getRoot () );
    while ( !nodes.isEmpty () )
    {
      ProofNode node = nodes.poll ();
      if ( node.getRules ().length == 0 )
      {
        return node;
      }
      for ( int i = 0 ; i < node.getChildCount () ; i++ )
      {
        nodes.add ( node.getChildAt ( i ) );
      }
    }
    throw new IllegalStateException ( "unable to find next node" );
  }


  private final static void testBigStepProofModel ( File pLatexFile )
  {
    try
    {
      Language language = LanguageFactory.newInstance ()
          .getLanguageById ( "l4" );
      String text = "true || false";
      text = "let rec map f l = if is_empty l then [] else (f (hd l)) :: map f (tl l) in let rec append l1 l2 = if is_empty l1 then l2 else hd l1 :: append (tl l1) l2 in let rec power_set l = if is_empty l then [[]] else let p = power_set (tl l) in append p (map ((::) (hd l)) p) in power_set [1;2]";
      text = "let rec fact x = if x = 0 then 1 else if x = 1 then 1 else if x = 2 then 2 else if x = 3 then 6 else x * fact (x-1) in fact 4";
      // text = " let x: int = let x : int = let x : int ="
      // + "3+2+4+5+6+7+8+9+11+2+3+4 in x+2 in x+3 in x+5" ;
      Expression expression = language.newParser ( new StringReader ( text ) )
          .parse ();
      BigStepProofModel model = language.newBigStepProofModel ( expression );
      model.complete ( nextNode ( model ) );
      LatexExport.export ( model, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testBigStepProofNode ( File pLatexFile )
  {
    try
    {
      Expression expression = new InfixOperation ( ArithmeticOperator
          .newPlus (), new Identifier ( "a", Identifier.Set.VARIABLE ),
          new Ref () );
      DefaultStore store1 = new DefaultStore ();
      store1.put ( new Location ( "f" ), new IntegerConstant ( 6 ) );
      store1.put ( new Location ( "e" ), new IntegerConstant ( 5 ) );
      store1.put ( new Location ( "d" ), new IntegerConstant ( 4 ) );
      DefaultBigStepProofNode node = new DefaultBigStepProofNode ( expression,
          store1 );
      // with result
      DefaultStore store2 = new DefaultStore ();
      store2.put ( new Location ( "c" ), new IntegerConstant ( 3 ) );
      store2.put ( new Location ( "b" ), new IntegerConstant ( 2 ) );
      store2.put ( new Location ( "a" ), new IntegerConstant ( 1 ) );
      BigStepProofResult result = new BigStepProofResult ( store2, new Ref () );
      node.setResult ( result );
      LatexExport.export ( node, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testBigStepProofRule ( File pLatexFile )
  {
    try
    {
      AbstractBigStepProofRule rule = new AbstractBigStepProofRule ( 0,
          "BIG-STEP-RULE" )
      {

        @Override
        protected void applyInternal ( BigStepProofContext context,
            BigStepProofNode node ) throws Exception
        {
        }


        @Override
        protected void updateInternal ( BigStepProofContext context,
            BigStepProofNode node ) throws Exception
        {
        }
      };
      LatexExport.export ( rule, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testBigStepResult ( File pLatexFile )
  {
    try
    {
      DefaultStore store = new DefaultStore ();
      store.put ( new Location ( "X" ), new IntegerConstant ( 3 ) );
      store.put ( new Location ( "Y" ), new IntegerConstant ( 2 ) );
      store.put ( new Location ( "Z" ), new IntegerConstant ( 1 ) );
      BigStepProofResult result = new BigStepProofResult ( store, new Ref () );
      LatexExport.export ( result, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testExpression ( File pLatexFile )
  {
    try
    {
      String text = "let rec map f l = if is_empty l then [] else (f (hd l)) :: map f (tl l) in let rec append l1 l2 = if is_empty l1 then l2 else hd l1 :: append (tl l1) l2 in let rec power_set l = if is_empty l then [[]] else let p = power_set (tl l) in append p (map ((::) (hd l)) p) in power_set [1;2]";
      text = "let x = 0 in 1";
      LanguageFactory factory = LanguageFactory.newInstance ();
      Language language = factory.getLanguageById ( "l4" );
      Expression expression = language.newParser ( new StringReader ( text ) )
          .parse ();
      LatexExport.export ( expression, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testMinimalTypingExpressionProofNode (
      File pLatexFile )
  {
    try
    {
      DefaultTypeEnvironment environment = new DefaultTypeEnvironment ();
      environment = ( DefaultTypeEnvironment ) environment.extend (
          new Identifier ( "b", Identifier.Set.VARIABLE ), new BooleanType () );
      environment = ( DefaultTypeEnvironment ) environment.extend (
          new Identifier ( "a", Identifier.Set.VARIABLE ), new UnitType () );
      Expression expression = new InfixOperation ( ArithmeticOperator
          .newPlus (), new Identifier ( "a", Identifier.Set.VARIABLE ),
          new Identifier ( "b", Identifier.Set.VARIABLE ) );
      MonoType type = new ArrowType ( new IntegerType (), new ArrowType (
          new IntegerType (), new IntegerType () ) );
      DefaultMinimalTypingExpressionProofNode node = new DefaultMinimalTypingExpressionProofNode (
          environment, expression );
      node.setType ( type );
      LatexExport.export ( node, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testMinimalTypingProofModel ( File pLatexFile )
  {
    try
    {
      Language language = LanguageFactory.newInstance ()
          .getLanguageById ( "l4" );
      String text = "true || false";
      Expression expression = language.newParser ( new StringReader ( text ) )
          .parse ();
      MinimalTypingProofModel model = language.newMinimalTypingProofModel (
          expression, false );
      model.complete ( nextNode ( model ) );
      LatexExport.export ( model, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testMinimalTypingProofRule ( File pLatexFile )
  {
    try
    {
      AbstractBigStepProofRule rule = new AbstractBigStepProofRule ( 0,
          "BIG-STEP-RULE" )
      {

        @Override
        protected void applyInternal ( BigStepProofContext context,
            BigStepProofNode node ) throws Exception
        {
        }


        @Override
        protected void updateInternal ( BigStepProofContext context,
            BigStepProofNode node ) throws Exception
        {
        }
      };
      LatexExport.export ( rule, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testMinimalTypingTypesProofNode ( File pLatexFile )
  {
    try
    {
      DefaultMinimalTypingTypesProofNode node = new DefaultMinimalTypingTypesProofNode (
          new IntegerType (), new BooleanType () );
      LatexExport.export ( node, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testRecSubTypingProofModel ( File pLatexFile )
  {
    try
    {
      Language language = LanguageFactory.newInstance ().getLanguageById (
          "l2sub" );
      MonoType type = new ArrowType ( new IntegerType (), new ArrowType (
          new IntegerType (), new IntegerType () ) );
      MonoType type2 = new ArrowType ( new IntegerType (), new ArrowType (
          new IntegerType (), new IntegerType () ) );
      RecSubTypingProofModel model = language.newRecSubTypingProofModel ( type,
          type2, false );
      model.complete ( nextNode ( model ) );
      LatexExport.export ( model, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testRecSubTypingProofNode ( File pLatexFile )
  {
    try
    {
      MonoType type = new ArrowType ( new IntegerType (), new ArrowType (
          new IntegerType (), new IntegerType () ) );
      MonoType type2 = new ArrowType ( new IntegerType (), new ArrowType (
          new IntegerType (), new BooleanType () ) );
      DefaultRecSubTypingProofNode node = new DefaultRecSubTypingProofNode (
          type, type2, new SeenTypes < DefaultSubType > () );
      LatexExport.export ( node, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testRecSubTypingProofRule ( File pLatexFile )
  {
    try
    {
      AbstractRecSubTypingProofRule rule = new AbstractRecSubTypingProofRule (
          0, "REC-SUB-TYPING-RULE" )
      {

        @Override
        protected void applyInternal ( RecSubTypingProofContext context,
            RecSubTypingProofNode node ) throws Exception
        {
        }
      };
      LatexExport.export ( rule, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testSeenTypes ( File pLatexFile )
  {
    try
    {
      SeenTypes < TypeEquation > seenTypes1 = new SeenTypes < TypeEquation > ();
      SeenTypes < TypeEquation > seenTypes2 = new SeenTypes < TypeEquation > ();
      seenTypes1.add ( new DefaultTypeEquation ( new IntegerType (),
          new BooleanType (), seenTypes2 ) );
      seenTypes1.add ( new DefaultTypeEquation ( new BooleanType (),
          new UnitType (), seenTypes2 ) );
      LatexExport.export ( seenTypes1, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testSmallStepProofModel ( File pLatexFile )
  {
    try
    {
      String text = "let x = 1 in x";
      text = "let rec map f l = if is_empty l then [] else (f (hd l)) :: map f (tl l) in let rec append l1 l2 = if is_empty l1 then l2 else hd l1 :: append (tl l1) l2 in let rec power_set l = if is_empty l then [[]] else let p = power_set (tl l) in append p (map ((::) (hd l)) p) in power_set [1;2]";
      text = "let rec f x = if x = 0 then 1 else x * f (x-1) in f 2";
      LanguageFactory factory = LanguageFactory.newInstance ();
      Language language = factory.getLanguageById ( "l4" );
      Expression expression = language.newParser ( new StringReader ( text ) )
          .parse ();
      SmallStepProofModel model = language.newSmallStepProofModel ( expression );
      model.complete ( nextNode ( model ) );
      LatexExport.export ( model, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testSmallStepProofNode ( File pLatexFile )
  {
    try
    {
      DefaultStore store = new DefaultStore ();
      store.put ( new Location ( "c" ), new IntegerConstant ( 3 ) );
      store.put ( new Location ( "b" ), new IntegerConstant ( 2 ) );
      store.put ( new Location ( "a" ), new IntegerConstant ( 1 ) );
      Expression expression = new InfixOperation ( ArithmeticOperator
          .newPlus (), new IntegerConstant ( 1 ), new InfixOperation (
          ArithmeticOperator.newPlus (), new IntegerConstant ( 2 ),
          new IntegerConstant ( 3 ) ) );
      DefaultSmallStepProofNode node = new DefaultSmallStepProofNode (
          expression );
      LatexExport.export ( node, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testSmallStepProofRule ( File pLatexFile )
  {
    try
    {
      DefaultSmallStepProofRule rule = new DefaultSmallStepProofRule ( 0,
          "SMALL-STEP-RULE", true );
      LatexExport.export ( rule, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testStore ( File pLatexFile )
  {
    try
    {
      DefaultStore store = new DefaultStore ();
      store.put ( new Location ( "c" ), new IntegerConstant ( 3 ) );
      store.put ( new Location ( "b" ), new IntegerConstant ( 2 ) );
      store.put ( new Location ( "a" ), new IntegerConstant ( 1 ) );
      LatexExport.export ( store, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testSubType ( File pLatexFile )
  {
    try
    {
      DefaultSubType subType = new DefaultSubType ( new IntegerType (),
          new BooleanType () );
      LatexExport.export ( subType, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testSubTypingProofModel ( File pLatexFile )
  {
    try
    {
      Language language = LanguageFactory.newInstance ().getLanguageById (
          "l2sub" );
      MonoType type = new ArrowType ( new IntegerType (), new ArrowType (
          new IntegerType (), new IntegerType () ) );
      MonoType type2 = new ArrowType ( new IntegerType (), new ArrowType (
          new IntegerType (), new IntegerType () ) );
      SubTypingProofModel model = language.newSubTypingProofModel ( type,
          type2, false );
      model.complete ( nextNode ( model ) );
      LatexExport.export ( model, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testSubTypingProofNode ( File pLatexFile )
  {
    try
    {
      MonoType type = new ArrowType ( new IntegerType (), new ArrowType (
          new IntegerType (), new IntegerType () ) );
      MonoType type2 = new ArrowType ( new IntegerType (), new ArrowType (
          new IntegerType (), new IntegerType () ) );
      DefaultSubTypingProofNode node = new DefaultSubTypingProofNode ( type,
          type2 );
      LatexExport.export ( node, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testSubTypingProofRule ( File pLatexFile )
  {
    try
    {
      AbstractSubTypingProofRule rule = new AbstractSubTypingProofRule ( 0,
          "SUB-TYPING-RULE" )
      {

        @Override
        protected void applyInternal ( SubTypingProofContext context,
            SubTypingProofNode node ) throws Exception
        {
        }
      };
      LatexExport.export ( rule, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testType ( File pLatexFile )
  {
    try
    {
      String text = "int * bool * unit";
      LanguageFactory factory = LanguageFactory.newInstance ();
      Language language = factory.getLanguageById ( "l4" );
      Type type = language.newTypeParser ( new StringReader ( text ) ).parse ();
      // PolyType
      /*
       * TreeSet < TypeVariable > quantified = new TreeSet < TypeVariable > ( ) ;
       * quantified.add ( new TypeVariable ( 0 , 0 ) ) ; quantified.add ( new
       * TypeVariable ( 0 , 1 ) ) ; quantified.add ( new TypeVariable ( 0 , 2 ) ) ;
       * type = new PolyType ( quantified , new IntegerType ( ) ) ;
       */
      LatexExport.export ( type, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testTypeCheckerExpressionProofNode ( File pLatexFile )
  {
    try
    {
      DefaultTypeEnvironment environment = new DefaultTypeEnvironment ();
      environment = ( DefaultTypeEnvironment ) environment.extend (
          new Identifier ( "b", Identifier.Set.VARIABLE ), new BooleanType () );
      environment = ( DefaultTypeEnvironment ) environment.extend (
          new Identifier ( "a", Identifier.Set.VARIABLE ), new UnitType () );
      Expression expression = new InfixOperation ( ArithmeticOperator
          .newPlus (), new Identifier ( "a", Identifier.Set.VARIABLE ),
          new Identifier ( "b", Identifier.Set.VARIABLE ) );
      MonoType type = new ArrowType ( new IntegerType (), new ArrowType (
          new IntegerType (), new IntegerType () ) );
      DefaultTypeCheckerExpressionProofNode node = new DefaultTypeCheckerExpressionProofNode (
          environment, expression, type );
      LatexExport.export ( node, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testTypeCheckerProofModel ( File pLatexFile )
  {
    try
    {
      Language language = LanguageFactory.newInstance ()
          .getLanguageById ( "l4" );
      String text = "true || false";
      Expression expression = language.newParser ( new StringReader ( text ) )
          .parse ();
      TypeCheckerProofModel model = language
          .newTypeCheckerProofModel ( expression );
      model.complete ( nextNode ( model ) );
      LatexExport.export ( model, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testTypeCheckerProofRule ( File pLatexFile )
  {
    try
    {
      AbstractTypeCheckerProofRule rule = new AbstractTypeCheckerProofRule ( 0,
          "TYPE-CHECKER-RULE" )
      {

        @Override
        protected void applyInternal ( TypeCheckerProofContext context,
            TypeCheckerProofNode node ) throws Exception
        {
        }


        @Override
        protected void updateInternal ( TypeCheckerProofContext context,
            TypeCheckerProofNode node ) throws Exception
        {
        }
      };
      LatexExport.export ( rule, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testTypeCheckerTypeProofNode ( File pLatexFile )
  {
    try
    {
      MonoType type = new ArrowType ( new IntegerType (), new ArrowType (
          new IntegerType (), new IntegerType () ) );
      MonoType type2 = new ArrowType ( new IntegerType (), new ArrowType (
          new IntegerType (), new IntegerType () ) );
      DefaultTypeCheckerTypeProofNode node = new DefaultTypeCheckerTypeProofNode (
          type, type2 );
      LatexExport.export ( node, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testTypeEnvironment ( File pLatexFile )
  {
    try
    {
      DefaultTypeEnvironment environment = new DefaultTypeEnvironment ();
      environment = ( DefaultTypeEnvironment ) environment.extend (
          new Identifier ( "c", Identifier.Set.VARIABLE ), new IntegerType () );
      environment = ( DefaultTypeEnvironment ) environment.extend (
          new Identifier ( "b", Identifier.Set.VARIABLE ), new BooleanType () );
      environment = ( DefaultTypeEnvironment ) environment.extend (
          new Identifier ( "a", Identifier.Set.VARIABLE ), new UnitType () );
      LatexExport.export ( environment, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testTypeEquationListTypeChecker ( File pLatexFile )
  {
    try
    {
      TypeEquationList equationList = DefaultTypeEquationList.EMPTY_LIST;
      SeenTypes < TypeEquation > seenTypes = new SeenTypes < TypeEquation > ();
      TypeEquation typeEquation1 = new DefaultTypeEquation (
          new IntegerType (), new BooleanType (), seenTypes );
      TypeEquation typeEquation2 = new DefaultTypeEquation (
          new BooleanType (), new UnitType (), seenTypes );
      equationList = equationList.extend ( typeEquation1 );
      equationList = equationList.extend ( typeEquation2 );
      LatexExport.export ( equationList, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testTypeEquationListTypeInference ( File pLatexFile )
  {
    try
    {
      TypeEquationList equationList = DefaultTypeEquationList.EMPTY_LIST;
      SeenTypes < TypeEquation > seenTypes = new SeenTypes < TypeEquation > ();
      TypeEquation typeEquation1 = new DefaultTypeEquation (
          new IntegerType (), new BooleanType (), seenTypes );
      TypeEquation typeEquation2 = new DefaultTypeEquation (
          new BooleanType (), new UnitType (), seenTypes );
      equationList = equationList.extend ( typeEquation1 );
      equationList = equationList.extend ( typeEquation2 );
      LatexExport.export ( equationList, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testTypeEquationTypeChecker ( File pLatexFile )
  {
    try
    {
      SeenTypes < TypeEquation > seenTypes = new SeenTypes < TypeEquation > ();
      TypeEquation typeEquation = new DefaultTypeEquation ( new IntegerType (),
          new BooleanType (), seenTypes );
      LatexExport.export ( typeEquation, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testTypeEquationTypeInference ( File pLatexFile )
  {
    try
    {
      SeenTypes < TypeEquation > seenTypes = new SeenTypes < TypeEquation > ();
      TypeEquation typeEquation = new DefaultTypeEquation ( new IntegerType (),
          new BooleanType (), seenTypes );
      LatexExport.export ( typeEquation, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testTypeInferenceProofModel ( File pLatexFile )
  {
    try
    {
      String text = "let rec map f l = if is_empty l then [] else (f (hd l)) :: map f (tl l) in let rec append l1 l2 = if is_empty l1 then l2 else hd l1 :: append (tl l1) l2 in let rec power_set l = if is_empty l then [[]] else let p = power_set (tl l) in append p (map ((::) (hd l)) p) in power_set [1;2]";
      text = "let rec fact x = if x = 0 then 1 else if x = 1 then 1 else if x = 2 then 2 else if x = 3 then 6 else x * fact (x-1) in fact 4";
      text = "1+1";
      LanguageFactory factory = LanguageFactory.newInstance ();
      Language language = factory.getLanguageById ( "l4" );
      Expression expression = language.newParser ( new StringReader ( text ) )
          .parse ();
      TypeInferenceProofModel model = language
          .newTypeInferenceProofModel ( expression );
      model.complete ( nextNode ( model ) );
      LatexExport.export ( model, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testTypeInferenceProofNode ( File pLatexFile )
  {
    try
    {
      SeenTypes < TypeEquation > seenTypes = new SeenTypes < TypeEquation > ();
      SeenTypes < TypeEquation > seenTypes2 = new SeenTypes < TypeEquation > ();
      seenTypes.add ( new DefaultTypeEquation ( new TypeVariable ( 0, 0 ),
          new BooleanType (), seenTypes2 ) );
      TypeEquation typeEquation1 = new DefaultTypeEquation ( new TypeVariable (
          0, 0 ), new BooleanType (), seenTypes );
      TypeEquation typeEquation2 = new DefaultTypeEquation ( new TypeVariable (
          0, 0 ), new TypeVariable ( 0, 1 ), seenTypes );
      TypeEquation typeEquation3 = new DefaultTypeEquation (
          new BooleanType (), new BooleanType (), seenTypes );
      TypeSubType typeSubType = new TypeSubType ( new IntegerType (),
          new BooleanType () );
      ArrayList < TypeFormula > formulas = new ArrayList < TypeFormula > ();
      DefaultTypeEnvironment environment = new DefaultTypeEnvironment ();
      environment = ( DefaultTypeEnvironment ) environment.extend (
          new Identifier ( "b", Identifier.Set.VARIABLE ), new BooleanType () );
      environment = ( DefaultTypeEnvironment ) environment.extend (
          new Identifier ( "a", Identifier.Set.VARIABLE ), new UnitType () );
      Expression expression = new InfixOperation ( ArithmeticOperator
          .newPlus (), new Identifier ( "a", Identifier.Set.VARIABLE ),
          new Identifier ( "b", Identifier.Set.VARIABLE ) );
      MonoType type = new ArrowType ( new IntegerType (), new ArrowType (
          new IntegerType (), new IntegerType () ) );
      TypeJudgement judgement = new TypeJudgement ( environment, expression,
          type );
      formulas.add ( typeEquation1 );
      formulas.add ( typeEquation2 );
      formulas.add ( typeEquation3 );
      formulas.add ( typeSubType );
      formulas.add ( judgement );
      DefaultTypeSubstitution typeSubstitution1 = new DefaultTypeSubstitution (
          new TypeVariable ( 0, 0 ), new BooleanType () );
      DefaultTypeSubstitution typeSubstitution2 = new DefaultTypeSubstitution (
          new TypeVariable ( 0, 1 ), new BooleanType () );
      DefaultTypeSubstitution typeSubstitution3 = new DefaultTypeSubstitution (
          new TypeVariable ( 0, 2 ), new BooleanType () );
      ArrayList < TypeSubstitution > substitutions = new ArrayList < TypeSubstitution > ();
      substitutions.add ( typeSubstitution1 );
      substitutions.add ( typeSubstitution2 );
      substitutions.add ( typeSubstitution3 );
      DefaultTypeInferenceProofNode node = new DefaultTypeInferenceProofNode (
          formulas, substitutions );
      LatexExport.export ( node, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testTypeJudgement ( File pLatexFile )
  {
    try
    {
      DefaultTypeEnvironment environment = new DefaultTypeEnvironment ();
      environment = ( DefaultTypeEnvironment ) environment.extend (
          new Identifier ( "b", Identifier.Set.VARIABLE ), new BooleanType () );
      environment = ( DefaultTypeEnvironment ) environment.extend (
          new Identifier ( "a", Identifier.Set.VARIABLE ), new UnitType () );
      Expression expression = new InfixOperation ( ArithmeticOperator
          .newPlus (), new Identifier ( "a", Identifier.Set.VARIABLE ),
          new Identifier ( "b", Identifier.Set.VARIABLE ) );
      MonoType type = new ArrowType ( new IntegerType (), new ArrowType (
          new IntegerType (), new IntegerType () ) );
      TypeJudgement judgement = new TypeJudgement ( environment, expression,
          type );
      LatexExport.export ( judgement, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testTypeSubstitution ( File pLatexFile )
  {
    try
    {
      DefaultTypeSubstitution typeSubstitution = new DefaultTypeSubstitution (
          new TypeVariable ( 0, 0 ), new BooleanType () );
      LatexExport.export ( typeSubstitution, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testTypeSubstitutionList ( File pLatexFile )
  {
    try
    {
      TypeSubstitutionList substitionList = TypeSubstitutionList.EMPTY_LIST;
      DefaultTypeSubstitution typeSubstitution1 = new DefaultTypeSubstitution (
          new TypeVariable ( 0, 0 ), new BooleanType () );
      DefaultTypeSubstitution typeSubstitution2 = new DefaultTypeSubstitution (
          new TypeVariable ( 0, 1 ), new IntegerType () );
      substitionList = substitionList.extend ( typeSubstitution1 );
      substitionList = substitionList.extend ( typeSubstitution2 );
      LatexExport.export ( substitionList, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }


  private final static void testTypeSubType ( File pLatexFile )
  {
    try
    {
      TypeSubType typeSubType = new TypeSubType ( new IntegerType (),
          new BooleanType () );
      LatexExport.export ( typeSubType, pLatexFile, oneFile );
    }
    catch ( Exception e )
    {
      e.printStackTrace ();
      System.exit ( 1 );
    }
  }
}
