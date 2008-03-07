package de.unisiegen.tpml.core.languages;


import de.unisiegen.tpml.core.typechecker.TypeEquationTypeChecker;


/**
 * Base interface for type scanners for the various languages, which are used in
 * conjunction with the {@link LanguageUnifyParser}s to translate the string
 * representation of monomorphic types to {@link TypeEquationTypeChecker}
 * hierarchies, in a way similar to the {@link LanguageScanner}, which is used
 * to tokenize the string representation of expressions. This interface does not
 * add methods to {@link LanguageScanner}, but is solely used as a <i>marker</i>
 * interface.
 * 
 * @author Christian Fehler
 * @version $Rev: 2760 $
 */
public interface LanguageUnifyScanner extends LanguageScanner
{
  // Nothing to do here
}
