package de.unisiegen.tpml.core.languages.l2o;

import java.io.Reader;
import java.text.MessageFormat;
import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.languages.AbstractLanguageTypeScanner ;
import de.unisiegen.tpml.core.languages.LanguageScannerException ;
import de.unisiegen.tpml.core.languages.LanguageSymbol ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle ;

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
		case MU:
		  return PrettyStyle.KEYWORD;
		case BOOL:
		case INT:
		case UNIT:
		case TYPEVARIABLE:
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
Identifier		= [a-zA-Z] [a-zA-Z0-9_]* '*
LetterAX		= [a-x]
LetterGreek		= [\u03b1-\u03c1\u03c3-\u03c9]

%state YYCOMMENTINIT, YYCOMMENT, YYCOMMENTMULT, YYCOMMENTEOF

%%

<YYINITIAL>
{
	"("					{ return symbol("LPAREN", LPAREN); }
	")"					{ return symbol("RPAREN", RPAREN); }
	"->"|"\u2192"		{ return symbol("ARROW", ARROW); }
	";"					{ return symbol("SEMI", SEMI); }
	":"					{ return symbol("COLON", COLON); }
	"<"					{ return symbol("LESS", LESS); }
	">"					{ return symbol("GREATER", GREATER); }
	"."					{ return symbol("DOT", DOT); }
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
	{Identifier}		{ return symbol("IDENTIFIER", IDENTIFIER, yytext()); }
	"(*"				{ yycommentChar = yychar; yybegin(YYCOMMENTINIT); }
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