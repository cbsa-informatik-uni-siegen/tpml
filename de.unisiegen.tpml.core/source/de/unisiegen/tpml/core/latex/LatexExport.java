package de.unisiegen.tpml.core.latex ;


import java.io.BufferedWriter ;
import java.io.File ;
import java.io.FileNotFoundException ;
import java.io.FileOutputStream ;
import java.io.IOException ;
import java.io.OutputStreamWriter ;
import java.io.UnsupportedEncodingException ;
import java.nio.charset.Charset ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.ProofModel ;
import de.unisiegen.tpml.core.bigstep.AbstractBigStepProofRule ;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel ;
import de.unisiegen.tpml.core.bigstep.BigStepProofResult ;
import de.unisiegen.tpml.core.bigstep.DefaultBigStepProofNode ;
import de.unisiegen.tpml.core.expressions.And ;
import de.unisiegen.tpml.core.expressions.Application ;
import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.BinaryOperator ;
import de.unisiegen.tpml.core.expressions.Class ;
import de.unisiegen.tpml.core.expressions.Coercion ;
import de.unisiegen.tpml.core.expressions.Condition ;
import de.unisiegen.tpml.core.expressions.Condition1 ;
import de.unisiegen.tpml.core.expressions.Constant ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.CurriedMethod ;
import de.unisiegen.tpml.core.expressions.Duplication ;
import de.unisiegen.tpml.core.expressions.Exn ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.InfixOperation ;
import de.unisiegen.tpml.core.expressions.Inherit ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
import de.unisiegen.tpml.core.expressions.List ;
import de.unisiegen.tpml.core.expressions.Location ;
import de.unisiegen.tpml.core.expressions.Method ;
import de.unisiegen.tpml.core.expressions.MultiLambda ;
import de.unisiegen.tpml.core.expressions.MultiLet ;
import de.unisiegen.tpml.core.expressions.New ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.expressions.Or ;
import de.unisiegen.tpml.core.expressions.Recursion ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.expressions.Send ;
import de.unisiegen.tpml.core.expressions.Sequence ;
import de.unisiegen.tpml.core.expressions.Tuple ;
import de.unisiegen.tpml.core.expressions.While ;
import de.unisiegen.tpml.core.interpreters.DefaultStore ;
import de.unisiegen.tpml.core.minimaltyping.AbstractMinimalTypingProofRule ;
import de.unisiegen.tpml.core.minimaltyping.DefaultMinimalTypingExpressionProofNode ;
import de.unisiegen.tpml.core.minimaltyping.DefaultMinimalTypingTypesProofNode ;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofModel ;
import de.unisiegen.tpml.core.smallstep.DefaultSmallStepProofNode ;
import de.unisiegen.tpml.core.smallstep.DefaultSmallStepProofRule ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;
import de.unisiegen.tpml.core.subtyping.AbstractSubTypingProofRule ;
import de.unisiegen.tpml.core.subtyping.DefaultSubTypingProofNode ;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel ;
import de.unisiegen.tpml.core.subtypingrec.AbstractRecSubTypingProofRule ;
import de.unisiegen.tpml.core.subtypingrec.DefaultRecSubTypingProofNode ;
import de.unisiegen.tpml.core.subtypingrec.DefaultSubType ;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofModel ;
import de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRule ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerExpressionProofNode ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerTypeProofNode ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment ;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution ;
import de.unisiegen.tpml.core.typechecker.SeenTypes ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel ;
import de.unisiegen.tpml.core.typechecker.TypeEquationListTypeChecker ;
import de.unisiegen.tpml.core.typechecker.TypeEquationTypeChecker ;
import de.unisiegen.tpml.core.typeinference.DefaultTypeInferenceProofNode ;
import de.unisiegen.tpml.core.typeinference.TypeEquationListTypeInference ;
import de.unisiegen.tpml.core.typeinference.TypeEquationTypeInference ;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel ;
import de.unisiegen.tpml.core.typeinference.TypeJudgement ;
import de.unisiegen.tpml.core.typeinference.TypeSubType ;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList ;
import de.unisiegen.tpml.core.types.ArrowType ;
import de.unisiegen.tpml.core.types.BooleanType ;
import de.unisiegen.tpml.core.types.ClassType ;
import de.unisiegen.tpml.core.types.IntegerType ;
import de.unisiegen.tpml.core.types.ListType ;
import de.unisiegen.tpml.core.types.ObjectType ;
import de.unisiegen.tpml.core.types.PolyType ;
import de.unisiegen.tpml.core.types.RecType ;
import de.unisiegen.tpml.core.types.RefType ;
import de.unisiegen.tpml.core.types.RowType ;
import de.unisiegen.tpml.core.types.TupleType ;
import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.core.types.TypeName ;
import de.unisiegen.tpml.core.types.TypeVariable ;
import de.unisiegen.tpml.core.types.UnifyType ;
import de.unisiegen.tpml.core.types.UnitType ;


