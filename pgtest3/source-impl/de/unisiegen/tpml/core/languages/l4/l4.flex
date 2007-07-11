package de.unisiegen.tpml.core.languages.l4;

import java.io.Reader ;
import java.text.MessageFormat ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.languages.AbstractLanguageScanner ;
import de.unisiegen.tpml.core.languages.LanguageScannerException ;
import de.unisiegen.tpml.core.languages.LanguageSymbol ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle ;

/**
 * This is the lexer class for L4.
 */
%%

%class L4Scanner
%extends AbstractLanguageScanner
%implements L4Terminals

%function nextSymbol
%type LanguageSymbol
%yylexthrow LanguageScannerException
%eofclose
%eofval{
	return null;
%eofval}

%unicode
%line
%column
%char

%{
	private int yycommentChar = 0;
	
	private int yyprojChar = 0;
	
	private Integer yyprojArity;
	private Integer yyprojArityStartOffset;
	private Integer yyprojArityEndOffset;

	private LanguageSymbol symbol(String name, int id)
	{
	  return symbol(name, id, yychar, yychar + yylength(), yytext());
	}
	
	private LanguageSymbol symbol(String name, int id, Object value)
	{
	  return symbol(name, id, yychar, yychar + yylength(), value);
	}

	@Override
	public PrettyStyle getStyleBySymbolId(int pId)
	{
	  switch (pId)
	  {
		case COMMENT:
		  return PrettyStyle.COMMENT;
		case TRUE:
		case FALSE:
		case NUMBER:
		case PARENPAREN:
		case MOD:
		case NOT:
		case FST:
		case SND:
		case PROJECTION:
		case CONS:
		case IS_EMPTY:
		case HD:
		case TL:
		case BRACKETBRACKET:
		case COLONEQUAL:
		case REF:
		  return PrettyStyle.CONSTANT;
		case LAMBDA:
		case LET:
		case IN:
		case IF:
		case THEN:
		case ELSE:
		case AMPERAMPER:
		case BARBAR:
		case MU:
		case REC:
		case WHILE:
		case DO:
		  return PrettyStyle.KEYWORD;
		case BOOL:
		case INT:
		case UNIT:
		case TYPEVARIABLE:
		case LIST:
		  return PrettyStyle.TYPE;
		case IDENTIFIER:
		  return PrettyStyle.IDENTIFIER;
		default:
		  return PrettyStyle.NONE;
	  }
	}
	
	public void restart(Reader pReader)
	{
	  if (pReader == null)
	  {
		throw new NullPointerException("Reader is null");
	  }
	  yyreset(pReader);
	}
%}

LineTerminator	= \r|\n|\r\n
WhiteSpace		= {LineTerminator} | [ \t\f]
Number			= [:digit:]+
Identifier		= [a-zA-Z] [a-zA-Z0-9_]* '*
LetterAX		= [a-x]
LetterGreek		= [\u03b1-\u03c1\u03c3-\u03c9]

%state YYCOMMENTINIT, YYCOMMENT, YYCOMMENTMULT, YYCOMMENTEOF
%state YYPROJARITY, YYPROJUNDERLINE, YYPROJINDEX

%%

<YYINITIAL>
{
	"+"					{ return symbol("PLUS", PLUS); }
	"-"					{ return symbol("MINUS", MINUS); }
	"*"					{ return symbol("STAR", STAR); }
	"/"					{ return symbol("SLASH", SLASH); }
	"mod"				{ return symbol("MOD", MOD); }
	"="					{ return symbol("EQUAL", EQUAL); }
	"<"					{ return symbol("LESS", LESS); }
	">"					{ return symbol("GREATER", GREATER); }
	"<="				{ return symbol("LESSEQUAL", LESSEQUAL); }
	">="				{ return symbol("GREATEREQUAL", GREATEREQUAL); }
	"&&"				{ return symbol("AMPERAMPER", AMPERAMPER); }
	"||"				{ return symbol("BARBAR", BARBAR); }
	"not"				{ return symbol("NOT", NOT); }
	"."					{ return symbol("DOT", DOT); }
	":"					{ return symbol("COLON", COLON); }
	"("					{ return symbol("LPAREN", LPAREN); }
	")"					{ return symbol("RPAREN", RPAREN); }
	"<:"				{ return symbol("SUBTYPE", SUBTYPE); }
	"->"|"\u2192"		{ return symbol("ARROW", ARROW); }
	"lambda"|"\u03bb"	{ return symbol("LAMBDA", LAMBDA); }
	"let"				{ return symbol("LET", LET); }
	"in"				{ return symbol("IN", IN); }
	"if"				{ return symbol("IF", IF); }
	"then"				{ return symbol("THEN", THEN); }
	"else"				{ return symbol("ELSE", ELSE); }
	"()"				{ return symbol("PARENPAREN", PARENPAREN); }
	"true"				{ return symbol("TRUE", TRUE); }
	"false"				{ return symbol("FALSE", FALSE); }
	"rec"				{ return symbol("REC", REC); }
	"fst"				{ return symbol("FST", FST); }
	"snd"				{ return symbol("SND", SND); }
	"cons"				{ return symbol("CONS", CONS); }
	"is_empty"			{ return symbol("IS_EMPTY", IS_EMPTY); }
	"hd"				{ return symbol("HD", HD); }
	"tl"				{ return symbol("TL", TL); }
	"::"				{ return symbol("COLONCOLON", COLONCOLON); }
	"[]"				{ return symbol("BRACKETBRACKET", BRACKETBRACKET); }
	","					{ return symbol("COMMA", COMMA); }
	";"					{ return symbol("SEMI", SEMI); }
	"["					{ return symbol("LBRACKET", LBRACKET); }
	"]"					{ return symbol("RBRACKET", RBRACKET); }
	"!"					{ return symbol("EXCLAMATION", EXCLAMATION); }
	":="				{ return symbol("COLONEQUAL", COLONEQUAL); }
	"ref"				{ return symbol("REF", REF); }
	"while"				{ return symbol("WHILE", WHILE); }
	"do"				{ return symbol("DO", DO); }
	"bool"				{ return symbol("BOOL", BOOL); }
	"int"				{ return symbol("INT", INT); }
	"unit"				{ return symbol("UNIT", UNIT); }
	"mu"|"\u03bc"		{ return symbol("MU", MU); }
	"list"				{ return symbol("LIST", LIST); }
	"'"{LetterAX}		{ return symbol("TYPEVARIABLE", TYPEVARIABLE, (int)(yycharat(1) - 'a')); }
	{LetterGreek}		{
						  int c = yycharat(0);
						  if (c > '\u03c1')
						  {
							/* special case for letters after rho 
							   (see unicode table) */
						    c -= 1;
						  }
						  return symbol("TYPEVARIABLE", TYPEVARIABLE, (int)(c - '\u03b1'));
						}
	{Number}			{
						  try
						  {
						    return symbol("NUMBER", NUMBER, Integer.valueOf(yytext()));
						  }
						  catch (NumberFormatException e) 
						  {
						    throw new LanguageScannerException(yychar, yychar + yylength(), 
							  MessageFormat.format ( Messages.getString ( "Parser.6" ) , yytext() ) , e);
						  }
						}
	{Identifier}		{ return symbol("IDENTIFIER", IDENTIFIER, yytext()); }
	"(*"				{ yycommentChar = yychar; yybegin(YYCOMMENTINIT); }
	"#"					{ yyprojChar = yychar; yybegin(YYPROJARITY); }
	{WhiteSpace}		{ /* Ignore */ }
}

