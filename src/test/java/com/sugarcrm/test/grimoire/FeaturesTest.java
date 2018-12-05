package com.sugarcrm.test.grimoire;

import com.sugarcrm.test.Features;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Unit tests for Features Annotation
 * @author Eric Tam <etam@sugarcrm.com>
 */
public class FeaturesTest {
	@Features(revenueLineItem = true)
	class trueRevenueLineItems {}

	@Features(revenueLineItem = false)
	class falseRevenueLineItems {}

	@Test
	public void testEnableFeaturesPositive() {
		Features features_true = trueRevenueLineItems.class.getAnnotation(Features.class);
		Assert.assertTrue(features_true.revenueLineItem());

		Features features_false = falseRevenueLineItems.class.getAnnotation(Features.class);
		Assert.assertFalse(features_false.revenueLineItem());
	}

	class noRevenueLineItems {}

	@Test
	public void testEnableFeaturesNegative() {
		Features features = noRevenueLineItems.class.getAnnotation(Features.class);
		Assert.assertNull(features);
	}
}
