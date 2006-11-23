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


  public LinkedList < String > getUserDebug ( )
  {
    return this.userDebug ;
  }


  public void print ( boolean toPrint , String username )
  {
    print ( String.valueOf ( toPrint ) , username ) ;
  }


  public void print ( double toPrint , String username )
  {
    print ( String.valueOf ( toPrint ) , username ) ;
  }


  public void print ( float toPrint , String username )
  {
    print ( String.valueOf ( toPrint ) , username ) ;
  }


  public void print ( int toPrint , String username )
  {
    print ( String.valueOf ( toPrint ) , username ) ;
  }


  public void print ( long toPrint , String username )
  {
    print ( String.valueOf ( toPrint ) , username ) ;
  }


  public void print ( short toPrint , String username )
  {
    print ( String.valueOf ( toPrint ) , username ) ;
  }
  
  
  public void print ( Object toPrint , String username )
  {
    print ( String.valueOf ( toPrint.toString() ) , username ) ;
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


  public void println ( boolean toPrint , String username )
  {
    print ( String.valueOf ( toPrint ) + "\n" , username ) ;
  }


  public void println ( double toPrint , String username )
  {
    print ( String.valueOf ( toPrint ) + "\n" , username ) ;
  }


  public void println ( float toPrint , String username )
  {
    print ( String.valueOf ( toPrint ) + "\n" , username ) ;
  }


  public void println ( int toPrint , String username )
  {
    print ( String.valueOf ( toPrint ) + "\n" , username ) ;
  }


  public void println ( long toPrint , String username )
  {
    print ( String.valueOf ( toPrint ) + "\n" , username ) ;
  }


  public void println ( short toPrint , String username )
  {
    print ( String.valueOf ( toPrint ) + "\n" , username ) ;
  }


  public void println ( String toPrint , String username )
  {
    print ( toPrint + "\n" , username ) ;
  }
  
  public void println ( Object toPrint , String username )
  {
    println ( String.valueOf ( toPrint.toString()+"\n" ) , username ) ;
  }


  public void remove ( String username )
  {
    for ( int i = 0 ; i < this.userDebug.size ( ) ; i ++ )
    {
      if ( username.equalsIgnoreCase ( this.userDebug.get ( i ) ) )
      {
        this.userDebug.remove ( i ) ;
        i -- ;
      }
    }
  }
}
