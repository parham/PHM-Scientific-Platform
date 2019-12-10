
package com.phm.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Target(value = {ElementType.METHOD, 
                 ElementType.TYPE, 
                 ElementType.PACKAGE})
public @interface PublicationDetails {

    public String [] author();

    public String title();

    public PublicationType type();

    public String year();

    public String journal() default "";

    public String booktitle() default "";

    public String[] pages() default {};

    public String chapter() default "";

    public String edition() default "";

    public String url() default "";

    public String note() default "";

    public String[] editor() default {};

    public String institution() default "";

    public String month() default "";

    public String number() default "";

    public String organization() default "";

    public String publisher() default "";

    public String school() default "";

    public String series() default "";

    public String volume() default "";

    public String[] customData() default {};
    
    public String paperAbstract () default "";
}
