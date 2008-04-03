package de.unisiegen.tpml.core.latex;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.ProofModel;
import de.unisiegen.tpml.core.bigstep.AbstractBigStepProofRule;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel;
import de.unisiegen.tpml.core.bigstep.BigStepProofResult;
import de.unisiegen.tpml.core.bigstep.DefaultBigStepProofNode;
import de.unisiegen.tpml.core.entities.DefaultTypeEquation;
import de.unisiegen.tpml.core.entities.DefaultTypeEquationList;
import de.unisiegen.tpml.core.expressions.And;
import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Attribute;
import de.unisiegen.tpml.core.expressions.BinaryOperator;
import de.unisiegen.tpml.core.expressions.Class;
import de.unisiegen.tpml.core.expressions.Coercion;
import de.unisiegen.tpml.core.expressions.Condition;
import de.unisiegen.tpml.core.expressions.Condition1;
import de.unisiegen.tpml.core.expressions.Constant;
import de.unisiegen.tpml.core.expressions.CurriedLet;
import de.unisiegen.tpml.core.expressions.CurriedLetRec;
import de.unisiegen.tpml.core.expressions.CurriedMethod;
import de.unisiegen.tpml.core.expressions.Duplication;
import de.unisiegen.tpml.core.expressions.Exn;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.InfixOperation;
import de.unisiegen.tpml.core.expressions.Inherit;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.expressions.LetRec;
import de.unisiegen.tpml.core.expressions.List;
import de.unisiegen.tpml.core.expressions.Location;
import de.unisiegen.tpml.core.expressions.Method;
import de.unisiegen.tpml.core.expressions.MultiLambda;
import de.unisiegen.tpml.core.expressions.MultiLet;
import de.unisiegen.tpml.core.expressions.New;
import de.unisiegen.tpml.core.expressions.ObjectExpr;
import de.unisiegen.tpml.core.expressions.Or;
import de.unisiegen.tpml.core.expressions.Recursion;
import de.unisiegen.tpml.core.expressions.Row;
import de.unisiegen.tpml.core.expressions.Send;
import de.unisiegen.tpml.core.expressions.Sequence;
import de.unisiegen.tpml.core.expressions.Tuple;
import de.unisiegen.tpml.core.expressions.While;
import de.unisiegen.tpml.core.interpreters.DefaultStore;
import de.unisiegen.tpml.core.minimaltyping.AbstractMinimalTypingProofRule;
import de.unisiegen.tpml.core.minimaltyping.DefaultMinimalTypingExpressionProofNode;
import de.unisiegen.tpml.core.minimaltyping.DefaultMinimalTypingTypesProofNode;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofModel;
import de.unisiegen.tpml.core.smallstep.DefaultSmallStepProofNode;
import de.unisiegen.tpml.core.smallstep.DefaultSmallStepProofRule;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;
import de.unisiegen.tpml.core.subtyping.AbstractSubTypingProofRule;
import de.unisiegen.tpml.core.subtyping.DefaultSubTypingProofNode;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel;
import de.unisiegen.tpml.core.subtypingrec.AbstractRecSubTypingProofRule;
import de.unisiegen.tpml.core.subtypingrec.DefaultRecSubTypingProofNode;
import de.unisiegen.tpml.core.subtypingrec.DefaultSubType;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofModel;
import de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRule;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerExpressionProofNode;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerTypeProofNode;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.SeenTypes;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typeinference.DefaultTypeInferenceProofNode;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel;
import de.unisiegen.tpml.core.typeinference.TypeJudgement;
import de.unisiegen.tpml.core.typeinference.TypeSubType;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.BooleanType;
import de.unisiegen.tpml.core.types.ClassType;
import de.unisiegen.tpml.core.types.IntegerType;
import de.unisiegen.tpml.core.types.ListType;
import de.unisiegen.tpml.core.types.ObjectType;
import de.unisiegen.tpml.core.types.PolyType;
import de.unisiegen.tpml.core.types.RecType;
import de.unisiegen.tpml.core.types.RefType;
import de.unisiegen.tpml.core.types.RowType;
import de.unisiegen.tpml.core.types.TupleType;
import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.core.types.TypeName;
import de.unisiegen.tpml.core.types.TypeVariable;
import de.unisiegen.tpml.core.types.UnifyType;
import de.unisiegen.tpml.core.types.UnitType;


