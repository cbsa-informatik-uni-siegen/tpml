package de.unisiegen.tpml.core.languages.l0;

import java.io.Reader;

import de.unisiegen.tpml.core.prettyprinter.PrettyStyle;
import de.unisiegen.tpml.core.languages.AbstractLanguageScanner;
import de.unisiegen.tpml.core.languages.LanguageScannerException;
import de.unisiegen.tpml.core.languages.LanguageSymbol;

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
	
	private LanguageSymbol symbol(String name, int id) {
		return symbol(name, id, yychar, yychar + yylength(), yytext());
	}
	
	private LanguageSymbol symbol(String name, int id, Object value) {
		return symbol(name, id, yychar, yychar + yylength(), value);
	}

	@Override
	public PrettyStyle getStyleBySymbolId(int id) {
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

Identifier		= [a-zA-Z] [a-zA-Z0-9_]* '*

%state YYCOMMENT, YYCOMMENTEOF

%%

<YYINITIAL> {
	// syntactic tokens
	"."					{ return symbol("DOT", DOT); }
	"("					{ return symbol("LPAREN", LPAREN); }
	")"					{ return symbol("RPAREN", RPAREN); }
	"lambda"|"\u03bb"	{ return symbol("LAMBDA", LAMBDA); }
	{Identifier}		{ return symbol("IDENTIFIER", IDENTIFIER, yytext()); }
	
	// comments
	"(*"				{ yycommentChar = yychar; yybegin(YYCOMMENT); }
	
	// whitespace
	{WhiteSpace}		{ /* ignore */ }
}

<YYCOMMENT> {
	<<EOF>>				{ yybegin(YYCOMMENTEOF); return symbol("COMMENT", COMMENT, yycommentChar, yychar, null); }
	"*)"				{ yybegin(YYINITIAL); return symbol("COMMENT", COMMENT, yycommentChar, yychar + yylength(), null); }
	.|\n				{ /* ignore */ }
}

<YYCOMMENTEOF> {
	<<EOF>>				{ throw new LanguageScannerException(yycommentChar, yychar, "Unexpected end of comment"); }
}

.|\n					{ throw new LanguageScannerException(yychar, yychar + yylength(), "Syntax error on token \"" + yytext() + "\""); }