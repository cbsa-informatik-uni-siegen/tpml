package de.unisiegen.tpml.core.latex ;


import java.io.BufferedWriter ;
import java.io.File ;
import java.io.FileNotFoundException ;
import java.io.FileOutputStream ;
import java.io.IOException ;
import java.io.OutputStreamWriter ;
import java.io.UnsupportedEncodingException ;
import java.nio.charset.Charset ;
import java.util.ArrayList ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.Messages ;
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
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment ;
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
 * This class is used to export all latex commands to one tex file.
 * 
 * @author Christian Fehler
 */
public class LatexExportAll
{
  /**
   * The name of a supported {@link Charset}.
   */
  private static final String CHARSET_NAME = "UTF8" ; //$NON-NLS-1$


  /**
   * Writes all latex commands to the given {@link File}.
   * 
   * @param pFile The input {@link File}.
   * @throws LatexException If something in the latex export does not work.
   */
  public final static void exportAll ( File pFile ) throws LatexException
  {
    BufferedWriter writer ;
    try
    {
      writer = new BufferedWriter ( new OutputStreamWriter (
          new FileOutputStream ( pFile ) , CHARSET_NAME ) ) ;
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
    ArrayList < LatexInstruction > instructions = getAllLatexInstructions ( ) ;
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
    return commands ;
  }


  /**
   * Returns all needed {@link LatexInstruction}s.
   * 
   * @return All needed {@link LatexInstruction}s.
   */
  private static ArrayList < LatexInstruction > getAllLatexInstructions ( )
  {
    ArrayList < LatexInstruction > instructions = new ArrayList < LatexInstruction > ( ) ;
    // Expression
    for ( LatexInstruction instruction : Expression
        .getLatexInstructionsStatic ( ) )
    {
      if ( ! instructions.contains ( instruction ) )
      {
        instructions.add ( instruction ) ;
      }
    }
    // Type
    for ( LatexInstruction instruction : Type.getLatexInstructionsStatic ( ) )
    {
      if ( ! instructions.contains ( instruction ) )
      {
        instructions.add ( instruction ) ;
      }
    }
    // Environment
    for ( LatexInstruction instruction : DefaultTypeEnvironment
        .getLatexInstructionsStatic ( ) )
    {
      if ( ! instructions.contains ( instruction ) )
      {
        instructions.add ( instruction ) ;
      }
    }
    // Store
    for ( LatexInstruction instruction : DefaultStore
        .getLatexInstructionsStatic ( ) )
    {
      if ( ! instructions.contains ( instruction ) )
      {
        instructions.add ( instruction ) ;
      }
    }
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
    return packages ;
  }
}
