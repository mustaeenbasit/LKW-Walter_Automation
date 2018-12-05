package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Opportunities_17702 extends SugarTest {
	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * Verify that opportunity amount(likely field) is readonly field in inline editing mode for Ent version and up.
	 *  
	 * @throws Exception
	 */
	@Test
	public void Opportunities_17702_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().opportunities.navToListView();
		sugar().opportunities.listView.editRecord(1);
		
		// Verify likely case is read only
		Assert.assertTrue("Likely case field is enabled", sugar().opportunities.listView.getEditField(1, "likelyCase").isDisabled());
		sugar().opportunities.listView.cancelRecord(1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}