package de.unisiegen.tpml.core;
import de.unisiegen.tpml.core.expressions.Closure;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.util.Environment;

/**
 * TODO
 *
 */
public interface ClosureEnvironment extends Environment<Identifier, Closure>, Cloneable
{
  public void put(Identifier identifier, Closure closure);
  public String toString();
  public Object clone(int newIndex);
  public String getName();
  public boolean isNotPrinted(); 
}
