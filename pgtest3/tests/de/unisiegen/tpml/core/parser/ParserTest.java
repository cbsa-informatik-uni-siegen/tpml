package de.unisiegen.tpml.core.parser ;


import java.io.StringReader ;
import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException ;
import de.unisiegen.tpml.core.exceptions.LanguageParserWarningException ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.List ;
import de.unisiegen.tpml.core.languages.Language ;
import de.unisiegen.tpml.core.languages.LanguageFactory ;
import de.unisiegen.tpml.core.languages.LanguageParserException ;
import de.unisiegen.tpml.core.languages.NoSuchLanguageException ;
import de.unisiegen.tpml.core.types.Type ;


/**
 * A test class for the expression and type parsers.
 * 
 * @author Christian Fehler
 */
@ SuppressWarnings ( value =
{ "all" } )
public class ParserTest
{
  private enum Output
  {
    NORMAL , ERROR
  }


  private static String IDENTIFIER = "id" ;


  private static String APPLICATION = "id id id" ;


  private static String LAMBDA_1 = "lambda x.x" ;


  private static String LAMBDA_2 = "lambda x:int.x" ;


  private static String LAMBDA_ERROR_1 = "lambda" ;


  private static String LAMBDA_ERROR_2 = "lambda id" ;


  private static String LAMBDA_ERROR_3 = "lambda id." ;


  private static String LAMBDA_ERROR_4 = "lambda id:" ;


  private static String LAMBDA_ERROR_5 = "lambda id:int" ;


  private static String LAMBDA_ERROR_6 = "lambda id:int." ;


  private static String LAMBDA_ERROR_7 = "lambda ." ;


  private static String LAMBDA_ERROR_8 = "lambda .e" ;


  private static String LAMBDA_ERROR_9 = "lambda :" ;


  private static String LAMBDA_ERROR_10 = "lambda : int" ;


  private static String LAMBDA_ERROR_11 = "lambda : int." ;


  private static String LAMBDA_ERROR_12 = "lambda : int.e" ;


  private static String LAMBDA_ERROR_13 = "lambda id e" ;


  private static String LAMBDA_ERROR_14 = "lambda id:int e" ;


  private static String LAMBDA_ERROR_15 = "lambda id int" ;


  private static String LAMBDA_ERROR_16 = "lambda id int." ;


  private static String LAMBDA_ERROR_17 = "lambda id int.e" ;


  private static String LAMBDA_ERROR_18 = "lambda id: ." ;


  private static String LAMBDA_ERROR_19 = "lambda id: .e" ;


  private static String LAMBDA_ERROR_20 = "lambda int" ;


  private static String LAMBDA_ERROR_21 = "lambda int." ;


  private static String LAMBDA_ERROR_22 = "lambda int.e" ;


  private static String LAMBDA_ERROR_23 = "lambda id: 1" ;


  private static String SIMPLE_EXPR_1 = "(id)" ;


  private static String SIMPLE_EXPR_2 = "(+)" ;


  private static String SIMPLE_EXPR_ERROR_1 = "(" ;


  private static String SIMPLE_EXPR_ERROR_2 = "(id" ;


  private static String SIMPLE_EXPR_ERROR_3 = "(+" ;


  private static String INFIX_OPERATION_1 = "1+1" ;


  private static String INFIX_OPERATION_2 = "1-1" ;


  private static String INFIX_OPERATION_3 = "1*1" ;


  private static String INFIX_OPERATION_4 = "1/1" ;


  private static String INFIX_OPERATION_5 = "1 mod 1" ;


  private static String INFIX_OPERATION_6 = "1=1" ;


  private static String INFIX_OPERATION_7 = "1<1" ;


  private static String INFIX_OPERATION_8 = "1>1" ;


  private static String INFIX_OPERATION_9 = "1<=1" ;


  private static String INFIX_OPERATION_10 = "1>=1" ;


  private static String INFIX_OPERATION_11 = "1::1" ;


  private static String INFIX_OPERATION_12 = "1:=1" ;


  private static String INFIX_OPERATION_ERROR_1 = "1+" ;


  private static String INFIX_OPERATION_ERROR_2 = "1-" ;


  private static String INFIX_OPERATION_ERROR_3 = "1*" ;


  private static String INFIX_OPERATION_ERROR_4 = "1/" ;


  private static String INFIX_OPERATION_ERROR_5 = "1 mod " ;


  private static String INFIX_OPERATION_ERROR_6 = "1=" ;


  private static String INFIX_OPERATION_ERROR_7 = "1<" ;


  private static String INFIX_OPERATION_ERROR_8 = "1>" ;


  private static String INFIX_OPERATION_ERROR_9 = "1<=" ;


  private static String INFIX_OPERATION_ERROR_10 = "1>=" ;


  private static String INFIX_OPERATION_ERROR_11 = "1::" ;


  private static String INFIX_OPERATION_ERROR_12 = "1:=" ;


  private static String AND = "true && false" ;


  private static String AND_ERROR = "true &&" ;


  private static String OR = "true || false" ;


  private static String OR_ERROR = "true ||" ;


  private static String CONDITION = "if true then 1 else 2" ;


  private static String CONDITION_ERROR_1 = "if" ;


  private static String CONDITION_ERROR_2 = "if true" ;


  private static String CONDITION_ERROR_3 = "if true then" ;


  private static String CONDITION_ERROR_4 = "if true then 1" ;


  private static String CONDITION_ERROR_5 = "if true then 1 else" ;


  private static String CONDITION_ERROR_6 = "if then" ;


  private static String CONDITION_ERROR_7 = "if then 1" ;


  private static String CONDITION_ERROR_8 = "if then 1 else" ;


  private static String CONDITION_ERROR_9 = "if then 1 else 2" ;


  private static String CONDITION_ERROR_10 = "if true then else" ;


  private static String CONDITION_ERROR_11 = "if true then else 2" ;


  private static String CONDITION_ERROR_12 = "if 1 else" ;


  private static String CONDITION_ERROR_13 = "if 1 else 2" ;


  private static String CONDITION_ERROR_14 = "if else" ;


  private static String CONDITION_ERROR_15 = "if else 2" ;


  private static String LET_1 = "let id = 0 in id" ;


  private static String LET_2 = "let id:int = 0 in id" ;


  private static String LET_ERROR_1 = "let" ;


  private static String LET_ERROR_2 = "let id" ;


  private static String LET_ERROR_3 = "let id:" ;


  private static String LET_ERROR_4 = "let id:int" ;


  private static String LET_ERROR_5 = "let id:int =" ;


  private static String LET_ERROR_6 = "let id:int = 0" ;


  private static String LET_ERROR_7 = "let id:int = 0 in" ;


  private static String LET_ERROR_8 = "let id =" ;


  private static String LET_ERROR_9 = "let id = 0" ;


  private static String LET_ERROR_10 = "let id = 0 in" ;


  private static String LET_ERROR_11 = "let =" ;


  private static String LET_ERROR_12 = "let = 0" ;


  private static String LET_ERROR_13 = "let = 0 in" ;


  private static String LET_ERROR_14 = "let = 0 in id" ;


  private static String LET_ERROR_15 = "let :" ;


  private static String LET_ERROR_16 = "let : int" ;


  private static String LET_ERROR_17 = "let : int =" ;


  private static String LET_ERROR_18 = "let : int = 0" ;


  private static String LET_ERROR_19 = "let : int = 0 in" ;


  private static String LET_ERROR_20 = "let : int = 0 in id" ;


  private static String LET_ERROR_21 = "let id: int 0" ;


  private static String LET_ERROR_22 = "let id: int 0 in" ;


  private static String LET_ERROR_23 = "let id: int 0 in id" ;


  private static String LET_ERROR_24 = "let id int" ;


  private static String LET_ERROR_25 = "let id int =" ;


  private static String LET_ERROR_26 = "let id int = 0" ;


  private static String LET_ERROR_27 = "let id int = 0 in" ;


  private static String LET_ERROR_28 = "let id int = 0 in id" ;


  private static String LET_ERROR_29 = "let id: =" ;


  private static String LET_ERROR_30 = "let id: = 0" ;


  private static String LET_ERROR_31 = "let id: = 0 in" ;


  private static String LET_ERROR_32 = "let id: = 0 in id" ;


  private static String LET_ERROR_33 = "let id = in" ;


  private static String LET_ERROR_34 = "let id = in id" ;


  private static String LET_ERROR_35 = "let id: int = in" ;


  private static String LET_ERROR_36 = "let id: int = in id" ;


  private static String LET_ERROR_37 = "let id in" ;


  private static String LET_ERROR_38 = "let id in id" ;


  private static String LET_ERROR_39 = "let id: int in" ;


  private static String LET_ERROR_40 = "let id: int in id" ;


  private static String LET_ERROR_41 = "let int" ;


  private static String LET_ERROR_42 = "let int =" ;


  private static String LET_ERROR_43 = "let int = 0" ;


  private static String LET_ERROR_44 = "let int = 0 in" ;


  private static String LET_ERROR_45 = "let int = 0 in id" ;


  private static String LET_ERROR_46 = "let id: 0" ;


  private static String LET_ERROR_47 = "let id: 0 in" ;


  private static String LET_ERROR_48 = "let id: 0 in id" ;


  private static String LET_ERROR_49 = "let in" ;


  private static String LET_ERROR_50 = "let in id" ;


  private static String LET_ERROR_51 = "let id: in" ;


  private static String LET_ERROR_52 = "let id: in id" ;


  private static String CURRIED_LET_1 = "let f a b = a + b in f" ;


  private static String CURRIED_LET_2 = "let f (a:int) (b:int) = a + b in f" ;


  private static String CURRIED_LET_3 = "let f a b:int = a + b in f" ;


  private static String CURRIED_LET_4 = "let f (a:int) (b:int):int = a + b in f" ;


  private static String CURRIED_LET_ERROR_1 = "let f a b" ;


  private static String CURRIED_LET_ERROR_2 = "let f a b:" ;


  private static String CURRIED_LET_ERROR_3 = "let f a b:int" ;


  private static String CURRIED_LET_ERROR_4 = "let f a b:int = " ;


  private static String CURRIED_LET_ERROR_5 = "let f a b:int = 1 + 2" ;


  private static String CURRIED_LET_ERROR_6 = "let f a b:int = 1 + 2 in" ;


  private static String CURRIED_LET_ERROR_7 = "let f a b =" ;


  private static String CURRIED_LET_ERROR_8 = "let f a b = 1 + 2" ;


  private static String CURRIED_LET_ERROR_9 = "let f a b = 1 + 2 in" ;


  private static String CURRIED_LET_ERROR_10 = "let f (" ;


  private static String CURRIED_LET_ERROR_11 = "let f (a" ;


  private static String CURRIED_LET_ERROR_12 = "let f (a:" ;


  private static String CURRIED_LET_ERROR_13 = "let f (a:int" ;


  private static String CURRIED_LET_ERROR_14 = "let f a b: = 1 + 2 in f" ;


  private static String CURRIED_LET_ERROR_15 = "let f (a:) b = 1 + 2 in f" ;


  private static String CURRIED_LET_ERROR_16 = "let f a b = in" ;


  private static String CURRIED_LET_ERROR_17 = "let f a b = in f" ;


  private static String CURRIED_LET_ERROR_18 = "let f a b: int = in" ;


  private static String CURRIED_LET_ERROR_19 = "let f a b: int = in f" ;


  private static String CURRIED_LET_ERROR_20 = "let f a b int" ;


  private static String CURRIED_LET_ERROR_21 = "let f a b int =" ;


  private static String CURRIED_LET_ERROR_22 = "let f a b int = 1 + 2" ;


  private static String CURRIED_LET_ERROR_23 = "let f a b int = 1 + 2 in" ;


  private static String CURRIED_LET_ERROR_24 = "let f a b int = 1 + 2 in f" ;


  private static String CURRIED_LET_ERROR_25 = "let f a b: =" ;


  private static String CURRIED_LET_ERROR_26 = "let f a b: = 1 + 2" ;


  private static String CURRIED_LET_ERROR_27 = "let f a b: = 1 + 2 in" ;


  private static String CURRIED_LET_ERROR_28 = "let f a b: = 1 + 2 in f" ;


  private static String CURRIED_LET_ERROR_29 = "let f a b: int 1 + 2" ;


  private static String CURRIED_LET_ERROR_30 = "let f a b: int 1 + 2 in" ;


  private static String CURRIED_LET_ERROR_31 = "let f a b: int 1 + 2 in f" ;


  private static String CURRIED_LET_ERROR_32 = "let f a b in" ;


  private static String CURRIED_LET_ERROR_33 = "let f a b in f" ;


  private static String CURRIED_LET_ERROR_34 = "let f a b: int in" ;


  private static String CURRIED_LET_ERROR_35 = "let f a b: int in f" ;


  private static String CURRIED_LET_ERROR_36 = "let f a b: 1 + 2" ;


  private static String CURRIED_LET_ERROR_37 = "let f a b:1 + 2 in" ;


  private static String CURRIED_LET_ERROR_38 = "let f a b: 1 + 2 in f" ;


  private static String CURRIED_LET_ERROR_39 = "let f a b: in f" ;


  private static String CURRIED_LET_ERROR_40 = "let f a b: in f" ;


  private static String COERCION = "(e: int <: int)" ;


  private static String COERCION_ERROR_1 = "(e:" ;


  private static String COERCION_ERROR_2 = "(e: int" ;


  private static String COERCION_ERROR_3 = "(e: int <:" ;


  private static String COERCION_ERROR_4 = "(e: int <: int" ;


  private static String COERCION_ERROR_5 = "(e int" ;


  private static String COERCION_ERROR_6 = "(e int <:" ;


  private static String COERCION_ERROR_7 = "(e int <: int" ;


  private static String COERCION_ERROR_8 = "(e int <: int)" ;


  private static String COERCION_ERROR_9 = "(e: <:" ;


  private static String COERCION_ERROR_10 = "(e: <: int" ;


  private static String COERCION_ERROR_11 = "(e: <: int)" ;


  private static String COERCION_ERROR_12 = "(e: int int" ;


  private static String COERCION_ERROR_13 = "(e: int int)" ;


  private static String COERCION_ERROR_14 = "(e: int <: )" ;


  private static String COERCION_ERROR_15 = "(e <:" ;


  private static String COERCION_ERROR_16 = "(e <: int" ;


  private static String COERCION_ERROR_17 = "(e <: int)" ;


  private static String COERCION_ERROR_18 = "(e: int )" ;


  private static String COERCION_ERROR_19 = "(e: )" ;


  private static String UNIT = "()" ;


  private static String BOOLEAN_1 = "true" ;


  private static String BOOLEAN_2 = "false" ;


  private static String INTEGER = "1" ;


  private static String NOT = "not" ;


  private static String OPERATOR_1 = "(+)" ;


  private static String OPERATOR_2 = "(-)" ;


  private static String OPERATOR_3 = "( *)" ;


  private static String OPERATOR_4 = "(/)" ;


  private static String OPERATOR_5 = "(mod)" ;


  private static String OPERATOR_6 = "(=)" ;


  private static String OPERATOR_7 = "(<)" ;


  private static String OPERATOR_8 = "(>)" ;


  private static String OPERATOR_9 = "(<=)" ;


  private static String OPERATOR_10 = "(>=)" ;


  private static String OPERATOR_11 = "(::)" ;


  private static String OPERATOR_12 = "(:=)" ;


  private static String EXPR_SIMPLE_TYPE = "lambda x:(int).x" ;


  private static String EXPR_SIMPLE_TYPE_ERROR_1 = "lambda x:(" ;


  private static String EXPR_SIMPLE_TYPE_ERROR_2 = "lambda x:(int" ;


  private static String EXPR_BOOLEAN_TYPE = "lambda x:bool.x" ;


  private static String EXPR_INTEGER_TYPE = "lambda x:int.x" ;


  private static String EXPR_UNIT_TYPE = "lambda x:unit.x" ;


  private static String EXPR_TYPE_VARIABLE = "lambda x:a'.x" ;


  private static String EXPR_TYPE_NAME = "lambda x:t.x" ;


  private static String EXPR_REC_TYPE = "lambda x:mu t.int -> t.x" ;


  private static String EXPR_ARROW_TYPE = "lambda x:int -> int.x" ;


  private static String EXPR_ARROW_TYPE_ERROR = "lambda x:int ->" ;


  private static String EXPR_REC_TYPE_ERROR_1 = "lambda x:mu" ;


  private static String EXPR_REC_TYPE_ERROR_2 = "lambda x:mu t" ;


  private static String EXPR_REC_TYPE_ERROR_3 = "lambda x:mu t." ;


  private static String SIMPLE_TYPE = "(int)" ;


  private static String SIMPLE_TYPE_ERROR_1 = "(" ;


  private static String SIMPLE_TYPE_ERROR_2 = "(int" ;


  private static String BOOLEAN_TYPE = "bool" ;


  private static String INTEGER_TYPE = "int" ;


  private static String UNIT_TYPE = "unit" ;


  private static String TYPE_VARIABLE = "a'" ;


  private static String TYPE_NAME = "t" ;


  private static String REC_TYPE = "mu t.int -> t" ;


  private static String ARROW_TYPE = "int -> int" ;


  private static String ARROW_TYPE_ERROR = "int ->" ;


  private static String REC_TYPE_ERROR_1 = "mu" ;


  private static String REC_TYPE_ERROR_2 = "mu t" ;


  private static String REC_TYPE_ERROR_3 = "mu t." ;


  private static String REC_1 = "rec id.id" ;


  private static String REC_2 = "rec id:int.id" ;


  private static String REC_ERROR_1 = "rec" ;


  private static String REC_ERROR_2 = "rec id" ;


  private static String REC_ERROR_3 = "rec id:" ;


  private static String REC_ERROR_4 = "rec id:int" ;


  private static String REC_ERROR_5 = "rec id:int." ;


  private static String REC_ERROR_6 = "rec id." ;


  private static String REC_ERROR_7 = "rec ." ;


  private static String REC_ERROR_8 = "rec .1" ;


  private static String REC_ERROR_9 = "rec : int." ;


  private static String REC_ERROR_10 = "rec : int.1" ;


  private static String REC_ERROR_11 = "rec id 1" ;


  private static String REC_ERROR_12 = "rec id: int 1" ;


  private static String REC_ERROR_13 = "rec id int" ;


  private static String REC_ERROR_14 = "rec id int." ;


  private static String REC_ERROR_15 = "rec id int.1" ;


  private static String REC_ERROR_16 = "rec id: ." ;


  private static String REC_ERROR_17 = "rec id: .1" ;


  private static String REC_ERROR_18 = "rec 1" ;


  private static String REC_ERROR_19 = "rec int" ;


  private static String REC_ERROR_20 = "rec int." ;


  private static String REC_ERROR_21 = "rec int.1" ;


  private static String REC_ERROR_22 = "rec id: 1" ;


  private static String LET_REC_1 = "let rec id = 0 in id" ;


  private static String LET_REC_2 = "let rec id:int = 0 in id" ;


  private static String LET_REC_ERROR_1 = "let rec" ;


  private static String LET_REC_ERROR_2 = "let rec id" ;


  private static String LET_REC_ERROR_3 = "let rec id:" ;


  private static String LET_REC_ERROR_4 = "let rec id:int" ;


  private static String LET_REC_ERROR_5 = "let rec id:int =" ;


  private static String LET_REC_ERROR_6 = "let rec id:int = 0" ;


  private static String LET_REC_ERROR_7 = "let rec id:int = 0 in" ;


  private static String LET_REC_ERROR_8 = "let rec id =" ;


  private static String LET_REC_ERROR_9 = "let rec id = 0" ;


  private static String LET_REC_ERROR_10 = "let rec id = 0 in" ;


  private static String LET_REC_ERROR_11 = "let rec =" ;


  private static String LET_REC_ERROR_12 = "let rec = 0" ;


  private static String LET_REC_ERROR_13 = "let rec = 0 in" ;


  private static String LET_REC_ERROR_14 = "let rec = 0 in id" ;


  private static String LET_REC_ERROR_15 = "let rec :" ;


  private static String LET_REC_ERROR_16 = "let rec : int" ;


  private static String LET_REC_ERROR_17 = "let rec : int =" ;


  private static String LET_REC_ERROR_18 = "let rec : int = 0" ;


  private static String LET_REC_ERROR_19 = "let rec : int = 0 in" ;


  private static String LET_REC_ERROR_20 = "let rec : int = 0 in id" ;


  private static String LET_REC_ERROR_21 = "let rec id: int 0" ;


  private static String LET_REC_ERROR_22 = "let rec id: int 0 in" ;


  private static String LET_REC_ERROR_23 = "let rec id: int 0 in id" ;


  private static String LET_REC_ERROR_24 = "let rec id int" ;


  private static String LET_REC_ERROR_25 = "let rec id int =" ;


  private static String LET_REC_ERROR_26 = "let rec id int = 0" ;


  private static String LET_REC_ERROR_27 = "let rec id int = 0 in" ;


  private static String LET_REC_ERROR_28 = "let rec id int = 0 in id" ;


  private static String LET_REC_ERROR_29 = "let rec id: =" ;


  private static String LET_REC_ERROR_30 = "let rec id: = 0" ;


  private static String LET_REC_ERROR_31 = "let rec id: = 0 in" ;


  private static String LET_REC_ERROR_32 = "let rec id: = 0 in id" ;


  private static String LET_REC_ERROR_33 = "let rec id = in" ;


  private static String LET_REC_ERROR_34 = "let rec id = in id" ;


  private static String LET_REC_ERROR_35 = "let rec id: int = in" ;


  private static String LET_REC_ERROR_36 = "let rec id: int = in id" ;


  private static String LET_REC_ERROR_37 = "let rec id in" ;


  private static String LET_REC_ERROR_38 = "let rec id in id" ;


  private static String LET_REC_ERROR_39 = "let rec id: int in" ;


  private static String LET_REC_ERROR_40 = "let rec id: int in id" ;


  private static String LET_REC_ERROR_41 = "let rec int" ;


  private static String LET_REC_ERROR_42 = "let rec int =" ;


  private static String LET_REC_ERROR_43 = "let rec int = 0" ;


  private static String LET_REC_ERROR_44 = "let rec int = 0 in" ;


  private static String LET_REC_ERROR_45 = "let rec int = 0 in id" ;


  private static String LET_REC_ERROR_46 = "let rec id: 0" ;


  private static String LET_REC_ERROR_47 = "let rec id: 0 in" ;


  private static String LET_REC_ERROR_48 = "let rec id: 0 in id" ;


  private static String LET_REC_ERROR_49 = "let rec in" ;


  private static String LET_REC_ERROR_50 = "let rec in id" ;


  private static String LET_REC_ERROR_51 = "let rec id: in" ;


  private static String LET_REC_ERROR_52 = "let rec id: in id" ;


  private static String LET_REC_ERROR_53 = "let rec 0" ;


  private static String LET_REC_ERROR_54 = "let rec 0 in" ;


  private static String LET_REC_ERROR_55 = "let rec 0 in id" ;


  private static String CURRIED_LET_REC_1 = "let rec f a b = a + b in f" ;


  private static String CURRIED_LET_REC_2 = "let rec f (a:int) (b:int) = a + b in f" ;


  private static String CURRIED_LET_REC_3 = "let rec f a b:int = a + b in f" ;


  private static String CURRIED_LET_REC_4 = "let rec f (a:int) (b:int):int = a + b in f" ;


  private static String CURRIED_LET_REC_ERROR_1 = "let rec f a b" ;


  private static String CURRIED_LET_REC_ERROR_2 = "let rec f a b:" ;


  private static String CURRIED_LET_REC_ERROR_3 = "let rec f a b:int" ;


  private static String CURRIED_LET_REC_ERROR_4 = "let rec f a b:int = " ;


  private static String CURRIED_LET_REC_ERROR_5 = "let rec f a b:int = 1 + 2" ;


