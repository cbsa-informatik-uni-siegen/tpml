package de.unisiegen.tpml.core ;


import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;


/**
 * Base interface for proof rules in the small and big step interpreters and the
 * type checker.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev$
 * @see de.unisiegen.tpml.core.ProofRuleSet
 */
public interface ProofRule extends Comparable < ProofRule > , PrettyPrintable ,
    LatexPrintable
{
  /**
   * Returns the group to which this proof rule belongs. The group is an
   * identifier used to sort the rules when displaying them to the user. The
   * user interface should simply sort the rules by group and name, adding a
   * separator between the different groups.
   * 
   * @return the group id for this proof rule.
   */
  public int getGroup ( ) ;


  /**
   * Returns the user visible name of the proof rule, without the parenthesis.
   * For example for the big step proof rule <b>(APP)</b> the name is just
   * <tt>"APP"</tt>.
   * 
   * @return the user visible rule name of the proof rule.
   */
  public String getName ( ) ;
}
