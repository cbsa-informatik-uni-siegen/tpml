package expressions.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is set on classes in the expression hierarchy
 * that represent syntactic sugar, like the {@link expressions.And}
 * and {@link expressions.Or} classes.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SyntacticSugar {
}
