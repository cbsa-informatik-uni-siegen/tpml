package de.unisiegen.tpml.core.prettyprinter ;


import de.unisiegen.tpml.core.expressions.Expression ;


/**
 * This interface includes the pretty print commands.
 * 
 * @author Christian Fehler
 * @see Expression
 * @see PrettyStringBuilder
 */
public interface PrettyCommandNames
{
  /**
   * The pretty print command for a line break.
   */
  public static final String PRETTY_LINE_BREAK = System
      .getProperty ( "line.separator" ) ; //$NON-NLS-1$


  /**
   * The pretty print command for a space.
   */
  public static final String PRETTY_SPACE = " " ; //$NON-NLS-1$


  /**
   * The pretty print command for a right triangle.
   */
  public static final String PRETTY_RIGHT_TRIANGLE = "\u22b3" ; //$NON-NLS-1$


  /**
   * The pretty print command for a nail.
   */
  public static final String PRETTY_NAIL = "\u22a2" ; //$NON-NLS-1$


  /**
   * The pretty print command for a slash.
   */
  public static final String PRETTY_SLASH = "/" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>&&</code>.
   */
  public static final String PRETTY_AMPERAMPER = "&&" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>val</code>.
   */
  public static final String PRETTY_VAL = "val" ; //$NON-NLS-1$


  /**
   * The pretty print equal string.
   */
  public static final String PRETTY_EQUAL = "=" ; //$NON-NLS-1$


  /**
   * The pretty print semi string.
   */
  public static final String PRETTY_SEMI = ";" ; //$NON-NLS-1$ 


  /**
   * The pretty print comma string.
   */
  public static final String PRETTY_COMMA = "," ; //$NON-NLS-1$ 


  /**
   * The pretty print dot string.
   */
  public static final String PRETTY_DOT = "." ; //$NON-NLS-1$ 


  /**
   * The pretty print keyword <code>(</code>.
   */
  public static final String PRETTY_LPAREN = "(" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>)</code>.
   */
  public static final String PRETTY_RPAREN = ")" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>class</code>.
   */
  public static final String PRETTY_CLASS = "class" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>end</code>.
   */
  public static final String PRETTY_END = "end" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>:</code>.
   */
  public static final String PRETTY_COLON = ":" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code><:</code>.
   */
  public static final String PRETTY_SUBTYPE = "<:" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>if</code>.
   */
  public static final String PRETTY_IF = "if" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>then</code>.
   */
  public static final String PRETTY_THEN = "then" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>else</code>.
   */
  public static final String PRETTY_ELSE = "else" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>let</code>.
   */
  public static final String PRETTY_LET = "let" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>in</code>.
   */
  public static final String PRETTY_IN = "in" ; //$NON-NLS-1$ 


  /**
   * The pretty print keyword <code>rec</code>.
   */
  public static final String PRETTY_REC = "rec" ; //$NON-NLS-1$  


  /**
   * The pretty print keyword <code>method</code>.
   */
  public static final String PRETTY_METHOD = "method" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>{<</code>.
   */
  public static final String PRETTY_DUPLBEGIN = "{<" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>>}</code>.
   */
  public static final String PRETTY_DUPLEND = ">}" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>{</code>.
   */
  public static final String PRETTY_CLPAREN = "{" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>}</code>.
   */
  public static final String PRETTY_CRPAREN = "}" ; //$NON-NLS-1$


  /**
   * The pretty print string for the exception.
   */
  public static final String PRETTY_EXCEPTION = "\u2191" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>inherit</code>.
   */
  public static final String PRETTY_INHERIT = "inherit" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>from</code>.
   */
  public static final String PRETTY_FROM = "from" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>lambda</code>.
   */
  public static final String PRETTY_LAMBDA = "\u03bb" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>[</code>.
   */
  public static final String PRETTY_LBRACKET = "[" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>]</code>.
   */
  public static final String PRETTY_RBRACKET = "]" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>new</code>.
   */
  public static final String PRETTY_NEW = "new" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>class</code>.
   */
  public static final String PRETTY_OBJECT = "object" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>||</code>.
   */
  public static final String PRETTY_BARBAR = "||" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>#</code>.
   */
  public static final String PRETTY_HASHKEY = "#" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>epsilon</code>.
   */
  public static final String PRETTY_EPSILON = "\u03B5" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>while</code>.
   */
  public static final String PRETTY_WHILE = "while" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>do</code>.
   */
  public static final String PRETTY_DO = "do" ; //$NON-NLS-1$  


  /**
   * The pretty print keyword <code>-></code>.
   */
  public static final String PRETTY_ARROW = "\u2192" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>zeta</code>.
   */
  public static final String PRETTY_ZETA = "\u03B6" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>list</code>.
   */
  public static final String PRETTY_LIST = "list" ; //$NON-NLS-1$


  /**
   * The pretty print greater string.
   */
  public static final String PRETTY_GREATER = ">" ; //$NON-NLS-1$


  /**
   * The pretty print lower string.
   */
  public static final String PRETTY_LOWER = "<" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>for all</code>.
   */
  public static final String PRETTY_FORALL = "\u2200" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>mu</code>.
   */
  public static final String PRETTY_MU = "\u03bc" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>ref</code>.
   */
  public static final String PRETTY_REF = "ref" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>empty set</code>.
   */
  public static final String PRETTY_EMPTY_SET = "\u00D8" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>attr</code>.
   */
  public static final String PRETTY_ATTR = "attr" ; //$NON-NLS-1$


  /**
   * The pretty print string <code>*</code>.
   */
  public static final String PRETTY_MULT = "*" ; //$NON-NLS-1$


  /**
   * The pretty print keyword <code>'</code>.
   */
  public static final String PRETTY_BAR = "'" ; //$NON-NLS-1$


  /**
   * The pretty print empty string.
   */
  public static final String PRETTY_EMPTY_STRING = "" ; //$NON-NLS-1$
}
