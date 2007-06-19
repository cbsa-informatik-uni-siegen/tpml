package de.unisiegen.tpml.core.util ;


import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.typechecker.SeenTypes ;
import de.unisiegen.tpml.core.typeinference.TypeEquationTypeInference ;
import de.unisiegen.tpml.core.types.ArrowType ;
import de.unisiegen.tpml.core.types.BooleanType ;
import de.unisiegen.tpml.core.types.IntegerType ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.RecType ;
import de.unisiegen.tpml.core.types.RowType ;
import de.unisiegen.tpml.core.types.TypeName ;
import de.unisiegen.tpml.core.types.TypeVariable ;
import de.unisiegen.tpml.graphics.components.ShowBonds ;


public class ChristianTest
{
  public static void main ( String [ ] pArguments )
  {
    testShowBounds ( ) ;
  }


  public static void testShowBounds ( )
  {
    TypeEquationTypeInference t = new TypeEquationTypeInference ( new RecType (
        new TypeName ( "t" ) , new ArrowType ( new IntegerType ( ) ,
            new TypeName ( "t" ) ) ) , new RecType ( new TypeName ( "s" ) ,
        new ArrowType ( new IntegerType ( ) , new TypeName ( "s" ) ) ) ,
        new SeenTypes < TypeEquationTypeInference > ( ) ) ;
    System.out.println ( t ) ;
    ShowBonds s = new ShowBonds ( ) ;
    s.setTypeEquationTypeInference ( t ) ;
    s.getAnnotations ( ) ;
    System.out.println ( s ) ;
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