  private static String CURRIED_LET_REC_ERROR_6 = "let rec f a b:int = 1 + 2 in" ;


  private static String CURRIED_LET_REC_ERROR_7 = "let rec f a b =" ;


  private static String CURRIED_LET_REC_ERROR_8 = "let rec f a b = 1 + 2" ;


  private static String CURRIED_LET_REC_ERROR_9 = "let rec f a b = 1 + 2 in" ;


  private static String CURRIED_LET_REC_ERROR_10 = "let rec f (" ;


  private static String CURRIED_LET_REC_ERROR_11 = "let rec f (a" ;


  private static String CURRIED_LET_REC_ERROR_12 = "let rec f (a:" ;


  private static String CURRIED_LET_REC_ERROR_13 = "let rec f (a:int" ;


  private static String CURRIED_LET_REC_ERROR_14 = "let rec f a b: = 1 + 2 in f" ;


  private static String CURRIED_LET_REC_ERROR_15 = "let rec f (a:) b = 1 + 2 in f" ;


  private static String CURRIED_LET_REC_ERROR_16 = "let rec f a b = in" ;


  private static String CURRIED_LET_REC_ERROR_17 = "let rec f a b = in f" ;


  private static String CURRIED_LET_REC_ERROR_18 = "let rec f a b: int = in" ;


  private static String CURRIED_LET_REC_ERROR_19 = "let rec f a b: int = in f" ;


  private static String CURRIED_LET_REC_ERROR_20 = "let rec f a b int" ;


  private static String CURRIED_LET_REC_ERROR_21 = "let rec f a b int =" ;


  private static String CURRIED_LET_REC_ERROR_22 = "let rec f a b int = 1 + 2" ;


  private static String CURRIED_LET_REC_ERROR_23 = "let rec f a b int = 1 + 2 in" ;


  private static String CURRIED_LET_REC_ERROR_24 = "let rec f a b int = 1 + 2 in f" ;


  private static String CURRIED_LET_REC_ERROR_25 = "let rec f a b: =" ;


  private static String CURRIED_LET_REC_ERROR_26 = "let rec f a b: = 1 + 2" ;


  private static String CURRIED_LET_REC_ERROR_27 = "let rec f a b: = 1 + 2 in" ;


  private static String CURRIED_LET_REC_ERROR_28 = "let rec f a b: = 1 + 2 in f" ;


  private static String CURRIED_LET_REC_ERROR_29 = "let rec f a b: int 1 + 2" ;


  private static String CURRIED_LET_REC_ERROR_30 = "let rec f a b: int 1 + 2 in" ;


  private static String CURRIED_LET_REC_ERROR_31 = "let rec f a b: int 1 + 2 in f" ;


  private static String CURRIED_LET_REC_ERROR_32 = "let rec f a b in" ;


  private static String CURRIED_LET_REC_ERROR_33 = "let rec f a b in f" ;


  private static String CURRIED_LET_REC_ERROR_34 = "let rec f a b: int in" ;


  private static String CURRIED_LET_REC_ERROR_35 = "let rec f a b: int in f" ;


  private static String CURRIED_LET_REC_ERROR_36 = "let rec f a b: 1 + 2" ;


  private static String CURRIED_LET_REC_ERROR_37 = "let rec f a b: 1 + 2 in" ;


  private static String CURRIED_LET_REC_ERROR_38 = "let rec f a b: 1 + 2 in f" ;


  private static String CURRIED_LET_REC_ERROR_39 = "let rec f a b: in f" ;


  private static String CURRIED_LET_REC_ERROR_40 = "let rec f a b: in f" ;


  private static String MULTI_LAMBDA_1 = "lambda (x,y).x + y" ;


  private static String MULTI_LAMBDA_2 = "lambda (x,y):int.x + y" ;


  private static String MULTI_LAMBDA_ERROR_1 = "lambda (" ;


  private static String MULTI_LAMBDA_ERROR_2 = "lambda (x,y" ;


  private static String MULTI_LAMBDA_ERROR_3 = "lambda (x,y)" ;


  private static String MULTI_LAMBDA_ERROR_4 = "lambda (x,y):" ;


  private static String MULTI_LAMBDA_ERROR_5 = "lambda (x,y):int" ;


  private static String MULTI_LAMBDA_ERROR_6 = "lambda (x,y):int." ;


  private static String MULTI_LAMBDA_ERROR_7 = "lambda (x,y)." ;


  private static String MULTI_LAMBDA_ERROR_8 = "lambda (x," ;


  private static String MULTI_LAMBDA_ERROR_9 = "lambda (x,y ." ;


  private static String MULTI_LAMBDA_ERROR_10 = "lambda (x,y .x + y" ;


  private static String MULTI_LAMBDA_ERROR_11 = "lambda (x,y :" ;


  private static String MULTI_LAMBDA_ERROR_12 = "lambda (x,y : int" ;


  private static String MULTI_LAMBDA_ERROR_13 = "lambda (x,y : int." ;


  private static String MULTI_LAMBDA_ERROR_14 = "lambda (x,y : int.x + y" ;


  private static String MULTI_LAMBDA_ERROR_15 = "lambda (x,y) x + y" ;


  private static String MULTI_LAMBDA_ERROR_16 = "lambda (x,y): int x + y" ;


  private static String MULTI_LAMBDA_ERROR_17 = "lambda (x,y) int" ;


  private static String MULTI_LAMBDA_ERROR_18 = "lambda (x,y) int." ;


  private static String MULTI_LAMBDA_ERROR_19 = "lambda (x,y) int.x + y" ;


  private static String MULTI_LAMBDA_ERROR_20 = "lambda (x,y): ." ;


  private static String MULTI_LAMBDA_ERROR_21 = "lambda (x,y): .x + y" ;


  private static String MULTI_LAMBDA_ERROR_22 = "lambda ( ." ;


  private static String MULTI_LAMBDA_ERROR_23 = "lambda ( .x + y" ;


  private static String MULTI_LAMBDA_ERROR_24 = "lambda ( :" ;


  private static String MULTI_LAMBDA_ERROR_25 = "lambda ( : int" ;


  private static String MULTI_LAMBDA_ERROR_26 = "lambda ( : int." ;


  private static String MULTI_LAMBDA_ERROR_27 = "lambda ( : int.x + y" ;


  private static String MULTI_LAMBDA_ERROR_28 = "lambda (x,y int" ;


  private static String MULTI_LAMBDA_ERROR_29 = "lambda (x,y int." ;


  private static String MULTI_LAMBDA_ERROR_30 = "lambda (x,y int.x + y" ;


  private static String MULTI_LAMBDA_ERROR_31 = "lambda (x,y): x + y" ;


  private static String MULTI_LAMBDA_ERROR_32 = "lambda ( x + y" ;


  private static String MULTI_LAMBDA_ERROR_33 = "lambda ( int" ;


  private static String MULTI_LAMBDA_ERROR_34 = "lambda ( int." ;


  private static String MULTI_LAMBDA_ERROR_35 = "lambda ( int.x + y" ;


  private static String MULTI_LET_1 = "let (x,y) = (1,2) in x+y" ;


  private static String MULTI_LET_2 = "let (x,y):int = (1,2) in x+y" ;


  private static String MULTI_LET_ERROR_1 = "let (" ;


  private static String MULTI_LET_ERROR_2 = "let (x,y" ;


  private static String MULTI_LET_ERROR_3 = "let (x,y)" ;


  private static String MULTI_LET_ERROR_4 = "let (x,y):" ;


  private static String MULTI_LET_ERROR_5 = "let (x,y):int" ;


  private static String MULTI_LET_ERROR_6 = "let (x,y):int =" ;


  private static String MULTI_LET_ERROR_7 = "let (x,y):int = (1,2)" ;


  private static String MULTI_LET_ERROR_8 = "let (x,y):int = (1,2) in" ;


  private static String MULTI_LET_ERROR_9 = "let (x,y) =" ;


  private static String MULTI_LET_ERROR_10 = "let (x,y) = (1,2)" ;


  private static String MULTI_LET_ERROR_11 = "let (x,y) = (1,2) in" ;


  private static String MULTI_LET_ERROR_12 = "let (x," ;


  private static String MULTI_LET_ERROR_13 = "let (x,y =" ;


  private static String MULTI_LET_ERROR_14 = "let (x,y = 1" ;


  private static String MULTI_LET_ERROR_15 = "let (x,y = 1 in" ;


  private static String MULTI_LET_ERROR_16 = "let (x,y = 1 in 2" ;


  private static String MULTI_LET_ERROR_17 = "let (x,y :" ;


  private static String MULTI_LET_ERROR_18 = "let (x,y : int" ;


  private static String MULTI_LET_ERROR_19 = "let (x,y : int =" ;


  private static String MULTI_LET_ERROR_20 = "let (x,y : int = 1" ;


  private static String MULTI_LET_ERROR_21 = "let (x,y : int = 1 in" ;


  private static String MULTI_LET_ERROR_22 = "let (x,y : int = 1 in 2" ;


  private static String MULTI_LET_ERROR_23 = "let (x,y) 1" ;


  private static String MULTI_LET_ERROR_24 = "let (x,y) 1 in" ;


  private static String MULTI_LET_ERROR_25 = "let (x,y) 1 in 2" ;


  private static String MULTI_LET_ERROR_26 = "let (x,y): int 1" ;


  private static String MULTI_LET_ERROR_27 = "let (x,y): int 1 in" ;


  private static String MULTI_LET_ERROR_28 = "let (x,y): int 1 in 2" ;


  private static String MULTI_LET_ERROR_29 = "let (x,y) = in" ;


  private static String MULTI_LET_ERROR_30 = "let (x,y) = in 2" ;


  private static String MULTI_LET_ERROR_31 = "let (x,y): int = in" ;


  private static String MULTI_LET_ERROR_32 = "let (x,y): int = in 2" ;


  private static String MULTI_LET_ERROR_33 = "let (x,y) int" ;


  private static String MULTI_LET_ERROR_34 = "let (x,y) int =" ;


  private static String MULTI_LET_ERROR_35 = "let (x,y) int = 1" ;


  private static String MULTI_LET_ERROR_36 = "let (x,y) int = 1 in" ;


  private static String MULTI_LET_ERROR_37 = "let (x,y) int = 1 in 2" ;


  private static String MULTI_LET_ERROR_38 = "let (x,y): =" ;


  private static String MULTI_LET_ERROR_39 = "let (x,y): = 1" ;


  private static String MULTI_LET_ERROR_40 = "let (x,y): = 1 in" ;


  private static String MULTI_LET_ERROR_41 = "let (x,y): = 1 in 2" ;


  private static String MULTI_LET_ERROR_42 = "let ( =" ;


  private static String MULTI_LET_ERROR_43 = "let ( = 1" ;


  private static String MULTI_LET_ERROR_44 = "let ( = 1 in" ;


  private static String MULTI_LET_ERROR_45 = "let ( = 1 in 2" ;


  private static String MULTI_LET_ERROR_46 = "let ( :" ;


  private static String MULTI_LET_ERROR_47 = "let ( : int" ;


  private static String MULTI_LET_ERROR_48 = "let ( : int =" ;


  private static String MULTI_LET_ERROR_49 = "let ( : int = 1" ;


  private static String MULTI_LET_ERROR_50 = "let ( : int = 1 in" ;


  private static String MULTI_LET_ERROR_51 = "let ( : int = 1 in 2" ;


  private static String MULTI_LET_ERROR_52 = "let (x,y) in" ;


  private static String MULTI_LET_ERROR_53 = "let (x,y) in 2" ;


  private static String MULTI_LET_ERROR_54 = "let (x,y): int in" ;


  private static String MULTI_LET_ERROR_55 = "let (x,y): int in 2" ;


  private static String MULTI_LET_ERROR_56 = "let (x,y int" ;


  private static String MULTI_LET_ERROR_57 = "let (x,y int =" ;


  private static String MULTI_LET_ERROR_58 = "let (x,y int = 1" ;


  private static String MULTI_LET_ERROR_59 = "let (x,y int = 1 in" ;


  private static String MULTI_LET_ERROR_60 = "let (x,y int = 1 in 2" ;


  private static String MULTI_LET_ERROR_61 = "let (x,y): 1" ;


  private static String MULTI_LET_ERROR_62 = "let (x,y): 1 in" ;


  private static String MULTI_LET_ERROR_63 = "let (x,y): 1 in 2" ;


  private static String MULTI_LET_ERROR_64 = "let (x,y in" ;


  private static String MULTI_LET_ERROR_65 = "let (x,y in 2" ;


  private static String MULTI_LET_ERROR_66 = "let ( int" ;


  private static String MULTI_LET_ERROR_67 = "let ( int =" ;


  private static String MULTI_LET_ERROR_68 = "let ( int = 1" ;


  private static String MULTI_LET_ERROR_69 = "let ( int = 1 in" ;


  private static String MULTI_LET_ERROR_70 = "let ( int = 1 in 2" ;


  private static String MULTI_LET_ERROR_71 = "let (x,y): in" ;


  private static String MULTI_LET_ERROR_72 = "let (x,y): in 2" ;


  private static String MULTI_LET_ERROR_73 = "let (x,): in 2" ;


  private static String MULTI_LET_ERROR_74 = "let (x,y,): in 2" ;


  private static String FST = "fst" ;


  private static String SND = "snd" ;


  private static String CONS = "cons" ;


  private static String IS_EMPTY = "is_empty" ;


  private static String HD = "hd" ;


  private static String TL = "tl" ;


  private static String EMPTY_LIST = "[]" ;


  private static String PROJECTION = "#3_1" ;


  private static String TUPLE = "(1,2,3)" ;


  private static String TUPLE_ERROR_1 = "(1,2,3" ;


  private static String TUPLE_ERROR_2 = "(1," ;


  private static String TUPLE_ERROR_3 = "(1,2," ;


  private static String TUPLE_ERROR_4 = "(1,)" ;


  private static String TUPLE_ERROR_5 = "(1,2,)" ;


  private static String LIST = "[1;2;3]" ;


  private static String LIST_ERROR_1 = "[" ;


  private static String LIST_ERROR_2 = "[1;2;3" ;


  private static String LIST_ERROR_3 = "[1;2;" ;


  private static String LIST_ERROR_4 = "[1;]" ;


  private static String LIST_ERROR_5 = "[1;2;]" ;


  private static String SEQUENCE_1 = "1+1;2+2" ;


  private static String SEQUENCE_2 = "1+1;2+2;3+3" ;


  private static String SEQUENCE_ERROR_1 = "1+1;" ;


  private static String SEQUENCE_ERROR_2 = "1+1;2+2;" ;


  private static String EXPR_TUPLE_TYPE_1 = "lambda x:int*int*int.x" ;


  private static String EXPR_TUPLE_TYPE_2 = "lambda x:int*int.x" ;


  private static String EXPR_TUPLE_TYPE_ERROR_1 = "lambda x:int*int*" ;


  private static String EXPR_TUPLE_TYPE_ERROR_2 = "lambda x:int*" ;


  private static String EXPR_LIST_TYPE = "lambda x:int list.x" ;


  private static String TUPLE_TYPE_1 = "int*int*int" ;


  private static String TUPLE_TYPE_2 = "int*int" ;


  private static String TUPLE_TYPE_ERROR_1 = "int*int*" ;


  private static String TUPLE_TYPE_ERROR_2 = "int*" ;


  private static String LIST_TYPE = "int list" ;


  private static String CONDITION1 = "if true then 1" ;


  private static String WHILE = "while true do 1" ;


  private static String WHILE_ERROR_1 = "while" ;


  private static String WHILE_ERROR_2 = "while true" ;


  private static String WHILE_ERROR_3 = "while true do" ;


  private static String REF = "ref" ;


  private static String DEREF = "!" ;


  private static String EXPR_REF_TYPE = "lambda x:int ref.x" ;


  private static String REF_TYPE = "int ref" ;


  private static String SELF = "self" ;


  private static String OBJECT_1 = "object (self) end" ;


  private static String OBJECT_2 = "object (self:<>) end" ;


  private static String OBJECT_3 = "object (self) method add = 0 ; end" ;


  private static String OBJECT_4 = "object (self:<add:int;>) method add = 0 ; end" ;


  private static String OBJECT_ERROR_1 = "object" ;


  private static String OBJECT_ERROR_2 = "object (" ;


  private static String OBJECT_ERROR_3 = "object (self" ;


  private static String OBJECT_ERROR_4 = "object (self:" ;


  private static String OBJECT_ERROR_5 = "object (self:<add:int;>" ;


  private static String OBJECT_ERROR_6 = "object (self:<add:int;>)" ;


  private static String OBJECT_ERROR_7 = "object (self:<add:int;>) method add = 1 ;" ;


  private static String OBJECT_ERROR_8 = "object (self)" ;


  private static String OBJECT_ERROR_9 = "object (self) method add = 1 ;" ;


  // left paren
  private static String OBJECT_ERROR_10 = "object self" ;


  private static String OBJECT_ERROR_11 = "object self)" ;


  private static String OBJECT_ERROR_12 = "object self) end" ;


  private static String OBJECT_ERROR_13 = "object self) method add = 1 ;" ;


  private static String OBJECT_ERROR_14 = "object self) method add = 1 ; end" ;


  private static String OBJECT_ERROR_15 = "object self:" ;


  private static String OBJECT_ERROR_16 = "object self: <add:int;>" ;


  private static String OBJECT_ERROR_17 = "object self: <add:int;>)" ;


  private static String OBJECT_ERROR_18 = "object self: <add:int;>) end" ;


  private static String OBJECT_ERROR_19 = "object self: <add:int;>) method add = 1 ;" ;


  private static String OBJECT_ERROR_20 = "object self: <add:int;>) method add = 1 ; end" ;


  // self
  private static String OBJECT_ERROR_21 = "object ( )" ;


  private static String OBJECT_ERROR_22 = "object ( ) end" ;


  private static String OBJECT_ERROR_23 = "object ( ) method add = 1 ;" ;


  private static String OBJECT_ERROR_24 = "object ( ) method add = 1 ; end" ;


  private static String OBJECT_ERROR_25 = "object ( :" ;


  private static String OBJECT_ERROR_26 = "object ( : <add:int;>" ;


  private static String OBJECT_ERROR_27 = "object ( : <add:int;>)" ;


  private static String OBJECT_ERROR_28 = "object ( : <add:int;>) end" ;


  private static String OBJECT_ERROR_29 = "object ( : <add:int;>) method add = 1 ;" ;


  private static String OBJECT_ERROR_30 = "object ( : <add:int;>) method add = 1 ; end" ;


  // right paren
  private static String OBJECT_ERROR_31 = "object (self end" ;


  private static String OBJECT_ERROR_32 = "object (self method add = 1 ;" ;


  private static String OBJECT_ERROR_33 = "object (self method add = 1 ; end" ;


  private static String OBJECT_ERROR_34 = "object (self: <add:int;> end" ;


  private static String OBJECT_ERROR_35 = "object (self: <add:int;> method add = 1" ;


  private static String OBJECT_ERROR_36 = "object (self: <add:int;> method add = 1 end" ;


  // colon
  private static String OBJECT_ERROR_37 = "object (self <add:int;>" ;


  private static String OBJECT_ERROR_38 = "object (self <add:int;>)" ;


  private static String OBJECT_ERROR_39 = "object (self <add:int;>) end" ;


  private static String OBJECT_ERROR_40 = "object (self <add:int;>) method add = 1 ;" ;


  private static String OBJECT_ERROR_41 = "object (self <add:int;>) method add = 1 ; end" ;


  // type
  private static String OBJECT_ERROR_42 = "object (self: )" ;


  private static String OBJECT_ERROR_43 = "object (self: ) end" ;


  private static String OBJECT_ERROR_44 = "object (self: ) method add = 1 ;" ;


  private static String OBJECT_ERROR_45 = "object (self: ) method add = 1 ; end" ;


  // left paren and self
  private static String OBJECT_ERROR_46 = "object )" ;


  private static String OBJECT_ERROR_47 = "object ) method add = 1 ;" ;


  private static String OBJECT_ERROR_48 = "object ) method add = 1 ; end" ;


  private static String OBJECT_ERROR_49 = "object :" ;


  private static String OBJECT_ERROR_50 = "object :<add:int;>" ;


  private static String OBJECT_ERROR_51 = "object :<add:int;>)" ;


  private static String OBJECT_ERROR_52 = "object :<add:int;>) end" ;


  private static String OBJECT_ERROR_53 = "object :<add:int;>) method add = 1 ;" ;


  private static String OBJECT_ERROR_54 = "object :<add:int;>) method add = 1 ; end" ;


  // self and right paren
  private static String OBJECT_ERROR_55 = "object ( end" ;


  private static String OBJECT_ERROR_56 = "object ( method add = 1 ;" ;


  private static String OBJECT_ERROR_57 = "object ( method add = 1 ; end" ;


  // self and colon
  private static String OBJECT_ERROR_58 = "object ( <add:int;>" ;


  private static String OBJECT_ERROR_59 = "object ( <add:int;>)" ;


  private static String OBJECT_ERROR_60 = "object ( <add:int;>) end" ;


  private static String OBJECT_ERROR_61 = "object ( <add:int;>) method add = 1 ;" ;


  private static String OBJECT_ERROR_62 = "object ( <add:int;>) method add = 1 ; end" ;


  // type and right paren
  private static String OBJECT_ERROR_63 = "object (self end" ;


  private static String OBJECT_ERROR_64 = "object (self method add = 1 ;" ;


  private static String OBJECT_ERROR_65 = "object (self method add = 1 ; end" ;


  // left paren, self and right paren
  private static String OBJECT_ERROR_66 = "object end" ;


  private static String OBJECT_ERROR_67 = "object method add = 1 ;" ;


  private static String OBJECT_ERROR_68 = "object method add = 1 ; end" ;


  // Missing left paren, self and colon
  private static String OBJECT_ERROR_69 = "object <add:int;>" ;


  private static String OBJECT_ERROR_70 = "object <add:int;>)" ;


  private static String OBJECT_ERROR_71 = "object <add:int;>) end" ;


  private static String OBJECT_ERROR_72 = "object <add:int;>) method add = 1 ;" ;


  private static String OBJECT_ERROR_73 = "object <add:int;>) method add = 1 ; end" ;


  private static String DUPLICATION_1 = "{< a = 0 >}" ;


  private static String DUPLICATION_2 = "{< a = 0 ; b = 1 >}" ;


  private static String DUPLICATION_3 = "{<  >}" ;


  private static String DUPLICATION_ERROR_1 = "{<" ;


  private static String DUPLICATION_ERROR_2 = "{< a = 0" ;


  private static String DUPLICATION_ERROR_3 = "{< a = 0 ;" ;


  private static String DUPLICATION_ERROR_4 = "{< a" ;


  private static String DUPLICATION_ERROR_5 = "{< a =" ;


  private static String SEND = "(object (self) method add = 0 ; end) # add" ;


  private static String SEND_ERROR = "(object (self) method add = 0 ; end) #" ;


  private static String ROW_1 = "object (self) val a = 0 ; end" ;


  private static String ROW_2 = "object (self) val a = 0 ; val b = 0 ; end" ;


  private static String ROW_3 = "object (self) method m = 0 ; end" ;


  private static String ROW_4 = "object (self) method m = 0 ; val b = 0 ; end" ;


  private static String ROW_5 = "object (self) method m x = 0 ; end" ;


  private static String ROW_6 = "object (self) method m x = 0 ; val b = 0 ; end" ;


  private static String ATTRIBUTE = "object (self) val a = 0 ; end" ;


  private static String ATTRIBUTE_ERROR_1 = "object (self) val" ;


  private static String ATTRIBUTE_ERROR_2 = "object (self) val a" ;


  private static String ATTRIBUTE_ERROR_3 = "object (self) val a =" ;


  private static String ATTRIBUTE_ERROR_4 = "object (self) val a = 0" ;


  private static String ATTRIBUTE_ERROR_5 = "object (self) val =" ;


  private static String ATTRIBUTE_ERROR_6 = "object (self) val = 0" ;


