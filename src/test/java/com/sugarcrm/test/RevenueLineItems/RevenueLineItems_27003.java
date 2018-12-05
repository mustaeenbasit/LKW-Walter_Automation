package com.sugarcrm.test.RevenueLineItems;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_27003 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * ENT/ULT: Verify that Unit Price is populated from likely in case it is empty on RLI create drawer
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_27003_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to RLI module and click create button
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();

		// In RLI record view type any amount inside likely field
		String likelyAmountData = testName.substring(testName.length()-5);
		String likelyCaseAmount = likelyAmountData.substring(0, 2)+ "," + likelyAmountData.substring(2) + "." + likelyAmountData.substring(2, 4);
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(likelyCaseAmount);

		// Hit Tab or click out to commit
		sugar().revLineItems.createDrawer.getEditField("name").click();

		// Verify that the Unit price field is populated with the value from likely field
		sugar().revLineItems.createDrawer.getEditField("unitPrice").assertEquals(likelyCaseAmount, true);

		// Click Cancel the create drawer of RLI
		sugar().revLineItems.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}