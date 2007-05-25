package de.unisiegen.tpml.core.util ;


import de.unisiegen.tpml.core.types.* ;


public class ChristianTest
{
  public static void main ( String [ ] pArguments )
  {
    ArrowType a = new ArrowType ( new ArrowType ( new IntegerType ( ) ,
        new TypeName ( "t" ) ) , new TypeName ( "t" ) ) ;
    System.out.println ( a ) ;
    System.out.println ( a.getTypeNamesFree ( ) ) ;
    ArrowType b = a.substitute ( new TypeName ( "t" ) , new BooleanType ( ) ) ;
    System.out.println ( b ) ;
    System.out.println ( b.getTypeNamesFree ( ) ) ;
  }
}