  private static String ATTRIBUTE_ERROR_7 = "object (self) val = 0 ;" ;


  private static String ATTRIBUTE_ERROR_8 = "object (self) val a 0" ;


  private static String ATTRIBUTE_ERROR_9 = "object (self) val a 0 ;" ;


  private static String ATTRIBUTE_ERROR_10 = "object (self) val a = ;" ;


  private static String ATTRIBUTE_ERROR_11 = "object (self) val 0" ;


  private static String ATTRIBUTE_ERROR_12 = "object (self) val 0 ;" ;


  private static String ATTRIBUTE_ERROR_13 = "object (self) val a ;" ;


  private static String ATTRIBUTE_ERROR_14 = "object (self) val ;" ;


  private static String METHOD_1 = "object (self) method m = 0 ; end" ;


  private static String METHOD_2 = "object (self) method m:int = 0 ; end" ;


  private static String METHOD_ERROR_1 = "object (self) method" ;


  private static String METHOD_ERROR_2 = "object (self) method m" ;


  private static String METHOD_ERROR_3 = "object (self) method m:" ;


  private static String METHOD_ERROR_4 = "object (self) method m:int" ;


  private static String METHOD_ERROR_5 = "object (self) method m:int =" ;


  private static String METHOD_ERROR_6 = "object (self) method m:int = 0" ;


  private static String METHOD_ERROR_7 = "object (self) method m =" ;


  private static String METHOD_ERROR_8 = "object (self) method m = 0" ;


  private static String METHOD_ERROR_9 = "object (self) method = end" ;


  private static String METHOD_ERROR_10 = "object (self) method = 0 end" ;


  private static String METHOD_ERROR_11 = "object (self) method = 0 ; end" ;


  private static String METHOD_ERROR_12 = "object (self) method : end" ;


  private static String METHOD_ERROR_13 = "object (self) method : int end" ;


  private static String METHOD_ERROR_14 = "object (self) method : int = end" ;


  private static String METHOD_ERROR_15 = "object (self) method : int = 0 end" ;


  private static String METHOD_ERROR_16 = "object (self) method : int = 0 ; end" ;


  private static String METHOD_ERROR_17 = "object (self) method m int end" ;


  private static String METHOD_ERROR_18 = "object (self) method m int = end" ;


  private static String METHOD_ERROR_19 = "object (self) method m int = 0 end" ;


  private static String METHOD_ERROR_20 = "object (self) method m int = 0 ; end" ;


  private static String METHOD_ERROR_21 = "object (self) method m: = end" ;


  private static String METHOD_ERROR_22 = "object (self) method m: = 0 end" ;


  private static String METHOD_ERROR_23 = "object (self) method m: = 0 ; end" ;


  private static String METHOD_ERROR_24 = "object (self) method m: int 0 end" ;


  private static String METHOD_ERROR_25 = "object (self) method m: int 0 ; end" ;


  private static String METHOD_ERROR_26 = "object (self) method m = ; end" ;


  private static String METHOD_ERROR_27 = "object (self) method m: int = ; end" ;


  private static String METHOD_ERROR_28 = "object (self) method 0 ; end" ;


  private static String METHOD_ERROR_29 = "object (self) method m ; end" ;


  private static String METHOD_ERROR_30 = "object (self) method ; end" ;


  private static String METHOD_ERROR_31 = "object (self) method int end" ;


  private static String METHOD_ERROR_32 = "object (self) method int = end" ;


  private static String METHOD_ERROR_33 = "object (self) method int = 0 end" ;


  private static String METHOD_ERROR_34 = "object (self) method int = 0 ; end" ;


  private static String METHOD_ERROR_35 = "object (self) method m: 0 end" ;


  private static String METHOD_ERROR_36 = "object (self) method m: 0 ; end" ;


  private static String METHOD_ERROR_37 = "object (self) method m: int ; end" ;


  private static String METHOD_ERROR_38 = "object (self) method m ; end" ;


  private static String CURRIED_METHOD_1 = "object (self) method m x y = 0 ; end" ;


  private static String CURRIED_METHOD_2 = "object (self) method m (x:int) y = 0 ; end" ;


  private static String CURRIED_METHOD_3 = "object (self) method m x y:int = 0 ; end" ;


  private static String CURRIED_METHOD_4 = "object (self) method m (x:int) y:int = 0 ; end" ;


  private static String CURRIED_METHOD_ERROR_1 = "object (self) method m x" ;


  private static String CURRIED_METHOD_ERROR_2 = "object (self) method m x:" ;


  private static String CURRIED_METHOD_ERROR_3 = "object (self) method m x:int" ;


  private static String CURRIED_METHOD_ERROR_4 = "object (self) method m x:int = " ;


  private static String CURRIED_METHOD_ERROR_5 = "object (self) method m x:int = 0" ;


  private static String CURRIED_METHOD_ERROR_6 = "object (self) method m x =" ;


  private static String CURRIED_METHOD_ERROR_7 = "object (self) method m x = 0" ;


  private static String CURRIED_METHOD_ERROR_8 = "object (self) method m (" ;


  private static String CURRIED_METHOD_ERROR_9 = "object (self) method m (x" ;


  private static String CURRIED_METHOD_ERROR_10 = "object (self) method m (x:" ;


  private static String CURRIED_METHOD_ERROR_11 = "object (self) method m (x:int" ;


  private static String CURRIED_METHOD_ERROR_12 = "object (self) method m x y: = 0 ; end" ;


  private static String CURRIED_METHOD_ERROR_13 = "object (self) method m (x:) y = 0 ; end" ;


  private static String CURRIED_METHOD_ERROR_14 = "object (self) method m x y int" ;


  private static String CURRIED_METHOD_ERROR_15 = "object (self) method m x y int =" ;


  private static String CURRIED_METHOD_ERROR_16 = "object (self) method m x y int = 0" ;


  private static String CURRIED_METHOD_ERROR_17 = "object (self) method m x y int = 0 ;" ;


  private static String CURRIED_METHOD_ERROR_18 = "object (self) method m x y int = 0 ; end" ;


  private static String CURRIED_METHOD_ERROR_19 = "object (self) method m x y: =" ;


  private static String CURRIED_METHOD_ERROR_20 = "object (self) method m x y: = 0" ;


  private static String CURRIED_METHOD_ERROR_21 = "object (self) method m x y: = 0 ;" ;


  private static String CURRIED_METHOD_ERROR_22 = "object (self) method m x y: = 0 ; end" ;


  private static String CURRIED_METHOD_ERROR_23 = "object (self) method m x y 0" ;


  private static String CURRIED_METHOD_ERROR_24 = "object (self) method m x y 0 ;" ;


  private static String CURRIED_METHOD_ERROR_25 = "object (self) method m x y 0 ; end" ;


  private static String CURRIED_METHOD_ERROR_26 = "object (self) method m x y: int 0" ;


  private static String CURRIED_METHOD_ERROR_27 = "object (self) method m x y: int 0 ;" ;


  private static String CURRIED_METHOD_ERROR_28 = "object (self) method m x y: int 0 ; end" ;


  private static String CURRIED_METHOD_ERROR_29 = "object (self) method m x y = ;" ;


  private static String CURRIED_METHOD_ERROR_30 = "object (self) method m x y = ; end" ;


  private static String CURRIED_METHOD_ERROR_31 = "object (self) method m x y: int = ;" ;


  private static String CURRIED_METHOD_ERROR_32 = "object (self) method m x y: int = ; end" ;


  private static String CURRIED_METHOD_ERROR_33 = "object (self) method m x y ;" ;


  private static String CURRIED_METHOD_ERROR_34 = "object (self) method m x y ; end" ;


  private static String CURRIED_METHOD_ERROR_35 = "object (self) method m x y: int ;" ;


  private static String CURRIED_METHOD_ERROR_36 = "object (self) method m x y: int ; end" ;


  private static String CURRIED_METHOD_ERROR_37 = "object (self) method m x y: 0" ;


  private static String CURRIED_METHOD_ERROR_38 = "object (self) method m x y: 0 ;" ;


  private static String CURRIED_METHOD_ERROR_39 = "object (self) method m x y: 0 ; end" ;


  private static String CURRIED_METHOD_ERROR_40 = "object (self) method m x y: ;" ;


  private static String CURRIED_METHOD_ERROR_41 = "object (self) method m x y: ; end" ;


  private static String EXPR_OBJECT_TYPE_1 = "lambda x:< add: int ; >.x" ;


  private static String EXPR_OBJECT_TYPE_2 = "lambda x:< >.x" ;


  private static String EXPR_OBJECT_TYPE_ERROR_1 = "lambda x:<" ;


  private static String EXPR_OBJECT_TYPE_ERROR_2 = "lambda x:< add: int ;" ;


  private static String EXPR_ROW_TYPE_1 = "lambda x:< add: int ; >.x" ;


  private static String EXPR_ROW_TYPE_2 = "lambda x:< add: int ; sub: int ; >.x" ;


  private static String EXPR_ROW_TYPE_ERROR_1 = "lambda x:< add" ;


  private static String EXPR_ROW_TYPE_ERROR_2 = "lambda x:< add:" ;


  private static String EXPR_ROW_TYPE_ERROR_3 = "lambda x:< add: int" ;


  private static String OBJECT_TYPE_1 = "< add: int ; >" ;


  private static String OBJECT_TYPE_2 = "< >" ;


  private static String OBJECT_TYPE_ERROR_1 = "<" ;


  private static String OBJECT_TYPE_ERROR_2 = "< add: int ;" ;


  private static String ROW_TYPE_1 = "< add: int ; >" ;


  private static String ROW_TYPE_2 = "< add: int ; sub: int ; >" ;


  private static String ROW_TYPE_ERROR_1 = "< add" ;


  private static String ROW_TYPE_ERROR_2 = "< add:" ;


  private static String ROW_TYPE_ERROR_3 = "< add: int" ;


  private static String CLASS_1 = "class (self) end" ;


  private static String CLASS_2 = "class (self) method m = 0 ; end" ;


  private static String CLASS_3 = "class (self) inherit a, b ; m, n from e as z ; method add = 0 ; end" ;


  private static String CLASS_ERROR_1 = "class" ;


  private static String CLASS_ERROR_2 = "class (" ;


  private static String CLASS_ERROR_3 = "class (self" ;


  private static String CLASS_ERROR_4 = "class (self)" ;


  private static String CLASS_ERROR_5 = "class (self) method add = 1 ;" ;


  private static String CLASS_ERROR_6 = "class self) end" ;


  private static String CLASS_ERROR_7 = "class self) method add = 0 ; end" ;


  private static String CLASS_ERROR_8 = "class (self end" ;


  private static String CLASS_ERROR_9 = "class (self method add = 0 ; end" ;


  private static String CLASS_ERROR_10 = "class ( ) end" ;


  private static String CLASS_ERROR_11 = "class ( ) method add = 0 ; end" ;


  private static String NEW = "new e" ;


  private static String NEW_ERROR = "new" ;


  private static String [ ] L0_NORMAL = new String [ ]
  { IDENTIFIER , APPLICATION , LAMBDA_1 , SIMPLE_EXPR_1 } ;


  private static String [ ] L0_ERROR = new String [ ]
  { LAMBDA_ERROR_1 , LAMBDA_ERROR_2 , LAMBDA_ERROR_3 , LAMBDA_ERROR_7 ,
      LAMBDA_ERROR_8 , LAMBDA_ERROR_13 , SIMPLE_EXPR_ERROR_1 ,
      SIMPLE_EXPR_ERROR_2 } ;


  private static String [ ] L1_NORMAL = new String [ ]
  { IDENTIFIER , APPLICATION , INFIX_OPERATION_1 , INFIX_OPERATION_2 ,
      INFIX_OPERATION_3 , INFIX_OPERATION_4 , INFIX_OPERATION_5 ,
      INFIX_OPERATION_6 , INFIX_OPERATION_7 , INFIX_OPERATION_8 ,
      INFIX_OPERATION_9 , INFIX_OPERATION_10 , AND , OR , CONDITION , LAMBDA_1 ,
      LAMBDA_2 , LET_1 , CURRIED_LET_1 , CURRIED_LET_2 , CURRIED_LET_3 ,
      CURRIED_LET_4 , COERCION , UNIT , BOOLEAN_1 , BOOLEAN_2 , INTEGER , NOT ,
      OPERATOR_1 , OPERATOR_2 , OPERATOR_3 , OPERATOR_4 , OPERATOR_5 ,
      OPERATOR_6 , OPERATOR_7 , OPERATOR_8 , OPERATOR_9 , OPERATOR_10 ,
      SIMPLE_EXPR_1 , SIMPLE_EXPR_2 , EXPR_SIMPLE_TYPE , EXPR_BOOLEAN_TYPE ,
      EXPR_INTEGER_TYPE , EXPR_UNIT_TYPE , EXPR_ARROW_TYPE ,
      EXPR_TYPE_VARIABLE , EXPR_TYPE_NAME , EXPR_REC_TYPE } ;


  private static String [ ] L1_ERROR = new String [ ]
  { INFIX_OPERATION_ERROR_1 , INFIX_OPERATION_ERROR_2 ,
      INFIX_OPERATION_ERROR_3 , INFIX_OPERATION_ERROR_4 ,
      INFIX_OPERATION_ERROR_5 , INFIX_OPERATION_ERROR_6 ,
      INFIX_OPERATION_ERROR_7 , INFIX_OPERATION_ERROR_8 ,
      INFIX_OPERATION_ERROR_9 , INFIX_OPERATION_ERROR_10 , AND_ERROR ,
      OR_ERROR , CONDITION_ERROR_1 , CONDITION_ERROR_2 , CONDITION_ERROR_3 ,
      CONDITION_ERROR_4 , CONDITION_ERROR_5 , CONDITION_ERROR_6 ,
      CONDITION_ERROR_7 , CONDITION_ERROR_8 , CONDITION_ERROR_9 ,
      CONDITION_ERROR_10 , CONDITION_ERROR_11 , CONDITION_ERROR_12 ,
      CONDITION_ERROR_13 , CONDITION_ERROR_14 , CONDITION_ERROR_15 ,
      LAMBDA_ERROR_1 , LAMBDA_ERROR_2 , LAMBDA_ERROR_3 , LAMBDA_ERROR_4 ,
      LAMBDA_ERROR_5 , LAMBDA_ERROR_6 , LAMBDA_ERROR_7 , LAMBDA_ERROR_8 ,
      LAMBDA_ERROR_9 , LAMBDA_ERROR_10 , LAMBDA_ERROR_11 , LAMBDA_ERROR_12 ,
      LAMBDA_ERROR_13 , LAMBDA_ERROR_14 , LAMBDA_ERROR_15 , LAMBDA_ERROR_16 ,
      LAMBDA_ERROR_17 , LAMBDA_ERROR_18 , LAMBDA_ERROR_19 , LAMBDA_ERROR_20 ,
      LAMBDA_ERROR_21 , LAMBDA_ERROR_22 , LAMBDA_ERROR_23 , LET_ERROR_1 ,
      LET_ERROR_2 , LET_ERROR_3 , LET_ERROR_4 , LET_ERROR_5 , LET_ERROR_6 ,
      LET_ERROR_7 , LET_ERROR_8 , LET_ERROR_9 , LET_ERROR_10 , LET_ERROR_11 ,
      LET_ERROR_12 , LET_ERROR_13 , LET_ERROR_14 , LET_ERROR_15 , LET_ERROR_16 ,
      LET_ERROR_17 , LET_ERROR_18 , LET_ERROR_19 , LET_ERROR_20 , LET_ERROR_21 ,
      LET_ERROR_22 , LET_ERROR_23 , LET_ERROR_24 , LET_ERROR_25 , LET_ERROR_26 ,
      LET_ERROR_27 , LET_ERROR_28 , LET_ERROR_29 , LET_ERROR_30 , LET_ERROR_31 ,
      LET_ERROR_32 , LET_ERROR_33 , LET_ERROR_34 , LET_ERROR_35 , LET_ERROR_36 ,
      LET_ERROR_37 , LET_ERROR_38 , LET_ERROR_39 , LET_ERROR_40 , LET_ERROR_41 ,
      LET_ERROR_42 , LET_ERROR_43 , LET_ERROR_44 , LET_ERROR_45 , LET_ERROR_46 ,
      LET_ERROR_47 , LET_ERROR_48 , LET_ERROR_49 , LET_ERROR_50 , LET_ERROR_51 ,
      LET_ERROR_52 , CURRIED_LET_ERROR_1 , CURRIED_LET_ERROR_2 ,
      CURRIED_LET_ERROR_3 , CURRIED_LET_ERROR_4 , CURRIED_LET_ERROR_5 ,
      CURRIED_LET_ERROR_6 , CURRIED_LET_ERROR_7 , CURRIED_LET_ERROR_8 ,
      CURRIED_LET_ERROR_9 , CURRIED_LET_ERROR_10 , CURRIED_LET_ERROR_11 ,
      CURRIED_LET_ERROR_12 , CURRIED_LET_ERROR_13 , CURRIED_LET_ERROR_14 ,
      CURRIED_LET_ERROR_15 , CURRIED_LET_ERROR_16 , CURRIED_LET_ERROR_17 ,
      CURRIED_LET_ERROR_18 , CURRIED_LET_ERROR_19 , CURRIED_LET_ERROR_20 ,
      CURRIED_LET_ERROR_21 , CURRIED_LET_ERROR_22 , CURRIED_LET_ERROR_23 ,
      CURRIED_LET_ERROR_24 , CURRIED_LET_ERROR_25 , CURRIED_LET_ERROR_26 ,
      CURRIED_LET_ERROR_27 , CURRIED_LET_ERROR_28 , CURRIED_LET_ERROR_29 ,
      CURRIED_LET_ERROR_30 , CURRIED_LET_ERROR_31 , CURRIED_LET_ERROR_32 ,
      CURRIED_LET_ERROR_33 , CURRIED_LET_ERROR_34 , CURRIED_LET_ERROR_35 ,
      CURRIED_LET_ERROR_36 , CURRIED_LET_ERROR_37 , CURRIED_LET_ERROR_38 ,
      CURRIED_LET_ERROR_39 , CURRIED_LET_ERROR_40 , COERCION_ERROR_1 ,
      COERCION_ERROR_2 , COERCION_ERROR_3 , COERCION_ERROR_4 ,
      COERCION_ERROR_5 , COERCION_ERROR_6 , COERCION_ERROR_7 ,
      COERCION_ERROR_8 , COERCION_ERROR_9 , COERCION_ERROR_10 ,
      COERCION_ERROR_11 , COERCION_ERROR_12 , COERCION_ERROR_13 ,
      COERCION_ERROR_14 , COERCION_ERROR_15 , COERCION_ERROR_16 ,
      COERCION_ERROR_17 , COERCION_ERROR_18 , COERCION_ERROR_19 ,
      SIMPLE_EXPR_ERROR_1 , SIMPLE_EXPR_ERROR_2 , SIMPLE_EXPR_ERROR_3 ,
      EXPR_SIMPLE_TYPE_ERROR_1 , EXPR_SIMPLE_TYPE_ERROR_2 ,
      EXPR_ARROW_TYPE_ERROR , EXPR_REC_TYPE_ERROR_1 , EXPR_REC_TYPE_ERROR_2 ,
      EXPR_REC_TYPE_ERROR_3 } ;


  private static String [ ] L1TYPE_NORMAL = new String [ ]
  { SIMPLE_TYPE , BOOLEAN_TYPE , INTEGER_TYPE , UNIT_TYPE , TYPE_VARIABLE ,
      TYPE_NAME , REC_TYPE , ARROW_TYPE } ;


  private static String [ ] L1TYPE_ERROR = new String [ ]
  { SIMPLE_TYPE_ERROR_1 , SIMPLE_TYPE_ERROR_2 , ARROW_TYPE_ERROR ,
      REC_TYPE_ERROR_1 , REC_TYPE_ERROR_2 , REC_TYPE_ERROR_3 } ;


  private static String [ ] L2_NORMAL = new String [ ]
  { IDENTIFIER , APPLICATION , INFIX_OPERATION_1 , INFIX_OPERATION_2 ,
      INFIX_OPERATION_3 , INFIX_OPERATION_4 , INFIX_OPERATION_5 ,
      INFIX_OPERATION_6 , INFIX_OPERATION_7 , INFIX_OPERATION_8 ,
      INFIX_OPERATION_9 , INFIX_OPERATION_10 , AND , OR , CONDITION , LAMBDA_1 ,
      LAMBDA_2 , LET_1 , CURRIED_LET_1 , CURRIED_LET_2 , CURRIED_LET_3 ,
      CURRIED_LET_4 , COERCION , UNIT , BOOLEAN_1 , BOOLEAN_2 , INTEGER , NOT ,
      OPERATOR_1 , OPERATOR_2 , OPERATOR_3 , OPERATOR_4 , OPERATOR_5 ,
      OPERATOR_6 , OPERATOR_7 , OPERATOR_8 , OPERATOR_9 , OPERATOR_10 ,
      SIMPLE_EXPR_1 , SIMPLE_EXPR_2 , EXPR_SIMPLE_TYPE , EXPR_BOOLEAN_TYPE ,
      EXPR_INTEGER_TYPE , EXPR_UNIT_TYPE , EXPR_ARROW_TYPE ,
      EXPR_TYPE_VARIABLE , EXPR_TYPE_NAME , EXPR_REC_TYPE , REC_1 , REC_2 ,
      LET_REC_1 , LET_REC_2 , CURRIED_LET_REC_1 , CURRIED_LET_REC_2 ,
      CURRIED_LET_REC_3 , CURRIED_LET_REC_4 } ;


