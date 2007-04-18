package de.unisiegen.tpml.core.languages.l2o;

import java.io.Reader;

import de.unisiegen.tpml.core.prettyprinter.PrettyStyle;
import de.unisiegen.tpml.core.languages.AbstractLanguageTypeScanner;
import de.unisiegen.tpml.core.languages.LanguageScannerException;
import de.unisiegen.tpml.core.languages.LanguageSymbol;

/**
 * This is the type scanner class for L2O.
 */
%%

%class L2OTypeScanner
%extends AbstractLanguageTypeScanner
%implements L2OTypeTerminals

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
	private LanguageSymbol symbol(String name, int id) {
		return symbol(name, id, yychar, yychar + yylength(), yytext());
	}
	
	private LanguageSymbol symbol(String name, int id, Object value) {
		return symbol(name, id, yychar, yychar + yylength(), value);
	}

	@Override
	public PrettyStyle getStyleBySymbolId(int id) {
		switch (id) {
		case BOOL: case INT: case UNIT: case TYPEVARIABLE:
			return PrettyStyle.TYPE;
	
		case IDENTIFIER:
			return PrettyStyle.IDENTIFIER;
			
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
LetterAX		= [a-x]
LetterGreek		= [\u03b1-\u03c1\u03c3-\u03c9]

%%

<YYINITIAL> {
	// interpunctation
	"("					{ return symbol("LPAREN", LPAREN); }
	")"					{ return symbol("RPAREN", RPAREN); }
	"->"|"\u2192"		{ return symbol("ARROW", ARROW); }
	
	";"					{ return symbol("SEMI", SEMI); }
	":"					{ return symbol("COLON", COLON); }
	"<"					{ return symbol("LESS", LESS); }
	">"					{ return symbol("GREATER", GREATER); }
	
	// types
	"bool"				{ return symbol("BOOL", BOOL); }
	"int"				{ return symbol("INT", INT); }
	"unit"				{ return symbol("UNIT", UNIT); }
	"'"{LetterAX}		{ return symbol("TYPEVARIABLE", TYPEVARIABLE, (int)(yycharat(1) - 'a')); }
	{LetterGreek}		{
							int c = yycharat(0);
							if (c > '\u03c1') {
								/* special case for letters after rho (see Unicode Table) */
								c -= 1;
							}
							return symbol("TYPEVARIABLE", TYPEVARIABLE, (int)(c - '\u03b1'));
						}

	{Identifier}		{ return symbol("IDENTIFIER", IDENTIFIER, yytext()); }
	
	// whitespace
	{WhiteSpace}		{ /* ignore */ }
}

.|\n					{ throw new LanguageScannerException(yychar, yychar + yylength(), "Syntax error on token \"" + yytext() + "\""); }