/**
 * The main class of the latex export. The export method writes the input
 * {@link LatexPrintable} to the given {@link File}.
 * 
 * @author Christian Fehler
 * @version $Id$
 */
public abstract class LatexExport implements LatexCommandNames
{

  /**
   * The name of a supported {@link Charset}.
   */
  private static final String CHARSET_NAME = "UTF8"; //$NON-NLS-1$


  /**
   * Writes the input {@link LatexPrintable} to the given {@link File}.
   * 
   * @param pLatexPrintable The input {@link LatexPrintable}.
   * @param pLatexFile The latex {@link File}.
   * @param pOneFile If true, only one file is exported, otherwise the tpml file
   *          is used as an import.
   * @param sourceCode Flag that indicates if the source code should be printed.
   * @throws LatexException If something in the latex export does not work.
   */
  public final static void export ( LatexPrintable pLatexPrintable,
      File pLatexFile, boolean pOneFile, boolean sourceCode )
      throws LatexException
  {
    if ( pLatexPrintable == null )
    {
      throw new NullPointerException ( "latex printable is null" ); //$NON-NLS-1$
    }
    if ( pLatexFile == null )
    {
      throw new NullPointerException ( "latex file is null" ); //$NON-NLS-1$
    }
    if ( pLatexFile.isDirectory () )
    {
      throw new IllegalArgumentException ( "input file is not a normal file" ); //$NON-NLS-1$
    }
    // esteregg
    if ( pLatexPrintable.toLatexString ().toString ().contains ( "\\" //$NON-NLS-1$
        + LATEX_IDENTIFIER + "{spiderschwein}" ) ) //$NON-NLS-1$
    {
      exportEasteregg ( pLatexFile );
    }
    else
    {
      exportLatex ( pLatexPrintable, pLatexFile, pOneFile, sourceCode );
    }
  }


  /**
   * Writes the easteregg to the given {@link BufferedWriter}.
   * 
   * @param pLatexFile The latex {@link File}.
   * @throws LatexException If something in the latex export does not work.
   */
  private final static void exportEasteregg ( File pLatexFile )
      throws LatexException
  {
    BufferedWriter writer;
    try
    {
      writer = new BufferedWriter ( new OutputStreamWriter (
          new FileOutputStream ( pLatexFile ), CHARSET_NAME ) );
    }
    catch ( UnsupportedEncodingException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.3" ) ); //$NON-NLS-1$
    }
    catch ( FileNotFoundException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.2" ) ); //$NON-NLS-1$
    }
    println ( writer, "%%" ); //$NON-NLS-1$
    println ( writer, "%% TPML LaTeX Export" ); //$NON-NLS-1$
    println ( writer, "%%" ); //$NON-NLS-1$
    println ( writer );
    println ( writer, "\\documentclass[a4paper,12pt]{report}" ); //$NON-NLS-1$
    println ( writer, "\\usepackage[utf8]{inputenc}" ); //$NON-NLS-1$
    println ( writer, "\\usepackage{lscape}" ); //$NON-NLS-1$
    println ( writer, "\\setlength{\\parindent}{0pt}" ); //$NON-NLS-1$
    println ( writer, "\\pagestyle{empty}" ); //$NON-NLS-1$
    println ( writer, "\\oddsidemargin=-30pt" ); //$NON-NLS-1$
    println ( writer, "\\topmargin=-60pt" ); //$NON-NLS-1$
    println ( writer, "\\textwidth=510pt" ); //$NON-NLS-1$
    println ( writer, "\\textheight=750pt" ); //$NON-NLS-1$
    println ( writer );
    println ( writer, "\\begin{document}" ); //$NON-NLS-1$
    println ( writer, "\\begin{landscape}" ); //$NON-NLS-1$
    println ( writer );
    println ( writer, "\\begin{verbatim}" ); //$NON-NLS-1$
    println ( writer, "----------.__.--------._.----------" ); //$NON-NLS-1$
    println ( writer, "---------,-|--|==\\__|-|------------" ); //$NON-NLS-1$
    println ( writer, "---_-_,'--|--|--------|--\\---------" ); //$NON-NLS-1$
    println ( writer, "---|@|---------------|---\\---------" ); //$NON-NLS-1$
    println ( writer, "----|o--o-------------'.---|-------" ); //$NON-NLS-1$
    println ( writer, "----\\----------------------/~~~----" ); //$NON-NLS-1$
    println ( writer, "-----|-__--======='----------------" ); //$NON-NLS-1$
    println ( writer, "-----\\/--\\/------------------------" ); //$NON-NLS-1$
    println ( writer, "-----------------------------------" ); //$NON-NLS-1$
    println ( writer, "\\end{verbatim}" ); //$NON-NLS-1$
    println ( writer, "Spiderschwein, Spiderschwein." ); //$NON-NLS-1$
    println ( writer, "Macht was immer ein Spiderschwein macht." ); //$NON-NLS-1$
    println ( writer, "Hängt es von einem Netz? Kann es nicht," ); //$NON-NLS-1$
    println ( writer, "es ist ein Schwein. Pass auf!" ); //$NON-NLS-1$
    println ( writer, "Es ist ein Spiderschwein." ); //$NON-NLS-1$
    // document end
    println ( writer );
    println ( writer, "\\end{landscape}" ); //$NON-NLS-1$
    println ( writer, "\\end{document}" ); //$NON-NLS-1$
    // close
    try
    {
      writer.close ();
    }
    catch ( IOException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.1" ) ); //$NON-NLS-1$
    }
  }