  private static String [ ] L2_ERROR = new String [ ]
  { INFIX_OPERATION_ERROR_1 , INFIX_OPERATION_ERROR_2 ,
      INFIX_OPERATION_ERROR_3 , INFIX_OPERATION_ERROR_4 ,
      INFIX_OPERATION_ERROR_5 , INFIX_OPERATION_ERROR_6 ,
      INFIX_OPERATION_ERROR_7 , INFIX_OPERATION_ERROR_8 ,
      INFIX_OPERATION_ERROR_9 , INFIX_OPERATION_ERROR_10 , AND_ERROR ,
      OR_ERROR , CONDITION_ERROR_1 , CONDITION_ERROR_2 , CONDITION_ERROR_3 ,
      CONDITION_ERROR_4 , CONDITION_ERROR_5 , CONDITION_ERROR_6 ,
      CONDITION_ERROR_7 , CONDITION_ERROR_8 , CONDITION_ERROR_9 ,
      CONDITION_ERROR_10 , CONDITION_ERROR_11 , CONDITION_ERROR_12 ,
      CONDITION_ERROR_13 , CONDITION_ERROR_14 , CONDITION_ERROR_15 ,
      LAMBDA_ERROR_1 , LAMBDA_ERROR_2 , LAMBDA_ERROR_3 , LAMBDA_ERROR_4 ,
      LAMBDA_ERROR_5 , LAMBDA_ERROR_6 , LAMBDA_ERROR_7 , LAMBDA_ERROR_8 ,
      LAMBDA_ERROR_9 , LAMBDA_ERROR_10 , LAMBDA_ERROR_11 , LAMBDA_ERROR_12 ,
      LAMBDA_ERROR_13 , LAMBDA_ERROR_14 , LAMBDA_ERROR_15 , LAMBDA_ERROR_16 ,
      LAMBDA_ERROR_17 , LAMBDA_ERROR_18 , LAMBDA_ERROR_19 , LAMBDA_ERROR_20 ,
      LAMBDA_ERROR_21 , LAMBDA_ERROR_22 , LAMBDA_ERROR_23 , LET_ERROR_1 ,
      LET_ERROR_2 , LET_ERROR_3 , LET_ERROR_4 , LET_ERROR_5 , LET_ERROR_6 ,
      LET_ERROR_7 , LET_ERROR_8 , LET_ERROR_9 , LET_ERROR_10 , LET_ERROR_11 ,
      LET_ERROR_12 , LET_ERROR_13 , LET_ERROR_14 , LET_ERROR_15 , LET_ERROR_16 ,
      LET_ERROR_17 , LET_ERROR_18 , LET_ERROR_19 , LET_ERROR_20 , LET_ERROR_21 ,
      LET_ERROR_22 , LET_ERROR_23 , LET_ERROR_24 , LET_ERROR_25 , LET_ERROR_26 ,
      LET_ERROR_27 , LET_ERROR_28 , LET_ERROR_29 , LET_ERROR_30 , LET_ERROR_31 ,
      LET_ERROR_32 , LET_ERROR_33 , LET_ERROR_34 , LET_ERROR_35 , LET_ERROR_36 ,
      LET_ERROR_37 , LET_ERROR_38 , LET_ERROR_39 , LET_ERROR_40 , LET_ERROR_41 ,
      LET_ERROR_42 , LET_ERROR_43 , LET_ERROR_44 , LET_ERROR_45 , LET_ERROR_46 ,
      LET_ERROR_47 , LET_ERROR_48 , LET_ERROR_49 , LET_ERROR_50 , LET_ERROR_51 ,
      LET_ERROR_52 , CURRIED_LET_ERROR_1 , CURRIED_LET_ERROR_2 ,
      CURRIED_LET_ERROR_3 , CURRIED_LET_ERROR_4 , CURRIED_LET_ERROR_5 ,
      CURRIED_LET_ERROR_6 , CURRIED_LET_ERROR_7 , CURRIED_LET_ERROR_8 ,
      CURRIED_LET_ERROR_9 , CURRIED_LET_ERROR_10 , CURRIED_LET_ERROR_11 ,
      CURRIED_LET_ERROR_12 , CURRIED_LET_ERROR_13 , CURRIED_LET_ERROR_14 ,
      CURRIED_LET_ERROR_15 , CURRIED_LET_ERROR_16 , CURRIED_LET_ERROR_17 ,
      CURRIED_LET_ERROR_18 , CURRIED_LET_ERROR_19 , CURRIED_LET_ERROR_20 ,
      CURRIED_LET_ERROR_21 , CURRIED_LET_ERROR_22 , CURRIED_LET_ERROR_23 ,
      CURRIED_LET_ERROR_24 , CURRIED_LET_ERROR_25 , CURRIED_LET_ERROR_26 ,
      CURRIED_LET_ERROR_27 , CURRIED_LET_ERROR_28 , CURRIED_LET_ERROR_29 ,
      CURRIED_LET_ERROR_30 , CURRIED_LET_ERROR_31 , CURRIED_LET_ERROR_32 ,
      CURRIED_LET_ERROR_33 , CURRIED_LET_ERROR_34 , CURRIED_LET_ERROR_35 ,
      CURRIED_LET_ERROR_36 , CURRIED_LET_ERROR_37 , CURRIED_LET_ERROR_38 ,
      CURRIED_LET_ERROR_39 , CURRIED_LET_ERROR_40 , COERCION_ERROR_1 ,
      COERCION_ERROR_2 , COERCION_ERROR_3 , COERCION_ERROR_4 ,
      COERCION_ERROR_5 , COERCION_ERROR_6 , COERCION_ERROR_7 ,
      COERCION_ERROR_8 , COERCION_ERROR_9 , COERCION_ERROR_10 ,
      COERCION_ERROR_11 , COERCION_ERROR_12 , COERCION_ERROR_13 ,
      COERCION_ERROR_14 , COERCION_ERROR_15 , COERCION_ERROR_16 ,
      COERCION_ERROR_17 , COERCION_ERROR_18 , COERCION_ERROR_19 ,
      SIMPLE_EXPR_ERROR_1 , SIMPLE_EXPR_ERROR_2 , SIMPLE_EXPR_ERROR_3 ,
      EXPR_SIMPLE_TYPE_ERROR_1 , EXPR_SIMPLE_TYPE_ERROR_2 ,
      EXPR_ARROW_TYPE_ERROR , EXPR_REC_TYPE_ERROR_1 , EXPR_REC_TYPE_ERROR_2 ,
      EXPR_REC_TYPE_ERROR_3 , REC_ERROR_1 , REC_ERROR_2 , REC_ERROR_3 ,
      REC_ERROR_4 , REC_ERROR_5 , REC_ERROR_6 , REC_ERROR_7 , REC_ERROR_8 ,
      REC_ERROR_9 , REC_ERROR_10 , REC_ERROR_11 , REC_ERROR_12 , REC_ERROR_13 ,
      REC_ERROR_14 , REC_ERROR_15 , REC_ERROR_16 , REC_ERROR_17 , REC_ERROR_18 ,
      REC_ERROR_19 , REC_ERROR_20 , REC_ERROR_21 , REC_ERROR_22 ,
      LET_REC_ERROR_1 , LET_REC_ERROR_2 , LET_REC_ERROR_3 , LET_REC_ERROR_4 ,
      LET_REC_ERROR_5 , LET_REC_ERROR_6 , LET_REC_ERROR_7 , LET_REC_ERROR_8 ,
      LET_REC_ERROR_9 , LET_REC_ERROR_10 , LET_REC_ERROR_11 , LET_REC_ERROR_12 ,
      LET_REC_ERROR_13 , LET_REC_ERROR_14 , LET_REC_ERROR_15 ,
      LET_REC_ERROR_16 , LET_REC_ERROR_17 , LET_REC_ERROR_18 ,
      LET_REC_ERROR_19 , LET_REC_ERROR_20 , LET_REC_ERROR_21 ,
      LET_REC_ERROR_22 , LET_REC_ERROR_23 , LET_REC_ERROR_24 ,
      LET_REC_ERROR_25 , LET_REC_ERROR_26 , LET_REC_ERROR_27 ,
      LET_REC_ERROR_28 , LET_REC_ERROR_29 , LET_REC_ERROR_30 ,
      LET_REC_ERROR_31 , LET_REC_ERROR_32 , LET_REC_ERROR_33 ,
      LET_REC_ERROR_34 , LET_REC_ERROR_35 , LET_REC_ERROR_36 ,
      LET_REC_ERROR_37 , LET_REC_ERROR_38 , LET_REC_ERROR_39 ,
      LET_REC_ERROR_40 , LET_REC_ERROR_41 , LET_REC_ERROR_42 ,
      LET_REC_ERROR_43 , LET_REC_ERROR_44 , LET_REC_ERROR_45 ,
      LET_REC_ERROR_46 , LET_REC_ERROR_47 , LET_REC_ERROR_48 ,
      LET_REC_ERROR_49 , LET_REC_ERROR_50 , LET_REC_ERROR_51 ,
      LET_REC_ERROR_52 , LET_REC_ERROR_53 , LET_REC_ERROR_54 ,
      LET_REC_ERROR_55 , CURRIED_LET_REC_ERROR_1 , CURRIED_LET_REC_ERROR_2 ,
      CURRIED_LET_REC_ERROR_3 , CURRIED_LET_REC_ERROR_4 ,
      CURRIED_LET_REC_ERROR_5 , CURRIED_LET_REC_ERROR_6 ,
      CURRIED_LET_REC_ERROR_7 , CURRIED_LET_REC_ERROR_8 ,
      CURRIED_LET_REC_ERROR_9 , CURRIED_LET_REC_ERROR_10 ,
      CURRIED_LET_REC_ERROR_11 , CURRIED_LET_REC_ERROR_12 ,
      CURRIED_LET_REC_ERROR_13 , CURRIED_LET_REC_ERROR_14 ,
      CURRIED_LET_REC_ERROR_15 , CURRIED_LET_REC_ERROR_16 ,
      CURRIED_LET_REC_ERROR_17 , CURRIED_LET_REC_ERROR_18 ,
      CURRIED_LET_REC_ERROR_19 , CURRIED_LET_REC_ERROR_20 ,
      CURRIED_LET_REC_ERROR_21 , CURRIED_LET_REC_ERROR_22 ,
      CURRIED_LET_REC_ERROR_23 , CURRIED_LET_REC_ERROR_24 ,
      CURRIED_LET_REC_ERROR_25 , CURRIED_LET_REC_ERROR_26 ,
      CURRIED_LET_REC_ERROR_27 , CURRIED_LET_REC_ERROR_28 ,
      CURRIED_LET_REC_ERROR_29 , CURRIED_LET_REC_ERROR_30 ,
      CURRIED_LET_REC_ERROR_31 , CURRIED_LET_REC_ERROR_32 ,
      CURRIED_LET_REC_ERROR_33 , CURRIED_LET_REC_ERROR_34 ,
      CURRIED_LET_REC_ERROR_35 , CURRIED_LET_REC_ERROR_36 ,
      CURRIED_LET_REC_ERROR_37 , CURRIED_LET_REC_ERROR_38 ,
      CURRIED_LET_REC_ERROR_39 , CURRIED_LET_REC_ERROR_40 } ;


  private static String [ ] L2TYPE_NORMAL = new String [ ]
  { SIMPLE_TYPE , BOOLEAN_TYPE , INTEGER_TYPE , UNIT_TYPE , TYPE_VARIABLE ,
      TYPE_NAME , REC_TYPE , ARROW_TYPE } ;


  private static String [ ] L2TYPE_ERROR = new String [ ]
  { SIMPLE_TYPE_ERROR_1 , SIMPLE_TYPE_ERROR_2 , ARROW_TYPE_ERROR ,
      REC_TYPE_ERROR_1 , REC_TYPE_ERROR_2 , REC_TYPE_ERROR_3 } ;


  private static String [ ] L2O_NORMAL = new String [ ]
  { IDENTIFIER , APPLICATION , INFIX_OPERATION_1 , INFIX_OPERATION_2 ,
      INFIX_OPERATION_3 , INFIX_OPERATION_4 , INFIX_OPERATION_5 ,
      INFIX_OPERATION_6 , INFIX_OPERATION_7 , INFIX_OPERATION_8 ,
      INFIX_OPERATION_9 , INFIX_OPERATION_10 , AND , OR , CONDITION , LAMBDA_1 ,
      LAMBDA_2 , LET_1 , CURRIED_LET_1 , CURRIED_LET_2 , CURRIED_LET_3 ,
      CURRIED_LET_4 , COERCION , UNIT , BOOLEAN_1 , BOOLEAN_2 , INTEGER , NOT ,
      OPERATOR_1 , OPERATOR_2 , OPERATOR_3 , OPERATOR_4 , OPERATOR_5 ,
      OPERATOR_6 , OPERATOR_7 , OPERATOR_8 , OPERATOR_9 , OPERATOR_10 ,
      SIMPLE_EXPR_1 , SIMPLE_EXPR_2 , EXPR_SIMPLE_TYPE , EXPR_BOOLEAN_TYPE ,
      EXPR_INTEGER_TYPE , EXPR_UNIT_TYPE , EXPR_ARROW_TYPE ,
      EXPR_TYPE_VARIABLE , EXPR_TYPE_NAME , EXPR_REC_TYPE , REC_1 , REC_2 ,
      LET_REC_1 , LET_REC_2 , CURRIED_LET_REC_1 , CURRIED_LET_REC_2 ,
      CURRIED_LET_REC_3 , CURRIED_LET_REC_4 , SELF , OBJECT_1 , OBJECT_2 ,
      OBJECT_3 , OBJECT_4 , DUPLICATION_1 , DUPLICATION_2 , DUPLICATION_3 ,
      SEND , ROW_1 , ROW_2 , ROW_3 , ROW_4 , ROW_5 , ROW_6 , ATTRIBUTE ,
      METHOD_1 , METHOD_2 , CURRIED_METHOD_1 , CURRIED_METHOD_2 ,
      CURRIED_METHOD_3 , CURRIED_METHOD_4 , EXPR_OBJECT_TYPE_1 ,
      EXPR_OBJECT_TYPE_2 , EXPR_ROW_TYPE_1 , EXPR_ROW_TYPE_2 } ;


  private static String [ ] L2O_ERROR = new String [ ]
  { INFIX_OPERATION_ERROR_1 , INFIX_OPERATION_ERROR_2 ,
      INFIX_OPERATION_ERROR_3 , INFIX_OPERATION_ERROR_4 ,
      INFIX_OPERATION_ERROR_5 , INFIX_OPERATION_ERROR_6 ,
      INFIX_OPERATION_ERROR_7 , INFIX_OPERATION_ERROR_8 ,
      INFIX_OPERATION_ERROR_9 , INFIX_OPERATION_ERROR_10 , AND_ERROR ,
      OR_ERROR , CONDITION_ERROR_1 , CONDITION_ERROR_2 , CONDITION_ERROR_3 ,
      CONDITION_ERROR_4 , CONDITION_ERROR_5 , CONDITION_ERROR_6 ,
      CONDITION_ERROR_7 , CONDITION_ERROR_8 , CONDITION_ERROR_9 ,
      CONDITION_ERROR_10 , CONDITION_ERROR_11 , CONDITION_ERROR_12 ,
      CONDITION_ERROR_13 , CONDITION_ERROR_14 , CONDITION_ERROR_15 ,
      LAMBDA_ERROR_1 , LAMBDA_ERROR_2 , LAMBDA_ERROR_3 , LAMBDA_ERROR_4 ,
      LAMBDA_ERROR_5 , LAMBDA_ERROR_6 , LAMBDA_ERROR_7 , LAMBDA_ERROR_8 ,
      LAMBDA_ERROR_9 , LAMBDA_ERROR_10 , LAMBDA_ERROR_11 , LAMBDA_ERROR_12 ,
      LAMBDA_ERROR_13 , LAMBDA_ERROR_14 , LAMBDA_ERROR_15 , LAMBDA_ERROR_16 ,
      LAMBDA_ERROR_17 , LAMBDA_ERROR_18 , LAMBDA_ERROR_19 , LAMBDA_ERROR_20 ,
      LAMBDA_ERROR_21 , LAMBDA_ERROR_22 , LAMBDA_ERROR_23 , LET_ERROR_1 ,
      LET_ERROR_2 , LET_ERROR_3 , LET_ERROR_4 , LET_ERROR_5 , LET_ERROR_6 ,
      LET_ERROR_7 , LET_ERROR_8 , LET_ERROR_9 , LET_ERROR_10 , LET_ERROR_11 ,
      LET_ERROR_12 , LET_ERROR_13 , LET_ERROR_14 , LET_ERROR_15 , LET_ERROR_16 ,
      LET_ERROR_17 , LET_ERROR_18 , LET_ERROR_19 , LET_ERROR_20 , LET_ERROR_21 ,
      LET_ERROR_22 , LET_ERROR_23 , LET_ERROR_24 , LET_ERROR_25 , LET_ERROR_26 ,
      LET_ERROR_27 , LET_ERROR_28 , LET_ERROR_29 , LET_ERROR_30 , LET_ERROR_31 ,
      LET_ERROR_32 , LET_ERROR_33 , LET_ERROR_34 , LET_ERROR_35 , LET_ERROR_36 ,
      LET_ERROR_37 , LET_ERROR_38 , LET_ERROR_39 , LET_ERROR_40 , LET_ERROR_41 ,
      LET_ERROR_42 , LET_ERROR_43 , LET_ERROR_44 , LET_ERROR_45 , LET_ERROR_46 ,
      LET_ERROR_47 , LET_ERROR_48 , LET_ERROR_49 , LET_ERROR_50 , LET_ERROR_51 ,
      LET_ERROR_52 , CURRIED_LET_ERROR_1 , CURRIED_LET_ERROR_2 ,
      CURRIED_LET_ERROR_3 , CURRIED_LET_ERROR_4 , CURRIED_LET_ERROR_5 ,
      CURRIED_LET_ERROR_6 , CURRIED_LET_ERROR_7 , CURRIED_LET_ERROR_8 ,
      CURRIED_LET_ERROR_9 , CURRIED_LET_ERROR_10 , CURRIED_LET_ERROR_11 ,
      CURRIED_LET_ERROR_12 , CURRIED_LET_ERROR_13 , CURRIED_LET_ERROR_14 ,
      CURRIED_LET_ERROR_15 , CURRIED_LET_ERROR_16 , CURRIED_LET_ERROR_17 ,
      CURRIED_LET_ERROR_18 , CURRIED_LET_ERROR_19 , CURRIED_LET_ERROR_20 ,
      CURRIED_LET_ERROR_21 , CURRIED_LET_ERROR_22 , CURRIED_LET_ERROR_23 ,
      CURRIED_LET_ERROR_24 , CURRIED_LET_ERROR_25 , CURRIED_LET_ERROR_26 ,
      CURRIED_LET_ERROR_27 , CURRIED_LET_ERROR_28 , CURRIED_LET_ERROR_29 ,
      CURRIED_LET_ERROR_30 , CURRIED_LET_ERROR_31 , CURRIED_LET_ERROR_32 ,
      CURRIED_LET_ERROR_33 , CURRIED_LET_ERROR_34 , CURRIED_LET_ERROR_35 ,
      CURRIED_LET_ERROR_36 , CURRIED_LET_ERROR_37 , CURRIED_LET_ERROR_38 ,
      CURRIED_LET_ERROR_39 , CURRIED_LET_ERROR_40 , COERCION_ERROR_1 ,
      COERCION_ERROR_2 , COERCION_ERROR_3 , COERCION_ERROR_4 ,
      COERCION_ERROR_5 , COERCION_ERROR_6 , COERCION_ERROR_7 ,
      COERCION_ERROR_8 , COERCION_ERROR_9 , COERCION_ERROR_10 ,
      COERCION_ERROR_11 , COERCION_ERROR_12 , COERCION_ERROR_13 ,
      COERCION_ERROR_14 , COERCION_ERROR_15 , COERCION_ERROR_16 ,
      COERCION_ERROR_17 , COERCION_ERROR_18 , COERCION_ERROR_19 ,
      SIMPLE_EXPR_ERROR_1 , SIMPLE_EXPR_ERROR_2 , SIMPLE_EXPR_ERROR_3 ,
      EXPR_SIMPLE_TYPE_ERROR_1 , EXPR_SIMPLE_TYPE_ERROR_2 ,
      EXPR_ARROW_TYPE_ERROR , EXPR_REC_TYPE_ERROR_1 , EXPR_REC_TYPE_ERROR_2 ,
      EXPR_REC_TYPE_ERROR_3 , REC_ERROR_1 , REC_ERROR_2 , REC_ERROR_3 ,
      REC_ERROR_4 , REC_ERROR_5 , REC_ERROR_6 , REC_ERROR_7 , REC_ERROR_8 ,
      REC_ERROR_9 , REC_ERROR_10 , REC_ERROR_11 , REC_ERROR_12 , REC_ERROR_13 ,
      REC_ERROR_14 , REC_ERROR_15 , REC_ERROR_16 , REC_ERROR_17 , REC_ERROR_18 ,
      REC_ERROR_19 , REC_ERROR_20 , REC_ERROR_21 , REC_ERROR_22 ,
      LET_REC_ERROR_1 , LET_REC_ERROR_2 , LET_REC_ERROR_3 , LET_REC_ERROR_4 ,
      LET_REC_ERROR_5 , LET_REC_ERROR_6 , LET_REC_ERROR_7 , LET_REC_ERROR_8 ,
      LET_REC_ERROR_9 , LET_REC_ERROR_10 , LET_REC_ERROR_11 , LET_REC_ERROR_12 ,
      LET_REC_ERROR_13 , LET_REC_ERROR_14 , LET_REC_ERROR_15 ,
      LET_REC_ERROR_16 , LET_REC_ERROR_17 , LET_REC_ERROR_18 ,
      LET_REC_ERROR_19 , LET_REC_ERROR_20 , LET_REC_ERROR_21 ,
      LET_REC_ERROR_22 , LET_REC_ERROR_23 , LET_REC_ERROR_24 ,
      LET_REC_ERROR_25 , LET_REC_ERROR_26 , LET_REC_ERROR_27 ,
      LET_REC_ERROR_28 , LET_REC_ERROR_29 , LET_REC_ERROR_30 ,
      LET_REC_ERROR_31 , LET_REC_ERROR_32 , LET_REC_ERROR_33 ,
      LET_REC_ERROR_34 , LET_REC_ERROR_35 , LET_REC_ERROR_36 ,
      LET_REC_ERROR_37 , LET_REC_ERROR_38 , LET_REC_ERROR_39 ,
      LET_REC_ERROR_40 , LET_REC_ERROR_41 , LET_REC_ERROR_42 ,
      LET_REC_ERROR_43 , LET_REC_ERROR_44 , LET_REC_ERROR_45 ,
      LET_REC_ERROR_46 , LET_REC_ERROR_47 , LET_REC_ERROR_48 ,
      LET_REC_ERROR_49 , LET_REC_ERROR_50 , LET_REC_ERROR_51 ,
      LET_REC_ERROR_52 , LET_REC_ERROR_53 , LET_REC_ERROR_54 ,
      LET_REC_ERROR_55 , CURRIED_LET_REC_ERROR_1 , CURRIED_LET_REC_ERROR_2 ,
      CURRIED_LET_REC_ERROR_3 , CURRIED_LET_REC_ERROR_4 ,
      CURRIED_LET_REC_ERROR_5 , CURRIED_LET_REC_ERROR_6 ,
      CURRIED_LET_REC_ERROR_7 , CURRIED_LET_REC_ERROR_8 ,
      CURRIED_LET_REC_ERROR_9 , CURRIED_LET_REC_ERROR_10 ,
      CURRIED_LET_REC_ERROR_11 , CURRIED_LET_REC_ERROR_12 ,
      CURRIED_LET_REC_ERROR_13 , CURRIED_LET_REC_ERROR_14 ,
      CURRIED_LET_REC_ERROR_15 , CURRIED_LET_REC_ERROR_16 ,
      CURRIED_LET_REC_ERROR_17 , CURRIED_LET_REC_ERROR_18 ,
      CURRIED_LET_REC_ERROR_19 , CURRIED_LET_REC_ERROR_20 ,
      CURRIED_LET_REC_ERROR_21 , CURRIED_LET_REC_ERROR_22 ,
      CURRIED_LET_REC_ERROR_23 , CURRIED_LET_REC_ERROR_24 ,
      CURRIED_LET_REC_ERROR_25 , CURRIED_LET_REC_ERROR_26 ,
      CURRIED_LET_REC_ERROR_27 , CURRIED_LET_REC_ERROR_28 ,
      CURRIED_LET_REC_ERROR_29 , CURRIED_LET_REC_ERROR_30 ,
      CURRIED_LET_REC_ERROR_31 , CURRIED_LET_REC_ERROR_32 ,
      CURRIED_LET_REC_ERROR_33 , CURRIED_LET_REC_ERROR_34 ,
      CURRIED_LET_REC_ERROR_35 , CURRIED_LET_REC_ERROR_36 ,
      CURRIED_LET_REC_ERROR_37 , CURRIED_LET_REC_ERROR_38 ,
      CURRIED_LET_REC_ERROR_39 , CURRIED_LET_REC_ERROR_40 , OBJECT_ERROR_1 ,
      OBJECT_ERROR_2 , OBJECT_ERROR_3 , OBJECT_ERROR_4 , OBJECT_ERROR_5 ,
      OBJECT_ERROR_6 , OBJECT_ERROR_7 , OBJECT_ERROR_8 , OBJECT_ERROR_9 ,
      OBJECT_ERROR_10 , OBJECT_ERROR_11 , OBJECT_ERROR_12 , OBJECT_ERROR_13 ,
      OBJECT_ERROR_14 , OBJECT_ERROR_15 , OBJECT_ERROR_16 , OBJECT_ERROR_17 ,
      OBJECT_ERROR_18 , OBJECT_ERROR_19 , OBJECT_ERROR_20 , OBJECT_ERROR_21 ,
      OBJECT_ERROR_22 , OBJECT_ERROR_23 , OBJECT_ERROR_24 , OBJECT_ERROR_25 ,
      OBJECT_ERROR_26 , OBJECT_ERROR_27 , OBJECT_ERROR_28 , OBJECT_ERROR_29 ,
      OBJECT_ERROR_30 , OBJECT_ERROR_31 , OBJECT_ERROR_32 , OBJECT_ERROR_33 ,
      OBJECT_ERROR_34 , OBJECT_ERROR_35 , OBJECT_ERROR_36 , OBJECT_ERROR_37 ,
      OBJECT_ERROR_38 , OBJECT_ERROR_39 , OBJECT_ERROR_40 , OBJECT_ERROR_41 ,
      OBJECT_ERROR_42 , OBJECT_ERROR_43 , OBJECT_ERROR_44 , OBJECT_ERROR_45 ,
      OBJECT_ERROR_46 , OBJECT_ERROR_47 , OBJECT_ERROR_48 , OBJECT_ERROR_49 ,
      OBJECT_ERROR_50 , OBJECT_ERROR_51 , OBJECT_ERROR_52 , OBJECT_ERROR_53 ,
      OBJECT_ERROR_54 , OBJECT_ERROR_55 , OBJECT_ERROR_56 , OBJECT_ERROR_57 ,
      OBJECT_ERROR_58 , OBJECT_ERROR_59 , OBJECT_ERROR_60 , OBJECT_ERROR_61 ,
      OBJECT_ERROR_62 , OBJECT_ERROR_63 , OBJECT_ERROR_64 , OBJECT_ERROR_65 ,
      OBJECT_ERROR_66 , OBJECT_ERROR_67 , OBJECT_ERROR_68 , OBJECT_ERROR_69 ,
      OBJECT_ERROR_70 , OBJECT_ERROR_71 , OBJECT_ERROR_72 , OBJECT_ERROR_73 ,
      DUPLICATION_ERROR_1 , DUPLICATION_ERROR_2 , DUPLICATION_ERROR_3 ,
      DUPLICATION_ERROR_4 , DUPLICATION_ERROR_5 , SEND_ERROR ,
      ATTRIBUTE_ERROR_1 , ATTRIBUTE_ERROR_2 , ATTRIBUTE_ERROR_3 ,
      ATTRIBUTE_ERROR_4 , ATTRIBUTE_ERROR_5 , ATTRIBUTE_ERROR_6 ,
      ATTRIBUTE_ERROR_7 , ATTRIBUTE_ERROR_8 , ATTRIBUTE_ERROR_9 ,
      ATTRIBUTE_ERROR_10 , ATTRIBUTE_ERROR_11 , ATTRIBUTE_ERROR_12 ,
      ATTRIBUTE_ERROR_13 , ATTRIBUTE_ERROR_14 , METHOD_ERROR_1 ,
      METHOD_ERROR_2 , METHOD_ERROR_3 , METHOD_ERROR_4 , METHOD_ERROR_5 ,
      METHOD_ERROR_6 , METHOD_ERROR_7 , METHOD_ERROR_8 , METHOD_ERROR_9 ,
      METHOD_ERROR_10 , METHOD_ERROR_11 , METHOD_ERROR_12 , METHOD_ERROR_13 ,
      METHOD_ERROR_14 , METHOD_ERROR_15 , METHOD_ERROR_16 , METHOD_ERROR_17 ,
      METHOD_ERROR_18 , METHOD_ERROR_19 , METHOD_ERROR_20 , METHOD_ERROR_21 ,
      METHOD_ERROR_22 , METHOD_ERROR_23 , METHOD_ERROR_24 , METHOD_ERROR_25 ,
      METHOD_ERROR_26 , METHOD_ERROR_27 , METHOD_ERROR_28 , METHOD_ERROR_29 ,
      METHOD_ERROR_30 , METHOD_ERROR_31 , METHOD_ERROR_32 , METHOD_ERROR_33 ,
      METHOD_ERROR_34 , METHOD_ERROR_35 , METHOD_ERROR_36 , METHOD_ERROR_37 ,
      METHOD_ERROR_38 , CURRIED_METHOD_ERROR_1 , CURRIED_METHOD_ERROR_2 ,
      CURRIED_METHOD_ERROR_3 , CURRIED_METHOD_ERROR_4 , CURRIED_METHOD_ERROR_5 ,
      CURRIED_METHOD_ERROR_6 , CURRIED_METHOD_ERROR_7 , CURRIED_METHOD_ERROR_8 ,
      CURRIED_METHOD_ERROR_9 , CURRIED_METHOD_ERROR_10 ,
      CURRIED_METHOD_ERROR_11 , CURRIED_METHOD_ERROR_12 ,
      CURRIED_METHOD_ERROR_13 , CURRIED_METHOD_ERROR_14 ,
      CURRIED_METHOD_ERROR_15 , CURRIED_METHOD_ERROR_16 ,
      CURRIED_METHOD_ERROR_17 , CURRIED_METHOD_ERROR_18 ,
      CURRIED_METHOD_ERROR_19 , CURRIED_METHOD_ERROR_20 ,
      CURRIED_METHOD_ERROR_21 , CURRIED_METHOD_ERROR_22 ,
      CURRIED_METHOD_ERROR_23 , CURRIED_METHOD_ERROR_24 ,
      CURRIED_METHOD_ERROR_25 , CURRIED_METHOD_ERROR_26 ,
      CURRIED_METHOD_ERROR_27 , CURRIED_METHOD_ERROR_28 ,
      CURRIED_METHOD_ERROR_29 , CURRIED_METHOD_ERROR_30 ,
      CURRIED_METHOD_ERROR_31 , CURRIED_METHOD_ERROR_32 ,
      CURRIED_METHOD_ERROR_33 , CURRIED_METHOD_ERROR_34 ,
      CURRIED_METHOD_ERROR_35 , CURRIED_METHOD_ERROR_36 ,
      CURRIED_METHOD_ERROR_37 , CURRIED_METHOD_ERROR_38 ,
      CURRIED_METHOD_ERROR_39 , CURRIED_METHOD_ERROR_40 ,
      CURRIED_METHOD_ERROR_41 , EXPR_OBJECT_TYPE_ERROR_1 ,
      EXPR_OBJECT_TYPE_ERROR_2 , EXPR_ROW_TYPE_ERROR_1 , EXPR_ROW_TYPE_ERROR_2 ,
      EXPR_ROW_TYPE_ERROR_3 } ;


