
package com.phm.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface ImplementationDetails {
    String className ();
    String [] developers () default {"Parham Nooralishahi"};
    String [] emails () default {"parham.nooralishahi@gmail.com"};
    String url () default "";
    String date () default "";
    String lastModification () default "";
    String description () default "";
    String version () default "";
}