  /**
   * Writes the input {@link LatexPrintable} to the given {@link File}.
   * 
   * @param pLatexPrintable The input {@link LatexPrintable}.
   * @param pLatexFile The latex {@link File}.
   * @param pOneFile If true, only one file is exported, otherwise the tpml file
   *          is used as an import.
   * @param sourceCode Flag that indicates if the source code should be printed.
   * @throws LatexException If something in the latex export does not work.
   */
  private final static void exportLatex ( LatexPrintable pLatexPrintable,
      File pLatexFile, boolean pOneFile, boolean sourceCode )
      throws LatexException
  {
    BufferedWriter writer;
    try
    {
      writer = new BufferedWriter ( new OutputStreamWriter (
          new FileOutputStream ( pLatexFile ), CHARSET_NAME ) );
    }
    catch ( UnsupportedEncodingException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.3" ) ); //$NON-NLS-1$
    }
    catch ( FileNotFoundException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.2" ) ); //$NON-NLS-1$
    }
    // header
    println ( writer, "%%" ); //$NON-NLS-1$
    println ( writer, "%% TPML LaTeX Export" ); //$NON-NLS-1$
    println ( writer, "%%" ); //$NON-NLS-1$
    println ( writer );
    println ( writer, "\\documentclass[a4paper,12pt]{report}" ); //$NON-NLS-1$
    println ( writer, "\\usepackage[utf8]{inputenc}" ); //$NON-NLS-1$
    println ( writer, "\\usepackage{lscape}" ); //$NON-NLS-1$
    println ( writer, "\\setlength{\\parindent}{0pt}" ); //$NON-NLS-1$
    println ( writer, "\\pagestyle{empty}" ); //$NON-NLS-1$
    println ( writer, "\\oddsidemargin=-30pt" ); //$NON-NLS-1$
    if ( sourceCode )
    {
      println ( writer, "\\topmargin=-120pt" ); //$NON-NLS-1$
    }
    else
    {
      println ( writer, "\\topmargin=-60pt" ); //$NON-NLS-1$
    }
    println ( writer, "\\textwidth=510pt" ); //$NON-NLS-1$
    println ( writer, "\\textheight=750pt" ); //$NON-NLS-1$
    println ( writer );
    if ( pOneFile )
    {
      // packages
      LatexPackageList packages = pLatexPrintable.getLatexPackages ();
      if ( packages.size () > 0 )
      {
        println ( writer, "%%" ); //$NON-NLS-1$
        println ( writer, "%% " + LatexPackage.DESCRIPTION ); //$NON-NLS-1$
        println ( writer, "%%" ); //$NON-NLS-1$
        println ( writer );
      }
      for ( LatexPackage pack : packages )
      {
        println ( writer, pack.toString () );
      }
      if ( packages.size () > 0 )
      {
        println ( writer );
      }
      // instructions
      LatexInstructionList instructions = pLatexPrintable
          .getLatexInstructions ();
      if ( instructions.size () > 0 )
      {
        println ( writer, "%%" ); //$NON-NLS-1$
        println ( writer, "%% " + LatexInstruction.DESCRIPTION ); //$NON-NLS-1$
        println ( writer, "%%" ); //$NON-NLS-1$
        println ( writer );
      }
      for ( LatexInstruction instruction : instructions )
      {
        println ( writer, instruction.toString () );
      }
      if ( instructions.size () > 0 )
      {
        println ( writer );
      }
      // commands
      LatexCommandList commands = pLatexPrintable.getLatexCommands ();
      if ( commands.size () > 0 )
      {
        println ( writer, "%%" ); //$NON-NLS-1$
        println ( writer, "%% " + LatexCommand.DESCRIPTION ); //$NON-NLS-1$
        println ( writer, "%%" ); //$NON-NLS-1$
        println ( writer );
      }
      for ( LatexCommand command : commands )
      {
        println ( writer, command.toString () );
      }
      if ( commands.size () > 0 )
      {
        println ( writer );
      }
    }
    else
    {
      println ( writer, "%%" ); //$NON-NLS-1$
      println ( writer, "%% Needed tpml input" ); //$NON-NLS-1$
      println ( writer, "%%" ); //$NON-NLS-1$
      println ( writer );
      println ( writer, "\\input{tpml}" ); //$NON-NLS-1$
      println ( writer );
    }
    // document begin
    println ( writer, "%%" ); //$NON-NLS-1$
    println ( writer, "%% Document" ); //$NON-NLS-1$
    println ( writer, "%%" ); //$NON-NLS-1$
    println ( writer );
    println ( writer, "\\begin{document}" ); //$NON-NLS-1$
    println ( writer, "\\begin{landscape}" ); //$NON-NLS-1$
    println ( writer );
    if ( ! ( pLatexPrintable instanceof ProofModel ) )
    {
      println ( writer, "$" ); //$NON-NLS-1$
    }
    // latex printable
    println ( writer, pLatexPrintable.toLatexString ().toString () );
    if ( ! ( pLatexPrintable instanceof ProofModel ) )
    {
      println ( writer, "$" ); //$NON-NLS-1$
    }
    // document end
    println ( writer );
    println ( writer, "\\end{landscape}" ); //$NON-NLS-1$
    println ( writer, "\\end{document}" ); //$NON-NLS-1$
    // close
    try
    {
      writer.close ();
    }
    catch ( IOException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.1" ) ); //$NON-NLS-1$
    }
  }