  private static String [ ] L2OTYPE_NORMAL = new String [ ]
  { SIMPLE_TYPE , BOOLEAN_TYPE , INTEGER_TYPE , UNIT_TYPE , TYPE_VARIABLE ,
      TYPE_NAME , REC_TYPE , ARROW_TYPE , OBJECT_TYPE_1 , OBJECT_TYPE_2 ,
      ROW_TYPE_1 , ROW_TYPE_2 } ;


  private static String [ ] L2OTYPE_ERROR = new String [ ]
  { SIMPLE_TYPE_ERROR_1 , SIMPLE_TYPE_ERROR_2 , ARROW_TYPE_ERROR ,
      REC_TYPE_ERROR_1 , REC_TYPE_ERROR_2 , REC_TYPE_ERROR_3 ,
      OBJECT_TYPE_ERROR_1 , OBJECT_TYPE_ERROR_2 , ROW_TYPE_ERROR_1 ,
      ROW_TYPE_ERROR_2 , ROW_TYPE_ERROR_3 } ;


  private static String [ ] L2C_NORMAL = new String [ ]
  { IDENTIFIER , APPLICATION , INFIX_OPERATION_1 , INFIX_OPERATION_2 ,
      INFIX_OPERATION_3 , INFIX_OPERATION_4 , INFIX_OPERATION_5 ,
      INFIX_OPERATION_6 , INFIX_OPERATION_7 , INFIX_OPERATION_8 ,
      INFIX_OPERATION_9 , INFIX_OPERATION_10 , AND , OR , CONDITION , LAMBDA_1 ,
      LAMBDA_2 , LET_1 , CURRIED_LET_1 , CURRIED_LET_2 , CURRIED_LET_3 ,
      CURRIED_LET_4 , COERCION , UNIT , BOOLEAN_1 , BOOLEAN_2 , INTEGER , NOT ,
      OPERATOR_1 , OPERATOR_2 , OPERATOR_3 , OPERATOR_4 , OPERATOR_5 ,
      OPERATOR_6 , OPERATOR_7 , OPERATOR_8 , OPERATOR_9 , OPERATOR_10 ,
      SIMPLE_EXPR_1 , SIMPLE_EXPR_2 , EXPR_SIMPLE_TYPE , EXPR_BOOLEAN_TYPE ,
      EXPR_INTEGER_TYPE , EXPR_UNIT_TYPE , EXPR_ARROW_TYPE ,
      EXPR_TYPE_VARIABLE , EXPR_TYPE_NAME , EXPR_REC_TYPE , REC_1 , REC_2 ,
      LET_REC_1 , LET_REC_2 , CURRIED_LET_REC_1 , CURRIED_LET_REC_2 ,
      CURRIED_LET_REC_3 , CURRIED_LET_REC_4 , SELF , OBJECT_1 , OBJECT_2 ,
      OBJECT_3 , OBJECT_4 , DUPLICATION_1 , DUPLICATION_2 , DUPLICATION_3 ,
      SEND , ROW_1 , ROW_2 , ROW_3 , ROW_4 , ROW_5 , ROW_6 , ATTRIBUTE ,
      METHOD_1 , METHOD_2 , CURRIED_METHOD_1 , CURRIED_METHOD_2 ,
      CURRIED_METHOD_3 , CURRIED_METHOD_4 , EXPR_OBJECT_TYPE_1 ,
      EXPR_OBJECT_TYPE_2 , EXPR_ROW_TYPE_1 , EXPR_ROW_TYPE_2 , CLASS_1 ,
      CLASS_2 , CLASS_3 , NEW } ;


  private static String [ ] L2C_ERROR = new String [ ]
  { INFIX_OPERATION_ERROR_1 , INFIX_OPERATION_ERROR_2 ,
      INFIX_OPERATION_ERROR_3 , INFIX_OPERATION_ERROR_4 ,
      INFIX_OPERATION_ERROR_5 , INFIX_OPERATION_ERROR_6 ,
      INFIX_OPERATION_ERROR_7 , INFIX_OPERATION_ERROR_8 ,
      INFIX_OPERATION_ERROR_9 , INFIX_OPERATION_ERROR_10 , AND_ERROR ,
      OR_ERROR , CONDITION_ERROR_1 , CONDITION_ERROR_2 , CONDITION_ERROR_3 ,
      CONDITION_ERROR_4 , CONDITION_ERROR_5 , CONDITION_ERROR_6 ,
      CONDITION_ERROR_7 , CONDITION_ERROR_8 , CONDITION_ERROR_9 ,
      CONDITION_ERROR_10 , CONDITION_ERROR_11 , CONDITION_ERROR_12 ,
      CONDITION_ERROR_13 , CONDITION_ERROR_14 , CONDITION_ERROR_15 ,
      LAMBDA_ERROR_1 , LAMBDA_ERROR_2 , LAMBDA_ERROR_3 , LAMBDA_ERROR_4 ,
      LAMBDA_ERROR_5 , LAMBDA_ERROR_6 , LAMBDA_ERROR_7 , LAMBDA_ERROR_8 ,
      LAMBDA_ERROR_9 , LAMBDA_ERROR_10 , LAMBDA_ERROR_11 , LAMBDA_ERROR_12 ,
      LAMBDA_ERROR_13 , LAMBDA_ERROR_14 , LAMBDA_ERROR_15 , LAMBDA_ERROR_16 ,
      LAMBDA_ERROR_17 , LAMBDA_ERROR_18 , LAMBDA_ERROR_19 , LAMBDA_ERROR_20 ,
      LAMBDA_ERROR_21 , LAMBDA_ERROR_22 , LAMBDA_ERROR_23 , LET_ERROR_1 ,
      LET_ERROR_2 , LET_ERROR_3 , LET_ERROR_4 , LET_ERROR_5 , LET_ERROR_6 ,
      LET_ERROR_7 , LET_ERROR_8 , LET_ERROR_9 , LET_ERROR_10 , LET_ERROR_11 ,
      LET_ERROR_12 , LET_ERROR_13 , LET_ERROR_14 , LET_ERROR_15 , LET_ERROR_16 ,
      LET_ERROR_17 , LET_ERROR_18 , LET_ERROR_19 , LET_ERROR_20 , LET_ERROR_21 ,
      LET_ERROR_22 , LET_ERROR_23 , LET_ERROR_24 , LET_ERROR_25 , LET_ERROR_26 ,
      LET_ERROR_27 , LET_ERROR_28 , LET_ERROR_29 , LET_ERROR_30 , LET_ERROR_31 ,
      LET_ERROR_32 , LET_ERROR_33 , LET_ERROR_34 , LET_ERROR_35 , LET_ERROR_36 ,
      LET_ERROR_37 , LET_ERROR_38 , LET_ERROR_39 , LET_ERROR_40 , LET_ERROR_41 ,
      LET_ERROR_42 , LET_ERROR_43 , LET_ERROR_44 , LET_ERROR_45 , LET_ERROR_46 ,
      LET_ERROR_47 , LET_ERROR_48 , LET_ERROR_49 , LET_ERROR_50 , LET_ERROR_51 ,
      LET_ERROR_52 , CURRIED_LET_ERROR_1 , CURRIED_LET_ERROR_2 ,
      CURRIED_LET_ERROR_3 , CURRIED_LET_ERROR_4 , CURRIED_LET_ERROR_5 ,
      CURRIED_LET_ERROR_6 , CURRIED_LET_ERROR_7 , CURRIED_LET_ERROR_8 ,
      CURRIED_LET_ERROR_9 , CURRIED_LET_ERROR_10 , CURRIED_LET_ERROR_11 ,
      CURRIED_LET_ERROR_12 , CURRIED_LET_ERROR_13 , CURRIED_LET_ERROR_14 ,
      CURRIED_LET_ERROR_15 , CURRIED_LET_ERROR_16 , CURRIED_LET_ERROR_17 ,
      CURRIED_LET_ERROR_18 , CURRIED_LET_ERROR_19 , CURRIED_LET_ERROR_20 ,
      CURRIED_LET_ERROR_21 , CURRIED_LET_ERROR_22 , CURRIED_LET_ERROR_23 ,
      CURRIED_LET_ERROR_24 , CURRIED_LET_ERROR_25 , CURRIED_LET_ERROR_26 ,
      CURRIED_LET_ERROR_27 , CURRIED_LET_ERROR_28 , CURRIED_LET_ERROR_29 ,
      CURRIED_LET_ERROR_30 , CURRIED_LET_ERROR_31 , CURRIED_LET_ERROR_32 ,
      CURRIED_LET_ERROR_33 , CURRIED_LET_ERROR_34 , CURRIED_LET_ERROR_35 ,
      CURRIED_LET_ERROR_36 , CURRIED_LET_ERROR_37 , CURRIED_LET_ERROR_38 ,
      CURRIED_LET_ERROR_39 , CURRIED_LET_ERROR_40 , COERCION_ERROR_1 ,
      COERCION_ERROR_2 , COERCION_ERROR_3 , COERCION_ERROR_4 ,
      COERCION_ERROR_5 , COERCION_ERROR_6 , COERCION_ERROR_7 ,
      COERCION_ERROR_8 , COERCION_ERROR_9 , COERCION_ERROR_10 ,
      COERCION_ERROR_11 , COERCION_ERROR_12 , COERCION_ERROR_13 ,
      COERCION_ERROR_14 , COERCION_ERROR_15 , COERCION_ERROR_16 ,
      COERCION_ERROR_17 , COERCION_ERROR_18 , COERCION_ERROR_19 ,
      SIMPLE_EXPR_ERROR_1 , SIMPLE_EXPR_ERROR_2 , SIMPLE_EXPR_ERROR_3 ,
      EXPR_SIMPLE_TYPE_ERROR_1 , EXPR_SIMPLE_TYPE_ERROR_2 ,
      EXPR_ARROW_TYPE_ERROR , EXPR_REC_TYPE_ERROR_1 , EXPR_REC_TYPE_ERROR_2 ,
      EXPR_REC_TYPE_ERROR_3 , REC_ERROR_1 , REC_ERROR_2 , REC_ERROR_3 ,
      REC_ERROR_4 , REC_ERROR_5 , REC_ERROR_6 , REC_ERROR_7 , REC_ERROR_8 ,
      REC_ERROR_9 , REC_ERROR_10 , REC_ERROR_11 , REC_ERROR_12 , REC_ERROR_13 ,
      REC_ERROR_14 , REC_ERROR_15 , REC_ERROR_16 , REC_ERROR_17 , REC_ERROR_18 ,
      REC_ERROR_19 , REC_ERROR_20 , REC_ERROR_21 , REC_ERROR_22 ,
      LET_REC_ERROR_1 , LET_REC_ERROR_2 , LET_REC_ERROR_3 , LET_REC_ERROR_4 ,
      LET_REC_ERROR_5 , LET_REC_ERROR_6 , LET_REC_ERROR_7 , LET_REC_ERROR_8 ,
      LET_REC_ERROR_9 , LET_REC_ERROR_10 , LET_REC_ERROR_11 , LET_REC_ERROR_12 ,
      LET_REC_ERROR_13 , LET_REC_ERROR_14 , LET_REC_ERROR_15 ,
      LET_REC_ERROR_16 , LET_REC_ERROR_17 , LET_REC_ERROR_18 ,
      LET_REC_ERROR_19 , LET_REC_ERROR_20 , LET_REC_ERROR_21 ,
      LET_REC_ERROR_22 , LET_REC_ERROR_23 , LET_REC_ERROR_24 ,
      LET_REC_ERROR_25 , LET_REC_ERROR_26 , LET_REC_ERROR_27 ,
      LET_REC_ERROR_28 , LET_REC_ERROR_29 , LET_REC_ERROR_30 ,
      LET_REC_ERROR_31 , LET_REC_ERROR_32 , LET_REC_ERROR_33 ,
      LET_REC_ERROR_34 , LET_REC_ERROR_35 , LET_REC_ERROR_36 ,
      LET_REC_ERROR_37 , LET_REC_ERROR_38 , LET_REC_ERROR_39 ,
      LET_REC_ERROR_40 , LET_REC_ERROR_41 , LET_REC_ERROR_42 ,
      LET_REC_ERROR_43 , LET_REC_ERROR_44 , LET_REC_ERROR_45 ,
      LET_REC_ERROR_46 , LET_REC_ERROR_47 , LET_REC_ERROR_48 ,
      LET_REC_ERROR_49 , LET_REC_ERROR_50 , LET_REC_ERROR_51 ,
      LET_REC_ERROR_52 , LET_REC_ERROR_53 , LET_REC_ERROR_54 ,
      LET_REC_ERROR_55 , CURRIED_LET_REC_ERROR_1 , CURRIED_LET_REC_ERROR_2 ,
      CURRIED_LET_REC_ERROR_3 , CURRIED_LET_REC_ERROR_4 ,
      CURRIED_LET_REC_ERROR_5 , CURRIED_LET_REC_ERROR_6 ,
      CURRIED_LET_REC_ERROR_7 , CURRIED_LET_REC_ERROR_8 ,
      CURRIED_LET_REC_ERROR_9 , CURRIED_LET_REC_ERROR_10 ,
      CURRIED_LET_REC_ERROR_11 , CURRIED_LET_REC_ERROR_12 ,
      CURRIED_LET_REC_ERROR_13 , CURRIED_LET_REC_ERROR_14 ,
      CURRIED_LET_REC_ERROR_15 , CURRIED_LET_REC_ERROR_16 ,
      CURRIED_LET_REC_ERROR_17 , CURRIED_LET_REC_ERROR_18 ,
      CURRIED_LET_REC_ERROR_19 , CURRIED_LET_REC_ERROR_20 ,
      CURRIED_LET_REC_ERROR_21 , CURRIED_LET_REC_ERROR_22 ,
      CURRIED_LET_REC_ERROR_23 , CURRIED_LET_REC_ERROR_24 ,
      CURRIED_LET_REC_ERROR_25 , CURRIED_LET_REC_ERROR_26 ,
      CURRIED_LET_REC_ERROR_27 , CURRIED_LET_REC_ERROR_28 ,
      CURRIED_LET_REC_ERROR_29 , CURRIED_LET_REC_ERROR_30 ,
      CURRIED_LET_REC_ERROR_31 , CURRIED_LET_REC_ERROR_32 ,
      CURRIED_LET_REC_ERROR_33 , CURRIED_LET_REC_ERROR_34 ,
      CURRIED_LET_REC_ERROR_35 , CURRIED_LET_REC_ERROR_36 ,
      CURRIED_LET_REC_ERROR_37 , CURRIED_LET_REC_ERROR_38 ,
      CURRIED_LET_REC_ERROR_39 , CURRIED_LET_REC_ERROR_40 , OBJECT_ERROR_1 ,
      OBJECT_ERROR_2 , OBJECT_ERROR_3 , OBJECT_ERROR_4 , OBJECT_ERROR_5 ,
      OBJECT_ERROR_6 , OBJECT_ERROR_7 , OBJECT_ERROR_8 , OBJECT_ERROR_9 ,
      OBJECT_ERROR_10 , OBJECT_ERROR_11 , OBJECT_ERROR_12 , OBJECT_ERROR_13 ,
      OBJECT_ERROR_14 , OBJECT_ERROR_15 , OBJECT_ERROR_16 , OBJECT_ERROR_17 ,
      OBJECT_ERROR_18 , OBJECT_ERROR_19 , OBJECT_ERROR_20 , OBJECT_ERROR_21 ,
      OBJECT_ERROR_22 , OBJECT_ERROR_23 , OBJECT_ERROR_24 , OBJECT_ERROR_25 ,
      OBJECT_ERROR_26 , OBJECT_ERROR_27 , OBJECT_ERROR_28 , OBJECT_ERROR_29 ,
      OBJECT_ERROR_30 , OBJECT_ERROR_31 , OBJECT_ERROR_32 , OBJECT_ERROR_33 ,
      OBJECT_ERROR_34 , OBJECT_ERROR_35 , OBJECT_ERROR_36 , OBJECT_ERROR_37 ,
      OBJECT_ERROR_38 , OBJECT_ERROR_39 , OBJECT_ERROR_40 , OBJECT_ERROR_41 ,
      OBJECT_ERROR_42 , OBJECT_ERROR_43 , OBJECT_ERROR_44 , OBJECT_ERROR_45 ,
      OBJECT_ERROR_46 , OBJECT_ERROR_47 , OBJECT_ERROR_48 , OBJECT_ERROR_49 ,
      OBJECT_ERROR_50 , OBJECT_ERROR_51 , OBJECT_ERROR_52 , OBJECT_ERROR_53 ,
      OBJECT_ERROR_54 , OBJECT_ERROR_55 , OBJECT_ERROR_56 , OBJECT_ERROR_57 ,
      OBJECT_ERROR_58 , OBJECT_ERROR_59 , OBJECT_ERROR_60 , OBJECT_ERROR_61 ,
      OBJECT_ERROR_62 , OBJECT_ERROR_63 , OBJECT_ERROR_64 , OBJECT_ERROR_65 ,
      OBJECT_ERROR_66 , OBJECT_ERROR_67 , OBJECT_ERROR_68 , OBJECT_ERROR_69 ,
      OBJECT_ERROR_70 , OBJECT_ERROR_71 , OBJECT_ERROR_72 , OBJECT_ERROR_73 ,
      DUPLICATION_ERROR_1 , DUPLICATION_ERROR_2 , DUPLICATION_ERROR_3 ,
      DUPLICATION_ERROR_4 , DUPLICATION_ERROR_5 , SEND_ERROR ,
      ATTRIBUTE_ERROR_1 , ATTRIBUTE_ERROR_2 , ATTRIBUTE_ERROR_3 ,
      ATTRIBUTE_ERROR_4 , ATTRIBUTE_ERROR_5 , ATTRIBUTE_ERROR_6 ,
      ATTRIBUTE_ERROR_7 , ATTRIBUTE_ERROR_8 , ATTRIBUTE_ERROR_9 ,
      ATTRIBUTE_ERROR_10 , ATTRIBUTE_ERROR_11 , ATTRIBUTE_ERROR_12 ,
      ATTRIBUTE_ERROR_13 , ATTRIBUTE_ERROR_14 , METHOD_ERROR_1 ,
      METHOD_ERROR_2 , METHOD_ERROR_3 , METHOD_ERROR_4 , METHOD_ERROR_5 ,
      METHOD_ERROR_6 , METHOD_ERROR_7 , METHOD_ERROR_8 , METHOD_ERROR_9 ,
      METHOD_ERROR_10 , METHOD_ERROR_11 , METHOD_ERROR_12 , METHOD_ERROR_13 ,
      METHOD_ERROR_14 , METHOD_ERROR_15 , METHOD_ERROR_16 , METHOD_ERROR_17 ,
      METHOD_ERROR_18 , METHOD_ERROR_19 , METHOD_ERROR_20 , METHOD_ERROR_21 ,
      METHOD_ERROR_22 , METHOD_ERROR_23 , METHOD_ERROR_24 , METHOD_ERROR_25 ,
      METHOD_ERROR_26 , METHOD_ERROR_27 , METHOD_ERROR_28 , METHOD_ERROR_29 ,
      METHOD_ERROR_30 , METHOD_ERROR_31 , METHOD_ERROR_32 , METHOD_ERROR_33 ,
      METHOD_ERROR_34 , METHOD_ERROR_35 , METHOD_ERROR_36 , METHOD_ERROR_37 ,
      METHOD_ERROR_38 , CURRIED_METHOD_ERROR_1 , CURRIED_METHOD_ERROR_2 ,
      CURRIED_METHOD_ERROR_3 , CURRIED_METHOD_ERROR_4 , CURRIED_METHOD_ERROR_5 ,
      CURRIED_METHOD_ERROR_6 , CURRIED_METHOD_ERROR_7 , CURRIED_METHOD_ERROR_8 ,
      CURRIED_METHOD_ERROR_9 , CURRIED_METHOD_ERROR_10 ,
      CURRIED_METHOD_ERROR_11 , CURRIED_METHOD_ERROR_12 ,
      CURRIED_METHOD_ERROR_13 , CURRIED_METHOD_ERROR_14 ,
      CURRIED_METHOD_ERROR_15 , CURRIED_METHOD_ERROR_16 ,
      CURRIED_METHOD_ERROR_17 , CURRIED_METHOD_ERROR_18 ,
      CURRIED_METHOD_ERROR_19 , CURRIED_METHOD_ERROR_20 ,
      CURRIED_METHOD_ERROR_21 , CURRIED_METHOD_ERROR_22 ,
      CURRIED_METHOD_ERROR_23 , CURRIED_METHOD_ERROR_24 ,
      CURRIED_METHOD_ERROR_25 , CURRIED_METHOD_ERROR_26 ,
      CURRIED_METHOD_ERROR_27 , CURRIED_METHOD_ERROR_28 ,
      CURRIED_METHOD_ERROR_29 , CURRIED_METHOD_ERROR_30 ,
      CURRIED_METHOD_ERROR_31 , CURRIED_METHOD_ERROR_32 ,
      CURRIED_METHOD_ERROR_33 , CURRIED_METHOD_ERROR_34 ,
      CURRIED_METHOD_ERROR_35 , CURRIED_METHOD_ERROR_36 ,
      CURRIED_METHOD_ERROR_37 , CURRIED_METHOD_ERROR_38 ,
      CURRIED_METHOD_ERROR_39 , CURRIED_METHOD_ERROR_40 ,
      CURRIED_METHOD_ERROR_41 , EXPR_OBJECT_TYPE_ERROR_1 ,
      EXPR_OBJECT_TYPE_ERROR_2 , EXPR_ROW_TYPE_ERROR_1 , EXPR_ROW_TYPE_ERROR_2 ,
      EXPR_ROW_TYPE_ERROR_3 , CLASS_ERROR_1 , CLASS_ERROR_2 , CLASS_ERROR_3 ,
      CLASS_ERROR_4 , CLASS_ERROR_5 , CLASS_ERROR_6 , CLASS_ERROR_7 ,
      CLASS_ERROR_8 , CLASS_ERROR_9 , CLASS_ERROR_10 , CLASS_ERROR_11 ,
      NEW_ERROR } ;


