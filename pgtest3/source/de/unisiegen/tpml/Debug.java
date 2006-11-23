package de.unisiegen.tpml ;

import java.util.Properties;


public class Debug
{
  public static DebugOutPrint out = new DebugOutPrint ( ) ;


  public static DebugErrPrint err = new DebugErrPrint ( ) ;


  public static void addUser ( String username )
  {
    out.getUserDebug ( ).add ( username ) ;
    err.getUserDebug ( ).add ( username ) ;
  }
  public static void removeUser(String username)
  {
  	 out.remove ( username ) ;
     err.remove ( username ) ;
  }
  public static void removeMe()
  {
  	Properties prop = System.getProperties ( ) ;
    String username= prop.getProperty ( "user.name" )  ;
  	 out.remove ( username ) ;
     err.remove ( username ) ;
  }
}