  /**
   * Writes all latex commands to the given {@link File}.
   * 
   * @param pDirectory The tpml {@link File} directory.
   * @throws LatexException If something in the latex export does not work.
   */
  public final static void exportTPML ( File pDirectory ) throws LatexException
  {
    if ( pDirectory == null )
    {
      throw new NullPointerException ( "directory is null" ); //$NON-NLS-1$
    }
    if ( !pDirectory.isDirectory () )
    {
      throw new IllegalArgumentException ( "input file is not a directory" ); //$NON-NLS-1$
    }
    BufferedWriter writer;
    try
    {
      writer = new BufferedWriter (
          new OutputStreamWriter ( new FileOutputStream ( pDirectory
              .getAbsolutePath ()
              + "/tpml.tex" ), CHARSET_NAME ) ); //$NON-NLS-1$
    }
    catch ( UnsupportedEncodingException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.3" ) ); //$NON-NLS-1$
    }
    catch ( FileNotFoundException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.2" ) ); //$NON-NLS-1$
    }
    // packages
    LatexPackageList packages = getAllLatexPackages ();
    if ( packages.size () > 0 )
    {
      LatexExport.println ( writer, "%%" ); //$NON-NLS-1$
      LatexExport.println ( writer, "%% " + LatexPackage.DESCRIPTION ); //$NON-NLS-1$
      LatexExport.println ( writer, "%%" ); //$NON-NLS-1$
      LatexExport.println ( writer );
    }
    for ( LatexPackage pack : packages )
    {
      LatexExport.println ( writer, pack.toString () );
    }
    if ( packages.size () > 0 )
    {
      LatexExport.println ( writer );
    }
    // instructions
    LatexInstructionList instructions = getAllLatexInstructions ();
    if ( instructions.size () > 0 )
    {
      LatexExport.println ( writer, "%%" ); //$NON-NLS-1$
      LatexExport.println ( writer, "%% " + LatexInstruction.DESCRIPTION ); //$NON-NLS-1$
      LatexExport.println ( writer, "%%" ); //$NON-NLS-1$
      LatexExport.println ( writer );
    }
    for ( LatexInstruction instruction : instructions )
    {
      LatexExport.println ( writer, instruction.toString () );
    }
    if ( instructions.size () > 0 )
    {
      LatexExport.println ( writer );
    }
    // commands
    LatexCommandList commands = getAllLatexCommands ();
    if ( commands.size () > 0 )
    {
      LatexExport.println ( writer, "%%" ); //$NON-NLS-1$
      LatexExport.println ( writer, "%% " + LatexCommand.DESCRIPTION ); //$NON-NLS-1$
      LatexExport.println ( writer, "%%" ); //$NON-NLS-1$
      LatexExport.println ( writer );
    }
    for ( LatexCommand command : commands )
    {
      LatexExport.println ( writer, command.toString () );
    }
    // close
    try
    {
      writer.close ();
    }
    catch ( IOException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.1" ) ); //$NON-NLS-1$
    }
  }


