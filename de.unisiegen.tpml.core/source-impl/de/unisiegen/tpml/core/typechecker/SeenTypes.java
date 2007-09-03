package de.unisiegen.tpml.core.typechecker ;


import java.util.ArrayList ;
import java.util.Iterator ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexInstruction ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.types.Type ;


/**
 * A list of seen {@link Type}s.
 * 
 * @author Christian Fehler
 * @param <E>
 */
public final class SeenTypes < E extends PrettyPrintable & LatexPrintable >
    implements Cloneable , Iterable < E > , PrettyPrintable , LatexPrintable
{
  /**
   * The internal list of seen {@link Type}s.
   */
  private ArrayList < E > list ;


  /**
   * Initializes the seen {@link Type}s.
   */
  public SeenTypes ( )
  {
    this.list = new ArrayList < E > ( ) ;
  }


  /**
   * Inserts the given item to the beginning of the list and removes it before.
   * So the new item is only one time in the list.
   * 
   * @param pItem The item to add.
   */
  public final void add ( E pItem )
  {
    this.list.remove ( pItem ) ;
    this.list.add ( 0 , pItem ) ;
  }


  /**
   * Inserts the given items to the beginning of the list and remove them
   * before. So the new items are only one time in the list.
   * 
   * @param pSeenTypes The {@link SeenTypes} to add.
   */
  public final void addAll ( SeenTypes < E > pSeenTypes )
  {
    for ( int i = pSeenTypes.size ( ) - 1 ; i >= 0 ; i -- )
    {
      E item = pSeenTypes.get ( i ) ;
      this.list.remove ( item ) ;
      this.list.add ( 0 , item ) ;
    }
  }


  /**
   * Returns a shallow copy of this <tt>SeenTypes</tt> instance. (The elements
   * themselves are not copied.)
   * 
   * @return A clone of this <tt>SeenTypes</tt> instance.
   * @see Object#clone()
   */
  @ Override
  public final SeenTypes < E > clone ( )
  {
    SeenTypes < E > newSeenTypes = new SeenTypes < E > ( ) ;
    for ( E item : this.list )
    {
      newSeenTypes.add ( item ) ;
    }
    return newSeenTypes ;
  }


  /**
   * Returns <tt>true</tt> if this {@link SeenTypes} contains the specified
   * item.
   * 
   * @param pItem The item whose presence in this list is to be tested.
   * @return <tt>true</tt> if this list contains the specified item.
   */
  public final boolean contains ( E pItem )
  {
    return this.list.contains ( pItem ) ;
  }


  /**
   * Returns the element at the specified position in this list.
   * 
   * @param pIndex The index of the element to return.
   * @return The element at the specified position in this list.
   */
  public final E get ( int pIndex )
  {
    return this.list.get ( pIndex ) ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_SEEN_TYPES , 1 , "\\{#1\\}" , //$NON-NLS-1$
        "E1, ... , En" ) ) ; //$NON-NLS-1$
    for ( E entry : this.list )
    {
      for ( LatexCommand command : entry.getLatexCommands ( ) )
      {
        commands.add ( command ) ;
      }
    }
    return commands ;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public ArrayList < LatexInstruction > getLatexInstructions ( )
  {
    ArrayList < LatexInstruction > instructions = new ArrayList < LatexInstruction > ( ) ;
    for ( E entry : this.list )
    {
      for ( LatexInstruction instruction : entry.getLatexInstructions ( ) )
      {
        if ( ! instructions.contains ( instruction ) )
        {
          instructions.add ( instruction ) ;
        }
      }
    }
    return instructions ;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public TreeSet < LatexPackage > getLatexPackages ( )
  {
    TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( ) ;
    for ( E entry : this.list )
    {
      for ( LatexPackage pack : entry.getLatexPackages ( ) )
      {
        packages.add ( pack ) ;
      }
    }
    return packages ;
  }


  /**
   * Returns an iterator over the elements in this list in proper sequence.
   * 
   * @return An iterator over the elements in this list in proper sequence.
   * @see Iterable#iterator()
   */
  public Iterator < E > iterator ( )
  {
    return this.list.iterator ( ) ;
  }


  /**
   * Returns the number of elements in this list.
   * 
   * @return The number of elements in this list.
   */
  public final int size ( )
  {
    return this.list.size ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexString()
   */
  public final LatexString toLatexString ( )
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance ( ) , 0 )
        .toLatexString ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  public final LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
  {
    StringBuilder body = new StringBuilder ( ) ;
    body.append ( PRETTY_CLPAREN ) ;
    for ( int i = 0 ; i < this.list.size ( ) ; i ++ )
    {
      body.append ( this.list.get ( i ).toPrettyString ( ).toString ( ) ) ;
      if ( i != this.list.size ( ) - 1 )
      {
        body.append ( PRETTY_COMMA ) ;
        body.append ( PRETTY_SPACE ) ;
      }
    }
    body.append ( PRETTY_CRPAREN ) ;
    String descriptions[] = new String [ 2 + this.list.size ( ) ] ;
    descriptions [ 0 ] = this.toPrettyString ( ).toString ( ) ;
    descriptions [ 1 ] = body.toString ( ) ;
    for ( int i = 0 ; i < this.list.size ( ) ; i ++ )
    {
      descriptions [ 2 + i ] = this.list.get ( i ).toPrettyString ( )
          .toString ( ) ;
    }
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0 ,
        LATEX_SEEN_TYPES , pIndent , descriptions ) ;
    builder.addBuilderBegin ( ) ;
    for ( int i = 0 ; i < this.list.size ( ) ; i ++ )
    {
      builder.addBuilder ( this.list.get ( i ).toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent + LATEX_INDENT * 2 ) , 0 ) ;
      if ( i < this.list.size ( ) - 1 )
      {
        builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
        builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
            + LATEX_INDENT ) ) ;
        builder.addText ( LATEX_COMMA ) ;
        builder.addText ( LATEX_SPACE ) ;
        builder.addBreak ( ) ;
      }
    }
    builder.addBuilderEnd ( ) ;
    return builder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyString()
   */
  public final PrettyString toPrettyString ( )
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance ( ) )
        .toPrettyString ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , 0 ) ;
    builder.addText ( PRETTY_CLPAREN ) ;
    for ( int i = 0 ; i < this.list.size ( ) ; i ++ )
    {
      builder.addBuilder ( this.list.get ( i ).toPrettyStringBuilder (
          pPrettyStringBuilderFactory ) , 0 ) ;
      if ( i != this.list.size ( ) - 1 )
      {
        builder.addText ( PRETTY_COMMA ) ;
        builder.addText ( PRETTY_SPACE ) ;
      }
    }
    builder.addText ( PRETTY_CRPAREN ) ;
    return builder ;
  }


  /**
   * Returns the string representation for this seen types. This method is
   * mainly used for debugging.
   * 
   * @return The pretty printed string representation for this expression.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @ Override
  public final String toString ( )
  {
    return toPrettyString ( ).toString ( ) ;
  }
}
