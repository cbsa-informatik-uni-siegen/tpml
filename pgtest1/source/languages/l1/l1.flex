package languages.l1;

import expressions.PrettyStyle;
import languages.AbstractLanguageScanner;
import languages.LanguageSymbol;

/**
 * This is the lexer class for L1.
 */
%%

%class L1Scanner
%extends AbstractLanguageScanner
%implements L1Terminals

%function nextSymbol
%type LanguageSymbol
%eofclose
%eofval{
	return null;
%eofval}

%unicode
%line
%column
%char

%{
	private LanguageSymbol symbol(String name, int id) {
		return symbol(name, id, null);
	}
	
	private LanguageSymbol symbol(String name, int id, Object value) {
		return symbol(name, id, yychar, yychar + yylength(), value);
	}

	protected PrettyStyle getStyleBySymbolId(int id) {
		// TODO: Implement this.
		return PrettyStyle.NONE;
	}
%}

LineTerminator	= \r|\n|\r\n
WhiteSpace	= {LineTerminator} | [ \t\f]

Number		= [:digit:]+
Identifier	= [:jletter:] [:jletterdigit:]*

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
	
	// logical operators
	"&&"			{ return symbol("AMPERAMPER", AMPERAMPER); }
	"||"			{ return symbol("BARBAR", BARBAR); }
	
	// unary operators
	"~-"			{ return symbol("TILDEMINUS", TILDEMINUS); }
	"not"			{ return symbol("NOT", NOT); }
	
	// tuple operators
	"fst"			{ return symbol("FST", FST); }
	"snd"			{ return symbol("SND", SND); }

	// list operators
	"cons"			{ return symbol("CONS", CONS); }
	"is_empty"		{ return symbol("IS_EMPTY", IS_EMPTY); }
	"hd"			{ return symbol("HD", HD); }
	"tl"			{ return symbol("TL", TL); }
	"::"			{ return symbol("COLONCOLON", COLONCOLON); }

	// reference operators
	":="			{ return symbol("COLONEQUAL", COLONEQUAL); }
	"!"				{ return symbol("DEREF", DEREF); }
	"ref"			{ return symbol("REF", REF); }

	// interpunctation
	"."				{ return symbol("DOT", DOT); }
	";"				{ return symbol("SEMI", SEMI); }
	","				{ return symbol("COMMA", COMMA); }
	"("				{ return symbol("LPAREN", LPAREN); }
	")"				{ return symbol("RPAREN", RPAREN); }
	"["				{ return symbol("LBRACKET", LBRACKET); }
	"]"				{ return symbol("RBRACKET", RBRACKET); }
	
	// keywords
	"lambda"		{ return symbol("LAMBDA", LAMBDA); }
	"let"			{ return symbol("LET", LET); }
	"in"			{ return symbol("IN", IN); }
	"rec"			{ return symbol("REC", REC); }
	"if"			{ return symbol("IF", IF); }
	"then"			{ return symbol("THEN", THEN); }
	"else"			{ return symbol("ELSE", ELSE); }
	"while"			{ return symbol("WHILE", WHILE); }
	"do"			{ return symbol("DO", DO); }

	// constants
	"()"			{ return symbol("BRACKETBRACKET", BRACKETBRACKET); }
	"[]"			{ return symbol("PARENPAREN", PARENPAREN); }
	"true"			{ return symbol("TRUE", TRUE); }
	"false"			{ return symbol("FALSE", FALSE); }
	
	// numbers and identifiers
	{Number}		{ return symbol("NUMBER", NUMBER, Integer.valueOf(yytext())); }
	{Identifier}	{ return symbol("IDENTIFIER", IDENTIFIER, yytext()); }
	
	// whitespace and comments
	{WhiteSpace}	{ /* ignore */ }
}

.|\n				{ throw new Error("Illegal character <"
									  + yytext() + ">"); }
