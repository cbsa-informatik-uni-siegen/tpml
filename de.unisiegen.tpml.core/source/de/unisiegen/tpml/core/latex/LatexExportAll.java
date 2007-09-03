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
import de.unisiegen.tpml.core.expressions.Expression ;


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
    if ( commands.size ( ) > 0 )
    {
      LatexExport.println ( writer ) ;
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
    for ( LatexInstruction instruction : Expression
        .getLatexInstructionsStatic ( ) )
    {
      instructions.add ( instruction ) ;
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
    for ( LatexPackage pack : Expression.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    for ( LatexPackage pack : Class.getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
    return packages ;
  }
}
