package pl.edu.pwr.master;

import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(MetricArgumentsProvider.class)
public @interface MetricSource {

    /**
     * The filename to be parsed
     */
    String file() default "";

    /**
     * The comma-separated metric values corresponding to parsed files
     */
    String[] values() default {};
}