  private static String [ ] L2CTYPE_NORMAL = new String [ ]
  { SIMPLE_TYPE , BOOLEAN_TYPE , INTEGER_TYPE , UNIT_TYPE , TYPE_VARIABLE ,
      TYPE_NAME , REC_TYPE , ARROW_TYPE , OBJECT_TYPE_1 , OBJECT_TYPE_2 ,
      ROW_TYPE_1 , ROW_TYPE_2 } ;


  private static String [ ] L2CTYPE_ERROR = new String [ ]
  { SIMPLE_TYPE_ERROR_1 , SIMPLE_TYPE_ERROR_2 , ARROW_TYPE_ERROR ,
      REC_TYPE_ERROR_1 , REC_TYPE_ERROR_2 , REC_TYPE_ERROR_3 ,
      OBJECT_TYPE_ERROR_1 , OBJECT_TYPE_ERROR_2 , ROW_TYPE_ERROR_1 ,
      ROW_TYPE_ERROR_2 , ROW_TYPE_ERROR_3 } ;


  private static String [ ] L3_NORMAL = new String [ ]
  { IDENTIFIER , APPLICATION , INFIX_OPERATION_1 , INFIX_OPERATION_2 ,
      INFIX_OPERATION_3 , INFIX_OPERATION_4 , INFIX_OPERATION_5 ,
      INFIX_OPERATION_6 , INFIX_OPERATION_7 , INFIX_OPERATION_8 ,
      INFIX_OPERATION_9 , INFIX_OPERATION_10 , AND , OR , CONDITION , LAMBDA_1 ,
      LAMBDA_2 , LET_1 , CURRIED_LET_1 , CURRIED_LET_2 , CURRIED_LET_3 ,
      CURRIED_LET_4 , COERCION , UNIT , BOOLEAN_1 , BOOLEAN_2 , INTEGER , NOT ,
      OPERATOR_1 , OPERATOR_2 , OPERATOR_3 , OPERATOR_4 , OPERATOR_5 ,
      OPERATOR_6 , OPERATOR_7 , OPERATOR_8 , OPERATOR_9 , OPERATOR_10 ,
      SIMPLE_EXPR_1 , SIMPLE_EXPR_2 , EXPR_SIMPLE_TYPE , EXPR_BOOLEAN_TYPE ,
      EXPR_INTEGER_TYPE , EXPR_UNIT_TYPE , EXPR_ARROW_TYPE ,
      EXPR_TYPE_VARIABLE , EXPR_TYPE_NAME , EXPR_REC_TYPE , REC_1 , REC_2 ,
      LET_REC_1 , LET_REC_2 , CURRIED_LET_REC_1 , CURRIED_LET_REC_2 ,
      CURRIED_LET_REC_3 , CURRIED_LET_REC_4 , INFIX_OPERATION_11 ,
      MULTI_LAMBDA_1 , MULTI_LAMBDA_2 , MULTI_LET_1 , MULTI_LET_2 , FST , SND ,
      CONS , IS_EMPTY , HD , TL , OPERATOR_11 , EMPTY_LIST , PROJECTION ,
      TUPLE , LIST , EXPR_TUPLE_TYPE_1 , EXPR_TUPLE_TYPE_2 , EXPR_LIST_TYPE } ;


  private static String [ ] L3_ERROR = new String [ ]
  { INFIX_OPERATION_ERROR_1 , INFIX_OPERATION_ERROR_2 ,
      INFIX_OPERATION_ERROR_3 , INFIX_OPERATION_ERROR_4 ,
      INFIX_OPERATION_ERROR_5 , INFIX_OPERATION_ERROR_6 ,
      INFIX_OPERATION_ERROR_7 , INFIX_OPERATION_ERROR_8 ,
      INFIX_OPERATION_ERROR_9 , INFIX_OPERATION_ERROR_10 , AND_ERROR ,
      OR_ERROR , CONDITION_ERROR_1 , CONDITION_ERROR_2 , CONDITION_ERROR_3 ,
      CONDITION_ERROR_4 , CONDITION_ERROR_5 , CONDITION_ERROR_6 ,
      CONDITION_ERROR_7 , CONDITION_ERROR_8 , CONDITION_ERROR_9 ,
      CONDITION_ERROR_10 , CONDITION_ERROR_11 , CONDITION_ERROR_12 ,
      CONDITION_ERROR_13 , CONDITION_ERROR_14 , CONDITION_ERROR_15 ,
      LAMBDA_ERROR_1 , LAMBDA_ERROR_2 , LAMBDA_ERROR_3 , LAMBDA_ERROR_4 ,
      LAMBDA_ERROR_5 , LAMBDA_ERROR_6 , LAMBDA_ERROR_7 , LAMBDA_ERROR_8 ,
      LAMBDA_ERROR_9 , LAMBDA_ERROR_10 , LAMBDA_ERROR_11 , LAMBDA_ERROR_12 ,
      LAMBDA_ERROR_13 , LAMBDA_ERROR_14 , LAMBDA_ERROR_15 , LAMBDA_ERROR_16 ,
      LAMBDA_ERROR_17 , LAMBDA_ERROR_18 , LAMBDA_ERROR_19 , LAMBDA_ERROR_20 ,
      LAMBDA_ERROR_21 , LAMBDA_ERROR_22 , LAMBDA_ERROR_23 , LET_ERROR_1 ,
      LET_ERROR_2 , LET_ERROR_3 , LET_ERROR_4 , LET_ERROR_5 , LET_ERROR_6 ,
      LET_ERROR_7 , LET_ERROR_8 , LET_ERROR_9 , LET_ERROR_10 , LET_ERROR_11 ,
      LET_ERROR_12 , LET_ERROR_13 , LET_ERROR_14 , LET_ERROR_15 , LET_ERROR_16 ,
      LET_ERROR_17 , LET_ERROR_18 , LET_ERROR_19 , LET_ERROR_20 , LET_ERROR_21 ,
      LET_ERROR_22 , LET_ERROR_23 , LET_ERROR_24 , LET_ERROR_25 , LET_ERROR_26 ,
      LET_ERROR_27 , LET_ERROR_28 , LET_ERROR_29 , LET_ERROR_30 , LET_ERROR_31 ,
      LET_ERROR_32 , LET_ERROR_33 , LET_ERROR_34 , LET_ERROR_35 , LET_ERROR_36 ,
      LET_ERROR_37 , LET_ERROR_38 , LET_ERROR_39 , LET_ERROR_40 , LET_ERROR_41 ,
      LET_ERROR_42 , LET_ERROR_43 , LET_ERROR_44 , LET_ERROR_45 , LET_ERROR_46 ,
      LET_ERROR_47 , LET_ERROR_48 , LET_ERROR_49 , LET_ERROR_50 , LET_ERROR_51 ,
      LET_ERROR_52 , CURRIED_LET_ERROR_1 , CURRIED_LET_ERROR_2 ,
      CURRIED_LET_ERROR_3 , CURRIED_LET_ERROR_4 , CURRIED_LET_ERROR_5 ,
      CURRIED_LET_ERROR_6 , CURRIED_LET_ERROR_7 , CURRIED_LET_ERROR_8 ,
      CURRIED_LET_ERROR_9 , CURRIED_LET_ERROR_10 , CURRIED_LET_ERROR_11 ,
      CURRIED_LET_ERROR_12 , CURRIED_LET_ERROR_13 , CURRIED_LET_ERROR_14 ,
      CURRIED_LET_ERROR_15 , CURRIED_LET_ERROR_16 , CURRIED_LET_ERROR_17 ,
      CURRIED_LET_ERROR_18 , CURRIED_LET_ERROR_19 , CURRIED_LET_ERROR_20 ,
      CURRIED_LET_ERROR_21 , CURRIED_LET_ERROR_22 , CURRIED_LET_ERROR_23 ,
      CURRIED_LET_ERROR_24 , CURRIED_LET_ERROR_25 , CURRIED_LET_ERROR_26 ,
      CURRIED_LET_ERROR_27 , CURRIED_LET_ERROR_28 , CURRIED_LET_ERROR_29 ,
      CURRIED_LET_ERROR_30 , CURRIED_LET_ERROR_31 , CURRIED_LET_ERROR_32 ,
      CURRIED_LET_ERROR_33 , CURRIED_LET_ERROR_34 , CURRIED_LET_ERROR_35 ,
      CURRIED_LET_ERROR_36 , CURRIED_LET_ERROR_37 , CURRIED_LET_ERROR_38 ,
      CURRIED_LET_ERROR_39 , CURRIED_LET_ERROR_40 , COERCION_ERROR_1 ,
      COERCION_ERROR_2 , COERCION_ERROR_3 , COERCION_ERROR_4 ,
      COERCION_ERROR_5 , COERCION_ERROR_6 , COERCION_ERROR_7 ,
      COERCION_ERROR_8 , COERCION_ERROR_9 , COERCION_ERROR_10 ,
      COERCION_ERROR_11 , COERCION_ERROR_12 , COERCION_ERROR_13 ,
      COERCION_ERROR_14 , COERCION_ERROR_15 , COERCION_ERROR_16 ,
      COERCION_ERROR_17 , COERCION_ERROR_18 , COERCION_ERROR_19 ,
      SIMPLE_EXPR_ERROR_1 , SIMPLE_EXPR_ERROR_2 , SIMPLE_EXPR_ERROR_3 ,
      EXPR_SIMPLE_TYPE_ERROR_1 , EXPR_SIMPLE_TYPE_ERROR_2 ,
      EXPR_ARROW_TYPE_ERROR , EXPR_REC_TYPE_ERROR_1 , EXPR_REC_TYPE_ERROR_2 ,
      EXPR_REC_TYPE_ERROR_3 , REC_ERROR_1 , REC_ERROR_2 , REC_ERROR_3 ,
      REC_ERROR_4 , REC_ERROR_5 , REC_ERROR_6 , REC_ERROR_7 , REC_ERROR_8 ,
      REC_ERROR_9 , REC_ERROR_10 , REC_ERROR_11 , REC_ERROR_12 , REC_ERROR_13 ,
      REC_ERROR_14 , REC_ERROR_15 , REC_ERROR_16 , REC_ERROR_17 , REC_ERROR_18 ,
      REC_ERROR_19 , REC_ERROR_20 , REC_ERROR_21 , REC_ERROR_22 ,
      LET_REC_ERROR_1 , LET_REC_ERROR_2 , LET_REC_ERROR_3 , LET_REC_ERROR_4 ,
      LET_REC_ERROR_5 , LET_REC_ERROR_6 , LET_REC_ERROR_7 , LET_REC_ERROR_8 ,
      LET_REC_ERROR_9 , LET_REC_ERROR_10 , LET_REC_ERROR_11 , LET_REC_ERROR_12 ,
      LET_REC_ERROR_13 , LET_REC_ERROR_14 , LET_REC_ERROR_15 ,
      LET_REC_ERROR_16 , LET_REC_ERROR_17 , LET_REC_ERROR_18 ,
      LET_REC_ERROR_19 , LET_REC_ERROR_20 , LET_REC_ERROR_21 ,
      LET_REC_ERROR_22 , LET_REC_ERROR_23 , LET_REC_ERROR_24 ,
      LET_REC_ERROR_25 , LET_REC_ERROR_26 , LET_REC_ERROR_27 ,
      LET_REC_ERROR_28 , LET_REC_ERROR_29 , LET_REC_ERROR_30 ,
      LET_REC_ERROR_31 , LET_REC_ERROR_32 , LET_REC_ERROR_33 ,
      LET_REC_ERROR_34 , LET_REC_ERROR_35 , LET_REC_ERROR_36 ,
      LET_REC_ERROR_37 , LET_REC_ERROR_38 , LET_REC_ERROR_39 ,
      LET_REC_ERROR_40 , LET_REC_ERROR_41 , LET_REC_ERROR_42 ,
      LET_REC_ERROR_43 , LET_REC_ERROR_44 , LET_REC_ERROR_45 ,
      LET_REC_ERROR_46 , LET_REC_ERROR_47 , LET_REC_ERROR_48 ,
      LET_REC_ERROR_49 , LET_REC_ERROR_50 , LET_REC_ERROR_51 ,
      LET_REC_ERROR_52 , LET_REC_ERROR_53 , LET_REC_ERROR_54 ,
      LET_REC_ERROR_55 , CURRIED_LET_REC_ERROR_1 , CURRIED_LET_REC_ERROR_2 ,
      CURRIED_LET_REC_ERROR_3 , CURRIED_LET_REC_ERROR_4 ,
      CURRIED_LET_REC_ERROR_5 , CURRIED_LET_REC_ERROR_6 ,
      CURRIED_LET_REC_ERROR_7 , CURRIED_LET_REC_ERROR_8 ,
      CURRIED_LET_REC_ERROR_9 , CURRIED_LET_REC_ERROR_10 ,
      CURRIED_LET_REC_ERROR_11 , CURRIED_LET_REC_ERROR_12 ,
      CURRIED_LET_REC_ERROR_13 , CURRIED_LET_REC_ERROR_14 ,
      CURRIED_LET_REC_ERROR_15 , CURRIED_LET_REC_ERROR_16 ,
      CURRIED_LET_REC_ERROR_17 , CURRIED_LET_REC_ERROR_18 ,
      CURRIED_LET_REC_ERROR_19 , CURRIED_LET_REC_ERROR_20 ,
      CURRIED_LET_REC_ERROR_21 , CURRIED_LET_REC_ERROR_22 ,
      CURRIED_LET_REC_ERROR_23 , CURRIED_LET_REC_ERROR_24 ,
      CURRIED_LET_REC_ERROR_25 , CURRIED_LET_REC_ERROR_26 ,
      CURRIED_LET_REC_ERROR_27 , CURRIED_LET_REC_ERROR_28 ,
      CURRIED_LET_REC_ERROR_29 , CURRIED_LET_REC_ERROR_30 ,
      CURRIED_LET_REC_ERROR_31 , CURRIED_LET_REC_ERROR_32 ,
      CURRIED_LET_REC_ERROR_33 , CURRIED_LET_REC_ERROR_34 ,
      CURRIED_LET_REC_ERROR_35 , CURRIED_LET_REC_ERROR_36 ,
      CURRIED_LET_REC_ERROR_37 , CURRIED_LET_REC_ERROR_38 ,
      CURRIED_LET_REC_ERROR_39 , CURRIED_LET_REC_ERROR_40 ,
      INFIX_OPERATION_ERROR_11 , MULTI_LAMBDA_ERROR_1 , MULTI_LAMBDA_ERROR_2 ,
      MULTI_LAMBDA_ERROR_3 , MULTI_LAMBDA_ERROR_4 , MULTI_LAMBDA_ERROR_5 ,
      MULTI_LAMBDA_ERROR_6 , MULTI_LAMBDA_ERROR_7 , MULTI_LAMBDA_ERROR_8 ,
      MULTI_LAMBDA_ERROR_9 , MULTI_LAMBDA_ERROR_10 , MULTI_LAMBDA_ERROR_11 ,
      MULTI_LAMBDA_ERROR_12 , MULTI_LAMBDA_ERROR_13 , MULTI_LAMBDA_ERROR_14 ,
      MULTI_LAMBDA_ERROR_15 , MULTI_LAMBDA_ERROR_16 , MULTI_LAMBDA_ERROR_17 ,
      MULTI_LAMBDA_ERROR_18 , MULTI_LAMBDA_ERROR_19 , MULTI_LAMBDA_ERROR_20 ,
      MULTI_LAMBDA_ERROR_21 , MULTI_LAMBDA_ERROR_22 , MULTI_LAMBDA_ERROR_23 ,
      MULTI_LAMBDA_ERROR_24 , MULTI_LAMBDA_ERROR_25 , MULTI_LAMBDA_ERROR_26 ,
      MULTI_LAMBDA_ERROR_27 , MULTI_LAMBDA_ERROR_28 , MULTI_LAMBDA_ERROR_29 ,
      MULTI_LAMBDA_ERROR_30 , MULTI_LAMBDA_ERROR_31 , MULTI_LAMBDA_ERROR_32 ,
      MULTI_LAMBDA_ERROR_33 , MULTI_LAMBDA_ERROR_34 , MULTI_LAMBDA_ERROR_35 ,
      MULTI_LET_ERROR_1 , MULTI_LET_ERROR_2 , MULTI_LET_ERROR_3 ,
      MULTI_LET_ERROR_4 , MULTI_LET_ERROR_5 , MULTI_LET_ERROR_6 ,
      MULTI_LET_ERROR_7 , MULTI_LET_ERROR_8 , MULTI_LET_ERROR_9 ,
      MULTI_LET_ERROR_10 , MULTI_LET_ERROR_11 , MULTI_LET_ERROR_12 ,
      MULTI_LET_ERROR_13 , MULTI_LET_ERROR_14 , MULTI_LET_ERROR_15 ,
      MULTI_LET_ERROR_16 , MULTI_LET_ERROR_17 , MULTI_LET_ERROR_18 ,
      MULTI_LET_ERROR_19 , MULTI_LET_ERROR_20 , MULTI_LET_ERROR_21 ,
      MULTI_LET_ERROR_22 , MULTI_LET_ERROR_23 , MULTI_LET_ERROR_24 ,
      MULTI_LET_ERROR_25 , MULTI_LET_ERROR_26 , MULTI_LET_ERROR_27 ,
      MULTI_LET_ERROR_28 , MULTI_LET_ERROR_29 , MULTI_LET_ERROR_30 ,
      MULTI_LET_ERROR_31 , MULTI_LET_ERROR_32 , MULTI_LET_ERROR_33 ,
      MULTI_LET_ERROR_34 , MULTI_LET_ERROR_35 , MULTI_LET_ERROR_36 ,
      MULTI_LET_ERROR_37 , MULTI_LET_ERROR_38 , MULTI_LET_ERROR_39 ,
      MULTI_LET_ERROR_40 , MULTI_LET_ERROR_41 , MULTI_LET_ERROR_42 ,
      MULTI_LET_ERROR_43 , MULTI_LET_ERROR_44 , MULTI_LET_ERROR_45 ,
      MULTI_LET_ERROR_46 , MULTI_LET_ERROR_47 , MULTI_LET_ERROR_48 ,
      MULTI_LET_ERROR_49 , MULTI_LET_ERROR_50 , MULTI_LET_ERROR_51 ,
      MULTI_LET_ERROR_52 , MULTI_LET_ERROR_53 , MULTI_LET_ERROR_54 ,
      MULTI_LET_ERROR_55 , MULTI_LET_ERROR_56 , MULTI_LET_ERROR_57 ,
      MULTI_LET_ERROR_58 , MULTI_LET_ERROR_59 , MULTI_LET_ERROR_60 ,
      MULTI_LET_ERROR_61 , MULTI_LET_ERROR_62 , MULTI_LET_ERROR_63 ,
      MULTI_LET_ERROR_64 , MULTI_LET_ERROR_65 , MULTI_LET_ERROR_66 ,
      MULTI_LET_ERROR_67 , MULTI_LET_ERROR_68 , MULTI_LET_ERROR_69 ,
      MULTI_LET_ERROR_70 , MULTI_LET_ERROR_71 , MULTI_LET_ERROR_72 ,
      MULTI_LET_ERROR_73 , MULTI_LET_ERROR_74 , TUPLE_ERROR_1 , TUPLE_ERROR_2 ,
      TUPLE_ERROR_3 , TUPLE_ERROR_4 , TUPLE_ERROR_5 , LIST_ERROR_1 ,
      LIST_ERROR_2 , LIST_ERROR_3 , LIST_ERROR_4 , LIST_ERROR_5 ,
      EXPR_TUPLE_TYPE_ERROR_1 , EXPR_TUPLE_TYPE_ERROR_2 } ;


