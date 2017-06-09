package io.github.trylovecatch.baselibrary.router.router;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @IntentUri("xxx://com.xxx.xxx.xxxActivity")
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntentUri {

    //完整的Intent URI
    String value();
}
