package de.unisiegen.tpml.core.languages.l4;

import java.io.Reader;

import de.unisiegen.tpml.core.prettyprinter.PrettyStyle;
import de.unisiegen.tpml.core.languages.AbstractLanguageTypeScanner;
import de.unisiegen.tpml.core.languages.LanguageScannerException;
import de.unisiegen.tpml.core.languages.LanguageSymbol;
import java.text.MessageFormat;
import de.unisiegen.tpml.core.Messages;

/**
 * This is the type scanner class for L4.
 */
%%

%class L4TypeScanner
%extends AbstractLanguageTypeScanner
%implements L4TypeTerminals

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
		case BOOL: case INT: case UNIT: case TYPEVARIABLE: case LIST: case REF:
			return PrettyStyle.TYPE;
			
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

LetterAX		= [a-x]
LetterGreek		= [\u03b1-\u03c1\u03c3-\u03c9]

%%

<YYINITIAL>
{
	// interpunctation
	"("					{ return symbol("LPAREN", LPAREN); }
	")"					{ return symbol("RPAREN", RPAREN); }
	"*"					{ return symbol("STAR", STAR); }
	"->"|"\u2192"		{ return symbol("ARROW", ARROW); }
	
	// types
	"bool"				{ return symbol("BOOL", BOOL); }
	"int"				{ return symbol("INT", INT); }
	"unit"				{ return symbol("UNIT", UNIT); }
	"list"				{ return symbol("LIST", LIST); }
	"ref"				{ return symbol("REF", REF); }
	"'"{LetterAX}		{ return symbol("TYPEVARIABLE", TYPEVARIABLE, (int)(yycharat(1) - 'a')); }
	{LetterGreek}		{
							int c = yycharat(0);
							if (c > '\u03c1')
							{
								/* special case for letters after rho (see Unicode Table) */
								c -= 1;
							}
							return symbol("TYPEVARIABLE", TYPEVARIABLE, (int)(c - '\u03b1'));
						}
	
	// whitespace
	{WhiteSpace}		{ /* ignore */ }
}

.|\n					{ 
						  throw new LanguageScannerException(yychar, yychar + yylength(), MessageFormat.format ( Messages.getString ( "Parser.1" ), yytext() ) );
						}