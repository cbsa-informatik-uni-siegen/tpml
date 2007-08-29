package de.unisiegen.tpml.core.latex ;


import java.io.BufferedWriter ;
import java.io.File ;
import java.io.FileNotFoundException ;
import java.io.FileOutputStream ;
import java.io.IOException ;
import java.io.OutputStreamWriter ;
import java.io.UnsupportedEncodingException ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.ProofModel ;


/**
 * The main class of the latex export. The export method writes the input
 * {@link LatexPrintable} to the given {@link File}.
 * 
 * @author Christian Fehler
 */
public abstract class LatexExport
{
  /**
   * Writes the input {@link LatexPrintable} to the given {@link File}.
   * 
   * @param pLatexPrintable The input {@link LatexPrintable}.
   * @param pFile The input {@link File}.
   */
  public final static void export ( LatexPrintable pLatexPrintable , File pFile )
  {
    try
    {
      BufferedWriter writer = new BufferedWriter ( new OutputStreamWriter (
          new FileOutputStream ( pFile ) , "UTF8" ) ) ; //$NON-NLS-1$
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
      TreeSet < LatexInstruction > instructions = pLatexPrintable
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
      writer.close ( ) ;
    }
    catch ( UnsupportedEncodingException e )
    {
      e.printStackTrace ( ) ;
    }
    catch ( FileNotFoundException e )
    {
      e.printStackTrace ( ) ;
    }
    catch ( IOException e )
    {
      e.printStackTrace ( ) ;
    }
  }


  /**
   * Writes the input {@link LatexPrintable} to the given {@link File}.
   * 
   * @param pLatexPrintable The input {@link LatexPrintable}.
   * @param pFile The input {@link File}.
   */
  public final static void export ( LatexPrintableNode pLatexPrintable ,
      File pFile )
  {
    try
    {
      BufferedWriter writer = new BufferedWriter ( new OutputStreamWriter (
          new FileOutputStream ( pFile ) , "UTF8" ) ) ; //$NON-NLS-1$
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
      TreeSet < LatexInstruction > instructions = pLatexPrintable
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
      println ( writer , pLatexPrintable.toLatexString ( 0 , 0 ).toString ( ) ) ;
      if ( ! ( pLatexPrintable instanceof ProofModel ) )
      {
        println ( writer , "$" ) ; //$NON-NLS-1$
      }
      // document end
      println ( writer ) ;
      println ( writer , "\\end{landscape}" ) ; //$NON-NLS-1$
      println ( writer , "\\end{document}" ) ; //$NON-NLS-1$
      // close
      writer.close ( ) ;
    }
    catch ( UnsupportedEncodingException e )
    {
      e.printStackTrace ( ) ;
    }
    catch ( FileNotFoundException e )
    {
      e.printStackTrace ( ) ;
    }
    catch ( IOException e )
    {
      e.printStackTrace ( ) ;
    }
  }


  /**
   * Writes a new line to the given {@link BufferedWriter}.
   * 
   * @param pBufferedWriter The {@link BufferedWriter} which should be used.
   * @throws IOException If an I/O error occurs.
   */
  private final static void println ( BufferedWriter pBufferedWriter )
      throws IOException
  {
    pBufferedWriter.newLine ( ) ;
  }


  /**
   * Writes a string and a new line to the given {@link BufferedWriter}.
   * 
   * @param pBufferedWriter The {@link BufferedWriter} which should be used.
   * @param pText The text which should be written to the {@link BufferedWriter}.
   * @throws IOException If an I/O error occurs.
   */
  private final static void println ( BufferedWriter pBufferedWriter ,
      String pText ) throws IOException
  {
    pBufferedWriter.write ( pText ) ;
    pBufferedWriter.newLine ( ) ;
  }
}
