package de.unisiegen.tpml.core.types ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;


/**
 * This interface includes the pretty print commands.
 * 
 * @author Christian Fehler
 * @see Type
 * @see PrettyStringBuilder
 */
public interface PrettyCommands
{
  /**
   * The pretty print command for a space.
   */
  public static final String PRETTY_SPACE = " " ; //$NON-NLS-1$


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
   * The pretty print keyword <code>:</code>.
   */
  public static final String PRETTY_COLON = ":" ; //$NON-NLS-1$


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
