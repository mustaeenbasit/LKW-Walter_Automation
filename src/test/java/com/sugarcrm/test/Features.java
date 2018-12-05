package com.sugarcrm.test;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is a custom annotation for enabling and disabling features in SugarCRM. It is
 * intended to be used for a subclass of SugarTest and PortalTest.
 *
 * @author Eric Tam <etam@sugarcrm.com>
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Features {
	/*
		This is a class level annotation that is used for tests that extend SugarTest.
		An example is shown below -

		@Features(revenueLineItem = true)
		public class someTest extends SugarTest {}
	 */
	boolean revenueLineItem() default true;
}