  /**
   * Returns all needed {@link LatexCommand}s.
   * 
   * @return All needed {@link LatexCommand}s.
   */
  private static LatexCommandList getAllLatexCommands ()
  {
    LatexCommandList commands = new LatexCommandList ();
    // Expression
    commands.add ( Expression.getLatexCommandsStatic () );
    commands.add ( And.getLatexCommandsStatic () );
    commands.add ( Application.getLatexCommandsStatic () );
    commands.add ( Attribute.getLatexCommandsStatic () );
    commands.add ( BinaryOperator.getLatexCommandsStatic () );
    commands.add ( Class.getLatexCommandsStatic () );
    commands.add ( Coercion.getLatexCommandsStatic () );
    commands.add ( Condition.getLatexCommandsStatic () );
    commands.add ( Condition1.getLatexCommandsStatic () );
    commands.add ( Constant.getLatexCommandsStatic () );
    commands.add ( CurriedLet.getLatexCommandsStatic () );
    commands.add ( CurriedLetRec.getLatexCommandsStatic () );
    commands.add ( CurriedMethod.getLatexCommandsStatic () );
    commands.add ( Duplication.getLatexCommandsStatic () );
    commands.add ( Exn.getLatexCommandsStatic () );
    commands.add ( Identifier.getLatexCommandsStatic () );
    commands.add ( InfixOperation.getLatexCommandsStatic () );
    commands.add ( Inherit.getLatexCommandsStatic () );
    commands.add ( Lambda.getLatexCommandsStatic () );
    commands.add ( Let.getLatexCommandsStatic () );
    commands.add ( LetRec.getLatexCommandsStatic () );
    commands.add ( List.getLatexCommandsStatic () );
    commands.add ( Location.getLatexCommandsStatic () );
    commands.add ( Method.getLatexCommandsStatic () );
    commands.add ( MultiLambda.getLatexCommandsStatic () );
    commands.add ( MultiLet.getLatexCommandsStatic () );
    commands.add ( New.getLatexCommandsStatic () );
    commands.add ( ObjectExpr.getLatexCommandsStatic () );
    commands.add ( Or.getLatexCommandsStatic () );
    commands.add ( Recursion.getLatexCommandsStatic () );
    commands.add ( Row.getLatexCommandsStatic () );
    commands.add ( Send.getLatexCommandsStatic () );
    commands.add ( Sequence.getLatexCommandsStatic () );
    commands.add ( Tuple.getLatexCommandsStatic () );
    commands.add ( While.getLatexCommandsStatic () );
    // Type
    commands.add ( Type.getLatexCommandsStatic () );
    commands.add ( ArrowType.getLatexCommandsStatic () );
    commands.add ( BooleanType.getLatexCommandsStatic () );
    commands.add ( ClassType.getLatexCommandsStatic () );
    commands.add ( IntegerType.getLatexCommandsStatic () );
    commands.add ( ListType.getLatexCommandsStatic () );
    commands.add ( ObjectType.getLatexCommandsStatic () );
    commands.add ( PolyType.getLatexCommandsStatic () );
    commands.add ( RecType.getLatexCommandsStatic () );
    commands.add ( RefType.getLatexCommandsStatic () );
    commands.add ( RowType.getLatexCommandsStatic () );
    commands.add ( TupleType.getLatexCommandsStatic () );
    commands.add ( TypeName.getLatexCommandsStatic () );
    commands.add ( TypeVariable.getLatexCommandsStatic () );
    commands.add ( UnifyType.getLatexCommandsStatic () );
    commands.add ( UnitType.getLatexCommandsStatic () );
    // Environment
    commands.add ( DefaultTypeEnvironment.getLatexCommandsStatic () );
    // Store
    commands.add ( DefaultStore.getLatexCommandsStatic () );
    // TypeChecker
    commands.add ( AbstractTypeCheckerProofRule.getLatexCommandsStatic () );
    commands.add ( SeenTypes.getLatexCommandsStatic () );
    commands.add ( DefaultTypeSubstitution.getLatexCommandsStatic () );
    commands.add ( DefaultTypeEquation.getLatexCommandsStatic () );
    commands.add ( DefaultTypeEquationList.getLatexCommandsStatic () );
    commands.add ( DefaultTypeCheckerExpressionProofNode
        .getLatexCommandsStatic () );
    commands.add ( DefaultTypeCheckerTypeProofNode.getLatexCommandsStatic () );
    commands.add ( TypeCheckerProofModel.getLatexCommandsStatic () );
    // TypeInference
    commands.add ( DefaultTypeEquation.getLatexCommandsStatic () );
    commands.add ( DefaultTypeEquationList.getLatexCommandsStatic () );
    commands.add ( TypeSubstitutionList.getLatexCommandsStatic () );
    commands.add ( TypeJudgement.getLatexCommandsStatic () );
    commands.add ( TypeSubType.getLatexCommandsStatic () );
    commands.add ( DefaultTypeInferenceProofNode.getLatexCommandsStatic () );
    commands.add ( TypeInferenceProofModel.getLatexCommandsStatic () );
    // SmallStep
    commands.add ( DefaultSmallStepProofRule.getLatexCommandsStatic () );
    commands.add ( DefaultSmallStepProofNode.getLatexCommandsStatic () );
    commands.add ( SmallStepProofModel.getLatexCommandsStatic () );
    // BigStep
    commands.add ( AbstractBigStepProofRule.getLatexCommandsStatic () );
    commands.add ( BigStepProofResult.getLatexCommandsStatic () );
    commands.add ( DefaultBigStepProofNode.getLatexCommandsStatic () );
    commands.add ( BigStepProofModel.getLatexCommandsStatic () );
    // MinimalTyping
    commands.add ( AbstractMinimalTypingProofRule.getLatexCommandsStatic () );
    commands.add ( DefaultMinimalTypingExpressionProofNode
        .getLatexCommandsStatic () );
    commands
        .add ( DefaultMinimalTypingTypesProofNode.getLatexCommandsStatic () );
    commands.add ( MinimalTypingProofModel.getLatexCommandsStatic () );
    // SubTyping
    commands.add ( AbstractSubTypingProofRule.getLatexCommandsStatic () );
    commands.add ( DefaultSubTypingProofNode.getLatexCommandsStatic () );
    commands.add ( SubTypingProofModel.getLatexCommandsStatic () );
    // RecSubTyping
    commands.add ( AbstractRecSubTypingProofRule.getLatexCommandsStatic () );
    commands.add ( DefaultSubType.getLatexCommandsStatic () );
    commands.add ( DefaultRecSubTypingProofNode.getLatexCommandsStatic () );
    commands.add ( RecSubTypingProofModel.getLatexCommandsStatic () );
    return commands;
  }


