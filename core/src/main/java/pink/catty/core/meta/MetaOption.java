package pink.catty.core.meta;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * MetaOption is mainly used to control the serialization of MetaInfo.
 */
@Target({FIELD})
@Retention(RUNTIME)
@Documented
public @interface MetaOption {

  String name();

  boolean serialize() default true;

}
