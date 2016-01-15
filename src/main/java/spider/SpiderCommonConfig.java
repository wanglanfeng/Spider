/*
 * Copyright (c) Jipzingking 2015.
 */

package spider;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SpiderCommonConfig {
    String domain();

    int sleepTime() default 500;

    int threadNum() default 5;

    Class<?>[] spiderClasses();
}
