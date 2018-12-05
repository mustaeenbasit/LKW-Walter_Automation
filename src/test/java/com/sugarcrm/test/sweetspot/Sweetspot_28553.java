package com.sugarcrm.test.sweetspot;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/** 
 * @author Ruchi Bhatnagar <rbhatnagar@sugarcrm.com>
 */
public class Sweetspot_28553 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify user can use Sweet Spot to go to profile
	 * 
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_28553_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = testData.get(testName).get(0);

		// Call Sweetspot
		sugar().sweetspot.show();

		// Search for profile	
		sugar().sweetspot.search(fs.get("searchText"));

		// Click on search result record
		sugar().sweetspot.clickActionsResult();

		// Verify User is directed to Profile page.
		// TODO: VOOD-1887 
		sugar().users.detailView.assertVisible(true);
		VoodooUtils.focusFrame("bwc-frame");

		// Verifying "fullName" field on user profile page to ensure user is redirected to profile page.
		sugar().users.detailView.getDetailField("fullName").assertEquals(fs.get("fullName"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}