package de.unisiegen.tpml.core.languages.l0;

import java.io.Reader ;
import java.text.MessageFormat ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.languages.AbstractLanguageScanner ;
import de.unisiegen.tpml.core.languages.LanguageScannerException ;
import de.unisiegen.tpml.core.languages.LanguageSymbol ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle ;

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
		case LAMBDA:
		  return PrettyStyle.KEYWORD;
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

%state YYCOMMENTINIT, YYCOMMENT, YYCOMMENTMULT, YYCOMMENTEOF

%%

<YYINITIAL>
{
	"."					{ return symbol("DOT", DOT); }
	"("					{ return symbol("LPAREN", LPAREN); }
	")"					{ return symbol("RPAREN", RPAREN); }
	"lambda"|"\u03bb"	{ return symbol("LAMBDA", LAMBDA); }
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