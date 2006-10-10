package de.unisiegen.tpml.core.languages;

/**
 * Base interface for type scanners for the various languages, which are used in conjunction with the
 * {@link de.unisiegen.tpml.core.languages.LanguageTypeParser}s to translate the string representation
 * of monomorphic types to {@link de.unisiegen.tpml.core.types.MonoType} hierarchies, in a way similar
 * to the {@link de.unisiegen.tpml.core.languages.LanguageScanner}, which is used to tokenize the
 * string representation of expressions.
 *
 * This interface does not add methods to {@link de.unisiegen.tpml.core.languages.LanguageScanner}, but
 * is solely used as a <i>marker</i> interface.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.languages.Language
 * @see de.unisiegen.tpml.core.languages.LanguageTypeParser
 */
public interface LanguageTypeScanner extends LanguageScanner {
  
}
