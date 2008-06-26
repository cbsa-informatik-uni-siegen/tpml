package de.unisiegen.tpml.core;
import de.unisiegen.tpml.core.util.Environment;
import de.unisiegen.tpml.core.expressions.Closure;
import de.unisiegen.tpml.core.expressions.Identifier;

/**
 * TODO
 *
 */
public interface ClosureEnvironment extends Environment<Identifier, Closure>
{
  public void put(Identifier identifier, Closure closure);
  public String toString();
}