  private static String [ ] L3TYPE_NORMAL = new String [ ]
  { SIMPLE_TYPE , BOOLEAN_TYPE , INTEGER_TYPE , UNIT_TYPE , TYPE_VARIABLE ,
      TYPE_NAME , REC_TYPE , ARROW_TYPE , TUPLE_TYPE_1 , TUPLE_TYPE_2 ,
      LIST_TYPE } ;


  private static String [ ] L3TYPE_ERROR = new String [ ]
  { SIMPLE_TYPE_ERROR_1 , SIMPLE_TYPE_ERROR_2 , ARROW_TYPE_ERROR ,
      REC_TYPE_ERROR_1 , REC_TYPE_ERROR_2 , REC_TYPE_ERROR_3 ,
      TUPLE_TYPE_ERROR_1 , TUPLE_TYPE_ERROR_2 } ;


  private static String [ ] L4_NORMAL = new String [ ]
  { IDENTIFIER , APPLICATION , INFIX_OPERATION_1 , INFIX_OPERATION_2 ,
      INFIX_OPERATION_3 , INFIX_OPERATION_4 , INFIX_OPERATION_5 ,
      INFIX_OPERATION_6 , INFIX_OPERATION_7 , INFIX_OPERATION_8 ,
      INFIX_OPERATION_9 , INFIX_OPERATION_10 , AND , OR , CONDITION , LAMBDA_1 ,
      LAMBDA_2 , LET_1 , CURRIED_LET_1 , CURRIED_LET_2 , CURRIED_LET_3 ,
      CURRIED_LET_4 , COERCION , UNIT , BOOLEAN_1 , BOOLEAN_2 , INTEGER , NOT ,
      OPERATOR_1 , OPERATOR_2 , OPERATOR_3 , OPERATOR_4 , OPERATOR_5 ,
      OPERATOR_6 , OPERATOR_7 , OPERATOR_8 , OPERATOR_9 , OPERATOR_10 ,
      SIMPLE_EXPR_1 , SIMPLE_EXPR_2 , EXPR_SIMPLE_TYPE , EXPR_BOOLEAN_TYPE ,
      EXPR_INTEGER_TYPE , EXPR_UNIT_TYPE , EXPR_ARROW_TYPE ,
      EXPR_TYPE_VARIABLE , EXPR_TYPE_NAME , EXPR_REC_TYPE , REC_1 , REC_2 ,
      LET_REC_1 , LET_REC_2 , CURRIED_LET_REC_1 , CURRIED_LET_REC_2 ,
      CURRIED_LET_REC_3 , CURRIED_LET_REC_4 , INFIX_OPERATION_11 ,
      MULTI_LAMBDA_1 , MULTI_LAMBDA_2 , MULTI_LET_1 , MULTI_LET_2 , FST , SND ,
      CONS , IS_EMPTY , HD , TL , OPERATOR_11 , EMPTY_LIST , PROJECTION ,
      TUPLE , LIST , EXPR_TUPLE_TYPE_1 , EXPR_TUPLE_TYPE_2 , EXPR_LIST_TYPE ,
      INFIX_OPERATION_12 , CONDITION1 , WHILE , REF , DEREF , OPERATOR_12 ,
      EXPR_REF_TYPE , SEQUENCE_1 , SEQUENCE_2 } ;


  private static String [ ] L4_ERROR = new String [ ]
  { INFIX_OPERATION_ERROR_1 , INFIX_OPERATION_ERROR_2 ,
      INFIX_OPERATION_ERROR_3 , INFIX_OPERATION_ERROR_4 ,
      INFIX_OPERATION_ERROR_5 , INFIX_OPERATION_ERROR_6 ,
      INFIX_OPERATION_ERROR_7 , INFIX_OPERATION_ERROR_8 ,
      INFIX_OPERATION_ERROR_9 , INFIX_OPERATION_ERROR_10 , AND_ERROR ,
      OR_ERROR , CONDITION_ERROR_1 , CONDITION_ERROR_2 , CONDITION_ERROR_3 ,
      CONDITION_ERROR_5 , CONDITION_ERROR_6 , CONDITION_ERROR_7 ,
      CONDITION_ERROR_8 , CONDITION_ERROR_9 , CONDITION_ERROR_10 ,
      CONDITION_ERROR_11 , CONDITION_ERROR_12 , CONDITION_ERROR_13 ,
      CONDITION_ERROR_14 , CONDITION_ERROR_15 , LAMBDA_ERROR_1 ,
      LAMBDA_ERROR_2 , LAMBDA_ERROR_3 , LAMBDA_ERROR_4 , LAMBDA_ERROR_5 ,
      LAMBDA_ERROR_6 , LET_ERROR_1 , LAMBDA_ERROR_7 , LAMBDA_ERROR_8 ,
      LAMBDA_ERROR_9 , LAMBDA_ERROR_10 , LAMBDA_ERROR_11 , LAMBDA_ERROR_12 ,
      LAMBDA_ERROR_13 , LAMBDA_ERROR_14 , LAMBDA_ERROR_15 , LAMBDA_ERROR_16 ,
      LAMBDA_ERROR_17 , LAMBDA_ERROR_18 , LAMBDA_ERROR_19 , LAMBDA_ERROR_20 ,
      LAMBDA_ERROR_21 , LAMBDA_ERROR_22 , LAMBDA_ERROR_23 , LET_ERROR_2 ,
      LET_ERROR_3 , LET_ERROR_4 , LET_ERROR_5 , LET_ERROR_6 , LET_ERROR_7 ,
      LET_ERROR_8 , LET_ERROR_9 , LET_ERROR_10 , LET_ERROR_11 , LET_ERROR_12 ,
      LET_ERROR_13 , LET_ERROR_14 , LET_ERROR_15 , LET_ERROR_16 , LET_ERROR_17 ,
      LET_ERROR_18 , LET_ERROR_19 , LET_ERROR_20 , LET_ERROR_21 , LET_ERROR_22 ,
      LET_ERROR_23 , LET_ERROR_24 , LET_ERROR_25 , LET_ERROR_26 , LET_ERROR_27 ,
      LET_ERROR_28 , LET_ERROR_29 , LET_ERROR_30 , LET_ERROR_31 , LET_ERROR_32 ,
      LET_ERROR_33 , LET_ERROR_34 , LET_ERROR_35 , LET_ERROR_36 , LET_ERROR_37 ,
      LET_ERROR_38 , LET_ERROR_39 , LET_ERROR_40 , LET_ERROR_41 , LET_ERROR_42 ,
      LET_ERROR_43 , LET_ERROR_44 , LET_ERROR_45 , LET_ERROR_46 , LET_ERROR_47 ,
      LET_ERROR_48 , LET_ERROR_49 , LET_ERROR_50 , LET_ERROR_51 , LET_ERROR_52 ,
      CURRIED_LET_ERROR_1 , CURRIED_LET_ERROR_2 , CURRIED_LET_ERROR_3 ,
      CURRIED_LET_ERROR_4 , CURRIED_LET_ERROR_5 , CURRIED_LET_ERROR_6 ,
      CURRIED_LET_ERROR_7 , CURRIED_LET_ERROR_8 , CURRIED_LET_ERROR_9 ,
      CURRIED_LET_ERROR_10 , CURRIED_LET_ERROR_11 , CURRIED_LET_ERROR_12 ,
      CURRIED_LET_ERROR_13 , CURRIED_LET_ERROR_14 , CURRIED_LET_ERROR_15 ,
      CURRIED_LET_ERROR_16 , CURRIED_LET_ERROR_17 , CURRIED_LET_ERROR_18 ,
      CURRIED_LET_ERROR_19 , CURRIED_LET_ERROR_20 , CURRIED_LET_ERROR_21 ,
      CURRIED_LET_ERROR_22 , CURRIED_LET_ERROR_23 , CURRIED_LET_ERROR_24 ,
      CURRIED_LET_ERROR_25 , CURRIED_LET_ERROR_26 , CURRIED_LET_ERROR_27 ,
      CURRIED_LET_ERROR_28 , CURRIED_LET_ERROR_29 , CURRIED_LET_ERROR_30 ,
      CURRIED_LET_ERROR_31 , CURRIED_LET_ERROR_32 , CURRIED_LET_ERROR_33 ,
      CURRIED_LET_ERROR_34 , CURRIED_LET_ERROR_35 , CURRIED_LET_ERROR_36 ,
      CURRIED_LET_ERROR_37 , CURRIED_LET_ERROR_38 , CURRIED_LET_ERROR_39 ,
      CURRIED_LET_ERROR_40 , COERCION_ERROR_1 , COERCION_ERROR_2 ,
      COERCION_ERROR_3 , COERCION_ERROR_4 , COERCION_ERROR_5 ,
      COERCION_ERROR_6 , COERCION_ERROR_7 , COERCION_ERROR_8 ,
      COERCION_ERROR_9 , COERCION_ERROR_10 , COERCION_ERROR_11 ,
      COERCION_ERROR_12 , COERCION_ERROR_13 , COERCION_ERROR_14 ,
      COERCION_ERROR_15 , COERCION_ERROR_16 , COERCION_ERROR_17 ,
      COERCION_ERROR_18 , COERCION_ERROR_19 , SIMPLE_EXPR_ERROR_1 ,
      SIMPLE_EXPR_ERROR_2 , SIMPLE_EXPR_ERROR_3 , EXPR_SIMPLE_TYPE_ERROR_1 ,
      EXPR_SIMPLE_TYPE_ERROR_2 , EXPR_ARROW_TYPE_ERROR , EXPR_REC_TYPE_ERROR_1 ,
      EXPR_REC_TYPE_ERROR_2 , EXPR_REC_TYPE_ERROR_3 , REC_ERROR_1 ,
      REC_ERROR_2 , REC_ERROR_3 , REC_ERROR_4 , REC_ERROR_5 , REC_ERROR_6 ,
      REC_ERROR_7 , REC_ERROR_8 , REC_ERROR_9 , REC_ERROR_10 , REC_ERROR_11 ,
      REC_ERROR_12 , REC_ERROR_13 , REC_ERROR_14 , REC_ERROR_15 , REC_ERROR_16 ,
      REC_ERROR_17 , REC_ERROR_18 , REC_ERROR_19 , REC_ERROR_20 , REC_ERROR_21 ,
      REC_ERROR_22 , LET_REC_ERROR_1 , LET_REC_ERROR_2 , LET_REC_ERROR_3 ,
      LET_REC_ERROR_4 , LET_REC_ERROR_5 , LET_REC_ERROR_6 , LET_REC_ERROR_7 ,
      LET_REC_ERROR_8 , LET_REC_ERROR_9 , LET_REC_ERROR_10 , LET_REC_ERROR_11 ,
      LET_REC_ERROR_12 , LET_REC_ERROR_13 , LET_REC_ERROR_14 ,
      LET_REC_ERROR_15 , LET_REC_ERROR_16 , LET_REC_ERROR_17 ,
      LET_REC_ERROR_18 , LET_REC_ERROR_19 , LET_REC_ERROR_20 ,
      LET_REC_ERROR_21 , LET_REC_ERROR_22 , LET_REC_ERROR_23 ,
      LET_REC_ERROR_24 , LET_REC_ERROR_25 , LET_REC_ERROR_26 ,
      LET_REC_ERROR_27 , LET_REC_ERROR_28 , LET_REC_ERROR_29 ,
      LET_REC_ERROR_30 , LET_REC_ERROR_31 , LET_REC_ERROR_32 ,
      LET_REC_ERROR_33 , LET_REC_ERROR_34 , LET_REC_ERROR_35 ,
      LET_REC_ERROR_36 , LET_REC_ERROR_37 , LET_REC_ERROR_38 ,
      LET_REC_ERROR_39 , LET_REC_ERROR_40 , LET_REC_ERROR_41 ,
      LET_REC_ERROR_42 , LET_REC_ERROR_43 , LET_REC_ERROR_44 ,
      LET_REC_ERROR_45 , LET_REC_ERROR_46 , LET_REC_ERROR_47 ,
      LET_REC_ERROR_48 , LET_REC_ERROR_49 , LET_REC_ERROR_50 ,
      LET_REC_ERROR_51 , LET_REC_ERROR_52 , LET_REC_ERROR_53 ,
      LET_REC_ERROR_54 , LET_REC_ERROR_55 , CURRIED_LET_REC_ERROR_1 ,
      CURRIED_LET_REC_ERROR_2 , CURRIED_LET_REC_ERROR_3 ,
      CURRIED_LET_REC_ERROR_4 , CURRIED_LET_REC_ERROR_5 ,
      CURRIED_LET_REC_ERROR_6 , CURRIED_LET_REC_ERROR_7 ,
      CURRIED_LET_REC_ERROR_8 , CURRIED_LET_REC_ERROR_9 ,
      CURRIED_LET_REC_ERROR_10 , CURRIED_LET_REC_ERROR_11 ,
      CURRIED_LET_REC_ERROR_12 , CURRIED_LET_REC_ERROR_13 ,
      CURRIED_LET_REC_ERROR_14 , CURRIED_LET_REC_ERROR_15 ,
      CURRIED_LET_REC_ERROR_16 , CURRIED_LET_REC_ERROR_17 ,
      CURRIED_LET_REC_ERROR_18 , CURRIED_LET_REC_ERROR_19 ,
      CURRIED_LET_REC_ERROR_20 , CURRIED_LET_REC_ERROR_21 ,
      CURRIED_LET_REC_ERROR_22 , CURRIED_LET_REC_ERROR_23 ,
      CURRIED_LET_REC_ERROR_24 , CURRIED_LET_REC_ERROR_25 ,
      CURRIED_LET_REC_ERROR_26 , CURRIED_LET_REC_ERROR_27 ,
      CURRIED_LET_REC_ERROR_28 , CURRIED_LET_REC_ERROR_29 ,
      CURRIED_LET_REC_ERROR_30 , CURRIED_LET_REC_ERROR_31 ,
      CURRIED_LET_REC_ERROR_32 , CURRIED_LET_REC_ERROR_33 ,
      CURRIED_LET_REC_ERROR_34 , CURRIED_LET_REC_ERROR_35 ,
      CURRIED_LET_REC_ERROR_36 , CURRIED_LET_REC_ERROR_37 ,
      CURRIED_LET_REC_ERROR_38 , CURRIED_LET_REC_ERROR_39 ,
      CURRIED_LET_REC_ERROR_40 , INFIX_OPERATION_ERROR_11 ,
      MULTI_LAMBDA_ERROR_1 , MULTI_LAMBDA_ERROR_2 , MULTI_LAMBDA_ERROR_3 ,
      MULTI_LAMBDA_ERROR_4 , MULTI_LAMBDA_ERROR_5 , MULTI_LAMBDA_ERROR_6 ,
      MULTI_LAMBDA_ERROR_7 , MULTI_LAMBDA_ERROR_8 , MULTI_LAMBDA_ERROR_9 ,
      MULTI_LAMBDA_ERROR_10 , MULTI_LAMBDA_ERROR_11 , MULTI_LAMBDA_ERROR_12 ,
      MULTI_LAMBDA_ERROR_13 , MULTI_LAMBDA_ERROR_14 , MULTI_LAMBDA_ERROR_15 ,
      MULTI_LAMBDA_ERROR_16 , MULTI_LAMBDA_ERROR_17 , MULTI_LAMBDA_ERROR_18 ,
      MULTI_LAMBDA_ERROR_19 , MULTI_LAMBDA_ERROR_20 , MULTI_LAMBDA_ERROR_21 ,
      MULTI_LAMBDA_ERROR_22 , MULTI_LAMBDA_ERROR_23 , MULTI_LAMBDA_ERROR_24 ,
      MULTI_LAMBDA_ERROR_25 , MULTI_LAMBDA_ERROR_26 , MULTI_LAMBDA_ERROR_27 ,
      MULTI_LAMBDA_ERROR_28 , MULTI_LAMBDA_ERROR_29 , MULTI_LAMBDA_ERROR_30 ,
      MULTI_LAMBDA_ERROR_31 , MULTI_LAMBDA_ERROR_32 , MULTI_LAMBDA_ERROR_33 ,
      MULTI_LAMBDA_ERROR_34 , MULTI_LAMBDA_ERROR_35 , MULTI_LET_ERROR_1 ,
      MULTI_LET_ERROR_2 , MULTI_LET_ERROR_3 , MULTI_LET_ERROR_4 ,
      MULTI_LET_ERROR_5 , MULTI_LET_ERROR_6 , MULTI_LET_ERROR_7 ,
      MULTI_LET_ERROR_8 , MULTI_LET_ERROR_9 , MULTI_LET_ERROR_10 ,
      MULTI_LET_ERROR_11 , MULTI_LET_ERROR_12 , MULTI_LET_ERROR_13 ,
      MULTI_LET_ERROR_14 , MULTI_LET_ERROR_15 , MULTI_LET_ERROR_16 ,
      MULTI_LET_ERROR_17 , MULTI_LET_ERROR_18 , MULTI_LET_ERROR_19 ,
      MULTI_LET_ERROR_20 , MULTI_LET_ERROR_21 , MULTI_LET_ERROR_22 ,
      MULTI_LET_ERROR_23 , MULTI_LET_ERROR_24 , MULTI_LET_ERROR_25 ,
      MULTI_LET_ERROR_26 , MULTI_LET_ERROR_27 , MULTI_LET_ERROR_28 ,
      MULTI_LET_ERROR_29 , MULTI_LET_ERROR_30 , MULTI_LET_ERROR_31 ,
      MULTI_LET_ERROR_32 , MULTI_LET_ERROR_33 , MULTI_LET_ERROR_34 ,
      MULTI_LET_ERROR_35 , MULTI_LET_ERROR_36 , MULTI_LET_ERROR_37 ,
      MULTI_LET_ERROR_38 , MULTI_LET_ERROR_39 , MULTI_LET_ERROR_40 ,
      MULTI_LET_ERROR_41 , MULTI_LET_ERROR_42 , MULTI_LET_ERROR_43 ,
      MULTI_LET_ERROR_44 , MULTI_LET_ERROR_45 , MULTI_LET_ERROR_46 ,
      MULTI_LET_ERROR_47 , MULTI_LET_ERROR_48 , MULTI_LET_ERROR_49 ,
      MULTI_LET_ERROR_50 , MULTI_LET_ERROR_51 , MULTI_LET_ERROR_52 ,
      MULTI_LET_ERROR_53 , MULTI_LET_ERROR_54 , MULTI_LET_ERROR_55 ,
      MULTI_LET_ERROR_56 , MULTI_LET_ERROR_57 , MULTI_LET_ERROR_58 ,
      MULTI_LET_ERROR_59 , MULTI_LET_ERROR_60 , MULTI_LET_ERROR_61 ,
      MULTI_LET_ERROR_62 , MULTI_LET_ERROR_63 , MULTI_LET_ERROR_64 ,
      MULTI_LET_ERROR_65 , MULTI_LET_ERROR_66 , MULTI_LET_ERROR_67 ,
      MULTI_LET_ERROR_68 , MULTI_LET_ERROR_69 , MULTI_LET_ERROR_70 ,
      MULTI_LET_ERROR_71 , MULTI_LET_ERROR_72 , MULTI_LET_ERROR_73 ,
      MULTI_LET_ERROR_74 , TUPLE_ERROR_1 , TUPLE_ERROR_2 , TUPLE_ERROR_3 ,
      TUPLE_ERROR_4 , TUPLE_ERROR_5 , LIST_ERROR_1 , LIST_ERROR_2 ,
      LIST_ERROR_3 , LIST_ERROR_4 , LIST_ERROR_5 , EXPR_TUPLE_TYPE_ERROR_1 ,
      EXPR_TUPLE_TYPE_ERROR_2 , INFIX_OPERATION_ERROR_12 , WHILE_ERROR_1 ,
      WHILE_ERROR_2 , WHILE_ERROR_3 , SEQUENCE_ERROR_1 , SEQUENCE_ERROR_2 } ;


  private static String [ ] L4TYPE_NORMAL = new String [ ]
  { SIMPLE_TYPE , BOOLEAN_TYPE , INTEGER_TYPE , UNIT_TYPE , TYPE_VARIABLE ,
      TYPE_NAME , REC_TYPE , ARROW_TYPE , TUPLE_TYPE_1 , TUPLE_TYPE_2 ,
      LIST_TYPE , REF_TYPE } ;


  private static String [ ] L4TYPE_ERROR = new String [ ]
  { SIMPLE_TYPE_ERROR_1 , SIMPLE_TYPE_ERROR_2 , ARROW_TYPE_ERROR ,
      REC_TYPE_ERROR_1 , REC_TYPE_ERROR_2 , REC_TYPE_ERROR_3 ,
      TUPLE_TYPE_ERROR_1 , TUPLE_TYPE_ERROR_2 } ;


  private static int max = 0 ;


  static int countNormal = L0_NORMAL.length + L1_NORMAL.length
      + L2_NORMAL.length + L2O_NORMAL.length + L2C_NORMAL.length
      + L3_NORMAL.length + L4_NORMAL.length ;


  static int countNormalType = L1TYPE_NORMAL.length + L2TYPE_NORMAL.length
      + L2OTYPE_NORMAL.length + L2CTYPE_NORMAL.length + L3TYPE_NORMAL.length
      + L4TYPE_NORMAL.length ;


  static int countError = L0_ERROR.length + L1_ERROR.length + L2_ERROR.length
      + L2O_ERROR.length + L2C_ERROR.length + L3_ERROR.length + L4_ERROR.length ;


  static int countErrorType = L1TYPE_ERROR.length + L2TYPE_ERROR.length
      + L2OTYPE_ERROR.length + L2CTYPE_ERROR.length + L3TYPE_ERROR.length
      + L4TYPE_ERROR.length ;


  public static String delete ( String s )
  {
    return s.replaceAll ( "<html>" , "" ).replaceAll ( "</html>" , "" )
        .replaceAll ( "<sub>" , "" ).replaceAll ( "</sub>" , "" ).replaceAll (
            "&lt" , "<" ).replaceAll ( "&gt" , ">" ).replaceAll ( "&amp" , "&" )
        .replaceAll ( "<br>" , "  -  " ) ;
  }


  public static String fillString ( String s , int i )
  {
    StringBuilder result = new StringBuilder ( s.length ( ) + i ) ;
    result.append ( s ) ;
    while ( i > 0 )
    {
      result.append ( " " ) ;
      i -- ;
    }
    return result.toString ( ) ;
  }


  private static boolean onlyError = false ;