<YYCOMMENTINIT> 
{
	<<EOF>>				{ yybegin(YYCOMMENTEOF); return symbol("COMMENT", COMMENT, yycommentChar, yychar, null); }
	")"				    { yybegin(YYCOMMENTMULT); return symbol("COMMENT", COMMENT, yycommentChar, yychar, null); }
	.|\n				{ yybegin(YYCOMMENT); return symbol("COMMENT", COMMENT, yycommentChar, yychar, null); }
}

<YYCOMMENT> 
{
	<<EOF>>				{ yybegin(YYCOMMENTEOF); return symbol("COMMENT", COMMENT, yycommentChar, yychar, null); }
	"*)"				{ yybegin(YYINITIAL); return symbol("COMMENT", COMMENT, yycommentChar, yychar + yylength(), null); }
	.|\n				{ /* Ignore */ }
}

<YYCOMMENTMULT>
{
	<<EOF>>			    { throw new LanguageScannerException(yycommentChar, yychar, Messages.getString ( "Scanner.0" ) ); }
	.|\n				{ yybegin(YYCOMMENT); return symbol("COMMENT", COMMENT, yycommentChar, yychar, null); }
}

<YYCOMMENTEOF> 
{
	<<EOF>>				{ throw new LanguageScannerException(yycommentChar, yychar, Messages.getString ( "Parser.7" )); }
}

<YYPROJARITY> 
{
	{Number}			{ 
						  yyprojArity = Integer.valueOf(yytext()); 
						  yyprojArityStartOffset = yychar ;
						  yyprojArityEndOffset = yychar + yylength() ;
						  yybegin(YYPROJUNDERLINE);
						}
	<<EOF>>				{ throw new LanguageScannerException(yyprojChar, yychar, Messages.getString ( "Parser.8" )); }
	\r|\n				{ throw new LanguageScannerException(yyprojChar, yychar, Messages.getString ( "Parser.8" )); }
	.					{ 
						  throw new LanguageScannerException(yyprojChar, yychar + yylength(), 
						    MessageFormat.format ( Messages.getString ( "Parser.11" ), yytext() ));
						}
}

<YYPROJUNDERLINE> 
{
	"_"					{ yybegin(YYPROJINDEX); }
	<<EOF>>				{ throw new LanguageScannerException(yyprojChar, yychar, Messages.getString ( "Parser.9" )); }
	\r|\n				{ throw new LanguageScannerException(yyprojChar, yychar, Messages.getString ( "Parser.9" )); }
	.					{ 
						  throw new LanguageScannerException(yyprojChar, yychar + yylength(), 
						    MessageFormat.format ( Messages.getString ( "Parser.12" ), yytext() ));
						}
}

<YYPROJINDEX> 
{
	{Number}			{ yybegin(YYINITIAL); return symbol("PROJECTION", PROJECTION, yyprojChar, yychar + yylength(), 
							new Integer[] { yyprojArity, Integer.valueOf(yytext()), yyprojArityStartOffset, 
							  yyprojArityEndOffset, yychar , yychar + yylength() }); }
	<<EOF>>				{ throw new LanguageScannerException(yyprojChar, yychar, Messages.getString ( "Parser.10" )); }
	\r|\n				{ throw new LanguageScannerException(yyprojChar, yychar, Messages.getString ( "Parser.10" )); }
	.					{ throw new LanguageScannerException(yyprojChar, yychar + yylength(), 
						    MessageFormat.format ( Messages.getString ( "Parser.13" ), yytext() )); }
}

.|\n					{ throw new LanguageScannerException(yychar, yychar + yylength(), MessageFormat.format ( Messages.getString ( "Parser.1" ), yytext() ) ); }