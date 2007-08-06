package de.unisiegen.tpml.core.languages.l2o;

import java.io.Reader ;
import java.text.MessageFormat ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.languages.AbstractLanguageScanner ;
import de.unisiegen.tpml.core.languages.LanguageScannerException ;
import de.unisiegen.tpml.core.languages.LanguageSymbol ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle ;

/**
 * This is the lexer class for L2O.
 */
%%

%class L2OScanner
%extends AbstractLanguageScanner
%implements L2OTerminals

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
		case OBJECT:
		case END:
		case ATTRIBUTE:
		case METHOD:
		  return PrettyStyle.KEYWORD;
		case BOOL:
		case INT:
		case UNIT:
		case TYPEVARIABLE:
		  return PrettyStyle.TYPE;
		case IDENTIFIER:
		case SELF:
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
	"object"			{ return symbol("OBJECT", OBJECT); }
	"self"				{ return symbol("SELF", SELF, yytext()); }
	"end"				{ return symbol("END", END); }
	"#"					{ return symbol("HASHKEY", HASHKEY); }
	"val"				{ return symbol("ATTRIBUTE", ATTRIBUTE); }
	"method"			{ return symbol("METHOD", METHOD); }
	";"					{ return symbol("SEMI", SEMI); }
	"{<"				{ return symbol("DUPLBEGIN", DUPLBEGIN); }
	">}"				{ return symbol("DUPLEND", DUPLEND); }
	"bool"				{ return symbol("BOOL", BOOL); }
	"int"				{ return symbol("INT", INT); }
	"unit"				{ return symbol("UNIT", UNIT); }
	"mu"|"\u03bc"		{ return symbol("MU", MU); }
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

.|\n					{ throw new LanguageScannerException(yychar, yychar + yylength(), MessageFormat.format ( Messages.getString ( "Parser.1" ), yytext() ) ); }