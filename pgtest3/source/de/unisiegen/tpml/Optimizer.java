package de.unisiegen.tpml ;


import java.text.DecimalFormat ;
import java.util.ArrayList ;


/**
 * This class sets time tags and is able to return the free and total memory of
 * the VM.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public final class Optimizer
{
  /**
   * Free and total memory is returned in MegaByte.
   */
  public static final int MEGABYTE = 0 ;


  /**
   * Free and total memory is returned in KiloByte.
   */
  public static final int KILOBYTE = 1 ;


  /**
   * Free and total memory is returned in Byte.
   */
  public static final int BYTE = 2 ;


  /**
   * Time tags are saved in this <code>ArrayList</code>.
   */
  private final ArrayList < Long > timeList = new ArrayList < Long > ( ) ;


  /**
   * Time tag comment are saved in this <code>ArrayList</code>.
   */
  private final ArrayList < String > commentList = new ArrayList < String > ( ) ;


  /**
   * The name of the tags.
   */
  private String tagName = "" ;


  /**
   * This constructor saves the first time tag and the name of the time tags.
   * 
   * @param pName of the tags
   */
  public Optimizer ( final String pName )
  {
    this.tagName = pName ;
    this.timeList.add ( new Long ( System.currentTimeMillis ( ) ) ) ;
    this.commentList.add ( "" ) ;
  }


  /**
   * This methode clears the <code>ArrayList</code> and saves the first time
   * tag.
   * 
   * @param pName Name of the tags.
   */
  public final void restartTag ( final String pName )
  {
    this.tagName = pName ;
    this.timeList.clear ( ) ;
    this.timeList.add ( new Long ( System.currentTimeMillis ( ) ) ) ;
    this.commentList.clear ( ) ;
    this.commentList.add ( "" ) ;
  }


  /**
   * This methode sets a tag with the current time.
   */
  public final void setTimeTag ( )
  {
    this.timeList.add ( new Long ( System.currentTimeMillis ( ) ) ) ;
    this.commentList.add ( "" ) ;
  }


  /**
   * This methode sets a tag with the current time with a comment.
   * 
   * @param pComment Sets the comment of the tag.
   */
  public final void setTimeTag ( final String pComment )
  {
    this.timeList.add ( new Long ( System.currentTimeMillis ( ) ) ) ;
    this.commentList.add ( pComment ) ;
  }


  /**
   * Returns a <code>String</code> of all saved tags with absolut time and
   * percent time.
   * 
   * @return <code>String</code> of all saved tags.
   */
  public final String getTimeTags ( )
  {
    String s = "" ;
    if ( this.timeList.size ( ) > 1 )
    {
      int bc = 0 ;
      for ( final String comment : this.commentList )
      {
        final int j = comment.length ( ) ;
        if ( bc < j )
        {
          bc = j ;
        }
      }
      for ( int i = 0 ; i < this.commentList.size ( ) ; i ++ )
      {
        String t = this.commentList.get ( i ) ;
        final int k = t.length ( ) ;
        for ( int j = 0 ; j < bc - k ; j ++ )
        {
          t = t + " " ;
        }
        this.commentList.set ( i , t ) ;
      }
      final long start = this.timeList.get ( 0 ).longValue ( ) ;
      long end = this.timeList.get ( this.timeList.size ( ) - 1 ).longValue ( )
          - start ;
      if ( end == 0 )
      {
        end = 1 ;
      }
      long last = 0 ;
      final DecimalFormat df1 = new DecimalFormat ( "00" ) ;
      final DecimalFormat df2 = new DecimalFormat ( "00,000 ms" ) ;
      final DecimalFormat df3 = new DecimalFormat ( "00.0" ) ;
      for ( int i = 1 ; i < this.timeList.size ( ) ; i ++ )
      {
        final long current = ( this.timeList.get ( i ).longValue ( ) - start ) ;
        s += this.tagName + " " + df1.format ( i ) + " ("
            + this.commentList.get ( i ) + "):   " + df2.format ( current )
            + "   " + df3.format ( ( current - last ) * 100 / end ) + " %"
            + System.getProperty ( "line.separator" ) ;
        last = current ;
      }
    }
    return s.substring ( 0 , s.length ( ) - 1 ) ;
  }


  /**
   * Returns the total amount of memory in the Java VM.
   * 
   * @param pModus Memory is returned in MB, KB or Byte.
   * @return The total amount of memory in the Java VM.
   */
  public static final int getTotalMemory ( final int pModus )
  {
    final int i = Math.round ( Runtime.getRuntime ( ).totalMemory ( ) ) ;
    if ( KILOBYTE == pModus )
    {
      return i / 1024 ;
    }
    if ( MEGABYTE == pModus )
    {
      return i / 1048576 ;
    }
    return i ;
  }


  /**
   * Returns the amount of free memory in the Java VM.
   * 
   * @param pModus Memory is returned in MB, KB or Byte.
   * @return The amount of free memory in the Java VM.
   */
  public static final int getFreeMemory ( final int pModus )
  {
    final int i = Math.round ( Runtime.getRuntime ( ).freeMemory ( ) ) ;
    if ( KILOBYTE == pModus )
    {
      return i / 1024 ;
    }
    if ( MEGABYTE == pModus )
    {
      return i / 1048576 ;
    }
    return i ;
  }
}
