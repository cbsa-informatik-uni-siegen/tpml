package languages.l0;

import java.io.Reader;

import common.prettyprinter.PrettyStyle;
import languages.AbstractLanguageScanner;
import languages.LanguageScannerException;
import languages.LanguageSymbol;

/**
 * This is the lexer class for L0.
 */
%%

%class L0Scanner
%extends AbstractLanguageScanner
%implements L0Terminals

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
		switch (id) {
		case COMMENT:
			return PrettyStyle.COMMENT;

		case LAMBDA:
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

Identifier		= [:jletter:] [:jletterdigit:]*

%state YYCOMMENT, YYCOMMENTEOF

%%

<YYINITIAL> {
	// syntactic tokens
	"."				{ return symbol("DOT", DOT); }
	"("				{ return symbol("LPAREN", LPAREN); }
	")"				{ return symbol("RPAREN", RPAREN); }
	"lambda"		{ return symbol("LAMBDA", LAMBDA); }
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
