package de.unisiegen.tpml.core.parser ;


import java.io.StringReader ;
import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException ;
import de.unisiegen.tpml.core.exceptions.LanguageParserWarningException ;
import de.unisiegen.tpml.core.languages.Language ;
import de.unisiegen.tpml.core.languages.LanguageFactory ;
import de.unisiegen.tpml.core.languages.LanguageParserException ;
import de.unisiegen.tpml.core.languages.NoSuchLanguageException ;


@ SuppressWarnings ( value =
{ "nls" } )
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


  // TODO Unable to parse because of comment
  private static String OPERATOR_3 = "6 * 7" ; // "(*)" ;


  private static String OPERATOR_4 = "(/)" ;


  private static String OPERATOR_5 = "(mod)" ;


  private static String OPERATOR_6 = "(=)" ;


  private static String OPERATOR_7 = "(<)" ;


  private static String OPERATOR_8 = "(>)" ;


  private static String OPERATOR_9 = "(<=)" ;


  private static String OPERATOR_10 = "(>=)" ;


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


  public static String deleteHTML ( String s )
  {
    return s.replaceAll ( "<html>" , "" ).replaceAll ( "</html>" , "" )
        .replaceAll ( "<sub>" , "" ).replaceAll ( "</sub>" , "" ) ;
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
    boolean l0Okay = testL0 ( ) ;
    boolean l1Okay = testL1 ( ) ;
    System.out.println ( "*** Overview ***" ) ;
    System.out.flush ( ) ;
    if ( l0Okay && l1Okay )
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


  public static boolean testL0 ( )
  {
    boolean l0NormalOkay = testL0Normal ( ) ;
    boolean l0ErrorOkay = testL0Error ( ) ;
    System.out.println ( "*** L0 ***" ) ;
    System.out.flush ( ) ;
    if ( l0NormalOkay )
    {
      System.out.println ( "L0 Normal:      SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L0 Normal:      NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l0ErrorOkay )
    {
      System.out.println ( "L0 Error:       SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L0 Error:       NOT SUCCESSFUL" ) ;
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


  public static boolean testL0Error ( )
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
    int max = 0 ;
    for ( String s : L0_ERROR )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L0_ERROR )
    {
      try
      {
        language.newParser ( new StringReader ( s ) ).parse ( ) ;
        System.err.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.out.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
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


  public static boolean testL0Normal ( )
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
    int max = 0 ;
    for ( String s : L0_NORMAL )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L0_NORMAL )
    {
      try
      {
        language.newParser ( new StringReader ( s ) ).parse ( ) ;
        System.out.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.err.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
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


  public static boolean testL1 ( )
  {
    boolean l1NormalOkay = testL1Normal ( ) ;
    boolean l1ErrorOkay = testL1Error ( ) ;
    boolean l1TypeNormalOkay = testL1TypeNormal ( ) ;
    boolean l1TypeErrorOkay = testL1TypeError ( ) ;
    System.out.println ( "*** L1 ***" ) ;
    System.out.flush ( ) ;
    if ( l1NormalOkay )
    {
      System.out.println ( "L1 Normal:      SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L1 Normal:      NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l1ErrorOkay )
    {
      System.out.println ( "L1 Error:       SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L1 Error:       NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l1TypeNormalOkay )
    {
      System.out.println ( "L1Type Normal:  SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L1Type Normal:  NOT SUCCESSFUL" ) ;
      System.err.flush ( ) ;
    }
    if ( l1TypeErrorOkay )
    {
      System.out.println ( "L1Type Error:   SUCCESSFUL" ) ;
      System.out.flush ( ) ;
    }
    else
    {
      System.err.println ( "L1Type Error:   NOT SUCCESSFUL" ) ;
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


  public static boolean testL1Error ( )
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
    int max = 0 ;
    for ( String s : L1_ERROR )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L1_ERROR )
    {
      try
      {
        language.newParser ( new StringReader ( s ) ).parse ( ) ;
        System.err.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.out.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
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


  public static boolean testL1Normal ( )
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
    int max = 0 ;
    for ( String s : L1_NORMAL )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L1_NORMAL )
    {
      try
      {
        language.newParser ( new StringReader ( s ) ).parse ( ) ;
        System.out.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.err.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
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


  public static boolean testL1TypeError ( )
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
    int max = 0 ;
    for ( String s : L1TYPE_ERROR )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L1TYPE_ERROR )
    {
      try
      {
        language.newTypeParser ( new StringReader ( s ) ).parse ( ) ;
        System.err.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.out.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
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


  public static boolean testL1TypeNormal ( )
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
    int max = 0 ;
    for ( String s : L1TYPE_NORMAL )
    {
      max = s.length ( ) > max ? s.length ( ) : max ;
    }
    for ( String s : L1TYPE_NORMAL )
    {
      try
      {
        language.newTypeParser ( new StringReader ( s ) ).parse ( ) ;
        System.out.println ( "Successful: \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) ) ;
        System.out.flush ( ) ;
      }
      catch ( LanguageParserMultiException e )
      {
        System.err.println ( "Multi:      \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserWarningException e )
      {
        System.err.println ( "Warning:    \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( LanguageParserException e )
      {
        System.err.println ( "Parser:     \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
        System.err.flush ( ) ;
        okay = false ;
      }
      catch ( Exception e )
      {
        System.err.println ( "Exception:  \""
            + fillString ( s + "\"" , max - s.length ( ) + 1 ) + "   "
            + deleteHTML ( e.getMessage ( ) ) ) ;
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
