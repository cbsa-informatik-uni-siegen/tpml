package de.unisiegen.tpml.core.parser ;


import java.io.StringReader ;
import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException ;
import de.unisiegen.tpml.core.exceptions.LanguageParserWarningException ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.languages.Language ;
import de.unisiegen.tpml.core.languages.LanguageFactory ;
import de.unisiegen.tpml.core.languages.LanguageParserException ;
import de.unisiegen.tpml.core.languages.NoSuchLanguageException ;
import de.unisiegen.tpml.core.types.Type ;


@ SuppressWarnings ( value =
{ "all" } )
public class ParserTest
{
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


  private static String CURRIED_LET_1 = "let f a b = a + b in f" ;


  private static String CURRIED_LET_2 = "let f (a:int) (b:int) = a + b in f" ;


  private static String CURRIED_LET_3 = "let f a b:int = a + b in f" ;


  private static String CURRIED_LET_4 = "let f (a:int) (b:int):int = a + b in f" ;


  private static String CURRIED_LET_ERROR_1 = "let f a b" ;


  private static String CURRIED_LET_ERROR_2 = "let f a b:" ;


  private static String CURRIED_LET_ERROR_3 = "let f a b:int" ;


  private static String CURRIED_LET_ERROR_4 = "let f a b:int = " ;


  private static String CURRIED_LET_ERROR_5 = "let f a b:int = a + b" ;


  private static String CURRIED_LET_ERROR_6 = "let f a b:int = a + b in" ;


  private static String CURRIED_LET_ERROR_7 = "let f a b =" ;


  private static String CURRIED_LET_ERROR_8 = "let f a b = a + b" ;


  private static String CURRIED_LET_ERROR_9 = "let f a b = a + b in" ;


  private static String CURRIED_LET_ERROR_10 = "let f (" ;


  private static String CURRIED_LET_ERROR_11 = "let f (a" ;


  private static String CURRIED_LET_ERROR_12 = "let f (a:" ;


  private static String CURRIED_LET_ERROR_13 = "let f (a:int" ;


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


  private static String REC_1 = "rec x.x" ;


  private static String REC_2 = "rec x:int.x" ;


  private static String REC_ERROR_1 = "rec" ;


  private static String REC_ERROR_2 = "rec x" ;


  private static String REC_ERROR_3 = "rec x:" ;


  private static String REC_ERROR_4 = "rec x:int" ;


  private static String REC_ERROR_5 = "rec x:int." ;


  private static String REC_ERROR_6 = "rec x." ;


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


  private static String CURRIED_REC_LET_1 = "let rec f a b = a + b in f" ;


  private static String CURRIED_REC_LET_2 = "let rec f (a:int) (b:int) = a + b in f" ;


  private static String CURRIED_REC_LET_3 = "let rec f a b:int = a + b in f" ;


  private static String CURRIED_REC_LET_4 = "let rec f (a:int) (b:int):int = a + b in f" ;


  private static String CURRIED_REC_LET_ERROR_1 = "let rec f a b" ;


  private static String CURRIED_REC_LET_ERROR_2 = "let rec f a b:" ;


  private static String CURRIED_REC_LET_ERROR_3 = "let rec f a b:int" ;


  private static String CURRIED_REC_LET_ERROR_4 = "let rec f a b:int = " ;


  private static String CURRIED_REC_LET_ERROR_5 = "let rec f a b:int = a + b" ;


  private static String CURRIED_REC_LET_ERROR_6 = "let rec f a b:int = a + b in" ;


  private static String CURRIED_REC_LET_ERROR_7 = "let rec f a b =" ;


  private static String CURRIED_REC_LET_ERROR_8 = "let rec f a b = a + b" ;


  private static String CURRIED_REC_LET_ERROR_9 = "let rec f a b = a + b in" ;


  private static String CURRIED_REC_LET_ERROR_10 = "let rec f (" ;


  private static String CURRIED_REC_LET_ERROR_11 = "let rec f (a" ;


  private static String CURRIED_REC_LET_ERROR_12 = "let rec f (a:" ;


  private static String CURRIED_REC_LET_ERROR_13 = "let rec f (a:int" ;


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


  private static String LIST = "[1;2;3]" ;


  private static String LIST_ERROR_1 = "[" ;


  private static String LIST_ERROR_2 = "[1;2;3" ;


  private static String LIST_ERROR_3 = "[1;2;" ;


  private static String SEQUENCE = "1+1;2+2" ;


  private static String SEQUENCE_ERROR = "1+1;" ;


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


  private static String OBJECT_3 = "object (self) end" ;


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


  private static String [ ] L0_NORMAL = new String [ ]
  { IDENTIFIER , APPLICATION , LAMBDA_1 , SIMPLE_EXPR_1 } ;


  private static String [ ] L0_ERROR = new String [ ]
  { LAMBDA_ERROR_1 , LAMBDA_ERROR_2 , LAMBDA_ERROR_3 , SIMPLE_EXPR_ERROR_1 ,
      SIMPLE_EXPR_ERROR_2 } ;


