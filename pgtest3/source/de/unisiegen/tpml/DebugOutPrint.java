package de.unisiegen.tpml ;


import java.util.LinkedList ;
import java.util.Properties ;


public class DebugOutPrint
{
  private LinkedList < String > userDebug = new LinkedList < String > ( ) ;


  public DebugOutPrint ( )
  {
    Properties prop = System.getProperties ( ) ;
    this.userDebug.add ( prop.getProperty ( "user.name" ) ) ;
  }


  public void print ( String toPrint , String username )
  {
    boolean print = false ;
    for ( int i = 0 ; i < this.userDebug.size ( ) ; i ++ )
    {
      if ( username.equalsIgnoreCase ( this.userDebug.get ( i ) ) )
      {
        print = true ;
        break ;
      }
    }
    if ( print ) System.out.print ( toPrint ) ;
  }


  public void println ( String toPrint , String username )
  {
    print ( toPrint + "\n" , username ) ;
  }


  public void print ( int toPrint , String username )
  {
    print ( String.valueOf ( toPrint ) , username ) ;
  }


  public void println ( int toPrint , String username )
  {
    print ( String.valueOf ( toPrint ) + "\n" , username ) ;
  }


  public LinkedList < String > getUserDebug ( )
  {
    return this.userDebug ;
  }
}