  public static void main ( String [ ] pArguments )
  {
    if ( pArguments.length > 0 )
    {
      if ( pArguments [ 0 ].equals ( "error" ) )
      {
        onlyError = true ;
      }
    }
    max ( ) ;
    boolean l0Okay = test_L0 ( ) ;
    boolean l1Okay = test_L1 ( ) ;
    boolean l2Okay = test_L2 ( ) ;
    boolean l2OOkay = test_L2O ( ) ;
    boolean l2COkay = test_L2C ( ) ;
    boolean l3Okay = test_L3 ( ) ;
    boolean l4Okay = test_L4 ( ) ;
    output ( "*** Overview ***" , Output.NORMAL ) ;
    output ( "Count Normal:      " + countNormal , Output.NORMAL ) ;
    output ( "Count Normal Type: " + countNormalType , Output.NORMAL ) ;
    output ( "Count Error:       " + countError , Output.NORMAL ) ;
    output ( "Count Error Type:  " + countErrorType , Output.NORMAL ) ;
    output ( "Count:             "
        + ( countNormal + countNormalType + countError + countErrorType ) ,
        Output.NORMAL ) ;
    if ( l0Okay && l1Okay && l2Okay && l2COkay && l2OOkay && l3Okay && l4Okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
  }


  public static void max ( )
  {
    for ( String s : L0_NORMAL )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L0_ERROR )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L1_NORMAL )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L1_ERROR )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L1TYPE_NORMAL )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L1TYPE_ERROR )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L2_NORMAL )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L2_ERROR )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L2TYPE_NORMAL )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L2TYPE_ERROR )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L2O_NORMAL )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L2O_ERROR )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L2OTYPE_NORMAL )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L2OTYPE_ERROR )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L2C_NORMAL )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L2C_ERROR )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L2CTYPE_NORMAL )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L2CTYPE_ERROR )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L3_NORMAL )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L3_ERROR )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L3TYPE_NORMAL )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L3TYPE_ERROR )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L4_NORMAL )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L4_ERROR )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L4TYPE_NORMAL )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L4TYPE_ERROR )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
  }


  public static synchronized void output ( String s , Output pOutput )
  {
    switch ( pOutput )
    {
      case NORMAL :
      {
        if ( ! onlyError )
        {
          System.out.println ( s ) ;
          System.out.flush ( ) ;
        }
        break ;
      }
      case ERROR :
      {
        System.err.println ( s ) ;
        System.err.flush ( ) ;
        break ;
      }
    }
  }


  public static boolean parseErrorExpression ( Language pLanguage , String pText )
  {
    StringBuilder text = new StringBuilder ( pText ) ;
    try
    {
      Expression expression = pLanguage.newParser (
          new StringReader ( text.toString ( ) ) ).parse ( ) ;
      output ( "Successful: \""
          + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
          + expression.getClass ( ).getSimpleName ( ) , Output.ERROR ) ;
      return false ;
    }
    catch ( LanguageParserMultiException e1 )
    {
      output ( "Multi:      \""
          + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
          + delete ( e1.getMessage ( ) ) , Output.ERROR ) ;
      return false ;
    }
    catch ( LanguageParserWarningException e1 )
    {
      output ( "Warning:    \""
          + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
          + delete ( e1.getMessage ( ) ) , Output.NORMAL ) ;
      // Insert the first time
      text.insert ( e1.getRight ( ) , e1.getInsertText ( ) ) ;
      try
      {
        countError ++ ;
        Expression expression = pLanguage.newParser (
            new StringReader ( text.toString ( ) ) ).parse ( ) ;
        output ( "Inserted:   \""
            + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
            + expression.getClass ( ).getSimpleName ( ) , Output.NORMAL ) ;
      }
      catch ( LanguageParserWarningException e2 )
      {
        // Insert the second time
        text.insert ( e2.getRight ( ) , e2.getInsertText ( ) ) ;
        try
        {
          countError ++ ;
          Expression expression = pLanguage.newParser (
              new StringReader ( text.toString ( ) ) ).parse ( ) ;
          output ( "Inserted:   \""
              + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
              + expression.getClass ( ).getSimpleName ( ) , Output.NORMAL ) ;
        }
        catch ( LanguageParserWarningException e3 )
        {
          // Insert the third time
          text.insert ( e3.getRight ( ) , e3.getInsertText ( ) ) ;
          try
          {
            countError ++ ;
            Expression expression = pLanguage.newParser (
                new StringReader ( text.toString ( ) ) ).parse ( ) ;
            output ( "Inserted:   \""
                + fillString ( text + "\"" , max - text.length ( ) + 1 )
                + "   " + expression.getClass ( ).getSimpleName ( ) ,
                Output.NORMAL ) ;
          }
          catch ( LanguageParserWarningException e4 )
          {
            // Insert the fourth time
            text.insert ( e4.getRight ( ) , e4.getInsertText ( ) ) ;
            try
            {
              countError ++ ;
              Expression expression = pLanguage.newParser (
                  new StringReader ( text.toString ( ) ) ).parse ( ) ;
              output ( "Inserted:   \""
                  + fillString ( text + "\"" , max - text.length ( ) + 1 )
                  + "   " + expression.getClass ( ).getSimpleName ( ) ,
                  Output.NORMAL ) ;
            }
            catch ( Exception e5 )
            {
              output ( "Inserted:   \""
                  + fillString ( text + "\"" , max - text.length ( ) + 1 )
                  + "   " + delete ( e5.getMessage ( ) ) , Output.ERROR ) ;
              return false ;
            }
          }
          catch ( Exception e4 )
          {
            output ( "Inserted:   \""
                + fillString ( text + "\"" , max - text.length ( ) + 1 )
                + "   " + delete ( e4.getMessage ( ) ) , Output.ERROR ) ;
            return false ;
          }
        }
        catch ( Exception e3 )
        {
          output ( "Inserted:   \""
              + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
              + delete ( e3.getMessage ( ) ) , Output.ERROR ) ;
          return false ;
        }
      }
      catch ( Exception e2 )
      {
        output ( "Inserted:   \""
            + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
            + delete ( e2.getMessage ( ) ) , Output.ERROR ) ;
        return false ;
      }
    }
    catch ( LanguageParserException e1 )
    {
      output ( "Parser:     \""
          + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
          + delete ( e1.getMessage ( ) ) , Output.ERROR ) ;
      return false ;
    }
    catch ( Exception e1 )
    {
      output ( "Exception:  \""
          + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
          + delete ( e1.getMessage ( ) ) , Output.ERROR ) ;
      return false ;
    }
    return true ;
  }


  public static boolean parseErrorType ( Language pLanguage , String pText )
  {
    StringBuilder text = new StringBuilder ( pText ) ;
    try
    {
      Type type = pLanguage.newTypeParser (
          new StringReader ( text.toString ( ) ) ).parse ( ) ;
      output ( "Successful: \""
          + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
          + type.getClass ( ).getSimpleName ( ) , Output.ERROR ) ;
      return false ;
    }
    catch ( LanguageParserMultiException e1 )
    {
      output ( "Multi:      \""
          + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
          + delete ( e1.getMessage ( ) ) , Output.ERROR ) ;
      return false ;
    }
    catch ( LanguageParserWarningException e1 )
    {
      output ( "Warning:    \""
          + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
          + delete ( e1.getMessage ( ) ) , Output.NORMAL ) ;
      // Insert the first time
      text.insert ( e1.getRight ( ) , e1.getInsertText ( ) ) ;
      try
      {
        countErrorType ++ ;
        Type type = pLanguage.newTypeParser (
            new StringReader ( text.toString ( ) ) ).parse ( ) ;
        output ( "Inserted:   \""
            + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
            + type.getClass ( ).getSimpleName ( ) , Output.NORMAL ) ;
      }
      catch ( LanguageParserWarningException e2 )
      {
        // Insert the second time
        text.insert ( e2.getRight ( ) , e2.getInsertText ( ) ) ;
        try
        {
          countErrorType ++ ;
          Type type = pLanguage.newTypeParser (
              new StringReader ( text.toString ( ) ) ).parse ( ) ;
          output ( "Inserted:   \""
              + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
              + type.getClass ( ).getSimpleName ( ) , Output.NORMAL ) ;
        }
        catch ( LanguageParserWarningException e3 )
        {
          // Insert the third time
          text.insert ( e3.getRight ( ) , e3.getInsertText ( ) ) ;
          try
          {
            countErrorType ++ ;
            Type type = pLanguage.newTypeParser (
                new StringReader ( text.toString ( ) ) ).parse ( ) ;
            output ( "Inserted:   \""
                + fillString ( text + "\"" , max - text.length ( ) + 1 )
                + "   " + type.getClass ( ).getSimpleName ( ) , Output.NORMAL ) ;
          }
          catch ( LanguageParserWarningException e4 )
          {
            // Insert the fourth time
            text.insert ( e4.getRight ( ) , e4.getInsertText ( ) ) ;
            try
            {
              countErrorType ++ ;
              Type type = pLanguage.newTypeParser (
                  new StringReader ( text.toString ( ) ) ).parse ( ) ;
              output ( "Inserted:   \""
                  + fillString ( text + "\"" , max - text.length ( ) + 1 )
                  + "   " + type.getClass ( ).getSimpleName ( ) , Output.NORMAL ) ;
            }
            catch ( Exception e5 )
            {
              output ( "Inserted:   \""
                  + fillString ( text + "\"" , max - text.length ( ) + 1 )
                  + "   " + delete ( e5.getMessage ( ) ) , Output.ERROR ) ;
              return false ;
            }
          }
          catch ( Exception e4 )
          {
            output ( "Inserted:   \""
                + fillString ( text + "\"" , max - text.length ( ) + 1 )
                + "   " + delete ( e4.getMessage ( ) ) , Output.ERROR ) ;
            return false ;
          }
        }
        catch ( Exception e3 )
        {
          output ( "Inserted:   \""
              + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
              + delete ( e3.getMessage ( ) ) , Output.ERROR ) ;
          return false ;
        }
      }
      catch ( Exception e2 )
      {
        output ( "Inserted:   \""
            + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
            + delete ( e2.getMessage ( ) ) , Output.ERROR ) ;
        return false ;
      }
    }
    catch ( LanguageParserException e1 )
    {
      output ( "Parser:     \""
          + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
          + delete ( e1.getMessage ( ) ) , Output.ERROR ) ;
      return false ;
    }
    catch ( Exception e1 )
    {
      output ( "Exception:  \""
          + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
          + delete ( e1.getMessage ( ) ) , Output.ERROR ) ;
      return false ;
    }
    return true ;
  }


  public static boolean parseNormalExpression ( Language pLanguage ,
      String pText )
  {
    String text = pText ;
    try
    {
      Expression e = pLanguage.newParser ( new StringReader ( text ) ).parse ( ) ;
      output ( "Successful: \""
          + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
          + e.getClass ( ).getSimpleName ( ) , Output.NORMAL ) ;
    }
    catch ( LanguageParserMultiException e )
    {
      output ( "Multi:      \""
          + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
          + delete ( e.getMessage ( ) ) , Output.ERROR ) ;
      return false ;
    }
    catch ( LanguageParserWarningException e )
    {
      output ( "Warning:    \""
          + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
          + delete ( e.getMessage ( ) ) , Output.ERROR ) ;
      return false ;
    }
    catch ( LanguageParserException e )
    {
      output ( "Parser:     \""
          + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
          + delete ( e.getMessage ( ) ) , Output.ERROR ) ;
      return false ;
    }
    catch ( Exception e )
    {
      output ( "Exception:  \""
          + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
          + delete ( e.getMessage ( ) ) , Output.ERROR ) ;
      return false ;
    }
    return true ;
  }


  public static boolean parseNormalType ( Language pLanguage , String pText )
  {
    String text = pText ;
    try
    {
      Type type = pLanguage.newTypeParser ( new StringReader ( text ) )
          .parse ( ) ;
      output ( "Successful: \""
          + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
          + type.getClass ( ).getSimpleName ( ) , Output.NORMAL ) ;
    }
    catch ( LanguageParserMultiException e )
    {
      output ( "Multi:      \""
          + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
          + delete ( e.getMessage ( ) ) , Output.ERROR ) ;
      return false ;
    }
    catch ( LanguageParserWarningException e )
    {
      output ( "Warning:    \""
          + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
          + delete ( e.getMessage ( ) ) , Output.ERROR ) ;
      return false ;
    }
    catch ( LanguageParserException e )
    {
      output ( "Parser:     \""
          + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
          + delete ( e.getMessage ( ) ) , Output.ERROR ) ;
      return false ;
    }
    catch ( Exception e )
    {
      output ( "Exception:  \""
          + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
          + delete ( e.getMessage ( ) ) , Output.ERROR ) ;
      return false ;
    }
    return true ;
  }


  public static boolean test_L0 ( )
  {
    boolean l0NormalOkay = test_L0_Normal ( ) ;
    boolean l0ErrorOkay = test_L0_Error ( ) ;
    output ( "*** L0 ***" , Output.NORMAL ) ;
    if ( l0NormalOkay )
    {
      output ( "L0 Normal:        " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L0 Normal:        " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l0ErrorOkay )
    {
      output ( "L0 Error:         " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L0 Error:         " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l0NormalOkay && l0ErrorOkay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return l0NormalOkay && l0ErrorOkay ;
  }


  public static boolean test_L0_Error ( )
  {
    output ( "*** L0 Error ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l0" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L0_ERROR )
    {
      okay = parseErrorExpression ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L0_Normal ( )
  {
    output ( "*** L0 Normal ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l0" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L0_NORMAL )
    {
      okay = parseNormalExpression ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L1 ( )
  {
    boolean l1NormalOkay = test_L1_Normal ( ) ;
    boolean l1ErrorOkay = test_L1_Error ( ) ;
    boolean l1TypeNormalOkay = test_L1_Type_Normal ( ) ;
    boolean l1TypeErrorOkay = test_L1_Type_Error ( ) ;
    output ( "*** L1 ***" , Output.NORMAL ) ;
    if ( l1NormalOkay )
    {
      output ( "L1 Normal:        " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L1 Normal:        " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l1ErrorOkay )
    {
      output ( "L1 Error:         " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L1 Error:         " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l1TypeNormalOkay )
    {
      output ( "L1Type Normal:    " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L1Type Normal:    " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l1TypeErrorOkay )
    {
      output ( "L1Type Error:     " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L1Type Error:     " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l1NormalOkay && l1ErrorOkay && l1TypeNormalOkay && l1TypeErrorOkay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return l1NormalOkay && l1ErrorOkay && l1TypeNormalOkay && l1TypeErrorOkay ;
  }


  public static boolean test_L1_Error ( )
  {
    output ( "*** L1 Error ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l1" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L1_ERROR )
    {
      okay = parseErrorExpression ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L1_Normal ( )
  {
    output ( "*** L1 Normal ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l1" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L1_NORMAL )
    {
      okay = parseNormalExpression ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L1_Type_Error ( )
  {
    output ( "*** L1Type Error ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l1" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L1TYPE_ERROR )
    {
      okay = parseErrorType ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L1_Type_Normal ( )
  {
    output ( "*** L1Type Normal ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l1" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L1TYPE_NORMAL )
    {
      okay = parseNormalType ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L2 ( )
  {
    boolean l2NormalOkay = test_L2_Normal ( ) ;
    boolean l2ErrorOkay = test_L2_Error ( ) ;
    boolean l2TypeNormalOkay = test_L2_Type_Normal ( ) ;
    boolean l2TypeErrorOkay = test_L2_Type_Error ( ) ;
    output ( "*** L2 ***" , Output.NORMAL ) ;
    if ( l2NormalOkay )
    {
      output ( "L2 Normal:        " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L2 Normal:        " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l2ErrorOkay )
    {
      output ( "L2 Error:         " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L2 Error:         " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l2TypeNormalOkay )
    {
      output ( "L2Type Normal:    " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L2Type Normal:    " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l2TypeErrorOkay )
    {
      output ( "L2Type Error:     " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L2Type Error:     " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l2NormalOkay && l2ErrorOkay && l2TypeNormalOkay && l2TypeErrorOkay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return l2NormalOkay && l2ErrorOkay && l2TypeNormalOkay && l2TypeErrorOkay ;
  }


  public static boolean test_L2_Error ( )
  {
    output ( "*** L2 Error ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l2" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L2_ERROR )
    {
      okay = parseErrorExpression ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L2_Normal ( )
  {
    output ( "*** L2 Normal ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l2" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L2_NORMAL )
    {
      okay = parseNormalExpression ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L2_Type_Error ( )
  {
    output ( "*** L2Type Error ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l2" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L2TYPE_ERROR )
    {
      okay = parseErrorType ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L2_Type_Normal ( )
  {
    output ( "*** L2Type Normal ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l2" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L2TYPE_NORMAL )
    {
      okay = parseNormalType ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L2C ( )
  {
    boolean l2CNormalOkay = test_L2C_Normal ( ) ;
    boolean l2CErrorOkay = test_L2C_Error ( ) ;
    boolean l2CTypeNormalOkay = test_L2C_Type_Normal ( ) ;
    boolean l2CTypeErrorOkay = test_L2C_Type_Error ( ) ;
    output ( "*** L2C ***" , Output.NORMAL ) ;
    if ( l2CNormalOkay )
    {
      output ( "L2C Normal:       " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L2C Normal:       " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l2CErrorOkay )
    {
      output ( "L2C Error:        " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L2C Error:        " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l2CTypeNormalOkay )
    {
      output ( "L2CType Normal:   " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L2CType Normal:   " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l2CTypeErrorOkay )
    {
      output ( "L2CType Error:    " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L2CType Error:    " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l2CNormalOkay && l2CErrorOkay && l2CTypeNormalOkay && l2CTypeErrorOkay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return l2CNormalOkay && l2CErrorOkay && l2CTypeNormalOkay
        && l2CTypeErrorOkay ;
  }


  public static boolean test_L2C_Error ( )
  {
    output ( "*** L2C Error ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l2C" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L2C_ERROR )
    {
      okay = parseErrorExpression ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L2C_Normal ( )
  {
    output ( "*** L2C Normal ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l2C" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L2C_NORMAL )
    {
      okay = parseNormalExpression ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L2C_Type_Error ( )
  {
    output ( "*** L2CType Error ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l2C" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L2CTYPE_ERROR )
    {
      okay = parseErrorType ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L2C_Type_Normal ( )
  {
    output ( "*** L2CType Normal ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l2C" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L2CTYPE_NORMAL )
    {
      okay = parseNormalType ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L2O ( )
  {
    boolean l2ONormalOkay = test_L2O_Normal ( ) ;
    boolean l2OErrorOkay = test_L2O_Error ( ) ;
    boolean l2OTypeNormalOkay = test_L2O_Type_Normal ( ) ;
    boolean l2OTypeErrorOkay = test_L2O_Type_Error ( ) ;
    output ( "*** L2O ***" , Output.NORMAL ) ;
    if ( l2ONormalOkay )
    {
      output ( "L2O Normal:       " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L2O Normal:       " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l2OErrorOkay )
    {
      output ( "L2O Error:        " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L2O Error:        " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l2OTypeNormalOkay )
    {
      output ( "L2OType Normal:   " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L2OType Normal:   " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l2OTypeErrorOkay )
    {
      output ( "L2OType Error:    " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L2OType Error:    " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l2ONormalOkay && l2OErrorOkay && l2OTypeNormalOkay && l2OTypeErrorOkay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return l2ONormalOkay && l2OErrorOkay && l2OTypeNormalOkay
        && l2OTypeErrorOkay ;
  }


  public static boolean test_L2O_Error ( )
  {
    output ( "*** L2O Error ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l2O" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L2O_ERROR )
    {
      okay = parseErrorExpression ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L2O_Normal ( )
  {
    output ( "*** L2O Normal ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l2O" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L2O_NORMAL )
    {
      okay = parseNormalExpression ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L2O_Type_Error ( )
  {
    output ( "*** L2OType Error ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l2O" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L2OTYPE_ERROR )
    {
      okay = parseErrorType ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L2O_Type_Normal ( )
  {
    output ( "*** L2OType Normal ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l2O" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L2OTYPE_NORMAL )
    {
      okay = parseNormalType ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L3 ( )
  {
    boolean l3NormalOkay = test_L3_Normal ( ) ;
    boolean l3ErrorOkay = test_L3_Error ( ) ;
    boolean l3TypeNormalOkay = test_L3_Type_Normal ( ) ;
    boolean l3TypeErrorOkay = test_L3_Type_Error ( ) ;
    output ( "*** L3 ***" , Output.NORMAL ) ;
    if ( l3NormalOkay )
    {
      output ( "L3 Normal:        " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L3 Normal:        " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l3ErrorOkay )
    {
      output ( "L3 Error:         " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L3 Error:         " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.NORMAL ) ;
    }
    if ( l3TypeNormalOkay )
    {
      output ( "L3Type Normal:    " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L3Type Normal:    " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l3TypeErrorOkay )
    {
      output ( "L3Type Error:     " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L3Type Error:     " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l3NormalOkay && l3ErrorOkay && l3TypeNormalOkay && l3TypeErrorOkay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return l3NormalOkay && l3ErrorOkay && l3TypeNormalOkay && l3TypeErrorOkay ;
  }


  public static boolean test_L3_Error ( )
  {
    output ( "*** L3 Error ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l3" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L3_ERROR )
    {
      okay = parseErrorExpression ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L3_Normal ( )
  {
    output ( "*** L3 Normal ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l3" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L3_NORMAL )
    {
      okay = parseNormalExpression ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L3_Type_Error ( )
  {
    output ( "*** L3Type Error ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l3" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L3TYPE_ERROR )
    {
      okay = parseErrorType ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L3_Type_Normal ( )
  {
    output ( "*** L3Type Normal ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l3" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L3TYPE_NORMAL )
    {
      okay = parseNormalType ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L4 ( )
  {
    boolean l4NormalOkay = test_L4_Normal ( ) ;
    boolean l4ErrorOkay = test_L4_Error ( ) ;
    boolean l4TypeNormalOkay = test_L4_Type_Normal ( ) ;
    boolean l4TypeErrorOkay = test_L4_Type_Error ( ) ;
    output ( "*** L4 ***" , Output.NORMAL ) ;
    if ( l4NormalOkay )
    {
      output ( "L4 Normal:        " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L4 Normal:        " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l4ErrorOkay )
    {
      output ( "L4 Error:         " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L4 Error:         " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l4TypeNormalOkay )
    {
      output ( "L4Type Normal:    " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L4Type Normal:    " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l4TypeErrorOkay )
    {
      output ( "L4Type Error:     " + fillString ( "" , max ) + "SUCCESSFUL" ,
          Output.NORMAL ) ;
    }
    else
    {
      output ( "L4Type Error:     " + fillString ( "" , max )
          + "NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    if ( l4NormalOkay && l4ErrorOkay && l4TypeNormalOkay && l4TypeErrorOkay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return l4NormalOkay && l4ErrorOkay && l4TypeNormalOkay && l4TypeErrorOkay ;
  }


  public static boolean test_L4_Error ( )
  {
    output ( "*** L4 Error ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l4" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L4_ERROR )
    {
      okay = parseErrorExpression ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L4_Normal ( )
  {
    output ( "*** L4 Normal ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l4" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L4_NORMAL )
    {
      okay = parseNormalExpression ( language , text ) && okay ;
    }
    String [ ] l4_list =
    { "[1;if true then 2;3 else 4;5]" , "[1;if true then 2;3]" ,
        "[1;lambda x.2;3]" , "[1;let x = 2;3 in 4;5]" ,
        "[1;let x y = 2;3 in 4;5]" , "[1;rec x.2;3]" ,
        "[1;let rec x y = 2;3 in 4;5]" , "[1;let rec x y = 2;3 in 4;5]" ,
        "[1;lambda (a,b).2;3]" , "[1;let (a,b) = 2;3 in 4;5]" ,
        "[1;while 2;3 do 4;5]" } ;
    for ( String text : l4_list )
    {
      try
      {
        countNormal ++ ;
        List l = ( List ) language.newParser ( new StringReader ( text ) )
            .parse ( ) ;
        if ( l.getExpressions ( ).length == 2 )
        {
          output ( "Successful: \""
              + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
              + l.getClass ( ).getSimpleName ( ) , Output.NORMAL ) ;
        }
        else
        {
          output ( "Not 2 child.\""
              + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
              + l.getClass ( ).getSimpleName ( ) , Output.ERROR ) ;
          okay = false ;
        }
      }
      catch ( Exception e )
      {
        output ( "Exception:  \""
            + fillString ( text + "\"" , max - text.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) , Output.ERROR ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L4_Type_Error ( )
  {
    output ( "*** L4Type Error ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l4" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L4TYPE_ERROR )
    {
      okay = parseErrorType ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }


  public static boolean test_L4_Type_Normal ( )
  {
    output ( "*** L4Type Normal ***" , Output.NORMAL ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l4" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      output ( "NoSuchLanguageException" , Output.ERROR ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String text : L4TYPE_NORMAL )
    {
      okay = parseNormalType ( language , text ) && okay ;
    }
    if ( okay )
    {
      output ( "-> SUCCESSFUL" , Output.NORMAL ) ;
    }
    else
    {
      output ( "-> NOT SUCCESSFUL" , Output.ERROR ) ;
    }
    output ( "" , Output.NORMAL ) ;
    return okay ;
  }
}
