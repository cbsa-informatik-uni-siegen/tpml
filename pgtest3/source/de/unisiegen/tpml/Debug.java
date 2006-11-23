package de.unisiegen.tpml ;


public class Debug
{
  public static DebugOutPrint out = new DebugOutPrint ( ) ;


  public static DebugErrPrint err = new DebugErrPrint ( ) ;


  public static void addUser ( String username )
  {
    out.getUserDebug ( ).add ( username ) ;
    err.getUserDebug ( ).add ( username ) ;
  }


  public static void removeUser ( String username )
  {
    out.remove ( username ) ;
    err.remove ( username ) ;
  }


  public static void removeMe ( )
  {
    removeUser ( System.getProperties ( ).getProperty ( "user.name" ) ) ;
  }
}
