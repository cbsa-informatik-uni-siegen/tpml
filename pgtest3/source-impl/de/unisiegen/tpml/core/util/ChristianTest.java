package de.unisiegen.tpml.core.util ;


import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.types.* ;


public class ChristianTest
{
  public static void main ( String [ ] pArguments )
  {
    rowTypeRemainingRowTypeUnion();
  }


  public static void rowTypeRemainingRowTypeUnion ( )
  {
    RowType a = new RowType ( new Identifier [ ]
    { new Identifier ( "a" ) } , new MonoType [ ]
    { new IntegerType ( ) } , new RowType ( new Identifier [ ]
    { new Identifier ( "b" ) } , new MonoType [ ]
    { new BooleanType ( ) } , new TypeVariable ( 1 , 0 ) ) ) ;
    System.out.println ( a ) ;
    System.out.println ( a.getRemainingRowType ( ) ) ;
  }


  public static void testRecTypeSubstitution ( )
  {
    RecType a = new RecType ( new TypeName ( "t" ) , new ArrowType (
        new ArrowType ( new IntegerType ( ) , new TypeName ( "t" ) ) ,
        new TypeName ( "s" ) ) ) ;
    System.out.println ( a ) ;
    System.out.println ( a.getTypeNamesFree ( ) ) ;
    RecType b = a.substitute ( new TypeName ( "s" ) , new TypeName ( "t" ) ) ;
    System.out.println ( b ) ;
    System.out.println ( b.getTypeNamesFree ( ) ) ;
  }


  public static void testRecType ( )
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