/**
 * The main class of the latex export. The export method writes the input
 * {@link LatexPrintable} to the given {@link File}.
 * 
 * @author Christian Fehler
 */
public abstract class LatexExport implements LatexCommandNames
{
  /**
   * The name of a supported {@link Charset}.
   */
  private static final String CHARSET_NAME = "UTF8" ; //$NON-NLS-1$


  /**
   * Writes the input {@link LatexPrintable} to the given {@link File}.
   * 
   * @param pLatexPrintable The input {@link LatexPrintable}.
   * @param pLatexFile The latex {@link File}.
   * @param pOneFile If true, only one file is exported, otherwise the tpml file
   *          is used as an import.
   * @throws LatexException If something in the latex export does not work.
   */
  public final static void export ( LatexPrintable pLatexPrintable ,
      File pLatexFile , boolean pOneFile ) throws LatexException
  {
    if ( pLatexPrintable == null )
    {
      throw new NullPointerException ( "latex printable is null" ) ; //$NON-NLS-1$
    }
    if ( pLatexFile == null )
    {
      throw new NullPointerException ( "latex file is null" ) ; //$NON-NLS-1$
    }
    if ( pLatexFile.isDirectory ( ) )
    {
      throw new IllegalArgumentException ( "input file is not a normal file" ) ; //$NON-NLS-1$
    }
    // eateregg
    if ( pLatexPrintable.toLatexString ( ).toString ( ).contains ( "\\" //$NON-NLS-1$
        + LATEX_IDENTIFIER + "{spiderschwein}" ) ) //$NON-NLS-1$
    {
      exportEasteregg ( pLatexFile ) ;
    }
    else
    {
      exportLatex ( pLatexPrintable , pLatexFile , pOneFile ) ;
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
    BufferedWriter writer ;
    try
    {
      writer = new BufferedWriter ( new OutputStreamWriter (
          new FileOutputStream ( pLatexFile ) , CHARSET_NAME ) ) ;
    }
    catch ( UnsupportedEncodingException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.3" ) ) ; //$NON-NLS-1$
    }
    catch ( FileNotFoundException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.2" ) ) ; //$NON-NLS-1$
    }
    println ( writer , "%%" ) ; //$NON-NLS-1$
    println ( writer , "%% TPML LaTeX Export" ) ; //$NON-NLS-1$
    println ( writer , "%%" ) ; //$NON-NLS-1$
    println ( writer ) ;
    println ( writer , "\\documentclass[a4paper,12pt]{report}" ) ; //$NON-NLS-1$
    println ( writer , "\\usepackage[utf8]{inputenc}" ) ; //$NON-NLS-1$
    println ( writer , "\\usepackage{lscape}" ) ; //$NON-NLS-1$
    println ( writer , "\\setlength{\\parindent}{0pt}" ) ; //$NON-NLS-1$
    println ( writer , "\\pagestyle{empty}" ) ; //$NON-NLS-1$
    println ( writer , "\\oddsidemargin=-30pt" ) ; //$NON-NLS-1$
    println ( writer , "\\topmargin=-60pt" ) ; //$NON-NLS-1$
    println ( writer , "\\textwidth=510pt" ) ; //$NON-NLS-1$
    println ( writer , "\\textheight=750pt" ) ; //$NON-NLS-1$
    println ( writer ) ;
    println ( writer , "\\begin{document}" ) ; //$NON-NLS-1$
    println ( writer , "\\begin{landscape}" ) ; //$NON-NLS-1$
    println ( writer ) ;
    println ( writer , "\\begin{verbatim}" ) ; //$NON-NLS-1$
    println ( writer , "----------.__.--------._.----------" ) ; //$NON-NLS-1$
    println ( writer , "---------,-|--|==\\__|-|------------" ) ; //$NON-NLS-1$
    println ( writer , "---_-_,'--|--|--------|--\\---------" ) ; //$NON-NLS-1$
    println ( writer , "---|@|---------------|---\\---------" ) ; //$NON-NLS-1$
    println ( writer , "----|o--o-------------'.---|-------" ) ; //$NON-NLS-1$
    println ( writer , "----\\----------------------/~~~----" ) ; //$NON-NLS-1$
    println ( writer , "-----|-__--======='----------------" ) ; //$NON-NLS-1$
    println ( writer , "-----\\/--\\/------------------------" ) ; //$NON-NLS-1$
    println ( writer , "-----------------------------------" ) ; //$NON-NLS-1$
    println ( writer , "\\end{verbatim}" ) ; //$NON-NLS-1$
    println ( writer , "Spiderschwein, Spiderschwein ..." ) ; //$NON-NLS-1$
    // document end
    println ( writer ) ;
    println ( writer , "\\end{landscape}" ) ; //$NON-NLS-1$
    println ( writer , "\\end{document}" ) ; //$NON-NLS-1$
    // close
    try
    {
      writer.close ( ) ;
    }
    catch ( IOException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.1" ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * Writes the input {@link LatexPrintable} to the given {@link File}.
   * 
   * @param pLatexPrintable The input {@link LatexPrintable}.
   * @param pLatexFile The latex {@link File}.
   * @param pOneFile If true, only one file is exported, otherwise the tpml file
   *          is used as an import.
   * @throws LatexException If something in the latex export does not work.
   */
  private final static void exportLatex ( LatexPrintable pLatexPrintable ,
      File pLatexFile , boolean pOneFile ) throws LatexException
  {
    BufferedWriter writer ;
    try
    {
      writer = new BufferedWriter ( new OutputStreamWriter (
          new FileOutputStream ( pLatexFile ) , CHARSET_NAME ) ) ;
    }
    catch ( UnsupportedEncodingException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.3" ) ) ; //$NON-NLS-1$
    }
    catch ( FileNotFoundException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.2" ) ) ; //$NON-NLS-1$
    }
    // header
    println ( writer , "%%" ) ; //$NON-NLS-1$
    println ( writer , "%% TPML LaTeX Export" ) ; //$NON-NLS-1$
    println ( writer , "%%" ) ; //$NON-NLS-1$
    println ( writer ) ;
    println ( writer , "\\documentclass[a4paper,12pt]{report}" ) ; //$NON-NLS-1$
    println ( writer , "\\usepackage[utf8]{inputenc}" ) ; //$NON-NLS-1$
    println ( writer , "\\usepackage{lscape}" ) ; //$NON-NLS-1$
    println ( writer , "\\setlength{\\parindent}{0pt}" ) ; //$NON-NLS-1$
    println ( writer , "\\pagestyle{empty}" ) ; //$NON-NLS-1$
    println ( writer , "\\oddsidemargin=-30pt" ) ; //$NON-NLS-1$
    println ( writer , "\\topmargin=-60pt" ) ; //$NON-NLS-1$
    println ( writer , "\\textwidth=510pt" ) ; //$NON-NLS-1$
    println ( writer , "\\textheight=750pt" ) ; //$NON-NLS-1$
    println ( writer ) ;
    if ( pOneFile )
    {
      // packages
      TreeSet < LatexPackage > packages = pLatexPrintable.getLatexPackages ( ) ;
      if ( packages.size ( ) > 0 )
      {
        println ( writer , "%%" ) ; //$NON-NLS-1$
        println ( writer , "%% " + LatexPackage.DESCRIPTION ) ; //$NON-NLS-1$
        println ( writer , "%%" ) ; //$NON-NLS-1$
        println ( writer ) ;
      }
      for ( LatexPackage pack : packages )
      {
        println ( writer , pack.toString ( ) ) ;
      }
      if ( packages.size ( ) > 0 )
      {
        println ( writer ) ;
      }
      // instructions
      LatexInstructionList instructions = pLatexPrintable
          .getLatexInstructions ( ) ;
      if ( instructions.size ( ) > 0 )
      {
        println ( writer , "%%" ) ; //$NON-NLS-1$
        println ( writer , "%% " + LatexInstruction.DESCRIPTION ) ; //$NON-NLS-1$
        println ( writer , "%%" ) ; //$NON-NLS-1$
        println ( writer ) ;
      }
      for ( LatexInstruction instruction : instructions )
      {
        println ( writer , instruction.toString ( ) ) ;
      }
      if ( instructions.size ( ) > 0 )
      {
        println ( writer ) ;
      }
      // commands
      TreeSet < LatexCommand > commands = pLatexPrintable.getLatexCommands ( ) ;
      if ( commands.size ( ) > 0 )
      {
        println ( writer , "%%" ) ; //$NON-NLS-1$
        println ( writer , "%% " + LatexCommand.DESCRIPTION ) ; //$NON-NLS-1$
        println ( writer , "%%" ) ; //$NON-NLS-1$
        println ( writer ) ;
      }
      for ( LatexCommand command : commands )
      {
        println ( writer , command.toString ( ) ) ;
      }
      if ( commands.size ( ) > 0 )
      {
        println ( writer ) ;
      }
    }
    else
    {
      println ( writer , "%%" ) ; //$NON-NLS-1$
      println ( writer , "%% Needed tpml input" ) ; //$NON-NLS-1$
      println ( writer , "%%" ) ; //$NON-NLS-1$
      println ( writer ) ;
      println ( writer , "\\input{tpml}" ) ; //$NON-NLS-1$
      println ( writer ) ;
    }
    // document begin
    println ( writer , "%%" ) ; //$NON-NLS-1$
    println ( writer , "%% Document" ) ; //$NON-NLS-1$
    println ( writer , "%%" ) ; //$NON-NLS-1$
    println ( writer ) ;
    println ( writer , "\\begin{document}" ) ; //$NON-NLS-1$
    println ( writer , "\\begin{landscape}" ) ; //$NON-NLS-1$
    println ( writer ) ;
    if ( ! ( pLatexPrintable instanceof ProofModel ) )
    {
      println ( writer , "$" ) ; //$NON-NLS-1$
    }
    // latex printable
    println ( writer , pLatexPrintable.toLatexString ( ).toString ( ) ) ;
    if ( ! ( pLatexPrintable instanceof ProofModel ) )
    {
      println ( writer , "$" ) ; //$NON-NLS-1$
    }
    // document end
    println ( writer ) ;
    println ( writer , "\\end{landscape}" ) ; //$NON-NLS-1$
    println ( writer , "\\end{document}" ) ; //$NON-NLS-1$
    // close
    try
    {
      writer.close ( ) ;
    }
    catch ( IOException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.1" ) ) ; //$NON-NLS-1$
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
      throw new NullPointerException ( "directory is null" ) ; //$NON-NLS-1$
    }
    if ( ! pDirectory.isDirectory ( ) )
    {
      throw new IllegalArgumentException ( "input file is not a directory" ) ; //$NON-NLS-1$
    }
    BufferedWriter writer ;
    try
    {
      writer = new BufferedWriter (
          new OutputStreamWriter ( new FileOutputStream ( pDirectory
              .getAbsolutePath ( )
              + "/tpml.tex" ) , CHARSET_NAME ) ) ; //$NON-NLS-1$
    }
    catch ( UnsupportedEncodingException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.3" ) ) ; //$NON-NLS-1$
    }
    catch ( FileNotFoundException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.2" ) ) ; //$NON-NLS-1$
    }
    // packages
    TreeSet < LatexPackage > packages = getAllLatexPackages ( ) ;
    if ( packages.size ( ) > 0 )
    {
      LatexExport.println ( writer , "%%" ) ; //$NON-NLS-1$
      LatexExport.println ( writer , "%% " + LatexPackage.DESCRIPTION ) ; //$NON-NLS-1$
      LatexExport.println ( writer , "%%" ) ; //$NON-NLS-1$
      LatexExport.println ( writer ) ;
    }
    for ( LatexPackage pack : packages )
    {
      LatexExport.println ( writer , pack.toString ( ) ) ;
    }
    if ( packages.size ( ) > 0 )
    {
      LatexExport.println ( writer ) ;
    }
    // instructions
    LatexInstructionList instructions = getAllLatexInstructions ( ) ;
    if ( instructions.size ( ) > 0 )
    {
      LatexExport.println ( writer , "%%" ) ; //$NON-NLS-1$
      LatexExport.println ( writer , "%% " + LatexInstruction.DESCRIPTION ) ; //$NON-NLS-1$
      LatexExport.println ( writer , "%%" ) ; //$NON-NLS-1$
      LatexExport.println ( writer ) ;
    }
    for ( LatexInstruction instruction : instructions )
    {
      LatexExport.println ( writer , instruction.toString ( ) ) ;
    }
    if ( instructions.size ( ) > 0 )
    {
      LatexExport.println ( writer ) ;
    }
    // commands
    TreeSet < LatexCommand > commands = getAllLatexCommands ( ) ;
    if ( commands.size ( ) > 0 )
    {
      LatexExport.println ( writer , "%%" ) ; //$NON-NLS-1$
      LatexExport.println ( writer , "%% " + LatexCommand.DESCRIPTION ) ; //$NON-NLS-1$
      LatexExport.println ( writer , "%%" ) ; //$NON-NLS-1$
      LatexExport.println ( writer ) ;
    }
    for ( LatexCommand command : commands )
    {
      LatexExport.println ( writer , command.toString ( ) ) ;
    }
    // close
    try
    {
      writer.close ( ) ;
    }
    catch ( IOException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.1" ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * Returns all needed {@link LatexCommand}s.
   * 
   * @return All needed {@link LatexCommand}s.
   */
  private static TreeSet < LatexCommand > getAllLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    // Expression
    for ( LatexCommand command : Expression.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : And.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : Application.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : Attribute.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : BinaryOperator.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : Class.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : Coercion.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : Condition.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : Condition1.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : Constant.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : CurriedLet.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : CurriedLetRec.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : CurriedMethod.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : Duplication.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : Exn.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : Identifier.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : InfixOperation.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : Inherit.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : Lambda.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : Let.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : LetRec.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : List.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : Location.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : Method.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : MultiLambda.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : MultiLet.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : New.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : ObjectExpr.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : Or.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : Recursion.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : Row.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : Send.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : Sequence.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : Tuple.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : While.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    // Type
    for ( LatexCommand command : Type.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : ArrowType.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : BooleanType.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : ClassType.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : IntegerType.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : ListType.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : ObjectType.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : PolyType.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : RecType.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : RefType.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : RowType.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : TupleType.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : TypeName.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : TypeVariable.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : UnifyType.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : UnitType.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    // Environment
    for ( LatexCommand command : DefaultTypeEnvironment
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    // Store
    for ( LatexCommand command : DefaultStore.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    // TypeChecker
    for ( LatexCommand command : AbstractTypeCheckerProofRule
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : SeenTypes.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : DefaultTypeSubstitution
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : TypeEquationTypeChecker
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : TypeEquationListTypeChecker
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : DefaultTypeCheckerExpressionProofNode
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : DefaultTypeCheckerTypeProofNode
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : TypeCheckerProofModel
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    // TypeInference
    for ( LatexCommand command : TypeEquationTypeInference
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : TypeEquationListTypeInference
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : TypeSubstitutionList.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : TypeJudgement.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : TypeSubType.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : DefaultTypeInferenceProofNode
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : TypeInferenceProofModel
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    // SmallStep
    for ( LatexCommand command : DefaultSmallStepProofRule
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : DefaultSmallStepProofNode
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : SmallStepProofModel.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    // BigStep
    for ( LatexCommand command : AbstractBigStepProofRule
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : BigStepProofResult.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : DefaultBigStepProofNode
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : BigStepProofModel.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    // MinimalTyping
    for ( LatexCommand command : AbstractMinimalTypingProofRule
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : DefaultMinimalTypingExpressionProofNode
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : DefaultMinimalTypingTypesProofNode
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : MinimalTypingProofModel
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    // SubTyping
    for ( LatexCommand command : AbstractSubTypingProofRule
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : DefaultSubTypingProofNode
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : SubTypingProofModel.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    // RecSubTyping
    for ( LatexCommand command : AbstractRecSubTypingProofRule
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : DefaultSubType.getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : DefaultRecSubTypingProofNode
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : RecSubTypingProofModel
        .getLatexCommandsStatic ( ) )
    {
      commands.add ( command ) ;
    }
    return commands ;
  }


  /**
   * Returns all needed {@link LatexInstruction}s.
   * 
   * @return All needed {@link LatexInstruction}s.
   */
  private static LatexInstructionList getAllLatexInstructions ( )
  {
    LatexInstructionList instructions = new LatexInstructionList ( ) ;
    // Expression
    instructions.add ( Expression.getLatexInstructionsStatic ( ) ) ;
    // Type
    instructions.add ( Type.getLatexInstructionsStatic ( ) ) ;
    // Environment
    instructions.add ( DefaultTypeEnvironment.getLatexInstructionsStatic ( ) ) ;
    // Store
    instructions.add ( DefaultStore.getLatexInstructionsStatic ( ) ) ;
    // TypeChecker
    instructions.add ( AbstractTypeCheckerProofRule
        .getLatexInstructionsStatic ( ) ) ;
    instructions.add ( SeenTypes.getLatexInstructionsStatic ( ) ) ;
    instructions.add ( DefaultTypeSubstitution.getLatexInstructionsStatic ( ) ) ;
    instructions.add ( TypeEquationTypeChecker.getLatexInstructionsStatic ( ) ) ;
    instructions.add ( TypeEquationListTypeChecker
        .getLatexInstructionsStatic ( ) ) ;
    instructions.add ( DefaultTypeCheckerExpressionProofNode
        .getLatexInstructionsStatic ( ) ) ;
    instructions.add ( DefaultTypeCheckerTypeProofNode
        .getLatexInstructionsStatic ( ) ) ;
    instructions.add ( TypeCheckerProofModel.getLatexInstructionsStatic ( ) ) ;
    // TypeInference
    instructions
        .add ( TypeEquationTypeInference.getLatexInstructionsStatic ( ) ) ;
    instructions.add ( TypeEquationListTypeInference
        .getLatexInstructionsStatic ( ) ) ;
    instructions.add ( TypeSubstitutionList.getLatexInstructionsStatic ( ) ) ;
    instructions.add ( TypeJudgement.getLatexInstructionsStatic ( ) ) ;
    instructions.add ( TypeSubType.getLatexInstructionsStatic ( ) ) ;
    instructions.add ( DefaultTypeInferenceProofNode
        .getLatexInstructionsStatic ( ) ) ;
    instructions.add ( TypeInferenceProofModel.getLatexInstructionsStatic ( ) ) ;
    // SmallStep
    instructions
        .add ( DefaultSmallStepProofRule.getLatexInstructionsStatic ( ) ) ;
    instructions
        .add ( DefaultSmallStepProofNode.getLatexInstructionsStatic ( ) ) ;
    instructions.add ( SmallStepProofModel.getLatexInstructionsStatic ( ) ) ;
    // BigStep
    instructions.add ( AbstractBigStepProofRule.getLatexInstructionsStatic ( ) ) ;
    instructions.add ( BigStepProofResult.getLatexInstructionsStatic ( ) ) ;
    instructions.add ( DefaultBigStepProofNode.getLatexInstructionsStatic ( ) ) ;
    instructions.add ( BigStepProofModel.getLatexInstructionsStatic ( ) ) ;
    // MinimalTyping
    instructions.add ( AbstractMinimalTypingProofRule
        .getLatexInstructionsStatic ( ) ) ;
    instructions.add ( DefaultMinimalTypingExpressionProofNode
        .getLatexInstructionsStatic ( ) ) ;
    instructions.add ( DefaultMinimalTypingTypesProofNode
        .getLatexInstructionsStatic ( ) ) ;
    instructions.add ( MinimalTypingProofModel.getLatexInstructionsStatic ( ) ) ;
    // SubTyping
    instructions
        .add ( AbstractSubTypingProofRule.getLatexInstructionsStatic ( ) ) ;
    instructions
        .add ( DefaultSubTypingProofNode.getLatexInstructionsStatic ( ) ) ;
    instructions.add ( SubTypingProofModel.getLatexInstructionsStatic ( ) ) ;
    // RecSubTyping
    instructions.add ( AbstractRecSubTypingProofRule
        .getLatexInstructionsStatic ( ) ) ;
    instructions.add ( DefaultSubType.getLatexInstructionsStatic ( ) ) ;
    instructions.add ( DefaultRecSubTypingProofNode
        .getLatexInstructionsStatic ( ) ) ;
    instructions.add ( RecSubTypingProofModel.getLatexInstructionsStatic ( ) ) ;
    return instructions ;
  }


  /**
   * Returns all needed {@link LatexPackage}s.
   * 
   * @return All needed {@link LatexPackage}s.
   */
  private static TreeSet < LatexPackage > getAllLatexPackages ( )
  {
    TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( ) ;
    // Expression
    for ( LatexPackage pack : Expression.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : Class.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : CurriedLet.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : CurriedMethod.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : Lambda.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : Let.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : MultiLambda.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : MultiLet.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : ObjectExpr.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : Recursion.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    // Type
    for ( LatexPackage pack : Type.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : PolyType.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    // Environment
    for ( LatexPackage pack : DefaultTypeEnvironment.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    // Store
    for ( LatexPackage pack : DefaultStore.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    // TypeChecker
    for ( LatexPackage pack : AbstractTypeCheckerProofRule
        .getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : SeenTypes.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : DefaultTypeSubstitution.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : TypeEquationTypeChecker.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : TypeEquationListTypeChecker
        .getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : DefaultTypeCheckerExpressionProofNode
        .getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : DefaultTypeCheckerTypeProofNode
        .getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : TypeCheckerProofModel.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    // TypeInference
    for ( LatexPackage pack : TypeEquationTypeInference
        .getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : TypeEquationListTypeInference
        .getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : TypeSubstitutionList.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : TypeJudgement.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : TypeSubType.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : DefaultTypeInferenceProofNode
        .getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : TypeInferenceProofModel.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    // SmallStep
    for ( LatexPackage pack : DefaultSmallStepProofRule
        .getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : DefaultSmallStepProofNode
        .getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : SmallStepProofModel.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    // BigStep
    for ( LatexPackage pack : AbstractBigStepProofRule
        .getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : BigStepProofResult.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : DefaultBigStepProofNode.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : BigStepProofModel.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    // MinimalTyping
    for ( LatexPackage pack : AbstractMinimalTypingProofRule
        .getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : DefaultMinimalTypingExpressionProofNode
        .getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : DefaultMinimalTypingTypesProofNode
        .getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : MinimalTypingProofModel.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    // SubTyping
    for ( LatexPackage pack : AbstractSubTypingProofRule
        .getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : DefaultSubTypingProofNode
        .getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : SubTypingProofModel.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    // RecSubTyping
    for ( LatexPackage pack : AbstractRecSubTypingProofRule
        .getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : DefaultSubType.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : DefaultRecSubTypingProofNode
        .getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : RecSubTypingProofModel.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    return packages ;
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
      pBufferedWriter.newLine ( ) ;
    }
    catch ( IOException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.0" ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * Writes a string and a new line to the given {@link BufferedWriter}.
   * 
   * @param pBufferedWriter The {@link BufferedWriter} which should be used.
   * @param pText The text which should be written to the {@link BufferedWriter}.
   * @throws LatexException If an I/O error occurs.
   */
  private final static void println ( BufferedWriter pBufferedWriter ,
      String pText ) throws LatexException
  {
    try
    {
      pBufferedWriter.write ( pText ) ;
      pBufferedWriter.newLine ( ) ;
    }
    catch ( IOException e )
    {
      throw new LatexException ( Messages.getString ( "LatexExport.0" ) ) ; //$NON-NLS-1$
    }
  }
}
