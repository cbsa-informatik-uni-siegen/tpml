package de.unisiegen.tpml.core.languages.l2;

import java.io.Reader;

import de.unisiegen.tpml.core.prettyprinter.PrettyStyle;
import de.unisiegen.tpml.core.languages.AbstractLanguageScanner;
import de.unisiegen.tpml.core.languages.LanguageScannerException;
import de.unisiegen.tpml.core.languages.LanguageSymbol;

/**
 * This is the lexer class for L2.
 */
%%

%class L2Scanner
%extends AbstractLanguageScanner
%implements L2Terminals

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
	
	private LanguageSymbol symbol(String name, int id) {
		return symbol(name, id, null);
	}
	
	private LanguageSymbol symbol(String name, int id, Object value) {
		return symbol(name, id, yychar, yychar + yylength(), value);
	}

	@Override
	protected PrettyStyle getStyleBySymbolId(int id) {
		switch (id) {
		case COMMENT:
			return PrettyStyle.COMMENT;

		case TRUE: case FALSE: case NUMBER: case PARENPAREN: case MOD:
			return PrettyStyle.CONSTANT;

		case LAMBDA: case LET: case REC: case IN: case IF: case THEN: case ELSE:
			return PrettyStyle.KEYWORD;
			
		default:
			return PrettyStyle.NONE;
		}
	}
	
	public void restart(Reader reader) {
		if (reader == null) {
			throw new NullPointerException("reader is null");
		}
		yyreset(reader);
	}
%}

LineTerminator	= \r|\n|\r\n
WhiteSpace		= {LineTerminator} | [ \t\f]

Number			= [:digit:]+
Identifier		= [:jletter:] [:jletterdigit:]*

%state YYCOMMENT, YYCOMMENTEOF

%%

<YYINITIAL> {
	// arithmetic binary operators
	"+"				{ return symbol("PLUS", PLUS); }
	"-"				{ return symbol("MINUS", MINUS); }
	"*"				{ return symbol("STAR", STAR); }
	"/"				{ return symbol("SLASH", SLASH); }
	"mod"			{ return symbol("MOD", MOD); }
	
	// relational binary operators
	"="				{ return symbol("EQUAL", EQUAL); }
	"<"				{ return symbol("LESS", LESS); }
	">"				{ return symbol("GREATER", GREATER); }
	"<="			{ return symbol("LESSEQUAL", LESSEQUAL); }
	">="			{ return symbol("GREATEREQUAL", GREATEREQUAL); }
	
	// interpunctation
	"."				{ return symbol("DOT", DOT); }
	"("				{ return symbol("LPAREN", LPAREN); }
	")"				{ return symbol("RPAREN", RPAREN); }
	
	// keywords
	"lambda"		{ return symbol("LAMBDA", LAMBDA); }
	"let"			{ return symbol("LET", LET); }
	"rec"			{ return symbol("REC", REC); }
	"in"			{ return symbol("IN", IN); }
	"if"			{ return symbol("IF", IF); }
	"then"			{ return symbol("THEN", THEN); }
	"else"			{ return symbol("ELSE", ELSE); }
	
	// constants
	"()"			{ return symbol("PARENPAREN", PARENPAREN); }
	"true"			{ return symbol("TRUE", TRUE); }
	"false"			{ return symbol("FALSE", FALSE); }
	
	// numbers and identifiers
	{Number}		{
						try {
							return symbol("NUMBER", NUMBER, Integer.valueOf(yytext()));
						}
						catch (NumberFormatException e) {
							throw new LanguageScannerException(yychar, yychar + yylength(), "Integer constant \"" + yytext() + "\" too large", e);
						}
					}
	{Identifier}	{ return symbol("IDENTIFIER", IDENTIFIER, yytext()); }
	
	// comments
	"(*"			{ yycommentChar = yychar; yybegin(YYCOMMENT); }
	
	// whitespace
	{WhiteSpace}	{ /* ignore */ }
}

<YYCOMMENT> {
	<<EOF>>			{ yybegin(YYCOMMENTEOF); return symbol("COMMENT", COMMENT, yycommentChar, yychar, null); }
	"*)"			{ yybegin(YYINITIAL); return symbol("COMMENT", COMMENT, yycommentChar, yychar + yylength(), null); }
	.|\n			{ /* ignore */ }
}

<YYCOMMENTEOF> {
	<<EOF>>			{ throw new LanguageScannerException(yycommentChar, yychar, "Unexpected end of comment"); }
}

.|\n				{ throw new LanguageScannerException(yychar, yychar + yylength(), "Syntax error on token \"" + yytext() + "\""); }