  private static String [ ] L1_NORMAL = new String [ ]
  { IDENTIFIER , APPLICATION , INFIX_OPERATION_1 , INFIX_OPERATION_2 ,
      INFIX_OPERATION_3 , INFIX_OPERATION_4 , INFIX_OPERATION_5 ,
      INFIX_OPERATION_6 , INFIX_OPERATION_7 , INFIX_OPERATION_8 ,
      INFIX_OPERATION_9 , INFIX_OPERATION_10 , AND , OR , CONDITION , LAMBDA_1 ,
      LAMBDA_2 , LET_1 , CURRIED_LET_1 , CURRIED_LET_2 , CURRIED_LET_3 ,
      CURRIED_LET_4 , UNIT , BOOLEAN_1 , BOOLEAN_2 , INTEGER , NOT ,
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
      CONDITION_ERROR_4 , CONDITION_ERROR_5 , LAMBDA_ERROR_1 , LAMBDA_ERROR_2 ,
      LAMBDA_ERROR_3 , LAMBDA_ERROR_4 , LAMBDA_ERROR_5 , LAMBDA_ERROR_6 ,
      LET_ERROR_1 , LET_ERROR_2 , LET_ERROR_3 , LET_ERROR_4 , LET_ERROR_5 ,
      LET_ERROR_6 , LET_ERROR_7 , LET_ERROR_8 , LET_ERROR_9 , LET_ERROR_10 ,
      CURRIED_LET_ERROR_1 , CURRIED_LET_ERROR_2 , CURRIED_LET_ERROR_3 ,
      CURRIED_LET_ERROR_4 , CURRIED_LET_ERROR_5 , CURRIED_LET_ERROR_6 ,
      CURRIED_LET_ERROR_7 , CURRIED_LET_ERROR_8 , CURRIED_LET_ERROR_9 ,
      CURRIED_LET_ERROR_10 , CURRIED_LET_ERROR_11 , CURRIED_LET_ERROR_12 ,
      CURRIED_LET_ERROR_13 , SIMPLE_EXPR_ERROR_1 , SIMPLE_EXPR_ERROR_2 ,
      SIMPLE_EXPR_ERROR_3 , EXPR_SIMPLE_TYPE_ERROR_1 ,
      EXPR_SIMPLE_TYPE_ERROR_2 , EXPR_ARROW_TYPE_ERROR , EXPR_REC_TYPE_ERROR_1 ,
      EXPR_REC_TYPE_ERROR_2 , EXPR_REC_TYPE_ERROR_3 } ;


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
      CURRIED_LET_4 , UNIT , BOOLEAN_1 , BOOLEAN_2 , INTEGER , NOT ,
      OPERATOR_1 , OPERATOR_2 , OPERATOR_3 , OPERATOR_4 , OPERATOR_5 ,
      OPERATOR_6 , OPERATOR_7 , OPERATOR_8 , OPERATOR_9 , OPERATOR_10 ,
      SIMPLE_EXPR_1 , SIMPLE_EXPR_2 , EXPR_SIMPLE_TYPE , EXPR_BOOLEAN_TYPE ,
      EXPR_INTEGER_TYPE , EXPR_UNIT_TYPE , EXPR_ARROW_TYPE ,
      EXPR_TYPE_VARIABLE , EXPR_TYPE_NAME , EXPR_REC_TYPE , REC_1 , REC_2 ,
      LET_REC_1 , LET_REC_2 , CURRIED_REC_LET_1 , CURRIED_REC_LET_2 ,
      CURRIED_REC_LET_3 , CURRIED_REC_LET_4 } ;


  private static String [ ] L2_ERROR = new String [ ]
  { INFIX_OPERATION_ERROR_1 , INFIX_OPERATION_ERROR_2 ,
      INFIX_OPERATION_ERROR_3 , INFIX_OPERATION_ERROR_4 ,
      INFIX_OPERATION_ERROR_5 , INFIX_OPERATION_ERROR_6 ,
      INFIX_OPERATION_ERROR_7 , INFIX_OPERATION_ERROR_8 ,
      INFIX_OPERATION_ERROR_9 , INFIX_OPERATION_ERROR_10 , AND_ERROR ,
      OR_ERROR , CONDITION_ERROR_1 , CONDITION_ERROR_2 , CONDITION_ERROR_3 ,
      CONDITION_ERROR_4 , CONDITION_ERROR_5 , LAMBDA_ERROR_1 , LAMBDA_ERROR_2 ,
      LAMBDA_ERROR_3 , LAMBDA_ERROR_4 , LAMBDA_ERROR_5 , LAMBDA_ERROR_6 ,
      LET_ERROR_1 , LET_ERROR_2 , LET_ERROR_3 , LET_ERROR_4 , LET_ERROR_5 ,
      LET_ERROR_6 , LET_ERROR_7 , LET_ERROR_8 , LET_ERROR_9 , LET_ERROR_10 ,
      CURRIED_LET_ERROR_1 , CURRIED_LET_ERROR_2 , CURRIED_LET_ERROR_3 ,
      CURRIED_LET_ERROR_4 , CURRIED_LET_ERROR_5 , CURRIED_LET_ERROR_6 ,
      CURRIED_LET_ERROR_7 , CURRIED_LET_ERROR_8 , CURRIED_LET_ERROR_9 ,
      CURRIED_LET_ERROR_10 , CURRIED_LET_ERROR_11 , CURRIED_LET_ERROR_12 ,
      CURRIED_LET_ERROR_13 , SIMPLE_EXPR_ERROR_1 , SIMPLE_EXPR_ERROR_2 ,
      SIMPLE_EXPR_ERROR_3 , EXPR_SIMPLE_TYPE_ERROR_1 ,
      EXPR_SIMPLE_TYPE_ERROR_2 , EXPR_ARROW_TYPE_ERROR , EXPR_REC_TYPE_ERROR_1 ,
      EXPR_REC_TYPE_ERROR_2 , EXPR_REC_TYPE_ERROR_3 , REC_ERROR_1 ,
      REC_ERROR_2 , REC_ERROR_3 , REC_ERROR_4 , REC_ERROR_5 , REC_ERROR_6 ,
      LET_REC_ERROR_1 , LET_REC_ERROR_2 , LET_REC_ERROR_3 , LET_REC_ERROR_4 ,
      LET_REC_ERROR_5 , LET_REC_ERROR_6 , LET_REC_ERROR_7 , LET_REC_ERROR_8 ,
      LET_REC_ERROR_9 , LET_REC_ERROR_10 , CURRIED_REC_LET_ERROR_1 ,
      CURRIED_REC_LET_ERROR_2 , CURRIED_REC_LET_ERROR_3 ,
      CURRIED_REC_LET_ERROR_4 , CURRIED_REC_LET_ERROR_5 ,
      CURRIED_REC_LET_ERROR_6 , CURRIED_REC_LET_ERROR_7 ,
      CURRIED_REC_LET_ERROR_8 , CURRIED_REC_LET_ERROR_9 ,
      CURRIED_REC_LET_ERROR_10 , CURRIED_REC_LET_ERROR_11 ,
      CURRIED_REC_LET_ERROR_12 , CURRIED_REC_LET_ERROR_13 } ;


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
      CURRIED_LET_4 , UNIT , BOOLEAN_1 , BOOLEAN_2 , INTEGER , NOT ,
      OPERATOR_1 , OPERATOR_2 , OPERATOR_3 , OPERATOR_4 , OPERATOR_5 ,
      OPERATOR_6 , OPERATOR_7 , OPERATOR_8 , OPERATOR_9 , OPERATOR_10 ,
      SIMPLE_EXPR_1 , SIMPLE_EXPR_2 , EXPR_SIMPLE_TYPE , EXPR_BOOLEAN_TYPE ,
      EXPR_INTEGER_TYPE , EXPR_UNIT_TYPE , EXPR_ARROW_TYPE ,
      EXPR_TYPE_VARIABLE , EXPR_TYPE_NAME , EXPR_REC_TYPE , REC_1 , REC_2 ,
      LET_REC_1 , LET_REC_2 , CURRIED_REC_LET_1 , CURRIED_REC_LET_2 ,
      CURRIED_REC_LET_3 , CURRIED_REC_LET_4 , SELF , OBJECT_1 , OBJECT_2 ,
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
      CONDITION_ERROR_4 , CONDITION_ERROR_5 , LAMBDA_ERROR_1 , LAMBDA_ERROR_2 ,
      LAMBDA_ERROR_3 , LAMBDA_ERROR_4 , LAMBDA_ERROR_5 , LAMBDA_ERROR_6 ,
      LET_ERROR_1 , LET_ERROR_2 , LET_ERROR_3 , LET_ERROR_4 , LET_ERROR_5 ,
      LET_ERROR_6 , LET_ERROR_7 , LET_ERROR_8 , LET_ERROR_9 , LET_ERROR_10 ,
      CURRIED_LET_ERROR_1 , CURRIED_LET_ERROR_2 , CURRIED_LET_ERROR_3 ,
      CURRIED_LET_ERROR_4 , CURRIED_LET_ERROR_5 , CURRIED_LET_ERROR_6 ,
      CURRIED_LET_ERROR_7 , CURRIED_LET_ERROR_8 , CURRIED_LET_ERROR_9 ,
      CURRIED_LET_ERROR_10 , CURRIED_LET_ERROR_11 , CURRIED_LET_ERROR_12 ,
      CURRIED_LET_ERROR_13 , SIMPLE_EXPR_ERROR_1 , SIMPLE_EXPR_ERROR_2 ,
      SIMPLE_EXPR_ERROR_3 , EXPR_SIMPLE_TYPE_ERROR_1 ,
      EXPR_SIMPLE_TYPE_ERROR_2 , EXPR_ARROW_TYPE_ERROR , EXPR_REC_TYPE_ERROR_1 ,
      EXPR_REC_TYPE_ERROR_2 , EXPR_REC_TYPE_ERROR_3 , REC_ERROR_1 ,
      REC_ERROR_2 , REC_ERROR_3 , REC_ERROR_4 , REC_ERROR_5 , REC_ERROR_6 ,
      LET_REC_ERROR_1 , LET_REC_ERROR_2 , LET_REC_ERROR_3 , LET_REC_ERROR_4 ,
      LET_REC_ERROR_5 , LET_REC_ERROR_6 , LET_REC_ERROR_7 , LET_REC_ERROR_8 ,
      LET_REC_ERROR_9 , LET_REC_ERROR_10 , CURRIED_REC_LET_ERROR_1 ,
      CURRIED_REC_LET_ERROR_2 , CURRIED_REC_LET_ERROR_3 ,
      CURRIED_REC_LET_ERROR_4 , CURRIED_REC_LET_ERROR_5 ,
      CURRIED_REC_LET_ERROR_6 , CURRIED_REC_LET_ERROR_7 ,
      CURRIED_REC_LET_ERROR_8 , CURRIED_REC_LET_ERROR_9 ,
      CURRIED_REC_LET_ERROR_10 , CURRIED_REC_LET_ERROR_11 ,
      CURRIED_REC_LET_ERROR_12 , CURRIED_REC_LET_ERROR_13 , OBJECT_ERROR_1 ,
      OBJECT_ERROR_2 , OBJECT_ERROR_3 , OBJECT_ERROR_4 , OBJECT_ERROR_5 ,
      OBJECT_ERROR_6 , OBJECT_ERROR_7 , OBJECT_ERROR_8 , OBJECT_ERROR_9 ,
      DUPLICATION_ERROR_1 , DUPLICATION_ERROR_2 , DUPLICATION_ERROR_3 ,
      DUPLICATION_ERROR_4 , DUPLICATION_ERROR_5 , SEND_ERROR ,
      ATTRIBUTE_ERROR_1 , ATTRIBUTE_ERROR_2 , ATTRIBUTE_ERROR_3 ,
      ATTRIBUTE_ERROR_4 , METHOD_ERROR_1 , METHOD_ERROR_2 , METHOD_ERROR_3 ,
      METHOD_ERROR_4 , METHOD_ERROR_5 , METHOD_ERROR_6 , METHOD_ERROR_7 ,
      METHOD_ERROR_8 , CURRIED_METHOD_ERROR_1 , CURRIED_METHOD_ERROR_2 ,
      CURRIED_METHOD_ERROR_3 , CURRIED_METHOD_ERROR_4 , CURRIED_METHOD_ERROR_5 ,
      CURRIED_METHOD_ERROR_6 , CURRIED_METHOD_ERROR_7 , CURRIED_METHOD_ERROR_8 ,
      CURRIED_METHOD_ERROR_9 , CURRIED_METHOD_ERROR_10 ,
      CURRIED_METHOD_ERROR_11 , EXPR_OBJECT_TYPE_ERROR_1 ,
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


  private static String [ ] L3_NORMAL = new String [ ]
  { IDENTIFIER , APPLICATION , INFIX_OPERATION_1 , INFIX_OPERATION_2 ,
      INFIX_OPERATION_3 , INFIX_OPERATION_4 , INFIX_OPERATION_5 ,
      INFIX_OPERATION_6 , INFIX_OPERATION_7 , INFIX_OPERATION_8 ,
      INFIX_OPERATION_9 , INFIX_OPERATION_10 , AND , OR , CONDITION , LAMBDA_1 ,
      LAMBDA_2 , LET_1 , CURRIED_LET_1 , CURRIED_LET_2 , CURRIED_LET_3 ,
      CURRIED_LET_4 , UNIT , BOOLEAN_1 , BOOLEAN_2 , INTEGER , NOT ,
      OPERATOR_1 , OPERATOR_2 , OPERATOR_3 , OPERATOR_4 , OPERATOR_5 ,
      OPERATOR_6 , OPERATOR_7 , OPERATOR_8 , OPERATOR_9 , OPERATOR_10 ,
      SIMPLE_EXPR_1 , SIMPLE_EXPR_2 , EXPR_SIMPLE_TYPE , EXPR_BOOLEAN_TYPE ,
      EXPR_INTEGER_TYPE , EXPR_UNIT_TYPE , EXPR_ARROW_TYPE ,
      EXPR_TYPE_VARIABLE , EXPR_TYPE_NAME , EXPR_REC_TYPE , REC_1 , REC_2 ,
      LET_REC_1 , LET_REC_2 , CURRIED_REC_LET_1 , CURRIED_REC_LET_2 ,
      CURRIED_REC_LET_3 , CURRIED_REC_LET_4 , INFIX_OPERATION_11 ,
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
      CONDITION_ERROR_4 , CONDITION_ERROR_5 , LAMBDA_ERROR_1 , LAMBDA_ERROR_2 ,
      LAMBDA_ERROR_3 , LAMBDA_ERROR_4 , LAMBDA_ERROR_5 , LAMBDA_ERROR_6 ,
      LET_ERROR_1 , LET_ERROR_2 , LET_ERROR_3 , LET_ERROR_4 , LET_ERROR_5 ,
      LET_ERROR_6 , LET_ERROR_7 , LET_ERROR_8 , LET_ERROR_9 , LET_ERROR_10 ,
      CURRIED_LET_ERROR_1 , CURRIED_LET_ERROR_2 , CURRIED_LET_ERROR_3 ,
      CURRIED_LET_ERROR_4 , CURRIED_LET_ERROR_5 , CURRIED_LET_ERROR_6 ,
      CURRIED_LET_ERROR_7 , CURRIED_LET_ERROR_8 , CURRIED_LET_ERROR_9 ,
      CURRIED_LET_ERROR_10 , CURRIED_LET_ERROR_11 , CURRIED_LET_ERROR_12 ,
      CURRIED_LET_ERROR_13 , SIMPLE_EXPR_ERROR_1 , SIMPLE_EXPR_ERROR_2 ,
      SIMPLE_EXPR_ERROR_3 , EXPR_SIMPLE_TYPE_ERROR_1 ,
      EXPR_SIMPLE_TYPE_ERROR_2 , EXPR_ARROW_TYPE_ERROR , EXPR_REC_TYPE_ERROR_1 ,
      EXPR_REC_TYPE_ERROR_2 , EXPR_REC_TYPE_ERROR_3 , REC_ERROR_1 ,
      REC_ERROR_2 , REC_ERROR_3 , REC_ERROR_4 , REC_ERROR_5 , REC_ERROR_6 ,
      LET_REC_ERROR_1 , LET_REC_ERROR_2 , LET_REC_ERROR_3 , LET_REC_ERROR_4 ,
      LET_REC_ERROR_5 , LET_REC_ERROR_6 , LET_REC_ERROR_7 , LET_REC_ERROR_8 ,
      LET_REC_ERROR_9 , LET_REC_ERROR_10 , CURRIED_REC_LET_ERROR_1 ,
      CURRIED_REC_LET_ERROR_2 , CURRIED_REC_LET_ERROR_3 ,
      CURRIED_REC_LET_ERROR_4 , CURRIED_REC_LET_ERROR_5 ,
      CURRIED_REC_LET_ERROR_6 , CURRIED_REC_LET_ERROR_7 ,
      CURRIED_REC_LET_ERROR_8 , CURRIED_REC_LET_ERROR_9 ,
      CURRIED_REC_LET_ERROR_10 , CURRIED_REC_LET_ERROR_11 ,
      CURRIED_REC_LET_ERROR_12 , CURRIED_REC_LET_ERROR_13 ,
      INFIX_OPERATION_ERROR_11 , MULTI_LAMBDA_ERROR_1 , MULTI_LAMBDA_ERROR_2 ,
      MULTI_LAMBDA_ERROR_3 , MULTI_LAMBDA_ERROR_4 , MULTI_LAMBDA_ERROR_5 ,
      MULTI_LAMBDA_ERROR_6 , MULTI_LAMBDA_ERROR_7 , MULTI_LAMBDA_ERROR_8 ,
      MULTI_LET_ERROR_1 , MULTI_LET_ERROR_2 , MULTI_LET_ERROR_3 ,
      MULTI_LET_ERROR_4 , MULTI_LET_ERROR_5 , MULTI_LET_ERROR_6 ,
      MULTI_LET_ERROR_7 , MULTI_LET_ERROR_8 , MULTI_LET_ERROR_9 ,
      MULTI_LET_ERROR_10 , MULTI_LET_ERROR_11 , MULTI_LET_ERROR_12 ,
      TUPLE_ERROR_1 , TUPLE_ERROR_2 , TUPLE_ERROR_3 , LIST_ERROR_1 ,
      LIST_ERROR_2 , LIST_ERROR_3 , EXPR_TUPLE_TYPE_ERROR_1 ,
      EXPR_TUPLE_TYPE_ERROR_2 } ;


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
      CURRIED_LET_4 , UNIT , BOOLEAN_1 , BOOLEAN_2 , INTEGER , NOT ,
      OPERATOR_1 , OPERATOR_2 , OPERATOR_3 , OPERATOR_4 , OPERATOR_5 ,
      OPERATOR_6 , OPERATOR_7 , OPERATOR_8 , OPERATOR_9 , OPERATOR_10 ,
      SIMPLE_EXPR_1 , SIMPLE_EXPR_2 , EXPR_SIMPLE_TYPE , EXPR_BOOLEAN_TYPE ,
      EXPR_INTEGER_TYPE , EXPR_UNIT_TYPE , EXPR_ARROW_TYPE ,
      EXPR_TYPE_VARIABLE , EXPR_TYPE_NAME , EXPR_REC_TYPE , REC_1 , REC_2 ,
      LET_REC_1 , LET_REC_2 , CURRIED_REC_LET_1 , CURRIED_REC_LET_2 ,
      CURRIED_REC_LET_3 , CURRIED_REC_LET_4 , INFIX_OPERATION_11 ,
      MULTI_LAMBDA_1 , MULTI_LAMBDA_2 , MULTI_LET_1 , MULTI_LET_2 , FST , SND ,
      CONS , IS_EMPTY , HD , TL , OPERATOR_11 , EMPTY_LIST , PROJECTION ,
      TUPLE , LIST , EXPR_TUPLE_TYPE_1 , EXPR_TUPLE_TYPE_2 , EXPR_LIST_TYPE ,
      INFIX_OPERATION_12 , CONDITION1 , WHILE , REF , DEREF , OPERATOR_12 ,
      EXPR_REF_TYPE , SEQUENCE } ;


  private static String [ ] L4_ERROR = new String [ ]
  { INFIX_OPERATION_ERROR_1 , INFIX_OPERATION_ERROR_2 ,
      INFIX_OPERATION_ERROR_3 , INFIX_OPERATION_ERROR_4 ,
      INFIX_OPERATION_ERROR_5 , INFIX_OPERATION_ERROR_6 ,
      INFIX_OPERATION_ERROR_7 , INFIX_OPERATION_ERROR_8 ,
      INFIX_OPERATION_ERROR_9 , INFIX_OPERATION_ERROR_10 , AND_ERROR ,
      OR_ERROR , CONDITION_ERROR_1 , CONDITION_ERROR_2 , CONDITION_ERROR_3 ,
      CONDITION_ERROR_5 , LAMBDA_ERROR_1 , LAMBDA_ERROR_2 , LAMBDA_ERROR_3 ,
      LAMBDA_ERROR_4 , LAMBDA_ERROR_5 , LAMBDA_ERROR_6 , LET_ERROR_1 ,
      LET_ERROR_2 , LET_ERROR_3 , LET_ERROR_4 , LET_ERROR_5 , LET_ERROR_6 ,
      LET_ERROR_7 , LET_ERROR_8 , LET_ERROR_9 , LET_ERROR_10 ,
      CURRIED_LET_ERROR_1 , CURRIED_LET_ERROR_2 , CURRIED_LET_ERROR_3 ,
      CURRIED_LET_ERROR_4 , CURRIED_LET_ERROR_5 , CURRIED_LET_ERROR_6 ,
      CURRIED_LET_ERROR_7 , CURRIED_LET_ERROR_8 , CURRIED_LET_ERROR_9 ,
      CURRIED_LET_ERROR_10 , CURRIED_LET_ERROR_11 , CURRIED_LET_ERROR_12 ,
      CURRIED_LET_ERROR_13 , SIMPLE_EXPR_ERROR_1 , SIMPLE_EXPR_ERROR_2 ,
      SIMPLE_EXPR_ERROR_3 , EXPR_SIMPLE_TYPE_ERROR_1 ,
      EXPR_SIMPLE_TYPE_ERROR_2 , EXPR_ARROW_TYPE_ERROR , EXPR_REC_TYPE_ERROR_1 ,
      EXPR_REC_TYPE_ERROR_2 , EXPR_REC_TYPE_ERROR_3 , REC_ERROR_1 ,
      REC_ERROR_2 , REC_ERROR_3 , REC_ERROR_4 , REC_ERROR_5 , REC_ERROR_6 ,
      LET_REC_ERROR_1 , LET_REC_ERROR_2 , LET_REC_ERROR_3 , LET_REC_ERROR_4 ,
      LET_REC_ERROR_5 , LET_REC_ERROR_6 , LET_REC_ERROR_7 , LET_REC_ERROR_8 ,
      LET_REC_ERROR_9 , LET_REC_ERROR_10 , CURRIED_REC_LET_ERROR_1 ,
      CURRIED_REC_LET_ERROR_2 , CURRIED_REC_LET_ERROR_3 ,
      CURRIED_REC_LET_ERROR_4 , CURRIED_REC_LET_ERROR_5 ,
      CURRIED_REC_LET_ERROR_6 , CURRIED_REC_LET_ERROR_7 ,
      CURRIED_REC_LET_ERROR_8 , CURRIED_REC_LET_ERROR_9 ,
      CURRIED_REC_LET_ERROR_10 , CURRIED_REC_LET_ERROR_11 ,
      CURRIED_REC_LET_ERROR_12 , CURRIED_REC_LET_ERROR_13 ,
      INFIX_OPERATION_ERROR_11 , MULTI_LAMBDA_ERROR_1 , MULTI_LAMBDA_ERROR_2 ,
      MULTI_LAMBDA_ERROR_3 , MULTI_LAMBDA_ERROR_4 , MULTI_LAMBDA_ERROR_5 ,
      MULTI_LAMBDA_ERROR_6 , MULTI_LAMBDA_ERROR_7 , MULTI_LAMBDA_ERROR_8 ,
      MULTI_LET_ERROR_1 , MULTI_LET_ERROR_2 , MULTI_LET_ERROR_3 ,
      MULTI_LET_ERROR_4 , MULTI_LET_ERROR_5 , MULTI_LET_ERROR_6 ,
      MULTI_LET_ERROR_7 , MULTI_LET_ERROR_8 , MULTI_LET_ERROR_9 ,
      MULTI_LET_ERROR_10 , MULTI_LET_ERROR_11 , MULTI_LET_ERROR_12 ,
      TUPLE_ERROR_1 , TUPLE_ERROR_2 , TUPLE_ERROR_3 , LIST_ERROR_1 ,
      LIST_ERROR_2 , LIST_ERROR_3 , EXPR_TUPLE_TYPE_ERROR_1 ,
      EXPR_TUPLE_TYPE_ERROR_2 , INFIX_OPERATION_ERROR_12 , WHILE_ERROR_1 ,
      WHILE_ERROR_2 , WHILE_ERROR_3 , SEQUENCE_ERROR } ;


  private static String [ ] L4TYPE_NORMAL = new String [ ]
  { SIMPLE_TYPE , BOOLEAN_TYPE , INTEGER_TYPE , UNIT_TYPE , TYPE_VARIABLE ,
      TYPE_NAME , REC_TYPE , ARROW_TYPE , TUPLE_TYPE_1 , TUPLE_TYPE_2 ,
      LIST_TYPE , REF_TYPE } ;


  private static String [ ] L4TYPE_ERROR = new String [ ]
  { SIMPLE_TYPE_ERROR_1 , SIMPLE_TYPE_ERROR_2 , ARROW_TYPE_ERROR ,
      REC_TYPE_ERROR_1 , REC_TYPE_ERROR_2 , REC_TYPE_ERROR_3 ,
      TUPLE_TYPE_ERROR_1 , TUPLE_TYPE_ERROR_2 } ;


  private static int max = 0 ;


  public static String delete ( String s )
  {
    return s.replaceAll ( "<html>" , "" ).replaceAll ( "</html>" , "" )
        .replaceAll ( "<sub>" , "" ).replaceAll ( "</sub>" , "" ).replaceAll (
            "<br>" , "  -  " ) ;
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


  public static void main ( String [ ] pArguments )
  {
    max ( ) ;
    int countNormal = L0_NORMAL.length + L1_NORMAL.length + L2_NORMAL.length
        + L2O_NORMAL.length + L3_NORMAL.length + L4_NORMAL.length ;
    int countNormalType = L1TYPE_NORMAL.length + L2TYPE_NORMAL.length
        + L2OTYPE_NORMAL.length + L3TYPE_NORMAL.length + L4TYPE_NORMAL.length ;
    int countError = L0_ERROR.length + L1_ERROR.length + L2_ERROR.length
        + L2O_ERROR.length + L3_ERROR.length + L4_ERROR.length ;
    int countErrorType = L1TYPE_ERROR.length + L2TYPE_ERROR.length
        + L2OTYPE_ERROR.length + L3TYPE_ERROR.length + L4TYPE_ERROR.length ;
    boolean l0Okay = test_L0 ( ) ;
    boolean l1Okay = test_L1 ( ) ;
    boolean l2Okay = test_L2 ( ) ;
    boolean l2OOkay = test_L2O ( ) ;
    boolean l3Okay = test_L3 ( ) ;
    boolean l4Okay = test_L4 ( ) ;
    System.out.println ( "*** Overview ***" ) ;
    System.out.flush ( ) ;
    System.out.println ( "Count Normal:      " + countNormal ) ;
    System.out.flush ( ) ;
    System.out.println ( "Count Normal Type: " + countNormalType ) ;
    System.out.flush ( ) ;
    System.out.println ( "Count Error:       " + countError ) ;
    System.out.flush ( ) ;
    System.out.println ( "Count Error Type:  " + countErrorType ) ;
    System.out.flush ( ) ;
    System.out.println ( "Count           :  "
        + ( countNormal + countNormalType + countError + countErrorType ) ) ;
    System.out.flush ( ) ;
    if ( l0Okay && l1Okay && l2Okay && l2OOkay && l3Okay && l4Okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
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


  public static boolean test_L0 ( )
  {
    boolean l0NormalOkay = test_L0_Normal ( ) ;
    boolean l0ErrorOkay = test_L0_Error ( ) ;
    System.out.println ( "*** L0 ***" ) ;
    System.out.flush ( ) ;
    if ( l0NormalOkay )
    {
      System.out.println ( "L0 Normal:        " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L0 Normal:        " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l0ErrorOkay )
    {
      System.out.println ( "L0 Error:         " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L0 Error:         " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l0NormalOkay && l0ErrorOkay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return l0NormalOkay && l0ErrorOkay ;
  }


  public static boolean test_L0_Error ( )
  {
    System.out.println ( "*** L0 Error ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l0" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L0_ERROR )
    {
      try
      {
        Expression e = language.newParser ( new StringReader ( s ) ).parse ( ) ;
        System.err.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + e.getClass ( ).getSimpleName ( ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.out.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }


  public static boolean test_L0_Normal ( )
  {
    System.out.println ( "*** L0 Normal ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l0" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L0_NORMAL )
    {
      try
      {
        Expression e = language.newParser ( new StringReader ( s ) ).parse ( ) ;
        System.out.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + e.getClass ( ).getSimpleName ( ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.err.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }


  public static boolean test_L1 ( )
  {
    boolean l1NormalOkay = test_L1_Normal ( ) ;
    boolean l1ErrorOkay = test_L1_Error ( ) ;
    boolean l1TypeNormalOkay = test_L1_Type_Normal ( ) ;
    boolean l1TypeErrorOkay = test_L1_Type_Error ( ) ;
    System.out.println ( "*** L1 ***" ) ;
    System.out.flush ( ) ;
    if ( l1NormalOkay )
    {
      System.out.println ( "L1 Normal:        " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L1 Normal:        " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l1ErrorOkay )
    {
      System.out.println ( "L1 Error:         " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L1 Error:         " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l1TypeNormalOkay )
    {
      System.out.println ( "L1Type Normal:    " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L1Type Normal:    " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l1TypeErrorOkay )
    {
      System.out.println ( "L1Type Error:     " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L1Type Error:     " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l1NormalOkay && l1ErrorOkay && l1TypeNormalOkay && l1TypeErrorOkay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return l1NormalOkay && l1ErrorOkay && l1TypeNormalOkay && l1TypeErrorOkay ;
  }


  public static boolean test_L1_Error ( )
  {
    System.out.println ( "*** L1 Error ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l1" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L1_ERROR )
    {
      try
      {
        Expression e = language.newParser ( new StringReader ( s ) ).parse ( ) ;
        System.err.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + e.getClass ( ).getSimpleName ( ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.out.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }


  public static boolean test_L1_Normal ( )
  {
    System.out.println ( "*** L1 Normal ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l1" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L1_NORMAL )
    {
      try
      {
        Expression e = language.newParser ( new StringReader ( s ) ).parse ( ) ;
        System.out.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + e.getClass ( ).getSimpleName ( ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.err.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }


  public static boolean test_L1_Type_Error ( )
  {
    System.out.println ( "*** L1Type Error ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l1" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L1TYPE_ERROR )
    {
      try
      {
        Type t = language.newTypeParser ( new StringReader ( s ) ).parse ( ) ;
        System.err.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + t.getClass ( ).getSimpleName ( ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.out.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }


  public static boolean test_L1_Type_Normal ( )
  {
    System.out.println ( "*** L1Type Normal ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l1" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L1TYPE_NORMAL )
    {
      try
      {
        Type t = language.newTypeParser ( new StringReader ( s ) ).parse ( ) ;
        System.out.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + t.getClass ( ).getSimpleName ( ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.err.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }


  public static boolean test_L2 ( )
  {
    boolean l2NormalOkay = test_L2_Normal ( ) ;
    boolean l2ErrorOkay = test_L2_Error ( ) ;
    boolean l2TypeNormalOkay = test_L2_Type_Normal ( ) ;
    boolean l2TypeErrorOkay = test_L2_Type_Error ( ) ;
    System.out.println ( "*** L2 ***" ) ;
    System.out.flush ( ) ;
    if ( l2NormalOkay )
    {
      System.out.println ( "L2 Normal:        " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L2 Normal:        " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l2ErrorOkay )
    {
      System.out.println ( "L2 Error:         " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L2 Error:         " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l2TypeNormalOkay )
    {
      System.out.println ( "L2Type Normal:    " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L2Type Normal:    " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l2TypeErrorOkay )
    {
      System.out.println ( "L2Type Error:     " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L2Type Error:     " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l2NormalOkay && l2ErrorOkay && l2TypeNormalOkay && l2TypeErrorOkay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return l2NormalOkay && l2ErrorOkay && l2TypeNormalOkay && l2TypeErrorOkay ;
  }


  public static boolean test_L2_Error ( )
  {
    System.out.println ( "*** L2 Error ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l2" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L2_ERROR )
    {
      try
      {
        Expression e = language.newParser ( new StringReader ( s ) ).parse ( ) ;
        System.err.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + e.getClass ( ).getSimpleName ( ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.out.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }


  public static boolean test_L2_Normal ( )
  {
    System.out.println ( "*** L2 Normal ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l2" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L2_NORMAL )
    {
      try
      {
        Expression e = language.newParser ( new StringReader ( s ) ).parse ( ) ;
        System.out.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + e.getClass ( ).getSimpleName ( ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.err.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }


  public static boolean test_L2_Type_Error ( )
  {
    System.out.println ( "*** L2Type Error ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l2" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L2TYPE_ERROR )
    {
      try
      {
        Type t = language.newTypeParser ( new StringReader ( s ) ).parse ( ) ;
        System.err.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + t.getClass ( ).getSimpleName ( ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.out.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }


  public static boolean test_L2_Type_Normal ( )
  {
    System.out.println ( "*** L2Type Normal ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l2" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L2TYPE_NORMAL )
    {
      try
      {
        Type t = language.newTypeParser ( new StringReader ( s ) ).parse ( ) ;
        System.out.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + t.getClass ( ).getSimpleName ( ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.err.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }


  public static boolean test_L2O ( )
  {
    boolean l2ONormalOkay = test_L2O_Normal ( ) ;
    boolean l2OErrorOkay = test_L2O_Error ( ) ;
    boolean l2OTypeNormalOkay = test_L2O_Type_Normal ( ) ;
    boolean l2OTypeErrorOkay = test_L2O_Type_Error ( ) ;
    System.out.println ( "*** L2O ***" ) ;
    System.out.flush ( ) ;
    if ( l2ONormalOkay )
    {
      System.out.println ( "L2O Normal:       " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L2O Normal:       " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l2OErrorOkay )
    {
      System.out.println ( "L2O Error:        " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L2O Error:        " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l2OTypeNormalOkay )
    {
      System.out.println ( "L2OType Normal:   " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L2OType Normal:   " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l2OTypeErrorOkay )
    {
      System.out.println ( "L2OType Error:    " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L2OType Error:    " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l2ONormalOkay && l2OErrorOkay && l2OTypeNormalOkay && l2OTypeErrorOkay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return l2ONormalOkay && l2OErrorOkay && l2OTypeNormalOkay
        && l2OTypeErrorOkay ;
  }


  public static boolean test_L2O_Error ( )
  {
    System.out.println ( "*** L2O Error ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l2O" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L2O_ERROR )
    {
      try
      {
        Expression e = language.newParser ( new StringReader ( s ) ).parse ( ) ;
        System.err.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + e.getClass ( ).getSimpleName ( ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.out.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }


  public static boolean test_L2O_Normal ( )
  {
    System.out.println ( "*** L2O Normal ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l2O" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L2O_NORMAL )
    {
      try
      {
        Expression e = language.newParser ( new StringReader ( s ) ).parse ( ) ;
        System.out.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + e.getClass ( ).getSimpleName ( ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.err.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }


  public static boolean test_L2O_Type_Error ( )
  {
    System.out.println ( "*** L2OType Error ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l2O" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L2OTYPE_ERROR )
    {
      try
      {
        Type t = language.newTypeParser ( new StringReader ( s ) ).parse ( ) ;
        System.err.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + t.getClass ( ).getSimpleName ( ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.out.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }


  public static boolean test_L2O_Type_Normal ( )
  {
    System.out.println ( "*** L2OType Normal ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l2O" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L2OTYPE_NORMAL )
    {
      try
      {
        Type t = language.newTypeParser ( new StringReader ( s ) ).parse ( ) ;
        System.out.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + t.getClass ( ).getSimpleName ( ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.err.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }


  public static boolean test_L3 ( )
  {
    boolean l3NormalOkay = test_L3_Normal ( ) ;
    boolean l3ErrorOkay = test_L3_Error ( ) ;
    boolean l3TypeNormalOkay = test_L3_Type_Normal ( ) ;
    boolean l3TypeErrorOkay = test_L3_Type_Error ( ) ;
    System.out.println ( "*** L3 ***" ) ;
    System.out.flush ( ) ;
    if ( l3NormalOkay )
    {
      System.out.println ( "L3 Normal:        " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L3 Normal:        " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l3ErrorOkay )
    {
      System.out.println ( "L3 Error:         " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L3 Error:         " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l3TypeNormalOkay )
    {
      System.out.println ( "L3Type Normal:    " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L3Type Normal:    " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l3TypeErrorOkay )
    {
      System.out.println ( "L3Type Error:     " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L3Type Error:     " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l3NormalOkay && l3ErrorOkay && l3TypeNormalOkay && l3TypeErrorOkay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return l3NormalOkay && l3ErrorOkay && l3TypeNormalOkay && l3TypeErrorOkay ;
  }


  public static boolean test_L3_Error ( )
  {
    System.out.println ( "*** L3 Error ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l3" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L3_ERROR )
    {
      try
      {
        Expression e = language.newParser ( new StringReader ( s ) ).parse ( ) ;
        System.err.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + e.getClass ( ).getSimpleName ( ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.out.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }


  public static boolean test_L3_Normal ( )
  {
    System.out.println ( "*** L3 Normal ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l3" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L3_NORMAL )
    {
      try
      {
        Expression e = language.newParser ( new StringReader ( s ) ).parse ( ) ;
        System.out.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + e.getClass ( ).getSimpleName ( ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.err.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }


  public static boolean test_L3_Type_Error ( )
  {
    System.out.println ( "*** L3Type Error ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l3" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L3TYPE_ERROR )
    {
      try
      {
        Type t = language.newTypeParser ( new StringReader ( s ) ).parse ( ) ;
        System.err.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + t.getClass ( ).getSimpleName ( ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.out.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }


  public static boolean test_L3_Type_Normal ( )
  {
    System.out.println ( "*** L3Type Normal ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l3" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L3TYPE_NORMAL )
    {
      try
      {
        Type t = language.newTypeParser ( new StringReader ( s ) ).parse ( ) ;
        System.out.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + t.getClass ( ).getSimpleName ( ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.err.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }


  public static boolean test_L4 ( )
  {
    boolean l4NormalOkay = test_L4_Normal ( ) ;
    boolean l4ErrorOkay = test_L4_Error ( ) ;
    boolean l4TypeNormalOkay = test_L4_Type_Normal ( ) ;
    boolean l4TypeErrorOkay = test_L4_Type_Error ( ) ;
    System.out.println ( "*** L4 ***" ) ;
    System.out.flush ( ) ;
    if ( l4NormalOkay )
    {
      System.out.println ( "L4 Normal:        " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L4 Normal:        " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l4ErrorOkay )
    {
      System.out.println ( "L4 Error:         " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L4 Error:         " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l4TypeNormalOkay )
    {
      System.out.println ( "L4Type Normal:    " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L4Type Normal:    " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l4TypeErrorOkay )
    {
      System.out.println ( "L4Type Error:     " + fillString ( "" , max )
          + "SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L4Type Error:     " + fillString ( "" , max )
          + "NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l4NormalOkay && l4ErrorOkay && l4TypeNormalOkay && l4TypeErrorOkay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return l4NormalOkay && l4ErrorOkay && l4TypeNormalOkay && l4TypeErrorOkay ;
  }


  public static boolean test_L4_Error ( )
  {
    System.out.println ( "*** L4 Error ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l4" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L4_ERROR )
    {
      try
      {
        Expression e = language.newParser ( new StringReader ( s ) ).parse ( ) ;
        System.err.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + e.getClass ( ).getSimpleName ( ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.out.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }


  public static boolean test_L4_Normal ( )
  {
    System.out.println ( "*** L4 Normal ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l4" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L4_NORMAL )
    {
      try
      {
        Expression e = language.newParser ( new StringReader ( s ) ).parse ( ) ;
        System.out.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + e.getClass ( ).getSimpleName ( ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.err.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }


  public static boolean test_L4_Type_Error ( )
  {
    System.out.println ( "*** L4Type Error ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l4" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L4TYPE_ERROR )
    {
      try
      {
        Type t = language.newTypeParser ( new StringReader ( s ) ).parse ( ) ;
        System.err.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + t.getClass ( ).getSimpleName ( ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.out.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }


  public static boolean test_L4_Type_Normal ( )
  {
    System.out.println ( "*** L4Type Normal ***" ) ;
    System.out.flush ( ) ;
    LanguageFactory factory = LanguageFactory.newInstance ( ) ;
    Language language ;
    try
    {
      language = factory.getLanguageById ( "l4" ) ;
    }
    catch ( NoSuchLanguageException e )
    {
      System.err.println ( "NoSuchLanguageException" ) ;
      System.err.flush ( ) ;
      return false ;
    }
    boolean okay = true ;
    for ( String s : L4TYPE_NORMAL )
    {
      try
      {
        Type t = language.newTypeParser ( new StringReader ( s ) ).parse ( ) ;
        System.out.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + t.getClass ( ).getSimpleName ( ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.err.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + delete ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
    }
    if ( okay )
    {
      System.out.println ( "-> SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "-> NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    System.out.println ( ) ;
    System.out.flush ( ) ;
    return okay ;
  }
}