  /**
   * Returns all needed {@link LatexInstruction}s.
   * 
   * @return All needed {@link LatexInstruction}s.
   */
  private static LatexInstructionList getAllLatexInstructions ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    // Expression
    instructions.add ( Expression.getLatexInstructionsStatic () );
    // Type
    instructions.add ( Type.getLatexInstructionsStatic () );
    // Environment
    instructions.add ( DefaultTypeEnvironment.getLatexInstructionsStatic () );
    // Store
    instructions.add ( DefaultStore.getLatexInstructionsStatic () );
    // TypeChecker
    instructions.add ( AbstractTypeCheckerProofRule
        .getLatexInstructionsStatic () );
    instructions.add ( SeenTypes.getLatexInstructionsStatic () );
    instructions.add ( DefaultTypeSubstitution.getLatexInstructionsStatic () );
    instructions.add ( DefaultTypeEquation.getLatexInstructionsStatic () );
    instructions.add ( DefaultTypeEquationList.getLatexInstructionsStatic () );
    instructions.add ( DefaultTypeCheckerExpressionProofNode
        .getLatexInstructionsStatic () );
    instructions.add ( DefaultTypeCheckerTypeProofNode
        .getLatexInstructionsStatic () );
    instructions.add ( TypeCheckerProofModel.getLatexInstructionsStatic () );
    // TypeInference
    instructions.add ( DefaultTypeEquation.getLatexInstructionsStatic () );
    instructions.add ( DefaultTypeEquationList.getLatexInstructionsStatic () );
    instructions.add ( TypeSubstitutionList.getLatexInstructionsStatic () );
    instructions.add ( TypeJudgement.getLatexInstructionsStatic () );
    instructions.add ( TypeSubType.getLatexInstructionsStatic () );
    instructions.add ( DefaultTypeInferenceProofNode
        .getLatexInstructionsStatic () );
    instructions.add ( TypeInferenceProofModel.getLatexInstructionsStatic () );
    // SmallStep
    instructions.add ( DefaultSmallStepProofRule.getLatexInstructionsStatic () );
    instructions.add ( DefaultSmallStepProofNode.getLatexInstructionsStatic () );
    instructions.add ( SmallStepProofModel.getLatexInstructionsStatic () );
    // BigStep
    instructions.add ( AbstractBigStepProofRule.getLatexInstructionsStatic () );
    instructions.add ( BigStepProofResult.getLatexInstructionsStatic () );
    instructions.add ( DefaultBigStepProofNode.getLatexInstructionsStatic () );
    instructions.add ( BigStepProofModel.getLatexInstructionsStatic () );
    // MinimalTyping
    instructions.add ( AbstractMinimalTypingProofRule
        .getLatexInstructionsStatic () );
    instructions.add ( DefaultMinimalTypingExpressionProofNode
        .getLatexInstructionsStatic () );
    instructions.add ( DefaultMinimalTypingTypesProofNode
        .getLatexInstructionsStatic () );
    instructions.add ( MinimalTypingProofModel.getLatexInstructionsStatic () );
    // SubTyping
    instructions
        .add ( AbstractSubTypingProofRule.getLatexInstructionsStatic () );
    instructions.add ( DefaultSubTypingProofNode.getLatexInstructionsStatic () );
    instructions.add ( SubTypingProofModel.getLatexInstructionsStatic () );
    // RecSubTyping
    instructions.add ( AbstractRecSubTypingProofRule
        .getLatexInstructionsStatic () );
    instructions.add ( DefaultSubType.getLatexInstructionsStatic () );
    instructions.add ( DefaultRecSubTypingProofNode
        .getLatexInstructionsStatic () );
    instructions.add ( RecSubTypingProofModel.getLatexInstructionsStatic () );
    return instructions;
  }


  /**
   * Returns all needed {@link LatexPackage}s.
   * 
   * @return All needed {@link LatexPackage}s.
   */
  private static LatexPackageList getAllLatexPackages ()
  {
    LatexPackageList packages = new LatexPackageList ();
    // Expression
    packages.add ( Expression.getLatexPackagesStatic () );
    packages.add ( Class.getLatexPackagesStatic () );
    packages.add ( CurriedLet.getLatexPackagesStatic () );
    packages.add ( CurriedMethod.getLatexPackagesStatic () );
    packages.add ( Lambda.getLatexPackagesStatic () );
    packages.add ( Let.getLatexPackagesStatic () );
    packages.add ( MultiLambda.getLatexPackagesStatic () );
    packages.add ( MultiLet.getLatexPackagesStatic () );
    packages.add ( ObjectExpr.getLatexPackagesStatic () );
    packages.add ( Recursion.getLatexPackagesStatic () );
    // Type
    packages.add ( Type.getLatexPackagesStatic () );
    packages.add ( PolyType.getLatexPackagesStatic () );
    // Environment
    packages.add ( DefaultTypeEnvironment.getLatexPackagesStatic () );
    // Store
    packages.add ( DefaultStore.getLatexPackagesStatic () );
    // TypeChecker
    packages.add ( AbstractTypeCheckerProofRule.getLatexPackagesStatic () );
    packages.add ( SeenTypes.getLatexPackagesStatic () );
    packages.add ( DefaultTypeSubstitution.getLatexPackagesStatic () );
    packages.add ( DefaultTypeEquation.getLatexPackagesStatic () );
    packages.add ( DefaultTypeEquationList.getLatexPackagesStatic () );
    packages.add ( DefaultTypeCheckerExpressionProofNode
        .getLatexPackagesStatic () );
    packages.add ( DefaultTypeCheckerTypeProofNode.getLatexPackagesStatic () );
    packages.add ( TypeCheckerProofModel.getLatexPackagesStatic () );
    // TypeInference
    packages.add ( DefaultTypeEquation.getLatexPackagesStatic () );
    packages.add ( DefaultTypeEquationList.getLatexPackagesStatic () );
    packages.add ( TypeSubstitutionList.getLatexPackagesStatic () );
    packages.add ( TypeJudgement.getLatexPackagesStatic () );
    packages.add ( TypeSubType.getLatexPackagesStatic () );
    packages.add ( DefaultTypeInferenceProofNode.getLatexPackagesStatic () );
    packages.add ( TypeInferenceProofModel.getLatexPackagesStatic () );
    // SmallStep
    packages.add ( DefaultSmallStepProofRule.getLatexPackagesStatic () );
    packages.add ( DefaultSmallStepProofNode.getLatexPackagesStatic () );
    packages.add ( SmallStepProofModel.getLatexPackagesStatic () );
    // BigStep
    packages.add ( AbstractBigStepProofRule.getLatexPackagesStatic () );
    packages.add ( BigStepProofResult.getLatexPackagesStatic () );
    packages.add ( DefaultBigStepProofNode.getLatexPackagesStatic () );
    packages.add ( BigStepProofModel.getLatexPackagesStatic () );
    // MinimalTyping
    packages.add ( AbstractMinimalTypingProofRule.getLatexPackagesStatic () );
    packages.add ( DefaultMinimalTypingExpressionProofNode
        .getLatexPackagesStatic () );
    packages
        .add ( DefaultMinimalTypingTypesProofNode.getLatexPackagesStatic () );
    packages.add ( MinimalTypingProofModel.getLatexPackagesStatic () );
    // SubTyping
    packages.add ( AbstractSubTypingProofRule.getLatexPackagesStatic () );
    packages.add ( DefaultSubTypingProofNode.getLatexPackagesStatic () );
    packages.add ( SubTypingProofModel.getLatexPackagesStatic () );
    // RecSubTyping
    packages.add ( AbstractRecSubTypingProofRule.getLatexPackagesStatic () );
    packages.add ( DefaultSubType.getLatexPackagesStatic () );
    packages.add ( DefaultRecSubTypingProofNode.getLatexPackagesStatic () );
    packages.add ( RecSubTypingProofModel.getLatexPackagesStatic () );
    return packages;
  }


  /**
   * Writes a new line to the given {@link BufferedWriter}.
   * 
   * @param pBufferedWriter The {@link BufferedWriter} which should be used.
   * @throws LatexException If an I/O error occurs.
   */
  private final static void println ( BufferedWriter pBufferedWriter )
      throws LatexException
  {
    try
    {
      pBufferedWriter.newLine ();
    }
    catch ( IOException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.0" ) ); //$NON-NLS-1$
    }
  }


  /**
   * Writes a string and a new line to the given {@link BufferedWriter}.
   * 
   * @param pBufferedWriter The {@link BufferedWriter} which should be used.
   * @param pText The text which should be written to the {@link BufferedWriter}.
   * @throws LatexException If an I/O error occurs.
   */
  private final static void println ( BufferedWriter pBufferedWriter,
      String pText ) throws LatexException
  {
    try
    {
      pBufferedWriter.write ( pText );
      pBufferedWriter.newLine ();
    }
    catch ( IOException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.0" ) ); //$NON-NLS-1$
    }
  }
}
