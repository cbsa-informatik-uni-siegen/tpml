package de.unisiegen.tpml.core.languages.l1;

import java.io.Reader;

import de.unisiegen.tpml.core.prettyprinter.PrettyStyle;
import de.unisiegen.tpml.core.languages.AbstractLanguageScanner;
import de.unisiegen.tpml.core.languages.LanguageScannerException;
import de.unisiegen.tpml.core.languages.LanguageSymbol;
import java.text.MessageFormat;
import de.unisiegen.tpml.core.Messages;

/**
 * This is the lexer class for L1.
 */
%%

%class L1Scanner
%extends AbstractLanguageScanner
%implements L1Terminals

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
	/** The starting character position of the comment. */
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
	public PrettyStyle getStyleBySymbolId(int id)
	{
		switch (id)
		{
		case COMMENT:
			return PrettyStyle.COMMENT;

		case TRUE: case FALSE: case NUMBER: case PARENPAREN: case MOD: case NOT:
			return PrettyStyle.CONSTANT;

		case LAMBDA: case LET: case IN: case IF: case THEN: case ELSE:
		case AMPERAMPER: case BARBAR:
		case MU:
			return PrettyStyle.KEYWORD;
			
		case BOOL: case INT: case UNIT: case TYPEVARIABLE:
			return PrettyStyle.TYPE;
	
		case IDENTIFIER:
			return PrettyStyle.IDENTIFIER;
			
		default:
			return PrettyStyle.NONE;
		}
	}
	
	public void restart(Reader reader)
	{
		if (reader == null)
		{
			throw new NullPointerException("reader is null");
		}
		yyreset(reader);
	}
%}

LineTerminator	= \r|\n|\r\n
WhiteSpace		= {LineTerminator} | [ \t\f]

Number			= [:digit:]+
Identifier		= [a-zA-Z] [a-zA-Z0-9_]* '*
LetterAX		= [a-x]
LetterGreek		= [\u03b1-\u03c1\u03c3-\u03c9]

%state YYCOMMENT, YYCOMMENTEOF, YYCOMMENTINIT, YYCOMMENTMULT

%%

<YYINITIAL>
{
	// arithmetic binary operators
	"+"					{ return symbol("PLUS", PLUS); }
	"-"					{ return symbol("MINUS", MINUS); }
	"*"					{ return symbol("STAR", STAR); }
	"/"					{ return symbol("SLASH", SLASH); }
	"mod"				{ return symbol("MOD", MOD); }
	
	// relational binary operators
	"="					{ return symbol("EQUAL", EQUAL); }
	"<"					{ return symbol("LESS", LESS); }
	">"					{ return symbol("GREATER", GREATER); }
	"<="				{ return symbol("LESSEQUAL", LESSEQUAL); }
	">="				{ return symbol("GREATEREQUAL", GREATEREQUAL); }
	
	// logical operators
	"&&"				{ return symbol("AMPERAMPER", AMPERAMPER); }
	"||"				{ return symbol("BARBAR", BARBAR); }
	"not"				{ return symbol("NOT", NOT); }
	
	// interpunctation
	"."					{ return symbol("DOT", DOT); }
	":"					{ return symbol("COLON", COLON); }
	"("					{ return symbol("LPAREN", LPAREN); }
	")"					{ return symbol("RPAREN", RPAREN); }
	"->"|"\u2192"		{ return symbol("ARROW", ARROW); }
	
	// keywords
	"lambda"|"\u03bb"	{ return symbol("LAMBDA", LAMBDA); }
	"let"				{ return symbol("LET", LET); }
	"in"				{ return symbol("IN", IN); }
	"if"				{ return symbol("IF", IF); }
	"then"				{ return symbol("THEN", THEN); }
	"else"				{ return symbol("ELSE", ELSE); }
	
	// constants
	"()"				{ return symbol("PARENPAREN", PARENPAREN); }
	"true"				{ return symbol("TRUE", TRUE); }
	"false"				{ return symbol("FALSE", FALSE); }
	
	// types
	"bool"				{ return symbol("BOOL", BOOL); }
	"int"				{ return symbol("INT", INT); }
	"unit"				{ return symbol("UNIT", UNIT); }
	"mu"|"\u03bc"		{ return symbol("MU", MU); }
	"'"{LetterAX}		{ return symbol("TYPEVARIABLE", TYPEVARIABLE, (int)(yycharat(1) - 'a')); }
	{LetterGreek}		{
							int c = yycharat(0);
							if (c > '\u03c1') {
								/* special case for letters after rho (see Unicode Table) */
								c -= 1;
							}
							return symbol("TYPEVARIABLE", TYPEVARIABLE, (int)(c - '\u03b1'));
						}
	
	// numbers and identifiers
	{Number}			{
							try
							{
								return symbol("NUMBER", NUMBER, Integer.valueOf(yytext()));
							}
							catch (NumberFormatException e) 
							{
							  throw new LanguageScannerException(yychar, yychar + yylength(), 
								MessageFormat.format ( Messages.getString ( "Parser.6" ) , 
								  yytext() ) , e);
							}
						}
	{Identifier}		{ return symbol("IDENTIFIER", IDENTIFIER, yytext()); }
	
	// comments
	"(*"				{ yycommentChar = yychar; yybegin(YYCOMMENTINIT); }
	
	// whitespace
	{WhiteSpace}		{ /* ignore */ }
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
	.|\n				{ /* ignore */ }
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

.|\n					{ 
						  throw new LanguageScannerException(yychar, yychar + yylength(), MessageFormat.format ( Messages.getString ( "Parser.1" ), yytext() ) );
						}