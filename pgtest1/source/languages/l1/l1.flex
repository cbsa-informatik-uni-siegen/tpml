package languages.l1;

import java.io.Reader;

import expressions.PrettyStyle;
import languages.AbstractLanguageScanner;
import languages.LanguageScannerException;
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
	
	/** The starting character position of the projection. */
	private int yyprojChar = 0;
	
	/** The parsed arity of the projection. */
	private Integer yyprojArity;

	private LanguageSymbol symbol(String name, int id) {
		return symbol(name, id, null);
	}
	
	private LanguageSymbol symbol(String name, int id, Object value) {
		return symbol(name, id, yychar, yychar + yylength(), value);
	}

	protected PrettyStyle getStyleBySymbolId(int id) {
		// TODO: Implement this.
		switch (id) {
		case COMMENT:
			return PrettyStyle.COMMENT;

		case TRUE: case FALSE: case NUMBER: case REF:
		case CONS: case IS_EMPTY: case HD: case TL:
		case BRACKETBRACKET: case PARENPAREN:
		case FST: case SND: case MOD:
			return PrettyStyle.CONSTANT;

		case LAMBDA: case LET: case IN: case REC:
		case IF: case THEN: case ELSE:
		case WHILE: case DO:
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
%state YYPROJARITY, YYPROJUNDERLINE, YYPROJINDEX

%%

<YYINITIAL> {
	// arithmetic binary operators
	"+"							{ return symbol("PLUS", PLUS); }
	"-"							{ return symbol("MINUS", MINUS); }
	"*"							{ return symbol("STAR", STAR); }
	"/"							{ return symbol("SLASH", SLASH); }
	"mod"						{ return symbol("MOD", MOD); }
	
	// relational binary operators
	"="							{ return symbol("EQUAL", EQUAL); }
	"<"							{ return symbol("LESS", LESS); }
	">"							{ return symbol("GREATER", GREATER); }
	"<="						{ return symbol("LESSEQUAL", LESSEQUAL); }
	">="						{ return symbol("GREATEREQUAL", GREATEREQUAL); }
	
	// logical operators
	"&&"						{ return symbol("AMPERAMPER", AMPERAMPER); }
	"||"						{ return symbol("BARBAR", BARBAR); }
	
	// unary operators
	"~-"						{ return symbol("TILDEMINUS", TILDEMINUS); }
	"not"						{ return symbol("NOT", NOT); }

	// tuple operators
	"fst"						{ return symbol("FST", FST); }
	"snd"						{ return symbol("SND", SND); }

	// list operators
	"cons"						{ return symbol("CONS", CONS); }
	"is_empty"					{ return symbol("IS_EMPTY", IS_EMPTY); }
	"hd"						{ return symbol("HD", HD); }
	"tl"						{ return symbol("TL", TL); }
	"::"						{ return symbol("COLONCOLON", COLONCOLON); }

	// reference operators
	":="						{ return symbol("COLONEQUAL", COLONEQUAL); }
	"!"							{ return symbol("DEREF", DEREF); }
	"ref"						{ return symbol("REF", REF); }

	// interpunctation
	"."							{ return symbol("DOT", DOT); }
	";"							{ return symbol("SEMI", SEMI); }
	","							{ return symbol("COMMA", COMMA); }
	"("							{ return symbol("LPAREN", LPAREN); }
	")"							{ return symbol("RPAREN", RPAREN); }
	"["							{ return symbol("LBRACKET", LBRACKET); }
	"]"							{ return symbol("RBRACKET", RBRACKET); }

	// keywords
	"lambda"					{ return symbol("LAMBDA", LAMBDA); }
	"let"						{ return symbol("LET", LET); }
	"in"						{ return symbol("IN", IN); }
	"rec"						{ return symbol("REC", REC); }
	"if"						{ return symbol("IF", IF); }
	"then"						{ return symbol("THEN", THEN); }
	"else"						{ return symbol("ELSE", ELSE); }
	"while"						{ return symbol("WHILE", WHILE); }
	"do"						{ return symbol("DO", DO); }

	// constants
	"()"						{ return symbol("BRACKETBRACKET", BRACKETBRACKET); }
	"[]"						{ return symbol("PARENPAREN", PARENPAREN); }
	"true"						{ return symbol("TRUE", TRUE); }
	"false"						{ return symbol("FALSE", FALSE); }

	// numbers and identifiers
	{Number}					{
									try {
										return symbol("NUMBER", NUMBER, Integer.valueOf(yytext()));
									}
									catch (NumberFormatException e) {
										throw new LanguageScannerException(yychar, yychar + yylength(), "Integer constant \"" + yytext() + "\" too large", e);
									}
								}
	{Identifier}				{ return symbol("IDENTIFIER", IDENTIFIER, yytext()); }

	// projections
	"#"							{ yyprojChar = yychar; yybegin(YYPROJARITY); }

	// comments
	"(*"						{ yycommentChar = yychar; yybegin(YYCOMMENT); }

	// whitespace
	{WhiteSpace}				{ /* ignore */ }
}

<YYCOMMENT> {
	<<EOF>>						{ yybegin(YYCOMMENTEOF); return symbol("COMMENT", COMMENT, yycommentChar, yychar, null); }
	"*)"						{ yybegin(YYINITIAL); return symbol("COMMENT", COMMENT, yycommentChar, yychar + yylength(), null); }
	.|\n						{ /* ignore */ }
}

<YYCOMMENTEOF> {
	<<EOF>>						{ throw new LanguageScannerException(yycommentChar, yychar, "Unexpected end of comment"); }
}

<YYPROJARITY> {
	{Number}					{ yyprojArity = Integer.valueOf(yytext()); yybegin(YYPROJUNDERLINE); }
	<<EOF>>						{ throw new LanguageScannerException(yyprojChar, yychar, "Unexpected end of projection"); }
	\r|\n						{ throw new LanguageScannerException(yyprojChar, yychar, "Unexpected end of projection"); }
	.							{ throw new LanguageScannerException(yyprojChar, yychar + yylength(), "Unexpected character \"" + yytext() + "\" in projection"); }
}

<YYPROJUNDERLINE> {
	"_"							{ yybegin(YYPROJINDEX); }
	<<EOF>>						{ throw new LanguageScannerException(yyprojChar, yychar, "Unexpected end of projection"); }
	\r|\n						{ throw new LanguageScannerException(yyprojChar, yychar, "Unexpected end of projection"); }
	.							{ throw new LanguageScannerException(yyprojChar, yychar + yylength(), "Unexpected character \"" + yytext() + "\" in projection"); }
}

<YYPROJINDEX> {
	{Number}					{ yybegin(YYINITIAL); return symbol("PROJECTION", PROJECTION, yyprojChar, yychar + yylength(), new Integer[] { yyprojArity, Integer.valueOf(yytext()) }); }
	<<EOF>>						{ throw new LanguageScannerException(yyprojChar, yychar, "Unexpected end of projection"); }
	\r|\n						{ throw new LanguageScannerException(yyprojChar, yychar, "Unexpected end of projection"); }
	.							{ throw new LanguageScannerException(yyprojChar, yychar + yylength(), "Unexpected character \"" + yytext() + "\" in projection"); }
}

.|\n							{ throw new LanguageScannerException(yychar, yychar + yylength(), "Syntax error on token \"" + yytext() + "\""); }
